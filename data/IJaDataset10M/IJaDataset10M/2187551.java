package ua.com.m1995.table;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import ua.com.m1995.client.Log;
import ua.com.m1995.client.RealEstateObject;
import ua.com.m1995.client.UserInterfaceConstants;

public class UserInterfaceObjectEditor {

    JComboBox comboREAType;

    JTextField fieldRooms;

    JComboBox comboRoomsType;

    JTextField fieldPrice;

    JTextField fieldCity;

    JComboBox comboDistrict;

    JTextField fieldSubDistrict;

    JTextField fieldStreet;

    JTextField fieldHouseNum;

    JTextField fieldLocation;

    JTextField fieldSqAll;

    JTextField fieldSqLive;

    JTextField fieldSqKitchen;

    JTextField fieldFloor;

    JTextField fieldFloors;

    JTextField fieldDescription;

    JTextField fieldErrorDescription;

    JButton buttonYes;

    JButton buttonNo;

    RealEstateObject realData;

    UserInterfaceObjectEditor(RealEstateObject object) {
        realData = object;
    }

    void initNewWindow() {
        if (realData == null) throw new RuntimeException("RealEstateObject is NULL");
        final JFrame jFrame = new JFrame("�����������");
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setAlwaysOnTop(true);
        jFrame.setBounds(50, 50, 700, 550);
        GridBagLayout layout = new GridBagLayout();
        JLabel labelREAType = new JLabel("��� ����������");
        comboREAType = new JComboBox();
        fillComboBoxREAType(comboREAType);
        JLabel labelRooms = new JLabel("ʳ����");
        fieldRooms = new JTextField();
        fieldRooms.setText(realData.getRooms());
        JLabel labelRoomsType = new JLabel("ʳ�����");
        comboRoomsType = new JComboBox();
        fillComboBoxRoomsType(comboRoomsType);
        JLabel labelPrice = new JLabel("ֳ��");
        fieldPrice = new JTextField();
        fieldPrice.setText(realData.getPrice());
        JLabel labelCity = new JLabel("̳���");
        fieldCity = new JTextField();
        if (UserInterfaceConstants.KYIV_LOCATION_ID.equals(realData.getLocationID())) {
            fieldCity.setText(UserInterfaceConstants.KYIV_LOCATION_NAME);
        }
        JLabel labelDistrict = new JLabel("�����");
        comboDistrict = new JComboBox();
        fillComboBoxDistrict(comboDistrict);
        JLabel labelSubDistrict = new JLabel("�����");
        fieldSubDistrict = new JTextField();
        fieldSubDistrict.setText(realData.getCitySubDistrict());
        JLabel labelStreet = new JLabel("������");
        fieldStreet = new JTextField();
        fieldStreet.setText(realData.getStreet());
        JLabel labelHouseNum = new JLabel("�������");
        fieldHouseNum = new JTextField();
        fieldHouseNum.setText(realData.getHouseNumber());
        JLabel labelLocation = new JLabel("����.������");
        fieldLocation = new JTextField();
        fieldLocation.setText(realData.getLocationText());
        JLabel labelSqAll = new JLabel("��.��������");
        fieldSqAll = new JTextField();
        fieldSqAll.setText(realData.getSquareAll());
        JLabel labelSqLive = new JLabel("��.�������");
        fieldSqLive = new JTextField();
        fieldSqLive.setText(realData.getSquareLive());
        JLabel labelSqKitchen = new JLabel("��.����");
        fieldSqKitchen = new JTextField();
        fieldSqKitchen.setText(realData.getSquareKitchen());
        JLabel labelFloor = new JLabel("������");
        fieldFloor = new JTextField();
        fieldFloor.setText(realData.getFloor());
        JLabel labelFloors = new JLabel("��������");
        fieldFloors = new JTextField();
        fieldFloors.setText(realData.getFloors());
        JLabel labelDescription = new JLabel("���������");
        fieldDescription = new JTextField();
        fieldDescription.setText(realData.getDescription());
        String errorDescription = realData.getErrorDescription();
        JLabel labelErrorDescription = null;
        if (errorDescription != null) {
            labelErrorDescription = new JLabel("�������");
            fieldErrorDescription = new JTextField();
            fieldErrorDescription.setText(realData.getErrorDescription());
            fieldErrorDescription.setEditable(false);
        }
        JButton buttonYes = new JButton("���");
        JButton buttonNo = new JButton("Ͳ");
        buttonYes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                readDataToRealEstateObject();
                jFrame.dispose();
            }
        });
        buttonNo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                jFrame.dispose();
            }
        });
        GridBagConstraints constraintsLabel = new GridBagConstraints();
        GridBagConstraints constraintsField = new GridBagConstraints();
        constraintsField.fill = GridBagConstraints.HORIZONTAL;
        Insets defaultInsets = new Insets(5, 0, 0, 0);
        constraintsLabel.insets = defaultInsets;
        constraintsField.insets = defaultInsets;
        Insets topInsets = new Insets(45, 0, 0, 0);
        constraintsLabel.weightx = 1.0;
        constraintsField.weightx = 1.0;
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 0;
        layout.setConstraints(labelREAType, constraintsLabel);
        constraintsField.gridx = 1;
        constraintsField.gridy = 0;
        layout.setConstraints(comboREAType, constraintsField);
        jFrame.getContentPane().add(labelREAType);
        jFrame.getContentPane().add(comboREAType);
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 1;
        layout.setConstraints(labelRooms, constraintsLabel);
        constraintsField.gridx = 1;
        constraintsField.gridy = 1;
        layout.setConstraints(fieldRooms, constraintsField);
        jFrame.getContentPane().add(labelRooms);
        jFrame.getContentPane().add(fieldRooms);
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 2;
        layout.setConstraints(labelRoomsType, constraintsLabel);
        constraintsField.gridx = 1;
        constraintsField.gridy = 2;
        layout.setConstraints(comboRoomsType, constraintsField);
        jFrame.getContentPane().add(labelRoomsType);
        jFrame.getContentPane().add(comboRoomsType);
        constraintsLabel.insets = topInsets;
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 3;
        layout.setConstraints(labelPrice, constraintsLabel);
        constraintsLabel.insets = defaultInsets;
        constraintsField.insets = topInsets;
        constraintsField.gridx = 1;
        constraintsField.gridy = 3;
        layout.setConstraints(fieldPrice, constraintsField);
        constraintsField.insets = defaultInsets;
        jFrame.getContentPane().add(labelPrice);
        jFrame.getContentPane().add(fieldPrice);
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 0;
        layout.setConstraints(labelCity, constraintsLabel);
        constraintsField.gridx = 3;
        constraintsField.gridy = 0;
        layout.setConstraints(fieldCity, constraintsField);
        jFrame.getContentPane().add(labelCity);
        jFrame.getContentPane().add(fieldCity);
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 1;
        layout.setConstraints(labelDistrict, constraintsLabel);
        constraintsField.gridx = 3;
        constraintsField.gridy = 1;
        layout.setConstraints(comboDistrict, constraintsField);
        jFrame.getContentPane().add(labelDistrict);
        jFrame.getContentPane().add(comboDistrict);
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 2;
        layout.setConstraints(labelSubDistrict, constraintsLabel);
        constraintsField.gridx = 3;
        constraintsField.gridy = 2;
        layout.setConstraints(fieldSubDistrict, constraintsField);
        jFrame.getContentPane().add(labelSubDistrict);
        jFrame.getContentPane().add(fieldSubDistrict);
        constraintsLabel.insets = topInsets;
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 3;
        layout.setConstraints(labelStreet, constraintsLabel);
        constraintsLabel.insets = defaultInsets;
        constraintsField.insets = topInsets;
        constraintsField.gridx = 3;
        constraintsField.gridy = 3;
        layout.setConstraints(fieldStreet, constraintsField);
        constraintsField.insets = defaultInsets;
        jFrame.getContentPane().add(labelStreet);
        jFrame.getContentPane().add(fieldStreet);
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 4;
        layout.setConstraints(labelHouseNum, constraintsLabel);
        constraintsField.gridx = 3;
        constraintsField.gridy = 4;
        layout.setConstraints(fieldHouseNum, constraintsField);
        jFrame.getContentPane().add(labelHouseNum);
        jFrame.getContentPane().add(fieldHouseNum);
        constraintsLabel.insets = topInsets;
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 5;
        layout.setConstraints(labelLocation, constraintsLabel);
        constraintsLabel.insets = defaultInsets;
        constraintsField.insets = topInsets;
        constraintsField.gridx = 3;
        constraintsField.gridy = 5;
        layout.setConstraints(fieldLocation, constraintsField);
        constraintsField.insets = defaultInsets;
        jFrame.getContentPane().add(labelLocation);
        jFrame.getContentPane().add(fieldLocation);
        constraintsLabel.insets = topInsets;
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 6;
        layout.setConstraints(labelSqAll, constraintsLabel);
        constraintsLabel.insets = defaultInsets;
        constraintsField.insets = topInsets;
        constraintsField.gridx = 1;
        constraintsField.gridy = 6;
        layout.setConstraints(fieldSqAll, constraintsField);
        constraintsField.insets = defaultInsets;
        jFrame.getContentPane().add(labelSqAll);
        jFrame.getContentPane().add(fieldSqAll);
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 7;
        layout.setConstraints(labelSqLive, constraintsLabel);
        constraintsField.gridx = 1;
        constraintsField.gridy = 7;
        layout.setConstraints(fieldSqLive, constraintsField);
        jFrame.getContentPane().add(labelSqLive);
        jFrame.getContentPane().add(fieldSqLive);
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 8;
        layout.setConstraints(labelSqKitchen, constraintsLabel);
        constraintsField.gridx = 1;
        constraintsField.gridy = 8;
        layout.setConstraints(fieldSqKitchen, constraintsField);
        jFrame.getContentPane().add(labelSqKitchen);
        jFrame.getContentPane().add(fieldSqKitchen);
        constraintsLabel.insets = topInsets;
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 6;
        layout.setConstraints(labelFloor, constraintsLabel);
        constraintsLabel.insets = defaultInsets;
        constraintsField.insets = topInsets;
        constraintsField.gridx = 3;
        constraintsField.gridy = 6;
        layout.setConstraints(fieldFloor, constraintsField);
        constraintsField.insets = defaultInsets;
        jFrame.getContentPane().add(labelFloor);
        jFrame.getContentPane().add(fieldFloor);
        constraintsLabel.gridx = 2;
        constraintsLabel.gridy = 7;
        layout.setConstraints(labelFloors, constraintsLabel);
        constraintsField.gridx = 3;
        constraintsField.gridy = 7;
        layout.setConstraints(fieldFloors, constraintsField);
        jFrame.getContentPane().add(labelFloors);
        jFrame.getContentPane().add(fieldFloors);
        GridBagConstraints constraintsDescr = new GridBagConstraints();
        constraintsDescr.weightx = 1.0;
        constraintsDescr.insets = topInsets;
        constraintsDescr.gridx = 0;
        constraintsDescr.gridy = 9;
        layout.setConstraints(labelDescription, constraintsDescr);
        constraintsDescr.gridwidth = GridBagConstraints.REMAINDER;
        constraintsDescr.fill = GridBagConstraints.HORIZONTAL;
        constraintsDescr.gridx = 1;
        constraintsDescr.gridy = 9;
        layout.setConstraints(fieldDescription, constraintsDescr);
        jFrame.getContentPane().add(labelDescription);
        jFrame.getContentPane().add(fieldDescription);
        if (labelErrorDescription != null) {
            GridBagConstraints constraintsErrorDescr = new GridBagConstraints();
            constraintsErrorDescr.weightx = 1.0;
            constraintsErrorDescr.insets = new Insets(20, 0, 0, 0);
            ;
            constraintsErrorDescr.gridx = 0;
            constraintsErrorDescr.gridy = 10;
            layout.setConstraints(labelErrorDescription, constraintsErrorDescr);
            constraintsErrorDescr.gridwidth = GridBagConstraints.REMAINDER;
            constraintsErrorDescr.fill = GridBagConstraints.HORIZONTAL;
            constraintsErrorDescr.gridx = 1;
            constraintsErrorDescr.gridy = 10;
            layout.setConstraints(fieldErrorDescription, constraintsErrorDescr);
            jFrame.getContentPane().add(labelErrorDescription);
            jFrame.getContentPane().add(fieldErrorDescription);
        }
        GridBagConstraints constraintsButton = new GridBagConstraints();
        constraintsButton.weightx = 1.0;
        constraintsButton.insets = topInsets;
        constraintsButton.gridx = 1;
        constraintsButton.gridy = 100;
        layout.setConstraints(buttonYes, constraintsButton);
        constraintsButton.gridx = 2;
        constraintsButton.gridy = 100;
        layout.setConstraints(buttonNo, constraintsButton);
        jFrame.getContentPane().add(buttonYes);
        jFrame.getContentPane().add(buttonNo);
        jFrame.getContentPane().setLayout(layout);
        jFrame.setVisible(true);
    }

    private void fillComboBoxDistrict(JComboBox aComboBox) {
        List<String> districts = UserInterfaceConstants.getDistricts();
        Iterator<String> iDistricts = districts.iterator();
        aComboBox.addItem("");
        while (iDistricts.hasNext()) {
            String value = iDistricts.next();
            aComboBox.addItem(value);
        }
        String district = realData.getCityDistrict();
        district = UserInterfaceConstants.getDistrictNameByID(district);
        if (district != null) {
            aComboBox.setSelectedItem(district);
        }
    }

    private void fillComboBoxRoomsType(JComboBox aComboBox) {
        List<String> roomsTypes = UserInterfaceConstants.getRoomsTypes();
        Iterator<String> iRoomsTypes = roomsTypes.iterator();
        while (iRoomsTypes.hasNext()) {
            String value = iRoomsTypes.next();
            aComboBox.addItem(value);
        }
        String roomsType = realData.getRoomsType();
        roomsType = UserInterfaceConstants.getRoomsTypeByID(roomsType);
        if (roomsType != null) {
            aComboBox.setSelectedItem(roomsType);
        }
    }

    private void fillComboBoxREAType(JComboBox aComboBox) {
        List<String> reaTypes = UserInterfaceConstants.getRealEstateTypes();
        Iterator<String> ireaTypes = reaTypes.iterator();
        while (ireaTypes.hasNext()) {
            String value = ireaTypes.next();
            aComboBox.addItem(value);
        }
        String type = realData.getRealEstateType();
        type = UserInterfaceConstants.getRealEstateTypeByID(type);
        if (type != null) {
            aComboBox.setSelectedItem(type);
        }
    }

    private void readDataToRealEstateObject() {
        Log.debug("Read data to RealEstateObject");
        Log.debug(realData.toTestString());
        String realEstateTypeID = UserInterfaceConstants.getRealEstateTypeIDByName((String) comboREAType.getSelectedItem());
        realData.setRealEstateType(realEstateTypeID);
        realData.setRooms(fieldRooms.getText());
        String roomsTypeID = UserInterfaceConstants.getRoomsTypeIDByName((String) comboRoomsType.getSelectedItem());
        realData.setRoomsType(roomsTypeID);
        realData.setPrice(fieldPrice.getText());
        realData.setSquareAll(fieldSqAll.getText());
        realData.setSquareLive(fieldSqLive.getText());
        realData.setSquareKitchen(fieldSqKitchen.getText());
        if (UserInterfaceConstants.KYIV_LOCATION_NAME.equals(fieldCity.getText().trim())) {
            Log.debug("Kyiv location.");
            realData.setLocationID(UserInterfaceConstants.KYIV_LOCATION_ID);
            String cityDistrict = (String) comboDistrict.getSelectedItem();
            if (cityDistrict == null) {
                Log.warn("City district = null");
            } else {
                cityDistrict = UserInterfaceConstants.getDistrictIDByName(cityDistrict);
                realData.setCityDistrict(cityDistrict);
            }
        } else {
            Log.debug("Not Kyiv location.");
            realData.setLocationID("");
            realData.setCityDistrict("");
        }
        realData.setCitySubDistrict(fieldSubDistrict.getText());
        realData.setStreet(fieldStreet.getText());
        realData.setHouseNumber(fieldHouseNum.getText());
        realData.setLocationText(fieldLocation.getText());
        realData.setFloor(fieldFloor.getText());
        realData.setFloors(fieldFloors.getText());
        realData.setDescription(fieldDescription.getText());
    }
}
