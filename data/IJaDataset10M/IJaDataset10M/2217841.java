package edu.cibertec.servicio;

import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import edu.cibertec.bean.BeanIndicador;

public class IndicadorService {

    SqlMapClient sqlMap;

    public IndicadorService() {
        try {
            String resource = "SqlMapConfigApplication.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error cargando configuracion en " + this.getClass().getName() + "Cause: " + e);
        }
    }

    public ArrayList<BeanIndicador> getIndicadores() {
        ArrayList<BeanIndicador> listaIndicadores = null;
        try {
            listaIndicadores = (ArrayList<BeanIndicador>) sqlMap.queryForList("getIndicadoresTodos");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaIndicadores;
    }

    public int insertarIndicador(BeanIndicador bean) {
        try {
            sqlMap.insert("insertIndicador", bean);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int eliminarIndicador(BeanIndicador bean) {
        try {
            System.out.println(bean.getCodigo());
            sqlMap.delete("deleteIndicador", bean);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public BeanIndicador obtenerIndicador(BeanIndicador bean) {
        try {
            System.out.println(bean.getCodigo());
            return (BeanIndicador) sqlMap.queryForObject("getIndicadorById", bean);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int actualizarIndicador(BeanIndicador bean) {
        try {
            System.out.println(bean.getCodigo());
            return sqlMap.update("actualizaIndicador", bean);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
