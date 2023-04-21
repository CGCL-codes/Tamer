package org.kalypso.model.wspm.sobek.core.model;

import org.kalypso.model.wspm.sobek.core.interfaces.IBranch;
import org.kalypso.model.wspm.sobek.core.interfaces.IModelMember;
import org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir;
import org.kalypso.model.wspm.sobek.core.interfaces.ISobekConstants;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author thuel2
 */
public class SbkStructWeir extends SbkStructure implements ISbkStructWeir {

    public SbkStructWeir(final IModelMember model, final Feature node) {
        super(model, node);
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir#getCrestLevel()
   */
    public double getCrestLevel() {
        final Object property = getFeature().getProperty(ISobekConstants.QN_HYDRAULIC_SBK_STRUCTURE_WEIR_CREST_HEIGHT);
        if (property instanceof Double) return ((Double) property).doubleValue();
        return Double.NaN;
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir#getCrestHeight()
   */
    public double getCrestWidth() {
        final Object property = getFeature().getProperty(ISobekConstants.QN_HYDRAULIC_SBK_STRUCTURE_WEIR_CREST_WIDTH);
        if (property instanceof Double) return ((Double) property).doubleValue();
        return Double.NaN;
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir#getDischargeCoeffCE()
   */
    public double getDischargeCoeffCE() {
        final Object property = getFeature().getProperty(ISobekConstants.QN_HYDRAULIC_SBK_STRUCTURE_WEIR_DISCHARGE_COEFF);
        if (property instanceof Double) return ((Double) property).doubleValue();
        return Double.NaN;
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir#getFlowDirection()
   */
    public String getFlowDirection() {
        return (String) getFeature().getProperty(ISobekConstants.QN_HYDRAULIC_SBK_STRUCTURE_WEIR_FLOW_DIRECTION);
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir#getLateralContractionCoeffCW()
   */
    public double getLateralContractionCoeffCW() {
        final Object property = getFeature().getProperty(ISobekConstants.QN_HYDRAULIC_SBK_STRUCTURE_WEIR_LATERAL_CONTRACTION_COEFF);
        if (property instanceof Double) return ((Double) property).doubleValue();
        return Double.NaN;
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.INode#getType()
   */
    @Override
    public TYPE getType() {
        return TYPE.eSbkStructWeir;
    }

    /**
   * @see org.kalypso.model.wspm.sobek.core.interfaces.ISbkStructWeir#setLinkToBranch()
   */
    public void setLinkToBranch(final IBranch branch) throws Exception {
        FeatureUtils.setInternalLinkedFeature(getModel().getWorkspace(), getFeature(), ISobekConstants.QN_SBK_STRUCT_LINKS_TO_BRANCH, branch.getFeature());
    }
}
