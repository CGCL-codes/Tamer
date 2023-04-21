package larpplanner.gui;

import larpplanner.logic.LARPManagerImp;

public class PlannerMain {

    public static String HOST_ADRR = "localhost";

    public static short PORT = 3305;

    public static String SCHEME = "DbMysql05";

    public static String USERNAME = "DbMysql05";

    public static String PASSWORD = "DbMysql05";

    public static void main(String[] args) {
        if (args.length == 5) {
            HOST_ADRR = args[0];
            PORT = Short.parseShort(args[1]);
            SCHEME = args[2];
            USERNAME = args[3];
            PASSWORD = args[4];
        } else if (args.length > 0) {
            System.out.println("Bad arguments");
        }
        LARPManagerImp.init(HOST_ADRR, PORT, SCHEME, USERNAME, PASSWORD);
        LoginWin.openLoginWin();
    }
}
