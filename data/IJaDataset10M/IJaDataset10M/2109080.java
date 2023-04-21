package com.xiaxueqi.web.portal;

import java.util.Map;
import com.niagara.dao.Page;
import com.niagara.service.base.BaseService;
import com.niagara.web.struts2.Struts2Utils;

public class BasePortalAction {

    private static final long serialVersionUID = -5752316500353193030L;

    /**
	 * 提供条件筛选式搜索返回的Page
	 * 
	 * @param bs
	 *            BaseService
	 * @param scope
	 *            查找的范围，决定Page&lt;T&gt; T的类型
	 * @param pageSize
	 *            分页时一页的数量
	 * @return Page&lt;scope&gt;
	 */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> getPageScope(BaseService bs, String scope, Integer pageSize) {
        Page<T> page = null;
        Map factor = Struts2Utils.getParameters();
        Integer pageNo = Struts2Utils.getIntParameter("niagara_params_pageNo", Integer.valueOf(1));
        try {
            Class c = Class.forName(scope);
            page = bs.findForPaging(c, factor, pageNo, pageSize);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return page;
    }
}
