package be.fedict.trust.admin.portal.bean;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.security.Admin;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.jboss.seam.log.Log;
import be.fedict.trust.admin.portal.AdminConstants;
import be.fedict.trust.admin.portal.CertificateAuthority;
import be.fedict.trust.service.TrustDomainService;
import be.fedict.trust.service.entity.CertificateAuthorityEntity;

@Stateful
@Name(AdminConstants.ADMIN_SEAM_PREFIX + "certificateAuthority")
@LocalBinding(jndiBinding = AdminConstants.ADMIN_JNDI_CONTEXT + "CertificateAuthorityBean")
public class CertificateAuthorityBean implements CertificateAuthority {

    private static final String CERTIFICATE_AUTHORITY_LIST_NAME = AdminConstants.ADMIN_SEAM_PREFIX + "caList";

    @Logger
    private Log log;

    @EJB
    private TrustDomainService trustDomainService;

    @In
    FacesMessages facesMessages;

    @DataModel(CERTIFICATE_AUTHORITY_LIST_NAME)
    @SuppressWarnings("unused")
    private List<CertificateAuthorityEntity> caList;

    @DataModelSelection(CERTIFICATE_AUTHORITY_LIST_NAME)
    private CertificateAuthorityEntity selectedCA;

    @Factory(CERTIFICATE_AUTHORITY_LIST_NAME)
    public void certificateAuthorityListFactory() {
        this.caList = this.trustDomainService.listCAs();
    }

    @Remove
    @Destroy
    public void destroyCallback() {
        this.log.debug("#destroy");
    }

    @Admin
    public String refresh() {
        this.log.debug("refresh CA cache: #0", this.selectedCA.getName());
        try {
            this.trustDomainService.refreshCACache(this.selectedCA);
        } catch (JMSException e) {
            this.facesMessages.addFromResourceBundle(StatusMessage.Severity.ERROR, "errorHarvesterNotification");
            return null;
        }
        return "success";
    }

    @Admin
    public long getCachedCertificates() {
        return this.trustDomainService.getTotalCachedCertificates();
    }

    @Admin
    public long getCachedCAs() {
        return this.trustDomainService.getTotalActiveCachedCAs();
    }
}
