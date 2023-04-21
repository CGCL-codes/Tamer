package fbench;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * IEC 61499 File Type Icon Bank

 * @author WD
 *
 * @version 20061126/WD
 */
public class ImageIconBank {

    private static Hashtable icons = new Hashtable(32);

    private static final ImageIconBank loader = new ImageIconBank();

    public static final Insets ibMargin = new Insets(0, 0, 0, 0);

    public ImageIconBank() {
        load("default");
    }

    public static ImageIcon get(String name) {
        Object icon = icons.get(name);
        return icon != null ? (ImageIcon) icon : loader.load(name);
    }

    private ImageIcon load(String name) {
        java.net.URL imgres = getClass().getResource("images/" + name + ".gif");
        if (imgres == null) return null; else return new ImageIcon(imgres);
    }

    public static JButton newIconButton(String iconName, String toolTipText, String actionCommand, ActionListener listener) {
        JButton ans = new JButton(get(iconName));
        ans.setToolTipText(toolTipText);
        ans.setActionCommand(actionCommand);
        ans.setMargin(ibMargin);
        ans.addActionListener(listener);
        return ans;
    }
}
