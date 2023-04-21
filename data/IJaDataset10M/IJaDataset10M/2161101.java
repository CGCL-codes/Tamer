package news_rack.database.db4o;

import news_rack.database.NewsIndex;
import java.util.Date;
import java.util.List;
import java.io.OutputStream;

class DB4o_NewsIndex extends NewsIndex {

    String _feedId;

    String _dateString;

    List _news;

    Date _lastUpdateTime;

    public DB4o_NewsIndex(String s, String d) {
        _feedId = s;
        _dateString = d;
        _news = DB4o_DB._db4odb.NewList();
        _lastUpdateTime = null;
    }

    protected void SetLastUpdateTime(Date d) {
        _lastUpdateTime = d;
    }

    public Date GetLastUpdateTime() {
        return _lastUpdateTime;
    }

    public String toString() {
        return _feedId + ":" + _dateString;
    }

    public void ExportToXML(OutputStream o) {
    }
}
