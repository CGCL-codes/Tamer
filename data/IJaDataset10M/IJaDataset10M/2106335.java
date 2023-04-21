package org.hardtokenmgmt.admin.model;

import org.hardtokenmgmt.admin.common.Constants;
import org.hardtokenmgmt.core.InterfaceFactory;

/**
 * 
 * Base class of all status analyzers containing all the
 * in common methods.
 * 
 * @author Philip Vendil 22 feb 2009
 *
 * @version $Id$
 */
public abstract class BaseStatusAnalyzer implements IStatusAnalyzer {

    protected String hostname = null;

    protected String status = null;

    protected String statusMessage = null;

    protected String threshold = null;

    protected Integer alarmPriority = null;

    protected BaseStatusAnalyzer(String hostname) {
        this.hostname = hostname;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#getHostname()
	 */
    @Override
    public String getHostname() {
        return hostname;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#getStatus()
	 */
    public String getStatus() {
        if (status == null) {
            analyze();
        }
        return status;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#getStatusMessage()
	 */
    public String getStatusMessage() {
        if (statusMessage == null) {
            analyze();
        }
        return statusMessage;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#getThreshold()
	 */
    @Override
    public String getThreshold() {
        if (threshold == null) {
            if (AnalyzeHelper.getThresholdSetting(this, false) != null) {
                threshold = InterfaceFactory.getAdministratorSettings().getProperty(AnalyzeHelper.getThresholdSetting(this, false));
            }
            String key = AnalyzeHelper.getThresholdSetting(this, true);
            if (key != null) {
                if (threshold == null) {
                    threshold = InterfaceFactory.getGlobalSettings().getProperty(key);
                }
                if (threshold == null) {
                    threshold = Constants.DEFAULT_SETTINGS.getProperty(key);
                }
            }
        }
        return threshold;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#setThreshold(java.lang.String)
	 */
    @Override
    public void setThreshold(String threshold) {
        InterfaceFactory.getAdministratorSettings().setProperty(AnalyzeHelper.getThresholdSetting(this, false), threshold);
        this.threshold = threshold;
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#getAlarmPriority()
	 */
    @Override
    public int getAlarmPriority() {
        if (alarmPriority == null) {
            analyze();
            if (alarmPriority == null) {
                alarmPriority = 0;
            }
        }
        return alarmPriority;
    }

    /**
	 * Method that should analyze the objects data
	 * and set the status, statusMessage and alarmPriority variables. 
	 */
    protected abstract void analyze();

    public void resetThreshold() {
        String key = AnalyzeHelper.getThresholdSetting(this, true);
        setThreshold(Constants.DEFAULT_SETTINGS.getProperty(key));
    }

    /**
	 * @see org.hardtokenmgmt.admin.model.IStatusAnalyzer#resetAnalyzis()
	 */
    @Override
    public void resetAnalyzis() {
        this.statusMessage = null;
        this.status = null;
        this.alarmPriority = null;
    }
}
