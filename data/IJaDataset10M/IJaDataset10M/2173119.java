package com.asoft.common.base.web.view;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * <p>Title: 试题结果列表</p>
 * <p>Description:
 *   /--功能按钮--/  /--功能切换区--/ <br>
 *   /--       记录标题区       --/<br>
 *   /--       记录列表        --/<br>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: asoft</p>
 * @ $Author: author $
 * @ $Date: 2005/02/16 01:04:14 $
 * @ $Revision: 1.7 $
 * @ created in 2005-11-14
 *
 */
public abstract class BaseObjectListTemplet implements FBArea, FIArea, ItemListArea, ItemTitleArea {

    static Logger logger = Logger.getLogger(BaseObjectListTemplet.class);

    public List getSubtotal(String contextPath, List models) {
        logger.debug("未实现小计");
        return new ArrayList();
    }

    public List getTotal(String contextPath, List models) {
        logger.debug("未实现合计");
        return new ArrayList();
    }
}
