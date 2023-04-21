package shieh.pnn.jgraph;

import shieh.pnn.core.Unit;
import com.jgraph.pad.graph.JGraphpadBusinessObject;

public class JCellUserObject extends JGraphpadBusinessObject {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 2061864039293998862L;

    protected Unit cell;

    public JCellUserObject(Unit cell) {
        this.cell = cell;
    }

    @Override
    public String getTooltip() {
        return cell.toString();
    }

    @Override
    public String toString() {
        return "";
    }
}
