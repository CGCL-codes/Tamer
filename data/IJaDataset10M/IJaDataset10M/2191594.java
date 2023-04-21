package JavaOrc.diagram;

import JavaOrc.ui.PathIconManager;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @class DeviceRendererComponent
 * @author Eric Crahen
 */
public class DeviceRendererComponent extends CustomComponent {

    protected static final CustomUI deviceUI = new CustomUI("device");

    protected static final Insets margin = new Insets(1, 1, 1, 1);

    protected Image picture;

    static {
        UIManager.put("device.background", new Color(0xFF, 0xFF, 0xEF));
        UIManager.put("device.foreground", Color.black);
        UIManager.put("device.border", BorderFactory.createEmptyBorder());
    }

    /**
   * Create a new Component for painting classes
   */
    public DeviceRendererComponent() {
        this.setLayout(null);
        setUI(deviceUI);
    }

    public void setPicture(String picname) {
        if (picname != "") {
            picture = PathIconManager.getInstance().getImageResource(this, picname);
        }
    }

    public String getPicture() {
        return "";
    }

    /**
   * Create a built in layout, there seems to be a bug with current LayoutManagers
   * placing TextAreas in scroll panes with borders in the same component correctly.
   * They leave an extra pixel at the bottom with the hieght would be an odd number.
   *
   * This will garuntee the component will be laid out as expected.
   */
    public void paintComponent(Graphics g) {
        Insets insets = this.getInsets();
        Graphics2D g2 = (Graphics2D) g;
        int w = this.getWidth() - (insets.left + insets.right);
        int h = this.getHeight() - (insets.top + insets.bottom);
        int x = insets.left;
        int y = insets.top;
        g2.drawImage(picture, x, y, null);
    }
}
