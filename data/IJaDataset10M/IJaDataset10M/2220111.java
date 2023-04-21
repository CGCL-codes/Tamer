package hu.javaspellcheck;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Utility class to extract words from the sources and create a text file 
 * to run through any spell checker.
 * @author akarnokd, 2011.10.17.
 */
public class SpellcheckSource extends JFrame {

    /** */
    private static final long serialVersionUID = 7394919066832498515L;

    /** The word-file-line matrix. */
    static class WordMatrix {

        /** Map by word to file to line numbers. */
        public final Map<String, Map<String, List<Integer>>> byWords = Maps.newHashMap();

        /** Map by file to word to line numbers. */
        public final Map<String, Map<String, List<Integer>>> byFile = Maps.newHashMap();

        /**
		 * Add a triplet to the matrix.
		 * @param file the file
		 * @param word the word
		 * @param line the line number (1 based)
		 */
        public void add(String file, String word, int line) {
            Map<String, List<Integer>> pi = byWords.get(word);
            if (pi == null) {
                pi = Maps.newHashMap();
                byWords.put(word, pi);
            }
            List<Integer> is = pi.get(file);
            if (is == null) {
                is = Lists.newArrayList();
                pi.put(file, is);
            }
            is.add(line);
            Map<String, List<Integer>> wi = byFile.get(file);
            if (wi == null) {
                wi = Maps.newHashMap();
                byFile.put(file, wi);
            }
            List<Integer> is2 = wi.get(word);
            if (is2 == null) {
                is2 = Lists.newArrayList();
                wi.put(word, is2);
            }
            is2.add(line);
        }

        /** Clear contents. */
        public void clear() {
            byFile.clear();
            byWords.clear();
        }

        /**
		 * Add all elements of the other.
		 * @param other the other matrix
		 */
        public void add(WordMatrix other) {
            for (Map.Entry<String, Map<String, List<Integer>>> e0 : other.byFile.entrySet()) {
                for (Map.Entry<String, List<Integer>> e1 : e0.getValue().entrySet()) {
                    for (Integer i : e1.getValue()) {
                        add(e0.getKey(), e1.getKey(), i);
                    }
                }
            }
        }
    }

    /** The thread pool for parallel evaluations. */
    final ExecutorService exec;

    /** The check list. */
    final Set<String> check = Sets.newHashSet();

    /** The ignore list. */
    final Set<String> ignore = Sets.newHashSet();

    /** The in progress count. */
    final AtomicInteger wip = new AtomicInteger();

    /** The word matrix. */
    final WordMatrix wordmatrix = new WordMatrix();

    /** The last directory. */
    File lastDir = new File(".");

    /** The scan menu. */
    JMenuItem scan;

    /** The table for files. */
    JTable byFileMain;

    /** The table for file details. */
    JTable byFileSlave;

    /** The table word words. */
    JTable byWordMain;

    /** The table for word details. */
    JTable byWordSlave;

    /** The model for files. */
    AbstractTableModel byFileMainModel;

    /** The model for file details. */
    AbstractTableModel byFileSlaveModel;

    /** The model word words. */
    AbstractTableModel byWordMainModel;

    /** The model for word details. */
    AbstractTableModel byWordSlaveModel;

    /** The minimum word length. */
    volatile int minWordLength = 3;

    /** The main table entry. */
    static class MainEntry {

        /** The key. */
        String key;

        /** Number of slaves. */
        int slaveCount;

        /** Number of total lines. */
        int lineCount;
    }

    /**
	 * The slave entries.
	 * @author akarnokd, 2011.10.17.
	 */
    static class SlaveEntry {

        /** The slave key. */
        String key;

        /** The slave line. */
        int line;
    }

    /** The file list. */
    List<MainEntry> byFileList = Lists.newArrayList();

    /** The word list. */
    List<MainEntry> byWordList = Lists.newArrayList();

    /** The selected slave list. */
    List<SlaveEntry> byFileSlaveList = Lists.newArrayList();

    /** The selected slave list. */
    List<SlaveEntry> byWordSlaveList = Lists.newArrayList();

    /** The config file. */
    final File configFile = new File("spellchecksource-config.xml");

    /**
	 * Creates the main GUI.
	 */
    public SpellcheckSource() {
        super("Spellcheck Source");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ((ThreadPoolExecutor) exec).setKeepAliveTime(1, TimeUnit.SECONDS);
        ((ThreadPoolExecutor) exec).allowCoreThreadTimeOut(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                doClose();
            }
        });
        JMenuBar mb = new JMenuBar();
        JMenu action = new JMenu("Action");
        scan = new JMenuItem("Scan...");
        scan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doSelectDir();
            }
        });
        JMenuItem wordLength = new JMenuItem("Minimum word length...");
        wordLength.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog("Minimum word length:", "" + minWordLength);
                if (s != null && s.length() > 0) {
                    minWordLength = Integer.parseInt(s);
                }
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exec.shutdown();
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(SpellcheckSource.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        action.add(scan);
        action.add(wordLength);
        action.addSeparator();
        action.add(exit);
        mb.add(action);
        setJMenuBar(mb);
        JTabbedPane tabs = new JTabbedPane();
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(tabs, BorderLayout.CENTER);
        byFileMainModel = createFileMainModel();
        byFileSlaveModel = createFileSlaveModel();
        byWordMainModel = createWordMainModel();
        byWordSlaveModel = createWordSlaveModel();
        byFileMain = new JTable(byFileMainModel);
        byFileSlave = new JTable(byFileSlaveModel);
        byWordMain = new JTable(byWordMainModel);
        byWordSlave = new JTable(byWordSlaveModel);
        byFileMain.setAutoCreateRowSorter(true);
        byFileMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        byFileMain.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                doFileSlave();
            }
        });
        byWordMain.setAutoCreateRowSorter(true);
        byWordMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        byWordMain.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                doWordSlave();
            }
        });
        byFileSlave.setAutoCreateRowSorter(true);
        byWordSlave.setAutoCreateRowSorter(true);
        byFileMain.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        byFileMain.setRowHeight(20);
        byWordMain.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        byWordMain.setRowHeight(20);
        byFileSlave.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        byFileSlave.setRowHeight(20);
        byWordSlave.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        byWordSlave.setRowHeight(20);
        tabs.addTab("By files", new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(byFileMain), new JScrollPane(byFileSlave)));
        tabs.addTab("By words", new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(byWordMain), new JScrollPane(byWordSlave)));
        JPopupMenu wordPopup = new JPopupMenu();
        JMenuItem addToIgnore = new JMenuItem("Add to ignore");
        addToIgnore.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doAddToIgnore();
            }

            ;
        });
        wordPopup.add(addToIgnore);
        byWordMain.addMouseListener(GUIUtils.getMousePopupAdapter(byWordMain, wordPopup));
        init();
    }

    /** @return create the model. */
    AbstractTableModel createFileMainModel() {
        return new AbstractTableModel() {

            /** */
            private static final long serialVersionUID = -5136392906597494880L;

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                MainEntry e = byFileList.get(rowIndex);
                switch(columnIndex) {
                    case 0:
                        return e.key;
                    case 1:
                        return e.slaveCount;
                    case 2:
                        return e.lineCount;
                    default:
                        return null;
                }
            }

            @Override
            public int getRowCount() {
                return byFileList.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class;
                }
                return Integer.class;
            }

            @Override
            public String getColumnName(int column) {
                return Arrays.asList("File", "Words", "Lines").get(column);
            }
        };
    }

    /** @return create the model. */
    AbstractTableModel createFileSlaveModel() {
        return new AbstractTableModel() {

            /** */
            private static final long serialVersionUID = -5136392906597494880L;

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                SlaveEntry e = byFileSlaveList.get(rowIndex);
                switch(columnIndex) {
                    case 0:
                        return e.key;
                    case 1:
                        return e.line;
                    default:
                        return null;
                }
            }

            @Override
            public int getRowCount() {
                return byFileSlaveList.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class;
                }
                return Integer.class;
            }

            @Override
            public String getColumnName(int column) {
                return Arrays.asList("Word", "Line").get(column);
            }
        };
    }

    /** @return create the model. */
    AbstractTableModel createWordMainModel() {
        return new AbstractTableModel() {

            /** */
            private static final long serialVersionUID = -5136392906597494880L;

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                MainEntry e = byWordList.get(rowIndex);
                switch(columnIndex) {
                    case 0:
                        return e.key;
                    case 1:
                        return e.slaveCount;
                    case 2:
                        return e.lineCount;
                    default:
                        return null;
                }
            }

            @Override
            public int getRowCount() {
                return byWordList.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class;
                }
                return Integer.class;
            }

            @Override
            public String getColumnName(int column) {
                return Arrays.asList("Word", "Files", "Lines").get(column);
            }
        };
    }

    /** @return create the model. */
    AbstractTableModel createWordSlaveModel() {
        return new AbstractTableModel() {

            /** */
            private static final long serialVersionUID = -5136392906597494880L;

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                SlaveEntry e = byWordSlaveList.get(rowIndex);
                switch(columnIndex) {
                    case 0:
                        return e.key;
                    case 1:
                        return e.line;
                    default:
                        return null;
                }
            }

            @Override
            public int getRowCount() {
                return byWordSlaveList.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class;
                }
                return Integer.class;
            }

            @Override
            public String getColumnName(int column) {
                return Arrays.asList("File", "Line").get(column);
            }
        };
    }

    /**
	 * Start the first actions.
	 */
    void init() {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                loadDictionary();
                return null;
            }

            @Override
            protected void done() {
                loadConfig();
                doSelectDir();
            }
        }.execute();
    }

    /**
	 * Load the dictionary.
	 * @throws IOException on error
	 */
    void loadDictionary() throws IOException {
        check.clear();
        for (String s : Files.readAllLines(Paths.get("dict.txt"), Charset.defaultCharset())) {
            check.add(s.toLowerCase());
        }
        Path da = Paths.get("dict-ignore.txt");
        if (Files.exists(da)) {
            ignore.addAll(Files.readAllLines(da, Charset.defaultCharset()));
        }
    }

    /**
	 * Scan the specified directory recursively.
	 * @param path the path
	 */
    void scanDir(Path path) {
        wordmatrix.clear();
        wip.incrementAndGet();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(final Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".java") || file.toString().endsWith(".xml")) {
                        wip.incrementAndGet();
                        exec.execute(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    parseFile(file);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                } finally {
                                    doneWip();
                                }
                            }
                        });
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            doneWip();
        }
    }

    /** When all tasks completed. */
    void doneWip() {
        if (wip.decrementAndGet() <= 0) {
            scan.setEnabled(true);
            updateModels();
        }
    }

    /**
	 * Parse the given text file.
	 * @param file the file
	 * @throws IOException on error
	 */
    void parseFile(Path file) throws IOException {
        WordMatrix wm = new WordMatrix();
        int i = 1;
        String base = Paths.get(".").toAbsolutePath().toString();
        for (String line : Files.readAllLines(file, Charset.forName("UTF-8"))) {
            String[] ws = line.split("\\b+|_|\\d+");
            for (String w : ws) {
                List<String> sw = camelCaseSplit(w);
                for (String s : sw) {
                    if (s.length() > minWordLength && Character.isAlphabetic(s.charAt(0))) {
                        if (!check.contains(s.toLowerCase()) && !ignore.contains(s.toLowerCase())) {
                            wm.add(uncommonPart(base, file.toAbsolutePath().toString()), s, i);
                        }
                    }
                }
            }
            i++;
        }
        synchronized (wordmatrix) {
            wordmatrix.add(wm);
        }
    }

    /**
	 * Returns the common prefix for the given two strings.
	 * @param first the first string
	 * @param second the second string
	 * @return the common prefix, may be empty if 
	 */
    String commonPart(String first, String second) {
        int len = Math.min(first.length(), second.length());
        for (int i = 0; i < len; i++) {
            if (first.charAt(i) != second.charAt(i)) {
                return first.substring(0, i);
            }
        }
        return first.substring(0, len);
    }

    /**
	 * Removes the common prefix from the second string based on the first string.
	 * @param first the first string
	 * @param second the second string
	 * @return the common prefix, may be empty if 
	 */
    String uncommonPart(String first, String second) {
        int len = Math.min(first.length(), second.length());
        for (int i = 0; i < len; i++) {
            if (first.charAt(i) != second.charAt(i)) {
                return second.substring(i);
            }
        }
        return second;
    }

    /**
	 * Split the string along camelcase and allcaps boundaries.
	 * @param word the word to split
	 * @return the words
	 */
    static List<String> camelCaseSplit(String word) {
        List<String> result = Lists.newArrayList();
        char[] chars = word.toCharArray();
        int idx = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            char c1 = chars[i];
            char c2 = chars[i + 1];
            if (Character.isLowerCase(c1) && Character.isUpperCase(c2)) {
                result.add(word.substring(idx, i + 1));
                idx = i + 1;
            } else if (Character.isUpperCase(c1) && Character.isLowerCase(c2) && i > 0) {
                result.add(word.substring(idx, i));
                idx = i;
            }
        }
        result.add(word.substring(idx));
        return result;
    }

    /**
	 * The main program.
	 * @param args no arguments
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                SpellcheckSource f = new SpellcheckSource();
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    /**
	 * Select the directory to scan.
	 */
    void doSelectDir() {
        JFileChooser fc = new JFileChooser(lastDir);
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lastDir = fc.getSelectedFile();
            scan.setEnabled(false);
            scanDir(fc.getSelectedFile().toPath());
        }
    }

    /** Update the on-screen models. */
    void updateModels() {
        byFileList.clear();
        byWordList.clear();
        for (Map.Entry<String, Map<String, List<Integer>>> e0 : wordmatrix.byFile.entrySet()) {
            MainEntry me = new MainEntry();
            me.key = e0.getKey().toString();
            for (Map.Entry<String, List<Integer>> e1 : e0.getValue().entrySet()) {
                me.slaveCount++;
                me.lineCount += e1.getValue().size();
            }
            byFileList.add(me);
        }
        for (Map.Entry<String, Map<String, List<Integer>>> e0 : wordmatrix.byWords.entrySet()) {
            MainEntry me = new MainEntry();
            me.key = e0.getKey();
            for (Map.Entry<String, List<Integer>> e1 : e0.getValue().entrySet()) {
                me.slaveCount++;
                me.lineCount += e1.getValue().size();
            }
            byWordList.add(me);
        }
        byWordMainModel.fireTableDataChanged();
        byFileMainModel.fireTableDataChanged();
        GUIUtils.autoResizeColWidth(byWordMain, byWordMainModel);
        GUIUtils.autoResizeColWidth(byFileMain, byFileMainModel);
    }

    /** If the file selection changes. */
    void doFileSlave() {
        int idx = byFileMain.getSelectedRow();
        byFileSlaveList.clear();
        if (idx >= 0) {
            idx = byFileMain.convertRowIndexToModel(idx);
            MainEntry me = byFileList.get(idx);
            for (Map.Entry<String, List<Integer>> e : wordmatrix.byFile.get(me.key).entrySet()) {
                for (Integer i : e.getValue()) {
                    SlaveEntry se = new SlaveEntry();
                    se.key = e.getKey();
                    se.line = i;
                    byFileSlaveList.add(se);
                }
            }
        }
        byFileSlaveModel.fireTableDataChanged();
        GUIUtils.autoResizeColWidth(byFileSlave, byFileSlaveModel);
    }

    /** If the word selection changes. */
    void doWordSlave() {
        int idx = byWordMain.getSelectedRow();
        byWordSlaveList.clear();
        if (idx >= 0) {
            idx = byWordMain.convertRowIndexToModel(idx);
            MainEntry me = byWordList.get(idx);
            for (Map.Entry<String, List<Integer>> e : wordmatrix.byWords.get(me.key).entrySet()) {
                for (Integer i : e.getValue()) {
                    SlaveEntry se = new SlaveEntry();
                    se.key = e.getKey().toString();
                    se.line = i;
                    byWordSlaveList.add(se);
                }
            }
        }
        byWordSlaveModel.fireTableDataChanged();
        GUIUtils.autoResizeColWidth(byWordSlave, byWordSlaveModel);
    }

    /**
	 * Apply the configuration settings from the properties.
	 * @param props the properties.
	 */
    void applyConfig(Properties props) {
        int x = Integer.parseInt(props.getProperty("main-x"));
        int y = Integer.parseInt(props.getProperty("main-y"));
        int w = Integer.parseInt(props.getProperty("main-w"));
        int h = Integer.parseInt(props.getProperty("main-h"));
        int s = Integer.parseInt(props.getProperty("main-s"));
        setBounds(x, y, w, h);
        setExtendedState(s);
        lastDir = new File(props.getProperty("last-dir"));
    }

    /**
	 * Store the configuration settings into the properties.
	 * @param props the properties
	 */
    void storeConfig(Properties props) {
        props.setProperty("main-x", "" + getX());
        props.setProperty("main-y", "" + getY());
        props.setProperty("main-w", "" + getWidth());
        props.setProperty("main-h", "" + getHeight());
        props.setProperty("main-s", "" + getExtendedState());
        props.setProperty("last-dir", lastDir.getAbsolutePath());
        Path da = Paths.get("dict-ignore.txt");
        try {
            Files.write(da, ignore, Charset.defaultCharset());
        } catch (IOException ex) {
            ex.toString();
        }
    }

    /** Load the configuration. */
    void loadConfig() {
        if (configFile.canRead()) {
            try {
                Properties props = new Properties();
                FileInputStream fin = new FileInputStream(configFile);
                try {
                    props.loadFromXML(fin);
                    applyConfig(props);
                } finally {
                    fin.close();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /** Save the configuration. */
    void saveConfig() {
        try {
            Properties props = new Properties();
            FileOutputStream fout = new FileOutputStream(configFile);
            try {
                storeConfig(props);
                props.storeToXML(fout, "SpellcheckSource config file");
            } finally {
                fout.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /** Close action. */
    void doClose() {
        saveConfig();
    }

    /** Add selected entries to ignore list. */
    void doAddToIgnore() {
        int[] sel = byWordMain.getSelectedRows();
        for (int i = 0; i < sel.length; i++) {
            int j = byWordMain.convertRowIndexToModel(sel[i]);
            MainEntry me = byWordList.get(j);
            ignore.add(me.key.toLowerCase());
            byWordList.remove(j);
        }
        byWordMainModel.fireTableDataChanged();
    }
}
