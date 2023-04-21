package ingenias.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Hashtable;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Map;
import java.util.Hashtable;
import java.util.ArrayList;
import javax.swing.event.UndoableEditEvent;
import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.jgraph.event.*;
import java.util.Vector;
import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.jgraph.event.*;
import org.jgraph.plaf.basic.*;
import java.lang.reflect.*;
import ingenias.editor.entities.*;
import ingenias.editor.widget.*;
import ingenias.exception.NullEntity;
import ingenias.generator.browser.BrowserImp;
import ingenias.generator.browser.Graph;
import ingenias.generator.browser.GraphEntity;
import ingenias.exception.InvalidEntity;

public class ActivityDiagramMarqueeHandler extends MarqueeHandler implements java.io.Serializable {

    public ActivityDiagramMarqueeHandler(ModelJGraph graph) {
        super(graph);
    }

    protected Vector<AbstractAction> createChangeViewActions(final DefaultGraphCell cell) {
        Vector<AbstractAction> possibleViews = new Vector<AbstractAction>();
        final ingenias.editor.entities.Entity ent = ((ingenias.editor.entities.Entity) cell.getUserObject());
        if (ent.getClass().getName().equals("ingenias.editor.entities.InitialNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.InitialNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.DecisionNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.DecisionNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.ForkNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.ForkNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.EndNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.EndNode")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Task")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Task")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Role")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Role")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WorkflowBox")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WorkflowBox")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.UMLComment")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.UMLComment")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.TextNote")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.TextNote")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.UMLAnnotatedElement")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.UMLAnnotatedElement")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.UMLAnnotatedElement")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFStarts")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFStarts")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFStarts")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFEnds")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFEnds")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFEnds")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFDecides")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFDecides")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFDecides")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFFollows")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFFollows")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFFollows")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFFollowsGuarded")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFFollowsGuarded")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.WFFollowsGuarded")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        return possibleViews;
    }

    protected Vector<AbstractAction> createDiagramSpecificInsertActions(final Point pt) {
        Vector<AbstractAction> nobjects = new Vector<AbstractAction>();
        nobjects.add(new AbstractAction("Insert InitialNode") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "InitialNode");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type InitialNode is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert DecisionNode") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "DecisionNode");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type DecisionNode is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert ForkNode") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "ForkNode");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type ForkNode is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert EndNode") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "EndNode");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type EndNode is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert Task") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "Task");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type Task is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert Role") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "Role");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type Role is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert WorkflowBox") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "WorkflowBox");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type WorkflowBox is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert UMLComment") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "UMLComment");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type UMLComment is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert TextNote") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "TextNote");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type TextNote is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        return nobjects;
    }
}
