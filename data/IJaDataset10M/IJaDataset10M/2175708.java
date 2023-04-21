package kadathMud.commands;

import java.util.ArrayList;
import java.util.StringTokenizer;
import kadathMud.network.Connection;

public class Where extends Command {

    @Override
    public void run() {
        StringTokenizer tokenizer = new StringTokenizer(arguments);
        if (tokenizer.hasMoreTokens()) {
            ArrayList<String> whereList = new ArrayList<String>();
            whereList.add("Players in your zone:");
            whereList.add("---------------------");
            for (Connection con : connection.getMud().getConnectionManager().getConnectionList()) {
                if (con != connection && connection.getMud().getWorld().getRoom(connection.getPlayer().getRoom()).getZone() == con.getMud().getWorld().getRoom(con.getPlayer().getRoom()).getZone()) {
                    whereList.add(con.getConnectionName() + " - " + con.getMud().getWorld().getRoom(con.getPlayer().getRoom()).getRoomName());
                }
            }
            connection.receive(whereList.toString());
        } else {
            Connection wanted = connection.getMud().getConnectionManager().getConnection(tokenizer.nextToken().toLowerCase());
            if (wanted != null) {
                connection.receive(wanted.getConnectionName() + " - " + connection.getMud().getWorld().getRoom(wanted.getPlayer().getRoom()).getRoomName());
            } else {
                connection.receive("There is no such player online right now.");
            }
        }
    }
}
