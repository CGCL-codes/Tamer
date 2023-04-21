package jpm.closeAction.logic.vessel;

import java.util.List;
import jpm.closeAction.logic.GameState;
import jpm.closeAction.logic.Plot;
import jtbs.logic.CellId;
import jtbs.logic.Player;

public class Unit extends jtbs.logic.Unit {

    String name;

    Player player;

    private CellId position;

    GameState gameState;

    private List<Plot> plots;

    public Unit(int id, CellId cellId) {
        super(id, cellId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CellId getPosition() {
        return position;
    }

    public void setPosition(CellId position) {
        this.position = position;
    }

    public int getPlottedSpeed() {
        return plots.get(gameState.getTurn()).getPlottedSpeed();
    }

    @Override
    public Player getOwner() {
        return null;
    }

    @Override
    public void refresh() {
    }
}
