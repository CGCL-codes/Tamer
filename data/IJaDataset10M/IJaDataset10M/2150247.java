package org.plazmaforge.bsolution.document.server.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.plazmaforge.bsolution.document.common.beans.Document;
import org.plazmaforge.bsolution.document.common.beans.DocumentType;
import org.plazmaforge.bsolution.document.common.services.DocumentTypeService;
import org.plazmaforge.framework.core.exception.DAOException;
import org.plazmaforge.framework.service.OwnCriteriaService;
import org.plazmaforge.framework.service.hibernate.AbstractHibernateEntityService;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @author hapon
 *
 */
public class DocumentTypeServiceImpl extends AbstractHibernateEntityService<DocumentType, Integer> implements DocumentTypeService, OwnCriteriaService {

    protected Class getEntityClass() {
        return DocumentType.class;
    }

    protected void populateHibernateAliasMap(Map<String, String> map) {
        map.put("entity", "ent");
    }

    protected void populateHibernatePropertyMap(Map<String, String> map) {
        map.put("code", "ent.code");
        map.put("name", "ent.name");
    }

    protected void prepareFindCriteria(Criteria criteria) {
        super.prepareFindCriteria(criteria);
        criteria.add(Expression.ne("entity.id", "<SYS_ROW>"));
    }

    public List<DocumentType> sysLoadDocumentTypes() throws DAOException {
        return findAll();
    }

    public void setNewDocumentNo(Document document) {
        String documentCode = generateDocumentNo(document);
        if (documentCode == null) {
            return;
        }
        document.setDocumentNo(documentCode);
    }

    public String generateDocumentNo(Document document) {
        return generateDocumentNo(document.getDocumentType().getEntity().getId());
    }

    public String generateDocumentNo(DocumentType documentType) {
        return generateDocumentNo(documentType.getEntity().getId());
    }

    public String generateDocumentNo(String entityId) {
        return doGenerateDocumentNo(entityId);
    }

    protected String doGenerateDocumentNo(final String entityId) {
        return (String) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                return doGenerateDocumentNo(session, entityId);
            }
        });
    }

    /**
     * Generate new Document No by <code>tableName</code>
     * If number is NULL then auto generation is not supports.
     */
    protected String doGenerateDocumentNo(Session session, String tableName) {
        SQLQuery query = session.createSQLQuery("SELECT LAST_DOCUMENT_NUMBER, DOCUMENT_NO_PREFIX, DOCUMENT_NO_SUFFIX FROM DOCUMENT_TYPE WHERE ENTITY_ID = '" + tableName + "' AND IS_USE_NUMERATOR = 'Y'");
        List list = query.list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        Object[] row = (Object[]) list.get(0);
        Integer lastNumber = (Integer) row[0];
        String prefix = (String) row[1];
        String suffix = (String) row[2];
        ;
        lastNumber++;
        query = session.createSQLQuery("UPDATE DOCUMENT_TYPE SET LAST_DOCUMENT_NUMBER = " + lastNumber + " WHERE ENTITY_ID = '" + tableName + "'");
        query.executeUpdate();
        StringBuffer buf = new StringBuffer();
        if (prefix != null) {
            buf.append(prefix.trim());
        }
        buf.append(lastNumber);
        if (suffix != null) {
            buf.append(suffix.trim());
        }
        return buf.toString();
    }
}
