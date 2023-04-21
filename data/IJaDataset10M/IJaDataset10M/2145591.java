package pspdash;

import java.util.Enumeration;
import javax.swing.UIManager;

class LookAndFeelSettings {

    public static void loadLocalizedSettings() {
        try {
            Resources r = Resources.getDashBundle("pspdash.LookAndFeelSettings");
            Enumeration e = r.getKeys();
            while (e.hasMoreElements()) {
                String dest = (String) e.nextElement();
                String src = r.getString(dest);
                UIManager.put(dest, UIManager.get(src));
            }
        } catch (Exception e) {
        }
    }
}
