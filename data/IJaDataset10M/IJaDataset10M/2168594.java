package Engine.Blocks;

import org.newdawn.slick.opengl.Texture;
import Engine.PhysicEngine.RigidBody;

public class Stone extends DefaultBlock {

    public Stone() {
        super();
        UV_X1 = 1 / 16f;
        UV_X2 = 2 / 16f;
        UV_Y1 = 0;
        UV_Y2 = 1 / 16f;
    }

    @Override
    public void OnCollision(RigidBody rigidbody) {
    }

    @Override
    public int ID() {
        return 1;
    }

    @Override
    public void Draw(DrawInfos drawinfos, float XPosition, float YPosition) {
        DrawTextured(XPosition, YPosition, drawinfos.GetTexture());
    }
}
