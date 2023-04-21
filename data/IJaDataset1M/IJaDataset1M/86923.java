package com.bbn.feupdater.gui;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.*;
import jfireeagle.Address;
import jfireeagle.CellTower;
import jfireeagle.GpsCoordinate;
import jfireeagle.LocationParameters;
import jfireeagle.WhereOnEarthId;

public class LocationParametersPanel extends JPanel {

    private JTextField longitude = new JTextField("", 1);

    private JTextField latitude = new JTextField("", 1);

    private JTextField address = new JTextField("", 1);

    private JTextField postal = new JTextField("", 1);

    private JTextField city = new JTextField("", 1);

    private JTextField state = new JTextField("", 1);

    private JTextField country = new JTextField("", 1);

    private JTextField woeid = new JTextField("", 1);

    private JTextField mnc = new JTextField("", 1);

    private JTextField mcc = new JTextField("", 1);

    private JTextField lac = new JTextField("", 1);

    private JTextField cellid = new JTextField("", 1);

    public LocationParametersPanel() {
        super();
        this.setLayout(new GridLayout(12, 2));
        addField("Latitude", latitude);
        addField("Longitude", longitude);
        addField("Address", address);
        addField("Postal code", postal);
        addField("City", city);
        addField("State", state);
        addField("Country", country);
        addField("WOEID", woeid);
        addField("MNC", mnc);
        addField("MCC", mcc);
        addField("LAC", lac);
        addField("Cell Id", cellid);
    }

    protected void addField(String label, JComponent c) {
        add(new JLabel(label));
        add(c);
    }

    public LocationParameters getLocationParameters() {
        LocationParameters loc = new LocationParameters();
        if (hasText(latitude) && hasText(longitude)) {
            GpsCoordinate coordinate = new GpsCoordinate(latitude.getText().trim(), longitude.getText().trim());
            loc.setGpsCoordinate(coordinate);
        }
        if (hasText(address) || hasText(city) || hasText(state) || hasText(country) || hasText(postal)) {
            Address a = new Address();
            a.setStreetAddress(address.getText().trim());
            a.setCity(city.getText().trim());
            a.setState(state.getText().trim());
            a.setCountry(country.getText().trim());
            a.setPostalCode(postal.getText().trim());
            loc.setAddress(a);
        }
        if (hasText(woeid)) {
            loc.setWhereOnEarthId(new WhereOnEarthId(woeid.getText()));
        }
        if (hasText(mnc) || hasText(mcc) || hasText(lac) || hasText(cellid)) {
            CellTower tower = new CellTower();
            tower.setMnc(new Integer(mnc.getText()));
            tower.setMcc(new Integer(mcc.getText()));
            tower.setLac(new Integer(lac.getText()));
            tower.setCellid(new Integer(cellid.getText()));
            loc.setCellTower(tower);
        }
        return loc;
    }

    public void clear() {
        Component[] components = this.getComponents();
        for (Component c : components) {
            if (c instanceof JTextField) {
                JTextField f = (JTextField) c;
                f.setText("");
            }
        }
    }

    protected boolean hasText(JTextField field) {
        return !isEmpty(field);
    }

    protected boolean isEmpty(JTextField field) {
        if (field.getText() == null) {
            return true;
        } else if (field.getText().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
