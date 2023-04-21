package edu.ups.gamedev.player;

import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jmex.terrain.TerrainPage;
import edu.ups.gamedev.game.TankGame;

public class TankController extends Controller {

    static final long serialVersionUID = 1;

    TerrainPage terrain;

    protected Tank tank;

    public TankController(Tank tank) {
        this.tank = tank;
        terrain = TankGame.GAMESTATE.getTerrain();
    }

    @Override
    public void update(float interpolation) {
        tank.primaryWeapon.update(interpolation);
        tank.secondaryWeapon.update(interpolation);
        Vector3f point = tank.getLocalTranslation();
        point.y = terrain.getHeight(point);
        Vector3f normal = new Vector3f();
        terrain.getSurfaceNormal(point, normal);
        tank.rotateUpTo(normal);
        tank.getLocalRotation().toAxes(tank.axes);
    }
}
