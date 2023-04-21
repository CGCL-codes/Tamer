package tracer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import tracer.basicdatatypes.Color;
import tracer.basicdatatypes.Vector;
import tracer.camera.Camera;
import tracer.camera.ICamera;
import tracer.imaging.Bitmap;
import tracer.imaging.PPMBitmapReaderWriter;
import tracer.scene.ISceneManager;
import tracer.scene.SceneManager;
import tracer.scene.light.ILightSource;
import tracer.scene.light.PointLightSource;
import tracer.scene.primitives.Primitive;
import tracer.scene.primitives.Sphere;
import tracer.scene.shader.ColorShader;
import tracer.scene.shader.IShader;
import tracer.scene.xml.XMLSceneLoader;
import junit.framework.TestCase;

/**
 * @author martin
 *
 */
public class IntegrationTests extends TestCase {

    public IntegrationTests(final String name) {
        super(name);
    }

    protected final void setUp() throws Exception {
        super.setUp();
    }

    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Testing SceneLoader with concrete objects and start rendering process
	 */
    public final void testSceneLoaderIntegration() {
        ISceneManager sceneManager = new SceneManager(new Camera());
        ICamera camera = sceneManager.getCamera();
        camera.setImageheight(1.0f);
        camera.setImageWidth(1.0f);
        camera.setLocation(new Vector(0.0f, 0.0f, -2.0f));
        camera.setLookat(new Vector(0.0f, 0.0f, 1.0f));
        camera.setUpVector(new Vector(0.0f, 1.0f, 0.0f));
        camera.setPixelHeight(500);
        camera.setPixelWidth(500);
        camera.setZoomValue(1);
        IShader basicColorShader = new ColorShader(new Color(1.0f, 0.0f, 0.5f), 1.0f, 0.0f);
        Primitive sphere = new Sphere(basicColorShader, new Vector(0.0f, 0.0f, 3.0f), null, null, 2.0f);
        sceneManager.addPrimitive(sphere);
        ILightSource cameraLightSource = new PointLightSource(new Vector(0.0f, 0.0f, -2.0f));
        sceneManager.addLight(cameraLightSource);
        Bitmap result = sceneManager.renderScene(1);
        try {
            (new PPMBitmapReaderWriter()).writeBitmap(new FileOutputStream("data/tests/test.ppm"), result, PPMBitmapReaderWriter.PPM6_FILEFORMAT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue("", true);
    }

    public final void testShadowing() {
        try {
            for (int i = 1; i < 6; i++) {
                assertTrue("Shadowing failed at " + i + "! ", this.raytraceAgainstExistingImage(new FileInputStream("data/tests/shadowing/shadowtest" + i + ".xml"), new FileInputStream("data/tests/shadowing/shadow" + i + ".ppm"), i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public final void testReflectionDepth() {
        try {
            for (int i = 0; i < 6; i++) {
                assertTrue("Mirroring failed at " + i + "! ", this.raytraceAgainstExistingImage(new FileInputStream("data/tests/mirroring/mirrortest.xml"), new FileInputStream("data/tests/mirroring/mirror" + i + ".ppm"), i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final boolean raytraceAgainstExistingImage(final FileInputStream sceneXML, final FileInputStream existingPPM, int reflectionDepth) {
        ISceneManager sceneManager = new SceneManager(new Camera());
        (new XMLSceneLoader()).load(sceneXML, "data/example_files/", sceneManager);
        ICamera camera = sceneManager.getCamera();
        if (camera == null) {
            camera = new Camera();
        }
        camera.setImageheight(1);
        camera.setImageWidth(1);
        camera.setZoomValue(1);
        camera.setPixelHeight(100);
        camera.setPixelWidth(100);
        sceneManager.setReflection(true);
        sceneManager.setReflectionDepth(reflectionDepth);
        Bitmap result = sceneManager.renderScene(2);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DigestOutputStream digestOutputStream = null;
        try {
            digestOutputStream = new DigestOutputStream(new FileOutputStream("data/tests/tmp.ppm"), messageDigest);
            (new PPMBitmapReaderWriter()).writeBitmap(digestOutputStream, result, PPMBitmapReaderWriter.PPM6_FILEFORMAT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        MessageDigest mdCompare = null;
        try {
            mdCompare = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DigestInputStream digestInputStream = new DigestInputStream(existingPPM, mdCompare);
        try {
            int i;
            while ((i = digestInputStream.read()) != -1) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (MessageDigest.isEqual(messageDigest.digest(), mdCompare.digest())) {
            return true;
        }
        return false;
    }
}
