package org.jcrpg.threed.standing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;
import org.jcrpg.apps.Jcrpg;
import org.jcrpg.space.Cube;
import org.jcrpg.space.Side;
import org.jcrpg.threed.GeoTileLoader;
import org.jcrpg.threed.J3DCore;
import org.jcrpg.threed.ModelLoader;
import org.jcrpg.threed.ModelPool;
import org.jcrpg.threed.NodePlaceholder;
import org.jcrpg.threed.ParallelLoadingHelper;
import org.jcrpg.threed.PooledNode;
import org.jcrpg.threed.RenderedAreaThread;
import org.jcrpg.threed.ModelLoader.BillboardNodePooled;
import org.jcrpg.threed.engine.GeometryBatchHelper;
import org.jcrpg.threed.engine.ModelGeometryBatch;
import org.jcrpg.threed.engine.RenderedCubePool;
import org.jcrpg.threed.engine.TrimeshGeometryBatch;
import org.jcrpg.threed.engine.VectorPool;
import org.jcrpg.threed.engine.geometryinstancing.BufferPool;
import org.jcrpg.threed.engine.geometryinstancing.ExactBufferPool;
import org.jcrpg.threed.engine.geometryinstancing.GeometryBatchMesh;
import org.jcrpg.threed.engine.geometryinstancing.QuickOrderedList;
import org.jcrpg.threed.engine.vegetation.BillboardPartVegetation;
import org.jcrpg.threed.scene.RenderedArea;
import org.jcrpg.threed.scene.RenderedCube;
import org.jcrpg.threed.scene.model.Model;
import org.jcrpg.threed.scene.model.SimpleModel;
import org.jcrpg.threed.scene.side.RenderedClimateDependentSide;
import org.jcrpg.threed.scene.side.RenderedContinuousSide;
import org.jcrpg.threed.scene.side.RenderedHashAlteredSide;
import org.jcrpg.threed.scene.side.RenderedHashRotatedSide;
import org.jcrpg.threed.scene.side.RenderedSide;
import org.jcrpg.threed.scene.side.RenderedTopSide;
import org.jcrpg.ui.UIBase;
import org.jcrpg.world.Engine;
import org.jcrpg.world.climate.CubeClimateConditions;
import org.jcrpg.world.place.Boundaries;
import org.jcrpg.world.place.SurfaceHeightAndType;
import org.jcrpg.world.place.World;
import org.jcrpg.world.time.Time;
import com.ardor3d.math.Quaternion;
import com.ardor3d.math.Vector3;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.Spatial;
import com.ardor3d.scenegraph.event.DirtyType;
import com.ardor3d.scenegraph.hint.CullHint;
import com.ardor3d.scenegraph.visitor.UpdateModelBoundVisitor;

/**
 * Static elements 3d display part.
 * @author pali
 *
 */
public class J3DStandingEngine {

    public static final Logger logger = Logger.getLogger(J3DStandingEngine.class.getName());

    public J3DCore core;

    public ModelLoader modelLoader;

    public ModelPool modelPool;

    public UIBase uiBase;

    public Engine engine;

    public World world;

    public RenderedArea renderedArea;

    public GeometryBatchHelper batchHelper;

    public Node intRootNode = null;

    public Node extRootNode = null;

    public Node intWaterRefNode = null;

    public Node extWaterRefNode = null;

    public Node intSSAONode = null;

    public Node extSSAONode = null;

    public J3DStandingEngine(J3DCore core) {
        this.core = core;
        this.batchHelper = core.batchHelper;
        this.modelLoader = core.modelLoader;
        this.uiBase = core.uiBase;
        this.engine = core.gameState.engine;
        this.world = core.gameState.world;
        renderedArea = core.renderedArea;
        renderedArea.setRenderDistance(J3DCore.SETTINGS.RENDER_DISTANCE_CALC);
        renderedArea.setRenderDistanceFarview(J3DCore.SETTINGS.RENDER_DISTANCE_FARVIEW);
        modelPool = core.modelPool;
        intRootNode = core.intRootNode;
        extRootNode = core.extRootNode;
        intWaterRefNode = core.intWaterRefNode;
        extWaterRefNode = core.extWaterRefNode;
        intSSAONode = core.intSSAONode;
        extSSAONode = core.extSSAONode;
    }

    /**
	 * Start this when newly initializing core.
	 */
    public void reinit() {
        this.batchHelper = core.batchHelper;
        this.uiBase = core.uiBase;
        this.engine = core.gameState.engine;
        this.world = core.gameState.world;
        renderedArea = core.renderedArea;
        renderedArea.setRenderDistance(J3DCore.SETTINGS.RENDER_DISTANCE_CALC);
        renderedArea.setRenderDistanceFarview(J3DCore.SETTINGS.RENDER_DISTANCE_FARVIEW);
        modelPool = core.modelPool;
        intRootNode = core.intRootNode;
        extRootNode = core.extRootNode;
        optimizeAngle = J3DCore.OPTIMIZE_ANGLES;
        intWaterRefNode = core.intWaterRefNode;
        extWaterRefNode = core.extWaterRefNode;
        intSSAONode = core.intSSAONode;
        extSSAONode = core.extSSAONode;
    }

    HashMap<Long, RenderedCube> hmCurrentCubes = new HashMap<Long, RenderedCube>();

    ArrayList<RenderedCube> alCurrentCubes = new ArrayList<RenderedCube>();

    HashMap<Long, RenderedCube> hmCurrentCubes_FARVIEW = new HashMap<Long, RenderedCube>();

    ArrayList<RenderedCube> alCurrentCubes_FARVIEW = new ArrayList<RenderedCube>();

    public ParallelLoadingHelper parallelLoadingHelper = new ParallelLoadingHelper();

    public int numberOfProcesses = 0;

    /**
	 * Renders the scenario, adds new jme Nodes, removes outmoved nodes and keeps old nodes on scenario.
	 * @param safeMode Parallel rendering should call it with that parameter.
	 */
    @SuppressWarnings("unchecked")
    public HashSet<RenderedCube>[] render(int renderPosX, int renderPosY, int renderPostZ, int viewPositionX, int viewPositionY, int viewPositionZ, boolean rerender, boolean safeMode) {
        numberOfProcesses++;
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("RENDERING...");
        HashSet<RenderedCube> detacheable = new HashSet<RenderedCube>();
        HashSet<RenderedCube> detacheable_FARVIEW = new HashSet<RenderedCube>();
        HashMap<Long, RenderedCube> hmCurrentCubesForSafeRenderLocal = null;
        hmCurrentCubesForSafeRenderLocal = safeMode ? parallelLoadingHelper.getBackupCurrentCubes() : hmCurrentCubes;
        if (!safeMode) {
            uiBase.hud.sr.setVisibility(true, "LOAD");
            uiBase.hud.mainBox.addEntry("Loading Geo at X/Z " + core.gameState.getCurrentRenderPositions().viewPositionX + "/" + core.gameState.getCurrentRenderPositions().viewPositionZ + "...");
            J3DCore.getInstance().drawForced();
        }
        lastRenderX = renderPosX;
        lastRenderY = renderPosY;
        lastRenderZ = renderPostZ;
        modelLoader.startRender();
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("**** RENDER ****");
        Time localTime = engine.getWorldMeanTime().getLocalTime(world, viewPositionX, viewPositionY, viewPositionZ);
        CubeClimateConditions conditions = world.climate.getCubeClimate(localTime, viewPositionX, viewPositionY, viewPositionZ, false);
        if (conditions != null) if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("- " + conditions.getBelt() + " \n - " + conditions.getSeason() + " \n" + conditions.getDayTime());
        long time = System.currentTimeMillis();
        if (rerender) {
            world.clearCaches();
        }
        RenderedCube[][] newAndOldCubes = renderedArea.getRenderedSpace(world, viewPositionX, viewPositionY, viewPositionZ, core.gameState.getCurrentRenderPositions().viewDirection, J3DCore.SETTINGS.FARVIEW_ENABLED, rerender);
        if (newAndOldCubes == null) {
            numberOfProcesses--;
            return null;
        }
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("RENDER AREA TIME: " + (System.currentTimeMillis() - time));
        RenderedCube[] cubes = newAndOldCubes[0];
        RenderedCube[] removableCubes = newAndOldCubes[1];
        detacheable = doRender(renderPosX, renderPosY, renderPostZ, cubes, removableCubes, hmCurrentCubesForSafeRenderLocal);
        if (J3DCore.SETTINGS.FARVIEW_ENABLED) {
            cubes = newAndOldCubes[2];
            removableCubes = newAndOldCubes[3];
            detacheable_FARVIEW = doRender(renderPosX, renderPosY, renderPostZ, cubes, removableCubes, hmCurrentCubes_FARVIEW);
        }
        modelLoader.stopRenderAndClear();
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info(" ######################## LIVE NODES = " + liveNodes + " --- LIVE HM QUADS " + J3DCore.hmSolidColorSpatials.size());
        if (!safeMode) {
            uiBase.hud.sr.setVisibility(false, "LOAD");
            uiBase.hud.mainBox.addEntry("Load Complete.");
        }
        HashSet<RenderedCube>[] ret = new HashSet[] { detacheable, detacheable_FARVIEW };
        numberOfProcesses--;
        return ret;
    }

    /**
	 * Renders all the cubes' sides' placeholders with renderSide.
	 * @param cubes
	 * @param removableCubes
	 * @param hmCurrentCubes
	 * @return
	 */
    public HashSet<RenderedCube> doRender(int relX, int relY, int relZ, RenderedCube[] cubes, RenderedCube[] removableCubes, HashMap<Long, RenderedCube> hmCurrentCubes) {
        int already = 0;
        int newly = 0;
        int removed = 0;
        long timeS = System.currentTimeMillis();
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("1-RSTAT = N" + newly + " A" + already + " R" + removed + " -- time: " + (System.currentTimeMillis() - timeS));
        HashSet<RenderedCube> detacheable = new HashSet<RenderedCube>();
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("!!!! REMOVABLE CUBES = " + removableCubes.length);
        for (RenderedCube c : removableCubes) {
            if (c == null) continue;
            Long cubeKey = Boundaries.getKey(c.cube.x, c.cube.y, c.cube.z);
            c = hmCurrentCubes.remove(cubeKey);
            if (c != null && c.hsRenderedNodes != null) {
                detacheable.add(c);
                liveNodes -= c.hsRenderedNodes.size();
            }
        }
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("1-RSTAT = N" + newly + " A" + already + " R" + removed + " -- time: " + (System.currentTimeMillis() - timeS));
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("getRenderedSpace size=" + cubes.length);
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("hmCurrentCubes: " + hmCurrentCubes.keySet().size());
        for (int i = 0; i < cubes.length; i++) {
            RenderedCube c = cubes[i];
            Long cubeKey = Boundaries.getKey(c.cube.x, c.cube.y, c.cube.z);
            if (hmCurrentCubes.containsKey(cubeKey)) {
                already++;
                continue;
            }
            newly++;
            Side[][] sides = c.cube.sides;
            for (int j = 0; j < sides.length; j++) {
                if (sides[j] != null) {
                    for (int k = 0; k < sides[j].length; k++) {
                        renderSide(c, relX, relY, relZ, c.renderedX, c.renderedY, c.renderedZ, j, sides[j][k], false);
                    }
                }
            }
            hmCurrentCubes.put(cubeKey, c);
        }
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("hmCurrentCubes: " + hmCurrentCubes.keySet().size());
        for (RenderedCube cToDetach : detacheable) {
            removed++;
            if (!cToDetach.farview) {
                inViewPort.remove(cToDetach);
                outOfViewPort.remove(cToDetach);
            }
            if (cToDetach.farview) {
                inFarViewPort.remove(cToDetach);
                outOfFarViewPort.remove(cToDetach);
            }
        }
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("RSTAT = N" + newly + " A" + already + " R" + removed + " -- time: " + (System.currentTimeMillis() - timeS));
        return detacheable;
    }

    /**
	 * Renders a set of node into 3d space, rotating, positioning them.
	 * @param n Nodes
	 * @param cube the r.cube parent of the nodes, needed for putting the rendered node as child into it.
	 * @param x X cubesized distance from current relativeX
	 * @param y Y cubesized distance from current relativeY
	 * @param z Z cubesized distance from current relativeZ
	 * @param direction Direction
	 * @param horizontalRotation Horizontal rotation
	 * @param scale Scale
	 */
    private void renderNodes(NodePlaceholder[] n, RenderedCube cube, int relX, int relY, int relZ, int x, int y, int z, int direction, int horizontalRotation, float scale) {
        if (n == null) return;
        Object[] f = (Object[]) J3DCore.directionAnglesAndTranslations.get(direction);
        double cX = ((x + relX) * J3DCore.CUBE_EDGE_SIZE + 1f * ((int[]) f[1])[0] * (cube.farview ? J3DCore.FARVIEW_GAP : 1));
        double cY = ((y + relY) * J3DCore.CUBE_EDGE_SIZE + 1f * ((int[]) f[1])[1] * (cube.farview ? J3DCore.FARVIEW_GAP : 1));
        double cZ = ((z - relZ) * J3DCore.CUBE_EDGE_SIZE + 1f * ((int[]) f[1])[2] * (cube.farview ? J3DCore.FARVIEW_GAP : 1));
        if (cube.farview) {
            cY += J3DCore.CUBE_EDGE_SIZE * 1.5f;
        }
        Quaternion hQ = null;
        Quaternion hQReal = null;
        if (horizontalRotation != -1) {
            hQ = J3DCore.horizontalRotations.get(horizontalRotation);
            hQReal = J3DCore.horizontalRotationsReal.get(horizontalRotation);
        }
        boolean needsFarviewScale = true;
        for (int i = 0; i < n.length; i++) {
            needsFarviewScale = true && cube.farview;
            if (n[i].model.type == Model.PARTLYBILLBOARDMODEL) {
                needsFarviewScale = false;
            }
            n[i].setLocalTranslation(new Vector3(cX + n[i].model.disposition[0], cY + n[i].model.disposition[1], cZ + n[i].model.disposition[2]));
            boolean groundTileModel = true;
            if (!(n[i].model instanceof SimpleModel) || !((SimpleModel) n[i].model).generatedGroundModel) {
                groundTileModel = false;
            }
            Quaternion q = (Quaternion) f[0];
            Quaternion qC = null;
            if (n[i].model.noSpecialSteepRotation) {
                qC = new Quaternion(q);
            } else {
                qC = new Quaternion();
            }
            if (!groundTileModel && hQ != null) {
                n[i].horizontalRotation = hQReal;
                qC.multiplyLocal(hQ);
            }
            if (n[i].model.rotateAndLocate) {
                Vector3 newTrans = null;
                Vector3 dislocation = null;
                int tmpDir = direction;
                if (direction == 5) {
                    tmpDir = horizontalRotation;
                }
                if (tmpDir == 0 || tmpDir == 3) {
                    dislocation = VectorPool.getVector3(0, 0, -2f * n[i].model.dislocationRate);
                    newTrans = n[i].getLocalTranslation().add(0, 0, -2f * n[i].model.dislocationRate, null);
                }
                if (tmpDir == 1 || tmpDir == 2) {
                    dislocation = VectorPool.getVector3(0, 0, +2f * n[i].model.dislocationRate);
                    newTrans = n[i].getLocalTranslation().add(0, 0, +2f * n[i].model.dislocationRate, null);
                }
                if (newTrans != null) {
                    if (n[i].model.elevateOnSteep) {
                        double height = GeoTileLoader.getHeight(dislocation, n[i]);
                        newTrans.setY(newTrans.getY() + height - 0.1d);
                    }
                    n[i].setLocalTranslation(newTrans);
                }
                VectorPool.releaseVector3(dislocation);
            }
            if (!groundTileModel) {
                if (n[i].model.elevateOnSteep && !n[i].model.rotateAndLocate) {
                    Vector3 newTrans = n[i].getLocalTranslation().addLocal(0f, (J3DCore.CUBE_EDGE_SIZE * cube.cube.middleHeight - J3DCore.CUBE_EDGE_SIZE * 0.25f) * (cube.farview ? J3DCore.FARVIEW_GAP : 1), 0f);
                    n[i].setLocalTranslation(newTrans);
                }
            }
            if (n[i].getUserData() != null) {
                if (!groundTileModel && cube.cube.steepDirection != SurfaceHeightAndType.NOT_STEEP) {
                    if (n[i].model.noSpecialSteepRotation) {
                        try {
                            qC.multiplyLocal(J3DCore.steepRotations.get(cube.cube.steepDirection));
                        } catch (Exception ex) {
                            if (J3DCore.LOGGING()) Jcrpg.LOGGER.info(cube.cube + " --- " + cube.cube.steepDirection);
                        }
                    } else {
                        qC = J3DCore.steepRotations_special.get(cube.cube.steepDirection);
                    }
                    if (cube.cube.steepDirection == J3DCore.NORTH || cube.cube.steepDirection == J3DCore.SOUTH) {
                        if (n[i].model.noSpecialSteepRotation) {
                            n[i].setLocalScale(new Vector3(1f, 1.41421356f, 1f).multiplyLocal(needsFarviewScale ? J3DCore.FARVIEW_GAP : 1));
                        } else {
                            n[i].setLocalScale(new Vector3(1.41421356f, 1, 1f).multiplyLocal(needsFarviewScale ? J3DCore.FARVIEW_GAP : 1));
                        }
                    } else {
                        n[i].setLocalScale(new Vector3(1.41421356f, 1, 1f).multiplyLocal(needsFarviewScale ? scale * J3DCore.FARVIEW_GAP : 1));
                    }
                } else {
                    n[i].setLocalScale(needsFarviewScale ? scale * J3DCore.FARVIEW_GAP : 1);
                }
            } else {
                if (n[i].model.genericScale != 1f) {
                    scale *= n[i].model.genericScale;
                }
                n[i].setLocalScale(needsFarviewScale ? scale * J3DCore.FARVIEW_GAP : scale);
            }
            if (!groundTileModel) {
                n[i].setLocalRotation(qC);
            } else {
                n[i].setLocalRotation(new Quaternion());
            }
            cube.hsRenderedNodes.add((NodePlaceholder) n[i]);
            liveNodes++;
        }
    }

    HashSet<RenderedCube> inViewPort = new HashSet<RenderedCube>();

    ArrayList<NodePlaceholder> conditionalNodes = new ArrayList<NodePlaceholder>();

    HashSet<RenderedCube> inFarViewPort = new HashSet<RenderedCube>();

    HashSet<RenderedCube> outOfViewPort = new HashSet<RenderedCube>();

    HashSet<RenderedCube> outOfFarViewPort = new HashSet<RenderedCube>();

    int cullVariationCounter = 0;

    public boolean optimizeAngle = J3DCore.OPTIMIZE_ANGLES;

    /**
	 * Rendering standing nodes to viewport. Converting nodePlaceHolders to actual Nodes if they are visible and removing non-visible nodes. (Using modelPool.)
	 */
    public void renderToViewPort() {
        renderToViewPort(optimizeAngle ? 1.1f : 3.15f);
    }

    public void renderToViewPort(int segmentCount, int segments) {
        renderToViewPort(optimizeAngle ? 1.1f : 3.15f, true, segmentCount, segments);
    }

    public void renderToViewPort(float refAngle) {
        renderToViewPort(refAngle, false, 0, 0);
    }

    /**
	 * Environment's looping sounds. Static field to make stopping it available for all instances
	 * (encounterEngine too).
	 */
    public static HashMap<String, Double> continuousSoundsAndDistance = new HashMap<String, Double>();

    public static HashMap<String, Double> previousContinuousSoundsAndDistance = new HashMap<String, Double>();

    /**
	 * list to store the newly rendered nodes during renderToViewPort for special processing: 
	 * first render with CULL_NEVER then update it to CULL_INHERIT, plus updateRenderState call.
	 */
    public ArrayList<Node> newNodesToSetCullingDynamic = new ArrayList<Node>();

    protected int fragmentedViewDivider = 8;

    public int lastRenderX = -1000, lastRenderY, lastRenderZ;

    /**
	 * Set this to true if you want a full rerender of the surroundings, renderedArea won't use it's cache and will return you all previous cubes to remove.
	 */
    public boolean rerender = false;

    /**
	 * Tells if before rerender all previous nodes should be removed.
	 */
    public boolean rerenderWithRemove = false;

    public static boolean nonDrawingRender = false;

    public boolean threadRendering = false;

    public boolean isRendering() {
        return threadRendering || newRenderPending || updateAfterRenderNeeded;
    }

    public boolean needsIterativeRendering() {
        return threadRendering;
    }

    public void iterativeRenderingStarted() {
        threadRendering = false;
        updateAfterRenderNeeded = true;
    }

    public boolean isForcedNonIterativeRenderNeeded() {
        return !threadRendering && !updateAfterRenderNeeded && newRenderPending;
    }

    int currentConditionalNode = 0;

    public boolean updateAfterRenderNeeded = false;

    public boolean newRenderPending = false;

    public int currentNode = 0;

    public int currentFarviewNode = 0;

    long sumAddRemoveBatch = 0;

    float refAngle = 3.16f;

    double minAngleCalc = J3DCore.CUBE_EDGE_SIZE * J3DCore.CUBE_EDGE_SIZE * 6;

    int fromCubeCount = 0;

    int toCubeCount = alCurrentCubes.size();

    int visibleNodeCounter = 0;

    int nonVisibleNodeCounter = 0;

    int removedNodeCounter = 0;

    int addedNodeCounter = 0;

    boolean overrideBatch = true;

    Vector3 blendedGeoTileDisplacement = new Vector3(-0.5f * J3DCore.CUBE_EDGE_SIZE * (J3DCore.FARVIEW_GAP), 0, -0.5f * J3DCore.CUBE_EDGE_SIZE * (J3DCore.FARVIEW_GAP));

    int fromCubeCount_FARVIEW = 0;

    int toCubeCount_FARVIEW = alCurrentCubes_FARVIEW.size();

    double maxFarViewDist = (J3DCore.CUBE_EDGE_SIZE * J3DCore.CUBE_EDGE_SIZE) * J3DCore.SETTINGS.RENDER_DISTANCE_FARVIEW * J3DCore.SETTINGS.RENDER_DISTANCE_FARVIEW;

    public static long maxRtvpTime = 0;

    /**
	 * rendering step by step the needed things for the view point - 
	 * gradually done, j3dcore should call this between draws until
	 * finished.
	 * @return True if finished.
	 */
    public boolean renderToViewPortStepByStep() {
        long countRtvpTime = System.currentTimeMillis();
        GeometryBatchMesh.GLOBAL_CAN_COMMIT = false;
        if (currentNode < alCurrentCubes.size()) {
            long time = System.currentTimeMillis();
            while (true) {
                int cc = currentNode;
                RenderedCube c = alCurrentCubes.get(cc);
                if (c.hsRenderedNodes.size() > 0) {
                    boolean found = false;
                    boolean fragmentViewDist = false;
                    int calcFragmentViewDivider = 2;
                    if (c.cube != null) {
                        fragmentViewDist = c.cube.internalCube && (!core.gameState.getCurrentRenderPositions().insideArea) || (!c.cube.internalCube && !c.cube.internalLight) && core.gameState.getCurrentRenderPositions().insideArea;
                        if (fragmentViewDist) calcFragmentViewDivider = fragmentedViewDivider;
                    }
                    if (c.cube != null) {
                        if (!core.gameState.getCurrentRenderPositions().insideArea && !core.gameState.getCurrentRenderPositions().internalLight) {
                            if (c.cube.internalCube) {
                                calcFragmentViewDivider *= 4;
                            }
                        }
                        if (core.gameState.getCurrentRenderPositions().insideArea && !core.gameState.getCurrentRenderPositions().internalLight) {
                            if (!c.cube.internalCube) {
                                calcFragmentViewDivider *= 4;
                            }
                        }
                    }
                    int checkDistCube = (J3DCore.SETTINGS.VIEW_DISTANCE / calcFragmentViewDivider);
                    boolean checked = false;
                    int distX = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionX + 1 - c.cube.x);
                    int distY = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionY - c.cube.y);
                    int distZ = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionZ - 1 - c.cube.z);
                    if (distX > world.realSizeX / 2) {
                        if (core.gameState.getCurrentRenderPositions().viewPositionX < world.realSizeX / 2) {
                            distX = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionX - (c.cube.x - world.realSizeX));
                        } else {
                            distX = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionX - (c.cube.x + world.realSizeX));
                        }
                    }
                    if (distZ > world.realSizeZ / 2) {
                        if (core.gameState.getCurrentRenderPositions().viewPositionZ < world.realSizeZ / 2) {
                            distZ = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionZ - (c.cube.z - world.realSizeZ));
                        } else {
                            distZ = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionZ - (c.cube.z + world.realSizeZ));
                        }
                    }
                    if (distX <= checkDistCube && distY <= checkDistCube && distZ <= checkDistCube) {
                        checked = true;
                    } else {
                    }
                    double dist = 0;
                    for (NodePlaceholder n : c.hsRenderedNodes) {
                        if (checked) {
                            dist = n.getLocalTranslation().distanceSquared(core.getCamera().getLocation());
                            found = true;
                            break;
                        }
                    }
                    if (checked && J3DCore.SETTINGS.SOUND_ENABLED) {
                        HashSet<String> sounds = c.cube.getContinuousSounds();
                        if (sounds != null) {
                            for (String s : sounds) {
                                Double f = continuousSoundsAndDistance.get(s);
                                if (f == null) {
                                    continuousSoundsAndDistance.put(s, dist);
                                } else {
                                    if (f > dist) {
                                        continuousSoundsAndDistance.put(s, dist);
                                    }
                                }
                            }
                        }
                    }
                    if (found) {
                        visibleNodeCounter++;
                        if (!inViewPort.contains(c)) {
                            addedNodeCounter++;
                            inViewPort.add(c);
                            if (inFarViewPort.contains(c)) {
                                removedNodeCounter++;
                                for (NodePlaceholder n : c.hsRenderedNodes) {
                                    if (!n.model.farViewEnabled) continue;
                                    overrideBatch = true;
                                    if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                        if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                            if (n.neighborCubeData == null) {
                                                n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                            }
                                            overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                        } else {
                                            overrideBatch = false;
                                        }
                                        if (!overrideBatch) {
                                            if (n != null) {
                                                long t0 = System.currentTimeMillis();
                                                batchHelper.removeItem(c.cube.internalCube, n.model, n, true);
                                                sumAddRemoveBatch += System.currentTimeMillis() - t0;
                                            }
                                        }
                                        if (n.model.type == Model.TEXTURESTATEVEGETATION) {
                                            conditionalNodes.remove(n);
                                        }
                                    }
                                    if (overrideBatch) {
                                        PooledNode pooledRealNode = n.realNode;
                                        if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                            if (pooledRealNode != null) {
                                                batchHelper.removeBillboardVegetationItem(c.cube.internalCube, n.model, n, n.farView, (BillboardPartVegetation) pooledRealNode);
                                            }
                                        }
                                        n.realNode = null;
                                        if (pooledRealNode != null) {
                                            Node realNode = (Node) pooledRealNode;
                                            if (J3DCore.SETTINGS.SHADOWS) core.removeOccludersRecoursive(realNode);
                                            realNode.removeFromParent();
                                            modelPool.releaseNode(pooledRealNode);
                                        }
                                    }
                                    n.farView = false;
                                }
                            }
                            inFarViewPort.remove(c);
                            outOfViewPort.remove(c);
                            for (NodePlaceholder n : c.hsRenderedNodes) {
                                n.farView = false;
                                overrideBatch = true;
                                if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                    if (!n.model.alwaysRenderBatch && n.model.type == Model.TEXTURESTATEVEGETATION) {
                                        conditionalNodes.add(n);
                                        overrideBatch = false;
                                        continue;
                                    } else if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                        if (n.neighborCubeData == null || n.neighborCubeData.wereNeigboursNotFullyDetected()) {
                                            n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                        }
                                        overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                    } else {
                                        overrideBatch = false;
                                    }
                                    if (!overrideBatch) {
                                        if (n.trimeshGeomBatchInstance == null && n.modelGeomBatchInstance == null) {
                                            long t0 = System.currentTimeMillis();
                                            batchHelper.addItem(this, c.cube.internalCube, n.model, n, false);
                                            sumAddRemoveBatch += System.currentTimeMillis() - t0;
                                        }
                                    }
                                }
                                if (overrideBatch) {
                                    Node realPooledNode = (Node) modelPool.getModel(c, n.model, n);
                                    if (realPooledNode == null) continue;
                                    n.realNode = (PooledNode) realPooledNode;
                                    if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                        if (realPooledNode != null) batchHelper.addBillboardVegetationItem(this, c.cube.internalCube, n.model, n, false, (BillboardPartVegetation) realPooledNode);
                                    } else {
                                        {
                                        }
                                        realPooledNode.setTranslation(n.getLocalTranslation());
                                        if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel && n.neighborCubeData != null && n.neighborCubeData.getTextureKeyPartForBatch() != null) {
                                            realPooledNode.setTranslation(realPooledNode.getTranslation().add(-0.5f * J3DCore.CUBE_EDGE_SIZE, 0, -0.5f * J3DCore.CUBE_EDGE_SIZE, null));
                                        }
                                        if (realPooledNode instanceof BillboardNodePooled) {
                                        } else {
                                            if (realPooledNode.getChildren() != null) for (Spatial s : realPooledNode.getChildren()) {
                                                if ((s instanceof Node)) {
                                                    if (((Node) s).getChildren() != null) for (Spatial s2 : ((Node) s).getChildren()) {
                                                        if (s2 instanceof Node) {
                                                            for (Spatial s3 : ((Node) s2).getChildren()) {
                                                                if (s3 instanceof TrimeshGeometryBatch) {
                                                                    ((TrimeshGeometryBatch) s3).horizontalRotation = n.horizontalRotation;
                                                                }
                                                            }
                                                        }
                                                        s2.setScale(n.getLocalScale());
                                                        if (s2 instanceof TrimeshGeometryBatch) {
                                                            ((TrimeshGeometryBatch) s2).horizontalRotation = n.horizontalRotation;
                                                        } else {
                                                            s2.setRotation(n.getLocalRotation());
                                                        }
                                                    }
                                                } else {
                                                    s.setRotation(n.getLocalRotation());
                                                    s.setScale(n.getLocalScale());
                                                }
                                            }
                                        }
                                        if (c.cube.internalCube) {
                                            intSSAONode.attachChild((Node) realPooledNode);
                                        } else {
                                            extSSAONode.attachChild((Node) realPooledNode);
                                            if (n.model.isShadowNeeded()) {
                                                core.addOccluder(realPooledNode);
                                            }
                                        }
                                        realPooledNode.getSceneHints().setCullHint(CullHint.Inherit);
                                        newNodesToSetCullingDynamic.add(realPooledNode);
                                    }
                                }
                            }
                        }
                    } else {
                        nonVisibleNodeCounter++;
                        if (!outOfViewPort.contains(c)) {
                            removedNodeCounter++;
                            outOfViewPort.add(c);
                            inViewPort.remove(c);
                            inFarViewPort.remove(c);
                            for (NodePlaceholder n : c.hsRenderedNodes) {
                                overrideBatch = true;
                                if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                    if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                        if (n.neighborCubeData == null) {
                                            n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                        }
                                        overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                    } else {
                                        overrideBatch = false;
                                    }
                                    if (!overrideBatch) {
                                        if (n != null) {
                                            long t0 = System.currentTimeMillis();
                                            batchHelper.removeItem(c.cube.internalCube, n.model, n, n.farView);
                                            sumAddRemoveBatch += System.currentTimeMillis() - t0;
                                        }
                                    }
                                }
                                if (overrideBatch) {
                                    PooledNode pooledRealNode = n.realNode;
                                    if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                        if (pooledRealNode != null) {
                                            batchHelper.removeBillboardVegetationItem(c.cube.internalCube, n.model, n, n.farView, (BillboardPartVegetation) pooledRealNode);
                                        }
                                    }
                                    n.realNode = null;
                                    if (pooledRealNode != null) {
                                        Node realNode = (Node) pooledRealNode;
                                        if (J3DCore.SETTINGS.SHADOWS) core.removeOccludersRecoursive(realNode);
                                        realNode.removeFromParent();
                                        modelPool.releaseNode(pooledRealNode);
                                    }
                                }
                                n.farView = false;
                            }
                        }
                    }
                }
                currentNode++;
                if (currentNode == alCurrentCubes.size()) {
                    countRtvpTime = System.currentTimeMillis() - countRtvpTime;
                    if (countRtvpTime > maxRtvpTime) {
                        maxRtvpTime = countRtvpTime;
                    }
                    return false;
                }
                if (System.currentTimeMillis() - time > 5) {
                    countRtvpTime = System.currentTimeMillis() - countRtvpTime;
                    if (countRtvpTime > maxRtvpTime) {
                        maxRtvpTime = countRtvpTime;
                    }
                    return false;
                }
            }
        }
        if (J3DCore.SETTINGS.FARVIEW_ENABLED) if (currentNode < alCurrentCubes_FARVIEW.size()) {
            long time = System.currentTimeMillis();
            while (true) {
                int cc = currentFarviewNode;
                RenderedCube c = alCurrentCubes_FARVIEW.get(cc);
                if (c.hsRenderedNodes.size() > 0) {
                    boolean foundFar = false;
                    boolean fragmentViewDist = false;
                    if (c.cube != null) {
                        fragmentViewDist = c.cube.internalCube && (!core.gameState.getCurrentRenderPositions().insideArea) || (!c.cube.internalCube) && core.gameState.getCurrentRenderPositions().insideArea;
                    }
                    int checkDistCube = (fragmentViewDist ? J3DCore.SETTINGS.VIEW_DISTANCE / fragmentedViewDivider : J3DCore.SETTINGS.VIEW_DISTANCE / 2);
                    boolean checked = false;
                    int distX = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionX - c.cube.x);
                    int distY = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionY - c.cube.y);
                    int distZ = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionZ - c.cube.z);
                    if (distX > world.realSizeX / 2) {
                        if (core.gameState.getCurrentRenderPositions().viewPositionX < world.realSizeX / 2) {
                            distX = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionX - (c.cube.x - world.realSizeX));
                        } else {
                            distX = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionX - (c.cube.x + world.realSizeX));
                        }
                    }
                    if (distZ > world.realSizeZ / 2) {
                        if (core.gameState.getCurrentRenderPositions().viewPositionZ < world.realSizeZ / 2) {
                            distZ = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionZ - (c.cube.z - world.realSizeZ));
                        } else {
                            distZ = Math.abs(core.gameState.getCurrentRenderPositions().viewPositionZ - (c.cube.z + world.realSizeZ));
                        }
                    }
                    if (distX <= checkDistCube && distY <= checkDistCube && distZ <= checkDistCube) {
                        checked = true;
                    } else {
                    }
                    boolean farviewGapFiller = false;
                    if (checked && J3DCore.SETTINGS.FARVIEW_ENABLED) {
                        int viewDistFarViewModuloX = core.gameState.getCurrentRenderPositions().viewPositionX % J3DCore.FARVIEW_GAP;
                        int viewDistFarViewModuloZ = core.gameState.getCurrentRenderPositions().viewPositionZ % J3DCore.FARVIEW_GAP;
                        if (Math.abs(checkDistCube - distX) <= viewDistFarViewModuloX) {
                            farviewGapFiller = true;
                        }
                        if (Math.abs(checkDistCube - distZ) <= viewDistFarViewModuloZ) {
                            farviewGapFiller = true;
                        }
                        if (c.cube.x % J3DCore.FARVIEW_GAP == 0 && c.cube.z % J3DCore.FARVIEW_GAP == 0) {
                        } else {
                            farviewGapFiller = false;
                        }
                    }
                    for (NodePlaceholder n : c.hsRenderedNodes) {
                        Vector3 t = n.getLocalTranslation();
                        if (checked && !farviewGapFiller) {
                            double dist = t.distanceSquared(core.getCamera().getLocation());
                            if (dist < minAngleCalc) {
                                break;
                            }
                        } else {
                            if (!J3DCore.SETTINGS.FARVIEW_ENABLED || fragmentViewDist) break;
                            double dist = t.distanceSquared(core.getCamera().getLocation());
                            if (dist > maxFarViewDist) {
                                break;
                            }
                            if (c.cube.x % J3DCore.FARVIEW_GAP == 0 && c.cube.z % J3DCore.FARVIEW_GAP == 0 && c.cube.y % J3DCore.FARVIEW_GAP == 0) {
                                if (n.model.farViewEnabled) {
                                    Vector3 relative = t.subtract(core.getCamera().getLocation(), null).normalize(null);
                                    double angle = core.getCamera().getDirection().normalize(null).smallestAngleBetween(relative);
                                    if (angle < refAngle) {
                                        foundFar = true;
                                    }
                                    break;
                                } else {
                                    continue;
                                }
                            }
                            break;
                        }
                    }
                    if (foundFar) {
                        visibleNodeCounter++;
                        if (!inFarViewPort.contains(c)) {
                            addedNodeCounter++;
                            inFarViewPort.add(c);
                            outOfFarViewPort.remove(c);
                            for (NodePlaceholder n : c.hsRenderedNodes) {
                                if (!n.model.farViewEnabled) continue;
                                n.farView = true;
                                overrideBatch = true;
                                if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                    if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                        if (n.neighborCubeData == null || n.neighborCubeData.wereNeigboursNotFullyDetected()) {
                                            n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                        }
                                        overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                    } else {
                                        overrideBatch = false;
                                    }
                                    if (!overrideBatch) {
                                        if (n.trimeshGeomBatchInstance == null && n.modelGeomBatchInstance == null) batchHelper.addItem(this, c.cube.internalCube, n.model, n, true);
                                    }
                                }
                                if (overrideBatch) {
                                    Node realPooledNode = (Node) modelPool.getModel(c, n.model, n);
                                    if (realPooledNode == null) continue;
                                    n.realNode = (PooledNode) realPooledNode;
                                    if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                        if (realPooledNode != null) batchHelper.addBillboardVegetationItem(this, c.cube.internalCube, n.model, n, true, (BillboardPartVegetation) realPooledNode);
                                    }
                                    {
                                    }
                                    realPooledNode.setTranslation(n.getLocalTranslation());
                                    if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel && n.neighborCubeData != null && n.neighborCubeData.getTextureKeyPartForBatch() != null) {
                                        realPooledNode.setTranslation(realPooledNode.getTranslation().add(blendedGeoTileDisplacement, null));
                                    }
                                    for (Spatial s : realPooledNode.getChildren()) {
                                        if (s instanceof Node) {
                                            for (Spatial s2 : ((Node) s).getChildren()) {
                                                if (s2 instanceof Node) {
                                                    for (Spatial s3 : ((Node) s2).getChildren()) {
                                                        if (s3 instanceof TrimeshGeometryBatch) {
                                                            ((TrimeshGeometryBatch) s3).horizontalRotation = n.horizontalRotation;
                                                        }
                                                    }
                                                }
                                                s2.setScale(n.getLocalScale());
                                                if (s2 instanceof TrimeshGeometryBatch) {
                                                    ((TrimeshGeometryBatch) s2).horizontalRotation = n.horizontalRotation;
                                                } else {
                                                    s2.setRotation(n.getLocalRotation());
                                                }
                                            }
                                        } else {
                                            s.setRotation(n.getLocalRotation());
                                            Vector3 scale = new Vector3(n.getLocalScale());
                                            scale.setX(scale.getX() * J3DCore.FARVIEW_GAP);
                                            scale.setZ(scale.getZ() * J3DCore.FARVIEW_GAP);
                                            s.setScale(scale);
                                        }
                                    }
                                    if (c.cube.internalCube) {
                                        intSSAONode.attachChild((Node) realPooledNode);
                                    } else {
                                        extSSAONode.attachChild((Node) realPooledNode);
                                    }
                                    realPooledNode.getSceneHints().setCullHint(CullHint.Inherit);
                                    newNodesToSetCullingDynamic.add(realPooledNode);
                                    {
                                    }
                                }
                            }
                        }
                    } else {
                        nonVisibleNodeCounter++;
                        if (!outOfFarViewPort.contains(c)) {
                            removedNodeCounter++;
                            outOfFarViewPort.add(c);
                            inFarViewPort.remove(c);
                            for (NodePlaceholder n : c.hsRenderedNodes) {
                                overrideBatch = true;
                                if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                    if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                        if (n.neighborCubeData == null) {
                                            n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                        }
                                        overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                    } else {
                                        overrideBatch = false;
                                    }
                                    if (!overrideBatch) {
                                        if (n != null) batchHelper.removeItem(c.cube.internalCube, n.model, n, n.farView);
                                    }
                                }
                                if (overrideBatch) {
                                    PooledNode pooledRealNode = n.realNode;
                                    n.realNode = null;
                                    if (pooledRealNode != null) {
                                        Node realNode = (Node) pooledRealNode;
                                        if (J3DCore.SETTINGS.SHADOWS) core.removeOccludersRecoursive(realNode);
                                        realNode.removeFromParent();
                                        modelPool.releaseNode(pooledRealNode);
                                    }
                                }
                                n.farView = false;
                            }
                        }
                    }
                }
                currentFarviewNode++;
                if (currentFarviewNode == alCurrentCubes_FARVIEW.size()) return false;
                if (System.currentTimeMillis() - time > 5) return false;
            }
        }
        long time = System.currentTimeMillis();
        while (true) {
            if (currentConditionalNode == conditionalNodes.size()) {
                countRtvpTime = System.currentTimeMillis() - countRtvpTime;
                if (countRtvpTime > maxRtvpTime) {
                    maxRtvpTime = countRtvpTime;
                }
                return true;
            }
            double dist = 0;
            NodePlaceholder cN = conditionalNodes.get(currentConditionalNode);
            {
                dist = cN.getLocalTranslation().distanceSquared(core.getCamera().getLocation());
                if (dist < J3DCore.SETTINGS.RENDER_GRASS_DISTANCE * J3DCore.SETTINGS.RENDER_GRASS_DISTANCE) {
                    if (cN.cube != null && cN.cube.cube != null && cN.trimeshGeomBatchInstance == null) {
                        batchHelper.addItem(this, cN.cube.cube.internalCube, cN.model, cN, cN.farView);
                    }
                } else {
                    if (cN.cube != null && cN.cube.cube != null && cN.trimeshGeomBatchInstance != null) {
                        batchHelper.removeItem(cN.cube.cube.internalCube, cN.model, cN, cN.farView);
                    }
                }
            }
            currentConditionalNode++;
            if (System.currentTimeMillis() - time > 5) {
                countRtvpTime = System.currentTimeMillis() - countRtvpTime;
                if (countRtvpTime > maxRtvpTime) {
                    maxRtvpTime = countRtvpTime;
                }
                return false;
            }
        }
    }

    private ArrayList<RenderedCube> toClearReferencesCubes = new ArrayList<RenderedCube>();

    /**
	 * Rendering standing nodes into viewport. Converting nodePlaceHolders to actual Nodes if they are visible. (Using modelPool.)
	 * @param refAngle
	 * @param segmented
	 * @param segmentCount
	 * @param segments
	 */
    public void renderToViewPort(float refAngle, boolean segmented, int segmentCount, int segments) {
        if (threadRendering || updateAfterRenderNeeded) {
            newRenderPending = true;
            return;
        }
        QuickOrderedList.timeCounter = 0;
        maxRtvpTime = 0;
        maxUARTime = 0;
        newRenderPending = false;
        logger.finest("######### GEOMBATCHMESH BUFFER REBUILD TIMES: PRE/COMMIT -- " + GeometryBatchMesh.preCommitTime + " " + GeometryBatchMesh.commitTime);
        GeometryBatchMesh.preCommitTime = 0;
        GeometryBatchMesh.commitTime = 0;
        synchronized (Engine.mutex) {
            if (J3DCore.GEOMETRY_BATCH) batchHelper.unlockAll();
            boolean overrideBatch = true;
            Vector3 lastLoc = new Vector3(lastRenderX * J3DCore.CUBE_EDGE_SIZE, lastRenderY * J3DCore.CUBE_EDGE_SIZE, lastRenderZ * J3DCore.CUBE_EDGE_SIZE);
            Vector3 currLoc = new Vector3(core.gameState.getCurrentRenderPositions().relativeX * J3DCore.CUBE_EDGE_SIZE, core.gameState.getCurrentRenderPositions().relativeY * J3DCore.CUBE_EDGE_SIZE, core.gameState.getCurrentRenderPositions().relativeZ * J3DCore.CUBE_EDGE_SIZE);
            int mulWalkDist = 1;
            if (J3DCore.SETTINGS.CONTINUOUS_LOAD && !rerender && !parallelLoadingHelper.isParallelRenderingRunning()) {
                if (lastLoc.distance(currLoc) * mulWalkDist * 1.5f > ((J3DCore.SETTINGS.RENDER_DISTANCE_CALC) * J3DCore.CUBE_EDGE_SIZE) - J3DCore.SETTINGS.VIEW_DISTANCE) {
                    RenderedAreaThread t = new RenderedAreaThread(this, world, core.gameState.getCurrentRenderPositions().relativeX, core.gameState.getCurrentRenderPositions().relativeY, core.gameState.getCurrentRenderPositions().relativeZ, core.gameState.getCurrentRenderPositions().viewPositionX, core.gameState.getCurrentRenderPositions().viewPositionY, core.gameState.getCurrentRenderPositions().viewPositionZ);
                    parallelLoadingHelper.runningThreads.add(t);
                    t.start();
                }
            }
            if (parallelLoadingHelper.areaResult != null) {
                uiBase.hud.sr.setVisibility(false, "LOAD");
                hmCurrentCubes = parallelLoadingHelper.getBackupCurrentCubes();
                long t0 = System.currentTimeMillis();
                HashSet<RenderedCube>[] detacheable = parallelLoadingHelper.areaResult;
                parallelLoadingHelper.areaResult = null;
                parallelLoadingHelper.endParallelRendering();
                if (true) {
                    for (int i = 0; i < detacheable.length; i++) for (RenderedCube c : detacheable[i]) {
                        if (c != null) {
                            inViewPort.remove(c);
                            inFarViewPort.remove(c);
                            outOfViewPort.remove(c);
                            for (Iterator<NodePlaceholder> itNode = c.hsRenderedNodes.iterator(); itNode.hasNext(); ) {
                                NodePlaceholder n = itNode.next();
                                overrideBatch = true;
                                if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                    if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                        if (n.neighborCubeData == null) {
                                            n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                        }
                                        overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                    } else {
                                        overrideBatch = false;
                                    }
                                    if (!overrideBatch) {
                                        if (n != null && (n.trimeshGeomBatchInstance != null || n.modelGeomBatchInstance != null)) batchHelper.removeItem(c.cube.internalCube, n.model, n, n.farView);
                                    }
                                }
                                if (overrideBatch) {
                                    PooledNode pooledRealNode = n.realNode;
                                    if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                        if (pooledRealNode != null) {
                                            batchHelper.removeBillboardVegetationItem(c.cube.internalCube, n.model, n, n.farView, (BillboardPartVegetation) pooledRealNode);
                                        }
                                    }
                                    n.realNode = null;
                                    if (pooledRealNode != null) {
                                        Node realNode = (Node) pooledRealNode;
                                        if (J3DCore.SETTINGS.SHADOWS) core.removeOccludersRecoursive(realNode);
                                        realNode.removeFromParent();
                                        modelPool.releaseNode(pooledRealNode);
                                    }
                                }
                                conditionalNodes.remove(n);
                                n.farView = false;
                            }
                        }
                        toClearReferencesCubes.add(c);
                    }
                }
                if (J3DCore.LOGGING()) Jcrpg.LOGGER.finer("DETACH TIME = " + (System.currentTimeMillis() - t0));
            } else if (rerender || lastLoc.distance(currLoc) * mulWalkDist > ((J3DCore.SETTINGS.RENDER_DISTANCE_CALC) * J3DCore.CUBE_EDGE_SIZE) - J3DCore.SETTINGS.VIEW_DISTANCE) {
                logger.fine("++++++ RERENDER : " + rerender + " DIST: " + lastLoc.distance(currLoc) * mulWalkDist);
                parallelLoadingHelper.haltAllRender();
                while (renderedArea.numberOfProcesses > 0 && numberOfProcesses > 0) {
                    Jcrpg.LOGGER.info("WAITING FOR RENDER THREADS TO STOP...");
                    System.out.println("WAITING...");
                    try {
                        Thread.sleep(5);
                    } catch (Exception ex) {
                    }
                }
                nonDrawingRender = true;
                GeometryBatchMesh.GLOBAL_CAN_COMMIT = false;
                if (rerenderWithRemove) {
                    HashSet<RenderedCube> fullInview = new HashSet<RenderedCube>();
                    fullInview.addAll(inViewPort);
                    fullInview.addAll(inFarViewPort);
                    for (RenderedCube c : fullInview) {
                        if (c != null) {
                            inViewPort.remove(c);
                            inFarViewPort.remove(c);
                            outOfViewPort.remove(c);
                            for (Iterator<NodePlaceholder> itNode = c.hsRenderedNodes.iterator(); itNode.hasNext(); ) {
                                overrideBatch = true;
                                NodePlaceholder n = itNode.next();
                                if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                    if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                        if (n.neighborCubeData == null) {
                                            n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                        }
                                        overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                    } else {
                                        overrideBatch = false;
                                    }
                                    if (!overrideBatch) {
                                        if (n != null && (n.trimeshGeomBatchInstance != null || n.modelGeomBatchInstance != null)) batchHelper.removeItem(c.cube.internalCube, n.model, n, n.farView);
                                    }
                                    if (n.model.type == Model.TEXTURESTATEVEGETATION) {
                                        conditionalNodes.remove(n);
                                    }
                                }
                                if (overrideBatch) {
                                    PooledNode pooledRealNode = n.realNode;
                                    if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                        if (pooledRealNode != null) {
                                            batchHelper.removeBillboardVegetationItem(c.cube.internalCube, n.model, n, n.farView, (BillboardPartVegetation) pooledRealNode);
                                        }
                                    }
                                    n.realNode = null;
                                    if (pooledRealNode != null) {
                                        Node realNode = (Node) pooledRealNode;
                                        if (J3DCore.SETTINGS.SHADOWS) core.removeOccludersRecoursive(realNode);
                                        realNode.removeFromParent();
                                        modelPool.releaseNode(pooledRealNode);
                                    }
                                }
                                n.farView = false;
                            }
                        }
                    }
                    batchHelper.releaseInstancesBuffersOnFullCleanUp();
                }
                long t0 = System.currentTimeMillis();
                HashSet<RenderedCube>[] detacheable = render(core.gameState.getCurrentRenderPositions().relativeX, core.gameState.getCurrentRenderPositions().relativeY, core.gameState.getCurrentRenderPositions().relativeZ, core.gameState.getCurrentRenderPositions().viewPositionX, core.gameState.getCurrentRenderPositions().viewPositionY, core.gameState.getCurrentRenderPositions().viewPositionZ, rerender, false);
                if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("DO RENDER TIME : " + (System.currentTimeMillis() - t0));
                for (int i = 0; i < detacheable.length; i++) for (RenderedCube c : detacheable[i]) {
                    if (c != null) {
                        inViewPort.remove(c);
                        inFarViewPort.remove(c);
                        outOfViewPort.remove(c);
                        for (Iterator<NodePlaceholder> itNode = c.hsRenderedNodes.iterator(); itNode.hasNext(); ) {
                            overrideBatch = true;
                            NodePlaceholder n = itNode.next();
                            if (J3DCore.GEOMETRY_BATCH && n.model.batchEnabled && (n.model.type == Model.QUADMODEL || n.model.type == Model.SIMPLEMODEL || J3DCore.GRASS_BIG_BATCH && n.model.type == Model.TEXTURESTATEVEGETATION)) {
                                if (n.model.type == Model.SIMPLEMODEL && ((SimpleModel) n.model).generatedGroundModel) {
                                    if (n.neighborCubeData == null) {
                                        n.neighborCubeData = GeoTileLoader.getNeighborCubes(n);
                                    }
                                    overrideBatch = n.neighborCubeData.getTextureKeyPartForBatch() != null;
                                } else {
                                    overrideBatch = false;
                                }
                                if (!overrideBatch) {
                                    if (n != null && (n.trimeshGeomBatchInstance != null || n.modelGeomBatchInstance != null)) batchHelper.removeItem(c.cube.internalCube, n.model, n, n.farView);
                                }
                                if (n.model.type == Model.TEXTURESTATEVEGETATION) {
                                    conditionalNodes.remove(n);
                                }
                            }
                            if (overrideBatch) {
                                PooledNode pooledRealNode = n.realNode;
                                if (n.model.type == Model.PARTLYBILLBOARDMODEL) {
                                    if (pooledRealNode != null) {
                                        batchHelper.removeBillboardVegetationItem(c.cube.internalCube, n.model, n, n.farView, (BillboardPartVegetation) pooledRealNode);
                                    }
                                }
                                n.realNode = null;
                                if (pooledRealNode != null) {
                                    Node realNode = (Node) pooledRealNode;
                                    if (J3DCore.SETTINGS.SHADOWS) core.removeOccludersRecoursive(realNode);
                                    realNode.removeFromParent();
                                    modelPool.releaseNode(pooledRealNode);
                                }
                            }
                            conditionalNodes.remove(n);
                            n.farView = false;
                        }
                    }
                    toClearReferencesCubes.add(c);
                }
            }
            long t2 = System.currentTimeMillis();
            if (segmented && segmentCount == 0 || !segmented) {
                alCurrentCubes.clear();
                alCurrentCubes.addAll(hmCurrentCubes.values());
                if (J3DCore.SETTINGS.FARVIEW_ENABLED) {
                    alCurrentCubes_FARVIEW.clear();
                    alCurrentCubes_FARVIEW.addAll(hmCurrentCubes_FARVIEW.values());
                }
                previousContinuousSoundsAndDistance = continuousSoundsAndDistance;
                continuousSoundsAndDistance = new HashMap<String, Double>();
            }
            if (segmented) {
                int sSize = alCurrentCubes.size() / segments;
                fromCubeCount = sSize * segmentCount;
                toCubeCount = sSize * (segmentCount + 1);
                if (toCubeCount > alCurrentCubes.size()) {
                    toCubeCount = alCurrentCubes.size();
                }
            }
            if (segmented && J3DCore.SETTINGS.FARVIEW_ENABLED) {
                int sSize = alCurrentCubes_FARVIEW.size() / segments;
                fromCubeCount_FARVIEW = sSize * segmentCount;
                toCubeCount_FARVIEW = sSize * (segmentCount + 1);
                if (toCubeCount_FARVIEW > alCurrentCubes_FARVIEW.size()) {
                    toCubeCount_FARVIEW = alCurrentCubes_FARVIEW.size();
                }
            }
            if (J3DCore.LOGGING()) Jcrpg.LOGGER.finer("ARRAY COPIES = " + (System.currentTimeMillis() - t2));
            ModelGeometryBatch.sumBuildMatricesTime = 0;
            TrimeshGeometryBatch.sumAddItemReal = 0;
            currentConditionalNode = 0;
            currentNode = 0;
            currentFarviewNode = 0;
            batchHelperLocksFinished = false;
            batchHelperUpdatesFinished = false;
            batchHelperRemovalsFinished = false;
            batchHelper.modelStepByStepCounter = 0;
            batchHelper.trimeshStepByStepCounter = 0;
            maxBatchLockTime = 0;
            maxBatchUpdateTime = 0;
            threadRendering = true;
        }
    }

    public static long maxUARTime = 0;

    public static long maxBatchUpdateTime = 0;

    public static long maxBatchLockTime = 0;

    boolean batchHelperRemovalsFinished = false;

    boolean batchHelperUpdatesFinished = false;

    boolean batchHelperLocksFinished = false;

    /**
	 * updates after render complition - gradually done, j3dcore should call this between draws until
	 * finished.
	 */
    @SuppressWarnings("unchecked")
    public void updateAfterRender() {
        long sysTime = System.currentTimeMillis();
        if (J3DCore.GEOMETRY_BATCH) {
            if (!batchHelperRemovalsFinished) {
                batchHelperRemovalsFinished = batchHelper.removeUnneededStepByStep();
                if (batchHelperRemovalsFinished) {
                    batchHelper.trimeshStepByStepCounter = 0;
                    batchHelper.modelStepByStepCounter = 0;
                }
                return;
            }
        }
        if (newNodesToSetCullingDynamic.size() > 0) {
            long time = System.currentTimeMillis();
            while (true) {
                Node n = newNodesToSetCullingDynamic.remove(0);
                n.getSceneHints().setCullHint(CullHint.Inherit);
                if (n.getChildren() != null && n.getChildren().size() == 1) {
                    Spatial s = n.getChild(0);
                    if (s instanceof Node) {
                        s = ((Node) s).getChild(0);
                    }
                    if (s instanceof GeometryBatchMesh) {
                        ((GeometryBatchMesh) s).rebuild();
                    }
                }
                if (newNodesToSetCullingDynamic.size() == 0) break;
                if (System.currentTimeMillis() - time > 5) {
                    break;
                }
            }
            sysTime = System.currentTimeMillis() - sysTime;
            if (sysTime > maxUARTime) {
                maxUARTime = sysTime;
            }
            return;
        }
        if (J3DCore.GEOMETRY_BATCH) {
            if (!batchHelperUpdatesFinished) {
                long bTime = System.currentTimeMillis();
                batchHelperUpdatesFinished = batchHelper.updateAllStepByStep();
                if (batchHelperUpdatesFinished) {
                    batchHelper.trimeshStepByStepCounter = 0;
                    batchHelper.modelStepByStepCounter = 0;
                }
                bTime = System.currentTimeMillis() - bTime;
                if (bTime > maxBatchUpdateTime) {
                    maxBatchUpdateTime = bTime;
                }
                return;
            }
        }
        GeometryBatchMesh.GLOBAL_CAN_COMMIT = true;
        if (J3DCore.GEOMETRY_BATCH) {
            if (!batchHelperLocksFinished) {
                long bTime = System.currentTimeMillis();
                batchHelperLocksFinished = batchHelper.lockAllStepByStep();
                if (batchHelperLocksFinished) {
                    batchHelper.trimeshStepByStepCounter = 0;
                    batchHelper.modelStepByStepCounter = 0;
                }
                bTime = System.currentTimeMillis() - bTime;
                if (bTime > maxBatchLockTime) {
                    maxBatchLockTime = bTime;
                }
                return;
            }
        }
        long finalTime = System.currentTimeMillis();
        cullVariationCounter++;
        core.updateTimeRelated();
        double dist = 0;
        if (J3DCore.SETTINGS.SOUND_ENABLED) {
            if (continuousSoundsAndDistance.size() > 0) {
                for (String key : continuousSoundsAndDistance.keySet()) {
                    dist = continuousSoundsAndDistance.get(key);
                    if (previousContinuousSoundsAndDistance.containsKey(key)) {
                        previousContinuousSoundsAndDistance.remove(key);
                    } else {
                    }
                    double power = Math.min(1f, 1f / (dist + 0.5f) * 10f);
                    if (power > 0.05f) {
                        if (core.audioServer != null) core.audioServer.playContinuousLoading(key, "continuous", power);
                    } else {
                        previousContinuousSoundsAndDistance.put(key, 0d);
                    }
                }
            }
            if (previousContinuousSoundsAndDistance.size() > 0) {
                for (String key : previousContinuousSoundsAndDistance.keySet()) {
                    if (core.audioServer != null) core.audioServer.fadeOut(key);
                }
            }
        }
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("CAMERA: " + core.getCamera().getLocation() + " NODES EXT: " + (extRootNode.getChildren() == null ? "-" : extRootNode.getChildren().size()));
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("crootnode cull update time: " + (System.currentTimeMillis() - sysTime));
        if (J3DCore.LOGGING()) logger.finest("crootnode cull update time: " + (System.currentTimeMillis() - sysTime));
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.info("hmSolidColorSpatials:" + J3DCore.hmSolidColorSpatials.size());
        if (cullVariationCounter % 40 == 0) {
            modelPool.cleanPools();
        }
        ArrayList<RenderedCube> cleared = new ArrayList<RenderedCube>();
        for (RenderedCube c : toClearReferencesCubes) {
            RenderedCubePool.release(c);
            cleared.add(c);
        }
        toClearReferencesCubes.removeAll(cleared);
        cleared.clear();
        core.garbCollCounter++;
        if (core.garbCollCounter == 20) {
            core.garbCollCounter = 0;
        }
        newNodesToSetCullingDynamic.clear();
        threadRendering = false;
        updateAfterRenderNeeded = false;
        nonDrawingRender = false;
        if (J3DCore.LOGGING()) logger.finest("???  CACHE LOOKUP ??? = " + QuickOrderedList.timeCounter);
        if (J3DCore.LOGGING()) logger.finest("MAX RTVP / UAR / BTCHUPD / LOCK TIME = " + maxRtvpTime + " " + maxUARTime + " " + maxBatchUpdateTime + " " + maxBatchLockTime);
        if (J3DCore.LOGGING()) logger.finest("FINAL things          = " + (System.currentTimeMillis() - finalTime));
        if (J3DCore.LOGGING()) logger.finest("BUFFER / EX BUFF POOL: CACHED   # " + BufferPool.v3BuffCacheSize + " ALL " + BufferPool.v3BuffCount + " # " + ExactBufferPool.v3BuffCacheSize + " ALL " + ExactBufferPool.v3BuffCount);
        QuickOrderedList.timeCounter = 0;
        if (core.firstRender) {
            core.getRootNode1().markDirty(DirtyType.RenderState);
            core.getRootNode1().updateGeometricState(0);
            core.firstRender = false;
        }
        core.busyPane.hide();
        core.initializationFinished();
    }

    private void renderNodes(NodePlaceholder[] n, RenderedCube cube, int relX, int relY, int relZ, int x, int y, int z, int direction) {
        renderNodes(n, cube, relX, relY, relZ, x, y, z, direction, -1, 1f);
    }

    public int liveNodes = 0;

    /**
	 * Renders one side into 3d space percepting what kind of RenderedSide it is.
	 * @param cube
	 * @param x
	 * @param y
	 * @param z
	 * @param direction
	 * @param side
	 * @param fakeLoadForCacheMaint No true rendering if this is true, only fake loading the objects through model loader.
	 */
    public void renderSide(RenderedCube cube, int relX, int relY, int relZ, int x, int y, int z, int direction, Side side, boolean fakeLoadForCacheMaint) {
        if (side == null || side.subtype == null) return;
        Integer n3dType = core.hmCubeSideSubTypeToRenderedSideId.get(side.subtype.id);
        if (n3dType == null) return;
        if (n3dType.equals(J3DCore.EMPTY_SIDE)) return;
        RenderedSide renderedSide = core.hm3dTypeRenderedSide.get(n3dType);
        NodePlaceholder[] n = modelPool.loadPlaceHolderObjects(cube, renderedSide.objects, fakeLoadForCacheMaint);
        cube.hmNodePlaceholderForSide.put(side, n);
        if (!fakeLoadForCacheMaint) {
            if (renderedSide.type == RenderedSide.RS_HASHROTATED) {
                int rD = ((RenderedHashRotatedSide) renderedSide).rotation(cube.cube.x, cube.cube.y, cube.cube.z);
                float scale = ((RenderedHashRotatedSide) renderedSide).scale(cube.cube.x, cube.cube.y, cube.cube.z);
                renderNodes(n, cube, relX, relY, relZ, x, y, z, direction, rD, scale);
            } else if (renderedSide.type == RenderedSide.RS_HASHALTERED) {
                renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                Model[] m = ((RenderedHashAlteredSide) renderedSide).getRenderedModels(cube.cube.x, cube.cube.y, cube.cube.z, cube.cube.steepDirection != SurfaceHeightAndType.NOT_STEEP);
                NodePlaceholder[] n2 = modelPool.loadPlaceHolderObjects(cube, m, fakeLoadForCacheMaint);
                if (n2.length > 0) renderNodes(n2, cube, relX, relY, relZ, x, y, z, direction);
            } else if (renderedSide.type == RenderedSide.RS_CLIMATEDEPENDENT) {
                renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                Model[] m = ((RenderedClimateDependentSide) renderedSide).getRenderedModels(cube.cube.climateId);
                if (m != null) {
                    NodePlaceholder[] n2 = modelPool.loadPlaceHolderObjects(cube, m, fakeLoadForCacheMaint);
                    if (n2.length > 0) renderNodes(n2, cube, relX, relY, relZ, x, y, z, direction);
                }
            } else {
                renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
            }
        }
        Cube checkCube = null;
        if (direction == J3DCore.TOP && renderedSide.type == RenderedSide.RS_TOPSIDE) {
            if (cube.cube.getNeighbour(J3DCore.TOP) != null) {
                if (cube.cube.getNeighbour(J3DCore.TOP).hasSideOfType(direction, side.type)) {
                    return;
                }
            } else {
            }
            boolean render = true;
            for (int i = J3DCore.NORTH; i <= J3DCore.WEST; i++) {
                Cube n1 = cube.cube.getNeighbour(i);
                if (n1 != null) {
                    if (n1.hasSideOfType(i, side.type) || n1.hasSideOfType(J3DCore.oppositeDirections.get(i), side.type)) {
                        render = false;
                        break;
                    }
                }
            }
            if (render) {
                n = modelPool.loadPlaceHolderObjects(cube, ((RenderedTopSide) renderedSide).nonEdgeObjects, fakeLoadForCacheMaint);
                if (!fakeLoadForCacheMaint) {
                    renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                }
            }
        }
        if (direction != J3DCore.TOP && direction != J3DCore.BOTTOM && renderedSide.type == RenderedSide.RS_CONTINUOUS) {
            int dir = J3DCore.nextDirections.get(direction);
            if (cube.cube.getNeighbour(J3DCore.TOP) != null) {
                if (cube.cube.getNeighbour(J3DCore.TOP).hasSideOfType(direction, side.type)) {
                    return;
                }
            }
            if (cube.cube.getNeighbour(dir) != null) if (cube.cube.getNeighbour(dir).hasSideOfType(direction, side.type)) {
                checkCube = cube.cube.getNeighbour(J3DCore.oppositeDirections.get(dir));
                if (checkCube != null) {
                    if (checkCube.hasSideOfType(direction, side.type)) {
                        n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).continuous, fakeLoadForCacheMaint);
                        if (!fakeLoadForCacheMaint) {
                            renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                        }
                    } else {
                        n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).oneSideContinuousNormal, fakeLoadForCacheMaint);
                        if (!fakeLoadForCacheMaint) {
                            renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                        }
                    }
                } else {
                    n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).oneSideContinuousNormal, fakeLoadForCacheMaint);
                    if (!fakeLoadForCacheMaint) {
                        renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                    }
                }
            } else {
                checkCube = cube.cube.getNeighbour(J3DCore.oppositeDirections.get(dir));
                if (checkCube != null) {
                    if (checkCube.hasSideOfType(direction, side.type)) {
                        n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).oneSideContinuousOpposite, fakeLoadForCacheMaint);
                        if (!fakeLoadForCacheMaint) renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                    } else {
                        n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).nonContinuous, fakeLoadForCacheMaint);
                        if (!fakeLoadForCacheMaint) renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                    }
                } else {
                    n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).oneSideContinuousOpposite, fakeLoadForCacheMaint);
                    if (!fakeLoadForCacheMaint) renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
                }
            } else {
                n = modelPool.loadPlaceHolderObjects(cube, ((RenderedContinuousSide) renderedSide).oneSideContinuousOpposite, fakeLoadForCacheMaint);
                if (!fakeLoadForCacheMaint) renderNodes(n, cube, relX, relY, relZ, x, y, z, direction);
            }
        }
    }

    public void backupCurrentCubesForSafeRender() {
        parallelLoadingHelper.backupCurrentCubesForSafeRender(hmCurrentCubes);
    }

    public void clearAll() {
        world = null;
        for (RenderedCube c : hmCurrentCubes.values()) {
            c.clear();
            RenderedCubePool.release(c);
        }
        hmCurrentCubes.clear();
        hmCurrentCubes_FARVIEW.clear();
        for (RenderedCube c : hmCurrentCubes_FARVIEW.values()) {
            RenderedCubePool.release(c);
        }
        parallelLoadingHelper.getBackupCurrentCubes().clear();
        alCurrentCubes.clear();
        alCurrentCubes_FARVIEW.clear();
        inFarViewPort.clear();
        inViewPort.clear();
        outOfViewPort.clear();
        outOfFarViewPort.clear();
        conditionalNodes.clear();
    }

    public void switchOn(boolean on) {
        if (on) {
            extRootNode.getSceneHints().setCullHint(CullHint.Dynamic);
            intRootNode.getSceneHints().setCullHint(CullHint.Dynamic);
            extWaterRefNode.getSceneHints().setCullHint(CullHint.Dynamic);
            intWaterRefNode.getSceneHints().setCullHint(CullHint.Dynamic);
            extSSAONode.getSceneHints().setCullHint(CullHint.Dynamic);
            intSSAONode.getSceneHints().setCullHint(CullHint.Dynamic);
        } else {
            extRootNode.getSceneHints().setCullHint(CullHint.Always);
            intRootNode.getSceneHints().setCullHint(CullHint.Always);
            extWaterRefNode.getSceneHints().setCullHint(CullHint.Always);
            intWaterRefNode.getSceneHints().setCullHint(CullHint.Always);
            extSSAONode.getSceneHints().setCullHint(CullHint.Always);
            intSSAONode.getSceneHints().setCullHint(CullHint.Always);
        }
    }

    public String toString() {
        return "J3DSE hmC " + hmCurrentCubes.size() + " - alC " + alCurrentCubes.size() + " - inVP " + inViewPort.size() + " - outVP " + outOfViewPort.size() + " - condNodes " + conditionalNodes.size();
    }
}
