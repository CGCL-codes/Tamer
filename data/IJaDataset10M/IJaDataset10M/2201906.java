package net.pleso.framework.client.bl.rb;

import net.pleso.framework.client.bl.IAuthDataSource;
import net.pleso.framework.client.bl.rb.columns.IRBColumn;
import net.pleso.framework.client.bl.rb.columns.IRBDataColumn;

/**
 * Represents reference book with caption, columns and authorized data source.
 */
public interface IRB {

    /**
	 * Use this function to retrieve reference book caption, which can be
	 * displayed as short description of this data set.
	 * 
	 * @return reference book caption
	 */
    String getCaption();

    /**
	 * Defines array of columns which must represent data. Each column has some
	 * information about item displaying rules. For more info look for
	 * {@link IRBDataColumn} extenders.
	 * 
	 * @return array of reference book columns
	 */
    IRBColumn[] getColumns();

    /**
	 * Grants access for authorized data source with reference book data.
	 * 
	 * @return authorized data source
	 */
    IAuthDataSource getDataSource();
}
