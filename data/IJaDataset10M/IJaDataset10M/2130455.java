package gui;

import java.lang.Math;
import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.naming.SizeLimitExceededException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import java.util.Vector;
import com.intellij.uiDesigner.core.*;
import info.clearthought.layout.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import main.exception.*;
import main.exception.configurationexception.*;
import main.memory.MemoryController;
import gui.configuration.*;
import main.configuration.*;
import main.LogicUnit;

public class Gui implements ActionListener {

    public View memoryView_;

    public View swapView_;

    public View diskView_;

    public int totalReferences;

    public MemoryConfigurationView memoryConfigurationView_;

    public DiskConfigurationView diskConfigurationView_;

    public ProcessConfigurationView processConfigurationView_;

    public MemoryConfiguration memoryConfiguration;

    public DiskConfiguration diskConfiguration;

    public ProcessConfiguration processConfiguration;

    public LogicConfiguration logicConfiguration;

    public LogicUnit logicUnit_;

    private boolean modificato = false;

    public JFrame frame1;

    private JPanel panel;

    private JMenuBar menuBar1;

    private JMenu menu1;

    private JMenuItem menuItemNuovaConfigurazione;

    private JMenuItem menuItemOpen;

    private JMenuItem menuItemSave;

    private JMenuItem menuItemExit;

    private JMenu menu2;

    private JMenuItem menuItemEdit;

    private JMenu menu3;

    private JMenuItem menuItemHelp;

    private JMenuItem menuItemAbout;

    private JScrollPane systemInfoScrollPane;

    private JPanel panel1;

    private JTextPane textPane2;

    private JTextPane textPane1;

    private JTextPane textPane5;

    private JTextPane textPane4;

    private JTextPane textPane3;

    private JTextPane textPane6;

    private JTabbedPane tabbedPane2;

    private JPanel panel4;

    private JPanel panel5;

    private JPanel memoryInfoPanel;

    private JScrollPane scrollPane2;

    private JScrollPane processoAttivoPane;

    private JPanel processoAttivoPanel;

    private JProgressBar progressBar1;

    private JPanel panel2;

    private JPanel processInfoPanel;

    private JButton stepBWButton;

    private JButton pauseButton;

    private JButton playButton;

    private JButton stepFWButton;

    private JScrollPane scrollPane3;

    private JLabel label1;

    private JLabel label2;

    private JLabel label3;

    private JLabel label4;

    private JLabel label5;

    private JLabel label6;

    private JLabel elapsedLabel;

    private JLabel diskAccessNumberLabel;

    private JLabel diskAccessNumber;

    private JLabel freeMemoryRatioLabel;

    private JLabel freeMemoryRatio;

    private JLabel pageFaultRatioLabel;

    private JLabel pageFaultRatio;

    private JLabel pageFaultNumberLabel;

    private JLabel pageFaultNumber;

    private JLabel internalFragmentationLabel;

    private JLabel internalFragmentation;

    private JLabel diskAccessOverheadLabel;

    private JLabel diskAccessOverhead;

    private JLabel cpuRatioLabel;

    private JLabel cpuRatio;

    private JLabel contextSwitchOverheadLabel;

    private JLabel contextSwitchOverhead;

    private JLabel processLabel, arrivalLabel, emittedLabel, executionLabel;

    private JLabel arrival, emitted, execution;

    private JTextField statusField;

    private TextualSimulation textualSimulation;

    private MyToolTipManager ttm = MyToolTipManager.sharedInstance();

    public Gui() {
        frame1 = new JFrame();
        panel = new JPanel();
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItemNuovaConfigurazione = new JMenuItem();
        menuItemNuovaConfigurazione.addActionListener(this);
        menuItemOpen = new JMenuItem();
        menuItemOpen.addActionListener(this);
        menuItemSave = new JMenuItem();
        menuItemSave.addActionListener(this);
        menuItemExit = new JMenuItem();
        menuItemExit.addActionListener(this);
        menu2 = new JMenu();
        menuItemEdit = new JMenuItem();
        menuItemEdit.addActionListener(this);
        menu3 = new JMenu();
        menuItemHelp = new JMenuItem();
        menuItemHelp.addActionListener(this);
        menuItemAbout = new JMenuItem();
        menuItemAbout.addActionListener(this);
        systemInfoScrollPane = new JScrollPane();
        panel1 = new JPanel() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        panel1.setToolTipText("<font face='Arial'><b>System Info</b><br>" + "Contiene le principali informazioni<br>" + "riguardanti la configurazione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/info.html#memory'>Consulta la guida</a></font>");
        ttm.registerComponent(panel1);
        textPane2 = new JTextPane();
        textPane1 = new JTextPane();
        textPane5 = new JTextPane();
        textPane4 = new JTextPane();
        textPane3 = new JTextPane();
        textPane6 = new JTextPane();
        tabbedPane2 = new JTabbedPane();
        panel4 = new JPanel();
        panel5 = new JPanel();
        memoryInfoPanel = new JPanel() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        memoryInfoPanel.setToolTipText("<font face='Arial'><b>Informazioni di Esecuzione</b><br>" + "Contiene le principali informazioni<br>" + "riguardanti la simulazione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/info.html#memory'>Consulta la guida</a></font>");
        ttm.registerComponent(memoryInfoPanel);
        scrollPane2 = new JScrollPane();
        processoAttivoPane = new JScrollPane();
        processoAttivoPanel = new JPanel() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        processoAttivoPanel.setToolTipText("<font face='Arial'><b>Informazioni sul processo attivo</b><br>" + "Contiene le principali informazioni<br>" + "riguardanti il processo in esecuzione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/info.html#activeaction'>Consulta la guida</a></font>");
        ttm.registerComponent(processoAttivoPanel);
        progressBar1 = new JProgressBar();
        panel2 = new JPanel();
        processInfoPanel = new JPanel() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        processInfoPanel.setToolTipText("<font face='Arial'><b>Informazioni dei Processi</b><br>" + "Contiene le principali informazioni<br>" + "riguardanti i processi<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/info.html#process'>Consulta la guida</a></font>");
        ttm.registerComponent(processInfoPanel);
        stepBWButton = new JButton() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        stepBWButton.setToolTipText("<font face='Arial'><b>StepBackward</b><br>" + "Permette di tornare indietro di un<br>" + "istante temporale<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/pulsanti.html#stepBW'>Consulta la guida</a></font>");
        ttm.registerComponent(stepBWButton);
        stepBWButton.addActionListener(this);
        pauseButton = new JButton() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        pauseButton.setToolTipText("<font face='Arial'><b>Pause</b><br>" + "Permette di mettere in pausa<br>" + "la simulazione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/pulsanti.html#pause'>Consulta la guida</a></font>");
        ttm.registerComponent(pauseButton);
        pauseButton.addActionListener(this);
        playButton = new JButton() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        playButton.setToolTipText("<font face='Arial'><b>Play</b><br>" + "Permette di eseguire<br>" + "la simulazione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/pulsanti.html#play'>Consulta la guida</a></font>");
        ttm.registerComponent(playButton);
        playButton.addActionListener(this);
        stepFWButton = new JButton() {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        stepFWButton.setToolTipText("<font face='Arial'><b>StepForward</b><br>" + "Permette avanzare di un<br>" + "istante temporale<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/pulsanti.html#stepFW'>Consulta la guida</a></font>");
        ttm.registerComponent(stepFWButton);
        stepFWButton.addActionListener(this);
        scrollPane3 = new JScrollPane();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        elapsedLabel = new JLabel();
        diskAccessNumberLabel = new JLabel("Numero accessi a disco:");
        freeMemoryRatioLabel = new JLabel("% memoria libera:");
        pageFaultRatioLabel = new JLabel("% page fault:");
        pageFaultNumberLabel = new JLabel("Numero page fault:");
        internalFragmentationLabel = new JLabel("% frammentazione interna:");
        diskAccessOverheadLabel = new JLabel("Tempo totale accessi a disco:");
        cpuRatioLabel = new JLabel("% utilizzo CPU");
        contextSwitchOverheadLabel = new JLabel("Tempo totale context switch");
        diskAccessNumber = new JLabel();
        freeMemoryRatio = new JLabel();
        pageFaultRatio = new JLabel();
        pageFaultNumber = new JLabel();
        internalFragmentation = new JLabel();
        diskAccessOverhead = new JLabel();
        cpuRatio = new JLabel();
        contextSwitchOverhead = new JLabel();
        processLabel = new JLabel();
        arrivalLabel = new JLabel("Tempo di arrivo:");
        arrival = new JLabel();
        emittedLabel = new JLabel("Riferimenti totali:");
        emitted = new JLabel();
        executionLabel = new JLabel("Durata del processo:");
        execution = new JLabel();
        statusField = new JTextField();
        textualSimulation = new TextualSimulation();
        initComponents();
        ImageIcon icon = new ImageIcon("SiGeMlogo" + System.getProperties().getProperty("file.separator") + "Probe_logo.gif");
        frame1.setIconImage(icon.getImage());
        frame1.setVisible(true);
        setViews();
        ttm.setEnabled(true);
        ttm.setInitialDelay(1);
        ttm.setDismissDelay(2500);
    }

    /**
	 * Gestore degli eventi (pressione pulsanti, selezione da un menu..)
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(stepBWButton)) {
            System.out.println(logicUnit_.getIterationNumber());
            try {
                logicUnit_.goTo(logicUnit_.getIterationNumber() - 1);
                setBar();
                setTime();
                setMemoryInfo();
            } catch (Exception ex) {
                stepBWButton.setEnabled(false);
                setBar();
                new ErrorWindow(frame1, ex.getClass().toString().substring(16), ex.getMessage());
            }
        } else if (e.getSource().equals(playButton)) {
            logicUnit_.play();
            unsetMemoryInfo();
            stepBWButton.setEnabled(false);
            pauseButton.setEnabled(true);
            playButton.setEnabled(false);
            stepFWButton.setEnabled(false);
        } else if (e.getSource().equals(pauseButton)) {
            logicUnit_.stop();
            setMemoryInfo();
            stepBWButton.setEnabled(true);
            pauseButton.setEnabled(false);
            playButton.setEnabled(true);
            stepFWButton.setEnabled(true);
        } else if (e.getSource().equals(stepFWButton)) {
            logicUnit_.step();
            setBar();
            setTime();
            setMemoryInfo();
            stepBWButton.setEnabled(true);
        } else if (e.getSource().equals(menuItemExit)) {
            System.exit(-1);
        } else {
            if (e.getSource().equals(menuItemAbout)) {
                ImageIcon icon = new ImageIcon("SiGeMlogo" + System.getProperties().getProperty("file.separator") + "Probe_logo.png");
                JOptionPane.showMessageDialog(frame1, "Programma sviluppato " + "dal gruppo P.R.OB.E.\n\n" + "Componenti:\n\n" + " - Beggiato Alessandro\n" + " - Bragagnolo Emanuele\n" + " - Ciccozzi Michele\n" + " - Davanzo Luca\n" + " - Gharaba Amir\n" + " - Lago Serena\n" + " - Marangoni Andrea\n" + " - Piergiovanni Luca\n\n" + "Mail: sigem-probe@lists.sourceforge.net\n" + "Sito: http://sourceforge.net/projects/sigem/\n\n", "Info", JOptionPane.INFORMATION_MESSAGE, icon);
            } else if (e.getSource().equals(menuItemHelp)) {
                String url = "file://" + System.getProperties().getProperty("user.dir") + System.getProperties().getProperty("file.separator") + "manuale" + System.getProperties().getProperty("file.separator") + "index.html";
                System.out.println(url);
                BareBonesBrowserLaunch.openURL(url);
            } else if (e.getSource().equals(menuItemOpen)) {
                if (this.logicConfiguration != null && modificato) {
                    int x = JOptionPane.showConfirmDialog(frame1, "Vuoi salvare la vecchia configurazione\n" + "prima di iniziarne una nuova?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (x == JOptionPane.CANCEL_OPTION) return;
                    if (x == JOptionPane.YES_OPTION) this.saveConfigurationDialog(e);
                }
                JFileChooser fc = new JFileChooser(".");
                fc.removeChoosableFileFilter(fc.getFileFilter());
                fc.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) return true;
                        if (f != null) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            String extension = getExtension(f);
                            if (extension != null && extension.equals("sigem")) {
                                return true;
                            }
                            ;
                        }
                        return false;
                    }

                    public String getExtension(File f) {
                        if (f != null) {
                            String filename = f.getName();
                            int i = filename.lastIndexOf('.');
                            if (i > 0 && i < filename.length() - 1) {
                                return filename.substring(i + 1).toLowerCase();
                            }
                            ;
                        }
                        return null;
                    }

                    @Override
                    public String getDescription() {
                        return "files .sigem";
                    }
                });
                Integer returnVal = fc.showOpenDialog(frame1);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String fileName = fc.getSelectedFile().getAbsolutePath();
                    loadConfiguration(fileName);
                    modificato = false;
                }
            } else if (e.getSource().equals(menuItemSave)) {
                if (this.logicUnit_ != null) this.logicUnit_.stop();
                this.saveConfigurationDialog(e);
            } else if (e.getSource().equals(menuItemNuovaConfigurazione)) {
                LogicConfiguration temp = this.logicConfiguration;
                System.out.println("modificato " + modificato);
                if (this.logicConfiguration != null && modificato) {
                    int x = JOptionPane.showConfirmDialog(frame1, "Vuoi salvare la vecchia configurazione\n" + "prima di iniziarne una nuova?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (x == JOptionPane.CANCEL_OPTION) return;
                    if (x == JOptionPane.YES_OPTION) this.saveConfigurationDialog(e);
                }
                if (this.logicUnit_ != null) this.logicUnit_.stop();
                memoryUnitConfiguration(false);
                modificato = true;
            } else if (e.getSource().equals(menuItemEdit)) {
                LogicConfiguration temp = this.logicConfiguration;
                System.out.println("modificato " + modificato);
                if (this.logicConfiguration != null && modificato) {
                    int x = JOptionPane.showConfirmDialog(frame1, "Vuoi salvare la vecchia configurazione\n" + "prima di iniziarne una nuova?", "Info", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (x == JOptionPane.CANCEL_OPTION) return;
                    if (x == JOptionPane.YES_OPTION) this.saveConfigurationDialog(e);
                }
                if (this.logicUnit_ != null) this.logicUnit_.stop();
                memoryUnitConfiguration(true);
                modificato = true;
            } else {
                final JDialog temp = new JDialog(frame1, "Alert!", true);
                Box b = Box.createVerticalBox();
                b.add(Box.createGlue());
                b.add(new JLabel("Funzione non ancora disponibile", JLabel.CENTER));
                b.add(Box.createGlue());
                temp.getContentPane().add(b, "Center");
                JPanel p2 = new JPanel();
                JButton ok = new JButton("Ok");
                p2.add(ok);
                temp.getContentPane().add(p2, "South");
                ok.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        temp.dispose();
                    }
                });
                temp.setSize(275, 130);
                temp.setLocationRelativeTo(null);
                temp.setResizable(false);
                temp.setVisible(true);
            }
        }
    }

    MemoryUnitConfiguration memoryUnitConfiguration;

    ProcessUnitConfiguration processUnitConfiguration;

    ProcessesConfiguration processesConfiguration;

    DiskUnitConfiguration diskUnitConfiguration;

    MemoryConfiguration memoryTemp;

    ProcessConfiguration processTemp;

    DiskConfiguration diskTemp;

    /**
	 * Configurazione della memoria primaria
	 */
    void memoryUnitConfiguration(boolean edit) {
        try {
            this.logicUnit_.stop();
        } catch (Exception e) {
        }
        try {
            memoryUnitConfiguration = new MemoryUnitConfiguration(this.frame1, this, edit);
            memoryUnitConfiguration.setVisible(true);
            memoryUnitConfiguration.setLocationRelativeTo(null);
        } catch (Exception ex) {
            new ErrorWindow(frame1, ex.getClass().toString().substring(16), ex.getMessage());
        }
    }

    /**
	 * Configurazione dello scheduler
	 */
    void processUnitConfiguration(boolean edit) {
        if (!edit) processUnitConfiguration = new ProcessUnitConfiguration(this.frame1, this);
        processUnitConfiguration.setVisible(true);
        processUnitConfiguration.setLocationRelativeTo(null);
    }

    /**
	 * Inizializzazione dei processi
	 */
    void processesConfiguration() {
        processesConfiguration = new ProcessesConfiguration(this.frame1, this, memoryConfiguration.getMemoryType());
        processesConfiguration.setLocationRelativeTo(null);
        processesConfiguration.setVisible(true);
    }

    /**
	 * Configurazione della memoria secondaria
	 */
    void diskUnitConfiguration() {
        diskUnitConfiguration = new DiskUnitConfiguration(this.frame1, this);
        diskUnitConfiguration.setLocationRelativeTo(null);
        diskUnitConfiguration.setVisible(true);
    }

    /**
	 * Conferma delle impostazioni definite; aggiornamento dei campi con il riepilogo
	 */
    void confirmConfiguration() {
        this.memoryView_.setVisible(true);
        this.swapView_.setVisible(true);
        this.diskView_.setVisible(true);
        playButton.setEnabled(true);
        stepFWButton.setEnabled(true);
        textPane2.setText("\tDimensione memoria: " + Integer.toString(memoryConfiguration.getMemoryCapacity()) + " B");
        textPane3.setText("\tVirtualizzazione: " + memoryConfiguration.getMemoryType());
        String temp;
        if (memoryConfiguration.getMemoryType().equals(MemoryController.MemoryType.PAGED)) temp = new String("Algoritmo di rimpiazzo: "); else temp = new String("Algoritmo di allocazione: ");
        textPane1.setText(temp + memoryConfiguration.getSelectedPolicyName());
        textPane5.setText("\tDimensione area swap: " + Integer.toString(diskConfiguration.getSwapCapacity()) + " B");
        textPane4.setText("Numero processi: " + Integer.toString(processConfiguration.getNumberOfProcesses()));
        if (memoryConfiguration.getMemoryType().equals(MemoryController.MemoryType.PAGED)) textPane6.setText("Dimensione pagine: " + Integer.toString(memoryConfiguration.getPageFrameSize())); else textPane6.setText("");
    }

    /**
	 * Annullamento dell'operazione di configurazione;
	 * elimina le relative finestre
	 */
    void cancelOperation() {
        try {
            memoryUnitConfiguration.dispose();
            processUnitConfiguration.dispose();
            processesConfiguration.dispose();
            diskUnitConfiguration.dispose();
        } catch (NullPointerException e) {
        }
    }

    private void initComponents() {
        {
            frame1.setResizable(false);
            frame1.setTitle("SiGeM - ®Probe Inc.");
            frame1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame1.setBackground(new Color(174, 178, 180));
            Container frame1ContentPane = frame1.getContentPane();
            frame1ContentPane.setLayout(null);
            {
                panel.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
                panel.setLayout(new TableLayout(new double[][] { { 581, 6, 312 }, { 26, 8, 156, 8, 192, TableLayout.PREFERRED, 47, TableLayout.FILL, 5, TableLayout.PREFERRED } }));
                {
                    {
                        menu1.setText("File");
                        menuItemNuovaConfigurazione.setText("Nuova Configurazione");
                        menu1.add(menuItemNuovaConfigurazione);
                        menuItemOpen.setText("Carica Configurazione");
                        menu1.add(menuItemOpen);
                        menuItemSave.setText("Salva Configurazione");
                        menu1.add(menuItemSave);
                        menuItemExit.setText("Esci");
                        menu1.add(menuItemExit);
                    }
                    menuBar1.add(menu1);
                    {
                        menu2.setText("Edit");
                        menuItemEdit.setText("Modifica Configurazione");
                        menu2.add(menuItemEdit);
                    }
                    menuBar1.add(menu2);
                    {
                        menu3.setText("Help");
                        menuItemHelp.setText("Help");
                        menu3.add(menuItemHelp);
                        menuItemAbout.setText("About");
                        menu3.add(menuItemAbout);
                    }
                    menuBar1.add(menu3);
                }
                panel.add(menuBar1, new TableLayoutConstraints(0, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.CENTER));
                {
                    systemInfoScrollPane.setViewportBorder(new TitledBorder("Informazioni sul sistema"));
                    systemInfoScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    systemInfoScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                    systemInfoScrollPane.setEnabled(false);
                    {
                        panel1.setLayout(new GridLayout(3, 0));
                        textPane2.setEditable(false);
                        textPane2.setBackground(UIManager.getColor("InternalFrame.background"));
                        panel1.add(textPane2);
                        textPane1.setEditable(false);
                        textPane1.setBackground(UIManager.getColor("InternalFrame.background"));
                        panel1.add(textPane1);
                        textPane5.setEditable(false);
                        textPane5.setBackground(UIManager.getColor("InternalFrame.background"));
                        panel1.add(textPane5);
                        textPane4.setEditable(false);
                        textPane4.setBackground(UIManager.getColor("InternalFrame.background"));
                        panel1.add(textPane4);
                        textPane3.setEditable(false);
                        textPane3.setBackground(UIManager.getColor("InternalFrame.background"));
                        panel1.add(textPane3);
                        textPane6.setEditable(false);
                        textPane6.setBackground(UIManager.getColor("InternalFrame.background"));
                        panel1.add(textPane6);
                    }
                    systemInfoScrollPane.setViewportView(panel1);
                }
                panel.add(systemInfoScrollPane, new TableLayoutConstraints(0, 7, 0, 8, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                {
                    panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), 0, 0));
                    tabbedPane2.addTab("Simulazione", panel4);
                    {
                        panel5.setLayout(new GridLayout());
                        JScrollPane areaScrollPane;
                        areaScrollPane = new JScrollPane(textualSimulation);
                        textualSimulation.append("Inizia Simulazione\n");
                        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                        areaScrollPane.setPreferredSize(new Dimension(250, 250));
                        areaScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Simulazione Testuale"), BorderFactory.createEmptyBorder(5, 5, 5, 5)), areaScrollPane.getBorder()));
                        panel5.add(areaScrollPane);
                    }
                    tabbedPane2.addTab("Testuale", panel5);
                }
                panel.add(tabbedPane2, new TableLayoutConstraints(0, 2, 0, 4, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                {
                    memoryInfoPanel.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
                    scrollPane2.setViewportBorder(new TitledBorder("Statistiche"));
                    scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane2.setEnabled(false);
                    memoryInfoPanel.add(diskAccessNumberLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(diskAccessNumber, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(freeMemoryRatioLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(freeMemoryRatio, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(pageFaultRatioLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(pageFaultRatio, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(pageFaultNumberLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(pageFaultNumber, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(internalFragmentationLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(internalFragmentation, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(diskAccessOverheadLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(diskAccessOverhead, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(cpuRatioLabel, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(cpuRatio, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(contextSwitchOverheadLabel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    memoryInfoPanel.add(contextSwitchOverhead, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    scrollPane2.setViewportView(memoryInfoPanel);
                }
                panel.add(scrollPane2, new TableLayoutConstraints(2, 3, 2, 4, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                {
                    processoAttivoPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    processoAttivoPane.setViewportBorder(new TitledBorder("Informazioni sul processo attivo"));
                    processoAttivoPane.setEnabled(false);
                    {
                        processoAttivoPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
                        processoAttivoPanel.add(processLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processoAttivoPanel.add(arrivalLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processoAttivoPanel.add(arrival, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processoAttivoPanel.add(emittedLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processoAttivoPanel.add(emitted, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processoAttivoPanel.add(executionLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processoAttivoPanel.add(execution, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    }
                    processoAttivoPane.setViewportView(processoAttivoPanel);
                }
                panel.add(processoAttivoPane, new TableLayoutConstraints(2, 1, 2, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                progressBar1.setValue(0);
                progressBar1.setStringPainted(true);
                panel.add(progressBar1, new TableLayoutConstraints(0, 5, 0, 5, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                {
                    panel2.setBackground(UIManager.getColor("InternalFrame.background"));
                    panel2.setLayout(new GridLayout(1, 6));
                    stepBWButton.setText("StepBW");
                    stepBWButton.setVerticalAlignment(SwingConstants.BOTTOM);
                    stepBWButton.setEnabled(false);
                    panel2.add(stepBWButton);
                    pauseButton.setText("Pause");
                    pauseButton.setVerticalAlignment(SwingConstants.BOTTOM);
                    pauseButton.setEnabled(false);
                    panel2.add(pauseButton);
                    playButton.setText("Play");
                    playButton.setVerticalAlignment(SwingConstants.BOTTOM);
                    playButton.setEnabled(false);
                    panel2.add(playButton);
                    stepFWButton.setText("StepFW");
                    stepFWButton.setVerticalAlignment(SwingConstants.BOTTOM);
                    stepFWButton.setEnabled(false);
                    panel2.add(stepFWButton);
                }
                panel.add(panel2, new TableLayoutConstraints(0, 6, 0, 6, TableLayoutConstraints.CENTER, TableLayoutConstraints.CENTER));
                {
                    scrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane3.setViewportBorder(new TitledBorder("Aree riferite"));
                    scrollPane3.setEnabled(false);
                    {
                        processInfoPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
                        processInfoPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processInfoPanel.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processInfoPanel.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processInfoPanel.add(label4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processInfoPanel.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processInfoPanel.add(label6, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                        processInfoPanel.add(elapsedLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null));
                    }
                    scrollPane3.setViewportView(processInfoPanel);
                }
                panel.add(scrollPane3, new TableLayoutConstraints(2, 5, 2, 8, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
                statusField.setEditable(false);
                statusField.setBackground(UIManager.getColor("InternalFrame.borderColor"));
                statusField.setText("Status...");
                panel.add(statusField, new TableLayoutConstraints(0, 9, 2, 9, TableLayoutConstraints.FULL, TableLayoutConstraints.CENTER));
            }
            frame1ContentPane.add(panel);
            panel.setBounds(0, 0, 913, 590);
            {
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < frame1ContentPane.getComponentCount(); i++) {
                    Rectangle bounds = frame1ContentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = frame1ContentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                frame1ContentPane.setMinimumSize(preferredSize);
                frame1ContentPane.setPreferredSize(preferredSize);
            }
            frame1.setSize(913, 615);
            frame1.setLocationRelativeTo(frame1.getOwner());
        }
    }

    /**
	 * Aggiorna lo stato della barra di avanzamento
	 */
    public void setBar() {
        progressBar1.setValue((int) (((double) (logicUnit_.getIterationNumber()) / (double) (totalReferences)) * 100));
        progressBar1.repaint();
    }

    /**
	 * Aggiorna il campo che visualizza il tempo assoluto della simulazione
	 */
    public void setTime() {
        elapsedLabel.setText("Tempo trascorso: " + logicUnit_.getSystemTime());
    }

    /**
	 * Istanzia i campi di tipo View;
	 * deve essere invocato a configurazione definita
	 * per evitare gravi inconsistenze
	 */
    public void setViews() {
        memoryView_ = new MemoryView("RAM View", panel4, this, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null)) {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        memoryView_.setToolTipText("<font face='Arial'><b>Memoria Principale</b><br>" + "Visualizza la ram in un <br>" + "istante della simulazione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/view.html'>Consulta la guida</a></font>");
        ttm.registerComponent(memoryView_);
        swapView_ = new DiskView("Swap View", panel4, this, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null)) {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        swapView_.setToolTipText("<font face='Arial'><b>Area di Swap</b><br>" + "Visualizza lo swap in un <br>" + "istante della simulazione<br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/view.html'>Consulta la guida</a></font>");
        ttm.registerComponent(swapView_);
        diskView_ = new DiskView("Disk View", panel4, this, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null)) {

            public JToolTip createToolTip() {
                JToolTip tip = new HyperLinkToolTip();
                tip.setComponent(this);
                return tip;
            }
        };
        diskView_.setToolTipText("<font face='Arial'><b>Program Area</b><br>" + "Visualizza la Program Area <br><br>" + "Press Shift to Focus<br>" + "<a href=\'/simulation/view.html'>Consulta la guida</a></font>");
        ttm.registerComponent(diskView_);
    }

    public void saveConfiguration(String filename) {
        try {
            this.logicConfiguration.saveConfiguration(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(frame1, "Non Puoi Salvare una Simulazionae\n" + "non ancora configurata", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadConfiguration(String filename) {
        this.logicConfiguration = new LogicConfiguration(this.memoryView_, this.swapView_);
        int cont = 0;
        try {
            this.logicConfiguration.loadConfiguration(filename);
        } catch (NoClassDefFoundError e) {
        } catch (TooManyProcessesException e) {
        } catch (CannotUseThisMethodException e) {
        } catch (SizeLimitExceededException e) {
        } catch (FileNotFoundException e) {
        } catch (IllegalArgumentException e) {
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (Exception ex) {
            new ErrorWindow(frame1, ex.getClass().toString().substring(16), "File .sigem caricato mal formato");
        } finally {
            try {
                memoryConfiguration = this.logicConfiguration.getMemoryConfiguration_();
                cont = 1;
                processConfiguration = this.logicConfiguration.getProcessConfiguration_();
                cont = 2;
                diskConfiguration = this.logicConfiguration.getDiskConfiguration_();
                cont = 3;
                startCompleteConfiguration();
                this.totalReferences = this.processConfiguration.getNumberOfReference();
                setBar();
                this.totalReferences = processConfiguration.getNumberOfReference();
                new ErrorWindow(frame1, "OK", "File caricato correttamente!");
                this.statusField.setForeground(Color.blue);
                this.statusField.setText("Caricato " + filename + " correttamente");
            } catch (CannotUseThisMethodException e) {
                switch(cont) {
                    case 0:
                        {
                            new ErrorWindow(frame1, e.getClass().toString().substring(16), "File non corretto. Il file non " + "e' utilizzabile in nessun modo");
                            break;
                        }
                    case 1:
                        {
                            new ErrorWindow(frame1, e.getClass().toString().substring(16), "File non corretto. Caricata solo " + "la configurazione della memoria");
                            this.processConfiguration = null;
                            this.diskConfiguration = null;
                            break;
                        }
                    case 2:
                        {
                            new ErrorWindow(frame1, e.getClass().toString().substring(16), "File non corretto. Caricata solo " + "la configurazione della memoria e dei processi");
                            this.diskConfiguration = null;
                            break;
                        }
                }
            }
        }
    }

    /**
	 * Aggiorna le stringhe informative dopo l'emissione di un riferimento logico
	 * @param i - l'area fisica riferita
	 * @param p - l'area logica richiesta
	 * @param id - l'ID del processo che ha emesso la richiesta
	 */
    public void setLogicalReferredPage(Integer i, Integer p, Integer id) {
        Color color = Colours.colorFactory(this.processConfiguration.getNumberOfProcesses(), id + 1);
        label1.setForeground(color);
        label2.setForeground(color);
        label1.setText("Area fisica rif: " + i.toString());
        label2.setText("Area logica rif: " + p.toString());
    }

    /**
	 * Aggiorna le stringhe informative dopo la rimozione di un'area fisica
	 * @param i - l'area fisica rimossa
	 * @param p - l'area logica corrispondente
	 * @param id - l'ID del processo proprietario dell'area
	 */
    public void setLogicalRemovedPage(Integer i, Integer p, Integer id) {
        Color color = Colours.colorFactory(this.processConfiguration.getNumberOfProcesses(), id + 1);
        label3.setForeground(color);
        label4.setForeground(color);
        label3.setText("Area fisica rim: " + i.toString());
        label4.setText("Area logica rim: " + p.toString());
    }

    /**
	 * Aggiorna le stringhe informative dopo l'inserimento in swap di un'area fisica
	 * @param i - l'area logica inserita
	 * @param id - l'ID del processo proprietario dell'area
	 */
    public void setPhysicalReferredPage(Integer i, Integer id) {
        Color color = Colours.colorFactory(this.processConfiguration.getNumberOfProcesses(), id + 1);
        label5.setForeground(color);
        label6.setForeground(color);
        label5.setText("Area in Swap:");
        label6.setText(i.toString());
    }

    public void startPagedMemoryConfiguration() {
        memoryConfigurationView_ = new MemoryConfigurationView(this);
        memoryConfigurationView_.defaultPagedMemoryConfigurationSettings();
        System.out.println("Memory Unit Paged Configurata");
    }

    public void startPagedMemoryConfiguration2() {
        memoryConfigurationView_ = new MemoryConfigurationView(this);
        memoryConfigurationView_.defaultPagedMemoryConfigurationSettings2();
        System.out.println("Memory Unit Paged 2 Configurata");
    }

    /**
	 * Imposta tutte le stringhe informative relative
	 * alle statistiche della simulazione
	 */
    public void setMemoryInfo() {
        this.diskAccessNumber.setText(Integer.toString(logicUnit_.getDiskAccessNumber()));
        this.freeMemoryRatio.setText(Integer.toString(logicUnit_.getFreeMemoryRatio()));
        this.pageFaultRatio.setText(Integer.toString(logicUnit_.getPageFaultRatio()));
        this.pageFaultNumber.setText(Integer.toString(logicUnit_.getPageFaultNumber()));
        this.internalFragmentation.setText(Integer.toString(logicUnit_.getInternalFragmentation()));
        this.diskAccessOverhead.setText(Integer.toString(logicUnit_.getDiskAccesOverheadTime()));
        this.cpuRatio.setText(Integer.toString(logicUnit_.getCpuRatio()));
        this.contextSwitchOverhead.setText(Integer.toString(logicUnit_.getContextSwitchOverhead()));
    }

    public void setProcessInfo() {
        int id = this.logicUnit_.getProcessId();
        Color color = Colours.colorFactory(this.processConfiguration.getNumberOfProcesses(), id + 1);
        processLabel.setText("Processo " + id);
        processLabel.setForeground(color);
        arrival.setText(Integer.toString(this.processConfiguration.getProcessInformation(id).getArrivalTime()));
        emitted.setText(Integer.toString(this.processConfiguration.getProcessInformation(id).getEmittedLogicReference_().size()));
        execution.setText(Integer.toString(this.processConfiguration.getProcessInformation(id).getExecutionTime_()));
    }

    public void unsetMemoryInfo() {
        this.diskAccessNumber.setText("");
        this.freeMemoryRatio.setText("");
        this.pageFaultRatio.setText("");
        this.pageFaultNumber.setText("");
        this.internalFragmentation.setText("");
        this.diskAccessOverhead.setText("");
        this.cpuRatio.setText("");
        this.contextSwitchOverhead.setText("");
    }

    public void startPagedProcessConfiguration() {
        processConfigurationView_ = new ProcessConfigurationView(this);
        processConfigurationView_.defaultPagedProcessConfigurationSettings();
        System.out.println("Process Unit Paged Configurata");
    }

    public void startPagedProcessConfiguration2() {
        processConfigurationView_ = new ProcessConfigurationView(this);
        processConfigurationView_.defaultPagedProcessConfigurationSettings2();
        System.out.println("Process Unit Paged 2 Configurata");
    }

    public void startPagedProcessConfiguration3() {
        processConfigurationView_ = new ProcessConfigurationView(this);
        processConfigurationView_.defaultPagedProcessConfigurationSettings3();
        System.out.println("Process Unit Paged 3 Configurata");
    }

    public void startSegmentedMemoryConfiguration() {
        memoryConfigurationView_ = new MemoryConfigurationView(this);
        memoryConfigurationView_.defaultSegmentedMemoryConfigurationSettings();
        System.out.println("Memory Unit Segmented Configurata");
    }

    public void startSegmentedProcessConfiguration() {
        processConfigurationView_ = new ProcessConfigurationView(this);
        processConfigurationView_.defaultSegmentedProcessConfigurationSettings();
        System.out.println("Process Unit Segmented Configurata");
    }

    public void startDiskConfiguration() {
        diskConfigurationView_ = new DiskConfigurationView(this);
        diskConfigurationView_.defaultDiskConfigurationSettings();
        System.out.println("Disk Unit Configurata");
    }

    /**
	 * Conferma i parametri inseriti e prepara il sistema ad eseguire la simulazione
	 */
    public void startCompleteConfiguration() {
        this.logicConfiguration = new LogicConfiguration(memoryConfiguration, processConfiguration, diskConfiguration);
        try {
            this.logicUnit_ = this.logicConfiguration.getLogicUnit();
        } catch (Exception t) {
            t.printStackTrace();
            System.out.println("Configurazioini mancanti alla logicUnit!");
        }
        System.out.println("LogicUnit Configurata e operativa");
        logicUnit_.addObserver(textualSimulation);
        memoryView_.initializeGraph(memoryConfiguration.getMemoryCapacity());
        diskView_.initializeGraph(diskConfiguration.getDiskCapacity());
        swapView_.initializeGraph(diskConfiguration.getSwapCapacity());
        confirmConfiguration();
        try {
            ((DiskView) diskView_).redraw(diskConfiguration.getProgramAreaScan());
        } catch (Exception e) {
            e.printStackTrace();
        }
        label5.setText("");
        label6.setText("");
    }

    private void saveConfigurationDialog(ActionEvent e) {
        if (this.logicConfiguration == null) {
            JOptionPane.showMessageDialog(frame1, "Non Puoi Salvare una Simulazione\n" + "non ancora configurata", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                if (f != null) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    String extension = getExtension(f);
                    if (extension != null && extension.equals("sigem")) {
                        return true;
                    }
                    ;
                }
                return false;
            }

            public String getExtension(File f) {
                if (f != null) {
                    String filename = f.getName();
                    int i = filename.lastIndexOf('.');
                    if (i > 0 && i < filename.length() - 1) {
                        return filename.substring(i + 1).toLowerCase();
                    }
                    ;
                }
                return null;
            }

            @Override
            public String getDescription() {
                return "files .sigem";
            }
        });
        int returnVal = fc.showSaveDialog(frame1);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String fileName = fc.getSelectedFile().getAbsolutePath();
            File file = new File(fileName);
            String extension = null;
            int i = fileName.lastIndexOf('.');
            int j = fileName.lastIndexOf('/');
            if (i > 0 && i < fileName.length() - 1 && i > j) {
                extension = fileName.substring(i + 1).toLowerCase();
            }
            if (extension == null) fileName = fileName + ".sigem";
            file = new File(fileName);
            if (file.exists()) {
                int ans = JOptionPane.showConfirmDialog(frame1, "Vuoi Sovrascrivere\n" + " il file selezionato?", "Attenzione!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ans == JOptionPane.NO_OPTION) return;
            }
            saveConfiguration(fileName);
        }
    }
}
