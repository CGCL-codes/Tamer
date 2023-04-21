package org.universa.tcc.gemda.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.universa.tcc.gemda.entidade.Sprint;
import org.universa.tcc.gemda.util.Utils;

@Repository("sprintDAO")
public class SprintDAO extends AbstractDAO<Sprint> {

    @Override
    protected void definirCriteria(Criteria criteria, Sprint sprint) {
        Utils.persistencia.restricaoIdEquals(criteria, sprint).restricaoGreaterOrEquals(criteria, sprint, "dataInicio").restricaoLessOrEquals(criteria, sprint, "dataFim").restricaoEquals(criteria, sprint, "projeto");
    }

    @Override
    protected void definirOrdenacao(Criteria criteria) {
        criteria.addOrder(Order.asc("dataInicio"));
    }
}
