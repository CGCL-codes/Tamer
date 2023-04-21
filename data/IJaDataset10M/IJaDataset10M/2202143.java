package org.xmi.graph.jgraph.uml;

import java.awt.Component;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.jgraph.JGraph;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;

public class ClassRenderer extends JComponent implements CellViewRenderer, Serializable {

    protected transient ClassView view;

    protected transient JGraph graph;

    protected JTextArea header = null;

    protected transient boolean hasFocus, selected, preview, opaque;

    public Component getRendererComponent(JGraph graph, CellView view, boolean sel, boolean focus, boolean preview) {
        if (view instanceof ClassView) {
            this.view = (ClassView) view;
            setComponentOrientation(graph.getComponentOrientation());
            if (graph.getEditingCell() != view.getCell()) {
                Object label = graph.convertValueToString(view.getCell());
                if (label != null) {
                    header.setText(label.toString());
                } else {
                    header.setText(null);
                }
            } else {
                header.setText(null);
            }
            this.graph = graph;
            this.hasFocus = focus;
            this.selected = sel;
            this.preview = preview;
            header.setText(null);
            setBorder(null);
            setOpaque(false);
            return this;
        }
        return null;
    }
}
