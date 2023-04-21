package org.ztest.graph.deprecated;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jdom.Element;
import org.ztest.graph.ZIEdge;
import org.ztest.jdom.ZWriteXML;
import org.ztest.jdom.ZXMLUtil;

public class ZDependencyEdgeMeta<N> implements ZWriteXML {

    private final Set<List<ZIEdge<N, ZWriteXML>>> paths = new HashSet<List<ZIEdge<N, ZWriteXML>>>();

    public Element toElement() throws Exception {
        Element ret = new Element("paths");
        for (List<ZIEdge<N, ZWriteXML>> p : paths) {
            Element pathNode = new Element("path");
            ret.addContent(pathNode);
            for (ZIEdge<N, ZWriteXML> edge : p) {
                pathNode.addContent(edge.toElement());
            }
        }
        return ret;
    }

    public String toXML() throws Exception {
        return ZXMLUtil.toXML(toElement());
    }

    public Set<List<ZIEdge<N, ZWriteXML>>> getPaths() {
        return paths;
    }
}
