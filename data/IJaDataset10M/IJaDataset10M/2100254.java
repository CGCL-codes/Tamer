package jvpc.jvpc;

import java.awt.*;
import java.awt.event.*;
import kero.awt.*;
import jvpc.vga.*;

/**
 * @author    Kero van Gelder
 * @copyright Kero van Gelder (the GNU General Public License applies)
 * @since     2 I 2000
 * @version   3 III 2001
 */
public class SettingsWindow extends Dialog implements VGAPlanets, ActionListener {

    InputField findplanet, findship, planetstring, shipstring;

    Map map;

    JVPCConfig config;

    public SettingsWindow(Frame parent, Map map) {
        super(parent);
        this.map = map;
        config = map.applet.getConfig();
        setLayout(new GridLayout(0, 1));
        findplanet = new InputField("find Planet:", "500", false);
        addInputField(findplanet, KeyEvent.VK_P, "selectPlanet");
        planetstring = new InputField("pLanet string:", config.getPlanetString(), false);
        addInputField(planetstring, KeyEvent.VK_L, "setPlanetString");
        findship = new InputField("find Ship:", "500", false);
        addInputField(findship, KeyEvent.VK_S, "selectShip");
        shipstring = new InputField("sHip string:", config.getShipString(), false);
        addInputField(shipstring, KeyEvent.VK_H, "setShipString");
        pack();
        show();
        findplanet.requestFocus();
    }

    private void addInputField(InputField input, int key, String methodname) {
        add(input);
        input.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        InputField source = (InputField) ae.getSource();
        if (source == findplanet) selectPlanet(); else if (source == planetstring) setPlanetString(); else if (source == findship) selectShip(); else if (source == shipstring) setShipString();
        dispose();
    }

    public void selectPlanet() {
        int planetid = Integer.parseInt(findplanet.getText());
        if ((planetid < 1) || (planetid > planets)) {
            return;
        } else {
            Planet planet = map.universe.getPlanet(planetid);
            if (planet == null) return;
            map.selected = planet;
            map.centerSelected();
        }
    }

    public void setPlanetString() {
        map.setPlanetString(planetstring.getText());
    }

    public void selectShip() {
        int shipid = Integer.parseInt(findship.getText());
        if ((shipid < 1) || (shipid > ships)) {
            return;
        } else {
            Ship ship = map.universe.getShip(shipid);
            if (ship == null) return;
            map.selected = ship;
            map.centerSelected();
        }
    }

    public void setShipString() {
        map.setShipString(shipstring.getText());
    }
}
