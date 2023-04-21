package com.ao.model.worldobject;

import static org.junit.Assert.*;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ao.model.character.Character;
import com.ao.model.worldobject.properties.DefensiveItemProperties;

public class HelmetTest extends AbstractDefensiveItemTest {

    private static final int MIN_DEF = 1;

    private static final int MAX_DEF = 5;

    private static final int MIN_MAGIC_DEF = 10;

    private static final int MAX_MAGIC_DEF = 50;

    private Helmet helmet1;

    private Helmet helmet2;

    @Before
    public void setUp() throws Exception {
        DefensiveItemProperties props1 = new DefensiveItemProperties(WorldObjectType.HELMET, 1, "Viking Helmet", 1, 1, 0, null, null, false, false, false, false, 1, MIN_DEF, MAX_DEF, MIN_MAGIC_DEF, MAX_MAGIC_DEF);
        helmet1 = new Helmet(props1, 5);
        DefensiveItemProperties props2 = new DefensiveItemProperties(WorldObjectType.HELMET, 1, "Viking Helmet", 1, 1, 0, null, null, false, false, false, false, 1, MAX_DEF, MAX_DEF, MAX_MAGIC_DEF, MAX_MAGIC_DEF);
        helmet2 = new Helmet(props2, 1);
        object = helmet1;
        ammount = 5;
        objectProps = props1;
        itemEquipped = false;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testClone() {
        Helmet clone = (Helmet) helmet1.clone();
        assertEquals(helmet1.amount, clone.amount);
        assertEquals(helmet1.properties, clone.properties);
        assertFalse(helmet1 == clone);
        clone = (Helmet) helmet2.clone();
        assertEquals(helmet2.amount, clone.amount);
        assertEquals(helmet2.properties, clone.properties);
        assertFalse(helmet2 == clone);
    }

    @Test
    public void testUse() {
        Character character = EasyMock.createMock(Character.class);
        EasyMock.replay(character);
        helmet1.use(character);
        helmet2.use(character);
        EasyMock.verify(character);
    }
}
