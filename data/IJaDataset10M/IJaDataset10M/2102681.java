package org.ezim.ui;

import java.awt.Component;
import java.net.InetAddress;
import java.net.NetworkInterface;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class EzimLocalAddressListRenderer extends JLabel implements ListCellRenderer {

    /**
	 * construct a list cell renderer for local address display
	 */
    public EzimLocalAddressListRenderer() {
    }

    /**
	 * return a component that has been configured to display the specified
	 * value
	 * @param jlstIn the JList we're painting
	 * @param objIn value returned by list.getModel().getElementAt(index)
	 * @param iIdx the cells index
	 * @param blnSelected true if the specified cell was selected
	 * @param blnCellHasFocus true if the specified cell has the focus
	 */
    public Component getListCellRendererComponent(JList jlstIn, Object objIn, int iIdx, boolean blnSelected, boolean blnCellHasFocus) {
        InetAddress iaTmp = (InetAddress) objIn;
        StringBuilder sbTmp = new StringBuilder(iaTmp.getHostAddress());
        NetworkInterface niTmp = null;
        try {
            niTmp = NetworkInterface.getByInetAddress(iaTmp);
            if (niTmp != null && niTmp.getName() != null && niTmp.getName().length() > 0) {
                sbTmp.insert(0, ") ");
                sbTmp.insert(0, niTmp.getName());
                sbTmp.insert(0, "(");
            }
        } catch (Exception e) {
            niTmp = null;
        }
        this.setText(sbTmp.toString());
        this.setEnabled(jlstIn.isEnabled());
        this.setFont(jlstIn.getFont());
        this.setOpaque(true);
        return this;
    }
}
