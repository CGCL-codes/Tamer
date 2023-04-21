package ingenias.editor.cell;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import ingenias.editor.entities.*;
import ingenias.editor.entities.*;
import org.swixml.SwingEngine;
import ingenias.editor.entities.Entity;

public class AGOSubordinationRelationshipGroupRenderer extends VertexRenderer implements CellViewRenderer {

    private static ViewPreferences.ViewType current = ViewPreferences.ViewType.INGENIAS;

    static {
        try {
            ViewPreferences.ViewType index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.NOICON;
            RenderComponentManager.loadRenderFile("AGOSubordinationRelationshipGroup", index, "/ingenias/editor/rendererxml/AGOSubordinationRelationshipGroupNOICONPanel.xml");
            index = ViewPreferences.ViewType.INGENIAS;
            RenderComponentManager.loadRenderFile("AGOSubordinationRelationshipGroup", index, "/ingenias/editor/rendererxml/AGOSubordinationRelationshipGroupINGENIASPanel.xml");
            index = ViewPreferences.ViewType.LABEL;
            RenderComponentManager.loadRenderFile("AGOSubordinationRelationshipGroup", index, "/ingenias/editor/rendererxml/AGOSubordinationRelationshipGroupLABELPanel.xml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs a renderer that may be used to render vertices.
     */
    public AGOSubordinationRelationshipGroupRenderer() {
    }

    public Dimension getSize() {
        return RenderComponentManager.getSize("AGOSubordinationRelationshipGroup", current);
    }

    public boolean supportsAttribute(Object key) {
        return true;
    }

    public static void setEntity(AGOSubordinationRelationshipGroup ent) {
        Map currentMap = (Map) RenderComponentManager.retrieveIDs("AGOSubordinationRelationshipGroup", ent.getPrefs().getView());
        if (currentMap.get("Label") != null) {
            if (ent != null && ent.getLabel() != null) {
                if (currentMap.get("Label") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("Label")).setText(ent.getLabel().toString());
                } else {
                    if (currentMap.get("Label") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("Label")).setText(ent.getLabel().toString());
                }
            } else {
                if (currentMap.get("Label") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("Label")).setText(""); else {
                    if (!(currentMap.get("Label") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("Label")).setText("");
                }
            }
        }
    }

    public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
        return RenderComponentManager.retrievePanel("AGOSubordinationRelationshipGroup", this.current);
    }

    public static JPanel setCurrent(ViewPreferences.ViewType c) {
        current = ViewPreferences.ViewType.INGENIAS;
        return (JPanel) RenderComponentManager.retrievePanel("AGOSubordinationRelationshipGroup", c);
    }
}
