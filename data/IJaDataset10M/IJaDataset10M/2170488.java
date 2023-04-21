package Action.lineMode.accessionSearch.relaxer;

import java.util.*;
import org.w3c.dom.*;

/**
 * RStack
 *
 * @since   Mar.  8, 2000
 * @version May. 22, 2003
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public final class RStack {

    private Element element_;

    private Object[] children_;

    private HashSet consumedAttrNodes_ = null;

    private HashSet consumedElementNodes_ = null;

    private HashMap pi_ = new HashMap();

    private int index_;

    protected RStack() {
    }

    public RStack(Element element) {
        element_ = element;
        NodeList nodes = element.getChildNodes();
        List list = new ArrayList();
        _makeList(nodes, list);
        int size = list.size();
        children_ = new Object[size];
        children_ = list.toArray(children_);
        index_ = 0;
    }

    private void _makeList(NodeList nodes, List list) {
        int size = nodes.getLength();
        StringBuffer buffer = null;
        for (int i = 0; i < size; i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                if (buffer != null) {
                    list.add(new String(buffer));
                    buffer = null;
                }
                list.add(node);
            } else if (node instanceof Text) {
                if (buffer == null) {
                    buffer = new StringBuffer();
                }
                buffer.append(((Text) node).getData());
            } else if (node instanceof ProcessingInstruction) {
                ProcessingInstruction pi = (ProcessingInstruction) node;
                pi_.put(pi.getTarget(), pi.getData());
            } else if (node instanceof EntityReference) {
                _makeList(node.getChildNodes(), list);
            }
        }
        if (buffer != null) {
            list.add(new String(buffer));
        }
    }

    public Element getContextElement() {
        return (element_);
    }

    public boolean isEmpty() {
        return (index_ == children_.length);
    }

    public boolean isEmptyElement() {
        if (index_ == children_.length) {
            return (true);
        }
        for (int i = index_; i < children_.length; i++) {
            if (children_[i] instanceof Element) {
                return (false);
            }
        }
        return (true);
    }

    public Object pop() {
        return (children_[index_++]);
    }

    public Object peek() {
        if (index_ == children_.length) {
            return (null);
        }
        return (children_[index_]);
    }

    public Element popElement() {
        if (index_ == children_.length) {
            return (null);
        }
        while (index_ < children_.length) {
            Object node = children_[index_++];
            if (node instanceof Element) {
                return ((Element) node);
            }
        }
        return (null);
    }

    public Element peekElement() {
        if (index_ == children_.length) {
            return (null);
        }
        for (int i = index_; i < children_.length; i++) {
            Object node = children_[i];
            if (node instanceof Element) {
                return ((Element) node);
            }
        }
        return (null);
    }

    public Element[] peekElements() {
        if (index_ == children_.length) {
            return (null);
        }
        List list = new ArrayList();
        for (int i = index_; i < children_.length; i++) {
            Object node = children_[i];
            if (node instanceof Element) {
                list.add(node);
            }
        }
        Element[] elements = new Element[list.size()];
        return ((Element[]) list.toArray(elements));
    }

    public Map getPIMap() {
        return ((Map) pi_.clone());
    }

    public boolean isConsumedElement(Attr attr) {
        if (consumedElementNodes_ == null) {
            return (false);
        }
        return (consumedElementNodes_.contains(attr));
    }

    public void consumeElement(Attr attr) {
        if (consumedElementNodes_ == null) {
            consumedElementNodes_ = new HashSet();
        }
        consumedElementNodes_.add(attr);
    }

    public boolean isConsumedAttribute(Attr attr) {
        if (consumedAttrNodes_ == null) {
            return (false);
        }
        return (consumedAttrNodes_.contains(attr));
    }

    public void consumeAttribute(Attr attr) {
        if (attr == null) {
            return;
        }
        if (consumedAttrNodes_ == null) {
            consumedAttrNodes_ = new HashSet();
        }
        consumedAttrNodes_.add(attr);
    }

    public void addDirectAttributes(Map map) {
        NamedNodeMap attrs = element_.getAttributes();
        int size = attrs.getLength();
        for (int i = 0; i < size; i++) {
            Attr attr = (Attr) attrs.item(i);
            if (!isConsumedAttribute(attr)) {
                map.put(attr.getName(), attr.getValue());
            }
        }
    }

    public RStack makeClone() {
        RStack newStack = new RStack();
        newStack.element_ = element_;
        newStack.children_ = (Object[]) children_.clone();
        if (consumedAttrNodes_ != null) {
            newStack.consumedAttrNodes_ = (HashSet) consumedAttrNodes_.clone();
        }
        if (consumedElementNodes_ != null) {
            newStack.consumedElementNodes_ = (HashSet) consumedElementNodes_.clone();
        }
        newStack.pi_ = (HashMap) pi_.clone();
        newStack.index_ = index_;
        return (newStack);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        if (index_ < children_.length) {
            Object object = children_[index_];
            buffer.append(object);
            for (int i = index_ + 1; i < children_.length; i++) {
                buffer.append(",");
                object = children_[i];
                buffer.append(object);
            }
        }
        buffer.append("]");
        return (new String(buffer));
    }
}
