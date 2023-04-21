package net.sf.indricotherium.sql.eventbuilder;

import java.sql.Types;
import java.util.List;

/**
 * @author M�rio Valentim Junior
 * @version $Revision: 1.1 $
 */
public interface Builder {

    /**
	 * Returns the SQL.
	 * 
	 * @return
	 * 		The builded and formated SQL.
	 * @since 1.1
	 */
    public String toString();

    /**
	 * Retrieves the paremeters used in the SQL in the same order that they were inserted.
	 * 
	 * @return
	 * 		The parameters of the SQL.
	 * @since 1.1
	 */
    public List getParameters();

    /**
	 * Retrieves the {@link Types} associated with the parameters used in the SQL. These types
	 * retain the same order that they were inserted.
	 * 
	 * @return
	 * 		The {@link Types}
	 * @since 1.1
	 */
    public List getTypes();
}
