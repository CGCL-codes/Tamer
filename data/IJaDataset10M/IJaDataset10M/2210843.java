package br.com.mcampos.ejb.cloudsystem.security.accesslog;

import br.com.mcampos.dto.security.AuthenticationDTO;
import br.com.mcampos.dto.user.login.AccessLogTypeDTO;
import br.com.mcampos.ejb.core.AbstractSecurity;
import br.com.mcampos.ejb.core.util.DTOFactory;
import br.com.mcampos.exception.ApplicationException;
import br.com.mcampos.sysutils.SysUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "AccessLogTypeFacade", mappedName = "CloudSystems-EjbPrj-AccessLogTypeFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccessLogTypeFacadeBean extends AbstractSecurity implements AccessLogTypeFacade {

    public static final Integer messageId = 11;

    @PersistenceContext(unitName = "EjbPrj")
    private EntityManager em;

    @EJB
    private AccessLogTypeSessionLocal session;

    public AccessLogTypeFacadeBean() {
    }

    public List<AccessLogTypeDTO> getAll(AuthenticationDTO currentUser) throws ApplicationException {
        authenticate(currentUser);
        List<AccessLogType> types = session.getAll();
        return toDTO(types);
    }

    public Integer getNextId(AuthenticationDTO currentUser) throws ApplicationException {
        authenticate(currentUser);
        return session.getNextId();
    }

    public void add(AuthenticationDTO currentUser, AccessLogTypeDTO dto) throws ApplicationException {
        authenticate(currentUser);
        AccessLogType entity = session.get(dto.getId());
        if (entity != null) throwException(1);
        entity = DTOFactory.copy(dto);
        session.add(entity);
    }

    public void update(AuthenticationDTO currentUser, AccessLogTypeDTO dto) throws ApplicationException {
        authenticate(currentUser);
        AccessLogType entity = session.get(dto.getId());
        if (entity == null) throwException(2);
        entity.setDescription(dto.getDescription());
        session.update(entity);
    }

    public void delete(AuthenticationDTO currentUser, AccessLogTypeDTO dto) throws ApplicationException {
        authenticate(currentUser);
        AccessLogType entity = session.get(dto.getId());
        if (entity == null) throwException(3);
        session.delete(entity.getId());
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    public Integer getMessageTypeId() {
        return messageId;
    }

    public static List<AccessLogTypeDTO> toDTO(List<AccessLogType> types) {
        if (SysUtils.isEmpty(types)) return Collections.emptyList();
        List<AccessLogTypeDTO> dtos = new ArrayList<AccessLogTypeDTO>(types.size());
        for (AccessLogType type : types) dtos.add(type.toDTO());
        return dtos;
    }
}
