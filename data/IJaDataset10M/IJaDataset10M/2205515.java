package screen.tools.sbs.actions.defaults;

import screen.tools.sbs.actions.Action;
import screen.tools.sbs.cmake.SBSCMakeCleaner;
import screen.tools.sbs.context.ContextException;
import screen.tools.sbs.context.ContextHandler;
import screen.tools.sbs.context.defaults.ContextKeys;
import screen.tools.sbs.context.defaults.PackContext;
import screen.tools.sbs.context.defaults.SbsFileAndPathContext;
import screen.tools.sbs.objects.Pack;

/**
 * Action to clean tests makefile and environment files for a given component
 * 
 * @author Ratouit Thomas
 *
 */
public class ActionTestClean implements Action {

    private ContextHandler contextHandler;

    /**
	 * Cleans component test
	 * @throws ContextException 
	 */
    public void perform() throws ContextException {
        SBSCMakeCleaner cleaner = new SBSCMakeCleaner();
        Pack pack = contextHandler.<PackContext>get(ContextKeys.TEST_PACK).getPack();
        String sbsXmlPath = contextHandler.<SbsFileAndPathContext>get(ContextKeys.SBS_FILE_AND_PATH).getSbsXmlPath();
        cleaner.clean(pack, sbsXmlPath + "test/");
    }

    public void setContext(ContextHandler contextHandler) {
        this.contextHandler = contextHandler;
    }
}
