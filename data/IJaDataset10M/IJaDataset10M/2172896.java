package mx.com.nyak.empresa.bo;

import java.util.List;
import mx.com.nyak.base.bo.AbstractBaseBO;
import mx.com.nyak.base.util.StackTraceUtil;
import mx.com.nyak.empresa.dao.EntidadFederativaDAO;
import mx.com.nyak.empresa.exception.BusinessException;
import mx.com.nyak.empresa.exception.DataAccesException;
import mx.com.nyak.empresa.hbm.EntidadFederativa;

public class EntidadFederativaBO extends AbstractBaseBO {

    private EntidadFederativaDAO entidadFederativaDAO;

    private EntidadFederativa entidadFederativa;

    private List<EntidadFederativa> entidadFederativas;

    public void saveEntidadFederativa(EntidadFederativa entidadFederativa) throws BusinessException {
        try {
            entidadFederativaDAO.save(entidadFederativa);
            logger.debug("INSERTO LA EMPRESA: ");
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public void deleteEntidadFederativa(EntidadFederativa entidadFederativa) throws BusinessException {
        try {
            entidadFederativaDAO.delete(entidadFederativa);
            logger.debug("BORRO entidadFederativa: ");
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public EntidadFederativa findEntidadFederativaById(Integer id) throws BusinessException {
        try {
            entidadFederativa = entidadFederativaDAO.findById(id);
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return entidadFederativa;
    }

    public List<EntidadFederativa> findEntidadFederativasByExample(EntidadFederativa entidadFederativa) throws BusinessException {
        try {
            entidadFederativas = entidadFederativaDAO.findByExample(entidadFederativa);
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return entidadFederativas;
    }

    public List<EntidadFederativa> findEntidadFederativasByProperty(String propertyName, Object value) throws BusinessException {
        try {
            entidadFederativas = entidadFederativaDAO.findByProperty(propertyName, value);
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return entidadFederativas;
    }

    public List<EntidadFederativa> findAllEntidadFederativas() throws BusinessException {
        try {
            entidadFederativas = entidadFederativaDAO.findAll();
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return entidadFederativas;
    }

    public void merge(EntidadFederativa entidadFederativa) throws BusinessException {
        try {
            entidadFederativas = entidadFederativaDAO.findAll();
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public void setEntidadFederativaDAO(EntidadFederativaDAO entidadFederativaDAO) {
        this.entidadFederativaDAO = entidadFederativaDAO;
    }

    public EntidadFederativaDAO getEntidadFederativaDAO() {
        return entidadFederativaDAO;
    }

    public void setEntidadFederativas(List<EntidadFederativa> entidadFederativas) {
        this.entidadFederativas = entidadFederativas;
    }

    public List<EntidadFederativa> getEntidadFederativas() {
        return entidadFederativas;
    }

    public EntidadFederativa getEntidadFederativa() {
        return entidadFederativa;
    }

    public void setEntidadFederativa(EntidadFederativa entidadFederativa) {
        this.entidadFederativa = entidadFederativa;
    }
}
