package org.sopera.configuration.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.sopera.configuration.model.ConfigurationTreeNode;
import org.sopware.toolsuite.admintool.config.delegate.ScopePathPartTO;
import org.sopware.toolsuite.admintool.config.delegate.ScopePathTO;

/**
 * Default implementation
 * 
 * @author zubairov
 */
public class ConfigurationTreeNodeImpl implements ConfigurationTreeNode {

    public ScopePathTO scopePath;

    private Collection<ConfigurationTreeNodeImpl> children = new ArrayList<ConfigurationTreeNodeImpl>();

    public ConfigurationTreeNodeImpl(ScopePathTO path) {
        this.scopePath = path;
    }

    /**
	 * {@inheritDoc}
	 */
    public ScopePathTO getScopePath() {
        return scopePath;
    }

    /**
	 * Adds child
	 * 
	 * @param child
	 */
    public void addChild(ConfigurationTreeNodeImpl child) {
        children.add(child);
    }

    /**
	 * {@inheritDoc}
	 */
    public Collection<ConfigurationTreeNode> getLeafs() {
        Collection<ConfigurationTreeNode> leafs = new ArrayList<ConfigurationTreeNode>();
        collectLeafs(leafs);
        return leafs;
    }

    /**
	 * Check leafs
	 * 
	 * @param configurationTreeNodeImpl
	 * @param leafs
	 */
    private void collectLeafs(Collection<ConfigurationTreeNode> leafs) {
        if (children.isEmpty()) {
            leafs.add(this);
        } else {
            for (ConfigurationTreeNodeImpl child : children) {
                child.collectLeafs(leafs);
            }
        }
    }

    @Override
    public String toString() {
        return scopePath.asString();
    }

    /**
	 * {@inheritDoc}
	 */
    public String getId() {
        return scopePath.asString();
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public String[] getPath() {
        ArrayList<ScopePathPartTO> source = new ArrayList<ScopePathPartTO>();
        source.addAll(scopePath.getScopeParts());
        int maxOrder = 0;
        for (ScopePathPartTO scopePathPartTO : source) {
            int order = scopePathPartTO.getType().getOrder();
            if (maxOrder < order) {
                maxOrder = order;
            }
        }
        final int maximumOrder = maxOrder;
        CollectionUtils.filter(source, new Predicate() {

            public boolean evaluate(Object arg0) {
                int order = ((ScopePathPartTO) arg0).getType().getOrder();
                return order < maximumOrder;
            }
        });
        Collections.sort(source, new Comparator<ScopePathPartTO>() {

            public int compare(ScopePathPartTO o1, ScopePathPartTO o2) {
                int order1 = o1.getType().getOrder();
                int order2 = o2.getType().getOrder();
                if (order1 == order2) {
                    return 0;
                }
                return order1 > order2 ? 1 : -1;
            }
        });
        String[] result = new String[source.size()];
        int i = 0;
        for (ScopePathPartTO part : source) {
            result[i] = part.toString();
            i++;
        }
        return result;
    }

    /**
	 * {@inheritDoc}
	 */
    public String getName() {
        return scopePath.getLastScopePart().toString();
    }
}
