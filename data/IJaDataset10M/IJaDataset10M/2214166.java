package gov.lansce.apps.rasterapp;

import gov.sns.xal.smf.impl.BPM;

/**
 * Notification called whenever the application data has changed.
 * Each view should implement this interface to receive such notifications. 
 * 
 * @author Rod McCrady
 * @author Christopher K. Allen
 *
 */
public interface IDataView {

    /**
     * The application data had changed.
     * 
     * @param data      updated application data
     */
    public void updateData(RasterDocument data);
}
