package net.sf.gamine.render;

import net.sf.gamine.common.*;
import java.util.*;

/**
 * A HeightField is a {@link Mesh} defined by a set of height values on a rectangular grid.  It is most often used
 * to create landscapes.  A HeightField can be defined in two ways: you can explicitly specify the height at each
 * point on the grid, or you can have it generate a random set of height values.  The latter is an easy way to create
 * random terrain for a game.
 */
public class HeightField extends Mesh {

    private final int numRows;

    private final int numColumns;

    private final float xwidth;

    private final float zwidth;

    /**
   * Create a HeightField based on a grid of height values.  The grid is in the X-Z plane, with increasing height
   * being in the Y direction.
   *
   * @param numRows      the number of rows (point along the X axis) in the grid
   * @param numColumns   the number of columns (point along the Z axis) in the grid
   * @param xwidth       the width of the height field in the X direction.  It is centered at the origin, so it extends
   *                     from -xwidth/2 to xwidth/2
   * @param zwidth       the width of the height field in the Z direction.  It is centered at the origin, so it extends
   *                     from -zwidth/2 to zwidth/2
   * @param height       the height value at each grid point.  The values are arranged in row order: all the values in the
   *                     first row, then all the values in the second row, etc.  The first element corresponds to
   *                     x = -xwidth/2, z = -zwidth/2.
   */
    public HeightField(int numRows, int numColumns, float xwidth, float zwidth, float height[]) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.xwidth = xwidth;
        this.zwidth = zwidth;
        buildHeightField(numRows, numColumns, xwidth, zwidth, height);
    }

    /**
   * This is called by the constructors to actually generate the mesh.
   */
    private void buildHeightField(int numRows, int numColumns, float xwidth, float zwidth, float height[]) {
        if (height.length != numRows * numColumns) throw new IllegalArgumentException("The number of height values must equal numRows*numColumns");
        if (numRows < 2) throw new IllegalArgumentException("A HeightField must have at least 2 rows");
        if (numColumns < 2) throw new IllegalArgumentException("A HeightField must have at least 2 columns");
        float xscale = xwidth / (numRows - 1);
        float zscale = zwidth / (numColumns - 1);
        float xoffset = -0.5f * xwidth;
        float zoffset = -0.5f * zwidth;
        Float3 position[] = new Float3[height.length];
        Float3 normal[] = new Float3[height.length];
        int index = 0;
        for (int i = 0; i < numRows; i++) for (int j = 0; j < numColumns; j++) {
            position[index] = new Float3(i * xscale + xoffset, height[index], j * zscale + zoffset);
            index++;
        }
        index = 0;
        for (int i = 0; i < numRows; i++) for (int j = 0; j < numColumns; j++) {
            Float3 dx, dz;
            if (j == 0) dx = position[index + 1].minus(position[index]); else if (j == numColumns - 1) dx = position[index].minus(position[index - 1]); else dx = position[index + 1].minus(position[index - 1]);
            if (i == 0) dz = position[index + numColumns].minus(position[index]); else if (i == numRows - 1) dz = position[index].minus(position[index - numColumns]); else dz = position[index + numColumns].minus(position[index - numColumns]);
            normal[index] = dx.cross(dz).normalize();
            index++;
        }
        Int3 faceVertexIndices[] = new Int3[(numRows - 1) * (numColumns - 1) * 2];
        index = 0;
        for (int i = 0; i < numRows - 1; i++) for (int j = 0; j < numColumns - 1; j++) {
            int base = i * numColumns + j;
            faceVertexIndices[index++] = new Int3(base, base + 1, base + numColumns + 1);
            faceVertexIndices[index++] = new Int3(base, base + numColumns + 1, base + numColumns);
        }
        initialize(position, normal, faceVertexIndices);
    }

    /**
   * Create a random HeightField.
   *
   * @param numRows      the number of rows (point along the X axis) in the grid
   * @param numColumns   the number of columns (point along the Z axis) in the grid
   * @param xwidth       the width of the height field in the X direction.  It is centered at the origin, so it extends
   *                     from -xwidth/2 to xwidth/2
   * @param zwidth       the width of the height field in the Z direction.  It is centered at the origin, so it extends
   *                     from -zwidth/2 to zwidth/2
   * @param minHeight    the smallest height value that should appear in the generated height field
   * @param maxHeight    the smallest height value that should appear in the generated height field
   * @param roughness    determines how rough the generated surface is.  This is typically between 0 (very smooth) and
   *                     1 (very rough)
   * @param randomSeed   the seed to use for the random number generator.  Each value produces a different random height
   *                     field.
   */
    public HeightField(int numRows, int numColumns, float xwidth, float zwidth, float minHeight, float maxHeight, float roughness, int randomSeed) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.xwidth = xwidth;
        this.zwidth = zwidth;
        float height[] = new float[numRows * numColumns];
        float xscale = xwidth / numRows;
        float zscale = zwidth / numColumns;
        float xoffset = -0.5f * xwidth;
        float zoffset = -0.5f * zwidth;
        float noiseWidth = Math.max(xwidth, zwidth);
        Random random = new Random(randomSeed);
        float noiseAmplitude = 1.0f;
        for (int noiseRows = 2; ; noiseRows *= 2) {
            float noiseScale = noiseWidth / noiseRows;
            if (noiseScale < xscale && noiseScale < zscale) break;
            float noise[] = new float[noiseRows * noiseRows];
            for (int i = 0; i < noise.length; i++) noise[i] = noiseAmplitude * random.nextFloat();
            int index = 0;
            for (int i = 0; i < numRows; i++) for (int j = 0; j < numColumns; j++) height[index++] += getNoise(noise, noiseRows, noiseWidth, i * xscale + xoffset, j * zscale + zoffset);
            noiseAmplitude *= roughness;
        }
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        for (float value : height) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        float heightScale = (min == max ? 0.0f : (maxHeight - minHeight) / (max - min));
        float heightOffset = minHeight - min;
        for (int i = 0; i < height.length; i++) height[i] = heightScale * height[i] + heightOffset;
        buildHeightField(numRows, numColumns, xwidth, zwidth, height);
    }

    /**
   * This is called by the random height field generator.  It calculates the value of a random noise field at a point.
   */
    private static float getNoise(float noise[], int noiseRows, float noiseWidth, float x, float z) {
        float scale = noiseRows / noiseWidth;
        float offset = 0.5f * noiseWidth;
        float noisex = (x + offset) * scale;
        float noisez = (z + offset) * scale;
        int baseRow = Math.min(Math.max((int) noisex, 0), noiseRows - 2);
        int baseColumn = Math.min(Math.max((int) noisez, 0), noiseRows - 2);
        float result = 0.0f;
        for (int row = baseRow; row < baseRow + 2; row++) for (int column = baseColumn; column < baseColumn + 2; column++) {
            float gridValue = noise[row * noiseRows + column];
            float dx = noisex - row;
            float dz = noisez - column;
            float dist2 = dx * dx + dz * dz;
            if (dist2 < 1) result += gridValue * (dist2 * dist2 - 2 * dist2 + 1);
        }
        return result;
    }

    /**
   * Compute the height and normal vector of the HeightField at a point in the X-Z plane.  If the point specified is
   * outside the bounds of the HeightField, the properties of the nearest point on the boundary are returned.
   *
   * @param x       the X coordinate of the point to get
   * @param z       the Z coordinate of the point to get
   * @param normal  on exit, this contains the surface normal of the point
   * @return the height at the specified point
   */
    public float computeSurfacePoint(float x, float z, Float3 normal) {
        x += 0.5 * xwidth;
        z += 0.5 * zwidth;
        float s = x * (numRows / xwidth);
        float t = z * (numColumns / zwidth);
        int row1 = (int) s;
        int col1 = (int) t;
        int row2 = row1 + 1;
        int col2 = col1 + 1;
        float row1weight = 1 - (s - row1);
        float col1weight = 1 - (t - col1);
        if (row1 < 0) {
            row1 = row2 = 0;
            row1weight = 1;
        }
        if (row1 >= numRows - 1) {
            row1 = row2 = numRows - 1;
            row1weight = 1;
        }
        if (col1 < 0) {
            col1 = col2 = 0;
            col1weight = 1;
        }
        if (col1 >= numColumns - 1) {
            col1 = col2 = numColumns - 1;
            col1weight = 1;
        }
        float row2weight = 1 - row1weight;
        float col2weight = 1 - col1weight;
        int index11 = 3 * (row1 * numColumns + col1);
        int index12 = 3 * (row1 * numColumns + col2);
        int index21 = 3 * (row2 * numColumns + col1);
        int index22 = 3 * (row2 * numColumns + col2);
        float weight11 = row1weight * col1weight;
        float weight12 = row1weight * col2weight;
        float weight21 = row2weight * col1weight;
        float weight22 = row2weight * col2weight;
        normal.x = weight11 * normals.get(index11) + weight12 * normals.get(index12) + weight21 * normals.get(index21) + weight22 * normals.get(index22);
        normal.y = weight11 * normals.get(index11 + 1) + weight12 * normals.get(index12 + 1) + weight21 * normals.get(index21 + 1) + weight22 * normals.get(index22 + 1);
        normal.z = weight11 * normals.get(index11 + 2) + weight12 * normals.get(index12 + 2) + weight21 * normals.get(index21 + 2) + weight22 * normals.get(index22 + 2);
        normal.normalize();
        return weight11 * positions.get(index11 + 1) + weight12 * positions.get(index12 + 1) + weight21 * positions.get(index21 + 1) + weight22 * positions.get(index22 + 1);
    }
}
