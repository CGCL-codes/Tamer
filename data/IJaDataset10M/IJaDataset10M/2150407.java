package mx.com.nyak.empresa.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import mx.com.nyak.base.AbstractBaseService;
import mx.com.nyak.base.util.StackTraceUtil;
import mx.com.nyak.empresa.bo.PaisBO;
import mx.com.nyak.empresa.exception.BusinessException;
import mx.com.nyak.empresa.exception.ServiceException;
import mx.com.nyak.empresa.hbm.Pais;
import mx.com.nyak.empresa.service.PaisService;

public class PaisServicePojoImpl implements PaisService {

    private PaisBO paisBO;

    private List<Pais> paises;

    private Pais pais;

    private Logger logger = Logger.getLogger(PaisServicePojoImpl.class);

    public void savePais(Pais pais) throws ServiceException {
        try {
            getPaisBO().savePais(pais);
        } catch (BusinessException e) {
            throw new ServiceException(e);
        }
    }

    public List<Pais> findAllPaises() throws ServiceException {
        logger.info("Entra a buscar todas las paises");
        try {
            paises = getPaisBO().findAllPaises();
        } catch (BusinessException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new ServiceException(e);
        }
        return paises;
    }

    public void deletePais(Pais pais) throws ServiceException {
        try {
            getPaisBO().deletePais(pais);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public Pais findPaisById(Integer id) throws ServiceException {
        try {
            pais = getPaisBO().findPaisById(id);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
        return pais;
    }

    public List<Pais> findPaisesByExample(Pais pais) throws ServiceException {
        try {
            paises = getPaisBO().findPaisesByExample(pais);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
        return paises;
    }

    public List<Pais> findPaisesByProperty(String propertyName, Object value) throws ServiceException {
        try {
            paises = getPaisBO().findPaisesByProperty(propertyName, value);
        } catch (BusinessException e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
        return paises;
    }

    public void setPaisBO(PaisBO paisBO) {
        this.paisBO = paisBO;
    }

    public PaisBO getPaisBO() {
        return paisBO;
    }
}
