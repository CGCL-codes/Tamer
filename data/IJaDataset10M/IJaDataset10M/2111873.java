package br.com.sinapp.dao;

import org.hibernate.Session;
import br.com.caelum.vraptor.ioc.Component;
import br.com.sinapp.model.Conta;

@Component
public class PersistenciaConta extends PersistenciaManager<Conta> implements PersistenciaContaDao {

    public PersistenciaConta(Session session) {
        super(session);
    }
}
