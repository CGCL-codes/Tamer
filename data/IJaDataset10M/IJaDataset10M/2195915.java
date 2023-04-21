package au.edu.qut.yawl.editor.elements.model;

import java.awt.Point;
import au.edu.qut.yawl.editor.data.Decomposition;

public class CompositeTask extends YAWLTask implements YAWLCompositeTask {

    public CompositeTask() {
        super();
    }

    public CompositeTask(Point startPoint) {
        super(startPoint);
    }

    public String getUnfoldingNetName() {
        if (getDecomposition() != null) {
            return getDecomposition().getLabel();
        }
        return "";
    }

    public void setDecomposition(Decomposition decomposition) {
        if (getDecomposition() == null || !getDecomposition().equals(decomposition)) {
            super.setDecomposition(decomposition);
            resetParameterLists();
        }
    }

    public String getType() {
        return "Composite Task";
    }
}
