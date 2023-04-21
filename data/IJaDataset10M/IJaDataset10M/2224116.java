package br.ita.doacoes.core.voluntarios;

import br.ita.doacoes.core.cadastrodoacoes.dao.impl.DAOImpl;
import br.ita.doacoes.domain.voluntarios.Voluntario;

public class VoluntarioDAOImpl extends DAOImpl<Voluntario> implements VoluntarioDAO {

    public VoluntarioDAOImpl(Class<Voluntario> x) {
        super(x);
    }
}
