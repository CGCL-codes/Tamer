package com.idna.dm.domain.reporting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DecisionMatrix")
public class DecisionLogReportRootElement {

    @XmlElementWrapper(name = "Decisions")
    @XmlElement(name = "Decision")
    private List<DecisionLogReport> decisionLogReports;

    public DecisionLogReportRootElement() {
        this.decisionLogReports = new LinkedList<DecisionLogReport>();
    }

    public void setDecisionLogReports(DecisionLogReport... decisionLogReports) {
        this.decisionLogReports.addAll(Arrays.asList(decisionLogReports));
    }

    public List<DecisionLogReport> getDecisionLogReports() {
        return decisionLogReports;
    }
}
