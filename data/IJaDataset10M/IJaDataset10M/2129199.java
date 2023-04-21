package org.poset.server.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;
import org.poset.model.Node;
import org.poset.model.Pair;
import org.poset.model.mapper.MapperClient;
import org.poset.model.mapper.MapperProvider;
import org.poset.model.mapper.NodeMapper;
import org.poset.server.parser.antlr.QueryTreeP3;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable(autowire = Autowire.BY_TYPE)
public class QueryObject implements MapperClient {

    private String queryString;

    private CommonTree root;

    private CommonTree current;

    private Map<Integer, List<String>> kinds = new HashMap<Integer, List<String>>();

    private List<Pair> pairs = null;

    private List<Node> nodes = new ArrayList<Node>();

    private boolean suggestionsNeeded = false;

    private List<String> uidShorts = new ArrayList<String>();

    private List<String> strings = null;

    private NodeMapper nodeMapper;

    public QueryObject(List<String> strings) {
        this.strings = strings;
    }

    public QueryObject(Node node) {
        uidShorts.add(node.getUIDShort());
    }

    public QueryObject(Object root, String queryString) {
        this.root = this.current = (CommonTree) root;
        this.queryString = queryString;
    }

    public List<String> getChildrenByType(int type) {
        if (root == null) return new ArrayList<String>();
        List<String> list = kinds.get(type);
        if (list != null) return list;
        list = new ArrayList<String>();
        BaseTree subTree = null;
        if (current.getType() == type) {
            subTree = current;
        } else {
            for (Object c : current.getChildren()) {
                BaseTree child = (BaseTree) c;
                if (child.getType() == type) {
                    subTree = child;
                    break;
                }
            }
        }
        if (subTree == null) {
            kinds.put(type, list);
            return list;
        }
        for (Object c : subTree.getChildren()) {
            list.add(((BaseTree) c).getText());
        }
        return list;
    }

    public List<Node> getNodes() {
        if (pairs == null) getPairs();
        return nodes;
    }

    public String getQueryString() {
        return queryString;
    }

    public CommonTree getRoot() {
        return root;
    }

    public List<String> getStrings() {
        if (strings != null) return strings;
        return strings = getChildrenByType(QueryTreeP3.STRINGS);
    }

    public List<String> getTypes() {
        return getChildrenByType(QueryTreeP3.TYPES);
    }

    public List<String> getUIDShorts() {
        return uidShorts;
    }

    public List<String> getWords() {
        return getChildrenByType(QueryTreeP3.WORDS);
    }

    public List<Pair> getPairs() {
        if (pairs != null) return pairs;
        pairs = new ArrayList<Pair>();
        if (root == null) return pairs;
        BaseTree subTree = null;
        if (current.getType() == QueryTreeP3.PAIRS) {
            subTree = current;
        } else {
            for (Object c : current.getChildren()) {
                BaseTree child = (BaseTree) c;
                if (child.getType() == QueryTreeP3.PAIRS) {
                    subTree = child;
                    break;
                }
            }
        }
        if (subTree == null) return pairs;
        for (Object c : subTree.getChildren()) {
            BaseTree pairTree = (BaseTree) c;
            BaseTree nodeTree = (BaseTree) pairTree.getChild(0);
            String nodeKeyword = nodeTree.getChild(0).getText();
            String nodeUid = nodeTree.getChild(1).getText();
            Node node = nodeMapper.findNodeByUidShort(nodeUid);
            assert node != null;
            nodes.add(node);
            String groupKeyword = null;
            String groupUid = null;
            Node group = null;
            if (pairTree.getChildCount() > 1) {
                BaseTree groupTree = (BaseTree) pairTree.getChild(1);
                groupKeyword = groupTree.getChild(0).getText();
                groupUid = groupTree.getChild(1).getText();
                group = nodeMapper.findNodeByUidShort(groupUid);
                nodes.add(group);
            }
            Pair p = new Pair(nodeKeyword, node, groupKeyword, group);
            pairs.add(p);
        }
        return pairs;
    }

    public boolean suggestionsNeeded() {
        return suggestionsNeeded;
    }

    public void setSuggestionsNeeded(boolean suggestionsNeeded) {
        this.suggestionsNeeded = suggestionsNeeded;
    }

    public void setMapperProvider(MapperProvider mappers) {
        this.nodeMapper = (NodeMapper) mappers.getMapper(Node.class);
    }
}
