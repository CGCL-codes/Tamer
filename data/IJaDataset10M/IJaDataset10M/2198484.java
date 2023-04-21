package org.openimmunizationsoftware.dqa.db.model;

import java.util.Date;

public class BatchReport {

    private static Date max(Date d1, Date d2) {
        if (d1 == null) {
            return d2;
        } else if (d2 == null) {
            return d1;
        } else if (d1.after(d2)) {
            return d1;
        }
        return d2;
    }

    private static Date min(Date d1, Date d2) {
        if (d1 == null) {
            return d2;
        } else if (d2 == null) {
            return d1;
        } else if (d1.before(d2)) {
            return d1;
        }
        return d2;
    }

    private int batchReportId = 0;

    private int completenessPatientScore = 0;

    private int completenessScore = 0;

    private int completenessVaccinationScore = 0;

    private int completenessVaccineGroupScore = 0;

    private MessageBatch messageBatch = null;

    private int messageCount = 0;

    private int messageWithAdminCount = 0;

    private int nextOfKinCount = 0;

    private int overallScore = 0;

    private int patientCount = 0;

    private int patientUnderageCount = 0;

    private int qualityErrorScore = 0;

    private int qualityScore = 0;

    private int qualityWarnScore = 0;

    private double timelinessAverage = 0.0;

    private int timelinessCountEarly = 0;

    private int timelinessCountLate = 0;

    private int timelinessCountOldData = 0;

    private int timelinessCountOnTime = 0;

    private int timelinessCountVeryLate = 0;

    private Date timelinessDateFirst = null;

    private Date timelinessDateLast = null;

    private int timelinessScore = 0;

    private int timelinessScoreEarly = 0;

    private int timelinessScoreLate = 0;

    private int timelinessScoreOnTime = 0;

    private int timelinessScoreVeryLate = 0;

    private int vaccinationAdministeredCount = 0;

    private int vaccinationDeleteCount = 0;

    private int vaccinationHistoricalCount = 0;

    private int vaccinationNotAdministeredCount = 0;

    public void addToCounts(BatchReport report) {
        messageCount += report.getMessageCount();
        messageWithAdminCount += report.getMessageWithAdminCount();
        nextOfKinCount += report.getNextOfKinCount();
        patientCount += report.getPatientCount();
        patientUnderageCount += report.getPatientUnderageCount();
        double a1 = this.getMessageWithAdminCount();
        double a2 = report.getMessageWithAdminCount();
        double s1 = this.getTimelinessAverage();
        double s2 = report.getTimelinessAverage();
        if ((a1 + a2) == 0) {
            timelinessAverage = 0.0;
        } else {
            timelinessAverage = (a1 * s1 + s2 * a2) / (a1 + a2);
        }
        timelinessCountEarly += report.getTimelinessCountEarly();
        timelinessCountOnTime += report.getTimelinessCountOnTime();
        timelinessCountLate += report.getTimelinessCountLate();
        timelinessCountVeryLate += report.getTimelinessCountVeryLate();
        timelinessCountOldData += report.getTimelinessCountOldData();
        timelinessDateFirst = min(timelinessDateFirst, report.getTimelinessDateFirst());
        timelinessDateLast = max(timelinessDateLast, report.getTimelinessDateLast());
        vaccinationAdministeredCount += report.getVaccinationAdministeredCount();
        vaccinationDeleteCount += report.getVaccinationDeleteCount();
        vaccinationHistoricalCount += report.getVaccinationHistoricalCount();
        vaccinationNotAdministeredCount += report.getVaccinationNotAdministeredCount();
    }

    public int getBatchReportId() {
        return batchReportId;
    }

    public int getCompletenessPatientScore() {
        return completenessPatientScore;
    }

    public int getCompletenessScore() {
        return completenessScore;
    }

    public int getCompletenessVaccinationScore() {
        return completenessVaccinationScore;
    }

    public int getCompletenessVaccineGroupScore() {
        return completenessVaccineGroupScore;
    }

    public MessageBatch getMessageBatch() {
        return messageBatch;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public int getMessageWithAdminCount() {
        return messageWithAdminCount;
    }

    public int getNextOfKinCount() {
        return nextOfKinCount;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public int getPatientUnderageCount() {
        return patientUnderageCount;
    }

    public int getQualityErrorScore() {
        return qualityErrorScore;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public int getQualityWarnScore() {
        return qualityWarnScore;
    }

    public double getTimelinessAverage() {
        return timelinessAverage;
    }

    public int getTimelinessCountEarly() {
        return timelinessCountEarly;
    }

    public int getTimelinessCountLate() {
        return timelinessCountLate;
    }

    public int getTimelinessCountOldData() {
        return timelinessCountOldData;
    }

    public int getTimelinessCountOnTime() {
        return timelinessCountOnTime;
    }

    public int getTimelinessCountVeryLate() {
        return timelinessCountVeryLate;
    }

    public Date getTimelinessDateFirst() {
        return timelinessDateFirst;
    }

    public Date getTimelinessDateLast() {
        return timelinessDateLast;
    }

    public int getTimelinessScore() {
        return timelinessScore;
    }

    public int getTimelinessScoreEarly() {
        return timelinessScoreEarly;
    }

    public int getTimelinessScoreLate() {
        return timelinessScoreLate;
    }

    public int getTimelinessScoreOnTime() {
        return timelinessScoreOnTime;
    }

    public int getTimelinessScoreVeryLate() {
        return timelinessScoreVeryLate;
    }

    public int getVaccinationAdministeredCount() {
        return vaccinationAdministeredCount;
    }

    public int getVaccinationCount() {
        return vaccinationAdministeredCount + vaccinationHistoricalCount + vaccinationDeleteCount + vaccinationNotAdministeredCount;
    }

    public int getVaccinationDeleteCount() {
        return vaccinationDeleteCount;
    }

    public int getVaccinationHistoricalCount() {
        return vaccinationHistoricalCount;
    }

    public int getVaccinationNotAdministeredCount() {
        return vaccinationNotAdministeredCount;
    }

    public void incMessageCount() {
        this.messageCount++;
    }

    public void incMessageWithAdminCount() {
        this.messageWithAdminCount++;
    }

    public void incNextOfKinCount() {
        this.nextOfKinCount++;
    }

    public void incNextOfKinCount(int amount) {
        this.nextOfKinCount += amount;
    }

    public void incPatientCount() {
        this.patientCount++;
    }

    public void incPatientUnderageCount() {
        this.patientUnderageCount++;
    }

    public void incTimelinessCountEarly() {
        this.timelinessCountEarly++;
    }

    public void incTimelinessCountLate() {
        this.timelinessCountLate++;
    }

    public void incTimelinessCountOldData() {
        this.timelinessCountOldData++;
    }

    public void incTimelinessCountOnTime() {
        this.timelinessCountOnTime++;
    }

    public void incTimelinessCountVeryLate() {
        this.timelinessCountVeryLate++;
    }

    public void incVaccinationAdministeredCount() {
        this.vaccinationAdministeredCount++;
    }

    public void incVaccinationDeleteCount() {
        this.vaccinationDeleteCount++;
    }

    public void incVaccinationHistoricalCount() {
        vaccinationHistoricalCount++;
    }

    public void incVaccinationNotAdministeredCount() {
        this.vaccinationNotAdministeredCount++;
    }

    public void setBatchReportId(int reportId) {
        this.batchReportId = reportId;
    }

    public void setCompletenessPatientScore(int completenessPatientScore) {
        this.completenessPatientScore = completenessPatientScore;
    }

    public void setCompletenessScore(int completenessScore) {
        this.completenessScore = completenessScore;
    }

    public void setCompletenessVaccinationScore(int completenessVaccinationScore) {
        this.completenessVaccinationScore = completenessVaccinationScore;
    }

    public void setCompletenessVaccineGroupScore(int completenessVaccineGroupScore) {
        this.completenessVaccineGroupScore = completenessVaccineGroupScore;
    }

    public void setMessageBatch(MessageBatch messageBatch) {
        this.messageBatch = messageBatch;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setMessageWithAdminCount(int messageWithAdminCount) {
        this.messageWithAdminCount = messageWithAdminCount;
    }

    public void setNextOfKinCount(int nextOfKinCount) {
        this.nextOfKinCount = nextOfKinCount;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    public void setPatientUnderageCount(int patientUnderageCount) {
        this.patientUnderageCount = patientUnderageCount;
    }

    public void setQualityErrorScore(int qualityErrorScore) {
        this.qualityErrorScore = qualityErrorScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public void setQualityWarnScore(int qualityWarnScore) {
        this.qualityWarnScore = qualityWarnScore;
    }

    public void setTimelinessAverage(double timelinessAverage) {
        this.timelinessAverage = timelinessAverage;
    }

    public void setTimelinessCountEarly(int timelinessCountEarly) {
        this.timelinessCountEarly = timelinessCountEarly;
    }

    public void setTimelinessCountLate(int timelinessCountLate) {
        this.timelinessCountLate = timelinessCountLate;
    }

    public void setTimelinessCountOldData(int timelinessCountOldData) {
        this.timelinessCountOldData = timelinessCountOldData;
    }

    public void setTimelinessCountOnTime(int timelinessCountOnTime) {
        this.timelinessCountOnTime = timelinessCountOnTime;
    }

    public void setTimelinessCountVeryLate(int timelinessCountVeryLate) {
        this.timelinessCountVeryLate = timelinessCountVeryLate;
    }

    public void setTimelinessDateFirst(Date timelinessDateFirst) {
        this.timelinessDateFirst = timelinessDateFirst;
    }

    public void setTimelinessDateLast(Date timelinessDateLast) {
        this.timelinessDateLast = timelinessDateLast;
    }

    public void setTimelinessScore(int timelinessScore) {
        this.timelinessScore = timelinessScore;
    }

    public void setTimelinessScoreEarly(int timelinessScoreEarly) {
        this.timelinessScoreEarly = timelinessScoreEarly;
    }

    public void setTimelinessScoreLate(int timelinessScoreLate) {
        this.timelinessScoreLate = timelinessScoreLate;
    }

    public void setTimelinessScoreOnTime(int timelinessScoreOnTime) {
        this.timelinessScoreOnTime = timelinessScoreOnTime;
    }

    public void setTimelinessScoreVeryLate(int timelinessScoreVeryLate) {
        this.timelinessScoreVeryLate = timelinessScoreVeryLate;
    }

    public void setVaccinationAdministeredCount(int vaccinationAdministeredCount) {
        this.vaccinationAdministeredCount = vaccinationAdministeredCount;
    }

    public void setVaccinationDeleteCount(int vaccinationDeleteCount) {
        this.vaccinationDeleteCount = vaccinationDeleteCount;
    }

    public void setVaccinationHistoricalCount(int vaccinationHistoricalCount) {
        this.vaccinationHistoricalCount = vaccinationHistoricalCount;
    }

    public void setVaccinationNotAdministeredCount(int vaccinationNotAdministeredCount) {
        this.vaccinationNotAdministeredCount = vaccinationNotAdministeredCount;
    }
}
