package tudresden.ocl20.benchmark.common;

public class Helper {

    /**
	 * Extracts Filename from a path WITHOUT extension.
	 * 
	 * @param path 
	 * 
	 * @return 
	 */
    public static String getFileNameFromPath(String path) {
        path = path.substring(Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/')) + 1);
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex != -1) {
            return path.substring(0, path.lastIndexOf('.'));
        } else {
            return path;
        }
    }

    /**
	 * Gets the full file name from path.
	 * 
	 * @param path 
	 * 
	 * @return the full file name from path
	 */
    public static String getFullFileNameFromPath(String path) {
        return path.substring(Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/')) + 1);
    }

    /**
	 * Sets the value to each element of the array.
	 * 
	 * @param array 
	 * @param value 
	 */
    public static void setArrayElements(int[] array, int value) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = value;
        }
    }
}
