package edu.xtec.jclic.beans;

import edu.xtec.jclic.bags.MediaBagEditor;
import edu.xtec.util.Options;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.border.Border;
import java.awt.Rectangle;
import edu.xtec.jclic.bags.MediaBag;
import edu.xtec.jclic.bags.MediaBagSelector;
import edu.xtec.jclic.misc.Utils;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class ImgButton extends NullableObject {

    public static final String PROP_IMG_NAME = "imageName";

    MediaBagEditor mbe;

    /** Creates a new instance of ImgPanel */
    public ImgButton() {
        super();
    }

    protected String getObjectType() {
        return PROP_IMG_NAME;
    }

    public void setMediaBagEditor(MediaBagEditor mbe) {
        this.mbe = mbe;
    }

    public String getImgName() {
        return (String) getObject();
    }

    public void setImgName(String name) {
        setObject(name);
    }

    public void setObject(Object value) {
        super.setObject(value);
        if (nullValue || object == null || mbe == null) button.setIcon(null); else {
            try {
                Rectangle r = button.getVisibleRect();
                int w = r.width - 4;
                int h = r.height - 4;
                if (w > 0 && h > 0) {
                    String s = (String) object;
                    MediaBag mb = mbe.getMediaBag();
                    button.setIcon(mb.getImageElement(s).getThumbNail(w, h, mb.getProject().getFileSystem()));
                }
            } catch (Exception ex) {
                System.err.println("Error reading image:\n" + ex);
            }
        }
    }

    protected Object createObject() {
        return null;
    }

    protected Object editObject(Object o) {
        if (options == null || mbe == null) return null;
        return MediaBagSelector.getMediaName((String) o, options, this, mbe, Utils.ALL_IMAGES_FF);
    }
}
