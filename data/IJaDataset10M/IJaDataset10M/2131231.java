package toolkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.system.JmeException;
import com.jmex.terrain.util.AbstractHeightMap;

public class PerlinNoiseHeightMap extends AbstractHeightMap {

    private static final Logger logger = Logger.getLogger(PerlinNoiseHeightMap.class.getName());

    /** The size of one cell grid */
    protected int sizeCell = 0;

    private Random randomizer;

    private float persistence = 0.5f;

    private float frequency = 0.25f;

    private float amplitude = 1;

    private int numOctaves = 4;

    /** Constructor */
    public PerlinNoiseHeightMap(int size, int sizeCell) {
        if (size <= 0 || sizeCell <= 0 || sizeCell >= size) throw new JmeException("size <= 0 or sizeCell <= 0 or sizeCell >= size");
        this.size = size;
        this.sizeCell = sizeCell;
        this.randomizer = new Random(System.currentTimeMillis());
        load();
    }

    @Override
    public boolean load() {
        if (heightData != null) unloadHeightMap();
        heightData = new float[size * size];
        float[][] tempBuffer = new float[size][size];
        Vector2f vectorTable[] = new Vector2f[256];
        int countCell = size / sizeCell;
        int[][] randVecorTable = new int[countCell + 1][countCell + 1];
        float angle = FastMath.TWO_PI / 256f;
        float value = 0f;
        for (int i = 0; i < 256; i++) {
            vectorTable[i] = new Vector2f(FastMath.cos(value), FastMath.sin(value));
            value += angle;
        }
        for (int y = 0; y < countCell + 1; y++) {
            for (int x = 0; x < countCell + 1; x++) {
                randVecorTable[x][y] = (int) (random() * 255f);
            }
        }
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Vector2f v0 = new Vector2f(x % sizeCell, y % sizeCell);
                Vector2f v1 = new Vector2f(x % sizeCell - sizeCell, y % sizeCell);
                Vector2f v2 = new Vector2f(x % sizeCell, y % sizeCell - sizeCell);
                Vector2f v3 = new Vector2f(x % sizeCell - sizeCell, y % sizeCell - sizeCell);
                double h0 = v0.dot(vectorTable[randVecorTable[x / sizeCell][y / sizeCell]]);
                double h1 = v1.dot(vectorTable[randVecorTable[x / sizeCell + 1][y / sizeCell]]);
                double h2 = v2.dot(vectorTable[randVecorTable[x / sizeCell][y / sizeCell + 1]]);
                double h3 = v3.dot(vectorTable[randVecorTable[x / sizeCell + 1][y / sizeCell + 1]]);
                double wx = 3 * Math.pow(v0.x, 2) - 2 * Math.pow(v0.x, 3);
                double wy = 3 * Math.pow(v0.y, 2) - 2 * Math.pow(v0.y, 3);
                double avg0 = h0 + wx * (h2 - h0);
                double avg1 = h1 + wx * (h3 - h1);
                double result = avg0 + wy * (avg1 - avg0);
                tempBuffer[x][y] = (float) result;
            }
        }
        erodeTerrain(tempBuffer);
        normalizeTerrain(tempBuffer);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setHeightAtPoint((float) tempBuffer[i][j], j, i);
            }
        }
        return true;
    }

    /**
     * Saves the final heightMap this class has created to the given filename as a png
     * @param filename
     */
    public void saveImage(String filename) {
        if (null == filename) throw new JmeException("filename cannot be null");
        BufferedImage bufimg = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                bufimg.setRGB(i, j, (int) getTrueHeightAtPoint(i, j));
            }
        }
        try {
            File out = new File(filename + ".png");
            ImageIO.write(bufimg, "png", out);
        } catch (IOException e) {
            logger.warning("Could not create file: " + filename + ".png");
        }
    }

    private float random() {
        return randomizer.nextFloat();
    }
}
