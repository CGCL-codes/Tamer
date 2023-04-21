package org.bissa.scaleTest.Master;

import org.bissa.NodeInfo;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigurationConsole {

    private JPanel panel1;

    private JTextField localIPText;

    private JTextField localPortText;

    private JTextField bootIPText;

    private JTextField bootPortText;

    private JButton OKButton;

    private JButton RESETButton;

    private JTextField filePathText;

    private JFrame frame;

    public ConfigurationConsole() throws Exception {
        frame = new JFrame("ConfigurationConsoleOld");
        frame.setContentPane(this.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        RESETButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                localIPText.setText("");
                localPortText.setText("");
                bootIPText.setText("");
                bootPortText.setText("");
            }
        });
        OKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NodeInfo info = NodeInfo.getInstance();
                info.setLocalAddress(localIPText.getText());
                info.setLocalPort(Integer.parseInt(localPortText.getText()));
                info.setBootAddress(bootIPText.getText());
                info.setBootPort(Integer.parseInt(bootPortText.getText()));
                info.setConfigurationFile(filePathText.getText());
                hide();
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }
}
