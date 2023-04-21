package infonode;

import java.io.File;
import javax.swing.JFileChooser;
import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.gui.DockableWindowFactory;

public class Plugin extends EditPlugin {

    public static final String NAME = "InfoNodeDW";

    public static final String OPTION_PREFIX = "options.infonode.";

    public static void doStart(View view) {
        WindowManager wm = new WindowManager();
        view.getDockableWindowManager().close();
        wm.construct(view, DockableWindowFactory.getInstance(), view.getViewConfig());
        view.setDockableWindowManager(wm);
    }

    public static void load(View view) {
        JFileChooser fc = new JFileChooser(new File(getConfigDirectory()));
        fc.showOpenDialog(view);
        File sel = fc.getSelectedFile();
        if (sel == null) return;
        WindowManager wm = (WindowManager) view.getDockableWindowManager();
        wm.load(sel.getAbsolutePath());
    }

    public static void save(View view) {
        JFileChooser fc = new JFileChooser(new File(getConfigDirectory()));
        fc.showOpenDialog(view);
        File sel = fc.getSelectedFile();
        if (sel == null) return;
        WindowManager wm = (WindowManager) view.getDockableWindowManager();
        wm.save(sel.getAbsolutePath());
    }

    public static String getConfigDirectory() {
        String dir = jEdit.getSettingsDirectory() + File.separator + "InfoNodeDW";
        File f = new File(dir);
        if (!f.exists()) f.mkdir();
        return dir;
    }

    public static void selectTheme(View view) {
        WindowManager wm = (WindowManager) view.getDockableWindowManager();
        wm.selectTheme();
    }
}
