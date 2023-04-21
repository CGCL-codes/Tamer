package com.google.code.cana.db;

import java.util.List;
import org.hibernate.criterion.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import com.google.code.cana.pojo.insumos.MateriaPrima;
import com.google.code.cana.service.ObjetoNaoEncontradoException;
import com.google.code.cana.service.RestricaoIntegridadeException;

/**
 * Implementa��o Hibernate para Dao de Mat�ria Prima
 * 
 * @author Rodrigo Barbosa Lira
 * 
 */
public class MateriaPrimaDaoHibernate extends CanaDaoHibernate<MateriaPrima, Integer> implements MateriaPrimaDao {

    @Override
    public MateriaPrima criar(MateriaPrima entidade) {
        try {
            return super.criar(entidade);
        } catch (DataIntegrityViolationException e) {
            throw new RestricaoIntegridadeException("Mat�ria Prima j� cadastrado.");
        }
    }

    @Override
    public MateriaPrima modificar(MateriaPrima entidade) {
        try {
            return super.modificar(entidade);
        } catch (DataRetrievalFailureException e) {
            throw new ObjetoNaoEncontradoException("Materia Prima n�o encontrada.");
        } catch (DataIntegrityViolationException e) {
            throw new RestricaoIntegridadeException("Mat�ria Prima j� cadastrado.");
        }
    }

    @Override
    public MateriaPrima recuperarPorId(Integer id, boolean lock) {
        try {
            return super.recuperarPorId(id, lock);
        } catch (DataRetrievalFailureException e) {
            throw new ObjetoNaoEncontradoException("Materia Prima n�o encontrada.");
        }
    }

    @Override
    public MateriaPrima apagar(MateriaPrima entidade) {
        try {
            return super.apagar(entidade);
        } catch (DataIntegrityViolationException e) {
            throw new RestricaoIntegridadeException("N�o � poss�vel remover Mat�ria Prima.");
        }
    }

    @Override
    public List<MateriaPrima> recuperarTodos() {
        Order order = Order.asc("nome");
        return recuperarPorCriterios(order);
    }
}
