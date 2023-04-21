package org.opennms.map.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class MapForm extends Composite {

    private static final String SELECT_A_MAP_TO_OPEN = "Select a map to open...";

    private Grid m_root;

    private String m_mapName = SELECT_A_MAP_TO_OPEN;

    private int m_mapId = 0;

    ClickListenerCollection m_listeners = new ClickListenerCollection();

    private ListBox m_mapNames;

    public MapForm() {
        m_root = new Grid(1, 2);
        m_mapNames = new ListBox();
        m_mapNames.clear();
        m_mapNames.addItem(SELECT_A_MAP_TO_OPEN, "" + m_mapId);
        m_mapNames.addChangeListener(new ChangeListener() {

            public void onChange(Widget w) {
                ListBox listBox = (ListBox) w;
                int index = listBox.getSelectedIndex();
                m_mapName = listBox.getItemText(index);
                m_mapId = Integer.parseInt(listBox.getValue(index));
            }
        });
        Button submitButton = new Button("Open Map");
        submitButton.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                submit();
            }
        });
        m_root.setWidget(0, 0, submitButton);
        m_root.setWidget(0, 1, m_mapNames);
        initWidget(m_root);
    }

    protected void submit() {
        if (m_mapId == 0) {
            Window.alert("You must select a map!");
        } else {
            m_listeners.fireClick(this);
        }
    }

    public void addClickListener(ClickListener listener) {
        m_listeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        m_listeners.remove(listener);
    }

    public MapRef[] getMaps() {
        return new MapRef[] { new MapRef(1, "map1"), new MapRef(2, "map2"), new MapRef(3, "map3") };
    }

    public String getMapName() {
        return m_mapName;
    }

    public void setMapName(String mapName) {
        m_mapName = mapName;
    }

    public int getMapId() {
        return m_mapId;
    }

    public void setMapId(int mapId) {
        m_mapId = mapId;
    }

    public void setMapRefs(MapRef[] mapRefs) {
        m_mapNames.clear();
        m_mapNames.addItem(SELECT_A_MAP_TO_OPEN, "0");
        for (int i = 0; i < mapRefs.length; i++) {
            MapRef ref = mapRefs[i];
            m_mapNames.addItem(ref.getName(), "" + ref.getId());
        }
    }
}
