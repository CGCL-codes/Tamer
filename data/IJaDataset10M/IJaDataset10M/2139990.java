package com.arykow.applications.ugabe.standalone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.arykow.applications.ugabe.client.CPU;
import com.arykow.applications.ugabe.client.CPURunner;
import com.arykow.applications.ugabe.client.Disassembler;

public class Debugger implements ActionListener, ItemListener, KeyListener, MouseListener, AdjustmentListener {

    public static boolean RIGHT_TO_LEFT = false;

    private static final int aaarg = 650;

    public JTable regs1;

    public JTable regs2;

    public JTable hwregs;

    public JTable mem;

    public boolean memiseditable = true;

    public JTable instrs;

    public JComboBox cmds;

    public MenuItemArrayGroup disassemblerRadioGroup;

    public JRadioButtonMenuItem radioButtonSimpleDisasm;

    public JRadioButtonMenuItem radioButtonExtendedDisasm;

    public JPopupMenu popup;

    private JScrollBar memScroller;

    private Disassembler deasm;

    private int memaddr = 0;

    private Color UpdateColor = new Color(255, 200, 200);

    public DebugRunner runner;

    GUI gui;

    private Font MonoFont = FHandler.getVeraMonoFont();

    private RDParser parser;

    private int[] oldRegVal;

    private Writer logwriter;

    private long remoteDebugOffset = 0;

    private int bpm = -1;

    public Debugger(GUI gui, String logfilename) {
        this.gui = gui;
        try {
            if (!logfilename.equals("")) {
                if (logfilename.startsWith("tcp://")) {
                    Socket LogOutputSocket = new Socket(logfilename.substring(6), 2016);
                    logwriter = new BufferedWriter(new OutputStreamWriter(LogOutputSocket.getOutputStream()));
                } else {
                    logwriter = new BufferedWriter(new FileWriter(logfilename));
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error opening logfile:" + e.getMessage());
            logwriter = null;
        }
        deasm = new Disassembler(gui.cpu, Disassembler.SIMPLE_DISASSEMBLY);
        oldRegVal = new int[11];
        parser = new RDParser();
        runner = new DebugRunner(this);
        createAndShowGUI();
        update();
    }

    void setRemoteDebugOffset(long i) {
        remoteDebugOffset = i;
    }

    public class DebugRunner implements Runnable, CPURunner {

        private volatile int threadStatus = 0;

        private Thread cpurunthread;

        private Debugger dbg;

        private int stopaddr = -1;

        private int watchaddr = -1;

        private int runFor = -1;

        private int instrstop = -1;

        private int breakinstr = -1;

        private int cyclestop = -1;

        public boolean hasThread(Thread t) {
            return cpurunthread.equals(t);
        }

        public synchronized void suspend() {
            while (threadStatus != 0) {
                threadStatus = 3;
                while (threadStatus == 3) {
                    {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                    ;
                }
                ;
            }
        }

        public synchronized void resume() {
            if (!gui.cpu.canRun()) return;
            if (threadStatus != 2) {
                threadStatus = 1;
                while (threadStatus == 1) {
                    {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                    ;
                }
                ;
            }
        }

        public boolean isRunning() {
            return (threadStatus != 0);
        }

        public void setBreakPoint(int addr) {
            if (threadStatus == 0) stopaddr = addr;
        }

        public void setBreakInstr(int instr) {
            if (threadStatus == 0) breakinstr = instr;
        }

        public void setWatchPoint(int addr) {
            if (threadStatus == 0) watchaddr = addr;
        }

        public void setInstrStop(int instrs) {
            if (threadStatus == 0) instrstop = instrs;
        }

        public void setCycleStop(int cycles) {
            cyclestop = cycles;
        }

        public void setRunFor(int i) {
            if (threadStatus == 0) this.runFor = i;
        }

        public int getRunFor() {
            return this.runFor;
        }

        public void decRunFor() {
            this.runFor--;
        }

        public DebugRunner(Debugger tdbg) {
            dbg = tdbg;
            cpurunthread = new Thread(this);
            cpurunthread.start();
            while (!cpurunthread.isAlive()) {
                {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ;
            }
            ;
        }

        public void run() {
            while (true) {
                while (threadStatus == 0) {
                    {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                        }
                    }
                    ;
                }
                ;
                if (threadStatus == 1) threadStatus = 2;
                boolean keeprunning = true;
                while ((threadStatus == 2) && keeprunning) {
                    int wval = (watchaddr >= 0) ? dbg.gui.cpu.read(watchaddr) : 0;
                    int OldPC = gui.cpu.getPC();
                    try {
                        gui.cpu.runlooponce();
                        decRunFor();
                    } catch (Throwable t) {
                        System.out.println("+=================================================+");
                        System.out.println("UNHANDLED ERROR:");
                        t.printStackTrace();
                        System.out.println("+-------------------------------------------------+");
                        gui.cpu.setPC(OldPC);
                        gui.cpu.printCPUstatus();
                        System.out.println("+=================================================+");
                        keeprunning = false;
                    }
                    if ((logwriter != null) && (gui.cpu.totalCycleCount >= remoteDebugOffset)) {
                        System.out.println("" + gui.cpu.totalCycleCount);
                        String out = String.format("cycles=" + gui.cpu.totalCycleCount + " PC=$%04x AF=$%02x%02x BC=$%02x%02x DE=$%02x%02x HL=$%02x%02x SP=$%04x\n", gui.cpu.getPC(), gui.cpu.A, gui.cpu.F, gui.cpu.B, gui.cpu.C, gui.cpu.D, gui.cpu.E, gui.cpu.H, gui.cpu.L, gui.cpu.SP);
                        try {
                            logwriter.write(out);
                        } catch (java.io.IOException e) {
                            System.out.println("Error writing logfile:" + e.getMessage());
                            logwriter = null;
                        }
                    }
                    if (dbg.gui.cpu.totalInstrCount == instrstop) {
                        keeprunning = false;
                    }
                    if (dbg.gui.cpu.getPC() == stopaddr) {
                        keeprunning = false;
                    }
                    if (cyclestop != -1 && cyclestop <= dbg.gui.cpu.totalCycleCount) {
                        keeprunning = false;
                    }
                    if ((watchaddr >= 0) && (wval != dbg.gui.cpu.read(watchaddr))) {
                        keeprunning = false;
                    }
                    if ((dbg.gui.cpu.B < 0) || (dbg.gui.cpu.B > 0xff) || (dbg.gui.cpu.C < 0) || (dbg.gui.cpu.C > 0xff) || (dbg.gui.cpu.D < 0) || (dbg.gui.cpu.D > 0xff) || (dbg.gui.cpu.E < 0) || (dbg.gui.cpu.E > 0xff) || (dbg.gui.cpu.F < 0) || (dbg.gui.cpu.F > 0xff) || (dbg.gui.cpu.H < 0) || (dbg.gui.cpu.H > 0xff) || (dbg.gui.cpu.L < 0) || (dbg.gui.cpu.L > 0xff) || (dbg.gui.cpu.SP < 0) || (dbg.gui.cpu.SP > 0xffff) || (dbg.gui.cpu.getPC() < 0) || (dbg.gui.cpu.getPC() > 0xffff)) {
                        keeprunning = false;
                    }
                    if (dbg.gui.cpu.read(dbg.gui.cpu.getPC()) == breakinstr) {
                        keeprunning = false;
                    }
                    if ((bpm >= 0) && (bpm == dbg.gui.cpu.last_memory_access)) {
                        keeprunning = false;
                        System.out.printf("Break due to memory access of $%04x\n", dbg.gui.cpu.last_memory_access);
                    }
                    if (getRunFor() == 0) keeprunning = false;
                }
                if (threadStatus == 2) threadStatus = 3;
                if (threadStatus == 3) threadStatus = 0;
                dbg.update();
            }
        }
    }

    public class MyCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private Color[] Colors = null;

        public MyCellRenderer(Color[] c) {
            super();
            Colors = c;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(String.valueOf(value));
            if ((this.Colors != null)) setBackground(this.Colors[row]); else setBackground(Color.WHITE);
            String s = "";
            String brep = "";
            if ((table == regs1) || (table == regs2) || (table == hwregs)) {
                int i = ((String) table.getValueAt(row, column)).indexOf('$');
                if (i > -1) {
                    s = (String) table.getValueAt(row, column);
                    s = s.substring(i);
                    i = parser.StrToInt(s);
                    brep = Integer.toBinaryString(i);
                    while (brep.length() < ((regs2 == table) ? 16 : 8)) brep = "0" + brep;
                    s = String.format(((regs2 == table) ? "%dd  %04xh " : "%dd  %02xh ") + brep + "b", i, i);
                }
                if (table == regs2 && column == 2) {
                    int intstat = (gui.cpu.IOP[0x0f] & 0x1f) | ((gui.cpu.IE & 0x1f) << 5) | (gui.cpu.IME ? 1 << 10 : 0);
                    s = "<HTML><PRE>";
                    s += " IME     : " + ((intstat & (1 << 10)) != 0 ? "Enabled" : "Disabled");
                    s += " <BR>";
                    s += " Joypad  : " + ((gui.cpu.IE & (1 << 4)) != 0 ? "Enabled" : "Disabled");
                    s += ((gui.cpu.IOP[0x0f] & (1 << 4)) != 0 ? ", pending" : "");
                    s += " <BR>";
                    s += " Serial  : " + ((gui.cpu.IE & (1 << 3)) != 0 ? "Enabled" : "Disabled");
                    s += ((gui.cpu.IOP[0x0f] & (1 << 3)) != 0 ? ", pending" : "");
                    s += " <BR>";
                    s += " Timer   : " + ((gui.cpu.IE & (1 << 2)) != 0 ? "Enabled" : "Disabled");
                    s += ((gui.cpu.IOP[0x0f] & (1 << 2)) != 0 ? ", pending" : "");
                    s += " <BR>";
                    s += " LCD/STAT: " + ((gui.cpu.IE & (1 << 1)) != 0 ? "Enabled" : "Disabled");
                    s += ((gui.cpu.IOP[0x0f] & (1 << 1)) != 0 ? ", pending" : "");
                    s += " <BR>";
                    s += " V-Blank : " + ((gui.cpu.IE & (1 << 0)) != 0 ? "Enabled" : "Disabled");
                    s += ((gui.cpu.IOP[0x0f] & (1 << 0)) != 0 ? ", pending" : "");
                    s += " <BR>";
                    s += "</PRE></HTML>";
                } else if (table == hwregs && row == 0 && column == 0) {
                    s = "<HTML><PRE>";
                    s += " " + String.format(((regs2 == table) ? "%dd  %04xh " : "%dd  %02xh ") + brep + "b", i, i) + "<BR>";
                    s += " ----------------------------------------- <BR>";
                    s += " LCD Display                   : " + ((gui.cpu.videoController.lcdController.operationEnabled) ? "Enabled" : "Disabled") + " <BR>";
                    s += " Window Tile Map Address       : " + ((gui.cpu.videoController.lcdController.windowTileMapAddress == 0x1C00) ? "9C00-9FFF" : "9800-9BFF") + " <BR>";
                    s += " Display Window                : " + ((gui.cpu.videoController.lcdController.windowDisplayEnabled) ? "Yes" : "No") + " <BR>";
                    s += " BG & Window Tile Data Address : " + ((gui.cpu.videoController.lcdController.tileMapAddressLow) ? "8000-8FFF" : "8800-97FF") + " <BR>";
                    s += " BG Tile Map Display Address   : " + ((gui.cpu.videoController.lcdController.backgroundTileMapAddress == 0x1800) ? "9800-9BFF" : "9C00-9FFF") + " <BR>";
                    s += " Sprite Size                   : " + ((gui.cpu.videoController.lcdController.spriteHeight == 16) ? "8x16" : "8x8") + " <BR>";
                    s += " Display Sprites               : " + ((gui.cpu.videoController.lcdController.spriteDisplayEnabled) ? "Yes" : "No") + " <BR>";
                    s += " Display Background            : " + ((gui.cpu.videoController.lcdController.backgroundDisplayEnabled) ? "Yes" : "No") + " <BR>";
                    s += "</PRE></HTML>";
                } else if (table == hwregs && row == 0 && column == 1) {
                    int j = gui.cpu.videoController.STAT;
                    s = "<HTML><PRE>";
                    s += " " + String.format(((regs2 == table) ? "%dd  %04xh " : "%dd  %02xh ") + brep + "b", i, i) + "<BR>";
                    s += " ----------------------------------------------------------------- <BR>";
                    s += " LY=LYC Coincidence Interrupt : " + (((j & (1 << 6)) != 0) ? "Enabled" : "Disabled") + " <BR>";
                    s += " Mode 2 OAM Interrupt         : " + (((j & (1 << 5)) != 0) ? "Enabled" : "Disabled") + " <BR>";
                    s += " Mode 1 V-Blank Interrupt     : " + (((j & (1 << 4)) != 0) ? "Enabled" : "Disabled") + " <BR>";
                    s += " Mode 0 H-Blank Interrupt     : " + (((j & (1 << 3)) != 0) ? "Enabled" : "Disabled") + " <BR>";
                    s += " Coincidence Flag             : " + (((j & (1 << 2)) != 0) ? "Set" : "Unset") + " <BR>";
                    s += " Mode Flag                    : " + (j & 3) + " (" + ((((j & 3) == 0) ? "H-Blank" : (((j & 3) == 1) ? "V-Blank" : (((j & 3) == 2) ? "Searching OAM-RAM" : "Transfering Data to LCD Driver")))) + ") <BR>";
                    s += "</PRE></HTML>";
                }
                setToolTipText(s);
            } else {
                setToolTipText("");
            }
            setFont(MonoFont);
            return this;
        }
    }

    public class EditMemTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            if (memiseditable) {
                if (evt.getType() == TableModelEvent.UPDATE) {
                    int column = evt.getColumn();
                    int row = evt.getFirstRow();
                    int index = (row) * 8 + (column - 2) + memaddr;
                    int i = memaddr;
                    memaddr = index;
                    prepareParser();
                    memaddr = i;
                    int value = parser.Evaluate(((String) mem.getValueAt(row, column)).trim());
                    gui.cpu.write(index, value);
                    update();
                }
            }
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        memaddr = e.getValue();
        update();
    }

    public void addComponentsToPane(Container contentPane) {
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        if (RIGHT_TO_LEFT) {
            contentPane.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }
        JScrollPane scroll = new JScrollPane(new JLabel("Registers:"));
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.LINE_END);
        regs1 = new JTable(1, 8);
        regs1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        regs1.setTableHeader(null);
        regs1.setFont(MonoFont);
        scroll = new JScrollPane(regs1);
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.NORTH);
        regs2 = new JTable(1, 4);
        regs2.setCellSelectionEnabled(false);
        regs2.setColumnSelectionAllowed(false);
        regs2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        regs2.setTableHeader(null);
        regs2.setFont(MonoFont);
        scroll = new JScrollPane(regs2);
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.NORTH);
        scroll = new JScrollPane(new JLabel("Hardware registers:"));
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.LINE_END);
        hwregs = new JTable(5, 4);
        hwregs.setCellSelectionEnabled(false);
        hwregs.setColumnSelectionAllowed(false);
        hwregs.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        hwregs.setTableHeader(null);
        hwregs.setFont(MonoFont);
        scroll = new JScrollPane(hwregs);
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19 * 5));
        contentPane.add(scroll, BorderLayout.NORTH);
        scroll = new JScrollPane(new JLabel("Memory:"));
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.LINE_END);
        JPanel memPanel = new JPanel();
        memPanel.setLayout(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(8, 8 + 2);
        tableModel.addTableModelListener(new EditMemTableModelListener());
        mem = new JTable();
        mem.setModel(tableModel);
        mem.setSurrendersFocusOnKeystroke(true);
        mem.setTableHeader(null);
        mem.setFont(MonoFont);
        scroll = new JScrollPane(mem);
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 131));
        memScroller = new JScrollBar(java.awt.Adjustable.VERTICAL, 0, 100, 0, 0xFFFF + 37);
        memScroller.addAdjustmentListener(this);
        contentPane.add(memScroller, BorderLayout.EAST);
        memPanel.add(mem, BorderLayout.CENTER);
        memPanel.add(memScroller, BorderLayout.EAST);
        contentPane.add(memPanel, BorderLayout.LINE_END);
        scroll = new JScrollPane(new JLabel("Instructions:"));
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.LINE_END);
        instrs = new JTable(16, 1);
        instrs.setTableHeader(null);
        TableColumnModel m = instrs.getColumnModel();
        TableColumn c = m.getColumn(0);
        Color[] ccc = new Color[16];
        for (int i = 0; i < 16; ++i) ccc[i] = new Color(255, 255, 255);
        ccc[7] = new Color(222, 222, 255);
        MyCellRenderer r = new MyCellRenderer(ccc);
        r.setFont(MonoFont);
        c.setCellRenderer(r);
        scroll = new JScrollPane(instrs);
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 259));
        contentPane.add(scroll, BorderLayout.LINE_END);
        scroll = new JScrollPane(new JLabel("Commands:"));
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 19));
        contentPane.add(scroll, BorderLayout.LINE_END);
        cmds = new JComboBox();
        cmds.setEditable(true);
        cmds.addActionListener(this);
        cmds.setFont(MonoFont);
        scroll = new JScrollPane(cmds);
        scroll.setMaximumSize(new Dimension(aaarg, Integer.MAX_VALUE));
        scroll.setPreferredSize(new Dimension(aaarg, 23));
        contentPane.add(scroll, BorderLayout.LINE_END);
        popup = new JPopupMenu();
        disassemblerRadioGroup = new MenuItemArrayGroup();
        radioButtonSimpleDisasm = new JRadioButtonMenuItem("Simple disassembler");
        radioButtonSimpleDisasm.setSelected(true);
        radioButtonExtendedDisasm = new JRadioButtonMenuItem("Extended disassembler");
        radioButtonSimpleDisasm.setMnemonic(KeyEvent.VK_S);
        radioButtonExtendedDisasm.setMnemonic(KeyEvent.VK_E);
        disassemblerRadioGroup.add(radioButtonSimpleDisasm);
        disassemblerRadioGroup.add(radioButtonExtendedDisasm);
        disassemblerRadioGroup.addActionListener(this);
        disassemblerRadioGroup.addToMenu(popup);
        MouseListener popupListener = new PopupListener();
        instrs.addMouseListener(popupListener);
    }

    class PopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    public synchronized void update() {
        if (!runner.isRunning() && gui.cpu.canRun()) {
            updateRegisters();
            updateHWRegs();
            updateMemory();
            updateInstructions();
            memScroller.setValue(Math.min(memaddr, memScroller.getMaximum()));
        }
    }

    public int[] oldHWRegs = new int[20];

    public int[] watches = { -1, -1 };

    public void updateHWRegs() {
        TableColumnModel m = hwregs.getColumnModel();
        TableColumn c;
        Color[] colors = new Color[5];
        colors[3] = Color.BLUE;
        int i = 0, j = 0, k = 0;
        i = gui.cpu.read(0xFF40);
        hwregs.setValueAt(String.format("ff40: LCDC=$%02x", i), k, 0);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF48);
        hwregs.setValueAt(String.format("ff48: OBP0=$%02x", i), k, 0);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF07);
        hwregs.setValueAt(String.format("ff07:  TAC=$%02x", i), k, 0);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFFFF);
        hwregs.setValueAt(String.format("ffff:   IE=$%02x", i), k, 0);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF43);
        hwregs.setValueAt(String.format("ff43:  SCX=$%02x", i), k, 0);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        c = m.getColumn(0);
        c.setCellRenderer(new MyCellRenderer(colors));
        colors = new Color[5];
        k = 0;
        i = gui.cpu.read(0xFF41);
        hwregs.setValueAt(String.format("ff41: STAT=$%02x", i), k, 01);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF49);
        hwregs.setValueAt(String.format("ff49: OBP1=$%02x", i), k, 1);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF04);
        hwregs.setValueAt(String.format("ff04:  DIV=$%02x", i), k, 1);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF0f);
        hwregs.setValueAt(String.format("ff0f:   IF=$%02x", i), k, 1);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF42);
        hwregs.setValueAt(String.format("ff42:  SCY=$%02x", i), k, 1);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        c = m.getColumn(1);
        c.setCellRenderer(new MyCellRenderer(colors));
        colors = new Color[5];
        k = 0;
        i = gui.cpu.read(0xFF44);
        hwregs.setValueAt(String.format("ff44:   LY=$%02x", i), k, 2);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF05);
        hwregs.setValueAt(String.format("ff05: TIMA=$%02x", i), k, 2);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF02);
        hwregs.setValueAt(String.format("ff02:   SC=$%02x", i), k, 2);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF4b);
        hwregs.setValueAt(String.format("ff4b:   WX=$%02x", i), k, 2);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = 0x100;
        if (watches[0] >= 0) {
            i = gui.cpu.read(watches[0]);
            hwregs.setValueAt(String.format("%04x:   w0=$%02x", watches[0], i), k, 2);
        } else hwregs.setValueAt("w0 unset", k, 2);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        c = m.getColumn(2);
        c.setCellRenderer(new MyCellRenderer(colors));
        colors = new Color[5];
        k = 0;
        i = gui.cpu.read(0xFF45);
        hwregs.setValueAt(String.format("ff45:  LYC=$%02x", i), k, 3);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF06);
        hwregs.setValueAt(String.format("ff06:  TMA=$%02x", i), k, 3);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF01);
        hwregs.setValueAt(String.format("ff01:   SB=$%02x", i), k, 3);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = gui.cpu.read(0xFF4a);
        hwregs.setValueAt(String.format("ff4a:   WY=$%02x", i), k, 3);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        i = 0x100;
        if (watches[1] >= 0) {
            i = gui.cpu.read(watches[1]);
            hwregs.setValueAt(String.format("%04x:   w1=$%02x", watches[1], i), k, 3);
        } else hwregs.setValueAt("w1 unset", k, 3);
        colors[k++] = (oldHWRegs[j] == i ? Color.WHITE : UpdateColor);
        oldHWRegs[j++] = i;
        c = m.getColumn(3);
        c.setCellRenderer(new MyCellRenderer(colors));
    }

    public void updateMemory() {
        memiseditable = false;
        int m = memaddr;
        for (int i = 0; i < 8; ++i) {
            mem.setValueAt(String.format("$%04x", m), i, 0);
            for (int j = 2; j < 10; ++j) {
                mem.setValueAt(String.format("$%02x", (m <= 0xffff) ? gui.cpu.read(m++) : 0x1000), i, j);
            }
        }
        memiseditable = true;
    }

    private int seekBackOneInstruction(int pc) {
        int j = pc - 3;
        int i = deasm.instructionLength(j);
        if (i == 3) return j;
        ++j;
        i = deasm.instructionLength(j);
        if (i == 2) return j;
        ++j;
        i = deasm.instructionLength(j);
        if (i == 1) return j;
        return j;
    }

    private void updateInstructions() {
        int pc = gui.cpu.getPC();
        for (int i = 0; i < 7; ++i) {
            pc = seekBackOneInstruction(pc);
            instrs.setValueAt(instrs.getValueAt(i + 1, 0), i, 0);
        }
        for (int i = 0; i < 16; ++i) {
            if (i == 7) pc = gui.cpu.getPC();
            instrs.setValueAt(deasm.disassemble(pc), i, 0);
            pc += deasm.instructionLength(pc);
        }
    }

    private void updateRegisters() {
        TableColumnModel m = regs1.getColumnModel();
        TableColumn c;
        Color[] colorsa = new Color[1];
        Color[] colorsb = new Color[1];
        colorsa[0] = UpdateColor;
        colorsb[0] = Color.WHITE;
        MyCellRenderer colored = new MyCellRenderer(colorsa);
        MyCellRenderer normal = new MyCellRenderer(colorsb);
        regs1.setValueAt(String.format("A=$%02x", gui.cpu.A), 0, 0);
        c = m.getColumn(0);
        c.setCellRenderer(oldRegVal[0] == gui.cpu.A ? normal : colored);
        oldRegVal[0] = gui.cpu.A;
        regs1.setValueAt(String.format("B=$%02x", gui.cpu.B), 0, 1);
        c = m.getColumn(1);
        c.setCellRenderer(oldRegVal[1] == gui.cpu.B ? normal : colored);
        oldRegVal[1] = gui.cpu.B;
        regs1.setValueAt(String.format("C=$%02x", gui.cpu.C), 0, 2);
        c = m.getColumn(2);
        c.setCellRenderer(oldRegVal[2] == gui.cpu.C ? normal : colored);
        oldRegVal[2] = gui.cpu.C;
        regs1.setValueAt(String.format("D=$%02x", gui.cpu.D), 0, 3);
        c = m.getColumn(3);
        c.setCellRenderer(oldRegVal[3] == gui.cpu.D ? normal : colored);
        oldRegVal[3] = gui.cpu.D;
        regs1.setValueAt(String.format("E=$%02x", gui.cpu.E), 0, 4);
        c = m.getColumn(4);
        c.setCellRenderer(oldRegVal[4] == gui.cpu.E ? normal : colored);
        oldRegVal[4] = gui.cpu.E;
        regs1.setValueAt(String.format("F=$%02x", gui.cpu.F), 0, 5);
        c = m.getColumn(5);
        c.setCellRenderer(oldRegVal[5] == gui.cpu.F ? normal : colored);
        regs1.setValueAt(String.format("H=$%02x", gui.cpu.H), 0, 6);
        c = m.getColumn(6);
        c.setCellRenderer(oldRegVal[6] == gui.cpu.H ? normal : colored);
        oldRegVal[6] = gui.cpu.H;
        regs1.setValueAt(String.format("L=$%02x", gui.cpu.L), 0, 7);
        c = m.getColumn(7);
        c.setCellRenderer(oldRegVal[7] == gui.cpu.L ? normal : colored);
        oldRegVal[7] = gui.cpu.L;
        m = regs2.getColumnModel();
        regs2.setValueAt(String.format("PC=$%04x", gui.cpu.getPC()), 0, 0);
        c = m.getColumn(0);
        c.setCellRenderer(normal);
        oldRegVal[8] = gui.cpu.getPC();
        regs2.setValueAt(String.format("SP=$%04x", gui.cpu.SP), 0, 1);
        c = m.getColumn(1);
        c.setCellRenderer(oldRegVal[9] == gui.cpu.SP ? normal : colored);
        oldRegVal[9] = gui.cpu.SP;
        String flags = "F=";
        flags += ((gui.cpu.F & CPU.ZF_Mask) == CPU.ZF_Mask) ? "Z " : "z ";
        flags += ((gui.cpu.F & CPU.NF_Mask) == CPU.NF_Mask) ? "N " : "n ";
        flags += ((gui.cpu.F & CPU.HC_Mask) == CPU.HC_Mask) ? "H " : "h ";
        flags += ((gui.cpu.F & CPU.CF_Mask) == CPU.CF_Mask) ? "C " : "c ";
        regs2.setValueAt(flags, 0, 3);
        c = m.getColumn(3);
        c.setCellRenderer(oldRegVal[5] == gui.cpu.F ? normal : colored);
        oldRegVal[5] = gui.cpu.F;
        int intstat = (gui.cpu.IOP[0x0f] & 0x1f) | ((gui.cpu.IE & 0x1f) << 5) | (gui.cpu.IME ? 1 << 10 : 0);
        String ime = ((intstat & (1 << 10)) != 0 ? "I " : "i ");
        ime += (gui.cpu.IE & (1 << 4)) != 0 ? "J" : "j";
        ime += (gui.cpu.IOP[0x0f] & (1 << 4)) != 0 ? "+" : "-";
        ime += " ";
        ime += (gui.cpu.IE & (1 << 3)) != 0 ? "S" : "s";
        ime += (gui.cpu.IOP[0x0f] & (1 << 3)) != 0 ? "+" : "-";
        ime += " ";
        ime += (gui.cpu.IE & (1 << 2)) != 0 ? "T" : "t";
        ime += (gui.cpu.IOP[0x0f] & (1 << 2)) != 0 ? "+" : "-";
        ime += " ";
        ime += (gui.cpu.IE & (1 << 1)) != 0 ? "L" : "l";
        ime += (gui.cpu.IOP[0x0f] & (1 << 1)) != 0 ? "+" : "-";
        ime += " ";
        ime += (gui.cpu.IE & (1 << 0)) != 0 ? "V" : "v";
        ime += (gui.cpu.IOP[0x0f] & (1 << 0)) != 0 ? "+" : "-";
        regs2.setValueAt(ime, 0, 2);
        c = m.getColumn(2);
        c.setCellRenderer(oldRegVal[10] == intstat ? normal : colored);
        oldRegVal[10] = intstat;
    }

    public void prepareParser() {
        parser.removeVariables();
        parser.addVariable("A", oldRegVal[0]);
        parser.addVariable("B", oldRegVal[1]);
        parser.addVariable("C", oldRegVal[2]);
        parser.addVariable("D", oldRegVal[3]);
        parser.addVariable("E", oldRegVal[4]);
        parser.addVariable("F", oldRegVal[5]);
        parser.addVariable("H", oldRegVal[6]);
        parser.addVariable("L", oldRegVal[7]);
        parser.addVariable("PC", oldRegVal[8]);
        parser.addVariable("SP", oldRegVal[9]);
        parser.addVariable("HL", oldRegVal[7] | (oldRegVal[6] << 8));
        parser.addVariable("M", memaddr);
        parser.addVariable("Q", gui.cpu.read(memaddr));
        parser.addVariable("a", oldRegVal[0]);
        parser.addVariable("b", oldRegVal[1]);
        parser.addVariable("c", oldRegVal[2]);
        parser.addVariable("d", oldRegVal[3]);
        parser.addVariable("e", oldRegVal[4]);
        parser.addVariable("f", oldRegVal[5]);
        parser.addVariable("h", oldRegVal[6]);
        parser.addVariable("l", oldRegVal[7]);
        parser.addVariable("pc", oldRegVal[8]);
        parser.addVariable("sp", oldRegVal[9]);
        parser.addVariable("hl", oldRegVal[7] | (oldRegVal[6] << 8));
        parser.addVariable("m", memaddr);
        parser.addVariable("q", gui.cpu.read(memaddr));
    }

    private void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JGameBoy Emulator DEBUGGER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(frame.getContentPane());
        frame.pack();
        frame.setLocation(00, 0);
        frame.setSize(new Dimension(520, 720));
        frame.setVisible(true);
        cmds.requestFocus();
    }

    private Object makeObj(final String item) {
        return new Object() {

            public String toString() {
                return item;
            }
        };
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JRadioButtonMenuItem) {
            if (e.getSource().equals(radioButtonSimpleDisasm)) deasm = new Disassembler(gui.cpu, Disassembler.SIMPLE_DISASSEMBLY); else deasm = new Disassembler(gui.cpu, Disassembler.EXTENDED_DISASSEMBLY);
            update();
            return;
        }
        JComboBox f = (JComboBox) (e.getSource());
        if (f == cmds && e.getActionCommand().equals("comboBoxEdited")) {
            int i = 0;
            String s = cmds.getSelectedItem().toString();
            cmds.insertItemAt(makeObj(s), 0);
            cmds.setSelectedIndex(0);
            cmds.getEditor().selectAll();
            i = s.indexOf("=");
            if (i > -1) {
                String l = s.substring(0, i).trim();
                updateRegisters();
                prepareParser();
                int v = parser.Evaluate(s.substring(i + 1).trim());
                if (!parser.parseError) {
                    if (l.equalsIgnoreCase("a")) {
                        gui.cpu.A = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("b")) {
                        gui.cpu.B = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("c")) {
                        gui.cpu.C = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("d")) {
                        gui.cpu.D = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("e")) {
                        gui.cpu.E = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("f")) {
                        gui.cpu.F = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("h")) {
                        gui.cpu.H = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("l")) {
                        gui.cpu.L = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("hl")) {
                        gui.cpu.H = (v >> 8) & 0xFF;
                        gui.cpu.L = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("pc")) {
                        gui.cpu.setPC(v & 0xFFFF);
                        update();
                    } else if (l.equalsIgnoreCase("sp")) {
                        gui.cpu.SP = v & 0xFFFF;
                        update();
                    } else if (l.equalsIgnoreCase("m")) {
                        memaddr = v & 0xFFFF;
                        update();
                    } else if (l.equalsIgnoreCase("af")) {
                        gui.cpu.A = (v >> 8) & 0xFF;
                        gui.cpu.F = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("bc")) {
                        gui.cpu.B = (v >> 8) & 0xFF;
                        gui.cpu.C = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("de")) {
                        gui.cpu.D = (v >> 8) & 0xFF;
                        gui.cpu.E = v & 0xFF;
                        update();
                    } else if (l.equalsIgnoreCase("bpi")) {
                        runner.setBreakPoint(v);
                        runner.resume();
                    } else if (l.equalsIgnoreCase("bp")) {
                        runner.setBreakPoint(v);
                    } else if (l.equalsIgnoreCase("r")) {
                        runner.suspend();
                        runner.setBreakPoint(v);
                        runner.resume();
                    } else if (l.equalsIgnoreCase("bpm")) {
                        bpm = v;
                    } else if (l.equalsIgnoreCase("w")) {
                        runner.setWatchPoint(v);
                    } else if (l.equalsIgnoreCase("w0")) {
                        watches[0] = v;
                        update();
                    } else if (l.equalsIgnoreCase("w1")) {
                        watches[1] = v;
                        update();
                    } else if (l.equalsIgnoreCase("i")) {
                        runner.setBreakInstr(v);
                    } else if (l.equalsIgnoreCase("p")) {
                        runner.setRunFor(v);
                        runner.resume();
                    } else if (l.equalsIgnoreCase("ic")) {
                        runner.setInstrStop(v);
                    } else if (l.equalsIgnoreCase("cycles")) {
                        runner.setCycleStop(v);
                    } else if (l.equalsIgnoreCase("ime")) {
                        gui.cpu.IME = (v != 0);
                        update();
                    } else if (l.equalsIgnoreCase("halted")) {
                        gui.cpu.halted = (v != 0);
                        update();
                    } else if (l.equalsIgnoreCase("disasm")) {
                        int pc = gui.cpu.getPC();
                        while (v > pc) {
                            System.out.println(deasm.disassemble(pc));
                            pc += deasm.instructionLength(pc);
                        }
                    } else {
                    }
                }
                s = "-";
            }
            if (s.equalsIgnoreCase("s")) {
                gui.cpu.runlooponce();
                update();
            }
            if (s.equalsIgnoreCase("reset")) {
                gui.cpu.reset(false);
                update();
            }
            if (s.equalsIgnoreCase("so")) {
                if (!runner.isRunning()) {
                    runner.setBreakPoint(gui.cpu.getPC() + deasm.instructionLength(gui.cpu.getPC()));
                    runner.resume();
                }
            }
            if (s.equalsIgnoreCase("g")) {
                runner.setBreakPoint(-1);
                runner.resume();
            }
            if (s.equalsIgnoreCase("b")) {
                if (runner.isRunning()) {
                    if (logwriter != null) {
                        try {
                            logwriter.flush();
                        } catch (java.io.IOException e2) {
                            System.out.println("Error flushing logfile:" + e2.getMessage());
                            logwriter = null;
                        }
                    }
                    runner.suspend();
                }
            }
        } else {
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    protected static String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex + 1);
    }

    public void itemStateChanged(ItemEvent e) {
    }
}
