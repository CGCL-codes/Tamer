package net.bpfurtado.tas.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.bpfurtado.tas.runner.CodeExecutionAnalyser;
import net.bpfurtado.tas.runner.PostCodeExecutionAction;
import net.bpfurtado.tas.runner.savegame.SaveGame;

/**
 * @author Bruno Patini Furtado
 */
public class GameImpl implements Game {

    private static final long serialVersionUID = -724652209259353975L;

    private static final int NO_SCENE_TO_GO = -1;

    private Adventure adventure;

    private Player player;

    private Scene currentScene;

    private Collection<GoToSceneListener> listeners = new LinkedList<GoToSceneListener>();

    /**
	 * Related to response of Scene Code Actions
	 */
    private int sceneIdToOpen = -1;

    /**
	 * Related to response of Scene Code Actions
	 */
    private Collection<Integer> pathsToHide = new LinkedList<Integer>();

    public GameImpl(Adventure a) {
        this.adventure = a;
        this.player = new Player("Player");
    }

    public void open(Scene scene) {
        innerOpenPath(scene);
        if (getSceneIdToOpen() != NO_SCENE_TO_GO) {
            Scene sceneOrientedByScriptCode = adventure.getScene(getSceneIdToOpen());
            innerOpenPath(sceneOrientedByScriptCode);
            setSceneToOpen(NO_SCENE_TO_GO);
        }
    }

    public void openNoActions(Scene to) {
        setCurrentScene(to);
    }

    public Adventure open(SaveGame saveGame) {
        this.player = saveGame.getPlayer();
        return adventure;
    }

    private void innerOpenPath(Scene scene) {
        setCurrentScene(scene);
        execCode(scene, scene.executeActions(this));
        execPostCodeActions();
        execAssertions();
        execPostCodeActions();
    }

    private void execCode(Scene scene, List<PostCodeExecutionAction> actions) {
        for (PostCodeExecutionAction action : actions) {
            action.exec(this);
        }
    }

    public void execPostCodeActions() {
        if (!pathsToHide.isEmpty()) {
            int order = 0;
            for (IPath p : currentScene.getPaths()) {
                if (pathsToHide.contains(order++)) {
                    p.setVisible(false);
                }
            }
            pathsToHide.clear();
        }
    }

    public void execAssertions() {
        List<PostCodeExecutionAction> actions = new CodeExecutionAnalyser().analyseCode(this, adventure.getAssertions(), getCurrentScene().getText());
        execCode(getCurrentScene(), actions);
    }

    public Player getPlayer() {
        return player;
    }

    public void addGoToSceneListener(GoToSceneListener goToSceneListener) {
        listeners.add(goToSceneListener);
    }

    public void setSceneToOpen(int sceneId) {
        this.sceneIdToOpen = sceneId;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void addPathToHideByOrder(Collection<Integer> pathsToHide) {
        this.pathsToHide = pathsToHide;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public int getSceneIdToOpen() {
        return sceneIdToOpen;
    }

    public Adventure getAdventure() {
        return adventure;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
