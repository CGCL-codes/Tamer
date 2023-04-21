package com.nodeshop.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.nodeshop.dao.PaymentDao;
import com.nodeshop.entity.Payment;
import com.nodeshop.service.PaymentService;
import com.nodeshop.util.SerialNumberUtil;

/**
 * Service实现类 - 支付
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop84F252BF71A4D877C0285E8086FBE56D
 
 */
@Service
public class PaymentServiceImpl extends BaseServiceImpl<Payment, String> implements PaymentService {

    @Resource
    private PaymentDao paymentDao;

    @Resource
    public void setBaseDao(PaymentDao paymentDao) {
        super.setBaseDao(paymentDao);
    }

    public String getLastPaymentSn() {
        return paymentDao.getLastPaymentSn();
    }

    public Payment getPaymentByPaymentSn(String paymentSn) {
        return paymentDao.getPaymentByPaymentSn(paymentSn);
    }

    @Override
    public String save(Payment payment) {
        payment.setPaymentSn(SerialNumberUtil.buildPaymentSn());
        return super.save(payment);
    }
}
