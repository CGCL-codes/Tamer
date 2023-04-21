package edu.upmc.opi.caBIG.caTIES.status.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class StatusPage implements EntryPoint, ClickHandler {

    private AuthenticationServiceAsync authenticationService;

    private CollectionDataServiceAsync collectionDataService;

    private CTRMServiceAsync ctrmService;

    private MMTxServiceAsync mmtxService;

    private PathologyReportsServiceAsync pathologyReportsService;

    private SearchServiceAsync searchService;

    private SystemInfoServiceAsync systemInfoService;

    private FlexTable serviceStatusTable = new FlexTable();

    private FlexTable reportsTable = new FlexTable();

    private FlexTable usageTable = new FlexTable();

    private FlexTable systemInfoTable = new FlexTable();

    private Button updateReportsButton = new Button("Refresh");

    private Button updateServicesButton = new Button("Refresh");

    private Button updateUsageStatsButton = new Button("Refresh");

    private NumberFormat numberFormatter = NumberFormat.getDecimalFormat();

    private boolean authenticationReturned;

    private boolean authorizationReturned;

    private boolean deidReturned;

    private boolean searchReturned;

    private boolean mmtxReturned;

    private boolean hasError;

    private static final int ROW_AUTHENTICATION = 1;

    private static final int ROW_CTRM = 2;

    private static final int ROW_DEIDENTIFIED = 3;

    private static final int ROW_SEARCH = 4;

    private static final int ROW_MMTX = 5;

    private static final int COLUMN_SERIVCE_STATUS = 0;

    private static final int COLUMN_SERIVCE_NAME = 1;

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        serviceStatusTable.setText(ROW_AUTHENTICATION, COLUMN_SERIVCE_NAME, "Authentication");
        serviceStatusTable.setText(ROW_CTRM, COLUMN_SERIVCE_NAME, "Authorization");
        serviceStatusTable.setText(ROW_DEIDENTIFIED, COLUMN_SERIVCE_NAME, "De-identified Data Access");
        serviceStatusTable.setText(ROW_SEARCH, COLUMN_SERIVCE_NAME, "Search");
        serviceStatusTable.setText(ROW_MMTX, COLUMN_SERIVCE_NAME, "MMTx");
        reportsTable.setText(0, 0, "Report State");
        reportsTable.setText(0, 1, "Count");
        reportsTable.setText(0, 2, "Latest Date");
        usageTable.setText(0, 0, "Users");
        usageTable.setText(1, 0, "Protocols");
        usageTable.setText(2, 0, "Queries (last 30 days)");
        systemInfoTable.setText(0, 0, "caTIES Version");
        systemInfoTable.setText(1, 0, "Server Java Version");
        systemInfoTable.setText(2, 0, "Database");
        updateServicesButton.addClickHandler(this);
        updateReportsButton.addClickHandler(this);
        updateReportsButton.setEnabled(false);
        updateUsageStatsButton.addClickHandler(this);
        updateUsageStatsButton.setEnabled(false);
        updateServicesButton.setStyleName("button");
        updateReportsButton.setStyleName("button");
        updateUsageStatsButton.setStyleName("button");
        reportsTable.setStylePrimaryName("lined");
        usageTable.setStylePrimaryName("lined");
        systemInfoTable.setStylePrimaryName("lined");
        reportsTable.getRowFormatter().setStyleName(0, "lined-th");
        RootPanel.get("statusTable").add(serviceStatusTable);
        RootPanel.get("reportsTable").add(reportsTable);
        RootPanel.get("usageTable").add(usageTable);
        RootPanel.get("systemInfoTable").add(systemInfoTable);
        RootPanel.get("updateReportsButton").add(updateReportsButton);
        RootPanel.get("updateServicesButton").add(updateServicesButton);
        RootPanel.get("updateUsageStatsButton").add(updateUsageStatsButton);
        RootPanel.get("systemStatusImage").add(new Image("image/busybar.gif", 0, 0, 16, 11));
        initialUpdate();
        showSystemInfo();
    }

    private void initialUpdate() {
        authenticationService = (AuthenticationServiceAsync) GWT.create(AuthenticationService.class);
        collectionDataService = (CollectionDataServiceAsync) GWT.create(CollectionDataService.class);
        ctrmService = (CTRMServiceAsync) GWT.create(CTRMService.class);
        mmtxService = (MMTxServiceAsync) GWT.create(MMTxService.class);
        pathologyReportsService = (PathologyReportsServiceAsync) GWT.create(PathologyReportsService.class);
        searchService = (SearchServiceAsync) GWT.create(SearchService.class);
        systemInfoService = (SystemInfoServiceAsync) GWT.create(SystemInfoService.class);
        updateServiceStatus();
    }

    private void updateServiceStatus() {
        serviceStatusTable.setWidget(ROW_AUTHENTICATION, COLUMN_SERIVCE_STATUS, new Image("image/busybar.gif", 0, 0, 16, 11));
        serviceStatusTable.setWidget(ROW_CTRM, COLUMN_SERIVCE_STATUS, new Image("image/busybar.gif", 0, 0, 16, 11));
        serviceStatusTable.setWidget(ROW_DEIDENTIFIED, COLUMN_SERIVCE_STATUS, new Image("image/busybar.gif", 0, 0, 16, 11));
        serviceStatusTable.setWidget(ROW_SEARCH, COLUMN_SERIVCE_STATUS, new Image("image/busybar.gif", 0, 0, 16, 11));
        serviceStatusTable.setWidget(ROW_MMTX, COLUMN_SERIVCE_STATUS, new Image("image/busybar.gif", 0, 0, 16, 11));
        authenticationReturned = false;
        authorizationReturned = false;
        deidReturned = false;
        searchReturned = false;
        mmtxReturned = false;
        hasError = false;
        AsyncCallback<ServiceStatus> auth_callback = new AsyncCallback<ServiceStatus>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ServiceStatus result) {
                updateServiceStatusTable(result);
            }
        };
        AsyncCallback<ServiceStatus> path_callback = new AsyncCallback<ServiceStatus>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ServiceStatus result) {
                updateServiceStatusTable(result);
            }
        };
        AsyncCallback<ServiceStatus> mmtx_callback = new AsyncCallback<ServiceStatus>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ServiceStatus result) {
                updateServiceStatusTable(result);
            }
        };
        AsyncCallback<ServiceStatus> search_callback = new AsyncCallback<ServiceStatus>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ServiceStatus result) {
                updateServiceStatusTable(result);
            }
        };
        AsyncCallback<ServiceStatus> ctrm_callback = new AsyncCallback<ServiceStatus>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ServiceStatus result) {
                updateServiceStatusTable(result);
            }
        };
        authenticationService.checkStatus(auth_callback);
        pathologyReportsService.checkStatus(path_callback);
        ctrmService.checkStatus(ctrm_callback);
        mmtxService.checkStatus(mmtx_callback);
        searchService.checkStatus(search_callback);
    }

    private void updateServiceStatusTable(ServiceStatus result) {
        String serviceName = result.getServiceName();
        boolean isRunning = result.isRunning();
        Throwable exception = result.getException();
        if (serviceName.equals("authentication")) {
            authenticationReturned = true;
            updateStatus(ROW_AUTHENTICATION, isRunning);
            updateException(ROW_AUTHENTICATION, exception);
        } else if (serviceName.equals("pathology_reports")) {
            deidReturned = true;
            if (isRunning) updateReportsButton.setEnabled(true); else updateReportsButton.setEnabled(false);
            updateStatus(ROW_DEIDENTIFIED, isRunning);
            updateException(ROW_DEIDENTIFIED, exception);
            updateQueryCount();
            updateReports();
        } else if (serviceName.equals("ctrm")) {
            authorizationReturned = true;
            if (isRunning) updateUsageStatsButton.setEnabled(true); else updateUsageStatsButton.setEnabled(false);
            updateStatus(ROW_CTRM, isRunning);
            updateException(ROW_CTRM, exception);
            updateUserAndProtocolCount();
        } else if (serviceName.equals("mmtx")) {
            mmtxReturned = true;
            updateStatus(ROW_MMTX, isRunning);
            updateException(ROW_MMTX, exception);
        } else if (serviceName.equals("search")) {
            searchReturned = true;
            updateStatus(ROW_SEARCH, isRunning);
            updateException(ROW_SEARCH, exception);
        }
        if (authenticationReturned && authorizationReturned && deidReturned && searchReturned && mmtxReturned) {
            RootPanel.get("refreshServiceImage").setVisible(false);
            RootPanel.get("systemStatusImage").remove(0);
            if (hasError) {
                RootPanel.get("systemStatusImage").add(new Image("image/warning.png"));
            } else {
                RootPanel.get("systemStatusImage").add(new Image("image/yes.png"));
            }
        }
    }

    private void updateStatus(int row, boolean isRunning) {
        if (isRunning) {
            serviceStatusTable.setWidget(row, COLUMN_SERIVCE_STATUS, new Image("image/yes.png"));
        } else {
            hasError = true;
            serviceStatusTable.setWidget(row, COLUMN_SERIVCE_STATUS, new Image("image/warning.png"));
        }
    }

    private void updateException(int row, Throwable exception) {
        if (exception != null) {
            Image img = new Image("image/warning.png");
            DOM.setElementAttribute(img.getElement(), "class", "tooltip");
            img.setTitle(exception.getLocalizedMessage());
            serviceStatusTable.setWidget(row, COLUMN_SERIVCE_STATUS, img);
        }
    }

    private void updateUserAndProtocolCount() {
        AsyncCallback<List<Long>> callback = new AsyncCallback<List<Long>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<Long> result) {
                updateUsageTable(result);
            }
        };
        ctrmService.getUserAndProtocolCount(callback);
    }

    private void updateQueryCount() {
        AsyncCallback<Long> callback = new AsyncCallback<Long>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Long result) {
                updateUsageTable(result);
            }
        };
        pathologyReportsService.getQueryCount(callback);
    }

    private void updateUsageTable(List<Long> counts) {
        String userCount = numberFormatter.format(counts.get(0));
        String protocolCount = numberFormatter.format(counts.get(1));
        usageTable.setText(0, 1, userCount);
        usageTable.setText(1, 1, protocolCount);
        RootPanel.get("refreshUsageImage").setVisible(false);
    }

    private void updateUsageTable(Long result) {
        String queriesCount = numberFormatter.format(result);
        usageTable.setText(2, 1, queriesCount);
    }

    private void updateReports() {
        AsyncCallback<Map<String, Long>> countCallback = new AsyncCallback<Map<String, Long>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Map<String, Long> results) {
                updateReportsTableCounts(results);
            }
        };
        pathologyReportsService.getCounts(countCallback);
        AsyncCallback<Map<String, Date>> dateCallback = new AsyncCallback<Map<String, Date>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Map<String, Date> results) {
                updateReportsTableDates(results);
            }
        };
        collectionDataService.getDates(dateCallback);
    }

    private void updateReportsTableCounts(Map<String, Long> results) {
        for (String status : results.keySet()) {
            String count = numberFormatter.format(results.get(status));
            if (status.equals("CODING")) {
                reportsTable.setText(1, 0, "De-identified");
                reportsTable.setText(1, 1, count);
            } else if (status.equals("SEQUENCING")) {
                reportsTable.setText(2, 0, "Coded");
                reportsTable.setText(2, 1, count);
            } else if (status.equals("INDEXING")) {
                reportsTable.setText(3, 0, "Indexing");
                reportsTable.setText(3, 1, count);
            } else if (status.equals("IDLING")) {
                reportsTable.setText(4, 0, "Available for search");
                reportsTable.setText(4, 1, count);
                DOM.getElementById("totalReportCount").getChild(0).setNodeValue(count);
            }
        }
    }

    private void updateReportsTableDates(Map<String, Date> results) {
        for (String status : results.keySet()) {
            DateTimeFormat formatter = DateTimeFormat.getFormat("MM-dd-yyyy");
            String date = formatter.format(results.get(status));
            if (status.equals("CODING")) {
                reportsTable.setText(1, 0, "De-identified");
                reportsTable.setText(1, 2, date);
            } else if (status.equals("SEQUENCING")) {
                reportsTable.setText(2, 0, "Coded");
                reportsTable.setText(2, 2, date);
            } else if (status.equals("INDEXING")) {
                reportsTable.setText(3, 0, "Indexing");
                reportsTable.setText(3, 2, date);
            } else if (status.equals("IDLING")) {
                reportsTable.setText(4, 0, "Available for search");
                reportsTable.setText(4, 2, date);
            }
        }
        RootPanel.get("refreshReportsImage").setVisible(false);
    }

    private void showSystemInfo() {
        AsyncCallback<List<String>> sys_callback = new AsyncCallback<List<String>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<String> result) {
                updateSystemInfoTable(result);
            }
        };
        systemInfoService.getSystemInfo(sys_callback);
    }

    private void updateSystemInfoTable(List<String> result) {
        String catiesVersion = result.get(0);
        String javaVersion = result.get(1);
        String databaseVersion = result.get(2);
        systemInfoTable.setText(0, 1, catiesVersion);
        systemInfoTable.setText(1, 1, javaVersion);
        systemInfoTable.setText(2, 1, databaseVersion);
        Document doc = Document.get();
        doc.setTitle("caTIES v" + catiesVersion + " - Services Status Dashboard");
    }

    public void onClick(ClickEvent event) {
        Widget sender = (Widget) event.getSource();
        if (sender == updateReportsButton) {
            RootPanel.get("refreshReportsImage").setVisible(true);
            updateReports();
        } else if (sender == updateServicesButton) {
            RootPanel.get("refreshServiceImage").setVisible(true);
            RootPanel.get("systemStatusImage").remove(0);
            RootPanel.get("systemStatusImage").add(new Image("image/busybar.gif", 0, 0, 16, 11));
            updateServiceStatus();
        } else if (sender == updateUsageStatsButton) {
            RootPanel.get("refreshUsageImage").setVisible(true);
            updateUserAndProtocolCount();
            updateQueryCount();
        }
    }
}
