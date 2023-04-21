package ch.unibe.a3ubAdmin.persistence.serializedtables;

public class ProperstiesOldThread extends Thread {

    private QuickPersistenceManagerPropertiesOld_v02 loc = null;

    ProperstiesOldThread(QuickPersistenceManagerPropertiesOld_v02 loc) {
        this.loc = loc;
    }

    public void run() {
        loc.SetMap(loc.getMap());
    }
}
