package datadog.changesets;

import datadog.id.Identity;
import datadog.policy.PersistencePolicy;
import datadog.services.ClassService;
import datadog.sessions.SessionProvider;
import datadog.sql.SqlValues;

/**
 * @author tomichj
 */
public class DeleteChangeRecord extends BaseChangeRecord implements ChangeRecord {

    Object instance;

    public DeleteChangeRecord(Object instance, ClassService service, PersistencePolicy savePolicy) {
        super(service, savePolicy);
        this.instance = instance;
    }

    public void addTo(TransactionChangeSet changeSet) {
        changeSet.add(this);
    }

    public boolean contains(Object instance) {
        return this.instance == instance;
    }

    public Object getInstance() {
        return instance;
    }

    public SqlValues[] prepareSql() {
        return persistencePolicy().delete(instance, getService());
    }

    public void syncToCache(SessionProvider session) {
        Identity id = getService().identify(instance);
        session.getCache().remove(id);
    }

    public int compareTo(Object o) {
        DeleteChangeRecord otherRecord = (DeleteChangeRecord) o;
        ClassService otherService = otherRecord.getService();
        Object otherInstance = otherRecord.getInstance();
        if (getService().dependsOn(getInstance(), otherInstance)) return -1;
        if (otherService.dependsOn(otherInstance, getInstance())) return 1;
        return 0;
    }

    public String toString() {
        return "DeleteChangeRecord{" + "instance=" + instance + "persistencePolicy:" + persistencePolicy() + "service=" + getService() + "}";
    }
}
