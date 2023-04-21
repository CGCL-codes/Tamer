package shu.cms.devicemodel.lcd.util;

import shu.cms.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class ModelReport {

    public ModelReport(String context, DeltaEReport[] forwardReport, DeltaEReport[] reverseReport) {
        this.context = context;
        this.forwardReport = forwardReport;
        this.reverseReport = reverseReport;
    }

    public String context;

    public DeltaEReport[] forwardReport;

    public DeltaEReport[] reverseReport;

    public String toString() {
        return context;
    }
}
