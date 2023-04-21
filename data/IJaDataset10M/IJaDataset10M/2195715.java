package org.fest.swing.junit.ant;

import static org.fest.assertions.Assertions.assertThat;
import java.awt.image.BufferedImage;
import org.fest.swing.image.ScreenshotTaker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests for <code>{@link ImageHandler}</code>.
 *
 * @author Alex Ruiz
 */
public class ImageHandlerTest {

    private ScreenshotTaker screenshotTaker;

    @BeforeClass
    public void setUp() {
        screenshotTaker = new ScreenshotTaker();
    }

    @Test
    public void shouldEncodeAndDecodeImage() {
        BufferedImage imageToEncode = screenshotTaker.takeDesktopScreenshot();
        String encoded = ImageHandler.encodeBase64(imageToEncode);
        assertThat(encoded).isNotEmpty();
        BufferedImage decodedImage = ImageHandler.decodeBase64(encoded);
        assertThat(decodedImage).isNotNull().isEqualTo(imageToEncode);
    }
}
