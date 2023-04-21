package eu.planets_project.pp.plato.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.richfaces.component.UITree;

@Scope(ScopeType.SESSION)
@Name("sensitivityTreeHelper")
public class SensitivityTreeHelper implements Serializable {

    private static final long serialVersionUID = 686300796774311162L;

    @In(required = false)
    ResultNode sensitivityNode;

    private static final Logger log = Logger.getLogger(SensitivityTreeHelper.class);

    private Set<Object> expandedNodes = new HashSet<Object>();

    public Boolean adviseNodeOpened(UITree tree) {
        if (expandedNodes.contains(tree.getRowData())) {
            return true;
        }
        return null;
    }

    public void closeNode(Object node) {
        expandedNodes.remove(node);
    }

    public boolean expandNode(Object node) {
        return expandedNodes.add(node);
    }

    public void resetAllNodes() {
        expandedNodes.clear();
    }

    public void collapseAll() {
        expandedNodes.clear();
    }

    public void expandAll() {
        resetAllNodes();
        expand(sensitivityNode);
    }

    private void expand(ResultNode n) {
        expandedNodes.add(n);
        for (ResultNode node : n.getChildren()) {
            expand(node);
        }
    }
}
