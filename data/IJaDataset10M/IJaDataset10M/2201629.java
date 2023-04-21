package Forms.StdComponents;

import Kino;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

/** enth�lt eine Liste von Buttons */
public class ButtonBar extends BarComponent {

    /** erzeugt eine noch leere ButtonBar */
    public ButtonBar() {
        GridLayout grid = new GridLayout(0, 1);
        setLayout(grid);
    }

    /** f�gt einer ButtonBar einen neuen Button zu */
    public void addButton(String name, String help, Action a) {
        componentCounter++;
        ButtonBarButton b = new ButtonBarButton(name, help);
        b.setActionCommand(name);
        a.setCommand(name);
        b.addActionListener(a);
        b.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                String helptxt = "";
                Component comp = e.getComponent();
                Enumeration enu = v.elements();
                while (enu.hasMoreElements()) {
                    ButtonBarButton button = (ButtonBarButton) enu.nextElement();
                    if (button == comp) helptxt = button.getHelpMsg();
                    {
                        if (Kino.curHelpLine != null) Kino.curHelpLine.setText(helptxt);
                    }
                }
            }

            public void mouseExited(MouseEvent e) {
                if (Kino.curHelpLine != null) Kino.curHelpLine.setText("");
            }
        });
        v.addElement(b);
        add(b);
    }

    /** - deaktiviert einen Button  <br>
      - name - Label des Buttons */
    public void disableButton(String name) {
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            ButtonBarButton b = (ButtonBarButton) e.nextElement();
            if (b.getLabel() == name) b.setEnabled(false);
        }
    }

    /** - aktiviert einen deaktivierten Button <br>
      - name - Label des Buttons */
    public void enableButton(String name) {
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            ButtonBarButton b = (ButtonBarButton) e.nextElement();
            if (b.getLabel() == name) b.setEnabled(true);
        }
    }

    /** deaktiviert alle Buttons */
    public void disableAll() {
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            ButtonBarButton b = (ButtonBarButton) e.nextElement();
            b.setEnabled(false);
        }
    }

    /** aktiviert alle Buttons */
    public void enableAll() {
        Enumeration e = v.elements();
        while (e.hasMoreElements()) {
            ButtonBarButton b = (ButtonBarButton) e.nextElement();
            b.setEnabled(true);
        }
    }
}
