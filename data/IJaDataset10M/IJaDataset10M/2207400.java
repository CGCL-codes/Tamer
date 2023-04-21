package ru.ksu.niimm.cll.mocassin.crawl.parser.impl.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import ru.ksu.niimm.cll.mocassin.crawl.parser.impl.NodeImpl;
import ru.ksu.niimm.cll.mocassin.crawl.parser.latex.Node;

public class NodeAdapter extends XmlAdapter<NodeImpl, Node> {

    @Override
    public NodeImpl marshal(Node n) throws Exception {
        return (NodeImpl) n;
    }

    @Override
    public Node unmarshal(NodeImpl n) throws Exception {
        return n;
    }
}
