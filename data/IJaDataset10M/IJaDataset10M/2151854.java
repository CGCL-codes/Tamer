package net.sf.yaxdiff.tree;

import net.sf.yaxdiff.util.HashCanonicalizer;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ramon Nogueira (ramon döt nogueira at g maíl döt cöm)
 * 
 */
public class CanonicalizingNodeFactory implements INodeFactory {

    private static final int DEFAULT_NUMBER_ELEMENTS = 4096;

    private final HashCanonicalizer<ElementNode> elements = new HashCanonicalizer<ElementNode>(DEFAULT_NUMBER_ELEMENTS);

    private final HashCanonicalizer<AttributeNode> attributes = new HashCanonicalizer<AttributeNode>(DEFAULT_NUMBER_ELEMENTS);

    private final HashCanonicalizer<CDataNode> cdatas = new HashCanonicalizer<CDataNode>(DEFAULT_NUMBER_ELEMENTS * 2);

    public Node getPIData(String name, String value) {
        throw new UnsupportedOperationException();
    }

    public AttributeNode createAttribute(char[] uri, char[] name, char[] value) {
        AttributeNode query = new AttributeNode(uri, name, value);
        return intern(attributes, query);
    }

    public CDataNode createCData(char[] ch, int start, int len) {
        char[] text = Arrays.copyOfRange(ch, start, start + len);
        CDataNode query = new CDataNode(text);
        return intern(cdatas, query);
    }

    public ElementNode createElement(char[] uri, char[] name, List<? extends Node> childNodes) {
        Node[] nodes = childNodes.toArray(new Node[childNodes.size()]);
        ElementNode query = new ElementNode(uri, name, nodes);
        return intern(elements, query);
    }

    private <T> T intern(HashCanonicalizer<T> map, T query) {
        synchronized (map) {
            return map.put(query);
        }
    }
}
