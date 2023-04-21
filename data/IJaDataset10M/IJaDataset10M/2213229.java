package org.goet.datamodel.reflect;

import org.goet.datamodel.*;
import org.goet.datamodel.impl.*;
import java.util.*;

/**
 * The class of all NodeClass objects
 */
public class PrimitiveTypeClass extends TypeClass implements ReflectionItem {

    protected static final URI uri = new URI("http://www.goet.org/daml+ext#primitiveTypeClass");

    public static final Property IS_NUMERIC = new PropertyImpl(new URI("http://www.goet.org/daml+ext#isNumeric"), Initializer.dataCollection);

    protected static PrimitiveTypeClass primitiveTypeClass = new PrimitiveTypeClass();

    protected static void init() {
        IS_NUMERIC.addPropertyValue(PropertyClass.DOMAIN, getPrimitiveTypeClass());
        IS_NUMERIC.addPropertyValue(PropertyClass.DOMAIN, BasicTypes.BOOLEAN_TYPE);
    }

    protected static final NodeClass[] superClasses = { TypeClass.getTypeClass() };

    protected static final Property[] properties = { HAS_SUPER_CLASS, IS_NUMERIC, IS_ABSTRACT };

    protected static final Property[] instanceProperties = new Property[0];

    protected static final NodeClass[] instanceClasses = { Thing.getThing() };

    public static PrimitiveTypeClass getPrimitiveTypeClass() {
        if (primitiveTypeClass == null) primitiveTypeClass = new PrimitiveTypeClass();
        return primitiveTypeClass;
    }

    protected PrimitiveTypeClass() {
        Initializer.dataCollection.addNode(this);
    }

    public Node createInstance(String uri, NodeClass[] otherClasses, DataCollection dc) {
        throw new RuntimeException("PrimitiveTypeClass cannot be instantiated");
    }

    public URI getURI() {
        return PrimitiveTypeClass.uri;
    }

    public String toString() {
        return "[class] PrimitiveTypeClass";
    }
}
