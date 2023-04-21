package org.yaoqiang.bpmn.editor.swing;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import org.yaoqiang.bpmn.editor.BPMNEditor;
import org.yaoqiang.bpmn.editor.action.BPMNModelActions;
import org.yaoqiang.graph.editor.action.EditorActions;
import org.yaoqiang.graph.editor.swing.GraphComponent;
import org.yaoqiang.graph.editor.swing.TabbedPane;
import org.yaoqiang.graph.editor.view.Graph;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;

/**
 * BPMNTabbedPane
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class BPMNTabbedPane extends TabbedPane {

    private static final long serialVersionUID = 1L;

    public BPMNTabbedPane(BPMNEditor editor) {
        super(editor);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            BPMNTabbedPane centerPane = (BPMNTabbedPane) e.getSource();
            Component c = centerPane.getSelectedComponent();
            if (c instanceof mxGraphComponent) {
                Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), c);
                JPopupMenu popup = new JPopupMenu();
                if (getEditor(e).getGraphComponent() == c) {
                    popup.add(getEditor(e).bind(mxResources.get("editDiagramName"), BPMNModelActions.getAction(BPMNModelActions.DIAGRAM_NAME), "/org/yaoqiang/graph/editor/images/edit.png"));
                } else if (getEditor(e).getGraphComponents().containsValue(c)) {
                    popup.add(getEditor(e).bind(mxResources.get("close"), EditorActions.getCloseCalledProcessAction(c.getName()), "/org/yaoqiang/graph/editor/images/delete.png"));
                }
                popup.show(c, pt.x, pt.y);
            }
            e.consume();
        }
    }

    public void stateChanged(ChangeEvent e) {
        BPMNEditor editor = (BPMNEditor) getEditor(e);
        Component c = ((JTabbedPane) e.getSource()).getSelectedComponent();
        if (c.getName() != null && c.getName().equals("BPMNView")) {
            if (!BPMNEditor.isNativeFormat()) {
                editor.getBpmnView().refreshView(editor.getGraphComponent().getGraph());
            }
        } else if (editor != null && c instanceof GraphComponent) {
            boolean reset = false;
            GraphComponent graphComponent = (GraphComponent) c;
            if (editor.getCurrentGraphComponent() != c) {
                reset = true;
            }
            editor.setCurrentGraphComponent(graphComponent);
            Graph graph = graphComponent.getGraph();
            graph.getView().setCurrentRoot(graphComponent.getLastViewRoot());
            graph.clearSelection();
            editor.refreshGraphOverview();
            if (reset) {
                editor.getUndoManager().clear();
            }
        }
    }
}
