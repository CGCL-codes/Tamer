package mx.com.nyak.base.bo;

import java.util.List;
import mx.com.nyak.empresa.exception.BusinessException;
import mx.com.nyak.empresa.hbm.Empleado;

public interface CRUDBusinessObject {

    public List<?> findByProperty(String propertyName, Object value) throws BusinessException;

    public void save(Object dataTransferObject) throws BusinessException;

    public void delete(Object dataTransferObject) throws BusinessException;

    public Object findById(Integer id) throws BusinessException;
}
