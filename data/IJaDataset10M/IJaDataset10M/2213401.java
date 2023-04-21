package org.swiftgantt;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.swiftgantt.common.EventLogger;
import org.swiftgantt.common.SwingComImageGenerator;
import org.swiftgantt.common.Time;
import org.swiftgantt.event.SelectionChangeEvent;
import org.swiftgantt.event.SelectionChangeListener;
import org.swiftgantt.event.TimeUnitChangeEvent;
import org.swiftgantt.event.TimeUnitChangeListener;
import org.swiftgantt.model.GanttModel;
import org.swiftgantt.model.GanttModelChangeEvent;
import org.swiftgantt.model.GanttModelListener;
import org.swiftgantt.model.Task;
import org.swiftgantt.ui.TimeUnit;
import org.swiftgantt.ui.timeaxis.TimeAxisUtils;

/**
 * The {@link GanttChart} is a Swing component that represents a Gantt chart. To display the Gantt chart as what
 * you want, you should get the {@link Config} object from {@link GanttChart} instance, the config object
 * contains all the configuration for the {@link GanttChart}, change properties of it to config the gantt chart
 * displaying. To put data into <code>GanttChart</code>,initialize the {@link GanttModel} and set to the
 * {@link GanttChart}.
 * 
 * @see Config
 * @see TimeUnit
 * @see GanttModel
 * @author Yuxing Wang
 */
public class GanttChart extends JScrollPane implements GanttModelListener, PropertyChangeListener, SelectionChangeListener {

    private static final long serialVersionUID = 1L;

    private static Config config = new Config();

    private int layoutSuspendCount = 0;

    protected Logger logger = null;

    /** The time unit for Gantt chart, default is <code>TimeUnit.Day</code>. */
    private TimeUnit timeUnit = TimeUnit.defaultUnit;

    private GanttModel model = null;

    private boolean showTreeView = true;

    private ChartView ganttChartView = null;

    private TimeScaleView timeScaleView = null;

    private TaskTreeView taskTreeView = null;

    private JScrollBar treeViewScrollBar = null;

    private LogoView logoView = null;

    protected int totalSteps = 0;

    protected int totalScheduleSteps = 0;

    private int tasksCount = 0;

    public GanttChart() {
        logger = LogManager.getLogger(this.getClass());
        UIManager.put("TimeScaleUI", "org.swiftgantt.ui.TimeScaleUI");
        UIManager.put("TaskTreeUI", "org.swiftgantt.ui.TaskTreeUI");
        UIManager.put("GanttChartViewUI", "org.swiftgantt.ui.ChartViewUI");
        UIManager.put("LogoViewUI", "org.swiftgantt.ui.LogoViewUI");
        timeScaleView = new TimeScaleView(this);
        taskTreeView = new TaskTreeView(this);
        ganttChartView = new ChartView(this);
        treeViewScrollBar = new TaskTreeScrollBar(taskTreeView);
        treeViewScrollBar.setOrientation(JScrollBar.HORIZONTAL);
        logoView = new LogoView(this);
        config.addPropertyChangeListener(timeScaleView);
        config.addPropertyChangeListener(taskTreeView);
        config.addPropertyChangeListener(ganttChartView);
        config.addPropertyChangeListener(this);
        this.setColumnHeaderView(timeScaleView);
        this.setRowHeaderView(taskTreeView);
        this.setViewportView(ganttChartView);
        this.setCorner(JScrollPane.UPPER_LEFT_CORNER, new CornerLogoView());
        this.setCorner(JScrollPane.LOWER_LEFT_CORNER, treeViewScrollBar);
        this.addSelectionChangeListener(this);
        this.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                logger.warn("X: " + e.getValue());
                timeScaleView.setX(0 - e.getValue());
                ganttChartView.setX(0 - e.getValue());
            }
        });
        this.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                logger.warn("Y: " + e.getValue());
                ganttChartView.setY(0 - e.getValue());
            }
        });
    }

    /**
	 * Handle the event that model changed.
	 */
    public void ganttModelChanged(GanttModelChangeEvent e) {
        String log = "GanttModel property '" + e.getPropertyName() + "' has been changed: ";
        if (e.getNewValue() != null) {
            if (e.getNewValue() instanceof Time) {
                log += ((Time) e.getNewValue()).getTime();
            } else {
                log += e.getNewValue();
            }
        }
        EventLogger.event(e, log);
        if (layoutSuspendCount == 0) {
            performLayout();
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("ganttModelChanged(GanttModelChangeEvent) - int layoutSuspendCount=" + layoutSuspendCount);
            }
        }
    }

    protected void performLayout() {
        this.recalculateSteps();
        tasksCount = model.getTaskTreeModel().getTasksCount() + 2;
        this.taskTreeView.setTaskCount(tasksCount);
        this.ganttChartView.setTaskCount(tasksCount);
        this.timeScaleView.refreshDisplay();
        this.taskTreeView.refreshDisplay();
        this.ganttChartView.refreshDisplay();
    }

    protected void recalculateSteps() {
        this.calcTotalSteps(this.model.getKickoffTime(), this.model.getDeadline());
        this.timeScaleView.setTotalSteps(totalSteps);
        this.ganttChartView.setTotalSteps(totalSteps);
        this.timeScaleView.setTotalScheduleSteps(totalScheduleSteps);
        this.ganttChartView.setTotalScheduleSteps(totalScheduleSteps);
    }

    /**
	 * Handler the Config changes.
	 */
    public void propertyChange(PropertyChangeEvent evt) {
    }

    /**
	 * Generate image file from the view of <code>GanttChart</code>.
	 * 
	 * @param fileName Support *.png and *.jpg file only.
	 */
    public void generateImageFile(String fileName) throws IOException {
        if (fileName == null || fileName.length() == 0) {
            throw new IOException("No file name specified, please provide one");
        }
        File imgFile = new File(fileName);
        if (imgFile.isDirectory()) {
            throw new IOException("No file name specified, please provide one");
        }
        String ext = FilenameUtils.getExtension(fileName);
        ChartView gcView = this.ganttChartView;
        TimeScaleView tsView = this.timeScaleView;
        TaskTreeView ttView = this.taskTreeView;
        LogoView logoView = this.logoView;
        logger.debug("[Bounds] GanttChartView" + gcView.getBounds());
        logger.debug("[Bounds] TimeScaleView" + tsView.getBounds());
        logger.debug("[Bounds] Tas[Bounds]eView" + ttView.getBounds());
        logger.debug("[Bounds] logoView" + ttView.getBounds());
        Dimension mainDimension = new Dimension(gcView.getBounds().width + ttView.getBounds().width, tsView.getBounds().height + gcView.getBounds().height);
        Rectangle tsRect = new Rectangle(ttView.getBounds().width, 0, tsView.getBounds().width, tsView.getBounds().height);
        Rectangle ttRect = new Rectangle(0, tsView.getBounds().height, ttView.getBounds().width, ttView.getBounds().height);
        Rectangle gcRect = new Rectangle(ttView.getBounds().width, tsView.getBounds().height, gcView.getBounds().width, gcView.getBounds().height);
        Rectangle logoRect = new Rectangle(0, 0, ttRect.width, tsRect.height);
        logoView.setBounds(logoRect);
        logger.debug("[Rectangle] main bounds" + mainDimension);
        logger.debug("[Rectangle] GanttChartView" + tsRect);
        logger.debug("[Rectangle] TimeScaleView" + ttRect);
        logger.debug("[Rectangle] TaskTreeView" + gcRect);
        if (ext.equals("png")) {
            SwingComImageGenerator.getInstance().genPNGImage(mainDimension, new JComponent[] { gcView, tsView, ttView, logoView }, new Rectangle[] { gcRect, tsRect, ttRect, logoRect }, fileName);
        } else if (ext.equals("jpg")) {
            SwingComImageGenerator.getInstance().genJPEGImage(mainDimension, new JComponent[] { gcView, tsView, ttView, logoView }, new Rectangle[] { gcRect, tsRect, ttRect, logoRect }, fileName);
        } else {
            throw new IOException("Only .png and .jpeg format are supported");
        }
    }

    /**
	 * Add <code>TimeUnit</code> change listener.
	 * 
	 * @param l
	 */
    public void addTimeUnitChangeListener(TimeUnitChangeListener l) {
        super.listenerList.add(TimeUnitChangeListener.class, l);
    }

    /**
	 * Add selection change listener.
	 * 
	 * @param l
	 */
    public void addSelectionChangeListener(SelectionChangeListener l) {
        super.listenerList.add(SelectionChangeListener.class, l);
    }

    /**
	 * Get <code>TimeUnit</code> as singleton.
	 * 
	 * @return the timeUnit
	 */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
	 * Set the singleton <code>TimeUnit</code> of <code>GanttChart</code>
	 * 
	 * @param timeUnit the timeUnit to set
	 */
    public void setTimeUnit(TimeUnit timeUnit) {
        TimeUnit oldTime = this.timeUnit;
        this.timeUnit = timeUnit;
        this.fireTimeUnitChange(this, oldTime, this.timeUnit);
    }

    /**
	 * get <code>Config</code>
	 * 
	 * @return
	 */
    public static Config getStaticConfig() {
        return config;
    }

    /**
	 * get <code>Config</code>
	 * 
	 * @return
	 */
    public Config getConfig() {
        return config;
    }

    /**
	 * Get the <code>GanttModel</code> for Gantt chart.
	 * 
	 * @return the model
	 */
    public GanttModel getModel() {
        return model;
    }

    /**
	 * Set new <code>GanttModel</code> to the <code>GanttChart</code>.
	 * 
	 * @param ganttModel
	 */
    public void setModel(GanttModel ganttModel) {
        logger.debug("Set model to GanttChart");
        this.model = ganttModel;
        this.model.addGanttModelListener(this);
        logger.debug("Set time unit for TaskTreeModel");
        this.model.getTaskTreeModel().setTimeUnit(this.timeUnit);
        this.model.recalculate();
        this.calcTotalSteps(this.model.getKickoffTime(), this.model.getDeadline());
        tasksCount = model.getTaskTreeModel().getTasksCount() + 2;
        this.timeScaleView.setTotalSteps(totalSteps);
        this.ganttChartView.setTotalSteps(totalSteps);
        this.timeScaleView.setTotalScheduleSteps(totalScheduleSteps);
        this.ganttChartView.setTotalScheduleSteps(totalScheduleSteps);
        this.taskTreeView.setTaskCount(tasksCount);
        this.ganttChartView.setTaskCount(tasksCount);
        this.timeScaleView.refreshDisplay();
        this.taskTreeView.refreshDisplay();
        this.ganttChartView.refreshDisplay();
    }

    /**
	 * Set selected tasks.
	 * 
	 * @param task
	 * @since 0.3.4
	 */
    public void setSelectedTasks(Task... tasks) {
        int[] ids = new int[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            Task t = tasks[i];
            ids[i] = t.getId();
        }
        this.model.setSelectedIds(ids);
    }

    /**
	 * Set selected tasks.
	 * 
	 * @param tasks
	 * @since 0.3.4
	 */
    public void setSelectedTasks(List<Task> tasks) {
        int[] ids = new int[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            ids[i] = t.getId();
        }
        this.model.setSelectedIds(ids);
    }

    /**
	 * Calculate total steps for the Gantt chart, it is determined by the end time of latest task first, if dealine time
	 * is later than it, this is determined by the deadline time.
	 * 
	 * @param kickoffTime
	 * @param deadline
	 * @return
	 */
    protected void calcTotalSteps(Time kickoffTime, Time deadline) {
        int ret = 0;
        Time endTimeOfLatestTask = this.getModel().getTaskTreeModel().getEndTimeOfLatestTask();
        if (endTimeOfLatestTask != null && deadline.before(endTimeOfLatestTask)) {
            deadline = endTimeOfLatestTask;
            logger.info("The deadline adjusted to " + deadline.getTime() + " by latest task.");
        }
        totalScheduleSteps = TimeAxisUtils.getTimeIntervalByTimeUnit(this.getTimeUnit(), kickoffTime, deadline);
        totalSteps = totalScheduleSteps + (config == null ? Config.DEFAULT_BLANK_STEPS_TO_KICKOFF_TIME : config.getBlankStepsToKickoffTime());
        totalSteps += config == null ? Config.DEFAULT_BLANK_STEPS_TO_DEADLINE : config.getBlankStepsToDeadline();
        if (config.isFillInvalidArea() == true) {
            int vSteps = this.getViewportBorderBounds().width / config.getTimeUnitWidth() + 1;
            if (totalSteps < vSteps) {
                totalSteps = vSteps;
            }
        }
        logger.debug(ret + " steps that needs to be drawn from [" + kickoffTime.getTime() + "] to [" + deadline.getTime() + "]");
    }

    /**
	 * Invoke this method after <code>TimeUnit</code> of <code>GanttChart</code> has been changed.
	 * 
	 * @param source
	 * @param oldTimeUnit
	 * @param newTimeUnit
	 */
    public void fireTimeUnitChange(Object source, Object oldTimeUnit, Object newTimeUnit) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == TimeUnitChangeListener.class) {
                TimeUnitChangeEvent e = new TimeUnitChangeEvent(source, oldTimeUnit, newTimeUnit);
                ((TimeUnitChangeListener) listeners[i + 1]).timeUnitChanged(e);
            }
        }
    }

    /**
	 * 
	 * @param source
	 * @param selection
	 * @since 0.3.4
	 */
    public void fireSelectionChange(Object source, Task selection) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == SelectionChangeListener.class) {
                SelectionChangeEvent e = new SelectionChangeEvent(source, selection);
                ((SelectionChangeListener) listeners[i + 1]).selectionChanged(e);
            }
        }
    }

    /**
	 * Handle Selection Changed Event.
	 */
    public void selectionChanged(SelectionChangeEvent e) {
        this.ganttChartView.refreshDisplay();
    }

    /**
	 * Whether display task tree view.
	 * 
	 * @return
	 */
    public boolean isShowTreeView() {
        return showTreeView;
    }

    /**
	 * Set to display or hide the task tree view.
	 * 
	 * @param showTreeView
	 */
    public void setShowTreeView(boolean showTreeView) {
        this.showTreeView = showTreeView;
        if (showTreeView == true) {
            this.setRowHeaderView(this.taskTreeView);
        } else {
            this.setRowHeaderView(null);
        }
    }

    /**
	 * Suspend UI to avoid refresh.
	 * 
	 * @since 0.3.2
	 */
    public void suspendUI() {
        if (logger.isInfoEnabled()) {
            logger.info("suspgendUI() - Suspend UI - layoutSuspendCount=" + layoutSuspendCount);
        }
        layoutSuspendCount++;
    }

    /**
	 * Resume UI to refresh.
	 * 
	 * @since 0.3.2
	 */
    public void resumeUI() {
        if (logger.isInfoEnabled()) {
            logger.info("resumeUI() - Resume UI - layoutSuspendCount=" + layoutSuspendCount);
        }
        if (layoutSuspendCount > 0) {
            if (--layoutSuspendCount == 0) {
                performLayout();
            }
        } else {
            performLayout();
        }
    }
}
