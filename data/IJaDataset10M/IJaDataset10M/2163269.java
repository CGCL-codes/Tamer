package org.opencms.setup.comptest;

import com.alkacon.simapi.RenderSettings;
import com.alkacon.simapi.Simapi;
import com.alkacon.simapi.filter.ImageMath;
import com.alkacon.simapi.filter.RotateFilter;
import org.opencms.setup.CmsSetupBean;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import javax.imageio.ImageIO;

/**
 * Tests the image processing capabilities.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.2 $ 
 * 
 * @since 6.1.8 
 */
public class CmsSetupTestSimapi implements I_CmsSetupTest {

    /** The test name. */
    public static final String TEST_NAME = "Image Processing";

    /**
     * @see org.opencms.setup.comptest.I_CmsSetupTest#getName()
     */
    public String getName() {
        return TEST_NAME;
    }

    /**
     * @see org.opencms.setup.comptest.I_CmsSetupTest#execute(org.opencms.setup.CmsSetupBean)
     */
    public CmsSetupTestResult execute(CmsSetupBean setupBean) {
        CmsSetupTestResult testResult = new CmsSetupTestResult(this);
        boolean ok = true;
        Throwable ex = null;
        try {
            RenderSettings settings = new RenderSettings(Simapi.RENDER_QUALITY);
            settings.setCompressionQuality(0.85f);
            Simapi simapi = new Simapi(settings);
            ImageIO.scanForPlugins();
            Iterator pngReaders = ImageIO.getImageReadersByFormatName(Simapi.TYPE_PNG);
            if (!pngReaders.hasNext()) {
                throw (new Exception("No Java ImageIO readers for the PNG format are available."));
            }
            Iterator pngWriters = ImageIO.getImageWritersByFormatName(Simapi.TYPE_PNG);
            if (!pngWriters.hasNext()) {
                throw (new Exception("No Java ImageIO writers for the PNG format are available."));
            }
            String basePath = setupBean.getWebAppRfsPath();
            if (!basePath.endsWith(File.separator)) {
                basePath += File.separator;
            }
            basePath += "setup" + File.separator + "resources" + File.separator;
            BufferedImage img1 = Simapi.read(basePath + "test1.png");
            BufferedImage img3 = simapi.applyFilter(img1, new RotateFilter(ImageMath.PI));
            simapi.write(img3, basePath + "test3.png", Simapi.TYPE_PNG);
            BufferedImage img2 = Simapi.read(basePath + "test2.png");
            ok = Arrays.equals(simapi.getBytes(img2, Simapi.TYPE_PNG), simapi.getBytes(img3, Simapi.TYPE_PNG));
        } catch (Throwable e) {
            ok = false;
            ex = e;
        }
        if (ok) {
            testResult.setResult(RESULT_PASSED);
            testResult.setGreen();
        } else {
            testResult.setYellow();
            if (ex != null) {
                testResult.setResult(RESULT_FAILED);
                testResult.setHelp(ex.toString());
                testResult.setInfo("<p><code>-Djava.awt.headless=true</code> JVM parameter or X-Server may be missing.<br>" + "<b>You can continue the setup, but image processing will be disabled.</b></p>");
            } else {
                testResult.setResult(RESULT_WARNING);
                testResult.setHelp("Image processing works but result does not exactly match.");
                StringBuffer info = new StringBuffer();
                info.append("<p>Please check the following images for visible differences:</p>");
                info.append("<table width='100%'>");
                info.append("<tr><th>Expected</th><th>Result</th></tr>");
                info.append("<tr><td align='center' width='50%'><img src='resources/test2.png'></td>");
                info.append("<td align='center' width='50%'><img src='resources/test3.png'></td></table>");
                info.append("<p><b>You can continue the setup, but image processing may not always work as expected.</b></p>");
                testResult.setInfo(info.toString());
            }
        }
        return testResult;
    }
}
