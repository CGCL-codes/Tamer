package com.commsen.stopwatch.swat.layout;

import java.beans.PropertyChangeListener;
import org.apache.log4j.Logger;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.list.AbstractListModel;
import nextapp.echo2.extras.app.layout.TabPaneLayoutData;
import com.commsen.stopwatch.Report;
import com.commsen.stopwatch.swat.StopwatchBean;

/**
 * TODO Dokumentacja 
 *
 * @author Milen Dyankov
 *
 */
public class StopwatchPane extends ContentPane {

    private static final Logger log = Logger.getLogger(StopwatchPane.class);

    public static final String TAB_ID_PREFIX = "TAB4";

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    StopwatchBean stopwatchBean;

    Row dataRow;

    StopwatchReportsTable stopwatchReportsTable;

    Button configButton;

    Button startButton;

    Button stopButton;

    Button refreshButton;

    EventProcessor eventProcessor;

    StopwatchPropertiesWindowPane stopwatchPropertiesWindow;

    public StopwatchPane(final StopwatchBean stopwatchBean) {
        super();
        this.stopwatchBean = stopwatchBean;
        stopwatchPropertiesWindow = new StopwatchPropertiesWindowPane(stopwatchBean);
        stopwatchReportsTable = new StopwatchReportsTable(stopwatchBean);
        eventProcessor = new EventProcessor();
        setId(TAB_ID_PREFIX + stopwatchBean.getURI());
        TabPaneLayoutData layoutData = new TabPaneLayoutData();
        layoutData.setTitle(stopwatchBean.getName());
        setLayoutData(layoutData);
        Column mainColumn = new Column();
        mainColumn.setStyleName("StopwatchPane.MainColumn");
        add(mainColumn);
        Row buttonRow = new Row();
        buttonRow.setStyleName("StopwatchPane.ButtonRow");
        mainColumn.add(buttonRow);
        configButton = new Button("config");
        configButton.setStyleName("app.button");
        configButton.setActionCommand(EventProcessor.ACTION_SHOW_PROPERTIES);
        configButton.addActionListener(eventProcessor);
        buttonRow.add(configButton);
        startButton = new Button("start");
        startButton.setEnabled(!stopwatchBean.isActive());
        startButton.setStyleName("app.button");
        startButton.setActionCommand(EventProcessor.ACTION_ACTIVATE);
        startButton.addActionListener(eventProcessor);
        buttonRow.add(startButton);
        stopButton = new Button("stop");
        stopButton.setEnabled(stopwatchBean.isActive());
        stopButton.setStyleName("app.button");
        stopButton.setActionCommand(EventProcessor.ACTION_DEACTIVATE);
        stopButton.addActionListener(eventProcessor);
        buttonRow.add(stopButton);
        refreshButton = new Button("refresh");
        refreshButton.setEnabled(stopwatchBean.isActive());
        refreshButton.setStyleName("app.button");
        refreshButton.setActionCommand(EventProcessor.ACTION_REFRESH);
        refreshButton.addActionListener(eventProcessor);
        buttonRow.add(refreshButton);
        dataRow = new Row();
        dataRow.setStyleName("StopwatchPane.DataRow");
        mainColumn.add(dataRow);
        if (stopwatchBean.isActive()) {
            dataRow.add(stopwatchReportsTable);
        } else {
            dataRow.add(new Label("Stopwatch angine is disabled !!!"));
        }
    }

    public static String getPaneId(StopwatchBean stopwatchBean) {
        return TAB_ID_PREFIX + stopwatchBean.getURI();
    }

    class EventProcessor implements ActionListener {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        public static final String ACTION_SHOW_PROPERTIES = "Stopwatch.SHOW_PROPERTIES";

        public static final String ACTION_ACTIVATE = "Stopwatch.ACTIVATE";

        public static final String ACTION_DEACTIVATE = "Stopwatch.DEACTIVATE";

        public static final String ACTION_REFRESH = "Stopwatch.REFRESH";

        /**
		 * @see nextapp.echo2.app.event.ActionListener#actionPerformed(nextapp.echo2.app.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent event) {
            if (ACTION_ACTIVATE.equals(event.getActionCommand())) {
                stopwatchBean.enable();
                dataRow.removeAll();
                dataRow.add(stopwatchReportsTable);
                stopwatchReportsTable.refresh();
            } else if (ACTION_DEACTIVATE.equals(event.getActionCommand())) {
                stopwatchBean.disable();
                dataRow.removeAll();
                dataRow.add(new Label("Stopwatch angine is disabled !!!"));
            } else if (ACTION_REFRESH.equals(event.getActionCommand())) {
                stopwatchReportsTable.refresh();
            } else if (ACTION_SHOW_PROPERTIES.equals(event.getActionCommand())) {
                add(stopwatchPropertiesWindow);
            } else {
            }
            startButton.setEnabled(!stopwatchBean.isActive());
            stopButton.setEnabled(stopwatchBean.isActive());
            refreshButton.setEnabled(stopwatchBean.isActive());
        }
    }
}
