package org.wcb.autohome.util.ui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.wcb.autohome.AutoHomeAdminSession;
import org.wcb.autohome.interfaces.X10DeviceConstants;
import org.wcb.autohome.interfaces.IX10Module;

/**
 * Filename:    $Id: LightRender.java,v 1.5 2004/07/22 03:06:50 wbogaardt Exp $
 *
 * Abstract:  This class renders an icon on a jtable cell. It helps to have
 *             a graphical representation of the item rather than just text.
 *
 *  $Log: LightRender.java,v $
 *  Revision 1.5  2004/07/22 03:06:50  wbogaardt
 *  removed deprecated method calls.
 *
 *  Revision 1.4  2004/02/27 01:29:53  wbogaardt
 *  modified classes so they conform to Checkstyle format in readability
 *
 *  Revision 1.3  2004/01/21 05:29:43  wbogaardt
 *  fixed bug saving file format and added disable monitoring
 *
 *  Revision 1.2  2004/01/16 00:53:43  wbogaardt
 *  Fixed a very obscure bug with the Macro Panel that it didn't added new
 *  x10 devices to the drop down of available x10 device for the macro. Modified Macro triggers to change the events
 * to integer verses strings cleaner this way.
 *
 *  Revision 1.1  2004/01/15 21:18:25  wbogaardt
 *  moved light render from util directory to sub ui directory package
 *
 *  Revision 1.4  2004/01/15 21:05:20  wbogaardt
 *  major revamp of Modules and interfaces changes overall structure of how information is stored
 *
 *  Revision 1.3  2003/12/22 20:51:32  wbogaardt
 *  refactored name assignments and formatted code for readability.
 *
 *  Revision 1.2  2003/12/13 05:36:52  wbogaardt
 *  fixed javadoc comments.
 *
 */
public class LightRender extends DefaultTableCellRenderer implements X10DeviceConstants {

    private ImageIcon onBulb = AutoHomeAdminSession.LIGHTICON;

    private ImageIcon offBulb = AutoHomeAdminSession.LIGHTICON_OFF;

    private ImageIcon onPlug = AutoHomeAdminSession.APPLIANCEICON;

    private ImageIcon offPlug = AutoHomeAdminSession.APPLIANCEICON_OFF;

    private ImageIcon timeIcon = AutoHomeAdminSession.CLOCKICON;

    private ImageIcon eventIcon = AutoHomeAdminSession.CAUTIONICON;

    /**
    * This creates a cell render that shows different icons instead of text
    * on a JTable.
    */
    public LightRender() {
        setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Returns the default table cell renderer.
     *
     * @param table the JTable
     * @param value the value to assign to the cell at [row, column]
     * @param isSelected true if cell is selected
     * @param hasFocus true if cell has focus
     * @param row the row of the cell to render
     * @param col the column of the cell to render
     * @return the customized default table cell renderer
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int s = 0;
        IX10Module idevice = (IX10Module) value;
        s = idevice.getType();
        super.setText(idevice.getFullDeviceCode());
        switch(s) {
            case LAMP_MODULE_ON:
                super.setIcon(onBulb);
                return this;
            case APPLIANCE_MODULE_ON:
                super.setIcon(onPlug);
                return this;
            case TIMER_EVENT:
                super.setIcon(timeIcon);
                return this;
            case TRIGGER_EVENT:
                super.setIcon(eventIcon);
                return this;
            default:
                return this;
        }
    }
}
