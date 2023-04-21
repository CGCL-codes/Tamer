package jemfis;

import java.io.File;
import jemfis.fs.themes.DefaultTheme;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.ogre4j.ColourValue;
import org.ogre4j.ICamera;
import org.ogre4j.IColourValue;
import org.ogre4j.INameValuePairList;
import org.ogre4j.IRenderSystemList;
import org.ogre4j.IRenderWindow;
import org.ogre4j.IRoot;
import org.ogre4j.ISceneManager;
import org.ogre4j.IViewport;
import org.ogre4j.NameValuePairList;
import org.ogre4j.ResourceGroupManager;
import org.ogre4j.Root;
import org.ogre4j.StringUtil;
import org.xbig.base.StringPointer;

/**
 * Main singleton application class.
 *
 * @author Max Vyaznikov
 */
public final class App {

    /** Default window width. */
    private static final int DEFAULT_WIDTH = 640;

    /** Default window height. */
    private static final int DEFAULT_HEIGHT = 480;

    /** Ogre Root. */
    private IRoot root;

    /** Main Camera. */
    private ICamera camera;

    /** Ogre RenderWindow object. Uses handle of ogreCanvas. */
    private IRenderWindow renderWindow;

    /** Set to true after exiting event loop. To avoid repaint after disposing of
     * canvas. */
    private boolean isDisposing = false;

    /** The SWT display.
     */
    private Display display;

    /** The SWT shell. */
    private Shell shell;

    /** The SWT canvas to render. */
    private Canvas canvas;

    /** The OGRE SceneManager. */
    private ISceneManager sceneManager;

    /** Current Jemfis theme */
    private Theme theme;

    private Navigator navigator;

    /** Default constructor. Sets media to "media" directory. */
    public App() {
        this("media", "FileSystem");
    }

    /** This constructor is the main program.
     * @param media
     *            The OGRE resource location.
     * @param locType
     *            The OGRE resource location type.
     */
    private App(String media, String locType) {
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        try {
            String libPath = "lib" + File.separator;
            System.load(new File(libPath + "OgreMain.dll").getAbsolutePath());
            System.load(new File(libPath + "RenderSystem_GL.dll").getAbsolutePath());
            setup(operatingSystem, media, locType, "OpenGL");
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
            cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Shuts down the app. */
    private void cleanup() {
        isDisposing = true;
        display.dispose();
        root.delete();
    }

    /** Sets up the app.
     *
     * @param media
     *            The path to the media (resourcelocation).
     * @param locType
     *            The OGRE type of the resource location.
     * @param renderSystem
     *
     * @throws Exception
     *             if an error occurs while initilaising OGRE.
     */
    private void setup(String operatingSystem, String media, String locType, String renderSystem) throws Exception {
        root = new Root("", "", this.getClass().getName() + ".log");
        display = new Display();
        shell = new Shell(display);
        shell.setText("Jemfis");
        shell.setLayout(new FillLayout());
        shell.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        if (operatingSystem.equals("linux")) {
            GLData data = new GLData();
            data.doubleBuffer = true;
            canvas = new GLCanvas(shell, SWT.NONE, data);
            ((GLCanvas) canvas).setCurrent();
        } else {
            canvas = new Canvas(shell, SWT.EMBEDDED);
        }
        shell.open();
        root.loadPlugin("RenderSystem_GL");
        IRenderSystemList list = root.getAvailableRenderers();
        if (list == null || list.size() == 0) {
            throw new Exception("No RenderSystem loaded!");
        }
        root.setRenderSystem(list.at(0));
        try {
            root.initialise(false, "", StringUtil.getBLANK());
        } catch (NullPointerException e) {
        }
        INameValuePairList params = new NameValuePairList();
        StringPointer windowHandlePointer;
        if (operatingSystem.equals("linux")) {
            windowHandlePointer = new StringPointer("true");
            params.insert("currentGLContext", windowHandlePointer);
        } else {
            windowHandlePointer = new StringPointer(Integer.toString(canvas.handle));
            params.insert("externalWindowHandle", windowHandlePointer);
        }
        renderWindow = root.createRenderWindow("", canvas.getBounds().width, canvas.getBounds().height, false, params);
        params.delete();
        windowHandlePointer.delete();
        sceneManager = root.createSceneManager(0, "Default");
        camera = sceneManager.createCamera("Main Camera");
        camera.setPosition(0, 0, 50);
        camera.lookAt(0, 0, 0);
        camera.setNearClipDistance(0.1f);
        IViewport viewport = renderWindow.addViewport(camera, 0, 0.0f, 0.0f, 1.0f, 1.0f);
        IColourValue backgroundColour = new ColourValue(0.3f, 0.3f, 0.3f, 1.0f);
        viewport.setBackgroundColour(backgroundColour);
        backgroundColour.delete();
        viewport.setOverlaysEnabled(true);
        viewport.setCamera(camera);
        camera.setAspectRatio((float) canvas.getBounds().width / (float) canvas.getBounds().height);
        renderWindow.setActive(true);
        ResourceGroupManager.getSingleton().addResourceLocation(media, locType, "General", false);
        ResourceGroupManager.getSingleton().initialiseAllResourceGroups();
        theme = new DefaultTheme();
        this.navigator = theme.init(camera, canvas, sceneManager);
        canvas.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                int height = canvas.getBounds().height;
                int width = canvas.getBounds().width;
                if (height == 0) {
                    height = 1;
                }
                if (width == 0) {
                    width = 1;
                }
                if (isDisposing == true || camera == null || renderWindow == null) {
                    return;
                }
                camera.setAspectRatio((float) width / (float) height);
                renderWindow.windowMovedOrResized();
                root.renderOneFrame();
            }
        });
        root.renderOneFrame();
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public Theme getTheme() {
        return theme;
    }

    public static void debugMsg(Object obj, String msg) {
        System.out.println("Jemfis> " + obj.toString() + ":" + msg);
    }

    /** Main function.
     * @param args
     *            commandline arguments.
     */
    public static void main(String[] args) {
        new App();
    }
}
