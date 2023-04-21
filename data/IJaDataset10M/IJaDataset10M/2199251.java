package org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.implementation;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.IHydraulicResultModel;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.IResultDocument;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.IResultDocumentMembers;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.FeatureList;

/**
 * @author Dirk Kuch
 */
public class ResultDocumentMembersHandler extends AbstractHydraulicResultModelListWrapper implements IResultDocumentMembers {

    public ResultDocumentMembersHandler(final IHydraulicResultModel model, final FeatureList list) {
        super(model, list);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.result.IResultDocumentMembers#getDocuments()
   */
    public IResultDocument[] getDocuments() {
        final List<IResultDocument> myList = new ArrayList<IResultDocument>();
        for (final Object obj : this) {
            if (!(obj instanceof Feature)) continue;
            myList.add(new ResultDocumentHandler(getModel(), (Feature) obj));
        }
        return myList.toArray(new IResultDocument[] {});
    }
}
