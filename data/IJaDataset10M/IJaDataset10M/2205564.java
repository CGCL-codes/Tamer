package com.vangent.hieos.services.pixpdq.empi.impl.base;

import com.vangent.hieos.empi.config.EMPIConfig;
import com.vangent.hieos.empi.exception.EMPIException;
import com.vangent.hieos.empi.match.MatchAlgorithm;
import com.vangent.hieos.empi.match.MatchAlgorithm.MatchType;
import com.vangent.hieos.empi.match.MatchResults;
import com.vangent.hieos.empi.match.Record;
import com.vangent.hieos.empi.match.RecordBuilder;
import com.vangent.hieos.empi.match.ScoredRecord;
import com.vangent.hieos.empi.persistence.PersistenceManager;
import com.vangent.hieos.hl7v3util.model.subject.DeviceInfo;
import com.vangent.hieos.hl7v3util.model.subject.Subject;
import com.vangent.hieos.hl7v3util.model.subject.SubjectIdentifier;
import com.vangent.hieos.hl7v3util.model.subject.SubjectIdentifierDomain;
import com.vangent.hieos.hl7v3util.model.subject.SubjectSearchCriteria;
import com.vangent.hieos.hl7v3util.model.subject.SubjectSearchResponse;
import com.vangent.hieos.xutil.xconfig.XConfigActor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class FindSubjectsHandler extends BaseHandler {

    private static final Logger logger = Logger.getLogger(FindSubjectsHandler.class);

    /**
     *
     * @param configActor
     * @param persistenceManager
     * @param senderDeviceInfo
     */
    public FindSubjectsHandler(XConfigActor configActor, PersistenceManager persistenceManager, DeviceInfo senderDeviceInfo) {
        super(configActor, persistenceManager, senderDeviceInfo);
    }

    /**
     *
     * @param subjectSearchCriteria
     * @return
     * @throws EMPIException
     */
    public SubjectSearchResponse findSubjects(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        SubjectSearchResponse subjectSearchResponse = new SubjectSearchResponse();
        this.validateSubjectIdentifierDomains(subjectSearchCriteria);
        if (subjectSearchCriteria.hasSubjectIdentifiers()) {
            logger.debug("Searching based on identifiers ...");
            subjectSearchResponse = this.loadSubjectByIdentifier(subjectSearchCriteria);
        } else if (subjectSearchCriteria.hasSubjectDemographics()) {
            logger.debug("Searching based on demographics ...");
            subjectSearchResponse = this.loadSubjectMatches(subjectSearchCriteria);
        } else {
            logger.debug("Not searching at all!!");
        }
        return subjectSearchResponse;
    }

    /**
     *
     * @param subjectSearchCriteria
     * @return
     * @throws EMPIException
     */
    public SubjectSearchResponse findSubjectByIdentifier(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        SubjectSearchResponse subjectSearchResponse = new SubjectSearchResponse();
        this.validateSubjectIdentifierDomains(subjectSearchCriteria);
        subjectSearchResponse = this.loadIdentifiersForSubjectByIdentifier(subjectSearchCriteria);
        return subjectSearchResponse;
    }

    /**
     *
     * @param searchRecord
     * @return
     * @throws EMPIException
     */
    public List<ScoredRecord> getRecordMatches(Record searchRecord, MatchType matchType) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        EMPIConfig empiConfig = EMPIConfig.getInstance();
        MatchAlgorithm matchAlgorithm = empiConfig.getMatchAlgorithm();
        matchAlgorithm.setPersistenceManager(pm);
        long startTime = System.currentTimeMillis();
        MatchResults matchResults = matchAlgorithm.findMatches(searchRecord, matchType);
        long endTime = System.currentTimeMillis();
        if (logger.isTraceEnabled()) {
            logger.trace("FindSubjectsHandler.getRecordMatches.findMatches: elapedTimeMillis=" + (endTime - startTime));
        }
        return matchResults.getMatches();
    }

    /**
     *
     * @param subjectSearchCriteria
     * @return
     * @throws EMPIException
     */
    private SubjectSearchResponse loadSubjectMatches(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        SubjectSearchResponse subjectSearchResponse = new SubjectSearchResponse();
        Subject searchSubject = subjectSearchCriteria.getSubject();
        boolean hasSpecifiedMinimumDegreeMatchPercentage = subjectSearchCriteria.hasSpecifiedMinimumDegreeMatchPercentage();
        int minimumDegreeMatchPercentage = subjectSearchCriteria.getMinimumDegreeMatchPercentage();
        RecordBuilder rb = new RecordBuilder();
        Record searchRecord = rb.build(searchSubject);
        List<ScoredRecord> recordMatches = this.getRecordMatches(searchRecord, MatchType.SUBJECT_FIND);
        List<Subject> subjectMatches = new ArrayList<Subject>();
        long startTime = System.currentTimeMillis();
        Set<String> enterpriseSubjectIds = new HashSet<String>();
        for (ScoredRecord scoredRecord : recordMatches) {
            Record record = scoredRecord.getRecord();
            int matchConfidencePercentage = scoredRecord.getMatchScorePercentage();
            if (logger.isDebugEnabled()) {
                logger.debug("match score = " + scoredRecord.getScore());
                logger.debug("gof score = " + scoredRecord.getGoodnessOfFitScore());
                logger.debug("... matchConfidencePercentage (int) = " + matchConfidencePercentage);
            }
            if (!hasSpecifiedMinimumDegreeMatchPercentage || (matchConfidencePercentage >= minimumDegreeMatchPercentage)) {
                String systemSubjectId = record.getId();
                String enterpriseSubjectId = pm.getEnterpriseSubjectId(systemSubjectId);
                if (!enterpriseSubjectIds.contains(enterpriseSubjectId)) {
                    enterpriseSubjectIds.add(enterpriseSubjectId);
                    Subject enterpriseSubject = pm.loadEnterpriseSubject(enterpriseSubjectId);
                    enterpriseSubject.setMatchConfidencePercentage(matchConfidencePercentage);
                    this.filterSubjectIdentifiers(subjectSearchCriteria, enterpriseSubject, null);
                    if (enterpriseSubject.hasSubjectIdentifiers()) {
                        subjectMatches.add(enterpriseSubject);
                    }
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("... not within specified minimum degree match percentage = " + minimumDegreeMatchPercentage);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (logger.isTraceEnabled()) {
            logger.trace("FindSubjectsHandler.findSubjectMatches.loadSubjects: elapedTimeMillis=" + (endTime - startTime));
        }
        subjectSearchResponse.getSubjects().addAll(subjectMatches);
        return subjectSearchResponse;
    }

    /**
     *
     * @param subjectSearchCriteria
     * @return
     * @throws EMPIException
     */
    private SubjectSearchResponse loadSubjectByIdentifier(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        SubjectSearchResponse subjectSearchResponse = new SubjectSearchResponse();
        Subject searchSubject = subjectSearchCriteria.getSubject();
        List<SubjectIdentifier> searchSubjectIdentifiers = searchSubject.getSubjectIdentifiers();
        if (!searchSubjectIdentifiers.isEmpty()) {
            SubjectIdentifier searchSubjectIdentifier = searchSubjectIdentifiers.get(0);
            Subject baseSubject = pm.loadBaseSubjectByIdentifier(searchSubjectIdentifier);
            if (baseSubject != null) {
                Subject enterpriseSubject = this.loadEnterpriseSubject(baseSubject, true);
                this.filterSubjectIdentifiers(subjectSearchCriteria, enterpriseSubject, null);
                enterpriseSubject.setMatchConfidencePercentage(100);
                subjectSearchResponse.getSubjects().add(enterpriseSubject);
            }
        }
        return subjectSearchResponse;
    }

    /**
     * 
     * @param subjectSearchCriteria
     * @return
     * @throws EMPIException
     */
    private SubjectSearchResponse loadIdentifiersForSubjectByIdentifier(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        SubjectSearchResponse subjectSearchResponse = new SubjectSearchResponse();
        Subject searchSubject = subjectSearchCriteria.getSubject();
        List<SubjectIdentifier> searchSubjectIdentifiers = searchSubject.getSubjectIdentifiers();
        if (!searchSubjectIdentifiers.isEmpty()) {
            SubjectIdentifier searchSubjectIdentifier = searchSubjectIdentifiers.get(0);
            Subject baseSubject = pm.loadBaseSubjectByIdentifier(searchSubjectIdentifier);
            if (baseSubject == null) {
                throw new EMPIException(searchSubjectIdentifier.getCXFormatted() + " is not a known identifier", EMPIException.ERROR_CODE_UNKNOWN_KEY_IDENTIFIER);
            }
            Subject enterpriseSubject = this.loadEnterpriseSubject(baseSubject, false);
            this.filterSubjectIdentifiers(subjectSearchCriteria, enterpriseSubject, searchSubjectIdentifier);
            if (enterpriseSubject.hasSubjectIdentifiers()) {
                enterpriseSubject.setMatchConfidencePercentage(100);
                subjectSearchResponse.getSubjects().add(enterpriseSubject);
            }
        }
        return subjectSearchResponse;
    }

    /**
     *
     * @param baseSubject
     * @param loadFullSubject
     * @return
     * @throws EMPIException
     */
    private Subject loadEnterpriseSubject(Subject baseSubject, boolean loadFullSubject) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        String enterpriseSubjectId = null;
        if (baseSubject.getType().equals(Subject.SubjectType.ENTERPRISE)) {
            enterpriseSubjectId = baseSubject.getInternalId();
        } else {
            enterpriseSubjectId = pm.getEnterpriseSubjectId(baseSubject);
        }
        Subject enterpriseSubject = null;
        if (loadFullSubject) {
            enterpriseSubject = pm.loadEnterpriseSubject(enterpriseSubjectId);
        } else {
            enterpriseSubject = pm.loadEnterpriseSubjectIdentifiersOnly(enterpriseSubjectId);
        }
        return enterpriseSubject;
    }

    /**
     *
     * @param subjectSearchCriteria
     * @throws EMPIException
     */
    private void validateSubjectIdentifierDomains(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        this.validateSubjectIdentifierDomains(subjectSearchCriteria.getSubject());
        this.validateScopingAssigningAuthorities(subjectSearchCriteria);
    }

    /**
     *
     * @param subjectSearchCriteria
     * @throws EMPIException
     */
    private void validateScopingAssigningAuthorities(SubjectSearchCriteria subjectSearchCriteria) throws EMPIException {
        PersistenceManager pm = this.getPersistenceManager();
        for (SubjectIdentifierDomain scopingIdentifierDomain : subjectSearchCriteria.getScopingAssigningAuthorities()) {
            boolean subjectIdentifierDomainExists = pm.doesSubjectIdentifierDomainExist(scopingIdentifierDomain);
            if (!subjectIdentifierDomainExists) {
                throw new EMPIException(scopingIdentifierDomain.getUniversalId() + " is not a known identifier domain", EMPIException.ERROR_CODE_UNKNOWN_KEY_IDENTIFIER);
            }
        }
    }

    /**
     *
     * @param subjectSearchCriteria
     * @param subject
     * @param subjectIdentifierToRemove
     */
    private void filterSubjectIdentifiers(SubjectSearchCriteria subjectSearchCriteria, Subject subject, SubjectIdentifier subjectIdentifierToRemove) {
        if (subjectIdentifierToRemove != null) {
            subject.removeSubjectIdentifier(subjectIdentifierToRemove);
        }
        if (subjectSearchCriteria.hasScopingAssigningAuthorities()) {
            List<SubjectIdentifier> subjectIdentifiers = subject.getSubjectIdentifiers();
            List<SubjectIdentifier> copyOfSubjectIdentifiers = new ArrayList<SubjectIdentifier>();
            copyOfSubjectIdentifiers.addAll(subjectIdentifiers);
            for (SubjectIdentifier subjectIdentifier : copyOfSubjectIdentifiers) {
                if (!this.shouldKeepSubjectIdentifier(subjectSearchCriteria, subjectIdentifier)) {
                    subjectIdentifiers.remove(subjectIdentifier);
                }
            }
        }
    }

    /**
     *
     * @param subjectSearchCriteria
     * @param subjectIdentifier
     * @return
     */
    private boolean shouldKeepSubjectIdentifier(SubjectSearchCriteria subjectSearchCriteria, SubjectIdentifier subjectIdentifier) {
        SubjectIdentifierDomain subjectIdentifierDomain = subjectIdentifier.getIdentifierDomain();
        boolean shouldKeepSubjectIdentifier = false;
        for (SubjectIdentifierDomain scopingIdentifierDomain : subjectSearchCriteria.getScopingAssigningAuthorities()) {
            if (subjectIdentifierDomain.equals(scopingIdentifierDomain)) {
                shouldKeepSubjectIdentifier = true;
                break;
            }
        }
        return shouldKeepSubjectIdentifier;
    }
}
