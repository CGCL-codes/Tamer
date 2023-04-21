package test.org.nakedobjects.object.reflect;

import org.nakedobjects.object.AdapterFactory;
import org.nakedobjects.object.NakedCollection;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedValue;

public class DummyAdapterFactory implements AdapterFactory {

    private NakedCollection nakedCollection;

    public NakedValue createValueAdapter(Object pojo) {
        return null;
    }

    public NakedCollection createCollectionAdapter(Object collection, NakedObjectSpecification specification) {
        return nakedCollection;
    }

    public void setupNakedCollection(NakedCollection nakedCollection) {
        this.nakedCollection = nakedCollection;
    }

    public void init() {
    }

    public void shutdown() {
    }
}
