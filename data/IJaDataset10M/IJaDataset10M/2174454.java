package org.vizzini.ui.game.boardgame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.vizzini.game.DefaultAdjudicator;
import org.vizzini.game.DefaultToken;
import org.vizzini.game.IAdjudicator;
import org.vizzini.game.IEnvironment;
import org.vizzini.game.ITeam;
import org.vizzini.game.IToken;
import org.vizzini.game.IntegerPosition;
import org.vizzini.game.action.IAction;
import org.vizzini.game.boardgame.IGridBoard;
import org.vizzini.game.boardgame.TestData;
import org.vizzini.math.Vector;
import org.vizzini.ui.ApplicationSupport;
import org.vizzini.ui.VizziniColor;
import org.vizzini.ui.game.ITokenUI;
import org.vizzini.ui.game.TokenUISupport;
import org.vizzini.ui.graphics.IShape;
import org.vizzini.ui.graphics.shape.AnchorShape;
import org.vizzini.ui.graphics.shape.Box;
import org.vizzini.ui.graphics.shape.Cylinder;

/**
 * Provides unit tests for the <code>GridBoard3DUISwing</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class TestGridBoard3DUISwing extends TestCase {

    /** First board. */
    private IGridBoard _board0;

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.2
     */
    public static void main(String[] args) {
        TestRunner.run(TestGridBoard3DUISwing.class);
    }

    /**
     * Test the user interface.
     *
     * @throws  InterruptedException  if there is an interruption.
     *
     * @since   v0.2
     */
    public void testUI() throws InterruptedException {
        ApplicationSupport.setStringBundleName("org.vizzini.ui.game.boardgame.resources");
        ApplicationSupport.setConfigBundleName("org.vizzini.ui.game.boardgame.config");
        IAdjudicator adjudicator = null;
        IGridBoard3DUISwing boardUI = new TestGridBoardUI(adjudicator, _board0);
        boardUI.setCurrentColor(VizziniColor.YELLOW.getColor());
        ((TestGridBoardUI) boardUI).reconcileBoard(_board0);
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(((TestGridBoardUI) boardUI).createArrangementUI());
        menuBar.add(viewMenu);
        JFrame frame = new JFrame();
        frame.getContentPane().add((Component) boardUI, BorderLayout.CENTER);
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
        frame.setSize(400, 400);
        frame.setVisible(true);
        synchronized (this) {
            wait();
        }
    }

    /**
     * @return  a grid board environment.
     *
     * @since   v0.2
     */
    protected static IGridBoard createBoard() {
        Properties properties = getProperties();
        TestData testData = new TestData(properties);
        IGridBoard board = testData.createGridBoard(4, 4, 4);
        ITeam teamWhite = board.getTeamCollection().findByName("white");
        int value = 1;
        for (int i = 0; i < 4; i++) {
            IToken kingWhite = new DefaultToken(IntegerPosition.get(0, 0, i), "king", value);
            kingWhite.setTeam(teamWhite);
            board.getTokenCollection().add(kingWhite);
        }
        for (int i = 0; i < 4; i++) {
            IToken rookWhite = new DefaultToken(IntegerPosition.get(0, 1, i), "rook", value);
            rookWhite.setTeam(teamWhite);
            board.getTokenCollection().add(rookWhite);
        }
        return board;
    }

    /**
     * @return  the grid board properties.
     *
     * @since   v0.2
     */
    protected static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("environmentUI.perspectiveConstant", "500.0");
        properties.setProperty("environmentUI.magnify", "60.0");
        properties.setProperty("environmentUI.magnifyPanel.isVisible", "true");
        properties.setProperty("environmentUI.xScrollBar.isVisible", "true");
        properties.setProperty("environmentUI.xScrollBar.value", "12");
        properties.setProperty("environmentUI.xScrollBar.visible", "30");
        properties.setProperty("environmentUI.xScrollBar.minimum", "0");
        properties.setProperty("environmentUI.xScrollBar.maximum", "120");
        properties.setProperty("environmentUI.yScrollBar.isVisible", "true");
        properties.setProperty("environmentUI.yScrollBar.value", "210");
        properties.setProperty("environmentUI.yScrollBar.visible", "30");
        properties.setProperty("environmentUI.yScrollBar.minimum", "0");
        properties.setProperty("environmentUI.yScrollBar.maximum", "390");
        properties.setProperty("environmentUI.zScrollBar.isVisible", "true");
        properties.setProperty("environmentUI.zScrollBar.value", "0");
        properties.setProperty("environmentUI.zScrollBar.visible", "10");
        properties.setProperty("environmentUI.zScrollBar.minimum", "0");
        properties.setProperty("environmentUI.zScrollBar.maximum", "180");
        properties.setProperty("adjudicator.class", "org.vizzini.ui.game.boardgame.TestGridBoard3DUISwing$TestAdjudicator");
        properties.setProperty("team.0.class", "org.vizzini.game.DefaultTeam");
        properties.setProperty("team.0.name", "white");
        properties.setProperty("team.0.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("team.1.class", "org.vizzini.game.DefaultTeam");
        properties.setProperty("team.1.name", "black");
        properties.setProperty("team.1.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("environment.tokenCollection.class", "org.vizzini.game.TokenPositionCollection");
        properties.setProperty("environment.dimensions", "4, 4, 4");
        return properties;
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _board0 = createBoard();
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _board0 = null;
    }

    /**
     * Provides a test adjudicator.
     *
     * @author   Jeffrey M. Thompson
     * @version  v0.4
     * @since    v0.2
     */
    public static class TestAdjudicator extends DefaultAdjudicator {

        /**
         * @see  org.vizzini.game.IAdjudicator#isActionLegal(org.vizzini.game.IEnvironment,
         *       org.vizzini.game.action.IAction)
         */
        @Override
        public boolean isActionLegal(IEnvironment environment, IAction action) {
            return true;
        }

        /**
         * @see  org.vizzini.game.IAdjudicator#isGameOver(org.vizzini.game.IEnvironment)
         */
        @Override
        public boolean isGameOver(IEnvironment environment) {
            return false;
        }

        /**
         * @see  org.vizzini.game.IAdjudicator#isGoal(org.vizzini.game.IEnvironment)
         */
        @Override
        public boolean isGoal(IEnvironment environment) {
            return false;
        }
    }

    /**
     * Provides a test compound token UI.
     *
     * @author   Jeffrey M. Thompson
     * @version  v0.4
     * @since    v0.2
     */
    static class CompoundTokenUI extends AnchorShape implements ITokenUI {

        /** Support. */
        private TokenUISupport _support = new TokenUISupport();

        /**
         * @param  team     Team.
         * @param  magnify  Magnification.
         */
        public CompoundTokenUI(ITeam team, double magnify) {
            super();
            double height0 = 4.0;
            IShape cylinder = new Cylinder(8, 3.5, height0, 3.5);
            int height1 = 2;
            IShape box = new Box(2, height1, 2);
            box.getState().setPosition(new Vector(0.0, -height0 - (height1 / 2), 0.0));
            add(cylinder);
            add(box);
        }

        /**
         * @see  org.vizzini.ui.game.ITokenUI#getToken()
         */
        public IToken getToken() {
            return _support.getToken();
        }

        /**
         * @see  org.vizzini.ui.game.ITokenUI#setToken(org.vizzini.game.IToken)
         */
        public void setToken(IToken token) {
            _support.setToken(token);
        }
    }

    /**
     * Provides a test token UI.
     *
     * @author   Jeffrey M. Thompson
     * @version  v0.4
     * @since    v0.2
     */
    static class CylinderTokenUI extends Cylinder implements ITokenUI {

        /** Support. */
        private TokenUISupport _support = new TokenUISupport();

        /**
         * @param  team     Team.
         * @param  magnify  Magnification.
         */
        public CylinderTokenUI(ITeam team, double magnify) {
            super(8, 3.5, 6.0, 3.5);
        }

        /**
         * @see  org.vizzini.ui.game.ITokenUI#getToken()
         */
        public IToken getToken() {
            return _support.getToken();
        }

        /**
         * @see  org.vizzini.ui.game.ITokenUI#setToken(org.vizzini.game.IToken)
         */
        public void setToken(IToken token) {
            _support.setToken(token);
        }
    }

    /**
     * Provides a test grid board UI.
     *
     * @author   Jeffrey M. Thompson
     * @version  v0.4
     * @since    v0.2
     */
    static class TestGridBoardUI extends GridBoard3DUISwing {

        /** Serial version UID. */
        private static final long serialVersionUID = 1L;

        /**
         * Construct this object with the given parameters.
         *
         * @param  adjudicator  Adjudicator.
         * @param  environment  Environment.
         *
         * @since  v0.4
         */
        public TestGridBoardUI(IAdjudicator adjudicator, IEnvironment environment) {
            super(adjudicator, environment);
        }

        /**
         * @see  org.vizzini.ui.game.boardgame.GridBoard3DUISwing#getTokenUIClassFor(org.vizzini.game.IToken)
         */
        @Override
        protected Class<?> getTokenUIClassFor(IToken token) {
            Class<?> answer = CylinderTokenUI.class;
            if ("rook".equals(token.getName())) {
                answer = CompoundTokenUI.class;
            }
            return answer;
        }
    }
}
