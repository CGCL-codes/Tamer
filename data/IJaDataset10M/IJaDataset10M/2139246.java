package net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.preview;

import net.sf.mzmine.data.Scan;

/**
 * 
 */
public interface RawDataFilter {

    /**
     * 
     * @return return the modified scan after being process by the filter
     */
    public Scan getNewScan(Scan scan);
}
