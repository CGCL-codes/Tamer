package com.ibatis.sqlmap.implgen.example;

import com.ibatis.sqlmap.implgen.annotations.Update;
import java.sql.SQLException;

/**
 * Exampe showing use of abstract class.
 */
public abstract class ExampleDaoTwo {

    /**
     * Sample update stetement
     */
    @Update
    protected abstract void updateNameActual(int id, String name) throws SQLException;

    public void updateName(int id, String name) throws SQLException {
        updateNameActual(id, name);
    }
}
