package be.fedict.trust.service.bean;

import java.security.cert.X509Certificate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.fedict.trust.service.AdministratorService;
import be.fedict.trust.service.dao.AdministratorDAO;
import be.fedict.trust.service.entity.AdministratorEntity;
import be.fedict.trust.service.exception.RemoveLastAdminException;

/**
 * Administrator Service Bean implementation.
 * 
 * @author wvdhaute
 */
@Stateless
public class AdministratorServiceBean implements AdministratorService {

    private static final Log LOG = LogFactory.getLog(AdministratorServiceBean.class);

    @EJB
    private AdministratorDAO administratorDAO;

    public List<AdministratorEntity> listAdmins() {
        return this.administratorDAO.listAdmins();
    }

    /**
	 * {@inheritDoc}
	 */
    public AdministratorEntity register(X509Certificate authnCert) {
        LOG.debug("register");
        if (null == this.administratorDAO.findAdmin(authnCert)) {
            return this.administratorDAO.addAdmin(authnCert, false);
        }
        LOG.error("failed to register administrator");
        return null;
    }

    /**
	 * {@inheritDoc}
	 */
    public void register(AdministratorEntity admin) {
        LOG.debug("register pending admin");
        AdministratorEntity attachedAdminEntity = this.administratorDAO.attachAdmin(admin);
        attachedAdminEntity.setPending(false);
    }

    /**
	 * {@inheritDoc}
	 */
    public void remove(AdministratorEntity admin) throws RemoveLastAdminException {
        LOG.debug("remove admin: " + admin.getName());
        if (listAdmins().size() == 1) {
            LOG.error("cannot remove last administrator");
            throw new RemoveLastAdminException();
        }
        this.administratorDAO.removeAdmin(admin);
    }
}
