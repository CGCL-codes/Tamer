package net.sf.myfacessandbox.backend.database.genericormmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.model.DataModel;

/**
 * @author Werner Punz werpu@gmx.at which works generically on orm mappers
 * 
 * The model uses caching to implement paging without constant ressource
 * allocation Note, with this approach lazy loading for displayed data from
 * within a datatable does not work all the data which is accessed from outside
 * has to be preloaded.
 * 
 * If you need something else there are other ways, for instance you can use
 * PageListeners, or RenderPhaseListeners to get scopes for ressource allocation
 * and freeing
 * 
 */
public class ORMPageCacheDataModel extends DataModel {

    int _cacheWindowSize = 10;

    List _cacheWindow = new ArrayList(_cacheWindowSize);

    Map _idCache = new TreeMap();

    int _cacheWindowPos = 0;

    boolean _cacheInvalidated = true;

    int _rowCount = -1;

    int _rowIndex = -1;

    IDataAccessor _queryDelegate = null;

    public void resetModel() {
        _rowCount = -1;
        _rowIndex = -1;
        invalidateCache();
        refillCache();
    }

    /**
	 * 
	 */
    public ORMPageCacheDataModel(IDataAccessor queryDelegate) {
        super();
        setQueryDelegate(queryDelegate);
    }

    public ORMPageCacheDataModel() {
        super();
    }

    public int getRowCount() {
        if (_rowCount == 0) {
            return -1;
        }
        return _rowCount;
    }

    /**
	 * setter accessed by the query object
	 * 
	 * @param rowCount
	 */
    public void setRowCount(int rowCount) {
        this._rowCount = rowCount;
    }

    boolean isCached() {
        if (_cacheWindow == null || _cacheWindow.size() == 0) return false; else return ((_cacheWindowPos <= getRowIndex()) && (getRowIndex() < (_cacheWindowPos + _cacheWindow.size())));
    }

    /**
	 * refills the cache window upon the given row index this one in the long
	 * term will possible be called from outside also
	 */
    public void refillCache() {
        _cacheWindowPos = Math.max(0, getRowIndex());
        if (_cacheWindowSize > 0) {
            _cacheWindow = _queryDelegate.queryDatasets(_cacheWindowPos, _cacheWindowSize);
            _idCache = new TreeMap();
            Iterator it = _cacheWindow.iterator();
            while (it.hasNext()) {
                BaseDatabaseObject elem = (BaseDatabaseObject) it.next();
                _idCache.put(elem.getId(), elem);
            }
            setRowCount(_queryDelegate.getSize().intValue());
            if (getRowIndex() == -1 && getRowCount() > 0) setRowIndex(0);
            if (getRowIndex() > getRowCount()) {
                setRowIndex(getRowCount() - 1);
            }
        }
        _cacheInvalidated = false;
    }

    public void invalidateCache() {
        _cacheInvalidated = true;
    }

    public Object getRowData() {
        try {
            synchronized (_cacheWindow) {
                if ((!_cacheInvalidated) && isCached()) {
                    return getObjectFromCache();
                }
                refillCache();
                return getObjectFromCache();
            }
        } catch (RuntimeException ex) {
            invalidateCache();
            throw ex;
        }
    }

    /**
	 * @return
	 */
    private Object getObjectFromCache() {
        if (_cacheWindow.size() == 0) return null;
        if ((getRowIndex() - _cacheWindowPos) == _cacheWindow.size()) refillCache();
        return _cacheWindow.get(Math.max(0, Math.min(_cacheWindow.size() - 1, Math.max(0, getRowIndex() - _cacheWindowPos))));
    }

    public int getRowIndex() {
        return _rowIndex;
    }

    public Object getWrappedData() {
        return null;
    }

    public boolean isRowAvailable() {
        return getRowIndex() < getRowCount();
    }

    public void setRowIndex(int arg0) {
        _rowIndex = arg0;
    }

    public void setWrappedData(Object arg0) {
    }

    public IDataAccessor getQueryDelegate() {
        return _queryDelegate;
    }

    /**
	 * query delegate setter
	 * 
	 * @param queryDelegate
	 */
    public void setQueryDelegate(IDataAccessor queryDelegate) {
        _queryDelegate = queryDelegate;
        _queryDelegate.setDataModel(this);
    }

    /**
	 * @param mode
	 */
    public void setOrderMode(String mode) {
        _queryDelegate.setOrderMode(mode);
    }

    /**
	 * returns an object from the page cache just to be sure
	 * 
	 * @param id
	 * @return
	 */
    public Object getPageObjectFromId(Integer id) {
        return _idCache.get(id);
    }

    /**
	 * @return Returns the _cacheWindow.
	 */
    public List get_cacheWindow() {
        return _cacheWindow;
    }

    /**
	 * @param window
	 *            The _cacheWindow to set.
	 */
    public void set_cacheWindow(List window) {
        _cacheWindow = window;
    }

    /**
	 * we can use this one to set the paging size
	 * of the datascroller
	 * 
	 * @return
	 */
    public int getCacheWindowSize() {
        return _cacheWindowSize;
    }

    public void setCacheWindowSize(int windowSize) {
        _cacheWindowSize = windowSize;
    }
}
