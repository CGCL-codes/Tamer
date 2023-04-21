package risk.game.utility;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;

public class ComputeTool {

    private static Random ran = new Random();

    public static Vector randomVector(Vector sourceVector) {
        return randomVector(sourceVector, sourceVector.size());
    }

    public static Vector randomVector(Vector sourceVector, int size) {
        if (size < 1) return new Vector(sourceVector);
        sourceVector.trimToSize();
        if (size > sourceVector.size()) return null;
        int randomVectorIndex;
        int sourceSize = sourceVector.size();
        boolean[] boolArray = new boolean[sourceSize];
        for (int i = boolArray.length - 1; i > -1; i--) boolArray[i] = true;
        Vector vec = new Vector(size + 1);
        do {
            randomVectorIndex = ran.nextInt(sourceSize);
            if (boolArray[randomVectorIndex]) {
                vec.add(sourceVector.get(randomVectorIndex));
                boolArray[randomVectorIndex] = false;
            }
        } while (vec.size() < size);
        return vec;
    }

    public static Point getTopLeftElement(int width, int height, Point centeredElement) {
        Point topleft = new Point(centeredElement);
        int down = (height + 1) / 2;
        down--;
        topleft.x -= down;
        topleft.y -= down;
        int right = (width + 1) / 2;
        right--;
        topleft.x -= right / 2;
        topleft.x -= right % 2;
        topleft.y += right / 2;
        return topleft;
    }

    public static Point getTopLeftElement(Dimension dimension, Point centeredElement) {
        return getTopLeftElement(dimension.width, dimension.height, centeredElement);
    }
}
