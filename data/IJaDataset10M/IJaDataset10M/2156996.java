package com.hsbc.hbfr.ccf.at.logreader.ui;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.log4j.Logger;
import com.hsbc.hbfr.ccf.at.logreader.model.ListLogProvider;
import com.hsbc.hbfr.ccf.at.logreader.model.LogEvent;
import com.hsbc.hbfr.ccf.at.logreader.model.LogEventRecognizer;
import com.hsbc.hbfr.ccf.at.logreader.model.LogProvider;
import com.hsbc.hbfr.ccf.at.logreader.model.ProvidedEvent;
import com.hsbc.hbfr.ccf.at.logreader.model.SimpleLogEventRecognizer;
import com.hsbc.hbfr.ccf.at.logreader.predicate.NegatableDateAfterPredicate;
import com.hsbc.hbfr.ccf.at.logreader.predicate.NegatableStringLikePredicate;

public class LogViewerFormSwing extends LogViewerForm {

    private static final Logger logger = Logger.getLogger("INFRA." + LogViewerFormSwing.class.getName());

    private static final JFileChooser fc = new JFileChooser();

    private static final String PREFERENCE_CONFIGURATION_NEXT = "configuration.next";

    private static final int SNAPSHOT_LENGTH = 5000;

    private static final WindowListener lastWindowCheckListener = new WindowAdapter() {

        /**
         * nettoie tout pour eviter les memory leaks
         *
         * @param e windowEvent
         */
        @Override
        public void windowClosed(WindowEvent e) {
            LogViewerForm closed = globalOpenViewers.get(e.getWindow());
            closed.cleanup();
        }

        /**
         * Confirmation avant fermeture pour la sauvegarde des preferences
         *
         * @param e windowEvent
         */
        @Override
        public void windowClosing(WindowEvent e) {
            if (globalOpenViewers.size() == 1) {
                LogViewerFormSwing closing = (LogViewerFormSwing) globalOpenViewers.get(e.getWindow());
                closing.exitAction.actionPerformed(new ActionEvent(e.getWindow(), 0, "Exit"));
                e.getWindow().setVisible(true);
            } else {
                e.getWindow().dispose();
            }
        }
    };

    private final JProgressBar progressBar = new JProgressBar();

    private final Action exitAction = new ExitAction(this);

    private final JMenu menuCouleurs = new JMenu(UIConstants.MENU_COLORS);

    private ArrayList<JPanel> filters = new ArrayList<JPanel>();

    private JTable table1;

    private JPanel mainPanel;

    private JTextArea textArea1 = new JTextArea();

    private JMenu menuProviders = new JMenu(UIConstants.MENU_PROVIDERS);

    private MemInspector memInspector;

    private JLabel labelEventCount = new JLabel();

    private JPanel panelFiltres;

    @Override
    protected void createTableModel() {
        table1 = new JTable();
        table1.setModel(new FilteringLogTableModel());
    }

    @Override
    protected Object getWindowObject() {
        return getFrame();
    }

    public LogViewerFormSwing() {
        super();
        initGUI();
        initEvents();
    }

    @Override
    protected File getCurrentDirectory() {
        return fc.getCurrentDirectory();
    }

    @Override
    protected AbstractColorManager getColorManager() {
        return ProviderColorManager.instance();
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        JFrame frame = getFrame();
        mainPanel = null;
        menuProviders = null;
        getLogTableModel().setEvents(new ArrayList<LogEvent>(), getParts(), createProgress(0));
        memInspector.stop();
        frame.removeAll();
        if (logger.isDebugEnabled()) {
            logger.debug("removed from globalOpenViewers(new size =" + globalOpenViewers.size() + ")");
        }
    }

    @Override
    protected void initGUI() {
        DefaultTableCellRenderer stringRenderer = (DefaultTableCellRenderer) table1.getDefaultRenderer(String.class);
        table1.setDefaultRenderer(String.class, new ProvidedEventCellRenderer(this, stringRenderer));
        table1.setDefaultRenderer(Date.class, new ProvidedEventCellRenderer(this, stringRenderer) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Date d = (Date) value;
                return super.getTableCellRendererComponent(table, df.format(d), isSelected, hasFocus, row, column);
            }
        });
        table1.setDefaultRenderer(Object.class, new ProvidedEventCellRenderer(this, (DefaultTableCellRenderer) table1.getDefaultRenderer(Object.class)));
        panelFiltres = new JPanel();
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelFiltres, BorderLayout.NORTH);
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(false);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(table1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), new JScrollPane(textArea1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        split.setOneTouchExpandable(true);
        mainPanel.add(split, BorderLayout.CENTER);
    }

    private class TextAreaUpdater implements ListSelectionListener {

        static final int WARN_TXT_AREA_SIZE = 5000000;

        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel selectionModel = table1.getSelectionModel();
            int min = selectionModel.getMinSelectionIndex();
            int max = selectionModel.getMaxSelectionIndex();
            List events = getLogTableModel().getEvents();
            if (events == null) {
                return;
            }
            StringBuffer buf = new StringBuffer();
            boolean warned = false;
            for (int i = min; i <= max; i++) {
                if (selectionModel.isSelectedIndex(i)) {
                    LogEvent selEvent = (LogEvent) events.get(i);
                    buf.append(selEvent.getString()).append("\n");
                }
                if (!warned && (buf.length() > WARN_TXT_AREA_SIZE)) {
                    String uiMessage = MessageUtils.getUIMessage(MessageUtils.many_event_selected_message, "" + WARN_TXT_AREA_SIZE);
                    String uiTitle = MessageUtils.getUIMessage(MessageUtils.many_event_selected_title);
                    int option = JOptionPane.showConfirmDialog(getFrame(), uiMessage, uiTitle, JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.NO_OPTION) {
                        return;
                    }
                    warned = true;
                }
            }
            textArea1.setText(buf.toString());
            textArea1.setCaretPosition(0);
        }
    }

    /**
     * Installe les event listeners : gestion de la selection : selectionner un
     * evenement affiche son detail dans le text area gestion du clic droit :
     * set du filtre associ� configuration des combos de filtre
     */
    @Override
    protected void initEvents() {
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel selectionModel = table1.getSelectionModel();
                int i = selectionModel.getMaxSelectionIndex();
                List events = getLogTableModel().getEvents();
                if (i >= 0 && i < events.size()) {
                    LogEvent selEvent = (LogEvent) events.get(i);
                    if (selEvent instanceof ProvidedEvent) {
                        getFrame().setTitle(((ProvidedEvent) selEvent).getProvider().toString());
                    } else {
                        getFrame().setTitle("unknown event");
                    }
                }
            }
        });
        table1.getSelectionModel().addListSelectionListener(new TextAreaUpdater());
        table1.addMouseListener(new MouseAdapter() {

            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
                    Point pointClicked = e.getPoint();
                    int column = table1.columnAtPoint(pointClicked);
                    int row = table1.rowAtPoint(pointClicked);
                    if (logger.isDebugEnabled()) {
                        logger.debug("right clicked cell [" + row + "," + column + "]");
                    }
                    Object value = getLogTableModel().getValueAt(row, column);
                    if (getLogTableModel().getColumnClass(column) == Date.class) {
                        DateFilterInputPanel dateFilter = ((DateFilterInputPanel) filters.get(column));
                        dateFilter.setPattern(df.toPattern());
                        dateFilter.setText(df.format(value));
                    } else {
                        getFilter(column).setText(value.toString());
                    }
                }
            }
        });
        table1.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                labelEventCount.setText(MessageUtils.getUIMessage(MessageUtils.label_event_count, "" + getLogTableModel().getEvents().size(), "" + getLogTableModel().getAllEvents().size()));
            }
        });
        initDnd();
    }

    private FilterInput getFilter(int column) {
        return (FilterInput) filters.get(column);
    }

    @Override
    public LogViewerForm createInFrame(LogProvider provider) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        final LogViewerFormSwing form = new LogViewerFormSwing();
        form.addFromProvider(provider);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(form.mainPanel, BorderLayout.CENTER);
        JPanel statusBar = new JPanel(new BorderLayout());
        form.memInspector = new MemInspector();
        statusBar.add(form.labelEventCount, BorderLayout.WEST);
        statusBar.add(form.progressBar, BorderLayout.CENTER);
        statusBar.add(form.memInspector, BorderLayout.EAST);
        form.progressBar.setVisible(false);
        f.getContentPane().add(statusBar, BorderLayout.SOUTH);
        f.setJMenuBar(form.createMenuBar());
        f.setTitle(provider.toString());
        f.setSize(800, 600);
        globalOpenViewers.put(f, form);
        f.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                form.setColumnWidths();
                form.getFrame().removeWindowListener(this);
                if (logger.isDebugEnabled()) {
                    logger.debug("removed component listener");
                }
            }
        });
        f.addWindowListener(lastWindowCheckListener);
        return form;
    }

    /**
     * set les tailles de colonnes pour thread : taille max a 15 caracteres date
     * : taille max a la taille adequate priority : taille max a la taille de
     * DEBUG
     */
    private void setColumnWidths() {
        if (logger.isDebugEnabled()) {
            logger.debug("setting column widths on first display");
        }
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private void setMaximumColumnWidth(int columnIndex, String biggestString) {
        Graphics graphics = table1.getGraphics();
        if (graphics != null) {
            FontMetrics fontMetrics = graphics.getFontMetrics();
            Rectangle2D stringBounds = fontMetrics.getStringBounds(biggestString, graphics);
            int OFFSET = 5;
            TableColumn column = table1.getColumnModel().getColumn(columnIndex);
            int preferredWidth = (int) (stringBounds.getWidth() + OFFSET);
            column.setPreferredWidth(preferredWidth);
            column.setMaxWidth(preferredWidth);
        }
    }

    @Override
    protected void addProviderToProviderMenu(final LogProvider provider) {
        if (provider.isTransient() || isProviderInMenu(provider)) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("adding color to menu for [" + provider + "]");
        }
        JMenuItem item = new ChangeProviderColorMenuItem(provider);
        menuCouleurs.add(item);
        item = new ToggleProviderMenuItem(provider);
        item.setSelected(!isProviderHidden(provider));
        item.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                ToggleProviderMenuItem menuItem = (ToggleProviderMenuItem) e.getSource();
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    hideEvents(menuItem.provider);
                } else {
                    showEvents(menuItem.provider);
                }
            }
        });
        menuProviders.add(item);
    }

    @Override
    protected String requestURLFromUser() {
        return (String) JOptionPane.showInputDialog(getFrame(), MessageUtils.getUIMessage(MessageUtils.request_url_message), MessageUtils.getUIMessage(MessageUtils.request_url_title), JOptionPane.QUESTION_MESSAGE, null, null, MessageUtils.getUIMessage(MessageUtils.request_url_initial_value));
    }

    @Override
    protected String requestFileFromUser() {
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * @param f the file that needs a recognizer to be parsed with
     * @return null if user cancels selection or a LogEventRecognizer
     */
    @Override
    protected LogEventRecognizer requestRecognizerFromUser(File f) {
        char[] cbuf = new char[SNAPSHOT_LENGTH];
        int readChars;
        try {
            FileReader reader = new FileReader(f);
            readChars = reader.read(cbuf);
            if (readChars <= 0) {
                throw new RuntimeException("Empty File");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("IO Error reading file", e);
        }
        List<LogEventRecognizer> recognizers = new ArrayList<LogEventRecognizer>(globalRecognizers.values());
        recognizers.add(new SimpleLogEventRecognizer());
        Collections.sort(recognizers, new Comparator<LogEventRecognizer>() {

            public int compare(LogEventRecognizer reco1, LogEventRecognizer reco2) {
                return reco1.getName().compareTo(reco2.getName());
            }
        });
        LogEventRecognizerForm form = LogEventRecognizerForm.showRequestDialog(getFrame(), recognizers, new String(cbuf, 0, readChars));
        LogEventRecognizer recognizer = form.getRecognizer();
        if (recognizer == null) {
            return null;
        }
        if (form.isSaveRecognizer()) {
            saveRecognizer(recognizer);
        }
        return recognizer;
    }

    @Override
    public boolean isProviderInMenu(final LogProvider provider) {
        String s = provider.toString();
        for (int i = 0, max = menuCouleurs.getItemCount(); i < max; i++) {
            if (menuCouleurs.getItem(i).getText().equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected FilteringLogEventList getLogTableModel() {
        return (FilteringLogEventList) table1.getModel();
    }

    private void initDnd() {
        DropTarget dropTarget = new DropTarget(this.mainPanel, DnDConstants.ACTION_COPY, new DropTargetListener() {

            public void drop(DropTargetDropEvent dtde) {
                if (logger.isDebugEnabled()) {
                    logger.debug("drop");
                }
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
                    List<File> filesToOpen = new ArrayList<File>();
                    try {
                        List transferData = (List) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        filesToOpen = new ArrayList<File>(transferData.size());
                        for (Object aTransferData : transferData) {
                            File file = (File) aTransferData;
                            filesToOpen.add(file);
                        }
                        dtde.dropComplete(true);
                    } catch (UnsupportedFlavorException e) {
                        dtde.rejectDrop();
                        logger.error(MessageUtils.getErrorMessage(MessageUtils.error_accepting_drop), e);
                    } catch (IOException e) {
                        dtde.rejectDrop();
                        logger.error(MessageUtils.getErrorMessage(MessageUtils.error_accepting_drop), e);
                    }
                    for (File file : filesToOpen) {
                        addFromFile(file);
                    }
                } else {
                    dtde.rejectDrop();
                }
            }

            public void dragEnter(DropTargetDragEvent dtde) {
                if (logger.isDebugEnabled()) {
                    DataFlavor[] currentDataFlavors = dtde.getCurrentDataFlavors();
                    for (DataFlavor flavor : currentDataFlavors) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("flavor : " + flavor.getHumanPresentableName() + "/" + flavor.getMimeType());
                        }
                    }
                }
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_REFERENCE);
                } else {
                    dtde.rejectDrag();
                }
            }

            public void dragOver(DropTargetDragEvent dtde) {
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            public void dragExit(DropTargetEvent dte) {
            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu(UIConstants.MENU_FILE);
        fileMenu.add(new ActionOpenFile(this));
        fileMenu.add(new ActionOpenURL(this));
        fileMenu.add(new ActionRefreshEvents(this));
        fileMenu.addSeparator();
        fileMenu.add(new AbstractAction(UIConstants.MENU_FILE_CLOSE) {

            public void actionPerformed(ActionEvent e) {
                getFrame().dispose();
            }
        });
        fileMenu.addSeparator();
        fileMenu.add(new AbstractAction(UIConstants.MENU_FILE_CONFIGURATION_LOAD) {

            public void actionPerformed(ActionEvent e) {
                Properties props = getConfigurations();
                if (props.containsKey(PREFERENCE_CONFIGURATION_NEXT)) {
                    props.remove(PREFERENCE_CONFIGURATION_NEXT);
                }
                List configsSorted = new ArrayList(props.values());
                Collections.sort(configsSorted);
                String sel = (String) JOptionPane.showInputDialog(getFrame(), MessageUtils.getUIMessage(MessageUtils.configuration_load_message), MessageUtils.getUIMessage(MessageUtils.configuration_load_title), JOptionPane.QUESTION_MESSAGE, null, configsSorted.toArray(), null);
                if (sel != null) {
                    currentConfig = sel;
                    for (LogViewerForm logViewerForm : globalOpenViewers.values()) {
                        LogViewerFormSwing form = (LogViewerFormSwing) logViewerForm;
                        form.getFrame().dispose();
                    }
                    restoreConfiguration(sel);
                }
            }
        });
        fileMenu.add(new AbstractAction(UIConstants.MENU_FILE_CONFIGURATION_SAVE) {

            public void actionPerformed(ActionEvent e) {
                if (currentConfig == null) {
                    currentConfig = JOptionPane.showInputDialog(getFrame(), MessageUtils.getUIMessage(MessageUtils.configuration_saveas_message), MessageUtils.getUIMessage(MessageUtils.configuration_saveas_title), JOptionPane.QUESTION_MESSAGE);
                }
                if (currentConfig != null) {
                    saveConfiguration(currentConfig);
                }
            }
        });
        fileMenu.add(new AbstractAction(UIConstants.MENU_FILE_CONFIGURATION_SAVEAS) {

            public void actionPerformed(ActionEvent e) {
                currentConfig = JOptionPane.showInputDialog(getFrame(), MessageUtils.getUIMessage(MessageUtils.configuration_saveas_message), MessageUtils.getUIMessage(MessageUtils.configuration_saveas_title), JOptionPane.QUESTION_MESSAGE);
                if (currentConfig != null) {
                    saveConfiguration(currentConfig);
                }
            }
        });
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        mb.add(fileMenu);
        mb.add(menuCouleurs);
        mb.add(menuProviders);
        mb.add(new JMenuItem(new SpawnLogViewerAction(this)));
        mb.add(new JMenuItem(new RenameFrameAction(this)));
        JMenu menuAPropos = new JMenu(MessageUtils.getUIMessage(MessageUtils.menu_help));
        menuAPropos.add(new JMenuItem(new AbstractAction(MessageUtils.getUIMessage(MessageUtils.menu_help)) {

            public void actionPerformed(ActionEvent event) {
                showHelp();
            }
        }));
        menuAPropos.addSeparator();
        menuAPropos.add(new JMenuItem(new AbstractAction(MessageUtils.getUIMessage(MessageUtils.menu_infos)) {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(getFrame(), getBuildInfos(), MessageUtils.getUIMessage(MessageUtils.about_build), JOptionPane.INFORMATION_MESSAGE);
            }
        }));
        mb.add(Box.createHorizontalGlue());
        mb.add(menuAPropos);
        return mb;
    }

    @Override
    protected void setCurrentDirectory(File currentDirectory) {
        fc.setCurrentDirectory(currentDirectory);
    }

    @Override
    public void setVisible(boolean b) {
        getFrame().setVisible(b);
    }

    @Override
    protected void setBounds(int x, int y, int width, int height) {
        getFrame().setBounds(x, y, width, height);
    }

    private JFrame getFrame() {
        return (table1 == null) ? null : (JFrame) table1.getTopLevelAncestor();
    }

    @Override
    protected List<LogEvent> getSelectedEvents() {
        LogEventList model = getLogTableModel();
        ListSelectionModel selectionModel = table1.getSelectionModel();
        int from = selectionModel.getMinSelectionIndex();
        int to = table1.getSelectionModel().getMaxSelectionIndex();
        List<LogEvent> l = new ArrayList<LogEvent>();
        List<LogEvent> events = model.getEvents();
        for (int i = from; i <= to; i++) {
            if (selectionModel.isSelectedIndex(i)) {
                l.add(events.get(i));
            }
        }
        return l;
    }

    public static void showHelp() {
        try {
            URL urlReadme = LogViewerForm.class.getResource("/readme.html");
            JEditorPane pane = new JEditorPane(urlReadme);
            JFrame f = new JFrame(MessageUtils.getUIMessage(MessageUtils.menu_help));
            f.getContentPane().add(new JScrollPane(pane));
            f.setSize(800, 400);
            f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        } catch (IOException e) {
            logger.error(MessageUtils.getErrorMessage(MessageUtils.help_file_not_found), e);
        }
    }

    @Override
    protected ProgressIndicatorNeutral createProgress(int max) {
        if (logger.isDebugEnabled()) {
            logger.debug("creating monitor (toBeWorked = " + max + ")");
        }
        if (getFrame() == null || !getFrame().isVisible()) {
            final ProgressMonitor aMonitor = new ProgressMonitor(null, MessageUtils.getUIMessage(MessageUtils.loading), "", 0, max);
            aMonitor.setMillisToDecideToPopup(1);
            aMonitor.setMillisToPopup(1);
            return new ProgressIndicatorNeutral() {

                private final Object lock = new Object();

                private boolean finished = false;

                private int progress = 0;

                private double uncommittedProgress = 0;

                ProgressMonitor monitor = aMonitor;

                public int getMax() {
                    return monitor.getMaximum();
                }

                public void setNote(String s) {
                    monitor.setNote(s);
                }

                public void setMax(int max) {
                    monitor.setMaximum(max);
                }

                public void start() {
                }

                public void worked(double howMuch) {
                    if (finished) {
                        return;
                    }
                    synchronized (lock) {
                        uncommittedProgress += howMuch;
                        int worked = (int) uncommittedProgress;
                        uncommittedProgress -= (double) worked;
                        progress += worked;
                        monitor.setProgress(progress);
                        if (progress >= getMax()) {
                            finished();
                        }
                    }
                }

                public int getProgress() {
                    return 0;
                }

                public void finished() {
                    monitor.setProgress(getMax());
                }
            };
        } else {
            ProgressIndicatorNeutral monitor = new ProgressIndicatorNeutral() {

                private final Object lock = new Object();

                private boolean finished = false;

                private double uncommittedProgress = 0;

                public void finished() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("hiding progressBar");
                    }
                    finished = true;
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                    progressBar.setMaximum(0);
                }

                public int getMax() {
                    return progressBar.getMaximum();
                }

                public int getProgress() {
                    return progressBar.getValue();
                }

                public void setMax(int aMax) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("setting progressBar max : " + aMax);
                    }
                    progressBar.setMaximum(aMax);
                }

                public void setNote(String s) {
                    progressBar.setToolTipText(s);
                }

                public void worked(double howMuch) {
                    if (finished) {
                        return;
                    }
                    synchronized (lock) {
                        uncommittedProgress += howMuch;
                        int worked = (int) uncommittedProgress;
                        uncommittedProgress -= (double) worked;
                        int progress = progressBar.getValue() + worked;
                        progressBar.setValue(progress);
                        if (progress >= getMax()) {
                            finished();
                        }
                    }
                }

                public void start() {
                    if (logger.isDebugEnabled()) {
                        logger.debug("displaying progressBar");
                    }
                    finished = false;
                    progressBar.setVisible(true);
                }
            };
            monitor.setMax(max);
            monitor.start();
            return monitor;
        }
    }

    @Override
    protected void updateParts(LogEvent.Part[] parts) {
        super.updateParts(parts);
        ActionListener tableUpdater = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ProgressIndicatorNeutral progress = createProgress(0);
                getLogTableModel().applyFilter(progress);
            }
        };
        filters = new ArrayList<JPanel>(parts.length);
        FilteringLogEventList filteringLogTableModel = getLogTableModel();
        panelFiltres.setLayout(new GridLayout(1, parts.length));
        for (LogEvent.Part part : parts) {
            JPanel filter;
            if (part.type == Date.class) {
                NegatableDateAfterPredicate predicate = (NegatableDateAfterPredicate) predicatesByPart.get(part);
                filter = new DateFilterInputPanel(predicate, part.name);
                filteringLogTableModel.setPredicate(part, predicate);
            } else {
                NegatableStringLikePredicate predicate = (NegatableStringLikePredicate) predicatesByPart.get(part);
                filter = new StringFilterInputPanel(predicate, part.name);
                filteringLogTableModel.setPredicate(part, predicate);
            }
            ((FilterInput) filter).addActionListener(tableUpdater);
            filters.add(filter);
            panelFiltres.add(filter);
        }
    }

    public static void main(String[] args) {
        URL url = LogViewerForm.class.getResource("/log4j.properties");
        org.apache.log4j.PropertyConfigurator.configure(url);
        logger.info("STARTING NEW SESSION : " + getBuildInfos());
        LogViewerFormSwing model = new LogViewerFormSwing();
        model.restorePreferences();
        if (globalOpenViewers.size() == 0) {
            JFrame f = ((LogViewerFormSwing) model.createInFrame(new ListLogProvider())).getFrame();
            f.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    globalOpenViewers.values().iterator().next().savePreferences();
                    System.exit(0);
                }
            });
            f.setVisible(true);
        }
    }

    private static class ActionOpenFile extends AbstractAction {

        LogViewerForm form;

        public ActionOpenFile(LogViewerForm form) {
            super(UIConstants.MENU_FILE_OPENFILE);
            this.form = form;
        }

        public void actionPerformed(ActionEvent event) {
            form.readFromFile();
        }
    }

    private static class ChangeProviderColorAction extends AbstractAction {

        LogViewerFormSwing form;

        LogProvider provider;

        public ChangeProviderColorAction(LogViewerFormSwing logViewerForm, LogProvider provider) {
            super(provider.toString());
            this.form = logViewerForm;
            this.provider = provider;
        }

        public void actionPerformed(ActionEvent e) {
            Color newColor = JColorChooser.showDialog(null, provider.toString(), ProviderColorManager.instance().getColorForProvider(provider));
            if (newColor != null) {
                ProviderColorManager.instance().setColorForProvider(provider, newColor);
                form.table1.repaint();
            }
        }
    }

    private static class ActionRefreshEvents extends AbstractAction {

        LogViewerForm form;

        public ActionRefreshEvents(LogViewerForm form) {
            super(UIConstants.MENU_FILE_REFRESH);
            this.form = form;
        }

        public void actionPerformed(ActionEvent event) {
            form.refreshProviders();
        }
    }

    private static class ActionOpenURL extends AbstractAction {

        LogViewerForm form;

        public ActionOpenURL(LogViewerForm form) {
            super(UIConstants.MENU_FILE_OPENURL);
            this.form = form;
        }

        public void actionPerformed(ActionEvent event) {
            form.readFromURL();
        }
    }

    private static class SpawnLogViewerAction extends AbstractAction {

        LogViewerForm form;

        public SpawnLogViewerAction(LogViewerForm f) {
            super(UIConstants.MENU_SPAWN);
            form = f;
        }

        public void actionPerformed(ActionEvent event) {
            List<LogEvent> l = form.getSelectedEvents();
            LogViewerFormSwing newForm = new LogViewerFormSwing();
            newForm.createInFrame(new ListLogProvider(l, form.getParts())).setVisible(true);
        }
    }

    private static class ProvidedEventCellRenderer implements TableCellRenderer {

        LogViewerForm form;

        private DefaultTableCellRenderer renderer;

        public ProvidedEventCellRenderer(LogViewerForm f, DefaultTableCellRenderer renderer) {
            this.renderer = renderer;
            form = f;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            LogEvent event = form.getLogTableModel().getEvents().get(row);
            renderer.setBackground(ProviderColorManager.instance().getColorForEvent(event));
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class ChangeProviderColorMenuItem extends JMenuItem {

        private final LogProvider provider;

        public ChangeProviderColorMenuItem(LogProvider provider) {
            super(new ChangeProviderColorAction(LogViewerFormSwing.this, provider));
            this.provider = provider;
        }

        @Override
        public Color getForeground() {
            return ProviderColorManager.instance().getColorForProvider(provider);
        }
    }

    private static class ToggleProviderMenuItem extends JCheckBoxMenuItem {

        private final LogProvider provider;

        public ToggleProviderMenuItem(LogProvider provider) {
            super(provider.toString());
            this.provider = provider;
        }

        @Override
        public Color getForeground() {
            return ProviderColorManager.instance().getColorForProvider(provider);
        }
    }

    @Override
    public PreferencesSaver.PersistentLogViewerForm getPersistentLogViewerForm() {
        PersistentLogViewerFormImpl p = new PersistentLogViewerFormImpl();
        p.openProviders = openProviders;
        p.hiddenProviders = hiddenProviders;
        p.frameBounds = getFrame().getBounds();
        return p;
    }

    private static class ExitAction extends AbstractAction {

        private final LogViewerFormSwing form;

        public ExitAction(final LogViewerFormSwing logViewerFormSwing) {
            super(UIConstants.MENU_FILE_QUIT);
            form = logViewerFormSwing;
        }

        public void actionPerformed(ActionEvent e) {
            int i = JOptionPane.showConfirmDialog(form.getFrame(), MessageUtils.getUIMessage(MessageUtils.configuration_confirm_save));
            switch(i) {
                case JOptionPane.YES_OPTION:
                    form.savePreferences();
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
                default:
            }
        }
    }
}
