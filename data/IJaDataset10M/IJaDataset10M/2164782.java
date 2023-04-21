package org.impalaframework.interactive.command;

import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandLineInputCapturer;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.GlobalCommandState;

public class InteractiveTestCommandTest extends ManualInteractiveTestCommandTest {

    @Override
    public void testInteractive() throws Exception {
        super.testInteractive();
        CommandPropertyValue property = GlobalCommandState.getInstance().getProperty("lastCommand");
        assertEquals("run", property.getValue());
    }

    @Override
    protected CommandLineInputCapturer getInputCapturer() {
        CommandLineInputCapturer inputCapturer = new CommandLineInputCapturer() {

            @Override
            public String capture(CommandInfo info) {
                if (info.getPropertyName().equals("commandText")) {
                    return "run";
                }
                return null;
            }
        };
        return inputCapturer;
    }
}
