package com.pr.bo;

import java.math.BigDecimal;
import java.sql.SQLException;
import com.pr.http.forms.LookupQSTForm;
import com.pr.vo.LookupQSTVO;
import com.be.bo.UserObject;
import com.core.util.*;
import com.util.vo.IConnectionPropertiesVO;
import com.util.bo.IUserObject;
import com.pr.dao.*;

public class LookupQSTBO {

    IConnectionPropertiesVO cp;

    IUserObject uo;

    LookupQSTDAO dao;

    public LookupQSTBO(IConnectionPropertiesVO cp, IUserObject uo) {
        this.cp = cp;
        this.uo = uo;
        dao = new LookupQSTDAO(cp);
    }

    public LookupQSTVO setData(LookupQSTForm fForm) throws SQLException {
        LookupQSTVO vo = new LookupQSTVO();
        vo.setId(fForm.getId());
        vo.setQstState(fForm.getQstState());
        vo.setQstCode(fForm.getQstCode());
        vo.setQstSex(fForm.getQstSex());
        vo.setQstChildren(fForm.getQstChildren());
        vo.setQstAmountStart(new BigDecimal(fForm.getQstAmountStart()));
        vo.setQstAmountEnd(new BigDecimal(fForm.getQstAmountEnd()));
        vo.setQstPercentage(new BigDecimal(fForm.getQstPercentage()));
        vo.setQstAmount(new BigDecimal(fForm.getQstAmount()));
        vo.setModified(new java.sql.Timestamp(new java.util.Date().getTime()));
        vo.setValidFrom(ValidationBO.parseDate(fForm.getValidFrom(), (UserObject) uo));
        vo.setValidTo(ValidationBO.parseDate(fForm.getValidTo(), (UserObject) uo));
        return vo;
    }

    public void insertLookupQSTVO(LookupQSTForm fForm) throws SQLException {
        LookupQSTVO vo = setData(fForm);
        LookupQSTVO[] voArray = new LookupQSTVO[1];
        voArray[0] = vo;
        LookupQSTDAO dao = new LookupQSTDAO(cp);
        dao.setData(voArray);
    }

    public void updateLookupQSTVO(LookupQSTForm fForm) throws SQLException {
        LookupQSTVO vo = setData(fForm);
        LookupQSTDAO dao = new LookupQSTDAO(cp);
        dao.updateData(vo);
    }

    public void deleteLookupQSTVO(LookupQSTForm fForm) throws SQLException {
        LookupQSTVO vo = setData(fForm);
        LookupQSTDAO dao = new LookupQSTDAO(cp);
        dao.deleteData(vo);
    }

    public LookupQSTDAO getLookupQSTDAO() {
        return dao;
    }

    public LookupQSTVO[] getLookupQSTVO() throws SQLException {
        return dao.getData();
    }
}
