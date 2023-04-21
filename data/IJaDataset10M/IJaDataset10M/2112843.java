package org.awelements.table.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.awelements.table.Filter;

public class SingleChoiceFilter extends Filter {

    private List<Object> mDistinctValues = new ArrayList();

    private Object mSelectedObject = "";

    public SingleChoiceFilter() {
        this(null, null);
    }

    public SingleChoiceFilter(String name) {
        this(name, null);
    }

    public SingleChoiceFilter(String name, String title) {
        super(name, title);
    }

    public String getDefaultDescription() {
        return "Select " + getColumn().getTitle();
    }

    public void setSelection(String idxString) {
        try {
            mSelectedObject = mDistinctValues.get(Integer.valueOf(idxString));
        } catch (Exception e) {
        }
        if (mSelectedObject == null) mSelectedObject = "";
    }

    public boolean isSelected(String idxString) {
        return mSelectedObject.equals(mDistinctValues.get(Integer.valueOf(idxString)));
    }

    public Object getSelectedObject() {
        return mSelectedObject;
    }

    @Override
    public boolean accept(int rowIndex, Object object) {
        if (object == null) object = "";
        return mSelectedObject == null || mSelectedObject.toString().length() == 0 || object.equals(mSelectedObject);
    }

    @Override
    public FilterParameter[] getParameterDescriptions() {
        final SelectFilterParameter select = new SelectFilterParameter("selection", getDescription());
        select.setSubmitOnChange(true);
        final Set<Object> objs = getDistinctValues();
        objs.add("");
        mDistinctValues = new ArrayList(objs);
        Collections.sort(mDistinctValues, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
        for (int i = 0; i < mDistinctValues.size(); ++i) select.put(String.valueOf(i), mDistinctValues.get(i).toString());
        return new FilterParameter[] { select, new ResetFilterParameter("singleChoiceReset") };
    }

    protected Set<Object> getDistinctValues() {
        final Set<Object> objs = new LinkedHashSet();
        for (Object elt : getTable().getTableStatusProvider().getColumnElements(getColumn().getIndexStack(), getLevel())) {
            if (elt != null) objs.add(elt);
        }
        return objs;
    }

    @Override
    public boolean isColumnFilter() {
        return true;
    }

    @Override
    public void reset() {
        mSelectedObject = "";
    }
}
