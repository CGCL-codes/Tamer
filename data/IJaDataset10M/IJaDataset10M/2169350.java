package net.bpfurtado.ljcolligo.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import net.bpfurtado.ljcolligo.LJColligo;
import net.bpfurtado.ljcolligo.LJColligoException;
import net.bpfurtado.ljcolligo.LJColligoListener;
import net.bpfurtado.ljcolligo.util.Conf;
import org.apache.log4j.Logger;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class LJColligoFrame extends JFrame implements LJColligoListener, DownloadInBackgroundClient {

    private static final Logger logger = Logger.getLogger(LJColligoFrame.class);

    private static final long serialVersionUID = -1664443660071340253L;

    private static final Font FONT = new Font("Tahoma", Font.PLAIN, 14);

    private static final Font TA_FONT = new Font("Tahoma", Font.PLAIN, 12);

    private static final Font DOWNLOAD_BT_FONT = new Font("Tahoma", Font.BOLD, 14);

    private LJColligo ljcolligo = new LJColligo();

    private JFileChooser chooser = new JFileChooser();

    private final JTextField outputDirTf = new JTextField(30);

    private JTextField userNameTf;

    private JPasswordField passwordTf;

    private JTextArea outputTA;

    private JButton chooseOutputDirBt;

    private JButton downloadBt;

    private JProgressBar progressBar;

    private ComponentsEnabler enabler;

    private File generatedOutputFile;

    private JButton openBt;

    public LJColligoFrame() {
    }

    public void launch() {
        ljcolligo.addListener(this);
        this.enabler = new ComponentsEnabler();
        initView();
    }

    private void downloadEvents(String userName, String password) {
        outputTA.setText("");
        progressBar.setIndeterminate(true);
        DownloadInBackground task = new DownloadInBackground(this, ljcolligo, userName, password, outputDirTf.getText().trim());
        task.execute();
    }

    private void initView() {
        menu();
        widgets();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setTitle("LJColligo - A LiveJournal backup tool");
        setResizable(false);
        setBounds(100, 100, 787, 472);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                logger.debug(LJColligoFrame.this.getSize());
            }
        });
        setVisible(true);
    }

    @Override
    public void receiveFromLJColligo(String message) {
        outputTA.append(message);
        outputTA.setCaretPosition(outputTA.getDocument().getLength());
    }

    private void widgets() {
        JPanel main = createMainPanel();
        main.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        add(main);
    }

    private JPanel createMainPanel() {
        initComponents();
        JPanel main = new JPanel(new BorderLayout());
        FormLayout layout = new FormLayout("right:pref, 4dlu, 35dlu, 60dlu, 60dlu, 30dlu, 4dlu, 45dlu", "p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, p");
        layout.setRowGroups(new int[][] { { 1, 3, 5 } });
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(label("User name:"), cc.xy(1, 1));
        builder.add(userNameTf, cc.xyw(3, 1, 2));
        builder.add(label("Password:"), cc.xy(1, 3));
        builder.add(passwordTf, cc.xyw(3, 3, 2));
        builder.add(label("Ouput folder:"), cc.xy(1, 5));
        builder.add(outputDirTf, cc.xyw(3, 5, 4));
        builder.add(chooseOutputDirBt, cc.xy(8, 5));
        JButton b = new JButton("One click, to download them all!");
        b.setPreferredSize(new Dimension(0, 40));
        b.setFont(DOWNLOAD_BT_FONT);
        builder.add(downloadBt, cc.xyw(1, 7, 8));
        builder.add(new JScrollPane(outputTA), cc.xyw(1, 9, 8));
        this.progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(0, 25));
        builder.add(progressBar, cc.xyw(1, 11, 8));
        this.openBt = new JButton("Open Generated File");
        openBt.setMnemonic('o');
        openBt.setEnabled(false);
        openBt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                openGeneratedOutputFileAction();
            }
        });
        builder.add(openBt, cc.xyw(6, 13, 3));
        JPanel formPanel = builder.getPanel();
        main.add(createImagePanel(), BorderLayout.LINE_START);
        main.add(formPanel);
        return main;
    }

    private void initComponents() {
        this.userNameTf = new JTextField(Conf.getInstance().getUserName(), 20);
        userNameTf.setFont(FONT);
        this.passwordTf = new JPasswordField(Conf.getInstance().getPassword(), 20);
        passwordTf.setFont(FONT);
        outputDirTf.setText(System.getProperty("user.home"));
        this.chooseOutputDirBt = new JButton("Choose...");
        chooseOutputDirBt.setMnemonic('c');
        chooseOutputDirBt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.setCurrentDirectory(new File(outputDirTf.getText()));
                int returnVal = chooser.showOpenDialog(LJColligoFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    outputDirTf.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        this.downloadBt = new JButton("One click to download them all!");
        downloadBt.setIcon(Util.getImage("24-em-down.png"));
        downloadBt.setFont(DOWNLOAD_BT_FONT);
        downloadBt.setPreferredSize(new Dimension(0, 40));
        downloadBt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                downloadEntriesAction();
            }
        });
        downloadBt.setFont(new Font("Tahoma", Font.BOLD, 12));
        downloadBt.setMnemonic('d');
        downloadBt.setPreferredSize(new Dimension(230, 45));
        enabler.add(downloadBt);
        this.outputTA = new JTextArea(10, 45);
        outputTA.setFont(TA_FONT);
        outputTA.setLineWrap(true);
        outputTA.setWrapStyleWord(true);
        outputTA.setEditable(false);
    }

    private JPanel createImagePanel() {
        JPanel p = new JPanel();
        final JLabel goatImage = new JLabel(Util.getImage("goat.jpg"));
        goatImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                goatImage.setVisible(false);
                Dimension size = LJColligoFrame.this.getSize();
                size.setSize(size.getWidth() - goatImage.getSize().getWidth(), size.getHeight());
                LJColligoFrame.this.setSize(size);
            }
        });
        goatImage.setToolTipText("Click me to give the application a more serious and boring look ;-)");
        p.add(goatImage, BorderLayout.LINE_START);
        return p;
    }

    private void menu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu mainMenu = new JMenu("LJ Colligo");
        mainMenu.setMnemonic('l');
        menuBar.add(mainMenu);
        JMenuItem downloadMnIt = new JMenuItem("Download Entries");
        downloadMnIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        downloadMnIt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                downloadEntriesAction();
            }
        });
        enabler.add(downloadMnIt);
        mainMenu.add(downloadMnIt);
        mainMenu.add(new JSeparator());
        JMenuItem quitMnIt = new JMenuItem("Quit");
        quitMnIt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        quitMnIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        mainMenu.add(quitMnIt);
        JMenu help = new JMenu("Help");
        help.setMnemonic('h');
        help.setAlignmentX(LEFT_ALIGNMENT);
        JMenuItem aboutMnIt = new JMenuItem("About");
        aboutMnIt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new About(LJColligoFrame.this);
            }
        });
        help.add(aboutMnIt);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(help);
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setFont(FONT);
        return l;
    }

    private void downloadEntriesAction() {
        enabler.setEnable(false);
        downloadEvents(userNameTf.getText(), new String(passwordTf.getPassword()));
    }

    @Override
    public void append(String msg) {
        outputTA.append(msg);
    }

    @Override
    public void downloadDone(File outputFile) {
        enabler.setEnable(true);
        progressBar.setIndeterminate(false);
        openBt.setEnabled(true);
        this.generatedOutputFile = outputFile;
        try {
            Conf c = Conf.getInstance();
            c.setUserName(userNameTf.getText().trim());
            Document d = passwordTf.getDocument();
            String pw = d.getText(0, d.getLength()).trim();
            c.setPassword(pw);
            c.save();
            openBt.requestFocusInWindow();
        } catch (BadLocationException e) {
            throw new LJColligoException(e);
        }
    }

    private void openGeneratedOutputFileAction() {
        try {
            Desktop.getDesktop().open(generatedOutputFile);
        } catch (Exception e) {
            throw new LJColligoException(e);
        }
    }

    public static void handleError(final LJColligoFrame colligoFrame, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        JOptionPane.showMessageDialog(colligoFrame, "An error has occured\nStacktrace:\n" + sw.toString() + "\n", "Exception: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        final LJColligoFrame colligoFrame = new LJColligoFrame();
        final UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, final Throwable e) {
                System.out.println(".main()");
                if (SwingUtilities.isEventDispatchThread()) {
                    handleError(colligoFrame, e);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            handleError(colligoFrame, e);
                        }
                    });
                }
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    Thread.currentThread().setUncaughtExceptionHandler(uncaughtExceptionHandler);
                    colligoFrame.launch();
                } catch (Exception e) {
                    System.out.println(".run()");
                    handleError(colligoFrame, e);
                }
            }
        });
    }
}

class ComponentsEnabler {

    private Collection<JComponent> cs = new LinkedList<JComponent>();

    void add(JComponent c) {
        cs.add(c);
    }

    void setEnable(boolean b) {
        for (JComponent c : cs) {
            c.setEnabled(b);
        }
    }
}
