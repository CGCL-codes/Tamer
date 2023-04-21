package onepoint.project.module;

import onepoint.project.OpProjectSession;

/**
 * Module checked interface that will be implemented by all module checkers.
 *
 * @author mihai.costin
 */
public interface OpModuleChecker {

    /**
    * Checks the associated module values, in a single thread.
    *
    * @param session project session
    */
    void check(OpProjectSession session);
}
