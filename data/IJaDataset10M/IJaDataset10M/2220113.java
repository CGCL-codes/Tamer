package com.momosw.games.engine.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.momosw.games.engine.Game;
import com.momosw.games.engine.player.Player;
import com.momosw.games.jericho.board.Card;
import com.momosw.games.jericho.board.WallType;

/**
 * <b>Project:</b> JerichoCardGame<br />
 * <b>Package:</b> com.momosw.games.engine.util<br />
 * <b>Class:</b> WallMeasurement.java<br />
 * <br />
 * <i>GSI 2011</i><br />
 *
 * @author Miguel Coronado (miguelcb84@gmail.com)
 * @version	Aug 21, 2011
 *
 */
public class WallMeasurement {

    Map<WallType, LongestWall> wallReport;

    public WallMeasurement(Game game) {
        wallReport = new HashMap<WallType, LongestWall>();
        for (Player player : game.getPlayers()) {
            for (WallType wallType : WallType.getTypes()) {
                List<Card> wall = game.getBoard().getWall(player, wallType);
                this.setWall(player, wallType, wall);
            }
        }
    }

    public Set<Player> getLongestPlayer(WallType wallType) {
        LongestWall lw = this.wallReport.get(wallType);
        if (lw == null) {
            return new HashSet<Player>();
        }
        return lw.players;
    }

    private void setWall(Player player, WallType wallType, List<Card> wall) {
        int lenght = wallLenght(wall);
        if (this.wallReport.get(wallType) == null) {
            if (lenght != 0) {
                wallReport.put(wallType, new LongestWall(player, wallType, lenght));
            }
            return;
        }
        LongestWall longest = wallReport.get(wallType);
        longest.update(player, lenght);
    }

    /** */
    public static int wallLenght(List<Card> cards) {
        int lenght = 0;
        for (Card card : cards) {
            lenght += card.getLenght();
        }
        return lenght;
    }
}

class LongestWall {

    Set<Player> players = null;

    WallType wallType = null;

    int lenght = 0;

    LongestWall() {
        players = new HashSet<Player>();
    }

    LongestWall(Player player, WallType wallType, int lenght) {
        players = new HashSet<Player>();
        players.add(player);
        this.wallType = wallType;
        this.lenght = lenght;
    }

    void update(Player player, int currentLenght) {
        if (currentLenght < this.lenght) {
            return;
        }
        if (currentLenght == this.lenght) {
            this.players.add(player);
            return;
        }
        this.players.clear();
        this.players.add(player);
        this.lenght = currentLenght;
    }

    public String toString() {
        return lenght + ":=>" + players.toString();
    }
}
