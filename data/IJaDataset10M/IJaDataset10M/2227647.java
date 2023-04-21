package com.google.code.sagetvaddons.sagealert.client;

import com.google.code.sagetvaddons.sagealert.shared.SettingsService;
import com.google.code.sagetvaddons.sagealert.shared.SettingsServiceAsync;
import com.google.code.sagetvaddons.sagealert.shared.UserSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Widget for configuring user settings
 * @author dbattams
 * @version $Id: UserSettingsPanel.java 1022 2010-09-19 01:06:11Z derek@battams.ca $
 */
final class UserSettingsPanel extends VerticalPanel {

    private static final UserSettingsPanel INSTANCE = new UserSettingsPanel();

    static final UserSettingsPanel getInstance() {
        return INSTANCE;
    }

    private FlexTable tbl;

    private HorizontalPanel toolbar;

    private TextBox recMonSleep, uiMonSleep, conflictMonSleep, conflictReportDelay, sysMsgMonSleep, lowSpaceMonSleep, viewingClntMonSleep;

    private TextBox lowSpaceThreshold;

    private TextBox timeShort, timeMed, timeLong;

    private SettingsServiceAsync rpc;

    private UserSettingsPanel() {
        rpc = GWT.create(SettingsService.class);
        tbl = new FlexTable();
        toolbar = new HorizontalPanel();
        recMonSleep = new TextBox();
        uiMonSleep = new TextBox();
        conflictMonSleep = new TextBox();
        conflictReportDelay = new TextBox();
        sysMsgMonSleep = new TextBox();
        lowSpaceMonSleep = new TextBox();
        lowSpaceThreshold = new TextBox();
        viewingClntMonSleep = new TextBox();
        timeShort = new TextBox();
        timeMed = new TextBox();
        timeLong = new TextBox();
        recMonSleep.setName(UserSettings.REC_MONITOR_SLEEP);
        tbl.setText(0, 0, "Amount of time between each run of the recording monitor thread (minutes):");
        tbl.setWidget(0, 1, recMonSleep);
        uiMonSleep.setName(UserSettings.UI_MONITOR_SLEEP);
        tbl.setText(1, 0, "Amount of time between each run of the UI monitor thread (minutes):");
        tbl.setWidget(1, 1, uiMonSleep);
        conflictMonSleep.setName(UserSettings.CONFLICT_MONITOR_SLEEP);
        tbl.setText(2, 0, "Amount of time between each run of the conflict monitor thread (minutes):");
        tbl.setWidget(2, 1, conflictMonSleep);
        conflictReportDelay.setName(UserSettings.CONFLICT_REPORT_DELAY);
        tbl.setText(3, 0, "Wait this long before repeating a conflict detected event (minutes):");
        tbl.setWidget(3, 1, conflictReportDelay);
        sysMsgMonSleep.setName(UserSettings.SYSMSG_MONITOR_SLEEP);
        tbl.setText(4, 0, "Amount of time between each run of the system message monitor thread (minutes):");
        tbl.setWidget(4, 1, sysMsgMonSleep);
        lowSpaceMonSleep.setName(UserSettings.LOW_SPACE_MONITOR_SLEEP);
        tbl.setText(5, 0, "Amount of time between each run of the low space monitor thread (minutes):");
        tbl.setWidget(5, 1, lowSpaceMonSleep);
        lowSpaceThreshold.setName(UserSettings.LOW_SPACE_THRESHOLD);
        tbl.setText(6, 0, "Space is low when below this many GBs:");
        tbl.setWidget(6, 1, lowSpaceThreshold);
        viewingClntMonSleep.setName(UserSettings.VIEWING_CLNT_MONITOR_SLEEP);
        tbl.setText(7, 0, "Amount of time between each run of the client media viewing monitor thread (minutes):");
        tbl.setWidget(7, 1, viewingClntMonSleep);
        timeShort.setName(UserSettings.TIME_FORMAT_SHORT);
        tbl.setWidget(8, 0, new HTML("<a target=\"_blank\" href=\"http://java.sun.com/javase/6/docs/api/java/text/SimpleDateFormat.html\">SimpleDateFormat</a> string to be used for short notification messages:"));
        tbl.setWidget(8, 1, timeShort);
        timeMed.setName(UserSettings.TIME_FORMAT_MEDIUM);
        tbl.setWidget(9, 0, new HTML("<a target=\"_blank\" href=\"http://java.sun.com/javase/6/docs/api/java/text/SimpleDateFormat.html\">SimpleDateFormat</a> string to be used for medium notification messages:"));
        tbl.setWidget(9, 1, timeMed);
        timeLong.setName(UserSettings.TIME_FORMAT_LONG);
        tbl.setWidget(10, 0, new HTML("<a target=\"_blank\" href=\"http://java.sun.com/javase/6/docs/api/java/text/SimpleDateFormat.html\">SimpleDateFormat</a> string to be used for long notification messages:"));
        tbl.setWidget(10, 1, timeLong);
        reloadValues();
        for (int i = 1; i < tbl.getRowCount(); i += 2) tbl.getRowFormatter().addStyleName(i, "sageOddRow");
        Button saveBtn = new Button("Save");
        saveBtn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                checkValidity();
            }
        });
        toolbar.add(saveBtn);
        Button resetBtn = new Button("Reset");
        resetBtn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                reloadValues();
            }
        });
        toolbar.add(resetBtn);
        add(tbl);
        add(toolbar);
    }

    private void setInputValue(final TextBox box, String var, String defaultVal) {
        rpc.getSetting(var, defaultVal, new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                Window.alert(caught.getLocalizedMessage());
            }

            public void onSuccess(String result) {
                box.setValue(result);
            }
        });
    }

    private void checkValidity() {
        saveSetting(recMonSleep.getName(), recMonSleep.getValue());
        saveSetting(uiMonSleep.getName(), uiMonSleep.getValue());
        saveSetting(conflictMonSleep.getName(), conflictMonSleep.getValue());
        saveSetting(sysMsgMonSleep.getName(), sysMsgMonSleep.getValue());
        saveSetting(lowSpaceMonSleep.getName(), lowSpaceMonSleep.getValue());
        saveSetting(lowSpaceThreshold.getName(), lowSpaceThreshold.getValue());
        saveSetting(conflictReportDelay.getName(), conflictReportDelay.getValue());
        saveSetting(viewingClntMonSleep.getName(), viewingClntMonSleep.getValue());
        saveSetting(timeShort.getName(), timeShort.getValue());
        saveSetting(timeMed.getName(), timeMed.getValue());
        saveSetting(timeLong.getName(), timeLong.getValue());
    }

    private void saveSetting(String var, String val) {
        rpc.setSetting(var, val, new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                Window.alert(caught.getLocalizedMessage());
            }

            public void onSuccess(Void result) {
            }
        });
    }

    private void reloadValues() {
        setInputValue(recMonSleep, recMonSleep.getName(), UserSettings.REC_MONITOR_SLEEP_DEFAULT);
        setInputValue(uiMonSleep, uiMonSleep.getName(), UserSettings.UI_MONITOR_SLEEP_DEFAULT);
        setInputValue(conflictMonSleep, conflictMonSleep.getName(), UserSettings.CONFLICT_MONITOR_SLEEP_DEFAULT);
        setInputValue(sysMsgMonSleep, sysMsgMonSleep.getName(), UserSettings.SYSMSG_MONITOR_SLEEP_DEFAULT);
        setInputValue(lowSpaceMonSleep, lowSpaceMonSleep.getName(), UserSettings.LOW_SPACE_MONITOR_SLEEP_DEFAULT);
        setInputValue(lowSpaceThreshold, lowSpaceThreshold.getName(), UserSettings.LOW_SPACE_THRESHOLD_DEFAULT);
        setInputValue(conflictReportDelay, conflictReportDelay.getName(), UserSettings.CONFLICT_REPORT_DELAY_DEFAULT);
        setInputValue(viewingClntMonSleep, viewingClntMonSleep.getName(), UserSettings.VIEWING_CLNT_MONITOR_SLEEP_DEFAULT);
        setInputValue(timeShort, timeShort.getName(), UserSettings.TIME_FORMAT_SHORT_DEFAULT);
        setInputValue(timeMed, timeMed.getName(), UserSettings.TIME_FORMAT_MEDIUM_DEFAULT);
        setInputValue(timeLong, timeLong.getName(), UserSettings.TIME_FORMAT_LONG_DEFAULT);
    }
}
