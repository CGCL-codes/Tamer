package com.faithbj.shop.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.faithbj.shop.dao.PaymentDao;
import com.faithbj.shop.model.entity.Payment;

/**
 * Dao实现类 - 支付
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.faithbj.com</p>
 * 
 * @author 	faithbj
 * @date 	2011-12-16
 * @version 1.0
 */
@Repository
public class PaymentDaoImpl extends BaseDaoImpl<Payment, String> implements PaymentDao {

    @SuppressWarnings("unchecked")
    public String getLastPaymentSn() {
        String hql = "from Payment as payment order by payment.createDate desc";
        List<Payment> paymentList = getSession().createQuery(hql).setFirstResult(0).setMaxResults(1).list();
        if (paymentList != null && paymentList.size() > 0) {
            return paymentList.get(0).getPaymentSn();
        } else {
            return null;
        }
    }

    public Payment getPaymentByPaymentSn(String paymentSn) {
        String hql = "from Payment as payment where payment.paymentSn = ?";
        return (Payment) getSession().createQuery(hql).setParameter(0, paymentSn).uniqueResult();
    }
}
