package jitt64.swing;

import jitt64.userAction;
import jitt64.Shared;
import jitt64.Instrument;
import jitt64.table.InstrTable;
import jitt64.gt2.Gt2Instrument;
import jitt64.midi.midiPlay;
import jitt64.swing.table.InstrTableCellRenderer;
import jitt64.swing.table.DataTableModelInstr;
import jitt64.swing.table.RowHeaderRenderer;
import jitt64.swing.table.XCellEditor;
import jitt64.swing.popup.JDialogDN;
import jitt64.swing.popup.JDialogRep;
import jitt64.swing.popup.JDialogFixedPulse;
import jitt64.swing.popup.JDialogFixedFreq;
import jitt64.swing.popup.JDialogFixedFilter;
import jitt64.swing.popup.JDialogFilterRes;
import jitt64.swing.popup.JDialogFilterType;
import jitt64.swing.popup.JDialogRelFilter;
import jitt64.swing.popup.JDialogRelPulse;
import jitt64.swing.popup.JDialogRelNote;
import jitt64.swing.popup.JDialogRelFreq;
import jitt64.swing.popup.JDialogAbsNote;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.io.File;

/**
 * A panel containing a full instrument manager
 * 
 * @author  ice
 */
public class JInstrumentPanel extends javax.swing.JPanel implements userAction {

    private static final int sizeA = 60;

    private static final int sizeB = 50;

    /** calling frame for this component */
    private JFrame jFrame;

    /** Dialog for list of instrument */
    JListDialog jListDialog;

    /** Dialog for tables of values */
    JTablesDialog jTablesDialog;

    /** Dialog for instr. view */
    JInstrViewDialog jInstrViewDialog;

    /** Model list used by the table*/
    DataTableModelInstr dataTableModelInstr = new DataTableModelInstr();

    /** Color renderer for instr table */
    InstrTableCellRenderer instrRenderer = new InstrTableCellRenderer();

    /** Panel for popup of DN */
    JDialogDN jDialogDN;

    /** Panel for popup of Repeat */
    JDialogRep jDialogRep;

    /** Panel for popup of Fixed Pulse */
    JDialogFixedPulse jDialogFixedPulse;

    /** Panel for popup of Relative Pulse */
    JDialogRelPulse jDialogRelPulse;

    /** Panel for popup of Fixed Frequency */
    JDialogFixedFreq jDialogFixedFreq;

    /** Panel for popup of Relative Note */
    JDialogRelNote jDialogRelNote;

    /** Panel for popup of Relative Frequency */
    JDialogRelFreq jDialogRelFreq;

    /** Panel for popup of Absolute Note */
    JDialogAbsNote jDialogAbsNote;

    /** Panel for popup of Filter Fixed */
    JDialogFixedFilter jDialogFixedFilter;

    /** Panel for popup of Filter Relative */
    JDialogRelFilter jDialogRelFilter;

    /** Panel for popup of Filter Resonance */
    JDialogFilterRes jDialogFilterRes;

    /** Panel for popup of Filter Type */
    JDialogFilterType jDialogFilterType;

    /** Internal object for user action */
    IntObj intObj = new IntObj();

    /** Pattern panel voice 1 */
    JPatternPanel jPatternPanel1;

    /** Pattern panel voice 2 */
    JPatternPanel jPatternPanel2;

    /** Pattern panel voice 3 */
    JPatternPanel jPatternPanel3;

    /** Instrument frame 1 */
    JInstrumentFrame jInstrumentFrame1;

    /** Instrument frame 2 */
    JInstrumentFrame jInstrumentFrame2;

    /** Used for internal managing */
    private InstrTable instrTable;

    /** Actual row for popup menu */
    private int actRow;

    /** Actual location for popup menu */
    private Point actLoc;

    /** Row header frp table */
    private JList rowHeader;

    /** True if notify is to suspend */
    private boolean suspendNotify = false;

    /** True if this is internal of main frame */
    private boolean isInternal;

    /**
   * Creates new form JInstrumentPanel
   */
    public JInstrumentPanel() {
        initComponents();
        Shared.framesList.add(this);
        Shared.framesList.add(this.jPopupMenuFilterFreq);
        Shared.framesList.add(this.jPopupMenuFilterRes);
        Shared.framesList.add(this.jPopupMenuFilterType);
        Shared.framesList.add(this.jPopupMenuFreq);
        Shared.framesList.add(this.jPopupMenuOnlyStep);
        Shared.framesList.add(this.jPopupMenuPulse);
    }

    /**
   * Setting up this component
   *  
   * @param jFrame the calling frame that contain this panel
   * @param canBeClosed True if this can be closed by user action
   * @param jPatternPanel1 first pattern panel
   * @param jPatternPanel2 second pattern panel
   * @param jPatternPanel3 thirt pattern panel
   * @param isInternal true if this is internal of main frame
   */
    public void settingUp(JFrame jFrame, boolean canBeClosed, JPatternPanel jPatternPanel1, JPatternPanel jPatternPanel2, JPatternPanel jPatternPanel3, boolean isInternal) {
        jButtonClose.setEnabled(canBeClosed);
        this.isInternal = isInternal;
        this.jFrame = jFrame;
        this.jPatternPanel1 = jPatternPanel1;
        this.jPatternPanel2 = jPatternPanel2;
        this.jPatternPanel3 = jPatternPanel3;
        jListDialog = new JListDialog(jFrame, true, intObj);
        jTablesDialog = new JTablesDialog(jFrame, true);
        jInstrViewDialog = new JInstrViewDialog(jFrame, true);
        jDialogDN = new JDialogDN(jFrame, true);
        jDialogRep = new JDialogRep(jFrame, true);
        jDialogFixedPulse = new JDialogFixedPulse(jFrame, true);
        jDialogRelPulse = new JDialogRelPulse(jFrame, true);
        jDialogFixedFreq = new JDialogFixedFreq(jFrame, true);
        jDialogRelNote = new JDialogRelNote(jFrame, true);
        jDialogRelFreq = new JDialogRelFreq(jFrame, true);
        jDialogAbsNote = new JDialogAbsNote(jFrame, true);
        jDialogFixedFilter = new JDialogFixedFilter(jFrame, true);
        jDialogRelFilter = new JDialogRelFilter(jFrame, true);
        jDialogFilterRes = new JDialogFilterRes(jFrame, true);
        jDialogFilterType = new JDialogFilterType(jFrame, true);
        if (isInternal) {
            jInstrumentFrame1 = new JInstrumentFrame(jPatternPanel1, jPatternPanel2, jPatternPanel3);
            jInstrumentFrame2 = new JInstrumentFrame(jPatternPanel1, jPatternPanel2, jPatternPanel3);
            Shared.jFrames[Shared.WIN_INSTR1] = jInstrumentFrame1;
            Shared.jFrames[Shared.WIN_INSTR2] = jInstrumentFrame2;
        }
    }

    /**
   * Use current instrument (to be used when multiple windows open)
   */
    public void useInstrument() {
        useInstrument((Integer) jSpinnerInstrument.getValue());
    }

    /**
   * Use the given instrument at given index
   * 
   * @param index the instrument index
   */
    public void useInstrument(int index) {
        int value;
        suspendNotify = true;
        index--;
        Shared.instrTimer.setInstrument(index);
        Shared.instrTimer.setLabel(jLabelState);
        jTextFieldName.setText(Shared.instruments[index].getName());
        jTextFieldNote.setText(Shared.instruments[index].getNote());
        jCheckBoxHR.setSelected(Shared.instruments[index].isFlagHR());
        jCheckBoxGate.setSelected(Shared.instruments[index].isFlagGateOff());
        if (Shared.instruments[index].isFlagOrder()) jRadioButtonADSRW.setSelected(true); else jRadioButtonWADSR.setSelected(true);
        value = Shared.instruments[index].getValueADHR();
        if (value == -1) jTextFieldAD.setText(""); else jTextFieldAD.setText(Hex2InputVerifier.getHex(value));
        value = Shared.instruments[index].getValueSRHR();
        if (value == -1) jTextFieldSR.setText(""); else jTextFieldSR.setText(Hex2InputVerifier.getHex(value));
        value = Shared.instruments[index].getValueCtrl1HR();
        if (value == -1) jTextFieldCTRL.setText(""); else jTextFieldCTRL.setText(Hex2InputVerifier.getHex(value));
        value = Shared.instruments[index].getValueCtrl2HR();
        if (value == -1) jTextFieldCTRL1.setText(""); else jTextFieldCTRL1.setText(Hex2InputVerifier.getHex(value));
        jSpinnerNTicks.setValue(Shared.instruments[index].getValueNTicks() + 1);
        dataTableModelInstr.setActInstr(index);
        instrRenderer.setActInstr(index);
        jPianoPanelNote.setInstr(index + 1);
        updateState();
        suspendNotify = false;
    }

    /**
   * Execute the passed user action
   * 
   * @param type the type of action to execute
   */
    public void execute(int type) {
        int index;
        File file;
        boolean result;
        index = (Integer) jSpinnerInstrument.getValue() - 1;
        switch(type) {
            case INSTR_NEW:
                boolean found = false;
                for (int i = 0; i < Shared.instruments.length; i++) {
                    if (Shared.instruments[i].getName().equals("")) {
                        jSpinnerInstrument.setValue(i + 1);
                        useInstrument(i + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "No more free instruments are available.", "New Instrument", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case INSTR_LOAD:
                if (Shared.chooserInstr.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    file = Shared.chooserInstr.getSelectedFile();
                    result = Shared.fileManager.readInstrFile(file, Shared.instruments[index]);
                    Shared.instruments[index].setFile(file);
                    Shared.timer.notifyChange();
                    Shared.instrTimer.notifyChange();
                    updateState();
                    if (!result) {
                        JOptionPane.showMessageDialog(this, "Read of file error", "Read Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case INSTR_SAVE:
                file = Shared.instruments[index].getFile();
                if (file != null) {
                    result = Shared.fileManager.writeInstrFile(file, Shared.instruments[index]);
                    if (!result) {
                        JOptionPane.showMessageDialog(this, "Write of file error", "Write Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else execute(INSTR_SAVE_AS);
                break;
            case INSTR_SAVE_AS:
                if (Shared.chooserInstr.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    file = Shared.chooserInstr.getSelectedFile();
                    if (file.getName().endsWith(".ins") == false) {
                        try {
                            file = new File(file.getCanonicalPath() + ".ins");
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    }
                    result = Shared.fileManager.writeInstrFile(file, Shared.instruments[index]);
                    Shared.instruments[index].setFile(file);
                    if (!result) {
                        JOptionPane.showMessageDialog(this, "Write of file error", "Write Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case INSTR_IMP_GT2:
                if (Shared.chooserImpGt2Ins.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    file = Shared.chooserImpGt2Ins.getSelectedFile();
                    int errResult = Shared.gt2FileManager.readInstrFile(file, Shared.instruments[index]);
                    Shared.timer.notifyChange();
                    Shared.instrTimer.notifyChange();
                    updateState();
                    if (errResult != Gt2Instrument.STATUS_OK) {
                        JOptionPane.showMessageDialog(this, "Read of file to import error:\n" + Gt2Instrument.decodeError(errResult), "Read Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case INSTR_CLEAR:
                if (JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the Instrument?") == JOptionPane.YES_OPTION) {
                    Shared.instruments[index].clear();
                    Shared.instruments[index].setFile(null);
                    if (!suspendNotify) {
                        Shared.timer.notifyChange();
                        Shared.instrTimer.notifyChange();
                    }
                    useInstrument();
                }
                break;
            case INSTR_LIST:
                jListDialog.setVisible(true);
                if (intObj.value != -1) {
                    jSpinnerInstrument.setValue(intObj.value + 1);
                    useInstrument(intObj.value + 1);
                    intObj.value = -1;
                }
                jPatternPanel1.repaint();
                jPatternPanel2.repaint();
                jPatternPanel3.repaint();
                break;
            case INSTR_NEW_VIEW:
                if (isInternal) {
                    if (!jInstrumentFrame1.isShowing()) {
                        jInstrumentFrame1.setVisible(true);
                        break;
                    }
                    if (!jInstrumentFrame2.isShowing()) {
                        jInstrumentFrame2.setVisible(true);
                        break;
                    }
                }
                new JInstrumentFrame(jPatternPanel1, jPatternPanel2, jPatternPanel3).setVisible(true);
                break;
            case INSTR_TABLE:
                jInstrViewDialog.setActInstr(index);
                jInstrViewDialog.setVisible(true);
                break;
            case INSTR_TABLES:
                jTablesDialog.setActInstr(index);
                jTablesDialog.updateInternal();
                jTablesDialog.setVisible(true);
                break;
            case INSTR_FIRST:
                jSpinnerInstrument.setValue(1);
                useInstrument(1);
                break;
            case INSTR_PREV:
                index = (Integer) jSpinnerInstrument.getValue() - 1;
                if (index != 0) {
                    jSpinnerInstrument.setValue(index);
                    useInstrument(index);
                }
                break;
            case INSTR_NEXT:
                index = (Integer) jSpinnerInstrument.getValue() + 1;
                if (index != 256) {
                    jSpinnerInstrument.setValue(index);
                    useInstrument(index);
                }
                break;
            case INSTR_LAST:
                jSpinnerInstrument.setValue(255);
                useInstrument(255);
                break;
            case INSTR_STOP:
                break;
            case INSTR_CLOSE:
                jFrame.setVisible(false);
                break;
        }
    }

    /**
   * Update the state according to the one of the instrument
   */
    private void updateState() {
    }

    /**
   * Popup an action according to the position
   * 
   * @param evt the mouse event
   */
    private void popUp(MouseEvent evt) {
        int actInstr = (Integer) jSpinnerInstrument.getValue() - 1;
        Instrument instr = Shared.instruments[actInstr];
        int row = jTableInstr.getSelectedRow();
        int column = jTableInstr.getSelectedColumn();
        Point loc = jTableInstr.getLocationOnScreen();
        actRow = row;
        actLoc = loc;
        actLoc.translate(evt.getX(), evt.getY());
        switch(column) {
            case DataTableModelInstr.COL_AD:
                instrTable = instr.getInstrTableAD();
                if (row < instrTable.getDimension()) {
                    jPopupMenuOnlyStep.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_SR:
                instrTable = instr.getInstrTableSR();
                if (row < instrTable.getDimension()) {
                    jPopupMenuOnlyStep.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_WAVE:
                instrTable = instr.getInstrTableWave();
                if (row < instrTable.getDimension()) {
                    jPopupMenuOnlyStep.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_FREQ:
                instrTable = instr.getInstrTableFreq();
                if (row < instrTable.getDimension()) {
                    jPopupMenuFreq.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_PULSE:
                instrTable = instr.getInstrTablePulse();
                if (row < instrTable.getDimension()) {
                    jPopupMenuPulse.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_FILTER:
                instrTable = instr.getInstrTableFilter();
                if (row < instrTable.getDimension()) {
                    jPopupMenuFilterFreq.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_FILTER_RES:
                instrTable = instr.getInstrTableFilterRes();
                if (row < instrTable.getDimension()) {
                    jPopupMenuFilterRes.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_FILTER_TYPE:
                instrTable = instr.getInstrTableFilterType();
                if (row < instrTable.getDimension()) {
                    jPopupMenuFilterType.show(evt.getComponent(), evt.getX(), evt.getY());
                }
                break;
            case DataTableModelInstr.COL_AD_DR:
                instrTable = instr.getInstrTableAD();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_SR_DR:
                instrTable = instr.getInstrTableSR();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_WAVE_DR:
                instrTable = instr.getInstrTableWave();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_FREQ_DR:
                instrTable = instr.getInstrTableFreq();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_PULSE_DR:
                instrTable = instr.getInstrTablePulse();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_FILTER_DR:
                instrTable = instr.getInstrTableFilter();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_FILTER_RES_DR:
                instrTable = instr.getInstrTableFilterRes();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
            case DataTableModelInstr.COL_FILTER_TYPE_DR:
                instrTable = instr.getInstrTableFilterType();
                if (row < instrTable.getDimension()) {
                    jDialogDN.setValues(instrTable, actInstr, row);
                    jDialogDN.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogDN.setVisible(true);
                } else {
                    jDialogRep.setValues(instrTable, actInstr);
                    jDialogRep.setLocation((int) actLoc.getX(), (int) actLoc.getY());
                    jDialogRep.setVisible(true);
                }
                break;
        }
    }

    /**
   * Get the actual instrument being edited
   * 
   * @return the actual instrument
   */
    public int getActInstrument() {
        return (Integer) jSpinnerInstrument.getValue();
    }

    /**
   * Get the midi player used by instrument
   *
   * @return the midi player used by instrument
   */
    public midiPlay getMidiPlayer() {
        return jPianoPanelNote;
    }

    @Override
    public void repaint() {
        super.repaint();
        if (rowHeader != null) rowHeader.setFixedCellHeight(jTableInstr.getRowHeight());
    }

    private void initComponents() {
        jPopupMenuOnlyStep = new javax.swing.JPopupMenu();
        jMenuItemStep = new javax.swing.JMenuItem();
        jMenuItemNoStep = new javax.swing.JMenuItem();
        jPopupMenuPulse = new javax.swing.JPopupMenu();
        jMenuItemPulseStep = new javax.swing.JMenuItem();
        jMenuItemPulseNoStep = new javax.swing.JMenuItem();
        jSeparatorPulse = new javax.swing.JSeparator();
        jMenuItemPulseFixed = new javax.swing.JMenuItem();
        jMenuItemPulseRel = new javax.swing.JMenuItem();
        jPopupMenuFreq = new javax.swing.JPopupMenu();
        jMenuItemFreqStep = new javax.swing.JMenuItem();
        jMenuItemFreqNoStep = new javax.swing.JMenuItem();
        jSeparatorFreq = new javax.swing.JSeparator();
        jMenuItemFreqAN = new javax.swing.JMenuItem();
        jMenuItemFreqRN = new javax.swing.JMenuItem();
        jMenuItemFreqRF = new javax.swing.JMenuItem();
        jMenuItemFreqAF = new javax.swing.JMenuItem();
        jPopupMenuFilterFreq = new javax.swing.JPopupMenu();
        jMenuItemFilterFreqStep = new javax.swing.JMenuItem();
        jMenuItemFilterFreqNoStep = new javax.swing.JMenuItem();
        jSeparatorFilterFreq = new javax.swing.JSeparator();
        jMenuItemFilterFreqRF = new javax.swing.JMenuItem();
        jMenuItemFilterFreqAF = new javax.swing.JMenuItem();
        jPopupMenuFilterRes = new javax.swing.JPopupMenu();
        jMenuItemFilterResStep = new javax.swing.JMenuItem();
        jMenuItemFilterResNoStep = new javax.swing.JMenuItem();
        jSeparatorFilterRes = new javax.swing.JSeparator();
        jMenuItemFilterRes = new javax.swing.JMenuItem();
        jPopupMenuFilterType = new javax.swing.JPopupMenu();
        jMenuItemFilterTypeStep = new javax.swing.JMenuItem();
        jMenuItemFilterTypeNoStep = new javax.swing.JMenuItem();
        jSeparatorFilterType = new javax.swing.JSeparator();
        jMenuItemFilterType = new javax.swing.JMenuItem();
        buttonGroupOrder = new javax.swing.ButtonGroup();
        jPanelUp = new javax.swing.JPanel();
        jLabelNote = new javax.swing.JLabel();
        jTextFieldNote = new javax.swing.JTextField();
        jLabelInstrument = new javax.swing.JLabel();
        jSpinnerInstrument = new javax.swing.JSpinner();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jPanelHR = new javax.swing.JPanel();
        jCheckBoxHR = new javax.swing.JCheckBox();
        jTextFieldAD = new javax.swing.JTextField();
        jLabelAD = new javax.swing.JLabel();
        jLabelSR = new javax.swing.JLabel();
        jTextFieldSR = new javax.swing.JTextField();
        jTextFieldCTRL = new javax.swing.JTextField();
        jLabelCTRL = new javax.swing.JLabel();
        jSpinnerNTicks = new javax.swing.JSpinner();
        jLabelNTicks = new javax.swing.JLabel();
        jCheckBoxGate = new javax.swing.JCheckBox();
        jTextFieldCTRL1 = new javax.swing.JTextField();
        jLabelOrder = new javax.swing.JLabel();
        jRadioButtonWADSR = new javax.swing.JRadioButton();
        jRadioButtonADSRW = new javax.swing.JRadioButton();
        jToolBarInstrument = new javax.swing.JToolBar();
        jButtonNew = new javax.swing.JButton();
        jButtonLoad = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAS = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jButtonList = new javax.swing.JButton();
        jButtonNewWin = new javax.swing.JButton();
        jButtonTable = new javax.swing.JButton();
        jButtonTables = new javax.swing.JButton();
        jButtonGoFirst = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jButtonGoLast = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jLabelState = new javax.swing.JLabel();
        jScrollPaneTable = new javax.swing.JScrollPane();
        jTableInstr = new javax.swing.JTable();
        rowHeader = new JList(new AbstractListModel() {

            public int getSize() {
                return jTableInstr.getRowCount();
            }

            public Object getElementAt(int index) {
                return "" + (index + 1);
            }
        });
        rowHeader.setFixedCellWidth(30);
        rowHeader.setFixedCellHeight(jTableInstr.getRowHeight());
        rowHeader.setCellRenderer(new RowHeaderRenderer(jTableInstr));
        jScrollPaneTable.setRowHeaderView(rowHeader);
        jTableInstr.setDefaultEditor(java.lang.Object.class, new XCellEditor());
        dataTableModelInstr.setToolkit(jTableInstr.getToolkit());
        jPanelDn = new javax.swing.JPanel();
        jPianoPanelNote = new jitt64.swing.JPianoPanel();
        jMenuItemStep.setText("Set step to here");
        jMenuItemStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStepActionPerformed(evt);
            }
        });
        jPopupMenuOnlyStep.add(jMenuItemStep);
        jMenuItemNoStep.setText("Set for no step");
        jMenuItemNoStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNoStepActionPerformed(evt);
            }
        });
        jPopupMenuOnlyStep.add(jMenuItemNoStep);
        jMenuItemPulseStep.setText("Set step to here");
        jMenuItemPulseStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPulseStepActionPerformed(evt);
            }
        });
        jPopupMenuPulse.add(jMenuItemPulseStep);
        jMenuItemPulseNoStep.setText("Set for no step");
        jMenuItemPulseNoStep.setToolTipText("");
        jMenuItemPulseNoStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPulseNoStepActionPerformed(evt);
            }
        });
        jPopupMenuPulse.add(jMenuItemPulseNoStep);
        jPopupMenuPulse.add(jSeparatorPulse);
        jMenuItemPulseFixed.setText("Insert Fixed Pulse Value");
        jMenuItemPulseFixed.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPulseFixedActionPerformed(evt);
            }
        });
        jPopupMenuPulse.add(jMenuItemPulseFixed);
        jMenuItemPulseRel.setText("Insert Relative Pulse Value");
        jMenuItemPulseRel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPulseRelActionPerformed(evt);
            }
        });
        jPopupMenuPulse.add(jMenuItemPulseRel);
        jMenuItemFreqStep.setText("Set step to here");
        jMenuItemFreqStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFreqStepActionPerformed(evt);
            }
        });
        jPopupMenuFreq.add(jMenuItemFreqStep);
        jMenuItemFreqNoStep.setText("Set for no step");
        jMenuItemFreqNoStep.setToolTipText("");
        jMenuItemFreqNoStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFreqNoStepActionPerformed(evt);
            }
        });
        jPopupMenuFreq.add(jMenuItemFreqNoStep);
        jPopupMenuFreq.add(jSeparatorFreq);
        jMenuItemFreqAN.setText("Insert absolute note");
        jMenuItemFreqAN.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFreqANActionPerformed(evt);
            }
        });
        jPopupMenuFreq.add(jMenuItemFreqAN);
        jMenuItemFreqRN.setText("Insert relative note");
        jMenuItemFreqRN.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFreqRNActionPerformed(evt);
            }
        });
        jPopupMenuFreq.add(jMenuItemFreqRN);
        jMenuItemFreqRF.setText("Insert relative frequency");
        jMenuItemFreqRF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFreqRFActionPerformed(evt);
            }
        });
        jPopupMenuFreq.add(jMenuItemFreqRF);
        jMenuItemFreqAF.setText("Insert absolute frequency");
        jMenuItemFreqAF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFreqAFActionPerformed(evt);
            }
        });
        jPopupMenuFreq.add(jMenuItemFreqAF);
        jMenuItemFilterFreqStep.setText("Set step to here");
        jMenuItemFilterFreqStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterFreqStepActionPerformed(evt);
            }
        });
        jPopupMenuFilterFreq.add(jMenuItemFilterFreqStep);
        jMenuItemFilterFreqNoStep.setText("Set for no step");
        jMenuItemFilterFreqNoStep.setToolTipText("");
        jMenuItemFilterFreqNoStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterFreqNoStepActionPerformed(evt);
            }
        });
        jPopupMenuFilterFreq.add(jMenuItemFilterFreqNoStep);
        jPopupMenuFilterFreq.add(jSeparatorFilterFreq);
        jMenuItemFilterFreqRF.setText("Insert Relative Frequency");
        jMenuItemFilterFreqRF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterFreqRFActionPerformed(evt);
            }
        });
        jPopupMenuFilterFreq.add(jMenuItemFilterFreqRF);
        jMenuItemFilterFreqAF.setText("Insert Absolute Frequency");
        jMenuItemFilterFreqAF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterFreqAFActionPerformed(evt);
            }
        });
        jPopupMenuFilterFreq.add(jMenuItemFilterFreqAF);
        jMenuItemFilterResStep.setText("Set step to here");
        jMenuItemFilterResStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterResStepActionPerformed(evt);
            }
        });
        jPopupMenuFilterRes.add(jMenuItemFilterResStep);
        jMenuItemFilterResNoStep.setText("Set for no step");
        jMenuItemFilterResNoStep.setToolTipText("");
        jMenuItemFilterResNoStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterResNoStepActionPerformed(evt);
            }
        });
        jPopupMenuFilterRes.add(jMenuItemFilterResNoStep);
        jPopupMenuFilterRes.add(jSeparatorFilterRes);
        jMenuItemFilterRes.setText("Insert value");
        jMenuItemFilterRes.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterResActionPerformed(evt);
            }
        });
        jPopupMenuFilterRes.add(jMenuItemFilterRes);
        jMenuItemFilterTypeStep.setText("Set step to here");
        jMenuItemFilterTypeStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterTypeStepActionPerformed(evt);
            }
        });
        jPopupMenuFilterType.add(jMenuItemFilterTypeStep);
        jMenuItemFilterTypeNoStep.setText("Set for no step");
        jMenuItemFilterTypeNoStep.setToolTipText("");
        jMenuItemFilterTypeNoStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterTypeNoStepActionPerformed(evt);
            }
        });
        jPopupMenuFilterType.add(jMenuItemFilterTypeNoStep);
        jPopupMenuFilterType.add(jSeparatorFilterType);
        jMenuItemFilterType.setText("Select type");
        jMenuItemFilterType.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterTypeActionPerformed(evt);
            }
        });
        jPopupMenuFilterType.add(jMenuItemFilterType);
        setLayout(new java.awt.BorderLayout());
        jPanelUp.setFocusable(false);
        jLabelNote.setText("Note:");
        jTextFieldNote.setPreferredSize(new java.awt.Dimension(280, 17));
        jTextFieldNote.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNoteActionPerformed(evt);
            }
        });
        jTextFieldNote.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldNoteFocusLost(evt);
            }
        });
        jLabelInstrument.setText("Instrument");
        jSpinnerInstrument.setModel(new javax.swing.SpinnerNumberModel(1, 1, 255, 1));
        jSpinnerInstrument.setMinimumSize(new java.awt.Dimension(50, 18));
        jSpinnerInstrument.setPreferredSize(new java.awt.Dimension(50, 18));
        jSpinnerInstrument.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerInstrumentStateChanged(evt);
            }
        });
        jLabelName.setText("Name:");
        jTextFieldName.setMinimumSize(new java.awt.Dimension(120, 17));
        jTextFieldName.setPreferredSize(new java.awt.Dimension(120, 17));
        jTextFieldName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNameActionPerformed(evt);
            }
        });
        jTextFieldName.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldNameFocusLost(evt);
            }
        });
        jPanelHR.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jCheckBoxHR.setText("HardRestart On/Off");
        jCheckBoxHR.setToolTipText("Activate the HardRestart for the instrument");
        jCheckBoxHR.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxHRItemStateChanged(evt);
            }
        });
        jTextFieldAD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldAD.setToolTipText("Attack/Decay value to use in HardRestart phase");
        jTextFieldAD.setInputVerifier(new Hex2InputVerifier());
        jTextFieldAD.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldADActionPerformed(evt);
            }
        });
        jTextFieldAD.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldADFocusLost(evt);
            }
        });
        jLabelAD.setText("Attack/Decay HR");
        jLabelSR.setText("Sustain/Release HR");
        jTextFieldSR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSR.setToolTipText("Sustain/Release value to use in HardRestart phase");
        jTextFieldSR.setInputVerifier(new Hex2InputVerifier());
        jTextFieldSR.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSRActionPerformed(evt);
            }
        });
        jTextFieldSR.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSRFocusLost(evt);
            }
        });
        jTextFieldCTRL.setToolTipText("The Control to use when start the HardRestart phase");
        jTextFieldCTRL.setInputVerifier(new Hex2InputVerifier());
        jTextFieldCTRL.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCTRLActionPerformed(evt);
            }
        });
        jTextFieldCTRL.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCTRLFocusLost(evt);
            }
        });
        jLabelCTRL.setText("Ctrl HR");
        jSpinnerNTicks.setModel(new javax.swing.SpinnerNumberModel(2, 1, 15, 1));
        jSpinnerNTicks.setEnabled(false);
        jSpinnerNTicks.setMinimumSize(new java.awt.Dimension(50, 18));
        jSpinnerNTicks.setPreferredSize(new java.awt.Dimension(50, 18));
        jSpinnerNTicks.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerNTicksStateChanged(evt);
            }
        });
        jLabelNTicks.setText("N. Ticks");
        jCheckBoxGate.setText("Gate Off");
        jCheckBoxGate.setToolTipText("Make the Gate Off before the next note");
        jCheckBoxGate.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxGateItemStateChanged(evt);
            }
        });
        jTextFieldCTRL1.setToolTipText("The Control to use when near to finish the HardRestart phase");
        jTextFieldCTRL1.setInputVerifier(new Hex2InputVerifier());
        jTextFieldCTRL1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCTRL1ActionPerformed(evt);
            }
        });
        jTextFieldCTRL1.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldCTRL1FocusLost(evt);
            }
        });
        jLabelOrder.setText("Write Order");
        buttonGroupOrder.add(jRadioButtonWADSR);
        jRadioButtonWADSR.setSelected(true);
        jRadioButtonWADSR.setText("Wave - ADSR");
        jRadioButtonWADSR.setToolTipText("Write first Wave then ADSR in sid registers");
        jRadioButtonWADSR.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonWADSRItemStateChanged(evt);
            }
        });
        buttonGroupOrder.add(jRadioButtonADSRW);
        jRadioButtonADSRW.setText("ADSR - Wave");
        jRadioButtonADSRW.setToolTipText("Write first ADSR then Wave in sid registers");
        jRadioButtonADSRW.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonADSRWItemStateChanged(evt);
            }
        });
        javax.swing.GroupLayout jPanelHRLayout = new javax.swing.GroupLayout(jPanelHR);
        jPanelHR.setLayout(jPanelHRLayout);
        jPanelHRLayout.setHorizontalGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHRLayout.createSequentialGroup().addContainerGap().addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHRLayout.createSequentialGroup().addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jTextFieldSR).addComponent(jTextFieldAD, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelSR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabelAD, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))).addComponent(jCheckBoxHR, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)).addGap(18, 18, 18).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHRLayout.createSequentialGroup().addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHRLayout.createSequentialGroup().addComponent(jTextFieldCTRL, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextFieldCTRL1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jSpinnerNTicks, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabelNTicks, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabelCTRL, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))).addComponent(jCheckBoxGate, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButtonADSRW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jRadioButtonWADSR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabelOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)).addContainerGap()));
        jPanelHRLayout.setVerticalGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHRLayout.createSequentialGroup().addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanelHRLayout.createSequentialGroup().addGap(69, 69, 69).addComponent(jLabelSR)).addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanelHRLayout.createSequentialGroup().addGap(67, 67, 67).addComponent(jTextFieldSR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanelHRLayout.createSequentialGroup().addGap(16, 16, 16).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jCheckBoxHR).addComponent(jCheckBoxGate).addComponent(jLabelOrder)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jTextFieldAD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelAD, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSpinnerNTicks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelNTicks).addComponent(jRadioButtonWADSR)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelHRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jTextFieldCTRL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTextFieldCTRL1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelCTRL).addComponent(jRadioButtonADSRW)))).addContainerGap(12, Short.MAX_VALUE)));
        jToolBarInstrument.setFloatable(false);
        jToolBarInstrument.setToolTipText("Instrument editor");
        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/filenew.png")));
        jButtonNew.setToolTipText("New Instrument (select first without a name)");
        jButtonNew.setFocusable(false);
        jButtonNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonNew);
        jButtonLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/fileopen.png")));
        jButtonLoad.setToolTipText("Load a instrument definition from file");
        jButtonLoad.setFocusable(false);
        jButtonLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonLoad.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonLoad);
        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/filesave.png")));
        jButtonSave.setToolTipText("Save current insturment to file");
        jButtonSave.setFocusable(false);
        jButtonSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonSave);
        jButtonSaveAS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/filesaveas.png")));
        jButtonSaveAS.setToolTipText("Save current instrument to file with a new name");
        jButtonSaveAS.setFocusable(false);
        jButtonSaveAS.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSaveAS.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSaveAS.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveASActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonSaveAS);
        jButtonClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/remove.png")));
        jButtonClear.setToolTipText("Clear this instrument definitions");
        jButtonClear.setFocusable(false);
        jButtonClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonClear);
        jButtonList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/view_detailed.png")));
        jButtonList.setToolTipText("List instruments");
        jButtonList.setFocusable(false);
        jButtonList.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonList.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonList.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonListActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonList);
        jButtonNewWin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/window_new.png")));
        jButtonNewWin.setToolTipText("Open a new window for edit a instrument");
        jButtonNewWin.setFocusable(false);
        jButtonNewWin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNewWin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNewWin.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewWinActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonNewWin);
        jButtonTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/mini/taskbar.png")));
        jButtonTable.setToolTipText("Show table view of the instrument");
        jButtonTable.setFocusable(false);
        jButtonTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonTable.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTableActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonTable);
        jButtonTables.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/frame_spreadsheet.png")));
        jButtonTables.setToolTipText("Tables Definition");
        jButtonTables.setFocusable(false);
        jButtonTables.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonTables.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonTables.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTablesActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonTables);
        jButtonGoFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/2leftarrow.png")));
        jButtonGoFirst.setToolTipText("Go to the first instrument");
        jButtonGoFirst.setFocusable(false);
        jButtonGoFirst.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonGoFirst.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonGoFirst.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoFirstActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonGoFirst);
        jButtonPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/1leftarrow.png")));
        jButtonPrev.setToolTipText("Go to previous instrument");
        jButtonPrev.setFocusable(false);
        jButtonPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPrev.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonPrev);
        jButtonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/1rightarrow.png")));
        jButtonNext.setToolTipText("Go to next instrument");
        jButtonNext.setFocusable(false);
        jButtonNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonNext);
        jButtonGoLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/2rightarrow.png")));
        jButtonGoLast.setToolTipText("Go to last instrument");
        jButtonGoLast.setFocusable(false);
        jButtonGoLast.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonGoLast.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonGoLast.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGoLastActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonGoLast);
        jButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/player_stop.png")));
        jButtonStop.setToolTipText("Stop sound for this instrument");
        jButtonStop.setFocusable(false);
        jButtonStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonStop);
        jButtonClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/exit.png")));
        jButtonClose.setToolTipText("Close this window");
        jButtonClose.setEnabled(false);
        jButtonClose.setFocusable(false);
        jButtonClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jToolBarInstrument.add(jButtonClose);
        jLabelState.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jitt64/swing/icons/red.png")));
        javax.swing.GroupLayout jPanelUpLayout = new javax.swing.GroupLayout(jPanelUp);
        jPanelUp.setLayout(jPanelUpLayout);
        jPanelUpLayout.setHorizontalGroup(jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBarInstrument, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelUpLayout.createSequentialGroup().addContainerGap().addComponent(jLabelInstrument).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSpinnerInstrument, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(43, 43, 43).addComponent(jLabelName).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE).addComponent(jLabelState).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelUpLayout.createSequentialGroup().addContainerGap().addGroup(jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelUpLayout.createSequentialGroup().addComponent(jLabelNote).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextFieldNote, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)).addComponent(jPanelHR, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanelUpLayout.setVerticalGroup(jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelUpLayout.createSequentialGroup().addComponent(jToolBarInstrument, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelInstrument).addComponent(jSpinnerInstrument, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelName).addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelState)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelNote).addComponent(jTextFieldNote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanelHR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        add(jPanelUp, java.awt.BorderLayout.NORTH);
        jScrollPaneTable.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPaneTable.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jTableInstr.setModel(dataTableModelInstr);
        jTableInstr.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableInstr.setRowSelectionAllowed(false);
        jTableInstr.getTableHeader().setReorderingAllowed(false);
        TableColumnModel cm = jTableInstr.getColumnModel();
        TableColumn tc;
        tc = cm.getColumn(DataTableModelInstr.COL_AD);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_AD_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_SR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_SR_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_WAVE);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_WAVE_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_FREQ);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_FREQ_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_PULSE);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_PULSE_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_FILTER);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_FILTER_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_FILTER_RES);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_FILTER_RES_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        tc = cm.getColumn(DataTableModelInstr.COL_FILTER_TYPE);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeA);
        tc = cm.getColumn(DataTableModelInstr.COL_FILTER_TYPE_DR);
        tc.setCellRenderer(instrRenderer);
        tc.setPreferredWidth(sizeB);
        jTableInstr.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableInstrMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTableInstrMouseReleased(evt);
            }
        });
        jTableInstr.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableInstrKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTableInstrKeyTyped(evt);
            }
        });
        jScrollPaneTable.setViewportView(jTableInstr);
        add(jScrollPaneTable, java.awt.BorderLayout.CENTER);
        jPanelDn.setLayout(new java.awt.BorderLayout());
        javax.swing.GroupLayout jPianoPanelNoteLayout = new javax.swing.GroupLayout(jPianoPanelNote);
        jPianoPanelNote.setLayout(jPianoPanelNoteLayout);
        jPianoPanelNoteLayout.setHorizontalGroup(jPianoPanelNoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 628, Short.MAX_VALUE));
        jPianoPanelNoteLayout.setVerticalGroup(jPianoPanelNoteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 62, Short.MAX_VALUE));
        jPanelDn.add(jPianoPanelNote, java.awt.BorderLayout.CENTER);
        add(jPanelDn, java.awt.BorderLayout.SOUTH);
    }

    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_NEW);
    }

    private void jButtonLoadActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_LOAD);
    }

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_SAVE);
    }

    private void jButtonSaveASActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_SAVE_AS);
    }

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_CLEAR);
    }

    private void jButtonListActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_LIST);
    }

    private void jButtonNewWinActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_NEW_VIEW);
    }

    private void jButtonTableActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_TABLE);
    }

    private void jButtonGoFirstActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_FIRST);
    }

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_PREV);
    }

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_NEXT);
    }

    private void jButtonGoLastActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_LAST);
    }

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_STOP);
    }

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_CLOSE);
    }

    private void jSpinnerInstrumentStateChanged(javax.swing.event.ChangeEvent evt) {
        useInstrument((Integer) jSpinnerInstrument.getValue());
        updateState();
    }

    private void jTextFieldNameActionPerformed(java.awt.event.ActionEvent evt) {
        Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setName(jTextFieldName.getText());
    }

    private void jTextFieldNameFocusLost(java.awt.event.FocusEvent evt) {
        Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setName(jTextFieldName.getText());
    }

    private void jTextFieldADActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueADHR(Integer.parseInt(jTextFieldAD.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueADHR(-1);
        }
        updateState();
    }

    private void jTextFieldADFocusLost(java.awt.event.FocusEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueADHR(Integer.parseInt(jTextFieldAD.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueADHR(-1);
        }
        updateState();
    }

    private void jTextFieldSRActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueSRHR(Integer.parseInt(jTextFieldSR.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueSRHR(-1);
        }
        updateState();
    }

    private void jTextFieldSRFocusLost(java.awt.event.FocusEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueSRHR(Integer.parseInt(jTextFieldSR.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueSRHR(-1);
        }
        updateState();
    }

    private void jTextFieldCTRLActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl1HR(Integer.parseInt(jTextFieldCTRL.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl1HR(-1);
        }
        updateState();
    }

    private void jTextFieldCTRLFocusLost(java.awt.event.FocusEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl1HR(Integer.parseInt(jTextFieldCTRL.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl1HR(-1);
        }
        updateState();
    }

    private void jSpinnerNTicksStateChanged(javax.swing.event.ChangeEvent evt) {
        Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueNTicks((Integer) jSpinnerNTicks.getValue() - 1);
        if (!suspendNotify) {
            Shared.timer.notifyChange();
            Shared.instrTimer.notifyChange();
        }
        updateState();
    }

    private void jTextFieldNoteActionPerformed(java.awt.event.ActionEvent evt) {
        Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setNote(jTextFieldNote.getText());
    }

    private void jTextFieldNoteFocusLost(java.awt.event.FocusEvent evt) {
        Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setNote(jTextFieldNote.getText());
    }

    private void jButtonTablesActionPerformed(java.awt.event.ActionEvent evt) {
        execute(INSTR_TABLES);
    }

    private void jTableInstrKeyPressed(java.awt.event.KeyEvent evt) {
        switch(evt.getKeyCode()) {
            case KeyEvent.VK_INSERT:
                if (!dataTableModelInstr.insert(jTableInstr.getSelectedRow(), jTableInstr.getSelectedColumn())) {
                    jTableInstr.getToolkit().beep();
                } else {
                    Shared.timer.notifyChange();
                    Shared.instrTimer.notifyChange();
                }
                evt.consume();
                break;
            case KeyEvent.VK_DELETE:
                if (!dataTableModelInstr.delete(jTableInstr.getSelectedRow(), jTableInstr.getSelectedColumn())) {
                    jTableInstr.getToolkit().beep();
                } else {
                    Shared.timer.notifyChange();
                    Shared.instrTimer.notifyChange();
                }
                evt.consume();
                break;
        }
    }

    private void jTableInstrMousePressed(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) popUp(evt);
    }

    private void jTableInstrMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) popUp(evt);
    }

    private void jMenuItemStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(jTableInstr.getSelectedRow() + 1);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemNoStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(0);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemPulseStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(jTableInstr.getSelectedRow() + 1);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemPulseNoStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(0);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemPulseFixedActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogFixedPulse.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogFixedPulse.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogFixedPulse.setLocationRelativeTo(this);
        jDialogFixedPulse.setVisible(true);
    }

    private void jMenuItemPulseRelActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogRelPulse.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogRelPulse.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogRelPulse.setLocationRelativeTo(this);
        jDialogRelPulse.setVisible(true);
    }

    private void jMenuItemFreqStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(jTableInstr.getSelectedRow() + 1);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFreqNoStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(0);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFreqANActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogAbsNote.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogAbsNote.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogAbsNote.setLocationRelativeTo(this);
        jDialogAbsNote.updateInternal();
        jDialogAbsNote.setVisible(true);
    }

    private void jMenuItemFreqRNActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogRelNote.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogRelNote.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogRelNote.setLocationRelativeTo(this);
        jDialogRelNote.setVisible(true);
    }

    private void jMenuItemFreqRFActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogRelFreq.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogRelFreq.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogRelFreq.setLocationRelativeTo(this);
        jDialogRelFreq.setVisible(true);
    }

    private void jMenuItemFreqAFActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogFixedFreq.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogFixedFreq.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogFixedFreq.setLocationRelativeTo(this);
        jDialogFixedFreq.setVisible(true);
    }

    private void jMenuItemFilterFreqStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(jTableInstr.getSelectedRow() + 1);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFilterFreqNoStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(0);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFilterFreqRFActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogRelFilter.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogRelFilter.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogRelFilter.setLocationRelativeTo(this);
        jDialogRelFilter.setVisible(true);
    }

    private void jMenuItemFilterFreqAFActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogFixedFilter.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogFixedFilter.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogFixedFilter.setLocationRelativeTo(this);
        jDialogFixedFilter.setVisible(true);
    }

    private void jMenuItemFilterResStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(jTableInstr.getSelectedRow() + 1);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFilterResNoStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(0);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFilterResActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogFilterRes.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogFilterRes.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogFilterRes.setLocationRelativeTo(this);
        jDialogFilterRes.setVisible(true);
    }

    private void jMenuItemFilterTypeStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(jTableInstr.getSelectedRow() + 1);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFilterTypeNoStepActionPerformed(java.awt.event.ActionEvent evt) {
        instrTable.setStepPos(0);
        dataTableModelInstr.fireTableDataChanged();
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jMenuItemFilterTypeActionPerformed(java.awt.event.ActionEvent evt) {
        jDialogFilterType.setValues(instrTable, (Integer) jSpinnerInstrument.getValue() - 1, actRow);
        if (Shared.option.getPopupType() == 0) jDialogFilterType.setLocation((int) actLoc.getX(), (int) actLoc.getY()); else jDialogFilterType.setLocationRelativeTo(this);
        jDialogFilterType.setVisible(true);
        Shared.timer.notifyChange();
        Shared.instrTimer.notifyChange();
    }

    private void jTextFieldCTRL1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl2HR(Integer.parseInt(jTextFieldCTRL1.getText(), 16));
            Shared.timer.notifyChange();
            Shared.instrTimer.notifyChange();
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl2HR(-1);
        }
        updateState();
    }

    private void jTextFieldCTRL1FocusLost(java.awt.event.FocusEvent evt) {
        try {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl2HR(Integer.parseInt(jTextFieldCTRL1.getText(), 16));
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        } catch (java.lang.NumberFormatException e) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setValueCtrl2HR(-1);
        }
        updateState();
    }

    private void jRadioButtonWADSRItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setFlagOrder(false);
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        }
    }

    private void jRadioButtonADSRWItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            Shared.instruments[(Integer) jSpinnerInstrument.getValue() - 1].setFlagOrder(true);
            if (!suspendNotify) {
                Shared.timer.notifyChange();
                Shared.instrTimer.notifyChange();
            }
        }
    }

    private void jCheckBoxHRItemStateChanged(java.awt.event.ItemEvent evt) {
        int index = (Integer) jSpinnerInstrument.getValue() - 1;
        boolean value = jCheckBoxHR.isSelected();
        Shared.instruments[index].setFlagHR(value);
        if (value) {
            jTextFieldAD.setEnabled(true);
            jTextFieldSR.setEnabled(true);
            jTextFieldCTRL.setEnabled(true);
            jTextFieldCTRL1.setEnabled(true);
            jSpinnerNTicks.setEnabled(true);
            jCheckBoxGate.setSelected(false);
            Shared.instruments[index].setFlagGateOff(false);
        } else {
            jTextFieldAD.setEnabled(false);
            jTextFieldSR.setEnabled(false);
            jTextFieldCTRL.setEnabled(false);
            jTextFieldCTRL1.setEnabled(false);
            if (!jCheckBoxGate.isSelected()) jSpinnerNTicks.setEnabled(false);
        }
        if (!suspendNotify) {
            Shared.timer.notifyChange();
            Shared.instrTimer.notifyChange();
        }
        updateState();
    }

    private void jCheckBoxGateItemStateChanged(java.awt.event.ItemEvent evt) {
        int index = (Integer) jSpinnerInstrument.getValue() - 1;
        boolean value = jCheckBoxGate.isSelected();
        Shared.instruments[index].setFlagGateOff(value);
        if (value) {
            jTextFieldAD.setEnabled(false);
            jTextFieldSR.setEnabled(false);
            jTextFieldCTRL.setEnabled(false);
            jSpinnerNTicks.setEnabled(true);
            jCheckBoxHR.setSelected(false);
            Shared.instruments[index].setFlagHR(false);
        } else if (!jCheckBoxHR.isSelected()) jSpinnerNTicks.setEnabled(false);
        if (!suspendNotify) {
            Shared.timer.notifyChange();
            Shared.instrTimer.notifyChange();
        }
        updateState();
    }

    private void jTableInstrKeyTyped(java.awt.event.KeyEvent evt) {
    }

    private javax.swing.ButtonGroup buttonGroupOrder;

    private javax.swing.JButton jButtonClear;

    private javax.swing.JButton jButtonClose;

    private javax.swing.JButton jButtonGoFirst;

    private javax.swing.JButton jButtonGoLast;

    private javax.swing.JButton jButtonList;

    private javax.swing.JButton jButtonLoad;

    private javax.swing.JButton jButtonNew;

    private javax.swing.JButton jButtonNewWin;

    private javax.swing.JButton jButtonNext;

    private javax.swing.JButton jButtonPrev;

    private javax.swing.JButton jButtonSave;

    private javax.swing.JButton jButtonSaveAS;

    private javax.swing.JButton jButtonStop;

    private javax.swing.JButton jButtonTable;

    private javax.swing.JButton jButtonTables;

    private javax.swing.JCheckBox jCheckBoxGate;

    private javax.swing.JCheckBox jCheckBoxHR;

    private javax.swing.JLabel jLabelAD;

    private javax.swing.JLabel jLabelCTRL;

    private javax.swing.JLabel jLabelInstrument;

    private javax.swing.JLabel jLabelNTicks;

    private javax.swing.JLabel jLabelName;

    private javax.swing.JLabel jLabelNote;

    private javax.swing.JLabel jLabelOrder;

    private javax.swing.JLabel jLabelSR;

    private javax.swing.JLabel jLabelState;

    private javax.swing.JMenuItem jMenuItemFilterFreqAF;

    private javax.swing.JMenuItem jMenuItemFilterFreqNoStep;

    private javax.swing.JMenuItem jMenuItemFilterFreqRF;

    private javax.swing.JMenuItem jMenuItemFilterFreqStep;

    private javax.swing.JMenuItem jMenuItemFilterRes;

    private javax.swing.JMenuItem jMenuItemFilterResNoStep;

    private javax.swing.JMenuItem jMenuItemFilterResStep;

    private javax.swing.JMenuItem jMenuItemFilterType;

    private javax.swing.JMenuItem jMenuItemFilterTypeNoStep;

    private javax.swing.JMenuItem jMenuItemFilterTypeStep;

    private javax.swing.JMenuItem jMenuItemFreqAF;

    private javax.swing.JMenuItem jMenuItemFreqAN;

    private javax.swing.JMenuItem jMenuItemFreqNoStep;

    private javax.swing.JMenuItem jMenuItemFreqRF;

    private javax.swing.JMenuItem jMenuItemFreqRN;

    private javax.swing.JMenuItem jMenuItemFreqStep;

    private javax.swing.JMenuItem jMenuItemNoStep;

    private javax.swing.JMenuItem jMenuItemPulseFixed;

    private javax.swing.JMenuItem jMenuItemPulseNoStep;

    private javax.swing.JMenuItem jMenuItemPulseRel;

    private javax.swing.JMenuItem jMenuItemPulseStep;

    private javax.swing.JMenuItem jMenuItemStep;

    private javax.swing.JPanel jPanelDn;

    private javax.swing.JPanel jPanelHR;

    private javax.swing.JPanel jPanelUp;

    private jitt64.swing.JPianoPanel jPianoPanelNote;

    private javax.swing.JPopupMenu jPopupMenuFilterFreq;

    private javax.swing.JPopupMenu jPopupMenuFilterRes;

    private javax.swing.JPopupMenu jPopupMenuFilterType;

    private javax.swing.JPopupMenu jPopupMenuFreq;

    private javax.swing.JPopupMenu jPopupMenuOnlyStep;

    private javax.swing.JPopupMenu jPopupMenuPulse;

    private javax.swing.JRadioButton jRadioButtonADSRW;

    private javax.swing.JRadioButton jRadioButtonWADSR;

    private javax.swing.JScrollPane jScrollPaneTable;

    private javax.swing.JSeparator jSeparatorFilterFreq;

    private javax.swing.JSeparator jSeparatorFilterRes;

    private javax.swing.JSeparator jSeparatorFilterType;

    private javax.swing.JSeparator jSeparatorFreq;

    private javax.swing.JSeparator jSeparatorPulse;

    private javax.swing.JSpinner jSpinnerInstrument;

    private javax.swing.JSpinner jSpinnerNTicks;

    private javax.swing.JTable jTableInstr;

    private javax.swing.JTextField jTextFieldAD;

    private javax.swing.JTextField jTextFieldCTRL;

    private javax.swing.JTextField jTextFieldCTRL1;

    private javax.swing.JTextField jTextFieldName;

    private javax.swing.JTextField jTextFieldNote;

    private javax.swing.JTextField jTextFieldSR;

    private javax.swing.JToolBar jToolBarInstrument;
}
