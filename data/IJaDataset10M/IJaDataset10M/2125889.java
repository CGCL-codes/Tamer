package com.debitors.bo;

import java.math.BigDecimal;
import java.sql.SQLException;
import com.be.bo.UserObject;
import com.be.vo.BankAccountVO;
import com.core.util.ESRValidator;
import com.core.util.ValidationBO;
import com.debitors.http.forms.BillForm;
import com.debitors.vo.BillVO;
import com.util.vo.IConnectionPropertiesVO;
import com.debitors.dao.*;

public class BillBO {

    IConnectionPropertiesVO cp;

    UserObject uo;

    BillDAO dao;

    public BillBO(IConnectionPropertiesVO cp, UserObject uo) {
        this.cp = cp;
        this.uo = uo;
        dao = new BillDAO(cp);
    }

    public BillVO setData(BillForm fForm) throws SQLException {
        BillVO vo = new BillVO();
        vo.setId(fForm.getId());
        vo.setType(fForm.getType());
        vo.setBillNumber(fForm.getBillNumber());
        vo.setOrderID(fForm.getOrderID());
        vo.setCustomerID(fForm.getCustomerID());
        vo.setAmount(new BigDecimal(fForm.getAmount()));
        vo.setAmountPaid(new BigDecimal(fForm.getAmountPaid()));
        vo.setCurrencyID(fForm.getCurrencyID());
        vo.setCreationDate(ValidationBO.parseDate(fForm.getCreationDate(), (UserObject) uo));
        vo.setDueDate(ValidationBO.parseDate(fForm.getDueDate(), (UserObject) uo));
        vo.setBankAccountID(fForm.getBankAccountID());
        vo.setBeneficiaryID(fForm.getBeneficiaryID());
        vo.setIntermediaryID(fForm.getIntermediaryID());
        vo.setBillingInfo(fForm.getBillingInfo());
        vo.setPaymentMethod(fForm.getPaymentMethod());
        vo.setDeliveryMethod(fForm.getDeliveryMethod());
        ESRValidator esrV = new ESRValidator();
        BankAccountVO[] bankVO = uo.getFacade().getBankAccountVO((int) vo.getBankAccountID());
        String referenceNumber = esrV.getCodeLine(esrV.getAmount(vo.getAmount().doubleValue()), esrV.getReferenceNumberFormatted(vo.getBillNumber()), esrV.getAccountNumberESR(bankVO[0].getAccountNumber()));
        System.out.println(referenceNumber);
        vo.setReferenceNumber(referenceNumber);
        return vo;
    }

    public void insertBillVO(BillForm fForm) throws SQLException {
        BillVO vo = setData(fForm);
        BillVO[] voArray = new BillVO[1];
        voArray[0] = vo;
        BillDAO dao = new BillDAO(cp);
        dao.setData(voArray);
    }

    public void insertBillVO(BillVO vo) throws SQLException {
        BillVO[] voArray = new BillVO[1];
        voArray[0] = vo;
        BillDAO dao = new BillDAO(cp);
        dao.setData(voArray);
    }

    public void updateBillVO(BillForm fForm) throws SQLException {
        BillVO vo = setData(fForm);
        BillDAO dao = new BillDAO(cp);
        dao.updateData(vo);
    }

    public void deleteBillVO(BillForm fForm) throws SQLException {
        BillVO vo = setData(fForm);
        BillDAO dao = new BillDAO(cp);
        dao.deleteData(vo);
    }

    public BillDAO getBillDAO() {
        return dao;
    }

    public BillVO[] getBillVO() throws SQLException {
        return dao.getData();
    }
}
