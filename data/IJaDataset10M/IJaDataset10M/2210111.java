package org.jpos.ui.factory;

import org.jdom.Element;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.gui.ISOChannelPanel;
import org.jpos.iso.gui.ISOMeter;
import org.jpos.ui.UI;
import org.jpos.ui.UIFactory;
import org.jpos.util.NameRegistrar;
import javax.swing.*;
import java.util.Observable;

/**
 * @author Alejandro Revilla
 *
 * Creates an ISOMeter component
 * i.e:
 * <pre>
 *  &lt;iso-meter idref="id" scroll="true|false" refresh="nnn"/&gt
 * </pre>
 * @see org.jpos.ui.UIFactory
 */
public class ISOMeterFactory implements UIFactory {

    public JComponent create(UI ui, Element e) {
        ISOChannelPanel icp = null;
        try {
            Object obj = (Object) NameRegistrar.get(e.getAttributeValue("idref"));
            if (obj instanceof ISOChannel) {
                icp = new ISOChannelPanel((ISOChannel) obj, e.getText());
            } else if (obj instanceof Observable) {
                icp = new ISOChannelPanel(e.getText());
                ((Observable) obj).addObserver(icp);
            }
            ISOMeter meter = icp.getISOMeter();
            if ("false".equals(e.getAttributeValue("scroll"))) meter.setScroll(false);
            String protect = e.getAttributeValue("protect");
            if (protect != null) icp.setProtectFields(ISOUtil.toIntArray(protect));
            String wipe = e.getAttributeValue("wipe");
            if (wipe != null) icp.setWipeFields(ISOUtil.toIntArray(wipe));
            String refresh = e.getAttributeValue("refresh");
            if (refresh != null) meter.setRefresh(Integer.parseInt(refresh));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JLabel(ex.getMessage());
        }
        return icp;
    }
}
