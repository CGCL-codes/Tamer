package graphic;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.ImageIcon;
import properties.ClanProperties;

/**
 * @author Imi
 */
public class ClanImageProvider {

    public static ClanProperties prop = new ClanProperties("icon");

    private static final ClanImageProvider clanIcon = new ClanImageProvider();

    HashMap cache = new HashMap();

    public static Image get(ClanImageDescriptor d) {
        return clanIcon.getImpl(d);
    }

    public static Image get(String filename) {
        return clanIcon.getImpl(new ClanImageDescriptor(filename, -1, 0, -1));
    }

    private Image getImpl(ClanImageDescriptor d) {
        Image ret = (Image) cache.get(d.toString());
        if (ret == null) {
            ret = new ImageIcon(d.filename).getImage();
            if (d.flavour != -1) {
                int ydimension = ret.getHeight(null);
                int xdimension = d.xdimension == -1 ? ydimension : d.xdimension;
                assert (xdimension > 0 && ydimension > 0) : d.filename;
                BufferedImage i = MainScreen.graphicConfiguration.createCompatibleImage(xdimension, ydimension, Transparency.TRANSLUCENT);
                Graphics g = i.createGraphics();
                g.drawImage(ret, 0, 0, xdimension, ydimension, xdimension * d.flavour, 0, xdimension * (d.flavour + 1), ydimension, null);
                g.dispose();
                ret = i;
            }
            if (d.rotation != 0) {
                int ydimension = ret.getHeight(null);
                int xdimension = d.xdimension == -1 ? ydimension : d.xdimension;
                AffineTransform at = new AffineTransform();
                at.rotate(Math.PI / 2 * d.rotation, xdimension / 2.0, ydimension / 2.0);
                BufferedImage i = MainScreen.graphicConfiguration.createCompatibleImage(xdimension, ydimension, Transparency.TRANSLUCENT);
                Graphics2D g = i.createGraphics();
                g.drawImage(ret, at, null);
                g.dispose();
                ret = i;
            }
            cache.put(d.toString(), ret);
        }
        assert ret.getWidth(null) != -1 : d.filename;
        return ret;
    }
}
