package org.vizzini.example.tictactoe.ui;

import org.uispec4j.Button;
import org.uispec4j.MenuBar;
import org.uispec4j.MenuItem;
import org.uispec4j.Mouse;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.interception.MainClassAdapter;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
import org.vizzini.game.IAgentCollection;
import org.vizzini.game.IGame;
import org.vizzini.game.ITokenCollection;
import org.vizzini.game.boardgame.IGridBoard;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Provides tests for the Tic-Tac-Toe GUI.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.4
 */
public class GameUISwingTest extends UISpecTestCase {

    private Window _gameWindow;

    /**
     * Test the New Game... menu item.
     */
    public void testNewGame() {
        WindowInterceptor mainInterceptor = WindowInterceptor.init(new Trigger() {

            public void run() throws Exception {
                Window window = getMainWindow();
                assertThat(window.titleEquals(""));
                System.out.println("0 window.getName() = " + window.getName());
                assertNotNull(window);
            }
        });
        mainInterceptor.process(new WindowHandler("main") {

            @Override
            public Trigger process(Window window) {
                assertThat(window.titleEquals("Tic-Tac-Toe"));
                System.out.println("1 window.getName() = " + window.getName());
                _gameWindow = window;
                printComponentsIn(_gameWindow.getAwtComponent());
                MenuBar menuBar = window.getMenuBar();
                MenuItem fileMenu = menuBar.getMenu("File");
                MenuItem newGameMenuItem = fileMenu.getSubMenu("New Game...");
                System.out.println("clicking New Game... menu item");
                return newGameMenuItem.triggerClick();
            }
        });
        mainInterceptor.process(new WindowHandler("newGameDialog") {

            @Override
            public Trigger process(Window window) {
                assertThat(window.titleEquals("Tic-Tac-Toe: New Game"));
                Button okButton = window.getButton("OK");
                System.out.println("clicking OK button");
                return okButton.triggerClick();
            }
        });
        mainInterceptor.run();
        UISpec4J.setAssertionTimeLimit(10000);
        System.out.println("UISpec4J.getAssertionTimeLimit() = " + UISpec4J.getAssertionTimeLimit());
        assertTrue(_gameWindow.isVisible());
        assertTrue(_gameWindow.isEnabled());
        System.out.println("_gameWindow.getName() =  " + _gameWindow.getName());
        System.out.println("_gameWindow.getClass() = " + _gameWindow.getClass().getName());
        System.out.println("_gameWindow = " + _gameWindow.getDescription());
        System.out.println("_gameWindow = " + _gameWindow.getDescriptionTypeName());
        Panel toolBarPanel = _gameWindow.getPanel("toolBarPanel");
        assertNotNull(toolBarPanel);
        Panel mainPanel = toolBarPanel.getPanel("mainPanel");
        assertNotNull(mainPanel);
        assertTrue(mainPanel.isVisible());
        assertEquals("mainPanel", mainPanel.getName());
        System.out.println("mainPanel.getClass() = " + mainPanel.getAwtComponent().getClass().getName());
        GameUISwing gameUI = (GameUISwing) ((JPanel) mainPanel.getAwtComponent()).getClientProperty("APPLICATION_UI");
        final IGame game = gameUI.getGame();
        assertNotNull(game);
        assertTrue(new Assertion() {

            @Override
            public void check() {
                assertTrue(!game.getEngine().isGameOver());
            }
        });
        Panel environmentPanel = mainPanel.getPanel("environmentUI");
        assertNotNull(environmentPanel);
        assertTrue(environmentPanel.isVisible());
        assertEquals("environmentUI", environmentPanel.getName());
        System.out.println("environmentPanel.getClass() = " + environmentPanel.getAwtComponent().getClass().getName());
        GridBoardUISwing gridBoardUI = (GridBoardUISwing) environmentPanel.getAwtComponent();
        assertNotNull(gridBoardUI);
        IGridBoard gridBoard = (IGridBoard) gridBoardUI.getEnvironment();
        assertNotNull(gridBoard);
        final IAgentCollection agentCollection = gridBoard.getAgentCollection();
        assertNotNull(agentCollection);
        assertTrue(and(new Assertion() {

            @Override
            public void check() {
                AssertAdapter.assertTrue(!game.getEngine().isGameOver());
            }
        }, new Assertion() {

            @Override
            public void check() {
            }
        }));
        ITokenCollection tokenCollection = gridBoard.getTokenCollection();
        System.out.println("0 tokenCollection.size() = " + tokenCollection.size());
        assertEquals(0, tokenCollection.size());
        Mouse.click(environmentPanel);
        assertTrue(_gameWindow.isVisible());
        assertTrue(environmentPanel.isVisible());
        assertTrue(environmentPanel.isEnabled());
        System.out.println("1 tokenCollection.size() = " + tokenCollection.size());
    }

    /**
     * @see  org.uispec4j.UISpecTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        setAdapter(new MainClassAdapter(GameUISwing.class, new String[0]));
    }

    private void printComponentsIn(Container component) {
        Component[] components = component.getComponents();
        System.out.println(component.getName() + " components.length = " + components.length);
        for (int i = 0; i < components.length; i++) {
            String text = "";
            if (components[i] instanceof JLabel) {
                text = ((JLabel) components[i]).getText();
            } else if (components[i] instanceof JTextArea) {
                text = ((JTextArea) components[i]).getText();
            }
            System.out.println(i + " " + components[i].getName() + " " + components[i].getClass().getName() + " " + text);
        }
    }
}
