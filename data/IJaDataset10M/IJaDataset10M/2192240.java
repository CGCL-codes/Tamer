package ingenias.editor.cell;

import org.jgraph.graph.*;
import ingenias.editor.entities.*;

public class ApplicationEventCell extends DefaultGraphCell {

    public ApplicationEventCell(ApplicationEvent userObject) {
        super(userObject);
        DefaultPort port = new DefaultPort(userObject);
        this.add(port);
    }

    public String toString() {
        return this.getUserObject().toString();
    }
}
