package br.com.mcampos.controller.admin.users.model;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.ListModelList;

public abstract class AbstractPagingListModel<T> extends ListModelList {

    /**
     *
     */
    protected static final long serialVersionUID = 6613208067174831719L;

    protected int _startPageNumber;

    protected int _pageSize;

    protected int _itemStartNumber;

    protected transient List<T> _items = new ArrayList<T>();

    public abstract int getTotalSize();

    public abstract List<T> getPageData(int itemStartNumber, int pageSize);

    public AbstractPagingListModel(int startPageNumber, int pageSize) {
        loadPage(startPageNumber, pageSize);
    }

    public void loadPage(int startPageNumber, int pageSize) {
        this._startPageNumber = startPageNumber;
        this._pageSize = pageSize;
        this._itemStartNumber = startPageNumber * pageSize;
        _items = getPageData(_itemStartNumber, _pageSize);
        clear();
        addAll(_items);
    }

    @Override
    public Object getElementAt(int index) {
        try {
            return _items.get(index);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getSize() {
        return _items.size();
    }

    public int getStartPageNumber() {
        return this._startPageNumber;
    }

    public int getPageSize() {
        return this._pageSize;
    }

    public int getItemStartNumber() {
        return _itemStartNumber;
    }
}
