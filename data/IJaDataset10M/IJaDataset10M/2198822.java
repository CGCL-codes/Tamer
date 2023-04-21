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

public class GTFailsRenderer extends VertexRenderer implements CellViewRenderer {

    private static ViewPreferences.ViewType current = ViewPreferences.ViewType.INGENIAS;

    static {
        try {
            ViewPreferences.ViewType index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.NOICON;
            RenderComponentManager.loadRenderFile("GTFails", index, "/ingenias/editor/rendererxml/GTFailsNOICONPanel.xml");
            index = ViewPreferences.ViewType.INGENIAS;
            RenderComponentManager.loadRenderFile("GTFails", index, "/ingenias/editor/rendererxml/GTFailsINGENIASPanel.xml");
            index = ViewPreferences.ViewType.LABEL;
            RenderComponentManager.loadRenderFile("GTFails", index, "/ingenias/editor/rendererxml/GTFailsLABELPanel.xml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs a renderer that may be used to render vertices.
     */
    public GTFailsRenderer() {
    }

    public Dimension getSize() {
        return RenderComponentManager.getSize("GTFails", current);
    }

    public boolean supportsAttribute(Object key) {
        return true;
    }

    public static void setEntity(GTFails ent) {
        Map currentMap = (Map) RenderComponentManager.retrieveIDs("GTFails", ent.getPrefs().getView());
        if (currentMap.get("FailureCondition") != null) {
            if (ent != null && ent.getFailureCondition() != null) {
                if (currentMap.get("FailureCondition") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("FailureCondition")).setText(ent.getFailureCondition().toString());
                } else {
                    if (currentMap.get("FailureCondition") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("FailureCondition")).setText(ent.getFailureCondition().toString());
                }
            } else {
                if (currentMap.get("FailureCondition") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("FailureCondition")).setText(""); else {
                    if (!(currentMap.get("FailureCondition") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("FailureCondition")).setText("");
                }
            }
        }
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
        return RenderComponentManager.retrievePanel("GTFails", this.current);
    }

    public static JPanel setCurrent(ViewPreferences.ViewType c) {
        current = ViewPreferences.ViewType.INGENIAS;
        return (JPanel) RenderComponentManager.retrievePanel("GTFails", c);
    }
}
