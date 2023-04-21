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

public class CompromiseRenderer extends VertexRenderer implements CellViewRenderer, Serializable {

    private static ViewPreferences.ViewType current = ViewPreferences.ViewType.INGENIAS;

    static {
        try {
            ViewPreferences.ViewType index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.UML;
            RenderComponentManager.loadRenderFile("Compromise", index, "/ingenias/editor/rendererxml/CompromiseUMLPanel.xml");
            index = ViewPreferences.ViewType.INGENIAS;
            RenderComponentManager.loadRenderFile("Compromise", index, "/ingenias/editor/rendererxml/CompromiseINGENIASPanel.xml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs a renderer that may be used to render vertices.
     */
    public CompromiseRenderer() {
    }

    public Dimension getSize() {
        return RenderComponentManager.getSize("Compromise", current);
    }

    public boolean supportsAttribute(Object key) {
        return true;
    }

    public static void setEntity(Compromise ent) {
        Map currentMap = (Map) RenderComponentManager.retrieveIDs("Compromise", ent.getPrefs().getView());
        if (ent != null && currentMap.get("_attributes_") != null && currentMap.get("_attributes_") instanceof ingenias.editor.rendererxml.AttributesPanel) {
            ((ingenias.editor.rendererxml.AttributesPanel) currentMap.get("_attributes_")).setEntity(ent);
        }
        if (currentMap.get("Id") != null) {
            if (ent != null && ent.getId() != null) {
                if (currentMap.get("Id") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("Id")).setText(ent.getId().toString());
                } else {
                    if (currentMap.get("Id") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("Id")).setText(ent.getId().toString());
                }
            } else {
                if (currentMap.get("Id") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("Id")).setText(""); else {
                    if (!(currentMap.get("Id") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("Id")).setText("");
                }
            }
        }
    }

    public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
        return RenderComponentManager.retrievePanel("Compromise", ((Entity) ((DefaultGraphCell) (view.getCell())).getUserObject()).getPrefs().getView());
    }

    public static JPanel setCurrent(ViewPreferences.ViewType c) {
        current = ViewPreferences.ViewType.INGENIAS;
        return (JPanel) RenderComponentManager.retrievePanel("Compromise", current);
    }
}
