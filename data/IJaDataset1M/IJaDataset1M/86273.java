package wsl.mdn.admin;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.Vector;
import wsl.fw.resource.ResId;
import wsl.fw.help.HelpId;
import wsl.fw.gui.WslButtonPanel;
import wsl.fw.gui.WslComboBox;
import wsl.fw.gui.GuiConst;
import wsl.fw.gui.WslButton;
import wsl.fw.gui.WslTabChildPanel;
import wsl.fw.datasource.DataSource;
import wsl.fw.datasource.DataManager;
import wsl.fw.datasource.Query;
import wsl.fw.datasource.RecordSet;
import wsl.fw.util.Util;
import wsl.fw.util.Log;
import wsl.fw.util.Type;
import wsl.mdn.dataview.DataTransfer;
import pv.jfcx.JPVCalendar;

/**
 *
 */
public class MaintainTransferSchedulePanel extends WslButtonPanel implements ChangeListener, ActionListener {

    private static final String _ident = "$Date: 2002/06/11 23:35:35 $  $Revision: 1.1.1.1 $ " + "$Archive: /Mobile Data Now/Source/wsl/mdn/admin/MaintainTransferSchedulePanel.java $ ";

    public static final ResId PANEL_TITLE = new ResId("MaintainTransferSchedulePanel.PanelTitle");

    public static final ResId BUTTON_CLOSE = new ResId("MaintainTransferSchedulePanel.button.Close");

    public static final ResId LABEL_DATA_TRANSFER = new ResId("MaintainTransferSchedulePanel.label.DataTransfer");

    public static final ResId COMBO_ITEM_ALL = new ResId("MaintainTransferSchedulePanel.combo.ItemAll");

    public static final ResId BUTTON_HELP = new ResId("OkPanel.button.Help");

    public static final ResId ERR_DATATRANSFER_COMBO = new ResId("MaintainTransferSchedulePanel.error.dataTransferCombo");

    public static final HelpId HID_MAINT_TRANSFERSCHEDULE = new HelpId("mdn.admin.MaintainTransferSchedulePanel");

    private WslButton _btnClose = new WslButton(BUTTON_CLOSE.getText(), GuiConst.BTN_WIDTH, this);

    private WslComboBox _cmbDataTransfers = new WslComboBox(200);

    private JTabbedPane _tabPanel;

    private MonthCalendarTabPane _monthCalendarTabPane;

    private SchedulingsTabPane _schedulingsTabPane;

    private Vector _calendarEvents;

    /**
     * Constructor creates screen.
     */
    public MaintainTransferSchedulePanel() {
        super(VERTICAL);
        this.setPanelTitle(PANEL_TITLE.getText());
        initControls();
        buildDataTransfersCombo();
    }

    /**
     * Initialise controls.
     */
    private void initControls() {
        _monthCalendarTabPane = new MonthCalendarTabPane(this, Type.NULL_INTEGER);
        _schedulingsTabPane = new SchedulingsTabPane(this);
        _schedulingsTabPane.postCreate();
        getMainPanel().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets.top = GuiConst.DEFAULT_INSET;
        gbc.insets.left = GuiConst.DEFAULT_INSET;
        gbc.insets.right = GuiConst.DEFAULT_INSET;
        _tabPanel = new JTabbedPane();
        _tabPanel.addChangeListener(this);
        _tabPanel.addTab(MonthCalendarTabPane.TITLE_MONTH_CALENDAR.getText(), _monthCalendarTabPane);
        _tabPanel.addTab(SchedulingsTabPane.LABEL_TITLE.getText(), _schedulingsTabPane);
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        getMainPanel().add(_tabPanel, gbc);
        gbc.insets.bottom = GuiConst.DEFAULT_INSET;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        getMainPanel().add(new JLabel(LABEL_DATA_TRANSFER.getText()), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        getMainPanel().add(_cmbDataTransfers, gbc);
        _btnClose.setIcon(Util.resourceIcon(GuiConst.FW_IMAGE_PATH + "close.gif"));
    }

    private void buildDataTransfersCombo() {
        DataSource sysDs = DataManager.getSystemDS();
        Query q = new Query(DataTransfer.ENT_DATATRANSFER);
        try {
            RecordSet rs = sysDs.select(q);
            _cmbDataTransfers.addItem(COMBO_ITEM_ALL.getText());
            _cmbDataTransfers.buildFromRecordSet(rs);
            _cmbDataTransfers.selectItem(COMBO_ITEM_ALL.getText());
            _cmbDataTransfers.addActionListener(this);
        } catch (Exception e) {
            wsl.fw.gui.GuiManager.showErrorDialog(this, ERR_DATATRANSFER_COMBO.getText(), e);
        }
    }

    /**
     * Button clicked.
     */
    public void actionPerformed(ActionEvent ev) {
        try {
            if (ev.getSource().equals(_btnClose)) closePanel(); else if (ev.getSource().equals(_cmbDataTransfers)) dataTransfersComboSelected();
        } catch (Exception e) {
            wsl.fw.gui.GuiManager.showErrorDialog(this, wsl.mdn.common.MdnAdminConst.ERR_UNHANDLED.getText(), e);
            Log.error(wsl.mdn.common.MdnAdminConst.ERR_UNHANDLED.getText(), e);
        }
    }

    public void stateChanged(ChangeEvent e) {
        WslTabChildPanel tabPane = (WslTabChildPanel) _tabPanel.getSelectedComponent();
        if (tabPane == _monthCalendarTabPane) {
            removeAllButtons();
            WslButton monthCalendarTabPaneButtons[] = _monthCalendarTabPane.getButtons();
            for (int i = 0; i < monthCalendarTabPaneButtons.length; i++) addButton(monthCalendarTabPaneButtons[i]);
            addHelpButton(BUTTON_HELP.getText(), HID_MAINT_TRANSFERSCHEDULE);
            addButton(_btnClose);
            _monthCalendarTabPane.buildCalendarEvents();
        } else if (tabPane == _schedulingsTabPane) {
            removeAllButtons();
            WslButton schedulingsTabPaneButtons[] = _schedulingsTabPane.getButtons();
            for (int i = 0; i < schedulingsTabPaneButtons.length; i++) addButton(schedulingsTabPaneButtons[i]);
            addHelpButton(BUTTON_HELP.getText(), HID_MAINT_TRANSFERSCHEDULE);
            addButton(_btnClose);
        }
    }

    private void dataTransfersComboSelected() {
        int dataTransferId;
        if (_cmbDataTransfers.getSelectedItem().equals(COMBO_ITEM_ALL.getText())) dataTransferId = Type.NULL_INTEGER; else dataTransferId = ((DataTransfer) (_cmbDataTransfers.getSelectedItem())).getId();
        _monthCalendarTabPane.buildCalendarEvents(dataTransferId);
        _schedulingsTabPane.buildTable(dataTransferId);
    }

    protected void selectDataTransfer(DataTransfer dataTransfer) {
        String cmb = _cmbDataTransfers.getSelectedItem().toString();
        _cmbDataTransfers.selectItem(COMBO_ITEM_ALL.getText());
        if (dataTransfer.toString().equals(cmb)) {
            _cmbDataTransfers.selectItem(cmb);
        }
    }

    protected void selectDataTransfer(int dataTransferId) {
        if (_cmbDataTransfers.getSelectedItem() == null || _cmbDataTransfers.getSelectedItem().equals(COMBO_ITEM_ALL.getText()) || ((DataTransfer) (_cmbDataTransfers.getSelectedItem())).getId() != dataTransferId) {
            _cmbDataTransfers.selectItem(COMBO_ITEM_ALL.getText());
            return;
        }
    }

    public void refreshMonthCalendar() {
        _monthCalendarTabPane.buildCalendarEvents();
    }

    /**
     * Return the preferred size
     */
    public Dimension getPreferredSize() {
        return new Dimension(750, 680);
    }
}
