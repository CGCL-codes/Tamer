package com.cafe.serve.event.handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import com.cafe.serve.controller.usermanagement.UserManagementMainController;
import com.cafe.serve.database.CreateDrinkingCard;
import com.cafe.serve.server.InsertNewDrinksDialog;
import com.cafe.serve.server.OrderProcessingServer;
import com.cafe.serve.view.usermanagement.UserManagementMainView;

/**
 * @author Raptis Asterios
 * Created first on 04.12.2004 in project ServerThreaded
 */
public class BestellAnnahmeServerEventHandler extends WindowAdapter implements ActionListener {

    private static final String BENUTZERVERWALTUNG = "Benutzerverwaltung";

    private static final String KARTE_SPEICHERN = "Karte speichern";

    private static final String OEFFNEN = "�ffnen";

    private static final String SPEICHERN = "Speichern";

    public static final String GETRAENKE = "Getr�nke";

    private static Logger logger = Logger.getLogger(BestellAnnahmeServerEventHandler.class.getName());

    private OrderProcessingServer bestellAnnahmeServer = null;

    /**
     *
     */
    public BestellAnnahmeServerEventHandler(OrderProcessingServer bestellAnnahmeServer) {
        this.bestellAnnahmeServer = bestellAnnahmeServer;
    }

    public void actionPerformed(ActionEvent event) {
        System.out.println(event.getActionCommand());
        String cmd = event.getActionCommand();
        if (cmd.equals(GETRAENKE)) {
            InsertNewDrinksDialog indd = new InsertNewDrinksDialog(bestellAnnahmeServer, "Getr�nke eintragen", bestellAnnahmeServer.getGetraenkeKarte());
            indd.setVisible(true);
        } else if (cmd.equals("Beenden")) {
            int r = JOptionPane.showConfirmDialog(bestellAnnahmeServer, "Sind sie sicher das der Server beendet werden soll?", "!?!Server Schliessen!?!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r == 0) {
                System.exit(0);
            }
        } else if (cmd.equals(SPEICHERN)) {
            System.out.println("Speichern wurde gew�hlt.");
        } else if (cmd.equals(OEFFNEN)) {
            System.out.println("�ffnen wurde gew�hlt.");
        } else if (cmd.equals(KARTE_SPEICHERN)) {
            System.out.println("Karte speichern wurde gew�hlt.");
            CreateDrinkingCard drinkcard = new CreateDrinkingCard();
            drinkcard.getAllDrinksFromDB();
        } else if (cmd.equals(BENUTZERVERWALTUNG)) {
            UserManagementMainView userManagementMainView = new UserManagementMainView(new UserManagementMainController());
            JDialog userManagementDialog = new JDialog(OrderProcessingServer.getInstance(), "UserManagement", true);
            userManagementDialog.add(userManagementMainView.getComponent());
            userManagementDialog.pack();
            userManagementDialog.setVisible(true);
            System.out.println("Benutzerverwaltung wurde gew�hlt.");
        }
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("Beendet von rotem X");
        System.exit(0);
    }

    public void windowClosing() {
        System.out.println("Beendet von rotem X");
        System.exit(0);
    }
}
