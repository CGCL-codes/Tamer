package hudson.zipscript.parser.template.element.lang.variable.adapter;

import java.util.List;

public class ListAdapter implements SequenceAdapter {

    public static ListAdapter INSTANCE = new ListAdapter();

    public boolean appliesTo(Object object) {
        return (object instanceof List);
    }

    public int getSize(Object object) {
        return ((List) object).size();
    }

    public Object getItemAt(int index, Object sequence, RetrievalContext retrievalContext, String contextHint) {
        return ((List) sequence).get(index);
    }

    public boolean contains(Object object, Object sequence) {
        return ((List) sequence).contains(object);
    }

    public void setItemAt(int index, Object value, Object sequence) {
        ((List) sequence).set(index, value);
    }

    public void addItemAt(int index, Object value, Object sequence) {
        ((List) sequence).add(index, value);
    }

    public boolean hasNext(int index, Object previousItem, Object sequence) {
        return ((List) sequence).size() > (index + 1);
    }

    public int indexOf(Object object, Object sequence) throws ClassCastException {
        return ((List) sequence).indexOf(object);
    }

    public int lastIndexOf(Object object, Object sequence) throws ClassCastException {
        return ((List) sequence).lastIndexOf(object);
    }

    public Object nextItem(int index, Object lastVal, Object sequence) throws ClassCastException {
        return ((List) sequence).get(index);
    }
}
