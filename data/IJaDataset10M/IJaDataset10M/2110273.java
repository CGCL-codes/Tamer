package openwar.world;

import com.jme3.math.FastMath;
import openwar.DB.Army;
import openwar.DB.Faction;
import openwar.DB.Settlement;
import openwar.Main;

/**
 *
 * @author kehl
 */
public class WorldAI {

    WorldMapGameLogic logic;

    float[][] infMap;

    int width;

    int height;

    int influence_iterations = 5;

    float influence_decay = 0.8f;

    public WorldAI(WorldMapGameLogic l) {
        logic = l;
        width = logic.game.worldMapState.map.width;
        height = logic.game.worldMapState.map.height;
    }

    private void calculateInfluenceStep(float[][] in, float[][] out) {
        float influence, curr;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!logic.game.worldMapState.map.walkableTile(x, y) || !logic.game.worldMapState.map.sailableTile(x, y)) {
                    out[x][y] = 0;
                    continue;
                }
                influence = in[x][y];
                if (x > 0) {
                    curr = in[x - 1][y] * influence_decay;
                    influence = Math.abs(curr) < Math.abs(influence) ? curr : influence;
                }
                if (x < width - 1) {
                    curr = in[x + 1][y] * influence_decay;
                    influence = Math.abs(curr) < Math.abs(influence) ? curr : influence;
                }
                if (y > 0) {
                    curr = in[x][y - 1] * influence_decay;
                    influence = Math.abs(curr) < Math.abs(influence) ? curr : influence;
                }
                if (y < height - 1) {
                    curr = in[x][y + 1] * influence_decay;
                    influence = Math.abs(curr) < Math.abs(influence) ? curr : influence;
                }
                out[x][y] = influence;
            }
        }
    }

    void calculateInfluenceMap(Faction f) {
        float[][] temp = new float[width][height];
        infMap = new float[width][height];
        float influence = 0, absMax = 0;
        for (Settlement s : Main.DB.settlements.values()) {
            influence = s.units.size() / 20f;
            influence += s.level / 20f;
            absMax = Math.max(absMax, influence);
            if (!s.owner.equals(f.refName)) {
                influence *= -1;
            }
            infMap[s.posX][s.posZ] = influence;
        }
        for (Army a : logic.game.worldMapState.map.hashedArmies.values()) {
            influence = a.units.size() / 20f;
            influence += a.calculateMovePoints() / 20f;
            absMax = Math.max(absMax, influence);
            if (!a.owner.equals(f.refName)) {
                influence *= -1;
            }
            infMap[a.posX][a.posZ] = influence;
        }
        for (int i = 0; i < influence_iterations; i++) {
            calculateInfluenceStep(infMap, temp);
            calculateInfluenceStep(temp, infMap);
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                infMap[x][y] /= absMax;
            }
        }
    }
}
