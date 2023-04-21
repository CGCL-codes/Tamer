package jaolho.simplerisk.handshaking;

import java.io.Serializable;
import java.util.Hashtable;
import jaolho.simplerisk.*;
import jaolho.simplerisk.utils.Message;

public class InitialRegions implements Message, Serializable {

    private static final long serialVersionUID = -939169485696898734L;

    /** The regions and owners as Object[0: Region, 1: Player][] */
    public final Object[][] list;

    public Player[] players;

    public InitialRegions(Object[][] list, Player[] players) {
        this.list = list;
        this.players = players;
    }

    public void showInClient(Client client) {
        client.rightHandPanel.log(getNiceList());
        client.rightHandPanel.log("INITIAL REGIONS:\n");
    }

    public String getNiceList() {
        String result = "";
        for (int i = 0; i < players.length; i++) {
            result += "\n" + players[i] + "\n-------------\n";
            for (int j = 0; j < list[0].length; j++) {
                Region r = (Region) list[0][j];
                Player p = (Player) list[1][j];
                if (p.id == players[i].id) {
                    result += r + "\n";
                }
            }
            result += ("\n");
        }
        return result;
    }
}
