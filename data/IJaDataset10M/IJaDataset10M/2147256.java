package edu.ucdavis.genomics.metabolomics.binbase.algorythm.data.statistic.normalize;

import java.util.List;
import org.jdom.Element;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.data.ResultDataFile;
import edu.ucdavis.genomics.metabolomics.util.io.source.Source;
import edu.ucdavis.genomics.metabolomics.util.statistics.normalize.Normalizeable;

/**
 * standard binbase normalization class
 * @author wohlgemuth
 * @version Feb 7, 2007
 *
 */
public abstract class BinBaseNormalization extends Normalizeable {

    private ResultDataFile file;

    public ResultDataFile getFile() {
        return file;
    }

    public void setFile(ResultDataFile file) {
        this.file = file;
    }

    /**
	 * needed to configure the given normalization
	 * @author wohlgemuth
	 * @version Feb 20, 2007
	 * @param e
	 */
    public boolean configure(Element e) {
        return true;
    }

    /**
	 * returns possible metainformations for this normalization like graphes and such stuff
	 * @author wohlgemuth
	 * @version Feb 8, 2007
	 * @return
	 */
    public abstract List<Source> getMetaInformations();
}
