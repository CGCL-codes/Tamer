package env3d;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.light.DirectionalLight;
import com.jme.light.Light;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.BumpMapColorController;
import com.jme.util.TextureManager;
import com.jme.util.export.ByteUtils;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryExporter;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.geom.BufferUtils;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.MaxToJme;
import com.jmex.model.converters.Md2ToJme;
import com.jmex.model.converters.Md3ToJme;
import com.jmex.model.converters.MilkToJme;
import com.jmex.model.converters.ObjToJme;
import com.jmex.model.ogrexml.OgreLoader;
import com.jmex.model.ogrexml.anim.MeshAnimationController;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import util.Sysutil;

/**
 * Write a description of class GameObjectAdapter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GameObjectAdapter {

    protected static final Logger logger = Logger.getLogger(GameObjectAdapter.class.getName());

    protected static HashMap<String, Object> modelsMap = new HashMap<String, Object>();

    protected static HashMap<String, Texture> texturesMap = new HashMap<String, Texture>();

    private static BlendState tlState = null;

    protected Node jme_node = new Node();

    protected DisplaySystem currentDisplay;

    private Spatial jme_object;

    protected Object internalObject;

    private String currentTexture = "", currentModel = "";

    private boolean currentTransparency = false;

    protected Quaternion q, qX, qY, qZ;

    protected String modelFormat = null;

    private Controller controller = null;

    private float secondsPerFrame;

    private HashMap<String, Integer> repeatTypeMap = new HashMap<String, Integer>();

    private int repeatDelay;

    private Light light;

    public GameObjectAdapter() {
        q = new Quaternion();
        qX = new Quaternion();
        qY = new Quaternion();
        qZ = new Quaternion();
        repeatTypeMap.put("wrap", Controller.RT_WRAP);
        repeatTypeMap.put("cycle", Controller.RT_CYCLE);
        repeatTypeMap.put("clamp", Controller.RT_CLAMP);
        repeatDelay = 0;
    }

    public GameObjectAdapter(Object obj, DisplaySystem display, Light light) {
        this();
        currentDisplay = display;
        internalObject = obj;
        this.light = light;
        update();
    }

    public void update() {
        if (internalObject instanceof EnvLine) {
            Line l = ((EnvLine) internalObject).getLine();
            jme_object = l;
            jme_node.attachChild(jme_object);
            jme_node.setLightCombineMode(Spatial.LightCombineMode.Off);
            jme_node.updateRenderState();
            return;
        }
        float x = castToFloat(getFieldFromObject("x"));
        float y = castToFloat(getFieldFromObject("y"));
        float z = castToFloat(getFieldFromObject("z"));
        float rotateX = castToFloat(getFieldFromObject("rotateX"));
        float rotateY = castToFloat(getFieldFromObject("rotateY"));
        float rotateZ = castToFloat(getFieldFromObject("rotateZ"));
        float scale = castToFloat(getFieldFromObject("scale"));
        int startFrame, endFrame;
        try {
            startFrame = (Integer) getFieldFromObject("startFrame");
            endFrame = (Integer) getFieldFromObject("endFrame");
        } catch (ClassCastException e) {
            startFrame = 0;
            endFrame = 0;
        }
        String action = getFieldFromObject("action");
        String repeatType = getFieldFromObject("repeat");
        Boolean transparent = getFieldFromObject("transparent");
        qY.fromAngleNormalAxis((float) Math.toRadians(rotateY), Vector3f.UNIT_Y);
        qX.fromAngleNormalAxis((float) Math.toRadians(rotateX), Vector3f.UNIT_X);
        qZ.fromAngleNormalAxis((float) Math.toRadians(rotateZ), Vector3f.UNIT_Z);
        String modelFile = getFieldFromObject("model");
        if (modelFile == null) {
            if (jme_node != null && jme_node.getChildren() != null && jme_node.getChildren().size() > 0) {
                jme_node.detachAllChildren();
            }
        } else if (!currentModel.equals(modelFile)) {
            currentModel = modelFile;
            try {
                Node model;
                if (currentModel.equals("sphere")) {
                    Node n = new Node();
                    Sphere s = new Sphere(internalObject.toString(), new Vector3f(0, 0, 0), 16, 16, 1, true);
                    n.attachChild(s);
                    s.getLocalRotation().fromAngles(0, (float) Math.PI / 2, 0).multLocal((float) Math.sqrt(0.5), 0, 0, (float) Math.sqrt(0.5) * -1);
                    s.setTextureMode(Sphere.TextureMode.Projected);
                    model = n;
                } else if (currentModel.startsWith("box")) {
                    Node n = new Node();
                    String[] params = currentModel.split(" ");
                    int width = 1, height = 1;
                    if (params.length > 3) {
                        width = Integer.parseInt(params[1]);
                        height = Integer.parseInt(params[2]);
                    }
                    TriMesh b = drawSquare(width, height);
                    if (params[0].equals("box")) {
                        b.getLocalRotation().fromAngleNormalAxis((float) (-1 * Math.PI / 2), Vector3f.UNIT_X);
                        b.setLocalTranslation(0, 0, height);
                    } else if (params[0].equals("box_north")) {
                    }
                    n.attachChild(b);
                    model = n;
                } else {
                    Object s = null;
                    s = loadModel(modelFile);
                    if (s instanceof TriMesh) {
                        TriMesh tm = (TriMesh) s;
                        model = new Node();
                        model.attachChild(tm);
                        tm.setName(internalObject.toString());
                    } else {
                        model = ((Node) BinaryImporter.getInstance().load((byte[]) s));
                        model.getChild(0).setName(internalObject.toString());
                    }
                }
                jme_node.detachAllChildren();
                jme_object = model;
                jme_node.attachChild(jme_object);
                jme_node.updateRenderState();
                jme_node.setModelBound(new BoundingBox());
                jme_node.updateModelBound();
                if (modelFormat != null) {
                    if (model.getControllerCount() > 0) {
                        controller = model.getController(0);
                    } else {
                        List<Spatial> decendents = model.descendantMatches(".*");
                        for (Spatial d : decendents) {
                            if (d.getControllerCount() > 0) {
                                controller = d.getController(0);
                            }
                        }
                    }
                    if (getController() != null) {
                        logger.log(Level.INFO, "Loading model controller: {0} {1} {2}", new Object[] { getController().getMinTime(), getController().getMaxTime(), getController().getSpeed() });
                        if (getController().getMinTime() <= 0) {
                            secondsPerFrame = 1;
                        } else {
                            secondsPerFrame = getController().getMinTime();
                        }
                    }
                }
                currentTexture = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (jme_object != null) {
            String textureFile = getFieldFromObject("texture");
            if (currentTexture != null && textureFile == null) {
                currentTexture = null;
                jme_object.setRenderState(null);
                jme_node.updateRenderState();
            } else if (currentTexture != null && !currentTexture.equals(textureFile)) {
                currentTexture = textureFile;
                String normalTexture = getFieldFromObject("textureNormal");
                if (normalTexture != null) {
                    TriMesh thisModel = (TriMesh) ((Node) jme_object).getChildren().get(0);
                    BumpMapColorController c = new BumpMapColorController(thisModel);
                    thisModel.addController(c);
                    MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
                    ms.setColorMaterial(MaterialState.ColorMaterial.AmbientAndDiffuse);
                    thisModel.setRenderState(ms);
                    thisModel.updateRenderState();
                    TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
                    ts.setEnabled(true);
                    Texture tex = TextureManager.loadTexture(Sysutil.getURL(normalTexture), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
                    tex.setWrap(Texture.WrapMode.Repeat);
                    tex.setApply(Texture.ApplyMode.Combine);
                    tex.setCombineFuncRGB(Texture.CombinerFunctionRGB.Dot3RGB);
                    tex.setCombineSrc0RGB(Texture.CombinerSource.CurrentTexture);
                    tex.setCombineSrc1RGB(Texture.CombinerSource.PrimaryColor);
                    ts.setTexture(tex, 0);
                    Texture tex2 = TextureManager.loadTexture(Sysutil.getURL(currentTexture), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
                    tex2.setApply(Texture.ApplyMode.Combine);
                    tex2.setWrap(Texture.WrapMode.Repeat);
                    tex2.setCombineFuncRGB(Texture.CombinerFunctionRGB.Modulate);
                    tex2.setCombineSrc0RGB(Texture.CombinerSource.Previous);
                    tex2.setCombineSrc1RGB(Texture.CombinerSource.CurrentTexture);
                    ts.setTexture(tex2, 1);
                    thisModel.setRenderState(ts);
                    LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
                    lightState.setGlobalAmbient(new ColorRGBA(0.75f, 0.75f, 0.75f, 1));
                    lightState.attach(light);
                    thisModel.setLightCombineMode(Spatial.LightCombineMode.Replace);
                    thisModel.setRenderState(lightState);
                    thisModel.updateRenderState();
                    jme_object.updateRenderState();
                } else {
                    TextureState ts = currentDisplay.getRenderer().createTextureState();
                    ts.setEnabled(true);
                    if (textureFile.startsWith("text:")) {
                        ts.setTexture(loadText(textureFile));
                    } else {
                        ts.setTexture(loadTexture(textureFile));
                    }
                    jme_object.setRenderState(ts);
                    MaterialState ms = currentDisplay.getRenderer().createMaterialState();
                    ms.setEmissive(new ColorRGBA(0.3f, 0.3f, 0.3f, 1));
                    jme_object.setRenderState(ms);
                }
                jme_node.updateRenderState();
            }
            jme_object.setLocalTranslation(x, y, z);
            jme_object.setLocalScale(scale);
            jme_object.setLocalRotation(qZ.mult(qY.mult(qX, q), q));
            if (transparent != currentTransparency) {
                currentTransparency = transparent;
                if (tlState == null) {
                    tlState = currentDisplay.getRenderer().createBlendState();
                    tlState.setBlendEnabled(true);
                    tlState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
                    tlState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
                    tlState.setEnabled(true);
                    tlState.setTestEnabled(true);
                    tlState.setTestFunction(BlendState.TestFunction.GreaterThan);
                }
                if (currentTransparency) {
                    jme_object.setRenderState(tlState);
                    jme_object.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
                } else {
                    jme_object.clearRenderState(StateType.Blend);
                    jme_object.setRenderQueueMode(Renderer.QUEUE_INHERIT);
                }
                jme_object.updateRenderState();
            }
            if (getController() != null) {
                if (repeatType != null) {
                    if (repeatType.equalsIgnoreCase("none") && getController().isActive()) {
                        getController().setActive(false);
                    } else if (!repeatType.equalsIgnoreCase("none") && getController().getRepeatType() != repeatTypeMap.get(repeatType)) {
                        if (repeatDelay > 1) {
                            getController().setRepeatType(repeatTypeMap.get(repeatType));
                            if (!getController().isActive()) {
                                getController().setActive(true);
                            }
                        }
                        repeatDelay++;
                    } else {
                        repeatDelay = 0;
                    }
                }
                if (getController() instanceof MeshAnimationController && action != null) {
                    MeshAnimationController meshController = ((MeshAnimationController) getController());
                    if (!meshController.getActiveAnimation().equalsIgnoreCase(action)) {
                        meshController.setAnimation(action);
                    }
                } else {
                    if (startFrame < endFrame) {
                        getController().setMinTime(startFrame * secondsPerFrame);
                        getController().setMaxTime(endFrame * secondsPerFrame);
                    }
                }
            }
        }
    }

    protected <T> T getFieldFromObject(String fname) {
        T fieldValue = null;
        Class c = internalObject.getClass();
        Field field = null;
        while (field == null && c != null) {
            try {
                field = c.getDeclaredField(fname);
            } catch (NoSuchFieldException e) {
                field = null;
                c = c.getSuperclass();
            }
        }
        if (field != null) {
            try {
                field.setAccessible(true);
                fieldValue = (T) field.get(internalObject);
            } catch (Exception e) {
                System.out.println("Error " + e);
                System.exit(1);
            }
        } else {
            if (fname.equalsIgnoreCase("texture")) {
                fieldValue = (T) Default.getInstance().getObjectTexture();
            } else if (fname.equalsIgnoreCase("model")) {
                fieldValue = (T) "sphere";
            } else if (fname.equalsIgnoreCase("textureNormal")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("action")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("repeat")) {
                fieldValue = null;
            } else if (fname.equalsIgnoreCase("scale")) {
                fieldValue = (T) new Float(1);
            } else if (fname.equalsIgnoreCase("transparent")) {
                fieldValue = (T) (Boolean) false;
            } else {
                fieldValue = (T) new Float(0);
            }
        }
        return fieldValue;
    }

    private float castToFloat(Object obj) {
        float val;
        try {
            val = (Float) obj;
        } catch (java.lang.ClassCastException e) {
            val = ((Double) obj).floatValue();
        }
        return val;
    }

    /**
     * @return the jme_object
     */
    public Node getJme_object() {
        return jme_node;
    }

    public Texture loadTexture(String textureFile) {
        Texture t = texturesMap.get(textureFile);
        if (t == null) {
            java.net.URL imageURL;
            if (textureFile.startsWith("http")) {
                try {
                    imageURL = new java.net.URL(textureFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    imageURL = null;
                }
            } else {
                java.lang.ClassLoader cldr = GameObjectAdapter.class.getClassLoader();
                imageURL = cldr.getResource(textureFile);
                if (imageURL == null) {
                    try {
                        imageURL = (new File(textureFile)).toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        imageURL = null;
                    }
                }
            }
            t = TextureManager.loadTexture(imageURL, Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);
            t.setWrap(Texture.WrapMode.Repeat);
            texturesMap.put(textureFile, t);
        }
        return t;
    }

    /**
     * Load a picece of text as texture
     * @param textureName
     * @return
     */
    public Texture loadText(String textureName) {
        Texture t = texturesMap.get(textureName);
        if (t == null) {
            int size = 128;
            BufferedImage tex = new BufferedImage(size, size, BufferedImage.TYPE_3BYTE_BGR);
            JTextPane pane = new JTextPane();
            pane.setSize(size, size);
            pane.setBackground(Color.gray);
            pane.setContentType("text/html");
            pane.setText(textureName.substring(5));
            Graphics2D g = (Graphics2D) tex.getGraphics();
            SwingUtilities.paintComponent(g, pane, new Container(), 0, 0, size, size);
            g.dispose();
            t = TextureManager.loadTexture(tex, Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear, true);
            texturesMap.put(textureName, t);
        }
        return t;
    }

    public Object loadModel(String modelFile) {
        Object m = modelsMap.get(this.toString() + modelFile);
        if (m == null) {
            logger.log(Level.INFO, "Reading model in memory {0}", modelFile);
            m = loadModelHelper(modelFile);
            if (m instanceof TriMesh) {
                m = new SharedMesh((TriMesh) m);
            }
            modelsMap.put(this.toString() + modelFile, m);
        }
        return m;
    }

    public Object loadModelHelper(String modelFile) {
        Object m = modelsMap.get(modelFile);
        modelFormat = modelFile.substring(modelFile.lastIndexOf(".") + 1, modelFile.length());
        if (m == null) {
            java.net.URL modelURL;
            if (modelFile.startsWith("http")) {
                try {
                    modelURL = new java.net.URL(modelFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    modelURL = null;
                }
            } else {
                java.lang.ClassLoader cldr = GameObjectAdapter.class.getClassLoader();
                modelURL = cldr.getResource(modelFile);
                if (modelURL == null) {
                    try {
                        modelURL = (new File(modelFile)).toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        modelURL = null;
                    }
                }
            }
            FormatConverter converter = null;
            if (modelFormat.equalsIgnoreCase("obj")) {
                converter = new ObjToJme();
            } else if (modelFormat.equalsIgnoreCase("3ds")) {
                converter = new MaxToJme();
            } else if (modelFormat.equalsIgnoreCase("ms3d")) {
                converter = new MilkToJme();
            } else if (modelFormat.equalsIgnoreCase("md2")) {
                converter = new Md2ToJme();
            } else if (modelFormat.equalsIgnoreCase("md3")) {
                converter = new Md3ToJme();
            } else if (modelFormat.equalsIgnoreCase("jme")) {
                converter = null;
            } else if (modelFormat.equalsIgnoreCase("xml")) {
                converter = null;
            }
            try {
                byte[] content = null;
                if (converter != null) {
                    converter.setProperty("mtllib", modelURL);
                    converter.setProperty("texdir", modelURL);
                    converter.setProperty("texurl", modelURL);
                    ByteArrayOutputStream BO = new ByteArrayOutputStream();
                    converter.convert(modelURL.openStream(), BO);
                    content = BO.toByteArray();
                } else {
                    if (modelFormat.equalsIgnoreCase("xml")) {
                        ByteArrayOutputStream BO = new ByteArrayOutputStream();
                        BinaryExporter.getInstance().save((new OgreLoader()).loadModel(modelURL, null), BO);
                        content = BO.toByteArray();
                    } else {
                        content = ByteUtils.getByteContent(modelURL.openStream());
                    }
                }
                Savable mod = BinaryImporter.getInstance().load(content);
                if (mod instanceof TriMesh) {
                    modelsMap.put(modelFile, mod);
                    ((TriMesh) mod).copyTextureCoordinates(0, 1, 1);
                    m = mod;
                } else {
                    modelsMap.put(modelFile, content);
                    m = content;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return m;
    }

    public static TriMesh drawSquare() {
        return drawSquare(1, 1);
    }

    /**
     * Draw a simple square.  Used in room and lots of other things ;)
     * @return
     */
    public static TriMesh drawSquare(int width, int height) {
        TriMesh m = new TriMesh();
        Vector3f[] vertexes = { new Vector3f(0, 0, 0), new Vector3f(width, 0, 0), new Vector3f(0, height, 0), new Vector3f(width, height, 0) };
        Vector3f[] normals = { new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 1) };
        ColorRGBA[] colors = { new ColorRGBA(1, 1, 1, 1), new ColorRGBA(1, 1, 1, 1), new ColorRGBA(1, 1, 1, 1), new ColorRGBA(1, 1, 1, 1) };
        Vector2f[] texCoords = { new Vector2f(0, 0), new Vector2f(width, 0), new Vector2f(0, height), new Vector2f(width, height) };
        int[] indexes = { 0, 1, 2, 3, 2, 1 };
        m.reconstruct(BufferUtils.createFloatBuffer(vertexes), BufferUtils.createFloatBuffer(normals), BufferUtils.createFloatBuffer(colors), TexCoords.makeNew(texCoords), BufferUtils.createIntBuffer(indexes));
        m.setModelBound(new BoundingBox());
        m.updateModelBound();
        return m;
    }

    public static void clearResources() {
        texturesMap = new HashMap<String, Texture>();
        modelsMap = new HashMap<String, Object>();
    }

    /**
     * @param currentDisplay the currentDisplay to set
     */
    public void setCurrentDisplay(DisplaySystem currentDisplay) {
        this.currentDisplay = currentDisplay;
    }

    /**
     * @return the controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @return the light
     */
    public Light getLight() {
        return light;
    }

    /**
     * @param light the light to set
     */
    public void setLight(Light light) {
        this.light = light;
    }
}
