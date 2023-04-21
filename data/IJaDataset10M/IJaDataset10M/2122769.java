package uk.ac.warwick.dcs.cokefolk.util.console;

import uk.ac.warwick.dcs.cokefolk.server.ServerInteraction;
import uk.ac.warwick.dcs.cokefolk.util.text.XMLUtils;
import java.security.cert.X509Certificate;

public class ConsoleInteraction extends ServerInteraction {

    @Override
    public synchronized String input(InputType type, String prompt) {
        String userInput = null;
        switch(type) {
            case USERNAME:
                System.out.print(prompt);
                break;
            case DB_PASSWORD:
                userInput = String.valueOf(Console.readLineSecure(prompt));
                break;
        }
        if (userInput == null) userInput = Console.readLine(prompt);
        return userInput.trim();
    }

    @Override
    public synchronized char[] secureInput(SecureInputType type, String prompt) {
        char[] userInput = null;
        if (userInput == null) Console.readLineSecure(prompt);
        return userInput;
    }

    @Override
    public synchronized void output(String out) {
        System.out.println(prefix() + out);
    }

    @Override
    public synchronized void error(String err) {
        System.err.println(prefix() + err);
    }

    public synchronized Confirm acceptCertificate(X509Certificate[] chain, String information) {
        System.out.println(XMLUtils.stripHTML(information));
        Confirm result = Confirm.YES;
        boolean resultSet = false;
        while (!resultSet) {
            String response = Console.readLine("Accept certificate (y/n/always)? ").trim().toLowerCase();
            if (response.equals("y")) {
                result = Confirm.YES;
                resultSet = true;
            } else if (response.equals("n")) {
                result = Confirm.NO;
                resultSet = true;
            } else if (response.equals("always")) {
                result = Confirm.ALWAYS;
                resultSet = true;
            }
        }
        return result;
    }
}
