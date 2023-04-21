package Forms.StdComponents;

import Kino;
import Sale.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;

/** besteht aus einer Liste von CatalogDisplayern */
public class CatalogDisplayerBar extends BarComponent {

    /** erzeugt noch leere CatalogDisplayBar */
    public CatalogDisplayerBar() {
        setLayout(new GridLayout(0, 1));
    }

    /** f�gt CatalogDisplayBar einen CatalogDisplayer zu */
    public void addComponent(CatalogDisplayer cd) {
        componentCounter++;
        cd.getList().addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                String helptxt = "";
                Component comp = e.getComponent();
                Enumeration enu = v.elements();
                while (enu.hasMoreElements()) {
                    CatalogDisplayer catD = (CatalogDisplayer) enu.nextElement();
                    if (((Component) catD.getList()) == comp) helptxt = catD.getHelpMsg();
                }
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText(helptxt);
            }

            public void mouseExited(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("");
            }
        });
        v.addElement(cd);
        add(cd);
    }

    /** gibt CatalogDisplayer mit entsprechender ID (Eintrag im Vector) zur�ck */
    public CatalogDisplayer getCatalogDisplayer(int id) {
        return (CatalogDisplayer) v.elementAt(id);
    }

    /** liefert alle selektierten Eintr�ge */
    public String[] getSelectedEntries() {
        String[] s = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            s[i] = getCatalogDisplayer(i).getSelectedEntry();
        }
        return s;
    }
}
