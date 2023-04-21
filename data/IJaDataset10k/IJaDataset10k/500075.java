package ctagsinterface.main;

import java.net.URL;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import org.gjt.sp.jedit.jEdit;

public class KindIconProvider {

    static Hashtable<String, ImageIcon> icons = new Hashtable<String, ImageIcon>();

    public static final String ICONS = "options.CtagsInterface.icons.kind.";

    public static ImageIcon getIcon(String kind) {
        ImageIcon icon = (kind == null) ? null : icons.get(kind);
        if (icon == null) {
            String iconName = jEdit.getProperty(ICONS + kind);
            if (iconName == null || iconName.length() == 0) iconName = "unknown.png";
            URL url = Tag.class.getClassLoader().getResource("icons/" + iconName);
            try {
                icon = new ImageIcon(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (icon != null) icons.put(kind, icon);
        }
        if (icon != null) return icon;
        return null;
    }

    public ImageIcon getIcon(Tag tag) {
        return getIcon(tag.getKind());
    }
}
