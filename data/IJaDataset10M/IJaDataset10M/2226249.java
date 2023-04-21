package daam.ui.gwt.client.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;

public class IDockPanel extends IControl {

    DockPanel dockPanel;

    protected List controls = new Vector();

    protected static Map orientationMap = new HashMap();

    static {
        orientationMap.put("NORTH", DockPanel.NORTH);
        orientationMap.put("SOUTH", DockPanel.SOUTH);
        orientationMap.put("WEST", DockPanel.WEST);
        orientationMap.put("EAST", DockPanel.EAST);
        orientationMap.put("CENTER", DockPanel.CENTER);
        orientationMap.put("LINE_START", DockPanel.LINE_START);
        orientationMap.put("LINE_END", DockPanel.LINE_END);
    }

    protected Widget initWidget() {
        dockPanel = new DockPanel();
        return dockPanel;
    }

    protected void addItem(JSONObject desc) {
        String orientation = ((JSONString) desc.get("position")).stringValue();
        JSONObject controlDesc = (JSONObject) desc.get("content");
        IControl control = builder.buildComponent(containerName, controlDesc);
        controls.add(control);
        dockPanel.add(control.widget, (DockLayoutConstant) orientationMap.get(orientation));
    }

    protected void removeItem(int index) {
        IControl control = (IControl) controls.remove(index);
        dockPanel.remove(index);
        stub.unregisterControl(control.controlId);
    }

    @Override
    public void receiveUpdateEvent(String propertyName, Object newValue) {
        super.receiveUpdateEvent(propertyName, newValue);
        if ("items".equals(propertyName)) {
            clear();
            JSONArray items = (JSONArray) newValue;
            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                addItem(item);
            }
        } else if ("add".equals(propertyName)) {
            addItem((JSONObject) newValue);
        } else if ("remove".equals(propertyName)) {
            removeItem(Integer.parseInt((String) newValue));
        } else if ("spacing".equals(propertyName)) {
            dockPanel.setSpacing(Integer.parseInt((String) newValue));
        } else if ("clear".equals(propertyName)) {
            clear();
        }
    }

    protected void clear() {
        for (int i = 0; i < controls.size(); i++) {
            IControl control = (IControl) controls.get(i);
            stub.unregisterControl(control.controlId);
        }
        controls.clear();
    }
}
