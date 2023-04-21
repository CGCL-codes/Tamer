package ormsamples;

import org.orm.*;

public class CreateFLExDatabaseSchema {

    public static void main(String[] args) {
        try {
            ORMDatabaseInitiator.createSchema(orm.FLExPersistentManager.instance());
            orm.FLExPersistentManager.instance().disposePersistentManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
