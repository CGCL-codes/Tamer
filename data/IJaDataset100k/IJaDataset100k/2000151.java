package ControleursBorne;

import VuesBorne.wAuthClient;
import VuesBorne.wAuthTechnicien;
import VuesBorne.wTypeUser;

/**
 *
 * @author Alban
 */
public class TypeUser {

    public void confirmTypeUser(String sTypeUser, wTypeUser wTypeUser) {
        if (sTypeUser.equals("Client")) {
            System.out.println("Ok - Client");
            wTypeUser.dispose();
            wAuthClient owAuthClient = new wAuthClient();
            owAuthClient.setVisible(true);
        } else if (sTypeUser.equals("Technicien")) {
            System.out.println("Ok - Technicien");
            wTypeUser.dispose();
            wAuthTechnicien owAuthTechnicien = new wAuthTechnicien();
            owAuthTechnicien.setVisible(true);
        }
    }
}
