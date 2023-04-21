package PrologPlusCG.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import PrologPlusCG.PrologPlusCGApplet;
import PrologPlusCG.prolog.CompileException;
import PrologPlusCG.prolog.DataTypes;
import PrologPlusCG.prolog.Resolution;
import PrologPlusCG.prolog.TypeHierarchy;
import PrologPlusCG.prolog.PPCGEnv;

public class PrologPlusCGFrame extends JFrame implements DataTypes {

    private static final long serialVersionUID = 3545233613848130354L;

    public JTextComponent pCurPrologTextArea;

    private PPCGEnv env;

    Hashtable<String, String> applet_deployment_parameters;

    JTextComponent focusTextArea = null;

    String strSelected = null;

    String strLastQuery = "";

    public int consoleQueryStartIndex = 0;

    public MyTextArea consoleArea;

    public MyTextArea promptArea;

    char lastConsoleChar = ' ';

    MyTextArea DebugArea;

    DefaultStyledDocument doc = new DefaultStyledDocument();

    public final JTextPane ProgramTxtArea = new JTextPane(doc);

    SimpleAttributeSet attrStyles = new SimpleAttributeSet();

    SimpleAttributeSet attrStylesNormal = new SimpleAttributeSet();

    SimpleAttributeSet attrStylesMM = new SimpleAttributeSet();

    int startOfQuoteString = -1;

    JScrollPane jsp_ConsoleArea;

    JScrollPane jsp_ProgramTxtArea;

    JScrollPane jsp_DebugArea;

    JScrollPane jsp_PromptArea;

    JSplitPane jspltp_MainFrm;

    JSplitPane jspltp_ConsolePromptFrm;

    public JInternalFrame jMainFrame;

    PrimitiveGoalsFrame pPrimitivesWindow = null;

    JTree jtreePrimGoal = new JTree(getDefaultTreeModel());

    HTMLFrame pPPCGManualWindow = null;

    Cursor curTxt = null;

    String strCurrentFile = null;

    public boolean bFileIsModified = false;

    public boolean bFileIsCompiled = false;

    String strCurrentDirectoryPPCGGUI = null;

    String strCurrentDirectoryApplet = null;

    ImageIcon imageIcoPrlg;

    boolean fromCompileToExec = false;

    int sizeFont = 16;

    int styleFont = Font.PLAIN;

    JCheckBoxMenuItem jChkBxMnItmBld = new JCheckBoxMenuItem();

    JCheckBoxMenuItem jChkBxMnItm14 = new JCheckBoxMenuItem();

    JCheckBoxMenuItem jChkBxMnItm16 = new JCheckBoxMenuItem();

    JCheckBoxMenuItem jChkBxMnItm18 = new JCheckBoxMenuItem();

    JCheckBoxMenuItem jChkBxMnItm20 = new JCheckBoxMenuItem();

    public static final String ppcgLogoPath = PrologPlusCGFrame.class.getResource("ImgPrlg.png").getPath();

    public static final String strPPCGRootPath = new File(ppcgLogoPath).getParentFile().getParentFile().getParentFile().getParentFile().getPath().replaceAll("%20", " ");

    public static final String ppcgManualPath = strPPCGRootPath + "/manual/";

    public final JDesktopPane desktop = new JDesktopPane();

    BorderLayout borderLayout1 = new BorderLayout();

    JMenuBar menuBar1 = new JMenuBar();

    JMenu menuFile = new JMenu();

    JMenuItem menuFileExit = new JMenuItem();

    JMenuItem menuFileDeployApplet = new JMenuItem();

    JMenuItem menuFileLoadAppletFromXML = new JMenuItem();

    JMenu menuFont = new JMenu();

    JMenu menuWindows = new JMenu();

    JMenuItem menuWinPrim = new JMenuItem();

    JMenu menuHelp = new JMenu();

    JMenuItem menuHelpAbout = new JMenuItem();

    JMenuItem menuHelpPrimGoal = new JMenuItem();

    JMenuItem menuHelpManual = new JMenuItem();

    JToolBar toolBar = new JToolBar();

    JButton jButton1 = new JButton();

    JButton jButton2 = new JButton();

    JButton jButton3 = new JButton();

    JButton jButton4 = new JButton();

    JButton jBtSave = new JButton();

    JButton jBtCut = new JButton();

    JButton jBtCopy = new JButton();

    JButton jBtPaste = new JButton();

    JButton jBtCompile = new JButton();

    JButton jBtQuest = new JButton();

    ImageIcon imgNew;

    ImageIcon imgOpen;

    ImageIcon imgSave;

    ImageIcon imgClse;

    ImageIcon imgHlp;

    ImageIcon imgCut;

    ImageIcon imgCopy;

    ImageIcon imgPaste;

    ImageIcon imgCompile;

    ImageIcon imgQuest;

    ImageIcon imgStop;

    JLabel statusBar = new JLabel();

    JMenuItem jMenuItem1 = new JMenuItem();

    JMenuItem jMenuItem2 = new JMenuItem();

    JMenuItem jMenuItem3 = new JMenuItem();

    JMenuItem jMenuItem4 = new JMenuItem();

    JMenuItem jMenuItem5 = new JMenuItem();

    JMenu jMenu1 = new JMenu();

    JMenuItem jMenuItem6 = new JMenuItem();

    JMenuItem jMenuItem7 = new JMenuItem();

    JMenuItem jMenuItem8 = new JMenuItem();

    JMenu jMenu2 = new JMenu();

    JMenuItem jMenuItem9 = new JMenuItem();

    JMenuItem jMenuItem10 = new JMenuItem();

    JMenuItem jMenuItem11 = new JMenuItem();

    JButton jBtStop = new JButton();

    JMenuItem jMenuItem13 = new JMenuItem();

    void initAppletDeploymentParameters() {
        applet_deployment_parameters.put("noofboxes", "1");
        applet_deployment_parameters.put("noofbuttons", "1");
        applet_deployment_parameters.put("title", "Prolog+CG Applet");
        applet_deployment_parameters.put("width", "600");
        applet_deployment_parameters.put("height", "400");
        applet_deployment_parameters.put("box1label", "Label1:");
        applet_deployment_parameters.put("box2label", "Label2:");
        applet_deployment_parameters.put("box3label", "Label3:");
        applet_deployment_parameters.put("box4label", "Label4:");
        applet_deployment_parameters.put("box5label", "Label5:");
        applet_deployment_parameters.put("button1label", "Clear");
        applet_deployment_parameters.put("button2label", "Clear");
        applet_deployment_parameters.put("button3label", "Clear");
        applet_deployment_parameters.put("button4label", "Clear");
        applet_deployment_parameters.put("button5label", "Clear");
        applet_deployment_parameters.put("button1goal", "clearConsole.");
        applet_deployment_parameters.put("button2goal", "clearConsole.");
        applet_deployment_parameters.put("button3goal", "clearConsole.");
        applet_deployment_parameters.put("button4goal", "clearConsole.");
        applet_deployment_parameters.put("button5goal", "clearConsole.");
    }

    public PrologPlusCGFrame(PPCGEnv myenv, boolean isApplet) {
        env = myenv;
        env.bIsApplet = isApplet;
        applet_deployment_parameters = new Hashtable<String, String>();
        initAppletDeploymentParameters();
        this.getContentPane().setLayout(borderLayout1);
        curTxt = new Cursor(Cursor.TEXT_CURSOR);
        if (!isApplet) {
            imageIcoPrlg = new ImageIcon(ppcgManualPath + "ImgPrlg.png");
            enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            try {
                jbInit();
                this.setIconImage(imageIcoPrlg.getImage());
                jButton1.setIcon(imgOpen);
                jButton2.setIcon(imgClse);
                jButton3.setIcon(imgHlp);
                jButton4.setIcon(imgNew);
                jBtSave.setIcon(imgSave);
                jBtCut.setIcon(imgCut);
                jBtCopy.setIcon(imgCopy);
                jBtPaste.setIcon(imgPaste);
                jBtCompile.setIcon(imgCompile);
                jBtQuest.setIcon(imgQuest);
                jBtStop.setIcon(imgStop);
                File_New();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            consoleArea = new MyTextArea(env, this, true, sizeFont);
            try {
                File_New();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static URL getManualFilenameURL(String filename) {
        URL myURL = null;
        try {
            File ppcgLogoFile = new File(ppcgLogoPath);
            String ppcgRootDir = ppcgLogoFile.getParentFile().getParentFile().getParentFile().getParentFile().getPath().replaceAll("\\\\", "/").replaceAll(" ", "%20");
            StringBuffer fileName = new StringBuffer("file://");
            if (!ppcgRootDir.startsWith("/")) {
                fileName.append("/");
            }
            fileName.append(ppcgRootDir);
            fileName.append("/manual/").append(filename);
            String myURLName = fileName.toString();
            myURL = new URL(myURLName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myURL;
    }

    public static ImageIcon getImageIcon(String filename) {
        ImageIcon icon = null;
        try {
            URL myFileURL = PrologPlusCGFrame.getManualFilenameURL(filename);
            icon = new ImageIcon(myFileURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return icon;
    }

    public void printErrorMessage() {
        String strMsg = "\n" + env.getAndClearErrorMessage() + "\n";
        if (!env.bIsApplet) {
            consoleArea.append(strMsg);
        } else {
            PrologPlusCGApplet.webConsoleArea.append(strMsg);
        }
    }

    public void compileApplet() {
        boolean analysisSucceeded = true;
        pCurPrologTextArea = ProgramTxtArea;
        env.setProgramText(ProgramTxtArea.getText());
        strCurrentFile = null;
        if (env.program != null) {
            env.program.clear();
            if (env.typeHierarchy != null) {
                env.typeHierarchy.clear();
            }
            env.program = null;
            env.typeHierarchy = null;
        }
        try {
            env.compile.doCompile(false);
        } catch (CompileException e1) {
            String strMsg = "###### PROLOG+CG Syntactic analysis aborted : \n" + e1.getMessage();
            if (env.bIsApplet) {
                PrologPlusCGApplet.webConsoleArea.append(strMsg);
            } else {
                System.out.println(strMsg);
            }
            analysisSucceeded = false;
        }
        if (analysisSucceeded) {
            try {
                env.compile.doCompile(true);
                createTypeHierarchyIfPresentInProgram();
                bFileIsCompiled = true;
                if (env.aResolution != null) {
                    env.aResolution.clear_globalPrlgPCGObjs();
                }
            } catch (CompileException e2) {
                String strMsg = "###### PROLOG+CG Semantic analysis aborted : \n" + e2.getMessage();
                if (env.bIsApplet) {
                    PrologPlusCGApplet.webConsoleArea.append(strMsg);
                } else {
                    System.out.println(strMsg);
                }
            }
            fromCompileToExec = true;
        }
    }

    public static boolean fileIsUTF8(String strFileName) {
        File file = new File(strFileName);
        try {
            FileInputStream inStream = new FileInputStream(file);
            byte firstThreeBytes[] = new byte[3];
            int noOfBytesRead = inStream.read(firstThreeBytes);
            if (noOfBytesRead != 3) {
                inStream.close();
                return false;
            }
            byte BOM[] = { (byte) 239, (byte) 187, (byte) 191 };
            for (int i = 0; i < 3; ++i) {
                if (firstThreeBytes[i] != BOM[i]) {
                    inStream.close();
                    return false;
                }
            }
            inStream.close();
            return true;
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
            return false;
        }
    }

    private void setMainFrameTitleFromFilename(String fileName) {
        if (jMainFrame != null) {
            jMainFrame.setTitle(" The Main Frame - Prolog+CG File : " + fileName);
        }
    }

    /**
	 * Loads the program from the file nomFich.
	 * Does not compile it.
	 * @param nomFich The path + filename of the file to load.
	 * @return true on success, false on error.
	 */
    public boolean loadProgramFromFile(String nomFich) {
        try {
            strCurrentDirectoryPPCGGUI = nomFich;
            if (fileIsUTF8(nomFich)) {
                File file = new File(nomFich);
                FileInputStream inStream = new FileInputStream(file);
                byte firstThreeBytes[] = new byte[3];
                inStream.read(firstThreeBytes);
                InputStreamReader inReader = new InputStreamReader(inStream, "UTF-8");
                ProgramTxtArea.setCharacterAttributes(attrStylesNormal, true);
                ProgramTxtArea.read(inReader, null);
                inReader.close();
                setMainFrameTitleFromFilename(nomFich);
                return true;
            } else {
                File file = new File(nomFich);
                FileReader in = new FileReader(file);
                ProgramTxtArea.setCharacterAttributes(attrStylesNormal, true);
                ProgramTxtArea.read(in, null);
                in.close();
                setMainFrameTitleFromFilename(nomFich);
                return true;
            }
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
            return false;
        }
    }

    public void LoadFile(String nomFich) {
        boolean analysisSucceeded = true;
        PurgeMemory();
        try {
            if (!nomFich.endsWith(".prlg") && !nomFich.endsWith(".plgCG")) {
                throw new IOException("Error : The file name should have prlg or plgCG as a suffix");
            }
            strCurrentFile = nomFich;
            bFileIsModified = false;
            bFileIsCompiled = false;
            if (!loadProgramFromFile(nomFich)) {
                return;
            }
        } catch (IOException e1) {
            System.out.println(e1.getMessage());
            return;
        }
        pCurPrologTextArea = ProgramTxtArea;
        env.setProgramText(ProgramTxtArea.getText());
        if (env.program != null) {
            env.program.clear();
            if (env.typeHierarchy != null) {
                env.typeHierarchy.clear();
            }
            env.program = null;
            env.typeHierarchy = null;
        }
        try {
            env.compile.doCompile(false);
        } catch (CompileException e1) {
            System.out.println("###### PROLOG+CG Syntactic analysis aborted : \n");
            System.out.println(e1.getMessage());
            analysisSucceeded = false;
        }
        if (analysisSucceeded) {
            try {
                env.compile.doCompile(true);
                createTypeHierarchyIfPresentInProgram();
                bFileIsCompiled = true;
                if (env.aResolution != null) {
                    env.aResolution.clear_globalPrlgPCGObjs();
                }
            } catch (CompileException e2) {
                System.out.println("###### PROLOG+CG Semantic analysis aborted : \n");
                System.out.println(e2.getMessage());
            }
            fromCompileToExec = true;
        }
    }

    public boolean ResolveApplet(String quest) {
        env.bConvResultToString = true;
        if (env.bIsApplet) {
            pCurPrologTextArea = PrologPlusCGApplet.webConsoleArea;
        } else {
            pCurPrologTextArea = consoleArea;
        }
        boolean analysisSucceeded = true;
        try {
            env.compile.curLineNumber = 1;
            env.compile.compileQuery(quest);
        } catch (CompileException e1) {
            String strMsg;
            if (e1.getMessage().equals("null request")) {
                strMsg = "Error : null request";
            } else {
                strMsg = e1.getMessage();
            }
            if (env.bIsApplet) {
                env.recordErrorMessage(strMsg);
                printErrorMessage();
            } else {
                System.out.println(strMsg);
            }
            analysisSucceeded = false;
        }
        if (analysisSucceeded) {
            env.aResolution = new Resolution(env, false);
            env.aResolution.start();
            try {
                env.aResolution.join();
            } catch (java.lang.InterruptedException iex) {
            }
        }
        if (!analysisSucceeded) {
            return false;
        }
        return true;
    }

    public Vector<Hashtable<String, String>> Resolve(String quest) {
        return Resolve(quest, true);
    }

    public Vector<Hashtable<String, String>> Resolve(String quest, boolean convertRes) {
        env.bConvResultToString = convertRes;
        pCurPrologTextArea = consoleArea;
        boolean analysisSucceeded = true;
        try {
            env.compile.curLineNumber = 1;
            env.compile.compileQuery(quest);
        } catch (CompileException e1) {
            String strMsg;
            if (e1.getMessage().equals("null request")) {
                strMsg = "Error : null request";
            } else {
                strMsg = e1.getMessage();
            }
            if (env.bIsApplet) {
                env.recordErrorMessage(strMsg);
                printErrorMessage();
            } else {
                System.out.println(strMsg);
            }
            analysisSucceeded = false;
        }
        env.vctResult = new Vector<Hashtable<String, String>>(7, 2);
        if (analysisSucceeded) {
            env.aResolution = new Resolution(env, false);
            env.aResolution.start();
            try {
                env.aResolution.join();
            } catch (java.lang.InterruptedException iex) {
            }
        }
        return env.vctResult;
    }

    public void PurgeMemory() {
        strCurrentFile = null;
        ProgramTxtArea.setText("");
        if (env.program != null) {
            env.program.clear();
            if (env.typeHierarchy != null) {
                env.typeHierarchy.clear();
            }
            env.program = null;
            env.typeHierarchy = null;
        }
    }

    private void jbInit() throws Exception {
        try {
            imgOpen = PrologPlusCGFrame.getImageIcon("openFile.png");
            imgClse = PrologPlusCGFrame.getImageIcon("closeFile.png");
            imgHlp = PrologPlusCGFrame.getImageIcon("help.png");
            imgNew = PrologPlusCGFrame.getImageIcon("new.png");
            imgSave = PrologPlusCGFrame.getImageIcon("save.png");
            imgCut = PrologPlusCGFrame.getImageIcon("cut.png");
            imgCopy = PrologPlusCGFrame.getImageIcon("copy.png");
            imgPaste = PrologPlusCGFrame.getImageIcon("paste.png");
            imgCompile = PrologPlusCGFrame.getImageIcon("compile.png");
            imgQuest = PrologPlusCGFrame.getImageIcon("shortcut.png");
            imgStop = PrologPlusCGFrame.getImageIcon("stop.png");
        } catch (java.lang.NullPointerException e) {
            ;
        }
        DebugArea = new MyTextArea(env, this, false, sizeFont);
        DebugArea.setEditable(false);
        DebugArea.addMouseListener(new java.awt.event.MouseListener() {

            public void mouseEntered(MouseEvent zevt) {
            }

            public void mouseExited(MouseEvent zevt) {
            }

            public void mouseClicked(MouseEvent zevt) {
                SwitchDbgCsle();
            }

            public void mousePressed(MouseEvent zevt) {
                SwitchDbgCsle();
            }

            public void mouseReleased(MouseEvent zevt) {
            }
        });
        jsp_DebugArea = new JScrollPane(DebugArea);
        jsp_DebugArea.setMinimumSize(new Dimension(0, 0));
        consoleArea = new MyTextArea(env, this, true, sizeFont);
        consoleArea.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(MouseEvent zevt) {
                consoleArea.setCursor(curTxt);
            }

            public void mouseExited(MouseEvent zevt) {
                consoleArea.setCursor(Cursor.getDefaultCursor());
            }
        });
        jsp_ConsoleArea = new JScrollPane(consoleArea);
        jsp_ConsoleArea.setMinimumSize(new Dimension(0, 0));
        promptArea = new MyTextArea(env, this, true, sizeFont);
        promptArea.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(MouseEvent zevt) {
                promptArea.setCursor(curTxt);
            }

            public void mouseExited(MouseEvent zevt) {
                promptArea.setCursor(Cursor.getDefaultCursor());
            }
        });
        promptArea.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                promptArea_keyReleased(e);
            }
        });
        jsp_PromptArea = new JScrollPane(promptArea);
        jsp_PromptArea.setMinimumSize(new Dimension(0, 0));
        promptArea.append("?- ");
        ProgramTxtArea.setFont(new Font("Serif", Font.PLAIN, sizeFont));
        ProgramTxtArea.setCharacterAttributes(attrStyles, true);
        ProgramTxtArea.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                programTxtArea_keyReleased(e);
            }
        });
        ProgramTxtArea.addMouseListener(new MouseListener() {

            public void mouseEntered(MouseEvent zevt) {
                ProgramTxtArea.setCursor(curTxt);
            }

            public void mouseExited(MouseEvent zevt) {
                ProgramTxtArea.setCursor(Cursor.getDefaultCursor());
            }

            public void mouseClicked(MouseEvent zevt) {
                onPrgmTxtAreaClick(zevt);
            }

            public void mousePressed(MouseEvent zevt) {
                onPrgmTxtMsPressed(zevt);
            }

            public void mouseReleased(MouseEvent zevt) {
                onPrgmTxtMsReleased(zevt);
            }
        });
        ProgramTxtArea.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent zevt) {
                programTextArea_focusGained();
            }
        });
        StyleConstants.setForeground(attrStylesMM, new Color(Color.green.getRGB()));
        attrStyles = new SimpleAttributeSet();
        StyleConstants.setForeground(attrStyles, new Color(Color.blue.getRGB()));
        startOfQuoteString = -1;
        jsp_ProgramTxtArea = new JScrollPane(ProgramTxtArea);
        jsp_ProgramTxtArea.setMinimumSize(new Dimension(0, 0));
        jspltp_ConsolePromptFrm = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jsp_ConsoleArea, jsp_PromptArea);
        jspltp_ConsolePromptFrm.setDividerSize(10);
        jspltp_ConsolePromptFrm.setDividerLocation(100);
        jspltp_ConsolePromptFrm.setOneTouchExpandable(true);
        jspltp_ConsolePromptFrm.setContinuousLayout(true);
        jspltp_MainFrm = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jsp_ProgramTxtArea, jspltp_ConsolePromptFrm);
        jspltp_MainFrm.setDividerSize(10);
        jspltp_MainFrm.setDividerLocation(300);
        jspltp_MainFrm.setOneTouchExpandable(true);
        jspltp_MainFrm.setContinuousLayout(true);
        jMainFrame = new JInternalFrame(" The Main Frame - Untitled Prolog+CG File", true, false, true, true);
        jMainFrame.setFrameIcon(imageIcoPrlg);
        jMainFrame.getContentPane().add(jspltp_MainFrm);
        desktop.add(jMainFrame);
        jMainFrame.setSize(400, 400);
        jMainFrame.setLocation(0, 0);
        jMainFrame.setMaximum(true);
        jMainFrame.show();
        jMainFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setTitle("Prolog+CG " + PPCGEnv.PPCGVersion);
        this.addMouseListener(new MouseListener() {

            public void mouseEntered(MouseEvent zevt) {
            }

            public void mouseExited(MouseEvent zevt) {
            }

            public void mouseClicked(MouseEvent zevt) {
            }

            public void mousePressed(MouseEvent zevt) {
            }

            public void mouseReleased(MouseEvent zevt) {
            }
        });
        statusBar.setText(" ");
        jMenuItem1.setText("New");
        jMenuItem1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_New();
            }
        });
        jMenuItem2.setText("Open");
        jMenuItem2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Open();
            }
        });
        jMenuItem3.setText("Save");
        jMenuItem3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Save();
            }
        });
        jMenuItem4.setText("Save As");
        jMenuItem4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Save_As();
            }
        });
        jMenuItem5.setText("Close");
        jMenuItem5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Close();
            }
        });
        jMenu1.setText("Edit");
        jMenuItem6.setText("Cut");
        jMenuItem6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Cut();
            }
        });
        jMenuItem7.setText("Copy");
        jMenuItem7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Copy();
            }
        });
        jMenuItem8.setText("Paste");
        jMenuItem8.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Paste();
            }
        });
        jMenuItem13.setText("Go to line");
        jMenuItem13.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Go_to();
            }
        });
        jChkBxMnItmBld.setText("Bold");
        jChkBxMnItmBld.setEnabled(true);
        jChkBxMnItmBld.setSelected(styleFont == Font.BOLD);
        jChkBxMnItmBld.setToolTipText("Bold");
        jChkBxMnItmBld.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                bold_actionPerformed();
            }
        });
        jChkBxMnItmBld.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent zevt) {
                bold_actionPerformed();
            }
        });
        jChkBxMnItm14.setText("14");
        jChkBxMnItm14.setToolTipText("Size14");
        jChkBxMnItm14.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                changeFontSize_actionPerformed(14);
            }
        });
        jChkBxMnItm16.setText("16");
        jChkBxMnItm16.setToolTipText("Size16");
        jChkBxMnItm16.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                changeFontSize_actionPerformed(16);
            }
        });
        jChkBxMnItm18.setText("18");
        jChkBxMnItm18.setToolTipText("Size18");
        jChkBxMnItm18.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                changeFontSize_actionPerformed(18);
            }
        });
        jChkBxMnItm20.setText("20");
        jChkBxMnItm20.setToolTipText("Size20");
        jChkBxMnItm20.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                changeFontSize_actionPerformed(20);
            }
        });
        jMenu2.setText("Build");
        jMenuItem9.setText("Compile");
        jMenuItem9.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Build_Compile();
            }
        });
        jMenuItem10.setText("Answer Question");
        jMenuItem10.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Build_Request_Session();
            }
        });
        jMenuItem11.setText("Stop Execution");
        jMenuItem11.setToolTipText("Stop execution");
        jBtStop.setMaximumSize(new Dimension(33, 25));
        jBtStop.setPreferredSize(new Dimension(33, 25));
        jBtStop.setMinimumSize(new Dimension(33, 25));
        jBtStop.setToolTipText("Stop Execution");
        jBtStop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                stopExec();
            }
        });
        jMenuItem11.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                stopExec();
            }
        });
        menuFile.setText("File");
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                fileExit_actionPerformed();
            }
        });
        menuFileDeployApplet.setText("Deploy Applet");
        menuFileDeployApplet.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                fileDeployApplet_actionPerformed();
            }
        });
        menuFileLoadAppletFromXML.setText("Load Applet from XML");
        menuFileLoadAppletFromXML.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                fileLoadAppletFromXML_actionPerformed();
            }
        });
        jButton3.setMaximumSize(new Dimension(33, 25));
        jButton3.setPreferredSize(new Dimension(33, 25));
        jButton3.setMinimumSize(new Dimension(33, 25));
        jButton3.setToolTipText("Help");
        jButton3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                helpAbout_actionPerformed();
            }
        });
        menuFont.setText("Font");
        menuWindows.setText("Windows");
        menuWinPrim.setText("Primitives");
        menuWinPrim.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                helpPrimGoals_actionPerformed();
            }
        });
        menuHelp.setText("Help");
        menuHelpAbout.setText("About PROLOG+CG 2.0");
        menuHelpAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                helpAbout_actionPerformed();
            }
        });
        menuHelpPrimGoal.setText("Primitive Goals");
        menuHelpPrimGoal.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                helpPrimGoals_actionPerformed();
            }
        });
        menuHelpManual.setText("PROLOG+CG 2.0 user's Manual");
        menuHelpManual.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                helpManual_actionPerformed();
            }
        });
        jButton1.setMaximumSize(new Dimension(33, 25));
        jButton1.setPreferredSize(new Dimension(33, 25));
        jButton1.setMinimumSize(new Dimension(33, 25));
        jButton1.setToolTipText("Open");
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Open();
            }
        });
        jButton2.setMaximumSize(new Dimension(33, 25));
        jButton2.setPreferredSize(new Dimension(33, 25));
        jButton2.setMinimumSize(new Dimension(33, 25));
        jButton2.setToolTipText("Close");
        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Close();
            }
        });
        jButton4.setMaximumSize(new Dimension(33, 25));
        jButton4.setPreferredSize(new Dimension(33, 25));
        jButton4.setMinimumSize(new Dimension(33, 25));
        jButton4.setToolTipText("New");
        jButton4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_New();
            }
        });
        jBtSave.setMaximumSize(new Dimension(33, 25));
        jBtSave.setPreferredSize(new Dimension(33, 25));
        jBtSave.setMinimumSize(new Dimension(33, 25));
        jBtSave.setToolTipText("Save");
        jBtSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                File_Save();
            }
        });
        jBtCut.setMaximumSize(new Dimension(33, 25));
        jBtCut.setPreferredSize(new Dimension(33, 25));
        jBtCut.setMinimumSize(new Dimension(33, 25));
        jBtCut.setToolTipText("Cut");
        jBtCut.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Cut();
            }
        });
        jBtCopy.setMaximumSize(new Dimension(33, 25));
        jBtCopy.setPreferredSize(new Dimension(33, 25));
        jBtCopy.setMinimumSize(new Dimension(33, 25));
        jBtCopy.setToolTipText("Copy");
        jBtCopy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Copy();
            }
        });
        jBtPaste.setMaximumSize(new Dimension(33, 25));
        jBtPaste.setPreferredSize(new Dimension(33, 25));
        jBtPaste.setMinimumSize(new Dimension(33, 25));
        jBtPaste.setToolTipText("Paste");
        jBtPaste.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Edit_Paste();
            }
        });
        jBtCompile.setMaximumSize(new Dimension(33, 25));
        jBtCompile.setPreferredSize(new Dimension(33, 25));
        jBtCompile.setMinimumSize(new Dimension(33, 25));
        jBtCompile.setToolTipText("Compile");
        jBtCompile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Build_Compile();
            }
        });
        jBtQuest.setMaximumSize(new Dimension(33, 25));
        jBtQuest.setPreferredSize(new Dimension(33, 25));
        jBtQuest.setMinimumSize(new Dimension(33, 25));
        jBtQuest.setToolTipText("Answer Request");
        jBtQuest.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent zevt) {
                Build_Request_Session();
            }
        });
        toolBar.add(jButton4);
        toolBar.add(jButton1);
        toolBar.add(jBtSave);
        toolBar.add(jButton2);
        toolBar.addSeparator();
        toolBar.add(jBtCut);
        toolBar.add(jBtCopy);
        toolBar.add(jBtPaste);
        toolBar.addSeparator();
        toolBar.add(jBtCompile);
        toolBar.add(jBtQuest);
        toolBar.add(jBtStop);
        toolBar.addSeparator();
        toolBar.add(jButton3);
        menuFont.add(jChkBxMnItmBld);
        menuFont.addSeparator();
        menuFont.add(jChkBxMnItm14);
        menuFont.add(jChkBxMnItm16);
        menuFont.add(jChkBxMnItm18);
        menuFont.add(jChkBxMnItm20);
        menuWindows.add(menuWinPrim);
        menuHelp.add(menuHelpPrimGoal);
        menuHelp.add(menuHelpManual);
        menuHelp.add(menuHelpAbout);
        menuBar1.add(menuFile);
        menuBar1.add(jMenu1);
        menuBar1.add(menuFont);
        menuBar1.add(jMenu2);
        menuBar1.add(menuWindows);
        menuBar1.add(menuHelp);
        this.setJMenuBar(menuBar1);
        desktop.setBackground(Color.gray);
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(desktop, BorderLayout.CENTER);
        menuFile.add(jMenuItem1);
        menuFile.add(jMenuItem2);
        menuFile.addSeparator();
        menuFile.add(jMenuItem3);
        menuFile.add(jMenuItem4);
        menuFile.add(jMenuItem5);
        menuFile.addSeparator();
        menuFile.add(menuFileDeployApplet);
        menuFile.add(menuFileLoadAppletFromXML);
        menuFile.addSeparator();
        menuFile.add(menuFileExit);
        jMenu1.add(jMenuItem6);
        jMenu1.add(jMenuItem7);
        jMenu1.add(jMenuItem8);
        jMenu1.add(jMenuItem13);
        jMenu2.add(jMenuItem9);
        jMenu2.add(jMenuItem10);
        jMenu2.addSeparator();
        jMenu2.add(jMenuItem11);
        updateUIFontMenu();
    }

    public void fileExit_actionPerformed() {
        if (ProgramTxtArea.getText().equals("") || okToAbandon()) {
            System.exit(0);
        }
    }

    public void fileDeployApplet_actionPerformed() {
        if (strCurrentFile == null) {
            JOptionPane.showMessageDialog(jMainFrame, "You need to save your program first.", "Warning", JOptionPane.OK_OPTION);
            saveFile();
        } else if (bFileIsModified) {
            saveFile();
        }
        boolean bIsModal = true;
        boolean bLoadFromXML = false;
        AppletDeploymentDialog jDialogDeployApplet = new AppletDeploymentDialog(this, bIsModal, bLoadFromXML);
        jDialogDeployApplet.setVisible(true);
    }

    public void fileLoadAppletFromXML_actionPerformed() {
        if ((strCurrentFile == null || bFileIsModified) && !ProgramTxtArea.getText().equals("")) {
            JOptionPane.showMessageDialog(jMainFrame, "You need to save your program first,\nas it will be replaced.", "Warning", JOptionPane.OK_OPTION);
            saveFile();
        }
        boolean bIsModal = true;
        boolean bLoadFromXML = true;
        AppletDeploymentDialog jDialogDeployApplet = new AppletDeploymentDialog(this, bIsModal, bLoadFromXML);
        jDialogDeployApplet.setVisible(true);
    }

    public void helpAbout_actionPerformed() {
        PrologPlusCGFrame_AboutBox dlg = new PrologPlusCGFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation(((frmSize.width - dlgSize.width) / 2) + loc.x, ((frmSize.height - dlgSize.height) / 2) + loc.y);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            if (ProgramTxtArea.getText().equals("") || okToAbandon()) {
                super.processWindowEvent(e);
                System.exit(0);
            }
        }
    }

    void File_New() {
        if (ProgramTxtArea.getText().equals("") || okToAbandon()) {
            ProgramTxtArea.setText("");
            if (!env.bIsApplet) {
                jMainFrame.setTitle(" The Main Frame - Untitled Prolog+CG File");
            }
            env.program = null;
            strCurrentFile = null;
            bFileIsModified = false;
            bFileIsCompiled = false;
        }
    }

    void File_Open() {
        if (!ProgramTxtArea.getText().equals("") && !okToAbandon()) {
            return;
        }
        String spath = null;
        if (strCurrentDirectoryPPCGGUI != null) {
            spath = strCurrentDirectoryPPCGGUI;
        } else {
            spath = new File(strPPCGRootPath).getPath() + System.getProperty("file.separator") + "Samples";
        }
        JFileChooser chooser = new JFileChooser(spath);
        chooser.setDialogTitle("Open a Prolog+CG File");
        chooser.setCurrentDirectory(new File(spath));
        MyFileFilter filter = new MyFileFilter();
        filter.addExtension("prlg");
        filter.addExtension("plgCG");
        filter.setDescription("Prolog+CG files");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        String fileName = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getPath();
        } else {
            return;
        }
        try {
            if (!loadProgramFromFile(fileName)) {
                throw new IOException();
            }
            strCurrentFile = fileName;
            bFileIsModified = false;
            bFileIsCompiled = false;
            statusBar.setText(fileName + " is open");
            jMainFrame.moveToFront();
        } catch (IOException e1) {
            statusBar.setText("Error in trying to open " + fileName);
        }
    }

    boolean saveFile() {
        if (strCurrentFile == null) {
            return saveAsFile();
        }
        try {
            File file = new File(strCurrentFile);
            FileOutputStream outStream = new FileOutputStream(file);
            int BOM[] = { 239, 187, 191 };
            for (int i = 0; i < 3; ++i) {
                outStream.write(BOM[i]);
            }
            OutputStreamWriter outWriter = new OutputStreamWriter(outStream, "UTF-8");
            String text = ProgramTxtArea.getText();
            outWriter.write(text);
            outWriter.close();
            return true;
        } catch (IOException e) {
            statusBar.setText("Error in trying to save " + strCurrentFile);
        }
        return false;
    }

    boolean saveAsFile() {
        String spath = null;
        if (strCurrentDirectoryPPCGGUI != null) {
            spath = strCurrentDirectoryPPCGGUI;
        } else {
            spath = System.getProperty("user.dir") + System.getProperty("file.separator") + "PrologPlusCG" + System.getProperty("file.separator") + "Samples";
        }
        String fileName = null;
        JFileChooser chooser = new JFileChooser(spath);
        chooser.setSelectedFile(new File(spath));
        chooser.setDialogTitle("Save a Prolog+CG File");
        MyFileFilter filter = new MyFileFilter();
        filter.addExtension("prlg");
        filter.addExtension("plgCG");
        filter.setDescription("Prolog+CG files");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = chooser.getSelectedFile().getPath();
        } else {
            return false;
        }
        if (fileName != null) {
            strCurrentDirectoryPPCGGUI = fileName;
            if (!fileName.endsWith(".prlg") && !fileName.endsWith(".plgCG")) {
                strCurrentFile = fileName + ".plgCG";
            } else {
                strCurrentFile = fileName;
            }
            File testFile = new File(strCurrentFile);
            boolean notExistOrSaveOnIt = false;
            if (testFile.exists()) {
                int result = JOptionPane.showConfirmDialog(jMainFrame, "The file " + strCurrentFile + " exists already.\n Do you want to save on it ?", "Save File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                switch(result) {
                    case JOptionPane.YES_OPTION:
                        {
                            notExistOrSaveOnIt = true;
                            if (!bFileIsCompiled && (env.program != null)) {
                                env.program.clear();
                                if (env.typeHierarchy != null) {
                                    env.typeHierarchy.clear();
                                }
                                env.program = null;
                                env.typeHierarchy = null;
                            }
                        }
                        break;
                    case JOptionPane.NO_OPTION:
                        saveAsFile();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                    case JOptionPane.CLOSED_OPTION:
                        return false;
                    default:
                        return false;
                }
            } else {
                notExistOrSaveOnIt = true;
            }
            if (notExistOrSaveOnIt && saveFile()) {
                statusBar.setText(strCurrentFile + " is open");
                setMainFrameTitleFromFilename(strCurrentFile);
                jMainFrame.moveToFront();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    void File_Save() {
        saveFile();
    }

    void File_Save_As() {
        saveAsFile();
    }

    boolean okToAbandon() {
        if (!bFileIsModified) {
            return true;
        }
        String titreFntreDlg = "Unsaved changes";
        String[] message = { " ", "Save before closing ? ", " " };
        int result = JOptionPane.showConfirmDialog(jMainFrame, message, titreFntreDlg, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        switch(result) {
            case JOptionPane.YES_OPTION:
                return saveFile();
            case JOptionPane.NO_OPTION:
                return true;
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                return false;
            default:
                return false;
        }
    }

    void File_Close() {
        if (ProgramTxtArea.getText().equals("") || okToAbandon()) {
            strCurrentFile = null;
            ProgramTxtArea.setText("");
            jMainFrame.setTitle(" The Main Frame - Untitled Prolog+CG File");
            jMainFrame.moveToFront();
            statusBar.setText(" ");
            if (env.program != null) {
                env.program.clear();
                if (env.typeHierarchy != null) {
                    env.typeHierarchy.clear();
                }
                env.program = null;
                env.typeHierarchy = null;
            }
        }
    }

    void Edit_Cut() {
        focusTextArea.cut();
        if (!(focusTextArea instanceof MyTextArea)) {
            bFileIsModified = true;
            bFileIsCompiled = false;
        }
    }

    void Edit_Copy() {
        focusTextArea.copy();
    }

    void Edit_Paste() {
        focusTextArea.paste();
        if (!(focusTextArea instanceof MyTextArea)) {
            bFileIsModified = true;
            bFileIsCompiled = false;
        }
    }

    void Edit_Go_to() {
        GoToDialog dlg = new GoToDialog(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation(((frmSize.width - dlgSize.width) / 2) + loc.x, ((frmSize.height - dlgSize.height) / 2) + loc.y);
        dlg.setModal(true);
        dlg.setVisible(true);
        if (dlg.getWasOK()) {
            int lineno = dlg.getInteger();
            int positionOfLine = this.getPositionOfLine(lineno);
            ProgramTxtArea.requestFocus();
            try {
                ProgramTxtArea.setCaretPosition(positionOfLine);
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(jMainFrame, "IllegalArgumentException while setting program caret position to " + positionOfLine);
            }
        }
    }

    int getPositionOfLine(int lineno) {
        String text = ProgramTxtArea.getText().replaceAll("\r", "");
        int index;
        int locallineno = 1;
        for (index = 0; index < text.length(); ++index) {
            if (locallineno >= lineno) {
                return index;
            }
            if (text.charAt(index) == '\n') {
                ++locallineno;
            }
        }
        if (index == 0) {
            return 0;
        } else {
            return index - 1;
        }
    }

    void Build_Compile() {
        jspltp_ConsolePromptFrm.setTopComponent(jsp_DebugArea);
        jspltp_ConsolePromptFrm.setDividerLocation(0.75);
        pCurPrologTextArea = ProgramTxtArea;
        env.setProgramText(ProgramTxtArea.getText());
        DebugArea.setText("");
        boolean analysisSucceeded = true;
        if (!bFileIsCompiled) {
            if (env.program != null) {
                env.program.clear();
                if (env.typeHierarchy != null) {
                    env.typeHierarchy.clear();
                }
                env.program = null;
                env.typeHierarchy = null;
            }
            try {
                env.compile.doCompile(false);
            } catch (CompileException e1) {
                DebugArea.append("###### Syntactic analysis aborted : \n");
                DebugArea.append(e1.getMessage());
                if (env.program != null) {
                    env.program.clear();
                    if (env.typeHierarchy != null) {
                        env.typeHierarchy.clear();
                    }
                    env.program = null;
                    env.typeHierarchy = null;
                }
                DebugArea.append("   Please, click to switch to the console mode.");
                fromCompileToExec = true;
                analysisSucceeded = false;
            }
            fromCompileToExec = true;
            if (analysisSucceeded) {
                try {
                    env.compile.doCompile(true);
                    createTypeHierarchyIfPresentInProgram();
                    bFileIsCompiled = true;
                    if (env.aResolution != null) {
                        env.aResolution.clear_globalPrlgPCGObjs();
                    }
                } catch (CompileException e2) {
                    DebugArea.append("###### Semantic analysis aborted : \n");
                    DebugArea.append(e2.getMessage());
                    if (env.program != null) {
                        env.program.clear();
                        if (env.typeHierarchy != null) {
                            env.typeHierarchy.clear();
                        }
                        env.program = null;
                        env.typeHierarchy = null;
                    }
                    fromCompileToExec = true;
                }
                if (env.program != null) {
                    DebugArea.append("###### Build operation terminated with success. ######\n\n");
                    DebugArea.append("   Please, click here to switch to console mode, where results can be shown.\n");
                    fromCompileToExec = true;
                }
            }
        }
    }

    void Build_Request_Session() {
        SwitchDbgCsle();
        if ((env.program != null) && !bFileIsCompiled) {
            JOptionPane.showMessageDialog(jMainFrame, "The program has been modified, so it will be compiled first");
            Build_Compile();
        }
        pCurPrologTextArea = consoleArea;
        boolean analysisSucceeded = true;
        String query = promptArea.getText();
        if (query.substring(0, 2).equals("?-")) {
            query = query.substring(2);
        }
        strLastQuery = query;
        consoleArea.append("\n?-" + query + "\n");
        try {
            env.compile.compileQuery(query);
        } catch (CompileException e1) {
            if (e1.getMessage().equals("null request")) {
                JOptionPane.showMessageDialog(jMainFrame, "Please, write your question first.", "Warning", JOptionPane.OK_OPTION);
            } else {
                consoleArea.append(e1.getMessage() + "\n");
            }
            analysisSucceeded = false;
        }
        consoleArea.append(" \n");
        if (analysisSucceeded) {
            env.aResolution = new Resolution(env, true);
            env.aResolution.start();
        } else {
            promptArea.setText("?-");
        }
    }

    void createTypeHierarchyIfPresentInProgram() {
        if (env.program.containsKey(env.compile.valSysSP)) {
            env.typeHierarchy = new TypeHierarchy(env);
            env.typeHierarchy.createTypeHierarchy();
        }
    }

    protected TreeModel getDefaultTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("PROLOG+CG 2.0 Primitive Goals");
        DefaultMutableTreeNode AritOpr = new DefaultMutableTreeNode("Arithmetic_Goals");
        root.add(AritOpr);
        AritOpr.add(new DefaultMutableTreeNode("add : add(number, number)"));
        AritOpr.add(new DefaultMutableTreeNode("sub : sub(number, number)"));
        AritOpr.add(new DefaultMutableTreeNode("mul : mul(number, number)"));
        AritOpr.add(new DefaultMutableTreeNode("div : div(number, number)"));
        AritOpr.add(new DefaultMutableTreeNode("val : val(Free_Variable OR IdObject, Prefix_Expression OR IdObject)"));
        DefaultMutableTreeNode RelOpr = new DefaultMutableTreeNode("Relational_Goals");
        root.add(RelOpr);
        RelOpr.add(new DefaultMutableTreeNode("sup : sup(number, number)"));
        RelOpr.add(new DefaultMutableTreeNode("inf : inf(number, number)"));
        RelOpr.add(new DefaultMutableTreeNode("eqv : eqv(arg1, arg2); the two arguments should have values"));
        RelOpr.add(new DefaultMutableTreeNode("eq : eq(arg1, arg2); unify arg1 and arg2"));
        RelOpr.add(new DefaultMutableTreeNode("dif : dif(arg1, arg2); arg1 is different from arg2. It corresponds to not(eq(arg1, arg2)). "));
        DefaultMutableTreeNode RndOpr = new DefaultMutableTreeNode("Randomness goals");
        root.add(RndOpr);
        RndOpr.add(new DefaultMutableTreeNode("seed : seed(number); set random seet to number"));
        RndOpr.add(new DefaultMutableTreeNode("rnd : rnd(number1, number1, free_var); set free_var to a pseudorandom number between number1 and number2"));
        DefaultMutableTreeNode LogicOpr = new DefaultMutableTreeNode("Logical_Goals");
        root.add(LogicOpr);
        LogicOpr.add(new DefaultMutableTreeNode("not : not(Goal)"));
        LogicOpr.add(new DefaultMutableTreeNode("fail : fail ; it cannot be satisfied by default"));
        DefaultMutableTreeNode ListOpr = new DefaultMutableTreeNode("List_Goals");
        root.add(ListOpr);
        ListOpr.add(new DefaultMutableTreeNode("| : the constructor operator"));
        ListOpr.add(new DefaultMutableTreeNode("member : member(arg1, List)"));
        ListOpr.add(new DefaultMutableTreeNode("length : length(List, Integer)"));
        ListOpr.add(new DefaultMutableTreeNode("shuffle : shuffle(InputList, FreeVarOutputList)"));
        DefaultMutableTreeNode StrgOpr = new DefaultMutableTreeNode("StringIdent_Goals");
        root.add(StrgOpr);
        StrgOpr.add(new DefaultMutableTreeNode("stringToLetters : stringToLetters(String, ListOfCharsAsStrings)"));
        StrgOpr.add(new DefaultMutableTreeNode("identToLetters : identToLetters(Ident, ListOfCharsAsString)"));
        StrgOpr.add(new DefaultMutableTreeNode("concat : concat(Data1, Data2, String3) ; two or three must be bound. Data1 and Data2 can be Number, Boolean, Ident, or String."));
        DefaultMutableTreeNode TypeOpr = new DefaultMutableTreeNode("Types_Goals");
        root.add(TypeOpr);
        TypeOpr.add(new DefaultMutableTreeNode("isSubType : isSubType(Type1, Type2); is Type1 a subtype of Type2"));
        TypeOpr.add(new DefaultMutableTreeNode("immediateSubTypes : immediateSubTypes(Type, ListOfChildren); give all the immediate subtypes of the type Type."));
        TypeOpr.add(new DefaultMutableTreeNode("subTypes : subTypes(Type, L); Put in the list L all the subtypes of Type"));
        TypeOpr.add(new DefaultMutableTreeNode("isSuperType : isSuperType(Type1, Type2); is Type1 a supertype of Type2"));
        TypeOpr.add(new DefaultMutableTreeNode("immediateSuperTypes : immediateSuperTypes(Type, ListOfParents); give all the immediate supertypes of the type Type."));
        TypeOpr.add(new DefaultMutableTreeNode("superTypes : superTypes(Type, L); Put in the list L all the supertypes of Type"));
        TypeOpr.add(new DefaultMutableTreeNode("maxComSubType : maxComSubType(Type1, Type2, Type3); the maxComSubType of Type1 & Type2 is Type3"));
        TypeOpr.add(new DefaultMutableTreeNode("maxComSubTypes : maxComSubTypes(Type1, Type2, ListOfTypes); returns in ListOfTypes the \"maximum common subtypes\" of Type1 and Type2"));
        TypeOpr.add(new DefaultMutableTreeNode("minComSuperType : minComSuperType(Type1, Type2, Type3); the minComSuperType of Type1 & Type2 is Type3"));
        TypeOpr.add(new DefaultMutableTreeNode("minComSuperTypes : minComSuperType(Type1, Type2, ListOfTypes); returns in ListOfTypes the \"minimum common supertypes\" of Type1 and Type2"));
        TypeOpr.add(new DefaultMutableTreeNode("isInstance : isInstance(Referent, Type); check if Referent is a referent of the type Type."));
        TypeOpr.add(new DefaultMutableTreeNode("addInstance : addInstance(Referent, Type); add the new information that Referent is a referent of the type Type."));
        DefaultMutableTreeNode CGOpr = new DefaultMutableTreeNode("CG_Goals");
        root.add(CGOpr);
        CGOpr.add(new DefaultMutableTreeNode("concOfCG : concOfCG(Concept, CG) ; checks if Concept is a concept of CG."));
        CGOpr.add(new DefaultMutableTreeNode("branchOfCG : branchOfCG(CG_Branch, CG) ; checks if CG_Branch is a branch of CG. branchOfCG is non-determinist; it is similar to member."));
        CGOpr.add(new DefaultMutableTreeNode("maximalJoin : maximalJoin(CG1, EntryConc_CG1, CG2, EntryConc_CG2, CGResult, EntryConcResult). EntryConcs are optionals. If given, they should be variables bound to concepts"));
        CGOpr.add(new DefaultMutableTreeNode("generalize : generalize(CG1, EntryConc_CG1, CG2, EntryConc_CG2, CGResult, EntryConcResult). EntryConcs are optionals. If given, they should be variables bound to concepts"));
        CGOpr.add(new DefaultMutableTreeNode("subsume : subsume(CG1, EntryConc_CG1, CG2, EntryConc_CG2, CGResult, EntryConcResult). EntryConcs are optionals as well as CGResult. If EntryConc_CG2 are given, they should be variables bound to concepts"));
        DefaultMutableTreeNode MetaOpr = new DefaultMutableTreeNode("Meta_Goals");
        root.add(MetaOpr);
        MetaOpr.add(new DefaultMutableTreeNode("/ : the cut meta_goal"));
        MetaOpr.add(new DefaultMutableTreeNode("free : free(X); checks if X is a free variable"));
        MetaOpr.add(new DefaultMutableTreeNode("findall : findall(IdVar, Goal, List); put in List all the possible values of IdVar that result from the possible (re)satisfactions of Goal"));
        MetaOpr.add(new DefaultMutableTreeNode("term_list : term_list(Term, List); produce a term that corresponds to the list or vice versa"));
        MetaOpr.add(new DefaultMutableTreeNode("set_list : set_list(Set, List); produce a set that corresponds to the list or vice versa"));
        DefaultMutableTreeNode IOOpr = new DefaultMutableTreeNode("IO_Goals");
        MetaOpr.add(IOOpr);
        IOOpr.add(new DefaultMutableTreeNode("read : read(Free_Variable)"));
        IOOpr.add(new DefaultMutableTreeNode("read_sentence : read_sentence(Free_Variable); read a sentence and bind it to Free_Variable as a list of lexical units"));
        IOOpr.add(new DefaultMutableTreeNode("read_sentence : read_sentence(String, Free_Variable); decompose the String as a list of lexical units and bind it to Free_Variable"));
        IOOpr.add(new DefaultMutableTreeNode("write : write(arg); no newline"));
        IOOpr.add(new DefaultMutableTreeNode("writenl : writenl(arg); with newline"));
        IOOpr.add(new DefaultMutableTreeNode("nl : nl/0; emit newline"));
        IOOpr.add(new DefaultMutableTreeNode("clearConsole : clearConsole/0; clear the console"));
        DefaultMutableTreeNode IDBOpr = new DefaultMutableTreeNode("IDB_Goals");
        MetaOpr.add(IDBOpr);
        IDBOpr.add(new DefaultMutableTreeNode("asserta : asserta(Goal, List); add at the top of the paquet"));
        IDBOpr.add(new DefaultMutableTreeNode("assertz : assertz(Goal, List); add at the bottom of the paquet"));
        IDBOpr.add(new DefaultMutableTreeNode("retract : retract(Goal)"));
        IDBOpr.add(new DefaultMutableTreeNode("suppress : suppress(TermIdent, NbreArgOfTheTerm); suppress the paquet with the specified signature"));
        DefaultMutableTreeNode CallOpr = new DefaultMutableTreeNode("Call_To_Application_Goals");
        root.add(CallOpr);
        CallOpr.add(new DefaultMutableTreeNode("exec : exec(String_filename); execute the application specified in the argument without waiting its termination"));
        CallOpr.add(new DefaultMutableTreeNode("execAndWait : execAndWait(String_filename); execute the application specified in the argument and wait its termination to resume the current resolution"));
        DefaultMutableTreeNode ExtCallOpr = new DefaultMutableTreeNode("Call_From_Application_Goals");
        root.add(ExtCallOpr);
        ExtCallOpr.add(new DefaultMutableTreeNode("LoadFile : LoadFile(String_filename); Load and compile the content of the file"));
        DefaultMutableTreeNode ExtCallOprRslve = new DefaultMutableTreeNode("Resolve");
        ExtCallOpr.add(ExtCallOprRslve);
        ExtCallOprRslve.add(new DefaultMutableTreeNode("Resolve : Resolve(String_Quest); Resolve the question without conversion of the result and with the default mode"));
        ExtCallOprRslve.add(new DefaultMutableTreeNode("Resolve : Resolve(String_Quest, boolean_ConvertRslt); Resolve the question with the conversion of the result (if true) and with the default mode"));
        ExtCallOpr.add(new DefaultMutableTreeNode("SaveFile : SaveFile(String_filename); Save the content of the current program in the specified file"));
        ExtCallOpr.add(new DefaultMutableTreeNode("PurgeMemory : PurgeMemory(); Delete the current program from the memory"));
        DefaultMutableTreeNode ClsJvOpr = new DefaultMutableTreeNode("Java_Class_Goals");
        root.add(ClsJvOpr);
        ClsJvOpr.add(new DefaultMutableTreeNode("new : new(IdObject, ClassName, ConstructorArgumentsList); Create a Java Object"));
        ClsJvOpr.add(new DefaultMutableTreeNode("destroy : destroy(IdObject); destroy the Java object"));
        ClsJvOpr.add(new DefaultMutableTreeNode("destroyAll : destroyAll; destroy all the Java objects that were created"));
        ClsJvOpr.add(new DefaultMutableTreeNode("get : get(Data, AttributeName, IdObject); get the value of the attribute of the object and unify it with Data"));
        ClsJvOpr.add(new DefaultMutableTreeNode("set : set(Data, AttributeName, IdObject); set Data as the value of the attribute of the object"));
        ClsJvOpr.add(new DefaultMutableTreeNode("objectify : objectify(JavaObject, IdObject); give the JavaObject the name IdObject, for use by execMethod"));
        DefaultMutableTreeNode ClsJvOprExecMtd = new DefaultMutableTreeNode("execMethod");
        ClsJvOpr.add(ClsJvOprExecMtd);
        ClsJvOprExecMtd.add(new DefaultMutableTreeNode("execMethod : execMethod(DataORvoid, ClassNameOrObjectName, MethodName, ArgumentsList); execute the call to the method of a ClassName or an object and unify the value returned with Data, or specify void if not"));
        ClsJvOprExecMtd.add(new DefaultMutableTreeNode("execMethod : execMethod(Data, Data_Type, ClassNameOrObjectName, MethodName, ArgumentsList); execute the call to the method of a ClassName or an object and unify the value returned with Data according to the specified DataType, or specify void if not"));
        DefaultMutableTreeNode OOOpr = new DefaultMutableTreeNode("object_oriented_Goals");
        root.add(OOOpr);
        OOOpr.add(new DefaultMutableTreeNode("createInstance : createInstance(ObjIdentifier, Term); create an instance of the object with the descriptor Term"));
        return new DefaultTreeModel(root);
    }

    void helpPrimGoals_actionPerformed() {
        pPrimitivesWindow = new PrimitiveGoalsFrame(desktop, jtreePrimGoal);
        pPrimitivesWindow.show();
        pPrimitivesWindow.moveToFront();
    }

    void helpManual_actionPerformed() {
        pPPCGManualWindow = new HTMLFrame(this, PrologPlusCGFrame.getManualFilenameURL("index.html"));
        pPPCGManualWindow.show();
        pPPCGManualWindow.moveToFront();
    }

    void stopExec() {
        env.bStopExec = true;
    }

    void bold_actionPerformed() {
        if (jChkBxMnItmBld.isSelected()) {
            styleFont = Font.BOLD;
        } else {
            styleFont = Font.PLAIN;
        }
        ProgramTxtArea.setFont(new Font("Serif", styleFont, sizeFont));
        DebugArea.setFont(new Font("Serif", styleFont, sizeFont));
        consoleArea.setFont(new Font("Serif", styleFont, sizeFont));
        promptArea.setFont(new Font("Serif", styleFont, sizeFont));
        DebugArea.repaint();
        ProgramTxtArea.repaint();
        consoleArea.repaint();
        promptArea.repaint();
        updateUIFontMenu();
    }

    void changeFontSize_actionPerformed(int t) {
        sizeFont = t;
        ProgramTxtArea.setFont(new Font("Serif", styleFont, sizeFont));
        ProgramTxtArea.repaint();
        DebugArea.setFont(new Font("Serif", styleFont, sizeFont));
        DebugArea.repaint();
        consoleArea.setFont(new Font("Serif", styleFont, sizeFont));
        consoleArea.repaint();
        promptArea.setFont(new Font("Serif", styleFont, sizeFont));
        promptArea.repaint();
        updateUIFontMenu();
    }

    void updateUIFontMenu() {
        jChkBxMnItmBld.setState(styleFont == Font.BOLD);
        jChkBxMnItm14.setState(sizeFont == 14);
        jChkBxMnItm16.setState(sizeFont == 16);
        jChkBxMnItm18.setState(sizeFont == 18);
        jChkBxMnItm20.setState(sizeFont == 20);
    }

    void promptArea_keyReleased(KeyEvent e) {
        int onmask = InputEvent.CTRL_DOWN_MASK;
        if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (promptArea.getText().trim().endsWith("."))) {
            if (!env.bInReadingMode) {
                Build_Request_Session();
            } else {
                env.aReaderThread.readSomething();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_C && (e.getModifiersEx() & (onmask)) == onmask) {
            Edit_Copy();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            env.io.setPrompt("?- " + strLastQuery);
        }
    }

    void programTxtArea_keyReleased(KeyEvent e) {
        if (e.getKeyChar() == '"') {
            if (startOfQuoteString == -1) {
                startOfQuoteString = ProgramTxtArea.getCaretPosition();
            } else {
                int endOfQuoteString = ProgramTxtArea.getCaretPosition() - 1 - startOfQuoteString;
                if (endOfQuoteString > 0) {
                    String strQuoteString = null;
                    try {
                        strQuoteString = ProgramTxtArea.getText(startOfQuoteString, endOfQuoteString);
                    } catch (java.lang.NullPointerException npe) {
                    } catch (BadLocationException e1) {
                    }
                    if (strQuoteString.endsWith(".gif") || strQuoteString.endsWith(".png") || strQuoteString.endsWith(".jpg") || strQuoteString.endsWith(".au") || strQuoteString.endsWith(".txt") || strQuoteString.endsWith(".rtf") || strQuoteString.endsWith(".htm") || strQuoteString.endsWith(".html")) {
                        doc.setCharacterAttributes(startOfQuoteString, endOfQuoteString, attrStylesMM, false);
                    }
                }
                startOfQuoteString = -1;
            }
        }
        bFileIsModified = true;
        bFileIsCompiled = false;
    }

    void SwitchDbgCsle() {
        jspltp_ConsolePromptFrm.setTopComponent(jsp_ConsoleArea);
        jspltp_ConsolePromptFrm.setDividerLocation(0.75);
        fromCompileToExec = false;
    }

    void onPrgmTxtAreaClick(MouseEvent zevt) {
        focusTextArea = ProgramTxtArea;
    }

    void onPrgmTxtMsReleased(MouseEvent zevt) {
        try {
            strSelected = focusTextArea.getSelectedText();
        } catch (IllegalArgumentException iae) {
        } catch (java.lang.NullPointerException npe) {
        }
    }

    void onPrgmTxtMsPressed(MouseEvent zevt) {
        strSelected = null;
        try {
            strSelected = ProgramTxtArea.getSelectedText();
        } catch (java.lang.NullPointerException npe) {
        }
        if ((strSelected != null) && SwingUtilities.isRightMouseButton(zevt)) {
            if (strSelected.endsWith(".gif")) {
                new ImageJInternalFrame(this, strSelected);
            } else if (strSelected.endsWith(".png")) {
                new ImageJInternalFrame(this, strSelected);
            } else if (strSelected.endsWith(".jpg")) {
                new ImageJInternalFrame(this, strSelected);
            } else if (strSelected.endsWith(".txt")) {
                new TextFileJInternalFrame(env, this, strSelected);
            } else if (strSelected.endsWith(".htm") || strSelected.endsWith(".rtf") || strSelected.endsWith(".html")) {
                try {
                    new HTMLFrame(this, new URL(strSelected));
                } catch (MalformedURLException e) {
                }
            }
        }
        strSelected = null;
    }

    void programTextArea_focusGained() {
        focusTextArea = ProgramTxtArea;
    }
}
