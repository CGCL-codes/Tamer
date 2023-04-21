package pl.edu.wat.wcy.jit.view.editor.menu;

import javax.swing.JMenuBar;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class EditorMenuBar extends JMenuBar {

    /**
     *
     */
    private static final long serialVersionUID = 4060203894740766714L;

    @SuppressWarnings("serial")
    public EditorMenuBar(final BasicGraphEditor editor) {
        final mxGraphComponent graphComponent = editor.getGraphComponent();
        final mxGraph graph = graphComponent.getGraph();
    }
}
