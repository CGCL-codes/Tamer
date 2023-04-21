package phex.api;

public class DefaultSearchHandle implements ISearchHandle {

    private IPhexDriver _phexDriver = null;

    private long _id = -1;

    DefaultSearchHandle(long id) {
        _id = id;
        _phexDriver = DefaultPhexDriver.getInstance();
    }

    public String getSearchString() {
        if (_phexDriver != null) {
            return _phexDriver.getSearchString(_id);
        } else {
            return null;
        }
    }

    public short getSearchStatus() {
        if (_phexDriver != null) {
            return _phexDriver.getSearchStatus(_id);
        } else {
            return ISearchHandle.STATUS_UNDEFINED;
        }
    }

    public boolean stopSearch() {
        if (_phexDriver != null) {
            return _phexDriver.stopSearch(_id);
        } else {
            return false;
        }
    }

    public boolean resumeSearch() {
        if (_phexDriver != null) {
            return _phexDriver.resumeSearch(_id);
        } else {
            return false;
        }
    }

    public ISearchHitHandle[] getSearchHits() {
        if (_phexDriver != null) {
            return convertSearchHitHandles(_phexDriver.getSearchHits(_id));
        } else {
            return null;
        }
    }

    private ISearchHitHandle[] convertSearchHitHandles(long[] ids) {
        if (ids == null) {
            return null;
        }
        final int size = ids.length;
        ISearchHitHandle[] result = new ISearchHitHandle[size];
        for (int index = 0; index < size; index++) {
            result[index] = new DefaultSearchHitHandle(ids[index]);
        }
        return result;
    }

    public boolean deleteSearch() {
        if (_phexDriver != null) {
            return _phexDriver.deleteSearch(_id);
        } else {
            return false;
        }
    }
}
