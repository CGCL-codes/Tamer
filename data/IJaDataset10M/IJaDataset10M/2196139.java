package org.jcvi.vics.shared.tasks;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cristian Goina
 */
public class SearchJobInfo extends JobInfo {

    private Map<String, SearchCategoryInfo> _categories;

    public SearchJobInfo() {
        super();
    }

    public void setCategories(String[] categories) {
        if (categories == null) return;
        _categories = new HashMap<String, SearchCategoryInfo>();
        for (String category : categories) {
            _categories.put(category, new SearchCategoryInfo(category));
        }
    }

    public Map<String, SearchCategoryInfo> getCategories() {
        return _categories;
    }
}
