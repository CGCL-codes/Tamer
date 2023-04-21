package net.sf.repbot;

import net.sf.repbot.db.*;
import java.io.*;
import java.util.*;

public class RegisterFibsAction implements FibsAction {

    private String COMMAND = null;

    private UserDBInterface userDB = null;

    public RegisterFibsAction() {
        this.COMMAND = " says: register";
        this.errResp = new String[1];
        this.ret = new String[1];
        try {
            this.userDB = (UserDBInterface) UserDBImplementation.getUserDB();
        } catch (Exception e) {
        }
    }

    public String getGrepString() {
        return this.COMMAND;
    }

    private String fromWho = "";

    private String aboutWho = "";

    private static String shouldBeSays = "says:";

    private static String shouldBeRegister = "register";

    private String[] errResp;

    private String[] ret;

    public String[] action(String command, OutputStream log) {
        System.out.println("\n\tREGISTER ACTION\n");
        System.out.flush();
        StringTokenizer st = new StringTokenizer(command);
        try {
            fromWho = st.nextToken();
            System.out.println("\tREGISTER");
            if (shouldBeSays.compareTo(st.nextToken()) != 0) {
                return ((String[]) null);
            }
            if (shouldBeRegister.compareTo(st.nextToken()) != 0) {
                errResp[0] = "tell " + fromWho + " I didn't understand you. Try: tell RepBot help\n";
                return (errResp);
            }
            System.out.println("\tfromWho=" + fromWho);
            try {
                ret[0] = "tell " + fromWho + " register command unimplemented. Check the web page for more";
                return (ret);
            } catch (Exception e) {
                ret[0] = "tell " + fromWho + " Already registered?";
                return (ret);
            }
        } catch (NoSuchElementException e) {
            return ((String[]) null);
        }
    }
}
