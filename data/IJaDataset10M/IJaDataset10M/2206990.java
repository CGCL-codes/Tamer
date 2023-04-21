package br.com.mcampos.ejb.cloudsystem.user.attribute.documenttype.facade;

import br.com.mcampos.dto.security.AuthenticationDTO;
import br.com.mcampos.dto.user.attributes.DocumentTypeDTO;
import br.com.mcampos.ejb.cloudsystem.user.attribute.documenttype.DocumentTypeUtil;
import br.com.mcampos.ejb.cloudsystem.user.attribute.documenttype.entity.DocumentType;
import br.com.mcampos.ejb.cloudsystem.user.attribute.documenttype.session.DocumentTypeSessionLocal;
import br.com.mcampos.ejb.core.AbstractSecurity;
import br.com.mcampos.exception.ApplicationException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "DocumentTypeFacade", mappedName = "CloudSystems-EjbPrj-DocumentTypeFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DocumentTypeFacadeBean extends AbstractSecurity implements DocumentTypeFacade {

    public static final Integer messageId = 19;

    @PersistenceContext(unitName = "EjbPrj")
    private transient EntityManager em;

    @EJB
    private DocumentTypeSessionLocal session;

    public DocumentTypeFacadeBean() {
    }

    public Integer nextId(AuthenticationDTO auth) throws ApplicationException {
        authenticate(auth);
        return session.nextIntegerId();
    }

    public void delete(AuthenticationDTO auth, Integer id) throws ApplicationException {
        authenticate(auth);
        DocumentType entity = session.get(id);
        if (entity == null) throwException(3);
        session.delete(id);
    }

    public DocumentTypeDTO get(AuthenticationDTO auth, Integer id) throws ApplicationException {
        authenticate(auth);
        return DocumentTypeUtil.copy(session.get(id));
    }

    public DocumentTypeDTO add(AuthenticationDTO auth, DocumentTypeDTO dto) throws ApplicationException {
        authenticate(auth);
        DocumentType entity = session.get(dto.getId());
        if (entity != null) throwException(1);
        entity = session.add(DocumentTypeUtil.createEntity(dto));
        return DocumentTypeUtil.copy(entity);
    }

    public DocumentTypeDTO update(AuthenticationDTO auth, DocumentTypeDTO dto) throws ApplicationException {
        authenticate(auth);
        DocumentType entity = session.get(dto.getId());
        if (entity == null) throwException(1);
        entity = session.update(DocumentTypeUtil.update(entity, dto));
        return DocumentTypeUtil.copy(entity);
    }

    public List<DocumentTypeDTO> getAll() throws ApplicationException {
        return DocumentTypeUtil.toDTOList(session.getAll());
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    public Integer getMessageTypeId() {
        return messageId;
    }
}
