package barsuift.simLife.j3d.environment;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import barsuift.simLife.JaxbTester;
import barsuift.simLife.j3d.DisplayDataCreatorForTests;
import static org.fest.assertions.Assertions.assertThat;

public class Environment3DStateTest {

    private final JaxbTester<Environment3DState> tester = new JaxbTester<Environment3DState>(getClass());

    @BeforeMethod
    protected void init() throws Exception {
        tester.init();
    }

    @AfterMethod
    protected void clean() {
        tester.clean();
    }

    @Test
    public void readWriteJaxb() throws Exception {
        Environment3DState originalState = DisplayDataCreatorForTests.createRandomEnvironment3DState();
        tester.write(originalState);
        Environment3DState readState = tester.read();
        assertThat(readState).isEqualTo(originalState);
    }
}
