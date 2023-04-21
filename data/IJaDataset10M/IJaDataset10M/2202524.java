package com.ibm.aglets.tahiti;

import java.awt.Color;
import java.awt.Component;
import java.security.cert.X509Certificate;
import java.util.Locale;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import net.sourceforge.aglets.util.AgletsTranslator;
import net.sourceforge.aglets.util.gui.JComponentBuilder;
import com.ibm.aglet.AgletException;
import com.ibm.aglet.AgletInfo;
import com.ibm.aglet.AgletProxy;
import com.ibm.aglets.AgletProxyImpl;

/**
 * @author Luca Ferrari - cat4hire@users.sourceforge.net
 * 
 *         26/set/07
 */
public class AgletListRenderer extends DefaultListCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 5494221603098422328L;

    /**
     * The translator of this class object.
     */
    private AgletsTranslator translator = null;

    /**
     * The base key used for the translating.
     */
    private String baseKey = this.getClass().getName();

    /**
     * Strings used in the visualization of a row. They are statically set each
     * time a constructor is called, thus to not recall the translate method
     * each time the line must be repainted.
     */
    private static String agletIDString = null;

    private static String creationTimeString = null;

    private static String certificateString = null;

    private static Icon activeIcon = null;

    private static Icon normalIcon = null;

    private static Icon deactivatedIcon = null;

    public AgletListRenderer(AgletListPanel panel) {
        super();
        this.translator = AgletsTranslator.getInstance("tahiti", Locale.getDefault());
        agletIDString = this.translator.translate(this.baseKey + ".agletID");
        certificateString = this.translator.translate(this.baseKey + ".certificate");
        creationTimeString = this.translator.translate(this.baseKey + ".creationTime");
        activeIcon = JComponentBuilder.getIcon(this.baseKey + ".running");
        normalIcon = JComponentBuilder.getIcon(this.baseKey);
        deactivatedIcon = JComponentBuilder.getIcon(this.baseKey + ".deactivated");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        try {
            if (value instanceof AgletProxy) {
                AgletProxy proxy = (AgletProxy) value;
                AgletInfo info = proxy.getAgletInfo();
                StringBuffer buffer = new StringBuffer(500);
                buffer.append(proxy.getAgletClassName());
                buffer.append(" - ");
                buffer.append(agletIDString);
                buffer.append(proxy.getAgletID());
                buffer.append(" - ");
                buffer.append(creationTimeString);
                buffer.append(info.getCreationTime());
                buffer.append(" - ");
                buffer.append(certificateString);
                buffer.append(((X509Certificate) info.getAuthorityCertificate()).getSubjectDN().getName());
                label.setText(buffer.toString());
                if (!isSelected) {
                    label.setForeground(Color.BLUE);
                    label.setBackground(Color.WHITE);
                } else {
                    label.setForeground(Color.GREEN);
                    label.setBackground(Color.BLACK);
                }
                Icon icon = null;
                if (proxy.isActive()) icon = activeIcon; else icon = normalIcon;
                if (icon != null) this.setIcon(icon);
                if (proxy instanceof AgletProxyImpl) label.setToolTipText(((AgletProxyImpl) proxy).toHTMLString());
            }
        } catch (AgletException e) {
            label.setText("!!NO INFORMATION!!");
        }
        return label;
    }
}
