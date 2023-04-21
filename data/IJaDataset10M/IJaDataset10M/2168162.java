package org.sourceforge.library.user.forms;

import org.apache.struts.action.ActionForm;
import org.sourceforge.library.user.logic.LibraryManagerFacade;
import org.apache.log4j.Logger;

/**
 *
 * @author tambro 2006
 */
public abstract class LibraryForm extends ActionForm {

    protected static LibraryManagerFacade libraryManager;

    /** log4j Logger */
    private Logger logger = Logger.getLogger(getClassName());

    /** Creates a new instance of LibraryForm */
    public LibraryForm() {
    }

    public void setLibraryManagerFacade(LibraryManagerFacade LibraryManager) {
        this.libraryManager = LibraryManager;
        if (logger.isDebugEnabled()) {
            logger.debug("Initializing LibraryManagerFacade in Form with:" + libraryManager);
        }
    }

    public abstract String getClassName();
}
