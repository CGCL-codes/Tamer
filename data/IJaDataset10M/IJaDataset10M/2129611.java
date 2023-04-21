package com.jspx.service.favorite.impl;

import com.jspx.service.favorite.FavoriteDAO;
import com.jspx.utils.StringUtil;
import com.jspx.service.favorite.Favorite;
import com.jspx.sober.jdbc.JdbcOperations;
import com.jspx.sober.Criteria;
import com.jspx.sober.criteria.expression.Expression;
import com.jspx.sober.criteria.Order;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: 陈原
 * Date: 2006-8-29
 * Time: 22:13:53
 */
public class FavoriteDAOImpl extends JdbcOperations implements FavoriteDAO {

    public FavoriteDAOImpl() {
    }

    /**
     * 得到自己的站点消息
     *
     * @param manId
     * @param find
     * @param ipage
     * @param count
     * @return List<SiteMessage>
     */
    public List getList(final String manId, final String nodeId, final String find, final int ipage, final int count) throws Exception {
        Criteria criteria = createCriteria(Favorite.class);
        if (!StringUtil.isNULL(find)) {
            criteria = criteria.add(Expression.like("title", "%" + find + "%"));
        }
        if (!"*".equals(manId)) {
            criteria = criteria.add(Expression.eq("manId", manId));
        }
        if (!"*".equals(manId)) {
            criteria = criteria.add(Expression.eq("nodeId", nodeId));
        }
        return criteria.addOrder(Order.desc("sortType")).addOrder(Order.desc("sortDate")).addOrder(Order.desc("createDate")).setCurrentPage(ipage).setTotalCount(count).list(false);
    }

    /**
     * 得到自己的站点消息
     *
     * @param manId
     * @param find
     * @return List<SiteMessage>
     */
    public int getCount(final String manId, final String nodeId, final String find) throws Exception {
        Criteria criteria = createCriteria(Favorite.class);
        if (!StringUtil.isNULL(find)) {
            criteria = criteria.add(Expression.like("title", "%" + find + "%"));
        }
        if (!"*".equals(manId)) {
            criteria = criteria.add(Expression.eq("manId", manId));
        }
        if (!"*".equals(manId)) {
            criteria = criteria.add(Expression.eq("nodeId", nodeId));
        }
        return (Integer) criteria.uniqueResult();
    }
}
