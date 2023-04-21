package net.sf.beatrix.ui.viewers.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.sf.beatrix.core.xml.XMLStructure;
import net.sf.beatrix.util.xml.XMLNode;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class DCXmlContentProvider implements ITreeContentProvider {

    private static final List<String> PARENT_TAGS = Arrays.asList(new String[] { XMLStructure.DETECTOR_TAG, XMLStructure.MODULES_TAG, XMLStructure.PREFERENCES_TAG });

    public static final List<String> USED_TAGS = Arrays.asList(new String[] { XMLStructure.DETECTOR_TAG, XMLStructure.MODULES_TAG, XMLStructure.ITEM_TAG, XMLStructure.PREFERENCES_TAG, XMLStructure.ITEM_TAG });

    @Override
    public Object[] getElements(Object inputElement) {
        return getChildren(inputElement);
    }

    private boolean isRelevantNode(XMLNode n) {
        return USED_TAGS.contains(n.getName());
    }

    private boolean isRelevantParentNode(XMLNode n) {
        return PARENT_TAGS.contains(n.getName());
    }

    @Override
    public boolean hasChildren(Object element) {
        return element instanceof IDocument || (element instanceof XMLNode && isRelevantParentNode((XMLNode) element));
    }

    private Object[] toRelevantArray(Collection<XMLNode> c) {
        ArrayList<XMLNode> nodes = new ArrayList<XMLNode>(c.size());
        for (Iterator<XMLNode> iter = c.iterator(); iter.hasNext(); ) {
            XMLNode n = iter.next();
            if (isRelevantNode((XMLNode) n)) {
                nodes.add(n);
            }
        }
        return nodes.toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof XMLNode) {
            return toRelevantArray(((XMLNode) parentElement).getChildNodes());
        }
        if (parentElement instanceof Object[]) {
            return (Object[]) parentElement;
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof XMLNode) {
            return ((XMLNode) element).getParent();
        }
        return null;
    }

    @Override
    public void inputChanged(Viewer newViewer, Object oldInput, Object newInput) {
    }

    @Override
    public void dispose() {
    }
}
