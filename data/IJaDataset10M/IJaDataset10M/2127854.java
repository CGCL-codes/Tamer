package br.uece.tcc.fh.dao;

import java.util.List;
import br.uece.tcc.fh.dao.util.FhQuery;
import br.uece.tcc.fh.exception.FhDAOException;

public abstract class FhDBDAO implements FhDAO {

    public int create(FhQuery query) throws FhDAOException {
        return 0;
    }

    public int delete(FhQuery query) throws FhDAOException {
        return 0;
    }

    public Object find(FhQuery query) throws FhDAOException {
        return null;
    }

    public List read(FhQuery query) throws FhDAOException {
        return null;
    }

    public int update(FhQuery query) throws FhDAOException {
        return 0;
    }
}
