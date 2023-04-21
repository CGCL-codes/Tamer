package com.wrupple.muba.catalogs.client.view;

import java.util.List;
import com.google.gwt.core.client.JavaScriptObject;
import com.wrupple.muba.common.shared.StateTransition;

public interface BrowseView<T extends JavaScriptObject> extends DataView<T> {

    public FilterToolbar getFilterView();

    public void addToSelection(T entry);

    public void startSelection(List<T> initialValues, StateTransition<List<T>> onDone);

    public boolean isSelecting();
}
