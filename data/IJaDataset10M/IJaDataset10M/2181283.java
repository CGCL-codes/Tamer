package gov.sns.apps.viewers.ringbpmviewer;

import java.net.*;
import java.io.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import gov.sns.ca.*;
import gov.sns.application.*;
import gov.sns.tools.xml.*;
import gov.sns.tools.scan.SecondEdition.UpdatingEventController;
import gov.sns.apps.viewers.ringbpmviewer.utils.*;

/**
 *  RingbpmviewerDocument is a custom XalDocument for Ringbpmviewer application.
 *  The document manages the data that is displayed in the window.
 *
 *@author     shishlo
 *@created    October 13, 2005
 */
public class RingbpmviewerDocument extends XalDocument {

    static {
        ChannelFactory.defaultFactory().init();
    }

    private JTextField messageTextLocal = new JTextField();

    UpdatingEventController updatingController = new UpdatingEventController();

    private UpdatingEventController ucContent = new UpdatingEventController();

    private JTabbedPane mainTabbedPanel = new JTabbedPane();

    private JPanel viewRingBPMPanel = null;

    private RingBPMsController ringBPMsController = null;

    private JPanel viewWaveFormPanel = null;

    RingBPMsWaveFormController ringBPMsWaveFormController = null;

    private JPanel viewHebtBPMPanel = null;

    private TrLineBPMsController hebtBPMsController = null;

    private JPanel viewRtbtBPMPanel = null;

    private TrLineBPMsController rtbtBPMsController = null;

    private JPanel preferencesPanel = new JPanel();

    private JButton setFont_PrefPanel_Button = new JButton("Set Font Size");

    private JSpinner fontSize_PrefPanel_Spinner = new JSpinner(new SpinnerNumberModel(7, 7, 26, 1));

    private JLabel timeDealyUC_Label = new JLabel("time delay for graphics update [sec]", JLabel.LEFT);

    private JSpinner timeDealyUC_Spinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.1));

    private Font globalFont = new Font("Monospaced", Font.BOLD, 10);

    private int ACTIVE_PANEL = 0;

    private int VIEW_RING_BPM_PANEL = 0;

    private int VIEW_WAVEFORM_PANEL = 1;

    private int VIEW_HEBT_BPM_PANEL = 2;

    private int VIEW_RTBT_BPM_PANEL = 3;

    private int PREFERENCES_PANEL = 4;

    private static DateAndTimeText dateAndTime = new DateAndTimeText();

    private String dataRootName = "RING_BPM_VIEWER";

    /**
	 *  Create a new empty RingbpmviewerDocument
	 */
    public RingbpmviewerDocument() {
        updatingController.setUpdateTime(1.0);
        ucContent.setUpdateTime(0.3);
        ringBPMsController = new RingBPMsController(updatingController, ucContent);
        viewRingBPMPanel = ringBPMsController.getPanel();
        ringBPMsWaveFormController = new RingBPMsWaveFormController(updatingController, ucContent);
        viewWaveFormPanel = ringBPMsWaveFormController.getPanel();
        hebtBPMsController = new TrLineBPMsController(updatingController, ucContent);
        viewHebtBPMPanel = hebtBPMsController.getPanel();
        rtbtBPMsController = new TrLineBPMsController(updatingController, ucContent);
        viewRtbtBPMPanel = rtbtBPMsController.getPanel();
        mainTabbedPanel.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JTabbedPane tbp = (JTabbedPane) e.getSource();
                setActivePanel(tbp.getSelectedIndex());
            }
        });
        makePreferencesPanel();
        mainTabbedPanel.add("Ring BPMs X,Y,Amp", viewRingBPMPanel);
        mainTabbedPanel.add("Ring TBT X,Y,Amp", viewWaveFormPanel);
        mainTabbedPanel.add("HEBT BPMs X,Y,Amp", viewHebtBPMPanel);
        mainTabbedPanel.add("RTBT BPMs X,Y,Amp", viewRtbtBPMPanel);
        mainTabbedPanel.add("Preferences", preferencesPanel);
        mainTabbedPanel.setSelectedIndex(0);
        URL init_url = this.getClass().getResource("config/ring_bpms.xml");
        XmlDataAdaptor root_da = XmlDataAdaptor.adaptorForUrl(init_url, false);
        XmlDataAdaptor init_da = root_da.childAdaptor("LINES");
        XmlDataAdaptor ring_bpms_da = init_da.childAdaptor("RING_BPM_TBT_PVs");
        XmlDataAdaptor hebt_bpms_da = init_da.childAdaptor("HEBT_BPM_PVs");
        XmlDataAdaptor rtbt_bpms_da = init_da.childAdaptor("RTBT_BPM_PVs");
        ringBPMsController.init(ring_bpms_da);
        hebtBPMsController.init(hebt_bpms_da);
        rtbtBPMsController.init(rtbt_bpms_da);
        ringBPMsWaveFormController.getListenToEPICS_Button().setModel(ringBPMsController.getListenToEPICS_Button().getModel());
        ringBPMsController.setListenToEPICS(false);
        ringBPMsWaveFormController.init(ringBPMsController.getRingBPMset());
    }

    /**
	 *  Create a new document loaded from the URL file
	 *
	 *@param  url  The URL of the file to load into the new document.
	 */
    public RingbpmviewerDocument(URL url) {
        this();
        if (url == null) {
            return;
        }
        setSource(url);
        readRingbpmviewerDocument(url);
        if (url.getProtocol().equals("jar")) {
            return;
        }
        setHasChanges(true);
    }

    /**
	 *  Make a main window by instantiating the RingbpmviewerWindow window.
	 */
    @Override
    public void makeMainWindow() {
        mainWindow = new RingbpmviewerWindow(this);
        getRingbpmviewerWindow().setJComponent(mainTabbedPanel);
        messageTextLocal = getRingbpmviewerWindow().getMessageTextField();
        ringBPMsWaveFormController.setMessageTextLocal(messageTextLocal);
        ringBPMsWaveFormController.setOnwnerFrame(getRingbpmviewerWindow());
        fontSize_PrefPanel_Spinner.setValue(new Integer(globalFont.getSize()));
        setFontForAll(globalFont);
        timeDealyUC_Spinner.setValue(new Double(updatingController.getUpdateTime()));
        updatingController.setUpdateTime(((Double) timeDealyUC_Spinner.getValue()).doubleValue());
        JTextField timeTxt_temp = dateAndTime.getNewTimeTextField();
        timeTxt_temp.setHorizontalAlignment(JTextField.CENTER);
        getRingbpmviewerWindow().addTimeStamp(timeTxt_temp);
        mainWindow.setSize(new Dimension(700, 600));
    }

    /**
	 *  Dispose of RingbpmviewerDocument resources. This method overrides an empty
	 *  superclass method.
	 */
    @Override
    protected void freeCustomResources() {
        cleanUp();
    }

    /**
	 *  Reads the content of the document from the specified URL.
	 *
	 *@param  url  Description of the Parameter
	 */
    public void readRingbpmviewerDocument(URL url) {
        XmlDataAdaptor readAdp = null;
        readAdp = XmlDataAdaptor.adaptorForUrl(url, false);
        if (readAdp != null) {
            XmlDataAdaptor ringbpmviewerData_Adaptor = readAdp.childAdaptor(dataRootName);
            if (ringbpmviewerData_Adaptor != null) {
                cleanUp();
                setTitle(ringbpmviewerData_Adaptor.stringValue("title"));
                XmlDataAdaptor params_font = ringbpmviewerData_Adaptor.childAdaptor("font");
                int font_size = params_font.intValue("size");
                int style = params_font.intValue("style");
                String font_Family = params_font.stringValue("name");
                globalFont = new Font(font_Family, style, font_size);
                fontSize_PrefPanel_Spinner.setValue(new Integer(font_size));
                setFontForAll(globalFont);
                XmlDataAdaptor params_da = ringbpmviewerData_Adaptor.childAdaptor("shared_parameters");
                updatingController.setUpdateTime(params_da.doubleValue("update_time"));
                XmlDataAdaptor ring_bpms_da = ringbpmviewerData_Adaptor.childAdaptor("RING_BPMs");
                ringBPMsController.readData(ring_bpms_da);
                XmlDataAdaptor hebt_bpms_da = ringbpmviewerData_Adaptor.childAdaptor("HEBT_BPMs");
                hebtBPMsController.readData(hebt_bpms_da);
                XmlDataAdaptor rtbt_bpms_da = ringbpmviewerData_Adaptor.childAdaptor("RTBT_BPMs");
                rtbtBPMsController.readData(rtbt_bpms_da);
            }
            ringBPMsWaveFormController.init(ringBPMsController.getRingBPMset());
        }
    }

    /**
	 *  Save the RingbpmviewerDocument document to the specified URL.
	 *
	 *@param  url  Description of the Parameter
	 */
    @Override
    public void saveDocumentAs(URL url) {
        XmlDataAdaptor da = XmlDataAdaptor.newEmptyDocumentAdaptor();
        XmlDataAdaptor ringbpmviewerData_Adaptor = da.createChild(dataRootName);
        ringbpmviewerData_Adaptor.setValue("title", url.getFile());
        XmlDataAdaptor params_font = ringbpmviewerData_Adaptor.createChild("font");
        params_font.setValue("name", globalFont.getFamily());
        params_font.setValue("style", globalFont.getStyle());
        params_font.setValue("size", globalFont.getSize());
        XmlDataAdaptor params_da = ringbpmviewerData_Adaptor.createChild("shared_parameters");
        params_da.setValue("update_time", updatingController.getUpdateTime());
        XmlDataAdaptor ring_bpms_da = ringbpmviewerData_Adaptor.createChild("RING_BPMs");
        ringBPMsController.dumpData(ring_bpms_da);
        XmlDataAdaptor hebt_bpms_da = ringbpmviewerData_Adaptor.createChild("HEBT_BPMs");
        hebtBPMsController.dumpData(hebt_bpms_da);
        XmlDataAdaptor rtbt_bpms_da = ringbpmviewerData_Adaptor.createChild("RTBT_BPMs");
        rtbtBPMsController.dumpData(rtbt_bpms_da);
        try {
            ringbpmviewerData_Adaptor.writeTo(new File(url.getFile()));
            setHasChanges(true);
        } catch (IOException e) {
            System.out.println("IOException e=" + e);
        }
    }

    /**
	 *  Edit preferences for the document.
	 */
    void editPreferences() {
        mainTabbedPanel.setSelectedIndex(PREFERENCES_PANEL);
        setActivePanel(PREFERENCES_PANEL);
    }

    /**
	 *  Convenience method for getting the RingbpmviewerWindow window. It is the
	 *  cast to the proper subclass of XalWindow. This allows me to avoid casting
	 *  the window every time I reference it.
	 *
	 *@return    The main window cast to its dynamic runtime class
	 */
    private RingbpmviewerWindow getRingbpmviewerWindow() {
        return (RingbpmviewerWindow) mainWindow;
    }

    /**
	 *  Register actions for the menu items and toolbar.
	 *
	 *@param  commander  Description of the Parameter
	 */
    @Override
    protected void customizeCommands(Commander commander) {
    }

    /**
	 *  Description of the Method
	 */
    private void makePreferencesPanel() {
        fontSize_PrefPanel_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        timeDealyUC_Spinner.setAlignmentX(JSpinner.CENTER_ALIGNMENT);
        JPanel tmp_0 = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        tmp_0.add(fontSize_PrefPanel_Spinner);
        tmp_0.add(setFont_PrefPanel_Button);
        JPanel tmp_1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        tmp_1.add(timeDealyUC_Spinner);
        tmp_1.add(timeDealyUC_Label);
        JPanel tmp_2 = new JPanel(new GridLayout(0, 1));
        tmp_2.add(tmp_0);
        tmp_2.add(tmp_1);
        preferencesPanel.setLayout(new BorderLayout());
        preferencesPanel.add(tmp_2, BorderLayout.NORTH);
        setFont_PrefPanel_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int fnt_size = ((Integer) fontSize_PrefPanel_Spinner.getValue()).intValue();
                globalFont = new Font(globalFont.getFamily(), globalFont.getStyle(), fnt_size);
                setFontForAll(globalFont);
                int h = getRingbpmviewerWindow().getHeight();
                int w = getRingbpmviewerWindow().getWidth();
                getRingbpmviewerWindow().validate();
            }
        });
        timeDealyUC_Spinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evnt) {
                updatingController.setUpdateTime(((Double) timeDealyUC_Spinner.getValue()).doubleValue());
            }
        });
    }

    /**
	 *  Clean up the document content
	 */
    private void cleanUp() {
        cleanMessageTextField();
    }

    /**
	 *  Description of the Method
	 */
    private void cleanMessageTextField() {
        messageTextLocal.setText(null);
        messageTextLocal.setForeground(Color.red);
    }

    /**
	 *  Sets the fontForAll attribute of the RingbpmviewerDocument object
	 *
	 *@param  fnt  The new fontForAll value
	 */
    private void setFontForAll(Font fnt) {
        messageTextLocal.setFont(fnt);
        fontSize_PrefPanel_Spinner.setValue(new Integer(fnt.getSize()));
        setFont_PrefPanel_Button.setFont(fnt);
        fontSize_PrefPanel_Spinner.setFont(fnt);
        ((JSpinner.DefaultEditor) fontSize_PrefPanel_Spinner.getEditor()).getTextField().setFont(fnt);
        globalFont = fnt;
        ringBPMsController.setFont(fnt);
        ringBPMsWaveFormController.setFont(fnt);
        hebtBPMsController.setFont(fnt);
        rtbtBPMsController.setFont(fnt);
        timeDealyUC_Label.setFont(fnt);
        timeDealyUC_Spinner.setFont(fnt);
        ((JSpinner.DefaultEditor) timeDealyUC_Spinner.getEditor()).getTextField().setFont(fnt);
    }

    /**
	 *  Sets the activePanel attribute of the RingbpmviewerDocument object
	 *
	 *@param  newActPanelInd  The new activePanel value
	 */
    private void setActivePanel(int newActPanelInd) {
        int oldActPanelInd = ACTIVE_PANEL;
        if (oldActPanelInd == newActPanelInd) {
            return;
        }
        if (oldActPanelInd == VIEW_RING_BPM_PANEL) {
        } else if (oldActPanelInd == VIEW_WAVEFORM_PANEL) {
            ringBPMsWaveFormController.setShowing(false);
        } else if (oldActPanelInd == VIEW_HEBT_BPM_PANEL) {
        } else if (oldActPanelInd == VIEW_RTBT_BPM_PANEL) {
        } else if (oldActPanelInd == PREFERENCES_PANEL) {
        }
        if (newActPanelInd == VIEW_RING_BPM_PANEL) {
        } else if (newActPanelInd == VIEW_WAVEFORM_PANEL) {
            ringBPMsWaveFormController.setShowing(true);
            ringBPMsWaveFormController.updateWFsetOnGraphs();
        } else if (newActPanelInd == VIEW_RTBT_BPM_PANEL) {
        } else if (newActPanelInd == PREFERENCES_PANEL) {
        } else if (newActPanelInd == PREFERENCES_PANEL) {
        }
        ACTIVE_PANEL = newActPanelInd;
        cleanMessageTextField();
    }
}

/**
 *  Description of the Class
 *
 *@author     shishlo
 *@created    July 8, 2004
 *@version
 */
class DateAndTimeText {

    private SimpleDateFormat dFormat = null;

    private JFormattedTextField dateTimeField = null;

    /**
	 *  Constructor for the DateAndTimeText object
	 */
    public DateAndTimeText() {
        dFormat = new SimpleDateFormat("'Time': MM.dd.yy HH:mm ");
        dateTimeField = new JFormattedTextField(dFormat);
        dateTimeField.setEditable(false);
        Runnable timer = new Runnable() {

            public void run() {
                while (true) {
                    dateTimeField.setValue(new Date());
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        Thread thr = new Thread(timer);
        thr.start();
    }

    /**
	 *  Returns the time attribute of the DateAndTimeText object
	 *
	 *@return    The time value
	 */
    protected String getTime() {
        return dateTimeField.getText();
    }

    /**
	 *  Returns the timeTextField attribute of the DateAndTimeText object
	 *
	 *@return    The timeTextField value
	 */
    protected JFormattedTextField getTimeTextField() {
        return dateTimeField;
    }

    /**
	 *  Returns the newTimeTextField attribute of the DateAndTimeText object
	 *
	 *@return    The newTimeTextField value
	 */
    protected JTextField getNewTimeTextField() {
        JTextField newText = new JTextField();
        newText.setDocument(dateTimeField.getDocument());
        newText.setEditable(false);
        return newText;
    }
}
