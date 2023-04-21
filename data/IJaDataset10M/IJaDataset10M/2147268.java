package com.controlexpenses.business;

import java.util.List;
import com.controlexpenses.dao.FunctionaryDAO;
import com.controlexpenses.domain.Functionary;

/**
 * Implementa��o do servi�o de funcion�rio
 * @author josenio.camelo
 *
 */
public class FunctionaryServiceImpl implements FunctionaryService {

    /**
     * DAO de Funcion�rio;
     */
    private FunctionaryDAO funcDAO;

    public void setFuncDAO(FunctionaryDAO funcDAO) {
        this.funcDAO = funcDAO;
    }

    public List findFunctionary() {
        return funcDAO.findFunctionary();
    }

    public Long insert(Functionary f) {
        return funcDAO.insert(f);
    }

    public void delete(Long id) {
        funcDAO.delete(id);
    }

    public Functionary find(Long id) {
        return funcDAO.findById(id);
    }

    public void update(Functionary f) {
        funcDAO.update(f);
    }
}
