package Code.Basic.Visual;

import org.jrabbit.base.core.types.Updateable;
import org.jrabbit.standard.game.objects.Sprite;

public class Warp extends Sprite implements Updateable {

    private Warp destination;

    public Warp() {
        super("Resources/Images/Terrain/warp.png");
    }

    public void setDestination(Warp dest) {
        destination = dest;
    }

    public Warp getDestination() {
        return destination;
    }

    public void update(int delta) {
        rotation.rotate(delta * 0.001);
    }
}
