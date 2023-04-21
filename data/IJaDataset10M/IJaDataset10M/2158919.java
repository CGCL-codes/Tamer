package org.eaasyst.tables.apps;

import javax.servlet.ServletException;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.apps.BrowseApplicationBase;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.utils.StringUtils;
import org.eaasyst.tables.data.impl.TableDefDabFactory;

/**
 * <p>This application browses Table Definitions.</p>
 *
 * @author Jeff Chilton
 */
public class TableDefBrowse extends BrowseApplicationBase {

    /**
	 * <p>Constructs a new "TableDefBrowse" object.</p>
	 */
    public TableDefBrowse() throws ServletException {
        className = StringUtils.computeClassName(getClass());
        setApplTitleKey("title.table.def.browse");
        setFormName("tableDefBrowse");
        setFormType("org.apache.struts.action.DynaActionForm");
        setViewComponent(EaasyStreet.getProperty(Constants.CFG_PAGES_TABLES_TBLDEFBROWSE));
        setAccessBeanFactory(new TableDefDabFactory());
    }
}
