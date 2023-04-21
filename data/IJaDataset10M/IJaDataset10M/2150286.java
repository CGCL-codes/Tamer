package net.ep.db4o.factories;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import java.util.List;
import net.ep.db4o.activator.GenericProxyActivationTest;
import net.ep.db4o.javassist.testclasses.BrownBag;
import org.testng.annotations.Test;
import com.db4o.ObjectContainer;

@Test
public class EnhancingFactoryTest extends GenericProxyActivationTest {

    private ObjectContainerProvider provider() {
        return new ObjectContainerProvider() {

            public ObjectContainer db() {
                return db;
            }
        };
    }

    public void brownBag() {
        BasicFactories facts = new PersistentFactories(provider());
        BrownBagFactory fac = facts.createFactory(BrownBagFactory.class);
        BrownBag bag = fac.createBrownbag("erik");
        assertNotNull(bag);
        assertEquals(bag.getOwner(), "erik");
    }

    @Test(expectedExceptions = InvalidFactoryException.class)
    public void brownBagException() {
        BasicFactories facts = new PersistentFactories(provider());
        InvalidBrownBagFactory fac = facts.createFactory(InvalidBrownBagFactory.class);
        BrownBag bag = fac.createBrownbag();
    }

    public void brownBag2() {
        BasicFactories facts = new PersistentFactories(provider());
        BrownBagFactory2 fac = facts.createFactory(BrownBagFactory2.class);
        BrownBag bag = fac.createBrownbag("erik", "hello");
        assertNotNull(bag);
        assertEquals(bag.getOwner(), "erik");
        BrownBag bag2 = fac.createBrownbag("erik");
        assertNotNull(bag2);
        assertEquals(bag2.getOwner(), "erik");
        BrownBag ex = new BrownBag("erik", "hello");
        List<BrownBag> bags = db.queryByExample(ex);
        assertEquals(bags.size(), 2);
        BrownBag ex2 = new BrownBag("olina");
        List<BrownBag> bags2 = db.queryByExample(ex2);
        assertEquals(bags2.size(), 0);
    }

    @Override
    protected void saveData(ObjectContainer db, int instances) {
    }
}
