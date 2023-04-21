package net.sf.rcpforms.widgetwrapper.advancedsamples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.swt.widgets.Display;

public class Test3Model extends net.sf.rcpforms.common.model.JavaBean {

    private List<Test3RowModel> list;

    private List<Test3RowModel> selected;

    private Test3RowModel selected1;

    public static final String PROP_LIST = "list";

    public static final String PROP_SELECTED = "selected";

    public static final String PROP_SELECTED1 = "selected1";

    public Test3Model() {
    }

    public List<Test3RowModel> getList() {
        return list;
    }

    public void setList(final List<Test3RowModel> list) {
        final Object oldValue = this.list;
        this.list = list;
        propertyChangeSupport.firePropertyChange(PROP_LIST, oldValue, this.list);
    }

    public List<Test3RowModel> getSelected() {
        return selected;
    }

    private Test3RowModel[] selectionHistory = null;

    public void setSelected(final Test3RowModel... selecteds) {
        setSelected(Arrays.asList(selecteds));
    }

    public void setSelected(final List<Test3RowModel> selected) {
        final Object oldValue = this.selected;
        final Object oldOne = getSelected1();
        this.selected = selected;
        final int count = selected == null ? 0 : selected.size();
        propertyChangeSupport.firePropertyChange(PROP_SELECTED, oldValue, this.selected);
        propertyChangeSupport.firePropertyChange(PROP_SELECTED1, oldOne, getSelected1());
        if (selected != null && selected.size() > 4) {
            final List<Test3RowModel> selHist = asList(selectionHistory);
            final List list = copyList(selected);
            list.removeAll(selHist);
            addAllAsSet(selHist, list);
            while (selHist.size() > 4) {
                selHist.remove(0);
            }
            Display.getCurrent().asyncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(70);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    setSelected(selHist);
                }
            });
            selectionHistory = selHist == null ? new Test3RowModel[0] : selHist.toArray(new Test3RowModel[selHist.size()]);
        } else {
            selectionHistory = selected == null ? new Test3RowModel[0] : selected.toArray(new Test3RowModel[selected.size()]);
        }
    }

    public Test3RowModel getSelected1() {
        final List<Test3RowModel> selected2 = getSelected();
        return selected2 != null && selected2.size() == 1 ? selected2.get(0) : null;
    }

    public void setSelected1(final Test3RowModel selection) {
        setSelected(selection);
    }

    private void addAllAsSet(final List list1, final List list2) {
        for (final Object object : list2) {
            if (!list1.contains(object)) {
                list1.add(object);
            }
        }
    }

    public <T> List<T> copyList(final List<T> list) {
        final ArrayList result = new ArrayList(list.size());
        result.addAll(list);
        return result;
    }

    public List asList(final Object[] array) {
        final ArrayList result = new ArrayList(array == null ? 0 : array.length);
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                result.add(array[i]);
            }
        }
        return result;
    }
}
