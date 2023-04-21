package org.kalypso.model.wspm.sobek.core.model;

import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.IConnectionNode;
import org.kalypso.model.wspm.sobek.core.interfaces.IModelMember;
import org.kalypso.model.wspm.sobek.core.interfaces.INode;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class ConnectionNode extends AbstractConnectionNode implements IConnectionNode {

    public ConnectionNode(final IModelMember model, final Feature node) {
        super(model, node);
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.INode#delete()
   */
    public void delete() throws Exception {
        FeatureUtils.deleteFeature(getModel().getWorkspace(), getFeature());
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.INode#getType()
   */
    public TYPE getType() {
        return INode.TYPE.eConnectionNode;
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.INode#isEmpty()
   */
    public boolean isEmpty() {
        final IBranch[] inflowingBranches = getInflowingBranches();
        final IBranch[] outflowingBranches = getOutflowingBranches();
        if (inflowingBranches.length == 0 && outflowingBranches.length == 0) return true;
        return false;
    }
}
