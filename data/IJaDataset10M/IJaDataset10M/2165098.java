package net.sf.doolin.cli;

import net.sf.doolin.cli.support.CLIException;
import net.sf.doolin.util.Strings;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for {@link CLIException}.
 * 
 * @author Damien Coraboeuf
 * 
 */
public class TestCLIException {

    @BeforeClass
    public static void load() {
        Strings.add("net.sf.doolin.cli.TestCLIExceptionBundle");
    }

    @Test
    public void testDisplay_NoShow() {
        CLIOutput output = Mockito.mock(CLIOutput.class);
        CLIException exception = new CLIException(2, "error.code", new NullPointerException("Test null"), "param1", "param2");
        exception.display(output);
        Mockito.verify(output).error(Integer.valueOf(2), "Error for param1 and param2", null, exception, "Run with -e option to see error details");
    }

    @Test
    public void testDisplay_ShowFalse() {
        CLIOutput output = Mockito.mock(CLIOutput.class);
        CLIException exception = new CLIException(2, "error.code", new NullPointerException("Test null"), "param1", "param2");
        exception.setShowError(false);
        exception.display(output);
        Mockito.verify(output).error(Integer.valueOf(2), "Error for param1 and param2", false, exception, "Run with -e option to see error details");
    }

    @Test
    public void testDisplay_ShowTrue() {
        CLIOutput output = Mockito.mock(CLIOutput.class);
        CLIException exception = new CLIException(2, "error.code", new NullPointerException("Test null"), "param1", "param2");
        exception.setShowError(true);
        exception.display(output);
        Mockito.verify(output).error(Integer.valueOf(2), "Error for param1 and param2", true, exception, "Run with -e option to see error details");
    }
}
