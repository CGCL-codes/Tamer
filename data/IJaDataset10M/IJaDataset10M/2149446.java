package org.eledge.pages;

import static org.eledge.Eledge.currentUser;
import static org.eledge.Eledge.dbcommit;
import java.util.HashMap;
import java.util.List;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.eledge.components.DeleteAssignmentListener;
import org.eledge.domain.ReportAssignment;
import org.eledge.domain.permissions.ManageReportPermission;
import org.eledge.domain.permissions.PermissionDeterminer;
import org.eledge.domain.permissions.PermissionRequired;

/**
 * @author robertz
 * 
 */
public abstract class ManageReport extends EledgeSecureBasePage implements PermissionRequired {

    public abstract ReportAssignment getCurrReport();

    public String getPermissionName() {
        return ManageReportPermission.NAME;
    }

    public List<?> getReportList() {
        return PermissionDeterminer.editFilter(ReportAssignment.lookupReportsForCourse(getCurrentCourse()), currentUser());
    }

    public void save(IRequestCycle cycle) {
        dbcommit(cycle);
    }

    public void add(IRequestCycle cycle) {
        ReportAssignment.createReport(getCurrentCourse());
        dbcommit(cycle);
    }

    public Integer getNumber() {
        return getCurrReport().getNumber();
    }

    public void setNumber(Integer i) {
        getCurrReport().setNumber(i);
    }

    public String getRtitle() {
        return getCurrReport().getTitle();
    }

    public void setRtitle(String t) {
        getCurrReport().setTitle(t);
    }

    public String getDescription() {
        return getCurrReport().getDescription();
    }

    public void setDescription(String d) {
        getCurrReport().setDescription(d);
    }

    public Integer getPointValue() {
        return getCurrReport().getPointValue();
    }

    public void setPointValue(Integer i) {
        getCurrReport().setPointValue(i);
    }

    public Integer getWeight() {
        return getCurrReport().getWeight();
    }

    public void setWeight(Integer i) {
        getCurrReport().setWeight(i);
    }

    public IActionListener getDelListener() {
        return new DeleteAssignmentListener();
    }

    public HashMap<String, ReportAssignment> getParamMap() {
        HashMap<String, ReportAssignment> map;
        map = new HashMap<String, ReportAssignment>();
        map.put("assignment", getCurrReport());
        return map;
    }
}
