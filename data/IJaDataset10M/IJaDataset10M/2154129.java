package com.openbravo.pos.ticket;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;

/**
 *
 * @author Mikel Irurita
 */
public class FindTicketsRenderer extends DefaultListCellRenderer {

    private Icon icoTicketNormal;

    private Icon icoTicketRefund;

    public static final int RECEIPT_NORMAL = 0;

    /** Creates a new instance of ProductRenderer */
    public FindTicketsRenderer() {
        this.icoTicketNormal = new ImageIcon(getClass().getClassLoader().getResource("com/openbravo/images/pay.png"));
        this.icoTicketRefund = new ImageIcon(getClass().getClassLoader().getResource("com/openbravo/images/refund.png"));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        int ticketType = ((FindTicketsInfo) value).getTicketType();
        setText("<html><table>" + value.toString() + "</table></html>");
        if (ticketType == RECEIPT_NORMAL) {
            setIcon(icoTicketNormal);
        } else {
            setIcon(icoTicketRefund);
        }
        return this;
    }
}
