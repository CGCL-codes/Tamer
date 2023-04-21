package org.brandao.brutos.xml.parser.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.brandao.brutos.mapping.MappingException;
import org.brandao.brutos.xml.parser.Stack;
import org.brandao.brutos.xml.parser.Tag;
import org.xml.sax.Attributes;

/**
 *
 * @author Afonso Brandao
 */
public class PropsTag implements Tag {

    private Stack stack;

    public void setStack(Stack stack) {
        this.stack = stack;
    }

    public void setText(String txt) {
    }

    public boolean isRead() {
        return false;
    }

    public void start(String localName, Attributes atts) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("complex-type", "properties");
        stack.push(props);
    }

    public void end(String localName) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> dta = (Map) stack.pop();
        while (dta.get("complex-type") == null) {
            data.add(dta);
            dta = (Map) stack.pop();
        }
        dta.put("data", data);
        stack.push(dta);
    }
}
