package mu.nu.nullpo.game.subsystem.ai;

import mu.nu.nullpo.game.component.Controller;
import mu.nu.nullpo.game.component.Field;
import mu.nu.nullpo.game.component.Piece;
import mu.nu.nullpo.game.component.WallkickResult;
import mu.nu.nullpo.game.play.GameEngine;
import mu.nu.nullpo.game.play.GameManager;
import org.apache.log4j.Logger;

/**
 * 普通のAI
 */
public class BasicAI extends DummyAI implements Runnable {

    /** Log */
    static Logger log = Logger.getLogger(BasicAI.class);

    /** 接地したあとのX-coordinate */
    public int bestXSub;

    /** 接地したあとのY-coordinate */
    public int bestYSub;

    /** 接地したあとのDirection(-1: None) */
    public int bestRtSub;

    /** 最善手のEvaluation score */
    public int bestPts;

    /** 移動を遅らせる用の変count */
    public int delay;

    /** The GameEngine that owns this AI */
    public GameEngine gEngine;

    /** The GameManager that owns this AI */
    public GameManager gManager;

    /** When true,スレッドにThink routineの実行を指示 */
    public boolean thinkRequest;

    /** true when thread is executing the think routine. */
    public boolean thinking;

    /** スレッドを停止させる time */
    public int thinkDelay;

    /** When true,スレッド動作中 */
    public volatile boolean threadRunning;

    /** Thread for executing the think routine */
    public Thread thread;

    @Override
    public String getName() {
        return "BASIC";
    }

    @Override
    public void init(GameEngine engine, int playerID) {
        delay = 0;
        gEngine = engine;
        gManager = engine.owner;
        thinkRequest = false;
        thinking = false;
        threadRunning = false;
        if (((thread == null) || !thread.isAlive()) && (engine.aiUseThread)) {
            thread = new Thread(this, "AI_" + playerID);
            thread.setDaemon(true);
            thread.start();
            thinkDelay = engine.aiThinkDelay;
            thinkCurrentPieceNo = 0;
            thinkLastPieceNo = 0;
        }
    }

    @Override
    public void shutdown(GameEngine engine, int playerID) {
        if ((thread != null) && (thread.isAlive())) {
            thread.interrupt();
            threadRunning = false;
            thread = null;
        }
    }

    @Override
    public void newPiece(GameEngine engine, int playerID) {
        if (!engine.aiUseThread) {
            thinkBestPosition(engine, playerID);
        } else {
            thinkRequest = true;
            thinkCurrentPieceNo++;
        }
    }

    @Override
    public void onFirst(GameEngine engine, int playerID) {
    }

    @Override
    public void onLast(GameEngine engine, int playerID) {
    }

    @Override
    public void setControl(GameEngine engine, int playerID, Controller ctrl) {
        if ((engine.nowPieceObject != null) && (engine.stat == GameEngine.STAT_MOVE) && (delay >= engine.aiMoveDelay) && (engine.statc[0] > 0) && (!engine.aiUseThread || (threadRunning && !thinking && (thinkCurrentPieceNo <= thinkLastPieceNo)))) {
            int input = 0;
            Piece pieceNow = engine.nowPieceObject;
            int nowX = engine.nowPieceX;
            int nowY = engine.nowPieceY;
            int rt = pieceNow.direction;
            Field fld = engine.field;
            boolean pieceTouchGround = pieceNow.checkCollision(nowX, nowY + 1, fld);
            if ((bestHold || forceHold) && engine.isHoldOK()) {
                input |= Controller.BUTTON_BIT_D;
            } else {
                if (rt != bestRt) {
                    int lrot = engine.getRotateDirection(-1);
                    int rrot = engine.getRotateDirection(1);
                    if ((Math.abs(rt - bestRt) == 2) && (engine.ruleopt.rotateButtonAllowDouble) && !ctrl.isPress(Controller.BUTTON_E)) {
                        input |= Controller.BUTTON_BIT_E;
                    } else if (!ctrl.isPress(Controller.BUTTON_B) && engine.ruleopt.rotateButtonAllowReverse && !engine.isRotateButtonDefaultRight() && (bestRt == rrot)) {
                        input |= Controller.BUTTON_BIT_B;
                    } else if (!ctrl.isPress(Controller.BUTTON_B) && engine.ruleopt.rotateButtonAllowReverse && engine.isRotateButtonDefaultRight() && (bestRt == lrot)) {
                        input |= Controller.BUTTON_BIT_B;
                    } else if (!ctrl.isPress(Controller.BUTTON_A)) {
                        input |= Controller.BUTTON_BIT_A;
                    }
                }
                int minX = pieceNow.getMostMovableLeft(nowX, nowY, rt, fld);
                int maxX = pieceNow.getMostMovableRight(nowX, nowY, rt, fld);
                if (((bestX < minX - 1) || (bestX > maxX + 1) || (bestY < nowY)) && (rt == bestRt)) {
                    thinkRequest = true;
                } else {
                    if ((nowX == bestX) && (pieceTouchGround) && (rt == bestRt)) {
                        if (bestRtSub != -1) {
                            bestRt = bestRtSub;
                            bestRtSub = -1;
                        }
                        if (bestX != bestXSub) {
                            bestX = bestXSub;
                            bestY = bestYSub;
                        }
                    }
                    if (nowX > bestX) {
                        if (!ctrl.isPress(Controller.BUTTON_LEFT) || (engine.aiMoveDelay >= 0)) input |= Controller.BUTTON_BIT_LEFT;
                    } else if (nowX < bestX) {
                        if (!ctrl.isPress(Controller.BUTTON_RIGHT) || (engine.aiMoveDelay >= 0)) input |= Controller.BUTTON_BIT_RIGHT;
                    } else if ((nowX == bestX) && (rt == bestRt)) {
                        if ((bestRtSub == -1) && (bestX == bestXSub)) {
                            if (engine.ruleopt.harddropEnable && !ctrl.isPress(Controller.BUTTON_UP)) input |= Controller.BUTTON_BIT_UP; else if (engine.ruleopt.softdropEnable || engine.ruleopt.softdropLock) input |= Controller.BUTTON_BIT_DOWN;
                        } else {
                            if (engine.ruleopt.harddropEnable && !engine.ruleopt.harddropLock && !ctrl.isPress(Controller.BUTTON_UP)) input |= Controller.BUTTON_BIT_UP; else if (engine.ruleopt.softdropEnable && !engine.ruleopt.softdropLock) input |= Controller.BUTTON_BIT_DOWN;
                        }
                    }
                }
            }
            delay = 0;
            ctrl.setButtonBit(input);
        } else {
            delay++;
            ctrl.setButtonBit(0);
        }
    }

    /**
	 * Search for the best choice
	 * @param engine The GameEngine that owns this AI
	 * @param playerID Player ID
	 */
    public void thinkBestPosition(GameEngine engine, int playerID) {
        bestHold = false;
        bestX = 0;
        bestY = 0;
        bestRt = 0;
        bestXSub = 0;
        bestYSub = 0;
        bestRtSub = -1;
        bestPts = 0;
        forceHold = false;
        Piece pieceNow = engine.nowPieceObject;
        int nowX = engine.nowPieceX;
        int nowY = engine.nowPieceY;
        boolean holdOK = engine.isHoldOK();
        boolean holdEmpty = false;
        Piece pieceHold = engine.holdPieceObject;
        Piece pieceNext = engine.getNextObject(engine.nextPieceCount);
        if (pieceHold == null) {
            holdEmpty = true;
        }
        Field fld = new Field(engine.field);
        for (int depth = 0; depth < getMaxThinkDepth(); depth++) {
            for (int rt = 0; rt < Piece.DIRECTION_COUNT; rt++) {
                int minX = pieceNow.getMostMovableLeft(nowX, nowY, rt, engine.field);
                int maxX = pieceNow.getMostMovableRight(nowX, nowY, rt, engine.field);
                for (int x = minX; x <= maxX; x++) {
                    fld.copy(engine.field);
                    int y = pieceNow.getBottom(x, nowY, rt, fld);
                    if (!pieceNow.checkCollision(x, y, rt, fld)) {
                        int pts = thinkMain(engine, x, y, rt, -1, fld, pieceNow, pieceNext, pieceHold, depth);
                        if (pts >= bestPts) {
                            bestHold = false;
                            bestX = x;
                            bestY = y;
                            bestRt = rt;
                            bestXSub = x;
                            bestYSub = y;
                            bestRtSub = -1;
                            bestPts = pts;
                        }
                        if ((depth > 0) || (bestPts <= 10) || (pieceNow.id == Piece.PIECE_T)) {
                            fld.copy(engine.field);
                            if (!pieceNow.checkCollision(x - 1, y, rt, fld) && pieceNow.checkCollision(x - 1, y - 1, rt, fld)) {
                                pts = thinkMain(engine, x - 1, y, rt, -1, fld, pieceNow, pieceNext, pieceHold, depth);
                                if (pts > bestPts) {
                                    bestHold = false;
                                    bestX = x;
                                    bestY = y;
                                    bestRt = rt;
                                    bestXSub = x - 1;
                                    bestYSub = y;
                                    bestRtSub = -1;
                                    bestPts = pts;
                                }
                            }
                            fld.copy(engine.field);
                            if (!pieceNow.checkCollision(x + 1, y, rt, fld) && pieceNow.checkCollision(x + 1, y - 1, rt, fld)) {
                                pts = thinkMain(engine, x + 1, y, rt, -1, fld, pieceNow, pieceNext, pieceHold, depth);
                                if (pts > bestPts) {
                                    bestHold = false;
                                    bestX = x;
                                    bestY = y;
                                    bestRt = rt;
                                    bestXSub = x + 1;
                                    bestYSub = y;
                                    bestRtSub = -1;
                                    bestPts = pts;
                                }
                            }
                            if (!engine.isRotateButtonDefaultRight() || engine.ruleopt.rotateButtonAllowReverse) {
                                int rot = pieceNow.getRotateDirection(-1, rt);
                                int newX = x;
                                int newY = y;
                                fld.copy(engine.field);
                                pts = 0;
                                if (!pieceNow.checkCollision(x, y, rot, fld)) {
                                    pts = thinkMain(engine, x, y, rot, rt, fld, pieceNow, pieceNext, pieceHold, depth);
                                } else if ((engine.wallkick != null) && (engine.ruleopt.rotateWallkick)) {
                                    boolean allowUpward = (engine.ruleopt.rotateMaxUpwardWallkick < 0) || (engine.nowUpwardWallkickCount < engine.ruleopt.rotateMaxUpwardWallkick);
                                    WallkickResult kick = engine.wallkick.executeWallkick(x, y, -1, rt, rot, allowUpward, pieceNow, fld, null);
                                    if (kick != null) {
                                        newX = x + kick.offsetX;
                                        newY = y + kick.offsetY;
                                        pts = thinkMain(engine, newX, newY, rot, rt, fld, pieceNow, pieceNext, pieceHold, depth);
                                    }
                                }
                                if (pts > bestPts) {
                                    bestHold = false;
                                    bestX = x;
                                    bestY = y;
                                    bestRt = rt;
                                    bestXSub = newX;
                                    bestYSub = newY;
                                    bestRtSub = rot;
                                    bestPts = pts;
                                }
                            }
                            if (engine.isRotateButtonDefaultRight() || engine.ruleopt.rotateButtonAllowReverse) {
                                int rot = pieceNow.getRotateDirection(1, rt);
                                int newX = x;
                                int newY = y;
                                fld.copy(engine.field);
                                pts = 0;
                                if (!pieceNow.checkCollision(x, y, rot, fld)) {
                                    pts = thinkMain(engine, x, y, rot, rt, fld, pieceNow, pieceNext, pieceHold, depth);
                                } else if ((engine.wallkick != null) && (engine.ruleopt.rotateWallkick)) {
                                    boolean allowUpward = (engine.ruleopt.rotateMaxUpwardWallkick < 0) || (engine.nowUpwardWallkickCount < engine.ruleopt.rotateMaxUpwardWallkick);
                                    WallkickResult kick = engine.wallkick.executeWallkick(x, y, 1, rt, rot, allowUpward, pieceNow, fld, null);
                                    if (kick != null) {
                                        newX = x + kick.offsetX;
                                        newY = y + kick.offsetY;
                                        pts = thinkMain(engine, newX, newY, rot, rt, fld, pieceNow, pieceNext, pieceHold, depth);
                                    }
                                }
                                if (pts > bestPts) {
                                    bestHold = false;
                                    bestX = x;
                                    bestY = y;
                                    bestRt = rt;
                                    bestXSub = newX;
                                    bestYSub = newY;
                                    bestRtSub = rot;
                                    bestPts = pts;
                                }
                            }
                            if (engine.ruleopt.rotateButtonAllowDouble) {
                                int rot = pieceNow.getRotateDirection(2, rt);
                                int newX = x;
                                int newY = y;
                                fld.copy(engine.field);
                                pts = 0;
                                if (!pieceNow.checkCollision(x, y, rot, fld)) {
                                    pts = thinkMain(engine, x, y, rot, rt, fld, pieceNow, pieceNext, pieceHold, depth);
                                } else if ((engine.wallkick != null) && (engine.ruleopt.rotateWallkick)) {
                                    boolean allowUpward = (engine.ruleopt.rotateMaxUpwardWallkick < 0) || (engine.nowUpwardWallkickCount < engine.ruleopt.rotateMaxUpwardWallkick);
                                    WallkickResult kick = engine.wallkick.executeWallkick(x, y, 2, rt, rot, allowUpward, pieceNow, fld, null);
                                    if (kick != null) {
                                        newX = x + kick.offsetX;
                                        newY = y + kick.offsetY;
                                        pts = thinkMain(engine, newX, newY, rot, rt, fld, pieceNow, pieceNext, pieceHold, depth);
                                    }
                                }
                                if (pts > bestPts) {
                                    bestHold = false;
                                    bestX = x;
                                    bestY = y;
                                    bestRt = rt;
                                    bestXSub = newX;
                                    bestYSub = newY;
                                    bestRtSub = rot;
                                    bestPts = pts;
                                }
                            }
                        }
                    }
                }
                if (pieceHold == null) {
                    pieceHold = engine.getNextObject(engine.nextPieceCount);
                }
                if ((holdOK == true) && (pieceHold != null) && (depth == 0)) {
                    int spawnX = engine.getSpawnPosX(engine.field, pieceHold);
                    int spawnY = engine.getSpawnPosY(pieceHold);
                    int minHoldX = pieceHold.getMostMovableLeft(spawnX, spawnY, rt, engine.field);
                    int maxHoldX = pieceHold.getMostMovableRight(spawnX, spawnY, rt, engine.field);
                    for (int x = minHoldX; x <= maxHoldX; x++) {
                        fld.copy(engine.field);
                        int y = pieceHold.getBottom(x, spawnY, rt, fld);
                        if (!pieceHold.checkCollision(x, y, rt, fld)) {
                            Piece pieceNext2 = engine.getNextObject(engine.nextPieceCount);
                            if (holdEmpty) pieceNext2 = engine.getNextObject(engine.nextPieceCount + 1);
                            int pts = thinkMain(engine, x, y, rt, -1, fld, pieceHold, pieceNext2, null, depth);
                            if (pts > bestPts) {
                                bestHold = true;
                                bestX = x;
                                bestY = y;
                                bestRt = rt;
                                bestRtSub = -1;
                                bestPts = pts;
                            }
                        }
                    }
                }
            }
            if (bestPts > 0) break;
        }
        thinkLastPieceNo++;
    }

    /**
	 * Think routine
	 * @param engine GameEngine
	 * @param x X-coordinate
	 * @param y Y-coordinate
	 * @param rt Direction
	 * @param rtOld Direction before rotation (-1: None）
	 * @param fld Field (Can be modified without problems)
	 * @param piece Piece
	 * @param nextpiece NEXTピース
	 * @param holdpiece HOLDピース(nullの場合あり)
	 * @param depth Compromise level (ranges from 0 through getMaxThinkDepth-1)
	 * @return Evaluation score
	 */
    public int thinkMain(GameEngine engine, int x, int y, int rt, int rtOld, Field fld, Piece piece, Piece nextpiece, Piece holdpiece, int depth) {
        int pts = 0;
        if (piece.checkCollision(x - 1, y, fld)) pts += 1;
        if (piece.checkCollision(x + 1, y, fld)) pts += 1;
        if (piece.checkCollision(x, y - 1, fld)) pts += 100;
        int holeBefore = fld.getHowManyHoles();
        int lidBefore = fld.getHowManyLidAboveHoles();
        int needIValleyBefore = fld.getTotalValleyNeedIPiece();
        int heightBefore = fld.getHighestBlockY();
        boolean tspin = false;
        if ((piece.id == Piece.PIECE_T) && (rtOld != -1) && (fld.isTSpinSpot(x, y, piece.big))) {
            tspin = true;
        }
        if (!piece.placeToField(x, y, rt, fld)) {
            return 0;
        }
        int lines = fld.checkLine();
        if (lines > 0) {
            fld.clearLine();
            fld.downFloatingBlocks();
        }
        boolean allclear = fld.isEmpty();
        if (allclear) pts += 500000;
        int heightAfter = fld.getHighestBlockY();
        boolean danger = (heightAfter <= 12);
        if ((!danger) && (depth == 0)) pts += y * 10; else pts += y * 20;
        if ((lines == 1) && (!danger) && (depth == 0) && (heightAfter >= 16) && (holeBefore < 3) && (!tspin) && (engine.combo < 1)) {
            return 0;
        }
        if ((!danger) && (depth == 0)) {
            if (lines == 1) pts += 10;
            if (lines == 2) pts += 50;
            if (lines == 3) pts += 100;
            if (lines >= 4) pts += 100000;
        } else {
            if (lines == 1) pts += 5000;
            if (lines == 2) pts += 10000;
            if (lines == 3) pts += 30000;
            if (lines >= 4) pts += 100000;
        }
        if ((lines < 4) && (!allclear)) {
            int holeAfter = fld.getHowManyHoles();
            int lidAfter = fld.getHowManyLidAboveHoles();
            int needIValleyAfter = fld.getTotalValleyNeedIPiece();
            if (holeAfter > holeBefore) {
                pts -= (holeAfter - holeBefore) * 10;
                if (depth == 0) return 0;
            } else if (holeAfter < holeBefore) {
                if (!danger) pts += (holeBefore - holeAfter) * 5; else pts += (holeBefore - holeAfter) * 10;
            }
            if (lidAfter > lidBefore) {
                if (!danger) pts -= (lidAfter - lidBefore) * 10; else pts -= (lidAfter - lidBefore) * 20;
            } else if (lidAfter < lidBefore) {
                if (!danger) pts += (lidBefore - lidAfter) * 10; else pts += (lidBefore - lidAfter) * 20;
            }
            if ((tspin) && (lines >= 1)) {
                pts += 100000 * lines;
            }
            if ((needIValleyAfter > needIValleyBefore) && (needIValleyAfter >= 2)) {
                pts -= (needIValleyAfter - needIValleyBefore) * 10;
                if (depth == 0) return 0;
            } else if (needIValleyAfter < needIValleyBefore) {
                if ((depth == 0) && (!danger)) pts += (needIValleyBefore - needIValleyAfter) * 10; else pts += (needIValleyBefore - needIValleyAfter) * 20;
            }
            if (heightBefore < heightAfter) {
                if ((depth == 0) && (!danger)) pts += (heightAfter - heightBefore) * 10; else pts += (heightAfter - heightBefore) * 20;
            } else if (heightBefore > heightAfter) {
                if ((depth > 0) || (danger)) pts -= (heightBefore - heightAfter) * 4;
            }
            if ((lines >= 1) && (engine.comboType != GameEngine.COMBO_TYPE_DISABLE)) {
                pts += lines * engine.combo * 100;
            }
        }
        return pts;
    }

    /**
	 * Maximum妥協 levelを取得
	 * @return Maximum妥協 level
	 */
    public int getMaxThinkDepth() {
        return 2;
    }

    public void run() {
        log.info("BasicAI: Thread start");
        threadRunning = true;
        while (threadRunning) {
            if (thinkRequest) {
                thinkRequest = false;
                thinking = true;
                try {
                    thinkBestPosition(gEngine, gEngine.playerID);
                } catch (Throwable e) {
                    log.debug("BasicAI: thinkBestPosition Failed", e);
                }
                thinking = false;
            }
            if (thinkDelay > 0) {
                try {
                    Thread.sleep(thinkDelay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        threadRunning = false;
        log.info("BasicAI: Thread end");
    }
}
