package principal;

import Composite.*;
import Popup.PopupGeneral;
import images.TabsDeDroite;
import java.awt.event.*;
import javax.swing.*;

public class ControleurJListBoutonDroit extends MouseAdapter {

    private JList liste;

    private TabsDeDroite lesTabs;

    private JPopupMenu maPopup;

    public ControleurJListBoutonDroit(JList liste, TabsDeDroite lesTabs) {
        this.lesTabs = lesTabs;
        this.liste = liste;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            int index = liste.locationToIndex(e.getPoint());
            if (index != -1) {
                liste.setSelectedIndex(index);
                Element el = (Element) liste.getModel().getElementAt(index);
                maPopup = PopupGeneral.getPopup(el, lesTabs);
                maPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}
