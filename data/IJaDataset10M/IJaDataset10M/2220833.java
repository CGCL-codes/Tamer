package net.sourceforge.squirrel_sql.plugins.netezza.exp;

import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpander;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpanderFactory;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;

/**
 * Implementation of INodeExpanderFactory for Netezza synonyms
 * 
 */
public class NetezzaSynonymInodeExpanderFactory implements INodeExpanderFactory {

    /**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpanderFactory#createExpander(net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType)
	 */
    public INodeExpander createExpander(DatabaseObjectType type) {
        return new NetezzaSynonymParentExpander();
    }

    /**
	 * @see net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpanderFactory#getParentLabelForType(net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType)
	 */
    public String getParentLabelForType(DatabaseObjectType type) {
        return "SYNONYM";
    }
}
