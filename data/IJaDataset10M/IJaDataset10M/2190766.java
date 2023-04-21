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

public class AUMLInteractionDiagramMarqueeHandler extends MarqueeHandler implements java.io.Serializable {

    public AUMLInteractionDiagramMarqueeHandler(ModelJGraph graph) {
        super(graph);
    }

    protected Vector<AbstractAction> createChangeViewActions(final DefaultGraphCell cell) {
        Vector<AbstractAction> possibleViews = new Vector<AbstractAction>();
        final ingenias.editor.entities.Entity ent = ((ingenias.editor.entities.Entity) cell.getUserObject());
        if (ent.getClass().getName().equals("ingenias.editor.entities.Protocol")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Protocol")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.SubProtocol")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.SubProtocol")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Lifeline")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Lifeline")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Column")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.Column")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLPort")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLPort")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLAlternativeBox")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLAlternativeBox")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.INGENIAS;
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.INGENIAS);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLAlternativeRow")) {
            final ViewPreferences.ViewType current1 = ViewPreferences.ViewType.UML;
            possibleViews.add(new AbstractAction("UML") {

                public void actionPerformed(ActionEvent e) {
                    ent.getPrefs().setView(ViewPreferences.ViewType.UML);
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLAlternativeRow")) {
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
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLSendSimple")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLSendSimple")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLSendSimple")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLSelection")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLSelection")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLSelection")) {
            possibleViews.add(new AbstractAction("LABEL") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.LABEL, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLUseProtocol")) {
            possibleViews.add(new AbstractAction("NOICON") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.NOICON, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLUseProtocol")) {
            possibleViews.add(new AbstractAction("INGENIAS") {

                public void actionPerformed(ActionEvent e) {
                    ingenias.editor.cell.RenderComponentManager.setRelationshipView(ViewPreferences.ViewType.INGENIAS, ent, cell, getGraph());
                    getGraph().repaint();
                }
            });
        }
        if (ent.getClass().getName().equals("ingenias.editor.entities.AUMLUseProtocol")) {
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
        nobjects.add(new AbstractAction("Insert Protocol") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "Protocol");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type Protocol is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert SubProtocol") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "SubProtocol");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type SubProtocol is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert Lifeline") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "Lifeline");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type Lifeline is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert Column") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "Column");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type Column is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert AUMLPort") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "AUMLPort");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type AUMLPort is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert AUMLAlternativeBox") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "AUMLAlternativeBox");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type AUMLAlternativeBox is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        nobjects.add(new AbstractAction("Insert AUMLAlternativeRow") {

            public void actionPerformed(ActionEvent ev) {
                try {
                    getGraph().insert(pt, "AUMLAlternativeRow");
                } catch (InvalidEntity e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(IDE.ide, "Object type AUMLAlternativeRow is not allowed in this diagram", "Warning", JOptionPane.WARNING_MESSAGE);
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
        return nobjects;
    }
}
