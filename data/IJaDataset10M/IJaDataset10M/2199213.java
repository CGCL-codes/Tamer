package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.view.reactor.bin.graph.nodes;

import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Bin;
import edu.ucdavis.genomics.metabolomics.binbase.gui.visualize.object.Node;

public class BinNode extends Node {

    @Override
    public int compareTo(Node o) {
        if (o instanceof BinNode) {
            BinNode x = (BinNode) o;
            return x.getBin().getId().compareTo(this.getBin().getId());
        }
        return super.compareTo(o);
    }

    private Bin bin;

    public BinNode(String name, Bin bin) {
        super(name);
        this.bin = bin;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinNode) {
            BinNode o = (BinNode) obj;
            return o.getBin().getId().equals(this.getBin().getId());
        }
        return false;
    }
}
