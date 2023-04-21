package com.sodad.metacode;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import com.sodad.metacode.ui.MetaCoDeAnnotationListView2;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.Gate;
import gate.GateConstants;
import gate.TextualDocument;
import gate.creole.ResourceData;
import gate.creole.ResourceInstantiationException;
import gate.event.*;
import gate.event.DocumentEvent;
import gate.event.DocumentListener;
import gate.gui.*;
import gate.gui.annedit.*;
import gate.gui.docview.*;
import gate.swing.ColorGenerator;
import gate.swing.XJTable;
import gate.swing.XJFileChooser;
import gate.util.*;
import java.sql.*;

/**
 * Display document annotation sets and types in a tree view like with a table.
 * Allow the selection of annotation type and modification of their color.
 */
public class MetaCoDeAnnotationSetsView extends AbstractDocumentView implements DocumentListener, AnnotationSetListener, AnnotationEditorOwner, MetathesaurusServiceListener {

    public void metathesaurusConnectionOpened(MetathesaurusServiceEvent e) {
        updateThesaurusStatusInfo();
    }

    public void metathesaurusConnectionClosed(MetathesaurusServiceEvent e) {
        updateThesaurusStatusInfo();
    }

    public void metathesaurusAbnormalSituation(MetathesaurusServiceEvent e) {
        updateThesaurusStatusInfo();
    }

    private MetathesaurusService itsMetathesaurusService;

    void updateThesaurusStatusInfo() {
        String status = itsMetathesaurusService.isDriverLoaded() ? "Driver loaded" : "Driver not loaded, cannot open connection";
        if (itsMetathesaurusService.isConnected()) status += ", connection opened";
        itsThesaurusConnectionStatusLbl.setText(status);
    }

    public void annotationChanged(Annotation ann, AnnotationSet set, String oldType) {
    }

    /**
   * Queues an action for selecting the provided annotation
   */
    public void selectAnnotation(final AnnotationData aData) {
        Runnable action = new Runnable() {

            public void run() {
                List<AnnotationData> selAnns = Collections.singletonList(aData);
                owner.setSelectedAnnotations(selAnns);
            }
        };
        pendingEvents.offer(new PerformActionEvent(action));
        eventMinder.restart();
    }

    public Annotation getNextAnnotation() {
        return null;
    }

    public Annotation getPreviousAnnotation() {
        return null;
    }

    public JTextComponent getTextComponent() {
        return textPane;
    }

    public AnnotationList getListComponent() {
        return listView;
    }

    public MetaCoDeAnnotationSetsView() {
        itsMetathesaurusService = new MetathesaurusService();
        metacodeSetHandlers = new ArrayList<MetaCoDeSetHandler>();
        tableRows = new ArrayList();
        visibleAnnotationSets = new LinkedBlockingQueue<MetaCoDeSetHandler>();
        actions = new ArrayList();
        actions.add(new SavePreserveFormatAction());
        pendingEvents = new LinkedBlockingQueue<GateEvent>();
        eventMinder = new Timer(EVENTS_HANDLE_DELAY, new HandleDocumentEventsAction());
        eventMinder.setRepeats(true);
        eventMinder.setCoalesce(true);
    }

    public List getActions() {
        return actions;
    }

    public int getType() {
        return VERTICAL;
    }

    protected void initGUI() {
        Iterator centralViewsIter = owner.getCentralViews().iterator();
        while (textView == null && centralViewsIter.hasNext()) {
            DocumentView aView = (DocumentView) centralViewsIter.next();
            if (aView instanceof TextualDocumentView) textView = (TextualDocumentView) aView;
        }
        textPane = (JTextArea) ((JScrollPane) textView.getGUI()).getViewport().getView();
        Iterator horizontalViewsIter = owner.getHorizontalViews().iterator();
        while (listView == null && horizontalViewsIter.hasNext()) {
            DocumentView aView = (DocumentView) horizontalViewsIter.next();
            if (aView instanceof AnnotationListView) listView = (AnnotationListView) aView;
        }
        horizontalViewsIter = owner.getHorizontalViews().iterator();
        while (stackView == null && horizontalViewsIter.hasNext()) {
            DocumentView aView = (DocumentView) horizontalViewsIter.next();
            if (aView instanceof AnnotationStackView) stackView = (AnnotationStackView) aView;
        }
        horizontalViewsIter = owner.getHorizontalViews().iterator();
        while (metacodeView == null && horizontalViewsIter.hasNext()) {
            DocumentView aView = (DocumentView) horizontalViewsIter.next();
            if (aView instanceof MetaCoDeAnnotationListView) {
                metacodeView = (MetaCoDeAnnotationListView) aView;
                metacodeView.setMetathesaurusService(itsMetathesaurusService);
            }
        }
        horizontalViewsIter = owner.getHorizontalViews().iterator();
        while (metacodeView2 == null && horizontalViewsIter.hasNext()) {
            DocumentView aView = (DocumentView) horizontalViewsIter.next();
            if (aView instanceof MetaCoDeAnnotationListView2) {
                metacodeView2 = (MetaCoDeAnnotationListView2) aView;
                metacodeView2.setMetathesaurusService(itsMetathesaurusService);
            }
        }
        mainTable = new XJTable();
        tableModel = new SetsTableModel();
        mainTable.setSortable(false);
        mainTable.setModel(tableModel);
        mainTable.setRowMargin(0);
        mainTable.getColumnModel().setColumnMargin(0);
        SetsTableCellRenderer cellRenderer = new SetsTableCellRenderer();
        mainTable.getColumnModel().getColumn(NAME_COL).setCellRenderer(cellRenderer);
        mainTable.getColumnModel().getColumn(SELECTED_COL).setCellRenderer(cellRenderer);
        SetsTableCellEditor cellEditor = new SetsTableCellEditor();
        mainTable.getColumnModel().getColumn(SELECTED_COL).setCellEditor(cellEditor);
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainTable.setColumnSelectionAllowed(false);
        mainTable.setRowSelectionAllowed(true);
        mainTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        mainTable.setAutoCreateColumnsFromModel(false);
        mainTable.setTableHeader(null);
        mainTable.setShowGrid(false);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Color tableBG = mainTable.getBackground();
        tableBG = new Color(tableBG.getRGB());
        mainTable.setBackground(tableBG);
        scroller = new JScrollPane(mainTable);
        scroller.getViewport().setOpaque(true);
        scroller.getViewport().setBackground(tableBG);
        try {
            annotationEditor = createAnnotationEditor(textView, this);
        } catch (ResourceInstantiationException e) {
            throw new GateRuntimeException("Could not initialise the annotation editor!", e);
        }
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.gridx = GridBagConstraints.RELATIVE;
        constraints.gridwidth = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        mainPanel.add(scroller, constraints);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        JLabel connectionLbl = new JLabel();
        connectionLbl.setText("Thesaurus Data Base:");
        bottomPanel.add(connectionLbl, constraints);
        connectionStringTextField = new JTextField();
        bottomPanel.add(connectionStringTextField, constraints);
        constraints.weightx = 0;
        testConnectionAction = new TestConnectionAction();
        bottomPanel.add(new JButton(testConnectionAction), constraints);
        itsThesaurusConnectionStatusLbl = new JLabel();
        bottomPanel.add(itsThesaurusConnectionStatusLbl, constraints);
        constraints.weighty = 0;
        mainPanel.add(bottomPanel, constraints);
        updateThesaurusStatusInfo();
        populateUI();
        tableModel.fireTableDataChanged();
        eventMinder.start();
        initListeners();
    }

    /**
   * Create the annotation editor (responsible for creating the window
   * used to edit individual annotations).
   * 
   * @param textViews
   * @param asView
   * @return
   * @throws ResourceInstantiationException
   */
    protected gate.gui.annedit.OwnedAnnotationEditor createAnnotationEditor(TextualDocumentView textView, MetaCoDeAnnotationSetsView asView) throws ResourceInstantiationException {
        List<String> vrTypes = new ArrayList<String>(Gate.getCreoleRegister().getPublicVrTypes());
        Collections.reverse(vrTypes);
        for (String aVrType : vrTypes) {
            ResourceData rData = (ResourceData) Gate.getCreoleRegister().get(aVrType);
            try {
                Class resClass = rData.getResourceClass();
                if (OwnedAnnotationEditor.class.isAssignableFrom(resClass)) {
                    OwnedAnnotationEditor newEditor = (OwnedAnnotationEditor) resClass.newInstance();
                    newEditor.setOwner(this);
                    newEditor.init();
                    return newEditor;
                }
            } catch (ClassNotFoundException cnfe) {
                Err.prln("Invalid CREOLE data:");
                cnfe.printStackTrace(Err.getPrintWriter());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Err.prln("Could not find any annotation editors. Editing annotations disabled.");
        return null;
    }

    protected void populateUI() {
        AnnotationSet a = document.getAnnotations();
        if (containsMetaCoDeType(a)) metacodeSetHandlers.add(new MetaCoDeSetHandler(a));
        List setNames = document.getNamedAnnotationSets() == null ? new ArrayList() : new ArrayList(document.getNamedAnnotationSets().keySet());
        Collections.sort(setNames);
        Iterator setsIter = setNames.iterator();
        while (setsIter.hasNext()) {
            a = document.getAnnotations((String) setsIter.next());
            if (containsMetaCoDeType(a)) metacodeSetHandlers.add(new MetaCoDeSetHandler(a));
        }
        tableRows.addAll(metacodeSetHandlers);
    }

    public Component getGUI() {
        return mainPanel;
    }

    /**
   * Get the saved colour for this annotation type or create a new one
   * and save it. The colours are saved in the user configuration file.
   * @param annotationType type to get a colour for
   * @return a colour
   */
    public static Color getColor(String annotationSet, String annotationType) {
        Map<String, String> colourMap = Gate.getUserConfig().getMap(MetaCoDeAnnotationSetsView.class.getName() + ".colours");
        String colourValue = colourMap.get(annotationSet + "." + annotationType);
        if (colourValue == null) colourValue = colourMap.get(annotationType);
        Color colour;
        if (colourValue == null) {
            float components[] = colourGenerator.getNextColor().getComponents(null);
            colour = new Color(components[0], components[1], components[2], 0.5f);
            int rgb = colour.getRGB();
            int alpha = colour.getAlpha();
            int rgba = rgb | (alpha << 24);
            colourMap.put(annotationType, String.valueOf(rgba));
            Gate.getUserConfig().put(MetaCoDeAnnotationSetsView.class.getName() + ".colours", colourMap);
        } else {
            colour = new Color(Integer.valueOf(colourValue), true);
        }
        return colour;
    }

    protected void saveColor(String annotationSet, String annotationType, Color colour) {
        Map<String, String> colourMap = Gate.getUserConfig().getMap(MetaCoDeAnnotationSetsView.class.getName() + ".colours");
        int rgb = colour.getRGB();
        int alpha = colour.getAlpha();
        int rgba = rgb | (alpha << 24);
        String defaultValue = colourMap.get(annotationType);
        String newValue = String.valueOf(rgba);
        if (newValue.equals(defaultValue)) {
            colourMap.remove(annotationSet + "." + annotationType);
        } else {
            colourMap.put(annotationSet + "." + annotationType, newValue);
        }
        Gate.getUserConfig().put(MetaCoDeAnnotationSetsView.class.getName() + ".colours", colourMap);
    }

    /**
   * Save type or remove unselected type in the preferences.
   * @param setName set name to save/remove or null for the default set
   * @param typeName type name to save/remove
   * @param selected state of the selection
   */
    public void saveType(String setName, String typeName, boolean selected) {
        List<String> typeList = Gate.getUserConfig().getList(MetaCoDeAnnotationSetsView.class.getName() + ".types");
        String prefix = (setName == null) ? "." : setName + ".";
        if (selected) {
            typeList.add(prefix + typeName);
        } else {
            typeList.remove(prefix + typeName);
        }
        Gate.getUserConfig().put(MetaCoDeAnnotationSetsView.class.getName() + ".types", typeList);
    }

    /**
   * Enables or disables creation of the new annotation set.
   */
    public void setNewAnnSetCreationEnabled(boolean b) {
    }

    private void storeSelectedSets() {
        visibleAnnotationSets.clear();
        for (MetaCoDeSetHandler h : metacodeSetHandlers) {
            if (h.isSelected()) {
                h.setSelected(false);
                visibleAnnotationSets.add(h);
            }
        }
    }

    private void restoreSelectedSets() {
        MetaCoDeSetHandler h;
        while ((h = visibleAnnotationSets.poll()) != null) {
            h.setSelected(true);
        }
    }

    /**
     * This method will be called whenever the view becomes active. Implementers 
     * should use this to add hooks (such as mouse listeners) to the other views
     * as required by their functionality. 
     */
    protected void registerHooks() {
        textPane.addMouseListener(textMouseListener);
        textPane.addMouseMotionListener(textMouseListener);
        textPane.addPropertyChangeListener("highlighter", textChangeListener);
        restoreSelectedSets();
    }

    /**
   * This method will be called whenever this view becomes inactive. 
   * Implementers should use it to unregister whatever hooks they registered
   * in {@link #registerHooks()}.
   *
   */
    protected void unregisterHooks() {
        textPane.removeMouseListener(textMouseListener);
        textPane.removeMouseMotionListener(textMouseListener);
        textPane.removePropertyChangeListener("highlighter", textChangeListener);
        storeSelectedSets();
    }

    protected void initListeners() {
        itsMetathesaurusService.addListener(this);
        document.addDocumentListener(this);
        mainTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                processMouseEvent(evt);
            }

            public void mousePressed(MouseEvent evt) {
                int row = mainTable.rowAtPoint(evt.getPoint());
                if (evt.isPopupTrigger() && !mainTable.isRowSelected(row)) {
                    mainTable.getSelectionModel().setSelectionInterval(row, row);
                }
                processMouseEvent(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                processMouseEvent(evt);
            }

            protected void processMouseEvent(MouseEvent evt) {
            }
        });
        mainTable.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
            }
        });
        mouseStoppedMovingAction = new MouseStoppedMovingAction();
        mouseMovementTimer = new javax.swing.Timer(MOUSE_MOVEMENT_TIMER_DELAY, mouseStoppedMovingAction);
        mouseMovementTimer.setRepeats(false);
        textMouseListener = new TextMouseListener();
        textChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
            }
        };
        mainTable.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "deleteAll");
        mainTable.getInputMap().put(KeyStroke.getKeyStroke("shift DELETE"), "deleteAll");
        mainTable.getActionMap().put("deleteAll", new DeleteSelectedAnnotationsAction("Delete"));
        textPane.getInputMap().put(KeyStroke.getKeyStroke("control E"), "edit annotation");
        textPane.getActionMap().put("edit annotation", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                mouseStoppedMovingAction.setTextLocation(textPane.getCaretPosition());
                mouseStoppedMovingAction.actionPerformed(null);
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        annotationEditor.setPinnedMode(true);
                    }
                });
            }
        });
        InputMap im = mainTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke("TAB");
        final Action oldTabAction = mainTable.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed(e);
                JTable table = (JTable) e.getSource();
                if (table.getSelectedColumn() == SELECTED_COL) {
                    oldTabAction.actionPerformed(e);
                }
            }
        };
        mainTable.getActionMap().put(im.get(tab), tabAction);
        KeyStroke shiftTab = KeyStroke.getKeyStroke("shift TAB");
        final Action oldShiftTabAction = mainTable.getActionMap().get(im.get(shiftTab));
        Action shiftTabAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                oldShiftTabAction.actionPerformed(e);
                JTable table = (JTable) e.getSource();
                if (table.getSelectedColumn() == SELECTED_COL) {
                    oldShiftTabAction.actionPerformed(e);
                }
            }
        };
        mainTable.getActionMap().put(im.get(shiftTab), shiftTabAction);
    }

    public void cleanup() {
        itsMetathesaurusService.close();
        itsMetathesaurusService.removeListener(this);
        document.removeDocumentListener(this);
        eventMinder.stop();
        pendingEvents.clear();
        super.cleanup();
        document = null;
    }

    public void annotationSetAdded(final DocumentEvent e) {
        pendingEvents.offer(e);
        eventMinder.restart();
    }

    public void annotationSetRemoved(final DocumentEvent e) {
        pendingEvents.offer(e);
        eventMinder.restart();
    }

    /**Called when the content of the document has changed through an edit 
   * operation.
   */
    public void contentEdited(DocumentEvent e) {
    }

    public void annotationAdded(final AnnotationSetEvent e) {
        pendingEvents.offer(e);
        eventMinder.restart();
    }

    public void annotationRemoved(final AnnotationSetEvent e) {
        pendingEvents.offer(e);
        eventMinder.restart();
    }

    @Override
    public void setSelectedAnnotations(List<AnnotationData> selectedAnnots) {
        if (selectedAnnots.size() > 0) {
            final AnnotationData aData = selectedAnnots.get(0);
            PerformActionEvent actionEvent = new PerformActionEvent(new Runnable() {

                public void run() {
                    if (annotationEditor != null && annotationEditor.isActive()) {
                        if (annotationEditor.getAnnotationSetCurrentlyEdited() != aData.getAnnotationSet() || annotationEditor.getAnnotationCurrentlyEdited() != aData.getAnnotation()) {
                            annotationEditor.editAnnotation(aData.getAnnotation(), aData.getAnnotationSet());
                        }
                    }
                }
            });
            pendingEvents.offer(actionEvent);
            eventMinder.restart();
        }
    }

    /**
   * Sets a particular annotation as selected. If the list view is visible
   * and active, it makes sure that the same annotation is selected there.
   * If the annotation editor exists and is active, it switches it to this 
   * current annotation.
   * @param ann the annotation
   * @param annSet the parent set
   */
    public void selectAnnotation(final Annotation ann, final AnnotationSet annSet) {
        selectAnnotation(new AnnotationDataImpl(annSet, ann));
    }

    protected class SetsTableModel extends AbstractTableModel {

        public int getRowCount() {
            return tableRows.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int column) {
            MetaCoDeSetHandler value = (MetaCoDeSetHandler) tableRows.get(row);
            switch(column) {
                case NAME_COL:
                    return value;
                case SELECTED_COL:
                    return new Boolean(value.isSelected());
                default:
                    return null;
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == SELECTED_COL;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            MetaCoDeSetHandler receiver = (MetaCoDeSetHandler) tableRows.get(rowIndex);
            switch(columnIndex) {
                case SELECTED_COL:
                    receiver.setSelected(((Boolean) aValue).booleanValue());
                    break;
                default:
                    break;
            }
        }
    }

    protected class SetsTableCellRenderer implements TableCellRenderer {

        public SetsTableCellRenderer() {
            setLabel = new JLabel() {

                public void repaint(long tm, int x, int y, int width, int height) {
                }

                public void repaint(Rectangle r) {
                }

                public void validate() {
                }

                public void revalidate() {
                }

                protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
                }
            };
            setLabel.setOpaque(true);
            setLabel.setFont(setLabel.getFont().deriveFont(Font.BOLD));
            setLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            setChk = new JCheckBox() {

                public void repaint(long tm, int x, int y, int width, int height) {
                }

                public void repaint(Rectangle r) {
                }

                public void validate() {
                }

                public void revalidate() {
                }

                protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
                }
            };
            setChk.setOpaque(true);
            normalBorder = BorderFactory.createLineBorder(mainTable.getBackground(), 2);
            selectedBorder = BorderFactory.createLineBorder(mainTable.getSelectionBackground(), 2);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            value = tableRows.get(row);
            MetaCoDeSetHandler sHandler = (MetaCoDeSetHandler) value;
            switch(column) {
                case NAME_COL:
                    setLabel.setText(sHandler.set.getName());
                    setLabel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    return setLabel;
                case SELECTED_COL:
                    setChk.setSelected(sHandler.isSelected());
                    setChk.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    return setChk;
            }
            setLabel.setText("?");
            return setLabel;
        }

        protected JLabel setLabel;

        protected JCheckBox setChk;

        protected Border selectedBorder;

        protected Border normalBorder;
    }

    protected class SetsTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        public SetsTableCellEditor() {
            setChk = new JCheckBox();
            setChk.setOpaque(false);
            setChk.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    fireEditingStopped();
                    System.err.println("actionPerformed");
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            value = tableRows.get(row);
            MetaCoDeSetHandler sHandler = (MetaCoDeSetHandler) value;
            switch(column) {
                case NAME_COL:
                    return null;
                case SELECTED_COL:
                    setChk.setSelected(sHandler.isSelected());
                    return setChk;
            }
            return null;
        }

        public boolean stopCellEditing() {
            return true;
        }

        public Object getCellEditorValue() {
            System.out.println("getCellEditorValue");
            return new Boolean(setChk.isSelected());
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        JCheckBox setChk;
    }

    private class MetaCoDeSetHandler {

        Color colour;

        private AnnotationSet set;

        private boolean selected = false;

        private Map<Integer, AnnotationData> annListTagsForAnn = new HashMap<Integer, AnnotationData>();

        private Map<Integer, Object> hghltTagsForAnnId = new HashMap<Integer, Object>();

        MetaCoDeSetHandler(AnnotationSet set) {
            this.set = set;
            colour = getColor(set.getName(), MetaCoDeTypeName);
        }

        boolean isSelected() {
            return selected;
        }

        void setSelected(boolean selected) {
            if (this.selected == selected) return;
            this.selected = selected;
            final List<Annotation> annots = new ArrayList<Annotation>(set.get(MetaCoDeTypeName));
            if (selected) {
                annListTagsForAnn.clear();
                System.err.println("" + annots.size() + " annotations");
                List<AnnotationData> listTags = metacodeView2.addAnnotations(annots, set);
                for (AnnotationData aData : listTags) annListTagsForAnn.put(aData.getAnnotation().getId(), aData);
                hghltTagsForAnnId.clear();
                List tags = textView.addHighlights(listTags, colour);
                for (int i = 0; i < annots.size(); i++) hghltTagsForAnnId.put(annots.get(i).getId(), tags.get(i));
            } else {
                try {
                    metacodeView2.removeAnnotationSet(set);
                    textView.removeHighlights(hghltTagsForAnnId.values());
                } finally {
                    hghltTagsForAnnId.clear();
                    annListTagsForAnn.clear();
                }
            }
        }
    }

    /**
   * A class storing the identifying information for an annotation type (i.e.
   * the set name and the type).
   * @author Valentin Tablan (valyt)
   *
   */
    private static class TypeSpec {

        private String setName;

        private String type;

        public TypeSpec(String setName, String type) {
            super();
            this.setName = setName;
            this.type = type;
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((setName == null) ? 0 : setName.hashCode());
            result = PRIME * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final TypeSpec other = (TypeSpec) obj;
            if (setName == null) {
                if (other.setName != null) return false;
            } else if (!setName.equals(other.setName)) return false;
            if (type == null) {
                if (other.type != null) return false;
            } else if (!type.equals(other.type)) return false;
            return true;
        }
    }

    /**
   * A mouse listener used for events in the text view. 
   */
    protected class TextMouseListener implements MouseInputListener {

        public void mouseDragged(MouseEvent e) {
            mouseMovementTimer.stop();
        }

        public void mouseMoved(MouseEvent e) {
            int modEx = e.getModifiersEx();
            if ((modEx & MouseEvent.CTRL_DOWN_MASK) != 0) {
                mouseMovementTimer.stop();
                return;
            }
            if ((modEx & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                mouseMovementTimer.stop();
                return;
            }
            int textLocation = textPane.viewToModel(e.getPoint());
            try {
                Rectangle viewLocation = textPane.modelToView(textLocation);
                int error = 10;
                viewLocation = new Rectangle(viewLocation.x - error, viewLocation.y - error, viewLocation.width + 2 * error, viewLocation.height + 2 * error);
                if (viewLocation.contains(e.getPoint())) {
                    mouseStoppedMovingAction.setTextLocation(textLocation);
                } else {
                    mouseStoppedMovingAction.setTextLocation(-1);
                }
            } catch (BadLocationException e1) {
            } finally {
                mouseMovementTimer.restart();
            }
        }

        public void mouseClicked(MouseEvent e) {
            if (mouseStoppedMovingAction.textLocation == -1) return;
            for (MetaCoDeSetHandler h : metacodeSetHandlers) {
                if (h.isSelected()) {
                    AnnotationSet as = h.set.get(MetaCoDeTypeName, Math.max(0l, mouseStoppedMovingAction.textLocation - 1), Math.min(document.getContent().size(), mouseStoppedMovingAction.textLocation + 1));
                    if (!as.isEmpty()) {
                        Annotation a = as.iterator().next();
                        AnnotationData ad = h.annListTagsForAnn.get(a.getId());
                        List<AnnotationData> l = new ArrayList<AnnotationData>();
                        l.add(ad);
                        owner.setSelectedAnnotations(l);
                    }
                }
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
            mouseMovementTimer.stop();
        }
    }

    protected class TestConnectionAction extends AbstractAction {

        public TestConnectionAction() {
            super("Test the connection");
            putValue(SHORT_DESCRIPTION, "Test the connection to the data base");
        }

        public void actionPerformed(ActionEvent evt) {
            String databaseName = connectionStringTextField.getText();
            String url = "jdbc:postgresql:" + databaseName;
            String username = "metacode";
            String password = "";
            itsMetathesaurusService.open(url, username, password);
        }
    }

    protected class NewAnnotationSetAction extends AbstractAction {

        public NewAnnotationSetAction() {
            super("New");
            putValue(SHORT_DESCRIPTION, "Creates a new annotation set");
        }

        public void actionPerformed(ActionEvent evt) {
        }
    }

    protected class NewAnnotationAction extends AbstractAction {

        public NewAnnotationAction(String selection) {
            super("Create new annotation");
            putValue(SHORT_DESCRIPTION, "Creates a new annotation from the" + " selection: [" + Strings.crop(selection, 30) + "]");
        }

        public void actionPerformed(ActionEvent evt) {
        }
    }

    /**
   * The beginning is the same as {@link NameBearerHandle.SaveAsXmlAction}.
   */
    protected class SavePreserveFormatAction extends AbstractAction {

        public SavePreserveFormatAction() {
            super("Save Preserving Format");
            putValue(SHORT_DESCRIPTION, "Saves original markups and highlighted annotations");
        }

        public void actionPerformed(ActionEvent evt) {
            Runnable runableAction = new Runnable() {

                public void run() {
                }
            };
            Thread thread = new Thread(runableAction, "");
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    /**
   * A fake GATE Event used to wrap a {@link Runnable} value. This is used for
   * queueing actions to the document UI update timer.  
   */
    private class PerformActionEvent extends GateEvent {

        public PerformActionEvent(Runnable runnable) {
            super(MetaCoDeAnnotationSetsView.this, 0);
            this.runnable = runnable;
            this.action = null;
        }

        public PerformActionEvent(Action action) {
            super(MetaCoDeAnnotationSetsView.this, 0);
            this.runnable = null;
            this.action = action;
        }

        /**
     * Runs the action (or runnable) enclosed by this event. 
     */
        public void run() {
            if (runnable != null) {
                runnable.run();
            } else if (action != null) {
                action.actionPerformed(null);
            }
        }

        private Action action;

        private Runnable runnable;
    }

    protected MetaCoDeSetHandler getSetHandler(String name) {
        for (MetaCoDeSetHandler setHandler : metacodeSetHandlers) {
            if (name == null) {
                throw new RuntimeException();
            } else {
                if (name.equals(setHandler.set.getName())) return setHandler;
            }
        }
        return null;
    }

    protected class HandleDocumentEventsAction extends AbstractAction {

        public void actionPerformed(ActionEvent ev) {
            while (!pendingEvents.isEmpty()) {
                GateEvent event = pendingEvents.remove();
                if (event instanceof DocumentEvent) {
                    DocumentEvent e = (DocumentEvent) event;
                    String theSetName = e.getAnnotationSetName();
                    AnnotationSet as = document.getAnnotations(theSetName);
                    if (containsMetaCoDeType(as)) {
                        if (theSetName == null) throw new RuntimeException();
                        if (event.getType() == DocumentEvent.ANNOTATION_SET_ADDED) {
                            MetaCoDeSetHandler h = new MetaCoDeSetHandler(as);
                            int i = 0;
                            for (; i < metacodeSetHandlers.size() && ((MetaCoDeSetHandler) metacodeSetHandlers.get(i)).set.getName().compareTo(theSetName) <= 0; i++) ;
                            metacodeSetHandlers.add(i, h);
                            tableRows.add(i, h);
                            tableModel.fireTableRowsInserted(i, i);
                            return;
                        }
                        if (event.getType() == DocumentEvent.ANNOTATION_SET_REMOVED) {
                            MetaCoDeSetHandler h = getSetHandler(theSetName);
                            if (h == null) throw new RuntimeException();
                            metacodeSetHandlers.remove(h);
                            int i = tableRows.indexOf(h);
                            tableRows.remove(i);
                            h.set.removeAnnotationSetListener(MetaCoDeAnnotationSetsView.this);
                            tableModel.fireTableRowsDeleted(i, i);
                        }
                    }
                }
            }
        }

        /**
     * This method is used to update the display by reading the associated
     * document when it is considered that doing so would be cheaper than 
     * acting on the events queued
     */
        protected void rebuildDisplay() {
        }

        boolean uiDirty = false;

        /**
     * Maximum number of events to treat individually. If we have more pending
     * events than this value, the UI will be rebuilt from scratch
     */
        private static final int MAX_EVENTS = 300;
    }

    /**
   * Used to select an annotation for editing.
   *
   */
    protected class MouseStoppedMovingAction extends AbstractAction {

        public void actionPerformed(ActionEvent evt) {
        }

        public void setTextLocation(int textLocation) {
            this.textLocation = textLocation;
        }

        int textLocation;
    }

    /**
   * The popup menu items used to select annotations
   * Apart from the normal {@link javax.swing.JMenuItem} behaviour, this menu
   * item also highlights the annotation which it would select if pressed.
   */
    protected class HighlightMenuItem extends JMenuItem {

        public HighlightMenuItem(Action action, int startOffset, int endOffset, JPopupMenu popup) {
            super(action);
            this.start = startOffset;
            this.end = endOffset;
            this.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    showHighlight();
                }

                public void mouseExited(MouseEvent e) {
                    removeHighlight();
                }
            });
            popup.addPopupMenuListener(new PopupMenuListener() {

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                    removeHighlight();
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    removeHighlight();
                }
            });
        }

        protected void showHighlight() {
            try {
                highlight = textPane.getHighlighter().addHighlight(start, end, DefaultHighlighter.DefaultPainter);
            } catch (BadLocationException ble) {
                throw new GateRuntimeException(ble.toString());
            }
        }

        protected void removeHighlight() {
            if (highlight != null) {
                textPane.getHighlighter().removeHighlight(highlight);
                highlight = null;
            }
        }

        int start;

        int end;

        Action action;

        Object highlight;
    }

    protected class EditAnnotationAction extends AbstractAction {

        public EditAnnotationAction(AnnotationData aData) {
            super(aData.getAnnotation().getType() + " [" + (aData.getAnnotationSet().getName() == null ? "  " : aData.getAnnotationSet().getName()) + "]");
            putValue(SHORT_DESCRIPTION, aData.getAnnotation().getFeatures().toString());
            this.aData = aData;
        }

        public void actionPerformed(ActionEvent evt) {
            if (annotationEditor == null) return;
            if (annotationEditor.editingFinished()) {
                selectAnnotation(aData);
                annotationEditor.editAnnotation(aData.getAnnotation(), aData.getAnnotationSet());
            }
        }

        private AnnotationData aData;
    }

    protected class SetSelectedAnnotationsAction extends AbstractAction {

        public SetSelectedAnnotationsAction(boolean selected) {
            String title = (selected) ? "Select all" : "Unselect all";
            putValue(NAME, title);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("SPACE"));
            this.selected = selected;
        }

        public void actionPerformed(ActionEvent evt) {
        }

        boolean selected;
    }

    protected class DeleteSelectedAnnotationsAction extends AbstractAction {

        public DeleteSelectedAnnotationsAction(String name) {
            putValue(NAME, name);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
        }

        public void actionPerformed(ActionEvent evt) {
        }
    }

    List<MetaCoDeSetHandler> metacodeSetHandlers;

    /** Contains the data of the main table. */
    List tableRows;

    XJTable mainTable;

    SetsTableModel tableModel;

    JScrollPane scroller;

    JPanel mainPanel;

    TextualDocumentView textView;

    AnnotationListView listView;

    AnnotationStackView stackView;

    MetaCoDeAnnotationListView metacodeView;

    MetaCoDeAnnotationListView2 metacodeView2;

    JTextArea textPane;

    gate.gui.annedit.OwnedAnnotationEditor annotationEditor;

    TestConnectionAction testConnectionAction;

    JTextField connectionStringTextField;

    JLabel itsThesaurusConnectionStatusLbl;

    /**
   * The listener for mouse and mouse motion events in the text view.
   */
    protected TextMouseListener textMouseListener;

    /**
   * Listener for property changes on the text pane.
   */
    protected PropertyChangeListener textChangeListener;

    /**
   * Stores the list of visible annotation types when the view is inactivated 
   * so that the selection can be restored when the view is made active again.
   * The values are String[2] pairs of form <set name, type>.
   */
    protected BlockingQueue<MetaCoDeSetHandler> visibleAnnotationSets;

    protected Timer mouseMovementTimer;

    /**
   * Timer used to handle events coming from the document
   */
    protected Timer eventMinder;

    protected BlockingQueue<GateEvent> pendingEvents;

    private static final int MOUSE_MOVEMENT_TIMER_DELAY = 500;

    protected MouseStoppedMovingAction mouseStoppedMovingAction;

    protected String lastAnnotationType = "_New_";

    protected List actions;

    protected static final ColorGenerator colourGenerator = new ColorGenerator();

    private static final int NAME_COL = 1;

    private static final int SELECTED_COL = 0;

    /**
   * A special GateEvent used as a flag.
   */
    private static final GateEvent END_OF_LIST = new GateEvent(MetaCoDeAnnotationSetsView.class, Integer.MAX_VALUE);

    private static final int EVENTS_HANDLE_DELAY = 300;

    public static boolean containsMetaCoDeType(AnnotationSet set) {
        return set.getAllTypes().contains(MetaCoDeTypeName);
    }

    public static final String MetaCoDeTypeName = "MetaCoDe";
}
