package il.co.gadiworks.gl3d;

import javax.microedition.khronos.opengles.GL10;
import il.co.gadiworks.games.framework.Game;
import il.co.gadiworks.games.framework.Screen;
import il.co.gadiworks.games.framework.gl.Vertices3;
import il.co.gadiworks.games.framework.impl.GLGame;
import il.co.gadiworks.games.framework.impl.GLScreen;

public class Vertices3Test extends GLGame {

    @Override
    public Screen getStartScreen() {
        return new Vertices3Screen(this);
    }

    class Vertices3Screen extends GLScreen {

        Vertices3 vertices;

        public Vertices3Screen(Game game) {
            super(game);
            this.vertices = new Vertices3(GL_GRAPHICS, 6, 0, true, false);
            this.vertices.setVertices(new float[] { -0.5f, -0.5f, -3, 1, 0, 0, 1, 0.5f, -0.5f, -3, 1, 0, 0, 1, 0.0f, 0.5f, -3, 1, 0, 0, 1, 0.0f, -0.5f, -5, 0, 1, 0, 1, 1.0f, -0.5f, -5, 0, 1, 0, 1, 0.5f, 0.5f, -5, 0, 1, 0, 1 }, 0, 7 * 6);
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = GL_GRAPHICS.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glViewport(0, 0, GL_GRAPHICS.getWidth(), GL_GRAPHICS.getHeight());
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrthof(-1, 1, -1, 1, 10, -10);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            this.vertices.bind();
            this.vertices.draw(GL10.GL_TRIANGLES, 0, 6);
            this.vertices.unbind();
        }

        @Override
        public void dispose() {
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }

        @Override
        public void update(float deltaTime) {
        }
    }
}
