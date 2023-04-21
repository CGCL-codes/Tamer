package org.yawlfoundation.yawl.editor.swing.element;

import javax.swing.JPanel;
import org.yawlfoundation.yawl.editor.elements.model.YAWLTask;
import org.yawlfoundation.yawl.editor.net.NetGraph;

public abstract class AbstractTaskDoneDialog extends AbstractVertexDoneDialog {

    public AbstractTaskDoneDialog(String title, boolean modality, boolean showCancelButton) {
        super(title, modality, showCancelButton);
    }

    public AbstractTaskDoneDialog(String title, boolean modality, JPanel contentPanel, boolean showCancelButton) {
        super(title, modality, contentPanel, showCancelButton);
    }

    public void setTask(YAWLTask task, NetGraph graph) {
        super.setVertex(task, graph);
    }

    public YAWLTask getTask() {
        return (YAWLTask) getVertex();
    }
}
