package org.ludo.safepeer.ui.swt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.ludo.config.ConfigConstants;
import org.ludo.config.ConfigEntry;
import org.ludo.plugins.azureus.AzureusConfigConstants;
import org.ludo.safepeer.SafepeerConfigConstants;

/** GUI for SafePeer using SWT components.
 * @author  <a href="mailto:masterludo@gmx.net">Ludovic Kim-Xuan Galibert</a>
 * @revision $Id: SafePeerSwtGui.java,v 1.2 2004/12/12 10:00:52 masterludo Exp $
 * @created 26. January 2004, 12:35
 */
public class SafePeerSwtGui implements SelectionListener {

    /** Holds the Button for enabling SafePeer */
    private Button enableSafePeerButton;

    /** Holds the Button for enabling the Blocklist manager */
    private Button enableBlocklistManagerButton;

    /** Holds the Text for the primary database Url */
    private Text primaryDbUrlText;

    /** Holds the Text for the secondary databases Urls */
    private Text secondaryDbUrlsText;

    /** Holds the FileChooserField for the cache file */
    private FileChooserField cacheFileChooserField;

    /** Holds the FileChooserField for the log file */
    private FileChooserField logFileChooserField;

    /** Holds the Button for enabling the logging */
    private Button enableLoggingButton;

    /** Holds the Button for enabling async loading */
    private Button enableAsyncLoadingButton;

    /** Holds the Button for enabling periodic update */
    private Button enablePeriodicUpdateButton;

    /** Holds the Text for the update timer */
    private Text updateTimerText;

    /** Holds the Text for the socket connect timeout */
    private Text socketConnectTimeoutText;

    /** Holds the Text for the socket read timeout */
    private Text socketReadTimeoutText;

    /** Holds the Button for enabling the proxy */
    private Button enableProxyButton;

    /** Holds the Text for the proxy host */
    private Text proxyHostText;

    /** Holds the Text for the proxy port */
    private Text proxyPortText;

    /** Holds the save Button */
    private Button saveButton;

    /** Holds the ConfigEntry */
    private ConfigEntry configEntry;

    /** Holds the parent Composite */
    private Composite parent;

    /** Holds the composite holding the Gui */
    private Composite composite;

    /**
	 * Creates a new SafePeerSwtGui.
	 * @param aConfigEntry  the ConfigEntry to use for the configuration.
	 * @param aParent  the parent composite of the Gui's main composite.
	 */
    public SafePeerSwtGui(ConfigEntry aConfigEntry, Composite aParent) {
        setConfigEntry(aConfigEntry);
        setParent(aParent);
        if (null == getConfigEntry() || null == getParent()) {
            throw new IllegalArgumentException("The ConfigEntry and the parent Composite must NOT be null!");
        }
        initGui();
    }

    /**
	 * Initializes the Gui.
	 */
    public void initGui() {
        Display display = getParent().getDisplay();
        TabFolder tab = new TabFolder(getParent(), SWT.NULL);
        TabItem spItem = new TabItem(tab, SWT.NULL);
        spItem.setText("SafePeer");
        Composite spComposite = new Composite(tab, SWT.NULL);
        spItem.setControl(spComposite);
        new BlocklistManagerSwtGui(getConfigEntry(), tab);
        setComposite(spComposite);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        getComposite().setLayout(layout);
        initEnableSafePeer();
        initEnableBlocklistManager();
        initPrimaryDbUrl();
        initSecondaryDbUrls();
        initCacheFile();
        initLogFile();
        initEnableLogging();
        initEnableAsyncLoading();
        initEnablePeriodicUpdate();
        initUpdateTimer();
        initSocketConnectTimeout();
        initSocketReadTimeout();
        initEnableProxy();
        initProxyHost();
        initProxyPort();
        initSaveButton();
        if (getEnableSafePeerButton().getSelection()) {
            enableComponents();
        } else {
            disableComponents();
        }
        boolean isBlocklistEnabled = getEnableBlocklistManagerButton().getSelection();
        getPrimaryDbUrlText().setEnabled(!isBlocklistEnabled);
        getSecondaryDbUrlsText().setEnabled(!isBlocklistEnabled);
        boolean isProxyEnabled = getEnableProxyButton().getSelection();
        getProxyHostText().setEnabled(isProxyEnabled);
        getProxyPortText().setEnabled(isProxyEnabled);
    }

    /**
	 * Initializes the label and button for the configuration: "Enable SafePeer".
	 */
    protected void initEnableSafePeer() {
        Label enableSafePeerLabel = new Label(getComposite(), SWT.NULL);
        enableSafePeerLabel.setText("Enable SafePeer (requires restart):");
        setEnableSafePeerButton(new Button(getComposite(), SWT.CHECK));
        boolean selected = Boolean.valueOf(getConfigEntry().getProperty("enable.safepeer")).booleanValue();
        getEnableSafePeerButton().setSelection(selected);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        getEnableSafePeerButton().setLayoutData(gridData);
        getEnableSafePeerButton().addSelectionListener(this);
    }

    /**
	 * Initializes the label and button for the configuration: "Enable SafePeer".
	 */
    protected void initEnableBlocklistManager() {
        Label enableBlocklistLabel = new Label(getComposite(), SWT.NULL);
        enableBlocklistLabel.setText("Enable Blocklist manager (requires restart):");
        setEnableBlocklistManagerButton(new Button(getComposite(), SWT.CHECK));
        boolean selected = Boolean.valueOf(getConfigEntry().getProperty(SafepeerConfigConstants.ENABLE_BLOCKLIST_MANAGER)).booleanValue();
        getEnableBlocklistManagerButton().setSelection(selected);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        getEnableBlocklistManagerButton().setLayoutData(gridData);
        getEnableBlocklistManagerButton().addSelectionListener(this);
    }

    /**
	 * Initializes the label and text field for the configuration: "Main Database URL".
	 */
    protected void initPrimaryDbUrl() {
        Label primaryUrlLabel = new Label(getComposite(), SWT.NULL);
        primaryUrlLabel.setText("Main Database URL:");
        setPrimaryDbUrlText(new Text(getComposite(), SWT.BORDER));
        getPrimaryDbUrlText().setText(getConfigEntry().getProperty("primary.url"));
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getPrimaryDbUrlText().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and text field for the configuration: "Secondary Databases URLs".
	 */
    protected void initSecondaryDbUrls() {
        Label secondaryUrlsLabel = new Label(getComposite(), SWT.NULL);
        secondaryUrlsLabel.setText("Secondary Databases URLs:");
        setSecondaryDbUrlsText(new Text(getComposite(), SWT.BORDER));
        getSecondaryDbUrlsText().setText(getConfigEntry().getProperty("secondary.urls"));
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getSecondaryDbUrlsText().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and FileChooserField for the configuration: "Cache file".
	 */
    protected void initCacheFile() {
        Label cacheFileLabel = new Label(getComposite(), SWT.NULL);
        cacheFileLabel.setText("Cache file:");
        FileChooserField cacheFileField = new FileChooserField(getComposite());
        cacheFileField.getTextField().setText(getConfigEntry().getProperty("cache.file"));
        setCacheFileChooserField(cacheFileField);
    }

    /**
	 * Initializes the label and FileChooserField for the configuration: "Log file".
	 */
    protected void initLogFile() {
        Label label4 = new Label(getComposite(), SWT.NULL);
        label4.setText("Log file:");
        FileChooserField logFileField = new FileChooserField(getComposite());
        logFileField.getTextField().setText(getConfigEntry().getProperty("log.file"));
        setLogFileChooserField(logFileField);
    }

    /**
	 * Initializes the label and button for the configuration: "Enable logging".
	 */
    protected void initEnableLogging() {
        Label enableLoggingLabel = new Label(getComposite(), SWT.NULL);
        enableLoggingLabel.setText("Enable logging:");
        setEnableLoggingButton(new Button(getComposite(), SWT.CHECK));
        boolean selected = Boolean.valueOf(getConfigEntry().getProperty("enable.logging")).booleanValue();
        getEnableLoggingButton().setSelection(selected);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        getEnableLoggingButton().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and button for the configuration: "Enable asynchronous loading".
	 */
    protected void initEnableAsyncLoading() {
        Label enableLoggingLabel = new Label(getComposite(), SWT.NULL);
        enableLoggingLabel.setText("Enable asynchronous loading:");
        setEnableAsyncLoadingButton(new Button(getComposite(), SWT.CHECK));
        boolean selected = Boolean.valueOf(getConfigEntry().getProperty(AzureusConfigConstants.ASYNC_LOADING)).booleanValue();
        getEnableAsyncLoadingButton().setSelection(selected);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        getEnableAsyncLoadingButton().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and button for the configuration: "Enable periodic update".
	 */
    protected void initEnablePeriodicUpdate() {
        Label enableLoggingLabel = new Label(getComposite(), SWT.NULL);
        enableLoggingLabel.setText("Enable periodic update:");
        setEnablePeriodicUpdateButton(new Button(getComposite(), SWT.CHECK));
        boolean selected = Boolean.valueOf(getConfigEntry().getProperty(AzureusConfigConstants.ENABLE_PERIODIC_UPDATE)).booleanValue();
        getEnablePeriodicUpdateButton().setSelection(selected);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        getEnablePeriodicUpdateButton().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and text field for the configuration: "Periodic update timer".
	 */
    protected void initUpdateTimer() {
        Label updateTimerLabel = new Label(getComposite(), SWT.NULL);
        updateTimerLabel.setText("Periodic update timer:");
        setUpdateTimerText(new Text(getComposite(), SWT.BORDER));
        getUpdateTimerText().setText(getConfigEntry().getProperty(AzureusConfigConstants.UPDATE_TIMER));
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getUpdateTimerText().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and text field for the configuration: "Socket connect timeout".
	 */
    protected void initSocketConnectTimeout() {
        Label socketConnectLabel = new Label(getComposite(), SWT.NULL);
        socketConnectLabel.setText("Socket connect timeout:");
        setSocketConnectTimeoutText(new Text(getComposite(), SWT.BORDER));
        getSocketConnectTimeoutText().setText(getConfigEntry().getProperty(ConfigConstants.SOCKET_CONNECT_TIMEOUT));
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getSocketConnectTimeoutText().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and text field for the configuration: "Socket read timeout".
	 */
    protected void initSocketReadTimeout() {
        Label socketReadLabel = new Label(getComposite(), SWT.NULL);
        socketReadLabel.setText("Socket read timeout:");
        setSocketReadTimeoutText(new Text(getComposite(), SWT.BORDER));
        getSocketReadTimeoutText().setText(getConfigEntry().getProperty(ConfigConstants.SOCKET_READ_TIMEOUT));
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getSocketReadTimeoutText().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and button for the configuration: "Enable proxy".
	 */
    protected void initEnableProxy() {
        Label enableProxyLabel = new Label(getComposite(), SWT.NULL);
        enableProxyLabel.setText("Enable proxy:");
        setEnableProxyButton(new Button(getComposite(), SWT.CHECK));
        boolean selected = Boolean.valueOf(getConfigEntry().getProperty(ConfigConstants.ENABLE_HTTP_PROXY)).booleanValue();
        getEnableProxyButton().setSelection(selected);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 2;
        getEnableProxyButton().setLayoutData(gridData);
        getEnableProxyButton().addSelectionListener(this);
    }

    /**
	 * Initializes the label and text field for the configuration: "Proxy host".
	 */
    protected void initProxyHost() {
        Label proxyHostLabel = new Label(getComposite(), SWT.NULL);
        proxyHostLabel.setText("Proxy host:");
        setProxyHostText(new Text(getComposite(), SWT.BORDER));
        String value = getConfigEntry().getProperty(ConfigConstants.HTTP_PROXY_HOST);
        if (null == value) value = "";
        getProxyHostText().setText(value);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getProxyHostText().setLayoutData(gridData);
    }

    /**
	 * Initializes the label and text field for the configuration: "Proxy port".
	 */
    protected void initProxyPort() {
        Label proxyPortLabel = new Label(getComposite(), SWT.NULL);
        proxyPortLabel.setText("Proxy host:");
        setProxyPortText(new Text(getComposite(), SWT.BORDER));
        String value = getConfigEntry().getProperty(ConfigConstants.HTTP_PROXY_PORT);
        if (null == value) value = "";
        getProxyPortText().setText(value);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 400;
        gridData.horizontalSpan = 2;
        getProxyPortText().setLayoutData(gridData);
    }

    /**
	 * Initializes the button for saving the configuration.
	 */
    protected void initSaveButton() {
        setSaveButton(new Button(getComposite(), SWT.BUTTON2));
        getSaveButton().setText("Save");
        getSaveButton().addSelectionListener(this);
    }

    /**
	 * Saves the properties currently defined in the Gui into the config file.
	 */
    protected void saveProperties() {
        try {
            getConfigEntry().getProperties().put(SafepeerConfigConstants.ENABLE_SAFEPEER, Boolean.toString(getEnableSafePeerButton().getSelection()));
            getConfigEntry().getProperties().put(SafepeerConfigConstants.ENABLE_BLOCKLIST_MANAGER, Boolean.toString(getEnableBlocklistManagerButton().getSelection()));
            getConfigEntry().getProperties().put(SafepeerConfigConstants.MAIN_DB_PROPERTY_KEY, getPrimaryDbUrlText().getText());
            getConfigEntry().getProperties().put(SafepeerConfigConstants.SECONDARY_DB_PROPERTY_KEY, getSecondaryDbUrlsText().getText());
            getConfigEntry().getProperties().put(SafepeerConfigConstants.CACHE_FILE_KEY, getCacheFileChooserField().getTextField().getText());
            getConfigEntry().getProperties().put(ConfigEntry.LOG_FILE_KEY, getLogFileChooserField().getTextField().getText());
            getConfigEntry().getProperties().put(SafepeerConfigConstants.LOGGING_KEY, Boolean.toString(getEnableLoggingButton().getSelection()));
            getConfigEntry().getProperties().put(AzureusConfigConstants.ASYNC_LOADING, Boolean.toString(getEnableAsyncLoadingButton().getSelection()));
            getConfigEntry().getProperties().put(AzureusConfigConstants.ENABLE_PERIODIC_UPDATE, Boolean.toString(getEnablePeriodicUpdateButton().getSelection()));
            getConfigEntry().getProperties().put(AzureusConfigConstants.UPDATE_TIMER, getUpdateTimerText().getText());
            getConfigEntry().getProperties().put(ConfigConstants.SOCKET_CONNECT_TIMEOUT, getSocketConnectTimeoutText().getText());
            getConfigEntry().getProperties().put(ConfigConstants.SOCKET_READ_TIMEOUT, getSocketReadTimeoutText().getText());
            getConfigEntry().getProperties().put(ConfigConstants.ENABLE_HTTP_PROXY, Boolean.toString(getEnableProxyButton().getSelection()));
            getConfigEntry().getProperties().put(ConfigConstants.HTTP_PROXY_HOST, getProxyHostText().getText());
            getConfigEntry().getProperties().put(ConfigConstants.HTTP_PROXY_PORT, getProxyPortText().getText());
            List missingKeys = new ArrayList(getConfigEntry().getProperties().keySet());
            File configFile = new File(getConfigEntry().getConfigPath(), SafepeerConfigConstants.CONFIG_FILE_NAME);
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line = null;
            while (null != (line = reader.readLine())) {
                if (line.indexOf("=") < 0) {
                    buffer.append(line);
                } else {
                    StringTokenizer tokenizer = new StringTokenizer(line, "=");
                    String key = tokenizer.nextToken();
                    if (getConfigEntry().getProperties().keySet().contains(key)) {
                        missingKeys.remove(key);
                        addPropertyToBuffer(getConfigEntry(), buffer, key);
                    } else {
                        buffer.append(line);
                    }
                }
                buffer.append(System.getProperty("line.separator"));
            }
            reader.close();
            for (Iterator iter = missingKeys.iterator(); iter.hasNext(); ) {
                addPropertyToBuffer(getConfigEntry(), buffer, iter.next().toString());
                buffer.append(System.getProperty("line.separator"));
            }
            FileWriter writer = new FileWriter(configFile);
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Adds the property with the given key to the buffer.
	 * @param aConfigEntry  the config entry holding the properties.
	 * @param aBuffer  the buffer.
	 * @param aKey  the key.
	 */
    protected void addPropertyToBuffer(ConfigEntry aConfigEntry, StringBuffer aBuffer, String aKey) {
        String value = getConfigEntry().getProperty(aKey);
        aBuffer.append(aKey);
        aBuffer.append("=");
        aBuffer.append(value);
    }

    /**
	 * Disables all the components of the Gui besides the button for enabling/disabling SafePeer.
	 * If "Enable SafePeer" is unchecked, this method should be called.
	 */
    protected void disableComponents() {
        getPrimaryDbUrlText().setEnabled(false);
        getSecondaryDbUrlsText().setEnabled(false);
        getCacheFileChooserField().setEnabled(false);
        getLogFileChooserField().setEnabled(false);
        getEnableLoggingButton().setEnabled(false);
        getEnableAsyncLoadingButton().setEnabled(false);
        getEnablePeriodicUpdateButton().setEnabled(false);
        getUpdateTimerText().setEnabled(false);
        getSocketConnectTimeoutText().setEnabled(false);
        getSocketReadTimeoutText().setEnabled(false);
    }

    /**
	 * Enables all the components of the Gui besides the button for enabling/disabling SafePeer.
	 * If "Enable SafePeer" is checked, this method should be called.
	 */
    protected void enableComponents() {
        getPrimaryDbUrlText().setEnabled(true);
        getSecondaryDbUrlsText().setEnabled(true);
        getCacheFileChooserField().setEnabled(true);
        getLogFileChooserField().setEnabled(true);
        getEnableLoggingButton().setEnabled(true);
        getEnableAsyncLoadingButton().setEnabled(true);
        getEnablePeriodicUpdateButton().setEnabled(true);
        getUpdateTimerText().setEnabled(true);
        getSocketConnectTimeoutText().setEnabled(true);
        getSocketReadTimeoutText().setEnabled(true);
    }

    /**
	 * Gets the button for the configuration: "Enable logging".
	 * @return  a Button as check box (SWT.CHECK).
	 */
    protected Button getEnableLoggingButton() {
        return enableLoggingButton;
    }

    /**
	 * Sets the button for the configuration: "Enable logging".
	 * @param aButton  a Button as check box (SWT.CHECK).
	 */
    protected void setEnableLoggingButton(Button aButton) {
        enableLoggingButton = aButton;
    }

    /**
	 * Gets the text field for the configuration: "Main Database URL".
	 * @return  a Text composite.
	 */
    protected Text getPrimaryDbUrlText() {
        return primaryDbUrlText;
    }

    /**
	 * Sets the text field for the configuration: "Main Database URL".
	 * @param aTextComposite  a Text composite.
	 */
    protected void setPrimaryDbUrlText(Text aTextComposite) {
        primaryDbUrlText = aTextComposite;
    }

    /**
	 * Gets the text field for the configuration: "Secondary Databases URLs".
	 * @return  a Text composite.
	 */
    protected Text getSecondaryDbUrlsText() {
        return secondaryDbUrlsText;
    }

    /**
	 * Sets the text field for the configuration: "Secondary Databases URLs".
	 * @param aTextComposite  a Text composite.
	 */
    protected void setSecondaryDbUrlsText(Text aTextComposite) {
        secondaryDbUrlsText = aTextComposite;
    }

    /**
	 * Gets the FileChooserField for the cache file.
	 * @return  a FileChooserField.
	 */
    protected FileChooserField getCacheFileChooserField() {
        return cacheFileChooserField;
    }

    /**
	 * Sets the FileChooserField for the cache file.
	 * @param aFileChooserField  a FileChooserField.
	 */
    protected void setCacheFileChooserField(FileChooserField aFileChooserField) {
        cacheFileChooserField = aFileChooserField;
    }

    /**
	 * Gets the FileChooserField for the log file.
	 * @return  a FileChooserField.
	 */
    protected FileChooserField getLogFileChooserField() {
        return logFileChooserField;
    }

    /**
	 * Sets the FileChooserField for the cache file.
	 * @param aFileChooserField  a FileChooserField.
	 */
    protected void setLogFileChooserField(FileChooserField aFileChooserField) {
        logFileChooserField = aFileChooserField;
    }

    /**
	 * Gets the button for the configuration: "Enable SafePeer".
	 * @return  a Button as check box (SWT.CHECK).
	 */
    protected Button getEnableSafePeerButton() {
        return enableSafePeerButton;
    }

    /**
	 * Sets the button for the configuration: "Enable SafePeer".
	 * @param aButton  a Button as check box (SWT.CHECK).
	 */
    protected void setEnableSafePeerButton(Button aButton) {
        enableSafePeerButton = aButton;
    }

    /**
	 * Gets the button for saving the configuration.
	 * @return  a Button composite.
	 */
    protected Button getSaveButton() {
        return saveButton;
    }

    /**
	 * Sets the button for saving the configuration.
	 * @param aButton  a Button composite.
	 */
    protected void setSaveButton(Button aButton) {
        saveButton = aButton;
    }

    /**
	 * Either saves the configuration or enables/disables all components, corresponding
	 * to the event that triggered this method.
	 * @param anEvent  the event that triggered this method, must not be null.
	 */
    public void widgetSelected(SelectionEvent anEvent) {
        if (null != anEvent && null != anEvent.getSource()) {
            if (anEvent.getSource().equals(getSaveButton())) {
                saveProperties();
            } else if (anEvent.getSource().equals(getEnableSafePeerButton())) {
                if (getEnableSafePeerButton().getSelection()) {
                    enableComponents();
                } else {
                    disableComponents();
                }
            } else if (anEvent.getSource().equals(getEnableBlocklistManagerButton())) {
                boolean isBlocklistEnabled = getEnableBlocklistManagerButton().getSelection();
                getPrimaryDbUrlText().setEnabled(!isBlocklistEnabled);
                getSecondaryDbUrlsText().setEnabled(!isBlocklistEnabled);
            } else if (anEvent.getSource().equals(getEnableProxyButton())) {
                boolean isProxyEnabled = getEnableProxyButton().getSelection();
                getProxyHostText().setEnabled(isProxyEnabled);
                getProxyPortText().setEnabled(isProxyEnabled);
            }
        }
    }

    /**
	 * Does nothing yet, needed as class implements SelectionListener.
	 * @param anEvent  the event that triggered this method.  
	 */
    public void widgetDefaultSelected(SelectionEvent anEvent) {
    }

    /**
	 * Gets the ConfigEntry to be used for the configuration.
	 * @return  a ConfigEntry object.
	 */
    protected ConfigEntry getConfigEntry() {
        return configEntry;
    }

    /**
	 * Sets the ConfigEntry to be used for the configuration.
	 * @param anEntry  a ConfigEntry object.
	 */
    protected void setConfigEntry(ConfigEntry anEntry) {
        configEntry = anEntry;
    }

    /**
	 * Gets the parent composite of the Gui's main composite.
	 * @return  a Composite, cannot be null.
	 */
    protected Composite getParent() {
        return parent;
    }

    /**
	 * Sets the parent composite of the Gui's main composite.
	 * @param aParent  a Composite, must not be null.
	 */
    protected void setParent(Composite aParent) {
        parent = aParent;
    }

    /**
	 * Gets the main composite of the Gui.
	 * @return  a Composite, cannot be null.
	 */
    public Composite getComposite() {
        return composite;
    }

    /**
	 * Sets the main composite of the Gui.
	 * @param aComposite  a Composite, must not be null.
	 */
    protected void setComposite(Composite aComposite) {
        composite = aComposite;
    }

    /**
	 * Main method for testing purpose.
	 * @param someArgs  not used, might be null.
	 */
    public static void main(String[] someArgs) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(640, 480);
        shell.setLayout(new FillLayout());
        ConfigEntry entry = new ConfigEntry("C:\\CVS_Projects\\ludo_sources", SafepeerConfigConstants.CONFIG_FILE_NAME);
        Composite parent = new Composite(shell, SWT.NULL);
        GridLayout layout = new GridLayout();
        parent.setLayout(layout);
        SafePeerSwtGui view = new SafePeerSwtGui(entry, parent);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    /**
	 * @return Returns the enableAsyncLoadingButton.
	 */
    public Button getEnableAsyncLoadingButton() {
        return enableAsyncLoadingButton;
    }

    /**
	 * @param enableAsyncLoadingButton The enableAsyncLoadingButton to set.
	 */
    protected void setEnableAsyncLoadingButton(Button enableAsyncLoadingButton) {
        this.enableAsyncLoadingButton = enableAsyncLoadingButton;
    }

    /**
	 * @return Returns the enablePeriodicUpdateButton.
	 */
    public Button getEnablePeriodicUpdateButton() {
        return enablePeriodicUpdateButton;
    }

    /**
	 * @param enablePeriodicUpdateButton The enablePeriodicUpdateButton to set.
	 */
    protected void setEnablePeriodicUpdateButton(Button enablePeriodicUpdateButton) {
        this.enablePeriodicUpdateButton = enablePeriodicUpdateButton;
    }

    /**
	 * @return Returns the socketConnectTimeoutText.
	 */
    protected Text getSocketConnectTimeoutText() {
        return socketConnectTimeoutText;
    }

    /**
	 * @param socketConnectTimeoutText The socketConnectTimeoutText to set.
	 */
    protected void setSocketConnectTimeoutText(Text socketConnectTimeoutText) {
        this.socketConnectTimeoutText = socketConnectTimeoutText;
    }

    /**
	 * @return Returns the socketReadTimeoutText.
	 */
    protected Text getSocketReadTimeoutText() {
        return socketReadTimeoutText;
    }

    /**
	 * @param socketReadTimeoutText The socketReadTimeoutText to set.
	 */
    protected void setSocketReadTimeoutText(Text socketReadTimeoutText) {
        this.socketReadTimeoutText = socketReadTimeoutText;
    }

    /**
	 * @return Returns the updateTimerText.
	 */
    protected Text getUpdateTimerText() {
        return updateTimerText;
    }

    /**
	 * @param updateTimerText The updateTimerText to set.
	 */
    protected void setUpdateTimerText(Text updateTimerText) {
        this.updateTimerText = updateTimerText;
    }

    /**
	 * @return Returns the enableBlocklistManagerButton.
	 */
    protected Button getEnableBlocklistManagerButton() {
        return enableBlocklistManagerButton;
    }

    /**
	 * @param enableBlocklistManagerButton The enableBlocklistManagerButton to set.
	 */
    protected void setEnableBlocklistManagerButton(Button enableBlocklistManagerButton) {
        this.enableBlocklistManagerButton = enableBlocklistManagerButton;
    }

    /**
	 * Gets the Text for the proxy host.
	 * @return  a Text.
	 */
    protected Text getProxyHostText() {
        return proxyHostText;
    }

    /**
	 * Sets the Text for the proxy host.
	 * @param aText a Text.
	 */
    protected void setProxyHostText(Text aText) {
        proxyHostText = aText;
    }

    /**
	 * Gets the Text for the proxy port.
	 * @return  a Text.
	 */
    protected Text getProxyPortText() {
        return proxyPortText;
    }

    /**
	 * Sets the Text for the proxy port.
	 * @param aText a Text.
	 */
    protected void setProxyPortText(Text aText) {
        proxyPortText = aText;
    }

    /**
	 * Gets the Button for enabling/disabling the proxy.
	 * @return  a Button.
	 */
    protected Button getEnableProxyButton() {
        return enableProxyButton;
    }

    /**
	 * Sets the Button for enabling/disabling the proxy.
	 * @param aButton  a Button.
	 */
    protected void setEnableProxyButton(Button aButton) {
        enableProxyButton = aButton;
    }
}
