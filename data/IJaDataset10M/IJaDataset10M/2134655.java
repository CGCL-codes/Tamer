package gnu.kinsight.guiutil;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import gnu.kinsight.Kinsight;

public class Kit {

    private static final String path = "gnu/kinsight/pix/";

    private static final String path16 = path + "16x16/";

    private static final String path22 = path + "22x22/";

    public static final ImageIcon APPROX_NOT = getIcon("approx-unselect.png");

    public static final ImageIcon APPROX = getIcon("approx.png");

    public static final ImageIcon DELETE16 = getIcon16("editdelete.png");

    public static final ImageIcon EDIT16 = getIcon16("pencil.png");

    public static final ImageIcon NEW22 = getIcon22("edit_add.png");

    public static final ImageIcon VIEW22 = getIcon22("viewmag.png");

    public static final ImageIcon FIND22 = getIcon22("find.png");

    public static ImageIcon getIcon(String icon) {
        return new ImageIcon(ClassLoader.getSystemResource(path + icon));
    }

    public static ImageIcon getIcon16(String icon) {
        return new ImageIcon(ClassLoader.getSystemResource(path16 + icon));
    }

    public static ImageIcon getIcon22(String icon) {
        return new ImageIcon(ClassLoader.getSystemResource(path22 + icon));
    }

    public static void newFrame(JComponent p, String title) {
        JInternalFrame f = new JInternalFrame(title, true, true, true, true);
        f.getContentPane().add(p);
        Kinsight.KINSIGHT.desktop.add(f);
        f.pack();
        f.setVisible(true);
    }
}
