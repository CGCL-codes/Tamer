package org.plazmaforge.bsolution.contact.client.swing.forms;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import org.plazmaforge.bsolution.contact.client.swing.GUIContactEnvironment;
import org.plazmaforge.bsolution.contact.common.beans.Address;
import org.plazmaforge.bsolution.contact.common.beans.AddressType;
import org.plazmaforge.bsolution.contact.common.beans.ApartmentType;
import org.plazmaforge.bsolution.contact.common.beans.City;
import org.plazmaforge.bsolution.contact.common.beans.Locality;
import org.plazmaforge.bsolution.contact.common.beans.StreetType;
import org.plazmaforge.bsolution.contact.common.beans.formatter.AddressFormatter;
import org.plazmaforge.bsolution.contact.common.beans.formatter.ContactFormatterFactory;
import org.plazmaforge.framework.client.swing.controls.XComboEdit;
import org.plazmaforge.framework.client.swing.controls.XTextEdit;
import org.plazmaforge.framework.client.swing.controls.XTextField;
import org.plazmaforge.framework.client.swing.forms.AbstractPanelController;
import org.plazmaforge.framework.client.swing.forms.IFORMFactory;
import org.plazmaforge.framework.client.swing.forms.PanelController;
import org.plazmaforge.framework.client.swing.gui.GridBagPanel;
import org.plazmaforge.framework.core.LocaleManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon Date: 13.03.2004 Time: 13:12:37 
 * $Id: AddressEdit.java,v 1.4 2010/12/05 07:56:45 ohapon Exp $
 */
public class AddressEdit extends AbstractContactableItemEdit {

    private JLabel addressTypeLabel;

    private JLabel cityLabel;

    private JLabel localityLabel;

    private JLabel defaultLabel;

    private JLabel addressStringLabel;

    private JLabel addressLabel;

    private JLabel zipCodeLabel;

    private JLabel streetLabel;

    private JLabel houseNumLabel;

    private JLabel appartNumLabel;

    private XComboEdit addressTypeComboEdit;

    private XComboEdit cityComboEdit;

    private XComboEdit localityComboEdit;

    private XComboEdit streetTypeComboEdit;

    private XComboEdit appartmentTypeComboEdit;

    private JCheckBox defaultField;

    private XTextField addressStringField;

    private XTextField zipCodeField;

    private XTextField streetField;

    private XTextField houseNumField;

    private XTextField appartNumField;

    private XTextEdit noteField;

    private XTextEdit streetText;

    private AddressFormatter addressFormatter = ContactFormatterFactory.getAddressFormatter();

    /** Address Panel Controller (SIMPLE | COMPLEX) */
    private PanelController panelController;

    public AddressEdit() throws ApplicationException {
        super(GUIContactEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(Address.class);
    }

    protected void initComponents() throws ApplicationException {
        setTitle(getString("title"));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(getString("panel.title.common.text"), createCommonPanel());
        tabbedPane.addTab(getString("panel.title.note.text"), createNotePanel());
        add(tabbedPane);
    }

    private JPanel createCommonPanel() throws ApplicationException {
        initCommonPanel();
        return getPanelController().createPanel();
    }

    private JPanel createNotePanel() {
        JPanel editPanel = new JPanel(new BorderLayout());
        noteField = new XTextEdit();
        editPanel.add(noteField, BorderLayout.CENTER);
        return editPanel;
    }

    private Address getAddress() {
        return (Address) this.getEntity();
    }

    protected void updateView() throws ApplicationException {
        appendTitle(getAddress().getAddressString());
        addressTypeComboEdit.setValue(getAddress().getAddressType());
        cityComboEdit.setValue(getAddress().getCity());
        localityComboEdit.setValue(getAddress().getLocality());
        streetTypeComboEdit.setValue(getAddress().getStreetType());
        appartmentTypeComboEdit.setValue(getAddress().getApartmentType());
        defaultField.setSelected(getAddress().isDefaultItem());
        addressStringField.setText(getAddress().getAddressString());
        zipCodeField.setText(getAddress().getZipCode());
        streetField.setText(getAddress().getStreet());
        houseNumField.setText(getAddress().getHouseNum());
        appartNumField.setText(getAddress().getApartNum());
        noteField.setText(getAddress().getNote());
    }

    protected void populateData() throws ApplicationException {
        formatAddressString();
        getAddress().setAddressType((AddressType) addressTypeComboEdit.getValue());
        getAddress().setCity((City) cityComboEdit.getValue());
        getAddress().setLocality((Locality) localityComboEdit.getValue());
        getAddress().setStreetType((StreetType) streetTypeComboEdit.getValue());
        getAddress().setApartmentType((ApartmentType) appartmentTypeComboEdit.getValue());
        getAddress().setDefaultItem(defaultField.isSelected());
        getAddress().setAddressString(addressStringField.getText());
        getAddress().setZipCode(zipCodeField.getText());
        getAddress().setStreet(streetField.getText());
        getAddress().setHouseNum(houseNumField.getText());
        getAddress().setApartNum(appartNumField.getText());
        getAddress().setNote(noteField.getText());
    }

    protected void addChild() throws ApplicationException {
        getContactable().addAddress(getAddress());
        getAddress().setDefaultItem(defaultField.isSelected());
    }

    private void formatAddressString() {
        addressStringField.setText(addressFormatter.formatFullAddress((City) cityComboEdit.getValue(), (Locality) localityComboEdit.getValue(), (StreetType) streetTypeComboEdit.getValue(), streetField.getText(), houseNumField.getText(), "", (ApartmentType) appartmentTypeComboEdit.getValue(), appartNumField.getText()));
    }

    protected class FormatterFocusListener implements FocusListener {

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            formatAddressString();
        }
    }

    protected class FormatterActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            formatAddressString();
        }
    }

    protected boolean isAutoFormatter() {
        return true;
    }

    protected void validateData() throws ApplicationException {
        super.validateData();
        StringBuffer buf = new StringBuffer();
        validateRequiredField(buf, getAddress().getAddressType(), addressTypeLabel.getText());
        checkError(buf);
    }

    private IFORMFactory getFormFactory() {
        return this;
    }

    private void initCommonPanel() throws ApplicationException {
        addressTypeLabel = new JLabel(getString("panel.label-address-type.text"));
        addressStringLabel = new JLabel(getString("panel.label-full-address.text"));
        cityLabel = new JLabel(getString("panel.label-city.text"));
        defaultLabel = new JLabel(getString("panel.label-default.text"));
        zipCodeLabel = new JLabel(getString("panel.label-zip-code.text"));
        addressLabel = new JLabel(getString("panel.label-address.text"));
        streetLabel = new JLabel(getString("panel.label-street.text"));
        localityLabel = new JLabel(getString("panel.label-locality.text"));
        houseNumLabel = new JLabel(getString("panel.label-house-num.text"));
        appartNumLabel = new JLabel(getString("panel.label-appart-num.text"));
        addressTypeComboEdit = new XComboEdit();
        cityComboEdit = new XComboEdit(true);
        cityComboEdit.setColumns(25);
        defaultField = new JCheckBox();
        addressStringField = new XTextField(30);
        addressStringField.setMaxChar(50);
        addressStringField.setEditable(!isAutoFormatter());
        zipCodeField = new XTextField(10);
        zipCodeField.setMaxChar(10);
        streetField = new XTextField(40);
        streetField.setMaxChar(50);
        localityComboEdit = new XComboEdit(true);
        localityComboEdit.setColumns(25);
        streetTypeComboEdit = new XComboEdit(true);
        streetTypeComboEdit.setColumns(5);
        appartmentTypeComboEdit = new XComboEdit(true);
        appartmentTypeComboEdit.setColumns(5);
        houseNumField = new XTextField(6);
        houseNumField.setMaxChar(6);
        appartNumField = new XTextField(6);
        appartNumField.setMaxChar(6);
    }

    /**
     * Get Panel Controller
     * 
     * @return
     */
    private PanelController getPanelController() {
        if (panelController == null) {
            if (LocaleManager.COMPLEX_ADDRESS.equals(LocaleManager.getAddressVariant())) {
                panelController = new ComplexAddressController();
            } else {
                panelController = new SimpleAddressController();
            }
        }
        return panelController;
    }

    /**
     * Simple Address Controller
     * 
     * 
     */
    private class SimpleAddressController extends AbstractPanelController {

        public JPanel createPanel() throws ApplicationException {
            GridBagPanel editPanel = new GridBagPanel();
            GridBagConstraints gbc = editPanel.getGridBagConstraints();
            int COLUMN_COUNT = 7;
            editPanel.add(addressTypeLabel);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editPanel.addByX(addressTypeComboEdit);
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            editPanel.addByY(addressStringLabel);
            gbc.gridwidth = COLUMN_COUNT - 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editPanel.addByX(addressStringField);
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            editPanel.addByY(defaultLabel);
            editPanel.addByX(defaultField);
            gbc.gridx = 0;
            gbc.gridwidth = COLUMN_COUNT;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editPanel.addByY(new JSeparator());
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            editPanel.addByY(zipCodeLabel);
            editPanel.addByX(zipCodeField);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            editPanel.addByY(cityLabel);
            editPanel.addByX(cityComboEdit);
            gbc.gridx = 0;
            editPanel.addByY(addressLabel);
            gbc.gridwidth = 2;
            editPanel.addByX(streetField);
            addressTypeComboEdit.initListFormAction(getFormFactory(), AddressTypeList.class);
            cityComboEdit.initListFormAction(getFormFactory(), CityList.class);
            if (isAutoFormatter()) {
                ActionListener actionListener = new FormatterActionListener();
                FocusListener focusListener = new FormatterFocusListener();
                cityComboEdit.addAfterListActionListener(actionListener);
                streetField.addFocusListener(focusListener);
            }
            return editPanel;
        }
    }

    /**
     * Complex Address Controller
     * 
     * 
     */
    private class ComplexAddressController extends AbstractPanelController {

        public JPanel createPanel() throws ApplicationException {
            GridBagPanel editPanel = new GridBagPanel();
            GridBagConstraints gbc = editPanel.getGridBagConstraints();
            int COLUMN_COUNT = 7;
            editPanel.add(addressTypeLabel);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = COLUMN_COUNT - 1;
            editPanel.addByX(addressTypeComboEdit);
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            editPanel.addByY(addressStringLabel);
            gbc.gridwidth = COLUMN_COUNT - 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editPanel.addByX(addressStringField);
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            editPanel.addByY(defaultLabel);
            editPanel.addByX(defaultField);
            gbc.gridx = 0;
            gbc.gridwidth = COLUMN_COUNT;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            editPanel.addByY(new JSeparator());
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            editPanel.addByY(zipCodeLabel);
            editPanel.addByX(zipCodeField);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            editPanel.addByY(cityLabel);
            gbc.gridwidth = COLUMN_COUNT - 1;
            editPanel.addByX(cityComboEdit);
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            editPanel.addByY(localityLabel);
            gbc.gridwidth = COLUMN_COUNT - 1;
            editPanel.addByX(localityComboEdit);
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            editPanel.addByY(streetTypeComboEdit);
            gbc.gridwidth = COLUMN_COUNT - 1;
            editPanel.addByX(streetField);
            gbc.gridx = 2;
            gbc.gridwidth = 1;
            editPanel.addByY(houseNumLabel);
            editPanel.addByX(houseNumField);
            editPanel.addByX(appartmentTypeComboEdit);
            editPanel.addByX(appartNumField);
            addressTypeComboEdit.initListFormAction(getFormFactory(), AddressTypeList.class);
            streetTypeComboEdit.initListFormAction(getFormFactory(), StreetTypeList.class);
            cityComboEdit.initListFormAction(getFormFactory(), CityList.class);
            localityComboEdit.initListFormAction(getFormFactory(), LocalityList.class);
            appartmentTypeComboEdit.initListFormAction(getFormFactory(), AppartmentTypeList.class);
            if (isAutoFormatter()) {
                ActionListener actionListener = new FormatterActionListener();
                FocusListener focusListener = new FormatterFocusListener();
                cityComboEdit.addAfterListActionListener(actionListener);
                localityComboEdit.addAfterListActionListener(actionListener);
                streetTypeComboEdit.addAfterListActionListener(actionListener);
                appartmentTypeComboEdit.addAfterListActionListener(actionListener);
                streetField.addFocusListener(focusListener);
                houseNumField.addFocusListener(focusListener);
                appartNumField.addFocusListener(focusListener);
            }
            return editPanel;
        }
    }
}
