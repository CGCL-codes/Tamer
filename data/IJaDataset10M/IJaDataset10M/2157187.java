package clubmixer.commons.plugins.console;

import clubmixer.commons.plugins.console.annotations.FlaggedOption;
import clubmixer.commons.plugins.console.annotations.Switch;
import clubmixer.commons.plugins.console.annotations.UnflaggedOption;

/**
 *
 * @author alex
 */
public class BasicCommandTest extends BasicCommand {

    @Switch(shortFlag = 't', longFlag = "test", helptext = "This is a test switch")
    private boolean testSwitch;

    @FlaggedOption(longFlag = "testFL", helptext = "This is a test Flagged Option", usageName = "testFlOpt")
    private String testFlaggedOption;

    @UnflaggedOption(helptext = "This is a test for an Unflagged Option")
    private Long[] testUnflaggedOptionArray;

    public BasicCommandTest() {
        super("test");
    }

    @Override
    public void execute() {
        System.out.println("Hello World");
        System.out.println("===========\n");
        System.out.println("testSwitch = " + testSwitch);
        System.out.println("testFlaggedOption = " + testFlaggedOption);
        for (Long string : testUnflaggedOptionArray) {
            System.out.println("testUnflaggedOption = " + string);
        }
    }

    public static void main(String[] args) {
        BasicCommandTest test = new BasicCommandTest();
        String[] arg = new String[4];
        arg[0] = "--testFL";
        arg[1] = "TestTestTest";
        arg[2] = "-t";
        arg[3] = "1,2,3";
        test.execute(arg);
    }
}
