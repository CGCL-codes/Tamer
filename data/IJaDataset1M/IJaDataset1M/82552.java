package com.game.tests;

/**
 *
 */
public class ProgramArgumentTest extends GameTest {

    private static final String PARAM = "gameRocks";

    private String arg;

    public ProgramArgumentTest(String arg) {
        this.arg = arg;
    }

    @Override
    public void startTest() {
        title("Program  Argument Test");
        if (PARAM.equals(arg)) {
            msg(String.format("Found Argument [%s]", PARAM));
            passed(2, "Program Argument test passed successfully");
            return;
        }
        msg(String.format("In-order to pass this test,run program with an argument name [%s]", PARAM));
        gameOver();
    }
}
