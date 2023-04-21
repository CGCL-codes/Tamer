package be.jabapage.snooker.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import be.jabapage.snooker.jdo.administration.Club;
import be.jabapage.snooker.jdo.administration.Player;

@Data
public class ClubContainer implements Serializable {

    private static final long serialVersionUID = 1202983530637279896L;

    private Club club;

    public ClubContainer() {
        teams = new HashMap<String, TeamContainer>();
        backupPlayers = new HashMap<String, Player>();
    }

    private Map<String, TeamContainer> teams;

    private Map<String, Player> backupPlayers;
}
