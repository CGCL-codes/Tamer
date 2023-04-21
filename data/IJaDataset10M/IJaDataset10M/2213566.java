package org.simplextensions.ui.menu;

import org.simplextensions.graph.Graph;
import org.simplextensions.graph.NodeAlreadyExistsException;
import org.simplextensions.registry.Extension;
import org.simplextensions.registry.PropertyValue;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import static org.simplextensions.ui.menu.MenuExtensionPoint.*;

/**
 * 
 * 
 * @author Tomasz Krzyzak, <a
 *         href="mailto:tomasz.krzyzak@gmail.com">tomasz.krzyzak@gmail.com</a>
 * @since 2010-05-10 09:57:48
 */
public class MenuExtensionMetadata {

    Extension extension;

    private final Graph graph;

    MenuExtensionMetadata(Graph graph, Extension extension) {
        this.graph = graph;
        this.extension = extension;
        try {
            if (getType() == MenuExtensionType.Action) graph.addNode("/action/" + getId(), getParent() != null ? new String[] { "/menu/" + getParent() } : new String[] {}, this); else if (getType() == MenuExtensionType.Menu) {
                graph.addNode("/menu/" + getId(), getParent() != null ? new String[] { "/menu/" + getParent() } : new String[] {}, this);
            } else if (getType() == MenuExtensionType.Separator) {
                graph.addNode("/separator/" + getId(), getParent() != null ? new String[] { "/menu/" + getParent() } : new String[] {}, this);
            }
        } catch (NodeAlreadyExistsException e1) {
        }
    }

    public String getParent() {
        PropertyValue pv = extension.getPropertyValue(PARENT);
        return pv != null ? pv.getStringValue() : null;
    }

    public String getId() {
        PropertyValue pv = extension.getPropertyValue(PARENT);
        return (pv != null && !"".equals(pv.getStringValue()) ? pv.getStringValue() + "/" : "") + extension.getId();
    }

    public IMenuExtension getExecutable() {
        return (IMenuExtension) extension.getExecutable();
    }

    public MenuExtensionType getType() {
        return MenuExtensionType.values()[extension.getPropertyValue(TYPE).getIntegerValue()];
    }

    public Integer getOrder() {
        return extension.getPropertyValue(ORDER).getIntegerValue();
    }

    public List<MenuExtensionMetadata> getSubItems() {
        List<MenuExtensionMetadata> result = new LinkedList<MenuExtensionMetadata>();
        for (Object o : graph.getIncomingNodes(this)) result.add((MenuExtensionMetadata) o);
        Collections.sort(result, new Comparator<MenuExtensionMetadata>() {

            public int compare(MenuExtensionMetadata o1, MenuExtensionMetadata o2) {
                int compareTo = o1.getOrder().compareTo(o2.getOrder());
                if (compareTo == 0) compareTo = o1.getId().compareTo(o2.getId());
                return compareTo;
            }
        });
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MenuExtensionMetadata[");
        sb.append("parent: ").append(getParent());
        sb.append(", id: ").append(this.extension.getId());
        sb.append(", type: ").append(getType()).append(" ]");
        return sb.toString();
    }
}
