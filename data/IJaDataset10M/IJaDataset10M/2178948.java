package net.community.apps.tools.srvident;

import net.community.chest.ui.helpers.table.EnumTableColumn;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Oct 25, 2007 9:52:55 AM
 */
public class IdTableColInfo extends EnumTableColumn<IdTableColumns> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6359588876563023075L;

    public IdTableColInfo(IdTableColumns colIndex) {
        super(IdTableColumns.class, colIndex);
    }

    public IdTableColInfo(Element elem) throws Exception {
        super(IdTableColumns.class, elem);
    }
}
