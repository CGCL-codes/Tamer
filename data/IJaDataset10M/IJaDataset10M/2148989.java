package com.flash.system.core.dao;

import com.flash.system.core.entity.Bill;
import java.util.List;

/**
 *
 * @author shan
 */
public interface BillDAO {

    public void addBill(Bill bill) throws Exception;

    public void updateBill(Bill bill) throws Exception;

    public void deleteBill(Bill bill) throws Exception;

    public Bill findByPrimaryKey(Long id) throws Exception;

    public List<Bill> findAll() throws Exception;
}
