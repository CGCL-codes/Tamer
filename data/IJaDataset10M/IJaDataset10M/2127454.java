package br.com.sigep.blo;

import java.util.List;
import br.com.sigep.dao.ResultadoFinalDaoInterface;
import br.com.sigep.bean.ResultadoFinal;
import br.com.sigep.dao.DaoException;

public class ResultadoFinalBlo implements ResultadoFinalBloInterface {

    ResultadoFinalDaoInterface dao;

    public ResultadoFinalBlo(ResultadoFinalDaoInterface dao) {
        this.dao = dao;
    }

    public ResultadoFinalBlo() {
    }

    public ResultadoFinalDaoInterface getDao() {
        return dao;
    }

    public void setDao(ResultadoFinalDaoInterface dao) {
        this.dao = dao;
    }

    public void salvarResultadoFinal(ResultadoFinal newInstance) {
        try {
            dao.salvar(newInstance);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public void atualizarResultadoFinal(ResultadoFinal transientObject) {
        try {
            dao.atualizar(transientObject);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public ResultadoFinal carregarResultadoFinal(long id) {
        try {
            return dao.carregar(id);
        } catch (DaoException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<ResultadoFinal> listarResultadoFinal() {
        try {
            return dao.listar();
        } catch (DaoException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<ResultadoFinal> listarNotasPorPeriodo(long idAluno, long idTurma) {
        try {
            return dao.listarNotasPorPeriodo(idAluno, idTurma);
        } catch (DaoException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void removerResultadoFinal(ResultadoFinal persistentObject) {
        try {
            dao.remover(persistentObject);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }

    public void removerResultadoFinal(long id) {
        try {
            dao.remover(id);
        } catch (DaoException ex) {
            ex.printStackTrace();
        }
    }
}
