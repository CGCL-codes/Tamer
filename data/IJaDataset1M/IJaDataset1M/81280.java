package org.ru.mse10.cvis.web.bean.action.search.datamodel;

import javax.faces.context.FacesContext;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ru.mse10.cvis.dto.BaseDTO;
import org.ru.mse10.cvis.entity.misc.Identity;
import org.ru.mse10.cvis.service.EntityService;
import org.ru.mse10.cvis.service.search.EntitySearcher;

public abstract class JPADataModel<Z extends EntityService, T extends Identity, M extends BaseDTO<T>> extends ExtendedDataModel<T> {

    private Object rowKey;

    @Override
    public void setRowKey(Object key) {
        rowKey = key;
    }

    @Override
    public Object getRowKey() {
        return rowKey;
    }

    public abstract EntitySearcher<T, M> getSearcher();

    public abstract Z getService();

    public abstract M getDto();

    public abstract Class<T> getEntityClass();

    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        SequenceRange sequenceRange = (SequenceRange) range;
        getDto().setFirstResult(sequenceRange.getFirstRow());
        getDto().setMaxResults(sequenceRange.getRows());
        getService().search(getSearcher(), getDto(), false);
        for (T t : getDto().getResult()) {
            visitor.process(context, t.getId(), argument);
        }
    }

    @Override
    public boolean isRowAvailable() {
        return rowKey != null;
    }

    @Override
    public int getRowCount() {
        return getDto().getTotalNumberOfRows();
    }

    @Override
    public T getRowData() {
        return getService().find(getEntityClass(), (Long) getRowKey());
    }

    @Override
    public int getRowIndex() {
        return -1;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }

    protected Object getId(T t) {
        return t.getId();
    }
}
