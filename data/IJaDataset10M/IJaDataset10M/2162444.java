package ingenias.editor.cell;

import org.jgraph.graph.*;
import ingenias.editor.entities.*;

public class MentalStateManagerCell extends DefaultGraphCell {

    public MentalStateManagerCell(MentalStateManager userObject) {
        super(userObject);
        DefaultPort port = new DefaultPort(userObject);
        this.add(port);
    }

    public String toString() {
        return this.getUserObject().toString();
    }
}
