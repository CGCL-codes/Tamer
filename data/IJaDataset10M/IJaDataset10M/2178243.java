package artem.finance.server.ejb.vipiska.slsb;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;
import javax.ejb.CreateException;
import org.springframework.ejb.support.AbstractStatelessSessionBean;
import artem.finance.server.persist.Persistancible;
import artem.finance.server.persist.beans.VipiskaBean;
import artem.finance.server.service.vipiska.VipiskaServiceI;

public class VipiskaServiceSLSB extends AbstractStatelessSessionBean implements VipiskaServiceI {

    private static final long serialVersionUID = 1L;

    private static final String BEAN_NAME = "vipiskaServiceI";

    private VipiskaServiceI service;

    @Override
    protected void onEjbCreate() throws CreateException {
        service = (VipiskaServiceI) getBeanFactory().getBean(BEAN_NAME);
    }

    @Override
    public void delete(VipiskaBean vipiskaBean) throws RemoteException {
        service.delete(vipiskaBean);
    }

    @Override
    public List<VipiskaBean> findAll() throws RemoteException {
        return service.findAll();
    }

    @Override
    public List<Object> findByExample(VipiskaBean vipiskaBean, Date dateFrom, Date dateTo, boolean shouldFindAll) throws RemoteException {
        return service.findByExample(vipiskaBean, dateFrom, dateTo, shouldFindAll);
    }

    @Override
    public Persistancible findById(Long id) throws RemoteException {
        return service.findById(id);
    }

    @Override
    public List<String> getGroups() throws RemoteException {
        return service.getGroups();
    }

    @Override
    public void save(VipiskaBean vipiskaBean) throws RemoteException {
        service.save(vipiskaBean);
    }

    @Override
    public void saveOrUpdate(VipiskaBean vipiskaBean) throws RemoteException {
        service.saveOrUpdate(vipiskaBean);
    }
}
