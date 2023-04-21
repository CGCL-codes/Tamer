package uk.ac.sanger.cgp.bioview.demo;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;
import uk.ac.sanger.cgp.bioview.chromatogram.ChromatogramRenderer;
import uk.ac.sanger.cgp.bioview.chromatogram.SimpleChromGraphic;
import uk.ac.sanger.cgp.bioview.exceptions.ChromatogramRenderException;
import uk.ac.sanger.cgp.bioview.util.ImageUtils;

/**
 *
 * @author Home
 */
public class ChromatogramDemo {

    private static ChromatogramRenderer chromRend = null;

    private static ImageUtils imageUtil = null;

    /** Creates a new instance of ChromatogramDemo */
    public ChromatogramDemo() {
    }

    public static void main(String[] args) {
        chromRend = new ChromatogramRenderer();
        imageUtil = new ImageUtils();
        File exampScf = new File("resources/BRAFexon11_normal1_reference_f_Run1.scf");
        File output = new File("image.png");
        try {
            imageUtil.writeImageToFile(output, createSimpleChrom(genChromatogram(exampScf)));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.toString());
        }
    }

    private static Chromatogram genChromatogram(File chromLoc) throws IOException {
        Chromatogram chrom = null;
        try {
            chrom = ChromatogramFactory.create(chromLoc);
        } catch (UnsupportedChromatogramFormatException e) {
            throw new IOException("UnsupportedChromatogramFormatException detected for: " + chromLoc.getPath());
        }
        return chrom;
    }

    private static BufferedImage createSimpleChrom(Chromatogram chrom) throws ChromatogramRenderException {
        BufferedImage bi = null;
        int startRenderAtScan = 0;
        int stopRenderAtScan = chrom.getTraceLength();
        int chromatHeight = 100;
        float horizScale = 2.0F;
        int localTopHeightBuffer = 5;
        boolean renderScale = true;
        SimpleChromGraphic chromGraph = new SimpleChromGraphic(chrom, startRenderAtScan, stopRenderAtScan, chromatHeight, horizScale, localTopHeightBuffer, renderScale);
        bi = chromRend.createBuffer(chromGraph);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        imageUtil.basicDrawArea(g2, bi);
        try {
            chromRend.drawChromatogram(g2, chromGraph);
        } catch (Exception e) {
            throw new ChromatogramRenderException(e.getMessage(), e);
        }
        g2.dispose();
        bi.flush();
        return bi;
    }

    private static BufferedImage createChromFeatures() {
        BufferedImage bi = null;
        return bi;
    }
}
