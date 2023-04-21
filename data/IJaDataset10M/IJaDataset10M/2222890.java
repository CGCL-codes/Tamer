package no.ugland.utransprod.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import no.ugland.utransprod.dao.OrderStatusNotSentVDAO;
import no.ugland.utransprod.model.OrderStatusNotSentV;
import no.ugland.utransprod.model.StatusOrdersNotSent;
import no.ugland.utransprod.util.excel.ExcelReportSetting;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Implementasjon av DAO mot view ORDER_STATUS_NOT_SENT_V
 * 
 * @author atle.brekka
 * 
 */
public class OrderStatusNotSentVDAOHibernate extends BaseDAOHibernate<OrderStatusNotSentV> implements OrderStatusNotSentVDAO {

    /**
	 * Konstrukt�r
	 */
    public OrderStatusNotSentVDAOHibernate() {
        super(OrderStatusNotSentV.class);
    }

    /**
	 * @see no.ugland.utransprod.dao.OrderStatusNotSentVDAO#findByParams(no.ugland.utransprod.util.excel.ExcelReportSetting)
	 */
    @SuppressWarnings("unchecked")
    public List<StatusOrdersNotSent> findByParams(final ExcelReportSetting params) {
        return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException {
                List<Object[]> list = getSumPacklistNotReady(params, session);
                if (list.size() > 1) {
                    return null;
                }
                List returnList = new ArrayList<Object>();
                StatusOrdersNotSent statusOrdersNotSent = createStatusOrderNotSentAndSetCountNoPacklist(list);
                list = getSumOrderNotReady(params, session);
                if (list.size() > 1) {
                    return null;
                }
                setCountNotReady(list, statusOrdersNotSent);
                list = getSumOrderReady(params, session);
                if (list.size() > 1) {
                    return null;
                }
                setCountNotSent(list, statusOrdersNotSent);
                returnList.add(statusOrdersNotSent);
                return returnList;
            }
        });
    }

    void setCountNotSent(List<Object[]> list, StatusOrdersNotSent statusOrdersNotSent) {
        Object[] object = list.get(0);
        statusOrdersNotSent.setCountNotSent((Integer) object[0]);
        statusOrdersNotSent.setGarageValueNotSent((BigDecimal) object[1]);
    }

    @SuppressWarnings("unchecked")
    List<Object[]> getSumOrderReady(final ExcelReportSetting params, Session session) {
        List<Object[]> list;
        list = session.createQuery("select count(orderStatusNotSentV.orderId)," + "sum(orderStatusNotSentV.garageValue) " + "from OrderStatusNotSentV orderStatusNotSentV " + "where orderStatusNotSentV.orderReady is not null and " + "		orderStatusNotSentV.productArea=:productArea").setParameter("productArea", params.getProductAreaName()).list();
        return list;
    }

    void setCountNotReady(List<Object[]> list, StatusOrdersNotSent statusOrdersNotSent) {
        Object[] object = list.get(0);
        statusOrdersNotSent.setCountNotReady((Integer) object[0]);
        statusOrdersNotSent.setGarageValueNotReady((BigDecimal) object[1]);
    }

    @SuppressWarnings("unchecked")
    List<Object[]> getSumPacklistNotReady(final ExcelReportSetting params, Session session) {
        List<Object[]> list = session.createQuery("select count(orderStatusNotSentV.orderId)," + "sum(orderStatusNotSentV.garageValue) " + "from OrderStatusNotSentV orderStatusNotSentV " + "where orderStatusNotSentV.packlistReady is null and " + "		orderStatusNotSentV.productArea=:productArea").setParameter("productArea", params.getProductAreaName()).list();
        return list;
    }

    StatusOrdersNotSent createStatusOrderNotSentAndSetCountNoPacklist(List<Object[]> list) {
        StatusOrdersNotSent statusOrdersNotSent = new StatusOrdersNotSent();
        Object[] object = list.get(0);
        statusOrdersNotSent.setCountNoPacklist((Integer) object[0]);
        statusOrdersNotSent.setGarageValueNoPacklist((BigDecimal) object[1]);
        return statusOrdersNotSent;
    }

    @SuppressWarnings("unchecked")
    List<Object[]> getSumOrderNotReady(final ExcelReportSetting params, Session session) {
        List<Object[]> list;
        list = session.createQuery("select count(orderStatusNotSentV.orderId)," + "sum(orderStatusNotSentV.garageValue) " + "from OrderStatusNotSentV orderStatusNotSentV " + "where orderStatusNotSentV.orderReady is null and " + "		orderStatusNotSentV.productArea=:productArea").setParameter("productArea", params.getProductAreaName()).list();
        return list;
    }
}
