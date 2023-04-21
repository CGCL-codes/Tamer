package persistence.util;

import java.rmi.RemoteException;
import persistence.PersistentClass;
import persistence.PersistentObject;

public final class HashMapClass extends PersistentClass {

    public void init(Class clazz) {
        super.init(clazz);
        setNULL_KEY(create(PersistentObject.class));
    }

    protected boolean banned(String property) {
        return super.banned(property) || property.equals("empty");
    }

    protected PersistentObject.Accessor createAccessor() throws RemoteException {
        return new Accessor();
    }

    protected class Accessor extends PersistentClass.Accessor {

        public Accessor() throws RemoteException {
        }

        public PersistentObject NULL_KEY() {
            return getNULL_KEY();
        }
    }

    PersistentObject NULL_KEY() {
        return (PersistentObject) executeAtomic(new MethodCall("NULL_KEY", new Class[] {}, new Object[] {}));
    }

    public PersistentObject getNULL_KEY() {
        return (PersistentObject) get("NULL_KEY");
    }

    public void setNULL_KEY(PersistentObject obj) {
        set("NULL_KEY", obj);
    }
}
