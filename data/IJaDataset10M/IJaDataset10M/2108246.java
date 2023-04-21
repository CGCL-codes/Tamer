package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.util.*;
import java.awt.*;
import java.io.InputStream;

/**
 * Illustrates how to use World Wind browser balloons to display HTML, JavaScript, and Flash content to the user in the
 * form of a screen-aligned balloon. There are two browser balloon types: <code>{@link ScreenBrowserBalloon}</code>
 * which displays a balloon at a point on the screen, and <code>{@link GlobeBrowserBalloon}</code> which displays a
 * balloon attached to a position on the Globe.
 * <p/>
 * <strong>Browser Balloon Content</strong> <br/> A balloon's HTML content is specified by calling <code>{@link
 * Balloon#setText(String)}</code> with a string containing either plain text or HTML + JavaScript. The balloon's visual
 * attributes are specified by calling <code>{@link Balloon#setAttributes(gov.nasa.worldwind.render.BalloonAttributes)}</code>
 * with an instance of <code>{@link BalloonAttributes}</code>.
 *
 * @author dcollins
 * @version $Id: WebBrowserBalloons.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class WebBrowserBalloons extends ApplicationTemplate {

    protected static final String BROWSER_BALLOON_CONTENT_PATH = "gov/nasa/worldwindx/examples/data/BrowserBalloonExample.html";

    public static class AppFrame extends ApplicationTemplate.AppFrame {

        protected HotSpotController hotSpotController;

        protected BalloonController balloonController;

        public AppFrame() {
            this.makeBrowserBalloon();
            this.hotSpotController = new HotSpotController(this.getWwd());
            this.balloonController = new BalloonController(this.getWwd());
            Dimension size = new Dimension(1200, 800);
            this.setPreferredSize(size);
            this.pack();
            WWUtil.alignComponent(null, this, AVKey.CENTER);
        }

        protected void makeBrowserBalloon() {
            String htmlString = null;
            InputStream contentStream = null;
            try {
                contentStream = WWIO.openFileOrResourceStream(BROWSER_BALLOON_CONTENT_PATH, this.getClass());
                htmlString = WWIO.readStreamToString(contentStream, null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                WWIO.closeStream(contentStream, BROWSER_BALLOON_CONTENT_PATH);
            }
            if (htmlString == null) htmlString = Logging.getMessage("generic.ExceptionAttemptingToReadFile", BROWSER_BALLOON_CONTENT_PATH);
            Position balloonPosition = Position.fromDegrees(38.883056, -77.016389);
            AbstractBrowserBalloon balloon = new GlobeBrowserBalloon(htmlString, balloonPosition);
            BalloonAttributes attrs = new BasicBalloonAttributes();
            attrs.setSize(new Size(Size.NATIVE_DIMENSION, 0d, null, Size.NATIVE_DIMENSION, 0d, null));
            balloon.setAttributes(attrs);
            PointPlacemark placemark = new PointPlacemark(balloonPosition);
            placemark.setLabelText("Click to open balloon");
            placemark.setValue(AVKey.BALLOON, balloon);
            RenderableLayer layer = new RenderableLayer();
            layer.setName("Web Browser Balloons");
            layer.addRenderable(balloon);
            layer.addRenderable(placemark);
            insertBeforePlacenames(getWwd(), layer);
            this.getLayerPanel().update(this.getWwd());
        }
    }

    public static void main(String[] args) {
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 62);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -77);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 9500000);
        Configuration.setValue(AVKey.INITIAL_PITCH, 45);
        start("World Wind Web Browser Balloons", AppFrame.class);
    }
}
