package org.kalypso.nofdpidss.flow.network.base.sperrzone;

import org.kalypso.model.wspm.sobek.core.interfaces.INode;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;

/**
 * @author Dirk Kuch
 */
public interface ISperrzoneFilter {

    public boolean lookForNode(INode node);

    public boolean lookForMeasure(IMeasure measure);
}
