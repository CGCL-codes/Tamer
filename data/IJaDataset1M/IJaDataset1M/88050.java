package com.platonov.gui.data;

import com.platonov.Main;
import com.platonov.engine.database.loader.DBObject;
import com.platonov.engine.network.NetworkProxy;
import com.platonov.utils.GUIUtilities;
import org.neuroph.util.TransferFunctionType;
import javax.swing.*;
import java.awt.event.*;

/**
 * User: User
 * Date: 20.04.11   /    Time: 9:16
 */
public class NetworkForm {

    private JPanel panel1;

    private JList listNetwork;

    private JTextField name;

    private JButton add;

    private JButton save;

    private JButton del;

    private JButton close;

    private JButton delLay;

    private JList listLayers;

    private JSpinner inNum;

    private JSpinner outNum;

    private JSpinner layNum;

    private JButton learn;

    private JComboBox functions;

    private JButton addLay;

    private static NetworkForm networkForm;

    public static JFrame frame;

    private NetworkProxy networkProxy = NetworkProxy.INSTANCE;

    static {
        frame = new JFrame("��������� ����");
        networkForm = new NetworkForm();
        frame.setContentPane(networkForm.panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                networkForm.close();
            }
        });
        frame.pack();
        frame.setVisible(false);
        GUIUtilities.setCenterLocated(frame);
    }

    public NetworkForm() {
        functions.setModel(new DefaultComboBoxModel(TransferFunctionType.values()));
        inNum.setValue(1);
        outNum.setValue(3);
        listLayers.setListData(new Object[] { "HiddenLay1" });
        refresh();
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        del.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                del();
            }
        });
        delLay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                delLay();
            }
        });
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        learn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                learn();
            }
        });
        listNetwork.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selectNetwork();
            }
        });
        listLayers.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selectLay();
            }
        });
    }

    private void refresh() {
        listNetwork.setListData(NetworkProxy.INSTANCE.getAll().toArray());
    }

    private void close() {
        frame.setVisible(false);
        Main.mainForm.setVisible(true);
    }

    private void add() {
        if (validation()) {
            NetworkProxy.INSTANCE.createNetwork(name.getText(), (TransferFunctionType) functions.getSelectedItem(), new int[] { Integer.parseInt(inNum.getValue().toString()), Integer.parseInt(layNum.getValue().toString()), Integer.parseInt(outNum.getValue().toString()) });
            refresh();
        }
    }

    private void del() {
        if (listNetwork.getSelectedIndex() > -1) {
            int buttSelect = JOptionPane.showConfirmDialog(frame, "�� ������������� ������ ������� ������?", "��������", JOptionPane.YES_NO_OPTION);
            if (buttSelect == JOptionPane.YES_OPTION) {
                DBObject network = (DBObject) listNetwork.getSelectedValue();
                network.delete();
                NetworkProxy.INSTANCE.reset();
                refresh();
            }
        } else JOptionPane.showMessageDialog(new JFrame(), "�������� ���� � ������", "��������������", JOptionPane.WARNING_MESSAGE);
    }

    private void save() {
        if (listNetwork.getSelectedIndex() > -1) {
            if (validation()) {
                DBObject network = (DBObject) listNetwork.getSelectedValue();
                network.setName(name.getText());
                networkProxy.save(network, (TransferFunctionType) functions.getSelectedItem(), new int[] { Integer.parseInt(inNum.getValue().toString()), Integer.parseInt(layNum.getValue().toString()), Integer.parseInt(outNum.getValue().toString()) });
                refresh();
            }
        } else JOptionPane.showMessageDialog(new JFrame(), "�������� ���� � ������", "��������������", JOptionPane.WARNING_MESSAGE);
    }

    private void delLay() {
    }

    private void selectNetwork() {
        if (listNetwork.getSelectedIndex() > -1) {
            DBObject network = (DBObject) listNetwork.getSelectedValue();
            NetworkProxy.INSTANCE.loadFromDB(network);
            name.setText(network.getName());
            layNum.setValue(NetworkProxy.INSTANCE.getMlp().getLayerAt(0).getNeuronsCount());
        }
    }

    private void selectLay() {
    }

    private void learn() {
        if (listNetwork.getSelectedIndex() > -1) {
            frame.setVisible(false);
            NetworkLearnForm.frame.setVisible(true);
        } else JOptionPane.showMessageDialog(new JFrame(), "�������� ���� � ������", "��������������", JOptionPane.WARNING_MESSAGE);
    }

    private boolean validation() {
        return true;
    }
}
