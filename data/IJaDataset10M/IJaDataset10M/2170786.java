package wilos.tools.imports.epfcomposer.fillers;

import org.w3c.dom.Node;
import wilos.model.spem2.element.Element;

public class FillerIteration extends FillerActivity {

    /**
     * Constructor of FillerIteration
     * 
     * @param _e
     * @param _aNode
     */
    public FillerIteration(Element _e, Node _aNode) {
        super(_e, _aNode);
        fill();
    }

    private void fill() {
    }
}
