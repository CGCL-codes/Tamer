package net.sf.serialex.transform;

import net.sf.serialex.marshal.MarshalException;
import net.sf.serialex.unmarshal.UnmarshalException;
import org.jdom.DataConversionException;
import org.jdom.Element;
import java.lang.reflect.InvocationTargetException;

public interface Transformer {

    void marshal(Object baseObject, Element baseElement) throws MarshalException;

    Object unmarshal(Element baseElement) throws DataConversionException, UnmarshalException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
