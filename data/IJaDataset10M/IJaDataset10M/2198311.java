package org.softmed.rest.server.xstream;

import java.util.List;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SimpleListConverter implements Converter {

    @Override
    public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
        boolean nice = true;
        List list = (List) arg0;
        arg1.addAttribute("item-count", "" + list.size());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
        return null;
    }

    @Override
    public boolean canConvert(Class arg0) {
        return List.class.isAssignableFrom(arg0);
    }
}
