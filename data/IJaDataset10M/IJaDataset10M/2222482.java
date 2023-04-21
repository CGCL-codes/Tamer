package system;

import client.ClientAction;
import exceptions.MatchException;
import exceptions.TeamException;
import java.util.Map;
import java.util.Vector;

/**
 * The match administration interface. It defines how the client can interact with the system for adding some elements
	* or retrieving data from it.
 */
public interface IMatchAdmin {

    public void addPlayer(String teamName, String playerName, String playerFirstName) throws MatchException, TeamException;

    public void addPlayer(String teamName, String playerName, String playerFirstName, int number) throws MatchException, TeamException;

    public Vector<Vector<ClientAction>> getHostActions();

    public Vector<Vector<ClientAction>> getGuestActions();

    public int getHostScore();

    public int getGuestScore();

    public String getHostName();

    public String getGuestName();

    public int getId();

    public Map<Integer, String> getHostRealPlayers();

    public Map<Integer, String> getGuestRealPlayers();

    public boolean isSaved();
}
