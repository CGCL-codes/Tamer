package de.andreavicentini.mosaicdomain.test3_desire;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.magiclabs.domain.DomainObject;
import org.magiclabs.domain.properties.Property;
import org.magiclabs.mosaic.MosaicSpec;
import org.magiclabs.mosaic.MosaicFactory;
import org.magiclabs.mosaic.Mosaic.Next;
import org.magiclabs.mosaic.Mosaic.Service;
import org.magiclabs.mosaic.MosaicSpec.WireContext;

public class TestDesire extends TestCase {

    public void testdesire() {
        MosaicFactory factory = new MosaicFactory.ServiceAwareFactory();
        MosaicSpec<Map> builder = factory.spec(Map.class).configure().addMixin(HashMap.class).addConcern(IntegerConcern.class).wire();
        Map<String, String> map = builder.instantiate();
        String string = map.get("andrea");
        assertEquals("andrea", string);
    }

    public static class IntegerConcern extends LazyConcern {

        @Override
        protected Object create(Object key) {
            return key.toString();
        }
    }

    public interface Person extends DomainObject {

        Property<String> name();
    }

    public abstract static class LazyConcern implements InvocationHandler {

        @Next
        InvocationHandler next;

        @Next
        Map composite;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = this.next.invoke(proxy, method, args);
            if (method.getName().equals("get") && result == null) {
                result = create(args[0]);
                this.composite.put(args[0], result);
            }
            return result;
        }

        protected abstract Object create(Object key);
    }
}
