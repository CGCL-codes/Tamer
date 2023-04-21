package bzflagai;

import ai.Intelligence;
import ai.Reactive;
import ai.Searching;
import ai.Stupid;
import comm.Messenger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import world.World;

/**
 *
 * @author dheath
 */
public class Controller {

    private Messenger msgr;

    private List<Intelligence> bots;

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader stdin = new BufferedReader(isr);
            System.out.println("What is the IP Address of the host?\n");
            String host = stdin.readLine();
            System.out.println("What port is used?\n");
            int port = Integer.parseInt(stdin.readLine());
            System.out.println("How many tank bots?\n");
            int botNum = Integer.parseInt(stdin.readLine());
            msgr = new Messenger(host, port);
            bots = new ArrayList();
            for (int i = 0; i < botNum; i++) {
                while (true) {
                    System.out.println("Enter AI for bot: " + i);
                    System.out.println("1 - Stupid");
                    System.out.println("2 - Potential Fields");
                    System.out.println("3 - Searching");
                    String ai = stdin.readLine();
                    int learn = 0;
                    try {
                        learn = Integer.parseInt(ai);
                    } catch (NumberFormatException ex) {
                        System.out.println("choice must be a number from 1 to 2");
                        continue;
                    }
                    if (learn > 0 && learn < 4) {
                        Intelligence bot = getIntelligence(learn, i);
                        bot.setMessenger(msgr);
                        bots.add(bot);
                        break;
                    }
                }
            }
            stdin.close();
            msgr.establishConnection();
            int c = 0;
            while (c++ < 1) {
                World world = msgr.buildWorld();
                for (Intelligence bot : bots) {
                    bot.setWorld(world);
                    bot.NextMove();
                }
            }
            msgr.endConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intelligence getIntelligence(int type, int botId) {
        switch(type) {
            case 1:
                return new Stupid(botId);
            case 2:
                return new Reactive(botId);
            case 3:
                return new Searching(botId);
            default:
                return new Stupid(botId);
        }
    }
}
