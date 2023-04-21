package org.moyoman.comm.client;

import org.moyoman.framework.ServerConfig;
import org.moyoman.log.*;
import org.moyoman.util.*;
import java.io.*;
import java.util.*;

/** This class manages the play of a single game of Go
  */
public class PlayerManager {

    /** The player playing black.*/
    private Player blackPlayer;

    /** The player playing white.*/
    private Player whitePlayer;

    /** The color of the player to move next.*/
    private Color toMove;

    /** If true, then play pauses after each move.*/
    private boolean singleStepFlag = false;

    /** If singleStepFlag is true, then play pauses until this variable is set to true.*/
    private boolean continueFlag = false;

    private CommandExecutor ce;

    /** Create the PlayerManager object.
	  * @param black The Player which is playing black.
	  * @param white The Player which is playing white.
	  */
    public PlayerManager(Player black, Player white) throws InternalErrorException {
        blackPlayer = black;
        whitePlayer = white;
        Handicap h = black.getHandicap();
        if (h.getTotal() < 2) toMove = Color.BLACK; else toMove = Color.WHITE;
        ce = CommandExecutor.get();
        ce.writeLastGameInformation(black.getId(), white.getId());
    }

    /** Create a PlayerManager object to continue play for the specified game.
	  * @param black - The black player
	  * @param white - The white player
	  * @param gr - The game record of the position to resume from.
	  * @throws InternalErrorException - Thrown if an error occurs in
	  * starting the game from the position in the game record.
	  */
    public PlayerManager(Player black, Player white, GameRecord gr) throws InternalErrorException {
        this(black, white);
        toMove = initializeBoard(gr);
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /** Cause one more move to be made.
	  * This method only needs to be called when the
	  * single step flag has been set.  Each time it
	  * is called, the player whose turn it is is allowed to
	  * make his move.  The purpose of the single step mode is
	  * to allow the user to examine the debug output for each move.
	  */
    public void singleStep() {
        continueFlag = true;
    }

    /** Set the single step flag.
	  * If true, then the singleStep method needs
	  * to be called before another move can be made.
	  * If false, then play proceeds at the pace
	  * of the two players.
	  * @param flag If true, then single step through the game.
	  */
    public void setSingleStepFlag(boolean flag) {
        singleStepFlag = flag;
    }

    /** Determine whether the single step flag is set.
	  * @return true if the single step flag is set, or false.
	  */
    public boolean isSingleStep() {
        return singleStepFlag;
    }

    /** Play a game of Go, using the two Player objects.
	  * @throws InternalErrorException Thrown if the game cannot be
	  * played for any reason, such as differing initial
	  * board for the two players.
	  */
    public void play() throws InternalErrorException {
        if (toMove.equals(Color.WHITE)) {
            if (whiteMove()) {
                return;
            }
        }
        while (true) {
            toMove = Color.BLACK;
            if (blackMove()) break;
            toMove = Color.WHITE;
            if (whiteMove()) break;
        }
    }

    /** Return to an earlier state in the game.
	  * The method name is a slight misnomer, since
	  * the last two moves are taken back.
	  * @return true if the game is reverted to the earlier
	  * state, or false.
	  * @throws InconsistentStateException - Thrown if the
	  * moves are taken back for one player but not the other.
	  * There should be rollback functionality to take care
	  * of this case.
	  * @throws InternalErrorException - Thrown if an error occurs for any reason.
	  */
    public boolean takeBackMove() throws InconsistentStateException, InternalErrorException {
        BooleanResponse br = ce.takeBackMove(this);
        return br.getResponse();
    }

    /** Get a move from the black player, and send it to the white player.
	  * The method will pause before getting the move from black if the
	  * single step flag is set.
	  * @return true if the game is now over, or false.
	  * @throws InternalErrorException - Thrown is an error occurs for any reason.
	  */
    private boolean blackMove() throws InternalErrorException {
        try {
            pause();
            MoveDescriptor md = blackPlayer.requestMove();
            whitePlayer.makeMove(md);
            if (md.isEndOfGame()) {
                return true;
            } else return false;
        } catch (Exception e) {
            InternalErrorException iee = new InternalErrorException(e);
            throw iee;
        }
    }

    /** Get a move from the white player, and send it to the black player.
	  * The method will pause before getting the move from white if the
	  * single step flag is set.
	  * @return true if the game is now over, or false.
	  * @throws InternalErrorException - Thrown is an error occurs for any reason.
	  */
    private boolean whiteMove() throws InternalErrorException {
        try {
            pause();
            MoveDescriptor md = whitePlayer.requestMove();
            blackPlayer.makeMove(md);
            if (md.isEndOfGame()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            InternalErrorException iee = new InternalErrorException(e);
            throw iee;
        }
    }

    /** Pause the game until receiving a signal to continue.
	  * The game is paused if the single step flag is set
	  * until the continue flag is set.  The continue flag
	  * is then set to false, and the method returns.
	  */
    private void pause() {
        if (singleStepFlag) {
            while (!continueFlag) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    SystemLog.error(e);
                }
            }
            continueFlag = false;
        }
    }

    /** Initialize the board for the two players.
	  * The players are responsible for adding any
	  * handicap stones.  For the case where a game
	  * is being resumed, players that are not
	  * self initializing have all of the moves of
	  * the game added to them one move at a time.
	  * @return The color of the player to play next.
	  * @throws InternalErrorException Thrown if the game 
	  * cannot be played for any reason, such as differing 
	  * initial boards for the two players.
	  */
    private Color initializeBoard(GameRecord gr) throws InternalErrorException {
        boolean blackFlag = blackPlayer.isSelfInitializingUser();
        boolean whiteFlag = whitePlayer.isSelfInitializingUser();
        return playPrerecordedMoves(gr, blackFlag, whiteFlag);
    }

    /** Play out a game record of prerecorded moves.
	  * @param gr The game record of the moves to be played out.
	  * @param blackFlag Only make calls on black player if this is false.
	  * @param whiteFlag Only make calls on white player if this is false.
	  * @return A Color object which is the color of the next player to play.
	  * @throws InternalErrorException Thrown if the operation fails for any reason.
	  */
    protected Color playPrerecordedMoves(GameRecord gr, boolean blackFlag, boolean whiteFlag) throws InternalErrorException {
        try {
            if (!blackFlag || !whiteFlag) {
                Handicap handicap = new Handicap(gr.getHandicapStones().length);
                MoveValidator mv = new MoveValidator(Color.BLACK, handicap);
                Move[] moves = gr.getMoves();
                for (int i = 0; i < moves.length; i++) {
                    MoveDescriptor md = mv.makeMove(moves[i]);
                    if (!blackFlag) {
                        pause();
                        blackPlayer.makeMoveNoEvents(md);
                    }
                    if (!whiteFlag) {
                        pause();
                        whitePlayer.makeMoveNoEvents(md);
                    }
                }
            }
            Color toMove = gr.getLastMove().getColor().flip();
            return toMove;
        } catch (Exception e) {
            InternalErrorException iee = new InternalErrorException(e);
            throw iee;
        }
    }

    /** Return an array of MoyomanPlayer objects.
	  * The client will need to distinguish between
	  * MoyomanPlayer players and other types of players
	  * because of functionality such as the debug window.
	  * @return An array of MoyomanPlayer objects.
	  */
    public MoyomanPlayer[] getMoyomanPlayers() {
        ArrayList al = new ArrayList();
        if (blackPlayer instanceof MoyomanPlayer) al.add(blackPlayer);
        if (whitePlayer instanceof MoyomanPlayer) al.add(whitePlayer);
        MoyomanPlayer[] mp = new MoyomanPlayer[al.size()];
        al.toArray(mp);
        return mp;
    }

    /** Get the game record for this game.
	  * @return A GameRecord object which is the record of this game so far.
	  * @throws InternalErrorException Thrown if the game record cannot be 
	  * retrieved for any reason.
	  */
    public GameRecord getGameRecord() throws InternalErrorException {
        return blackPlayer.getGameRecord();
    }

    public MoveDescriptor getSuggestedMove() throws RuntimeException {
        MoyomanPlayer mp;
        if (toMove.equals(Color.BLACK)) {
            if (!(whitePlayer instanceof MoyomanPlayer)) throw new RuntimeException("White player is not a computer player");
            mp = (MoyomanPlayer) whitePlayer;
        } else {
            if (!(blackPlayer instanceof MoyomanPlayer)) throw new RuntimeException("Black player is not a computer player");
            mp = (MoyomanPlayer) blackPlayer;
        }
        try {
            return mp.getSuggestedMove();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
