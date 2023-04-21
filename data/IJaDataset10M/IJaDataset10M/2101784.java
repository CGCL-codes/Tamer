package com.gcsf.books.search.filters;

import com.gcsf.books.export.IBookDecorator;
import com.gcsf.books.export.IStaticFilterNameProvider;
import com.gcsf.books.search.SearchActivator;
import com.gcsf.books.search.preferences.ISearchPreferencesConstants;

public class ActiveFilterFakeProxy implements IBookDecorator, IStaticFilterNameProvider {

    @Override
    public String getDecoratorImagePath() {
        return StaticFiltersController.getInstance().getActiveFilter().getFilterOverImagePath();
    }

    @Override
    public String getActiveFilterName() {
        return SearchActivator.getDefault().getPreferenceStore().getString(ISearchPreferencesConstants.P_STATIC_FILTER);
    }
}
