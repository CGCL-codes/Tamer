package org.quartz.jobs;

/**
 * Interface for objects wishing to receive a 'call-back' from a 
 * <code>FileScanJob</code>.
 * 
 * @author jhouse
 * @see org.quartz.jobs.FileScanJob
 */
public interface FileScanListener {

    void fileUpdated(String fileName);
}
