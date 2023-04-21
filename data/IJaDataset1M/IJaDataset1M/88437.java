package org.silicolife.util.xls2sql.gui.io;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import org.silicolife.gui.img.Images;
import org.silicolife.util.xls2sql.gui.DesktopPane;
import org.silicolife.util.xls2sql.gui.LabeledIconPanel;
import org.silicolife.util.xls2sql.gui.sql.DatabaseConnectionPanel;

public class ExcelFilePanel extends LabeledIconPanel {

    private static final long serialVersionUID = 1L;

    public ExcelFilePanel(DesktopPane desktop, String text, String toolTip, String key) {
        super(desktop, text, toolTip, key, Images.excelFileImage);
    }

    public static ExcelFilePanel createExcelFilePanel(DesktopPane desktop, String text, String toolTip, String key) {
        ExcelFilePanel panel = new ExcelFilePanel(desktop, text, toolTip, key);
        return panel;
    }

    public void mouseReleased(MouseEvent event) {
        Component c = event.getComponent();
        Component d = getDesktop().getComponentAt(c.getLocation());
        System.out.println(c);
        System.out.println(d);
    }
}
