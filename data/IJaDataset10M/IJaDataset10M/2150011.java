package org.wcb.gui.forms;

import org.wcb.model.vo.hibernate.AirportBO;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;

/**
 * <small>
 * Copyright (c)  2006  wbogaardt.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * <p/>
 * $File:  $ <br>
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Mar 29, 2006 2:23:05 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Mar 29, 2006
 *          Time: 2:23:05 PM
 */
public class AirportEntryForm extends JPanel {

    private JTextField jTextFieldICAO;

    private JTextField jTextFieldFAACode;

    private JTextField jTextFieldIATACode;

    private JTextField jTextFieldName;

    private JTextField jTextFieldMunicipal;

    private JTextField jTextFieldStateCounty;

    private JTextField jTextFieldCountry;

    private AirportBO oAirport;

    /**
     * Create Airport Entry Form.
     */
    public AirportEntryForm() {
        initComponents();
    }

    /**
     * Initialize components of this form panel.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        jTextFieldICAO = new JTextField(5);
        jTextFieldFAACode = new JTextField(5);
        jTextFieldIATACode = new JTextField(5);
        jTextFieldName = new JTextField(5);
        jTextFieldMunicipal = new JTextField();
        jTextFieldMunicipal.setPreferredSize(new Dimension(200, 18));
        jTextFieldStateCounty = new JTextField(15);
        jTextFieldCountry = new JTextField(15);
        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        add(new JLabel("Airport"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("ICAO"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldICAO, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("FAA Code"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldFAACode, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("IATA Code"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldIATACode, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("Airport Name"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldName, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("City / Municipality"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldMunicipal, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("State / County"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldStateCounty, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        add(new JLabel("Country"), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldCountry, gridBagConstraints);
    }

    /**
     * This marshalls an object to populate values in the form.
     * @param airport The object to marshall into the form.
     */
    private void marshallToForm(AirportBO airport) {
        this.setICAO(airport.getIcao());
        this.setIATACode(airport.getIata());
        this.setFAACode(airport.getFaa());
        this.setMunicipal(airport.getMunicipal());
        this.setStateCounty(airport.getState());
        this.setName(airport.getName());
        if (airport.getCountry() != null && !airport.getCountry().equalsIgnoreCase("")) {
            this.setCountry(airport.getCountry());
        } else {
            this.setCountry("USA");
        }
    }

    /**
     * This marshals airport information from the form.
     * @param airport The inputed object
     * @return the same object with updated values.
     */
    private AirportBO marshallFromForm(AirportBO airport) {
        airport.setIata(this.getIATACode());
        airport.setIcao(this.getICAO());
        airport.setFaa(this.getFAACode());
        airport.setName(this.getName());
        airport.setMunicipal(this.getMunicipal());
        airport.setCountry(this.getCountry());
        airport.setState(this.getStateCounty());
        return airport;
    }

    /**
     * Takes the airport information and marshalls
     * the information into the form for purposes of editing.
     * If it is a new aiport this would be a new airport value.
     * @param vAirport airport to marshall into the form.
     */
    public void setAirport(AirportBO vAirport) {
        this.oAirport = vAirport;
        this.marshallToForm(this.oAirport);
    }

    /**
     * Access to get the form's airport information If the
     * airport was not set in this class then it is considered a new
     * airport and will get a new object. If it is not new
     * then it will be simply remarshalled with the updated data as in the
     * case of editing a row.
     * @return the airport value object.
     */
    public AirportBO getAirport() {
        if (oAirport == null) {
            oAirport = new AirportBO();
        }
        return marshallFromForm(oAirport);
    }

    /**
     * The ICAO code for the airport.
     * @return  the code for the airport
     */
    public String getICAO() {
        return jTextFieldICAO.getText();
    }

    /**
     * Set the ICAO code of the airport into the form.
     * @param sValue the ICAO Code of the airport
     */
    public void setICAO(String sValue) {
        jTextFieldICAO.setText(sValue);
    }

    /**
     * The FAA code for the airport.
     * @return  the code for the airport
     */
    public String getFAACode() {
        return jTextFieldFAACode.getText();
    }

    /**
     * Set the FAA code of the airport into the form.
     * @param sValue the FAA Code of the airport
     */
    public void setFAACode(String sValue) {
        jTextFieldFAACode.setText(sValue);
    }

    /**
     * The IATA code for the airport.
     * @return  the code for the airport
     */
    public String getIATACode() {
        return jTextFieldIATACode.getText();
    }

    /**
     * Set the IATA code of the airport into the
     * form.
     * @param sValue the IATA Code of the airport
     */
    public void setIATACode(String sValue) {
        jTextFieldIATACode.setText(sValue);
    }

    /**
     * Get the airport name.
     * @return  Name of the airport
     */
    public String getName() {
        return jTextFieldName.getText();
    }

    /**
     * Set the name for the airport in the form.
     * @param sValue name of the airport
     */
    public void setName(String sValue) {
        jTextFieldName.setText(sValue);
    }

    /**
     * Get the city where airport is located.
     * @return  City name
     */
    public String getMunicipal() {
        return jTextFieldMunicipal.getText();
    }

    /**
     * Set the name for the city where airport is located.
     * @param sValue name of the city
     */
    public void setMunicipal(String sValue) {
        jTextFieldMunicipal.setText(sValue);
    }

    /**
     * State where airport is located.
     * @return State/county name
     */
    public String getStateCounty() {
        return jTextFieldStateCounty.getText();
    }

    /**
     * State where airport is located.
     * @param sValue The state.
     */
    public void setStateCounty(String sValue) {
        jTextFieldStateCounty.setText(sValue);
    }

    /**
     * Get the country where airport is located.
     * @return  Country
     */
    public String getCountry() {
        return jTextFieldCountry.getText();
    }

    /**
     * Set the country where airport is located.
     * @param sValue country name
     */
    public void setCountry(String sValue) {
        jTextFieldCountry.setText(sValue);
    }
}
