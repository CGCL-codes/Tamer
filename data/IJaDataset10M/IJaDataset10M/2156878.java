package kenwudi.java.msn;

import java.util.ArrayList;
import java.util.List;
import rath.msnm.BuddyList;
import rath.msnm.MSNMessenger;
import rath.msnm.SwitchboardSession;
import rath.msnm.UserStatus;
import rath.msnm.entity.MsnFriend;
import rath.msnm.event.MsnAdapter;
import rath.msnm.msg.MimeMessage;

/**
 * MSN
 */
public class MSN extends Thread {

    private static MSNMessenger msn;

    public static void main(String[] args) {
        msn = new MSNMessenger("kenwudy@hotmail.com", "ilovenijune");
        msn.setInitialStatus(UserStatus.ONLINE);
        msn.login();
        System.out.println("Waiting for the response....");
        System.out.println("Start Login....");
        Runtime.getRuntime().addShutdownHook(new MSN());
    }

    /**
     * EXIT
     */
    public void run() {
        msn.logout();
        System.out.println("MSN Logout OK");
    }
}
