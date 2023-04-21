package test.org.nakedobjects.object;

import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.utility.NakedObjectRuntimeException;
import java.util.Enumeration;
import java.util.Hashtable;

public class DummyNakedObjectSpecificationLoader implements NakedObjectSpecificationLoader {

    private Hashtable specs = new Hashtable();

    public NakedObjectSpecification loadSpecification(String name) {
        if (specs.containsKey(name)) {
            return (NakedObjectSpecification) specs.get(name);
        } else {
            throw new NakedObjectRuntimeException("no specification for " + name);
        }
    }

    public NakedObjectSpecification loadSpecification(Class cls) {
        return loadSpecification(cls.getName());
    }

    public NakedObjectSpecification[] allSpecifications() {
        NakedObjectSpecification[] specsArray;
        specsArray = new NakedObjectSpecification[specs.size()];
        int i = 0;
        Enumeration e = specs.elements();
        while (e.hasMoreElements()) {
            specsArray[i++] = (NakedObjectSpecification) e.nextElement();
        }
        return specsArray;
    }

    public void shutdown() {
    }

    public void init() {
    }

    public void addSpecification(NakedObjectSpecification specification) {
        specs.put(specification.getFullName(), specification);
    }
}
