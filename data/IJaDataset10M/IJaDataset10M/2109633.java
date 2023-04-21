package mx.com.nyak.empresa.service;

import java.util.List;
import mx.com.nyak.empresa.exception.ServiceException;
import mx.com.nyak.empresa.hbm.Telefono;

/** 
 * 
 * Derechos Reservados (c)Jose Carlos Perez Cervantes 2009 
 * 
 * 
 * */
public interface TelfonoService {

    public void saveTelefono(Telefono email) throws ServiceException;

    public List<Telefono> findAllTelefonos() throws ServiceException;

    public void deleteTelefono(Telefono email) throws ServiceException;

    public List<Telefono> findTelefonosByProperty(String propertyName, Object value) throws ServiceException;

    public Telefono findTelefonoById(Integer id) throws ServiceException;
}
