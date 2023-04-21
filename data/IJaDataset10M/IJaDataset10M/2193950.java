package org.easyform.xml;

import java.util.ArrayList;
import java.util.List;
import org.easyform.ConfigException;
import org.easyform.Field;
import org.easyform.component.Option;
import org.easyform.core.BOTypeInfo;
import org.easyform.core.CustomSource;
import org.easyform.core.SourceInitException;
import org.easyform.core.StandedSource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author ZhuYanYu
 * @version $Revision: 1.1
 * @since 2009-7-27
 */
public class SourceParser {

    Element source;

    public SourceParser(Element source) {
        this.source = source;
    }

    public void parseSource(BOTypeInfo bo) {
        String clz = source.getAttribute("class");
        if (clz != null && !clz.equals("")) {
            bo.setSourceClass(clz);
        } else {
            bo.setSource(new StandedSource(toList()));
        }
    }

    public void parseSource(Field bo) {
        String clz = source.getAttribute("class");
        if (clz != null && !clz.equals("")) {
            try {
                bo.setSource(new CustomSource(clz));
                return;
            } catch (SourceInitException e) {
                throw new ConfigException(e);
            }
        }
        bo.setSource(new StandedSource(toList()));
    }

    private List toList() {
        List l = new ArrayList();
        NodeList nodes = source.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                l.add(toOption((Element) node));
            }
        }
        return l;
    }

    private Option toOption(Element ele) {
        if (ele.getNodeName().equals("option")) {
            return new Option(ele.getAttribute("value"), ele.getAttribute("text"));
        } else if (ele.getNodeName().equals("optionList")) {
            Option op = new Option(ele.getAttribute("value"), ele.getAttribute("text"));
            NodeList nodes = ele.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node instanceof Element) {
                    op.addSubOption(toOption((Element) node));
                }
            }
            return op;
        } else {
            throw new ConfigException("Element source only contains option or optionList.");
        }
    }
}
