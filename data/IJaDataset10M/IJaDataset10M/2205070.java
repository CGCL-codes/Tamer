package org.owasp.orizon.ui;

import java.awt.FileDialog;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.owasp.orizon.About;
import com.apple.mrj.MRJApplicationUtils;

public class MilkGui extends JFrame {

    private StaticReviewer sR;

    /**
	 * 
	 */
    private static final long serialVersionUID = -6930510212121939931L;

    private static JFrame myself;

    private static JTextArea reportArea;

    private static String inputName;

    private static String reportTxt;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        MilkGui mG = new MilkGui();
    }

    /**
	 * This is the default constructor
	 */
    public MilkGui() {
        super();
        initialize();
        reportArea = null;
    }

    public static void setReportString(String report) {
        if (MilkGui.reportArea != null) {
            MilkGui.reportArea.setVisible(false);
            MilkGui.myself.remove(MilkGui.reportArea);
            MilkGui.myself.repaint();
        }
        JTextArea area = new JTextArea();
        area.setSize(500, 200);
        if (report != null) area.setText(report); else area.setText("No issues found!!!");
        area.setAutoscrolls(true);
        area.setEditable(false);
        area.setVisible(true);
        MilkGui.myself.add(area);
        MilkGui.myself.pack();
        MilkGui.reportArea = area;
        SourceViewer.show(inputName);
        return;
    }

    public static ImageIcon getImage(String name) {
        return (MilkGui.class.getResource("/" + name) != null) ? new ImageIcon(MilkGui.class.getResource("/" + name)) : new ImageIcon(name);
    }

    private void createMainReportArea() {
        JTextArea area = new JTextArea();
        area.setSize(500, 200);
        area.setVisible(true);
        MilkGui.myself.add(area);
        MilkGui.myself.pack();
        MilkGui.reportArea = area;
    }

    private void createMenus() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileOpen = new JMenuItem("Open file");
        fileOpen.setMnemonic(KeyEvent.VK_O);
        fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        fileOpen.setToolTipText("Open");
        fileOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                FileDialog dialog = new FileDialog(MilkGui.getRootFrame(), "Open source to review", FileDialog.LOAD);
                dialog.setVisible(true);
                dialog.setFilenameFilter(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".java"));
                    }
                });
                MilkGui.setInputName(dialog.getDirectory() + dialog.getFile());
            }
        });
        file.add(fileOpen);
        JMenuItem foldOpen = new JMenuItem("Open folder");
        foldOpen.setMnemonic(KeyEvent.VK_L);
        foldOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        foldOpen.setToolTipText("Open folder");
        foldOpen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                FileDialog dialog = new FileDialog(MilkGui.getRootFrame(), "Open folder to review", FileDialog.LOAD);
                System.setProperty("apple.awt.fileDialogForDirectories", "true");
                dialog.setVisible(true);
                MilkGui.setInputName(dialog.getFile());
                System.setProperty("apple.awt.fileDialogForDirectories", "false");
            }
        });
        file.add(foldOpen);
        menubar.add(file);
        JMenu review = new JMenu("Review");
        file.setMnemonic(KeyEvent.VK_R);
        JMenuItem reviewStatic = new JMenuItem("Static");
        reviewStatic.setMnemonic(KeyEvent.VK_S);
        reviewStatic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        reviewStatic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                sR = new StaticReviewer(MilkGui.getInputName());
                if (!sR.review()) MilkGui.setReportString(sR.getReport());
            }
        });
        review.add(reviewStatic);
        menubar.add(review);
        setJMenuBar(menubar);
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (Milk.isMac()) {
            new MacDefaultMenuHandler();
        }
        setVisible(true);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        MilkGui.myself = this;
        final SplashScreen splash = new SplashScreen();
        splash.pack();
        splash.setVisible(true);
        new Thread(new Runnable() {

            public void run() {
                try {
                    splash.setStatus("starting up", 10);
                    Thread.currentThread().sleep(100);
                    splash.setStatus("loading framework", 30);
                    Thread.currentThread().sleep(900);
                    splash.setStatus("initializing engine", 80);
                    Thread.currentThread().sleep(900);
                    splash.setStatus("enjoy your code review experience", 99);
                    Thread.currentThread().sleep(1000);
                    splash.setVisible(false);
                    MilkGui.myself.setVisible(true);
                    MilkGui.myself.setSize(500, 200);
                    MilkGui.myself.setTitle("Owasp Orizon");
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                    createMenus();
                } catch (InterruptedException ex) {
                }
            }
        }).start();
    }

    public static JFrame getRootFrame() {
        return myself;
    }

    public static String getInputName() {
        return inputName;
    }

    public static void setInputName(String inputName) {
        MilkGui.inputName = inputName;
        if (inputName != null) {
            myself.setTitle("Owasp Orizon - " + inputName);
        }
    }
}
