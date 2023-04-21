package freeguide.test.fast;

import freeguide.common.lib.fgspecific.data.TVProgramme;

public class TVProgrammeHashCodeFastTest {

    public TVProgrammeHashCodeFastTest() {
    }

    public void run() throws Exception {
        test_NullEverything();
    }

    /**
     * We can get the hashCode of a programme even if it has no title etc.
     */
    private void test_NullEverything() throws Exception {
        TVProgramme prog = new TVProgramme();
        prog.hashCode();
    }
}
