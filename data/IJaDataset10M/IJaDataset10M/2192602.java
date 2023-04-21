package scenes.editor;

import java.awt.event.KeyEvent;
import java.io.File;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import event.CompositeData;
import event.EventHandler;
import gui.Window;
import objects.dialog.Tooltip;
import objects.editor.LayerEditorPanel;
import objects.editor.ObjectEditorPanel;
import objects.editor.PlayerEditorPanel;
import objects.editor.PropertyEditorPanel;
import resource.GetResourceLoaderException;
import resource.LevelFactoryFactory;
import resource.LoadResourceException;
import resource.ResourceLoader;
import resource.meta.LevelMeta;
import scene.RObject;
import scene.Scene;
import scene.factory.LevelFactory;
import scene.factory.ObjectCreateException;
import scene.factory.SerializationException;
import scenes.base.FormScene;
import scenes.dialog.ErrorDialog;
import scenes.dialog.YesNoDialog;
import scenes.editor.InfoDialog.Info;
import visual.Gradient;

/**
 * The scene representing the complete level editor.
 * 
 * @author tom
 * 
 */
public class EditorScene extends Scene implements FormScene {

    /**
	 * The border around the level view when the view is zoomed out.
	 */
    public static final double EDITOR_BORDER = 5;

    /**
	 * The scene containing the preview and the factory objects.
	 */
    protected FactoryScene factoryScene;

    /**
	 * The top of the level view (in screen coordinates).
	 */
    double levelTop = 0;

    /**
	 * The bottom of the level view (in screen coordinates).
	 */
    double levelBottom = getHeight();

    /**
	 * The left of the level view (in screen coordinates).
	 */
    double levelLeft = 0;

    /**
	 * The right of the level view (in screen coordinates).
	 */
    double levelRight = getWidth();

    /**
	 * The gradient to be displayed in the top left corner above the level view.
	 */
    Gradient topLeft = new Gradient();

    /**
	 * The gradient to be displayed straight above the level view.
	 */
    Gradient top = new Gradient();

    /**
	 * The gradient to be displayed in the top right corner above the level
	 * view.
	 */
    Gradient topRight = new Gradient();

    /**
	 * The gradient to be displayed to the left of the level view.
	 */
    Gradient left = new Gradient();

    /**
	 * The gradient to be displayed to the right of the level view.
	 */
    Gradient right = new Gradient();

    /**
	 * The gradient to be displayed in the bottom left corner below the level
	 * view.
	 */
    Gradient bottomLeft = new Gradient();

    /**
	 * The gradient to be displayed right below the level view the level view.
	 */
    Gradient bottom = new Gradient();

    /**
	 * The gradient to be displayed in the bottom right corner below the level
	 * view.
	 */
    Gradient bottomRight = new Gradient();

    /**
	 * The bottom (player) panel.
	 */
    PlayerEditorPanel bottomPanel;

    /**
	 * The top (layer) panel.
	 */
    LayerEditorPanel topPanel;

    /**
	 * The left (object) panel.
	 */
    ObjectEditorPanel leftPanel;

    /**
	 * The right (property) panel.
	 */
    PropertyEditorPanel rightPanel;

    /**
	 * The tooltip that is shown when the mouse cursor is hovered over one of
	 * the various components.
	 */
    Tooltip tooltip;

    /**
	 * The OpenGL object. Saved here for usage in code for loading levels.
	 */
    GL gl;

    /**
	 * The currently focused form object.
	 */
    RObject focused = null;

    /**
	 * @param gl
	 *            The OpenGL object used for the textures.
	 * @param window
	 *            The window containing this scene.
	 * @throws GetResourceLoaderException
	 *             if the resource loader can not be retrieved.
	 * @throws LoadResourceException
	 *             if a required resource can not be loaded.
	 */
    public EditorScene(GL gl, Window window) throws GetResourceLoaderException, LoadResourceException {
        super(window);
        this.gl = gl;
        newLevel();
        tooltip = new Tooltip(this);
        bottomPanel = new PlayerEditorPanel(this, gl);
        topPanel = new LayerEditorPanel(this, gl);
        leftPanel = new ObjectEditorPanel(this, gl);
        rightPanel = new PropertyEditorPanel(this, gl);
        onStep().addHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                factoryScene.step();
                double desiredLeft = 0;
                double desiredTop = 0;
                double desiredBottom = getHeight();
                double desiredRight = getWidth();
                if (bottomPanel.getMouseOver()) {
                    desiredBottom = getHeight() - bottomPanel.getHeight() - EDITOR_BORDER;
                    desiredTop = EDITOR_BORDER;
                }
                if (topPanel.getMouseOver()) {
                    desiredTop = topPanel.getHeight() + EDITOR_BORDER;
                    desiredBottom = getHeight() - EDITOR_BORDER;
                }
                if (leftPanel.getMouseOver()) {
                    desiredLeft = leftPanel.getWidth() + EDITOR_BORDER;
                    desiredRight = getWidth() - EDITOR_BORDER;
                }
                if (rightPanel.getMouseOver()) {
                    desiredLeft = EDITOR_BORDER;
                    desiredRight = getWidth() - EDITOR_BORDER - rightPanel.getWidth();
                }
                final double ratio = getWidth() / getHeight();
                if (ratio * (desiredBottom - desiredTop) < (desiredRight - desiredLeft)) {
                    desiredLeft = (getWidth() - ratio * (desiredBottom - desiredTop)) / 2;
                    desiredRight = (getWidth() + ratio * (desiredBottom - desiredTop)) / 2;
                } else {
                    desiredTop = (getHeight() - (desiredRight - desiredLeft) / ratio) / 2;
                    desiredBottom = (getHeight() + (desiredRight - desiredLeft) / ratio) / 2;
                }
                levelLeft = (levelLeft * 9 + desiredLeft) / 10;
                levelRight = (levelRight * 9 + desiredRight) / 10;
                levelTop = (levelTop * 9 + desiredTop) / 10;
                levelBottom = (levelBottom * 9 + desiredBottom) / 10;
            }
        });
        bottomPanel.onPlayPause().setHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                playPause();
            }
        });
        bottomPanel.onStop().setHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                stop();
            }
        });
        onKeyPress().addHandler(KeyEvent.VK_SPACE, new EventHandler<CompositeData<Integer, Void>>() {

            @Override
            public void handle(CompositeData<Integer, Void> data) {
                playPause();
            }
        });
        onKeyPress().addHandler(KeyEvent.VK_R, new EventHandler<CompositeData<Integer, Void>>() {

            @Override
            public void handle(CompositeData<Integer, Void> data) {
                stop();
            }
        });
        bottomPanel.onLoad().setHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                load();
            }
        });
        bottomPanel.onSave().setHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                save();
            }
        });
        bottomPanel.onInfo().setHandler(new EventHandler<Void>() {

            @Override
            public void handle(Void data) {
                changeInfo(false);
            }
        });
        double[] dkgray = { .06, .06, .06 };
        topLeft.setBottomRight(dkgray);
        top.setBottomLeft(dkgray);
        top.setBottomRight(dkgray);
        topRight.setBottomLeft(dkgray);
        left.setTopRight(dkgray);
        left.setBottomRight(dkgray);
        right.setTopLeft(dkgray);
        right.setBottomLeft(dkgray);
        bottomLeft.setTopRight(dkgray);
        bottom.setTopLeft(dkgray);
        bottom.setTopRight(dkgray);
        bottomRight.setTopLeft(dkgray);
        onMousePressed().addHandler(new EventHandler<Integer>() {

            @Override
            public void handle(Integer data) {
                focus(null);
            }
        }, -1);
    }

    /**
	 * Open a dialog which allows the user to change level information, such as
	 * the name and creator.
	 * 
	 * @param save
	 *            Whether to open the save dialog directly afterwards.
	 */
    public void changeInfo(boolean save) {
        String name = factoryScene.getName();
        if (name == null) name = "Level";
        String description = factoryScene.getDescription();
        if (description == null) description = "";
        String creator = factoryScene.getCreator();
        if (creator == null) creator = "Anonymous";
        String screenshotPath = factoryScene.getScreenshotPath();
        if (screenshotPath == null) screenshotPath = "";
        try {
            InfoDialog d = new InfoDialog(getWindow(), name, creator, screenshotPath, description);
            if (save) d.onReturn().setHandler(new EventHandler<Info>() {

                @Override
                public void handle(Info data) {
                    factoryScene.setName(data.name);
                    factoryScene.setCreator(data.creator);
                    factoryScene.setDescription(data.description);
                    factoryScene.setScreenshotPath(data.screenshotPath);
                    save();
                }
            }); else d.onReturn().setHandler(new EventHandler<Info>() {

                @Override
                public void handle(Info data) {
                    factoryScene.setName(data.name);
                    factoryScene.setCreator(data.creator);
                    factoryScene.setDescription(data.description);
                    factoryScene.setScreenshotPath(data.screenshotPath);
                }
            });
            showDialog(d);
        } catch (LoadResourceException e) {
            ErrorDialog.showException(this, e);
        } catch (GetResourceLoaderException e) {
            ErrorDialog.showException(this, e);
        }
    }

    /**
	 * Present the user with a "load" dialog, allowing the user to load a level
	 * from a file on the system.
	 */
    public void load() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFileChooser dialog = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Level Files", "xml");
                dialog.setFileFilter(filter);
                if (dialog.showOpenDialog(getWindow().getFrame()) == JFileChooser.APPROVE_OPTION) {
                    load(dialog.getSelectedFile());
                }
            }
        });
    }

    /**
	 * Load a level from a chosen file on the system.
	 * 
	 * @param path
	 *            The file containing the level information.
	 */
    public void load(File path) {
        onStep().addHandler(new LoadHandler(path.getAbsolutePath()));
    }

    /**
	 * Present the user with a save dialog, allowing the user to save a level to
	 * a path on the file system.
	 * 
	 * If the level information is incomplete, the user is first presented with
	 * a dialog to fill in the necessary information.
	 */
    public void save() {
        if (factoryScene.isInfoComplete()) SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFileChooser dialog = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Level Files", "xml");
                dialog.setFileFilter(filter);
                dialog.setSelectedFile(new File(factoryScene.getName().replace(" ", "").toLowerCase() + ".xml"));
                if (dialog.showSaveDialog(getWindow().getFrame()) == JFileChooser.APPROVE_OPTION) {
                    File newFile = dialog.getSelectedFile();
                    if (newFile.getAbsolutePath().endsWith(".xml")) save(newFile); else save(new File(newFile.getAbsolutePath() + ".xml"));
                }
            }
        }); else changeInfo(true);
    }

    /**
	 * Save a level to a path on the file system. If the file already exists,
	 * present the user with a dialog, allowing him to overwrite the file or
	 * cancel the operation.
	 * 
	 * Note that the level information should be complete.
	 * 
	 * In essence, this is a deferred call to saveCheck(path). If no deferred
	 * call is necessary, a call to saveFile is preferred.
	 * 
	 * @param path
	 *            The path for the file.
	 */
    public void save(File path) {
        onStep().addHandler(new SaveCheckHandler(path));
    }

    /**
	 * Get a short representation of a file system path, showing only a few
	 * levels in the hierarchy.
	 * 
	 * Example: /home/tom/some/long/path with three levels becomes
	 * .../some/long/path .
	 * 
	 * @param path
	 *            The path to shorten.
	 * @param levels
	 *            The number of levels to show.
	 * @return The shortened path.
	 */
    private static String shortPath(File path, int levels) {
        String n = path.getName();
        for (int i = 1; i < levels; ++i) {
            path = path.getParentFile();
            if (path == null) return File.separatorChar + n;
            n = path.getName() + File.separatorChar + n;
        }
        if (path.getParent() == null) return File.separatorChar + n;
        return "..." + File.separatorChar + n;
    }

    /**
	 * Save a level to a path on the file system. If the file already exists,
	 * present the user with a dialog, allowing him to overwrite the file or
	 * cancel the operation.
	 * 
	 * Note that the level information should be complete.
	 * 
	 * @param path
	 *            The path for the file.
	 */
    public void saveCheck(File path) {
        if (path.exists()) {
            YesNoDialog ynDialog;
            try {
                ynDialog = new YesNoDialog(getWindow(), "The following file already exists:\n" + shortPath(path, 3) + "\nAre you sure you want to continue?");
                ynDialog.onYes().setHandler(new SaveHandler(path.getAbsolutePath()));
                showDialog(ynDialog);
            } catch (LoadResourceException e) {
                ErrorDialog.showException(EditorScene.this, e);
            } catch (GetResourceLoaderException e) {
                ErrorDialog.showException(EditorScene.this, e);
            }
        } else saveFile(path.getAbsolutePath());
    }

    /**
	 * Save a level to a path on the file system. If the file already exists, it
	 * is overwritten.
	 * 
	 * Note that the level information should be complete.
	 * 
	 * @param path
	 *            The path for the file.
	 */
    public void saveFile(String path) {
        LevelFactory factory = factoryScene.getFactory();
        try {
            Document doc = factory.serialize();
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            StreamResult result = new StreamResult(new File(path));
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
        } catch (SerializationException e) {
            ErrorDialog.showException(this, e);
        } catch (TransformerException e) {
            ErrorDialog.showException(this, e);
        }
    }

    @Override
    public Tooltip getTooltip() {
        return tooltip;
    }

    /**
	 * Toggle play/pause in the preview. If the preview is stopped, start it.
	 */
    public void playPause() {
        if (isRunning()) {
            pause();
        } else {
            start();
        }
    }

    /**
	 * Get whether the preview is currently running.
	 * 
	 * @return Whether the preview is currently running.
	 */
    public boolean isRunning() {
        return factoryScene.isRunning();
    }

    /**
	 * Pause the preview.
	 */
    public void pause() {
        factoryScene.pause();
        bottomPanel.pause();
    }

    /**
	 * Start the preview (continue if paused, start if stopped).
	 */
    public void start() {
        try {
            factoryScene.start();
        } catch (ObjectCreateException e) {
            ErrorDialog.showException(this, e);
        }
        bottomPanel.play();
    }

    /**
	 * Stop the preview.
	 */
    public void stop() {
        factoryScene.stop();
        bottomPanel.pause();
    }

    /**
	 * Create a new level.
	 */
    void newLevel() {
        factoryScene = new FactoryScene(window);
        factoryScene.setParent(this);
    }

    /**
	 * Load a level from a file.
	 * 
	 * @param path
	 *            The path to the file. This may not be an internal path, but
	 *            the actual location on the file system must be used.
	 * @param gl
	 *            The OpenGL object, which is used for loading resources.
	 */
    void loadFile(String path, GL gl) {
        newLevel();
        LevelFactoryFactory f = new LevelFactoryFactory(0);
        LevelMeta m = new LevelMeta(null);
        m.setPath(path);
        m.setLocal(false);
        LevelFactory fac;
        try {
            fac = f.getResource(path, ResourceLoader.getInstance(), m, null);
            factoryScene.load(fac, gl);
        } catch (LoadResourceException e) {
            ErrorDialog.showException(this, e);
        } catch (GetResourceLoaderException e) {
            ErrorDialog.showException(this, e);
        } catch (ObjectCreateException e) {
            ErrorDialog.showException(this, e);
        }
    }

    @Override
    public void keyPressed(int key) {
        super.keyPressed(key);
        factoryScene.keyPressed(key);
    }

    @Override
    public void keyReleased(int key) {
        super.keyReleased(key);
        factoryScene.keyReleased(key);
    }

    @Override
    public void keyTyped(char character) {
        super.keyTyped(character);
        factoryScene.keyTyped(character);
    }

    @Override
    public void mouseMove(int x, int y) {
        super.mouseMove(x, y);
        factoryScene.mouseMove((int) Math.round((x - levelLeft) * getWidth() / (levelRight - levelLeft)), (int) Math.round((y - levelTop) * getHeight() / (levelBottom - levelTop)));
    }

    @Override
    public void mousePress(int button) {
        super.mousePress(button);
        factoryScene.mousePress(button);
    }

    @Override
    public void mouseRelease(int button) {
        super.mouseRelease(button);
        factoryScene.mouseRelease(button);
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        final double w = (levelRight - levelLeft) / getWidth();
        final double h = (levelBottom - levelTop) / getHeight();
        gl.glTranslated(levelLeft, levelTop, 0);
        gl.glScaled(w, h, 1);
        factoryScene.draw(gl);
        gl.glPopMatrix();
        topLeft.draw(gl, 0, 0, levelLeft, levelTop);
        top.draw(gl, levelLeft, 0, levelRight, levelTop);
        topRight.draw(gl, levelRight, 0, getWidth(), levelTop);
        left.draw(gl, 0, levelTop, levelLeft, levelBottom);
        right.draw(gl, levelRight, levelTop, getWidth(), levelBottom);
        bottomLeft.draw(gl, 0, levelBottom, levelLeft, getHeight());
        bottom.draw(gl, levelLeft, levelBottom, levelRight, getHeight());
        bottomRight.draw(gl, levelRight, levelBottom, getWidth(), getHeight());
        drawObjects(gl);
    }

    /**
	 * Get the factory scene embedded in this editor.
	 * 
	 * @return The factory scene embedded in this editor.
	 */
    public FactoryScene getFactoryScene() {
        return factoryScene;
    }

    /**
	 * An event handler for the step event, causing some level to be loaded. The
	 * reason for this is that the load dialog is executed in a deferred call to
	 * the AWT thread. During this deferred call, some resources can not be
	 * loaded. The solution is to use a new deferred call from the one
	 * displaying the "Load" window. This is done in the form of an event
	 * handler which is executed exactly once, in the next step event.
	 * 
	 * @author tom
	 * 
	 */
    class LoadHandler implements EventHandler<Void> {

        /**
		 * The path to the level. This must be an external (file system) path.
		 */
        String path;

        /**
		 * @param path
		 *            The path to the level. This must be an external (file
		 *            system) path.
		 */
        public LoadHandler(String path) {
            this.path = path;
        }

        @Override
        public void handle(Void data) {
            onStep().removeHandler(this);
            loadFile(path, gl);
        }
    }

    /**
	 * An event handler for the step event, causing the level to be saved to
	 * some location. The reason for this is that the save dialog is executed in
	 * a deferred call to the AWT thread. During this deferred call, some
	 * resources can not be loaded, so dialogs can not be generated. The
	 * solution is to use a new deferred call from the one displaying the "Save"
	 * window. This is done in the form of an event handler which is executed
	 * exactly once, in the next step event.
	 * 
	 * @author tom
	 * 
	 */
    class SaveCheckHandler implements EventHandler<Void> {

        /**
		 * The path for the level. This must be an external (file system) path.
		 */
        File path;

        /**
		 * @param path
		 *            The path for the level. This must be an external (file
		 *            system) path.
		 */
        public SaveCheckHandler(File path) {
            this.path = path;
        }

        @Override
        public void handle(Void data) {
            onStep().removeHandler(this);
            saveCheck(path);
        }
    }

    /**
	 * An event handler used as a callback from a dialog asking for confirmation
	 * about saving.
	 * 
	 * @author tom
	 * 
	 */
    class SaveHandler implements EventHandler<Void> {

        /**
		 * The path for the level. This must be an external (file system) path.
		 */
        String path;

        /**
		 * @param path
		 *            The path for the level. This must be an external (file
		 *            system) path.
		 */
        public SaveHandler(String path) {
            this.path = path;
        }

        @Override
        public void handle(Void data) {
            saveFile(path);
        }
    }

    @Override
    public RObject getFocused() {
        return focused;
    }

    @Override
    public void focus(RObject object) {
        focused = object;
    }
}
