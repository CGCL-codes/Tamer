package jtk.project4.fleet.ibatis.dao;

import java.sql.SQLException;
import java.util.List;
import nl.coderight.jazz.dialog.MessageDialog;
import nl.coderight.jazz.dialog.MessageType;
import jtk.project4.fleet.domain.LabrHist;
import jtk.project4.fleet.domain.Vendor;
import jtk.project4.fleet.domain.VendorType;

public class LabrHistDao extends IbatisDao {

    public List selectLabrHists() throws SQLException {
        List selectLabrHists = null;
        selectLabrHists = getSqlMap().queryForList("SELECT_ALL_LabrHist", null);
        return selectLabrHists;
    }

    public int deleteLabrHist(LabrHist labrHist) throws SQLException {
        return (int) getSqlMap().delete("DELETE_LabrHist", labrHist);
    }

    public int updateLabrHist(LabrHist labrHist) throws SQLException {
        return (int) getSqlMap().update("UPDATE_LabrHist", labrHist);
    }

    public LabrHist insertLabrHist(LabrHist labrHist) throws SQLException {
        return (LabrHist) getSqlMap().insert("INSERT_LABRHIST", labrHist);
    }

    public LabrHist insertLabrHistType(LabrHist labrHist) throws SQLException {
        return (LabrHist) getSqlMap().insert("INSERT_LabrHist_TYPE", labrHist);
    }

    public List selectLabrHistType() throws SQLException {
        return getSqlMap().queryForList("SELECT_ALL_LabrHist_TYPE", null);
    }

    public static void main(String args[]) throws SQLException {
        LabrHist labrHist;
        labrHist = new LabrHist();
        labrHist.setDescription("Coba dari Main");
        new LabrHistDao().insertLabrHist(labrHist);
    }
}
