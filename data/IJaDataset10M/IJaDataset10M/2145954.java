package com.lb.trac.pojo.condition;

import java.math.BigDecimal;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.lb.trac.util.SQLCondition;

public class StoricoTicketFinderByIdStorico implements SQLCondition {

    public StoricoTicketFinderByIdStorico(String idStorico) {
        super();
        this.idStorico = idStorico;
    }

    private String idStorico;

    public Criterion create(Object... params) {
        Criterion c = Restrictions.eq("idStorico", new BigDecimal(idStorico));
        return c;
    }
}
