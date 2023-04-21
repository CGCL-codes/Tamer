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

public class SimulationPackageRenderer extends VertexRenderer implements CellViewRenderer, Serializable {

    private static ViewPreferences.ViewType current = ViewPreferences.ViewType.INGENIAS;

    static {
        try {
            ViewPreferences.ViewType index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.INGENIAS;
            index = ViewPreferences.ViewType.UML;
            RenderComponentManager.loadRenderFile("SimulationPackage", index, "/ingenias/editor/rendererxml/SimulationPackageUMLPanel.xml");
            index = ViewPreferences.ViewType.INGENIAS;
            RenderComponentManager.loadRenderFile("SimulationPackage", index, "/ingenias/editor/rendererxml/SimulationPackageINGENIASPanel.xml");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Constructs a renderer that may be used to render vertices.
     */
    public SimulationPackageRenderer() {
    }

    public Dimension getSize() {
        return RenderComponentManager.getSize("SimulationPackage", current);
    }

    public boolean supportsAttribute(Object key) {
        return true;
    }

    public static void setEntity(SimulationPackage ent) {
        Map currentMap = (Map) RenderComponentManager.retrieveIDs("SimulationPackage", ent.getPrefs().getView());
        if (ent != null && currentMap.get("_attributes_") != null && currentMap.get("_attributes_") instanceof ingenias.editor.rendererxml.AttributesPanel) {
            ((ingenias.editor.rendererxml.AttributesPanel) currentMap.get("_attributes_")).setEntity(ent);
        }
        if (currentMap.get("Parameters") != null && currentMap.get("Parameters") instanceof ingenias.editor.rendererxml.CollectionPanel) {
            try {
                ((ingenias.editor.rendererxml.CollectionPanel) currentMap.get("Parameters")).setCollection("Parameters", ent.Parameters, ent.Parameters.getType());
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        if (currentMap.get("Test") != null) {
            if (ent != null && ent.getTest() != null) {
                if (currentMap.get("Test") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("Test")).setText(ent.getTest().toString());
                } else {
                    if (currentMap.get("Test") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("Test")).setText(ent.getTest().toString());
                }
            } else {
                if (currentMap.get("Test") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("Test")).setText(""); else {
                    if (!(currentMap.get("Test") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("Test")).setText("");
                }
            }
        }
        if (currentMap.get("SimulationDeployment") != null) {
            if (ent != null && ent.getSimulationDeployment() != null) {
                if (currentMap.get("SimulationDeployment") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("SimulationDeployment")).setText(ent.getSimulationDeployment().toString());
                } else {
                    if (currentMap.get("SimulationDeployment") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("SimulationDeployment")).setText(ent.getSimulationDeployment().toString());
                }
            } else {
                if (currentMap.get("SimulationDeployment") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("SimulationDeployment")).setText(""); else {
                    if (!(currentMap.get("SimulationDeployment") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("SimulationDeployment")).setText("");
                }
            }
        }
        if (currentMap.get("Duration") != null) {
            if (ent != null && ent.getDuration() != null) {
                if (currentMap.get("Duration") instanceof javax.swing.JLabel) {
                    ((javax.swing.JLabel) (currentMap).get("Duration")).setText(ent.getDuration().toString());
                } else {
                    if (currentMap.get("Duration") instanceof javax.swing.text.JTextComponent) ((javax.swing.text.JTextComponent) (currentMap).get("Duration")).setText(ent.getDuration().toString());
                }
            } else {
                if (currentMap.get("Duration") instanceof javax.swing.JLabel) ((javax.swing.JLabel) (currentMap).get("Duration")).setText(""); else {
                    if (!(currentMap.get("Duration") instanceof ingenias.editor.rendererxml.CollectionPanel)) ((javax.swing.text.JTextComponent) (currentMap).get("Duration")).setText("");
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
        return RenderComponentManager.retrievePanel("SimulationPackage", ((Entity) ((DefaultGraphCell) (view.getCell())).getUserObject()).getPrefs().getView());
    }

    public static JPanel setCurrent(ViewPreferences.ViewType c) {
        current = ViewPreferences.ViewType.INGENIAS;
        return (JPanel) RenderComponentManager.retrievePanel("SimulationPackage", current);
    }
}
