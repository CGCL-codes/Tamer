package ingenias.editor.cell;

import org.jgraph.graph.*;
import ingenias.editor.entities.*;

public class RoleWSCell extends DefaultGraphCell {

    public RoleWSCell(RoleWS userObject) {
        super(userObject);
        DefaultPort port = new DefaultPort(userObject);
        this.add(port);
    }

    public String toString() {
        return this.getUserObject().toString();
    }
}
