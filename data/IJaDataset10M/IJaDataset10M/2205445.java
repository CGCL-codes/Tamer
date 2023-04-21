package com.volantis.map.ics.imageprocessor.servlet;

import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.io.InputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.imageio.ImageIO;
import org.apache.log4j.Category;

/**
 * This class contains tests that are based around converting an image from one
 * format to another.
 */
public class GIFWatermarkingTestCase extends TestCaseAbstract {

    protected void tearDown() throws Exception {
        Category.shutdown();
        super.tearDown();
    }

    /**
     * Load an image given a rule and test the resulting image type
     *
     * @param rule     the transcode rule to use
     * @param image    the name of the image to load
     * @param expected the string representation of the image type expected
     */
    private void doWatermarking(String rule, String image, String expected) throws Throwable {
        String parameters[][] = { { "reload", "true" } };
        InputStream stream = TestUtilities.doImageTranscodeTest(expectations, rule, image, parameters, true);
        RenderedOp img = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        if (expected.equals("tc")) {
            assertEquals("Image is not truecolour", true, TestUtilities.isTrueColour(img));
        } else if (expected.equals("mono")) {
            assertEquals("Image is not Monochrome", true, TestUtilities.isMonochrome(img));
        } else if (expected.equals("grey")) {
            assertEquals("Image is not greyscale", true, TestUtilities.isGreyscale(img));
        } else if (expected.equals("grey4")) {
            assertEquals("Image is not greyscale4", true, TestUtilities.isGreyscale4(img));
        } else if (expected.equals("grey2")) {
            assertEquals("Image is not greyscale2", true, TestUtilities.isGreyscale2(img));
        } else {
            assertEquals("Image is not indexed", true, TestUtilities.isIndexed(img));
        }
    }

    /**
     * This test is necessary to ensure that a valid JPEG image is created
     * because of the JAI bug 4908419.
     */
    public void testTransparentGIFtoJPEGWithWatermarking() throws Throwable {
        doWatermarking(OutputImageRules.COLOURJPEG24, "trans-test.gif", "tc");
    }

    /**
     * Convert true colour image to every other type
     */
    public void testConvertWithWatermarkingTCtoTC() throws Throwable {
        doWatermarking(OutputImageRules.COLOURJPEG24, "lena.tiff", "tc");
        doWatermarking(OutputImageRules.COLOURPNG24, "lena.tiff", "tc");
        doWatermarking(OutputImageRules.COLOURTIFF24, "lena.tiff", "tc");
    }

    public void testConvertWithWatermarkingTCtoIndexed() throws Throwable {
        doWatermarking(OutputImageRules.COLOURPNG8, "lena.tiff", "indexed");
        doWatermarking(OutputImageRules.COLOURGIF8, "lena.tiff", "indexed");
    }

    public void testConvertWithWatermarkingTCtoGrey8() throws Throwable {
        doWatermarking(OutputImageRules.GREYSCALEJPEG8, "lena.tiff", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF8, "lena.tiff", "grey");
    }

    public void testConvertWithWatermarkingTCtoGrey4() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG4, "lena.tiff", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF4, "lena.tiff", "grey4");
    }

    public void testConvertWithWatermarkingTCtoGrey2() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG2, "lena.tiff", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF2, "lena.tiff", "grey2");
    }

    public void testConvertWithWatermarkingTCtoMonochrome() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG1, "lena.tiff", "mono");
        doWatermarking(OutputImageRules.GREYSCALEGIF1, "lena.tiff", "mono");
    }

    /**
     * Convert greyscale image to every other type
     */
    public void testConvertWithWatermarkingGreyToTC() throws Throwable {
        doWatermarking(OutputImageRules.COLOURJPEG24, "greymarbles.tiff", "tc");
        doWatermarking(OutputImageRules.COLOURPNG24, "greymarbles.tiff", "tc");
        doWatermarking(OutputImageRules.COLOURTIFF24, "greymarbles.tiff", "tc");
    }

    public void testConvertWithWatermarkingGreyToIndexed() throws Throwable {
        doWatermarking(OutputImageRules.COLOURPNG8, "greymarbles.tiff", "grey");
        doWatermarking(OutputImageRules.COLOURGIF8, "greymarbles.tiff", "grey");
    }

    public void testConvertWithWatermarkingGreyToGrey8() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG8, "greymarbles.tiff", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF8, "greymarbles.tiff", "grey");
    }

    public void testConvertWithWatermarkingGreyToGrey4() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG4, "greymarbles.tiff", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF4, "greymarbles.tiff", "grey4");
    }

    public void testConvertWithWatermarkingGreyToGrey2() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG2, "greymarbles.tiff", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF2, "greymarbles.tiff", "grey2");
    }

    public void testConvertWithWatermarkingGreyToMonochrome() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG1, "greymarbles.tiff", "mono");
        doWatermarking(OutputImageRules.GREYSCALEGIF1, "greymarbles.tiff", "mono");
    }

    /**
     * Convert indexed image to every other type
     */
    public void testConvertWithWatermarkingIndexedToTC() throws Throwable {
        doWatermarking(OutputImageRules.COLOURJPEG24, "circusshow4256.bmp", "tc");
        doWatermarking(OutputImageRules.COLOURPNG24, "circusshow4256.bmp", "tc");
        doWatermarking(OutputImageRules.COLOURTIFF24, "circusshow4256.bmp", "tc");
    }

    public void testConvertWithWatermarkingIndexedToIndexed() throws Throwable {
        doWatermarking(OutputImageRules.COLOURPNG8, "circusshow4256.bmp", "indexed");
        doWatermarking(OutputImageRules.COLOURGIF8, "circusshow4256.bmp", "indexed");
    }

    public void testConvertWithWatermarkingIndexedToGrey() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG8, "circusshow4256.bmp", "grey");
        doWatermarking(OutputImageRules.GREYSCALEGIF8, "circusshow4256.bmp", "grey");
    }

    public void testConvertWithWatermarkingIndexedToMonochrome() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG1, "circusshow4256.bmp", "mono");
        doWatermarking(OutputImageRules.GREYSCALEGIF1, "circusshow4256.bmp", "mono");
    }

    /**
     * Convert SVG image to every other available type
     */
    public void testConvertWithWatermarkingSVGToColourJPEG() throws Throwable {
        doWatermarking(OutputImageRules.COLOURJPEG24, "asf-logo.svg", "tc");
    }

    public void testConvertWithWatermarkingSVGToColourTIFF() throws Throwable {
        doWatermarking(OutputImageRules.COLOURTIFF24, "asf-logo.svg", "tc");
    }

    public void testConvertWithWatermarkingSVGToJPEGGreyscale() throws Throwable {
        doWatermarking(OutputImageRules.GREYSCALEJPEG8, "asf-logo.svg", "grey");
    }

    public void testConvertWithWatermarkingSVGToGifIndexed() throws Throwable {
        doWatermarking(OutputImageRules.COLOURGIF8, "asf-logo.svg", "indexed");
    }

    public void testConvertWithWatermarkingSVGToGifGreyscale() throws Throwable {
        doWatermarking(OutputImageRules.GREYSCALEGIF8, "asf-logo.svg", "grey");
    }

    public void testConvertWithWatermarkingSVGToGifGreyscale4() throws Throwable {
        doWatermarking(OutputImageRules.GREYSCALEGIF2, "asf-logo.svg", "grey2");
    }

    public void testConvertWithWatermarkingSVGToGifGreyscale16() throws Throwable {
        doWatermarking(OutputImageRules.GREYSCALEGIF4, "asf-logo.svg", "grey4");
    }

    public void testConvertWithWatermarkingSVGToGIFMonochrome() throws Throwable {
        doWatermarking(OutputImageRules.GREYSCALEGIF1, "asf-logo.svg", "mono");
    }

    public void testConvertWithWatermarkingSVGToPNGGreyscale() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG8, "asf-logo.svg", "grey");
    }

    public void testConvertWithWatermarkingSVGToPNGGreyscale4() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG2, "asf-logo.svg", "grey");
    }

    public void testConvertWithWatermarkingSVGToPNGGreyscale16() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG4, "asf-logo.svg", "grey");
    }

    public void testConvertWithWatermarkingSVGToPNGMonochrome() throws Throwable {
        doWatermarking(OutputImageRules.GRAYSCALEPNG1, "asf-logo.svg", "mono");
    }

    public void testConvertWithWatermarkingSVGToPNGIndexed() throws Throwable {
        doWatermarking(OutputImageRules.COLOURPNG8, "asf-logo.svg", "indexed");
    }

    public void testConvertWithWatermarkingSVGToPNGColour() throws Throwable {
        doWatermarking(OutputImageRules.COLOURPNG24, "asf-logo.svg", "tc");
    }
}
