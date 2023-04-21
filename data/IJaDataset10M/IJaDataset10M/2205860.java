package eu.cherrytree.paj.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.Animator;
import eu.cherrytree.paj.base.AppDefinition;
import eu.cherrytree.paj.file.FileReader;
import eu.cherrytree.paj.graphics.light.Light;
import eu.cherrytree.paj.graphics.light.LightManager;
import eu.cherrytree.paj.gui.Console;
import eu.cherrytree.paj.math.TransformationMatrix;
import eu.cherrytree.paj.math.Vector2d;
import eu.cherrytree.paj.math.Vector3d;
import eu.cherrytree.paj.utilities.BufferUtil;
import eu.cherrytree.paj.utilities.Configuration;
import eu.cherrytree.paj.utilities.MessageBox;

public class Graphics {

    public static class ShaderProgramProcessException extends Exception {

        private static final long serialVersionUID = 3911897715670247319L;

        public ShaderProgramProcessException(String msg) {
            super(msg);
        }
    }

    public static class ShaderNotLoadedException extends ShaderProgramProcessException {

        private static final long serialVersionUID = 4810891246177280710L;

        public ShaderNotLoadedException(String v_path, String f_path) {
            super("Couldn't load shader: " + v_path + " " + f_path);
        }
    }

    public static class ShaderNotCompiledException extends ShaderProgramProcessException {

        private static final long serialVersionUID = 4312708912162648932L;

        public ShaderNotCompiledException(String msg) {
            super("Couldn't compile shader. " + msg);
        }
    }

    public static class ShaderNotLinkedException extends ShaderProgramProcessException {

        private static final long serialVersionUID = 4312708912162648932L;

        public ShaderNotLinkedException(String msg) {
            super("Couldn't link shader. " + msg);
        }
    }

    public static class ShaderNotValidatedException extends ShaderProgramProcessException {

        private static final long serialVersionUID = 4312708912162648932L;

        public ShaderNotValidatedException() {
            super("Couldn't validate shader.");
        }
    }

    public static class ScreenResolution {

        public int resX;

        public int resY;

        public String resStr;

        ScreenResolution(int rx, int ry) {
            resX = rx;
            resY = ry;
            resStr = rx + "x" + ry;
        }
    }

    public static class Shader {

        int shaderId;

        int viewMatrixUniformId;

        int modelMatrixUniformId;

        int fogVerticalDisperseUniformId;

        int[] textureUnitIds;

        public Shader(int shaderId, int[] textureUnitIds, int viewMatrixUniformId, int modelMatrixUniformId, int fogVerticalDisperseUniformId) {
            update(shaderId, textureUnitIds, viewMatrixUniformId, modelMatrixUniformId, fogVerticalDisperseUniformId);
        }

        public void update(int shaderId, int[] textureUnitIds, int viewMatrixUniformId, int modelMatrixUniformId, int fogVerticalDisperseUniformId) {
            if (textureUnitIds != null) this.textureUnitIds = textureUnitIds.clone(); else this.textureUnitIds = new int[0];
            this.shaderId = shaderId;
            this.viewMatrixUniformId = viewMatrixUniformId;
            this.modelMatrixUniformId = modelMatrixUniformId;
            this.fogVerticalDisperseUniformId = fogVerticalDisperseUniformId;
        }

        public int getShaderId() {
            return shaderId;
        }
    }

    public static final float near = 0.1f;

    public static final float far = 1000.0f;

    private static int width = 800;

    private static int height = 600;

    private static int screenWidth;

    private static int screenHeight;

    private static float[] currentViewMatrix;

    private static float[] projectionMatrix = new float[16];

    private static int[] viewportData = new int[4];

    private static GL2 gl;

    private static GLUgl2 glu = new GLUgl2();

    private static int antialiasing = 0;

    private static boolean vSync = false;

    private static boolean fullscreen = false;

    private static boolean showShaderCompileOutput = false;

    private static Frame appFrame;

    private static GLCanvas appCanvas;

    private static Animator animator;

    private static Vector<ScreenResolution> availableResolutions;

    private static Shader currentShader;

    private static ShaderID currentShaderID;

    private static Vector<ShaderBank> shaderBanks = new Vector<ShaderBank>();

    private static ShaderBank currentShaderBank;

    private static Map<String, Shader> defaultShaders = new HashMap<String, Shader>();

    public Graphics(Configuration config) {
        checkAvailableResolutions();
        GraphicsDevice gs = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        width = config.getResolutionX();
        height = config.getResolutionY();
        antialiasing = config.getAntialiasing();
        vSync = config.getVerticalSync();
        if (antialiasing > 0) {
            capabilities.setSampleBuffers(true);
            capabilities.setNumSamples(antialiasing);
        }
        if (gs.isFullScreenSupported() && config.getFullscreen()) fullscreen = true;
        appFrame = new Frame(AppDefinition.getApplicationFullName());
        appFrame.setBackground(new Color(0.0f, 0.0f, 0.0f));
        if (fullscreen) {
            appFrame.setUndecorated(true);
            appFrame.setIgnoreRepaint(true);
        }
        appCanvas = new GLCanvas(capabilities);
        appFrame.add(appCanvas);
        appFrame.setSize(width, height);
        Dimension d = appFrame.getToolkit().getScreenSize();
        screenWidth = d.width;
        screenHeight = d.height;
        appFrame.setLocation(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2);
        if (AppDefinition.getIconPath() != null) {
            try {
                String iconpath = AppDefinition.getDefaultDataPackagePath() + "/" + AppDefinition.getIconPath();
                BufferedImage img = ImageIO.read(new eu.cherrytree.paj.file.FileReader(iconpath).getInputStream());
                appFrame.setIconImage(img);
            } catch (Exception e) {
                System.err.println("Couldn't set icon.");
            }
        }
        animator = new Animator(appCanvas);
        animator.setRunAsFastAsPossible(true);
        appFrame.setVisible(true);
        appCanvas.requestFocus();
        if (fullscreen) {
            gs.setFullScreenWindow(appFrame);
            boolean nofull = !gs.isDisplayChangeSupported();
            DisplayMode[] modes = gs.getDisplayModes();
            int bit_depth = DisplayMode.BIT_DEPTH_MULTI;
            int ref_rate = DisplayMode.REFRESH_RATE_UNKNOWN;
            for (int i = 0; i < modes.length; i++) {
                if (modes[i].getWidth() == width && modes[i].getHeight() == height) {
                    if (bit_depth <= modes[i].getBitDepth() && ref_rate <= modes[i].getRefreshRate()) {
                        bit_depth = modes[i].getBitDepth();
                        ref_rate = modes[i].getRefreshRate();
                    }
                }
            }
            DisplayMode dm = new DisplayMode(width, height, bit_depth, ref_rate);
            if (!nofull) {
                try {
                    gs.setDisplayMode(dm);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Display Mode:");
                    System.err.println("	width:" + dm.getWidth());
                    System.err.println("	height:" + dm.getHeight());
                    System.err.println("	bit depth:" + dm.getBitDepth());
                    System.err.println("	refresh rate:" + dm.getRefreshRate());
                    nofull = true;
                }
            }
            if (nofull) {
                gs.setFullScreenWindow(null);
                appFrame.setIgnoreRepaint(false);
                if (appFrame.isDisplayable()) {
                    appFrame.dispose();
                    appFrame.setUndecorated(false);
                    appFrame.setSize(width, height);
                    appFrame.setLocation(screenWidth / 2 - width / 2, screenHeight / 2 - height / 2);
                    appFrame.setVisible(true);
                } else appFrame.setUndecorated(false);
                fullscreen = false;
                MessageBox msg = new MessageBox(appFrame, "Error!", "Could not initiate fullscreen mode!", false, true);
                msg.dispose();
            }
        }
        currentViewMatrix = new TransformationMatrix().getMatrix();
    }

    public void init(GL2 ogl) {
        gl = ogl;
        if (vSync) gl.setSwapInterval(1); else gl.setSwapInterval(0);
        setDefaultViewport();
        setClearColor(0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glFrontFace(GL2.GL_CW);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (antialiasing != 0) gl.glEnable(GL2.GL_MULTISAMPLE);
    }

    public void reshape(GL2 ogl, int w, int h) {
        gl = ogl;
        width = w;
        height = h;
        if (vSync) gl.setSwapInterval(1); else gl.setSwapInterval(0);
        setDefaultViewport();
    }

    public void setLimitFPS(boolean limit) {
        if (limit) animator.setRunAsFastAsPossible(false); else animator.setRunAsFastAsPossible(true);
    }

    public static GL2 getGL() {
        return gl;
    }

    public static Frame getWindow() {
        return appFrame;
    }

    public static Canvas getCanvas() {
        return appCanvas;
    }

    public static boolean isFullscreen() {
        return fullscreen;
    }

    public void startGraphics(GLEventListener application) {
        appCanvas.addGLEventListener(application);
        animator.start();
    }

    public void startGraphics() {
        animator.start();
    }

    public void stopGraphics() {
        animator.stop();
    }

    public static Vector<ScreenResolution> getAvailableResolutions() {
        Vector<ScreenResolution> resolutions = new Vector<Graphics.ScreenResolution>();
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (int i = 0; i < devices.length; i++) {
            DisplayMode[] modes = devices[i].getDisplayModes();
            for (int j = 0; j < modes.length; j++) resolutions.add(new ScreenResolution(modes[j].getWidth(), modes[j].getHeight()));
        }
        return resolutions;
    }

    private void checkAvailableResolutions() {
        availableResolutions = getAvailableResolutions();
    }

    public static void checkForGLErrors() {
        GL2 gl = getGL();
        int err = gl.glGetError();
        if (err != GL2.GL_NO_ERROR) {
            String s_err = "";
            switch(err) {
                case GL2.GL_INVALID_ENUM:
                    s_err = "GL_INVALID_ENUM";
                    break;
                case GL2.GL_INVALID_VALUE:
                    s_err = "GL_INVALID_VALUE";
                    break;
                case GL2.GL_INVALID_OPERATION:
                    s_err = "GL_INVALID_OPERATION";
                    break;
                case GL2.GL_OUT_OF_MEMORY:
                    s_err = "GL_OUT_OF_MEMORY";
                    break;
                case GL2.GL_INVALID_FRAMEBUFFER_OPERATION:
                    s_err = "GL_INVALID_FRAMEBUFFER_OPERATION";
                    break;
                case GL2.GL_STACK_OVERFLOW:
                    s_err = "GL_STACK_OVERFLOW";
                    break;
                case GL2.GL_STACK_UNDERFLOW:
                    s_err = "GL_STACK_UNDERFLOW";
                    break;
                case GL2.GL_TABLE_TOO_LARGE:
                    s_err = "GL_TABLE_TOO_LARGE";
                    break;
            }
            System.err.println("OpenGL Error: " + s_err + " (0x" + Integer.toHexString(err) + ")");
        }
    }

    public static void removeSystemCursor() {
        appFrame.setCursor(appFrame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
    }

    public static void restoreSystemCursor() {
        appFrame.setCursor(Cursor.getDefaultCursor());
    }

    public static Vector2d getLocationOnScreen(int x, int y) {
        Point loc = appFrame.getLocation();
        Insets in = appFrame.getInsets();
        return new Vector2d(x + loc.x + in.left, y + loc.y + in.top);
    }

    public static void getLocationOnScreen(int x, int y, Vector2d out) {
        Point loc = appFrame.getLocation();
        Insets in = appFrame.getInsets();
        out.set(x + loc.x + in.left, y + loc.y + in.top);
    }

    public static void setShowShaderCompilerOutput(boolean shaderoutput) {
        showShaderCompileOutput = shaderoutput;
    }

    public static void addShaderBank(ShaderBank bank) {
        shaderBanks.add(bank);
    }

    public static ShaderBank getShaderBank(Light.LightType[] lightType, boolean fog) {
        for (int i = 0; i < shaderBanks.size(); i++) {
            ShaderBank sb = shaderBanks.get(i);
            if (sb.isType(lightType, fog)) return sb;
        }
        return null;
    }

    public static ShaderBank getCurrentShaderBank() {
        return currentShaderBank;
    }

    public static void loadDefaultShaders() throws ShaderProgramProcessException {
        defaultShaders.put("basic", loadShader("basic/basic.vert", "basic/basic.frag", 1, true, true));
        defaultShaders.put("ui shader", loadShader("basic/uishader.vert", "basic/uishader.frag", 1, false, false));
        defaultShaders.put("framebuffer flush shader", loadShader("basic/framebufferflush.vert", "basic/framebufferflush.frag", 1, false, false));
    }

    public static Shader loadShader(String vspath, String fspath, int textureUnits, boolean separateCam, boolean fog) throws ShaderProgramProcessException {
        if (vspath == "" || fspath == "") return null;
        BufferedReader in;
        String vert = "", frag = "";
        try {
            URL v_url = Graphics.class.getClass().getResource("/eu/cherrytree/paj/graphics/shaders/" + vspath);
            String v_path = AppDefinition.getDefaultDataPackagePath() + "/shaders/" + vspath;
            if (v_url != null) in = new BufferedReader(new InputStreamReader(v_url.openStream())); else in = new BufferedReader(new InputStreamReader(new FileReader(v_path).getInputStream()));
            boolean run = true;
            String str;
            while (run) {
                str = in.readLine();
                if (str != null) vert += str + "\n"; else run = false;
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Couldn't read in vertex shader \"" + vspath + "\".");
            throw new ShaderNotLoadedException(vspath, fspath);
        }
        try {
            URL f_url = Graphics.class.getClass().getResource("/eu/cherrytree/paj/graphics/shaders/" + fspath);
            String f_path = AppDefinition.getDefaultDataPackagePath() + "/shaders/" + fspath;
            if (f_url != null) in = new BufferedReader(new InputStreamReader(f_url.openStream())); else in = new BufferedReader(new InputStreamReader(new FileReader(f_path).getInputStream()));
            boolean run = true;
            String str;
            while (run) {
                str = in.readLine();
                if (str != null) frag += str + "\n"; else run = false;
            }
            in.close();
        } catch (Exception e) {
            System.err.println("Couldn't read in fragment shader \"" + fspath + "\".");
            throw new ShaderNotLoadedException(vspath, fspath);
        }
        return loadShaderFromSource(vert, frag, textureUnits, separateCam, fog);
    }

    public static String getShaderLog(int obj, boolean shader) {
        int[] length = { 0 };
        int[] chars = { 0 };
        byte[] log;
        if (shader) gl.glGetShaderiv(obj, GL2.GL_INFO_LOG_LENGTH, length, 0); else gl.glGetProgramiv(obj, GL2.GL_INFO_LOG_LENGTH, length, 0);
        if (length[0] > 0) {
            log = new byte[length[0]];
            if (shader) gl.glGetShaderInfoLog(obj, length[0], chars, 0, log, 0); else gl.glGetProgramInfoLog(obj, length[0], chars, 0, log, 0);
            if (log[0] != 0) {
                String ret = "";
                for (int i = 0; i < log.length; i++) {
                    ret += (char) log[i];
                }
                return ret;
            }
        }
        if (shader) return "Shader compilation OK."; else return "Shader linking OK.";
    }

    public static Shader loadShaderFromSource(String vert_source, String frag_source, int textureUnits, boolean separateCam, boolean fog) throws ShaderProgramProcessException {
        String[] vert = { vert_source };
        String[] frag = { frag_source };
        int v = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
        int f = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
        int[] shaderstatus = { 0 };
        gl.glShaderSource(v, 1, vert, null);
        gl.glShaderSource(f, 1, frag, null);
        gl.glCompileShader(v);
        gl.glCompileShader(f);
        gl.glGetShaderiv(v, GL2.GL_COMPILE_STATUS, shaderstatus, 0);
        if (shaderstatus[0] == GL2.GL_FALSE) {
            System.err.println("Vertex shader compilation failed!");
            String log = getShaderLog(v, true);
            if (showShaderCompileOutput) System.err.println(log);
            throw new ShaderNotCompiledException(log);
        }
        gl.glGetShaderiv(f, GL2.GL_COMPILE_STATUS, shaderstatus, 0);
        if (shaderstatus[0] == GL2.GL_FALSE) {
            System.err.println("Fragment shader compilation failed!");
            String log = getShaderLog(f, true);
            if (showShaderCompileOutput) System.err.println(log);
            throw new ShaderNotCompiledException(log);
        }
        int shaderId = gl.glCreateProgram();
        gl.glAttachShader(shaderId, v);
        gl.glAttachShader(shaderId, f);
        gl.glLinkProgram(shaderId);
        gl.glGetProgramiv(shaderId, GL2.GL_LINK_STATUS, shaderstatus, 0);
        if (shaderstatus[0] == GL2.GL_FALSE) {
            System.err.println("Shader linking failed!");
            String log = getShaderLog(shaderId, false);
            if (showShaderCompileOutput) System.err.println(log);
            throw new ShaderNotLinkedException(log);
        }
        gl.glValidateProgram(shaderId);
        gl.glGetProgramiv(shaderId, GL2.GL_VALIDATE_STATUS, shaderstatus, 0);
        if (shaderstatus[0] == GL2.GL_FALSE) {
            System.err.println("Shader validation failed!");
            throw new ShaderNotValidatedException();
        }
        int vm;
        int mm;
        int fd;
        int[] tu = new int[textureUnits];
        if (separateCam) {
            vm = gl.glGetUniformLocation(shaderId, "viewMat");
            mm = gl.glGetUniformLocation(shaderId, "modelMat");
        } else {
            vm = -1;
            mm = -1;
        }
        if (fog) {
            fd = gl.glGetUniformLocation(shaderId, "fogVerticalDisperse");
        } else {
            fd = -1;
        }
        for (int i = 0; i < tu.length; i++) tu[i] = gl.glGetUniformLocation(shaderId, "tex" + i);
        if (showShaderCompileOutput) {
            System.out.println("Vertex compilation:");
            System.out.println(getShaderLog(v, true));
            System.out.println("Fragment compilation:");
            System.out.println(getShaderLog(f, true));
            System.out.println("Shader linking:");
            System.out.println(getShaderLog(shaderId, false));
        }
        Console.print("Created shader: [" + shaderId + "]");
        gl.glDeleteShader(v);
        gl.glDeleteShader(f);
        return new Shader(shaderId, tu, vm, mm, fd);
    }

    public static void setShader(ShaderID id) {
        if (currentShaderBank.containsShader(id)) {
            currentShaderID = id;
            currentShader = currentShaderBank.getShader(id);
            gl.glUseProgram(currentShader.shaderId);
        }
    }

    public static void setShader(Shader s) {
        currentShaderID = null;
        currentShader = s;
        gl.glUseProgram(s.shaderId);
    }

    public static Shader getDefaultShader(String s) {
        if (defaultShaders.containsKey(s)) return defaultShaders.get(s); else return null;
    }

    public static void setDefaultShader(String s) {
        if (defaultShaders.containsKey(s)) {
            currentShaderID = null;
            currentShader = defaultShaders.get(s);
            gl.glUseProgram(currentShader.shaderId);
        }
    }

    public static Shader getCurrentShader() {
        return currentShader;
    }

    public static ShaderID getCurrentShaderID() {
        return currentShaderID;
    }

    public static Shader getShader(ShaderID shader) {
        if (currentShaderBank != null) return currentShaderBank.getShader(shader); else return null;
    }

    public static void removeShader(Shader shader) {
        gl.glDeleteProgram(shader.shaderId);
    }

    public static void deleteShaderBank(ShaderBank bank) {
        if (shaderBanks.contains(bank)) {
            bank.destroy();
            shaderBanks.remove(bank);
            if (currentShaderBank == bank) currentShaderBank = null;
        }
    }

    public static void clearAllShaders() {
        for (int i = 0; i < shaderBanks.size(); i++) shaderBanks.get(i).destroy();
        currentShaderBank = null;
        shaderBanks.clear();
    }

    public static void updateCurrentShaderBank() {
        Light.LightType[] lights = LightManager.getLightTypes();
        boolean fog = FogManager.isFogEnabled();
        currentShaderBank = getShaderBank(lights, fog);
    }

    public static void setCustomViewport(int posX, int posY, int sizeX, int sizeY) {
        gl.glViewport(posX, posY, sizeX, sizeY);
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewportData, 0);
    }

    public static void setDefaultViewport() {
        setCustomViewport(0, 0, width, height);
    }

    public static void setProjection(float fov, float near, float far) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(fov, (float) width / (float) height, near, far);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);
        float[] mat = TransformationMatrix.calculatePerspectiveMatrix(fov, (float) width / (float) height, near, far).getMatrix();
    }

    public static void setProjection(float fov) {
        setProjection(fov, 0.1f, 1000.0f);
    }

    public static void setOrtho(float near, float far) {
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0f, width, height, 0.0f, near, far);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);
    }

    public static void setOrtho() {
        setOrtho(0.1f, 100.0f);
    }

    public static void clearView() {
        gl.glLoadIdentity();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static Rectangle getRectangle() {
        return new Rectangle(appFrame.getX(), appFrame.getY(), appFrame.getWidth(), appFrame.getHeight());
    }

    public static void setAlphaTest(float val) {
        if (val > 0.0f && val < 1.0f) {
            gl.glAlphaFunc(GL2.GL_GREATER, val);
            gl.glEnable(GL2.GL_ALPHA_TEST);
        } else gl.glDisable(GL2.GL_ALPHA_TEST);
    }

    public static void setPointSprites(boolean on) {
        if (on) {
            gl.glTexEnvi(GL2.GL_POINT_SPRITE, GL2.GL_COORD_REPLACE, GL2.GL_TRUE);
            gl.glEnable(GL2.GL_POINT_SPRITE);
        }
    }

    public static Vector3d projectToView(Vector3d v, float[] projMatrix, float[] viewMatrix) {
        float[] ret = new float[3];
        glu.gluProject(v.getX(), v.getY(), v.getZ(), viewMatrix, 0, projMatrix, 0, viewportData, 0, ret, 0);
        return new Vector3d(ret[0], height - ret[1], ret[2]);
    }

    public static Vector3d projectToView(Vector3d v, TransformationMatrix projMatrix, TransformationMatrix viewMatrix) {
        return projectToView(v, projMatrix.getMatrix(), viewMatrix.getMatrix());
    }

    public static Vector3d projectToScreen(Vector3d v) {
        return projectToView(v, projectionMatrix, currentViewMatrix);
    }

    public static void setShaderParamaters() {
        if (currentShader.viewMatrixUniformId != -1) gl.glUniformMatrix4fv(currentShader.viewMatrixUniformId, 1, false, currentViewMatrix, 0);
        if (currentShader.fogVerticalDisperseUniformId != -1) gl.glUniform1f(currentShader.fogVerticalDisperseUniformId, FogManager.getFogVerticalDisperse());
        for (int i = 0; i < currentShader.textureUnitIds.length; i++) gl.glUniform1i(currentShader.textureUnitIds[i], i);
    }

    public static void setViewMatrix(TransformationMatrix matrix) {
        currentViewMatrix = matrix.getMatrix();
    }

    public static void setViewMatrix(float[] matrix) {
        currentViewMatrix = matrix;
    }

    public static void setModelMatrix(float[] matrix) {
        if (currentShader.modelMatrixUniformId != -1) gl.glUniformMatrix4fv(currentShader.modelMatrixUniformId, 1, false, matrix, 0);
    }

    public static void setClearColor(float R, float G, float B) {
        gl.glClearColor(R, G, B, 0.0f);
        appFrame.setBackground(new Color(R, G, B));
    }

    public static Vector2d calculateFullscreenDimensions(Vector2d size) {
        return calculateFullscreenDimensions(size.getX(), size.getY());
    }

    public static Vector2d calculateFullscreenDimensions(float w, float h) {
        Vector2d dim = new Vector2d();
        float ar_img = w / h;
        float ar_scr = Graphics.getWidth() / Graphics.getHeight();
        if (ar_img == ar_scr) {
            dim.setX(Graphics.getWidth());
            dim.setY(Graphics.getHeight());
        } else if (ar_img > 1.0f || (ar_img == 1.0f && ar_scr < 1.0f)) {
            dim.setX(Graphics.getWidth());
            dim.setY((int) ((float) Graphics.getWidth() / ar_img));
        } else if (ar_img < 1.0f || (ar_img == 1.0f && ar_scr > 1.0f)) {
            dim.setX((int) (ar_img * (float) Graphics.getHeight()));
            dim.setY(Graphics.getHeight());
        }
        return dim;
    }
}
