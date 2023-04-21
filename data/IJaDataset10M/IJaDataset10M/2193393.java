package barsuift.simLife.process;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import barsuift.simLife.JaxbTester;
import barsuift.simLife.UtilDataCreatorForTests;
import static org.fest.assertions.Assertions.assertThat;

public class ConditionalTaskStateTest {

    private final JaxbTester<ConditionalTaskState> tester = new JaxbTester<ConditionalTaskState>(getClass());

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
        ConditionalTaskState originalState = UtilDataCreatorForTests.createRandomConditionalTaskState();
        tester.write(originalState);
        ConditionalTaskState readState = tester.read();
        assertThat(readState).isEqualTo(originalState);
    }
}
