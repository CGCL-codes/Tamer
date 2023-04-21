package ucalgary.ebe.ci.gestures.recognition.direction;

/**
 * @author hkolenda
 * 
 * licence http://www.mozilla.org/MPL/
 * 
 * adapteed from the optimoz gesture recognition for mozilla
 * 
 */
public class RelativeCoordsRecongition implements IDirectionRecognizer {

    /**
     * tolerance for diagonal movement. see processCoordinates() value from
     * about:config-> mozgest.diagonalTolerance = 60
     */
    int dTolerance = 60;

    /**
     * grid size; minimal gesture has to be 'grid' pixels long value from
     * about:config-> mozgest.grid =15
     */
    int grid = 15;

    public char putCoord(int x, int y) {
        char diretion = recognizeDirection(x, y);
        return diretion;
    }

    public char recognizeDirection(int x, int y) {
        int x_dir = x;
        int y_dir = y;
        int x_dir_abs = Math.abs(x_dir);
        int y_dir_abs = Math.abs(y_dir);
        if ((x_dir_abs >= grid / 2 || y_dir_abs >= grid / 2)) {
            if ((dTolerance != 0) && (x_dir_abs != 0) && (y_dir_abs != 0) && ((x_dir_abs / y_dir_abs >= (1 - dTolerance / 100) && x_dir_abs / y_dir_abs <= 1) || (y / x_dir_abs >= (1 - dTolerance / 100) && y_dir_abs / x_dir_abs <= 1))) {
                if (x_dir < 0 && y_dir > 0) {
                    return Directions.DOWN_LEFT;
                } else if (x_dir > 0 && y_dir > 0) {
                    return Directions.DOWN_RIGHT;
                } else if (x_dir < 0 && y_dir < 0) {
                    return Directions.UP_LEFT;
                } else if (x_dir > 0 && y_dir < 0) {
                    return Directions.UP_RIGHT;
                }
            } else if (x_dir_abs > y) {
                if (x_dir > 0) {
                    return Directions.RIGHT;
                } else if (x_dir < 0) {
                    return Directions.LEFT;
                }
            } else if (x_dir_abs < y) {
                if (y_dir > 0) {
                    return Directions.DOWN;
                } else if (y_dir < 0) {
                    return Directions.UP;
                }
            }
        }
        return Directions.NONE;
    }

    public void start() {
    }
}
