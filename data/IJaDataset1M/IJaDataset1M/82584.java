package org.jcvi.vics.web.gwt.common.client.panel.user.jobs;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtwidgets.client.wrap.EffectOption;
import org.jcvi.vics.model.common.SortArgument;
import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.tasks.export.BlastResultExportTask;
import org.jcvi.vics.shared.export.ExportWriterConstants;
import org.jcvi.vics.shared.tasks.BlastJobInfo;
import org.jcvi.vics.shared.tasks.JobInfo;
import org.jcvi.vics.web.gwt.common.client.SystemWebTracker;
import org.jcvi.vics.web.gwt.common.client.effect.SafeEffect;
import org.jcvi.vics.web.gwt.common.client.jobs.AsyncExportTaskController;
import org.jcvi.vics.web.gwt.common.client.jobs.JobSelectionListener;
import org.jcvi.vics.web.gwt.common.client.jobs.JobStatusListener;
import org.jcvi.vics.web.gwt.common.client.popup.jobs.BlastJobParamHelper;
import org.jcvi.vics.web.gwt.common.client.popup.launcher.PopupCenteredLauncher;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.common.client.ui.imagebundles.ImageBundleFactory;
import org.jcvi.vics.web.gwt.common.client.ui.link.Link;
import org.jcvi.vics.web.gwt.common.client.ui.table.SortableColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.SortableTable;
import org.jcvi.vics.web.gwt.common.client.ui.table.TableCell;
import org.jcvi.vics.web.gwt.common.client.ui.table.TableRow;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.DateColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.ImageColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.NumericColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.TextColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.comparables.FormattedDateTime;
import org.jcvi.vics.web.gwt.common.client.ui.table.comparables.FulltextPopperUpperHTML;
import org.jcvi.vics.web.gwt.common.client.ui.table.comparables.PopperUpperHTML;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.AdvancedSortableRemotePaginatorClickListener;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.DataRetrievedListener;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.PagedDataRetriever;
import org.jcvi.vics.web.gwt.common.client.ui.table.paging.RemotePagingPanel;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;
import org.jcvi.vics.web.gwt.common.client.util.SystemProps;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.MenuBar;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.MenuBarWithRightAlignedDropdowns;
import org.jcvi.vics.web.gwt.common.google.user.client.ui.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Jul 28, 2008
 * Time: 11:38:01 AM
 */
public class ReversePsiBlastJobResultsPanel extends GeneralJobResultsPanel {

    private static Logger _logger = Logger.getLogger("org.jcvi.vics.web.gwt.common.client.panel.user.jobs.ReversePsiBlastJobResultsPanel");

    public static final String TASK_RPS_BLAST = "ReversePsiBlastTask";

    public static final boolean SHOW_BLAST_RESULTS_PAGE = SystemProps.getBoolean("BlastClient.ShowBlastResultsPage", false);

    private static final String NUM_HITS_HEADING = "# Hits";

    private static final String SUBJECT_HEADING = "Subject Sequences";

    private static final int DELETE_JOB_COLUMN = 0;

    private static final int JOB_NAME_COLUMN = 1;

    private static final int JOB_TIMESTAMP_COLUMN = 2;

    private static final int JOB_STATUS_COLUMN = 3;

    private static final int NUM_HITS_COLUMN = 4;

    private static final int SUBJECT_COLUMN = 5;

    private static final int ACTIONS_COLUMN = 6;

    public ReversePsiBlastJobResultsPanel(JobSelectionListener jobSelectionListener, JobSelectionListener reRunJobListener, String[] rowsPerPageOptions, int defaultRowsPerPage) {
        super(TASK_RPS_BLAST, jobSelectionListener, reRunJobListener, rowsPerPageOptions, defaultRowsPerPage, "ReversePsiBlastJobResults");
    }

    protected Widget createTable(String[] rowsPerPageOptions) {
        VerticalPanel tablePanel = new VerticalPanel();
        _table = new SortableTable();
        _table.addColumn(new ImageColumn("&nbsp;"));
        _table.addColumn(new TextColumn(JobStatusHelper.JOB_NAME_HEADING));
        _table.addColumn(new DateColumn(JobStatusHelper.JOB_TIMESTAMP_HEADING));
        _table.addColumn(new TextColumn(JobStatusHelper.JOB_STATUS_HEADING));
        _table.addColumn(new NumericColumn(NUM_HITS_HEADING, "Number of matching sequences"));
        _table.addColumn(new TextColumn(SUBJECT_HEADING));
        _table.addColumn(new TextColumn(ACTIONS_HEADING, false));
        String[][] sortConstants = new String[][] { { "", "" }, { JobInfo.SORT_BY_JOB_NAME, JobStatusHelper.JOB_NAME_HEADING }, { JobInfo.SORT_BY_SUBMITTED, JobStatusHelper.JOB_TIMESTAMP_HEADING }, { JobInfo.SORT_BY_STATUS, JobStatusHelper.JOB_STATUS_HEADING }, { BlastJobInfo.SORT_BY_NUM_HITS, NUM_HITS_HEADING }, { BlastJobInfo.SORT_BY_SUBJECT_DB, SUBJECT_HEADING } };
        JobStatusPaginator dataPaginator = new JobStatusPaginator(_table, new JobDataRetriever(), sortConstants);
        _pagingPanel = new RemotePagingPanel(_table, rowsPerPageOptions, false, true, dataPaginator, true, 0, _defaultRowsPerPage, "BlastJobResults");
        _table.setDefaultSortColumns(new SortableColumn[] { new SortableColumn(JOB_TIMESTAMP_COLUMN, JobStatusHelper.JOB_TIMESTAMP_HEADING, SortableColumn.SORT_DESC) });
        _pagingPanel.setNoDataMessage("No job results.");
        _pagingPanel.addAdvancedSortClickListener(new AdvancedSortableRemotePaginatorClickListener(_table, dataPaginator, _pagingPanel));
        tablePanel.add(_pagingPanel);
        return _pagingPanel;
    }

    public void showDeletionColumn(boolean showColumn) {
        _table.getCol(0).setVisible(showColumn);
    }

    public void showActionColumn(boolean showColumn) {
        _table.getCol(6).setVisible(showColumn);
    }

    /**
     * This callback is invoked by the paging panel when the user changes pages.  It retrieves one page of jobs
     * from the server, creates a table model (List of TableRow) and gives it to the paginator to update the table
     */
    public class JobDataRetriever implements PagedDataRetriever {

        public void retrieveTotalNumberOfDataRows(final DataRetrievedListener listener) {
            _statusservice.getNumTaskResultsForUser(TASK_RPS_BLAST, new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    _logger.error("JobDataRetriever.getNumTaskResultsForUser().onFailure(): ", caught);
                    listener.onFailure(caught);
                }

                public void onSuccess(Object result) {
                    _logger.debug("JobDataRetriever.getNumTaskResultsForUser().onSuccess() got " + result);
                    listener.onSuccess(result);
                }
            });
        }

        public void retrieveDataRows(int startIndex, int numRows, SortArgument[] sortArgs, final DataRetrievedListener listener) {
            _statusservice.getPagedBlastTaskResultsForUser(TASK_RPS_BLAST, startIndex, numRows, sortArgs, new AsyncCallback() {

                public void onFailure(Throwable caught) {
                    _logger.error("JobDataRetriever.retrieveDataRows().onFailure(): ", caught);
                    listener.onFailure(caught);
                }

                public void onSuccess(Object result) {
                    BlastJobInfo[] jobs = (BlastJobInfo[]) result;
                    if (jobs == null || jobs.length == 0) {
                        _logger.debug("JobDataRetriever.retrieveDataRows().onSuccess() got no data");
                        listener.onNoData();
                    } else {
                        _logger.debug("JobDataRetriever.retrieveDataRows().onSuccess() got data");
                        listener.onSuccess(formatData((BlastJobInfo[]) result));
                    }
                }
            });
        }

        private List formatData(BlastJobInfo[] jobs) {
            _logger.debug("JobDataRetriever processing " + jobs.length + " jobs");
            List<TableRow> tableRows = new ArrayList<TableRow>();
            for (final BlastJobInfo job : jobs) {
                if (job == null) continue;
                TableRow tableRow = new TableRow();
                tableRow.setValue(DELETE_JOB_COLUMN, new TableCell("&nbsp;", createRemoveJobWidget(job, tableRow)));
                tableRow.setValue(JOB_NAME_COLUMN, new TableCell(job.getJobname(), JobNameWidget.getWidget(job, tableRow, JOB_NAME_COLUMN, _pagingPanel)));
                TableCell dateCell;
                if (null != job.getSubmitted()) {
                    dateCell = new TableCell(new FormattedDateTime(job.getSubmitted().getTime()));
                } else {
                    dateCell = new TableCell("NA");
                }
                tableRow.setValue(JOB_TIMESTAMP_COLUMN, dateCell);
                tableRow.setValue(JOB_STATUS_COLUMN, new TableCell(job.getStatus(), _jobStatusHelper.createJobStatusWidget(job, ignoreStatusMessages())));
                tableRow.setValue(NUM_HITS_COLUMN, new TableCell(BlastNumHitsWidget.getNumHitsField(job), BlastNumHitsWidget.createNumHitsWidget(job, new ClickListener() {

                    public void onClick(Widget sender) {
                        _table.clearHover();
                        if (_jobSelectionListener != null) _jobSelectionListener.onSelect(job);
                    }
                })));
                TableCell subjSeqCell;
                if (job.getSubjectName() != null) {
                    subjSeqCell = new TableCell(job.getSubjectName(), new PopperUpperHTML(job.getSubjectName(), BlastJobParamHelper.getConcatenatedSubjectNames(job)));
                } else {
                    String tmpText = (null == job.getAllSubjectNames() || 0 == job.getAllSubjectNames().size()) ? "NA" : job.getAllSubjectNames().get(0);
                    subjSeqCell = new TableCell(tmpText, new FulltextPopperUpperHTML(tmpText, JobStatusHelper.DB_MAX_SIZE));
                }
                tableRow.setValue(SUBJECT_COLUMN, subjSeqCell);
                tableRow.setValue(ACTIONS_COLUMN, new TableCell("&nbsp;", getActionColumnWidget(job)));
                tableRows.add(tableRow);
                if (!Task.isDone(job.getStatus()) && _jobStatusHelper.isJobSubmittedSuccessfully(job)) {
                    final TableRow currentTableRow = tableRow;
                    _jobStatusHelper.startJobResultMonitor(job, new JobStatusListener() {

                        private String _previousStatus = null;

                        private Integer _previousPercentComplete = null;

                        public void onJobRunning(JobInfo newJobInfo) {
                            _logger.debug("Job " + newJobInfo.getJobId() + " still running...");
                            updateDisplay((BlastJobInfo) newJobInfo, false);
                        }

                        public void onJobFinished(JobInfo newJobInfo) {
                            _logger.info("Job " + newJobInfo.getJobId() + " completed, status = " + newJobInfo.getStatus());
                            _jobStatusHelper.removeJobResultMonitor(newJobInfo.getJobId());
                            updateDisplay((BlastJobInfo) newJobInfo, true);
                            notifyJobCompletedListener(newJobInfo);
                        }

                        public void onCommunicationError() {
                            _logger.error("Communication error retrieving status for job " + job.getJobId());
                        }

                        private void updateDisplay(BlastJobInfo newJobInfo, boolean isDone) {
                            if (newJobInfo == null) {
                                _logger.error("got invalid (null) task");
                                return;
                            }
                            if (!isDone && _previousStatus != null && _previousStatus.equals(newJobInfo.getStatus()) && _previousPercentComplete.equals(newJobInfo.getPercentComplete())) {
                                return;
                            }
                            _previousStatus = newJobInfo.getStatus();
                            _previousPercentComplete = newJobInfo.getPercentComplete();
                            TableCell statusCell = currentTableRow.getTableCell(JOB_STATUS_COLUMN);
                            Widget statusWidget = _jobStatusHelper.createJobStatusWidget(newJobInfo, ignoreStatusMessages());
                            statusCell.setValue(newJobInfo.getStatus());
                            statusCell.setWidget(statusWidget);
                            TableCell hitsCell = currentTableRow.getTableCell(NUM_HITS_COLUMN);
                            Widget numHitsWidget = BlastNumHitsWidget.createNumHitsWidget(newJobInfo, new ClickListener() {

                                public void onClick(Widget sender) {
                                    _table.clearHover();
                                    if (_jobSelectionListener != null) _jobSelectionListener.onSelect(job);
                                }
                            });
                            hitsCell.setValue(BlastNumHitsWidget.getNumHitsField(newJobInfo));
                            hitsCell.setWidget(numHitsWidget);
                            TableCell actionsCell = currentTableRow.getTableCell(ACTIONS_COLUMN);
                            if (isDone) {
                                _logger.debug("job is done");
                                actionsCell.setWidget(getActionColumnWidget(newJobInfo));
                            }
                            if (statusCell.getRow() != TableCell.NOT_SET && statusCell.getCol() != TableCell.NOT_SET) {
                                _pagingPanel.getSortableTable().refreshCell(statusCell.getRow(), statusCell.getCol());
                                _pagingPanel.getSortableTable().refreshCell(hitsCell.getRow(), hitsCell.getCol());
                                _pagingPanel.getSortableTable().refreshCell(actionsCell.getRow(), actionsCell.getCol());
                            }
                            if (isDone) {
                                SafeEffect.highlight(statusWidget, new EffectOption[] { new EffectOption("duration", "4.0") });
                            }
                        }
                    });
                }
            }
            _haveData = true;
            return tableRows;
        }
    }

    private boolean hasHits(BlastJobInfo job) {
        return (job.getNumHits() != null) && (job.getNumHits().intValue() > 0);
    }

    private Widget getActionColumnWidget(BlastJobInfo job) {
        boolean largeJobFlag = false;
        if (hasHits(job) && job.getNumHits() > BlastNumHitsWidget.NHITS_HIGHLIMIT) {
            largeJobFlag = true;
        }
        Grid grid = new Grid(1, 5);
        grid.setCellSpacing(0);
        grid.setCellPadding(0);
        grid.setWidget(0, 0, getResultsMenu(job, largeJobFlag));
        grid.setWidget(0, 1, HtmlUtils.getHtml("/", "linkSeparator"));
        grid.setWidget(0, 2, getJobMenu(job));
        grid.setWidget(0, 3, HtmlUtils.getHtml("/", "linkSeparator"));
        grid.setWidget(0, 4, getExportMenu(job, largeJobFlag));
        return grid;
    }

    private Widget getResultsMenu(final BlastJobInfo job, boolean largeJobFlag) {
        if (hasHits(job) && !largeJobFlag && SHOW_BLAST_RESULTS_PAGE) {
            return new Link("Results", new ClickListener() {

                public void onClick(Widget sender) {
                    _table.clearHover();
                    if (_jobSelectionListener != null) _jobSelectionListener.onSelect(job);
                }
            });
        } else {
            return HtmlUtils.getHtml("Results", "disabledTextLink");
        }
    }

    private Widget getExportMenu(final BlastJobInfo job, boolean largeJobFlag) {
        if (!hasHits(job)) return getDisabledExportMenu(); else if (largeJobFlag) return getLargeJobExportMenu(job); else return getJobExportMenu(job);
    }

    private Widget getLargeJobExportMenu(final BlastJobInfo job) {
        MenuBar menu = new MenuBarWithRightAlignedDropdowns();
        menu.setAutoOpen(false);
        MenuBar dropDown = new MenuBarWithRightAlignedDropdowns(true);
        dropDown.addStyleName("largeBLASTJobStatusPageExportMenu");
        MenuItem blastResultsItem = new MenuItem("Export Large BLAST results", true, new Command() {

            public void execute() {
                _logger.debug("Export Large BLAST results");
                SystemWebTracker.trackActivity("Status.ExportJob.LargeBLAST", new String[] { job.getJobId() });
                Window.open(getLargeBlastResultDownloadLink(job), "_self", "");
            }
        });
        dropDown.addItem(blastResultsItem);
        MenuItem export = new MenuItem("Export&nbsp;" + ImageBundleFactory.getControlImageBundle().getArrowDownEnabledImage().getHTML(), true, dropDown);
        export.setStyleName("tableTopLevelMenuItem");
        menu.addItem(export);
        return menu;
    }

    protected Widget getJobExportMenu(final JobInfo job) {
        MenuBar menu = new MenuBarWithRightAlignedDropdowns();
        menu.setAutoOpen(false);
        MenuBar dropDown = new MenuBarWithRightAlignedDropdowns(true);
        MenuItem queryItem = new MenuItem("All Matching Query Sequences as FASTA", true, new Command() {

            public void execute() {
                _logger.debug("Execute All FASTA (Query)");
                SystemWebTracker.trackActivity("Status.ExportJob.Query.FASTA", new String[] { job.getJobId() });
                BlastResultExportTask blastResultExportTask = new BlastResultExportTask(job.getJobId(), BlastResultExportTask.SEQUENCES_QUERY, ExportWriterConstants.EXPORT_TYPE_FASTA, null, null);
                new AsyncExportTaskController(blastResultExportTask).start();
            }
        });
        dropDown.addItem(queryItem);
        MenuItem subjectItem = new MenuItem("All Matching Subject Sequences as FASTA", true, new Command() {

            public void execute() {
                _logger.debug("Execute All FASTA (Subject)");
                SystemWebTracker.trackActivity("Status.ExportJob.Subject.FASTA", new String[] { job.getJobId() });
                BlastResultExportTask blastResultExportTask = new BlastResultExportTask(job.getJobId(), BlastResultExportTask.SEQUENCES_SUBJECT, ExportWriterConstants.EXPORT_TYPE_FASTA, null, null);
                new AsyncExportTaskController(blastResultExportTask).start();
            }
        });
        dropDown.addItem(subjectItem);
        MenuItem csvItem = new MenuItem("All BLAST Results with Metadata as CSV", true, new Command() {

            public void execute() {
                _logger.debug("Execute All CSV");
                SystemWebTracker.trackActivity("Status.ExportJob.CSV", new String[] { job.getJobId() });
                ArrayList<SortArgument> sortList = new ArrayList<SortArgument>();
                BlastResultExportTask blastResultExportTask = new BlastResultExportTask(job.getJobId(), BlastResultExportTask.SEQUENCES_ALL, ExportWriterConstants.EXPORT_TYPE_CSV, null, sortList);
                new AsyncExportTaskController(blastResultExportTask).start();
            }
        });
        dropDown.addItem(csvItem);
        MenuItem xlsItem = new MenuItem("All BLAST Results with Metadata as MS-Excel", true, new Command() {

            public void execute() {
                _logger.debug("Execute All Excel");
                SystemWebTracker.trackActivity("Status.ExportJob.XLS", new String[] { job.getJobId() });
                ArrayList<SortArgument> sortList = new ArrayList<SortArgument>();
                BlastResultExportTask blastResultExportTask = new BlastResultExportTask(job.getJobId(), BlastResultExportTask.SEQUENCES_ALL, ExportWriterConstants.EXPORT_TYPE_EXCEL, null, sortList);
                new AsyncExportTaskController(blastResultExportTask).start();
            }
        });
        dropDown.addItem(xlsItem);
        MenuItem xmlItem = new MenuItem("All BLAST Results as NCBI XML", true, new Command() {

            public void execute() {
                _logger.debug("Execute All NCBI XML");
                SystemWebTracker.trackActivity("Status.ExportJob.XML", new String[] { job.getJobId() });
                ArrayList<SortArgument> sortList = new ArrayList<SortArgument>();
                BlastResultExportTask blastResultExportTask = new BlastResultExportTask(job.getJobId(), BlastResultExportTask.SEQUENCES_ALL, ExportWriterConstants.EXPORT_TYPE_NCBI_BLAST_XML, null, sortList);
                new AsyncExportTaskController(blastResultExportTask).start();
            }
        });
        dropDown.addItem(xmlItem);
        MenuItem export = new MenuItem("Export&nbsp;" + ImageBundleFactory.getControlImageBundle().getArrowDownEnabledImage().getHTML(), true, dropDown);
        export.setStyleName("tableTopLevelMenuItem");
        menu.addItem(export);
        return menu;
    }

    protected Widget getJobMenu(final JobInfo job) {
        final BlastJobInfo blastJob = (BlastJobInfo) job;
        final MenuBar menu = new MenuBarWithRightAlignedDropdowns();
        menu.setAutoOpen(false);
        MenuBar dropDown = new MenuBarWithRightAlignedDropdowns(true);
        MenuItem paramItem = new MenuItem("Show Parameters", true, new Command() {

            public void execute() {
                _logger.debug("Displaying parameters for job=" + blastJob.getJobname() + " with parameter popup");
                Map<String, String> blastPopupParamMap = BlastJobParamHelper.createBlastPopupParamMap(blastJob);
                _paramPopup = new org.jcvi.vics.web.gwt.common.client.popup.jobs.JobParameterPopup(blastJob.getJobname(), new FormattedDateTime(blastJob.getSubmitted().getTime()).toString(), blastPopupParamMap, false);
                _paramPopup.setPopupTitle("Job Parameters - " + blastJob.getProgram());
                new PopupCenteredLauncher(_paramPopup).showPopup(menu);
            }
        });
        dropDown.addItem(paramItem);
        MenuItem rerun = new MenuItem("Job&nbsp;" + ImageBundleFactory.getControlImageBundle().getArrowDownEnabledImage().getHTML(), true, dropDown);
        rerun.setStyleName("tableTopLevelMenuItem");
        menu.addItem(rerun);
        return menu;
    }

    /**
     * Method which will ultimately fire-off the ftp of large blast results.
     * The string below, "filestoreBacked=true" makes all this happen in the FileDeliveryController and gets the
     * critical paths from vics.properties, set via Spring's fileDeliveryController bean.
     *
     * @param job - job which is having results exported
     * @return - url to make this happen
     */
    public static String getLargeBlastResultDownloadLink(BlastJobInfo job) {
        String blastResultsFileName = "blastResults.zip";
        String originalFileName = "/" + job.getUsername() + "/ReversePsiBlastResult/" + job.getBlastResultFileNodeId() + "/" + blastResultsFileName;
        return "/vics/fileDelivery.htm?inputfilename=" + originalFileName + "&suggestedfilename=" + blastResultsFileName + "&filestoreBacked=true";
    }
}
