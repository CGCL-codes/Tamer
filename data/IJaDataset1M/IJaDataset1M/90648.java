package com.tensegrity.palowebviewer.modules.engine.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tensegrity.palowebviewer.modules.paloclient.client.XDimension;
import com.tensegrity.palowebviewer.modules.paloclient.client.XSubset;

public class DimensionSubsetCache {

    private final Map map = new HashMap();

    public void clear() {
        map.clear();
    }

    public XSubset[] getSubsets(XDimension dim, XSubset[] subsets) {
        if (dim == null) throw new IllegalArgumentException("Dimension can not be null.");
        List list = getSubsetList(dim);
        XSubset[] result = new XSubset[subsets.length];
        for (int i = 0; i < subsets.length; i++) {
            result[i] = getSubset(dim, subsets[i], list);
        }
        return result;
    }

    public XSubset getSubset(XDimension dim, XSubset subset) {
        if (dim == null) throw new IllegalArgumentException("Dimension can not be null.");
        List list = getSubsetList(dim);
        XSubset result = getSubset(dim, subset, list);
        return result;
    }

    protected XSubset getSubset(XDimension dim, XSubset subset, List list) {
        int index = list.indexOf(subset);
        XSubset result = subset;
        if (index >= 0) {
            result = (XSubset) list.get(index);
        } else {
            list.add(subset);
            subset.setParent(dim);
        }
        return result;
    }

    protected List getSubsetList(XDimension dim) {
        ObjectKey key = new ObjectKey(dim);
        List result = (List) map.get(key);
        if (result == null) {
            result = new ArrayList();
            map.put(key, result);
        }
        return result;
    }
}
