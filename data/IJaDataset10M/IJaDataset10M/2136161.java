package net.sourceforge.strategema.games.games;

import net.sourceforge.strategema.games.Board;
import net.sourceforge.strategema.games.BoardChangeListener;
import net.sourceforge.strategema.games.BoardGeometry;
import net.sourceforge.strategema.games.Coordinate;
import net.sourceforge.strategema.games.EndCondition;
import net.sourceforge.strategema.games.Game;
import net.sourceforge.strategema.games.GameAction.ActionType;
import net.sourceforge.strategema.games.GameActionHandler;
import net.sourceforge.strategema.games.GameDriver;
import net.sourceforge.strategema.games.GameState;
import net.sourceforge.strategema.games.Piece;
import net.sourceforge.strategema.games.PieceInPlay;
import net.sourceforge.strategema.games.PieceSet;
import net.sourceforge.strategema.games.Player;
import net.sourceforge.strategema.games.PromotionRule;
import net.sourceforge.strategema.games.SoundSet;
import net.sourceforge.strategema.games.StatelessSpecialCondition;
import net.sourceforge.strategema.games.VolatileGameState;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A base class for deriving adapters where the adapted game has the same visual representation as
 * the game being adapted.
 * 
 * @author Lizzy
 * 
 * @param <T> The visual representation of the original and adapted games.
 */
public abstract class GameAdapter<T> implements Game<T> {

    /** The game being adapted. */
    protected final Game<T> game;

    /** The visual representation of the game being adapted. */
    private final Class<T> cl;

    /**
	 * Derives a game from an existing game.
	 * @param game The game to provide a new visual representation for.
	 * @param cl The visual representation of the game being adapted.
	 */
    protected GameAdapter(final Game<T> game, final Class<T> cl) {
        this.game = game;
        this.cl = cl;
    }

    @Override
    public Board<? super T> getBoard() {
        return this.game.getBoard();
    }

    @Override
    public PieceSet<? extends T> getDefaultPieceSet() {
        return this.game.getDefaultPieceSet();
    }

    @Override
    public Class<T> getVisualRepresentation() {
        return this.cl;
    }

    @Override
    public BoardChangeListener getBoardChangeListener() {
        return this.game.getBoardChangeListener();
    }

    @Override
    public GameDriver getGameDriver() {
        return this.game.getGameDriver();
    }

    @Override
    public GameActionHandler getNewActionHandler() {
        return this.game.getNewActionHandler();
    }

    @Override
    public boolean canJoinAfterGameStarted() {
        return this.game.canJoinAfterGameStarted();
    }

    @Override
    public String formatColour(final int colour) {
        return this.game.formatColour(colour);
    }

    @Override
    public Set<ActionType> getActions() {
        return this.game.getActions();
    }

    @Override
    public Set<String> getCharacterAttributesToDisplay() {
        return this.game.getCharacterAttributesToDisplay();
    }

    @Override
    public Map<String, String> getDefaultSoundMap() {
        return this.game.getDefaultSoundMap();
    }

    @Override
    public SoundSet getDefaultSoundSet() {
        return this.game.getDefaultSoundSet();
    }

    @Override
    public Set<EndCondition> getEndConditions() {
        return this.game.getEndConditions();
    }

    @Override
    public Set<String> getHighScoreFields() {
        return this.game.getHighScoreFields();
    }

    @Override
    public Comparator<Object> getHighScoreSortOrder(final String field) {
        return this.game.getHighScoreSortOrder(field);
    }

    @Override
    public VolatileGameState getInitialState(final List<Player> players, final BoardGeometry geom) {
        return this.game.getInitialState(players, geom);
    }

    @Override
    public String getName() {
        return this.game.getName();
    }

    @Override
    public Set<Piece> getPieces() {
        return this.game.getPieces();
    }

    @Override
    public Set<PromotionRule> getPromotionRules() {
        return this.game.getPromotionRules();
    }

    @Override
    public Set<Piece> getRoyalPieces() {
        return this.game.getRoyalPieces();
    }

    @Override
    public Set<String> getSoundEvents() {
        return this.game.getSoundEvents();
    }

    @Override
    public Set<StatelessSpecialCondition> getSpecialConditions() {
        return this.game.getSpecialConditions();
    }

    @Override
    public Set<String> getVisiblePlayerStateInfo() {
        return this.game.getVisiblePlayerStateInfo();
    }

    @Override
    public int maxColours() {
        return this.game.maxColours();
    }

    @Override
    public int maxPlayers() {
        return this.game.maxPlayers();
    }

    @Override
    public int minPlayers() {
        return this.game.minPlayers();
    }

    @Override
    public int maxPlayersPerColour() {
        return this.game.maxPlayersPerColour();
    }

    @Override
    public int minPlayersPerColour() {
        return this.game.minPlayersPerColour();
    }

    @Override
    public int maxStartingColoursPerPlayer() {
        return this.game.maxStartingColoursPerPlayer();
    }

    @Override
    public boolean playerCanMove(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position) {
        return this.game.playerCanMove(state, board, piece, position);
    }

    @Override
    public boolean validateGameState(final GameState state) {
        return this.game.validateGameState(state);
    }

    @Override
    public boolean validateVolatileGameState(final VolatileGameState state) {
        return this.game.validateVolatileGameState(state);
    }

    @Override
    public InformationLevel promotionInformation() {
        return this.game.promotionInformation();
    }

    @Override
    public boolean seesPiece(final GameState state, final BoardGeometry board, final int player, final PieceInPlay piece, final Coordinate<?, ?, ?> location) {
        return this.game.seesPiece(state, board, player, piece, location);
    }

    @Override
    public String[] getEnquiries(final int player) {
        return this.game.getEnquiries(player);
    }

    @Override
    public String makeEnquiry(final GameState state, final BoardGeometry board, final int enquiryNo) {
        return this.game.makeEnquiry(state, board, enquiryNo);
    }

    @Override
    public boolean showValidMoves() {
        return this.game.showValidMoves();
    }

    @Override
    public boolean showCapturedPieces() {
        return this.game.showCapturedPieces();
    }
}
