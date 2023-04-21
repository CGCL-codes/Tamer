package esdomaci.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import esdomaci.ai.AI;
import esdomaci.gamemap.MatricaIgre;
import esdomaci.mediator.IAIVidljivoSimulatoru;
import esdomaci.mediator.IMapaVidljivoSimulatoru;
import esdomaci.mediator.IPlatnoVidljivoSimulatoru;
import esdomaci.mediator.ISimulatorVidljivoAI;

/**
 * @author Milan Aleksic, milanaleksic@gmail.com
 */
public class Simulator extends JFrame implements ISimulatorVidljivoAI {

    private static Simulator mainForm;

    DijalogIzbora dijalog = null;

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JScrollPane log = null;

    private JSplitPane glavniPanel = null;

    private JMenuBar jJMenuBar = null;

    private JMenu fileMenu = null;

    private JMenu helpMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem newMenuItem = null;

    private JMenuItem stepMenuItem = null;

    private JCheckBoxMenuItem prikaziAIMapuMenuItem = null;

    private JTextArea logField = null;

    private ImageIcon kanibalSlika = null;

    private ImageIcon rupaSlika = null;

    private ImageIcon igracSlika = null;

    private ImageIcon zlatoSlika = null;

    private IAIVidljivoSimulatoru ai = null;

    private IPlatnoVidljivoSimulatoru platno = null;

    private IMapaVidljivoSimulatoru matricaIgre = null;

    private Point prethodnaPocetnaPozicijaIgraca = null;

    private boolean pripremljenDijalog = false;

    public Simulator() {
        super();
        initialize();
    }

    private void initialize() {
        this.setJMenuBar(getJJMenuBar());
        this.setContentPane(getJContentPane());
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        this.setSize(670, 500);
        this.setLocation(50, 50);
        this.setTitle("Симулација \"Пећина\" - Милан Алексић 63/02");
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                if (!pripremljenDijalog) {
                    pripremljenDijalog = true;
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            output("Симулација није покренута");
                            output("-----");
                            output("Учитавам слике...");
                            ucitajSlike();
                            output("Учитавам старе пресете...");
                            pripremiDijalogZaNovuIgru();
                            fileMenu.setEnabled(true);
                            helpMenu.setEnabled(true);
                            output("Готово!");
                            output("-----");
                            output("Поставите одаје (F3)");
                        }
                    });
                }
                super.windowActivated(e);
            }
        });
    }

    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new javax.swing.JMenuBar();
            jJMenuBar.add(getFileMenu());
            jJMenuBar.add(getHelpMenu());
        }
        return jJMenuBar;
    }

    private JMenu getFileMenu() {
        if (fileMenu == null) {
            fileMenu = new javax.swing.JMenu();
            fileMenu.setText("Игра");
            fileMenu.add(getNewGameMenuItem());
            fileMenu.add(getStepMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getShowAIMapMenuItem());
            fileMenu.addSeparator();
            fileMenu.add(getExitMenuItem());
            fileMenu.setEnabled(false);
        }
        return fileMenu;
    }

    private JMenu getHelpMenu() {
        if (helpMenu == null) {
            helpMenu = new javax.swing.JMenu();
            helpMenu.setText("Помоћ");
            helpMenu.add(getAboutMenuItem());
            helpMenu.setEnabled(false);
        }
        return helpMenu;
    }

    private JMenuItem getNewGameMenuItem() {
        if (newMenuItem == null) {
            newMenuItem = new JMenuItem("Нова игра...");
            newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
            newMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (ai.getSpremnoZaRad()) dijalog.postavi(prethodnaPocetnaPozicijaIgraca, matricaIgre.getZlato(), matricaIgre.getKanibal(), matricaIgre.getRupa1(), matricaIgre.getRupa2());
                    dijalog.setVisible(true);
                }
            });
        }
        return newMenuItem;
    }

    private JMenuItem getStepMenuItem() {
        if (stepMenuItem == null) {
            stepMenuItem = new JMenuItem("Обради корак !");
            stepMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
            stepMenuItem.setEnabled(false);
            stepMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (!ai.getSpremnoZaRad()) return;
                    newMenuItem.setEnabled(false);
                    stepMenuItem.setEnabled(false);
                    ai.korakUnapred();
                    newMenuItem.setEnabled(true);
                    stepMenuItem.setEnabled(true);
                }
            });
        }
        return stepMenuItem;
    }

    private JCheckBoxMenuItem getShowAIMapMenuItem() {
        if (prikaziAIMapuMenuItem == null) {
            prikaziAIMapuMenuItem = new JCheckBoxMenuItem("Исписуј АИ мапу", false);
        }
        return prikaziAIMapuMenuItem;
    }

    private javax.swing.JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new javax.swing.JMenuItem();
            exitMenuItem.setText("Излаз");
            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return exitMenuItem;
    }

    private javax.swing.JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new javax.swing.JMenuItem();
            aboutMenuItem.setText("О програму...");
            aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
            aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    getOProgramu().setVisible(true);
                }

                OProgramu oProgramu = null;

                private Window getOProgramu() {
                    if (oProgramu == null) oProgramu = new OProgramu();
                    return oProgramu;
                }
            });
        }
        return aboutMenuItem;
    }

    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getGlavniPanel(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JSplitPane getGlavniPanel() {
        if (glavniPanel == null) {
            glavniPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getLog(), getPlatno());
            glavniPanel.setContinuousLayout(true);
            glavniPanel.setOneTouchExpandable(true);
            glavniPanel.setDividerLocation(200);
        }
        return glavniPanel;
    }

    private JPanel getPlatno() {
        if (platno == null) {
            MatricaIgre tmpMatrica = new MatricaIgre(this);
            AI tmpAI = new AI(this, tmpMatrica);
            Platno tmpPlatno = new Platno(tmpAI, tmpMatrica);
            matricaIgre = tmpMatrica;
            ai = tmpAI;
            platno = tmpPlatno;
        }
        return (Platno) platno;
    }

    private JScrollPane getLog() {
        if (log == null) {
            log = new JScrollPane();
            log.setViewportView(getLogField());
        }
        return log;
    }

    private JTextArea getLogField() {
        if (logField == null) {
            logField = new JTextArea();
            logField.setFont(new Font("Lucida Console", Font.PLAIN, 12));
            logField.setLineWrap(true);
            logField.setEditable(false);
            logField.append("Програм је покренут");
        }
        return logField;
    }

    private void ucitajSlike() {
        kanibalSlika = new ImageIcon("res/ogre.png");
        rupaSlika = new ImageIcon("res/hole.png");
        igracSlika = new ImageIcon("res/explorer.png");
        zlatoSlika = new ImageIcon("res/gold.png");
        platno.setSlike(kanibalSlika, rupaSlika, igracSlika, zlatoSlika);
    }

    private void pripremiDijalogZaNovuIgru() {
        dijalog = new DijalogIzbora(new Runnable() {

            public void run() {
                logField.setText("Започињем игру...\n");
                matricaIgre.resetuj();
                matricaIgre.setZlato(new Point(dijalog.getZlatoX(), dijalog.getZlatoY()));
                matricaIgre.setRupa1(new Point(dijalog.getRupa1X(), dijalog.getRupa1Y()));
                matricaIgre.setRupa2(new Point(dijalog.getRupa2X(), dijalog.getRupa2Y()));
                matricaIgre.setKanibal(new Point(dijalog.getKanibalX(), dijalog.getKanibalY()));
                ai.resetuj();
                prethodnaPocetnaPozicijaIgraca = new Point(dijalog.getIgracX(), dijalog.getIgracY());
                ai.setIgrac(new Point(dijalog.getIgracX(), dijalog.getIgracY()));
                ai.obradaTrenutneOdaje();
                platno.repaint();
                stepMenuItem.setEnabled(true);
            }
        });
    }

    public void output(String tekst) {
        if (logField != null) {
            logField.append("\n" + tekst);
            logField.setCaretPosition(logField.getText().length());
        }
    }

    public void outputAIMap(String tekst) {
        if (prikaziAIMapuMenuItem.isSelected()) output(tekst);
        System.out.println(tekst);
    }

    public void osveziPanel() {
        platno.repaint();
    }

    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");
        MetalLookAndFeel.setCurrentTheme(new KhakiMetalTheme());
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Metal Look & Feel not supported on this platform. \nProgram Terminated");
            System.exit(0);
        }
        mainForm = new Simulator();
        mainForm.setSize(new Dimension(900, 740));
        mainForm.setLocation(50, 50);
        mainForm.setVisible(true);
    }
}
