package com.ecomponentes.formularios.empresatask.dao;

import java.util.List;
import com.ecomponentes.dao.BaseDAOHibernate;
import com.ecomponentes.hibernate.empresatask.TbEmpresaTask;

/**
 * 
 * @author Enrique Campos Quaggio
 */
public class EmpresaTaskDAO extends BaseDAOHibernate {

    /**
	 * Utilizado para retornar todos os registros de <b>TbEmpresaTask</b>,
	 * ordenado pelo campo <b>nomeCidade</b>. 
	 * @return java.util.List com os registros.
	 */
    public List selecionarTodos() {
        return getObjects("from TbEmpresaTask as empresatask order by empresatask.tbTask.dataTask desc, empresatask.tbEmpresa.tbCategoria.idCategoria");
    }

    public Object selecionarEmpresaTask(Integer id) {
        return getObject(TbEmpresaTask.class, id);
    }

    public List selecionarEmpresaTaskByEmpresa(String id) {
        return getObjects("from TbEmpresaTask as empresatask where empresatask.tbEmpresa = " + id + " order by empresatask.tbTask.dataTask desc , empresatask.tbEmpresa.tbCategoria.idCategoria");
    }

    public List selecionarEmpresaTaskByEmpresaPerfil(String id, String idPerfil) {
        return getObjects("from TbEmpresaTask as empresatask where empresatask.tbEmpresa = " + id + " and empresatask.tbPerfil = " + idPerfil + " order by empresatask.tbTask.dataTask desc , empresatask.tbEmpresa.tbCategoria.idCategoria");
    }

    public List selecionarEmpresaTaskByTask(String id) {
        return getObjects("from TbEmpresaTask as empresatask where empresatask.tbTask = " + id + " order by empresatask.tbTask.dataTask desc , empresatask.tbEmpresa.tbCategoria.idCategoria");
    }

    public List selecionarTodosByContatoInterno(String id) {
        return getObjects("from TbEmpresaTask as empresatask where empresatask.tbTask.idContatoInterno = " + id + " order by empresatask.tbTask.dataTask desc , empresatask.tbEmpresa.tbCategoria.idCategoria");
    }
}
