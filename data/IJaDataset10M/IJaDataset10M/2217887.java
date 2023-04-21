package net.sf.mzmine.modules.rawdatamethods.filtering.datasetfilters;

import java.io.IOException;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.data.RawDataFileWriter;

public interface RawDataFilter {

    /**
	 * Returns a modified data file after being processed by the filter
	 */
    public RawDataFile filterDatafile(RawDataFile dataFile, RawDataFileWriter newFile) throws IOException;

    public double getProgress();
}
