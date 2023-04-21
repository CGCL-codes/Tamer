package org.neodatis.odb.test.fromusers.kasper;

import org.neodatis.odb.ODB;
import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.tool.mutex.Mutex;
import org.neodatis.tool.mutex.MutexFactory;

public class SequenceTrigger extends org.neodatis.odb.core.server.trigger.ServerInsertTrigger {

    public void afterInsert(ObjectRepresentation objectRepresentation, OID oid) {
    }

    public boolean beforeInsert(ObjectRepresentation objectRepresentation) {
        String className = objectRepresentation.getObjectClassName();
        Mutex mutex = MutexFactory.get("SEQUENCE_MUTEX_[" + className + "]");
        System.out.print("SequenceTrigger called by " + className);
        try {
            Long existingId = null;
            try {
                existingId = (Long) objectRepresentation.getValueOf("id");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("\n\t\t\t error with " + objectRepresentation.getObjectClassName());
                return false;
            }
            if (existingId == null || existingId.longValue() == 0) {
                mutex.acquire("SEQUENCE_TRIGGER_[" + className + "]");
                long id = getNextId(className);
                objectRepresentation.setValueOf("id", new Long(id));
                System.out.println(" which got the ID " + id);
                return true;
            }
            System.out.println(" which already got the ID " + existingId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in SequenceTrigger. Code should be improved.", e);
        } finally {
            if (mutex != null && mutex.isInUse()) {
                mutex.release("SEQUENCE_TRIGGER_[" + className + "]");
            }
        }
        return false;
    }

    /**
	 * Actually gets the next id Gets the object of type ID from the database
	 * with the specific name. Then increment the id value and returns. If
	 * object does not exist, creates t.
	 * 
	 * @param idName
	 * @return
	 */
    private long getNextId(String className) {
        ODB odb = getOdb();
        Objects objects = odb.getObjects(new CriteriaQuery(Sequence.class, Where.equal("className", className)));
        if (objects.isEmpty()) {
            Sequence sequence = new Sequence(className, 1L);
            odb.store(sequence);
            return 1L;
        }
        Sequence sequence = (Sequence) objects.getFirst();
        sequence.increment();
        odb.store(sequence);
        return sequence.getId();
    }
}
