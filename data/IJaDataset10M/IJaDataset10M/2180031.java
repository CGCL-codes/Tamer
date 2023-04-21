package desperateDoctors;

import java.io.Serializable;

public class ConvertCoordinates implements Serializable {

    public static Coordinates convertToScreen(Coordinates c) {
        double x = 32 * (c.getX() - c.getY());
        double y = 16 * (c.getX() + c.getY());
        return new Coordinates(x, y);
    }

    public static Coordinates convertToIsometric(Coordinates c) {
        double x = c.getX() / 64 + c.getY() / 32;
        double y = -c.getX() / 64 + c.getY() / 32;
        return new Coordinates(x, y);
    }
}
