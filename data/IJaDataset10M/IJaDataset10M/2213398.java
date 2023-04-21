package com.dfruits.queries.ui.binding.swt;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;
import com.dfruits.dto.ITransferObject;
import com.dfruits.dto.TransferObject;
import com.dfruits.queries.ui.QueryObject;
import com.dfruits.queries.ui.internal.QueryObjectSelection;

class QueryObjectObservableValue extends AbstractObservableValue {

    private ITransferObject data;

    private QueryObject queryObject;

    private Class type = null;

    private String valueField;

    private String valueTypeClass;

    public QueryObjectObservableValue(QueryObject queryObject, String valueField, String valueTypeClass) {
        this.queryObject = queryObject;
        this.valueField = valueField;
        this.valueTypeClass = valueTypeClass;
        try {
            type = Class.forName(valueTypeClass);
        } catch (Exception e) {
        }
    }

    public ITransferObject getData() {
        return data;
    }

    public void setData(ITransferObject newData, boolean notifyListeners) {
        if (queryObject.getProperty(QueryObject.PROP_PREVENT_RECURSION) != null) {
            return;
        }
        Object oldData = this.data;
        this.data = newData;
        if (notifyListeners) {
            final ValueDiff diff = Diffs.createValueDiff(oldData, newData);
            getRealm().asyncExec(new Runnable() {

                public void run() {
                    fireValueChange(diff);
                }
            });
        }
    }

    protected Object doGetValue() {
        Object ret = null;
        if (data != null) {
            ret = data.get(valueField);
        }
        return ret;
    }

    protected void doSetValue(Object value) {
        if (data == null) {
            data = new TransferObject();
        }
        data.set(valueField, value);
        QueryObjectSelection selection = new QueryObjectSelection(data);
        queryObject.setProperty(QueryObject.PROP_PREVENT_RECURSION, true);
        try {
            queryObject.setSelection(selection);
        } finally {
            queryObject.setProperty(QueryObject.PROP_PREVENT_RECURSION, null);
        }
    }

    public Object getValueType() {
        return type;
    }
}
