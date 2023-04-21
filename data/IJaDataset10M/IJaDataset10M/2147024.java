package test.openmobster.device.agent.provisioning;

import test.openmobster.device.agent.AbstractTestEnv;
import org.openmobster.device.agent.configuration.Configuration;

/**
 * @author openmobster@gmail
 *
 */
public class TestReActivation extends AbstractTestEnv {

    public void test() throws Exception {
        Configuration.getInstance().cleanup();
        this.runner.activateDevice();
    }
}
