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

public class StateGoalRenderer extends VertexRenderer implements CellViewRenderer, Serializable {

    private static ViewPreferences.ViewType current = ViewPreferences.ViewType.INGENIAS;

    static {
        try {
            ViewPreferences.ViewType index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.UML;
            RenderComponentManager.loadRenderFile("StateGoal", index, "/ingenias/editor/rendererxml/StateGoalUMLPanel.xml");
            index = ViewPreferences.ViewType.INGENIAS;
            RenderComponentManager.loadRenderFile("StateGoal", index, "/ingenias/editor/rendererxml/StateGoalINGENIASPanel.xml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs a renderer that may be used to render vertices.
     */
    public StateGoalRenderer() {
    }

    public Dimension getSize() {
        return RenderComponentManager.getSize("StateGoal", current);
    }

    public boolean supportsAttribute(Object key) {
        return true;
    }

    public static void setEntity(StateGoal ent) {
        Map currentMap = (Map) RenderComponentManager.retrieveIDs("StateGoal", ent.getPrefs().getView());
        if (ent != null && currentMap.get("_attributes_") != null && currentMap.get("_attributes_") instanceof ingenias.editor.rendererxml.AttributesPanel) {
            ((ingenias.editor.rendererxml.AttributesPanel) currentMap.get("_attributes_")).setEntity(ent);
        }
        if (currentMap.get("State") != null) {
            if (ent != null && ent.getState() != null) {
                if (currentMap.get("State") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("State")).setText(ent.getState().toString());
                } else {
                    if (currentMap.get("State") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("State")).setText(ent.getState().toString());
                }
            } else {
                if (currentMap.get("State") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("State")).setText(""); else {
                    if (!(currentMap.get("State") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("State")).setText("");
                }
            }
        }
        if (currentMap.get("LinkedGoal") != null) {
            if (ent != null && ent.getLinkedGoal() != null) {
                if (currentMap.get("LinkedGoal") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("LinkedGoal")).setText(ent.getLinkedGoal().toString());
                } else {
                    if (currentMap.get("LinkedGoal") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("LinkedGoal")).setText(ent.getLinkedGoal().toString());
                }
            } else {
                if (currentMap.get("LinkedGoal") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("LinkedGoal")).setText(""); else {
                    if (!(currentMap.get("LinkedGoal") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("LinkedGoal")).setText("");
                }
            }
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
        return RenderComponentManager.retrievePanel("StateGoal", ((Entity) ((DefaultGraphCell) (view.getCell())).getUserObject()).getPrefs().getView());
    }

    public static JPanel setCurrent(ViewPreferences.ViewType c) {
        current = ViewPreferences.ViewType.INGENIAS;
        return (JPanel) RenderComponentManager.retrievePanel("StateGoal", current);
    }
}
