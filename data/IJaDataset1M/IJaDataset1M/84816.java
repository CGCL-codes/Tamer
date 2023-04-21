package net.sf.javarisk.three;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.javarisk.TestBase;
import net.sf.javarisk.three.game.City;
import net.sf.javarisk.three.game.GameBoard;
import net.sf.javarisk.three.game.GameField;
import net.sf.javarisk.three.game.Unit;
import org.junit.After;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/** Base class for JUnit tests providing convenient methods to create domain objects. */
public abstract class TestBase3 extends TestBase {

    /** Creates an empty game board with the specified properties. */
    public static final GameBoard createBoard(int boardWidth, int boardHeight) {
        return new GameBoard(boardWidth, boardHeight);
    }

    /** Creates an empty 1x1 game board. */
    public static final GameBoard createBoard() {
        return createBoard(1, 1);
    }

    /** Creates a field on the specified game board. */
    public static final GameField createField(GameBoard board, int fieldX, int fieldY) {
        GameField f = new GameField(board, new Point(fieldX, fieldY));
        board.setField(f);
        return f;
    }

    /** Creates a field on an otherwise empty game board with the specified properties. */
    public static final GameField createField(int boardWidth, int boardHeight, int fieldX, int fieldY) {
        return createField(createBoard(boardWidth, boardHeight), fieldX, fieldY);
    }

    /** Creates a field on a 1x1 game board. */
    public static final GameField createField() {
        return createField(createBoard(), 0, 0);
    }

    /** Creates a {@link GameField} at each position of the board where non yet exists. */
    public static final GameBoard fillWithFields(GameBoard board) {
        for (int x = board.getSize().width; x-- > 0; ) {
            for (int y = board.getSize().height; y-- > 0; ) {
                if (board.getField(new Point(x, y)) == null) {
                    createField(board, x, y);
                }
            }
        }
        return board;
    }

    /** Creates a game board with the specified properties and filled with {@link GameField}s. */
    public static final GameBoard createBoardWithFields(int boardWidth, int boardHeight) {
        return fillWithFields(new GameBoard(boardWidth, boardHeight));
    }

    /** Adds the specified number of {@link Unit}s to the {@link GameField} and returns the created instances. */
    public static Set<Unit> addUnits(GameField field, int numberOfUnits) {
        HashSet<Unit> units = new HashSet<Unit>(numberOfUnits);
        for (int i = numberOfUnits; i-- > 0; ) {
            Unit u = new Unit(field);
            units.add(u);
        }
        return units;
    }

    /** Creates a city on a field on a 1x1 game board. */
    public static final City createCity() {
        City c = new City(createField());
        c.getField().setCity(c);
        return c;
    }

    /** Creates a city on the specified field. */
    public static final City createCity(GameField field) {
        City c = new City(field);
        field.setCity(c);
        return c;
    }

    /** Returns an {@link Iterable} of {@link GameField}s. */
    public static final Iterable<GameField> filterGameFields(Iterable<GameField> fields, GameField field) {
        return Iterables.filter(fields, Predicates.not(Predicates.equalTo(field)));
    }

    private GameBoard board;

    private final Set<GameField> fields = new HashSet<GameField>();

    @After
    public void tearDown() {
        this.board = null;
        this.fields.clear();
    }

    /**
     * Returns an existing {@link GameBoard} with the specified size or creates one.
     * If a board with a different size already exists, an exception is thrown.
     */
    protected final GameBoard givenGameBoard(int width, int height) {
        if (this.board == null) {
            this.board = createBoard(width, height);
        } else if (this.board.getSize().width != width || this.board.getSize().height != height) {
            throw new RuntimeException("Different GameBoad has already been set up!");
        }
        return this.board;
    }

    /** Calls {@link #givenGameBoard(int, int)} with 1 width & height. */
    protected final GameBoard givenGameBoard() {
        return givenGameBoard(1, 1);
    }

    /**
     * Returns or creates a {@link GameField} on the specified board & position.
     * Created fields are available via {@link #getGivenGameFields()}.
     */
    protected final GameField givenGameField(GameBoard gameBoard, Point position) {
        GameField field = gameBoard.getField(position);
        if (field != null) {
            return field;
        }
        field = new GameField(gameBoard, position);
        gameBoard.setField(field);
        this.fields.add(field);
        return field;
    }

    /** See {@link #givenGameField(GameBoard, Point)}. */
    protected final GameField givenGameField(GameBoard gameBoard, int x, int y) {
        return givenGameField(gameBoard, new Point(x, y));
    }

    /** Creates or returns a board as defined in {@link #givenGameBoard(int, int)} and adds fields to it. */
    protected final GameBoard givenGameBoardWithFields(int width, int height) {
        GameBoard board = givenGameBoard(width, height);
        fillBoard(board);
        return board;
    }

    /** Creates a field on each free position using {@link #givenGameField(GameBoard, Point)}. */
    protected final Collection<GameField> fillBoard(GameBoard board) {
        ArrayList<GameField> fields = new ArrayList<GameField>();
        for (int x = board.getSize().width; x-- > 0; ) {
            for (int y = board.getSize().height; y-- > 0; ) {
                Point position = new Point(x, y);
                if (board.getField(position) != null) continue;
                fields.add(givenGameField(board, position));
            }
        }
        return fields;
    }

    /**
     * Returns or creates a {@link GameField} on the internally available board.
     * 
     * @see #givenGameField(GameBoard, int, int)
     */
    protected final GameField givenGameField(int x, int y) {
        return givenGameField(this.board, x, y);
    }

    /** Returns or creates the 0;0 field of an existing or then-created 1x1 board. */
    protected final GameField givenDefaultField() {
        GameBoard board = givenGameBoard();
        return givenGameField(board, 0, 0);
    }

    protected final List<Unit> givenNumberOfUnits(GameField field, int numberOfUnits) {
        ArrayList<Unit> units = new ArrayList<Unit>(numberOfUnits);
        for (int i = numberOfUnits; i-- > 0; ) units.add(new Unit(field));
        return units;
    }

    public final GameBoard getGivenGameBoard() {
        return this.board;
    }

    public final GameField getGivenGameFieldAt(Point position) {
        return getGivenGameBoard().getField(position);
    }

    public final GameField getGivenGameFieldAt(int x, int y) {
        return getGivenGameFieldAt(new Point(x, y));
    }

    public final Set<GameField> getGivenGameFields() {
        return Collections.unmodifiableSet(this.fields);
    }

    public final GameField getSingleGameField() throws IllegalStateException {
        if (this.fields.size() != 1) throw new IllegalStateException("No single GameField given!");
        return this.fields.iterator().next();
    }
}
