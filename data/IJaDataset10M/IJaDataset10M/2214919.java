package org.neodatis.odb.test.server.trigger;

import org.neodatis.odb.OID;
import org.neodatis.odb.ObjectRepresentation;
import org.neodatis.odb.core.server.trigger.ServerDeleteTrigger;

public class MyDeleteTriggerForAllClasses extends ServerDeleteTrigger {

    private int nbDeletesAfter;

    private int nbDeletesBefore;

    public int getNbDeletesAfter() {
        return nbDeletesAfter;
    }

    public int getNbDeletesBefore() {
        return nbDeletesBefore;
    }

    public void afterDelete(ObjectRepresentation objectRepresentation, OID oid) {
        System.out.println("allclass: object with oid " + oid + " has been deleted");
        nbDeletesAfter++;
    }

    public boolean beforeDelete(ObjectRepresentation objectRepresentation, OID oid) {
        System.out.println("allclass : object is going to be deleted " + oid);
        nbDeletesBefore++;
        return false;
    }
}
