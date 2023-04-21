package com.be.bo;

import java.sql.SQLException;
import com.be.dao.*;
import com.be.http.forms.LocationForm;
import com.be.vo.LocationVO;
import com.util.vo.IConnectionPropertiesVO;
import com.util.bo.IUserObject;

public class LocationBO {

    IConnectionPropertiesVO cp;

    IUserObject uo;

    LocationDAO dao;

    public LocationBO(IConnectionPropertiesVO cp, IUserObject uo) {
        this.cp = cp;
        this.uo = uo;
        dao = new LocationDAO(cp);
    }

    public LocationVO setData(LocationForm fForm) throws SQLException {
        LocationVO vo = new LocationVO();
        vo.setId(fForm.getId());
        vo.setType1(fForm.getType1());
        vo.setPlz(fForm.getPlz());
        vo.setType2(fForm.getType2());
        vo.setOrt18(fForm.getOrt18());
        vo.setOrt(fForm.getOrt());
        vo.setKanton(fForm.getKanton());
        return vo;
    }

    public void insertLocationVO(LocationForm fForm) throws SQLException {
        LocationVO vo = setData(fForm);
        LocationVO[] voArray = new LocationVO[1];
        voArray[0] = vo;
        LocationDAO dao = new LocationDAO(cp);
        dao.setData(voArray);
    }

    public void updateLocationVO(LocationForm fForm) throws SQLException {
        LocationVO vo = setData(fForm);
        LocationDAO dao = new LocationDAO(cp);
        dao.updateData(vo);
    }

    public void deleteLocationVO(LocationForm fForm) throws SQLException {
        LocationVO vo = setData(fForm);
        LocationDAO dao = new LocationDAO(cp);
        dao.deleteData(vo);
    }

    public LocationDAO getLocationDAO() {
        return dao;
    }

    public LocationVO[] getLocationVO() throws SQLException {
        return dao.getData();
    }
}
