package com.serie402.business.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.kiss.fw.dto.AbstractDAODTO;
import com.kiss.fw.exceptions.DAOException;
import com.kiss.fw.utils.Constants;
import com.kiss.fw.utils.Util;
import com.serie402.business.dao.dto.AuthorDAODTO;
import com.serie402.business.dao.mappers.AuthorMapper;

public final class AuthorDAO extends Serie402DAO {

    private static final Logger logger = Logger.getLogger(AuthorDAO.class);

    private static final AuthorDAO instance = new AuthorDAO();

    private AuthorDAO() {
    }

    public static AuthorDAO getInstance() {
        return instance;
    }

    @Override
    public AbstractDAODTO create(AbstractDAODTO _dto) throws DAOException {
        AuthorDAODTO dto = getAuthorDAODTO(_dto);
        SqlSession sqlSession = openSqlSession();
        AuthorMapper mapper = MapperFactory.getAuthorMapper(sqlSession);
        try {
            mapper.createAuthor(dto.getAuthor());
        } catch (Exception _exception) {
            Util.handleException(logger, "", _exception);
            throw Constants.Exceptions.STORED_PROCEDURE_EXCEPTION;
        }
        return _dto;
    }

    @Override
    public AbstractDAODTO read(AbstractDAODTO _dto) throws DAOException {
        AuthorDAODTO dto = getAuthorDAODTO(_dto);
        SqlSession sqlSession = openSqlSession();
        AuthorMapper mapper = MapperFactory.getAuthorMapper(sqlSession);
        try {
            if (_dto.isSearch()) {
                mapper.searchAuthors(dto.getAuthor());
            } else {
                mapper.getAuthor(dto.getAuthorId());
            }
        } catch (Exception _exception) {
            Util.handleException(logger, "", _exception);
            throw Constants.Exceptions.STORED_PROCEDURE_EXCEPTION;
        }
        return _dto;
    }

    @Override
    public AbstractDAODTO update(AbstractDAODTO _dto) throws DAOException {
        AuthorDAODTO dto = getAuthorDAODTO(_dto);
        SqlSession sqlSession = openSqlSession();
        AuthorMapper mapper = MapperFactory.getAuthorMapper(sqlSession);
        try {
            mapper.updateAuthor(dto.getAuthor());
        } catch (Exception _exception) {
            Util.handleException(logger, "", _exception);
            throw Constants.Exceptions.STORED_PROCEDURE_EXCEPTION;
        }
        return _dto;
    }

    @Override
    public AbstractDAODTO delete(AbstractDAODTO _dto) throws DAOException {
        AuthorDAODTO dto = getAuthorDAODTO(_dto);
        SqlSession sqlSession = openSqlSession();
        AuthorMapper mapper = MapperFactory.getAuthorMapper(sqlSession);
        try {
            mapper.deleteAuthor(dto.getAuthorId());
        } catch (Exception _exception) {
            Util.handleException(logger, "", _exception);
            throw Constants.Exceptions.STORED_PROCEDURE_EXCEPTION;
        }
        return _dto;
    }

    @Override
    public AbstractDAODTO search(AbstractDAODTO _dto) throws DAOException {
        return null;
    }

    private AuthorDAODTO getAuthorDAODTO(final AbstractDAODTO _dto) {
        return (AuthorDAODTO) _dto;
    }
}
