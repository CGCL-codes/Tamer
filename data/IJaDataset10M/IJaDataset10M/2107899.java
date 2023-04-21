package net.sourceforge.javautil.database.query;

import java.util.Collection;
import net.sourceforge.javautil.common.ReflectionUtil;
import net.sourceforge.javautil.database.query.sql.QueryBuilderSelect;

/**
 * A fragment that contains criteria.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: IQueryFragmentCriteriaParameter.java 2286 2010-06-14 04:18:27Z ponderator $
 */
public interface IQueryFragmentCriteriaParameter extends IQueryFragment<QueryBuilderSelect> {

    /**
	 * @return The named parameters in this fragment
	 */
    String[] getNamedParameters();
}
