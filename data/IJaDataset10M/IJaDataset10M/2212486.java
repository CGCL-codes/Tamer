package net.community.apps.apache.maven.pom2cpsync;

import net.community.chest.ui.helpers.table.EnumTableColumn;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 17, 2008 7:43:11 AM
 */
public class DependencyDetailsTableColumn extends EnumTableColumn<DependencyDetailsColumns> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5051720488547765828L;

    public DependencyDetailsTableColumn(DependencyDetailsColumns colIndex, String colName, int colWidth) {
        super(DependencyDetailsColumns.class, colIndex, colWidth);
        setColumnName(colName);
    }
}
