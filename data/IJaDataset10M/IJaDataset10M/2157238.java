package ingenias.editor.events;

import ingenias.editor.IDEState;
import ingenias.editor.ModelJGraph;

public interface DiagramCreationAction {

    public ModelJGraph execute(String diagramName, Object[] path, IDEState ids);

    public String getActionName();

    public String getIconName();
}
