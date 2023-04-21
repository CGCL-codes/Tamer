package ormsamples;

import org.orm.*;

public class CreateGoodReading3DatabaseSchema {

    public static void main(String[] args) {
        try {
            ORMDatabaseInitiator.createSchema(common.GoodReading3PersistentManager.instance());
            common.GoodReading3PersistentManager.instance().disposePersistentManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
