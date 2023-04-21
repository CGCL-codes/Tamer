package org.vizzini.ui.game.boardgame.chess;

import org.vizzini.game.IAdjudicator;
import org.vizzini.game.IAgent;
import org.vizzini.game.IAgentCollection;
import org.vizzini.game.ITeamCollection;
import org.vizzini.game.IToken;
import org.vizzini.game.ITokenCollection;
import org.vizzini.game.IntegerPosition;
import org.vizzini.game.boardgame.ArrangementEnum;
import org.vizzini.game.boardgame.MarkingEnum;
import org.vizzini.game.boardgame.chess.ChessTokenCollection;
import org.vizzini.game.boardgame.chess.Constants;
import org.vizzini.game.boardgame.chess.DefaultChessEnvironment;
import org.vizzini.game.boardgame.chess.IChessAdjudicator;
import org.vizzini.game.boardgame.chess.IChessEnvironment;
import org.vizzini.game.event.StateEvent;
import org.vizzini.ui.ApplicationSupport;
import org.vizzini.ui.game.boardgame.IGridBoard3DUISwing;
import org.vizzini.ui.graphics.ImageCreator;
import org.vizzini.util.ClassUtilities;
import org.vizzini.util.IProvider;
import org.vizzini.util.MissingPropertyException;
import java.awt.Dimension;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 * Provides a utility application which creates images of token moves for
 * documentation.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public class MoveImager {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(MoveImager.class.getName());

    static {
        IntegerPosition.setDimensions(4, 4, 4);
    }

    /** First position. */
    private static final IntegerPosition POSITION0 = IntegerPosition.get(1, 1, 1);

    /** Second position. */
    private static final IntegerPosition POSITION1 = IntegerPosition.get(1, 2, 2);

    /** Phantom king position, somewhere out of the way. */
    private static final IntegerPosition POSITION2 = IntegerPosition.get(3, 2, 3);

    /** All types. */
    private static final String ALL_TYPES = "all";

    /** Bishop. */
    private static final String BISHOP = "bishop";

    /** King. */
    private static final String KING = "king";

    /** Knight. */
    private static final String KNIGHT = "knight";

    /** Mace. */
    private static final String MACE = "mace";

    /** Black pawn. */
    private static final String PAWN_BLACK = "pawnBlack";

    /** Black pawn first move. */
    private static final String PAWN_BLACK_FIRST = "pawnBlackFirst";

    /** White pawn. */
    private static final String PAWN_WHITE = "pawnWhite";

    /** White pawn first move. */
    private static final String PAWN_WHITE_FIRST = "pawnWhiteFirst";

    /** Queen. */
    private static final String QUEEN = "queen";

    /** Rook. */
    private static final String ROOK = "rook";

    /** Class utilities. */
    private ClassUtilities _classUtilities;

    /**
     * Application method.
     *
     * @param   args  Input arguments.
     *
     * @throws  Exception  if there is a problem.
     *
     * @since   v0.3
     */
    public static void main(String[] args) throws Exception {
        ApplicationSupport.setStringBundleName("org.vizzini.ui.game.boardgame.chess.resources");
        ApplicationSupport.setConfigBundleName("org.vizzini.ui.game.boardgame.chess.moveImagerConfig");
        MoveImager imager = new MoveImager();
        String type = ALL_TYPES;
        ArrangementEnum arrangement = ArrangementEnum.UPRIGHT;
        MarkingEnum marking = MarkingEnum.VARYING;
        if (args != null) {
            if (args.length > 0) {
                arrangement = ArrangementEnum.valueOf(args[0]);
                if (args.length > 1) {
                    marking = MarkingEnum.valueOf(args[1]);
                    if (args.length > 2) {
                        type = args[2];
                    }
                }
            }
        }
        LOGGER.info("type = " + type);
        LOGGER.info("arrangement = " + arrangement);
        LOGGER.info("marking = " + marking);
        if (ALL_TYPES.equals(type)) {
            String[] types = { BISHOP, KING, KNIGHT, MACE, PAWN_BLACK, PAWN_BLACK_FIRST, PAWN_WHITE, PAWN_WHITE_FIRST, QUEEN, ROOK };
            for (int i = 0; i < types.length; i++) {
                imager.createImage(types[i], arrangement, marking);
            }
        } else {
            imager.createImage(type, arrangement, marking);
        }
        System.exit(0);
    }

    /**
     * Create an image for the given type, and save it to a file.
     *
     * @param   type         Type.
     * @param   arrangement  Arrangement.
     * @param   marking      Marking.
     *
     * @throws  IllegalAccessException  if there is an illegal access.
     * @throws  InstantiationException  if there is an instantiation problem.
     * @throws  IOException             if there is an I/O problem.
     *
     * @since   v0.3
     */
    public void createImage(String type, ArrangementEnum arrangement, MarkingEnum marking) throws IllegalAccessException, InstantiationException, IOException {
        IChessEnvironment board = createEnvironment();
        IGridBoard3DUISwing boardUI = createEnvironmentUI(board);
        boardUI.setArrangement(arrangement);
        boardUI.setMarking(marking, boardUI.getFirstColor(), boardUI.getSecondColor());
        boardUI.setMagnify(getMagnify(arrangement));
        StateEvent stateEvent = new StateEvent(board);
        boardUI.stateChange(stateEvent);
        if (PAWN_BLACK.equals(type) || PAWN_BLACK_FIRST.equals(type)) {
            boardUI.highlightSelectedPosition(POSITION1);
        } else {
            boardUI.highlightSelectedPosition(POSITION0);
        }
        ((DefaultGridBoardUISwing) boardUI).setHintMode(false);
        ((DefaultGridBoardUISwing) boardUI).setHintMode(true);
        JComponent boardComponent = (JComponent) boardUI;
        boardComponent.setPreferredSize(getImageDimension(arrangement));
        if (!KING.equals(type)) {
            removePhantomKing(board, boardUI);
        }
        writeToFile(type, boardComponent, arrangement, marking);
    }

    /**
     * Create the agents, and add them to the environment.
     *
     * @param   board  Chess board.
     *
     * @throws  IllegalAccessException  if there is an illegal access.
     * @throws  InstantiationException  if there is an instantiation problem.
     *
     * @since   v0.3
     */
    private void createAgents(IChessEnvironment board) throws InstantiationException, IllegalAccessException {
        Properties properties = createBaseProperties();
        int i = 0;
        String propertyName = "agent." + i + ".class";
        String value = properties.getProperty(propertyName);
        if (value != null) {
            while (value != null) {
                ClassUtilities classUtils = getClassUtilities();
                IAgent agent = (IAgent) classUtils.getClassFrom(value).newInstance();
                board.getAgentCollection().add(agent);
                i++;
                propertyName = "agent." + i + ".class";
                value = properties.getProperty(propertyName);
            }
        } else {
            throw new MissingPropertyException(propertyName);
        }
    }

    /**
     * Create base properties for all boards.
     *
     * @return  the properties.
     *
     * @since   v0.3
     */
    private Properties createBaseProperties() {
        Properties properties = new Properties();
        properties.setProperty("adjudicator.class", "org.vizzini.game.boardgame.chess.ChessAdjudicator");
        properties.setProperty("team.0.class", "org.vizzini.game.DefaultTeam");
        properties.setProperty("team.0.name", Constants.TEAM_NAME_WHITE);
        properties.setProperty("team.0.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("team.1.class", "org.vizzini.game.DefaultTeam");
        properties.setProperty("team.1.name", Constants.TEAM_NAME_BLACK);
        properties.setProperty("team.1.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("environment.tokenCollection.class", "org.vizzini.game.boardgame.chess.ChessTokenCollection");
        properties.setProperty("environment.dimensions", "4, 4, 4");
        properties.setProperty("environment.castleDeltaFiles.king", "1, -2");
        properties.setProperty("environment.castleDeltaFiles.rook", "-2, 2");
        properties.setProperty("environment.pawn.isDoubleFirstMove", "false");
        properties.setProperty("agent.0.class", "org.vizzini.game.boardgame.chess.SimpleChessAgent");
        properties.setProperty("agent.0.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("agent.0.actionGenerator.class", "org.vizzini.game.boardgame.chess.action.ChessActionGenerator");
        properties.setProperty("agent.0.evaluator.class", "org.vizzini.game.boardgame.chess.DefaultChessEvaluator");
        properties.setProperty("agent.0.evaluator.values", "100, 300, 320, 500, 900, 0");
        properties.setProperty("agent.1.class", "org.vizzini.game.boardgame.chess.SimpleChessAgent");
        properties.setProperty("agent.1.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("agent.1.actionGenerator.class", "org.vizzini.game.boardgame.chess.action.ChessActionGenerator");
        properties.setProperty("agent.1.evaluator.class", "org.vizzini.game.boardgame.chess.DefaultChessEvaluator");
        properties.setProperty("agent.1.evaluator.values", "100, 300, 320, 500, 900, 0");
        properties.setProperty("environmentUI.class", "org.vizzini.ui.game.boardgame.chess.DefaultGridBoardUISwing");
        properties.setProperty("environmentUI.perspectiveConstant", "500.0");
        properties.setProperty("environmentUI.magnify", "40.0");
        properties.setProperty("environmentUI.magnifyPanel.isVisible", "false");
        properties.setProperty("environmentUI.xScrollBar.isVisible", "false");
        properties.setProperty("environmentUI.xScrollBar.value", "10");
        properties.setProperty("environmentUI.xScrollBar.visible", "30");
        properties.setProperty("environmentUI.xScrollBar.minimum", "0");
        properties.setProperty("environmentUI.xScrollBar.maximum", "120");
        properties.setProperty("environmentUI.xScrollBar.unitIncrement", "2");
        properties.setProperty("environmentUI.yScrollBar.isVisible", "false");
        properties.setProperty("environmentUI.yScrollBar.value", "140");
        properties.setProperty("environmentUI.yScrollBar.visible", "30");
        properties.setProperty("environmentUI.yScrollBar.minimum", "0");
        properties.setProperty("environmentUI.yScrollBar.maximum", "390");
        properties.setProperty("environmentUI.yScrollBar.unitIncrement", "2");
        properties.setProperty("environmentUI.zScrollBar.isVisible", "false");
        properties.setProperty("mapping.token.0.class", "org.vizzini.game.boardgame.chess.standardtoken.King");
        properties.setProperty("mapping.tokenUI.0.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.KingShape");
        properties.setProperty("mapping.token.1.class", "org.vizzini.game.boardgame.chess.standardtoken.Queen");
        properties.setProperty("mapping.tokenUI.1.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.QueenShape");
        properties.setProperty("mapping.token.2.class", "org.vizzini.game.boardgame.chess.standardtoken.Bishop");
        properties.setProperty("mapping.tokenUI.2.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.BishopShape");
        properties.setProperty("mapping.token.3.class", "org.vizzini.game.boardgame.chess.standardtoken.Knight");
        properties.setProperty("mapping.tokenUI.3.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.KnightShape");
        properties.setProperty("mapping.token.4.class", "org.vizzini.game.boardgame.chess.standardtoken.Rook");
        properties.setProperty("mapping.tokenUI.4.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.RookShape");
        properties.setProperty("mapping.token.5.class", "org.vizzini.game.boardgame.chess.standardtoken.Pawn");
        properties.setProperty("mapping.tokenUI.5.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.PawnShape");
        properties.setProperty("mapping.token.6.class", "org.vizzini.game.boardgame.chess.standardtoken.Mace");
        properties.setProperty("mapping.tokenUI.6.class", "org.vizzini.ui.game.boardgame.chess.standardtoken.MaceShape");
        return properties;
    }

    /**
     * Create an environment.
     *
     * @return  the new chess board.
     *
     * @throws  IllegalAccessException  if there is an illegal access.
     * @throws  InstantiationException  if there is an instantiation problem.
     *
     * @since   v0.3
     */
    private IChessEnvironment createEnvironment() throws IllegalAccessException, InstantiationException {
        IChessAdjudicator adjudicator = null;
        IAgentCollection agentCollection = null;
        List<IProvider<IToken>> initialTokenProviders = null;
        ITeamCollection teamCollection = null;
        IProvider<ITokenCollection> tokenCollectionProvider = null;
        int fileCount = 4;
        int rankCount = 4;
        int levelCount = 4;
        int[] kingsCastleDeltaFiles = null;
        int[] rooksCastleDeltaFiles = null;
        int[] enPassantCaptureRanks = null;
        IChessEnvironment answer = new DefaultChessEnvironment(adjudicator, agentCollection, initialTokenProviders, teamCollection, tokenCollectionProvider, fileCount, rankCount, levelCount, kingsCastleDeltaFiles, rooksCastleDeltaFiles, enPassantCaptureRanks);
        createAgents(answer);
        answer.reset();
        return answer;
    }

    /**
     * Create an environment UI component.
     *
     * @param   board  Chess board.
     *
     * @return  the new chess board UI.
     *
     * @throws  IllegalAccessException  if there is an illegal access.
     * @throws  InstantiationException  if there is an instantiation problem.
     *
     * @since   v0.3
     */
    private IGridBoard3DUISwing createEnvironmentUI(IChessEnvironment board) throws IllegalAccessException, InstantiationException {
        IAdjudicator adjudicator = null;
        Map<Class<?>, Class<?>> tokenToUIMap = new HashMap<Class<?>, Class<?>>();
        IGridBoard3DUISwing answer = new DefaultGridBoardUISwing(adjudicator, board, tokenToUIMap);
        answer.reset();
        answer.setLabelPainted(true);
        return answer;
    }

    /**
     * @return  the classUtilities
     */
    private ClassUtilities getClassUtilities() {
        if (_classUtilities == null) {
            _classUtilities = new ClassUtilities();
        }
        return _classUtilities;
    }

    /**
     * @param   type         Type.
     * @param   arrangement  Arrangement.
     * @param   marking      Marking.
     *
     * @return  the filename for the given type.
     *
     * @since   v0.3
     */
    private String getFilenameForType(String type, ArrangementEnum arrangement, MarkingEnum marking) {
        String answer = type + arrangement.getDisplayName() + marking.getDisplayName();
        return answer;
    }

    /**
     * @param   arrangement  Board arrangement.
     *
     * @return  the preferred dimension for the given parameter.
     */
    private Dimension getImageDimension(ArrangementEnum arrangement) {
        Dimension answer = null;
        if (arrangement == ArrangementEnum.CANT) {
            answer = new Dimension(295, 325);
        } else if (arrangement == ArrangementEnum.SKEW) {
            answer = new Dimension(225, 325);
        } else if (arrangement == ArrangementEnum.SLANT) {
            answer = new Dimension(275, 325);
        } else if (arrangement == ArrangementEnum.UPRIGHT) {
            answer = new Dimension(225, 325);
        }
        return answer;
    }

    /**
     * @param   arrangement  Board arrangement.
     *
     * @return  the preferred magnification for the given parameter.
     */
    private double getMagnify(ArrangementEnum arrangement) {
        double answer = 0.0;
        if (arrangement == ArrangementEnum.CANT) {
            answer = 65.0;
        } else if (arrangement == ArrangementEnum.SKEW) {
            answer = 50.0;
        } else if (arrangement == ArrangementEnum.SLANT) {
            answer = 60.0;
        } else if (arrangement == ArrangementEnum.UPRIGHT) {
            answer = 40.0;
        }
        return answer;
    }

    /**
     * Remove the phantom king using the given parameters.
     *
     * @param  board    Board.
     * @param  boardUI  Board user interface.
     */
    private void removePhantomKing(IChessEnvironment board, IGridBoard3DUISwing boardUI) {
        ChessTokenCollection tokens = (ChessTokenCollection) board.getTokenCollection();
        IToken king = tokens.get(POSITION2);
        tokens.remove(king);
        StateEvent stateEvent = new StateEvent(board);
        boardUI.stateChange(stateEvent);
    }

    /**
     * Write the given component to a file specified by the other properties.
     *
     * @param   type         Token type.
     * @param   component    Component.
     * @param   arrangement  Arrangement.
     * @param   marking      Marking.
     *
     * @throws  IOException  if there is an I/O problem.
     *
     * @since   v0.3
     */
    private void writeToFile(String type, JComponent component, ArrangementEnum arrangement, MarkingEnum marking) throws IOException {
        ImageCreator imageCreator = new ImageCreator();
        String filename = getFilenameForType(type, arrangement, marking);
        imageCreator.setFilePrefix(System.getProperty("user.dir") + "/doc/chess3d/images");
        imageCreator.setBaseFilename(filename);
        imageCreator.setEncodingType(ImageCreator.EncodingType.JPG);
        imageCreator.setComponent(component);
        imageCreator.saveImage();
    }
}
