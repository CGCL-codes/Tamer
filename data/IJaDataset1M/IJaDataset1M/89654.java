package org.jcrpg.world.place.geography.sub;

import org.jcrpg.space.Cube;
import org.jcrpg.space.Side;
import org.jcrpg.space.sidetype.Climbing;
import org.jcrpg.space.sidetype.GroundSubType;
import org.jcrpg.space.sidetype.NotPassable;
import org.jcrpg.space.sidetype.SideSubType;
import org.jcrpg.util.HashUtil;
import org.jcrpg.world.ai.MusicDescription;
import org.jcrpg.world.generator.program.algorithm.GenAlgoAdd;
import org.jcrpg.world.generator.program.algorithm.GenAlgoAddParams;
import org.jcrpg.world.place.Geography;
import org.jcrpg.world.place.Place;
import org.jcrpg.world.place.PlaceLocator;
import org.jcrpg.world.place.Surface;
import org.jcrpg.world.place.SurfaceHeightAndType;
import org.jcrpg.world.place.World;

public class Cave extends Geography implements Surface {

    public static int LIMIT_NORTH = 1;

    public static int LIMIT_EAST = 2;

    public static int LIMIT_SOUTH = 4;

    public static int LIMIT_WEST = 8;

    public static final String TYPE_CAVE = "CAVE";

    public static final SideSubType SUBTYPE_STEEP = new Climbing(TYPE_CAVE + "_GROUND_STEEP");

    public static final SideSubType SUBTYPE_ROCK = new NotPassable(TYPE_CAVE + "_ROCK");

    public static final SideSubType SUBTYPE_BLOCK = new NotPassable(TYPE_CAVE + "_BLOCK");

    public static final SideSubType SUBTYPE_BLOCK_GROUND = new GroundSubType(TYPE_CAVE + "_BLOCK_GROUND");

    public static final SideSubType SUBTYPE_WALL = new NotPassable(TYPE_CAVE + "_WALL");

    public static final SideSubType SUBTYPE_WALL_REVERSE = new NotPassable(TYPE_CAVE + "_WALL_REVERSE");

    public static final SideSubType SUBTYPE_GROUND = new GroundSubType(TYPE_CAVE + "_GROUND", true);

    public static final SideSubType SUBTYPE_ENTRANCE = new SideSubType(TYPE_CAVE + "_ENTRANCE", true);

    static Side[] ROCK = { new Side(TYPE_CAVE, SUBTYPE_ROCK) };

    static Side[] BLOCK = { new Side(TYPE_CAVE, SUBTYPE_BLOCK) };

    static Side[] BLOCK_GROUND = { new Side(TYPE_CAVE, SUBTYPE_BLOCK_GROUND) };

    static Side[] GROUND = { new Side(TYPE_CAVE, SUBTYPE_GROUND) };

    static Side[] ENTRANCE_GROUND = { new Side(TYPE_CAVE, SUBTYPE_GROUND), new Side(TYPE_GEO, SUBTYPE_ROCK_DOWNSIDE) };

    static Side[] WALL = { new Side(TYPE_CAVE, SUBTYPE_WALL) };

    static Side[] WALL_REVERSE = { new Side(TYPE_CAVE, SUBTYPE_WALL_REVERSE) };

    static Side[] ENTRANCE = { new Side(TYPE_CAVE, SUBTYPE_ENTRANCE) };

    static Side[][] CAVE_GROUND = new Side[][] { null, null, null, null, null, GROUND };

    static Side[][] CAVE_CEILING = new Side[][] { null, null, null, null, WALL, null };

    static Side[][] CAVE_GROUND_CEILING = new Side[][] { null, null, null, null, WALL, GROUND };

    static Side[][] CAVE_NORTH = new Side[][] { WALL_REVERSE, null, null, null, null, null };

    static Side[][] CAVE_EAST = new Side[][] { null, WALL_REVERSE, null, null, null, null };

    static Side[][] CAVE_SOUTH = new Side[][] { null, null, WALL_REVERSE, null, null, null };

    static Side[][] CAVE_WEST = new Side[][] { null, null, null, WALL_REVERSE, null, null };

    static Side[][] CAVE_ROCK = new Side[][] { BLOCK, BLOCK, BLOCK, BLOCK, ROCK, null };

    static Side[][] CAVE_ROCK_NO_MODEL = new Side[][] { BLOCK, BLOCK, BLOCK, BLOCK, BLOCK_GROUND, null };

    static Side[][] CAVE_ENTRANCE_NORTH = new Side[][] { ENTRANCE, BLOCK, EMPTY_SIDE, BLOCK, BLOCK_GROUND, ENTRANCE_GROUND };

    static Side[][] CAVE_ENTRANCE_EAST = new Side[][] { BLOCK, ENTRANCE, BLOCK, EMPTY_SIDE, BLOCK_GROUND, ENTRANCE_GROUND };

    static Side[][] CAVE_ENTRANCE_SOUTH = new Side[][] { EMPTY_SIDE, BLOCK, ENTRANCE, BLOCK, BLOCK_GROUND, ENTRANCE_GROUND };

    static Side[][] CAVE_ENTRANCE_WEST = new Side[][] { BLOCK, EMPTY_SIDE, BLOCK, ENTRANCE, BLOCK_GROUND, ENTRANCE_GROUND };

    public int density, entranceSide, levelSize;

    public int maxLevels = 1;

    /**
	 * If you want an all internal cubed cave set this to true - used for encoutner ground.
	 */
    public boolean alwaysInsideCubesForEncounterGround = false;

    public Cave(String id, Place parent, PlaceLocator loc, int worldGroundLevel, int worldHeight, int magnification, int sizeX, int sizeY, int sizeZ, int origoX, int origoY, int origoZ, int density, int entranceSide, int levelSize, boolean fillBoundaries) throws Exception {
        super(id, parent, loc, worldGroundLevel, worldHeight, magnification, sizeX, sizeY, sizeZ, origoX, origoY, origoZ, fillBoundaries);
        ruleSet.presentWhereBaseExists = false;
        ruleSet.genType = GenAlgoAdd.GEN_TYPE_NAME;
        ruleSet.genParams = new Object[] { new GenAlgoAddParams(new String[] { "Mountain" }, 100, new int[] { 0 }) };
        this.density = density;
        this.entranceSide = entranceSide;
        this.levelSize = levelSize;
        returnsGeoOutsideHeight = false;
        placeNeedsToBeEnteredForEncounter = true;
        audioDescriptor.ENVIRONMENTAL = new String[] { "cave_drip1", "cave_drip2", "cave_drip3", "cave_wind1" };
    }

    @Override
    public Cube getCube(long key, int worldX, int worldY, int worldZ, boolean farView) {
        Cube c = getCubeBase(key, worldX, worldY, worldZ, farView);
        if (c == null) {
            return null;
        }
        if (c.overwritePower > 0) {
            c.overwrite = true;
        }
        if (c.overwritePower != 2) {
            c.internalCube = true;
            c.internalLight = true;
        }
        return c;
    }

    public int ENTRANCE_DISTANCE = 8;

    public int ENTRANCE_LEVEL = 0;

    private Cube getCubeBase(long key, int worldX, int worldY, int worldZ, boolean farView) {
        if (worldY >= worldHeight || worldY >= worldGroundLevel + maxLevels * levelSize) return null;
        int[] values = calculateTransformedCoordinates(worldX, worldY, worldZ);
        int realSizeX = values[0];
        int realSizeZ = values[2];
        int relX = values[3];
        int relY = values[4];
        int relZ = values[5];
        if (relX < 0 || relZ < 0 || relY < 0 || relX > realSizeX || relZ > realSizeZ) {
            return null;
        }
        int height = (int) getPointHeightOutside(worldX, worldZ, farView);
        int FARVIEW_GAP = 1;
        int inTheCaveHeight = height + ((World) getRoot()).worldGroundLevel - worldGroundLevel;
        boolean entranceOverwrite = false;
        int tmpWorldX = 0;
        int tmpWorldZ = 0;
        for (int i = -5; i < 6; i++) {
            tmpWorldX = worldX + i;
            tmpWorldZ = worldZ + i;
            tmpWorldX = ((World) getRoot()).shrinkToWorld(tmpWorldX);
            tmpWorldZ = ((World) getRoot()).shrinkToWorld(tmpWorldZ);
            int tmpHeightX = (int) getPointHeightOutside(tmpWorldX, worldZ, farView);
            int tmpHeightZ = (int) getPointHeightOutside(worldX, tmpWorldZ, farView);
            if (Math.abs(i) == 1 && true == false) {
                if ((relZ % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel + 1 && tmpHeightX + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel + 1) {
                    Cube c = null;
                    int kind = (int) getCubeKindOutside(-1, tmpWorldX, worldY, worldZ, farView)[4];
                    if ((kind == K_STEEP_WEST || kind == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                        c = new Cube(this, CAVE_GROUND, worldX, worldY, worldZ);
                        c.overwritePower = 2;
                    }
                    if (c != null) {
                        return inTheCaveHeight >= 0 ? c : null;
                    }
                }
                if ((relZ % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel + 1 && tmpHeightX + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel + 1 + 1) {
                    Cube c = null;
                    int kind = (int) getCubeKindOutside(-1, tmpWorldX, worldY, worldZ, farView)[4];
                    if ((kind == K_STEEP_WEST || kind == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                        c = new Cube(this, CAVE_GROUND, worldX, worldY, worldZ);
                        c.overwritePower = 2;
                    }
                    if (c != null) {
                        return inTheCaveHeight >= 0 ? c : null;
                    }
                }
                if ((relX % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel + 1 && tmpHeightZ + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel + 1) {
                    int kind = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ, farView)[4];
                    Cube c = null;
                    if ((kind == K_STEEP_NORTH || kind == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                        c = new Cube(this, CAVE_GROUND, worldX, worldY, worldZ);
                        c.overwritePower = 2;
                    }
                    if (c != null) {
                        return inTheCaveHeight >= 0 ? c : null;
                    }
                }
                if ((relX % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel + 1 && tmpHeightZ + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel + 1 + 1) {
                    int kind = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ, farView)[4];
                    Cube c = null;
                    if ((kind == K_STEEP_NORTH || kind == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                        c = new Cube(this, CAVE_GROUND, worldX, worldY, worldZ);
                        c.overwritePower = 2;
                    }
                    if (c != null) {
                        return inTheCaveHeight >= 0 ? c : null;
                    }
                }
            }
            if ((relZ % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel && tmpHeightX + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel) {
                int kind = (int) getCubeKindOutside(-1, tmpWorldX, worldY, worldZ, farView)[4];
                int kindNext = (int) getCubeKindOutside(-1, tmpWorldX + 1, worldY, worldZ, farView)[4];
                int kindPrev = (int) getCubeKindOutside(-1, tmpWorldX - 1, worldY, worldZ, farView)[4];
                Cube c = null;
                if ((kind == K_STEEP_WEST || kind == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                    c = new Cube(this, CAVE_ENTRANCE_EAST, worldX, worldY, worldZ);
                    c.overwritePower = 2;
                }
                if ((kindNext == K_STEEP_WEST || kindNext == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                    entranceOverwrite = true;
                }
                if ((kindPrev == K_STEEP_WEST || kindPrev == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                    entranceOverwrite = true;
                }
                if (c != null) {
                    c.internalLight = true;
                    return inTheCaveHeight >= 0 ? c : null;
                }
            }
            if ((relZ % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel && tmpHeightX + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel + 1) {
                int kind = (int) getCubeKindOutside(-1, tmpWorldX, worldY, worldZ, farView)[4];
                int kindNext = (int) getCubeKindOutside(-1, tmpWorldX + 1, worldY, worldZ, farView)[4];
                int kindPrev = (int) getCubeKindOutside(-1, tmpWorldX - 1, worldY, worldZ, farView)[4];
                Cube c = null;
                if ((kind == K_STEEP_WEST || kind == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                    c = new Cube(this, CAVE_ENTRANCE_EAST, worldX, worldY, worldZ);
                    c.overwritePower = 2;
                }
                if ((kindNext == K_STEEP_WEST || kindNext == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                    entranceOverwrite = true;
                }
                if ((kindPrev == K_STEEP_WEST || kindPrev == K_STEEP_EAST) && (relX < realSizeX && relX > 0)) {
                    entranceOverwrite = true;
                }
                if (c != null) {
                    c.internalLight = true;
                    return inTheCaveHeight >= 0 ? c : null;
                }
            }
            if ((relX % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel && tmpHeightZ + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel) {
                int kind = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ, farView)[4];
                int kindNext = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ + 1, farView)[4];
                int kindPrev = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ - 1, farView)[4];
                Cube c = null;
                if ((kind == K_STEEP_NORTH || kind == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                    c = new Cube(this, CAVE_ENTRANCE_NORTH, worldX, worldY, worldZ);
                    c.overwritePower = 2;
                }
                if ((kindNext == K_STEEP_NORTH || kindNext == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                    entranceOverwrite = true;
                }
                if ((kindPrev == K_STEEP_NORTH || kindPrev == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                    entranceOverwrite = true;
                }
                if (c != null) {
                    c.internalLight = true;
                    return inTheCaveHeight >= 0 ? c : null;
                }
            }
            if ((relX % ENTRANCE_DISTANCE == 2) && worldY == ENTRANCE_LEVEL + worldGroundLevel && tmpHeightZ + ((World) getRoot()).worldGroundLevel == ENTRANCE_LEVEL + worldGroundLevel + 1) {
                int kind = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ, farView)[4];
                int kindNext = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ + 1, farView)[4];
                int kindPrev = (int) getCubeKindOutside(-1, worldX, worldY, tmpWorldZ - 1, farView)[4];
                Cube c = null;
                if ((kind == K_STEEP_NORTH || kind == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                    c = new Cube(this, CAVE_ENTRANCE_NORTH, worldX, worldY, worldZ);
                    c.overwritePower = 2;
                }
                if ((kindNext == K_STEEP_NORTH || kindNext == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                    entranceOverwrite = true;
                }
                if ((kindPrev == K_STEEP_NORTH || kindPrev == K_STEEP_SOUTH) && (relZ < realSizeZ && relZ > 0)) {
                    entranceOverwrite = true;
                }
                if (c != null) {
                    c.internalLight = true;
                    return inTheCaveHeight >= 0 ? c : null;
                }
            }
        }
        if (height + ((World) getRoot()).worldGroundLevel <= worldY + 1) {
            return null;
        }
        int per = HashUtil.mixPercentage(worldX, relY / levelSize, worldZ);
        if ((relZ % ENTRANCE_DISTANCE == 2 || relX % ENTRANCE_DISTANCE == 2) && relY == ENTRANCE_LEVEL) {
            per += 20;
        }
        if (per < density) {
            Cube c = new Cube(this, CAVE_ROCK, worldX, worldY, worldZ);
            c.overwritePower = 0;
            return c;
        }
        boolean ceiling = true;
        Cube c = new Cube(this, CAVE_GROUND_CEILING, worldX, worldY, worldZ);
        if (worldRelHeight > 1) {
            if (relY % levelSize == 0) {
                if (relY % levelSize == levelSize - 1 || relY == inTheCaveHeight - 1 || relY == inTheCaveHeight - 2) {
                    ceiling = true;
                } else {
                    ceiling = false;
                    c = new Cube(this, CAVE_GROUND, worldX, worldY, worldZ);
                }
            } else if (relY % levelSize == levelSize - 1 && relY + 2 <= inTheCaveHeight) {
                ceiling = true;
                c = new Cube(this, CAVE_CEILING, worldX, worldY, worldZ);
            } else c = new Cube(this, new Side[][] { null, null, null, null, null, null }, worldX, worldY, worldZ);
        }
        {
            c.overwritePower = 0;
            if (ceiling) {
                if ((relY == inTheCaveHeight || relY == inTheCaveHeight - 1 || relY == inTheCaveHeight - 2)) {
                    int YNorth = (int) getPointHeight(relX, relZ + FARVIEW_GAP, realSizeX, realSizeZ, worldX, shrinkToWorld(worldZ + FARVIEW_GAP), farView) / FARVIEW_GAP + ((World) getRoot()).worldGroundLevel - worldGroundLevel;
                    int YSouth = (int) getPointHeight(relX, relZ - FARVIEW_GAP, realSizeX, realSizeZ, worldX, shrinkToWorld(worldZ - FARVIEW_GAP), farView) / FARVIEW_GAP + ((World) getRoot()).worldGroundLevel - worldGroundLevel;
                    int YWest = (int) getPointHeight(relX - FARVIEW_GAP, relZ, realSizeX, realSizeZ, shrinkToWorld(worldX - FARVIEW_GAP), worldZ, farView) / FARVIEW_GAP + ((World) getRoot()).worldGroundLevel - worldGroundLevel;
                    int YEast = (int) getPointHeight(relX + FARVIEW_GAP, relZ, realSizeX, realSizeZ, shrinkToWorld(worldX + FARVIEW_GAP), worldZ, farView) / FARVIEW_GAP + ((World) getRoot()).worldGroundLevel - worldGroundLevel;
                    if (YEast < inTheCaveHeight || inTheCaveHeight == 0) {
                        c.merge(new Cube(this, CAVE_EAST, worldX, worldY, worldZ), worldX, worldY, worldZ, SurfaceHeightAndType.NOT_STEEP);
                    }
                    if (YSouth < inTheCaveHeight || inTheCaveHeight == 0) {
                        c.merge(new Cube(this, CAVE_SOUTH, worldX, worldY, worldZ), worldX, worldY, worldZ, SurfaceHeightAndType.NOT_STEEP);
                    }
                    if (YWest < inTheCaveHeight || inTheCaveHeight == 0) {
                        c.merge(new Cube(this, CAVE_WEST, worldX, worldY, worldZ), worldX, worldY, worldZ, SurfaceHeightAndType.NOT_STEEP);
                    }
                    if (YNorth < inTheCaveHeight || inTheCaveHeight == 0) {
                        c.merge(new Cube(this, CAVE_NORTH, worldX, worldY, worldZ), worldX, worldY, worldZ, SurfaceHeightAndType.NOT_STEEP);
                    }
                    c.overwritePower = 1;
                } else {
                    c.overwritePower = 1;
                }
            }
        }
        return c;
    }

    SurfaceHeightAndType[] cachedType = null;

    SurfaceHeightAndType[] cachedNonType = null;

    int s_lastWorldX = -9999, s_lastWorldZ = -9999;

    SurfaceHeightAndType[] s_lastType = null;

    public SurfaceHeightAndType[] getPointSurfaceData(int worldX, int worldZ, Cube preCube, boolean farView) {
        if (s_lastWorldX == worldX && s_lastWorldZ == worldZ) {
            return s_lastType;
        } else {
            s_lastWorldX = worldX;
            s_lastWorldZ = worldZ;
        }
        if (getCubeBase(-1, worldX, worldGroundLevel, worldZ, farView) == null) {
            if (cachedNonType == null) {
                cachedNonType = new SurfaceHeightAndType[] { new SurfaceHeightAndType(this, worldGroundLevel, false, SurfaceHeightAndType.NOT_STEEP) };
            }
            s_lastType = cachedNonType;
            return cachedNonType;
        }
        int per = HashUtil.mixPercentage(worldX, 0, worldZ);
        if (per >= density) {
            if (cachedType == null) cachedType = new SurfaceHeightAndType[] { new SurfaceHeightAndType(this, worldGroundLevel, true, SurfaceHeightAndType.NOT_STEEP) };
            s_lastType = cachedType;
            return cachedType;
        }
        if (cachedNonType == null) {
            cachedNonType = new SurfaceHeightAndType[] { new SurfaceHeightAndType(this, worldGroundLevel, false, SurfaceHeightAndType.NOT_STEEP) };
        }
        s_lastType = cachedNonType;
        return cachedNonType;
    }

    @Override
    public boolean denyOtherEnvironmentSounds() {
        return true;
    }

    @Override
    public int[][] getFilledZonesOfY(int worldX, int worldZ, int minY, int maxY) {
        if (minY <= worldHeight) return new int[][] { { worldGroundLevel, worldHeight } };
        return null;
    }

    @Override
    public boolean isWorldMapTinter() {
        return false;
    }

    MusicDescription musicDesc = new MusicDescription("cave");

    @Override
    public MusicDescription getMusicDescription() {
        return musicDesc;
    }
}
