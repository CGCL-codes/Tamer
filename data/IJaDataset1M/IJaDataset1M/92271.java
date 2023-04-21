package com.viators.model;

import com.viators.beans.*;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import java.io.Reader;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author mvalentin
 */
public class GestionPasajero {

    private static final Logger logger = Logger.getLogger(GestionPasajero.class);

    public Collection listarPasajeros(int nlipa_codigo) {
        List<ListaNueva> lista = null;
        try {
            String resource = "com/viators/xml/Conexion.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            lista = sqlMap.queryForList("getListaTotalPasajeros", nlipa_codigo);
            logger.debug("lista pasajeros " + lista.size());
        } catch (Exception ex) {
            logger.error("--->" + ex.getMessage());
            ex.printStackTrace();
        } finally {
        }
        return lista;
    }

    public boolean insertarPasajero(Sisma_personaActionForm paf, int ntipa_codigo, String accion) {
        boolean flag = false;
        try {
            String resource = "com/viators/xml/Conexion.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            Sisde_pasajerosBean pb = new Sisde_pasajerosBean();
            pb.setNpers_codigo(paf.getNpers_codigo());
            pb.setNlipa_codigo(paf.getNlipa_codigo());
            pb.setNtipa_codigo(ntipa_codigo);
            if (accion.equals("nuevo")) {
                sqlMap.insert("insertarPasajero", pb);
            }
            flag = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
        }
        return flag;
    }

    public boolean eliminarPasajero(int npers_codigo) {
        boolean flag = false;
        try {
            String resource = "com/viators/xml/Conexion.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            Sisde_pasajerosBean pb = new Sisde_pasajerosBean();
            pb.setNpers_codigo(npers_codigo);
            sqlMap.delete("eliminarPasajero", pb);
            flag = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
        }
        return flag;
    }

    public String obtenerTipoDocumento(int ntido_codigo) {
        String res = "";
        try {
            String resource = "com/viators/xml/Conexion.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            res = (String) sqlMap.queryForObject("obtenerTipoDocumento", ntido_codigo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
        }
        return res;
    }

    public String obtenerEstadoCivil(int nesci_codigo) {
        String res = "";
        try {
            String resource = "com/viators/xml/Conexion.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            res = (String) sqlMap.queryForObject("obtenerEstadoCivil", nesci_codigo);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
        }
        return res;
    }

    public Sisma_personaBean obtenrPasajero(int npers_codigo) {
        Sisma_personaBean pasajero = null;
        try {
            logger.debug("obtenerPasajero " + npers_codigo);
            String resource = "com/viators/xml/Conexion.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlMapClient sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
            pasajero = (Sisma_personaBean) sqlMap.queryForObject("getPasajeroById", npers_codigo);
        } catch (Exception ex) {
            logger.error("--->" + ex.getMessage());
            ex.printStackTrace();
        } finally {
        }
        return pasajero;
    }
}
