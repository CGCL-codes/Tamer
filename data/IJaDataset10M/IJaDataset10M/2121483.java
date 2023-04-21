package com.ixora.rms.dataengine.functions;

import java.util.LinkedList;
import java.util.List;
import com.ixora.rms.ResourceId;
import com.ixora.common.utils.Utils;
import com.ixora.rms.CounterType;
import com.ixora.rms.EntityId;
import com.ixora.rms.client.locator.SessionArtefactInfoLocator;
import com.ixora.rms.client.locator.SessionResourceInfo;
import com.ixora.rms.data.CounterValue;
import com.ixora.rms.dataengine.HintManager;
import com.ixora.rms.dataengine.QueryResult;
import com.ixora.rms.dataengine.Resource;
import com.ixora.rms.dataengine.Style;
import com.ixora.rms.dataengine.definitions.FunctionDef;
import com.ixora.rms.dataengine.definitions.ParamDef;
import com.ixora.rms.dataengine.definitions.ScriptFunctionDef;
import com.ixora.rms.dataengine.definitions.ValueFilterDef;
import com.ixora.rms.exception.RMSException;

/**
 * Function.
 * @author Cristian Costache
 * @author Daniel Moraru
 */
public abstract class Function extends QueryResult {

    private static final long serialVersionUID = 5100714696272652271L;

    /** A function can operate on one or more counters */
    private List<ResourceId> fParams = new LinkedList<ResourceId>();

    /** Whether this function maintains state, which means cloning is required */
    private boolean fKeepsState;

    /**
     * @param fd
     * @param listQueryResources
     * @param ridContext
     * @param keepsState
     */
    public Function(FunctionDef fd, List<Resource> listQueryResources, ResourceId ridContext, boolean keepsState) {
        this(fd, listQueryResources, ridContext, keepsState, null);
    }

    /**
     * @param fd
     * @param listQueryResources
     * @param ridContext
     * @param keepsState
     * @param inheritFrom the style to inherit; used when a function is defined from
     * a resource with a code tag attached in its style
     */
    public Function(FunctionDef fd, List<Resource> listQueryResources, ResourceId ridContext, boolean keepsState, Style inheritFrom) {
        super(fd);
        this.fKeepsState = keepsState;
        if (inheritFrom != null) {
            fStyle.merge(inheritFrom);
        }
        if (fStyle.getIName() == null) {
            fStyle.setIName("$host" + EntityId.DELIMITER + "$agent" + EntityId.DELIMITER + "$entity" + EntityId.DELIMITER + fStyle.getName());
        }
        if (fStyle.getType() == null) {
            fStyle.setType(Style.TYPE_NUMBER);
        }
        for (ParamDef pd : fd.getParameters()) {
            if (pd.getID() != null) {
                for (Resource resource : listQueryResources) {
                    if (pd.getID().equals(resource.getStyle().getID())) {
                        ResourceId resourceID = resource.getResourceID().complete(ridContext);
                        fParams.add(resourceID);
                        break;
                    }
                }
            }
        }
        if (Utils.isEmptyCollection(fParams)) {
            throw new IllegalArgumentException("No resources matching parameters ids were found in the list provided");
        }
        HintManager hm = HintManager.instance();
        hm.resolveStyle(fStyle);
    }

    /**
	 * Create a function from a given Resource
	 * @param r
	 */
    public Function(Resource r) {
        super();
        fParams.add(r.getResourceID());
        fStyle.merge(r.getStyle());
        HintManager hm = HintManager.instance();
        hm.resolveStyle(getStyle());
    }

    /**
	 * @return the list of ResourceIds on which this function is defined
	 */
    public List<ResourceId> getParameters() {
        return fParams;
    }

    /**
	 * Applies the function on the arguments and returns the result
	 * @param vals
	 * @param types
	 */
    public abstract CounterValue execute(CounterValue[] args, CounterType[] types) throws RMSException;

    /**
	 * @return the counter type matching the returned type
	 */
    public abstract CounterType getReturnedType();

    /**
	 * Static function which acts as a factory: it reads the node,
	 * instantiates the right Function class. Since a function's
	 * parameters will refer to resources of a Query, it needs a
	 * list with these Resources.
	 * @param fd a FunctionDef to create a Function from
	 * @param listQueryResources list of all Resources defined by
	 * the Query which holds this function.
	 */
    public static Function createFunction(FunctionDef fd, List<Resource> listQueryResources, ResourceId ridContext) {
        Function f = null;
        String name = fd.getOp();
        if (name.equalsIgnoreCase(Sum.getOp())) f = new Sum(fd, listQueryResources, ridContext); else if (name.equalsIgnoreCase(Average.getOp())) f = new Average(fd, listQueryResources, ridContext); else if (name.equalsIgnoreCase(Differential.getOp())) f = new Differential(fd, listQueryResources, ridContext); else if (name.equalsIgnoreCase(Identity.getOp())) f = new Identity(fd, listQueryResources, ridContext); else if (name.equalsIgnoreCase(TimeDifferential.getOp())) f = new TimeDifferential(fd, listQueryResources, ridContext); else if (name.equalsIgnoreCase(Janino.getOp())) {
            ScriptFunctionDef sfd = (ScriptFunctionDef) fd;
            f = new Janino(sfd, listQueryResources, ridContext);
        } else if (name.equalsIgnoreCase(Filter.getOp())) {
            ValueFilterDef vfd = (ValueFilterDef) fd;
            f = new Filter(vfd, listQueryResources, ridContext);
        }
        return f;
    }

    /**
	 * @return the defintion for this function
	 */
    public FunctionDef getFunctionDef() {
        return new FunctionDef();
    }

    /**
     * @return Returns the fKeepsState.
     */
    public boolean getKeepsState() {
        return fKeepsState;
    }

    /**
     * @param locator
     */
    public void localize(SessionArtefactInfoLocator locator) {
        localize(locator, this.fParams.get(0), null, true);
    }

    /**
	 * @see com.ixora.rms.dataengine.QueryResult#localize(com.ixora.rms.client.locator.SessionResourceInfo, com.ixora.rms.ResourceId, java.lang.String[], boolean)
	 */
    public void localize(SessionResourceInfo rInfo, ResourceId ridMatched, String[] captures, boolean bLocalizeStaticOnly) {
        if (fWasLocalized) {
            return;
        }
        if (rInfo != null) {
            fStyle.setName(replaceTokens(rInfo, fStyle.getName(), ridMatched, captures));
            if (!bLocalizeStaticOnly) {
                fStyle.setIName(replaceTokens(rInfo, fStyle.getIName(), ridMatched, captures));
            }
        }
    }
}
