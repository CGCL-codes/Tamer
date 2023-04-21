package org.swiftgantt.swing.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.swiftgantt.core.GanttContext;
import org.swiftgantt.core.layout.LayoutTask;
import org.swiftgantt.swing.SwingUtils;
import com.yxwang.common.event.BaseEventListener;

/**
 * TODO Handle mouse events for {@ChartView}
 * @author Yuxing Wang
 * @since 0.5.0
 */
public class ChartViewMouseListener extends BaseEventListener implements MouseListener {

    protected GanttContext context;

    public ChartViewMouseListener(GanttContext context) {
        this.context = context;
    }

    /**
	 * Handle mouse click event to select, deselect tasks on the chart view.
	 *
	 * @param e
	 */
    public void mouseClicked(MouseEvent e) {
        log.debug("mouseClicked at: " + e.getX() + ", " + e.getY());
        LayoutTask newSelected = context.getTaskLocationManager().getActiveTask(SwingUtils.swingPointToGanttPoint(e.getPoint()));
        int[] oldSelectionIndex = context.getGanttModel().getSelectedIds();
        log.debug("Old Selection IDs:" + Arrays.toString(oldSelectionIndex));
        log.debug("New selection ID:" + newSelected == null ? "null" : newSelected.getId());
        if (ArrayUtils.isEmpty(oldSelectionIndex) && newSelected == null) {
            log.debug("no old selected，no new selected.");
            return;
        }
        if (oldSelectionIndex != null && oldSelectionIndex.length == 1 && newSelected != null && newSelected.getId() == oldSelectionIndex[0]) {
            log.debug("new selected is one of the old selected tasks.");
            return;
        }
        boolean isNeedSelect = false;
        ChartView chartView = (ChartView) e.getSource();
        if (ArrayUtils.isEmpty(oldSelectionIndex) && newSelected != null) {
            log.debug("no old selected, new selected one.");
            isNeedSelect = true;
        } else if (oldSelectionIndex != null && oldSelectionIndex.length == 1 && newSelected != null && newSelected.getId() != oldSelectionIndex[0]) {
            log.debug("one old selected, new selected another one.");
            isNeedSelect = true;
        } else if (oldSelectionIndex != null && oldSelectionIndex.length > 1 && newSelected != null) {
            log.debug("old selected more than one, new selected any one.");
            isNeedSelect = true;
        } else {
            log.debug("Unknow selection.");
        }
        if (isNeedSelect == true) {
            context.getGanttModel().setSelectedIds(new int[] { newSelected.getId() });
            chartView.fireSelectionChange(e.getSource(), newSelected);
            return;
        }
        if (oldSelectionIndex != null && oldSelectionIndex.length >= 1 && newSelected == null) {
            log.debug("old selected more than one, no new selected.");
            context.getGanttModel().setSelectedIds(null);
            chartView.fireSelectionChange(e.getSource(), newSelected);
            return;
        }
        log.debug("Unkown selection");
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
