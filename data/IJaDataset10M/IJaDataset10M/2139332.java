package org.nodes4knime.chemicalfilewriter;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ChemicalFileWriter" Node. read table of
 * chemical data and write to chemical file
 * 
 * @author M Simmons, The Edge
 * Contact: Andrew Lemon, The Edge, andrew@edgesoftwareconsultancy.com
 */
public class ChemicalFileWriterNodeFactory extends NodeFactory {

    /**
	 * @see org.knime.core.node.NodeFactory#createNodeModel()
	 */
    @Override
    public NodeModel createNodeModel() {
        return new ChemicalFileWriterNodeModel();
    }

    /**
	 * @see org.knime.core.node.NodeFactory#getNrNodeViews()
	 */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
	 * @see org.knime.core.node.NodeFactory#createNodeView(int,
	 *      org.knime.core.node.NodeModel)
	 */
    @Override
    public NodeView createNodeView(final int viewIndex, final NodeModel nodeModel) {
        return new ChemicalFileWriterNodeView(nodeModel);
    }

    /**
	 * @see org.knime.core.node.NodeFactory#hasDialog()
	 */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
	 * @see org.knime.core.node.NodeFactory#createNodeDialogPane()
	 */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new ChemicalFileWriterNodeDialog();
    }
}
