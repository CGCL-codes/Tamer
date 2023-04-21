package Tests;

import org.junit.Assert;
import org.junit.Test;
import fitnesseTfs.TfRunner;

public class IntegratedTfRunnerTests {

    @Test
    public void canGetWorkspaceOutput() {
        TfRunner runner = new TfRunner();
        runner.setPath("C:\\program files\\Microsoft Visual Studio 9.0\\Common7\\IDE\\");
        String output = runner.execute("WORKSPACES", "");
        Assert.assertTrue(output.contains("LAPTOP-LEA-07"));
    }
}
