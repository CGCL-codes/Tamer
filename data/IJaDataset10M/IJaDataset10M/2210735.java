package de.uniAugsburg.MAF.core.util.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.uniAugsburg.MAF.core.MAFConstants;
import de.uniAugsburg.MAF.core.MAFCore;
import de.uniAugsburg.MAF.core.adapter.AMetaModelAdapter;
import de.uniAugsburg.MAF.core.exceptions.EvaluatorException;
import de.uniAugsburg.MAF.core.exceptions.InstantiatorException;
import de.uniAugsburg.MAF.core.exceptions.InvokerException;
import de.uniAugsburg.MAF.core.exceptions.RepositoryException;
import de.uniAugsburg.MAF.core.exceptions.VisualizerException;
import de.uniAugsburg.MAF.core.visualizer.MAFLevel;

/**
 * Parameters for configuring the framework.
 */
public class CoreParameters implements IParameters {

    /**
	 * The framework.
	 */
    private MAFCore core;

    private int log_level;

    private Boolean synchronize_resources;

    private String autodispose_policy;

    private Boolean parameters_autoreset;

    /**
	 * The constructor.
	 * 
	 * @param logLevel
	 * @param logFilePath
	 * @param appendToLog
	 * @param intendLog
	 * @param synchronizeResources
	 * @param instantiator
	 * @param evaluator_type
	 */
    public CoreParameters(int logLevel, Boolean synchronizeResources, String autodisposePolicy, Boolean parametersAutoreset) {
        this.log_level = logLevel;
        this.synchronize_resources = synchronizeResources;
        this.autodispose_policy = autodisposePolicy;
        this.parameters_autoreset = parametersAutoreset;
    }

    public void setCore(MAFCore core) {
        this.core = core;
    }

    /**
	 * Create a default parameter set.
	 * 
	 * @return
	 */
    public static CoreParameters parametersDefault() {
        return new CoreParameters(MAFLevel.WARNING, true, MAFConstants.PARAM_CORE_AUTODISPOSE_POLICY_BOTH, true);
    }

    /**
	 * Create a debug parameter set.
	 * 
	 * @return
	 */
    public static CoreParameters parametersDebug() {
        return new CoreParameters(MAFLevel.ALL, true, MAFConstants.PARAM_CORE_AUTODISPOSE_POLICY_BOTH, true);
    }

    /**
	 * The log level.
	 * 
	 * @return
	 */
    public int getLogLevel() {
        return log_level;
    }

    /**
	 * Sets the log level.
	 * 
	 * NOTE: Takes effect immediately for future actions.
	 * 
	 * @param level
	 * @throws VisualizerException
	 */
    public void setLogLevel(int level) throws VisualizerException {
        this.log_level = level;
        if (core != null) {
            core.getVisualizerFacade().parametersChanged(this, MAFConstants.PARAM_CORE_LOG_LEVEL, level);
        }
    }

    /**
	 * Determines whether resources should be synchronized automatically (e.g.
	 * if a metamodel changes, all depending models, attributions and
	 * instantiations are reloaded as well).
	 * 
	 * @return
	 */
    public Boolean getSynchronizeResources() {
        return synchronize_resources;
    }

    /**
	 * Determines whether resources should be synchronized automatically (e.g.
	 * if a metamodel changes, all depending models, attributions and
	 * instantiations are reloaded as well).
	 * 
	 * NOTE: Setting without "parameters_autoreset" will affect future actions
	 * NOTE: Setting with "parameters_autoreset" will trigger reload of the
	 * repository
	 * 
	 * @param synchronizeResources
	 * @throws VisualizerException
	 * @throws RepositoryException
	 * @throws InvokerException
	 * @throws InstantiatorException
	 * @throws EvaluatorException
	 */
    public void setSynchronizeResources(Boolean synchronizeResources) throws VisualizerException, RepositoryException, InvokerException, InstantiatorException, EvaluatorException {
        this.synchronize_resources = synchronizeResources;
        if (parameters_autoreset && synchronizeResources) {
            for (Iterator<AMetaModelAdapter> iter = core.getRepositoryFacade().iteratorMetaModel(); iter.hasNext(); ) {
                AMetaModelAdapter mmadapter = iter.next();
                core.getRepositoryFacade().reloadMetaModel(mmadapter.getMetaModelID());
            }
        }
        if (core != null) {
            core.getVisualizerFacade().parametersChanged(this, MAFConstants.PARAM_CORE_SYNCHRONIZE_RESOURCES, synchronizeResources);
        }
    }

    /**
	 * Determines whether adapters/visualizers are automatically disposed on
	 * removal from repositories / framework disposal.
	 * 
	 * @return
	 */
    public String getAutodisposePolicy() {
        return autodispose_policy;
    }

    /**
	 * Determines whether adapters/visualizers are automatically disposed on
	 * removal from repositories / framework disposal.
	 * 
	 * NOTE: Takes effect immediately for future actions.
	 * 
	 * @param autodisposePolicy
	 * @throws VisualizerException
	 */
    public void setAutodisposePolicy(String autodisposePolicy) throws VisualizerException {
        this.autodispose_policy = autodisposePolicy;
        if (core != null) {
            core.getVisualizerFacade().parametersChanged(this, MAFConstants.PARAM_CORE_AUTODISPOSE_POLICY, autodisposePolicy);
        }
    }

    /**
	 * Determines whether changing parameters which take effect only after
	 * refreshing the affected object automatically triggers this refresh.
	 * 
	 * @return
	 */
    public Boolean getParametersAutoreset() {
        return parameters_autoreset;
    }

    /**
	 * Determines whether changing parameters which take effect only after
	 * refreshing the affected object automatically triggers this refresh.
	 * 
	 * NOTE: Takes effect immediately for future actions.
	 * 
	 * NOTE: It is not guaranteed that the previous internal state is fully
	 * restored after refreshing the affected objects
	 * 
	 * @param parametersAutoreset
	 * @throws VisualizerException
	 */
    public void setParametersAutoreset(Boolean parametersAutoreset) throws VisualizerException {
        this.parameters_autoreset = parametersAutoreset;
        if (core != null) {
            core.getVisualizerFacade().parametersChanged(this, MAFConstants.PARAM_CORE_PARAMETERS_AUTORESET, parametersAutoreset);
        }
    }

    public List<String[]> getParameterInfo() {
        List<String[]> returnList = new ArrayList<String[]>();
        String logl = "";
        switch(log_level) {
            case MAFLevel.ALL:
                logl = "ALL";
                break;
            case MAFLevel.DEBUG:
                logl = "DEBUG";
                break;
            case MAFLevel.ERROR:
                logl = "ERROR";
                break;
            case MAFLevel.INFO:
                logl = "INFO";
                break;
            case MAFLevel.OFF:
                logl = "OFF";
                break;
            case MAFLevel.WARNING:
                logl = "WARNING";
                break;
            default:
                logl = "UNKNOWN";
                break;
        }
        returnList.add(new String[] { MAFConstants.PARAM_CORE_LOG_LEVEL, logl, "The log level" });
        returnList.add(new String[] { MAFConstants.PARAM_CORE_SYNCHRONIZE_RESOURCES, synchronize_resources.toString(), "Automatically reload/remove dependent resources if a resource changes" });
        returnList.add(new String[] { MAFConstants.PARAM_CORE_AUTODISPOSE_POLICY, autodispose_policy, "Autodispose adapters/visualizers on removal or framework dispose" });
        returnList.add(new String[] { MAFConstants.PARAM_CORE_PARAMETERS_AUTORESET, parameters_autoreset.toString(), "Automatically reset affected elements if their parameters have changed" });
        return returnList;
    }
}
