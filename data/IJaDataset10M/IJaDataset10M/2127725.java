package net.community.apps.eclipse.cp2pom;

import net.community.chest.ui.helpers.table.EnumTableColumn;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jul 27, 2009 10:29:43 AM
 */
public class RepositoryEntryTableColumn extends EnumTableColumn<RepositoryEntryColumns> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3022370830251190041L;

    public RepositoryEntryTableColumn(RepositoryEntryColumns colIndex, String colName, int colWidth) {
        super(RepositoryEntryColumns.class, colIndex, colWidth);
        setColumnName(colName);
    }
}
