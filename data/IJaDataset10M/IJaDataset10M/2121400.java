package base.testing;

import manager.Manager;

public class TestFull {

    public static void DoTest() {
        System.out.println("Starting full bot test.");
        Manager MNGR = new Manager();
        MNGR.run();
    }
}
