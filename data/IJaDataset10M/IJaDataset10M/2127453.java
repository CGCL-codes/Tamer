package com.ttporg.pe.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ttporg.pe.bean.BaseBean;
import com.ttporg.pe.bean.Transaccion;
import com.ttporg.pe.dao.TransaccionDao;

/**
 * @author Cesar Ricardo.
 * @clase: TransaccionDaoImpl.java  
 * @descripci�n descripci�n de la clase.
 * @author_web: http://frameworksjava2008.blogspot.com
                http://viviendoconjavaynomoririntentandolo.blogspot.com
 * @author_email: nombre del email del autor.
 * @author_company: nombre de la compa��a del autor.
 * @fecha_de_creaci�n: dd-mm-yyyy.
 * @fecha_de_ultima_actualizaci�n: dd-mm-yyyy.
 * @versi�n 1.0
 */
public class TransaccionDaoImpl extends SqlMapClientDaoSupport implements TransaccionDao {

    public static final String OBJETO_NEGOCIO = "Transaccion";

    private BaseBean beanBase = null;

    {
        this.beanBase = new BaseBean();
    }

    /**
	 * eliminarTransaccion_x_codigo
	 * @param codigo
	 */
    public boolean eliminarTransaccion_x_codigo(int codigo) {
        this.imprimeLog("DENTRO DE 'eliminarTransaccion_x_codigo' ");
        boolean mensaje = false;
        try {
            String nombReferMetodoMapeado = this.getObjetoNegocio("deleteTransaccion");
            Map<Object, Object> mapaTransaccions = new HashMap<Object, Object>();
            mapaTransaccions.put("codigoEliminacion", codigo);
            int estadoEliminacion = getSqlMapClientTemplate().delete(nombReferMetodoMapeado, mapaTransaccions);
            this.imprimeLog("EstadoEliminacion: " + estadoEliminacion);
            if (estadoEliminacion == 1) {
                mensaje = true;
            } else {
                mensaje = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = false;
        }
        return mensaje;
    }

    /**
	 * ingresarEmpresa
	 * @param empresa
	 */
    public boolean ingresarTransaccion(Transaccion transaccion) {
        this.imprimeLog("DENTRO DE 'ingresarTransaccion' ");
        boolean mensaje = false;
        try {
            String nombReferMetodoMapeado = this.getObjetoNegocio("insertTransaccion");
            this.getSqlMapClientTemplate().insert(nombReferMetodoMapeado, transaccion);
            mensaje = true;
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = false;
        }
        return mensaje;
    }

    /**
	 * modificarTransaccion
	 * @param Transaccion
	 */
    public boolean modificarTransaccion(Transaccion Transaccion) {
        this.imprimeLog("DENTRO DE 'modificarTransaccion' ");
        boolean mensaje = false;
        try {
            String nombReferMetodoMapeado = this.getObjetoNegocio("updateTransaccion");
            Integer estadoGuardar = (Integer) getSqlMapClientTemplate().update(nombReferMetodoMapeado, Transaccion);
            this.imprimeLog("Estado Guardar: " + estadoGuardar);
            if (estadoGuardar == 1) {
                mensaje = true;
            } else {
                mensaje = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = false;
        }
        return mensaje;
    }

    /**
	 * obtenerListaTransaccions
	 * @param codigo
	 */
    public List<Transaccion> obtenerListaTransaccions() {
        this.imprimeLog("DENTRO DE 'obtenerListaTransaccions' ");
        List<Transaccion> listaTransaccion = null;
        try {
            String nombReferMetodoMapeado = this.getObjetoNegocio("getListaTransaccion");
            listaTransaccion = (List<Transaccion>) getSqlMapClientTemplate().queryForList(nombReferMetodoMapeado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTransaccion;
    }

    /**
	 * obtenerObjetoTransaccion_x_codigo
	 * @param codigo
	 */
    public Transaccion obtenerObjetoTransaccion_x_codigo(int codigo) {
        this.imprimeLog("DENTRO DE 'obtenerObjetoTransaccion_x_codigo' ");
        Transaccion Transaccion = null;
        try {
            String nombReferMetodoMapeado = this.getObjetoNegocio("getTransaccion");
            Transaccion = (Transaccion) getSqlMapClientTemplate().queryForObject(nombReferMetodoMapeado, codigo);
            this.imprimeLog("Transaccion: " + Transaccion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Transaccion;
    }

    /**
	 * getObjetoNegocio
	 * @param  nombReferMetodoMapeado
	 * @return String
	 */
    private String getObjetoNegocio(String nombReferMetodoMapeado) {
        String nombObjNegocio = (OBJETO_NEGOCIO + "." + nombReferMetodoMapeado);
        return nombObjNegocio;
    }

    /**
	 * this.imprimeLog
	 * @param mensaje
	 **/
    public void imprimeLog(String mensaje) {
        this.beanBase.imprimeLog(mensaje, this.getClass().toString());
    }
}
