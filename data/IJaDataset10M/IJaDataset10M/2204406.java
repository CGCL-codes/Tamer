package br.com.spacecreatures.prototypes.planetviewer;

import java.util.List;
import edu.emory.mathcs.backport.java.util.Arrays;

public class HeightMapGenerator extends Generator<List<Double>, HeightMapGenerator.HeightMapData> {

    public static class HeightMapData {

        int mapSize;

        double heightScale;

        double amplitude;

        public HeightMapData(int arg0, double arg1, double arg2) {
            mapSize = arg0;
            heightScale = arg1;
            amplitude = arg2;
        }
    }

    private static HeightMapGenerator instance = null;

    public static HeightMapGenerator getInstance() {
        if (instance == null) instance = new HeightMapGenerator();
        return instance;
    }

    /**
	 * avgDiamondVals: Given the i,j location as the center of a diamond,
	 * average the data values at the four corners of the diamond and
	 * return it. "Stride" represents the distance from the diamond center
	 * to a diamond corner.
	 *
	 * Called by fill2DFractArray.
	 **/
    private static double avgDiamondVals(int i, int j, int stride, int size, int subSize, Double[] fa) {
        if (i == 0) return ((fa[(i * size) + j - stride] + fa[(i * size) + j + stride] + fa[((subSize - stride) * size) + j] + fa[((i + stride) * size) + j]) * 0.25); else if (i == size - 1) return ((fa[(i * size) + j - stride] + fa[(i * size) + j + stride] + fa[((i - stride) * size) + j] + fa[((0 + stride) * size) + j]) * 0.25); else if (j == 0) return ((fa[((i - stride) * size) + j] + fa[((i + stride) * size) + j] + fa[(i * size) + j + stride] + fa[(i * size) + subSize - stride]) * 0.25); else if (j == size - 1) return ((fa[((i - stride) * size) + j] + fa[((i + stride) * size) + j] + fa[(i * size) + j - stride] + fa[(i * size) + 0 + stride]) * 0.25); else return ((fa[((i - stride) * size) + j] + fa[((i + stride) * size) + j] + fa[(i * size) + j - stride] + fa[(i * size) + j + stride]) * 0.25);
    }

    /**
	 * avgSquareVals: Given the i,j location as the center of a square,
	 * average the data values at the four corners of the square and return
	 * it. "Stride" represents half the length of one side of the square.
	 *
	 * Called by fill2DFractArray.
	 **/
    private static double avgSquareVals(int i, int j, int stride, int size, Double[] fa) {
        return ((fa[((i - stride) * size) + j - stride] + fa[((i - stride) * size) + j + stride] + fa[((i + stride) * size) + j - stride] + fa[((i + stride) * size) + j + stride]) * 0.25);
    }

    /**
	 * powerOf2: Returns true if size is a power of 2. Returns false if size is
	 * not a power of 2, or is zero.
	 **/
    private static boolean isPowerOf2(int size) {
        int bitcount = 0;
        for (int i = 0; i < 32; i++) if ((size & (1 << i)) != 0) bitcount++;
        if (bitcount == 1) return true; else return false;
    }

    private static double fractRand(double arg0) {
        return ((Math.random() * (arg0 * 2)) - (arg0 / 2));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Double> generate(HeightMapData arg0) {
        Double[] data;
        int stride;
        boolean oddLine;
        int subSize;
        double ratio, scale;
        int i, j;
        if (arg0.mapSize == 1 || !isPowerOf2(arg0.mapSize)) {
            return null;
        }
        subSize = arg0.mapSize;
        arg0.mapSize++;
        data = new Double[arg0.mapSize * arg0.mapSize];
        ratio = Math.pow(2.0, -arg0.amplitude);
        scale = arg0.heightScale * ratio;
        stride = subSize >> 1;
        data[(0 * arg0.mapSize) + 0] = data[(subSize * arg0.mapSize) + 0] = data[(subSize * arg0.mapSize) + subSize] = data[(0 * arg0.mapSize) + subSize] = 0.0;
        while (stride != 0) {
            for (i = stride; i < subSize; i += stride) {
                for (j = stride; j < subSize; j += stride) {
                    data[(i * arg0.mapSize) + j] = scale * fractRand(0.5) + avgSquareVals(i, j, stride, arg0.mapSize, data);
                    j += stride;
                }
                i += stride;
            }
            oddLine = false;
            for (i = 0; i < subSize; i += stride) {
                oddLine = !oddLine;
                for (j = 0; j < subSize; j += stride) {
                    if (oddLine && j != 0) j += stride;
                    data[(i * arg0.mapSize) + j] = scale * fractRand(0.5) + avgDiamondVals(i, j, stride, arg0.mapSize, subSize, data);
                    if (i == 0) data[(subSize * arg0.mapSize) + j] = data[(i * arg0.mapSize) + j];
                    if (j == 0) data[(i * arg0.mapSize) + subSize] = data[(i * arg0.mapSize) + j];
                    j += stride;
                }
            }
            scale *= ratio;
            stride >>= 1;
        }
        return (List<Double>) Arrays.asList(data);
    }
}
