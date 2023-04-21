package procsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Program extends JFrame implements ActionListener {

    private Paint panelCenter;

    private JPanel panelCCenter = new JPanel(new BorderLayout());

    private JPanel panelControl = new JPanel(new BorderLayout());

    private JPanel panelCSignals = new JPanel(new GridBagLayout());

    private JPanel panelCNavigate = new JPanel(new GridBagLayout());

    private JTextField tfT = new JTextField("00", 3);

    private JTextField tfClk = new JTextField("0", 3);

    private JTextField tfInstr = new JTextField("0", 3);

    private JTextField tfPC = new JTextField("0100", 3);

    private JButton bClk = new JButton("clk+");

    private JButton bGoTo = new JButton("go to");

    private JButton bInstr = new JButton("instr+");

    private JButton bReset = new JButton("reset");

    private JTextArea taSig = new JTextArea("", 7, 20);

    private JScrollPane spSig = new JScrollPane(taSig, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    private JButton bRegs = new JButton("Registers");

    private JButton bMem = new JButton("Memory");

    private JButton bSigs = new JButton("Signals");

    private JLabel labStatus = new JLabel("Welcome to ProcSim!");

    private JMenuBar menubar = new JMenuBar();

    private JMenu menuFile = new JMenu("File");

    private JMenu menuView = new JMenu("View");

    private JMenu menuHelp = new JMenu("Help");

    private JMenuItem miOpen = new JMenuItem("Open");

    private JMenuItem miOpenP1 = new JMenuItem("Program 1");

    private JMenuItem miOpenP2 = new JMenuItem("Program 2");

    private JMenuItem miShowProgram = new JMenuItem("Show program");

    private JMenuItem miExit = new JMenuItem("Exit");

    private JMenuItem miRegisters = new JMenuItem("Registers");

    private JMenuItem miMemory = new JMenuItem("Memory");

    private JMenuItem miSignals = new JMenuItem("Signals");

    private JMenuItem miDocumentation = new JMenuItem("Documentation");

    private JMenuItem miAbout = new JMenuItem("About");

    private AboutDialog aboutD;

    private OpenDialog openD;

    private DocumentationDialog documentationD;

    private MemoryDialog memoryD;

    private RegistersDialog registersD;

    private SignalsDialog signalsD;

    private int clk = 0;

    private int instr = 0;

    private int step = 0;

    Program() {
        super("ProcSim");
        Design.createDesign();
        panelCenter = new Paint(this);
        Tick.tick(this, false);
        SignalRegistry.updateLastSignals();
        menus();
        control();
        aboutD = new AboutDialog();
        documentationD = new DocumentationDialog();
        openD = new OpenDialog(Design.memory, this);
        memoryD = new MemoryDialog(Design.memory, this);
        registersD = new RegistersDialog(Design.org1, Design.org2, Design.arch, this);
        signalsD = new SignalsDialog();
        add(panelCenter, "Center");
        add(panelControl, "South");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("procsim/images/logo-small.png")));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void menus() {
        miOpen.setMnemonic('O');
        miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        miOpen.addActionListener(this);
        miOpenP1.setMnemonic('1');
        miOpenP1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
        miOpenP1.addActionListener(this);
        miOpenP2.setMnemonic('2');
        miOpenP2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
        miOpenP2.addActionListener(this);
        miShowProgram.setEnabled(false);
        miShowProgram.setMnemonic('P');
        miShowProgram.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        miShowProgram.addActionListener(this);
        miExit.setMnemonic('x');
        miExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        miExit.addActionListener(this);
        miRegisters.setMnemonic('R');
        miRegisters.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        miRegisters.addActionListener(this);
        miMemory.setMnemonic('M');
        miMemory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
        miMemory.addActionListener(this);
        miSignals.setMnemonic('S');
        miSignals.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        miSignals.addActionListener(this);
        miDocumentation.setMnemonic('D');
        miDocumentation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        miDocumentation.addActionListener(this);
        miAbout.setMnemonic('A');
        miAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK));
        miAbout.addActionListener(this);
        menuFile.add(miOpen);
        menuFile.add(miOpenP1);
        menuFile.add(miOpenP2);
        menuFile.add(miShowProgram);
        menuFile.addSeparator();
        menuFile.add(miExit);
        menuFile.setMnemonic('F');
        menubar.add(menuFile);
        menuView.add(miRegisters);
        menuView.add(miMemory);
        menuView.add(miSignals);
        menuView.setMnemonic('V');
        menubar.add(menuView);
        menuHelp.add(miDocumentation);
        menuHelp.add(miAbout);
        menuHelp.setMnemonic('H');
        menubar.add(menuHelp);
        setJMenuBar(menubar);
    }

    private void control() {
        taSig.setLineWrap(true);
        taSig.setWrapStyleWord(true);
        taSig.setEditable(false);
        tfT.setEditable(false);
        tfInstr.setEditable(false);
        tfPC.setEditable(false);
        bClk.addActionListener(this);
        bGoTo.addActionListener(this);
        bInstr.addActionListener(this);
        bReset.addActionListener(this);
        bRegs.addActionListener(this);
        bMem.addActionListener(this);
        bSigs.addActionListener(this);
        bClk.setMargin(new Insets(3, 3, 3, 3));
        bGoTo.setMargin(new Insets(3, 3, 3, 3));
        bInstr.setMargin(new Insets(3, 3, 3, 3));
        bReset.setMargin(new Insets(3, 3, 3, 3));
        bRegs.setMargin(new Insets(3, 3, 3, 3));
        bMem.setMargin(new Insets(3, 3, 3, 3));
        bSigs.setMargin(new Insets(3, 3, 3, 3));
        JLabel labT = new JLabel("T =");
        JLabel labClk = new JLabel("clk =");
        JLabel labInstr = new JLabel("instr =");
        JLabel labPC = new JLabel("PC =");
        JLabel labSigs = new JLabel("Active signals:");
        panelControl.add("South", labStatus);
        panelControl.add("Center", panelCCenter);
        panelCCenter.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Control"), BorderFactory.createEmptyBorder(-2, -2, -2, -2)), null));
        panelCCenter.add("West", panelCNavigate);
        panelCCenter.add("East", panelCSignals);
        panelCNavigate.add(labT, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(labClk, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(labInstr, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(labPC, new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(tfT, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(tfClk, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(tfInstr, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(tfPC, new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bClk, new GridBagConstraints(2, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bGoTo, new GridBagConstraints(2, 1, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bInstr, new GridBagConstraints(2, 2, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bReset, new GridBagConstraints(2, 3, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bRegs, new GridBagConstraints(4, 0, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bMem, new GridBagConstraints(4, 1, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCNavigate.add(bSigs, new GridBagConstraints(4, 2, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCSignals.add(labSigs, new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        panelCSignals.add(spSig, new GridBagConstraints(0, 1, 6, 5, 0, 0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }

    public void setStatus(String str) {
        labStatus.setText(str);
    }

    public void setActiveSignals(String str) {
        taSig.setText(str);
    }

    public void setShowProgram(boolean bool) {
        miShowProgram.setEnabled(bool);
    }

    private void setCounters() {
        ++clk;
        tfClk.setText("" + clk);
        setStatus("clk = " + clk);
        step = Design.CUCNT.result().get();
        tfT.setText(String.format("%02X", step));
        tfPC.setText(String.format("%04X", Design.PC.result().get()));
        if (step == 0) tfInstr.setText("" + ++instr);
    }

    private void resetPaint() {
        remove(panelCenter);
        panelCenter = new Paint(this);
        add(panelCenter, "Center");
    }

    private void tick() {
        Tick.tick(this, true);
        setCounters();
        SignalRegistry.update();
    }

    private void preTick() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        bClk.setEnabled(false);
        bGoTo.setEnabled(false);
        bInstr.setEnabled(false);
        bReset.setEnabled(false);
    }

    private void postTick() {
        bClk.setEnabled(true);
        bGoTo.setEnabled(true);
        bInstr.setEnabled(true);
        bReset.setEnabled(true);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void openFile(BufferedReader in, String name) {
        String text = "";
        try {
            String str;
            while ((str = in.readLine()) != null) text += str + "\n";
            in.close();
            setStatus("File " + name + " opened.");
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "Program encountered an error while opening file " + name + "!", "File error", JOptionPane.ERROR_MESSAGE);
        }
        openD.setText(text, "Opened program");
        openD.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("clk+") || e.getActionCommand().equals("go to")) {
            try {
                int i = Integer.parseInt(tfClk.getText());
                if (e.getActionCommand().equals("clk+")) {
                    new Thread() {

                        @Override
                        public void run() {
                            preTick();
                            tick();
                            postTick();
                        }
                    }.start();
                } else {
                    final int tclk = clk;
                    final int until = i;
                    if (tclk > until) {
                        JOptionPane.showMessageDialog(null, "Can't go back in time!", "Time error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new Thread() {

                        @Override
                        public void run() {
                            preTick();
                            for (int j = tclk; j < until; j++) tick();
                            postTick();
                        }
                    }.start();
                }
                panelCenter.rePaint();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Integer required!", "Not an integer", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("instr+")) {
            new Thread() {

                @Override
                public void run() {
                    preTick();
                    do tick(); while (step != 0x00 && Design.halt.isNull());
                    panelCenter.rePaint();
                    postTick();
                }
            }.start();
        } else if (e.getActionCommand().equals("reset")) {
            final Program parent = this;
            new Thread() {

                @Override
                public void run() {
                    preTick();
                    Design.resetDesign();
                    SignalRegistry.emptyRegistry();
                    Design.createDesign();
                    resetPaint();
                    Tick.tick(parent, false);
                    panelCenter.rePaint();
                    SignalRegistry.updateLastSignals();
                    openD = new OpenDialog(Design.memory, parent);
                    memoryD = new MemoryDialog(Design.memory, parent);
                    registersD = new RegistersDialog(Design.org1, Design.org2, Design.arch, parent);
                    clk = instr = 0;
                    tfT.setText("00");
                    tfClk.setText("0");
                    tfInstr.setText("0");
                    tfPC.setText(String.format("%04x", Design.PC.result().get()));
                    setStatus("clk = 0");
                    setActiveSignals("");
                    postTick();
                }
            }.start();
        } else if (e.getActionCommand().equals("Open")) {
            JFileChooser fcopen = new JFileChooser(System.getProperty("user.dir"));
            int val = fcopen.showOpenDialog(this);
            if (val == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fcopen.getSelectedFile();
                    openFile(new BufferedReader(new FileReader(file)), file.getPath());
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Could not open the specified file!", "File error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getActionCommand().equals("Program 1")) {
            openFile(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("procsim/programs/program1"))), "procsim/programs/program1");
        } else if (e.getActionCommand().equals("Program 2")) {
            openFile(new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("procsim/programs/program2"))), "procsim/programs/program2");
        } else if (e.getActionCommand().equals("Show program")) {
            openD.setVisible(true);
        } else if (e.getActionCommand().equals("Registers")) {
            registersD.setVisible(true);
            registersD.update();
        } else if (e.getActionCommand().equals("Signals")) {
            signalsD.setVisible(true);
            signalsD.update();
        } else if (e.getActionCommand().equals("Memory")) {
            memoryD.setVisible(true);
            memoryD.update();
        } else if (e.getActionCommand().equals("About")) {
            aboutD.setVisible(true);
        } else if (e.getActionCommand().equals("Documentation")) {
            documentationD.setVisible(true);
        }
    }
}
