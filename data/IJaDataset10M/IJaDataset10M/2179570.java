package logic.common.player;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player> {

    private static PlayerComparator comp = new PlayerComparator();

    private PlayerComparator() {
    }

    public static PlayerComparator get() {
        return comp;
    }

    @Override
    public int compare(Player a, Player b) {
        if (a.getKills() > b.getKills()) return -1; else if (a.getKills() < b.getKills()) return 1; else if (a.getDeaths() > b.getDeaths()) return 1; else if (a.getDeaths() < b.getDeaths()) return -1;
        return 0;
    }
}
