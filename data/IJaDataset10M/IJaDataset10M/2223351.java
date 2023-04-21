package uk.co.cocking.runner.test;

import java.io.IOException;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import uk.co.cocking.runner.CommandRunner;

public class CommandRunnerTest {

    CommandRunner runner = new CommandRunner();

    @Test
    public void returnsReturnCodeFromExecutedCommand() throws InterruptedException, IOException {
        int returnCode = runClass(MainClassWithNonZeroReturnCode.class);
        assertThat(returnCode, equalTo(42));
    }

    @Test
    public void capturesSysErr() throws InterruptedException, IOException {
        runClass(ClassWithNoMainMethod.class);
        assertThat(runner.sysErr(), containsString("Exception in thread \"main\" java.lang.NoSuchMethodError: main"));
        assertThat(runner.sysOut(), equalTo(""));
    }

    @Test
    public void capturesSysOut() throws InterruptedException, IOException {
        runClass(Soliloquy.class);
        assertThat(runner.sysOut(), containsString("O, what a rogue and peasant slave am I!"));
        assertThat(runner.sysErr(), equalTo(""));
    }

    private int runClass(Class<?> class1) throws InterruptedException, IOException {
        return runner.run("java -cp bin " + class1.getCanonicalName());
    }
}
