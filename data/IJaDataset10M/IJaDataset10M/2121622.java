package net.sourceforge.traffiscope.model.xml;

import org.junit.Assert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectorHelper {

    public static final CollectorXO PROD_COLLECTOR = init("prodCollector");

    public static final CollectorXO TEST_COLLECTOR = init("testCollector");

    public static final CollectorXO DEVEL_COLLECTOR = init("develCollector");

    public static void assertEquals(CollectorXO expected, CollectorXO actual) {
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getType(), actual.getType());
        Assert.assertEquals(expected.getInterval(), actual.getInterval());
        Assert.assertEquals(expected.getProperties().size(), actual.getProperties().size());
        Assert.assertEquals(list2map(expected.getProperties()), list2map(actual.getProperties()));
    }

    public static void assertEquals(List<CollectorXO> expecteds, List<CollectorXO> actuals) {
        Assert.assertEquals(expecteds.size(), actuals.size());
        for (CollectorXO x : expecteds) {
            CollectorXO a = null;
            for (CollectorXO s : actuals) {
                if (x.getName().equals(s.getName())) {
                    a = s;
                    break;
                }
            }
            Assert.assertNotNull(a);
            assertEquals(x, a);
        }
    }

    private static CollectorXO init(String name) {
        CollectorXO collector = new CollectorXO();
        collector.setName(name);
        collector.setInterval(10000);
        collector.setType("PROPERTIES");
        List<PropertyXO> props = new ArrayList<PropertyXO>();
        PropertyXO p;
        p = new PropertyXO();
        p.setName("location");
        p.setValue("http://localhost:0/" + name + ".properties");
        props.add(p);
        collector.setProperties(props);
        return collector;
    }

    private static Map<String, String> list2map(List<PropertyXO> props) {
        Map<String, String> ret = new HashMap<String, String>();
        for (PropertyXO p : props) {
            ret.put(p.getName(), p.getValue());
        }
        return ret;
    }
}
