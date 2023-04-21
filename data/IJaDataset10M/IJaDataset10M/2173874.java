package bcry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List.*;

public class battlecryGUI extends JFrame implements ActionListener {

    private final String TEMP_LAYOUT = "data/temp/temp_lo.dat";

    private final String BANNER = "data/images/bcBanner.jpg";

    private final String BACKGROUND_IMAGE = "data/images/bcBackground.jpg";

    private final String README = "readme.html";

    private final String DEFAULT_OUTPUT_FILE = "data/temp/output.txt";

    private int fontSize = 4;

    private String fontColor = "#c0c0c0";

    private boolean showBackground = true;

    private java.util.List currentLyrics;

    private bcFileDialog fd;

    private String currentModule = "";

    private JFrame owner;

    private JButton buttonExit;

    private JButton buttonGen;

    private JButton buttonClear;

    private JButton buttonModinfo;

    private JButton buttonLoad;

    private JButton buttonClearLayout;

    private JMenuBar mainMenu = new JMenuBar();

    private JMenu menuFile;

    private JMenu menuView;

    private JMenu menuTools;

    private JMenu menuHelp;

    private JMenu submenuFont;

    private JMenu submenuFontSize;

    private JMenu submenuFontColor;

    private JMenuItem menuOpen;

    private JMenuItem menuSave;

    private JMenuItem menuOpenLayout;

    private JMenuItem menuSaveLayout;

    private JMenuItem menuExit;

    private JMenuItem menuAbout;

    private JMenuItem menuEditor;

    private JMenuItem menuConverter;

    private JMenuItem menuReadme;

    private JMenuItem menuToggleImage;

    private JMenuItem submenuFontSize2;

    private JMenuItem submenuFontSize3;

    private JMenuItem submenuFontSize4;

    private JMenuItem submenuFontSize5;

    private JMenuItem submenuFontSize6;

    private JMenuItem submenuFontColorBlack;

    private JMenuItem submenuFontColorGrey;

    private JPanel panel = new JPanel();

    private JPanel subpanel1 = new JPanel();

    private JPanel subpanel2 = new JPanel();

    private JPanel textPanel = new JPanel();

    private JPanel sideBarLeft = new JPanel();

    private JPanel sideBarRight = new JPanel();

    private JPanel logPanel = new JPanel();

    private JPanel layoutPanel = new JPanel();

    private TextArea logWindow = new TextArea(5, 80);

    private JTextArea layoutField = new JTextArea(3, 12);

    private JEditorPane textField;

    private JScrollPane mainScroll;

    private bcGenerator bc = null;

    private bcVoice voice = null;

    private bcModule module = null;

    private static battlecryGUI f;

    public battlecryGUI() {
        super("Battlecry 0.2cvs");
        initComponents();
    }

    private void initComponents() {
        menuFile = new JMenu("File");
        menuFile.setMnemonic('F');
        menuOpen = new JMenuItem("Open Module...", 'o');
        menuOpen.addActionListener(this);
        menuSave = new JMenuItem("Save Lyrics...", 's');
        menuSave.addActionListener(this);
        menuOpenLayout = new JMenuItem("Open Layout...", 'l');
        menuOpenLayout.addActionListener(this);
        menuSaveLayout = new JMenuItem("Save Layout...", 'y');
        menuSaveLayout.addActionListener(this);
        menuExit = new JMenuItem("Exit", 'x');
        menuExit.addActionListener(this);
        menuFile.add(menuOpen);
        menuFile.add(menuSave);
        menuFile.addSeparator();
        menuFile.add(menuOpenLayout);
        menuFile.add(menuSaveLayout);
        menuFile.addSeparator();
        menuFile.add(menuExit);
        menuView = new JMenu("View");
        menuView.setMnemonic('V');
        menuToggleImage = new JMenuItem("Toggle Background", 'b');
        menuToggleImage.addActionListener(this);
        submenuFont = new JMenu("Font");
        submenuFont.setMnemonic('N');
        submenuFontSize = new JMenu("Size");
        submenuFontSize2 = new JMenuItem("Very Small");
        submenuFontSize2.addActionListener(this);
        submenuFontSize3 = new JMenuItem("Small");
        submenuFontSize3.addActionListener(this);
        submenuFontSize4 = new JMenuItem("Normal");
        submenuFontSize4.addActionListener(this);
        submenuFontSize5 = new JMenuItem("Large");
        submenuFontSize5.addActionListener(this);
        submenuFontSize6 = new JMenuItem("Very Large");
        submenuFontSize6.addActionListener(this);
        submenuFontSize.add(submenuFontSize2);
        submenuFontSize.add(submenuFontSize3);
        submenuFontSize.add(submenuFontSize4);
        submenuFontSize.add(submenuFontSize5);
        submenuFontSize.add(submenuFontSize6);
        submenuFontColor = new JMenu("Color");
        submenuFontColorBlack = new JMenuItem("Black");
        submenuFontColorBlack.addActionListener(this);
        submenuFontColorGrey = new JMenuItem("Grey");
        submenuFontColorGrey.addActionListener(this);
        submenuFontColor.add(submenuFontColorBlack);
        submenuFontColor.add(submenuFontColorGrey);
        submenuFont.add(submenuFontColor);
        submenuFont.add(submenuFontSize);
        menuView.add(menuToggleImage);
        menuView.add(submenuFont);
        menuHelp = new JMenu("Help");
        menuHelp.setMnemonic('h');
        menuReadme = new JMenuItem("Show Readme", 'r');
        menuReadme.addActionListener(this);
        menuAbout = new JMenuItem("About...", 'a');
        menuAbout.addActionListener(this);
        menuHelp.add(menuReadme);
        menuHelp.add(menuAbout);
        menuTools = new JMenu("Tools");
        menuTools.setMnemonic('t');
        menuEditor = new JMenuItem("Module Editor", 'e');
        menuEditor.addActionListener(this);
        menuConverter = new JMenuItem("List Converter", 'c');
        menuConverter.addActionListener(this);
        menuTools.add(menuEditor);
        menuTools.add(menuConverter);
        mainMenu.add(menuFile);
        mainMenu.add(menuView);
        mainMenu.add(menuHelp);
        this.setJMenuBar(mainMenu);
        textField = new JEditorPane() {

            public void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon(BACKGROUND_IMAGE);
                Dimension d = getSize();
                g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        textField.setBackground(new Color(100, 100, 210));
        textField.setContentType("text/html");
        mainScroll = new JScrollPane(textField);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainScroll.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        logWindow.setFont(new Font("SansSerif", Font.PLAIN, 10));
        logWindow.setEditable(false);
        layoutField.setFont(new Font("SansSerif", Font.PLAIN, 10));
        layoutField.setBackground(new Color(155, 155, 200));
        JScrollPane layoutScroll = new JScrollPane(layoutField);
        buttonExit = new JButton("Exit");
        buttonExit.addActionListener(this);
        buttonGen = new JButton("Generate");
        buttonGen.addActionListener(this);
        buttonClear = new JButton("Clear");
        buttonClear.addActionListener(this);
        buttonModinfo = new JButton("Module Info");
        buttonModinfo.addActionListener(this);
        buttonLoad = new JButton("Load Module");
        buttonLoad.addActionListener(this);
        buttonClearLayout = new JButton("Clear");
        buttonClearLayout.addActionListener(this);
        logPanel.setLayout(new GridLayout(1, 0));
        logPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log Window"));
        logPanel.add(logWindow);
        textPanel.setLayout(new BorderLayout());
        textPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lyrics"));
        textPanel.add("Center", mainScroll);
        textPanel.setMinimumSize(new Dimension(10, 40));
        layoutPanel.setLayout(new BorderLayout());
        layoutPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Custom Layout"));
        layoutPanel.add("Center", layoutScroll);
        layoutPanel.add("South", buttonClearLayout);
        sideBarRight.setLayout(new GridLayout(3, 1));
        sideBarRight.add(layoutPanel);
        sideBarRight.setMinimumSize(new Dimension(20, 10));
        sideBarLeft.setLayout(new GridLayout(5, 1));
        sideBarLeft.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Actions"));
        sideBarLeft.add(buttonLoad);
        sideBarLeft.add(buttonGen);
        sideBarLeft.add(buttonClear);
        sideBarLeft.add(buttonModinfo);
        sideBarLeft.add(buttonExit);
        sideBarLeft.setMinimumSize(new Dimension(20, 10));
        subpanel2.setLayout(new BorderLayout());
        subpanel2.add("East", sideBarRight);
        subpanel2.add("Center", textPanel);
        subpanel1.setLayout(new BorderLayout());
        subpanel1.setOpaque(false);
        subpanel1.add("West", sideBarLeft);
        subpanel1.add("Center", subpanel2);
        JPanel subpanel3 = new JPanel();
        subpanel3.setLayout(new BorderLayout());
        subpanel3.add("Center", subpanel1);
        subpanel3.add("North", new JLabel(new ImageIcon(BANNER)));
        panel.setLayout(new BorderLayout());
        panel.add("Center", subpanel3);
        panel.add("South", logPanel);
        getContentPane().add(panel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setResizable(true);
        Window wnd = new Window(this);
        setLocation((int) (wnd.getToolkit().getScreenSize().getWidth() / 2 - this.getWidth() / 2), (int) (wnd.getToolkit().getScreenSize().getHeight() / 2 - this.getHeight() / 2));
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });
        guiLog("Initialized.");
        boolean[] options = new boolean[2];
        options[0] = true;
        options[1] = false;
        voice = new bcVoice(options, this);
    }

    private void lyricsToTextField() {
        if (bc != null) {
            currentLyrics = bc.getLyrics();
            drawCurrentLyrics(currentLyrics);
        } else {
            guiLog("You must load a module first.");
        }
    }

    private void writeLayoutToFile(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(layoutField.getText());
            bw.close();
        } catch (IOException e) {
        }
    }

    private void writeLyricsToFile(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            if (!textField.getText().equals("")) {
                bw.write(textField.getText());
                bw.newLine();
                bw.newLine();
                bw.close();
            } else {
                guiLog("Please load a module and generate a set of lyrics first.");
            }
        } catch (IOException e) {
        }
    }

    private void loadModule() {
        String layout = "";
        fd = new bcFileDialog("bcm", "Battlecry Module");
        if (!(layoutField.getText().equals(""))) {
            String temp = layoutField.getText().substring(layoutField.getText().length() - 3);
            if (temp.equals("dat")) {
                layout = layoutField.getText();
                guiLog("Trying to open layout file " + layoutField.getText());
            } else {
                guiLog("Saving custom layout...");
                writeLayoutToFile(TEMP_LAYOUT);
                layout = TEMP_LAYOUT;
            }
        }
        if (currentModule.equals("")) {
            clearLayout();
            currentModule = fd.openDialog().getPath();
            buttonLoad.setText("Reload");
        }
        if (!currentModule.equals("")) {
            if ((module != null) && (module.getDictionary() != null)) {
                module = new bcModule(currentModule, layout, voice, module.getDictionary());
            } else {
                module = new bcModule(currentModule, layout, voice);
            }
            bc = new bcGenerator(module, voice);
        }
    }

    private void readModuleInfo() {
        if (bc != null) {
            bc.getModuleInfo();
        } else {
            guiLog("You must load a module first.");
        }
    }

    private void clearLayout() {
        if (!(layoutField.getText().equals(""))) {
            layoutField.setText("");
            if (!(currentModule.equals(""))) {
                guiLog("Reloading Module...");
                loadModule();
            }
        }
    }

    private void showReadme() {
        try {
            textField.setPage("file://localhost/" + new File(README).getAbsoluteFile());
        } catch (IOException e) {
            guiLog(e.toString());
        }
    }

    private void clearMainWindow() {
        textField.setText("");
    }

    private void toggleBackground() {
        if (showBackground == true) {
            showBackground = false;
            textField = new JEditorPane();
            fontColor = "#000000";
        } else {
            showBackground = true;
            textField = new JEditorPane() {

                public void paintComponent(Graphics g) {
                    ImageIcon icon = new ImageIcon(BACKGROUND_IMAGE);
                    Dimension d = getSize();
                    g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
                    setOpaque(false);
                    super.paintComponent(g);
                }
            };
            fontColor = "#c0c0c0";
        }
        textField.setBackground(new Color(100, 100, 210));
        textField.setContentType("text/html");
        mainScroll.setViewportView(textField);
        drawCurrentLyrics(currentLyrics);
    }

    private void drawCurrentLyrics(java.util.List lyrics) {
        String output;
        if (lyrics != null) {
            output = "<html><font color=" + fontColor + "style=sans-serif size=" + (fontSize + 1) + "><b>";
            output += "<center>" + ((String) lyrics.get(1)).substring(1, ((String) lyrics.get(1)).length() - 1) + "</center></font><hr>";
            output += "<font color=" + fontColor + "style=sans-serif size=" + fontSize + ">";
            for (int i = 3; i < lyrics.size() - 1; i++) {
                output += (String) lyrics.get(i) + "<br>";
            }
            output += "<hr></b></font></html>";
            textField.setText(output);
        } else {
            guiLog("No lyrics found; you must load a module first.");
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() == buttonExit) || (e.getSource() == menuExit)) {
            System.out.println("Shutdown.");
            System.exit(0);
        } else if (e.getSource() == buttonGen) {
            lyricsToTextField();
        } else if (e.getSource() == buttonClear) {
            clearMainWindow();
        } else if (e.getSource() == buttonModinfo) {
            readModuleInfo();
        } else if (e.getSource() == buttonLoad) {
            loadModule();
        } else if (e.getSource() == menuOpen) {
            currentModule = "";
            loadModule();
        } else if (e.getSource() == menuSave) {
            writeLyricsToFile(DEFAULT_OUTPUT_FILE);
        } else if (e.getSource() == buttonClearLayout) {
            clearLayout();
        } else if (e.getSource() == menuReadme) {
            showReadme();
        } else if (e.getSource() == menuToggleImage) {
            toggleBackground();
        } else if (e.getSource() == submenuFontColorBlack) {
            fontColor = "#000000";
            drawCurrentLyrics(currentLyrics);
        } else if (e.getSource() == submenuFontColorGrey) {
            fontColor = "#c0c0c0";
            drawCurrentLyrics(currentLyrics);
        } else if (e.getSource() == submenuFontSize2) {
            fontSize = 2;
            drawCurrentLyrics(currentLyrics);
        } else if (e.getSource() == submenuFontSize3) {
            fontSize = 3;
            drawCurrentLyrics(currentLyrics);
        } else if (e.getSource() == submenuFontSize4) {
            fontSize = 4;
            drawCurrentLyrics(currentLyrics);
        } else if (e.getSource() == submenuFontSize5) {
            fontSize = 5;
            drawCurrentLyrics(currentLyrics);
        } else if (e.getSource() == submenuFontSize6) {
            fontSize = 6;
            drawCurrentLyrics(currentLyrics);
        }
    }

    private void guiLog(String text) {
        logWindow.append("Gui: " + text + '\n');
    }

    public void updateLog(String text) {
        logWindow.append(text);
    }

    public static void main(String[] args) {
        System.out.println("Battlecry GUI is starting...");
        f = new battlecryGUI();
    }
}
