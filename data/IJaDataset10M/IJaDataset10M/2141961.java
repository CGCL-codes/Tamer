package org.wcb.gui.forms;

import org.wcb.model.vo.hibernate.AircraftTypeBO;
import org.wcb.resources.MessageResourceRegister;
import org.wcb.resources.MessageKey;
import org.wcb.gui.component.JXTitlePanel;
import org.jdesktop.swingx.border.DropShadowBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Insets;

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
 * $Change:  $ submitted by $Author: wbogaardt $ at $DateTime: Mar 27, 2006 3:02:31 PM $ <br>
 * </small>
 *
 * @author wbogaardt
 * @version 1
 *          Date: Mar 27, 2006
 *          Time: 3:02:31 PM
 */
public class AircraftTypeEntryForm extends JXTitlePanel {

    private JTextField jTextFieldModel;

    private JTextField jTextFieldShortName;

    private JComboBox comboBoxManufacture;

    private JComboBox comboTypeBox;

    private AircraftTypeBO oAircraftType;

    /**
     * Default constructor for aircraft type entry form.
     */
    public AircraftTypeEntryForm() {
        super("AircraftBO Type", new Color(0x522aec));
        this.setBorder(new CompoundBorder(new DropShadowBorder(Color.BLACK, 0, 5, .5f, 12, true, true, true, true), this.getBorder()));
        initComponents();
    }

    /**
     * Initialize componets.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        jTextFieldModel = new JTextField();
        comboBoxManufacture = new JComboBox();
        jTextFieldShortName = new JTextField();
        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(0, 3, 0, 3);
        add(new JLabel(MessageResourceRegister.getInstance().getValue(MessageKey.LABEL_MANUFACTURE)), gridBagConstraints);
        comboBoxManufacture.setModel(new DefaultComboBoxModel(new String[] { "Aerimacchi", "Aero Commander", "Aero Vodochody", "Aerocar", "Aeronca", "Aerospatiale", "Aerospatiale-Matra", "Airbus", "Airco", "Airspeed", "American Aviation", "AMD", "American Champion", "Antonov", "Arado", "Armstrong-Whitworth", "Augusta", "Auster", "Avia Bellanca", "Aviat", "Aviation Traders", "Avro", "Avro Canada", "Bachem", "BAE Systems", "BAMC", "Bayerische Flugzeugwerke", "Beagle", "Beechcraft", "Bell", "Bell Helicopter", "Bellanca", "Beriev", "Blackburn", "Bl'roit", "Blohm und Voss", "Boeing", "B'lkow", "Bombardier Aerospace", "Boulton Paul", "Brantly", "Breguet", "Brewster", "Bristol Aeroplane Company", "British Aerospace", "British AircraftBO Corportation", "Britten-Norman", "Cessna", "Champion", "Chilton", "Chrislea", "Cirrus Design", "Comper", "Consolidated", "Consair", "Culver", "Curtis-Wright", "Dassult Aviation", "de Havilland", "Diamond AircraftBO", "Dornier", "Douglas", "Druine", "Eagle", "EADS", "Eclipse", "Edgley", "Embraer", "English Electric", "Enstrom", "Ercoupe", "Eurocopter", "EXPERIMENTAL", "Extra Flugzeugbau", "Fairchild", "Found AircraftBO Co", "Fairchild Dornier", "Fairey", "Farman", "Fiat", " Fiesler", "Focke Achgelis", "Focke-Wulf", "Fokker", "Folland", "Ford", "Fuji", "General AircraftBO Factory", "General Dynamics", "Gloster", "Grahame-White", "Grob", "Grumman", "Gulfstream Aerospace", "Great Lakes", "Grumman American", "Hamburger Flugzeugbau(HFB)", "Handley-Page", "Harbin", "Hawker", "Hawker Pacific-Aerospace", "Hawker Siddeley Co", "Heinkel", "Helio", "Henschel", "Hiller", "Hindustan Aeronautics", "Hughes", "Hunting", "IAR", "Ikarus", "Llyushin", "Israeli AircraftBO Industries", "Junkers", "Kamen", "Kamov", "Kawasaki", "Lake", "Lancair", "Lancashire", "Laverda", "Lockheed Martin", "Loening Aeronautical", "Luscombe", "Luton", "Martin", "Martin-Baker", "Martinsyde", "Maule", "McDonnel-Douglass", "Messershmitt", "MiG", "Mil", "Miles", "Meyers", "Midwest Aerosport", "Mitsubishi", "Mooney", "Murphy", "NAMC", "North American", "Northrop", "Northrop-Grumman", "Navion", "Panavia", "Parnall", "Partenavia", "Percival", "Piaggio Aero", "Pilatus", "Piper", "Pitts", "PZL", "Raytheon", "Rearwin", "Reims", "Republic", "Robin", "Robinson", "Rockwell", "Rotorway", "Rutan", "Saab", "Saunders-Roe", "Scaled Composites", "Schweizer", "Scottish Aviation", "SEPECAT", "Shorts", "Simulator", "Siai Marchetti", "Siebel", "Sikorsky", "Slingsby", "Socata", "Soko", "Sopwith", "SPAD", "Spartan", "Stemme", "Stinson", "Supermarine", "Swearingen", "Symphony AircraftBO", "Taylorcraft", "Technovia", "Temco", "Tiger AircraftBO", "Thomas-Martin", "Trago Mills", "Travelair", "Tupolev", "Udet-Flugzeugbau", "Ulta", "Varga", "Vickers", "Vickers-Armstrong", "Waco", "Westland", "Wright Aeronautical", "Yakovlev" }));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(comboBoxManufacture, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(new JLabel(MessageResourceRegister.getInstance().getValue(MessageKey.LABEL_MODEL)), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(jTextFieldModel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(new JLabel(MessageResourceRegister.getInstance().getValue(MessageKey.LABEL_SHORTFORM)), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(jTextFieldShortName, gridBagConstraints);
        JLabel label4 = new JLabel("(Ex: C-150M, PA-28)");
        label4.setForeground(new Color(0, 68, 182));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 5, 0, 5);
        add(label4, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        add(new JLabel("Characteristics"), gridBagConstraints);
        comboTypeBox = new JComboBox();
        comboTypeBox.setModel(new DefaultComboBoxModel(new String[] { "Airplane", "Rotocraft", "Powered Lift", "Glider", "Lighter-Than-Air", "Simulator", "Training Device", "PCATD" }));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(comboTypeBox, gridBagConstraints);
    }

    /**
     * This calls a private method which marshalls the
     * object into the form fields.
     * @param oType Object to set in the form.
     */
    public void setAircraftType(AircraftTypeBO oType) {
        this.oAircraftType = oType;
        this.marshallToForm(this.oAircraftType);
    }

    /**
     * This gets an aircraft type but this calls a private
     * class marshallToForm and returns the updated aircraft type object
     * from the form.
     * @return The updated aircraft type object.
     */
    public AircraftTypeBO getAircraftType() {
        if (oAircraftType == null) {
            oAircraftType = new AircraftTypeBO();
        }
        return this.marshallFromForm(oAircraftType);
    }

    /**
     * Marshall data from the aircraft type business object into the form.
     * @param oType Aircraft type object to marshall.
     */
    private void marshallToForm(AircraftTypeBO oType) {
        this.setModel(oType.getModel());
        this.setManufacture(oType.getManufacturer());
        this.setShortName(oType.getAbbreviation());
        this.setCharacteristics(oType.getCharacteristics());
    }

    /**
     * Marshall a reference object from the form information and
     * return that object filled in with the form data.
     * @param oType Aircraft type reference object.
     * @return Same object with data filled in from the form.
     */
    private AircraftTypeBO marshallFromForm(AircraftTypeBO oType) {
        oType.setModel(this.getModel());
        oType.setManufacturer(this.getManufacture());
        oType.setAbbreviation(this.getShortName());
        oType.setCharacteristics(this.getCharacteristics());
        return oType;
    }

    /**
     * Reset the form information to blank.
     */
    public void reset() {
        this.setModel("");
        this.setShortName("");
        comboBoxManufacture.setSelectedIndex(0);
    }

    /**
     * Get the model type of the plane this is a.
     * 150M, 172P and so on.
     * @return String value of the model airplane
     */
    public String getModel() {
        return jTextFieldModel.getText();
    }

    /**
     * sets the model value of the airplane.
     * @param sValue model name of aircraft
     */
    public void setModel(String sValue) {
        this.jTextFieldModel.setText(sValue);
    }

    /**
     * Get the short abreviated name of the aircraft.
     * This is like C-150, PA-180 and so on.
     * @return String value of manufacture and model number
     */
    public String getShortName() {
        return jTextFieldShortName.getText();
    }

    /**
     * Set the short name of the aircraft which is model.
     * and manufacture abreviation such as C-172, BE-77
     * @param sVal The short abreviation of the model and manufacture
     */
    public void setShortName(String sVal) {
        this.jTextFieldShortName.setText(sVal);
    }

    /**
     * Gets the manufacture there should be a finite list.
     * of different manufactures like beech, cessna, piper
     * @return  String name of the manufacture
     */
    public String getManufacture() {
        return (String) comboBoxManufacture.getSelectedItem();
    }

    /**
     * Set the manufacture name in the combo box.
     * @param sManufacture manufacture full name.
     */
    public void setManufacture(String sManufacture) {
        this.comboBoxManufacture.setSelectedItem(sManufacture);
    }

    /**
     * Gets the integer value of the characteristics of the aircraft.
     * This is usually Airplane, Rotocraft, PowerLift, Glider, lighter than air, Simulator, Training Device, PCATD
     * @return integer value from the drop down
     */
    public int getCharacteristics() {
        return comboTypeBox.getSelectedIndex();
    }

    /**
     * Sets the different types of aircraft characteristics.
     * @param sCharacter Characteristics of the plane.
     */
    public void setCharacteristics(Integer sCharacter) {
        this.comboTypeBox.setSelectedIndex(sCharacter);
    }
}
