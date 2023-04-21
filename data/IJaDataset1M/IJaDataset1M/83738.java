package test.terrain;

import env3d.advanced.EnvAdvanced;
import env3d.advanced.EnvSkyRoom;
import env3d.advanced.EnvTerrain;

/**
 * This terrain test implements a type of "infinite" terrain.  Which basically
 * just repeats the same terrain over and over again.
 * 
 * @author jmadar
 */
public class Game {

    private EnvTerrain[][] terrainGrid = new EnvTerrain[3][3];

    private EnvAdvanced env;

    private int scale = 1;

    public Game() {
        env = new EnvAdvanced();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                terrainGrid[x][y] = new EnvTerrain("textures/terrain/termap1.png");
                terrainGrid[x][y].setTexture("textures/mud.gif");
                terrainGrid[x][y].setX((x - 1) * 255 * scale);
                terrainGrid[x][y].setZ((y - 1) * 255 * scale);
                terrainGrid[x][y].setScale(scale, 50, scale);
            }
        }
    }

    public void play() {
        env.setRoom(new EnvSkyRoom("textures/skybox/default"));
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                EnvTerrain terrain = terrainGrid[x][y];
                env.addObject(terrain);
            }
        }
        env.addObject(terrainGrid[1][1]);
        env.setCameraXYZ(-126, terrainGrid[1][1].getHeight(-126, 126) + 2, 126);
        int prevGX = 1, prevGZ = 1;
        while (env.getKey() != 1) {
            double cx = env.getCameraX();
            double cy = env.getCameraY();
            double cz = env.getCameraZ();
            int gridX = (((int) Math.ceil(cx) + (cx > 0 ? (127 * scale) : (-127 * scale))) / (255 * scale)) + 1;
            int gridZ = (((int) Math.ceil(cz) + (cz > 0 ? (127 * scale) : (-127 * scale))) / (255 * scale)) + 1;
            env.setDisplayStr((((cx + ((gridX - 1) * 127)) % 255) - ((gridX - 1) * 127)) + " " + cz);
            env.setDisplayStr(gridX + " " + gridZ, 25, 25);
            env.setCameraXYZ(cx, terrainGrid[1][1].getHeight(cx, cz) + 2, cz);
            if (gridX != prevGX || gridZ != prevGZ) {
                prevGX = gridX;
                prevGZ = gridZ;
                System.out.println("Reload grid");
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        terrainGrid[x][z].setX((gridX + x - 2) * 255 * scale);
                        terrainGrid[x][z].setZ((gridZ + z - 2) * 255 * scale);
                    }
                }
            } else {
                env.advanceOneFrame();
            }
        }
        env.exit();
    }

    public static void main(String[] args) {
        (new Game()).play();
    }
}
