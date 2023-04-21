package org.bpmsuite.facade.ejb.handler;

import java.util.Collection;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import org.bpmsuite.facade.ejb.interfaces.HrMetaDataServiceEjbFacade;
import org.bpmsuite.facade.ejb.interfaces.HrMetaDataServiceEjbFacadeHome;
import org.bpmsuite.facadehandler.AbstractEjbFacadeHandler;
import org.bpmsuite.facadehandler.FacadeHandlerException;
import org.bpmsuite.service.HrMetaDataService;
import org.bpmsuite.service.ServiceException;
import org.bpmsuite.vo.hrmetadata.Division;
import org.bpmsuite.vo.hrmetadata.Employee;

/**
 * @author Dirk Weiser
 */
public class HrMetaDataServiceEjbFacadeHandler extends AbstractEjbFacadeHandler implements HrMetaDataService {

    private HrMetaDataServiceEjbFacade _facade;

    public HrMetaDataServiceEjbFacadeHandler() {
    }

    public void init() throws FacadeHandlerException {
        try {
            InitialContext ctx = new javax.naming.InitialContext(getContextProperties());
            Object objHome = ctx.lookup(HrMetaDataServiceEjbFacadeHome.JNDI_NAME);
            HrMetaDataServiceEjbFacadeHome facadeHome = (HrMetaDataServiceEjbFacadeHome) PortableRemoteObject.narrow(objHome, HrMetaDataServiceEjbFacadeHome.class);
            _facade = facadeHome.create();
        } catch (Exception e) {
            throw new FacadeHandlerException(e.getMessage());
        }
    }

    public void destroy() throws FacadeHandlerException {
        try {
            _facade.remove();
        } catch (Exception e) {
            throw new FacadeHandlerException(e.getMessage());
        }
    }

    public Employee updateEmployee(Employee employee) throws ServiceException {
        try {
            init();
            Employee result = _facade.updateEmployee(employee);
            destroy();
            return result;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Employee retrieveEmployeeBySystemId(String systemId) throws ServiceException {
        try {
            init();
            Employee result = _facade.retrieveEmployeeBySystemId(systemId);
            destroy();
            return result;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Employee retrieveEmployeeByPersonnelNumber(String personnelNumber) throws ServiceException {
        try {
            init();
            Employee result = _facade.retrieveEmployeeByPersonnelNumber(personnelNumber);
            destroy();
            return result;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Collection retrieveEmployees(boolean active) throws ServiceException {
        try {
            init();
            Collection result = _facade.retrieveEmployees(active);
            destroy();
            return result;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public Division retrieveDivisionByName(String name) throws ServiceException {
        try {
            init();
            Division result = _facade.retrieveDivisionByName(name);
            destroy();
            return result;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
