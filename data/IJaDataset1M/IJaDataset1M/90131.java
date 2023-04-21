package edu.upmc.opi.caBIG.caTIES.server.dispatcher.search;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Sort;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_ExceptionLogger;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_FormatUtils;

/**
 * Class CaTIES_LuceneSearcher searches against the public datastore index in
 * response to a dispatcher service command. Commands are in SPIN query valid
 * XML and are parsed into the Lucene query language.
 * 
 * @author mitchellkj
 * 
 */
public class CaTIES_LuceneSortingStageCrawler extends CaTIES_LuceneStageCrawler {

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_LuceneSortingStageCrawler.class);

    private static final String STATE_QUERYING = "STATE_QUERYING";

    private static final String STATE_FINISHED = "STATE_FINISHED";

    private static final String STATE_INCREMENTING_FIRST_PATIENT = "STATE_INCREMENTING_FIRST_PATIENT";

    private static final String STATE_FINDING_PATIENT = "STATE_FINDING_PATIENT";

    private static final String STATE_PROCESSING_PATIENT = "STATE_PROCESSING_PATIENT";

    private static final int DIRECTION_BACKWARDS = 0;

    private static final int DIRECTION_FORWARDS = 1;

    private String currentState = STATE_QUERYING;

    private String goalPatientUuid = "UNDEFINED";

    private Long goalSequence = -1L;

    private Long currentSequence = -1L;

    private String lowerBound;

    private String upperBound;

    private int patientStartIdx = -1;

    private int patientCurrentIdx = -1;

    private Sort sortCriteria;

    private int cursorPosition = 0;

    public static void main(String[] args) {
        new CaTIES_LuceneSortingStageCrawler();
    }

    public CaTIES_LuceneSortingStageCrawler() {
    }

    public void process() {
        try {
            if (getCurrentState().equals(STATE_QUERYING)) {
                processStateQuerying();
            } else if (isFinished()) {
                logger.debug("FINISHED state has been reached. Exiting search.");
                this.crawlController.setCurrentCrawlerStage(null);
            } else if (this.currentState.equals(STATE_INCREMENTING_FIRST_PATIENT)) {
                processStateIncrementingFirstPatient();
            } else if (this.currentState.equals(STATE_FINDING_PATIENT)) {
                processStateFindingPatient();
            } else if (this.currentState.equals(STATE_PROCESSING_PATIENT)) {
                processStateProcessingPatient();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private String shouldHitStatus(String goalPatient) {
        final String[] shouldHits = { "b5b30c57-15a5-11dd-8af2-95bfc659d5b0", "a1e24801-1770-11dd-bef7-573de699ece7", "bc643482-177c-11dd-bef7-573de699ece7" };
        List<String> shouldHitsArray = Arrays.asList(shouldHits);
        String returnValue = " ";
        if (shouldHitsArray.contains(goalPatient)) {
            returnValue = "*";
        }
        return returnValue;
    }

    private void displayStateDiagnostics(String directionalPrefix) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\n" + directionalPrefix);
        sb.append("\nSTAGE #" + this.stageNumber + " " + shouldHitStatus(this.goalPatientUuid));
        sb.append("\n\t\tSTATE: " + this.currentState);
        if (this.currentSequence < this.goalSequence) {
            sb.append("\n\t\tLESS THAN goal sequence:");
        } else if (this.currentSequence > this.goalSequence) {
            sb.append("\n\t\tGREATER THAN goal sequence:");
        } else {
            sb.append("\n\t\tEQUAL sequences:");
        }
        sb.append("\n\t\t\tgs: " + this.goalSequence);
        sb.append("\n\t\t\tcs: " + this.currentSequence);
        String currentPatientUuid = (this.patientUuid == null) ? "null" : this.patientUuid;
        if (this.goalPatientUuid.compareTo(currentPatientUuid) == 0) {
            sb.append("\n\t\tEQUAL patients:");
        } else {
            sb.append("\n\t\tUN EQUAL patients:");
        }
        sb.append("\n\t\t\tgp: " + this.goalPatientUuid);
        sb.append("\n\t\t\tcp: " + currentPatientUuid);
        sb.append("\n\t\tCurrent Report UUID: " + this.DocumentUuid);
        sb.append("\n\t\tCollection Date and Time As String: " + this.collectionDateAndTimeAsString);
        sb.append("\n\n");
        logger.debug(sb.toString());
    }

    protected void processStateQuerying() {
        buildQuery();
        processQuery();
        if (hasSuccessor()) {
            this.crawlController.setCurrentCrawlerStage(this.next);
        } else {
            CaTIES_LuceneSortingStageCrawler first = (CaTIES_LuceneSortingStageCrawler) this.crawlController.getFirstCrawlerStage();
            first.setCurrentState(STATE_INCREMENTING_FIRST_PATIENT);
            this.crawlController.setCurrentCrawlerStage(first);
        }
    }

    protected void displayQueryResults() {
        try {
            logger.debug("Results of query:");
            for (Iterator iterator = this.hits.iterator(); iterator.hasNext(); ) {
                Hit h = (Hit) iterator.next();
                Document d = h.getDocument();
                String sequence = d.get("sequence");
                String patientUuid = d.get("patientUuid");
                String DocumentUuid = d.get("uuid");
                String collectionDateTime = d.get("collectionDateTime");
                logger.debug(sequence + ", " + patientUuid + ", " + DocumentUuid + ", " + collectionDateTime);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    protected boolean checkIsSorted() {
        boolean isSortedCollection = true;
        try {
            Document predecessor = hits.doc(0);
            for (int hdx = 1; hdx < hits.length(); hdx++) {
                if (hdx % 10000 == 0) {
                    logger.info("Checked " + hdx + " documents.");
                }
                String sequencePred = predecessor.getField("sequence").stringValue();
                String patientUuidPred = predecessor.getField("patientUuid").stringValue();
                String DocumentUuidPred = predecessor.getField("uuid").stringValue();
                String collectionDateTimePred = predecessor.getField("collectionDateTime").stringValue();
                Document successor = hits.doc(hdx);
                String sequenceSucc = successor.getField("sequence").stringValue();
                String patientUuidSucc = successor.getField("patientUuid").stringValue();
                String DocumentUuidSucc = successor.getField("uuid").stringValue();
                String collectionDateTimeSucc = successor.getField("collectionDateTime").stringValue();
                if (patientUuidSucc.compareTo(patientUuidPred) < 0 || (patientUuidSucc.compareTo(patientUuidPred) == 0 && collectionDateTimeSucc.compareTo(collectionDateTimePred) < 0)) {
                    isSortedCollection = false;
                    logger.fatal("Invariant is broken at " + generateDiagnostic("Predeccessor", sequencePred, patientUuidPred, DocumentUuidPred, collectionDateTimePred) + "\n" + generateDiagnostic("Successor", sequenceSucc, patientUuidSucc, DocumentUuidSucc, collectionDateTimeSucc) + "\n");
                    break;
                }
                patientUuidPred = patientUuidSucc;
                collectionDateTimePred = collectionDateTimeSucc;
            }
            if (isSortedCollection) {
                logger.info("Collection is sorted.");
            } else {
                logger.fatal("Collection is unsorted");
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSortedCollection;
    }

    private String generateDiagnostic(String prefixLabel, String sequence, String patientUuid, String DocumentUuid, String collectionDateTime) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        sb.append(prefixLabel + "_sequence ==> " + sequence + "\n");
        sb.append(prefixLabel + "_patientUuid ==> " + patientUuid + "\n");
        sb.append(prefixLabel + "_DocumentUuid ==> " + DocumentUuid + "\n");
        sb.append(prefixLabel + "_collectionDateTime ==> " + collectionDateTime + "\n");
        result = sb.toString();
        return result;
    }

    protected void processStateIncrementingFirstPatient() throws IOException, ParseException {
        this.nextHit++;
        if (isFirstStage() && hasNonNullGoalPatient() && hasHitsToBeProcessed()) {
            cacheHit(this.nextHit);
            if (!getPatientUuid().equals("NOT_IN_INDEX") && !getPatientUuid().equals(getGoalPatientUuid())) {
                setGoalPatientUuid(getPatientUuid());
                setGoalSequence(getCurrentSequence());
                this.patientStartIdx = this.nextHit;
                this.patientCurrentIdx = this.nextHit - 1;
                if (hasSuccessor()) {
                    propogateState(STATE_FINDING_PATIENT, getGoalPatientUuid(), getGoalSequence(), DIRECTION_FORWARDS);
                    this.crawlController.setCurrentCrawlerStage(this.next);
                } else {
                    setCurrentState(STATE_PROCESSING_PATIENT);
                    this.crawlController.setCurrentCrawlerStage(this);
                }
            }
        }
    }

    protected void processStateFindingPatient() throws IOException, ParseException {
        if (this.nextHit < 0) {
            this.nextHit++;
        }
        if (hasNoHitsToBeProcessed()) {
            moveToStartWithState(STATE_INCREMENTING_FIRST_PATIENT);
        } else {
            cacheHit(this.nextHit);
            if (isLessThanGoalPatient()) {
                this.nextHit++;
            } else if (isGreaterThanGoalPatient()) {
                moveToStartWithState(STATE_INCREMENTING_FIRST_PATIENT);
            } else {
                this.patientStartIdx = this.nextHit;
                this.patientCurrentIdx = this.nextHit - 1;
                if (hasSuccessor()) {
                    this.crawlController.setCurrentCrawlerStage(this.next);
                } else {
                    propogateState(STATE_PROCESSING_PATIENT, getGoalPatientUuid(), getGoalSequence(), DIRECTION_BACKWARDS);
                    moveToStartWithState(STATE_PROCESSING_PATIENT);
                }
            }
        }
    }

    protected void processStateFindingPatientBinary() throws IOException, ParseException {
        if (this.nextHit < 0) {
            this.nextHit++;
        }
        if (hasNoHitsToBeProcessed()) {
            moveToStartWithState(STATE_FINISHED);
        } else {
            int insertionPoint = findInsertionPoint(this.nextHit, getNumberOfHits() - 1);
            if (isGoalPatientAtInsertionPoint(insertionPoint)) {
                insertionPoint = adjustToStartOfGoalPatient(insertionPoint);
                this.patientStartIdx = insertionPoint + 1;
                this.patientCurrentIdx = insertionPoint;
                this.nextHit = insertionPoint;
                if (hasSuccessor()) {
                    this.crawlController.setCurrentCrawlerStage(this.next);
                } else {
                    propogateState(STATE_PROCESSING_PATIENT, getGoalPatientUuid(), getGoalSequence(), DIRECTION_BACKWARDS);
                    moveToStartWithState(STATE_PROCESSING_PATIENT);
                }
            } else {
                updateGoalToPatientAtInsertionPoint(insertionPoint);
            }
        }
    }

    private void updateGoalToPatientAtInsertionPoint(int insertionPoint) {
        if (insertionPoint == getNumberOfHits()) {
            moveToStartWithState(STATE_FINISHED);
        } else {
            this.nextHit = insertionPoint;
            try {
                cacheHit(insertionPoint);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setGoalPatientUuid(getPatientUuid());
            propogateState(STATE_FINDING_PATIENT, getGoalPatientUuid(), getGoalSequence(), DIRECTION_BACKWARDS);
            propogateState(STATE_FINDING_PATIENT, getGoalPatientUuid(), getGoalSequence(), DIRECTION_FORWARDS);
            moveToStartWithState(STATE_FINDING_PATIENT);
        }
    }

    private boolean isGoalPatientAtInsertionPoint(int insertionPoint) {
        if (insertionPoint == getNumberOfHits()) {
            return isGoalPatientAt(getNumberOfHits() - 1);
        } else {
            return isGoalPatientAt(insertionPoint);
        }
    }

    private int adjustToStartOfGoalPatient(int insertionPoint) {
        while (insertionPoint >= this.nextHit && isGoalPatientAt(insertionPoint)) {
            insertionPoint--;
        }
        return insertionPoint;
    }

    private boolean isGoalPatientAt(int insertionPoint) {
        boolean result = false;
        try {
            this.cacheHit(insertionPoint);
            result = getGoalPatientUuid().equals(getPatientUuid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int findInsertionPoint(int lowerIndexBound, int upperIndexBound) {
        try {
            cacheHit(lowerIndexBound);
            if (isGreaterThanOrEqualToGoalPatient()) {
                return (lowerIndexBound);
            }
            cacheHit(upperIndexBound);
            if (isLessThanOrEqualToGoalPatient()) {
                return (upperIndexBound + 1);
            }
            int midPointIndex = (int) lowerIndexBound + ((upperIndexBound - lowerIndexBound) / 2);
            if (midPointIndex == lowerIndexBound || midPointIndex == upperIndexBound) {
                return upperIndexBound;
            }
            cacheHit(midPointIndex);
            if (isLessThanOrEqualToGoalPatient()) {
                return findInsertionPoint(midPointIndex, upperIndexBound);
            } else {
                return findInsertionPoint(lowerIndexBound, midPointIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lowerIndexBound - 1;
    }

    protected void processStateProcessingPatient() throws IOException, ParseException {
        this.patientCurrentIdx++;
        if (this.patientCurrentIdx < getNumberOfHits()) {
            cacheHit(this.patientCurrentIdx);
        } else {
            this.patientUuid = "UNDEFINED";
        }
        if (!getPatientUuid().equals(getGoalPatientUuid())) {
            if (isFirstStage()) {
                this.currentState = STATE_INCREMENTING_FIRST_PATIENT;
                this.crawlController.setCurrentCrawlerStage(this);
            } else {
                this.patientCurrentIdx = this.patientStartIdx - 1;
                this.crawlController.setCurrentCrawlerStage(previous);
            }
        } else {
            processCurrentHit();
        }
    }

    protected void cacheHit(int hitIdx) throws IOException, ParseException {
        this.document = getHitAt(hitIdx);
        this.DocumentUuid = this.document.get("uuid");
        setDocumentUuid(DocumentUuid);
        this.collectionDateAndTimeAsString = document.get("collectionDateTime");
        if (this.collectionDateAndTimeAsString != null) {
            Date collectionDateAndTime = CaTIES_FormatUtils.indexDateFormatter.parse(collectionDateAndTimeAsString);
            setEventTime(collectionDateAndTime);
        } else {
            setEventTime(null);
        }
        String patientUuid = document.get("patientUuid");
        if (patientUuid != null) {
            setPatientUuid(patientUuid);
        } else {
            logger.debug("Report " + DocumentUuid + " has no patient in the index. ");
            setPatientUuid("NOT_IN_INDEX");
        }
        String sequenceAsString = document.get("sequence");
        if (sequenceAsString != null) {
            Long sequence = new Long(sequenceAsString);
            setCurrentSequence(sequence);
        } else {
            logger.fatal("Report " + DocumentUuid + " has no sequence in the index. ");
            setCurrentSequence(-1L);
        }
    }

    protected void moveToStartWithState(String newStartState) {
        CaTIES_LuceneSortingStageCrawler first = (CaTIES_LuceneSortingStageCrawler) this.crawlController.getFirstCrawlerStage();
        first.setCurrentState(newStartState);
        this.crawlController.setCurrentCrawlerStage(first);
    }

    public void propogateState(String propogationState, String propogationPatient, Long propogationSequence, int direction) {
        this.currentState = propogationState;
        this.goalPatientUuid = propogationPatient;
        this.goalSequence = propogationSequence;
        switch(direction) {
            case DIRECTION_BACKWARDS:
                if (this.previous != null) {
                    this.previous.propogateState(propogationState, propogationPatient, propogationSequence, direction);
                }
                break;
            case DIRECTION_FORWARDS:
                if (this.next != null) {
                    this.next.propogateState(propogationState, propogationPatient, propogationSequence, direction);
                }
                break;
            default:
        }
    }

    protected void processCurrentHit() {
        boolean meetsPreviousConstraint = true;
        if (hasPredecessor()) {
            if (this.DocumentUuid.equals(previous.getDocumentUuid())) {
                meetsPreviousConstraint = false;
            } else {
                calculateTimeConstraints();
                if (this.collectionDateAndTimeAsString == null) {
                    meetsPreviousConstraint = false;
                } else {
                    if (this.collectionDateAndTimeAsString.compareTo(this.lowerBound) < 0) {
                        meetsPreviousConstraint = false;
                    } else if (this.collectionDateAndTimeAsString.compareTo(this.upperBound) > 0) {
                        meetsPreviousConstraint = false;
                    } else {
                        ;
                    }
                }
            }
        }
        if (meetsPreviousConstraint) {
            if (isLastStage()) {
                logger.debug("Temporal hit discovered for patient " + getGoalPatientUuid());
                this.crawlController.appendNextHitBatch();
                this.crawlController.setCurrentCrawlerStage(this);
            } else {
                this.crawlController.setCurrentCrawlerStage(this.next);
            }
        } else {
        }
    }

    protected void displayConstraintDiagnostic() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n\n");
        sb.append("Current Patient Uuid: " + this.patientUuid + "\n");
        sb.append("Previous Patient Uuid: " + this.previous.patientUuid + "\n");
        sb.append("Previous Pathology Report Uuid: " + this.previous.DocumentUuid + "\n");
        sb.append("Current Pathology Report Uuid: " + this.DocumentUuid + "\n");
        sb.append("Previous Collection Date Time: " + this.previous.collectionDateAndTimeAsString + "\n");
        sb.append("Current Collection Date Time: " + this.collectionDateAndTimeAsString + "\n");
        sb.append("Lowerbound: " + this.lowerBound + "\n");
        sb.append("CollectTime: " + this.collectionDateAndTimeAsString + "\n");
        sb.append("Upperbound: " + this.upperBound + "\n");
        logger.debug(sb.toString());
    }

    protected void calculateTimeConstraints() {
        Calendar lowerBoundCalendar = new GregorianCalendar();
        if (this.previous.getEventTime() != null) {
            lowerBoundCalendar.setTime(this.previous.getEventTime());
            if (this.temporalConstraintLowerBound != null) {
                lowerBoundCalendar.add(this.timeUnit, this.temporalConstraintLowerBound.intValue());
            } else {
                lowerBoundCalendar.add(Calendar.MILLISECOND, 1);
            }
            this.lowerBound = CaTIES_FormatUtils.indexDateFormatter.format(lowerBoundCalendar.getTime());
            if (this.temporalConstraintUpperBound != null) {
                Calendar upperBoundCalendar = new GregorianCalendar();
                upperBoundCalendar.setTime(this.previous.getEventTime());
                upperBoundCalendar.add(this.timeUnit, this.temporalConstraintUpperBound.intValue());
                this.upperBound = CaTIES_FormatUtils.indexDateFormatter.format(upperBoundCalendar.getTime());
            } else {
                this.upperBound = CaTIES_FormatUtils.maximumTimeString;
            }
        } else {
            this.lowerBound = this.upperBound = CaTIES_FormatUtils.maximumTimeString;
        }
    }

    /**
	 * Method buildQuery
	 * 
	 */
    public void buildQuery() {
        try {
            Date startTime = new Date();
            QueryParser queryParser = new QueryParser("conceptCodeSet", this.perFieldAnalyzer);
            this.luceneQueryObject = queryParser.parse(this.nonTemporalQuerySQL);
            logger.debug("Lucene query language from xml ==> \n" + this.nonTemporalQuerySQL);
            Date endTime = new Date();
            logger.debug("QueryParser.parse took " + (endTime.getTime() - startTime.getTime()) + " milliseconds");
        } catch (Exception e) {
            logger.fatal("ERROR Lucene can't read parsed query " + this.querySQL);
            e.printStackTrace();
            return;
        }
    }

    /**
	 * Method processQuery
	 * 
	 * @return String
	 */
    protected void processQuery() {
        this.nextHit = getCursorPosition() - 1;
        try {
            Date startTime = new Date();
            logger.debug(luceneQueryObject.toString());
            BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
            if (this.searcher == null) {
                logger.fatal("Searcher is null");
            } else if (getRandomCount() > 0) {
                this.hits = this.searcher.search(luceneQueryObject);
                randomizeHitAccess();
            } else if (this.sortCriteria != null) {
                this.hits = this.searcher.search(luceneQueryObject, this.sortCriteria);
            } else {
                this.hits = this.searcher.search(luceneQueryObject);
            }
            Date endTime = new Date();
            logger.debug("searcher.search " + (endTime.getTime() - startTime.getTime()) + " milliseconds");
            this.crawlController.setIterationIndexSearches(this.crawlController.getIterationIndexSearches() + 1);
        } catch (Exception e) {
            CaTIES_ExceptionLogger.logException(logger, e);
            logger.fatal("ERROR Lucene failed during a query " + this.querySQL);
            return;
        }
        int numberOfHits = (this.hits == null) ? 0 : this.hits.length();
        logger.debug("Lucene found " + numberOfHits + " hits for query \n" + this.querySQL);
    }

    protected boolean isFinished() {
        return getCurrentState().equals(STATE_FINISHED) || (this.previous == null && (this.hits == null || this.nextHit >= getNumberOfHits()));
    }

    protected boolean isFirstStage() {
        return (this.previous == null);
    }

    protected boolean isLastStage() {
        return (this.next == null);
    }

    protected boolean hasHitsToBeProcessed() {
        return (this.hits != null) && (this.nextHit < getNumberOfHits());
    }

    protected boolean hasNoHitsToBeProcessed() {
        return (this.hits == null) || (this.nextHit >= getNumberOfHits());
    }

    protected boolean hasNonNullGoalPatient() {
        return (this.goalPatientUuid != null);
    }

    protected boolean hasPredecessor() {
        return (this.previous != null);
    }

    protected boolean hasSuccessor() {
        return (this.next != null);
    }

    protected boolean isLessThanGoalPatientOriginal() {
        int result = getPatientUuid().compareTo(getGoalPatientUuid());
        boolean booleanResult = result < 0;
        return booleanResult;
    }

    protected boolean isGreaterThanGoalPatientOriginal() {
        int result = getPatientUuid().compareTo(getGoalPatientUuid());
        boolean booleanResult = result > 0;
        return booleanResult;
    }

    protected boolean isGreaterThanOrEqualToGoalPatientOriginal() {
        int patientComparisonResult = getPatientUuid().compareTo(getGoalPatientUuid());
        boolean booleanResult = patientComparisonResult >= 0;
        return booleanResult;
    }

    protected boolean isLessThanGoalPatient() {
        boolean booleanResult = false;
        if (!isEqualToGoalPatient()) {
            booleanResult = getCurrentSequence() < getGoalSequence();
        }
        return booleanResult;
    }

    protected boolean isGreaterThanGoalPatient() {
        boolean booleanResult = false;
        if (!isEqualToGoalPatient()) {
            booleanResult = getCurrentSequence() > getGoalSequence();
        }
        return booleanResult;
    }

    protected boolean isEqualToGoalPatient() {
        boolean booleanResult = false;
        int result = getPatientUuid().compareTo(getGoalPatientUuid());
        booleanResult = result == 0;
        return booleanResult;
    }

    protected boolean isLessThanOrEqualToGoalPatient() {
        boolean booleanResult = isEqualToGoalPatient() || isLessThanGoalPatient();
        return booleanResult;
    }

    protected boolean isGreaterThanOrEqualToGoalPatient() {
        boolean booleanResult = isEqualToGoalPatient() || isGreaterThanGoalPatient();
        return booleanResult;
    }

    public String getGoalPatientUuid() {
        return goalPatientUuid;
    }

    public void setGoalPatientUuid(String currentPatientUuid) {
        this.goalPatientUuid = currentPatientUuid;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Sort getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(Sort sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public Long getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(Long currentSequence) {
        this.currentSequence = currentSequence;
    }

    public Long getGoalSequence() {
        return goalSequence;
    }

    public void setGoalSequence(Long goalSequence) {
        this.goalSequence = goalSequence;
    }
}
