package com.google.gwt.uibinder.elementparsers;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.uibinder.rebind.FieldWriter;
import com.google.gwt.uibinder.rebind.UiBinderWriter;
import com.google.gwt.uibinder.rebind.XMLElement;
import java.util.HashMap;

/**
 * Parses {@link com.google.gwt.user.client.ui.DockPanel} widgets.
 */
public class DockPanelParser implements ElementParser {

    private static final String TAG_DOCK = "Dock";

    private static final HashMap<String, String> values = new HashMap<String, String>();

    static {
        values.put("NORTH", "com.google.gwt.user.client.ui.DockPanel.NORTH");
        values.put("SOUTH", "com.google.gwt.user.client.ui.DockPanel.SOUTH");
        values.put("EAST", "com.google.gwt.user.client.ui.DockPanel.EAST");
        values.put("WEST", "com.google.gwt.user.client.ui.DockPanel.WEST");
        values.put("CENTER", "com.google.gwt.user.client.ui.DockPanel.CENTER");
        values.put("LINE_START", "com.google.gwt.user.client.ui.DockPanel.LINE_START");
        values.put("LINE_END", "com.google.gwt.user.client.ui.DockPanel.LINE_END");
    }

    public void parse(XMLElement elem, String fieldName, JClassType type, UiBinderWriter writer) throws UnableToCompleteException {
        for (XMLElement child : elem.consumeChildElements()) {
            String ns = child.getNamespaceUri();
            String tagName = child.getLocalName();
            if (!ns.equals(elem.getNamespaceUri())) {
                writer.die(elem, "Invalid DockPanel child namespace: " + ns);
            }
            if (!tagName.equals(TAG_DOCK)) {
                writer.die(elem, "Invalid DockPanel child element: " + tagName);
            }
            if (!child.hasAttribute("direction")) {
                writer.die(elem, "Dock must specify the 'direction' attribute");
            }
            String value = child.consumeRawAttribute("direction");
            String translated = values.get(value);
            if (translated == null) {
                writer.die(elem, "Invalid value: dockDirection='" + value + "'");
            }
            XMLElement widget = child.consumeSingleChildElement();
            FieldWriter childField = writer.parseElementToField(widget);
            writer.addStatement("%1$s.add(%2$s, %3$s);", fieldName, childField.getNextReference(), translated);
            CellPanelParser.parseCellAttributes(child, fieldName, childField, writer);
        }
    }
}
