package mx.com.nyak.empresa.bo;

import java.util.List;
import mx.com.nyak.base.bo.AbstractBaseBO;
import mx.com.nyak.base.util.StackTraceUtil;
import mx.com.nyak.empresa.dao.EmpresaDAO;
import mx.com.nyak.empresa.exception.BusinessException;
import mx.com.nyak.empresa.exception.DataAccesException;
import mx.com.nyak.empresa.hbm.Persona;
import mx.com.nyak.empresa.hbm.Empresa;

/** 
 * 
 * Derechos Reservados (c)Jose Carlos Perez Cervantes 2009 
 * 
 * 
 * */
public class EmpresaBO extends AbstractBaseBO {

    private EmpresaDAO empresaDAO;

    private Persona persona;

    private Empresa empresa;

    private List<Persona> personas;

    private List<Empresa> empresas;

    public void saveEmpresa(Empresa empresa) throws BusinessException {
        try {
            empresaDAO.save(empresa);
            logger.debug("INSERTO LA EMPRESA: " + getPersona());
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public void deleteEmpresa(Empresa empresa) throws BusinessException {
        try {
            empresaDAO.delete(empresa);
            logger.debug("BORRO empresa: ");
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public Empresa findEmpresaById(Integer id) throws BusinessException {
        try {
            empresa = empresaDAO.findById(id);
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return empresa;
    }

    public List<Empresa> findEmpresasByExample(Empresa empresa) throws BusinessException {
        try {
            empresas = empresaDAO.findByExample(empresa);
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return empresas;
    }

    public List<Empresa> findEmpresasByProperty(String propertyName, Object value) throws BusinessException {
        try {
            empresas = empresaDAO.findByProperty(propertyName, value);
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return empresas;
    }

    public List<Empresa> findAllEmpresas() throws BusinessException {
        try {
            empresas = empresaDAO.findAll();
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
        return empresas;
    }

    public void merge(Empresa empresa) throws BusinessException {
        try {
            empresas = empresaDAO.findAll();
        } catch (DataAccesException e) {
            logger.error(StackTraceUtil.getStackTrace(e));
            throw new BusinessException(e);
        }
    }

    public void setEmpresaDAO(EmpresaDAO empresaDAO) {
        this.empresaDAO = empresaDAO;
    }

    public EmpresaDAO getEmpresaDAO() {
        return empresaDAO;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
