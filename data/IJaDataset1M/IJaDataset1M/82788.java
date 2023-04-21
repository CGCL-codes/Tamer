package org.kalypso.nofdpidss.core.base.gml.model.project.info.implementation;

import org.kalypso.nofdpidss.core.base.gml.model.AbstractListWrapper;
import org.kalypso.nofdpidss.core.base.gml.model.project.info.IProjectInfoModel;
import org.kalypsodeegree.model.feature.FeatureList;

/**
 * @author Dirk Kuch
 */
public abstract class AbstractProjectInfoModelListWrapper extends AbstractListWrapper {

    private final IProjectInfoModel m_model;

    public AbstractProjectInfoModelListWrapper(final IProjectInfoModel model, final FeatureList list) {
        super(list);
        m_model = model;
    }

    public IProjectInfoModel getModel() {
        return m_model;
    }
}
