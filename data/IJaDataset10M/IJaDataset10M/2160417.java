package ge.telasi.tasks.ui;

import ge.telasi.tasks.TaskException;
import ge.telasi.tasks.ui.comp.CommonDialog;
import ge.telasi.tasks.ui.log.LoggerUtils;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * @author dimitri
 */
public class UIUtils {

    public static Window findShell(Component comp) {
        Component mycomp = comp;
        while (mycomp != null) {
            if (mycomp instanceof Window) {
                return (Window) mycomp;
            }
            mycomp = mycomp.getParent();
        }
        return null;
    }

    public static void openDialog(CommonDialog dialog) {
        try {
            if (dialog.getParent() == null) {
                throw new TaskException("parent not defined");
            }
            centerDialog(dialog);
            dialog.setVisible(true);
        } catch (Exception ex) {
            LoggerUtils.manageException(dialog, ex);
        }
    }

    /**
     * Place dialog in the center of it's parent.
     */
    private static void centerDialog(JDialog dialog) {
        centerDialog(dialog, dialog.getParent());
    }

    /**
     * Center dialog relative to the given component.
     * @direction &gt; 0 slightly up, &lt; 0 slightly down, = 0 exactly at center
     */
    private static void centerDialog(JDialog dialog, Component comp) {
        Dimension size = dialog.getSize();
        Dimension parentSize;
        Point parentPosition;
        if (comp != null && comp.isVisible()) {
            parentSize = comp.getSize();
            parentPosition = comp.getLocationOnScreen();
        } else if (dialog.getParent() != null && dialog.getParent().isVisible()) {
            parentSize = dialog.getParent().getSize();
            parentPosition = dialog.getParent().getLocation();
        } else {
            parentSize = Toolkit.getDefaultToolkit().getScreenSize();
            parentPosition = new Point();
        }
        int x = parentPosition.x + parentSize.width / 2 - size.width / 2;
        int y = parentPosition.y + parentSize.height / 2 - size.height / 2;
        dialog.setLocation(x, y);
    }

    /**
     * Place component new the field.
     */
    public static void placeNearField(JDialog dialog, JComponent comp) {
        Point loc = comp.getLocationOnScreen();
        Dimension size = comp.getSize();
        int x = loc.x;
        int y = loc.y + size.height;
        dialog.setLocation(x, y);
    }

    public static boolean askConfirmation(Component parent, String message) {
        JLabel label = new JLabel(message);
        int resp = JOptionPane.showConfirmDialog(findShell(parent), label, "დადასტურება", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return resp == JOptionPane.YES_OPTION;
    }

    public static void showMessage(Component parent, String message) {
        JLabel label = new JLabel(message);
        JOptionPane.showMessageDialog(findShell(parent), label, "ინფორმაცია", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void setPreferredWidth(JComponent comp, int width) {
        Dimension size = comp.getPreferredSize();
        Dimension newSize = new Dimension(width, size.height);
        comp.setPreferredSize(newSize);
    }

    public static Font makeBold(Font font) {
        Font boldFont = new Font(font.getName(), Font.BOLD, font.getSize());
        return boldFont;
    }

    public static Font makePlain(Font font) {
        Font plainFont = new Font(font.getName(), Font.PLAIN, font.getSize());
        return plainFont;
    }

    public static Font makeItalic(Font font) {
        Font boldFont = new Font(font.getName(), Font.ITALIC, font.getSize());
        return boldFont;
    }

    public static Font changeSize(Font font, int diff) {
        int newSize = font.getSize() + diff;
        if (newSize < 6) {
            newSize = 6;
        }
        return new Font(font.getName(), font.getStyle(), newSize);
    }

    public static void correctTitleBorderFont(JPanel panel) {
        if (panel.getBorder() instanceof TitledBorder) {
            TitledBorder border = (TitledBorder) panel.getBorder();
            correctTitleBorderFont(border);
        }
    }

    public static void correctTitleBorderFont(TitledBorder border) {
        Font oldFont = border.getTitleFont();
        border.setTitleFont(new Font(FontManagement.FAMILY, oldFont.getStyle(), oldFont.getSize()));
    }

    public static void correctLabelFont(JLabel label) {
        Font oldFont = label.getFont();
        label.setFont(new Font(FontManagement.FAMILY, oldFont.getStyle(), oldFont.getSize()));
    }

    public static ImageIcon getIcon16x16(String imgName) {
        try {
            return new ImageIcon(UIUtils.class.getResource("/ge/telasi/resorces/icons16x16/" + imgName));
        } catch (NullPointerException nex) {
            return null;
        }
    }

    public static ImageIcon getIcon48x48(String imgName) {
        try {
            return new ImageIcon(UIUtils.class.getResource("/ge/telasi/resorces/icons48x48/" + imgName));
        } catch (NullPointerException nex) {
            return null;
        }
    }

    public static ImageIcon getIcon128x128(String imgName) {
        try {
            return new ImageIcon(UIUtils.class.getResource("/ge/telasi/resorces/icons128x128/" + imgName));
        } catch (NullPointerException nex) {
            return null;
        }
    }

    public static void showWaitCursor(Component comp) {
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public static void showCommonCursor(Component comp) {
        comp.setCursor(Cursor.getDefaultCursor());
    }
}
