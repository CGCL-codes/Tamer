package skycastle.gamerenderer.terrain;

import com.jme.image.Texture;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.TextureState;
import skycastle.gamerenderer.LightingSettings;
import skycastle.gamerenderer.TerrainUtils;
import skycastle.gamerenderer.renderer.GameRendererContext;
import skycastle.jmeutils.ShaderUtils;
import skycastle.util.ParameterChecker;
import skycastle.util.buffer.FloatBufferDataProvider;
import skycastle.util.buffer.LocatedFloatBuffer2D;
import skycastle.util.buffer.LocatedFloatBuffer2DImpl;
import skycastle.util.grid.GridSnapper;
import skycastle.util.region.RegionOfInterest;
import java.util.HashMap;

/**
 * A section of terrain.  Keeps track of the 3D terrain as well as the float buffer with the data and the generator
 * function.
 * <p/>
 * Can be updated.
 *
 * @author Hans H�ggstr�m
 */
public class TerrainChunk {

    private final LocatedFloatBuffer2D myHeightMap;

    private final TerrainChunk myParentChunk;

    private final int myChunkIndex;

    private TerrainTextureProvider myTerrainTextureProvider = new SimpleTerrainTextureProvider();

    private FloatBufferDataProvider myFloatBufferDataProvider;

    private TriMesh myTriMesh = null;

    private boolean myIsHollow = false;

    private boolean myIsVisible;

    private GridSnapper myEdgeGridSnapper;

    private GridSnapper myHoleGridSnapper;

    private float myGridSizeX_m;

    private float myGridSizeZ_m;

    private GLSLShaderObjectsState myTerrainShader;

    private TextureState myTextureState;

    private LightingSettings myLightingSettings;

    private boolean myHeightmapValuesInitiailzed = false;

    private static final String TERRAIN_SHADER_PATH = "skycastle/gamerenderer/terrain/terrain_shader";

    /**
     * @param sizeAcross_grids
     * @param region
     * @param floatBufferDataProvider
     * @param hollow
     * @param parentChunk             the chunk that is one step larger than the current one, or null if there is none.
     *                                Used for smoothing the seams.
     * @param chunkIndex
     * @param lightingSettings
     */
    public TerrainChunk(final int sizeAcross_grids, final RegionOfInterest region, FloatBufferDataProvider floatBufferDataProvider, boolean hollow, final TerrainChunk parentChunk, final int chunkIndex, final LightingSettings lightingSettings) {
        ParameterChecker.checkNotNull(region, "region");
        ParameterChecker.checkNotNull(floatBufferDataProvider, "floatBufferDataProvider");
        ParameterChecker.checkIntegerInRange(sizeAcross_grids, "sizeAcross_grids", 1, Integer.MAX_VALUE);
        ParameterChecker.checkNotNull(lightingSettings, "lightingSettings");
        myChunkIndex = chunkIndex;
        myParentChunk = parentChunk;
        myIsHollow = hollow;
        myFloatBufferDataProvider = floatBufferDataProvider;
        myLightingSettings = lightingSettings;
        myHeightMap = new LocatedFloatBuffer2DImpl(sizeAcross_grids, sizeAcross_grids, region);
        myGridSizeX_m = (region.getWidth_m() / (1.0f * (sizeAcross_grids - 1)));
        myGridSizeZ_m = (region.getDepth_m() / (1.0f * (sizeAcross_grids - 1)));
        myEdgeGridSnapper = new GridSnapper(myGridSizeX_m * 2f, myGridSizeZ_m * 2f, 0, 0, 0.7f);
        myHoleGridSnapper = new GridSnapper(myGridSizeX_m, myGridSizeZ_m, 0, 0, 0.7f);
        update();
    }

    public Spatial get3DModel(final GameRendererContext context) {
        if (myTriMesh == null) {
            myTriMesh = TerrainUtils.createTerrainMesh(myHeightMap.getRegion(), myHeightMap.getWidth_grids(), myHeightMap.getDepth_grids(), myIsHollow);
            update();
            initTextureStates(context);
        }
        return myTriMesh;
    }

    /**
     * Updates the terrain with the float data buffer provider.
     */
    public final void update() {
    }

    public void update(final float secondsSinceLastCall, final GameRendererContext context) {
    }

    /**
     * Sets the center location, and updates the terrain chunk to show the content at the specified location.
     *
     * @param x
     * @param z
     * @param visible
     */
    public void updateCenter(final float x, final float z, boolean hollow, final boolean visible) {
        boolean updateIndexes = false;
        if (myIsVisible != visible) {
            myIsVisible = visible;
            updateIndexes = true;
            myHeightmapValuesInitiailzed = false;
        }
        if (myIsHollow != hollow) {
            myIsHollow = hollow;
            updateIndexes = true;
            myHeightmapValuesInitiailzed = false;
        }
        int previousEdgeGridX = myEdgeGridSnapper.getGridX();
        int previousEdgeGridZ = myEdgeGridSnapper.getGridZ();
        final boolean holeMoved = myHoleGridSnapper.updatePosition(x, z);
        final boolean edgeMoved = myEdgeGridSnapper.updatePosition(x, z);
        if (edgeMoved || holeMoved) {
            updateIndexes = true;
        }
        int gridSnapOffsetX = myEdgeGridSnapper.getGridX() - previousEdgeGridX;
        int gridSnapOffsetZ = myEdgeGridSnapper.getGridZ() - previousEdgeGridZ;
        int holeOffsetX = myEdgeGridSnapper.getGridX() * 2 - myHoleGridSnapper.getGridX();
        int holeOffsetZ = myEdgeGridSnapper.getGridZ() * 2 - myHoleGridSnapper.getGridZ();
        final float snappedX = myEdgeGridSnapper.getWorldX();
        final float snappedZ = myEdgeGridSnapper.getWorldZ();
        if (updateIndexes) {
            System.out.println("gridSnapOffsetX = " + gridSnapOffsetX + "   gridSnapOffsetZ = " + gridSnapOffsetZ);
            updateHeightMapValues(snappedX, snappedZ, gridSnapOffsetX, gridSnapOffsetZ, myHeightMap, myFloatBufferDataProvider);
            TerrainUtils.updateMeshIndexes(myTriMesh, hollow, visible, myHeightMap.getWidth_grids(), myHeightMap.getDepth_grids(), holeOffsetX, holeOffsetZ);
            updateTextureCoordinatesOfMesh(snappedX, snappedZ);
            updateTerrainHeightOfMesh(holeOffsetX, holeOffsetZ);
            myTriMesh.updateRenderState();
            myTriMesh.updateGeometricState(0, true);
        }
    }

    public float getResolution_m() {
        return myHeightMap.getResolution_m();
    }

    public float getCenterHeight() {
        return myHeightMap.getValueAtFractionalGridCoordinates(myHeightMap.getWidth_grids() * 0.5f, myHeightMap.getDepth_grids() * 0.5f);
    }

    protected LocatedFloatBuffer2D getHeightMap() {
        return myHeightMap;
    }

    private void updateHeightMapValues(final float snappedX, final float snappedZ, final int gridSnapOffsetX, final int gridSnapOffsetZ, final LocatedFloatBuffer2D heightMap, final FloatBufferDataProvider floatBufferDataProvider) {
        heightMap.setCenter(snappedX, snappedZ);
        if (myHeightmapValuesInitiailzed) {
            heightMap.offsetBufferRotationOffsets(gridSnapOffsetX, gridSnapOffsetZ);
            floatBufferDataProvider.getValues(heightMap, gridSnapOffsetX, gridSnapOffsetZ);
        } else {
            floatBufferDataProvider.getValues(heightMap);
            myHeightmapValuesInitiailzed = true;
        }
    }

    private void initTextureStates(final GameRendererContext context) {
        myTextureState = context.getTextureService().createTextureState();
        myTriMesh.setRenderState(myTextureState);
        Texture ground = myTerrainTextureProvider.getGroundTexture(context);
        Texture ground2 = myTerrainTextureProvider.getSecondaryGroundTexture(context);
        myTextureState.setTexture(ground, 0);
        myTextureState.setTexture(ground2, 1);
        final HashMap<String, Object> shaderParameters = new HashMap<String, Object>();
        shaderParameters.putAll(myLightingSettings.getEnvironmentShaderParameters());
        shaderParameters.put("currentTexture", 0);
        shaderParameters.put("parentTexture", 1);
        shaderParameters.put("worldWidth_m", myHeightMap.getMaxX() - myHeightMap.getMinX());
        shaderParameters.put("worldDepth_m", myHeightMap.getMaxZ() - myHeightMap.getMinZ());
        myTerrainShader = ShaderUtils.createShader(context.getDisplaySystem(), TERRAIN_SHADER_PATH, shaderParameters);
        myTriMesh.setRenderState(myTerrainShader);
        myTriMesh.updateRenderState();
    }

    private void updateTerrainHeightOfMesh(final int holeOffsetX, final int holeOffsetZ) {
        LocatedFloatBuffer2D parentBuffer = null;
        if (myParentChunk != null) {
            parentBuffer = myParentChunk.getHeightMap();
        }
        if (myTriMesh != null) {
            TerrainUtils.updateTerrainHeight(myTriMesh, myHeightMap, parentBuffer, myIsHollow, holeOffsetX, holeOffsetZ);
        }
    }

    private void updateTextureCoordinatesOfMesh(final float x, final float z) {
        TerrainUtils.updateTextureOffset(myTriMesh, x, z, myHeightMap.getWidth_grids(), myHeightMap.getDepth_grids(), myHeightMap.getRegion().getWidth_m(), myHeightMap.getRegion().getDepth_m());
    }
}
