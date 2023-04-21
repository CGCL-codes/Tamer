package br.com.mcampos.ejb.cloudsystem.resale.dealer.type;

import br.com.mcampos.dto.resale.DealerTypeDTO;
import br.com.mcampos.dto.security.AuthenticationDTO;
import br.com.mcampos.ejb.cloudsystem.resale.dealer.type.entity.DealerType;
import br.com.mcampos.ejb.cloudsystem.resale.dealer.type.session.DealerTypeSessionLocal;
import br.com.mcampos.ejb.core.AbstractSecurity;
import br.com.mcampos.exception.ApplicationException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "DealerTypeFacade", mappedName = "CloudSystems-EjbPrj-DealerTypeFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DealerTypeFacadeBean extends AbstractSecurity implements DealerTypeFacade {

    public static final Integer messageId = 35;

    @PersistenceContext(unitName = "EjbPrj")
    private transient EntityManager em;

    @EJB
    private DealerTypeSessionLocal session;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Integer getMessageTypeId() {
        return messageId;
    }

    public Integer nextId(AuthenticationDTO auth) throws ApplicationException {
        authenticate(auth);
        return session.nextId();
    }

    public void delete(AuthenticationDTO auth, Integer id) throws ApplicationException {
        authenticate(auth);
        session.delete(id);
    }

    public DealerTypeDTO get(AuthenticationDTO auth, Integer id) throws ApplicationException {
        authenticate(auth);
        return DealerTypeUtil.copy(session.get(id));
    }

    public DealerTypeDTO add(AuthenticationDTO auth, DealerTypeDTO dto) throws ApplicationException {
        authenticate(auth);
        return DealerTypeUtil.copy(session.add(DealerTypeUtil.createEntity(dto)));
    }

    public DealerTypeDTO update(AuthenticationDTO auth, DealerTypeDTO dto) throws ApplicationException {
        authenticate(auth);
        DealerType type = session.get(dto.getId());
        return DealerTypeUtil.copy(session.add(DealerTypeUtil.update(type, dto)));
    }

    public List<DealerTypeDTO> getAll(AuthenticationDTO auth) throws ApplicationException {
        authenticate(auth);
        return DealerTypeUtil.toDTOList(session.getAll());
    }
}
