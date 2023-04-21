package com.tieland.demo.dao;

import com.tieland.demo.entity.Organ;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2008-3-12
 * Time: 8:38:06
 * To change this template use File | Settings | File Templates.
 */
public interface OrganDAO {

    Organ getOrgan(String organNo) throws SQLException;

    void insertOrgan(Organ organ) throws SQLException;

    void updateOrgan(Organ organ) throws SQLException;

    List<Organ> getOrganList() throws SQLException;
}
