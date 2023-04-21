package starcraft.gamemodel.utils;

import starcraft.gamemodel.shared.Planet;

public class PlanetSystemArrayUtils {

    public static Planet[][] addRowBefore(Planet[][] array) {
        int columns = array[0].length;
        Planet[][] result = new Planet[array.length + 1][columns];
        System.arraycopy(array, 0, result, 1, array.length);
        result[0] = new Planet[columns];
        return result;
    }

    public static int[][] addRowBefore(int[][] array) {
        int columns = array[0].length;
        int[][] result = new int[array.length + 1][columns];
        System.arraycopy(array, 0, result, 1, array.length);
        result[0] = new int[columns];
        return result;
    }

    public static Planet[][] addRowAfter(Planet[][] array) {
        int columns = array.length == 0 ? 0 : array[0].length;
        Planet[][] result = new Planet[array.length + 1][columns];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = new Planet[columns];
        return result;
    }

    public static int[][] addRowAfter(int[][] array) {
        int columns = array.length == 0 ? 0 : array[0].length;
        int[][] result = new int[array.length + 1][columns];
        System.arraycopy(array, 0, result, 0, array.length);
        result[array.length] = new int[columns];
        return result;
    }

    public static Planet[][] addColumnBefore(Planet[][] array) {
        int rows = array.length;
        for (int row = 0; row < rows; row++) {
            Planet[] rowArray = array[row];
            Planet[] newRowArray = new Planet[rowArray.length + 1];
            System.arraycopy(rowArray, 0, newRowArray, 1, rowArray.length);
            array[row] = newRowArray;
        }
        return array;
    }

    public static int[][] addColumnBefore(int[][] array) {
        int rows = array.length;
        for (int row = 0; row < rows; row++) {
            int[] rowArray = array[row];
            int[] newRowArray = new int[rowArray.length + 1];
            System.arraycopy(rowArray, 0, newRowArray, 1, rowArray.length);
            array[row] = newRowArray;
        }
        return array;
    }

    public static Planet[][] addColumnAfter(Planet[][] array) {
        int rows = array.length;
        for (int row = 0; row < rows; row++) {
            Planet[] rowArray = array[row];
            Planet[] newRowArray = new Planet[rowArray.length + 1];
            System.arraycopy(rowArray, 0, newRowArray, 0, rowArray.length);
            array[row] = newRowArray;
        }
        return array;
    }

    public static int[][] addColumnAfter(int[][] array) {
        int rows = array.length;
        for (int row = 0; row < rows; row++) {
            int[] rowArray = array[row];
            int[] newRowArray = new int[rowArray.length + 1];
            System.arraycopy(rowArray, 0, newRowArray, 0, rowArray.length);
            array[row] = newRowArray;
        }
        return array;
    }
}
