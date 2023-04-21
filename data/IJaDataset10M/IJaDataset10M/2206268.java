package org.mozilla.browser.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.mozilla.browser.MozillaAutomation;

public class RenderToImageTest extends MozillaTest {

    public void testRender() throws IOException {
        MozillaAutomation.blockingLoad(moz, "about:");
        byte[] pngImage = MozillaAutomation.renderToImage(moz);
        ByteArrayInputStream bis = new ByteArrayInputStream(pngImage);
        ImageIO.read(bis);
    }
}
