package org.ztest.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom.Element;
import org.jdom.Text;
import org.ztest.classinfo.ZIClassEdge;
import org.ztest.classinfo.ZIPath;
import org.ztest.jdom.ZXMLUtil;
import org.ztest.sets.ZIClassSet;

public class ZDependencyFailure implements ZITestFailure {

    private final Map<String, Set<ZIClassSet>> class2ClassSetMap;

    private final ZIPath path;

    public ZDependencyFailure(ZIPath path, Map<String, Set<ZIClassSet>> class2ClassSetMap) {
        super();
        this.class2ClassSetMap = class2ClassSetMap;
        this.path = path;
    }

    public ZIPath getPath() {
        return path;
    }

    public Element toElement() throws Exception {
        Element ret = new Element("dependency-failure");
        for (ZIClassEdge edge : path.getEdges()) {
            ret.addContent(toElement(edge));
        }
        return ret;
    }

    private Element toElement(ZIClassEdge edge) throws Exception {
        Element ret = new Element("edge");
        ret.addContent(toElement(edge.getSource()));
        ret.addContent(toElement(edge.getTarget()));
        ret.addContent(edge.getMetaData().toElement());
        return ret;
    }

    private Element toElement(String name) throws Exception {
        Element ret = new Element("class");
        ret.addContent(new Text(name));
        Set<ZIClassSet> classSet = class2ClassSetMap.get(name);
        if (classSet != null && !classSet.isEmpty()) {
            List<ZIClassSet> classSets = new ArrayList<ZIClassSet>(classSet);
            Collections.sort(classSets);
            StringBuffer sb = new StringBuffer();
            for (ZIClassSet cs : classSets) {
                sb.append(cs.getName());
                sb.append(',');
            }
            String msg = sb.toString();
            if (msg.length() > 0) {
                msg = msg.substring(0, msg.length() - 1);
            }
            ret.setAttribute("belongs-to", msg);
        }
        return ret;
    }

    public String toXML() throws Exception {
        return ZXMLUtil.toXML(toElement());
    }

    public Set<String> getRelatedClasses() {
        Set<String> ret = new HashSet<String>();
        ret.addAll(path.getRelatedClasses());
        return ret;
    }

    public Set<ZIClassEdge> getRelatedEdges() {
        Set<ZIClassEdge> ret = new HashSet<ZIClassEdge>();
        ret.addAll(path.getRelatedEdges());
        return ret;
    }

    public Map<String, Set<ZIClassSet>> getClass2ClassSetMap() {
        return class2ClassSetMap;
    }
}
