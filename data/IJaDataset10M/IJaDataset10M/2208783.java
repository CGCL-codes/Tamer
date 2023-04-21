package jtk.project4.fleet.ibatis.dao;

import java.sql.SQLException;
import java.util.List;
import jtk.project4.fleet.domain.History;

public class HistoryDao extends IbatisDao {

    public List selectHistories() throws SQLException {
        List selectHistories = null;
        selectHistories = getSqlMap().queryForList("SELECT_ALL_HISTORY", null);
        return selectHistories;
    }

    public int deleteHistory(History history) throws SQLException {
        return (int) getSqlMap().delete("DELETE_HISTORY", history);
    }

    public int updateHistory(History history) throws SQLException {
        return (int) getSqlMap().update("UPDATE_HISTORY", history);
    }

    public History insertHistory(History history) throws SQLException {
        return (History) getSqlMap().insert("INSERT_HISTORY", history);
    }

    public static void main(String args[]) throws SQLException {
        History history;
        history = new History();
        history.setObjectId(123);
        history.setComments("hhhhhh");
        new HistoryDao().insertHistory(history);
    }
}
