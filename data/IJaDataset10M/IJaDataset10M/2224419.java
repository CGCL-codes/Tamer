package menfis.view.mbean;

import java.io.Serializable;
import menfis.view.util.ServiceLocator;
import smi.view.util.FacesUtils;

/**
 *
 * @author lpelegrini
 */
public class BaseMBean {

    protected ServiceLocator serviceLocator;

    public ServiceLocator getServiceLocator() {
        return this.serviceLocator;
    }

    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public BaseMBean() {
        this.serviceLocator = (ServiceLocator) FacesUtils.getManagedBean(MBeanNames.SERVICE_LOCATOR_BEAN);
    }
}
