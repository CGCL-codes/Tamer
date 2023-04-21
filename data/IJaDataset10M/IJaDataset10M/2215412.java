package com.tensegrity.webetlclient.modules.ui.client.contexts;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.tensegrity.palowebviewer.modules.widgets.client.IColumnModel;
import com.tensegrity.palowebviewer.modules.widgets.client.ListView;
import com.tensegrity.webetlclient.modules.model.client.ParameterListModel;

public class ParameterList extends Composite {

    private static final IColumnModel[] COLUMNS = new IColumnModel[] { new NameColumn(), new ValueColumn() };

    private final Panel panel = new FlowPanel();

    private final ParameterListButtonPanel buttonPanel = new ParameterListButtonPanel();

    private final ListView listView = new ListView();

    public ParameterList() {
        panel.add(buttonPanel);
        panel.add(listView);
        initWidget(panel);
        listView.setColumnModel(COLUMNS);
        setStyleName("parameter-list");
    }

    public void setModel(ParameterListModel model) {
        listView.setModel(model);
        buttonPanel.setListModel(model);
    }
}
