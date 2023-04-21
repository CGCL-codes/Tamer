package net.sf.dbchanges.actions;

import net.sf.dbchanges.common.DbChangesException;

/**
 * @author olex
 */
public class ParamNotFoundException extends DbChangesException {

    private String id;

    public ParamNotFoundException(String id) {
        this.id = id;
    }

    /**
	 * @see java.lang.Throwable#getMessage()
	 */
    @Override
    public String getMessage() {
        return "Option missing: " + id;
    }
}
