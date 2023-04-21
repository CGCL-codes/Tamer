package net.sf.breed.kout.draw;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import net.sf.breed.kout.IBreedContext;
import net.sf.breed.kout.KOutConstants;
import net.sf.breed.kout.model.config.RandomSpriteConfiguration;

/**
 * A generated sprite.
 * 
 * @author <a href="mailto:kain@land-of-kain.de">Kai Ruhl</a>
 * @since Oct 25, 2006
 */
public class RandomSpriteDrawer implements IBreedDrawer {

    /** The minumum number of vertices on the base shape. */
    private static final int MIN_RING_VERTICES = 3;

    /** The minimum number of rings (= trunk parts + 1). */
    private static final int MIN_TRUNK_RINGS = 3;

    /** The "down" angle. */
    private static final float REFERENCE_ANGLE = 180f;

    /** The sprite configuration. */
    private RandomSpriteConfiguration config;

    /** The number of vertices on the base shape ("ring"). */
    private int ringVertCount;

    /** The number of rings. */
    private int trunkRingCount;

    /** The nominal radius of the trunk rings. */
    private float[] trunkRingRadius;

    /** The length of the trunk parts. */
    private float[] trunkLengths;

    /** The coordinates of the base shape ("trunk"). */
    private float[][][] trunkRings;

    /** The position of the sprite in space. */
    private float[] position = new float[3];

    /** The current damage state (0 to 1). */
    private float damageState = 0;

    /**
   * A new random sprite.
   */
    public RandomSpriteDrawer() {
        this(new RandomSpriteConfiguration());
    }

    /**
   * A new random sprite.
   * 
   * @param config The configuration.
   */
    public RandomSpriteDrawer(RandomSpriteConfiguration config) {
        this.config = config;
        generate();
    }

    /**
   * Generates the whole sprite.
   */
    public void generate() {
        generateTrunk();
    }

    /**
   * Generates the main body.
   */
    private void generateTrunk() {
        ringVertCount = MIN_RING_VERTICES + (int) Math.floor(Math.random() * (config.ringVerticesMax - MIN_RING_VERTICES + 1));
        trunkRingCount = MIN_TRUNK_RINGS + (int) Math.floor(Math.random() * (config.trunkRingsMax - MIN_TRUNK_RINGS + 1));
        trunkLengths = new float[trunkRingCount];
        final int trunkPartCount = trunkRingCount - 1;
        final float trunkPartNominalLength = config.length / (float) trunkPartCount;
        float trunkPartRemainingLength = config.length;
        float trunkPartLen, trunkPartVariation;
        for (int trunkPos = 0; trunkPos < trunkPartCount; trunkPos++) {
            boolean isLastTrunkPart = trunkPos == trunkPartCount - 1;
            if (isLastTrunkPart) {
                trunkPartLen = trunkPartRemainingLength;
            } else {
                trunkPartVariation = 2f * (float) (Math.random() * config.trunkPartLengthVariation);
                trunkPartVariation -= trunkPartVariation / 2f;
                trunkPartLen = trunkPartNominalLength + trunkPartVariation;
            }
            trunkPartRemainingLength -= trunkPartLen;
            trunkLengths[trunkPos] = trunkPartLen;
        }
        trunkRings = new float[trunkRingCount][ringVertCount][3];
        trunkRingRadius = new float[trunkRingCount];
        float[][] ring;
        float anglePart = 360 / ringVertCount;
        float angleStart = REFERENCE_ANGLE - 0.5f * anglePart;
        float angle;
        float radius, prevRadius = 0;
        float x = 0;
        final float trunkRadiusRandom = config.trunkRadiusMax - config.trunkRadiusMin;
        for (int trunkPos = 0; trunkPos < trunkRingCount; trunkPos++) {
            do {
                radius = config.trunkRadiusMin + (float) (trunkRadiusRandom * Math.random());
            } while (trunkPos > 0 && Math.abs(radius - prevRadius) <= config.trunkRadiusMinVariation);
            trunkRingRadius[trunkPos] = radius;
            prevRadius = radius;
            ring = trunkRings[trunkPos];
            angle = angleStart;
            for (int vertPos = 0; vertPos < ringVertCount; vertPos++) {
                ring[vertPos][0] = x;
                ring[vertPos][1] = radius * (float) Math.cos(Math.toRadians(angle));
                ring[vertPos][2] = radius * (float) Math.sin(Math.toRadians(angle));
                angle += anglePart;
            }
            x += trunkLengths[trunkPos];
        }
    }

    /**
   * Draws the main body.
   * 
   * @param gl The GL context.
   */
    private void drawMainBody(GL gl) {
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0f);
        GLDraw.drawPolygon(gl, trunkRings[0]);
        GLDraw.drawPolygon(gl, trunkRings[trunkRingCount - 1]);
        int trunkPosMinus;
        int vertMax = ringVertCount - 1;
        int vertPosMinus;
        float colorStep = 0.1f;
        float color1 = 0.6f;
        float color2 = 1f - trunkRingCount * colorStep;
        float damageColor = 0;
        for (int trunkPos = 1; trunkPos < trunkRingCount; trunkPos++) {
            damageColor = color1;
            damageColor += (1 - color1) * damageState;
            gl.glColor4f(damageColor, color1, color2, 0f);
            color2 += colorStep;
            trunkPosMinus = trunkPos - 1;
            GLDraw.drawQuad(gl, trunkRings[trunkPosMinus][0], trunkRings[trunkPosMinus][vertMax], trunkRings[trunkPos][vertMax], trunkRings[trunkPos][0]);
            for (int vertPos = 1; vertPos < ringVertCount; vertPos++) {
                vertPosMinus = vertPos - 1;
                GLDraw.drawQuad(gl, trunkRings[trunkPosMinus][vertPosMinus], trunkRings[trunkPosMinus][vertPos], trunkRings[trunkPos][vertPos], trunkRings[trunkPos][vertPosMinus]);
            }
        }
    }

    /**
   * Draws the bounding box.
   * 
   * @param gl The GL canvas.
   */
    private void drawBoundingBox(GL gl) {
        float[][] boundingBox = getBoundingBox();
        float[] point1 = boundingBox[0];
        float[] point2 = boundingBox[1];
        gl.glColor4f(0.8f, 0.8f, 0.8f, 0.5f);
        GLDraw.drawBoundingBox(gl, point1, point2);
    }

    /**
   * Moves stuff around the sprite.
   * 
   * @see net.sf.breed.kout.IBreedTickable#tick(net.sf.breed.kout.IBreedContext)
   */
    public void tick(IBreedContext context) {
    }

    /**
   * Draws the sprite.
   * 
   * @see net.sf.breed.kout.draw.IBreedDrawer#draw(javax.media.opengl.GL, javax.media.opengl.glu.GLU)
   */
    public void draw(GL gl, GLU glu) {
        if (KOutConstants.DRAW_BOUNDING_BOX) {
            drawBoundingBox(gl);
        }
        gl.glTranslatef(position[0], position[1], position[2]);
        drawMainBody(gl);
    }

    /**
   * Returns the position of the sprite in space.
   * 
   * @return The position of the sprite.
   */
    public float[] getPosition() {
        return position;
    }

    /**
   * Returns the bounding box.
   * 
   * @return The bounding box.
   */
    public float[][] getBoundingBox() {
        float[][] boundingBox = new float[2][3];
        System.arraycopy(position, 0, boundingBox[0], 0, 3);
        System.arraycopy(position, 0, boundingBox[1], 0, 3);
        boundingBox[0][1] -= config.trunkRadiusMax / 2;
        boundingBox[0][2] -= config.trunkRadiusMax / 2;
        boundingBox[1][0] += config.length;
        boundingBox[1][1] += config.trunkRadiusMax / 2;
        boundingBox[1][2] += config.trunkRadiusMax / 2;
        return boundingBox;
    }

    /**
   * Indicates whether given coordinates are out of given circle.
   * 
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @param radius The radius of the circle.
   * @return
   */
    private boolean isOutOfCircle(float x, float y, float radius) {
        float hypothenuse = (float) Math.sqrt(x * x + y * y);
        return hypothenuse > radius;
    }

    /**
   * Sets the position delta from the previous position.
   * 
   * @param x The X delta.
   * @param y The Y delta.
   * @param z The Z delta.
   */
    public void setPositionDelta(float x, float y, float z) {
        position[0] += x;
        final float halfX = KOutConstants.TUBE_VISIBLE_LENGTH / 2;
        if (position[0] < -halfX || position[0] > halfX) {
            position[0] -= x;
        }
        position[1] += y;
        if (isOutOfCircle(position[1], position[2], KOutConstants.TUBE_VISIBLE_RADIUS)) {
            position[1] -= y;
        }
        position[2] += z;
        if (isOutOfCircle(position[1], position[2], KOutConstants.TUBE_VISIBLE_RADIUS)) {
            position[2] -= z;
        }
    }

    /**
   * Sets the damage state (0 to 1 percent).
   * 
   * @param damageState The damage state.
   */
    public void setDamageState(float damageState) {
        if (damageState < 0 || damageState > 1) {
            throw new IllegalArgumentException("Damage state must be between 0 and 1.");
        }
        this.damageState = damageState;
    }
}
