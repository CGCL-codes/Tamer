package game.visualizations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.util.LinkedList;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Light;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import game.gui.ProgressBarWindow;
import game.data.Instance;
import game.data.InputFactor;
import game.data.GlobalData;

/**
 * This class encapsulates Canvas3D and 3d model.
 *
 */
class KopsaClassification3Dvisual {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(KopsaClassification3Dvisual.class);

    private KopsaClassification3D classification3D;

    private Canvas3D canvas;

    private SimpleUniverse universe;

    private BranchGroup modelRoot;

    private BranchGroup spotsRoot;

    private BranchGroup rootGroup;

    /**
     * It constructs Canvas3D and 3D model in background.
     * It may throw an exception if an error occurs or the work is cancelled by the user.
     * In that case, a message error should be shown by Classification3D.
     *
     * @param classification3D
     * @throws Exception
     */
    public KopsaClassification3Dvisual(KopsaClassification3D classification3D) throws Exception {
        System.gc();
        this.classification3D = classification3D;
        ProgressBarWindow.runLongTask(new Runnable() {

            public void run() {
                try {
                    runInitialTask();
                    ProgressBarWindow.done();
                } catch (Exception e) {
                    ProgressBarWindow.done(e);
                }
            }
        });
    }

    /**
     * This method is supposed to be run by a worker thread.
     * It creates the Canvas3D, scene and model.
     * @throws Exception
     */
    private void runInitialTask() throws Exception {
        createCanvas3D();
        ProgressBarWindow.getProgressBarWindow().update("Creating lights", 20);
        rootGroup = new BranchGroup();
        rootGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        rootGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        rootGroup.addChild(createLight());
        rootGroup.addChild(createLight2());
        rootGroup.addChild(createLight3());
        rootGroup.addChild(createLight4());
        rootGroup.addChild(createAmbientLight());
        rootGroup.addChild(createAxis());
        universe.getLocale().addBranchGraph(rootGroup);
        modelRoot = createModel();
        ProgressBarWindow.getProgressBarWindow().update("Adding model to the scene", 100);
        rootGroup.addChild(modelRoot);
        spotsRoot = createSpots();
        rootGroup.addChild(spotsRoot);
    }

    /**
     * This method is called when some parameters are changed.
     *
     * It runs the worker thread recreating the 3d model in background.
     * @throws Exception
     */
    public void recreate() throws Exception {
        System.gc();
        ProgressBarWindow.runLongTask(new Runnable() {

            public void run() {
                try {
                    runRecreateTask();
                    ProgressBarWindow.done();
                } catch (Exception e) {
                    ProgressBarWindow.done(e);
                }
            }
        });
    }

    /**
     * This method is supposed to be run by the worker thread in background.
     *
     * It basicly creates a new 3d model.
     * @throws Exception
     */
    private void runRecreateTask() throws Exception {
        ProgressBarWindow.getProgressBarWindow().update("Destroying current model", 0);
        canvas.stopRenderer();
        modelRoot.detach();
        modelRoot = createModel();
        modelRoot.addChild(createSpots());
        ProgressBarWindow.getProgressBarWindow().update("Adding model to the scene", 100);
        rootGroup.addChild(modelRoot);
        spotsRoot.detach();
        spotsRoot = createSpots();
        rootGroup.addChild(spotsRoot);
        canvas.startRenderer();
    }

    /**
     * This method recreates spots representing data.
     */
    public void recreateSpots() {
        canvas.stopRenderer();
        spotsRoot.detach();
        spotsRoot = createSpots();
        rootGroup.addChild(spotsRoot);
        canvas.startRenderer();
    }

    /**
     * Creates the 3D canvas and sets up some other stuff like universe and viewing platform.
     * @throws Exception
     */
    private void createCanvas3D() throws Exception {
        ProgressBarWindow.getProgressBarWindow().update("Creating Canvas3D", 0);
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config, false) {

            public void postSwap() {
                super.postSwap();
                doPostSwap();
            }
        };
        canvas.setFocusable(true);
        canvas.requestFocus();
        ProgressBarWindow.getProgressBarWindow().update("Creating 3D universe", 20);
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100));
        ViewingPlatform vp = universe.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }

    /**
     * Creates the 3D model visualising the response and returns it as a BranchGroup instance.
     *
     * It uses visualConfiguration's appearances for each response level.
     *
     * @return
     * @throws Exception
     */
    private BranchGroup createModel() throws Exception {
        ProgressBarWindow.getProgressBarWindow().update("Creating Model", 30);
        BranchGroup modelRoot = new BranchGroup();
        modelRoot.setCapability(BranchGroup.ALLOW_DETACH);
        LinkedList[] lists = new LinkedList[KopsaClassification3DvisualConfiguration.CLASSMEMBERSHIP_LEVELS];
        LinkedList[] listsBorders = new LinkedList[KopsaClassification3DvisualConfiguration.CLASSMEMBERSHIP_LEVELS];
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new LinkedList();
            listsBorders[i] = new LinkedList();
        }
        int res_x = KopsaClassification3DvisualConfiguration.getVisualConfiguration().getRes_x();
        int res_y = KopsaClassification3DvisualConfiguration.getVisualConfiguration().getRes_y();
        int res_z = KopsaClassification3DvisualConfiguration.getVisualConfiguration().getRes_z();
        ProgressBarWindow.getProgressBarWindow().update("Creating Model - computing response levels", 10, res_z - 1);
        double[][][] responses = new double[res_x][res_y][res_z];
        int[][][] responseLevel = new int[res_x][res_y][res_z];
        for (int z = 0; z < res_z; z++) {
            ProgressBarWindow.getProgressBarWindow().update(z);
            classification3D.classification3Dmodel.vect[classification3D.classification3Dmodel.paramZ] = (double) z / (double) res_z + 1.0 / (double) res_z;
            for (int x = 0; x < res_x; x++) {
                classification3D.classification3Dmodel.vect[classification3D.classification3Dmodel.paramX] = (double) x / (double) res_x + 1.0 / (double) res_x;
                for (int y = 0; y < res_y; y++) {
                    classification3D.classification3Dmodel.vect[classification3D.classification3Dmodel.paramY] = (double) y / (double) res_y + 1.0 / (double) res_y;
                    responses[x][y][z] = KopsaClassification3Dmodel.getNorm(classification3D.classification3Dmodel.myNetwork.getOutput(classification3D.classification3Dmodel.vect));
                    responseLevel[x][y][z] = (int) (responses[x][y][z] * KopsaClassification3DvisualConfiguration.CLASSMEMBERSHIP_LEVELS);
                }
            }
        }
        double quadDimX = ((double) 1 / res_x) / 2;
        double quadDimY = ((double) 1 / res_y) / 2;
        double quadDimZ = ((double) 1 / res_z) / 2;
        ProgressBarWindow.getProgressBarWindow().update("Creating Model - XY quads", 10, res_z - 1);
        for (int z = 0; z < res_z - 1; z++) {
            ProgressBarWindow.getProgressBarWindow().update(z);
            for (int x = 0; x < res_x; x++) {
                for (int y = 0; y < res_y; y++) {
                    int l1 = responseLevel[x][y][z];
                    int l2 = responseLevel[x][y][z + 1];
                    if (l1 > l2) {
                        LinkedList list = lists[l1 - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    } else if (l1 < l2) {
                        LinkedList list = lists[l2 - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    }
                }
            }
        }
        {
            LinkedList list;
            for (int x = 0; x < res_x; x++) {
                for (int y = 0; y < res_y; y++) {
                    int z = 0;
                    int l = responseLevel[x][y][z];
                    if (l > 0) {
                        list = listsBorders[l - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                    }
                    z = res_z - 1;
                    l = responseLevel[x][y][z];
                    if (l > 0) {
                        list = listsBorders[l - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    }
                }
            }
        }
        ProgressBarWindow.getProgressBarWindow().update("Creating Model - YZ quads", 10, res_x - 1);
        for (int x = 0; x < res_x - 1; x++) {
            ProgressBarWindow.getProgressBarWindow().update(x);
            for (int z = 0; z < res_z; z++) {
                for (int y = 0; y < res_y; y++) {
                    int l1 = responseLevel[x][y][z];
                    int l2 = responseLevel[x + 1][y][z];
                    if (l1 > l2) {
                        LinkedList list = lists[l1 - 1];
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    } else if (l1 < l2) {
                        LinkedList list = lists[l2 - 1];
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                    }
                }
            }
        }
        {
            LinkedList list;
            for (int z = 0; z < res_z; z++) {
                for (int y = 0; y < res_y; y++) {
                    int x = 0;
                    int l = responseLevel[x][y][z];
                    if (l > 0) {
                        list = listsBorders[l - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                    }
                    x = res_x - 1;
                    l = responseLevel[x][y][z];
                    if (l > 0) {
                        list = listsBorders[l - 1];
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    }
                }
            }
        }
        ProgressBarWindow.getProgressBarWindow().update("Creating Model - XZ quads", 10, res_y - 1);
        for (int y = 0; y < res_y - 1; y++) {
            ProgressBarWindow.getProgressBarWindow().update(y);
            for (int x = 0; x < res_x; x++) {
                for (int z = 0; z < res_z; z++) {
                    int l1 = responseLevel[x][y][z];
                    int l2 = responseLevel[x][y + 1][z];
                    if (l1 > l2) {
                        LinkedList list = lists[l1 - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                    } else if (l1 < l2) {
                        LinkedList list = lists[l2 - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    }
                }
            }
        }
        {
            LinkedList list;
            for (int x = 0; x < res_x; x++) {
                for (int z = 0; z < res_z; z++) {
                    int y = 0;
                    int l = responseLevel[x][y][z];
                    if (l > 0) {
                        list = listsBorders[l - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) - quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                    }
                    y = res_y - 1;
                    l = responseLevel[x][y][z];
                    if (l > 0) {
                        list = listsBorders[l - 1];
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x - quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z + quadDimZ - 0.5));
                        list.add(new Point3d((double) x / (double) res_x + quadDimX - 0.5, ((double) y / (double) res_y) + quadDimY - 0.5, (double) z / (double) res_z - quadDimZ - 0.5));
                    }
                }
            }
        }
        ProgressBarWindow.getProgressBarWindow().update("Creating Model - 3D shape", 30, lists.length);
        for (int i = 0; i < lists.length; i++) {
            ProgressBarWindow.getProgressBarWindow().update(i);
            LinkedList list = lists[i];
            if (list.size() != 0) {
                Point3d[] coords = new Point3d[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    coords[j] = (Point3d) list.get(j);
                }
                QuadArray quadArray = new QuadArray(coords.length, GeometryArray.COORDINATES);
                quadArray.setCoordinates(0, coords);
                GeometryInfo gi = new GeometryInfo(quadArray);
                NormalGenerator ng = new NormalGenerator();
                ng.generateNormals(gi);
                Shape3D shape3D = new Shape3D();
                shape3D.setAppearance(KopsaClassification3DvisualConfiguration.getVisualConfiguration().getAppearance(i + 1));
                shape3D.setGeometry(gi.getGeometryArray());
                modelRoot.addChild(shape3D);
            }
            list = listsBorders[i];
            if (list.size() != 0) {
                Point3d[] coords = new Point3d[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    coords[j] = (Point3d) list.get(j);
                }
                QuadArray quadArray = new QuadArray(coords.length, GeometryArray.COORDINATES);
                quadArray.setCoordinates(0, coords);
                GeometryInfo gi = new GeometryInfo(quadArray);
                NormalGenerator ng = new NormalGenerator();
                ng.generateNormals(gi);
                Shape3D shape3D = new Shape3D();
                shape3D.setAppearance(KopsaClassification3DvisualConfiguration.getVisualConfiguration().getBorderAppearance(i + 1));
                shape3D.setGeometry(gi.getGeometryArray());
                modelRoot.addChild(shape3D);
            }
        }
        return modelRoot;
    }

    /**
     * Creates some light.
     * @return
     */
    private Light createLight() {
        Vector3f direction = new Vector3f(-1, -1, -1);
        Color3f color = new Color3f(0.5f, 0.5f, 0.5f);
        DirectionalLight dl = new DirectionalLight(color, direction);
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 0), 50);
        dl.setInfluencingBounds(bs);
        return dl;
    }

    /**
     * Creates some light.
     * @return
     */
    private Light createLight2() {
        Vector3f direction = new Vector3f(1, 1, 1);
        Color3f color = new Color3f(0.5f, 0.5f, 0.5f);
        DirectionalLight dl = new DirectionalLight(color, direction);
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 0), 50);
        dl.setInfluencingBounds(bs);
        return dl;
    }

    /**
     * Creates some light.
     * @return
     */
    private Light createLight3() {
        Vector3f direction = new Vector3f(-1, 1, 1);
        Color3f color = new Color3f(0.5f, 0.5f, 0.5f);
        DirectionalLight dl = new DirectionalLight(color, direction);
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 0), 50);
        dl.setInfluencingBounds(bs);
        return dl;
    }

    /**
     * Creates some light.
     * @return
     */
    private Light createLight4() {
        Vector3f direction = new Vector3f(1, -1, -1);
        Color3f color = new Color3f(0.5f, 0.5f, 0.5f);
        DirectionalLight dl = new DirectionalLight(color, direction);
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 0), 50);
        dl.setInfluencingBounds(bs);
        return dl;
    }

    /**
     * Creates ambient light.
     * @return
     */
    private Light createAmbientLight() {
        AmbientLight amb = new AmbientLight(new Color3f(0.6f, 0.6f, 0.6f));
        BoundingSphere bs = new BoundingSphere(new Point3d(0, 0, 0), 50);
        amb.setInfluencingBounds(bs);
        return amb;
    }

    /**
     * Creates axis (without labels - those are rendered in the postSwap method)
     * @return
     */
    private Shape3D createAxis() {
        BranchGroup axisGroup = new BranchGroup();
        LineArray lineArray = new LineArray(6, GeometryArray.COORDINATES);
        lineArray.setCoordinates(0, new Point3d[] { new Point3d(-0.5, -0.5, -0.5), new Point3d(+0.5, -0.5, -0.5), new Point3d(-0.5, -0.5, -0.5), new Point3d(-0.5, +0.5, -0.5), new Point3d(-0.5, -0.5, -0.5), new Point3d(-0.5, -0.5, +0.5) });
        Appearance app = new Appearance();
        app.setLineAttributes(new LineAttributes(1, LineAttributes.PATTERN_SOLID, true));
        Shape3D axisShape = new Shape3D(lineArray, app);
        return axisShape;
    }

    /**
     * Creates spots representing input data.
     * @return
     */
    private BranchGroup createSpots() {
        BranchGroup bg = new BranchGroup();
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        double klobouk = KopsaClassification3DvisualConfiguration.getVisualConfiguration().getKlobouk();
        LinkedList[] list = new LinkedList[classification3D.classification3Dmodel.myData.getONumber()];
        for (int i = 0; i < list.length; i++) list[i] = new LinkedList();
        for (int i = 0; i < GlobalData.getInstance().getInstNumber(); i++) {
            Instance instance = (Instance) classification3D.classification3Dmodel.myData.group.elementAt(i);
            double spotX = instance.getStiValue(classification3D.classification3Dmodel.paramX);
            double spotY = instance.getStiValue(classification3D.classification3Dmodel.paramY);
            double spotZ = instance.getStiValue(classification3D.classification3Dmodel.paramZ);
            double spotD = 0;
            for (int j = 0; j < classification3D.classification3Dmodel.myData.getINumber(); j++) {
                if ((j != classification3D.classification3Dmodel.paramX) && (j != classification3D.classification3Dmodel.paramY) && (j != classification3D.classification3Dmodel.paramZ)) {
                    double d = instance.getStiValue(j) - classification3D.classification3Dmodel.vect[j];
                    spotD += d * d;
                }
            }
            if (spotD < klobouk) {
                for (int j = 0; j < classification3D.classification3Dmodel.myData.getONumber(); j++) {
                    if (instance.getStoValue(j) == 1) {
                        list[j].add(new double[] { spotX, spotY, spotZ, spotD });
                        break;
                    }
                }
            }
        }
        double maxSpotSize = KopsaClassification3DvisualConfiguration.getVisualConfiguration().getMaxSpotSize();
        for (int i = 0; i < list.length; i++) {
            if (list[i].size() > 0) {
                LineArray lineArray = new LineArray(list[i].size() * 6, GeometryArray.COORDINATES);
                int k = 0;
                for (Object o : list[i]) {
                    double[] spot = (double[]) o;
                    double size;
                    if (spot[3] < 0.0001) size = maxSpotSize; else {
                        size = (Math.sqrt(20 * (klobouk / spot[3])));
                        if (size > 20) size = maxSpotSize; else size = (size / 20) * maxSpotSize;
                    }
                    lineArray.setCoordinate(k++, new Point3d(spot[0] - size - 0.5, spot[1] - 0.5, spot[2] - 0.5));
                    lineArray.setCoordinate(k++, new Point3d(spot[0] + size - 0.5, spot[1] - 0.5, spot[2] - 0.5));
                    lineArray.setCoordinate(k++, new Point3d(spot[0] - 0.5, spot[1] - size - 0.5, spot[2] - 0.5));
                    lineArray.setCoordinate(k++, new Point3d(spot[0] - 0.5, spot[1] + size - 0.5, spot[2] - 0.5));
                    lineArray.setCoordinate(k++, new Point3d(spot[0] - 0.5, spot[1] - 0.5, spot[2] - size - 0.5));
                    lineArray.setCoordinate(k++, new Point3d(spot[0] - 0.5, spot[1] - 0.5, spot[2] + size - 0.5));
                }
                Shape3D spotsShape = new Shape3D(lineArray, KopsaClassification3DvisualConfiguration.getVisualConfiguration().getSpotAppearance(i));
                bg.addChild(spotsShape);
            }
        }
        return bg;
    }

    /**
     * This method is called by overriden Canvas3D method - it is called within postSwap method of the Canvas3D.
     *
     * It paints names and labels of axis.
     */
    private void doPostSwap() {
        Graphics graphics2D = canvas.getGraphics();
        graphics2D.setFont(new Font("SansSerif", Font.PLAIN, 12));
        graphics2D.setColor(Color.WHITE);
        Transform3D t3d = new Transform3D();
        canvas.getVworldToImagePlate(t3d);
        Point3d point3d = new Point3d();
        Point2d point2d = new Point2d();
        t3d.transform(new Point3d(+0.5, -0.5, -0.5), point3d);
        canvas.getPixelLocationFromImagePlate(point3d, point2d);
        graphics2D.drawString(((InputFactor) classification3D.classification3Dmodel.myData.iFactor.elementAt(classification3D.classification3Dmodel.paramX)).getName(), (int) point2d.x, (int) point2d.y);
        t3d.transform(new Point3d(-0.5, +0.5, -0.5), point3d);
        canvas.getPixelLocationFromImagePlate(point3d, point2d);
        graphics2D.drawString(((InputFactor) classification3D.classification3Dmodel.myData.iFactor.elementAt(classification3D.classification3Dmodel.paramY)).getName(), (int) point2d.x, (int) point2d.y);
        t3d.transform(new Point3d(-0.5, -0.5, +0.5), point3d);
        canvas.getPixelLocationFromImagePlate(point3d, point2d);
        graphics2D.drawString(((InputFactor) classification3D.classification3Dmodel.myData.iFactor.elementAt(classification3D.classification3Dmodel.paramZ)).getName(), (int) point2d.x, (int) point2d.y);
        if (KopsaClassification3DvisualConfiguration.getVisualConfiguration().isShowAxis()) {
            writeAxisValues(0, graphics2D, t3d);
            writeAxisValues(1, graphics2D, t3d);
            writeAxisValues(2, graphics2D, t3d);
        }
        graphics2D.setColor(Color.BLACK);
    }

    /**
     * Writes values of one axis.
     *
     * @param dimension determines axis to be drawn
     * @param graphics2D
     * @param t3d
     */
    private void writeAxisValues(int dimension, Graphics graphics2D, Transform3D t3d) {
        Point2d point2d = new Point2d();
        double[] point = new double[] { -0.5, -0.5, -0.5 };
        Point3d point3d = new Point3d();
        for (int i = 0; i < classification3D.classification3Dmodel.valuesPos[dimension].length; i++) {
            point[dimension] = (Double) classification3D.classification3Dmodel.valuesPos[dimension][i];
            t3d.transform(new Point3d(point), point3d);
            canvas.getPixelLocationFromImagePlate(point3d, point2d);
            graphics2D.drawString((String) classification3D.classification3Dmodel.valuesVals[dimension][i], (int) point2d.x, (int) point2d.y);
        }
    }

    /**
     * Simple getter.
     * @return Canvas3D being created encapsulated by this class.
     */
    public Canvas3D getCanvas() {
        return canvas;
    }
}
