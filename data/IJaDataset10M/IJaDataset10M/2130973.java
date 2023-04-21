package name.huzhenbo.java.enums;

import org.junit.Test;

public class PlanetTest {

    @Test
    public void associate_data_with_enum() {
        double earthWeight = 10d;
        double mass = earthWeight / Planet.EARTH.surfaceGravity();
        for (Planet p : Planet.values()) {
        }
    }
}
