package net.sourceforge.strategema.games;

import net.sourceforge.strategema.games.actions.MoveAction;
import net.sourceforge.strategema.games.timers.ComputationTimer;
import net.sourceforge.strategema.util.Clone;
import net.sourceforge.strategema.util.datastructures.Pair;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a game state, including volatile information such as the current values shown on the
 * dice.
 * 
 * @author Lizzy
 * 
 */
public class VolatileGameState extends GameState {

    /** Serialization */
    private static final long serialVersionUID = -3494784786690634001L;

    /** The faces currently shown on the dice. */
    private final Serializable[] diceFacesShown;

    /** The current positions of the spinners. */
    private final Map<String, Serializable> spinners;

    /** The faces currently shown on the coins. */
    private final CoinToss.Result[] coinTosses;

    /**
	 * The number of heads thrown on the repeatedly tossed coin (which is none of those listed under
	 * {@link #coinTosses}).
	 */
    private int numHeads;

    /** The total number of heads thrown on the repeatedly tossed coin. */
    private int numCoinTosses;

    /** True if the last question was answered correctly. */
    public boolean lastQuestionCorrect;

    /** The number of the action within the current turn, starting from 1 for the first action. */
    private int actionNumber;

    /**
	 * False if some reason "volatile" state information is not actually volatile because it is needed
	 * after the turn is completed, for example if the game rules depend on the dice role of the
	 * previous player.
	 */
    private final boolean isVolatile;

    /**
	 * Constructs the game state.
	 * @param initialState The non-volatile state information.
	 * @param initialDiceFaces The faces initially shown on the dice, or null if the game doesn't use
	 * dice.
	 * @param initialSpinnerPositions The initial positions of the spinners, or null if the game
	 * doesn't use spinners.
	 * @param coins The faces initially shown on the coins, or null if the game doesn't use coins or
	 * only uses one coin.
	 * @throws NullPointerException If {@code initialState} is null.
	 */
    public VolatileGameState(final GameState initialState, final Serializable[] initialDiceFaces, final Map<String, Serializable> initialSpinnerPositions, final CoinToss.Result[] coins) throws NullPointerException {
        this(initialState, initialDiceFaces, initialSpinnerPositions, coins, true);
    }

    /**
	 * Constructs the game state.
	 * @param initialState The non-volatile state information.
	 * @param initialDiceFaces The faces initially shown on the dice, or null if the game doesn't use
	 * dice.
	 * @param initialSpinnerPositions The initial positions of the spinners, or null if the game
	 * doesn't use spinners.
	 * @param coins The faces initially shown on the coins, or null if the game doesn't use coins or
	 * only uses one coin.
	 * @param isVolatile False if some reason "volatile" state information is needed after a turn is
	 * completed, for example if the game rules depend on the dice role of the previous player.
	 * @throws NullPointerException If {@code initialState} is null or if any element of {@code coins}
	 * is null.
	 */
    public VolatileGameState(final GameState initialState, final Serializable[] initialDiceFaces, final Map<String, Serializable> initialSpinnerPositions, final CoinToss.Result[] coins, final boolean isVolatile) throws NullPointerException {
        super(initialState, false);
        if (this.board != null) {
            this.board.setParentState(this);
        }
        if (initialDiceFaces != null && initialDiceFaces.length > 0) {
            this.diceFacesShown = Arrays.copyOf(initialDiceFaces, initialDiceFaces.length);
        } else {
            this.diceFacesShown = null;
        }
        if (initialSpinnerPositions != null && !initialSpinnerPositions.isEmpty()) {
            this.spinners = new HashMap<String, Serializable>(initialSpinnerPositions);
        } else {
            this.spinners = null;
        }
        if (coins != null && coins.length > 0) {
            this.coinTosses = new CoinToss.Result[coins.length];
            for (int i = 0; i < coins.length; i++) {
                if (coins[i] == null) {
                    throw new NullPointerException("coins[" + Integer.toString(i) + "]");
                }
                this.coinTosses[i] = coins[i];
            }
        } else {
            this.coinTosses = null;
        }
        this.isVolatile = isVolatile;
        this.numHeads = 0;
        this.numCoinTosses = 0;
        this.lastQuestionCorrect = false;
        this.actionNumber = 1;
    }

    /**
	 * Creates a game state by combining non-volatile information from one game state with volatile
	 * information from another. Used to execute take backs.
	 * @param nonVolatile The non-volatile state information.
	 * @param volatileState The game state to used to initialize the volatile state information.
	 */
    private VolatileGameState(final GameState nonVolatile, final VolatileGameState volatileState) {
        super(nonVolatile, false);
        if (nonVolatile.board != null) {
            this.board.setParentState(this);
        }
        if (volatileState.diceFacesShown != null) {
            this.diceFacesShown = Arrays.copyOf(volatileState.diceFacesShown, volatileState.diceFacesShown.length);
        } else {
            this.diceFacesShown = null;
        }
        if (volatileState.spinners != null) {
            this.spinners = new HashMap<String, Serializable>(volatileState.spinners);
        } else {
            this.spinners = null;
        }
        if (volatileState.coinTosses != null) {
            this.coinTosses = Arrays.copyOf(volatileState.coinTosses, volatileState.coinTosses.length);
        } else {
            this.coinTosses = null;
        }
        this.numHeads = volatileState.numHeads;
        this.numCoinTosses = volatileState.numCoinTosses;
        this.lastQuestionCorrect = volatileState.lastQuestionCorrect;
        this.actionNumber = volatileState.actionNumber;
        this.isVolatile = true;
    }

    /**
	 * Creates a game state by combining non-volatile information from one game state with volatile
	 * state information from another, if needed. Used to execute take backs.
	 * @param nonVolatile The non-volatile state information.
	 * @param volatileState The game state to used to initialize the volatile state information.
	 * @return A volatile game state matching the given the game state specified by the first
	 * argument, which may or may not be a VolatileGameState.
	 */
    public VolatileGameState deriveVolatileState(final GameState nonVolatile, final VolatileGameState volatileState) {
        if (nonVolatile instanceof VolatileGameState) {
            return (VolatileGameState) nonVolatile;
        } else {
            return new VolatileGameState(nonVolatile, volatileState);
        }
    }

    /** Informs the game state that a new turn has begun. */
    public void beginTurn() {
        this.actionNumber = 1;
    }

    /**
	 * Notifies the game state that an action was performed.
	 * @param action The action that was performed.
	 */
    public void actionPerformed(final GameAction action) {
        if (action.isSignificant()) {
            this.actionNumber++;
        }
    }

    /**
	 * Gets the number of the next action that will be performed.
	 * @return The number of the action within the current turn, starting from 1 for the first action.
	 */
    public int getActionNumber() {
        return this.actionNumber;
    }

    /**
	 * Inspects the value currently shown on a die.
	 * @param die The number of the die to inspect.
	 * @return The value currently shown on the die.
	 * @throws IndexOutOfBoundsException If {@code die} is less than zero or greater than the maximum
	 * die number.
	 */
    public Object inspectDie(final int die) throws IndexOutOfBoundsException {
        if (this.diceFacesShown == null) {
            throw new IndexOutOfBoundsException(Integer.toString(die));
        }
        return this.diceFacesShown[die];
    }

    /**
	 * Gets the number of dice.
	 * @return The number of dice used in the game.
	 */
    public int getNumDice() {
        if (this.diceFacesShown == null) {
            return 0;
        }
        return this.diceFacesShown.length;
    }

    /**
	 * Notes that a die has been rolled to a new value.
	 * @param die The number of the die that was rolled.
	 * @param outcome The number rolled on the die.
	 * @throws IndexOutOfBoundsException If {@code die} is less than zero or greater than the maximum
	 * die number.
	 */
    public void registerDiceRoll(final int die, final Serializable outcome) throws IndexOutOfBoundsException {
        if (this.diceFacesShown == null) {
            throw new IndexOutOfBoundsException(Integer.toString(die));
        }
        this.diceFacesShown[die] = outcome;
    }

    /**
	 * Inspects the value currently selected by a spinner.
	 * @param spinner The name of the spinner to inspect.
	 * @return The value currently selected by the specified spinner.
	 * @throws IndexOutOfBoundsException If {@code spinner} is not the name of a known spinner.
	 * @throws NullPointerException If {@code spinner} is null and the {@link Map} passed to the
	 * constructor does not support null keys.
	 */
    public Object inspectSpinner(final String spinner) throws IndexOutOfBoundsException, NullPointerException {
        if (this.spinners != null && this.spinners.containsKey(spinner)) {
            return this.spinners.get(spinner);
        } else {
            throw new IndexOutOfBoundsException(spinner);
        }
    }

    /**
	 * Notes that a spinner has been spun to a new value.
	 * @param spinner The name of the spinner that was spun.
	 * @param outcome The value selected by the spinner.
	 * @throws IndexOutOfBoundsException If {@code spinner} is not the name of a known spinner.
	 * @throws NullPointerException If {@code spinner} is null and the {@link Map} passed to the
	 * constructor does not support null keys.
	 */
    public void registerSpin(final String spinner, final Serializable outcome) throws IndexOutOfBoundsException, NullPointerException {
        if (this.spinners != null && this.spinners.containsKey(spinner)) {
            this.spinners.put(spinner, outcome);
        } else {
            throw new IndexOutOfBoundsException(spinner);
        }
    }

    /**
	 * Gets the number of heads thrown on the repeatedly tossed coin.
	 * @return The number of heads thrown on the repeatedly tossed coin.
	 */
    public int getNumHeads() {
        return this.numHeads;
    }

    /**
	 * Gets the number of tails thrown on the repeatedly tossed coin.
	 * @return The number of tails thrown on the repeatedly tossed coin.
	 */
    public int getNumTails() {
        return this.numCoinTosses - this.numHeads;
    }

    /**
	 * Gets the number tosses of the repeatedly tossed coin since it was last reset.
	 * @return The number tosses of the repeatedly tossed coin since it was last reset.
	 */
    public int getNumTosses() {
        return this.numCoinTosses;
    }

    /**
	 * Sets the number of times heads was thrown on the repeatedly tossed coin. Called by the system
	 * during set up for custom scenarios.
	 * @param heads The number of heads thrown.
	 * @throws IllegalArgumentException If {@code heads} is less than zero or greater than the total
	 * number of times the coin was tossed.
	 */
    void setNumHeads(final int heads) throws IllegalArgumentException {
        if (heads < 0 || heads > this.numCoinTosses) {
            throw new IllegalArgumentException("heads");
        }
        this.numHeads = heads;
    }

    /**
	 * Sets the number of times the repeatedly tossed coin was tossed. Called by the system during set
	 * up for custom scenarios.
	 * @param tosses The number of coin tosses.
	 * @throws IllegalArgumentException If {@code tosses} is less than the number of times heads was
	 * thrown.
	 */
    void setNumTosses(final int tosses) throws IllegalArgumentException {
        if (tosses < this.numHeads) {
            throw new IllegalArgumentException("tosses");
        }
        this.numCoinTosses = tosses;
    }

    /**
	 * Resets the number of tosses, the number of heads, and the number of tails of the repeatedly
	 * tossed coin to zero.
	 */
    public void resetCoinTosses() {
        this.numHeads = 0;
        this.numCoinTosses = 0;
    }

    /**
	 * Notes that the repeatedly tossed coin has been tossed
	 * @param result The result of the coin toss.
	 */
    public void registerCoinToss(final CoinToss.Result result) {
        if (result.equals(CoinToss.Result.HEADS)) {
            this.numHeads++;
        }
        this.numCoinTosses++;
    }

    /**
	 * Notes that a coin has been tossed.
	 * @param coin The number of the coin that was tossed.
	 * @param result The result of the coin toss.
	 * @throws IndexOutOfBoundsException If {@code coin} is less than zero or greater than the maximum
	 * coin number.
	 * @throws NullPointerException If {@code result} is null.
	 */
    public void registerCoinToss(final int coin, final CoinToss.Result result) throws IndexOutOfBoundsException, NullPointerException {
        if (result == null) {
            throw new NullPointerException("result");
        }
        this.coinTosses[coin] = result;
    }

    /**
	 * Inspects which side of coin is currently showing face up.
	 * @param coin The number of the coin to inspect.
	 * @return HEADS if heads is face up and TAILS if tails is face up.
	 * @throws IndexOutOfBoundsException If {@code coin} is less than zero or greater than the maximum
	 * coin number.
	 */
    public CoinToss.Result inspectCoin(final int coin) throws IndexOutOfBoundsException {
        return this.coinTosses[coin];
    }

    /**
	 * Gets the possible moves for a given piece in a specified position for this game state,
	 * including accounting for the maximal captures rule, if applicable.
	 * @param board The game board.
	 * @param position The position where the piece is located.
	 * @param pieceInPlay The piece.
	 * @param timer A computer players computation timer to charge the computation effort to, or null
	 * if the player is a human player.
	 * @return The set of squares that a piece can move to and the squares that it moves through to
	 * get there.
	 * @throws NullPointerException If the game doesn't have a board.
	 */
    public Set<List<Coordinate<?, ?, ?>>> getPossibleMoves(final BoardGeometry board, final Coordinate<?, ?, ?> position, final PieceInPlay pieceInPlay, final ComputationTimer timer) throws NullPointerException {
        return pieceInPlay.getPiece().movement.getPossibleMaximalCaptureMoves(this, board, pieceInPlay, position, timer);
    }

    /**
	 * Gets the possible captures for a given piece in a specified position for this game state.
	 * @param board The game board.
	 * @param moves The movements that the piece can make.
	 * @param pieceInPlay The piece.
	 * @param timer A computer player's computation timer to charge the computation effort to, or null
	 * if the player is a human player.
	 * @return A map from the destination square of this piece to the squares where pieces are
	 * captured by moving to the aforementioned square.
	 * @throws NullPointerException If If the game doesn't have a board or {@code pieceInPlay} does
	 * not have a capturing rule.
	 */
    public Map<List<Coordinate<?, ?, ?>>, Collection<Pair<Coordinate<?, ?, ?>, PieceInPlay>>> getPossibleCaptures(final BoardGeometry board, final Set<List<Coordinate<?, ?, ?>>> moves, final PieceInPlay pieceInPlay, final ComputationTimer timer) throws NullPointerException {
        return pieceInPlay.getPiece().capture.getPossibleCaptures(this, board, pieceInPlay, moves, timer);
    }

    /**
	 * Gets the possible captures a can make by moving to a specific square.
	 * @param board The game board.
	 * @param move The the move made by the piece.
	 * @param pieceInPlay The piece.
	 * @param timer A computer player's computation timer to charge the computation effort to, or null
	 * if the player is a human player.
	 * @return A map from the destination square of this piece to the squares where pieces are
	 * captured by moving to the aforementioned square.
	 * @throws NullPointerException If If the game doesn't have a board or {@code pieceInPlay} does
	 * not have a capturing rule.
	 */
    public Collection<Pair<Coordinate<?, ?, ?>, PieceInPlay>> getCaptures(final BoardGeometry board, final List<Coordinate<?, ?, ?>> move, final PieceInPlay pieceInPlay, final ComputationTimer timer) throws NullPointerException {
        return pieceInPlay.getPiece().capture.getCaptures(this, board, pieceInPlay, move, timer, false);
    }

    /**
	 * Performs a specified move, checking that it is valid, including checking the maximal captures
	 * rule, if applicable.
	 * @param game The game being played.
	 * @param board The game board.
	 * @param moveAction The move to perform.
	 * @throws NullPointerException If {@code move} is null or the game doesn't have a board.
	 * @throws IllegalMoveException If the move is invalid.
	 */
    public void executeMove(final Game<?> game, final Board<?> board, final MoveAction moveAction) throws NullPointerException, IllegalMoveException {
        final PieceInPlay pieceInPlay = moveAction.pieceInPlay;
        final List<Coordinate<?, ?, ?>> move = moveAction.move;
        final PieceInPlay secondPiece = moveAction.continuationPiece;
        final List<Coordinate<?, ?, ?>> continuation = moveAction.continuationMove;
        final MovementRule moveRule = pieceInPlay.getPiece().movement;
        if (moveAction.subject != this.getCurrentPlayerNumber()) {
            throw new IllegalMoveException(moveAction);
        }
        if (move.size() == 0) {
            throw new IllegalMoveException(moveAction);
        }
        final Coordinate<?, ?, ?> startSquare = move.get(0);
        final Coordinate<?, ?, ?> endSquare = move.get(move.size() - 1);
        final Player currentPlayer = this.getCurrentPlayer();
        if (currentPlayer == null) {
            throw new IllegalMoveException(moveAction);
        }
        if (!currentPlayer.hasStarted || !this.board.getPiecesAt(startSquare).contains(pieceInPlay) || !board.isSquare(endSquare) || !this.board.isStorableLocation(endSquare) || !game.playerCanMove(this, board, pieceInPlay, startSquare) || !moveRule.isValidMove(this, board, pieceInPlay, move, null) || !moveRule.isValidContinuation(this, board, pieceInPlay, move, secondPiece, continuation)) {
            throw new IllegalMoveException(moveAction);
        }
        this.executeMoveUnchecked(board, pieceInPlay, move);
        if (secondPiece != null) {
            this.executeMoveUnchecked(board, secondPiece, continuation);
        }
    }

    /**
	 * Performs a specified move without checking that it is valid.
	 * @param <T> The type of visuals the game uses.
	 * @param board The game board.
	 * @param pieceInPlay The piece being moved.
	 * @param move The move to perform.
	 * @throws NullPointerException If the current has left the game.
	 */
    public <T> void executeMoveUnchecked(final Board<T> board, final PieceInPlay pieceInPlay, final List<Coordinate<?, ?, ?>> move) throws NullPointerException {
        final Coordinate<?, ?, ?> startSquare = move.get(0);
        final Coordinate<?, ?, ?> endSquare = move.get(move.size() - 1);
        this.board.movePiece(pieceInPlay, board, startSquare, endSquare);
        pieceInPlay.setMoved();
        final int player = this.getCurrentPlayerNumber();
        if (this.getCurrentPlayer().getArmies().contains(pieceInPlay.getColour())) {
            pieceInPlay.playerNum = player;
        }
        board.showMovement(move, board.getPieceVisual(pieceInPlay));
    }

    /**
	 * Performs a specified capture, checking that it is valid, assuming that the move itself is
	 * valid.
	 * @param board The game board.
	 * @param pieceInPlay The piece performing the capture.
	 * @param move The move that the piece has made.
	 * @param capturedPieces The pieces captured.
	 * @param showPieces True if captured pieces should be displayed even when previously secret.
	 * @throws IllegalCaptureException If the capture is invalid.
	 */
    public void executeCapture(final Board<?> board, final List<Coordinate<?, ?, ?>> move, final PieceInPlay pieceInPlay, final Collection<Pair<Coordinate<?, ?, ?>, PieceInPlay>> capturedPieces, final boolean showPieces) throws IllegalCaptureException {
        this.resetLastCapture();
        pieceInPlay.getPiece().capture.executeCapture(this, board, move, pieceInPlay, capturedPieces, showPieces);
    }

    /**
	 * Performs a specified capture without checking that it is valid.
	 * @param board The game board.
	 * @param pieceInPlay The piece performing the capture.
	 * @param move The move that the piece has made.
	 * @param capturedPieces The pieces captured.
	 * @param showPieces True if captured pieces should be shown even when previously secret.
	 * @throws NullPointerException If the piece does not have a capturing rule.
	 */
    public void executeCaptureUnchecked(final Board<?> board, final List<Coordinate<?, ?, ?>> move, final PieceInPlay pieceInPlay, final Collection<Pair<Coordinate<?, ?, ?>, PieceInPlay>> capturedPieces, final boolean showPieces) throws NullPointerException {
        this.resetLastCapture();
        pieceInPlay.getPiece().capture.executeCaptureUnchecked(this, board, move, pieceInPlay, capturedPieces, showPieces);
    }

    /**
	 * Creates a game state that contains only the non-volatile portion of this game state.
	 * @return The non-volatile portion of this game state.
	 */
    @Override
    public GameState asStableGameState() {
        if (this.isVolatile) {
            return new GameState(this, false);
        } else {
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public VolatileGameState clone() {
        final Serializable[] diceCopy;
        if (this.diceFacesShown != null) {
            diceCopy = Arrays.copyOf(this.diceFacesShown, this.diceFacesShown.length);
        } else {
            diceCopy = null;
        }
        final CoinToss.Result[] coinsCopy;
        if (this.coinTosses != null) {
            coinsCopy = Arrays.copyOf(this.coinTosses, this.coinTosses.length);
        } else {
            coinsCopy = null;
        }
        final Map<String, Serializable> spinnersCopy;
        if (this.spinners != null) {
            spinnersCopy = new HashMap<String, Serializable>(this.spinners.size(), 1);
            for (final Map.Entry<String, Serializable> entry : this.spinners.entrySet()) {
                final Serializable value = entry.getValue();
                if (value instanceof Clone<?>) {
                    spinnersCopy.put(entry.getKey(), ((Clone<? extends Serializable>) value).clone());
                } else {
                    spinnersCopy.put(entry.getKey(), value);
                }
            }
        } else {
            spinnersCopy = null;
        }
        final GameState nonVolatileCopy = new GameState(this, true);
        final VolatileGameState copy = new VolatileGameState(nonVolatileCopy, diceCopy, spinnersCopy, coinsCopy, this.isVolatile);
        copy.actionNumber = this.actionNumber;
        return copy;
    }

    /**
	 * Serialization.
	 * @param in The input stream.
	 * @throws IOException If an I/O error occurs.
	 * @throws ClassNotFoundException If a class cannot be found.
	 */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        final String error = "The stream is corrupted.";
        if (this.actionNumber < 1) {
            throw new IOException(error);
        }
    }
}
