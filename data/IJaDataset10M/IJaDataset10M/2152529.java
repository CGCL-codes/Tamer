package jsh.game;

import jsh.map.*;
import org.apache.log4j.*;

public class Stealer extends Piece {

    static Logger logger = Logger.getLogger(Stealer.class);

    static final int TurnCost[][] = { { -1, 1, 0, 0 }, { 1, -1, 0, 0 }, { 0, 0, -1, 1 }, { 0, 0, 1, -1 } };

    static final int MoveCost[][] = { { 1, 2, 2, 2, 1, -1, 1, -1 }, { 2, 1, 2, 2, -1, 1, -1, 1 }, { 2, 2, 1, 2, 1, 1, -1, -1 }, { 2, 2, 2, 1, -1, -1, 1, 1 } };

    static final int NextFacing[][] = { { Game.DirN, Game.DirS, Game.DirE, Game.DirW, Game.DirN, Game.DirE, Game.DirN, Game.DirW }, { Game.DirN, Game.DirS, Game.DirE, Game.DirW, Game.DirE, Game.DirS, Game.DirW, Game.DirS }, { Game.DirN, Game.DirS, Game.DirE, Game.DirW, Game.DirE, Game.DirE, Game.DirN, Game.DirS }, { Game.DirN, Game.DirS, Game.DirE, Game.DirW, Game.DirN, Game.DirS, Game.DirW, Game.DirW } };

    public boolean isMarine() {
        return false;
    }

    public boolean isBlip() {
        return false;
    }

    public boolean isStealer() {
        return true;
    }

    public boolean isStealerPlayer() {
        return true;
    }

    public void reset() {
        super.reset();
        ActionPoints = 6;
    }

    static Tile[] tiles;

    static {
        tiles = new Tile[4];
        tiles[Game.FacingNorthIdx] = new Tile(Palette.TilesDirectory + "StealerN.png");
        tiles[Game.FacingEastIdx] = new Tile(Palette.TilesDirectory + "StealerE.png");
        tiles[Game.FacingSouthIdx] = new Tile(Palette.TilesDirectory + "StealerS.png");
        tiles[Game.FacingWestIdx] = new Tile(Palette.TilesDirectory + "StealerW.png");
    }

    public void die() {
        super.die();
        Sound.play("deadstealer");
        Mission.stealerkilled();
    }

    public Stealer() {
        reset();
        CloseAssaultDie = 3;
    }

    public Tile getTile() {
        return tiles[Game.facingToIndex(facing)];
    }

    public int costToMove(int dir) {
        return MoveCost[Game.facingToIndex(facing)][dir];
    }

    public int costToTurn(int dir) {
        return TurnCost[Game.facingToIndex(facing)][dir];
    }

    public boolean canOpenDoor(int dir) {
        return isInFrontArc(dir);
    }

    public boolean canMoveTo(int dir) {
        logger.debug("canMoveTo: Cost is: " + MoveCost[Game.facingToIndex(facing)][dir]);
        logger.debug("canMoveTo: Facing: " + facing + "(" + Game.facingToIndex(facing) + ") moving to " + dir);
        return MoveCost[Game.facingToIndex(facing)][dir] != -1;
    }

    public int getNextFacing(int dir) {
        return NextFacing[Game.facingToIndex(facing)][dir];
    }
}
