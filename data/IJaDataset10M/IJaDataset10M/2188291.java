package ch.oois.infofeeder.fileimporter;

import ch.oois.infofeeder.LifecycleControl;

/**
 * TODO
 *
 *
 * @author Mario D�pp
 */
public interface FileImporterControl extends LifecycleControl {

    /**
   * Set the resultlist directory.
   *
   * @param directory String the resultlist directory.
   */
    public void setResultlistImportDirectory(String directory);

    /**
   * Get the resultlist import directory.
   *
   * @return String resultlist import directory
   */
    public String getResultlistImportDirectory();

    /**
   * Set the scanning interval.
   *
   * @param interval int the interval in milliseconds.
   */
    public void setScanningInterval(int interval);

    /**
   * Get the scanning interval.
   *
   * @return int the scanning interval in milliseconds.
   */
    public int getScanningInterval();
}
