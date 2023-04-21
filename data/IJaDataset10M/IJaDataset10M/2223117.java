package gestalt.demo.advanced;

import gestalt.candidates.JoglPointSprites;
import gestalt.render.AnimatorRenderer;
import gestalt.shape.Color;
import gestalt.shape.Mesh;
import gestalt.texture.Bitmaps;
import mathematik.Random;
import mathematik.Vector3f;
import data.Resource;

/**
 * this demo shows how to use gestalt point sprites in gestalt
 */
public class UsingAnimatedGestaltPointSprites extends AnimatorRenderer {

    private ParticleManager _myParticleManager;

    private Mesh _myMesh;

    private JoglPointSprites _myPointSprites;

    public void setup() {
        _myParticleManager = new ParticleManager();
        _myMesh = drawablefactory().mesh(false, _myParticleManager.positionbackuparray, 3, _myParticleManager.colorbackuparray, 4, null, 0, null, MESH_POINTS);
        _myPointSprites = new JoglPointSprites();
        _myPointSprites.load(Bitmaps.getBitmap(Resource.getStream("demo/common/flower-particle.png"), "flower"));
        _myPointSprites.quadric = new float[] { 0.001f, 0.000002f, 0.00001f };
        _myPointSprites.pointsize = 50;
        _myPointSprites.minpointsize = 10;
        _myPointSprites.maxpointsize = 250;
        _myMesh.material().addPlugin(_myPointSprites);
        _myMesh.material().depthtest = false;
        _myMesh.material().transparent = true;
        bin(BIN_3D).add(_myMesh);
        framerate(UNDEFINED);
    }

    public void loop(final float theDeltaTime) {
        _myParticleManager.loop();
    }

    private class ParticleManager {

        public final float[] positionbackuparray;

        public final float[] colorbackuparray;

        private final Particle[] particles;

        private final Random _myRandom = new Random();

        private static final int NUMBER_OF_PARTICLES = 4000;

        private static final float SPAWN_DEPTH = -400;

        public ParticleManager() {
            positionbackuparray = new float[NUMBER_OF_PARTICLES * 3];
            colorbackuparray = new float[NUMBER_OF_PARTICLES * 4];
            particles = new Particle[NUMBER_OF_PARTICLES];
            for (int i = 0; i < particles.length; i++) {
                particles[i] = new Particle();
                particles[i].color.set(1, 0f);
                particles[i].speed = (float) Math.random() * 0.4f + 0.1f;
            }
        }

        public void loop() {
            for (int i = 0; i < particles.length; i++) {
                if (particles[i].isActive) {
                    Vector3f myDirection = new Vector3f(event().mouseX, event().mouseY, 0);
                    myDirection.sub(particles[i].position);
                    final float myDistance = myDirection.length();
                    myDirection.scale(particles[i].speed / myDistance);
                    particles[i].position.add(myDirection);
                    particles[i].color.a = 1 - myDistance / (-1.5f * SPAWN_DEPTH);
                    if (myDistance < 1) {
                        particles[i].isActive = false;
                    }
                } else {
                    particles[i].isActive = true;
                    particles[i].position.set(_myRandom.getFloat(-640, 640), _myRandom.getFloat(-480, 480), SPAWN_DEPTH);
                    particles[i].color.a = 0;
                }
                positionbackuparray[i * 3 + 0] = particles[i].position.x;
                positionbackuparray[i * 3 + 1] = particles[i].position.y;
                positionbackuparray[i * 3 + 2] = particles[i].position.z;
                colorbackuparray[i * 4 + 0] = particles[i].color.r;
                colorbackuparray[i * 4 + 1] = particles[i].color.g;
                colorbackuparray[i * 4 + 2] = particles[i].color.b;
                colorbackuparray[i * 4 + 3] = particles[i].color.a;
            }
        }

        private class Particle {

            Color color = new Color();

            Vector3f position = new Vector3f();

            float speed = 1;

            boolean isActive = false;
        }
    }

    public static void main(String[] args) {
        new UsingAnimatedGestaltPointSprites().init();
    }
}
