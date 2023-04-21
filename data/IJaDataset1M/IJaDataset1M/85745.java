package edu.cibertec.servicio;

import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import edu.cibertec.bean.BeanArea;
import edu.cibertec.bean.BeanDepartamento;
import edu.cibertec.bean.BeanIndicador;

public class DepartamentoService {

    SqlMapClient sqlMap;

    public DepartamentoService() {
        try {
            String resource = "SqlMapConfigApplication.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error cargando configuracion en " + this.getClass().getName() + "Cause: " + e);
        }
    }

    public ArrayList<BeanDepartamento> getDepartamentos() {
        ArrayList<BeanDepartamento> listaDepartamento = null;
        try {
            listaDepartamento = (ArrayList<BeanDepartamento>) sqlMap.queryForList("getDepartamentos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaDepartamento;
    }
}
