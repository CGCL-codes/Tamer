package com.flash.system.view;

import com.flash.system.logic.VehicleManagerService;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author shan
 */
public class AddNewVehicleModel extends JPanel implements ActionListener {

    private CommonWindowUtilities comUtil;

    private JPanel base;

    private JLabel lVehicleModel;

    private JTextField tVehicleModel;

    private JButton bAddVehicleModel;

    public AddNewVehicleModel(CommonWindowUtilities comUtil) {
        this.comUtil = comUtil;
        base = new JPanel();
        base.setPreferredSize(new Dimension(400, 200));
        base.setBorder(BorderFactory.createTitledBorder("  Add New Vehicle Model  "));
        lVehicleModel = new JLabel("Vehicle Model Name : ");
        lVehicleModel.setPreferredSize(new Dimension(150, 30));
        base.add(lVehicleModel);
        tVehicleModel = new JTextField();
        tVehicleModel.setPreferredSize(new Dimension(160, 30));
        base.add(tVehicleModel);
        bAddVehicleModel = new JButton("Add Model");
        bAddVehicleModel.setPreferredSize(new Dimension(120, 30));
        base.add(bAddVehicleModel);
        bAddVehicleModel.addActionListener(this);
        add(base);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bAddVehicleModel) {
            VehicleManagerService vehicleManagerService = new VehicleManagerService();
            vehicleManagerService.addNewVehicleModel(tVehicleModel.getText());
            comUtil.clearMainBody();
        }
    }
}
