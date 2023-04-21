package com.otom.bcel.object;

import static org.junit.Assert.*;
import org.junit.Test;
import com.otom.bcel.Mapper;
import com.otom.bcel.testobjects.InnerDestinationObject;
import com.otom.bcel.testobjects.InnerSourceObject;
import com.otom.bcel.testobjects.SecondLevelObject;
import com.otom.bcel.testobjects.TestDestObject;
import com.otom.bcel.testobjects.TestSourceObject;

public class ObjectToObjectMappingTest {

    @Test
    public void testSourceToDestination() {
        TestSourceObject source = new TestSourceObject();
        TestDestObject destination = new TestDestObject();
        TestSourceObject.called = false;
        source.setInnerSourceObject(new InnerSourceObject());
        source.getInnerSourceObject().setInnerString("test");
        source.setName("Ram M");
        Mapper<TestSourceObject, TestDestObject> mapper = new Mapper<TestSourceObject, TestDestObject>();
        mapper.map(source, destination);
        assertNotNull(destination.getInnerDestinationObject());
        assertEquals("test", destination.getInnerDestinationObject().getInnerString());
        assertEquals("Ram M", destination.getInnerDestinationObject().getSecondLevelObject().getName());
        assertTrue(TestSourceObject.called);
    }

    @Test
    public void testDestinationToSource() {
        TestSourceObject source = new TestSourceObject();
        TestDestObject destination = new TestDestObject();
        TestSourceObject.called = false;
        destination.setInnerDestinationObject(new InnerDestinationObject());
        destination.getInnerDestinationObject().setInnerString("test");
        destination.getInnerDestinationObject().setSecondLevelObject(new SecondLevelObject());
        destination.getInnerDestinationObject().getSecondLevelObject().setName("ram m");
        Mapper<TestDestObject, TestSourceObject> mapper = new Mapper<TestDestObject, TestSourceObject>();
        mapper.map(destination, source);
        assertNotNull(source.getInnerSourceObject());
        assertEquals("test", source.getInnerSourceObject().getInnerString());
        assertEquals("ram m", source.getName());
        assertTrue(TestSourceObject.called);
    }
}
