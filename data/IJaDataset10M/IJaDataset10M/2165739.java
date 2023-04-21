package shiva.domain.operation.filter;

import shiva.domain.operation.Restriction;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 */
public interface Filter {

    /**
	 * 
	 * @param field
	 * @param type
	 * @param value
	 */
    public void addRestriction(String field, String type, String value);

    /**
	 * 
	 * @param restriction
	 */
    public void addRestriction(Restriction restriction);

    /**
	 * 
	 */
    public void clearRestrictions();

    /**
	 * 
	 * @param restrictAll
	 * @return
	 */
    public String getRestrictStatement(boolean restrictAll);

    /**
	 * 
	 * @return
	 */
    public boolean hasRestrictions();
}
