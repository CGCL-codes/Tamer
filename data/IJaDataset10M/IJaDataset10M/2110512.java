package org.kalypso.nofdpidss.evaluation.assessment.view.edit.detailtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICostCriterionDefinition;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICriterion;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ILocalCriterion;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ILocalCriterionDefinition;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.utils.gml.ATGmlUtils;
import org.kalypso.nofdpidss.core.common.utils.gml.VMGmlUtils;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class ATDetailTableMapping {

    private final List<ATCriterionData> m_lCrData = new ArrayList<ATCriterionData>();

    private final List<String> m_lMeasures = new LinkedList<String>();

    public ATDetailTableMapping() {
    }

    public Double getAggregatedMeasureValue(final Feature fVariant, final Feature fCrDef) {
        for (final ATCriterionData data : m_lCrData) if (fVariant.equals(data.getVariant()) && fCrDef.equals(data.getCriterionDef())) return data.getCriterionAggregatedValue();
        return null;
    }

    public Map<Integer, String> getColMeasureMapping(final List<String> lMeasures) {
        final Map<Integer, String> mapping = new HashMap<Integer, String>();
        for (final String measure : lMeasures) {
            final int index = m_lMeasures.indexOf(measure);
            if (index >= 0) mapping.put(index, measure);
        }
        return mapping;
    }

    public ATCriterionData getCriterionData(final int index) {
        return m_lCrData.get(index);
    }

    public String getMeasurementInColumn(final int index) {
        if (m_lMeasures.size() > index) return m_lMeasures.get(index);
        return null;
    }

    public int getNumberOfMeasureColumns() {
        if (m_lMeasures.size() == 0) return 1;
        return m_lMeasures.size();
    }

    public ATCriterionData.TYPE getTypeOfCriterion(final Feature fCrDef) {
        for (final ATCriterionData data : m_lCrData) if (fCrDef.equals(data.getCriterionDef())) return data.getCriterionType();
        return ATCriterionData.TYPE.UNDEFINED;
    }

    public void map() {
        final List lstCrMembers = ATGmlUtils.getCriterionMembers();
        if (lstCrMembers == null) return;
        for (final Object obj : lstCrMembers) {
            if (obj == null || !(obj instanceof Feature)) continue;
            final Feature fCr = (Feature) obj;
            final ATCriterionData data = new ATCriterionData();
            data.m_sCriterionName = ATGmlUtils.getFeatureNameAssessment(fCr);
            final Object objValue = fCr.getProperty(ICriterion.QN_VALUE);
            data.setCriterionValue((Double) objValue);
            final Object objVariant = fCr.getProperty(ICriterion.QN_LINKED_VARIANT);
            if (objVariant == null) continue;
            data.setVariantLink((String) objVariant);
            final Feature fLnkCrDef = ATGmlUtils.getLinkedCriterionDefinition(fCr);
            if (fLnkCrDef == null) continue;
            data.setLinkedCriterionDef(fLnkCrDef);
            if (ILocalCriterionDefinition.QN_TYPE.equals(fLnkCrDef.getFeatureType().getQName()) || ICostCriterionDefinition.QN_TYPE.equals(fLnkCrDef.getFeatureType().getQName())) {
                final Object objAggValue = fCr.getProperty(ILocalCriterion.QN_AGGREGATED_VALUE);
                if (objAggValue instanceof Double) data.setCriterionAggregatedValue((Double) objAggValue);
            }
            m_lCrData.add(data);
        }
        mapVariantMeasures();
    }

    private void mapVariantMeasures() {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IVariant[] variantsList = VMGmlUtils.getListOfVariants(pool);
        if (variantsList == null) return;
        for (final Object obj : variantsList) {
            if (obj == null || !(obj instanceof Feature)) continue;
            final Feature fVar = (Feature) obj;
            final Object objLstMeasures = fVar.getProperty(IVariant.QN_MEASURES);
            if (objLstMeasures == null || !(objLstMeasures instanceof List)) continue;
            final List lstMeasures = (List) objLstMeasures;
            for (final Object objMeasure : lstMeasures) {
                if (objMeasure == null) continue;
                final String sMeasureId = (String) objMeasure;
                if (!m_lMeasures.contains(sMeasureId)) m_lMeasures.add(sMeasureId);
            }
        }
    }
}
