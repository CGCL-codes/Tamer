package dim133.bibliotheca.providers;

import java.util.ArrayList;
import java.util.List;
import dim133.bibliotheca.Bibliotheca;
import dim133.bibliotheca.Util;
import dim133.bibliotheca.interfaces.IItem;

public abstract class BaseItemProvider {

    protected FileItemProvider _parent;

    protected int _itemsperpage = Bibliotheca.ITEMSMAX;

    protected String _title = Util.EMPTYSTR;

    protected IItem[] _items = null;

    int _lastpage = 0;

    protected IItem _goupitem = null;

    protected Boolean _issorted = false;

    protected abstract void Fill();

    Boolean Check() {
        if (_items == null) {
            Fill();
            if (_items == null) return false;
        }
        return true;
    }

    public Boolean isItemsSorted() {
        return _issorted;
    }

    public int getCount() {
        return _items == null ? 0 : _items.length;
    }

    public IItem getGoUpItem() {
        return _goupitem;
    }

    public Iterable<IItem> getItems() {
        List<IItem> out = new ArrayList<IItem>();
        out.clear();
        if (!Check()) return out;
        for (int pos = 0; pos < _items.length; pos++) out.add(_items[pos]);
        return out;
    }

    public Iterable<IItem> getItems(int pagenumber) {
        List<IItem> out = new ArrayList<IItem>();
        out.clear();
        if (!Check()) return out;
        if (_itemsperpage > 1) {
            int start = pagenumber * _itemsperpage;
            int stop = start + _itemsperpage;
            int pos = start;
            while (pos < stop) {
                if (pos >= _items.length) break;
                out.add(_items[pos]);
                pos++;
            }
        }
        return out;
    }

    public int getPagesCount() {
        if (!Check()) return 0;
        int N = (_items.length / _itemsperpage) - 1;
        if ((_items.length % _itemsperpage) != 0) N++;
        return N;
    }

    public String getTitle() {
        return _title == null ? Util.EMPTYSTR : _title;
    }

    public void setLastPage(int page) {
        _lastpage = page;
    }

    public int getLastPage() {
        return _lastpage;
    }
}
