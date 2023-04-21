package net.sourceforge.pmakbsc.dao;

import net.sourceforge.pmakbsc.entity.Tarefa;

/**
 *
 */
public class TarefaJpaDAO extends GenericJpaDAO<Tarefa> implements TarefaDAO {

    public TarefaJpaDAO() {
        super(Tarefa.class);
    }
}
