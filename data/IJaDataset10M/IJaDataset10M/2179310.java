package com.be.bo;

import java.sql.SQLException;
import com.be.http.forms.MktFXRateForm;
import com.be.vo.MktFXRateVO;
import com.core.util.*;
import com.util.vo.IConnectionPropertiesVO;
import com.util.bo.IUserObject;
import com.be.dao.*;

public class MktFXRateBO {

    IConnectionPropertiesVO cp;

    IUserObject uo;

    MktFXRateDAO dao;

    public MktFXRateBO(IConnectionPropertiesVO cp, IUserObject uo) {
        this.cp = cp;
        this.uo = uo;
        dao = new MktFXRateDAO(cp);
    }

    public MktFXRateVO setData(MktFXRateForm fForm) throws SQLException {
        MktFXRateVO vo = new MktFXRateVO();
        vo.setId(fForm.getId());
        vo.setUnitCurrencyID(fForm.getUnitCurrencyID());
        vo.setPriceCurrencyID(fForm.getPriceCurrencyID());
        vo.setExchangeRate(fForm.getExchangeRate());
        vo.setTradeDate(ValidationBO.parseDate(fForm.getTradeDate(), (UserObject) uo));
        return vo;
    }

    public void insertMktFXRateVO(MktFXRateForm fForm) throws SQLException {
        MktFXRateVO vo = setData(fForm);
        MktFXRateVO[] voArray = new MktFXRateVO[1];
        voArray[0] = vo;
        MktFXRateDAO dao = new MktFXRateDAO(cp);
        dao.setData(voArray);
    }

    public void updateMktFXRateVO(MktFXRateForm fForm) throws SQLException {
        MktFXRateVO vo = setData(fForm);
        MktFXRateDAO dao = new MktFXRateDAO(cp);
        dao.updateData(vo);
    }

    public void deleteMktFXRateVO(MktFXRateForm fForm) throws SQLException {
        MktFXRateVO vo = setData(fForm);
        MktFXRateDAO dao = new MktFXRateDAO(cp);
        dao.deleteData(vo);
    }

    public MktFXRateDAO getMktFXRateDAO() {
        return dao;
    }

    public MktFXRateVO[] getMktFXRateVO() throws SQLException {
        return dao.getData();
    }
}
