package at.redcross.tacos.web.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import org.ajax4jsf.model.KeepAlive;
import org.apache.log4j.Logger;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import at.redcross.tacos.dbal.entity.EntityImpl;
import at.redcross.tacos.dbal.entity.RevisionInfoChange;
import at.redcross.tacos.dbal.entity.RevisionInfoEntry;
import at.redcross.tacos.dbal.helper.AuditQueryHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.dbal.utils.ObjectChange;
import at.redcross.tacos.dbal.utils.ObjectComparator;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "historyBean")
public class HistoryBean extends PagingBean {

    private static final long serialVersionUID = 1L;

    /** the primary key of the entity to show the revision */
    private Long primaryKey;

    /** the class to show the history information */
    private String className;

    /** the entity for that the history is shown */
    private EntityImpl entity;

    /** THe entity manager (only valid during #queryHistory) */
    private EntityManager manager;

    /** the history entries */
    private List<RevisionInfoEntry> revisionEntries;

    @PostConstruct
    public void setup() {
        maxResults = 10;
    }

    @Override
    protected void init() throws Exception {
    }

    public void queryHistory(ActionEvent event) {
        Class<?> entityClass;
        try {
            entityClass = Class.forName(className);
            manager = EntityManagerFactory.createEntityManager();
            entity = (EntityImpl) manager.find(entityClass, primaryKey);
            AuditReader auditReader = AuditReaderFactory.get(manager);
            AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(entityClass, false, true);
            auditQuery = auditQuery.add(AuditEntity.id().eq(primaryKey));
            auditQuery.addOrder(AuditEntity.revisionNumber().asc());
            revisionEntries = AuditQueryHelper.listRevisions(auditQuery);
            if (revisionEntries != null && !revisionEntries.isEmpty()) {
                computeChanges(revisionEntries);
            }
            Collections.reverse(revisionEntries);
        } catch (Exception e) {
            Logger.getLogger(HistoryBean.class).error(e);
            FacesUtils.addErrorMessage("Änderungshistorie konnte nicht geladen werden");
        } finally {
            manager = EntityManagerHelper.close(manager);
        }
    }

    /** Computes the changes for the given revisions */
    private void computeChanges(List<RevisionInfoEntry> entries) throws Exception {
        EntityImpl baseEntity = null;
        for (RevisionInfoEntry entry : entries) {
            if (baseEntity == null) {
                baseEntity = entry.getEntity();
                continue;
            }
            ObjectComparator comparator = new ObjectComparator(baseEntity, entry.getEntity());
            comparator.setManager(manager);
            List<RevisionInfoChange> changes = new ArrayList<RevisionInfoChange>();
            for (ObjectChange change : comparator.getChanges()) {
                RevisionInfoChange revisionChange = new RevisionInfoChange(change.getAttribute());
                revisionChange.setNewValue(change.getNewValue());
                revisionChange.setOldValue(change.getOldValue());
                changes.add(revisionChange);
            }
            entry.addChanges(changes);
            baseEntity = entry.getEntity();
        }
    }

    public void setPrimaryKey(Long primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getPrimaryKey() {
        return primaryKey;
    }

    public String getClassName() {
        return className;
    }

    public EntityImpl getEntity() {
        return entity;
    }

    public List<RevisionInfoEntry> getRevisionEntries() {
        return revisionEntries;
    }
}
