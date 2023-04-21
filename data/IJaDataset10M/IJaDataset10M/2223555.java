package org.starobjects.wicket.viewer.components.actions;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.wicket.PageParameters;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.oid.stringable.OidStringifier;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.starobjects.wicket.viewer.base.ModelAbstract;
import org.starobjects.wicket.viewer.bootstrap.PageParameterNames;
import org.starobjects.wicket.viewer.common.SelectionHandler;
import org.starobjects.wicket.viewer.common.mementos.ActionMemento;
import org.starobjects.wicket.viewer.common.mementos.ActionParameterMemento;
import org.starobjects.wicket.viewer.common.mementos.NakedObjectMemento;
import org.starobjects.wicket.viewer.common.mementos.SpecMemento;
import org.starobjects.wicket.viewer.common.scalars.ScalarModel;
import org.starobjects.wicket.viewer.components.collection.EntityCollectionModel;
import org.starobjects.wicket.viewer.components.entity.EntityModel;
import com.google.common.collect.Maps;

/**
 * Returns the results of the action invocation.
 */
public class ActionModel extends ModelAbstract<NakedObject> {

    private static final long serialVersionUID = 1L;

    /**
	 * Whether we are obtaining arguments (eg in a dialog), or displaying the
	 * results
	 */
    enum Mode {

        PARAMETERS, RESULTS
    }

    /**
	 * Factory method for creating {@link PageParameters}.
	 * 
	 * see {@link #ActionModel(PageParameters)}
	 */
    public static PageParameters createPageParameters(final NakedObject adapter, final NakedObjectAction noAction, OidStringifier oidStringifier, NakedObject contextAdapter) {
        PageParameters pageParameters = EntityModel.createPageParameters(adapter, oidStringifier);
        String actionType = noAction.getType().name();
        String actionNameParmsId = determineActionId(noAction);
        String actionMode = noAction.getParameterCount() > 0 ? Mode.PARAMETERS.name() : Mode.RESULTS.name();
        PageParameterNames.ACTION_TYPE.addTo(pageParameters, actionType);
        NakedObjectSpecification actionOnTypeSpec = noAction.getOnType();
        if (actionOnTypeSpec != null) {
            PageParameterNames.ACTION_OWNING_SPEC.addTo(pageParameters, actionOnTypeSpec.getFullName());
        }
        PageParameterNames.ACTION_NAME_PARMS.addTo(pageParameters, actionNameParmsId);
        PageParameterNames.ACTION_MODE.addTo(pageParameters, actionMode);
        addActionParamContextIfPossible(noAction, oidStringifier, contextAdapter, pageParameters);
        return pageParameters;
    }

    private static void addActionParamContextIfPossible(final NakedObjectAction noAction, OidStringifier oidStringifier, NakedObject contextAdapter, PageParameters pageParameters) {
        if (contextAdapter != null) {
            int i = 0;
            for (NakedObjectActionParameter actionParam : noAction.getParameters()) {
                if (contextAdapter.getSpecification().isOfType(actionParam.getSpecification())) {
                    PageParameterNames.ACTION_PARAM_CONTEXT.addTo(pageParameters, "" + i + "=" + oidStringifier.enString(contextAdapter.getOid()));
                    break;
                }
                i++;
            }
        }
    }

    private static String determineActionId(final NakedObjectAction noAction) {
        Identifier identifier = noAction.getIdentifier();
        if (identifier != null) {
            return identifier.toNameParmsIdentityString();
        }
        return noAction.getId();
    }

    private static Mode determineMode(final NakedObjectAction action) {
        return action.getParameterCount() > 0 ? Mode.PARAMETERS : Mode.RESULTS;
    }

    private NakedObjectMemento targetAdapterMemento;

    private ActionMemento actionMemento;

    private Mode actionMode;

    private transient NakedObject targetAdapter;

    private SelectionHandler selectionHandler;

    /**
	 * Lazily populated in {@link #getArgumentModel(ActionParameterMemento)}
	 */
    private Map<Integer, ScalarModel> arguments = Maps.newHashMap();

    private ActionExecutor executor;

    public ActionModel(final NakedObject targetAdapter, final NakedObjectAction action) {
        this(new NakedObjectMemento(targetAdapter), new ActionMemento(action), determineMode(action));
    }

    public ActionModel(PageParameters pageParameters, OidStringifier oidStringifier) {
        this(new NakedObjectMemento(oidStringifier.deString(PageParameterNames.OID.getFrom(pageParameters)), SpecMemento.representing(PageParameterNames.SPEC.getFrom(pageParameters))), new ActionMemento(SpecMemento.representing(PageParameterNames.ACTION_OWNING_SPEC.getFrom(pageParameters)), NakedObjectActionType.valueOf(PageParameterNames.ACTION_TYPE.getFrom(pageParameters)), PageParameterNames.ACTION_NAME_PARMS.getFrom(pageParameters)), Mode.valueOf(PageParameterNames.ACTION_MODE.getFrom(pageParameters)));
        setContextArgumentIfPossible(pageParameters, oidStringifier);
    }

    private ActionModel(final NakedObjectMemento nakedObjectMemento, final ActionMemento actionMemento, final Mode actionMode) {
        this.targetAdapterMemento = nakedObjectMemento;
        this.actionMemento = actionMemento;
        this.actionMode = actionMode;
    }

    private void setContextArgumentIfPossible(PageParameters pageParameters, OidStringifier oidStringifier) {
        String paramContext = PageParameterNames.ACTION_PARAM_CONTEXT.getFrom(pageParameters);
        if (paramContext == null) {
            return;
        }
        NakedObjectAction action = actionMemento.getAction();
        int parameterCount = action.getParameterCount();
        Map.Entry<Integer, String> mapEntry = parse(paramContext);
        Oid oid;
        int paramNum = mapEntry.getKey();
        if (paramNum >= parameterCount) {
            return;
        }
        try {
            oid = oidStringifier.deString(mapEntry.getValue());
        } catch (Exception e) {
            return;
        }
        NakedObject argumentAdapter = getAdapterManager().getAdapterFor(oid);
        if (argumentAdapter == null) {
            return;
        }
        NakedObjectActionParameter actionParam = action.getParameters()[paramNum];
        ActionParameterMemento apm = new ActionParameterMemento(actionParam);
        ScalarModel argumentModel = getArgumentModel(apm);
        argumentModel.setObject(argumentAdapter);
    }

    static Entry<Integer, String> parse(String paramContext) {
        Pattern compile = Pattern.compile("([^=]+)=(.+)");
        Matcher matcher = compile.matcher(paramContext);
        if (!matcher.matches()) {
            return null;
        }
        final int paramNum;
        try {
            paramNum = Integer.parseInt(matcher.group(1));
        } catch (Exception e) {
            return null;
        }
        final String oidStr;
        try {
            oidStr = matcher.group(2);
        } catch (Exception e) {
            return null;
        }
        return new Map.Entry<Integer, String>() {

            @Override
            public Integer getKey() {
                return paramNum;
            }

            @Override
            public String getValue() {
                return oidStr;
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        };
    }

    public ScalarModel getArgumentModel(ActionParameterMemento apm) {
        ScalarModel scalarModel = arguments.get(apm.getNumber());
        if (scalarModel == null) {
            scalarModel = new ScalarModel(apm);
            int number = scalarModel.getActionParameterMemento().getNumber();
            arguments.put(number, scalarModel);
        }
        return scalarModel;
    }

    public NakedObject getTargetAdapter() {
        if (targetAdapter == null) {
            targetAdapter = getAdapter();
        }
        return targetAdapter;
    }

    private NakedObject getAdapter() {
        return targetAdapterMemento.getNakedObject();
    }

    public Mode getActionMode() {
        return actionMode;
    }

    public ActionMemento getActionMemento() {
        return actionMemento;
    }

    @Override
    public NakedObject getObject() {
        detach();
        NakedObject result = super.getObject();
        arguments.clear();
        return result;
    }

    @Override
    protected NakedObject load() {
        NakedObject adapter = getAdapter();
        if (adapter == null) {
            return null;
        }
        NakedObject results = executeAction(adapter);
        this.actionMode = Mode.RESULTS;
        return results;
    }

    private NakedObject executeAction(NakedObject adapter) {
        NakedObjectAction objectAction = getActionMemento().getAction();
        NakedObject[] arguments = getArgumentsAsArray();
        NakedObject results = objectAction.execute(adapter, arguments);
        return results;
    }

    @Override
    public void setObject(NakedObject object) {
        throw new UnsupportedOperationException("target adapter for ActionModel cannot be changed");
    }

    public NakedObject[] getArgumentsAsArray() {
        NakedObjectAction objectAction = getActionMemento().getAction();
        NakedObject[] arguments = new NakedObject[objectAction.getParameterCount()];
        for (int i = 0; i < arguments.length; i++) {
            ScalarModel scalarModel = this.arguments.get(i);
            arguments[i] = scalarModel.getObject();
        }
        return arguments;
    }

    /**
	 * The {@link SelectionHandler}, if any.
	 * 
	 * <p>
	 * If specified, then
	 * {@link EntityCollectionModel#setSelectionHandler(SelectionHandler)
	 * propogated} if the results is a {@link EntityCollectionModel collection},
	 * or used directly if the results is an {@link EntityModel}.
	 */
    public SelectionHandler getSelectionHandler() {
        return selectionHandler;
    }

    public void setSelectionHandler(SelectionHandler selectionHandler) {
        this.selectionHandler = selectionHandler;
    }

    public ActionExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ActionExecutor executor) {
        this.executor = executor;
    }
}
