package org.jdna.bmt.web.client.ui.widgets;

import com.google.gwt.user.client.ui.Widget;

public interface ListAdapter {

    public int getCount();

    public Object getItem(int pos);

    public Widget getView(int pos);

    public boolean isEmpty();

    public void setListView(ListView listView);
}
