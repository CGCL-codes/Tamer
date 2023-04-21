package mu.nu.nullpo.game.subsystem.ai;

import mu.nu.nullpo.game.component.Controller;
import mu.nu.nullpo.game.play.GameEngine;

/**
 * DummyAI - Base class for AI players
 */
public class DummyAI implements AIPlayer {

    /** ホールド使用予定 */
    public boolean bestHold;

    /** 置く予定のX-coordinate */
    public int bestX;

    /** 置く予定のY-coordinate */
    public int bestY;

    /** 置く予定のDirection */
    public int bestRt;

    /** 強制ホールド */
    public boolean forceHold;

    /** Current piece number */
    public int thinkCurrentPieceNo;

    /** 思考が終わったピースの number */
    public int thinkLastPieceNo;

    /** Did the thinking thread finish successfully? */
    public boolean thinkComplete;

    public String getName() {
        return "DummyAI";
    }

    public void init(GameEngine engine, int playerID) {
    }

    public void newPiece(GameEngine engine, int playerID) {
    }

    public void onFirst(GameEngine engine, int playerID) {
    }

    public void onLast(GameEngine engine, int playerID) {
    }

    public void renderState(GameEngine engine, int playerID) {
    }

    public void setControl(GameEngine engine, int playerID, Controller ctrl) {
    }

    public void shutdown(GameEngine engine, int playerID) {
    }

    public void renderHint(GameEngine engine, int playerID) {
    }
}
