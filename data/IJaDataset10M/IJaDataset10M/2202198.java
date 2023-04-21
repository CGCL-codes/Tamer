package uk.ac.sanger.cgp.bioview.beans;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.biojava.bio.chromatogram.Chromatogram;
import uk.ac.sanger.cgp.bioview.enums.FeatureType;
import uk.ac.sanger.cgp.bioview.exceptions.ImageRenderException;

/**
 * This class is used to hold all of the information required to generate an
 * 'annotated' chromatogram.  Using this class will allow easier creation of a
 * chromatogram image as mehods are already written to work with data in this format.
 * 
 * By 'annotated' we mean:
 * <ul>
 *  <li>Coverage boundries marked</li>
 *  <li>Optional variant locations</li>
 *  <li>Optional amplimer sequence</li>
 *  <li>Optional domains</li>
 *  <li>Optional inclusive and exclusive range highlighting</li>
 * </ul>
 * @author Original: kr2
 * @author $Author: kr2 $
 * @version $Revision: 1.1 $
 */
public class ChromRenderBean {

    protected final Log log = LogFactory.getLog(this.getClass());

    private int traceCoverageStart = -1;

    private int traceCoverageStop = -1;

    private int startRenderAtBase = -1;

    private int stopRenderAtBase = -1;

    private List perBaseScans = null;

    private Chromatogram chromatogram = null;

    private String amplimerSeq = null;

    /**
   * A list of FeatureBeans
   */
    private List features = null;

    /**
   * Constructor for a ChromRenderBean.  Inorder to use this object it is expected
   * that some form of peak detection has been performed providing the following information:<br/>
   * <ul>
   *  <li>A list of scan indicies indicating peak locations matching to provided amplimer</li>
   *  <li>The base in the amplimer that the first scan matches to.</li>
   *  <li>The base in the amplimer that the last scan matches to.</li>
   * </ul>
   * If render start and stop are set to 0 then the full chromatogram is rendered.
   * @param chrom a Chromatogram object.
   * @param coverageStart location of the first scan index with respect to the provided amplimer.
   * @param coverageStop location of the last scan index with respect to the provided amplimer.
   * @param renderStart point at which rendering of the image should begin with respect to the amplimer.
   * @param renderStop point at which rendering of the image should end with respect to the amplimer.
   * @param scans list of scan indicies indicating peaks matching amplimer.
   * @param seq the amplimer sequence
   */
    public ChromRenderBean(Chromatogram chrom, int coverageStart, int coverageStop, int renderStart, int renderStop, List scans, String seq) {
        traceCoverageStart = coverageStart;
        traceCoverageStop = coverageStop;
        startRenderAtBase = renderStart;
        stopRenderAtBase = renderStop;
        perBaseScans = scans;
        amplimerSeq = seq;
        chromatogram = chrom;
        try {
            FeatureBean fb = new FeatureBean(traceCoverageStart, traceCoverageStop, FeatureType.EXCLUSIVE_BOUNDRY, Color.YELLOW);
            addFeature(fb);
        } catch (ImageRenderException e) {
            log.error("The trace coverage feature failed to construct, can't happen here unless someone changes the code but needed to handle the exception", e);
        }
    }

    /**
   * Get the start of coverage.
   * @return start of coverage.
   */
    public int getTraceCoverageStart() {
        return traceCoverageStart;
    }

    /**
   * Get the end of coverage.
   * @return end of coverage
   */
    public int getTraceCoverageStop() {
        return traceCoverageStop;
    }

    /**
   * Get start of rendering.
   * @return start of rendering.
   */
    public int getStartRenderAtBase() {
        return startRenderAtBase;
    }

    /**
   * Get end of rendering.
   * @return end of rendering
   */
    public int getStopRenderAtBase() {
        return stopRenderAtBase;
    }

    /**
   * Get the list of scan indicies.
   * @return List of scan indicies.
   */
    public List getPerBaseScans() {
        return perBaseScans;
    }

    /**
   * Get the amplimer sequence.
   * @return amplimer sequence.
   */
    public String getAmplimerSeq() {
        return amplimerSeq;
    }

    /**
   * Get the current list of FeatureBean objects.
   * @return list of FeatureBeans
   */
    public List getFeatures() {
        if (features == null) {
            features = new ArrayList();
        }
        return features;
    }

    /**
   * Add a new feature.
   * @param feat a new feature to be rendered.
   */
    public void addFeature(FeatureBean feat) {
        if (features == null) {
            features = new ArrayList();
        }
        features.add(feat);
    }

    /**
   * Get the Chromatogram object.
   * @return Chromatogram object.
   */
    public Chromatogram getChromatogram() {
        return chromatogram;
    }

    /**
   * A string representation of this class.
   * @return string representation.
   */
    public String toString() {
        String nl = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
        String dec = "========";
        sb.append(dec).append(" [S] uk.ac.sanger.cgp.biodraw.beans.ChromRenderBean ").append(dec).append(nl);
        sb.append("traceCoverageStart=").append(traceCoverageStart).append(nl);
        sb.append("traceCoverageStop=").append(traceCoverageStop).append(nl);
        sb.append("startRenderAtBase=").append(startRenderAtBase).append(nl);
        sb.append("stopRenderAtBase=").append(stopRenderAtBase).append(nl);
        sb.append("perBaseScans=").append(perBaseScans).append(nl);
        sb.append("amplimerSeq=").append(amplimerSeq).append(nl);
        sb.append("features=").append(features).append(nl);
        sb.append(dec).append(" [E] uk.ac.sanger.cgp.biodraw.beans.ChromRenderBean ").append(dec);
        return sb.toString();
    }
}
