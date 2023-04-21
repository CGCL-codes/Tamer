package org.libreplan.business.reports.dtos;

import java.util.Date;
import java.util.Set;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.libreplan.business.labels.entities.Label;
import org.libreplan.business.resources.entities.Resource;
import org.libreplan.business.workingday.EffortDuration;
import org.libreplan.business.workreports.entities.WorkReportLine;
import org.libreplan.business.workreports.valueobjects.DescriptionValue;

public class HoursWorkedPerResourceDTO implements Comparable {

    private String workerName;

    private Date date;

    private LocalTime clockStart;

    private LocalTime clockFinish;

    private EffortDuration effort;

    private String orderElementCode;

    private String orderElementName;

    private String descriptionValues;

    private String labels;

    private HoursWorkedPerResourceDTO self;

    public HoursWorkedPerResourceDTO(Resource resource, WorkReportLine workReportLine) {
        this.workerName = resource.getName();
        this.date = workReportLine.getDate();
        this.clockStart = workReportLine.getClockStart();
        this.clockFinish = workReportLine.getClockFinish();
        this.effort = workReportLine.getEffort();
        this.orderElementCode = workReportLine.getOrderElement().getCode();
        this.orderElementName = workReportLine.getOrderElement().getName();
        this.descriptionValues = descriptionValuesAsString(workReportLine.getDescriptionValues());
        Set<Label> labels = workReportLine.getLabels();
        if (workReportLine.getOrderElement() != null) {
            labels.addAll(workReportLine.getOrderElement().getLabels());
        }
        this.labels = labelsAsString(labels);
        this.self = this;
    }

    private String labelsAsString(Set<Label> labels) {
        String result = "";
        for (Label label : labels) {
            result = label.getType().getName() + ": " + label.getName() + ", ";
        }
        return (result.length() > 0) ? result.substring(0, result.length() - 2) : result;
    }

    private String descriptionValuesAsString(Set<DescriptionValue> descriptionValues) {
        String result = "";
        for (DescriptionValue descriptionValue : descriptionValues) {
            result = descriptionValue.getFieldName() + ": " + descriptionValue.getValue() + ", ";
        }
        return (result.length() > 0) ? result.substring(0, result.length() - 2) : result;
    }

    public EffortDuration getEffort() {
        return effort;
    }

    public void setEffort(EffortDuration effort) {
        this.effort = effort;
    }

    public LocalTime getClockStart() {
        return clockStart;
    }

    public void setClockStart(LocalTime clockStart) {
        this.clockStart = clockStart;
    }

    public LocalTime getClockFinish() {
        return clockFinish;
    }

    public void setClockFinish(LocalTime clockFinish) {
        this.clockFinish = clockFinish;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public Date getDate() {
        return LocalDate.fromDateFields(date).toDateTimeAtStartOfDay().toDate();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrderElementCode() {
        return orderElementCode;
    }

    public void setOrderElementCode(String orderElementCode) {
        this.orderElementCode = orderElementCode;
    }

    public String getOrderElementName() {
        return orderElementName;
    }

    public void setOrderElementName(String orderElementName) {
        this.orderElementName = orderElementName;
    }

    public String getDescriptionValues() {
        return descriptionValues;
    }

    public void setDescriptionValues(String descriptionValues) {
        this.descriptionValues = descriptionValues;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public int compareTo(Object o) {
        return this.workerName.compareTo(((HoursWorkedPerResourceDTO) o).workerName);
    }

    /**
     * @return the self
     */
    public HoursWorkedPerResourceDTO getSelf() {
        return self;
    }

    /**
     * @param self
     *            the self to set
     */
    public void setSelf(HoursWorkedPerResourceDTO self) {
        this.self = self;
    }
}
