package jmetest.TutorialGuide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingSphere;
import com.jme.curve.BezierCurve;
import com.jme.curve.CurveController;
import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.KeyExitAction;
import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.lod.AreaClodMesh;
import com.jme.scene.state.RenderState;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

/**
 * Started Date: Aug 16, 2004<br><br>
 *
 * This program teaches Complex Level of Detail mesh objects.  To use this program, move
 * the camera backwards and watch the model disappear.
 * 
 * @author Jack Lindamood
 */
public class HelloLOD extends SimpleGame {

    private static final Logger logger = Logger.getLogger(HelloLOD.class.getName());

    CameraNode cn;

    public static void main(String[] args) {
        HelloLOD app = new HelloLOD();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleInitGame() {
        URL model = HelloModelLoading.class.getClassLoader().getResource("jmetest/data/model/maggie.obj");
        FormatConverter converter = new ObjToJme();
        converter.setProperty("mtllib", model);
        ByteArrayOutputStream BO = new ByteArrayOutputStream();
        Spatial maggie = null;
        try {
            converter.convert(model.openStream(), BO);
            maggie = (Spatial) BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
        } catch (IOException e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "simpleInitGame()", "Exception", e);
            System.exit(0);
        }
        Node clodNode = getClodNodeFromParent((Node) maggie);
        clodNode.setLocalScale(.1f);
        rootNode.attachChild(clodNode);
        maggie.setLocalScale(.1f);
        maggie.setLocalTranslation(new Vector3f(-15, 0, 0));
        rootNode.attachChild(maggie);
        input = new InputHandler();
        input.addAction(new KeyExitAction(this), "exit", KeyInput.KEY_ESCAPE, false);
        Vector3f[] cameraPoints = new Vector3f[] { new Vector3f(0, 5, 20), new Vector3f(0, 20, 90), new Vector3f(0, 30, 200), new Vector3f(0, 100, 300), new Vector3f(0, 150, 400) };
        BezierCurve bc = new BezierCurve("camera path", cameraPoints);
        cn = new CameraNode("camera node", cam);
        CurveController cc = new CurveController(bc, cn);
        cc.setRepeatType(Controller.RT_CYCLE);
        cc.setSpeed(.25f);
        cn.addController(cc);
        rootNode.attachChild(cn);
    }

    private Node getClodNodeFromParent(Node meshParent) {
        Node clodNode = new Node("Clod node");
        for (int i = 0; i < meshParent.getQuantity(); i++) {
            final Spatial child = meshParent.getChild(i);
            if (child instanceof Node) {
                clodNode.attachChild(getClodNodeFromParent((Node) child));
            } else if (child instanceof TriMesh) {
                AreaClodMesh acm = new AreaClodMesh("part" + i, (TriMesh) child, null);
                acm.setModelBound(new BoundingSphere());
                acm.updateModelBound();
                acm.setTrisPerPixel(.5f);
                acm.setDistanceTolerance(2);
                acm.setRenderState(child.getRenderState(RenderState.StateType.Material));
                clodNode.attachChild(acm);
            } else {
                logger.warning("Unhandled Spatial type: " + child.getClass());
            }
        }
        return clodNode;
    }

    Vector3f up = new Vector3f(0, 1, 0);

    Vector3f left = new Vector3f(1, 0, 0);

    private static Vector3f tempVa = new Vector3f();

    private static Vector3f tempVb = new Vector3f();

    private static Vector3f tempVc = new Vector3f();

    private static Vector3f tempVd = new Vector3f();

    private static Matrix3f tempMa = new Matrix3f();

    protected void simpleUpdate() {
        Vector3f objectCenter = rootNode.getWorldBound().getCenter(tempVa);
        Vector3f lookAtObject = tempVb.set(objectCenter).subtractLocal(cam.getLocation()).normalizeLocal();
        tempMa.setColumn(0, up.cross(lookAtObject, tempVc).normalizeLocal());
        tempMa.setColumn(1, left.cross(lookAtObject, tempVd).normalizeLocal());
        tempMa.setColumn(2, lookAtObject);
        cn.setLocalRotation(tempMa);
    }
}
