package com.astrium.faceo.client.ui.programming.sps2.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.astrium.faceo.client.bean.programming.sps2.SensorBean;
import com.astrium.faceo.client.bean.programming.sps2.TaskBean;
import com.astrium.faceo.client.bean.programming.sps2.TaskResultBean;
import com.astrium.faceo.client.bean.programming.sps2.request.TaskingParametersBean;
import com.astrium.faceo.client.bean.programming.sps2.responses.TaskingResponseBean;
import com.astrium.faceo.client.common.ConfigConstant;
import com.astrium.faceo.client.common.GeneralConstant;
import com.astrium.faceo.client.common.ImagesConstant;
import com.astrium.faceo.client.common.Utils;
import com.astrium.faceo.client.common.programming.sps2.Sps2Constants;
import com.astrium.faceo.client.common.programming.sps2.Sps2GeneralConstants;
import com.astrium.faceo.client.rpc.programming.sps2.GetTaskResultService;
import com.astrium.faceo.client.rpc.programming.sps2.GetTaskResultServiceAsync;
import com.astrium.faceo.client.rpc.programming.sps2.GetTasksListService;
import com.astrium.faceo.client.rpc.programming.sps2.GetTasksListServiceAsync;
import com.astrium.faceo.client.ui.HomePage;
import com.astrium.faceo.client.ui.MultiSensorsSettingsPanel;
import com.astrium.faceo.client.ui.programming.sps2.SpsClientUtils;
import com.astrium.faceo.client.ui.programming.sps2.controller.SpsTaskOperationsManagment;
import com.astrium.faceo.client.ui.programming.sps2.windows.WindowCancel;
import com.astrium.faceo.client.ui.programming.sps2.windows.WindowConfirm;
import com.astrium.faceo.client.ui.programming.sps2.windows.WindowDescribeResultAccess;
import com.astrium.faceo.client.ui.programming.sps2.windows.WindowGetStatus;
import com.astrium.faceo.client.ui.programming.sps2.windows.WindowValidate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.RowParams;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtextux.client.data.PagingMemoryProxy;

/**
 * <B>HMA-FO</B> <BR>
 * 
 * <P>
 * La classe SpsTasksPanel g&egrave;re les widgets 
 * du panneau des taches SPS
 * de la page d'accueil
 * </P>
 * </P>
 * 
 * @author ASTRIUM
 * @version 1.0, le 20/05/2010
 */
public class SpsTasksPanel extends Panel {

    private static final Sps2Constants sps2Constants = (Sps2Constants) com.google.gwt.core.client.GWT.create(Sps2Constants.class);

    private final int FORM_PANEL_WIDTH = GeneralConstant.WEST_PANEL_WIDTH - 5;

    private final String TASK_ID = "taskId";

    private final String TASK_NAME = "name";

    private final String OPERATION = "operation";

    private final String REQUEST_STATUS = "requestStatus";

    private final String TASK_STATUS = "taskStatus";

    private final String CREATED_TIME = "createdTime";

    private final String LAST_UPDATED_TIME = "lastUpdatedTime";

    private final String SHOW_RESULTS = "showResults";

    private final String GET_STATUS = "getStatus";

    private final String DESCRIBE_RESULT_ACCESS = "resultAccess";

    private final String CANCEL = "cancel";

    private final String CONFIRM = "confirm";

    private final String VALIDATE = "validate";

    private final String DELETE = "delete";

    private final String SENSOR = "sensor";

    private final String UPDATED_TASK = "updated";

    private final int GRID_SHOW_RESULTS_ID = 8;

    private final int GRID_GET_STATUS_ID = 9;

    private final int GRID_CANCEL_ID = 10;

    private final int GRID_CONFIRM_ID = 11;

    private final int GRID_VALIDATE_ID = 12;

    private final int GRID_DESCRIBE_RESULT_ACCESS_ID = 13;

    private final int GRID_DELETE_TASK_ID = 14;

    private final int PROGRAMMING_TASKS_PAGE_SIZE = 100;

    private final String IFRAME_APPLET_SPS2 = "iframeAppletSps2";

    private final String SHOW_RESULTS_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_171_SHOW_FOOTPRINT + "\" width=\"24px\" height=\"24px\" />";

    private final String GET_STATUS_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_180_TASK_GET_STATUS + "\" width=\"24px\" height=\"24px\" />";

    private final String CONFIRM_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_184_TASK_CONFIRM + "\" width=\"24px\" height=\"24px\" />";

    private final String VALIDATE_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_185_TASK_VALIDATE + "\" width=\"24px\" height=\"24px\" />";

    private final String DELETE_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_171_DEL_TASK + "\" width=\"24px\" height=\"24px\" />";

    private final String CANCEL_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_182_TASK_CANCEL + "\" width=\"24px\" height=\"24px\" />";

    private final String DESCRIBE_RESULT_ACCESS_HTML = "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_181_TASK_DESCRIBE_RESULT_ACCESS + "\" width=\"24px\" height=\"24px\" />";

    private Store storeTasks;

    private PagingToolbar pagingToolbar;

    private ToolbarButton refreshButton;

    private RecordDef recordDefTasks;

    private GridPanel gridPanelTasks;

    private ColumnModel columnModelTasks;

    private WindowGetStatus windowGetStatusPanel;

    private static WindowCancel windowCancelPanel;

    private static WindowConfirm windowConfirmPanel;

    private static WindowValidate windowValidatePanel;

    private WindowDescribeResultAccess windowDescribeResultAccessPanel;

    private Timer timerGetTasksUpdated;

    /**
	 * SPS Mode
	 * true if you want to use SPS web services
	 * false to use xml files which contains task's list and tasks results
	 * 
	 */
    private static boolean spsWebServiceMode = true;

    /**
	 * Map object with identifier : 'Task' objects
	 */
    private Map<Integer, TaskBean> spsTasks = new HashMap<Integer, TaskBean>();

    /**
	 * constructor
	 * 
	 * @param _panelId (String) : HTML identifier on the page
	 */
    public SpsTasksPanel(String _panelId) {
        super();
        Utils.addIframeToRootElement(IFRAME_APPLET_SPS2);
        this.setId(_panelId);
        this.setLayout(new AnchorLayout());
        this.setTitle(sps2Constants.tasksTitle());
        this.setBorder(false);
        this.setAutoScroll(true);
        this.setAnimCollapse(true);
        this.setCollapsible(true);
        this.setCollapsed(false);
        this.setClosable(false);
        FieldDef[] fieldsTasks = new FieldDef[] { new StringFieldDef(TASK_ID), new StringFieldDef(TASK_NAME), new StringFieldDef(OPERATION), new StringFieldDef(REQUEST_STATUS), new StringFieldDef(TASK_STATUS), new StringFieldDef(CREATED_TIME), new StringFieldDef(LAST_UPDATED_TIME), new StringFieldDef(SENSOR), new BooleanFieldDef(UPDATED_TASK) };
        this.recordDefTasks = new RecordDef(fieldsTasks);
        Object[][] dataTasks = new Object[0][0];
        MemoryProxy proxyTasks = new MemoryProxy(dataTasks);
        ArrayReader readerTasks = new ArrayReader(this.recordDefTasks);
        this.storeTasks = new Store(proxyTasks, readerTasks, false);
        this.storeTasks.setDefaultSort(CREATED_TIME, SortDir.DESC);
        ColumnConfig idColumn = new ColumnConfig("id", TASK_ID, 50, true);
        idColumn.setHidden(true);
        ColumnConfig nameColumn = new ColumnConfig(sps2Constants.taskName(), TASK_NAME, 160, false);
        nameColumn.setAlign(TextAlign.LEFT);
        nameColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                String taskName = _record.getAsString(TASK_NAME);
                String taskId = _record.getAsString(TASK_ID);
                return (taskName + "<div>" + taskId + "</div>");
            }
        });
        ColumnConfig operationColumn = new ColumnConfig(sps2Constants.taskOperation(), OPERATION, 110, false);
        ColumnConfig requestStatusColumn = new ColumnConfig(sps2Constants.taskStatus(), REQUEST_STATUS, 95, false);
        requestStatusColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                String requestStatus = "";
                String taskStatus = "";
                if (_record.getAsString(REQUEST_STATUS) != null) {
                    requestStatus = _record.getAsString(REQUEST_STATUS);
                }
                if (_record.getAsString(TASK_STATUS) != null) {
                    taskStatus = _record.getAsString(TASK_STATUS);
                }
                return (requestStatus + "<div>" + taskStatus + "</div>");
            }
        });
        ColumnConfig taskStatusColumn = new ColumnConfig(sps2Constants.taskStatus(), TASK_STATUS, 95, false);
        taskStatusColumn.setHidden(true);
        taskStatusColumn.setFixed(true);
        taskStatusColumn.setAlign(TextAlign.LEFT);
        ColumnConfig createdTimeColumn = new ColumnConfig(sps2Constants.taskCreatedTime() + "-" + sps2Constants.taskLastUpdatedTime(), CREATED_TIME, 150, true);
        createdTimeColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                String createdTime = _record.getAsString(CREATED_TIME);
                String updatedTime = _record.getAsString(LAST_UPDATED_TIME);
                return (createdTime + "<div>" + updatedTime + "</div>");
            }
        });
        ColumnConfig lastUpdatedTimeColumn = new ColumnConfig(sps2Constants.taskLastUpdatedTime(), LAST_UPDATED_TIME, 100, true);
        lastUpdatedTimeColumn.setHidden(true);
        lastUpdatedTimeColumn.setFixed(true);
        lastUpdatedTimeColumn.setSortable(false);
        ColumnConfig sensorColumn = new ColumnConfig(sps2Constants.sensor(), SENSOR, 20, false);
        sensorColumn.setHidden(true);
        ColumnConfig showResultsColumn = new ColumnConfig(sps2Constants.taskShowResults(), SHOW_RESULTS, 50, false);
        showResultsColumn.setHidden(false);
        showResultsColumn.setFixed(true);
        showResultsColumn.setAlign(TextAlign.CENTER);
        showResultsColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean ok = SpsTasksUtils.showResultsRenderable(_record, OPERATION, REQUEST_STATUS, TASK_STATUS);
                if (ok) {
                    return SHOW_RESULTS_HTML;
                } else {
                    return "";
                }
            }
        });
        ColumnConfig getStatusColumn = new ColumnConfig(sps2Constants.taskStatusDetails(), GET_STATUS, 55, false);
        getStatusColumn.setHidden(false);
        getStatusColumn.setFixed(true);
        getStatusColumn.setAlign(TextAlign.CENTER);
        getStatusColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean ok = SpsTasksUtils.getStatusRenderable(_record, OPERATION, REQUEST_STATUS, TASK_STATUS);
                if (ok) {
                    return GET_STATUS_HTML;
                } else {
                    return "";
                }
            }
        });
        ColumnConfig cancelColumn = new ColumnConfig(sps2Constants.taskCancel(), CANCEL, 50, false);
        cancelColumn.setHidden(false);
        cancelColumn.setFixed(true);
        cancelColumn.setAlign(TextAlign.CENTER);
        cancelColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean ok = SpsTasksUtils.cancelRenderable(_record, OPERATION, REQUEST_STATUS, TASK_STATUS);
                if (ok) {
                    return CANCEL_HTML;
                } else {
                    return "";
                }
            }
        });
        ColumnConfig confirmColumn = new ColumnConfig(sps2Constants.taskConfirm(), CONFIRM, 50, false);
        confirmColumn.setHidden(false);
        confirmColumn.setFixed(true);
        confirmColumn.setAlign(TextAlign.CENTER);
        confirmColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean ok = SpsTasksUtils.confirmRenderable(_record, OPERATION, REQUEST_STATUS, TASK_STATUS);
                if (ok) {
                    return CONFIRM_HTML;
                } else {
                    return "";
                }
            }
        });
        ColumnConfig validateColumn = new ColumnConfig(sps2Constants.taskValidate(), VALIDATE, 50, false);
        validateColumn.setHidden(false);
        validateColumn.setFixed(true);
        validateColumn.setAlign(TextAlign.CENTER);
        validateColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean ok = SpsTasksUtils.validateRenderable(_record, OPERATION, REQUEST_STATUS, TASK_STATUS);
                if (ok) {
                    return VALIDATE_HTML;
                } else {
                    return "";
                }
            }
        });
        ColumnConfig getDescribeResultAccessColumn = new ColumnConfig(sps2Constants.taskResultAccess(), DESCRIBE_RESULT_ACCESS, 50, false);
        getDescribeResultAccessColumn.setHidden(false);
        getDescribeResultAccessColumn.setFixed(true);
        getDescribeResultAccessColumn.setAlign(TextAlign.CENTER);
        getDescribeResultAccessColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean ok = SpsTasksUtils.describeResultRenderable(_record, OPERATION, REQUEST_STATUS, TASK_STATUS);
                if (ok) {
                    return DESCRIBE_RESULT_ACCESS_HTML;
                } else {
                    return "";
                }
            }
        });
        ColumnConfig deleteColumn = new ColumnConfig(sps2Constants.taskDelete(), DELETE, 45, false);
        deleteColumn.setHidden(false);
        deleteColumn.setFixed(true);
        deleteColumn.setAlign(TextAlign.CENTER);
        deleteColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                return DELETE_HTML;
            }
        });
        ColumnConfig updatedColumn = new ColumnConfig(sps2Constants.taskUpdated(), UPDATED_TASK, 50, false);
        updatedColumn.setHidden(false);
        updatedColumn.setFixed(true);
        updatedColumn.setAlign(TextAlign.CENTER);
        updatedColumn.setRenderer(new Renderer() {

            public String render(Object _value, CellMetadata _cellMetadata, Record _record, int _rowIndex, int _colNum, Store _store) {
                boolean updated = _record.getAsBoolean(UPDATED_TASK);
                if (updated) {
                    return "<img class=\"faceo-image_button\" src=\"" + GWT.getHostPageBaseURL() + ImagesConstant.ICON_183_TASK_UPDATED + "\" width=\"24px\" height=\"24px\" />";
                } else {
                    return "";
                }
            }
        });
        BaseColumnConfig[] baseColumnConfigProgrammingTasks = new BaseColumnConfig[] { idColumn, nameColumn, operationColumn, requestStatusColumn, taskStatusColumn, createdTimeColumn, lastUpdatedTimeColumn, sensorColumn, showResultsColumn, getStatusColumn, cancelColumn, confirmColumn, validateColumn, getDescribeResultAccessColumn, deleteColumn, updatedColumn };
        ColumnModel columnModel = new ColumnModel(baseColumnConfigProgrammingTasks);
        this.columnModelTasks = columnModel;
        this.windowGetStatusPanel = new WindowGetStatus(_panelId + "_windowGetStatusPanel");
        windowCancelPanel = new WindowCancel(_panelId + "_windowCancelPanel");
        windowConfirmPanel = new WindowConfirm(_panelId + "_windowConfirmPanel");
        windowValidatePanel = new WindowValidate(_panelId + "_windowValidatePanel");
        this.windowDescribeResultAccessPanel = new WindowDescribeResultAccess(_panelId + "_windowDescribeResultAccessPanel");
        this.gridPanelTasks = new GridPanel();
        this.gridPanelTasks.setId(Sps2GeneralConstants.GRID_PANEL_PROGRAMMING_TASKS);
        this.gridPanelTasks.setLayout(new AnchorLayout());
        this.gridPanelTasks.setAutoScroll(true);
        this.gridPanelTasks.setTitle(sps2Constants.tasksTitle());
        int clientHeight = com.google.gwt.user.client.Window.getClientHeight();
        this.gridPanelTasks.setHeight(clientHeight);
        this.gridPanelTasks.setWidth(FORM_PANEL_WIDTH - 10);
        this.gridPanelTasks.setStore(this.storeTasks);
        this.gridPanelTasks.setColumnModel(this.columnModelTasks);
        this.gridPanelTasks.setTrackMouseOver(true);
        this.gridPanelTasks.setLoadMask(true);
        this.gridPanelTasks.setFrame(true);
        this.gridPanelTasks.setStripeRows(false);
        this.gridPanelTasks.setIconCls("grid-icon");
        this.gridPanelTasks.setCollapsible(true);
        GridView viewProgrammingTasks = new GridView() {

            public String getRowClass(Record record, int index, RowParams rowParams, Store store) {
                return "x-grid3-row-collapsed";
            }
        };
        viewProgrammingTasks.setForceFit(false);
        viewProgrammingTasks.setEnableRowBody(true);
        addGridPanelCellListener(this.gridPanelTasks);
        this.gridPanelTasks.setView(viewProgrammingTasks);
        this.pagingToolbar = new PagingToolbar(this.storeTasks);
        this.pagingToolbar.setId(_panelId + "_pagingToolbar");
        this.pagingToolbar.setPageSize(PROGRAMMING_TASKS_PAGE_SIZE);
        this.pagingToolbar.setDisplayInfo(true);
        this.pagingToolbar.setDisplayMsg(sps2Constants.tasksDisplayMsg());
        this.pagingToolbar.setEmptyMsg(sps2Constants.tasksNoTaskMsg());
        this.pagingToolbar.addSeparator();
        this.gridPanelTasks.setBottomToolbar(this.pagingToolbar);
        this.gridPanelTasks.setEnableDragDrop(true);
        this.gridPanelTasks.setDdGroup(GeneralConstant.CARTO_DRAG_DROP_GROUP);
        this.gridPanelTasks.setDragDropText("Drag Programming Task");
        this.gridPanelTasks.addListener(new PanelListenerAdapter() {

            public void onRender(Component component) {
                refreshButton = pagingToolbar.getRefreshButton();
                refreshButton.addListener(new ButtonListenerAdapter() {

                    public void onClick(Button button, EventObject e) {
                        updateGetTasksList();
                    }
                });
            }
        });
        this.gridPanelTasks.getView().setAutoFill(true);
        this.add(this.gridPanelTasks, new AnchorLayoutData("100% 100%"));
    }

    /**
	 * 
	 * @param _cols (int)	: number of columns of the Grid
	 * @param _rows (int)	: number of rows of the Grid
	 * @param _tasks (Map<String, TaskBean>)	:
	 */
    private void updateGridTasks(int _cols, int _rows, Map<String, TaskBean> _tasks) {
        if (this.storeTasks != null) {
            this.storeTasks.removeAll();
        }
        if (this.spsTasks != null) {
            this.spsTasks.clear();
        }
        Object[][] data = new Object[_rows][_cols];
        DateTimeFormat timeFormat = DateTimeFormat.getFormat("yyyy.MM.dd '-' HH:mm");
        int i = 0;
        for (Entry<String, TaskBean> task : _tasks.entrySet()) {
            TaskBean taskBean = task.getValue();
            data[i][0] = taskBean.getId();
            data[i][1] = taskBean.getName();
            data[i][2] = taskBean.getOperation();
            data[i][3] = taskBean.getRequestStatus();
            data[i][4] = taskBean.getStatus();
            try {
                data[i][5] = timeFormat.format(taskBean.getCreatedTime());
            } catch (Exception exception) {
            }
            try {
                data[i][6] = timeFormat.format(taskBean.getLastUpdatedTime());
            } catch (Exception exception) {
            }
            data[i][7] = taskBean.getSensor();
            data[i][8] = taskBean.getUpdatedTask();
            this.spsTasks.put(i, taskBean);
            i++;
        }
        this.expand();
        PagingMemoryProxy proxy = new PagingMemoryProxy(data);
        ArrayReader reader = new ArrayReader(this.recordDefTasks);
        this.storeTasks = new Store(proxy, reader, true);
        this.gridPanelTasks.reconfigure(this.storeTasks, this.gridPanelTasks.getColumnModel());
        this.gridPanelTasks.getView().setAutoFill(false);
        this.gridPanelTasks.getView().setForceFit(false);
        this.storeTasks.sort(CREATED_TIME, SortDir.DESC);
        this.pagingToolbar.setStore(this.storeTasks);
        this.pagingToolbar.setPageSize(PROGRAMMING_TASKS_PAGE_SIZE);
        this.storeTasks.load(0, this.pagingToolbar.getPageSize());
        this.pagingToolbar.updateInfo();
        this.gridPanelTasks.show();
    }

    private void addGridPanelCellListener(GridPanel _gridPanel) {
        _gridPanel.addGridCellListener(new GridCellListenerAdapter() {

            public void onCellClick(GridPanel _editorGrid, int rowIndex, int colIndex, EventObject e) {
                switch(colIndex) {
                    case GRID_SHOW_RESULTS_ID:
                        Record record1 = _editorGrid.getStore().getAt(rowIndex);
                        if ((record1 != null) && (SpsTasksUtils.showResultsRenderable(record1, OPERATION, REQUEST_STATUS, TASK_STATUS))) {
                            showPanelResults(record1.getAsString(TASK_ID), record1.getAsString(SENSOR));
                        }
                        break;
                    case GRID_GET_STATUS_ID:
                        Record record2 = _editorGrid.getStore().getAt(rowIndex);
                        if ((record2 != null) && (SpsTasksUtils.getStatusRenderable(record2, OPERATION, REQUEST_STATUS, TASK_STATUS))) {
                            SpsTaskOperationsManagment.getTaskStatus(record2.getAsString(TASK_ID), record2.getAsString(SENSOR), _editorGrid, sps2Constants.requestPending(), windowGetStatusPanel);
                        }
                        break;
                    case GRID_CANCEL_ID:
                        Record record3 = _editorGrid.getStore().getAt(rowIndex);
                        if ((record3 != null) && (SpsTasksUtils.cancelRenderable(record3, OPERATION, REQUEST_STATUS, TASK_STATUS))) {
                            SpsTaskOperationsManagment.cancelTask(record3.getAsString(TASK_ID), record3.getAsString(SENSOR), _editorGrid, sps2Constants.requestPending(), windowCancelPanel);
                        }
                        break;
                    case GRID_CONFIRM_ID:
                        Record record6 = _editorGrid.getStore().getAt(rowIndex);
                        if ((record6 != null) && (SpsTasksUtils.confirmRenderable(record6, OPERATION, REQUEST_STATUS, TASK_STATUS))) {
                            SpsTaskOperationsManagment.confirmTask(record6.getAsString(TASK_ID), record6.getAsString(SENSOR), _editorGrid, sps2Constants.requestPending(), windowConfirmPanel);
                        }
                        break;
                    case GRID_VALIDATE_ID:
                        Record record7 = _editorGrid.getStore().getAt(rowIndex);
                        if ((record7 != null) && (SpsTasksUtils.validateRenderable(record7, OPERATION, REQUEST_STATUS, TASK_STATUS))) {
                            SpsTaskOperationsManagment.validateTask(record7.getAsString(TASK_ID), record7.getAsString(TASK_NAME), record7.getAsString(SENSOR), _editorGrid, sps2Constants.requestPending(), windowValidatePanel);
                        }
                        break;
                    case GRID_DESCRIBE_RESULT_ACCESS_ID:
                        Record record4 = _editorGrid.getStore().getAt(rowIndex);
                        if ((record4 != null) && (SpsTasksUtils.describeResultRenderable(record4, OPERATION, REQUEST_STATUS, TASK_STATUS))) {
                            SpsTaskOperationsManagment.describeTaskResultAccess(record4.getAsString(TASK_ID), record4.getAsString(SENSOR), _editorGrid, sps2Constants.requestPending(), windowDescribeResultAccessPanel);
                        }
                        break;
                    case GRID_DELETE_TASK_ID:
                        Record record5 = _editorGrid.getStore().getAt(rowIndex);
                        if (record5 != null) {
                            SpsTaskOperationsManagment.deleteTask(record5.getAsString(TASK_ID));
                        }
                        break;
                    default:
                        ;
                }
            }
        });
    }

    /**
	 * update of the parameters of the getFeasibility SPS operation
	 * and the Grid which contain the results of the selected task
	 * 
	 * @param _idTask (String) 		: the selected task identifier
	 * @param _sensorUrn (String) 	: the selected task sensor urn
	 */
    private void showPanelResults(final String _idTask, String _sensorUrn) {
        final String idUser = ConfigConstant.SPS_FACEO_USER;
        final GetTaskResultServiceAsync getTaskResultServiceAsync = GetTaskResultService.Util.getInstance();
        ServiceDefTarget endpoint = (ServiceDefTarget) getTaskResultServiceAsync;
        String moduleRelativeURL = GWT.getModuleBaseURL() + Sps2GeneralConstants.TASK_RESULT_CONTROLLER;
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        gridPanelTasks.getEl().mask("pending ...");
        AsyncCallback<TaskResultBean> callbackTaskResult = new AsyncCallback<TaskResultBean>() {

            public void onSuccess(TaskResultBean _taskResultBean) {
                gridPanelTasks.getEl().unmask();
                if (_taskResultBean == null) {
                    com.google.gwt.user.client.Window.alert(sps2Constants.submitOnSuccess() + " : " + sps2Constants.submitOnSuccessNoResult());
                } else {
                    if (!_taskResultBean.getError().getMessage().equalsIgnoreCase("")) {
                        com.google.gwt.user.client.Window.alert("error : " + _taskResultBean.getError().getMessage());
                    } else {
                        TaskingResponseBean taskingResponseBean = _taskResultBean.getTaskingResponseBean();
                        boolean updateResults = false;
                        String message = "";
                        if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_FEASIBLE)) || (_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_ACQUIRED)) || (_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_INEXECUTION)) || (_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_RESERVED)) || (_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_CONFIRMED)) || (_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_COMPLETED)) || (_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_ACCEPTED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_FEASIBLE)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_ACQUIRED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_INEXECUTION)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_RESERVED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_CONFIRMED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_COMPLETED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_ACCEPTED))) {
                            if (taskingResponseBean != null) {
                                HomePage.getWestPanel().getMissionRequestPanel(GeneralConstant.SPS_PANEL_ID).setActiveTab(Sps2GeneralConstants.SPS_TAB_RESULTS);
                                SpsPanels.getSpsSolutionsPanel().updateGridSegments(_taskResultBean);
                                updateResults = true;
                            } else {
                                updateResults = false;
                                if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_ACQUIRED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_ACQUIRED))) {
                                    message = sps2Constants.acquired();
                                } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_RESERVED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_RESERVED))) {
                                    message = sps2Constants.reserved();
                                }
                            }
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_REJECTED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_REJECTED))) {
                            updateResults = false;
                            message = sps2Constants.rejected();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_FAILED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_FAILED))) {
                            updateResults = false;
                            message = sps2Constants.failed();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_PENDING)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_PENDING))) {
                            updateResults = false;
                            message = sps2Constants.pending();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_EXPIRED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_EXPIRED))) {
                            updateResults = false;
                            message = sps2Constants.expired();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_CANCELLED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_CANCELLED))) {
                            updateResults = false;
                            message = sps2Constants.cancelled();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_PLANNED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_PLANNED))) {
                            updateResults = false;
                            message = sps2Constants.planned();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_POTENTIAL)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_POTENTIAL))) {
                            updateResults = false;
                            message = sps2Constants.potential();
                        } else if ((_taskResultBean.getRequestStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_VALIDATED)) || (_taskResultBean.getStatus().equalsIgnoreCase(Sps2GeneralConstants.STATUS_VALIDATED))) {
                            updateResults = false;
                            message = sps2Constants.validated();
                        } else {
                            updateResults = false;
                            message = _taskResultBean.getRequestStatus() + "-" + _taskResultBean.getStatus();
                        }
                        if (!updateResults) {
                            com.google.gwt.user.client.Window.alert(sps2Constants.task() + " '" + _taskResultBean.getId() + "' \n" + message);
                            SpsPanels.getSpsSolutionsPanel().clearGridProgrammingSegments();
                        }
                        HomePage.getWestPanel().getMissionRequestPanel(GeneralConstant.SPS_PANEL_ID).setActiveTab(Sps2GeneralConstants.SPS_TAB_SETTINGS);
                        SpsClientUtils.updatePanelSettings(_taskResultBean);
                    }
                }
            }

            public void onFailure(Throwable caught) {
                gridPanelTasks.getEl().unmask();
                com.google.gwt.user.client.Window.alert(sps2Constants.submitOnFailure() + " : " + caught.getLocalizedMessage());
            }
        };
        TaskingParametersBean parameters = getSensorTaskParameters(_sensorUrn);
        getTaskResultServiceAsync.getTaskResult(Sps2GeneralConstants.SPS_GET_TASK_RESULT, idUser, _idTask, parameters, spsWebServiceMode, callbackTaskResult);
    }

    /**
	 * update of the parameters of the getFeasibility SPS operation
	 * 
	 * @param _sensorUrn (String) : sensor urn
	 * 
	 * @return TaskingParametersBean : tasking parametres for the sensor urn
	 */
    public static TaskingParametersBean getSensorTaskParameters(String _sensorUrn) {
        TaskingParametersBean parameters = new TaskingParametersBean();
        SpsSettingsPanel settingsPanel = SpsPanels.getSpsSettingsPanel();
        if (settingsPanel != null) {
            MultiSensorsSettingsPanel sensorsPanel = settingsPanel.getMultiSensorsSettingsPanel();
            if (sensorsPanel != null) {
                SpsSensorSettingsPanel sensorSettingsPanel = null;
                for (Entry<String, SensorBean> sensors : Sps2GeneralConstants.getSensors().entrySet()) {
                    SensorBean sensor = sensors.getValue();
                    if (sensor.getActive()) {
                        if (_sensorUrn.equalsIgnoreCase(sensor.getUrn())) {
                            sensorSettingsPanel = sensorsPanel.getSpsSensorSettingsPanel(sensor.getId());
                        }
                    }
                }
                if (sensorSettingsPanel != null) {
                    parameters = sensorSettingsPanel.getTaskingParameters();
                }
            }
        }
        return parameters;
    }

    private void updateGetTasksList() {
        final GetTasksListServiceAsync getTasksListServiceAsync = GetTasksListService.Util.getInstance();
        final String idUser = ConfigConstant.SPS_FACEO_USER;
        ServiceDefTarget endpoint = (ServiceDefTarget) getTasksListServiceAsync;
        String moduleRelativeURL = GWT.getModuleBaseURL() + Sps2GeneralConstants.GET_TASKS_LIST_CONTROLLER;
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        AsyncCallback<Map<String, TaskBean>> callbackGetTasksList = new AsyncCallback<Map<String, TaskBean>>() {

            public void onFailure(Throwable caught) {
                com.google.gwt.user.client.Window.alert(sps2Constants.tasksListSubmitOnFailure() + " : " + caught.getLocalizedMessage());
            }

            public void onSuccess(Map<String, TaskBean> _tasks) {
                if (_tasks == null) {
                    com.google.gwt.user.client.Window.alert(sps2Constants.submitOnSuccess() + " : " + sps2Constants.submitOnSuccessNoResult());
                } else {
                    String keyError = "0";
                    if ((_tasks.size() == 1) && (_tasks.containsKey(keyError))) {
                        TaskBean taskBean = _tasks.get(keyError);
                        if (taskBean != null) com.google.gwt.user.client.Window.alert(sps2Constants.tasksListSubmitOnFailure() + " : " + taskBean.getError().getMessage());
                    } else {
                        updateGridTasks(Sps2GeneralConstants.PROGRAMMING_GRID_PANEL_NB_COLUMNS, _tasks.size(), _tasks);
                        if (timerGetTasksUpdated == null) {
                            timerGetTasksUpdated = new Timer() {

                                public void run() {
                                    refreshButton.fireEvent("click");
                                }
                            };
                            timerGetTasksUpdated.scheduleRepeating(60000);
                        }
                    }
                }
            }
        };
        getTasksListServiceAsync.getTasksList(Sps2GeneralConstants.SPS_GET_TASKS_LIST, idUser, spsWebServiceMode, callbackGetTasksList);
    }

    /**
	 * getter on refreshButton
	 * @return ToolbarButton : the Refresh ToolBar Button
	 */
    public ToolbarButton getRefreshButton() {
        return this.refreshButton;
    }

    /**
	 * getter on spsWebServiceMode
	 * @return boolean : true if can must use web services, false else
	 */
    public static boolean getSpsWebServiceMode() {
        return spsWebServiceMode;
    }
}
