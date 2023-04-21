package com.creditors.bo;

import java.math.BigDecimal;
import java.sql.SQLException;
import com.be.bo.UserObject;
import com.core.util.ValidationBO;
import com.creditors.http.forms.PFExtViewForm;
import com.creditors.vo.PFExtViewVO;
import com.util.vo.IConnectionPropertiesVO;
import com.util.bo.IUserObject;
import com.creditors.dao.*;

public class PFExtViewBO {

    IConnectionPropertiesVO cp;

    IUserObject uo;

    PFExtViewDAO dao;

    public PFExtViewBO(IConnectionPropertiesVO cp, IUserObject uo) {
        this.cp = cp;
        this.uo = uo;
        dao = new PFExtViewDAO(cp);
    }

    public PFExtViewVO setData(PFExtViewForm fForm) throws SQLException {
        PFExtViewVO vo = new PFExtViewVO();
        vo.setId(fForm.getId());
        vo.setType(fForm.getType());
        vo.setPrincipal(fForm.getPrincipal());
        vo.setCurrencyBook(fForm.getCurrencyBook());
        vo.setAmount(new BigDecimal(fForm.getAmount()));
        vo.setCurrencyPay(fForm.getCurrencyPay());
        vo.setCountry(fForm.getCountry());
        vo.setCreationDate(ValidationBO.parseDate(fForm.getCreationDate(), (UserObject) uo));
        vo.setDueDate(ValidationBO.parseDate(fForm.getDueDate(), (UserObject) uo));
        vo.setPostAccount(fForm.getPostAccount());
        vo.setBankAccount(fForm.getBankAccount());
        vo.setRecipient(fForm.getRecipient());
        vo.setBeneficiary(fForm.getBeneficiary());
        vo.setMessage4x35(fForm.getMessage4x35());
        vo.setOrderer(fForm.getOrderer());
        return vo;
    }

    public void insertPFExtViewVO(PFExtViewForm fForm) throws SQLException {
        PFExtViewVO vo = setData(fForm);
        PFExtViewVO[] voArray = new PFExtViewVO[1];
        voArray[0] = vo;
        PFExtViewDAO dao = new PFExtViewDAO(cp);
        dao.setData(voArray);
    }

    public void updatePFExtViewVO(PFExtViewForm fForm) throws SQLException {
        PFExtViewVO vo = setData(fForm);
        PFExtViewDAO dao = new PFExtViewDAO(cp);
        dao.updateData(vo);
    }

    public void deletePFExtViewVO(PFExtViewForm fForm) throws SQLException {
        PFExtViewVO vo = setData(fForm);
        PFExtViewDAO dao = new PFExtViewDAO(cp);
        dao.deleteData(vo);
    }

    public PFExtViewDAO getPFExtViewDAO() {
        return dao;
    }

    public PFExtViewVO[] getPFExtViewVO() throws SQLException {
        return dao.getData();
    }
}
