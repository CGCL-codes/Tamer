package com.vividsolutions.xdo.strategies.datatypes;

import java.util.Map;
import javax.naming.OperationNotSupportedException;
import org.xml.sax.Attributes;
import com.vividsolutions.xdo.Encoder;
import com.vividsolutions.xdo.Node;
import com.vividsolutions.xdo.Strategy;
import com.vividsolutions.xdo.schemas.XSDSchema;
import com.vividsolutions.xdo.xsi.ComplexType;
import com.vividsolutions.xdo.xsi.Element;
import com.vividsolutions.xdo.xsi.SimpleType;
import com.vividsolutions.xdo.xsi.Type;

public class XS_longStrategy extends Strategy {

    public XS_longStrategy() {
        super("long", "long", XSDSchema.getInstance().getTargetNamespace(), Long.class);
    }

    public boolean canDecode(Element element, Map hints) {
        if (element == null) return false;
        Type type = element.getType();
        while (type != null) {
            if (id != null && id.equals(type.getId())) return true;
            if (namespace.equals(type.getNamespace()) && (XSDSchema.getInstance().getSimpleTypes()[(XSDSchema.SIMPLE_TYPE_LONG)].getName().equals(type.getName()) || XSDSchema.getInstance().getSimpleTypes()[(XSDSchema.SIMPLE_TYPE_UNSIGNEDLONG)].getName().equals(type.getName()))) return true;
            if (type instanceof SimpleType) type = ((SimpleType) type).getParents()[0]; else type = ((ComplexType) type).getParent();
        }
        return false;
    }

    public Object decode(Element element, Node[] children, Attributes attrs, Map hints) throws OperationNotSupportedException {
        if (!canDecode(element, hints)) throw new OperationNotSupportedException();
        if (children != null && children.length > 0 && children[0] != null) {
            return new Long((String) children[0].value);
        }
        return null;
    }

    public boolean cache(Element element, Map hints) {
        return true;
    }

    public boolean canEncode(Element element, Map hints) {
        return false;
    }

    public void encode(Node value, Encoder output, Map hints) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
