package org.lc.dao.page;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.lc.dao.hibernate.BaseHibernateDao;
import org.lc.util.BeanUtils;

/**
 * ʹ��Criteria�ķ�ҳ��ѯ��. ֧��ִ��Count��ѯ, ����JDBC��SrollLast()���� ��ȡ��ȫ�����ͳ��size���ַ�ʽȡ���ܼ�¼����
 */
public class CriteriaPage extends Page {

    private static final long serialVersionUID = -6099222615755442158L;

    public static final CriteriaPage EMPTY_PAGE = new CriteriaPage();

    private CriteriaPage() {
        super();
    }

    public CriteriaPage(int start, int avaCount, int totalSize, int pageSize, Object data) {
        super(start, avaCount, totalSize, pageSize, data);
    }

    /**
     * Ĭ����getCount��ʽ����Page
     */
    public static CriteriaPage getHibernatePageInstance(Criteria criteria, int pageNo, int pageSize) {
        return getHibernatePageInstance(criteria, pageNo, pageSize, BaseHibernateDao.DEFAULT_MODE);
    }

    /**
     * ʹ�ò�ͬģʽ����Page.
     */
    public static CriteriaPage getHibernatePageInstance(Criteria criteria, int pageNo, int pageSize, int mode) {
        if (mode == BaseHibernateDao.COUNT_MODE) {
            return CriteriaPage.getPageInstanceByCount(criteria, pageNo, pageSize);
        }
        if (mode == BaseHibernateDao.SCROLL_MODE) {
            return CriteriaPage.getScrollPageInstanceWithTotalByScroll(criteria, pageNo, pageSize);
        }
        if (mode == BaseHibernateDao.LIST_MODE) {
            return CriteriaPage.getScrollPageInstanceWithTotalByList(criteria, pageNo, pageSize);
        }
        return null;
    }

    /**
     * �Բ�ѯCount����ʽ��ȡtotalCount�ĺ���
     */
    @SuppressWarnings("unchecked")
    protected static CriteriaPage getPageInstanceByCount(Criteria criteria, int pageNo, int pageSize) {
        CriteriaImpl impl = (CriteriaImpl) criteria;
        Projection projection = impl.getProjection();
        List<OrderEntry> orderEntries;
        try {
            orderEntries = (List) BeanUtils.forceGetProperty(impl, "orderEntries");
            BeanUtils.forceSetProperty(impl, "orderEntries", new ArrayList());
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }
        int totalCount = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
        criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        try {
            BeanUtils.forceSetProperty(impl, "orderEntries", orderEntries);
        } catch (Exception e) {
            throw new InternalError(" Runtime Exception impossibility throw ");
        }
        return fillElements(criteria, totalCount, pageNo, pageSize);
    }

    /**
     * ��JDBC Scroll to last����ʽ��ȡtotalCount�ĺ���
     */
    protected static CriteriaPage getScrollPageInstanceWithTotalByScroll(Criteria criteria, int pageNo, int pageSize) {
        ScrollableResults scrollableResults = criteria.scroll(ScrollMode.SCROLL_SENSITIVE);
        scrollableResults.last();
        int totalCount = scrollableResults.getRowNumber();
        return fillElements(criteria, totalCount + 1, pageNo, pageSize);
    }

    /**
     * ȡ��ȫ�����Ȼ��ȡList.size()�����totalCount��Ч�������ʽ�ĺ���
     */
    protected static CriteriaPage getScrollPageInstanceWithTotalByList(Criteria criteria, int pageNo, int pageSize) {
        criteria.scroll(ScrollMode.FORWARD_ONLY);
        int totalCount = calculateTotalElementsByList(criteria);
        return fillElements(criteria, totalCount, pageNo, pageSize);
    }

    private static CriteriaPage fillElements(Criteria criteria, int totalCount, int pageNo, int pageSize) {
        if (totalCount < 1) return CriteriaPage.EMPTY_PAGE;
        if (pageNo < 1) pageNo = 1;
        int startIndex = CriteriaPage.getStartOfPage(pageNo, pageSize);
        List list = criteria.setFirstResult(startIndex - 1).setMaxResults(pageSize).list();
        int avaCount = list == null ? 0 : list.size();
        return new CriteriaPage(startIndex, avaCount, totalCount, pageSize, list);
    }

    private static int calculateTotalElementsByList(Criteria criteria) {
        return criteria.list().size();
    }
}
