package uk.co.marcoratto.j2me.about;

import javax.microedition.lcdui.*;
import uk.co.marcoratto.j2me.i18n.I18N;

/**
 * Typical about box with a string and an image.
 * In this case the Sun copyright and logo.
 */
public class About {

    private static About istanza = null;

    private Alert alert;

    private Display display;

    private String copyright;

    /**
     * Do not allow anyone to create this class
     */
    private About() {
    }

    public static About getInstance(Display d) {
        if (istanza == null) {
            try {
                istanza = new About();
                istanza.display = d;
                istanza.init();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return istanza;
    }

    private void init() throws Exception {
        copyright = I18N.getInstance().translate("about.copyright");
        alert = new Alert(I18N.getInstance().translate("about.title"));
        alert.setTimeout(Alert.FOREVER);
        if (display.numColors() > 2) {
            String icon = (display.isColor()) ? "/icons/JavaPowered-8.png" : "/icons/JavaPowered-2.png";
            try {
                Image image = Image.createImage(icon);
                alert.setImage(image);
            } catch (java.io.IOException x) {
            }
        }
        alert.setString(copyright);
    }

    public void show() {
        display.setCurrent(alert);
    }
}
