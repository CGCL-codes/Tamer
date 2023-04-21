package datadog.policy;

import datadog.services.ClassService;
import datadog.services.values.ObjectInsertWriter;
import datadog.services.values.ObjectUpdateWriter;
import datadog.sql.SqlValues;
import datadog.util.Validate;

/**
 * @author Justin Tomich
 */
public abstract class BaseSavePolicy implements SavePolicy {

    DeletePolicy deletePolicy = new DeleteByTableValuePolicy();

    public SqlValues[] delete(Object instance, ClassService service) {
        return deletePolicy.delete(instance, service);
    }

    /**
     * Prepare sql statements to insert values for instance.
     *
     * @param instance
     * @param service
     * @return BoundSql array of sql to insert.
     */
    final SqlValues[] getInsert(Object instance, ClassService service) {
        Validate.notNull(instance);
        Validate.notNull(service);
        ObjectInsertWriter writer = service.objectWriteInsert(instance);
        return writer.write();
    }

    final SqlValues[] getUpdate(Object instance, Object oldInstance, ClassService service) {
        Validate.notNull(instance);
        Validate.notNull(service);
        ObjectUpdateWriter writer = service.objectWriteUpdate(oldInstance, instance);
        return writer.write();
    }
}
