package com.faithbj.shop.dao;

import com.faithbj.shop.model.entity.Payment;

/**
 * Dao接口 - 支付
 * <p>Copyright: Copyright (c) 2011</p> 
 * 
 * <p>Company: www.faithbj.com</p>
 * 
 * @author 	faithbj
 * @date 	2011-12-16
 * @version 1.0
 */
public interface PaymentDao extends BaseDao<Payment, String> {

    /**
	 * 获取最后生成的支付编号
	 * 
	 * @return 支付编号
	 */
    public String getLastPaymentSn();

    /**
	 * 根据支付编号获取对象（若对象不存在，则返回null）
	 * 
	 * @return 支付对象
	 */
    public Payment getPaymentByPaymentSn(String paymentSn);
}
