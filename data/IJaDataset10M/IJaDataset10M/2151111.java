package daam.ui.gwt.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import daam.ui.gwt.client.component.IControl;
import daam.ui.gwt.client.component.ILabel;

public class ComponentBuilder {

    public DaamStub stub;

    public ComponentBuilder(DaamStub stub) {
        this.stub = stub;
    }

    public Widget buildComponents(String jsonString) {
        JSONObject screen = (JSONObject) JSONParser.parse(jsonString);
        return buildComponent("root", screen).widget;
    }

    public IControl buildComponent(String containerName, JSONObject component) {
        String elementName = ((JSONString) component.get("element")).stringValue();
        if ("container".equals(elementName)) {
            String newContainerName = ((JSONString) component.get("containerName")).stringValue();
            JSONObject content = (JSONObject) component.get("content");
            return buildComponent(newContainerName, content);
        }
        JSONArray attributes = ((JSONArray) component.get("attributes"));
        IControl result = null;
        int controlId = (int) ((JSONNumber) component.get("control")).doubleValue();
        for (int i = 0; i < ComponentFactory.componentFactories.size(); i++) {
            ComponentFactory componentFactory = (ComponentFactory) ComponentFactory.componentFactories.get(i);
            result = componentFactory.getControl(elementName);
            if (result != null) break;
        }
        if (result == null) {
            result = new ILabel();
            JSONObject text = new JSONObject();
            text.put("name", new JSONString("text"));
            text.put("value", new JSONString("no component registered for: " + elementName));
            attributes = new JSONArray();
            attributes.set(0, text);
        }
        result.init(this, containerName, controlId, attributes);
        stub.registerControl(controlId, result);
        return result;
    }
}
