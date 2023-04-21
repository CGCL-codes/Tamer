package com.tetratech.edas2.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import com.tetratech.edas2.model.ActivityMedia;
import com.tetratech.edas2.model.ActivityMediaSubdivision;
import com.tetratech.edas2.model.ActivityType;
import com.tetratech.edas2.model.AnalyticalMethod;
import com.tetratech.edas2.model.AnalyticalMethodContext;
import com.tetratech.edas2.model.Assemblage;
import com.tetratech.edas2.model.BiologicalIntent;
import com.tetratech.edas2.model.Characteristic;
import com.tetratech.edas2.model.CharacteristicType;
import com.tetratech.edas2.model.DetectionQuantitationLimitType;
import com.tetratech.edas2.model.FrequencyClassDescriptor;
import com.tetratech.edas2.model.FunctionalFeedingGroup;
import com.tetratech.edas2.model.Habit;
import com.tetratech.edas2.model.HorizontalCollectionMethod;
import com.tetratech.edas2.model.HorizontalReferenceDatum;
import com.tetratech.edas2.model.MeasurementUnit;
import com.tetratech.edas2.model.MonitoringLocationType;
import com.tetratech.edas2.model.Project;
import com.tetratech.edas2.model.RelativeDepth;
import com.tetratech.edas2.model.ResultDetectionCondition;
import com.tetratech.edas2.model.ResultMeasurementQualifier;
import com.tetratech.edas2.model.ResultStatus;
import com.tetratech.edas2.model.ResultValueType;
import com.tetratech.edas2.model.ResultWeightBasis;
import com.tetratech.edas2.model.SampleFraction;
import com.tetratech.edas2.model.SampleTissueAnatomy;
import com.tetratech.edas2.model.SamplingCollectionEquipment;
import com.tetratech.edas2.model.Taxon;
import com.tetratech.edas2.model.TaxonRank;
import com.tetratech.edas2.model.TrophicLevel;

@Name("lookupValueFactory")
@Scope(ScopeType.STATELESS)
public class LookupValueFactory {

    @Logger
    private Log log;

    @In
    private EntityManager entityManager;

    @Factory(value = "resultStatusValues", scope = ScopeType.CONVERSATION)
    public List<ResultStatus> getResultStatusValues() {
        return findAll(ResultStatus.class);
    }

    @Factory(value = "resultMeasurementQualifierValues", scope = ScopeType.CONVERSATION)
    public List<ResultMeasurementQualifier> getResultMeasureQualifierValues() {
        return findAll(ResultMeasurementQualifier.class);
    }

    @Factory(value = "resultValueTypeValues", scope = ScopeType.CONVERSATION)
    public List<ResultValueType> getResultValueTypeValues() {
        return findAll(ResultValueType.class);
    }

    @Factory(value = "characteristicValues", scope = ScopeType.CONVERSATION)
    public List<Characteristic> getCharacteristicValues() {
        return findAll(Characteristic.class);
    }

    @Factory(value = "characteristicTypeValues", scope = ScopeType.CONVERSATION)
    public List<CharacteristicType> getCharacteristicTypeValues() {
        return findAll(CharacteristicType.class);
    }

    @Factory(value = "resultDetectionConditionValues", scope = ScopeType.CONVERSATION)
    public List<ResultDetectionCondition> getResultDetectionConditionValues() {
        return findAll(ResultDetectionCondition.class);
    }

    @Factory(value = "measurementUnitValues", scope = ScopeType.CONVERSATION)
    public List<MeasurementUnit> getMeasurementUnitValues() {
        return findAll(MeasurementUnit.class);
    }

    @Factory(value = "assemblageValues", scope = ScopeType.CONVERSATION)
    public List<Assemblage> getAssemblageValues() {
        return findAll(Assemblage.class);
    }

    @Factory(value = "sampleCollEquipValues", scope = ScopeType.CONVERSATION)
    public List<SamplingCollectionEquipment> getSampleCollEquipValues() {
        return findAll(SamplingCollectionEquipment.class, "name asc");
    }

    @Factory(value = "activityTypeValues", scope = ScopeType.CONVERSATION)
    public List<ActivityType> getActivityTypeValues() {
        return findAll(ActivityType.class, "code asc");
    }

    @Factory(value = "activityMediaValues", scope = ScopeType.CONVERSATION)
    public List<ActivityMedia> getActivityMediaValues() {
        return findAll(ActivityMedia.class, "name asc");
    }

    @Factory(value = "activityMediaSubdivisionValues", scope = ScopeType.CONVERSATION)
    public List<ActivityMediaSubdivision> getActivityMediaSubdivValues() {
        return findAll(ActivityMediaSubdivision.class, "name asc");
    }

    @Factory(value = "lifestageValues", scope = ScopeType.CONVERSATION)
    public List<FrequencyClassDescriptor> getLifestageValues() {
        return entityManager.createQuery("select fc from FrequencyClassDescriptor fc join fetch fc.frequencyClassType where fc.frequencyClassType.uid in (2,10)").getResultList();
    }

    @Factory(value = "taxonValues", scope = ScopeType.CONVERSATION)
    public List<Taxon> getTaxonValues() {
        return findAll(Taxon.class, "name asc");
    }

    @Factory(value = "biologicalIntentValues", scope = ScopeType.CONVERSATION)
    public List<BiologicalIntent> getBiologicalIntentValues() {
        return findAll(BiologicalIntent.class, "name asc");
    }

    @Factory(value = "projectValues", scope = ScopeType.CONVERSATION)
    public List<Project> getProjectValues() {
        return findAll(Project.class, "name asc");
    }

    @Factory(value = "horizontalCollectionMethodValues", scope = ScopeType.CONVERSATION)
    public List<HorizontalCollectionMethod> getHorizontalCollectionMethodValues() {
        return findAll(HorizontalCollectionMethod.class, "name asc");
    }

    @Factory(value = "horizontalReferenceDatumValues", scope = ScopeType.CONVERSATION)
    public List<HorizontalReferenceDatum> getHorizontalReferenceDatumValues() {
        return findAll(HorizontalReferenceDatum.class, "name asc");
    }

    @Factory(value = "taxonRankValues", scope = ScopeType.CONVERSATION)
    public List<TaxonRank> getTaxonRankValues() {
        return findAll(TaxonRank.class, "name asc");
    }

    @Factory(value = "monitoringLocationTypeValues", scope = ScopeType.CONVERSATION)
    public List<MonitoringLocationType> getMonitoringLocationTypeValues() {
        return findAll(MonitoringLocationType.class, "name asc");
    }

    @Factory(value = "sampleFractionValues", scope = ScopeType.CONVERSATION)
    public List<SampleFraction> getSampleFractionValues() {
        return findAll(SampleFraction.class, "name asc");
    }

    @Factory(value = "analyticalMethodValues", scope = ScopeType.CONVERSATION)
    public List<AnalyticalMethod> getAnalyticalMethodValues() {
        return findAll(AnalyticalMethod.class, "name asc");
    }

    @Factory(value = "detQuantLimitTypeValues", scope = ScopeType.CONVERSATION)
    public List<DetectionQuantitationLimitType> getDetQuantLimitTypeValues() {
        return findAll(DetectionQuantitationLimitType.class, "name asc");
    }

    @Factory(value = "habitValues", scope = ScopeType.CONVERSATION)
    public List<Habit> getHabitValues() {
        return findAll(Habit.class, "name asc");
    }

    @Factory(value = "relativeDepthValues", scope = ScopeType.CONVERSATION)
    public List<RelativeDepth> getRelativeDepthValues() {
        return findAll(RelativeDepth.class, "name asc");
    }

    @Factory(value = "resultWeighBasisValues", scope = ScopeType.CONVERSATION)
    public List<ResultWeightBasis> getResultWeightBasisValues() {
        return findAll(ResultWeightBasis.class, "name asc");
    }

    @Factory(value = "sampleTissueAnatomyValues", scope = ScopeType.CONVERSATION)
    public List<SampleTissueAnatomy> getSampleTissueAnatomyValues() {
        return findAll(SampleTissueAnatomy.class, "name asc");
    }

    @Factory(value = "analyMethdCtxValues", scope = ScopeType.CONVERSATION)
    public List<AnalyticalMethodContext> getAnalyMethdCtxValues() {
        return findAll(AnalyticalMethodContext.class, "code asc");
    }

    @Factory(value = "trophicLevelValues", scope = ScopeType.CONVERSATION)
    public List<TrophicLevel> getTrophicLevelValues() {
        return findAll(TrophicLevel.class, "name asc");
    }

    @Factory(value = "functFeedingGrpCodeValues", scope = ScopeType.CONVERSATION)
    public List<String> getFunctFeedingGrpValues() {
        List<FunctionalFeedingGroup> vals = findAll(FunctionalFeedingGroup.class, "name asc");
        List<String> codeVals = new ArrayList<String>();
        for (Iterator<FunctionalFeedingGroup> it = vals.iterator(); it.hasNext(); ) {
            FunctionalFeedingGroup val = (FunctionalFeedingGroup) it.next();
            codeVals.add(val.getCode());
        }
        return codeVals;
    }

    private <T> List<T> findAll(Class<T> type) {
        return findAll(type, null);
    }

    private <T> List<T> findAll(Class<T> type, String orderBy) {
        StringBuilder query = new StringBuilder();
        query.append("select e from ").append(type.getSimpleName()).append(" e");
        if (orderBy != null) {
            query.append(" order by e.").append(orderBy);
        }
        List<T> records = entityManager.createQuery(query.toString()).setHint("org.hibernate.readOnly", true).getResultList();
        if (records.size() > 25) {
            log.warn("You should be using auto-complete for " + type.getSimpleName() + " rather than a list of values.");
        }
        return records;
    }
}
