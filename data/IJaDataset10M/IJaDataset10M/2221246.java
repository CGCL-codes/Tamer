package org.dbwiki.web.ui.printer.index;

import java.util.Hashtable;
import java.util.Vector;
import org.dbwiki.data.index.ContentIterator;
import org.dbwiki.data.index.DatabaseEntry;

/** 
 * 	Class providing a list/vector of tagged listings
 * @author jcheney
 *
 */
public class ContentIndex {

    private static final String nonalphabeticContentKey = "&nbsp;&nbsp;&nbsp;OTHER";

    private static final String azIndex = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private Hashtable<String, ContentIndexContainer> _containerIndex;

    private Vector<ContentIndexContainer> _containerList;

    public ContentIndex(ContentIterator iterator) {
        _containerIndex = new Hashtable<String, ContentIndexContainer>();
        _containerList = new Vector<ContentIndexContainer>();
        for (int iChar = 0; iChar < azIndex.length(); iChar++) {
            String key = azIndex.substring(iChar, iChar + 1);
            ContentIndexContainer container = new ContentIndexContainer(key);
            _containerIndex.put(key, container);
            _containerList.add(container);
        }
        ContentIndexContainer container = new ContentIndexContainer(nonalphabeticContentKey);
        _containerIndex.put(nonalphabeticContentKey, container);
        _containerList.add(container);
        DatabaseEntry entry = null;
        while ((entry = iterator.next()) != null) {
            String key = entry.label().substring(0, 1);
            container = _containerIndex.get(key);
            if (container == null) {
                container = _containerList.lastElement();
            }
            container.add(entry);
        }
        if (_containerList.lastElement().size() == 0) {
            _containerIndex.remove(nonalphabeticContentKey);
            _containerList.remove(_containerIndex.size());
        }
    }

    public boolean containsKey(String key) {
        return _containerIndex.containsKey(key);
    }

    public ContentIndexContainer get(int index) {
        return _containerList.get(index);
    }

    public ContentIndexContainer get(String key) {
        return _containerIndex.get(key);
    }

    public int size() {
        return _containerList.size();
    }
}
