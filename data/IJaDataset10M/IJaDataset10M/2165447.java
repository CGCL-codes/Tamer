package nij.qrfrp.extract;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import nij.qrfrp.extract.RidgeExtractor.RidgeExtractorConf;
import nij.qrfrp.template.XytqTemplate;

/**
 * <p>Class that manages the image preprocessor and 
 * all of the individual feature extractors.</p>
 * 
 * <p>This software is distributed under the GNU General Public License, version 3.0.
 * A copy of the license should have been distributed along with the code. If not, 
 * please visit http://www.fsf.org to view the terms of the license.</p>
 * 
 * @author jmetzger 
 */
public class ExtractionManager {

    private BufferedImage inputImage = null;

    private int dpi;

    private File configFile = null;

    private ImagePreprocessor preProc = null;

    private PoreExtractor poreExtr = null;

    private RidgeExtractor ridgeExtr = null;

    private MinutiaExtractor minExtr = null;

    private EdgeoscopicExtractor edgeExtr = null;

    private ProximalPointMapper proxPM = null;

    /**
	 * <p>Constructor with a config file specified (defaults overridden).</p>
	 * 
	 * @param inputImage the Hilditch-thinned image
	 * @param dpi the dpi of the input image
	 * @param configFile a config file defining necessary parameters
	 */
    public ExtractionManager(BufferedImage inputImage, int dpi, File configFile) {
        this.inputImage = inputImage;
        this.dpi = dpi;
        this.configFile = configFile;
        this.preProcess();
    }

    /**
	 * <p>Constructor with no config file specified (defaults used).</p>
	 * 
	 * @param inputImage the Hilditch-thinned image
	 * @param dpi the dpi of the input image
	 */
    public ExtractionManager(BufferedImage inputImage, int dpi) {
        this.inputImage = inputImage;
        this.dpi = dpi;
        this.preProcess();
    }

    /**
	 * <p>Extract all the features from the input image.</p>
	 * 
	 * @throws IOException 
	 */
    public void extractAll() throws IOException {
        this.extractPores();
        this.extractRidges();
        this.extractMinutiae();
        this.extractEdgeoscopicFeatures();
        this.constrainRidges();
    }

    /**
	 * <p> Perform the image preprocessing.</p>
	 * 
	 * @return a preprocessed image
	 */
    public BufferedImage preProcess() {
        if (this.configFile != null) this.preProc = new ImagePreprocessor(this.inputImage, this.dpi, this.configFile); else this.preProc = new ImagePreprocessor(this.inputImage, this.dpi);
        this.preProc.execute();
        return this.preProc.getContrastAdjustedImage();
    }

    /**
	 * <p>Extract pores from the preprocessed image.</p>
	 * 
	 * @return a template containing location and orientation of pores
	 * @throws IOException
	 */
    public XytqTemplate extractPores() throws IOException {
        if (this.preProc.getContrastAdjustedImage() != null) {
            if (this.configFile != null) this.poreExtr = new PoreExtractor(preProc.getContrastAdjustedImage(), this.dpi, this.preProc.getMacroOrientation(), this.configFile); else this.poreExtr = new PoreExtractor(preProc.getContrastAdjustedImage(), this.dpi, this.preProc.getMacroOrientation());
            this.poreExtr.execute();
            return this.poreExtr.getPoreTemplate();
        }
        return null;
    }

    /**
	 * <p>Extract ridges from the preprocessed image.</p>
	 * 
	 * @return a template containing location and orientation of ridge contour points
	 * @throws IOException
	 */
    public XytqTemplate extractRidges() throws IOException {
        if (this.preProc.getContrastAdjustedImage() != null && this.preProc.getMacroOrientation() != null) {
            if (this.configFile != null) this.ridgeExtr = new RidgeExtractor(preProc.getContrastAdjustedImage(), this.dpi, preProc.getMacroOrientation(), this.configFile); else this.ridgeExtr = new RidgeExtractor(preProc.getContrastAdjustedImage(), this.dpi, preProc.getMacroOrientation());
            this.ridgeExtr.execute();
            return this.ridgeExtr.getRidgeTemplate();
        }
        return null;
    }

    /**
	 * <p>Extract minutia from the blob-filled image created during ridge extraction.</p>
	 * 
	 * @return a template containing location and orientation of minutiae
	 * @throws IOException
	 */
    public XytqTemplate extractMinutiae() throws IOException {
        if (this.ridgeExtr.getBlobFilledImage() != null) {
            this.minExtr = new MinutiaExtractor(ridgeExtr.getBlobFilledImage(), this.dpi, this.ridgeExtr.getExecutablePath());
            this.minExtr.execute();
            return this.minExtr.getMinTemplate();
        }
        return null;
    }

    /**
	 * <p>Constrain the extracted ridge contour points to the region within a specified 
	 * radius of detected minutiae.</p>
	 * 
	 * @return a template containing location and orientation of minutia-constrained ridge contour points
	 * @throws IOException
	 */
    public XytqTemplate constrainRidges() {
        if (this.getRidgeExtr().getRidges() != null && this.getMinExtr().getScaledMinutiae() != null) {
            this.ridgeExtr.thinRidgePoints();
            this.proxPM = new ProximalPointMapper(this.getMinExtr().getScaledMinutiae(), this.getRidgeExtr().getRidges(), this.getRidgeExtr().getSuppressionRadius());
            this.proxPM.generateMap();
            this.proxPM.createXytqTemplate();
            this.proxPM.createDiagnosticImage(this.ridgeExtr.getHilditchImage());
            return this.proxPM.getXytqTemplate();
        }
        return null;
    }

    /**
	 * <p>Extract edgeoscopic features using the ridge contour extraction data.</p>
	 * 
	 * @return a template containing location and orientation of edgeoscopic features.  
	 * @throws IOException
	 */
    public XytqTemplate extractEdgeoscopicFeatures() throws IOException {
        if (this.getRidgeExtr().getHilditchImage() != null && this.ridgeExtr.getMicroOrientation() != null && this.preProc.getMacroOrientation() != null) {
            if (this.configFile != null) this.edgeExtr = new EdgeoscopicExtractor(ridgeExtr.getHilditchImage(), this.dpi, ridgeExtr.getRidges(), this.ridgeExtr.getMicroOrientation(), this.preProc.getMacroOrientation(), this.configFile); else this.edgeExtr = new EdgeoscopicExtractor(ridgeExtr.getHilditchImage(), this.dpi, ridgeExtr.getRidges(), this.ridgeExtr.getMicroOrientation(), this.preProc.getMacroOrientation());
            this.edgeExtr.execute();
            return this.edgeExtr.getEdgeTemplate();
        }
        return null;
    }

    /**
	 * <p>Writes diagnostic images created in the feature extraction process.
	 * if the MINIMUM_DIAGNOSTICS boolean variable is true, only the essential images will
	 * be written to the specified directory.</p> 
	 * 
	 * @param outputDirectory the directory to write the images to
	 * @param root prefix string to attach to the beginning of the diagnostic image filename 
	 * @return a boolean indicating whether or not each image has written successfully
	 * @throws IOException if there is a problem writing the images
	 */
    public boolean dumpDiagnostics(File outputDirectory, String root) throws IOException {
        if (!outputDirectory.exists()) outputDirectory.mkdirs();
        boolean pp = false, p = false, r = false, m = false, e = false, pm = false;
        if (this.preProc != null) pp = this.preProc.writeDiagnosticImages(outputDirectory, root);
        if (this.poreExtr != null) p = this.poreExtr.writeDiagnosticImages(outputDirectory, root);
        if (this.ridgeExtr != null) r = this.ridgeExtr.writeDiagnosticImages(outputDirectory, root);
        if (this.minExtr != null && Boolean.parseBoolean(this.getRidgeExtr().getConf().get(RidgeExtractorConf.Key.DIAGNOSTICS_KEY))) m = this.minExtr.writeDiagnosticImages(outputDirectory, root);
        if (this.edgeExtr != null) e = this.edgeExtr.writeDiagnosticImages(outputDirectory, root);
        if (this.proxPM != null && Boolean.parseBoolean(this.getRidgeExtr().getConf().get(RidgeExtractorConf.Key.DIAGNOSTICS_KEY))) pm = this.proxPM.writeDiagnosticImages(outputDirectory, root);
        return (pp && p && r && m && e && pm);
    }

    /**
	 * <p>Writes the templates created to a specified output directory.</p>
	 * 
	 * @param outputDirectory the directory to write the templates to
	 * @param root prefix string to attach to the beginning of the template filename 
	 * @return a boolean indicating whether or not each template has written successfully
	 * @throws IOException if there is a problem writing the templates to disk
	 */
    public boolean dumpTemplates(File outputDirectory, String root) throws IOException {
        boolean p = false, r = false, m = false, e = false;
        if (this.poreExtr != null) {
            if (this.poreExtr.getPoreTemplate() != null) p = this.poreExtr.writeTemplate(new File(outputDirectory + File.separator + root + "_p.xyt"));
        }
        if (this.proxPM != null) {
            if (this.proxPM.getXytqTemplate() != null) r = this.proxPM.writeTemplate(new File(outputDirectory + File.separator + root + "_r.xyt"));
        }
        if (this.minExtr != null) {
            if (this.minExtr.getMinTemplate() != null) m = this.minExtr.writeTemplate(new File(outputDirectory + File.separator + root + "_m.xyt"));
        }
        if (this.edgeExtr != null) {
            if (this.edgeExtr.getEdgeTemplate() != null) e = this.edgeExtr.writeTemplate(new File(outputDirectory + File.separator + root + "_e.xyt"));
        }
        return (p && r && m && e);
    }

    /**
	 * <p>Writes the templates created to a specified output directories.</p>
	 * 
	 * @param poreDir the directory to write the pore template
	 * @param ridgeDir the directory to write the ridge template
	 * @param minDir the directory to write the minutia template
	 * @param edgeDir the directory to write the edgeoscopic feature template
	 * @param root prefix string to attach to the beginning of the template filename 
	 * @return a boolean indicating whether or not each template has written successfully
	 * @throws IOException if there is a problem writing the templates to disk 
	 */
    public boolean dumpTemplates(File poreDir, File ridgeDir, File minDir, File edgeDir, String root) throws IOException {
        boolean p = false, r = false, m = false, e = false;
        if (this.poreExtr != null) {
            if (this.poreExtr.getPoreTemplate() != null) p = this.poreExtr.writeTemplate(new File(poreDir + File.separator + root + "_p.xyt"));
        }
        if (this.proxPM != null) {
            if (this.proxPM.getXytqTemplate() != null) r = this.proxPM.writeTemplate(new File(ridgeDir + File.separator + root + "_r.xyt"));
        }
        if (this.minExtr != null) {
            if (this.minExtr.getMinTemplate() != null) m = this.minExtr.writeTemplate(new File(minDir + File.separator + root + "_m.xyt"));
        }
        if (this.edgeExtr != null) {
            if (this.edgeExtr.getEdgeTemplate() != null) e = this.edgeExtr.writeTemplate(new File(edgeDir + File.separator + root + "_e.xyt"));
        }
        return (p && r && m && e);
    }

    /**
	 * <p>Writes the quality information to files in the specified output directory.</p>
	 * 
	 * @param outputDir the directory to write the quality files to
	 * @param root prefix string to attach to the beginning of the quality filename 
	 * @return a boolean indicating whether or not each quality file has written successfully
	 * @throws IOException if there is a problem writing the quality files to disk
	 */
    public boolean dumpQuality(File outputDir, String root) {
        if (this.poreExtr != null) this.poreExtr.getPoreQuality().writeToFile(new File(outputDir + File.separator + root + "_qp.txt"));
        if (this.ridgeExtr != null) this.ridgeExtr.getRidgeQuality().writeToFile(new File(outputDir + File.separator + root + "_qr.txt"));
        return true;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public int getDpi() {
        return dpi;
    }

    public void setInputImage(BufferedImage inputImage) {
        this.inputImage = inputImage;
    }

    public BufferedImage getInputImage() {
        return inputImage;
    }

    public MinutiaExtractor getMinExtr() {
        return minExtr;
    }

    public PoreExtractor getPoreExtr() {
        return poreExtr;
    }

    public ImagePreprocessor getPreProc() {
        return preProc;
    }

    public RidgeExtractor getRidgeExtr() {
        return ridgeExtr;
    }

    public EdgeoscopicExtractor getEdgeExtr() {
        return edgeExtr;
    }

    public ProximalPointMapper getProxPM() {
        return proxPM;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public void setEdgeExtr(EdgeoscopicExtractor edgeExtr) {
        this.edgeExtr = edgeExtr;
    }

    public void setMinExtr(MinutiaExtractor minExtr) {
        this.minExtr = minExtr;
    }

    public void setPoreExtr(PoreExtractor poreExtr) {
        this.poreExtr = poreExtr;
    }

    public void setPreProc(ImagePreprocessor preProc) {
        this.preProc = preProc;
    }

    public void setProxPM(ProximalPointMapper proxPM) {
        this.proxPM = proxPM;
    }

    public void setRidgeExtr(RidgeExtractor ridgeExtr) {
        this.ridgeExtr = ridgeExtr;
    }
}
