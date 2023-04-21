package net.assimilator.examples.demo.serviceui;

import net.assimilator.examples.demo.ExampleService;
import net.assimilator.examples.demo.ExampleServiceAdmin;
import net.jini.core.lookup.ServiceItem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Logger;

/**
 * @author Larry Mitchell
 * @version $Id: ExampleServiceAdminUI.java 191 2007-08-01 23:58:22Z khartig $
 */
public class ExampleServiceAdminUI extends JPanel {

    /**
     * The Logger for this example
     */
    private static final Logger logger = Logger.getLogger("net.assimilator.examples.demo.example");

    private ExampleService service;

    private ExampleServiceAdmin serviceAdmin;

    private ServiceItem item;

    private JTextField imageBaseDirTextField = new JTextField();

    /**
     * Creates new VFSServiceUI
     */
    public ExampleServiceAdminUI(Object obj) {
        super();
        getAccessibleContext().setAccessibleName("Example Service Admin UI");
        this.item = (ServiceItem) obj;
        this.service = (ExampleService) item.service;
        try {
            serviceAdmin = (ExampleServiceAdmin) service.getAdmin();
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            add(new JLabel("Enter the facial image directory: "), c);
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 4;
            add(imageBaseDirTextField, c);
            c.gridx = 2;
            c.gridy = 0;
            c.gridwidth = 2;
            JButton setBaseDirButton = new JButton("Set Facial DB Directory");
            setBaseDirButton.addActionListener(new ActionListener() {

                /**
                 * Invoked when an action occurs.
                 */
                public void actionPerformed(ActionEvent e) {
                    setTrainingDir();
                }
            });
            add(setBaseDirButton, c);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void setTrainingDir() {
        try {
            serviceAdmin.setTrainingImageLocation(imageBaseDirTextField.getText());
        } catch (RemoteException rme) {
            logger.severe("failed to set facial database directory: " + rme);
        }
    }
}
