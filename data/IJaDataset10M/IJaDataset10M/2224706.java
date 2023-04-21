package at.redcross.tacos.web.beans;

import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import org.ajax4jsf.model.KeepAlive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.redcross.tacos.dbal.entity.SecuredResource;
import at.redcross.tacos.dbal.helper.SecuredResourceHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.beans.dto.DtoHelper;
import at.redcross.tacos.web.beans.dto.EntryState;
import at.redcross.tacos.web.beans.dto.GenericDto;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebPermissionListenerRegistry;

@KeepAlive
@ManagedBean(name = "securedResourceMaintenanceBean")
public class SecuredResourceMaintenanceBean extends SecuredBean {

    private static final long serialVersionUID = -8165438157429138118L;

    private static final Logger logger = LoggerFactory.getLogger(SecuredResourceMaintenanceBean.class);

    /** the available secured resources */
    private List<GenericDto<SecuredResource>> resources;

    /** the id of the selected resources */
    private long securedResourceId;

    @Override
    public void init() throws Exception {
        super.init();
        EntityManager manager = null;
        try {
            manager = EntityManagerFactory.createEntityManager();
            resources = DtoHelper.fromList(SecuredResource.class, SecuredResourceHelper.list(manager));
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    public void removeSecuredResource(ActionEvent event) {
        Iterator<GenericDto<SecuredResource>> iter = resources.iterator();
        while (iter.hasNext()) {
            GenericDto<SecuredResource> dto = iter.next();
            SecuredResource securedResource = dto.getEntity();
            if (securedResource.getId() != securedResourceId) {
                continue;
            }
            if (dto.getState() == EntryState.NEW) {
                iter.remove();
            }
            dto.setState(EntryState.DELETE);
        }
    }

    public void unremoveSecuredResource(ActionEvent event) {
        for (GenericDto<SecuredResource> dto : resources) {
            SecuredResource securedResource = dto.getEntity();
            if (securedResource.getId() != securedResourceId) {
                continue;
            }
            dto.setState(EntryState.SYNC);
        }
    }

    public void addSecuredResource(ActionEvent event) {
        GenericDto<SecuredResource> dto = new GenericDto<SecuredResource>(new SecuredResource());
        dto.setState(EntryState.NEW);
        resources.add(dto);
    }

    public void saveSecuredResources() {
        EntityManager manager = null;
        try {
            int errorCount = 0;
            for (GenericDto<SecuredResource> dto : resources) {
                if (dto.getState() == EntryState.DELETE) {
                    continue;
                }
                SecuredResource resource = dto.getEntity();
                String access = resource.getAccess();
                if (!isValidExpression(access)) {
                    FacesUtils.addErrorMessage("No valid access definition");
                    errorCount++;
                }
            }
            if (errorCount > 0) {
                return;
            }
            manager = EntityManagerFactory.createEntityManager();
            DtoHelper.syncronize(manager, resources);
            EntityManagerHelper.commit(manager);
            DtoHelper.filter(resources);
            WebPermissionListenerRegistry.getInstance().notifyInvalidateCache();
        } catch (Exception ex) {
            logger.error("Failed to persist the secured resources", ex);
            FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
        } finally {
            EntityManagerHelper.close(manager);
        }
    }

    public void validateSecuredResource(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String stringValue = (String) value;
        if (isValidExpression(stringValue)) {
            return;
        }
        throw new ValidatorException(FacesUtils.createErrorMessage("No valid access definition"));
    }

    public void setSecuredResourceId(long securedResourceId) {
        this.securedResourceId = securedResourceId;
    }

    public long getSecuredResourceId() {
        return securedResourceId;
    }

    public List<GenericDto<SecuredResource>> getResources() {
        return resources;
    }
}
