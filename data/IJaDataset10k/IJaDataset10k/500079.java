package ro.masc.server.gui;

import com.ibm.aglet.AgletProxy;
import com.ibm.aglet.InvalidAgletException;
import javax.swing.*;

/**
 * Description
 *
 * @author <a href="mailto:andrei.chiritescu@gmail.com">Andrei Chiritescu</a>
 * @version $Revision: 1.2 $
 *          $Date: 2005/05/08 21:04:25 $
 */
public class InfoWindow extends JTextArea {

    AgletProxy proxy;

    public InfoWindow(AgletProxy proxy) throws InvalidAgletException {
        this.proxy = proxy;
        setRows(15);
        setColumns(40);
        this.setName(proxy.getAgletClassName() + " : " + proxy.getAgletID());
        setEditable(false);
        setFocusable(true);
        setAutoscrolls(true);
    }

    public AgletProxy getProxy() {
        return proxy;
    }

    public boolean equalsTo(AgletProxy proxy) {
        try {
            return this.proxy.getAgletID().equals(proxy.getAgletID());
        } catch (InvalidAgletException e) {
            return false;
        }
    }
}
