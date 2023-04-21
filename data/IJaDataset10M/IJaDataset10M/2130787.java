package test;

import junit.framework.TestCase;
import pobs.PContext;
import pobs.PMatch;
import pobs.PParser;
import pobs.PScanner;
import pobs.control.PDisableActions;
import pobs.scanner.PStringScanner;

/**
 * @author Franz-Josef Elmer
 */
public class PDisableActionsTest extends TestCase {

    static class MockParser extends PParser {

        public boolean skip;

        protected PMatch parse(PScanner input, long begin, PContext context) {
            skip = context.getDirective().isActions();
            return new PMatch(true, 1);
        }
    }

    public void testDisableADisabledDirective() {
        boolean before = false;
        boolean mockState = false;
        check(before, mockState);
    }

    public void testDisableAnEnabledDirective() {
        boolean before = true;
        boolean mockState = false;
        check(before, mockState);
    }

    private void check(boolean stateBefore, boolean expectedStateInMock) {
        MockParser mock = new MockParser();
        PParser parser = mock.addControl(new PDisableActions());
        check(stateBefore, expectedStateInMock, mock, parser);
    }

    /**
	 * @param stateBefore			Actions state before possible modification 
	 * 								and expected state after modification.
	 * @param expectedStateInMock	Expected actions state during modification. 
	 * @param mock					Mock parser.
	 * @param parser				Disabled/Enable wrapper around mock parser.
	 */
    protected void check(boolean stateBefore, boolean expectedStateInMock, MockParser mock, PParser parser) {
        PStringScanner input = new PStringScanner("a");
        PContext context = new PContext();
        context.getDirective().setActions(stateBefore);
        parser.process(input, 0, context);
        assertEquals("mock", expectedStateInMock, mock.skip);
        assertEquals("directive", stateBefore, context.getDirective().isActions());
    }
}
