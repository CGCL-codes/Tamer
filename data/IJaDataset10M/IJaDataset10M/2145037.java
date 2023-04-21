package net.sf.yari.hypergraph;

import org.eclipse.swt.widgets.Control;
import hypergraph.graph.NodeImpl;

/**
 * @author Remo Loetscher
 * 
 */
public class YariNodeImpl extends NodeImpl {

    Control swtControl;

    /**
	 * @param arg0
	 */
    protected YariNodeImpl(String arg0) {
        super(arg0);
    }

    /**
	 * @return
	 */
    public Control getSwtControl() {
        return swtControl;
    }

    /**
	 * @param swtControl
	 */
    public void setSwtControl(Control swtControl) {
        this.swtControl = swtControl;
    }
}
