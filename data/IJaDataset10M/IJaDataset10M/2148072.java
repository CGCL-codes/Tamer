package net.sourceforge.oaw.workflow.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.oaw.workflow.convertor.StringToObjectConvertorFactory;
import net.sourceforge.oaw.workflow.interfaces.IObjectAround;
import net.sourceforge.oaw.workflow.interfaces.IObjectWeaver;
import net.sourceforge.oaw.workflow.interfaces.IStringToObjectConvertor;
import net.sourceforge.oaw.workflow.internal.RegistryReader;
import net.sourceforge.oaw.workflow.internal.WorkflowActivator;
import net.sourceforge.oaw.workflow.util.Messages;
import net.sourceforge.oaw.workflow.weaver.ObjectWeaverFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.osgi.util.NLS;
import org.openarchitectureware.workflow.Workflow;
import org.openarchitectureware.workflow.WorkflowComponent;
import org.openarchitectureware.workflow.WorkflowComponentWithID;
import org.openarchitectureware.workflow.util.StringHelper;

/**
 * The purpose of this class is to read all of the various workflow
 * components, beans, arguments, properties, etc from plugins that
 * contributed through the workFlow extension point and construct a
 * '{@link org.openarchitectureware.workflow.Workflow <em>Workflow</em>}' 
 * objec which can be executed. 
 * <p>
 * This factory has a single public method, <code>createWorkflow</code>, 
 * which is a static routine that will return the Workflow object when
 * finished (or null if it fails). This class cannot be instantiated
 * due to the private constructor.
 * 
 * @author rmemory
 *
 */
public class WorkflowFactory extends RegistryReader {

    class UnresolvedBean {

        public String setterName;

        public Object parentObject;

        UnresolvedBean(String s, Object o) {
            setterName = s;
            parentObject = 0;
        }
    }

    class UnresolvedProperty {

        public Object parentObject;

        public Method parentMethod;

        UnresolvedProperty(Object o, Method m) {
            parentObject = o;
            parentMethod = m;
        }
    }

    static final String EXT_POINT_ID = "workFlow";

    static final String TAG_WORKFLOW = "workflow";

    static final String ATT_WORKFLOW_ID = "workflowId";

    static final String TAG_WORKFLOW_COMPONENT = "workflowComponent";

    static final String ATT_CLASS = "class";

    static final String ATT_WORKFLOW_COMP_ID = "workflowComponentId";

    static final String TAG_PROPERTY = "property";

    static final String ATT_NAME = "name";

    static final String ATT_VALUE = "value";

    static final String ATT_ID = "id";

    static final String ATT_IDREF = "idRef";

    static final String TAG_BEAN = "bean";

    static final String TAG_ARGUMENT = "argument";

    static final String TAG_AROUND = "around";

    static final String ATT_INSERT_CONTEXT = "insertionContext";

    static final String ATT_TGT_WORKFLOW_COMP_ID = "tgtWorkflowComponentId";

    static final String ARG_REGEX_START = "\\Q${\\E";

    static final String ARG_REGEX_END = "\\Q}\\E";

    static final String ARG_REGEX_ANYCHAR = ".*";

    /**
	 * Method to create a '{@link org.openarchitectureware.workflow.Workflow <em>Workflow</em>}' 
	 * object.
	 *
	 * @param workflowId - Must match the workflowId specified by the extenders.
	 * @param arguments - a map of arguments usually specified at the command line.
	 * @return - A workflow object or null on failure.
	 * @throws CoreException
	 */
    public static Workflow createWorkflow(String workflowId, Map<String, String> arguments) throws CoreException {
        WorkflowFactory r = new WorkflowFactory(workflowId);
        r.fObjConvertor = StringToObjectConvertorFactory.createConvertor();
        r.fArguments = arguments;
        r.readRegistry(WorkflowActivator.getDefault().getBundle().getSymbolicName());
        return r.fWorkflow;
    }

    private final String fWorkflowId;

    private Workflow fWorkflow;

    private final HashMap<String, IObjectAround> fUnorderedWorkflowComponentMap;

    private List<Object> fFinalWcList;

    private Map<String, String> fArguments;

    private final Map<String, Object> fPropertyIdMap;

    private final Map<String, List<UnresolvedProperty>> fUnresolvedPropertyIdMap;

    private final Map<String, Object> fBeanIdMap;

    private final Map<String, List<UnresolvedBean>> fUnresolvedBeanIdMap;

    private final Pattern fPattern;

    private IStringToObjectConvertor fObjConvertor;

    /**
	 * Constructor
	 *
	 * (notice that it is private)
	 *
	 * Inform the super class of which extension point we
	 * are interested in. Initialize variables.
	 */
    private WorkflowFactory(String applicationId) {
        super(EXT_POINT_ID);
        fWorkflowId = applicationId;
        fWorkflow = null;
        fUnorderedWorkflowComponentMap = new HashMap<String, IObjectAround>();
        fPropertyIdMap = new HashMap<String, Object>();
        fUnresolvedPropertyIdMap = new HashMap<String, List<UnresolvedProperty>>();
        fBeanIdMap = new HashMap<String, Object>();
        fUnresolvedBeanIdMap = new HashMap<String, List<UnresolvedBean>>();
        fPattern = Pattern.compile(ARG_REGEX_START + ARG_REGEX_ANYCHAR + ARG_REGEX_END);
    }

    /**
	 * Beans in a workflow are allowed to refer to other beans in the workflow
	 * using the idRef/id mechanism. If a bean with an idRef that doesn't exist
	 * yet is encountered, that bean is added to the 
	 * <code>fUnresolvedBeanIdMap</code> which is searched at the end of the
	 * parsing to see if it can be resolved then.
	 * <p>
	 * This allows beans to refer to other id'd beans in the workflow even
	 * if the id'd bean isn't defined until later in the workflow. Given the usage
	 * of eclipse extension points to read WorkflowComponents, there isn't a guarantee
	 * on which order they will be read anyway.
	 *
	 * @param idRef
	 * @param beanElement
	 * @param parentObject
	 * @return
	 * @throws CoreException
	 * @throws InvalidRegistryObjectException
	 */
    private void addUnresolvedBean(String idRef, IConfigurationElement beanElement, Object parentObject) throws InvalidRegistryObjectException, CoreException {
        String setterName = beanElement.getAttribute(ATT_NAME);
        if (setterName == null) {
            logMissingAttribute(beanElement, ATT_NAME);
        }
        List<UnresolvedBean> l = fUnresolvedBeanIdMap.get(idRef);
        if (l == null) {
            l = new ArrayList<UnresolvedBean>();
            fUnresolvedBeanIdMap.put(idRef, l);
        }
        l.add(new UnresolvedBean(setterName, parentObject));
    }

    /**
	 * Propertys in a workflow are allowed to refer to other propertys in the workflow
	 * using the idRef/id mechanism. If a property with an idRef that doesn't exist
	 * yet is encountered, that property is added to the 
	 * <code>fUnresolvedPropertyIdMap</code> which is searched at the end of the
	 * parsing to see if it can be resolved then.
	 * <p>
	 * This allows properties to refer to other id'd properties in the workflow even
	 * if the id'd property isn't defined until later in the workflow. Given the usage
	 * of eclipse extension points to read WorkflowComponents, there isn't a guarantee
	 * on which order they will be read anyway.
	 *
	 * @param idRef
	 * @param parentObject
	 * @param parentMethod
	 * @return
	 */
    private void addUnresolvedProperty(String idRef, Object parentObject, Method parentMethod) {
        List<UnresolvedProperty> l = fUnresolvedPropertyIdMap.get(idRef);
        if (l == null) {
            l = new ArrayList<UnresolvedProperty>();
            fUnresolvedPropertyIdMap.put(idRef, l);
        }
        l.add(new UnresolvedProperty(parentObject, parentMethod));
    }

    /**
	 * Check the incoming string for any variables that need to be resolved. These are
	 * all variables that use the <code>${someVar}</code> syntax. This method searches
	 * through the list of arguments that were passed to the workflow from the command 
	 * line or have been declared inside the workflow itself to replace the <code>${someVar}</code> 
	 * syntax with an actual value.
	 *
     * @param propertyValue
     * @return
	 * @throws CoreException
     */
    private String checkArguments(String propertyValue) throws CoreException {
        Matcher matcher = fPattern.matcher(propertyValue);
        if (matcher.find()) {
            for (Entry<String, String> entry : fArguments.entrySet()) {
                String theValue = entry.getValue();
                Matcher aMatcher = fPattern.matcher(theValue);
                if (aMatcher.find()) {
                    theValue = checkArguments(entry.getValue());
                }
                propertyValue = propertyValue.replaceAll(ARG_REGEX_START + entry.getKey() + ARG_REGEX_END, theValue);
                Matcher m2 = fPattern.matcher(propertyValue);
                if (m2.find() == false) {
                    break;
                }
            }
        }
        matcher = fPattern.matcher(propertyValue);
        boolean unresolvedPatternStillFound = false;
        while (matcher.find()) {
            WorkflowActivator.log().error(NLS.bind(Messages.UNKNOWN_ARGUMENT, propertyValue.substring(matcher.start(), matcher.end())));
            unresolvedPatternStillFound = true;
        }
        if (unresolvedPatternStillFound == true) {
            WorkflowActivator.log().throwError(Messages.UNRESOLVED_ARGUMENTS);
        }
        return propertyValue;
    }

    /**
	 * Given a property name, find the proper adder or setter method in the 
	 * parentClass.
	 *
     * @param propertyName
     * @param parentClass
     * @return
     */
    private Method findAccessorMethod(String propertyName, Class<?> parentClass) {
        String adderName = Messages.ADDER_PREFIX + StringHelper.firstUpper(propertyName);
        String setterName = Messages.SETTER_PREFIX + StringHelper.firstUpper(propertyName);
        Method[] methods = parentClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            int mod = method.getModifiers();
            if (Modifier.isPublic(mod) && !Modifier.isStatic(mod) && method.getParameterTypes().length == 1) {
                String mn = method.getName();
                if (mn.equals(adderName) || mn.equals(setterName)) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
	 * Get the insertion point information about the workflow component (if any).
	 * This information is added to the 
	 * '{@link net.sourceforge.oaw.workflow.interfaces.IObjectAround <em>IObjectAround</em>}' 
	 * wrapper object which is used to order workflow components.
	 *
     * @param element
     * @param srcWcCompId
     * @return
	 * @throws CoreException
	 * @throws InvalidRegistryObjectException
     */
    private void getInsertionPoint(IConfigurationElement element, IObjectAround aroundObject) throws InvalidRegistryObjectException, CoreException {
        IConfigurationElement[] children = element.getChildren();
        for (int i = 0; i < children.length; ++i) {
            IConfigurationElement child = children[i];
            if (child.getName().equals(TAG_AROUND)) {
                String insertContext = child.getAttribute(ATT_INSERT_CONTEXT);
                if (insertContext == null) {
                    logMissingAttribute(child, ATT_INSERT_CONTEXT);
                }
                if (insertContext.equalsIgnoreCase(IObjectAround.VAL_AFTER)) {
                    String targetWcCompId = child.getAttribute(ATT_TGT_WORKFLOW_COMP_ID);
                    if (targetWcCompId == null) {
                        logMissingAttribute(child, ATT_TGT_WORKFLOW_COMP_ID);
                    } else {
                        aroundObject.setAfter(targetWcCompId);
                    }
                } else if (insertContext.equalsIgnoreCase(IObjectAround.VAL_BEFORE)) {
                    String targetWcCompId = child.getAttribute(ATT_TGT_WORKFLOW_COMP_ID);
                    if (targetWcCompId == null) {
                        logMissingAttribute(child, ATT_TGT_WORKFLOW_COMP_ID);
                    } else {
                        aroundObject.setBefore(targetWcCompId);
                    }
                } else if (insertContext.equalsIgnoreCase(IObjectAround.VAL_OVERRIDE)) {
                    String targetWcCompId = child.getAttribute(ATT_TGT_WORKFLOW_COMP_ID);
                    if (targetWcCompId == null) {
                        logMissingAttribute(child, ATT_TGT_WORKFLOW_COMP_ID);
                    } else {
                        aroundObject.setOverride(targetWcCompId);
                    }
                } else {
                    logError(child, NLS.bind(Messages.INCORRECT_INSERTION_CONTEXT_ERROR, insertContext));
                }
            }
        }
    }

    /**
	 * This method is called after both the property value and property method
	 * have both been successfully resolved and the value can be set (or added)
	 * to the object.
	 *
	 * @param parentObject
	 * @param parentMethod
	 * @param valueObject
	 * @return
	 * @throws CoreException
	 */
    private void invokePropertyMethod(Object parentObject, Method parentMethod, Object valueObject) throws CoreException {
        try {
            parentMethod.invoke(parentObject, valueObject);
        } catch (Exception e) {
            WorkflowActivator.log().throwError(Messages.UNEXPECTED_EXCEPTION, e);
        }
    }

    /**
	 * This method uses an eclipse safe mechanism to create and load the workflow 
	 * component object that is defined in the workflow.
	 *
     * @param element
     * @return - true on success
	 * @throws CoreException
	 * @throws InvalidRegistryObjectException
     */
    private WorkflowComponent loadWorkflowComponentClass(IConfigurationElement element) throws InvalidRegistryObjectException, CoreException {
        String className = element.getAttribute(ATT_CLASS);
        if (className == null) {
            logMissingAttribute(element, ATT_CLASS);
        } else {
            try {
                Object workflowComponent = element.createExecutableExtension(ATT_CLASS);
                if (workflowComponent instanceof WorkflowComponent) {
                    return (WorkflowComponent) workflowComponent;
                } else {
                    logError(element, className + Messages.NOT_A_WORKFLOW_COMPONENT_ERROR);
                }
            } catch (Exception e) {
                logAttributeProblem(element, className, e);
            }
        }
        return null;
    }

    /**
	 * This method is used to parse any arguments that are added directly to the workflow,
	 * as opposed to arguments that are passed through the command line.
	 *
	 * @param child
	 * @return - true on success
	 * @throws CoreException
	 * @throws InvalidRegistryObjectException
	 */
    private void parseArgument(IConfigurationElement child) throws InvalidRegistryObjectException, CoreException {
        String argumentName = child.getAttribute(ATT_NAME);
        if (argumentName == null) {
            logMissingAttribute(child, ATT_NAME);
        } else {
            if (fArguments.get(argumentName) != null) return;
            String argumentValue = child.getAttribute(ATT_VALUE);
            if (argumentValue == null) {
                logMissingAttribute(child, ATT_VALUE);
            } else {
                argumentValue = argumentValue.replaceAll("\\\\", "/");
                fArguments.put(argumentName, argumentValue);
            }
        }
    }

    /**
     * Parse any beans that are declared inside of a WorkflowComponent in the workflow.
     *
     * @param parentElement
     * @param parentObject
     * @return
     * @throws CoreException
     */
    private void parseBeans(IConfigurationElement parentElement, Object parentObject) throws CoreException {
        IConfigurationElement[] beanElements = parentElement.getChildren();
        for (int i = 0; i < beanElements.length; ++i) {
            Object beanObject = null;
            IConfigurationElement beanElement = beanElements[i];
            String beanElementName = beanElement.getName();
            if (beanElementName.equals(TAG_BEAN)) {
                String setterName = beanElement.getAttribute(ATT_NAME);
                if (setterName == null) {
                    logMissingAttribute(beanElement, ATT_NAME);
                }
                String idRef = beanElement.getAttribute(ATT_IDREF);
                if (idRef != null) {
                    beanObject = fBeanIdMap.get(idRef);
                    if (beanObject == null) {
                        addUnresolvedBean(idRef, beanElement, parentObject);
                    } else {
                        setPropertyValue(parentObject, setterName, beanObject, null, null, null);
                    }
                } else {
                    setBean(parentObject, beanElement, setterName);
                }
            }
        }
    }

    /**
     * parseProperties
     *
     * @param parentElement
     * @param parentObject
     * @return
     * @throws CoreException
     */
    private void parseProperties(IConfigurationElement parentElement, Object parentObject) throws CoreException {
        IConfigurationElement[] propertyElements = parentElement.getChildren();
        for (int i = 0; i < propertyElements.length; ++i) {
            IConfigurationElement propertyElement = propertyElements[i];
            if (propertyElement.getName().equals(TAG_PROPERTY)) {
                String propertyName = propertyElement.getAttribute(ATT_NAME);
                if (propertyName == null) {
                    logMissingAttribute(propertyElement, ATT_NAME);
                } else {
                    String propertyValue = propertyElement.getAttribute(ATT_VALUE);
                    String id = propertyElement.getAttribute(ATT_ID);
                    String idRef = propertyElement.getAttribute(ATT_IDREF);
                    Object object = null;
                    if (propertyElement.getAttribute(ATT_CLASS) != null) {
                        try {
                            object = propertyElement.createExecutableExtension(ATT_CLASS);
                        } catch (CoreException e) {
                            WorkflowActivator.log().throwError(Messages.UNEXPECTED_EXCEPTION, e);
                        }
                    }
                    setPropertyValue(parentObject, propertyName, propertyValue, id, idRef, object);
                }
            }
        }
    }

    /**
     * Parse any "globally" or top level beans in the workflow, as opposed to beans which
     * are defined inside of a workflow component.
     *
	 * @param child
	 * @return - true on success
     * @throws CoreException
	 */
    private void parseTopLevelBeans(IConfigurationElement child) throws CoreException {
        Object beanObject = null;
        String idRef = child.getAttribute(ATT_IDREF);
        if (idRef != null) {
            if (fBeanIdMap.get(idRef) != null) {
                beanObject = fBeanIdMap.get(idRef);
            } else {
                addUnresolvedBean(idRef, child, null);
                beanObject = null;
            }
        } else {
            String beanClassName = child.getAttribute(ATT_CLASS);
            if (beanClassName != null) {
                try {
                    beanObject = child.createExecutableExtension(ATT_CLASS);
                } catch (CoreException e) {
                    WorkflowActivator.log().throwError(Messages.UNEXPECTED_EXCEPTION, e);
                }
            } else {
                logMissingAttribute(child, ATT_CLASS);
            }
            parseProperties(child, beanObject);
            String id = child.getAttribute(ATT_ID);
            if (id != null) {
                fBeanIdMap.put(id, beanObject);
            }
        }
    }

    /**
     * Parsing of each workflow component begins here.
     *
	 * @param child
	 * @return
	 * @throws CoreException
	 * @throws InvalidRegistryObjectException
	 */
    private void parseWorkflowComponents(IConfigurationElement child) throws InvalidRegistryObjectException, CoreException {
        WorkflowComponent wc = loadWorkflowComponentClass(child);
        if (wc == null) {
            logError(child, NLS.bind(Messages.FAILED_TO_LOAD_WORKFLOW_COMPONENT_ERROR, child.getName()));
        }
        String wcCompId = child.getAttribute(ATT_WORKFLOW_COMP_ID);
        if (wcCompId == null) {
            logMissingAttribute(child, ATT_WORKFLOW_COMP_ID);
        }
        if (wc instanceof WorkflowComponentWithID) {
            ((WorkflowComponentWithID) wc).setId(wcCompId);
        }
        parseProperties(child, wc);
        parseBeans(child, wc);
        IObjectAround aroundObject = ObjectWeaverFactory.createAround(wc);
        getInsertionPoint(child, aroundObject);
        fUnorderedWorkflowComponentMap.put(wcCompId, aroundObject);
        WorkflowActivator.log().info("Adding " + wcCompId + " to the workflowId: " + fWorkflowId);
    }

    @Override
    protected boolean readElements(IConfigurationElement elements[]) throws CoreException {
        if (elements.length == 0) {
            WorkflowActivator.log().throwError(Messages.MISSING_CHILDREN_ERROR);
        }
        for (IConfigurationElement element : elements) {
            if (isPartOfWorkflow(element)) {
                IConfigurationElement[] children = element.getChildren();
                for (int j = 0; j < children.length; ++j) {
                    IConfigurationElement child = children[j];
                    if (child.getName().equals(TAG_ARGUMENT)) {
                        parseArgument(child);
                    } else if (child.getName().equals(TAG_BEAN)) {
                        parseTopLevelBeans(child);
                    }
                }
            }
        }
        for (IConfigurationElement element : elements) {
            if (isPartOfWorkflow(element)) {
                IConfigurationElement[] children = element.getChildren();
                for (int j = 0; j < children.length; ++j) {
                    IConfigurationElement child = children[j];
                    if (child.getName().equals(TAG_WORKFLOW_COMPONENT)) {
                        parseWorkflowComponents(child);
                    }
                }
            }
        }
        resolveUnresolvedProperties();
        resolveUnresolvedBeans();
        IObjectWeaver weaver = ObjectWeaverFactory.createWeaver();
        fFinalWcList = weaver.weaveObjects(fUnorderedWorkflowComponentMap);
        fWorkflow = new Workflow();
        for (Object wc : fFinalWcList) {
            fWorkflow.addComponent((WorkflowComponent) wc);
        }
        return true;
    }

    /**
	 * Determines whether or not a particular extension element is part of the workflow
	 * being processed. This method allows multiple independent workflows to be 
	 * parsed using the same plugin.
	 * 
	 * @param element
	 * @return
	 */
    private boolean isPartOfWorkflow(IConfigurationElement element) {
        return element.getName().equals(TAG_WORKFLOW) && element.getAttribute(ATT_WORKFLOW_ID).equals(fWorkflowId);
    }

    /**
	 * Typically called at the end of the workflow parsing, to determine if there are 
	 * any unresolved beans remaining, and attempts to resolve them. If it is still
	 * unresolved, an error will be thrown.
	 *
	 * @return - true on success
     * @throws CoreException
	 */
    private void resolveUnresolvedBeans() throws CoreException {
        for (Entry<String, List<UnresolvedBean>> entry : fUnresolvedBeanIdMap.entrySet()) {
            String idRef = entry.getKey();
            Object beanObject = fBeanIdMap.get(idRef);
            if (beanObject == null) {
                WorkflowActivator.log().throwError(Messages.UNRESOLVED_BEAN + " " + entry.getKey());
            }
            List<UnresolvedBean> l = entry.getValue();
            for (UnresolvedBean ub : l) {
                if (ub.parentObject != null) {
                    setPropertyValue(ub.parentObject, ub.setterName, beanObject, null, null, null);
                }
            }
        }
        return;
    }

    /**
	 * Typically called at the end of the workflow parsing, to determine if there are 
	 * any unresolved properties remaining, and attempts to resolve them. If it is still
	 * unresolved, an error will be thrown.
     *
	 * @return
     * @throws CoreException
	 */
    private void resolveUnresolvedProperties() throws CoreException {
        for (Entry<String, List<UnresolvedProperty>> entry : fUnresolvedPropertyIdMap.entrySet()) {
            String idRef = entry.getKey();
            Object propertyObject = fPropertyIdMap.get(idRef);
            if (propertyObject == null) {
                WorkflowActivator.log().throwError(Messages.UNRESOLVED_PROPERTY + " " + entry.getKey());
            }
            List<UnresolvedProperty> l = entry.getValue();
            for (UnresolvedProperty up : l) {
                invokePropertyMethod(up.parentObject, up.parentMethod, propertyObject);
            }
        }
    }

    /**
	 * Used by both setProperty and setBean to set the value (or object) into the 
	 * parent object.
	 *
     * @param parentObject
     * @param propertyName
     * @param propertyValue
     * @param id
     * @param idRef
	 * @param value 
     * @return
	 * @throws CoreException
     */
    private void setPropertyValue(Object parentObject, String propertyName, Object propertyValue, String id, String idRef, Object value) throws CoreException {
        Class<?> parentClass = parentObject.getClass();
        Method parentMethod = findAccessorMethod(propertyName, parentClass);
        if (parentMethod == null) {
            WorkflowActivator.log().throwError(NLS.bind(Messages.APPROPRIATE_SETTER_NOT_FOUND_ERROR, parentClass.getName()));
        } else {
            Class<?> paramTypes[] = parentMethod.getParameterTypes();
            Object valueObject;
            if (idRef != null) {
                valueObject = fPropertyIdMap.get(idRef);
                if (valueObject == null) {
                    addUnresolvedProperty(idRef, parentObject, parentMethod);
                    return;
                }
            } else {
                if (value != null) {
                    valueObject = value;
                } else if (propertyValue instanceof String) {
                    valueObject = fObjConvertor.convert(paramTypes[0], checkArguments((String) propertyValue));
                } else {
                    valueObject = propertyValue;
                }
                if (id != null) {
                    fPropertyIdMap.put(id, valueObject);
                }
            }
            invokePropertyMethod(parentObject, parentMethod, valueObject);
        }
    }

    /**
     * Instantiate the bean and set any of its properties. 
     *
	 * @param parentObject
	 * @param beanElement
	 * @param setterName
	 * @return
     * @throws CoreException
	 */
    private void setBean(Object parentObject, IConfigurationElement beanElement, String setterName) throws CoreException {
        String beanClassName = beanElement.getAttribute(ATT_CLASS);
        Object beanObject = null;
        if (beanClassName == null) {
            logMissingAttribute(beanElement, ATT_CLASS);
        } else {
            try {
                beanObject = beanElement.createExecutableExtension(ATT_CLASS);
                if (beanObject == null) {
                    WorkflowActivator.log().throwError(Messages.BAD_BEAN_OBJECT + " " + beanClassName);
                }
            } catch (CoreException e) {
                logAttributeProblem(beanElement, ATT_CLASS, e);
            }
            parseProperties(beanElement, beanObject);
        }
        setPropertyValue(parentObject, setterName, beanObject, null, null, null);
        String id = beanElement.getAttribute(ATT_ID);
        if (id != null) {
            fBeanIdMap.put(id, beanObject);
        }
    }
}
