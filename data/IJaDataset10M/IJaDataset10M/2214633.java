package org.libreplan.web.orders;

import java.util.List;
import org.libreplan.business.orders.entities.OrderElement;
import org.libreplan.business.reports.dtos.WorkReportLineDTO;
import org.libreplan.business.workingday.EffortDuration;

/**
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
public interface IAssignedHoursToOrderElementModel {

    public List<WorkReportLineDTO> getWorkReportLines();

    public EffortDuration getTotalAssignedEffort();

    public EffortDuration getAssignedDirectEffortChildren();

    public void initOrderElement(OrderElement orderElement);

    public EffortDuration getEstimatedEffort();

    public int getProgressWork();

    public EffortDuration getAssignedDirectEffort();
}
