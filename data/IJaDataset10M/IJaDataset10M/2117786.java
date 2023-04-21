package modelcrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Niall
 *
 */
public class BigIndex {

    private Map<String, List<String>> _keywordToUrls = null;

    private static BigIndex _index = null;

    public static BigIndex getIndex() {
        if (_index == null) {
            _index = new BigIndex();
        }
        return _index;
    }

    /**
	 * 
	 */
    public BigIndex() {
        _keywordToUrls = new HashMap<String, List<String>>();
    }

    public synchronized boolean addToIndex(String index, String url) {
        List<String> urls = null;
        String[] keywords = index.replaceAll(" ", ",").split(",");
        for (String key : keywords) {
            String kw = key.toLowerCase();
            if (!kw.trim().isEmpty()) {
                if (_keywordToUrls.containsKey(kw.trim())) {
                    urls = _keywordToUrls.get(kw.trim());
                    if (!urls.contains(url.trim())) {
                        urls.add(url.trim());
                    }
                } else {
                    urls = new ArrayList<String>();
                    urls.add(url.trim());
                    _keywordToUrls.put(kw.trim(), urls);
                }
            }
        }
        return true;
    }

    public List<String> search(String keywords) {
        List<String> results = new LinkedList<String>();
        String[] kws = keywords.replaceAll(",", " ").split(" ");
        for (String kw : kws) {
            String key = kw.toLowerCase().trim();
            if (!key.trim().isEmpty()) {
                List<String> res = _keywordToUrls.get(key.trim());
                if (res != null) {
                    Iterator<String> t = res.iterator();
                    while (t.hasNext()) {
                        String s = t.next().trim();
                        if (!results.contains(s)) {
                            results.add(s);
                        }
                    }
                }
            }
        }
        return results;
    }

    public int getCount() {
        return _keywordToUrls.size();
    }

    public Set<String> getKeyWords() {
        return _keywordToUrls.keySet();
    }
}
