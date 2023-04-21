package com.serie402.business.dao;

import com.kiss.fw.dto.AbstractDAODTO;
import com.kiss.fw.exceptions.DAOException;

public final class ClassifiedsDAO extends Serie402DAO {

    private static final ClassifiedsDAO instance = new ClassifiedsDAO();

    private ClassifiedsDAO() {
    }

    public static ClassifiedsDAO getInstance() {
        return instance;
    }

    @Override
    public AbstractDAODTO create(AbstractDAODTO _dto) throws DAOException {
        return _dto;
    }

    @Override
    public AbstractDAODTO read(AbstractDAODTO _dto) throws DAOException {
        return _dto;
    }

    @Override
    public AbstractDAODTO update(AbstractDAODTO _dto) throws DAOException {
        return _dto;
    }

    @Override
    public AbstractDAODTO delete(AbstractDAODTO _dto) throws DAOException {
        return _dto;
    }

    @Override
    public AbstractDAODTO search(AbstractDAODTO _dto) throws DAOException {
        return _dto;
    }
}
