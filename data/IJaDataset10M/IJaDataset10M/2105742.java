package com.be.bo;

import java.sql.SQLException;
import com.be.http.forms.ResourceForm;
import com.be.vo.ResourceVO;
import com.core.util.*;
import com.util.vo.IConnectionPropertiesVO;
import com.util.bo.IUserObject;
import com.be.dao.*;

public class ResourceBO {

    IConnectionPropertiesVO cp;

    IUserObject uo;

    ResourceDAO dao;

    public ResourceBO(IConnectionPropertiesVO cp, IUserObject uo) {
        this.cp = cp;
        this.uo = uo;
        dao = new ResourceDAO(cp);
    }

    public ResourceVO setData(ResourceForm fForm) throws SQLException {
        ResourceVO vo = new ResourceVO();
        vo.setId(fForm.getId());
        vo.setType(fForm.getType());
        vo.setLocale(fForm.getLocale());
        vo.setKey(fForm.getKey());
        vo.setValue(fForm.getValue());
        vo.setModified(new java.sql.Timestamp(new java.util.Date().getTime()));
        vo.setValidFrom(ValidationBO.parseDate(fForm.getValidFrom(), (UserObject) uo));
        vo.setValidTo(ValidationBO.parseDate(fForm.getValidTo(), (UserObject) uo));
        return vo;
    }

    public void insertResourceVO(ResourceForm fForm) throws SQLException {
        ResourceVO vo = setData(fForm);
        ResourceVO[] voArray = new ResourceVO[1];
        voArray[0] = vo;
        ResourceDAO dao = new ResourceDAO(cp);
        dao.setData(voArray);
    }

    public void updateResourceVO(ResourceForm fForm) throws SQLException {
        ResourceVO vo = setData(fForm);
        ResourceDAO dao = new ResourceDAO(cp);
        dao.updateData(vo);
    }

    public void deleteResourceVO(ResourceForm fForm) throws SQLException {
        ResourceVO vo = setData(fForm);
        ResourceDAO dao = new ResourceDAO(cp);
        dao.deleteData(vo);
    }

    public ResourceDAO getResourceDAO() {
        return dao;
    }

    public ResourceVO[] getResourceVO() throws SQLException {
        return dao.getData();
    }
}
