package org.efs.openreports.delivery;

import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.DeliveredReport;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;

public interface DeliveryMethod {

    public void deliverReport(ReportSchedule reportSchedule, ReportEngineOutput reportOutput) throws DeliveryException;

    public DeliveredReport[] getDeliveredReports(ReportUser user) throws DeliveryException;

    public byte[] getDeliveredReport(DeliveredReport deliveredReport) throws DeliveryException;
}
