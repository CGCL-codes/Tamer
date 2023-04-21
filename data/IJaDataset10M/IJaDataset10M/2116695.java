package skycastle.huntergatherers.model;

import com.gargoylesoftware.base.testing.EqualsTester;
import junit.framework.TestCase;

/**
 * @author Hans H�ggstr�m
 */
public class EntityTest extends TestCase {

    private Entity myEntity;

    public void testEntityIdNotReassignable() throws Exception {
        myEntity.setId(new GameObjectId(1337));
        try {
            myEntity.setId(new GameObjectId(8001));
            fail("It should not be possible to assign an id twice to an entity");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testIdEquality() throws Exception {
        GameObjectId a = new GameObjectId(1003);
        GameObjectId b = new GameObjectId(1003);
        GameObjectId c = new GameObjectId(1123);
        new EqualsTester(a, b, c, null);
    }

    protected void setUp() throws Exception {
        super.setUp();
        myEntity = new EntityImpl();
    }
}
