package javapoptools;

import com.novusradix.JavaPop.Client.GLHelper;
import com.novusradix.JavaPop.Client.GLHelper.GLHelperException;
import com.novusradix.JavaPop.Client.Model;
import com.novusradix.JavaPop.Client.ModelData;
import com.novusradix.JavaPop.Math.Matrix4;
import com.novusradix.JavaPop.Math.Vector3;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import static javax.media.opengl.GL.*;

/**
 *
 * @author gef
 */
public class PreviewPanel extends GLCanvas implements GLEventListener {

    private static final float fHeightScale = 0.4082f;

    private Model model;

    private ModelData data;

    private URL textureURL;

    private Vector3 modelPosition;

    private Matrix4 modelBasis;

    private long startTime;

    private float zoom = 1;

    private MainWindow mw;

    private String vertexShaderSource, fragmentShaderSource;

    private int vertexShader, fragmentShader, shaderProgram;

    private boolean initShaders = true;

    private boolean vertexShaderEnabled, fragmentShaderEnabled;

    private Pipeline pipe = Pipeline.CUSTOM;

    public enum Pipeline {

        FIXED, DEFAULT, CUSTOM
    }

    ;

    public PreviewPanel(GLCapabilities caps, MainWindow mainWindow) {
        super(caps);
        addGLEventListener(this);
        startTime = System.nanoTime();
        mw = mainWindow;
    }

    void setPipeline(Pipeline p) {
        pipe = p;
    }

    private void setColor() {
        float r, g, b, a;
        r = mw.getRed();
        g = mw.getGreen();
        b = mw.getBlue();
        a = mw.getAlpha();
        model.setColor(r, g, b, a);
    }

    public void setData(ModelData d) {
        data = d;
        model = new Model(data, textureURL);
        modelPosition = new Vector3();
        modelBasis = new Matrix4(Matrix4.identity);
    }

    public ModelData getModelData() {
        return data;
    }

    public void setTexture(URL u) {
        textureURL = u;
        if (model != null) {
            model.setTextureURL(u);
        }
    }

    public void init(GLAutoDrawable glAD) {
        final GL gl = glAD.getGL();
        GLHelper.glHelper.init(gl);
        gl.setSwapInterval(1);
        gl.glEnable(GL.GL_LIGHTING);
        float global_ambient[] = { 0.1f, 0.1f, 0.1f, 1.0f };
        gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(global_ambient));
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, FloatBuffer.wrap(new float[] { 0.8f, 0.8f, 0.8f, 1.0f }));
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        vertexShader = gl.glCreateShader(GL.GL_VERTEX_SHADER);
        fragmentShader = gl.glCreateShader(GL.GL_FRAGMENT_SHADER);
        shaderProgram = gl.glCreateProgram();
        gl.glAttachShader(shaderProgram, vertexShader);
        gl.glAttachShader(shaderProgram, fragmentShader);
        vertexShaderEnabled = true;
        fragmentShaderEnabled = true;
    }

    public void display(GLAutoDrawable glAD) {
        final GL gl = glAD.getGL();
        float time = (System.nanoTime() - startTime) / 1e9f;
        if (!mw.getVertexShader().equals(vertexShaderSource)) {
            vertexShaderSource = mw.getVertexShader();
            initShaders = true;
        }
        if (!mw.getFragmentShader().equals(fragmentShaderSource)) {
            fragmentShaderSource = mw.getFragmentShader();
            initShaders = true;
        }
        if (mw.isVertexShaderEnabled() != vertexShaderEnabled || mw.isFragmentShaderEnabled() != fragmentShaderEnabled) {
            initShaders = true;
        }
        if (initShaders) {
            initializeShaders(gl);
        }
        setColor();
        gl.glClearColor(0, 0, 0.8f, 0);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, -50);
        gl.glRotatef(-60.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
        gl.glScalef(1.0f, 1.0f, fHeightScale);
        gl.glScalef(zoom, zoom, zoom);
        gl.glRotatef(time * 20.f, 0, 0, 1);
        if (model != null) {
            switch(pipe) {
                case CUSTOM:
                    model.setShader(shaderProgram);
                    break;
                case DEFAULT:
                    model.setDefaultShader();
                    break;
                case FIXED:
                    model.setShader(0);
                    break;
            }
            model.prepare(gl);
            model.display(modelPosition, modelBasis, gl);
        }
        gl.glPopMatrix();
        try {
            GLHelper.glHelper.checkGL(gl);
        } catch (GLHelperException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reshape(final GLAutoDrawable glDrawable, final int x, final int y, final int w, int h) {
        final GL gl = glDrawable.getGL();
        if (h <= 0) {
            h = 1;
        }
        gl.glViewport(0, 0, w, h);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-w / 64.0f, w / 64.0f, -h / 64.0f, h / 64.0f, 1, 100);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void setZoom(float value) {
        zoom = value;
    }

    private void initializeShaders(final GL gl) {
        int[] is = new int[1];
        byte[] chars;
        initShaders = false;
        gl.glUseProgram(0);
        detachAllShaders(gl);
        vertexShaderEnabled = false;
        fragmentShaderEnabled = false;
        mw.setStatus("");
        if (mw.isVertexShaderEnabled()) {
            mw.appendStatus("Compile Vertex Shader:\n");
            mw.appendStatus(GLHelper.glHelper.CompileShader(gl, vertexShader, new String[] { vertexShaderSource }));
            gl.glAttachShader(shaderProgram, vertexShader);
            vertexShaderEnabled = true;
        }
        if (mw.isFragmentShaderEnabled()) {
            mw.appendStatus("Compile Fragment Shader:\n");
            mw.appendStatus(GLHelper.glHelper.CompileShader(gl, fragmentShader, new String[] { fragmentShaderSource }));
            gl.glAttachShader(shaderProgram, fragmentShader);
            fragmentShaderEnabled = true;
        }
        mw.appendStatus("Link:\n");
        gl.glLinkProgram(shaderProgram);
        gl.glGetProgramiv(shaderProgram, GL_INFO_LOG_LENGTH, is, 0);
        if (is[0] > 0) {
            chars = new byte[is[0]];
            gl.glGetProgramInfoLog(shaderProgram, is[0], is, 0, chars, 0);
            String s = new String(chars);
            mw.appendStatus(s);
        } else {
            mw.appendStatus("OK\n");
        }
        mw.appendStatus("Validate:\n");
        gl.glValidateProgram(shaderProgram);
        gl.glGetProgramiv(shaderProgram, GL_INFO_LOG_LENGTH, is, 0);
        if (is[0] > 0) {
            chars = new byte[is[0]];
            gl.glGetProgramInfoLog(shaderProgram, is[0], is, 0, chars, 0);
            String s = new String(chars);
            mw.appendStatus(s);
        } else {
            mw.appendStatus("OK");
        }
        try {
            GLHelper.glHelper.checkGL(gl);
        } catch (GLHelperException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void detachAllShaders(GL gl) {
        int is[] = new int[1];
        int shaders[] = new int[2];
        gl.glGetAttachedShaders(shaderProgram, 2, is, 0, shaders, 0);
        for (int i = 0; i < is[0]; i++) {
            gl.glDetachShader(shaderProgram, shaders[i]);
        }
        try {
            GLHelper.glHelper.checkGL(gl);
        } catch (GLHelperException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
