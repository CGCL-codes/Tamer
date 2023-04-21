package wsmg.demo;

import java.net.URISyntaxException;
import org.exolab.jms.util.CommandLine;
import wsmg.WsntClientAPI;

/**
 * <p>Title: Ws-Notification</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Indiana University</p>
 * @author yi Huang
 * @version 1.0
 */
public class DeleteSubscription {

    public DeleteSubscription() {
    }

    public static void main(String[] args) throws URISyntaxException {
        CommandLine cmdline = new CommandLine(args);
        String subManagerLoc = cmdline.value("subManager", "localhost:12345");
        String subId = cmdline.value("subId", null);
        if (subId == null) {
            System.out.println("You need to specify the subscriptionId to be deleted using '-subId'");
            System.exit(0);
        }
        WsntClientAPI client = new WsntClientAPI();
        client.deleteSubscription(subManagerLoc, subId);
        System.out.println("Finished!");
    }
}
