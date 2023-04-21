package barsuift.simLife.j3d.environment;

import org.testng.annotations.Test;
import static org.fest.assertions.Assertions.assertThat;

public class Wind3DStateFactoryTest {

    @Test
    public void testCreateWind3DState() {
        Wind3DStateFactory factory = new Wind3DStateFactory();
        Wind3DState windState = factory.createWind3DState();
        assertThat(windState.getWindTask()).isNotNull();
    }
}
