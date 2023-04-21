package org.moyoman.module.endgame.simpleendgame;

import org.moyoman.debug.*;
import org.moyoman.log.*;
import org.moyoman.module.*;
import org.moyoman.module.board.*;
import org.moyoman.module.endgame.*;
import org.moyoman.module.groups.*;
import org.moyoman.module.loosegroups.*;
import org.moyoman.module.geometry.*;
import org.moyoman.util.*;
import java.util.*;

/** This module implements the Endgame interface.  It is
  * responsible for finding sequences of moves at the
  * end of the game.
  */
public class SimpleEndgame extends Module implements Endgame {

    /** The debug types that this module supports.*/
    private static DebugType[] dt;

    /** The modules that this module requires.*/
    private ModuleType[] mt;

    /** The moves that were rated by this module.*/
    private RatedMove[] rm;

    /** The last board obtained from generateMove().*/
    private Board lastBoard;

    /** The MoveDescriptorForest object containing sequences of moves.*/
    private MoveDescriptorForest mdf;

    /** This flag is used to determine when to clear variables set by generateMove().*/
    private boolean isGenerated;

    /** This flag is used to determine if a call to getSequences() is valid. */
    private boolean sequenceFlag;

    static {
        dt = new DebugType[1];
        dt[0] = new DebugType(DebugType.CONTINUOUS);
    }

    /** Create the SimpleEndgame object.
	  * @param id The id of the game.
	  * @param name The name of this module.
	  * @throws InternalErrorException Thrown if the operation fails for any reason.
	  */
    public SimpleEndgame(GameId id, ModuleName name) throws InternalErrorException {
        super(id, name);
        rm = new RatedMove[0];
        mdf = null;
        lastBoard = null;
        isGenerated = false;
        sequenceFlag = true;
        mt = new ModuleType[4];
        try {
            mt[0] = ModuleType.getModuleType("Board");
            mt[1] = ModuleType.getModuleType("Groups");
            mt[2] = ModuleType.getModuleType("LooseGroups");
            mt[3] = ModuleType.getModuleType("Geometry");
        } catch (Exception e) {
            error(e);
            mt = new ModuleType[0];
            throw new InternalErrorException(e);
        }
    }

    /** Generate the move for this module.  The side effect of
	  * calling this method is that an array of RatedMove objects
	  * is stored which is returned by the getMoves() method.
	  * @param modules An array of modules which this module needs
	  * in order to perform its work.  Those modules reflect the
	  * current state of the board but are copies of the actual modules,
	  * so this method is free to manipulate them.
	  * @param modules The modules that this module needs to make its move.
	  */
    public void generateMove(Module[] modules) {
        try {
            ModuleType btype = ModuleType.getModuleType("Board");
            Board board = (Board) Module.getModule(modules, btype);
            lastBoard = board;
            ModuleType grtype = ModuleType.getModuleType("Groups");
            Groups groups = (Groups) Module.getModule(modules, grtype);
            ModuleType lgtype = ModuleType.getModuleType("LooseGroups");
            LooseGroups lg = (LooseGroups) Module.getModule(modules, lgtype);
            ModuleType geotype = ModuleType.getModuleType("Geometry");
            Geometry geo = (Geometry) Module.getModule(modules, geotype);
            ArrayList hor = new ArrayList();
            ArrayList vert = new ArrayList();
            Color color = board.getColorToMove();
            SingleLooseGroup[] slg = lg.getSingleLooseGroups();
            for (int i = 0; i < slg.length; i++) {
                if (!slg[i].getColor().equals(color)) continue;
                if (geo.isAdjacentToSide(slg[i])) {
                    ConvexHull ch = geo.getConvexHull(slg[i]);
                    Point[] points = ch.getVertices();
                    Stone[] stones = new Stone[points.length];
                    for (int j = 0; j < points.length; j++) {
                        stones[j] = Stone.get(color, points[j]);
                    }
                    for (int j = 0; j < stones.length; j++) {
                        hor.add(stones[j]);
                        vert.add(stones[j]);
                    }
                }
            }
            for (int i = hor.size() - 1; i >= 0; i--) {
                Stone st = (Stone) hor.get(i);
                if (!isCloseToHorizontalEdge(st, board)) hor.remove(st);
            }
            for (int i = vert.size() - 1; i >= 0; i--) {
                Stone st = (Stone) vert.get(i);
                if (!isCloseToVerticalEdge(st, board)) vert.remove(st);
            }
            for (int i = hor.size() - 1; i >= 0; i--) {
                Stone st = (Stone) hor.get(i);
                if (!clearHorizontalPathToEdge(st, board)) hor.remove(st);
            }
            for (int i = vert.size() - 1; i >= 0; i--) {
                Stone st = (Stone) vert.get(i);
                if (!clearVerticalPathToEdge(st, board)) vert.remove(st);
            }
            short moveNumber = board.getMoveNumber();
            HashMap ratedMoves = new HashMap();
            evaluateMoves(board, geo, hor, color, moveNumber, true, ratedMoves);
            evaluateMoves(board, geo, vert, color, moveNumber, false, ratedMoves);
            rm = new RatedMove[ratedMoves.size()];
            Collection coll = ratedMoves.values();
            coll.toArray(rm);
            isGenerated = true;
            sequenceFlag = true;
        } catch (Exception e) {
            error(e);
            rm = new RatedMove[0];
        }
    }

    /** Generate the MoveDescriptorForest object describing sequences of moves.
	  */
    private void generateMDF() {
        mdf = new MoveDescriptorForest();
        for (int i = 0; i < rm.length; i++) {
            try {
                Board newBoard = (Board) lastBoard.clone();
                newBoard.makeMove(rm[i]);
                MoveDescriptor md = newBoard.getLastMoveDescriptor();
                mdf.addInitialMoveDescriptor(md);
            } catch (Exception e) {
                error(e);
            }
        }
    }

    /** Evaluate the moves in the list, and rate them accordingly.
	  * It is possible that the same move will get rated twice.
	  * There should only be one RatedMove object for each Stone object.
	  * @param board The Board module.
	  * @param geo The Geometry module.
	  * @param al Each element is a stone to be rated.
	  * @param color The color of the side to move.
	  * @param moveNumber The move number of the next move.
	  * @param isHorizontal indicates if the edge examined is a horizontal or vertical one.
	  * @param ratedMoves The rated moves are added to this HashMap as the value, the key is 
	  * the corresponding Stone object.
	  */
    private void evaluateMoves(Board board, Geometry geo, ArrayList al, Color color, short moveNumber, boolean isHorizontal, HashMap ratedMoves) {
        for (int i = 0; i < al.size(); i++) {
            Stone st = (Stone) al.get(i);
            int x = st.getX();
            int y = st.getY();
            Stone target;
            int distance;
            if (isHorizontal) {
                if (x > 9) {
                    distance = 18 - x;
                    target = Stone.get(color, 18, y);
                } else {
                    distance = x;
                    target = Stone.get(color, 0, y);
                }
            } else {
                if (y > 9) {
                    distance = 18 - y;
                    target = Stone.get(color, x, 18);
                } else {
                    distance = y;
                    target = Stone.get(color, x, 0);
                }
            }
            float val;
            switch(distance) {
                case 1:
                    val = 1.0f;
                    if (moveNumber < 300) {
                        val -= (float) (300 - moveNumber) * 0.005;
                    }
                    if (board.isLegal(target)) {
                        addRatedMove(geo, target, moveNumber, val, ratedMoves);
                    }
                    break;
                case 2:
                    val = 1.0f;
                    if (moveNumber < 250) {
                        val -= (float) (250 - moveNumber) * 0.006;
                    }
                    if (isHorizontal) {
                        if (target.getX() == 0) target = Stone.get(color, 1, target.getY()); else target = Stone.get(color, 17, target.getY());
                    } else {
                        if (target.getY() == 0) target = Stone.get(color, target.getX(), 1); else target = Stone.get(color, target.getY(), 17);
                    }
                    if (board.isLegal(target)) {
                        addRatedMove(geo, target, moveNumber, val, ratedMoves);
                    }
                    break;
                case 3:
                    Stone target1;
                    Stone target2;
                    val = 1.0f;
                    if (moveNumber < 200) {
                        val -= (float) (200 - moveNumber) * 0.0075;
                    }
                    if (isHorizontal) {
                        if (target.getX() == 0) {
                            target1 = Stone.get(color, 1, target.getY());
                            target2 = Stone.get(color, 2, target.getY());
                        } else {
                            target1 = Stone.get(color, 16, target.getY());
                            target2 = Stone.get(color, 17, target.getY());
                        }
                    } else {
                        if (target.getY() == 0) {
                            target1 = Stone.get(color, target.getX(), 1);
                            target2 = Stone.get(color, target.getX(), 2);
                        } else {
                            target1 = Stone.get(color, target.getX(), 16);
                            target2 = Stone.get(color, target.getX(), 17);
                        }
                    }
                    if (board.isLegal(target1)) {
                        addRatedMove(geo, target1, moveNumber, val, ratedMoves);
                    }
                    if (board.isLegal(target2)) {
                        addRatedMove(geo, target2, moveNumber, val, ratedMoves);
                    }
                    break;
                case 4:
                    val = 1.0f;
                    if (moveNumber < 100) {
                        val -= (float) (100 - moveNumber) * 0.015;
                    }
                    if (isHorizontal) {
                        if (target.getX() == 0) {
                            target1 = Stone.get(color, 2, target.getY());
                            target2 = Stone.get(color, 3, target.getY());
                        } else {
                            target1 = Stone.get(color, 15, target.getY());
                            target2 = Stone.get(color, 16, target.getY());
                        }
                    } else {
                        if (target.getY() == 0) {
                            target1 = Stone.get(color, target.getX(), 2);
                            target2 = Stone.get(color, target.getX(), 3);
                        } else {
                            target1 = Stone.get(color, target.getX(), 15);
                            target2 = Stone.get(color, target.getX(), 16);
                        }
                    }
                    if (board.isLegal(target1)) {
                        addRatedMove(geo, target1, moveNumber, val, ratedMoves);
                    }
                    if (board.isLegal(target2)) {
                        addRatedMove(geo, target2, moveNumber, val, ratedMoves);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /** Add rated moves to the HashMap if they are not in the convex hull.
	  * @param geo The Geometry module.
	  * @param target The position of the move.
	  * @param num The move number
	  * @param val The rating of the move.
	  * @param rm The HashMap to add the Stone, RatedMove pair to.
	  */
    private void addRatedMove(Geometry geo, Stone target, short num, float val, HashMap rm) {
        if (rm.containsKey(target)) {
            RatedMove old = (RatedMove) rm.get(target);
            val += old.getRating();
            if (val > 1.0f) val = 1.0f;
        }
        NumberedStone ns = new NumberedStone(num, target);
        RatedMove rmove = new RatedMove(ns, val, CONFIDENCE);
        rm.put(target, rmove);
    }

    /** Determine if a stone is close to a horizontal edge.
	  * Stones which are on the horizontal edge fail this test.
	  * This method does not consider if there are stones between	
	  * it and the edge, just the distance.
	  * @param st The stone being tested.
	  * @param board The Board module.
	  * @return true if the stone is close to the horizontal edge, or false.
	  */
    private boolean isCloseToHorizontalEdge(Stone st, Board board) {
        boolean flag = true;
        int x = st.getX();
        if ((x == 0) || (x == 18)) flag = false; else if ((x > 4) && (x < 14)) flag = false;
        return flag;
    }

    /** Determine if a stone is close to a vertical edge.
	  * This method does not consider if there are stones between	
	  * it and the edge, just the distance.
	  * @param st The stone being tested.
	  * @param board The Board module.
	  * @return true if the stone is close to the vertical edge, or false.
	  */
    private boolean isCloseToVerticalEdge(Stone st, Board board) {
        boolean flag = true;
        int y = st.getY();
        if ((y == 0) || (y == 18)) flag = false; else if ((y > 4) && (y < 14)) flag = false;
        return flag;
    }

    /** Determine if the stone has a clear path to the nearest horizontal edge.
	  * @param st The stone being tested.
	  * @param board The Board module.
	  * @return true if the stone has a clear path, or false.
	  */
    private boolean clearHorizontalPathToEdge(Stone st, Board board) {
        boolean flag = true;
        int x = st.getX();
        int y = st.getY();
        if (x < 9) {
            for (int i = 0; i < x; i++) {
                if (!board.getColor(Point.get(i, y)).equals(Color.EMPTY)) {
                    flag = false;
                    break;
                }
            }
        } else {
            for (int i = x + 1; i <= 18; i++) {
                if (!board.getColor(Point.get(i, y)).equals(Color.EMPTY)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /** Determine if there is a clear path from the stone to the vertical edge.
	  * @param st The stone being tested.
	  * @param board The Board module.
	  * @return true if there is a clear path, or false.
	  */
    private boolean clearVerticalPathToEdge(Stone st, Board board) {
        boolean flag = false;
        int x = st.getX();
        int y = st.getY();
        if (y < 9) {
            for (int i = 0; i < y; i++) {
                if (!board.getColor(Point.get(x, i)).equals(Color.EMPTY)) {
                    flag = false;
                    break;
                }
            }
        } else {
            for (int i = y + 1; i <= 18; i++) {
                if (!board.getColor(Point.get(x, i)).equals(Color.EMPTY)) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /** Return the debug information for this module.
	  * @param types Only return a subset of the debug types in this array.
	  * @return The debug information that this module supports.
	  */
    public Debug[] getDebugInformation(DebugType[] types) {
        if (rm.length > 0) {
            Continuous cont = new Continuous("DEBUG_RECOMMENDED");
            cont.addMoves(rm);
            Debug[] debug = new Debug[1];
            debug[0] = cont;
            return debug;
        } else {
            return new Debug[0];
        }
    }

    /** Return the types of debug information that this module supports.
	  * @return The types of debug information that this module supports.
	  */
    public DebugType[] getDebugTypes() {
        return dt;
    }

    /** Return the moves generated by the module.
	  * Note that both good and bad moves can be returned.
	  * @return An array of RatedMove objects.
	  */
    public RatedMove[] getMoves() {
        return rm;
    }

    /** Return the modules that this module requires to perform its task.
	  * @return An array of module types that the module requires.
	  */
    public ModuleType[] getRequiredModuleList() {
        return mt;
    }

    /** Update the internal data structures of this module with the new move.
	  * Note that this method is called both when the client makes a move,
	  * and when the server makes a move.
	  * @param move The move that was just made.
	  * @param modules The modules that this method needs to perform its job.
	  */
    public void makeMove(Move move, Module[] modules) {
        if (!isGenerated) {
            rm = new RatedMove[0];
            sequenceFlag = false;
        }
        isGenerated = false;
        lastBoard = null;
    }

    /** Get sequences of moves that might occur in endgame play.
	  * The sequences are not considered to be equally good.
	  * Use the getMoves() method to determine which move this
	  * module considers to be best.
	  * @return A MoveDescriptorForest object which contains sequences
	  * of moves for endgame play.	
	  * @throws NoSuchDataException Thrown if the last move made was not generated by this player.
	  */
    public MoveDescriptorForest getSequences() throws NoSuchDataException {
        if (!sequenceFlag) {
            throw new NoSuchDataException("Last move not made by this player.");
        } else {
            if (mdf == null) generateMDF();
            return mdf;
        }
    }

    /** Clone the module.
	  * @return The A SimpleEndgame object which is a clone of this one.
	  */
    public Object clone() {
        SimpleEndgame se = (SimpleEndgame) super.clone();
        se.rm = new RatedMove[rm.length];
        for (int i = 0; i < rm.length; i++) {
            se.rm[i] = (RatedMove) rm[i].clone();
        }
        if (lastBoard != null) se.lastBoard = (Board) lastBoard.clone();
        if (mdf != null) se.mdf = (MoveDescriptorForest) mdf.clone();
        return se;
    }
}
