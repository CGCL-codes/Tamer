package main.swissnet.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import tcp.swissnet.com.TCP_Server;

/***************************************************************************************/
public class Main {

    public static final String META_APP_NAME = "SwissKnet";

    public static final String META_APP_VERSION = "0.0.1";

    public static final String META_APP_HEADER = META_APP_NAME + " version " + META_APP_VERSION;

    public static final String FORM_BORDER_SIGN = "-";

    public static final int FORM_BORDER_SIZE = 70;

    public static final int FORM_BORDER_MARGIN = 5;

    public static final int FORM_MENU_SPACERS = 2;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        printMainMenu();
        String menuSelect = null;
        try {
            menuSelect = stdIn.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printMenuSpacer();
        if (menuSelect.equals("1")) {
            System.out.println("What port would you like the TCP server to listen on?");
            try {
                menuSelect = stdIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printMenuSpacer();
            System.out.println("Starting TCP server on port " + menuSelect + ".");
            try {
                TCP_Server tcpServer = new TCP_Server(Integer.valueOf(menuSelect));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (menuSelect.equals("2")) {
            System.out.println("What port would you like the TCP server to listen on?");
        } else if (menuSelect.equals("3")) {
        } else if (menuSelect.equals("4")) {
            System.out.println("HAVE A GOOD DAY! Bye.");
            System.exit(0);
        }
    }

    public static void printMainMenu() {
        String border = "";
        String header = "";
        String margin = "";
        for (int i = 0; i < FORM_BORDER_SIZE; i++) {
            border = border + FORM_BORDER_SIGN;
        }
        for (int i = 0; i < ((FORM_BORDER_SIZE / 2) - (META_APP_HEADER.length() / 2)); i++) {
            header = header + " ";
        }
        header = header + META_APP_HEADER;
        for (int i = 0; i < FORM_BORDER_MARGIN; i++) {
            margin = margin + " ";
        }
        System.out.println(border);
        System.out.println(header);
        System.out.println(border);
        System.out.println("What would you like to do?");
        System.out.println(margin + "1  Create a TCP Server");
        System.out.println(margin + "2  Create a UDP Server");
        System.out.println(margin + "3  Connect to a Server");
        System.out.println(margin + "4  Exit this application");
        System.out.println();
        System.out.println();
        System.out.println("(choose a number)");
    }

    public static void printMenuSpacer() {
        for (int i = 0; i < FORM_MENU_SPACERS; i++) {
            System.out.println();
        }
    }
}
