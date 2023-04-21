package mou.gui.shipdesignscreen;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mou.core.ship.ShipClass;
import mou.gui.GUI;

/**
 * Klasse zeig kurzgefassene Eigenschaften einer ShipClass
 * 
 * @author pbu
 */
public class ShipClassInfoPanel extends JPanel {

    private JLabel labelReichweite = new JLabel("0");

    private JLabel labelWaffenstaerke = new JLabel("0");

    private JLabel labelPanzerungstaerke = new JLabel("0");

    private JLabel labelSchild = new JLabel("0");

    private JLabel labelCrew = new JLabel("0");

    private JLabel labelLebenserhaltung = new JLabel("0");

    private JLabel labelMasse = new JLabel("0");

    private JLabel labelBuildCost = new JLabel("0");

    private JLabel labelSupportCost = new JLabel("0");

    private JLabel labelEnergy = new JLabel("0");

    /**
	 * 
	 */
    public ShipClassInfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Box boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Masse: "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelMasse);
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Geschwindigkeit : "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelReichweite);
        boxX.add(new JLabel(" Lj/Tag"));
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Besatzung : "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelCrew);
        boxX.add(new JLabel(" Mann"));
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Lebenserhaltung f�r: "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelLebenserhaltung);
        boxX.add(new JLabel(" Mann"));
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Waffenst�rke: "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelWaffenstaerke);
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Panzerung : "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelPanzerungstaerke);
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Schutzschild : "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelSchild);
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Baukosten : "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelBuildCost);
        boxX.add(new JLabel(" Credits"));
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Instandhaltung: "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelSupportCost);
        boxX.add(new JLabel(" Credits"));
        boxX = new Box(BoxLayout.X_AXIS);
        add(boxX);
        boxX.add(new JLabel("Energie: "));
        boxX.add(Box.createHorizontalGlue());
        boxX.add(labelEnergy);
        boxX.add(new JLabel(" MW"));
    }

    public void showShipClass(ShipClass shipImpl) {
        shipImpl.computeWerte();
        labelPanzerungstaerke.setText(GUI.formatLong((long) shipImpl.getPanzer()));
        labelReichweite.setText(GUI.formatDouble(shipImpl.getSpeed()));
        labelCrew.setText((GUI.formatLong((long) shipImpl.getCrew())));
        labelSchild.setText(GUI.formatLong((long) shipImpl.getSchild()));
        labelWaffenstaerke.setText(GUI.formatLong((long) shipImpl.getWaffenstarke()));
        labelMasse.setText(GUI.formatLong((long) shipImpl.getMasse()));
        labelBuildCost.setText(GUI.formatLong((long) shipImpl.getBuildCost()));
        labelSupportCost.setText(GUI.formatLong((long) shipImpl.getSupportCost()));
        labelLebenserhaltung.setText(GUI.formatLong((long) shipImpl.getLebenserhaltung()));
        labelEnergy.setText(GUI.formatLong((long) shipImpl.getEnergie()));
    }

    /**
	 * Setzt alle Labels auf Leerwerte
	 */
    public void reset() {
        labelPanzerungstaerke.setText("----");
        labelReichweite.setText("----");
        labelCrew.setText("----");
        labelSchild.setText("----");
        labelWaffenstaerke.setText("----");
        labelMasse.setText("----");
        labelBuildCost.setText("----");
        labelSupportCost.setText("----");
        labelLebenserhaltung.setText("----");
    }
}
