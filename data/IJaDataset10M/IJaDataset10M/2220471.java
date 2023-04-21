package br.com.sinapp.dao;

import org.hibernate.Session;
import br.com.caelum.vraptor.ioc.Component;
import br.com.sinapp.model.Especializacoes;

/**
 * @author Fabiomrodriguez
 */
@Component
public class PersistenciaEspecializacoes extends PersistenciaManager<Especializacoes> implements PersistenciaEspecializacoesDao {

    public PersistenciaEspecializacoes(Session session) {
        super(session);
    }
}
