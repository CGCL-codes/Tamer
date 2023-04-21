package eq.server;

import eq.common.database.Database;
import eq.common.network.LoginMessage;
import eq.common.network.ResponseLoginMessage;

public class ServerProcessor {

    Database d = new Database();

    public String processInput(String s) {
        String temp[] = s.split("(?<!\\\\):");
        String type = temp[0];
        String result = "";
        System.out.println("SERVER TYPE " + type);
        if (type.equalsIgnoreCase("login")) {
            LoginMessage m = new LoginMessage(s);
            if (m.isCreateUser()) {
                d.createPerson(m.getUsername(), m.getPassword(), "", "");
            }
            if (d.authenticateUser(m.getUsername(), m.getPassword())) {
                System.out.println("SERVER LOGN " + m.getUsername());
                result = new ResponseLoginMessage(true).toString();
            } else {
                System.out.println("SERVER NACK " + m.getUsername());
                result = new ResponseLoginMessage(false).toString();
            }
        } else if (type.equalsIgnoreCase("logout")) {
            result = "goodbye";
        } else {
            result = "nack:unknown command";
        }
        return result;
    }
}
