package com.weespers.ui.search;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import com.weespers.model.MoreSearchResults;
import com.weespers.model.Search;

public class SearchContentProvider implements IStructuredContentProvider {

    protected Search youTubeSearch;

    @Override
    public Object[] getElements(Object parent) {
        if (youTubeSearch.hasMore()) {
            List<Object> allContents = new ArrayList<Object>();
            allContents.addAll(youTubeSearch.getResults());
            allContents.add(new MoreSearchResults());
            return allContents.toArray();
        } else {
            return youTubeSearch.getResults().toArray();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        youTubeSearch = (Search) newInput;
    }
}
