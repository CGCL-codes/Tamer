package simpatest;

import alice.simpa.*;

public class TestLogger {

    public static void main(String[] args) throws Exception {
        SIMPALauncher.launchApplication("test", "simpatest.Agent005", "a0");
        SIMPALauncher.registerLogger("test", new alice.simpa.tools.BasicLogger());
    }
}
