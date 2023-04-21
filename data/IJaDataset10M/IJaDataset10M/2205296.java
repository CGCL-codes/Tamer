package com.monad.homerun.pkg.schedule;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.rmi.RemoteException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ListCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import org.jdesktop.swingx.JXMonthView;
import com.monad.homerun.action.Month;
import com.monad.homerun.action.Binding;
import com.monad.homerun.action.DateBinding;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.rmictrl.ScheduleCtrl;
import com.monad.homerun.rmictrl.ActionCtrl;
import com.monad.homerun.uiutl.BindDialog;
import com.monad.homerun.uiutl.NoteDialog;
import com.monad.homerun.uiutl.RmiAppBase;
import com.monad.homerun.util.TimeUtil;

public class Calendar extends RmiAppBase implements ActionListener {

    private static final long serialVersionUID = 7808367857433614081L;

    private static final int schedEdWidth = 320;

    private static final int schedEdHeight = 420;

    private Month selMonth = null;

    private int selDay = -1;

    private int selEntryIdx = -1;

    private DateBinding clipEntry = null;

    private Map<String, String[]> ruleMap = null;

    private Map<String, Month> monthCache = null;

    private ScheduleCtrl schedCtrl = null;

    private ActionCtrl actionCtrl = null;

    private DefaultListModel listModel = null;

    private boolean newMonth = false;

    private java.util.Calendar cal = null;

    private JMenuItem newMI = null;

    private JMenuItem exitMI = new JMenuItem("Exit", KeyEvent.VK_X);

    private JMenuItem editMI = null;

    private JMenuItem cutMI = null;

    private JMenuItem copyMI = null;

    private JMenuItem pasteMI = null;

    private JMenuItem noteMI = null;

    private JMenuItem helpWhatMI = new JMenuItem("What is this?", KeyEvent.VK_W);

    private JMenuItem helpHowMI = new JMenuItem("How do I ...?", KeyEvent.VK_H);

    private JMenuItem helpCmdMI = new JMenuItem("Commands", KeyEvent.VK_C);

    private JButton addButton = new JButton();

    private JButton editButton = new JButton();

    private JButton noteButton = new JButton();

    private JButton copyButton = new JButton();

    private JButton cutButton = new JButton();

    private JButton pasteButton = new JButton();

    private JPanel contentPanel = new JPanel(new BorderLayout());

    private JToolBar toolBar = new JToolBar();

    private JXMonthView monthView = null;

    private JMenuBar menuBar = new JMenuBar();

    private JLabel listLabel = new JLabel("Entries");

    private SimpleDateFormat sdf = null;

    public Calendar() {
        appName = "Calendar";
        appIcon = "month.gif";
        appWidth = schedEdWidth;
        appHeight = schedEdHeight;
        appResize = false;
    }

    public void start() {
        super.start();
        ruleMap = new HashMap<String, String[]>();
        monthCache = new HashMap<String, Month>();
        cal = java.util.Calendar.getInstance();
        sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        try {
            schedCtrl = (ScheduleCtrl) appReg.getServerControl("schedule");
            cal.setTimeInMillis(System.currentTimeMillis());
            setMonth(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH));
            actionCtrl = (ActionCtrl) appReg.getServerControl("rule");
            String[] ruleCats = actionCtrl.getRuleCategories();
            for (int j = 0; j < ruleCats.length; j++) {
                ruleMap.put(ruleCats[j], null);
            }
        } catch (RemoteException remExp) {
            setSysMessage("Unable to initialize");
            remExp.printStackTrace();
        }
        setEntryEditContext();
        repaint();
    }

    public void initUIComponents(Container cp) {
        cp.setLayout(new BorderLayout());
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        newMI = new JMenuItem("New...", getHRIcon("toolbar", "addtsk_tsk.gif"));
        newMI.setMnemonic(KeyEvent.VK_N);
        newMI.addActionListener(this);
        fileMenu.add(newMI);
        fileMenu.addSeparator();
        exitMI.addActionListener(this);
        fileMenu.add(exitMI);
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        editMI = new JMenuItem("Entry", getHRIcon("toolbar", "Edit16.gif"));
        editMI.setMnemonic(KeyEvent.VK_E);
        editMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        editMI.addActionListener(this);
        editMenu.add(editMI);
        editMenu.addSeparator();
        cutMI = new JMenuItem("Cut", getHRIcon("toolbar", "Cut16.gif"));
        cutMI.setMnemonic(KeyEvent.VK_T);
        cutMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cutMI.addActionListener(this);
        editMenu.add(cutMI);
        copyMI = new JMenuItem("Copy", getHRIcon("toolbar", "Copy16.gif"));
        copyMI.setMnemonic(KeyEvent.VK_C);
        copyMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copyMI.addActionListener(this);
        editMenu.add(copyMI);
        pasteMI = new JMenuItem("Paste", getHRIcon("toolbar", "Paste16.gif"));
        pasteMI.setMnemonic(KeyEvent.VK_P);
        pasteMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        pasteMI.addActionListener(this);
        editMenu.add(pasteMI);
        editMenu.addSeparator();
        noteMI = new JMenuItem("Note", getHRIcon("toolbar", "note_edit.png"));
        noteMI.setMnemonic(KeyEvent.VK_N);
        noteMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        noteMI.addActionListener(this);
        editMenu.add(noteMI);
        menuBar.add(editMenu);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpWhatMI.addActionListener(this);
        helpMenu.add(helpWhatMI);
        helpHowMI.addActionListener(this);
        helpMenu.add(helpHowMI);
        helpCmdMI.addActionListener(this);
        helpMenu.add(helpCmdMI);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        initToolbarButton(addButton, "addtsk_tsk.gif", "New Entry");
        initToolbarButton(editButton, "Edit16.gif", "Edit Entry");
        initToolbarButton(noteButton, "note_edit.png", "Edit Note");
        toolBar.addSeparator();
        initToolbarButton(cutButton, "Cut16.gif", "Cut Entry");
        initToolbarButton(copyButton, "Copy16.gif", "Copy Entry");
        initToolbarButton(pasteButton, "Paste16.gif", "Paste Entry");
        JPanel listPanel = new JPanel(new BorderLayout());
        listModel = new DefaultListModel();
        listPanel.add(listLabel, BorderLayout.NORTH);
        listPanel.add(makeDayList(listModel), BorderLayout.CENTER);
        monthView = new JXMonthView();
        monthView.setTraversable(true);
        monthView.addActionListener(this);
        monthView.addMouseListener(new ViewMouseHandler());
        contentPanel.add(monthView, BorderLayout.CENTER);
        contentPanel.add(listPanel, BorderLayout.SOUTH);
        cp.add(toolBar, BorderLayout.NORTH);
        cp.add(contentPanel, BorderLayout.CENTER);
        cp.add(msgLabel, BorderLayout.SOUTH);
    }

    private void initToolbarButton(JButton button, String icon, String tipText) {
        button.setIcon(getHRIcon("toolbar", icon));
        button.setToolTipText(tipText);
        button.addActionListener(this);
        toolBar.add(button);
    }

    private void mapMonthDay(int dayIdx) {
        DateBinding[] entries = selMonth.getEntries((short) dayIdx);
        if (GlobalProps.DEBUG) {
            System.out.println("mapMonthD: got entries: " + entries.length);
        }
        listModel.clear();
        for (DateBinding entry : entries) {
            listModel.addElement(entry);
        }
        cal.set(selMonth.getYear(), selMonth.getMonth(), dayIdx);
        String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
        listLabel.setText("Entries for " + dateStr);
    }

    private void clearMonthDay() {
        selDay = -1;
        monthView.clearSelection();
        listModel.clear();
        listLabel.setText("Entries");
    }

    private void mapMonthEntries() {
        Integer[] eDays = selMonth.getEntryDays();
        if (GlobalProps.DEBUG) {
            System.out.println("mapMonthEnt: got # entries: " + eDays.length);
        }
        Date[] dates = new Date[eDays.length];
        for (int i = 0; i < eDays.length; i++) {
            cal.set(selMonth.getYear(), selMonth.getMonth(), eDays[i]);
            dates[i] = cal.getTime();
        }
        monthView.setFlaggedDates(dates);
    }

    public DateBinding getSelectedEntry() {
        if (selEntryIdx != -1) {
            return (DateBinding) listModel.get(selEntryIdx);
        }
        return null;
    }

    private Component makeDayList(ListModel model) {
        JList list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new EntryCellRenderer());
        list.addListSelectionListener(new EntrySelectionListener(list));
        list.addMouseListener(new MouseHandler());
        return new JScrollPane(list);
    }

    private class EntryCellRenderer extends JPanel implements ListCellRenderer {

        private static final long serialVersionUID = 4681778391760898778L;

        private JLabel timeLabel = null;

        private JLabel modeLabel = null;

        private JLabel nameLabel = null;

        public EntryCellRenderer() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            timeLabel = new JLabel();
            add(timeLabel);
            add(Box.createRigidArea(new Dimension(10, 0)));
            modeLabel = new JLabel();
            add(modeLabel);
            add(Box.createRigidArea(new Dimension(10, 0)));
            nameLabel = new JLabel();
            add(nameLabel);
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            DateBinding entry = (DateBinding) value;
            timeLabel.setText(TimeUtil.stringFormat(entry.getMinutes()));
            ImageIcon icon = getHRIcon("system", Binding.getModeIconName(entry.getMode()));
            modeLabel.setIcon(icon);
            nameLabel.setText(entry.getActionName());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setToolTipText(entry.getNote());
            return this;
        }
    }

    public void actionPerformed(ActionEvent actionEvt) {
        setSysMessage(null);
        Object source = actionEvt.getSource();
        try {
            if (source == monthView) {
                calEvent(actionEvt);
            } else if (source == exitMI) {
                safeExit();
            } else if (source == newMI || source == addButton) {
                addEntry();
            } else if (source == editMI || source == editButton) {
                editEntry();
            } else if (source == cutMI || source == cutButton) {
                clipEntry = getSelectedEntry();
                deleteEntry();
            } else if (source == copyMI || source == copyButton) {
                clipEntry = getSelectedEntry();
                setEntryEditContext();
            } else if (source == pasteMI || source == pasteButton) {
                pasteEntry();
            } else if (source == noteMI || source == noteButton) {
                editNote();
            } else if (source == helpWhatMI) {
                showHelp("calendar");
            } else if (source == helpHowMI) {
                showHelp("calendar-howto");
            } else if (source == helpCmdMI) {
                showHelp("calendar-cmds");
            }
        } catch (RemoteException remExp) {
            if (GlobalProps.DEBUG) {
                System.out.println("actionPerformed caught exception: " + remExp);
            }
            setSysMessage("Update failed");
        }
        repaint();
    }

    private class EntrySelectionListener implements ListSelectionListener {

        private JList list = null;

        public EntrySelectionListener(JList list) {
            this.list = list;
        }

        public void valueChanged(ListSelectionEvent listEvt) {
            selEntryIdx = list.getSelectedIndex();
            setEntryEditContext();
        }
    }

    private class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                editEntry();
            }
        }
    }

    private class ViewMouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            Date newDate = monthView.getFirstDisplayedDay();
            cal.setTime(newDate);
            int newYear = cal.get(java.util.Calendar.YEAR);
            int newMonth = cal.get(java.util.Calendar.MONTH);
            if (GlobalProps.DEBUG) {
                System.out.println("ViewMH: newY: " + newYear + " newM: " + newMonth);
            }
            if (selMonth.getYear() != newYear || selMonth.getMonth() != newMonth) {
                try {
                    setMonth(newYear, newMonth);
                } catch (Exception exp) {
                    ;
                }
            }
        }
    }

    private void setEntryEditContext() {
        boolean canEdit = getSelectedEntry() != null;
        boolean haveRules = ruleMap.size() > 0;
        cal.set(selMonth.getYear(), selMonth.getMonth(), selDay);
        java.util.Calendar nowCal = java.util.Calendar.getInstance();
        long day = 1000 * 60 * 60 * 24;
        nowCal.setTimeInMillis(System.currentTimeMillis() - day);
        boolean future = cal.after(nowCal);
        newMI.setEnabled(future && haveRules);
        addButton.setEnabled(future && haveRules);
        editMI.setEnabled(future && canEdit);
        editButton.setEnabled(future && canEdit);
        cutMI.setEnabled(future && canEdit);
        cutButton.setEnabled(future && canEdit);
        copyMI.setEnabled(canEdit);
        copyButton.setEnabled(canEdit);
        noteMI.setEnabled(future && canEdit);
        noteButton.setEnabled(future && canEdit);
        pasteMI.setEnabled(future && clipEntry != null);
        pasteButton.setEnabled(future && clipEntry != null);
    }

    private void addEntry() throws RemoteException {
        DateBindDialog dialog = new DateBindDialog();
        dialog.setVisible(true);
        DateBinding addEntry = (DateBinding) dialog.getBinding();
        if (addEntry != null) {
            if (legalChange(addEntry)) {
                selMonth.addEntry(addEntry);
                saveMonth();
                mapMonthDay(selDay);
                setEntryEditContext();
            } else {
                setSysMessage("Entry time has already passed");
            }
        }
    }

    private void editEntry() {
        DateBinding entry = getSelectedEntry();
        if (entry != null) {
            DateBindDialog dialog = new DateBindDialog(new DateBinding(entry));
            dialog.setVisible(true);
            DateBinding replEntry = (DateBinding) dialog.getBinding();
            if (replEntry != null) {
                if (legalChange(replEntry)) {
                    selMonth.removeEntry(entry);
                    selMonth.addEntry(replEntry);
                    saveMonth();
                    mapMonthDay(selDay);
                    setEntryEditContext();
                } else {
                    setSysMessage("Entry time has already passed");
                }
            }
        }
    }

    private void pasteEntry() {
        DateBinding pasteEntry = new DateBinding(clipEntry);
        pasteEntry.setDay((short) selDay);
        if (legalChange(pasteEntry)) {
            selMonth.addEntry(pasteEntry);
            saveMonth();
            mapMonthDay(selDay);
            setEntryEditContext();
        } else {
            setSysMessage("Entry time has already passed");
        }
    }

    private void deleteEntry() {
        DateBinding delEntry = getSelectedEntry();
        String msg = "Really delete '" + Binding.getActionMode(delEntry.getMode());
        msg += " " + delEntry.getActionName() + " at " + TimeUtil.stringFormat(delEntry.getMinutes()) + "'?";
        int ret = JOptionPane.showConfirmDialog(null, msg, "Warning", JOptionPane.YES_NO_OPTION);
        if (ret == JOptionPane.YES_OPTION && selEntryIdx != -1) {
            if (legalChange(delEntry)) {
                selMonth.removeEntry(delEntry);
                saveMonth();
                mapMonthDay(selDay);
                setEntryEditContext();
            } else {
                setSysMessage("Entry time has already passed");
            }
        }
    }

    private boolean legalChange(DateBinding entry) {
        cal.setTimeInMillis(System.currentTimeMillis());
        if (selMonth.getYear() > cal.get(java.util.Calendar.YEAR) || selMonth.getMonth() > cal.get(java.util.Calendar.MONTH) || entry.getDay() > cal.get(java.util.Calendar.DAY_OF_MONTH)) {
            return true;
        }
        int nowMins = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE);
        return (entry.getMinutes() > nowMins);
    }

    private void editNote() {
        String note = null;
        DateBinding entry = null;
        if (selEntryIdx != -1) {
            entry = getSelectedEntry();
            note = entry.getNote();
        } else {
            setSysMessage("An entry must be selected");
            return;
        }
        NoteDialog nd = new NoteDialog(getFrame(), note);
        nd.setVisible(true);
        if (nd.isAltered()) {
            DateBinding replEntry = new DateBinding(entry);
            replEntry.setNote(nd.getNote());
            selMonth.removeEntry(entry);
            selMonth.addEntry(replEntry);
            saveMonth();
            setEntryEditContext();
        }
    }

    private class DateBindDialog extends BindDialog {

        private static final long serialVersionUID = -1787421838732570597L;

        public DateBindDialog() {
            super(getFrame(), ruleMap, actionCtrl, "time");
        }

        public DateBindDialog(DateBinding binding) {
            super(getFrame(), ruleMap, actionCtrl, "time", binding);
        }

        public Binding makeBinding() {
            Binding binding = new DateBinding(selDay, getUITime(selDay), Binding.RUN_MODE, (String) rulesBox.getSelectedItem(), (String) catsBox.getSelectedItem());
            Map<String, String> varMap = getBindingVariables();
            if (varMap.size() > 0) {
                binding.setProperties(varMap);
            }
            return binding;
        }
    }

    private void saveMonth() {
        String selectedName = selMonth.getName();
        boolean res = false;
        try {
            if (newMonth) {
                res = schedCtrl.addMonth(selMonth);
                newMonth = false;
            } else {
                res = schedCtrl.updateMonth(selMonth);
            }
        } catch (RemoteException re) {
            res = false;
        }
        if (res) {
            setSysMessage("Month '" + selectedName + "' saved");
            setEntryEditContext();
        } else {
            setSysMessage("Unable to save '" + selectedName + "'");
        }
    }

    private void calEvent(ActionEvent event) throws RemoteException {
        Date selDate = monthView.getSelectionDate();
        cal.setTime(selDate);
        int year = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH);
        int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
        if (GlobalProps.DEBUG) {
            System.out.println("calEvent - year : " + year + " month: " + month + " day: " + day);
        }
        if (year != selMonth.getYear() || month != selMonth.getMonth()) {
            setMonth(year, month);
        } else if (day != selDay) {
            setDay(day);
        }
    }

    private void setMonth(int year, int month) throws RemoteException {
        setSysMessage(null);
        String monthName = year + "-" + month;
        if (GlobalProps.DEBUG) {
            System.out.println("setMonth- month: " + monthName);
        }
        selMonth = monthCache.get(monthName);
        if (selMonth == null) {
            if (GlobalProps.DEBUG) {
                System.out.println("setMonth- cache miss for: " + monthName);
            }
            selMonth = schedCtrl.getMonth(monthName);
            if (selMonth != null) {
                if (GlobalProps.DEBUG) {
                    System.out.println("setMonth- server miss for: " + monthName);
                }
                mapMonthEntries();
                monthCache.put(monthName, selMonth);
            }
        }
        newMonth = (selMonth == null);
        if (newMonth) {
            if (GlobalProps.DEBUG) {
                System.out.println("setMonth- new Month for: " + monthName);
            }
            selMonth = new Month(year, month);
        }
        setEntryEditContext();
        clearMonthDay();
    }

    private void setDay(int day) {
        mapMonthDay(day);
        selDay = day;
        setEntryEditContext();
    }

    public static void main(String[] args) {
        new Calendar().runAppInFrame(args);
    }
}
