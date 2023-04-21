package net.sourceforge.javabits.tree;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import net.sourceforge.javabits.closure.Closure;
import net.sourceforge.javabits.function.Function;
import net.sourceforge.javabits.lang.Objects;

public abstract class AbstractTreeNode<E, T extends AbstractTreeNode<E, T>> implements TreeNode<E, T> {

    private T parent;

    private List<T> childList = new ArrayList<T>();

    private E element;

    public AbstractTreeNode() {
    }

    public AbstractTreeNode(E element) {
        this.element = element;
    }

    public E get() {
        return element;
    }

    public void set(E elem) {
        this.element = elem;
    }

    public Collection<T> getChildNodeCollection() {
        return Collections.unmodifiableCollection(childList);
    }

    public boolean add(E child) {
        return childList.add(createNode(child));
    }

    public boolean remove(E child) {
        boolean result = false;
        for (ListIterator<T> i = childList.listIterator(); i.hasNext(); ) {
            T childNode = i.next();
            if (Objects.equals(child, childNode.get())) {
                result = true;
                i.remove();
                childNode.parent = null;
                break;
            }
        }
        return result;
    }

    protected abstract T createNode(E elem);

    public boolean addAll(Collection<? extends E> childCollection) {
        boolean result = false;
        for (E child : childCollection) {
            if (add(child)) {
                result = true;
            }
        }
        return result;
    }

    public boolean addAllNodes(Collection<? extends T> nodeCollection) {
        return childList.addAll(nodeCollection);
    }

    public Collection<E> getNodeCollection() {
        return new AbstractList<E>() {

            @Override
            public E get(int index) {
                return childList.get(index).get();
            }

            @Override
            public int size() {
                return childList.size();
            }
        };
    }

    public <R, C extends Collection<? super R>> C evaluate(Function<E, R> function, C collection) {
        R result = function.evaluate(element);
        collection.add(result);
        for (T childNode : childList) {
            childNode.evaluate(function, collection);
        }
        return collection;
    }

    @SuppressWarnings("unchecked")
    public void apply(Closure<? super T> closure) {
        closure.evaluate((T) this);
        for (T childNode : childList) {
            childNode.apply(closure);
        }
    }

    public T getParent() {
        return parent;
    }

    @SuppressWarnings("unchecked")
    public void setParent(T parent) {
        if (this.parent != null && this.parent != parent) {
            if (this.parent != null) {
                parent.childList.remove(this);
            }
            this.parent = parent;
            if (parent != null) {
                parent.childList.add((T) this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public boolean addNode(T child) {
        if (child.getParent() != null && child.getParent() != this) {
            throw new IllegalArgumentException("The node already belongs to another parent.");
        }
        childList.add(child);
        child.parent = (T) this;
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean removeNode(T child) {
        boolean changed = false;
        if (child.getParent() == this) {
            childList.remove(child);
            child.parent = null;
            changed = true;
        }
        return changed;
    }

    public boolean isLeaf() {
        return childList.size() == 0;
    }

    public T deepCopy() {
        T root = createNode(this.get());
        for (T child : getChildNodeCollection()) {
            root.addNode(child.deepCopy());
        }
        return root;
    }
}
