package ch.jworms.engine;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.golden.gamedev.Game;

/**
 * <code>SceneableGame</code> provides an easy way to handle different scenes in
 * a game.
 * <p>
 * You can register as many scenes as you want, each with its own, uniqe name. A
 * certain scene can be showed by calling <code>Scene.show()</code> or
 * <code>SceneableGame.showScene(String)</code> or
 * <code>SceneableGame.showScene(Scene)</code>. These functions set the scene as
 * the next scene to display. At the next update-cycle it will become the
 * current scene.
 * 
 * @see SceneableGame.showScene(String)
 * @see SceneableGame.showScene(Scene)
 * @see SceneableGame.update(long)
 * 
 * @author mbrotz
 * 
 */
public abstract class SceneableGame extends Game {

    private final HashMap<String, Scene> scenes;

    private Scene currentScene, nextScene;

    private boolean firstCycle;

    /**
   * This function is called by the constructor of class
   * <code>SceneableGame</code> and has to return an instance of the default
   * scene of the game.
   * 
   * @return The scene which is showed by default. If the return value is
   *         <code>null</code> an exception is thrown.
   */
    protected Scene createDefaultScene() {
        return new EmptyScene();
    }

    /**
   * 
   */
    public SceneableGame() {
        super();
        this.firstCycle = true;
        this.scenes = new HashMap<String, Scene>();
        Scene def = createDefaultScene();
        assert (def != null);
        registerScene(def, "default");
        this.nextScene = null;
        this.currentScene = def;
    }

    /**
   * @return The currently displayed scene. Normally should never be null.
   */
    public Scene currentScene() {
        return this.currentScene;
    }

    /**
   * @return The scene which will become the currently displayed scene at the
   *         next update-cycle.
   * 
   * @see #showScene(String)
   * @see #showScene(Scene)
   */
    public Scene nextScene() {
        return this.nextScene;
    }

    /**
   * Sets the property {@linkplain #nextScene()} to the scene identified by the
   * given name. Throws an error when there is no scene registered with the
   * given name. At success the function
   * {@linkplain #willBeHidden(Scene, Scene)} of the game and the current scene
   * is called with {@linkplain #currentScene()} and {@linkplain #nextScene()}
   * as parameters.
   * 
   * @param name
   *        - The name of the scene which has to be displayed. if
   *        <code>null</code>, the default-scene is used.
   * 
   * @return The scene which is registered with the given name.
   */
    public Scene showScene(String name) {
        Scene tmp = null;
        if (name != null) {
            tmp = this.scenes.get(name);
            if (tmp == null) {
            }
        } else {
            tmp = this.scenes.get("default");
        }
        if (tmp != this.currentScene) {
            this.nextScene = tmp;
            willBeHidden(this.currentScene, this.nextScene);
            this.currentScene.willBeHidden(this, this.nextScene);
        }
        return tmp;
    }

    /**
   * Sets the property {@linkplain #nextScene()} to the given scene. At success
   * the function {@linkplain #willBeHidden(Scene, Scene)} of the game and the
   * current scene is called with {@linkplain #currentScene()} and
   * {@linkplain #nextScene()} as parameters.
   * 
   * @param scene
   *        - The scene which has to be displayed. If <code>null</code>, the
   *        default-scene is used.
   * 
   * @return The given scene or the default-scene.
   */
    public Scene showScene(Scene scene) {
        if (scene == null) {
            scene = this.scenes.get("default");
            if (scene == null) {
            }
        }
        if (scene != this.currentScene) {
            this.nextScene = scene;
            willBeHidden(this.currentScene, this.nextScene);
            this.currentScene.willBeHidden(this, this.nextScene);
        }
        return scene;
    }

    /**
   * @param scene
   * @param name
   */
    public void registerScene(Scene scene, String name) {
        assert (scene != null);
        assert (name != null);
        Scene tmp = this.scenes.get(name);
        if (tmp == null) {
            this.scenes.put(name, scene);
            scene.registeredByGame(this, name);
        } else if (tmp != scene) {
        }
    }

    /**
   * @param name
   */
    public void unregisterScene(String name) {
        assert (name != null);
        Scene scene = this.scenes.remove(name);
        if (scene != null) {
            scene.unregisteredFromGame(this, name);
        } else {
        }
    }

    /**
   * @param scene
   */
    public void unregisterScene(Scene scene) {
        assert (scene != null);
        Iterator<Entry<String, Scene>> iterator = this.scenes.entrySet().iterator();
        ArrayList<String> names = new ArrayList<String>();
        while (iterator.hasNext()) {
            Entry<String, Scene> entry = iterator.next();
            if (entry.getValue() == scene) {
                names.add(entry.getKey());
            }
        }
        for (String name : names) {
            unregisterScene(name);
        }
    }

    /**
   * @param current
   * @param previous
   */
    public abstract void becameVisible(Scene current, Scene previous);

    /**
   * @param current
   * @param next
   */
    public abstract void willBeHidden(Scene current, Scene next);

    @Override
    public void render(Graphics2D g) {
        assert (this.currentScene != null);
        this.currentScene.render(this, g);
    }

    @Override
    public void update(long elapsedTime) {
        if (this.nextScene != null) {
            Scene previous = this.currentScene;
            this.currentScene = this.nextScene;
            this.nextScene = null;
            becameVisible(this.currentScene, previous);
            this.currentScene.becameVisible(this, previous);
            this.firstCycle = false;
        }
        assert (this.currentScene != null);
        if (this.firstCycle) {
            this.firstCycle = false;
            becameVisible(this.currentScene, null);
            this.currentScene.becameVisible(this, null);
        }
        this.currentScene.update(this, elapsedTime);
    }
}
