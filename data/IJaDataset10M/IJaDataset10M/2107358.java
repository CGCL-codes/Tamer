package homura.hde.ext.terrain.util;

import homura.hde.util.JmeException;
import java.util.Random;
import java.util.logging.Logger;

/**
 * <code>HillHeightMap</code> generates a height map base on the Hill
 * Algorithm. Terrain is generatd by growing hills of random size and height at
 * random points in the heightmap. The terrain is then normalized and valleys
 * can be flattened.
 * 
 * @author Frederik B�lthoff
 * @see <a href="http://www.robot-frog.com/3d/hills/hill.html">Hill Algorithm</a>
 */
public class HillHeightMap extends AbstractHeightMap {

    private static final Logger logger = Logger.getLogger(HillHeightMap.class.getName());

    private int iterations;

    private float minRadius;

    private float maxRadius;

    private byte flattening;

    private long seed;

    /**
	 * Constructor sets the attributes of the hill system and generates the
	 * height map.
	 * 
	 * @param size
	 *            size the size of the terrain to be generated
	 * @param iterations
	 *            the number of hills to grow
	 * @param minRadius
	 *            the minimum radius of a hill
	 * @param maxRadius
	 *            the maximum radius of a hill
	 * @param flattening
	 *            the power of flattening done, 1 means none
	 * @param seed
	 *            the seed to generate the same heightmap again
	 * @throws JmeException
	 *             if size of the terrain is not greater that zero, or number of
	 *             iterations is not greater that zero
	 */
    public HillHeightMap(int size, int iterations, float minRadius, float maxRadius, byte flattening, long seed) {
        if (size <= 0 || iterations <= 0 || minRadius <= 0 || maxRadius <= 0 || minRadius >= maxRadius || flattening < 1) {
            throw new JmeException("Either size of the terrain is not greater that zero, " + "or number of iterations is not greater that zero, " + "or minimum or maximum radius are not greater than zero, " + "or minimum radius is greater than maximum radius, " + "or power of flattening is below one");
        }
        this.size = size;
        this.seed = seed;
        this.iterations = iterations;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.flattening = flattening;
        load();
    }

    /**
	 * Constructor sets the attributes of the hill system and generates the
	 * height map by using a random seed.
	 * 
	 * @param size
	 *            size the size of the terrain to be generated
	 * @param iterations
	 *            the number of hills to grow
	 * @param minRadius
	 *            the minimum radius of a hill
	 * @param maxRadius
	 *            the maximum radius of a hill
	 * @param flattening
	 *            the power of flattening done, 1 means none
	 * @throws JmeException
	 *             if size of the terrain is not greater that zero, or number of
	 *             iterations is not greater that zero
	 */
    public HillHeightMap(int size, int iterations, float minRadius, float maxRadius, byte flattening) {
        this(size, iterations, minRadius, maxRadius, flattening, new Random().nextLong());
    }

    public boolean load() {
        if (null != heightData) {
            unloadHeightMap();
        }
        heightData = new int[size * size];
        float[][] tempBuffer = new float[size][size];
        Random random = new Random(seed);
        for (int i = 0; i < iterations; i++) {
            addHill(tempBuffer, random);
        }
        normalize(tempBuffer);
        flatten(tempBuffer);
        normalizeTerrain(tempBuffer);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setHeightAtPoint((int) tempBuffer[i][j], j, i);
            }
        }
        logger.info("Created Heightmap using the Hill Algorithm");
        return true;
    }

    /**
	 * Generates a new hill of random size and height at a random position in
	 * the heightmap. This is the actual Hill algorithm. The <code>Random</code>
	 * object is used to guarantee the same heightmap for the same seed and
	 * attributes.
	 * 
	 * @param tempBuffer
	 *            the temporary height map buffer
	 * @param random
	 *            the random number generator
	 */
    protected void addHill(float[][] tempBuffer, Random random) {
        float radius = randomRange(random, minRadius, maxRadius);
        float x = randomRange(random, -radius, size + radius);
        float y = randomRange(random, -radius, size + radius);
        float radiusSq = radius * radius;
        float distSq;
        float height;
        int xMin = Math.round(x - radius - 1);
        int xMax = Math.round(x + radius + 1);
        int yMin = Math.round(y - radius - 1);
        int yMax = Math.round(y + radius + 1);
        if (xMin < 0) xMin = 0;
        if (xMax > size) xMax = size - 1;
        if (yMin < 0) yMin = 0;
        if (yMax > size) yMax = size - 1;
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMin; j <= yMax; j++) {
                distSq = (x - i) * (x - i) + (y - j) * (y - j);
                height = radiusSq - distSq;
                if (height > 0) tempBuffer[i][j] += height;
            }
        }
    }

    /**
	 * Normalizes the heightmap values between 0.0 and 1.0. This is necessary
	 * for the flatten algorithm to work properly.
	 * 
	 * @param tempBuffer
	 *            the temporary heightmap buffer
	 */
    protected void normalize(float[][] tempBuffer) {
        float min = tempBuffer[0][0];
        float max = min;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                float z = tempBuffer[x][y];
                if (z < min) min = z;
                if (z > max) max = z;
            }
        }
        if (max != min) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    tempBuffer[x][y] = (tempBuffer[x][y] - min) / (max - min);
                }
            }
        }
    }

    /**
	 * Flattens out the valleys. The flatten algorithm makes the valleys more
	 * prominent while keeping the hills mostly intact. This effect is based on
	 * what happens when values below one are squared.
	 * 
	 * @param tempBuffer
	 *            the temporary heightmap buffer
	 * @see #setFlattening(byte)
	 */
    protected void flatten(float[][] tempBuffer) {
        if (flattening > 1) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    float flat = 1.0f;
                    float original = tempBuffer[x][y];
                    for (int i = 0; i < flattening; i++) {
                        flat *= original;
                    }
                    tempBuffer[x][y] = flat;
                }
            }
        }
    }

    private float randomRange(Random random, float min, float max) {
        return (random.nextInt() * (max - min) / Integer.MAX_VALUE) + min;
    }

    /**
	 * Sets the number of hills to grow. More hills usually mean a nicer
	 * heightmap.
	 * 
	 * @param iterations
	 *            the number of hills to grow
	 * @throws JmeException
	 *             if iterations if not greater than zero
	 */
    public void setIterations(int iterations) {
        if (iterations <= 0) {
            throw new JmeException("Number of iterations is not greater than zero");
        }
        this.iterations = iterations;
    }

    /**
	 * Sets the amount of flattening done in the algorithm. Flattening makes the
	 * valleys more prominent while leaving the hills mostly untouched.
	 * 
	 * @param flattening
	 *            the power of flattening, one means none
	 * @theows JmeException if flattening is below one
	 */
    public void setFlattening(byte flattening) {
        if (flattening < 1) {
            throw new JmeException("Power is below one");
        }
        this.flattening = flattening;
    }

    /**
	 * Sets the minimum radius of a hill.
	 * 
	 * @param maxRadius
	 *            the maximum radius of a hill
	 * @throws JmeException
	 *             if the maximum radius if not greater than zero or not greater
	 *             than the minimum radius
	 */
    public void setMaxRadius(float maxRadius) {
        if (maxRadius <= 0 || maxRadius <= minRadius) {
            throw new JmeException("The maximum radius is not greater than 0, " + "or not greater than the minimum radius");
        }
        this.maxRadius = maxRadius;
    }

    /**
	 * Sets the maximum radius of a hill.
	 * 
	 * @param minRadius
	 *            the minimum radius of a hill
	 * @throw JmeException if the minimum radius is not greater than zero or not
	 *        lower than the maximum radius
	 */
    public void setMinRadius(float minRadius) {
        if (minRadius <= 0 || minRadius >= maxRadius) {
            throw new JmeException("The minimum radius is not greater than 0, " + "or not lower than the maximum radius");
        }
        this.minRadius = minRadius;
    }
}
