package se.kth.cid.imsevimse.app;

import se.kth.cid.metadata.*;
import se.kth.cid.metadata.local.*;
import se.kth.cid.metadata.xml.*;
import se.kth.cid.imsevimse.panels.*;
import se.kth.cid.imsevimse.components.*;
import se.kth.cid.xml.*;
import se.kth.cid.util.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class ImseVimse extends JFrame {

    JTabbedPane tabPane;

    JFileChooser chooser;

    static int docCount = 0;

    class Document implements MetaDataEditListener {

        File file;

        String title;

        PanelMetaDataDisplayer displayer;

        boolean edited = false;

        Document(File file, PanelMetaDataDisplayer displayer) {
            this.file = file;
            this.displayer = displayer;
            if (file == null) title = "Doc " + docCount++; else title = file.getName();
            displayer.addMetaDataEditListener(this);
        }

        public void fieldEdited(MetaDataEditEvent e) {
            edited = true;
        }

        boolean isEdited() {
            return edited;
        }

        PanelMetaDataDisplayer getDisplayer() {
            return displayer;
        }

        File getFile() {
            return file;
        }

        String getTitle() {
            return title;
        }

        void setFile(File f) {
            file = f;
            title = f.getName();
        }

        void saveTo(Writer w) {
            displayer.storeMetaData();
            XmlPrinter p = new XmlPrinter();
            XmlDocument xmldoc = new XmlDocument();
            try {
                xmldoc.setRoot(XmlMetaDataHandler.buildXmlTree(displayer.getMetaData()));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            p.setStandalone(true);
            p.print(xmldoc, w, "ISO-8859-1");
        }

        boolean save() {
            displayer.storeMetaData();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                XmlPrinter p = new XmlPrinter();
                XmlDocument xmldoc = new XmlDocument();
                xmldoc.setRoot(XmlMetaDataHandler.buildXmlTree(displayer.getMetaData()));
                p.setStandalone(true);
                p.print(xmldoc, fos, "ISO-8859-1");
                fos.close();
                edited = false;
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        }

        void close() {
            displayer.detach();
            displayer = null;
        }
    }

    Vector docs;

    public static void main(String[] argv) throws Exception {
        if (argv.length == 1 && argv[0].equals("--help")) {
            Tracer.trace("Usage: ImseVimse [File.xml ...]", Tracer.ERROR);
            System.exit(-1);
        }
        Tracer.setLogLevel(Tracer.WARNING);
        Font font = new Font("Lucida Sans", Font.BOLD, 10);
        Font noboldfont = font.deriveFont(Font.PLAIN);
        UIManager.put("Button.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("TextField.font", noboldfont);
        UIManager.put("TextArea.font", noboldfont);
        UIManager.put("TitledBorder.font", font);
        ImseVimse iv = new ImseVimse();
        for (int i = 0; i < argv.length; i++) iv.editFile(new File(argv[i]));
    }

    public ImseVimse() {
        super("ImseVimse - The IMS Editor");
        final LocaleManager lm = LocaleManager.getLocaleManager();
        lm.addLocale(new Locale("en", "", ""));
        lm.addLocale(new Locale("sv", "", ""));
        lm.addLocale(new Locale("fr", "", ""));
        lm.addLocale(new Locale("de", "", ""));
        lm.addLocale(new Locale("it", "", ""));
        tabPane = new JTabbedPane(JTabbedPane.BOTTOM);
        docs = new Vector();
        getContentPane().add(tabPane, BorderLayout.CENTER);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem newf = new JMenuItem("New");
        fileMenu.add(newf);
        newf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editDocument(new Document(null, new PanelMetaDataDisplayer(new LocalMetaData(), true)));
            }
        });
        JMenuItem open = new JMenuItem("Open...");
        fileMenu.add(open);
        open.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = getChooser(null);
                int returnVal = chooser.showOpenDialog(ImseVimse.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) editFile(chooser.getSelectedFile());
            }
        });
        JMenuItem close = new JMenuItem("Close");
        fileMenu.add(close);
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close((Document) docs.get(tabPane.getSelectedIndex()));
            }
        });
        fileMenu.addSeparator();
        JMenuItem save = new JMenuItem("Save");
        fileMenu.add(save);
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Document doc = (Document) docs.get(tabPane.getSelectedIndex());
                if (doc.getFile() != null) doc.save(); else saveAs(doc);
            }
        });
        JMenuItem saveAs = new JMenuItem("Save as...");
        fileMenu.add(saveAs);
        saveAs.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Document doc = (Document) docs.get(tabPane.getSelectedIndex());
                saveAs(doc);
            }
        });
        fileMenu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(exit);
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        JMenu toolsMenu = new JMenu("Tools");
        menuBar.add(toolsMenu);
        JMenuItem languageTool = new JMenuItem("Select Languages...");
        toolsMenu.add(languageTool);
        languageTool.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lm.displayEditor(ImseVimse.this);
            }
        });
        JMenuItem previewTool = new JMenuItem("Preview XML...");
        toolsMenu.add(previewTool);
        previewTool.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Document doc = (Document) docs.get(tabPane.getSelectedIndex());
                StringWriter w = new StringWriter();
                doc.saveTo(w);
                String s = w.toString();
                JTextArea a = new JTextArea(s);
                a.setEditable(false);
                JScrollPane p = new JScrollPane(a);
                JFrame f = new JFrame(doc.getTitle());
                f.getContentPane().add(p, BorderLayout.CENTER);
                f.setSize(400, 400);
                f.setLocation(100, 100);
                f.show();
            }
        });
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        setSize(400, 400);
        setLocation(100, 100);
        show();
    }

    boolean save(Document doc) {
        if (doc.getFile() == null) return saveAs(doc); else return doc.save();
    }

    boolean saveAs(Document doc) {
        JFileChooser chooser = getChooser(doc.getFile());
        int returnVal = chooser.showSaveDialog(ImseVimse.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            doc.setFile(chooser.getSelectedFile());
            if (doc.save()) {
                tabPane.setTitleAt(docs.indexOf(doc), doc.getTitle());
                return true;
            }
        }
        return false;
    }

    boolean close(Document doc) {
        if (doc.isEdited()) {
            String opts[] = { "Save", "Discard changes", "Cancel" };
            int ans = JOptionPane.showOptionDialog(this, "The document \"" + doc.getTitle() + "\"\n has unsaved changes. Save now?", "Save document?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opts, opts[0]);
            if (ans == 0) {
                if (!save(doc)) return false;
            } else if (ans == 2) return false;
        }
        doc.close();
        int i = docs.indexOf(doc);
        docs.removeElementAt(i);
        tabPane.removeTabAt(i);
        return true;
    }

    void exit() {
        int ndoc = docs.size();
        for (int i = 0; i < ndoc; i++) {
            Document doc = (Document) docs.get(0);
            if (!close(doc)) return;
        }
        System.exit(0);
    }

    JFileChooser getChooser(File file) {
        if (chooser == null) {
            chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

                public boolean accept(File f) {
                    if (f.isDirectory()) return true;
                    return (f.getName().lastIndexOf(".xml") == f.getName().length() - 4);
                }

                public String getDescription() {
                    return "XML files (*.xml)";
                }
            });
        }
        chooser.setSelectedFile(file);
        return chooser;
    }

    public void editFile(File file) {
        XmlLoader loader = new XmlLoader(null);
        XmlDocument doc = null;
        try {
            doc = loader.parse(file.toURL());
        } catch (XmlLoaderException e) {
            Tracer.debug("Parse Error:\n " + e.getMessage());
            return;
        } catch (MalformedURLException e) {
            Tracer.debug("Strange file:\n " + e.getMessage());
            return;
        }
        MetaData md = new LocalMetaData();
        XmlElement mdEl = doc.getRoot();
        if (mdEl == null) {
            Tracer.debug("Empty XML file");
            return;
        }
        try {
            XmlMetaDataHandler.load(md, mdEl);
        } catch (XmlStructureException e) {
            Tracer.debug("XML Error: " + e.getMessage());
            return;
        }
        PanelMetaDataDisplayer disp = new PanelMetaDataDisplayer(md, true);
        editDocument(new Document(file, disp));
    }

    void editDocument(Document doc) {
        docs.add(doc);
        tabPane.addTab(doc.getTitle(), doc.getDisplayer());
        tabPane.setSelectedIndex(tabPane.getTabCount() - 1);
    }
}
