package net.sourceforge.hourglass.swingui;

import java.awt.Component;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.sourceforge.hourglass.framework.DateUtilities;
import net.sourceforge.hourglass.framework.Project;
import net.sourceforge.hourglass.framework.HourglassPreferences;
import net.sourceforge.hourglass.framework.Prefs;

/**
 * Renders projects in a tree by displaying their names.
 *
 * @author Mike Grant
 */
public class ProjectTreeCellRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof Project) {
            Project p = (Project) value;
            String name;
            if (p.equals(tree.getModel().getRoot())) {
                name = gu().getString(Strings.ROOT_NO_PARENT);
            } else {
                name = getProperString(p.getName(), Strings.NO_NAME);
            }
            JComponent result = (JComponent) super.getTreeCellRendererComponent(tree, name, sel, expanded, leaf, row, hasFocus);
            result.setToolTipText(createToolTipText(p));
            return result;
        } else {
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }

    private String createToolTipText(Project p) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><strong>");
        sb.append(getProperString(p.getName(), Strings.NO_NAME)).append("</strong>");
        sb.append("<br>");
        sb.append(getProperString(p.getDescription(), Strings.NO_DESCRIPTION));
        sb.append("<br>");
        sb.append(gu().getString(Strings.TODAY_COLON) + " " + formatTime(getTime(p, p.getTimeSince(DateUtilities.getBeginningOfDay(new Date()), true))));
        sb.append(" - " + gu().getString(Strings.TOTAL_COLON) + " " + formatTime(getTime(p, p.getTotalTime(true))));
        sb.append("<html>");
        return sb.toString();
    }

    private long getTime(Project p, long time) {
        long now = System.currentTimeMillis();
        ClientState cs = ClientState.getInstance();
        if (cs.isRunning() && p.equals(cs.getSelectedProject())) {
            long elapsed = now - cs.getOpenTimeSpanStartMillis();
            return time + elapsed;
        } else {
            return time;
        }
    }

    /**
   * Formats the given string for display in the GUI, by giving it a default
   * value of "(no value)" and reducing its size to 40 characters.
   */
    private String getProperString(String fullString, String what) {
        if (fullString == null || gu().isAllWhitespace(fullString)) {
            return "(" + gu().getString(what) + ")";
        } else {
            return gu().chopString(fullString, gp().getInt(Prefs.TOOLTIP_CHOP_WIDTH), gp().getString(Prefs.TOOLTIP_CHOP_STRING));
        }
    }

    private String formatTime(long ms) {
        long hours = ms / MS_PER_HOUR;
        long minutes = (ms - hours * MS_PER_HOUR) / MS_PER_MINUTE;
        return ((hours < 10) ? "0" : "") + hours + ":" + ((minutes < 10) ? "0" : "") + minutes;
    }

    private static Utilities gu() {
        return Utilities.getInstance();
    }

    private static HourglassPreferences gp() {
        return HourglassPreferences.getInstance();
    }

    private static final long MS_PER_MINUTE = 60000;

    private static final long MS_PER_HOUR = MS_PER_MINUTE * 60;
}
