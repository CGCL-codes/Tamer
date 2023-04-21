package br.com.promove.dao;

import java.util.List;
import br.com.promove.entity.Modelo;
import br.com.promove.exception.DAOException;

public class ModeloDAO extends BaseDAO<Integer, Modelo> {

    public List<Modelo> getByCodigoOrDescricao(String codigo, String desc) throws DAOException {
        StringBuilder hql = new StringBuilder();
        hql.append("select m from Modelo m where 1=1 ");
        if (codigo != null) {
            hql.append(" and m.codigoExternoNacional = :txtcod ");
            addParamToQuery("txtcod", codigo);
        }
        if (desc != null) {
            hql.append(" and m.descricao = :txtmod ");
            addParamToQuery("txtmod", desc);
        }
        return executeQuery(hql.toString(), paramsToQuery, 0, Integer.MAX_VALUE);
    }
}
