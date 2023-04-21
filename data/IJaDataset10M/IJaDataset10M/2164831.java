package org.universa.tcc.gemda.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.universa.tcc.gemda.entidade.Usuario;
import org.universa.tcc.gemda.util.Utils;

@Repository("usuarioDAO")
public class UsuarioDAO extends AbstractDAO<Usuario> {

    @Override
    protected void definirCriteria(Criteria criteria, Usuario usuario) {
        Utils.persistencia.restricaoIdEquals(criteria, usuario).restricaoEquals(criteria, usuario, "grupo").restricaoLike(criteria, usuario, "nomeCompleto").restricaoLike(criteria, usuario, "login").restricaoEquals(criteria, usuario, "senha").restricaoLike(criteria, usuario, "email").restricaoEquals(criteria, usuario, "ativo");
    }

    @Override
    protected void definirOrdenacao(Criteria criteria) {
        criteria.addOrder(Order.asc("nomeCompleto"));
    }
}
