package net.sf.jabref.oo;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import net.sf.jabref.*;
import net.sf.jabref.export.layout.Layout;
import net.sf.jabref.export.layout.LayoutHelper;
import net.sf.jabref.external.PushToApplication;
import net.sf.jabref.plugin.SidePanePlugin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This test panel can be opened by reflection from JabRef, passing the JabRefFrame as an
 * argument to the start() method. It displays buttons for testing interaction functions
 * between JabRef and OpenOffice.
 */
public class OOTestPanel extends AbstractWorker implements SidePanePlugin, PushToApplication {

    public static boolean postLayoutSupported;

    static {
        postLayoutSupported = true;
        try {
            Layout l = new LayoutHelper(new StringReader("")).getLayoutFromText(Globals.FORMATTER_PACKAGE);
            l.setPostFormatter(null);
        } catch (NoSuchMethodError ex) {
            postLayoutSupported = false;
        } catch (Exception ex) {
        }
    }

    TestPanel comp;

    JDialog diag;

    static JButton connect, manualConnect, selectDocument, setStyleFile = new JButton(Globals.lang("Select style file")), pushEntries = new JButton(Globals.lang("Cite")), pushEntriesInt = new JButton(Globals.lang("Cite in-text")), pushEntriesEmpty = new JButton(Globals.lang("Insert empty citation")), pushEntriesAdvanced = new JButton(Globals.lang("Cite special")), focus = new JButton("Focus OO document"), update, insertFullRef = new JButton("Insert reference text"), merge = new JButton(Globals.lang("Merge citations")), settingsB = new JButton(Globals.lang("Settings")), test = new JButton("Test");

    JRadioButton inPar, inText;

    private JPanel settings = null;

    private static String styleFile = null;

    private static OOBibBase ooBase;

    private static JabRefFrame frame;

    private SidePaneManager manager;

    private static OOBibStyle style = null;

    private StyleSelectDialog styleDialog = null;

    private boolean dialogOkPressed = false, autoDetected = false;

    private String sOffice = null;

    private Exception connectException = null;

    public OOTestPanel() {
        ImageIcon connectImage = new ImageIcon(OOTestPanel.class.getResource("/images/connect_no.png"));
        connect = new JButton(connectImage);
        manualConnect = new JButton(connectImage);
        connect.setToolTipText(Globals.lang("Connect"));
        manualConnect.setToolTipText(Globals.lang("Manual connect"));
        selectDocument = new JButton(GUIGlobals.getImage("open"));
        selectDocument.setToolTipText(Globals.lang("Select Writer document"));
        update = new JButton(GUIGlobals.getImage("refresh"));
        update.setToolTipText(Globals.lang("Sync OO bibliography"));
        String defExecutable, defJarsDir;
        if (Globals.ON_WIN) {
            Globals.prefs.putDefaultValue("ooPath", "C:\\Program Files\\OpenOffice.org 3");
            Globals.prefs.putDefaultValue("ooExecutablePath", "C:\\Program Files\\OpenOffice.org 2.3\\program\\soffice.exe");
            Globals.prefs.putDefaultValue("ooJarsPath", "C:\\Program Files\\OpenOffice.org 2.3\\program\\classes");
        } else if (Globals.ON_MAC) {
            Globals.prefs.putDefaultValue("ooExecutablePath", "/Applications/OpenOffice.org.app/Contents/MacOS/soffice.bin");
            Globals.prefs.putDefaultValue("ooPath", "/Applications/OpenOffice.org.app");
            Globals.prefs.putDefaultValue("ooJarsPath", "/Applications/OpenOffice.org.app/Contents/basis-link");
        } else {
            Globals.prefs.putDefaultValue("ooPath", "/opt/openoffice.org3");
            Globals.prefs.putDefaultValue("ooExecutablePath", "/usr/lib/openoffice/program/soffice");
            Globals.prefs.putDefaultValue("ooJarsPath", "/opt/openoffice.org/basis3.0");
        }
        Globals.prefs.putDefaultValue("connectToOO3", Boolean.TRUE);
        Globals.prefs.putDefaultValue("ooStyleFileLastDir", System.getProperty("user.home"));
        Globals.prefs.putDefaultValue("ooInParCitation", true);
        Globals.prefs.putDefaultValue("syncOOWhenCiting", false);
        Globals.prefs.putDefaultValue("showOOPanel", true);
        Globals.prefs.putDefaultValue("useAllOpenBases", true);
        styleFile = Globals.prefs.get("ooBibliographyStyleFile");
    }

    public SidePaneComponent getSidePaneComponent() {
        return comp;
    }

    public void init(JabRefFrame frame, SidePaneManager manager) {
        this.frame = frame;
        this.manager = manager;
        comp = new TestPanel(manager, GUIGlobals.getIconUrl("openoffice"), Globals.lang("OpenOffice"));
        try {
            initPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JMenuItem getMenuItem() {
        if (Globals.prefs.getBoolean("showOOPanel")) manager.show(getName());
        JMenuItem item = new JMenuItem("OpenOffice.org panel", GUIGlobals.getImage("openoffice"));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                manager.show(getName());
            }
        });
        return item;
    }

    public String getShortcutKey() {
        return null;
    }

    private void initPanel() throws Exception {
        Action al = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                connect(true);
            }
        };
        connect.addActionListener(al);
        manualConnect.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                connect(false);
            }
        });
        selectDocument.setToolTipText(Globals.lang("Select which open Writer document to work on"));
        selectDocument.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    ooBase.selectDocument();
                    frame.output(Globals.lang("Connected to document") + ": " + ooBase.getCurrentDocumentTitle());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), Globals.lang("Error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setStyleFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (styleDialog == null) {
                    styleDialog = new StyleSelectDialog(frame, styleFile);
                }
                styleDialog.setVisible(true);
                if (styleDialog.isOkPressed()) {
                    style = styleDialog.getSelectedStyle();
                    if (style != null) {
                        styleFile = style.getFile().getPath();
                        Globals.prefs.put("ooBibliographyStyleFile", styleFile);
                    }
                }
            }
        });
        pushEntries.setToolTipText(Globals.lang("Cite selected entries"));
        pushEntries.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pushEntries(true, true, false);
            }
        });
        pushEntries.setToolTipText(Globals.lang("Cite selected entries with in-text citation"));
        pushEntriesInt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pushEntries(false, true, false);
            }
        });
        pushEntriesEmpty.setToolTipText(Globals.lang("Insert a citation without text (the entry will appear in the reference list)"));
        pushEntriesEmpty.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                pushEntries(false, false, false);
            }
        });
        pushEntriesAdvanced.setToolTipText(Globals.lang("Cite selected entries with extra information"));
        pushEntriesAdvanced.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                pushEntries(false, true, true);
            }
        });
        focus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ooBase.setFocus();
            }
        });
        update.setToolTipText(Globals.lang("Ensure that the bibliography is up-to-date"));
        Action updateAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                try {
                    style.ensureUpToDate();
                    ooBase.updateSortedReferenceMarks();
                    java.util.List<BibtexDatabase> databases = getBaseList();
                    java.util.List<String> unresolvedKeys = ooBase.refreshCiteMarkers(databases, style);
                    ooBase.rebuildBibTextSection(databases, style);
                    if (unresolvedKeys.size() > 0) {
                        JOptionPane.showMessageDialog(frame, Globals.lang("Your OpenOffice document references the BibTeX key '%0', which could not be found in your current database.", unresolvedKeys.get(0)), Globals.lang("Unable to synchronize bibliography"), JOptionPane.ERROR_MESSAGE);
                    }
                } catch (UndefinedCharacterFormatException ex) {
                    reportUndefinedCharacterFormat(ex);
                } catch (UndefinedParagraphFormatException ex) {
                    reportUndefinedParagraphFormat(ex);
                } catch (ConnectionLostException ex) {
                    showConnectionLostErrorMessage();
                } catch (BibtexEntryNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, Globals.lang("Your OpenOffice document references the BibTeX key '%0', which could not be found in your current database.", ex.getBibtexKey()), Globals.lang("Unable to synchronize bibliography"), JOptionPane.ERROR_MESSAGE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };
        update.addActionListener(updateAction);
        insertFullRef.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    insertFullRefs();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        merge.setToolTipText(Globals.lang("Combine pairs of citations that are separated by spaces only"));
        merge.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    ooBase.combineCiteMarkers(getBaseList(), style);
                } catch (UndefinedCharacterFormatException e) {
                    reportUndefinedCharacterFormat(e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        settingsB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                showSettingsPopup();
            }
        });
        test.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    CitationManager cm = new CitationManager(frame, ooBase);
                    cm.showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        selectDocument.setEnabled(false);
        pushEntries.setEnabled(false);
        pushEntriesInt.setEnabled(false);
        pushEntriesEmpty.setEnabled(false);
        pushEntriesAdvanced.setEnabled(false);
        focus.setEnabled(false);
        update.setEnabled(false);
        insertFullRef.setEnabled(false);
        merge.setEnabled(false);
        test.setEnabled(false);
        diag = new JDialog((JFrame) null, "OpenOffice panel", false);
        DefaultFormBuilder b = new DefaultFormBuilder(new FormLayout("fill:pref:grow", "p,p,p,p,p,p,p,p,p,p"));
        DefaultFormBuilder bb = new DefaultFormBuilder(new FormLayout("fill:pref:grow, 1dlu, fill:pref:grow, 1dlu, fill:pref:grow, " + "1dlu, fill:pref:grow", ""));
        bb.append(connect);
        bb.append(manualConnect);
        bb.append(selectDocument);
        bb.append(update);
        b.append(bb.getPanel());
        b.append(setStyleFile);
        b.append(pushEntries);
        b.append(pushEntriesInt);
        b.append(pushEntriesAdvanced);
        b.append(pushEntriesEmpty);
        b.append(merge);
        b.append(settingsB);
        JPanel content = new JPanel();
        comp.setContent(content);
        content.setLayout(new BorderLayout());
        content.add(b.getPanel(), BorderLayout.CENTER);
        System.out.println("Adding shortcuts");
        frame.getTabbedPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(Globals.prefs.getKey("Refresh OO"), "Refresh OO");
        frame.getTabbedPane().getActionMap().put("Refresh OO", updateAction);
    }

    public java.util.List<BibtexDatabase> getBaseList() {
        java.util.List<BibtexDatabase> databases = new ArrayList<BibtexDatabase>();
        if (Globals.prefs.getBoolean("useAllOpenBases")) {
            for (int i = 0; i < frame.baseCount(); i++) databases.add(frame.baseAt(i).database());
        } else databases.add(frame.basePanel().database());
        System.out.println(databases.size());
        return databases;
    }

    public void connect(boolean auto) {
        String unoilDir, ooBaseDirectory;
        if (auto) {
            AutoDetectPaths adp = new AutoDetectPaths(diag);
            if (adp.runAutodetection()) {
                autoDetected = true;
                dialogOkPressed = true;
                diag.dispose();
            } else if (!adp.cancelled()) {
                JOptionPane.showMessageDialog(diag, Globals.lang("Autodetection failed"), Globals.lang("Autodetection failed"), JOptionPane.ERROR_MESSAGE);
            }
            if (!autoDetected) return;
            unoilDir = Globals.prefs.get("ooUnoilPath");
            ooBaseDirectory = Globals.prefs.get("ooJurtPath");
            sOffice = Globals.prefs.get("ooExecutablePath");
        } else {
            showConnectDialog();
            if (!dialogOkPressed) return;
            String ooPath = Globals.prefs.get("ooPath");
            String ooJars = Globals.prefs.get("ooJarsPath");
            sOffice = Globals.prefs.get("ooExecutablePath");
            boolean openOffice3 = true;
            if (Globals.ON_WIN) {
                unoilDir = ooPath + "\\Basis\\program\\classes";
                ooBaseDirectory = ooPath + "\\URE\\java";
                sOffice = ooPath + "\\program\\soffice.exe";
            } else if (Globals.ON_MAC) {
                sOffice = ooPath + "/Contents/MacOS/soffice.bin";
                ooBaseDirectory = ooPath + "/Contents/basis-link/ure-link/share/java";
                unoilDir = ooPath + "/Contents/basis-link/program/classes";
            } else {
                unoilDir = ooJars + "/program/classes";
                ooBaseDirectory = ooJars + "/ure-link/share/java";
            }
        }
        try {
            File[] jarFiles = new File[] { new File(unoilDir, "unoil.jar"), new File(ooBaseDirectory, "jurt.jar"), new File(ooBaseDirectory, "juh.jar"), new File(ooBaseDirectory, "ridl.jar") };
            URL[] jarList = new URL[jarFiles.length];
            for (int i = 0; i < jarList.length; i++) {
                if (!jarFiles[i].exists()) throw new Exception(Globals.lang("File not found") + ": " + jarFiles[i].getPath());
                jarList[i] = jarFiles[i].toURI().toURL();
            }
            addURL(jarList);
            if (styleFile == null) {
                JOptionPane.showMessageDialog(diag, "You must choose a style file before you can connect.", "No style file selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (style == null) readStyleFile();
            final JDialog progDiag = (new AutoDetectPaths(diag)).showProgressDialog(diag, Globals.lang("Connecting"), Globals.lang("Please wait..."), false);
            getWorker().run();
            progDiag.dispose();
            diag.dispose();
            if (ooBase == null) throw connectException;
            if (ooBase.isConnectedToDocument()) frame.output(Globals.lang("Connected to document") + ": " + ooBase.getCurrentDocumentTitle());
            selectDocument.setEnabled(true);
            pushEntries.setEnabled(true);
            pushEntriesInt.setEnabled(true);
            pushEntriesEmpty.setEnabled(true);
            pushEntriesAdvanced.setEnabled(true);
            focus.setEnabled(true);
            update.setEnabled(true);
            insertFullRef.setEnabled(true);
            merge.setEnabled(true);
            test.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, Globals.lang("Could not connect to running OpenOffice.\n" + "Make sure you have installed OpenOffice with Java support.\nIf connecting manually, please verify program and library paths.\n" + "\nError message: " + e.getMessage()));
        }
    }

    public void run() {
        try {
            ooBase = new OOBibBase(sOffice, true);
        } catch (Exception e) {
            ooBase = null;
            connectException = e;
        }
    }

    /**
     * Read the style file. Record the last modified time of the file.
     * @throws Exception
     */
    public void readStyleFile() throws Exception {
        style = new OOBibStyle(new File(styleFile));
    }

    private static final Class[] parameters = new Class[] { URL.class };

    public static void addURL(URL[] u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            for (int i = 0; i < u.length; i++) method.invoke(sysloader, u[i]);
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    public void setStyleFile() {
        JFileChooser jfc = new JFileChooser(System.getProperty("user.home"));
        int answer = jfc.showOpenDialog(null);
        if (answer == JFileChooser.APPROVE_OPTION) {
            styleFile = jfc.getSelectedFile().getPath();
            Globals.prefs.put("ooBibliographyStyleFile", styleFile);
            try {
                style = new OOBibStyle(new FileReader(styleFile));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(diag, e.getMessage());
            }
        }
    }

    public void updateConnectionParams(String ooPath, String ooExec, String ooJars, boolean oo3) {
        Globals.prefs.put("ooPath", ooPath);
        Globals.prefs.put("ooExecutablePath", ooExec);
        Globals.prefs.put("ooJarsPath", ooJars);
        Globals.prefs.putBoolean("connectToOO3", oo3);
    }

    public void showConnectDialog() {
        dialogOkPressed = false;
        final JDialog diag = new JDialog(frame, Globals.lang("Set connection parameters"), true);
        final JTextField ooPath = new JTextField(30);
        JButton browseOOPath = new JButton(Globals.lang("Browse"));
        ooPath.setText(Globals.prefs.get("ooPath"));
        final JTextField ooExec = new JTextField(30);
        JButton browseOOExec = new JButton(Globals.lang("Browse"));
        browseOOExec.addActionListener(new BrowseAction(null, ooExec, false));
        final JTextField ooJars = new JTextField(30);
        JButton browseOOJars = new JButton(Globals.lang("Browse"));
        browseOOJars.addActionListener(new BrowseAction(null, ooJars, true));
        ooExec.setText(Globals.prefs.get("ooExecutablePath"));
        ooJars.setText(Globals.prefs.get("ooJarsPath"));
        DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("left:pref, 4dlu, fill:pref:grow, 4dlu, fill:pref", ""));
        if (Globals.ON_WIN || Globals.ON_MAC) {
            builder.append(Globals.lang("Path to OpenOffice directory"));
            builder.append(ooPath);
            builder.append(browseOOPath);
            builder.nextLine();
        } else {
            builder.append(Globals.lang("Path to OpenOffice executable"));
            builder.append(ooExec);
            builder.append(browseOOExec);
            builder.nextLine();
            builder.append(Globals.lang("Path to OpenOffice library dir"));
            builder.append(ooJars);
            builder.append(browseOOJars);
            builder.nextLine();
        }
        ButtonBarBuilder bb = new ButtonBarBuilder();
        JButton ok = new JButton(Globals.lang("Ok"));
        JButton cancel = new JButton(Globals.lang("Cancel"));
        ActionListener tfListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                updateConnectionParams(ooPath.getText(), ooExec.getText(), ooJars.getText(), true);
                diag.dispose();
            }
        };
        ooPath.addActionListener(tfListener);
        ooExec.addActionListener(tfListener);
        ooJars.addActionListener(tfListener);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                updateConnectionParams(ooPath.getText(), ooExec.getText(), ooJars.getText(), true);
                dialogOkPressed = true;
                diag.dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                diag.dispose();
            }
        });
        bb.addGlue();
        bb.addRelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        bb.addGlue();
        builder.getPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bb.getPanel().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        diag.getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        diag.getContentPane().add(bb.getPanel(), BorderLayout.SOUTH);
        diag.pack();
        diag.setLocationRelativeTo(frame);
        diag.setVisible(true);
    }

    public void pushEntries(boolean inParenthesis, boolean withText, boolean addPageInfo) {
        if (!ooBase.isConnectedToDocument()) {
            JOptionPane.showMessageDialog(frame, Globals.lang("Not connected to any Writer document. Please" + " make sure a document is open, and use the 'Select Writer document' button to connect to it."), Globals.lang("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        String pageInfo = null;
        if (addPageInfo) {
            AdvancedCiteDialog acd = new AdvancedCiteDialog(frame);
            acd.showDialog();
            if (acd.cancelled()) return;
            if (acd.getPageInfo().length() > 0) pageInfo = acd.getPageInfo();
            inParenthesis = acd.isInParenthesisCite();
        }
        BasePanel panel = frame.basePanel();
        final BibtexDatabase database = panel.database();
        if (panel != null) {
            BibtexEntry[] entries = panel.getSelectedEntries();
            if (entries.length > 0) {
                try {
                    ooBase.insertEntry(entries, database, getBaseList(), style, inParenthesis, withText, pageInfo, Globals.prefs.getBoolean("syncOOWhenCiting"));
                } catch (ConnectionLostException ex) {
                    showConnectionLostErrorMessage();
                } catch (UndefinedCharacterFormatException ex) {
                    reportUndefinedCharacterFormat(ex);
                } catch (UndefinedParagraphFormatException ex) {
                    reportUndefinedParagraphFormat(ex);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void showConnectionLostErrorMessage() {
        JOptionPane.showMessageDialog(frame, Globals.lang("Connection to OpenOffice has been lost. " + "Please make sure OpenOffice is running, and try to reconnect."), Globals.lang("Connection lost"), JOptionPane.ERROR_MESSAGE);
    }

    public void insertFullRefs() {
        try {
            BasePanel panel = frame.basePanel();
            final BibtexDatabase database = panel.database();
            Map<BibtexEntry, BibtexDatabase> entries = new LinkedHashMap<BibtexEntry, BibtexDatabase>();
            if (panel != null) {
                BibtexEntry[] e = panel.getSelectedEntries();
                ArrayList<BibtexEntry> el = new ArrayList<BibtexEntry>();
                for (int i = 0; i < e.length; i++) {
                    entries.put(e[i], database);
                }
                ooBase.insertFullReferenceAtViewCursor(entries, style, "Default");
            }
        } catch (UndefinedParagraphFormatException ex) {
            reportUndefinedParagraphFormat(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void reportUndefinedParagraphFormat(UndefinedParagraphFormatException ex) {
        JOptionPane.showMessageDialog(frame, "<html>" + Globals.lang("Your style file specifies the paragraph format '%0', " + "which is undefined in your current OpenOffice document.", ex.getFormatName()) + "<br>" + Globals.lang("The paragraph format is controlled by the property 'ReferenceParagraphFormat' or 'ReferenceHeaderParagraphFormat' in the style file.") + "</html>", Globals.lang(""), JOptionPane.ERROR_MESSAGE);
    }

    private void reportUndefinedCharacterFormat(UndefinedCharacterFormatException ex) {
        JOptionPane.showMessageDialog(frame, "<html>" + Globals.lang("Your style file specifies the character format '%0', " + "which is undefined in your current OpenOffice document.", ex.getFormatName()) + "<br>" + Globals.lang("The character format is controlled by the citation property 'CitationCharacterFormat' in the style file.") + "</html>", Globals.lang(""), JOptionPane.ERROR_MESSAGE);
    }

    public void insertUsingBST() {
        try {
            BasePanel panel = frame.basePanel();
            final BibtexDatabase database = panel.database();
            if (panel != null) {
                BibtexEntry[] entries = panel.getSelectedEntries();
                ArrayList<BibtexEntry> el = new ArrayList<BibtexEntry>();
                for (int i = 0; i < entries.length; i++) {
                    el.add(entries[i]);
                }
                BstWrapper wrapper = new BstWrapper();
                wrapper.loadBstFile(new File("/home/usr/share/texmf-tetex/bibtex/bst/ams/amsalpha.bst"));
                Map<String, String> result = wrapper.processEntries(el, database);
                for (String key : result.keySet()) {
                    System.out.println("Key: " + key);
                    System.out.println("Entry: " + result.get(key));
                    ooBase.insertMarkedUpTextAtViewCursor(result.get(key), "Default");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void showSettingsPopup() {
        JPopupMenu menu = new JPopupMenu();
        final JCheckBoxMenuItem autoSync = new JCheckBoxMenuItem(Globals.lang("Automatically sync bibliography when inserting citations"), Globals.prefs.getBoolean("syncOOWhenCiting"));
        final JRadioButtonMenuItem useActiveBase = new JRadioButtonMenuItem(Globals.lang("Look up BibTeX entries in the active tab only"));
        final JRadioButtonMenuItem useAllBases = new JRadioButtonMenuItem(Globals.lang("Look up BibTeX entries in all open databases"));
        ButtonGroup bg = new ButtonGroup();
        bg.add(useActiveBase);
        bg.add(useAllBases);
        if (Globals.prefs.getBoolean("useAllOpenBases")) useAllBases.setSelected(true); else useActiveBase.setSelected(true);
        autoSync.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                Globals.prefs.putBoolean("syncOOWhenCiting", autoSync.isSelected());
            }
        });
        useAllBases.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                Globals.prefs.putBoolean("useAllOpenBases", useAllBases.isSelected());
            }
        });
        useActiveBase.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                Globals.prefs.putBoolean("useAllOpenBases", !useActiveBase.isSelected());
            }
        });
        menu.add(autoSync);
        menu.addSeparator();
        menu.add(useActiveBase);
        menu.add(useAllBases);
        menu.show(settingsB, 0, settingsB.getHeight());
    }

    public void pushEntries(boolean inParenthesis, BibtexEntry[] entries) {
        final BibtexDatabase database = frame.basePanel().database();
        if (entries.length > 0) {
            String pageInfo = null;
            AdvancedCiteDialog acd = new AdvancedCiteDialog(frame);
            acd.showDialog();
            if (acd.cancelled()) return;
            if (acd.getPageInfo().length() > 0) pageInfo = acd.getPageInfo();
            inParenthesis = acd.isInParenthesisCite();
            try {
                ooBase.insertEntry(entries, database, getBaseList(), style, inParenthesis, true, pageInfo, Globals.prefs.getBoolean("syncOOWhenCiting"));
            } catch (ConnectionLostException ex) {
                showConnectionLostErrorMessage();
            } catch (UndefinedCharacterFormatException ex) {
                reportUndefinedCharacterFormat(ex);
            } catch (UndefinedParagraphFormatException ex) {
                reportUndefinedParagraphFormat(ex);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getName() {
        return "Push to OpenOffice";
    }

    public String getApplicationName() {
        return "OpenOffice";
    }

    public String getTooltip() {
        return "Push selection to OpenOffice";
    }

    public Icon getIcon() {
        return GUIGlobals.getImage("openoffice");
    }

    public String getKeyStrokeName() {
        return null;
    }

    public JPanel getSettingsPanel() {
        return null;
    }

    private void initSettingsPanel() {
        boolean inParen = Globals.prefs.getBoolean("ooInParCitation");
        inPar = new JRadioButton(Globals.lang("Use in-parenthesis citation"), inParen);
        inText = new JRadioButton(Globals.lang("Use in-text citation"), !inParen);
        ButtonGroup bg = new ButtonGroup();
        bg.add(inPar);
        bg.add(inText);
        settings = new JPanel();
        settings.setLayout(new BorderLayout());
        settings.add(inPar, BorderLayout.NORTH);
        settings.add(inText, BorderLayout.SOUTH);
    }

    public void storeSettings() {
        Globals.prefs.putBoolean("ooInParCitation", inPar.isSelected());
    }

    public void pushEntries(BibtexDatabase bibtexDatabase, BibtexEntry[] entries, String s, MetaData metaData) {
        if (ooBase == null) {
            connect(true);
        }
        if (ooBase != null) {
            pushEntries(Globals.prefs.getBoolean("ooInParCitation"), entries);
        }
    }

    public void operationCompleted(BasePanel basePanel) {
    }

    public boolean requiresBibtexKeys() {
        return true;
    }

    class TestPanel extends SidePaneComponent {

        public TestPanel(SidePaneManager sidePaneManager, URL url, String s) {
            super(sidePaneManager, url, s);
        }

        public String getName() {
            return OOTestPanel.this.getName();
        }

        @Override
        public void componentClosing() {
            Globals.prefs.putBoolean("showOOPanel", false);
        }

        @Override
        public void componentOpening() {
            Globals.prefs.putBoolean("showOOPanel", true);
        }
    }
}
