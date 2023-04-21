package DAO.TableAttributDAO;

import DAO.DAO_Mysql;

public class TableAttributDAO_Mysql extends TableAttributDAO_Bd {

    public TableAttributDAO_Mysql() {
        dao = DAO_Mysql.getInstance();
    }
}
