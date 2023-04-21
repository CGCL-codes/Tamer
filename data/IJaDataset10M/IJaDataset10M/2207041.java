package jdiff;

import java.awt.Color;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.msg.PropertiesChanged;
import jdiff.util.DualDiffUtil;

public class JDiffPlugin extends EBPlugin {

    public static Color overviewChangedColor;

    public static Color overviewDeletedColor;

    public static Color overviewInsertedColor;

    public static Color overviewInvalidColor;

    public static Color leftCursorColor;

    public static Color rightCursorColor;

    static {
        propertiesChanged();
    }

    public JDiffPlugin() {
        super();
    }

    public void start() {
    }

    public void stop() {
    }

    public void handleMessage(EBMessage message) {
        if (message instanceof PropertiesChanged) {
            DualDiffUtil.propertiesChanged();
            JDiffPlugin.propertiesChanged();
        }
    }

    public static void propertiesChanged() {
        overviewChangedColor = GUIUtilities.parseColor(jEdit.getProperty("jdiff.overview-changed-color", "#FFFF90"));
        overviewDeletedColor = GUIUtilities.parseColor(jEdit.getProperty("jdiff.overview-deleted-color", "#FF9090"));
        overviewInsertedColor = GUIUtilities.parseColor(jEdit.getProperty("jdiff.overview-inserted-color", "#D9FF90"));
        overviewInvalidColor = GUIUtilities.parseColor(jEdit.getProperty("jdiff.overview-invalid-color", "#909090"));
        leftCursorColor = jEdit.getColorProperty("jdiff.left-cursor-color", jEdit.getColorProperty("view.caretColor", Color.BLACK));
        rightCursorColor = jEdit.getColorProperty("jdiff.right-cursor-color", jEdit.getColorProperty("view.caretColor", Color.BLACK));
    }
}
