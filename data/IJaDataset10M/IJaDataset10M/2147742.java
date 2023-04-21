package il.co.gadiworks.gamedev2d;

import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import android.util.FloatMath;
import il.co.gadiworks.games.framework.Game;
import il.co.gadiworks.games.framework.Screen;
import il.co.gadiworks.games.framework.Input.TouchEvent;
import il.co.gadiworks.games.framework.gl.Camera2D;
import il.co.gadiworks.games.framework.gl.SpatialHashGrid;
import il.co.gadiworks.games.framework.gl.Texture;
import il.co.gadiworks.games.framework.gl.Vertices;
import il.co.gadiworks.games.framework.impl.GLGame;
import il.co.gadiworks.games.framework.impl.GLGraphics;
import il.co.gadiworks.games.framework.math.OverlapTester;
import il.co.gadiworks.games.framework.math.Vector2;
import il.co.gadiworks.games.framework.DynamicGameObject;
import il.co.gadiworks.games.framework.GameObject;

public class TextureAtlasTest extends GLGame {

    @Override
    public Screen getStartScreen() {
        return new TextureAtlasScreen(this);
    }

    class TextureAtlasScreen extends Screen {

        final int NUM_TARGETS = 20;

        final float WORLD_WIDTH = 9.6f;

        final float WORLD_HEIGHT = 4.8f;

        GLGraphics glGraphics;

        Cannon cannon;

        DynamicGameObject ball;

        List<GameObject> targets;

        SpatialHashGrid grid;

        Camera2D camera;

        Texture texture;

        Vertices cannonVertices;

        Vertices ballVertices;

        Vertices targetVertices;

        Vector2 touchPos = new Vector2();

        Vector2 gravity = new Vector2(0, -10);

        public TextureAtlasScreen(Game game) {
            super(game);
            this.glGraphics = ((GLGame) game).getGLGraphics();
            this.cannon = new Cannon(0, 0, 1, 0.5f);
            this.ball = new DynamicGameObject(0, 0, 0.2f, 0.2f);
            this.targets = new ArrayList<GameObject>(this.NUM_TARGETS);
            this.grid = new SpatialHashGrid(this.WORLD_WIDTH, this.WORLD_HEIGHT, 2.5f);
            this.camera = new Camera2D(this.glGraphics, this.WORLD_WIDTH, this.WORLD_HEIGHT);
            for (int i = 0; i < this.NUM_TARGETS; i++) {
                GameObject target = new GameObject((float) Math.random() * this.WORLD_WIDTH, (float) Math.random() * this.WORLD_HEIGHT, 0.5f, 0.5f);
                this.grid.insertStaticObject(target);
                this.targets.add(target);
            }
            this.cannonVertices = new Vertices(this.glGraphics, 4, 6, false, true);
            this.cannonVertices.setVertices(new float[] { -0.5f, -0.25f, 0.0f, 0.5f, 0.5f, -0.25f, 1.0f, 0.5f, 0.5f, 0.25f, 1.0f, 0.0f, -0.5f, 0.25f, 0.0f, 0.0f }, 0, 16);
            this.cannonVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0 }, 0, 6);
            this.ballVertices = new Vertices(this.glGraphics, 4, 6, false, true);
            this.ballVertices.setVertices(new float[] { -0.1f, -0.1f, 0.0f, 0.75f, 0.1f, -0.1f, 0.25f, 0.75f, 0.1f, 0.1f, 0.25f, 0.5f, -0.1f, 0.1f, 0.0f, 0.5f }, 0, 16);
            this.ballVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0 }, 0, 6);
            this.targetVertices = new Vertices(this.glGraphics, 4, 6, false, true);
            this.targetVertices.setVertices(new float[] { -0.25f, -0.25f, 0.5f, 1.0f, 0.25f, -0.25f, 1.0f, 1.0f, 0.25f, 0.25f, 1.0f, 0.5f, -0.25f, 0.25f, 0.5f, 0.5f }, 0, 16);
            this.targetVertices.setIndices(new short[] { 0, 1, 2, 2, 3, 0 }, 0, 6);
        }

        @Override
        public void update(float deltaTime) {
            List<TouchEvent> touchEvents = GAME.getInput().getTouchEvents();
            GAME.getInput().getKeyEvents();
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                TouchEvent event = touchEvents.get(i);
                this.camera.touchToWorld(this.touchPos.set(event.x, event.y));
                this.cannon.angle = this.touchPos.sub(this.cannon.POSITION).angle();
                if (event.type == TouchEvent.TOUCH_UP) {
                    float radians = this.cannon.angle * Vector2.TO_RADIANS;
                    float ballSpeed = this.touchPos.len() * 2;
                    this.ball.POSITION.set(this.cannon.POSITION);
                    this.ball.VELOCITY.x = FloatMath.cos(radians) * ballSpeed;
                    this.ball.VELOCITY.y = FloatMath.sin(radians) * ballSpeed;
                    this.ball.BOUNDS.LOWER_LEFT.set(this.ball.POSITION.x - 0.1f, this.ball.POSITION.y - 0.1f);
                }
            }
            this.ball.VELOCITY.add(this.gravity.x * deltaTime, this.gravity.y * deltaTime);
            this.ball.POSITION.add(this.ball.VELOCITY.x * deltaTime, this.ball.VELOCITY.y * deltaTime);
            this.ball.BOUNDS.LOWER_LEFT.add(this.ball.VELOCITY.x * deltaTime, this.ball.VELOCITY.y * deltaTime);
            List<GameObject> colliders = this.grid.getPotentialColliders(ball);
            len = colliders.size();
            for (int i = 0; i < len; i++) {
                GameObject collider = colliders.get(i);
                if (OverlapTester.overlapRectangles(this.ball.BOUNDS, collider.BOUNDS)) {
                    this.grid.removeObject(collider);
                    this.targets.remove(collider);
                }
            }
            if (this.ball.POSITION.y > 0) {
                this.camera.POSITION.set(this.ball.POSITION);
                this.camera.zoom = 1 + this.ball.POSITION.y / this.WORLD_HEIGHT;
            } else {
                this.camera.POSITION.set(this.WORLD_WIDTH / 2, this.WORLD_HEIGHT / 2);
                this.camera.zoom = 1;
            }
        }

        @Override
        public void present(float deltaTime) {
            GL10 gl = this.glGraphics.getGL();
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            this.camera.setViewportAndMatrices();
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            this.texture.bind();
            this.targetVertices.bind();
            int len = this.targets.size();
            for (int i = 0; i < len; i++) {
                GameObject target = this.targets.get(i);
                gl.glLoadIdentity();
                gl.glTranslatef(target.POSITION.x, target.POSITION.y, 0);
                this.targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            }
            this.targetVertices.unbind();
            gl.glLoadIdentity();
            gl.glTranslatef(this.ball.POSITION.x, this.ball.POSITION.y, 0);
            this.ballVertices.bind();
            this.ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            this.ballVertices.unbind();
            gl.glLoadIdentity();
            gl.glTranslatef(this.cannon.POSITION.x, this.cannon.POSITION.y, 0);
            gl.glRotatef(this.cannon.angle, 0, 0, 1);
            this.cannonVertices.bind();
            this.cannonVertices.draw(GL10.GL_TRIANGLES, 0, 6);
            this.cannonVertices.unbind();
        }

        @Override
        public void resume() {
            this.texture = new Texture(((GLGame) GAME), "atlas.png");
        }

        @Override
        public void dispose() {
        }

        @Override
        public void pause() {
        }
    }
}
