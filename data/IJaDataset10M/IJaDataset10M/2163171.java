package fr.univartois.cril.xtext.alloyplugin.editor;

import java.util.List;
import org.eclipse.swt.graphics.Image;
import fr.univartois.cril.xtext.alloyplugin.api.IALSTreeDecorated;
import fr.univartois.cril.xtext.alloyplugin.api.Iconable;
import fr.univartois.cril.xtext.alloyplugin.api.NodeWithChildren;

class RootContent<T extends IALSTreeDecorated> implements Iconable, NodeWithChildren {

    private final String label;

    private final Image icon;

    private List<T> children;

    RootContent(String label, Image icon) {
        this.label = label;
        this.icon = icon;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public boolean hasChildren() {
        if (children == null) return false;
        return !children.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public IALSTreeDecorated[] children() {
        if (children == null) {
            return (T[]) new IALSTreeDecorated[0];
        }
        T[] tab = (T[]) new IALSTreeDecorated[children.size()];
        return children.toArray(tab);
    }

    public <V> V[] children(V[] t) {
        return children.toArray(t);
    }

    public Image getIcon() {
        return icon;
    }

    public int size() {
        return children.size();
    }

    @Override
    public String toString() {
        return label;
    }
}
