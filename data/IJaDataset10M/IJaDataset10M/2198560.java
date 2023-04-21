package org.myrobotlab.control;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.myrobotlab.image.Util;
import org.myrobotlab.service.interfaces.CommunicationInterface;
import org.myrobotlab.service.interfaces.GUI;

public class Welcome extends ServiceGUI {

    static final long serialVersionUID = 1L;

    CommunicationInterface comm = null;

    JTextField loginValue = new JTextField("bob");

    JTextField loginPasswordValue = new JPasswordField("blahblah");

    JTextField hostnameValue = new JTextField("localhost", 15);

    JIntegerField servicePortValue = new JIntegerField();

    public Welcome(final String boundServiceName, final GUI myService) {
        super(boundServiceName, myService);
    }

    public void init() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.ipadx = 5;
        servicePortValue.setInt(6767);
        gc.gridx = 0;
        JLabel image = new JLabel();
        image.setIcon(Util.getResourceIcon("mrl_logo.gif"));
        display.add(image);
        ++gc.gridy;
        ++gc.gridy;
        ++gc.gridy;
        ++gc.gridy;
        display.add(new JLabel("<html><h3><i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;I for one, welcome our new robot overlords ...</i></h3></html>"), gc);
    }

    public String setRemoteConnectionStatus(String state) {
        return state;
    }

    class connect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            myService.sendServiceDirectoryUpdate(loginValue.getText(), loginPasswordValue.getText(), null, hostnameValue.getText(), servicePortValue.getInt(), null);
        }
    }

    @Override
    public void attachGUI() {
    }

    @Override
    public void detachGUI() {
    }
}
