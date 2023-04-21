package net.davidrobles.games.pacsim;

import net.davidrobles.games.pacsim.view.GameModelViewer;
import net.davidrobles.games.pacsim.view.GameFrame;
import net.davidrobles.games.pacsim.model.GameModel;
import net.davidrobles.games.pacsim.model.FullGame;
import net.davidrobles.games.pacsim.agents.PacManAgent;
import net.davidrobles.games.pacsim.agents.KeyboardController;
import net.davidrobles.games.pacsim.agents.ToPillsAgent;

public class Run {

    static void runGameModelWithAgent(PacManAgent agent) {
        GameModel gameModel = new GameModel(agent);
        FullGame fullGame = new FullGame(gameModel, 20);
        GameModelViewer viewer = new GameModelViewer(fullGame);
        new GameFrame(viewer);
        viewer.addKeyListener(fullGame);
        fullGame.playGame();
    }

    static void runWithKeyboard() {
        KeyboardController agent = new KeyboardController();
        GameModel gameModel = new GameModel(agent);
        FullGame fullGame = new FullGame(gameModel, 20);
        GameModelViewer viewer = new GameModelViewer(fullGame);
        gameModel.registerEventsObserver(viewer);
        new GameFrame(viewer);
        viewer.addKeyListener(fullGame);
        viewer.addKeyListener(agent);
        fullGame.playGame();
    }

    static void runModel() {
        GameModel gameModel = new GameModel(new ToPillsAgent());
        FullGame fullGame = new FullGame(gameModel, 0);
        GameModelViewer viewer = new GameModelViewer(fullGame);
        new GameFrame(viewer);
        viewer.addKeyListener(fullGame);
        fullGame.playGame();
    }

    public static void main(String[] args) {
        runModel();
        System.out.println("ant test");
    }
}
