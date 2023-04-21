package com.abacus.pay.dao;

import java.util.Calendar;
import com.abacus.pay.model.PayVoucher;

public class HibernateTest {

    public static void main(String[] arg0) {
        new Thread(new SaveData()).start();
        TransactionStrategy.getInstance();
        TransactionStrategy.getInstance().begin();
        PayVoucherDao dao = new PayVoucherDaoImpl();
        dao.save(new PayVoucher(12L, Calendar.getInstance()));
        PayVoucher payVoucher = (PayVoucher) dao.findById(1L);
        payVoucher.setCalendar("2011-12-12");
        System.out.println(payVoucher.getDate());
        TransactionStrategy.getInstance().commit();
        HibernateUtil.close();
    }
}
