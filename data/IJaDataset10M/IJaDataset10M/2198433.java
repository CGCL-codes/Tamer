package tudresden.ocl20.pivot.metamodels.mof.internal.model;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import tudresden.ocl20.core.jmi.mof14.model.Classifier;
import tudresden.ocl20.pivot.pivotmodel.Operation;
import tudresden.ocl20.pivot.pivotmodel.Parameter;
import tudresden.ocl20.pivot.pivotmodel.Type;
import tudresden.ocl20.pivot.pivotmodel.base.AbstractOperation;

/**
 * An implementation of the Pivot Model {@link Operation} concept for
 * MOF metamodel in MDR.
 * 
 * @author Ronny Brandt
 * @version 1.0 09.05.2007
 */
public class MofOperation extends AbstractOperation implements Operation {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(MofOperation.class);

    private tudresden.ocl20.core.jmi.mof14.model.Operation operation;

    /**
	 * Creates a new <code>MofOperation</code> instance.
	 * 
	 * @param operation the MOF {@link tudresden.ocl20.core.jmi.mof14.model.Operation} adapted
	 * by this class
	 */
    public MofOperation(tudresden.ocl20.core.jmi.mof14.model.Operation operation) {
        if (logger.isDebugEnabled()) {
            logger.debug("MofOperation(tudresden.ocl20.core.jmi.mof14.model.Operation operation=" + operation + ") - enter");
        }
        this.operation = operation;
        if (logger.isDebugEnabled()) {
            logger.debug("MofOperation(tudresden.ocl20.core.jmi.mof14.model.Operation) - exit");
        }
    }

    @Override
    public String getName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - enter");
        }
        String returnString = operation.getName();
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - exit - return value=" + returnString);
        }
        return returnString;
    }

    @Override
    public List<Parameter> getOwnedParameter() {
        if (logger.isDebugEnabled()) {
            logger.debug("getOwnedParameter() - enter");
        }
        List<Parameter> ownedParameter = new ArrayList<Parameter>();
        Iterator<tudresden.ocl20.core.jmi.mof14.model.Parameter> it = operation.getParametersA().iterator();
        while (it.hasNext()) {
            tudresden.ocl20.core.jmi.mof14.model.Parameter parameter = it.next();
            ownedParameter.add(MofAdapterFactory.INSTANCE.createParameter(parameter));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getOwnedParameter() - exit - return value=" + ownedParameter);
        }
        return ownedParameter;
    }

    public List<Parameter> getInputParameter() {
        if (logger.isDebugEnabled()) {
            logger.debug("getInputParameter() - enter");
        }
        List<Parameter> inputParameter = new ArrayList<Parameter>();
        Iterator<tudresden.ocl20.core.jmi.mof14.model.Parameter> it = operation.getInParametersA().iterator();
        while (it.hasNext()) {
            tudresden.ocl20.core.jmi.mof14.model.Parameter parameter = it.next();
            inputParameter.add(MofAdapterFactory.INSTANCE.createParameter(parameter));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getInputParameter() - exit - return value=" + inputParameter);
        }
        return inputParameter;
    }

    public List<Parameter> getOutputParameter() {
        if (logger.isDebugEnabled()) {
            logger.debug("getOutputParameter() - enter");
        }
        List<Parameter> outputParameter = new ArrayList<Parameter>();
        Iterator<tudresden.ocl20.core.jmi.mof14.model.Parameter> it = operation.getOutParametersA().iterator();
        while (it.hasNext()) {
            tudresden.ocl20.core.jmi.mof14.model.Parameter parameter = it.next();
            outputParameter.add(MofAdapterFactory.INSTANCE.createParameter(parameter));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getOutputParameter() - exit - return value=" + outputParameter);
        }
        return outputParameter;
    }

    public Parameter getReturnParameter() {
        if (logger.isDebugEnabled()) {
            logger.debug("getReturnParameter() - enter");
        }
        Parameter returnParameter = operation.getReturnParameterA() == null ? null : MofAdapterFactory.INSTANCE.createParameter((tudresden.ocl20.core.jmi.mof14.model.Parameter) operation.getReturnParameterA());
        if (logger.isDebugEnabled()) {
            logger.debug("getReturnParameter() - exit - return value=" + returnParameter);
        }
        return returnParameter;
    }

    @Override
    public Type getOwningType() {
        if (logger.isDebugEnabled()) {
            logger.debug("getOwningType() - enter");
        }
        Type returnType = MofAdapterFactory.INSTANCE.createType((Classifier) operation.getContainer());
        if (logger.isDebugEnabled()) {
            logger.debug("getOwningType() - exit - return value=" + returnType);
        }
        return returnType;
    }
}
