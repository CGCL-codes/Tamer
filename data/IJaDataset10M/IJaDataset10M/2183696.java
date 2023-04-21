package net.sourceforge.javautil.developer.web.library.jsf;

import net.sourceforge.javautil.database.query.QueryDataScroller;
import net.sourceforge.javautil.developer.web.library.jsf.data.DataModelQueryScroller;

/**
 * Additional JSF specific extensions related to this library. 
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JSFGraceletCategoryExtension {

    /**
	 * @param scroller The scroller for which to create a model
	 * @return A model that will allow integration with JSF data tables
	 */
    public static DataModelQueryScroller getScrollerModel(QueryDataScroller scroller) {
        return new DataModelQueryScroller(scroller);
    }
}
