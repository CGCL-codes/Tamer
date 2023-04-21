package fitlibraryGeneric.specify.genericFinder;

import java.lang.reflect.Type;
import fitlibrary.object.DomainFixtured;
import fitlibrary.traverse.DomainAdapter;

@SuppressWarnings({ "rawtypes", "unused" })
public class GenericFinderException implements DomainAdapter, DomainFixtured {

    private Pair<Integer, Integer> integerIntegerPair;

    public Pair<Integer, Integer> getIntegerIntegerPair() {
        return integerIntegerPair;
    }

    public void setIntegerIntegerPair(Pair<Integer, Integer> pair) {
        this.integerIntegerPair = pair;
    }

    public Pair findPair(String key, Type type) {
        throw new RuntimeException();
    }

    @Override
    public Object getSystemUnderTest() {
        return null;
    }
}
