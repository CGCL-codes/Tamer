package cswilly.jeditPlugins.spell;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.*;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.Mode;
import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.MiscUtilities;
import org.gjt.sp.util.StandardUtilities;
import common.gui.FileTextField;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import cswilly.spell.SpellException;
import cswilly.spell.FutureListModes;
import static cswilly.jeditPlugins.spell.SpellCheckPlugin.*;
import static cswilly.jeditPlugins.spell.AspellEngineManager.*;

/**
 * manage properties for:
 *  - executable to invoke
 *  - modes handling (auto/none/manual)
 *  - jEdit mode <-> Aspell filter handling
 *  - additional parameters
 */
public class AspellOptionPane extends AbstractOptionPane {

    static final String REFRESH_BUTTON_START = "options.SpellCheck.refresh-langs";

    static final String REFRESH_BUTTON_STOP = "options.SpellCheck.stop-refresh-langs";

    static final String BROWSE = "browse";

    private PropertyStore propertyStore;

    private static final int ASPELL_OPTION_VERTICAL_STRUT = 7;

    private JTextArea _filtersDescription;

    public AspellOptionPane() {
        super("spellcheck.aspell");
    }

    public void _init() {
        propertyStore = new PropertyStore(this);
        String aspellExecutable = AspellEngineManager.getAspellExeFilename();
        propertyStore.put(ASPELL_EXE_PROP, aspellExecutable);
        AspellMarkupMode modeValue = AspellMarkupMode.AUTO_MARKUP_MODE;
        try {
            modeValue = AspellMarkupMode.fromString(jEdit.getProperty(ASPELL_MARKUP_MODE_PROP, ""));
        } catch (IllegalArgumentException iae) {
        }
        propertyStore.put(ASPELL_MARKUP_MODE_PROP, modeValue.toString());
        String otherParams = jEdit.getProperty(ASPELL_OTHER_PARAMS_PROP, "");
        propertyStore.put(ASPELL_OTHER_PARAMS_PROP, otherParams);
        String encoding = jEdit.getProperty(ASPELL_ENCODING_PROP, "UTF-8");
        propertyStore.put(ASPELL_ENCODING_PROP, encoding);
        boolean check = Boolean.parseBoolean(jEdit.getProperty(ASPELL_CHECK_POS_PROP, "false"));
        propertyStore.put(ASPELL_CHECK_POS_PROP, String.valueOf(check));
        JLabel _aspellExeFilenameLabel = new JLabel();
        _aspellExeFilenameLabel.setText(jEdit.getProperty("options.SpellCheck.aspellExe"));
        addComponent(_aspellExeFilenameLabel);
        FileActionHandler _aspellExeFilenameComp = new FileActionHandler(aspellExecutable);
        JTextField _aspellExeFilenameField = _aspellExeFilenameComp.getTextField();
        _aspellExeFilenameField.setColumns(25);
        _aspellExeFilenameField.setName("AspellPath");
        TextFieldHandler exeHandler = new TextFieldHandler(ASPELL_EXE_PROP);
        _aspellExeFilenameField.addFocusListener(exeHandler);
        _aspellExeFilenameField.addActionListener(exeHandler);
        addComponent(_aspellExeFilenameComp);
        addComponent(Box.createVerticalStrut(ASPELL_OPTION_VERTICAL_STRUT));
        String[] encodings = MiscUtilities.getEncodings(true);
        java.util.Arrays.sort(encodings, new StandardUtilities.StringCompare(true));
        JComboBox encodingCombo = new JComboBox(encodings);
        encodingCombo.setEditable(true);
        encodingCombo.setSelectedItem(encoding);
        TextFieldHandler encodingHandler = new TextFieldHandler(ASPELL_ENCODING_PROP);
        encodingCombo.addFocusListener(encodingHandler);
        encodingCombo.addActionListener(encodingHandler);
        addComponent(jEdit.getProperty("options.SpellCheck.aspell-encoding"), encodingCombo);
        JCheckBox checkOffsetsBox = new JCheckBox(jEdit.getProperty("options.SpellCheck.aspell-check-pos"));
        checkOffsetsBox.setSelected(check);
        checkOffsetsBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                propertyStore.put(ASPELL_CHECK_POS_PROP, String.valueOf(((AbstractButton) ae.getSource()).isSelected()));
            }
        });
        checkOffsetsBox.setToolTipText(jEdit.getProperty("options.SpellCheck.aspell-check-pos.tooltip"));
        addComponent(checkOffsetsBox);
        JPanel listingPanel = new JPanel(new BorderLayout(5, 0));
        MutableComboBoxModel model2 = new DefaultComboBoxModel();
        _filtersDescription = new JTextArea(jEdit.getProperty("options.SpellCheck.filtersDescription.default"));
        final AspellCapabilitiesAction actionRefresh = new AspellCapabilitiesAction(model2);
        propertyStore.addPropertyChangeListener(ASPELL_EXE_PROP, actionRefresh);
        JButton _refreshButton = new JButton(actionRefresh);
        _refreshButton.setName("Refresh");
        listingPanel.add(_refreshButton, BorderLayout.EAST);
        addComponent(listingPanel);
        addComponent(Box.createVerticalStrut(ASPELL_OPTION_VERTICAL_STRUT));
        JLabel _aspellMarkupModeLabel = new JLabel();
        _aspellMarkupModeLabel.setText(jEdit.getProperty("options.SpellCheck.aspellMarkupMode"));
        addComponent(_aspellMarkupModeLabel);
        ButtonGroup _markupModeGroup = new ButtonGroup();
        JPanel _markupModePanel = new JPanel(new BorderLayout(5, 0));
        final EnumMap<AspellMarkupMode, String> positions = new EnumMap<AspellMarkupMode, String>(AspellMarkupMode.class);
        positions.put(AspellMarkupMode.NO_MARKUP_MODE, BorderLayout.NORTH);
        positions.put(AspellMarkupMode.AUTO_MARKUP_MODE, BorderLayout.CENTER);
        positions.put(AspellMarkupMode.MANUAL_MARKUP_MODE, BorderLayout.SOUTH);
        for (final AspellMarkupMode mode : AspellMarkupMode.values()) {
            JRadioButton _aspellMarkupMode = new JRadioButton();
            _aspellMarkupMode.setName(mode.toString());
            _aspellMarkupMode.setText(jEdit.getProperty("options.SpellCheck." + mode.toString()));
            _aspellMarkupMode.setToolTipText(jEdit.getProperty("options.SpellCheck." + mode.toString() + ".tooltip"));
            _aspellMarkupMode.setSelected(mode == modeValue);
            _aspellMarkupMode.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        propertyStore.put(ASPELL_MARKUP_MODE_PROP, mode.toString());
                    }
                }
            });
            _markupModeGroup.add(_aspellMarkupMode);
            _markupModePanel.add(_aspellMarkupMode, positions.get(mode));
        }
        addComponent(_markupModePanel);
        JPanel filtersPanel = new JPanel(new BorderLayout());
        JComponent table = createModesSelector(model2);
        filtersPanel.add(table, BorderLayout.WEST);
        filtersPanel.add(new JScrollPane(_filtersDescription), BorderLayout.CENTER);
        addComponent(filtersPanel);
        addComponent(Box.createVerticalStrut(ASPELL_OPTION_VERTICAL_STRUT));
        JLabel _aspellOtherParamsLabel = new JLabel();
        _aspellOtherParamsLabel.setText(jEdit.getProperty("options.SpellCheck.aspellOtherParams"));
        addComponent(_aspellOtherParamsLabel);
        final JTextField _aspellOtherParamsField = new JTextField(25);
        _aspellOtherParamsField.setName("AdditionalParameters");
        _aspellOtherParamsField.setText(otherParams);
        TextFieldHandler otherParamsHandler = new TextFieldHandler(ASPELL_OTHER_PARAMS_PROP);
        _aspellOtherParamsField.addActionListener(otherParamsHandler);
        _aspellOtherParamsField.addFocusListener(otherParamsHandler);
        addComponent(_aspellOtherParamsField);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                actionRefresh.actionPerformed(null);
            }
        });
    }

    public void _save() {
        jEdit.setProperty(ASPELL_EXE_PROP, propertyStore.get(ASPELL_EXE_PROP));
        jEdit.setProperty(ASPELL_MARKUP_MODE_PROP, propertyStore.get(ASPELL_MARKUP_MODE_PROP));
        jEdit.setProperty(ASPELL_OTHER_PARAMS_PROP, propertyStore.get(ASPELL_OTHER_PARAMS_PROP));
        model.save();
    }

    private ModeTableModel model;

    private JScrollPane createModesSelector(final MutableComboBoxModel comboModel) {
        model = new ModeTableModel();
        JTable table = new JTable(model);
        table.setName("filtersTable");
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        for (String filter : model.getUsedFilters()) comboModel.addElement(filter);
        final JComboBox comboBox = new JComboBox(comboModel);
        comboBox.setName("filtersCombo");
        table.setRowHeight(comboBox.getPreferredSize().height);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellEditor(new DefaultCellEditor(comboBox));
        column.setPreferredWidth(comboBox.getPreferredSize().width);
        Dimension d = table.getPreferredSize();
        d.height = Math.min(d.height, 200);
        JScrollPane scroller = new JScrollPane(table);
        scroller.setPreferredSize(d);
        return scroller;
    }

    private class TextFieldHandler extends FocusAdapter implements ActionListener {

        private String propName;

        TextFieldHandler(String propName) {
            this.propName = propName;
        }

        public void actionPerformed(ActionEvent evt) {
            String value = ((JTextComponent) evt.getSource()).getText().trim();
            propertyStore.put(propName, value);
        }

        public void focusLost(FocusEvent e) {
            String value = ((JTextComponent) e.getSource()).getText().trim();
            propertyStore.put(propName, value);
        }
    }

    private class FileActionHandler extends FileTextField {

        FileActionHandler(String text) {
            super(text, true);
        }

        public void actionPerformed(ActionEvent evt) {
            super.actionPerformed(evt);
            String path = getTextField().getText();
            propertyStore.put(ASPELL_EXE_PROP, path);
        }
    }

    private class AspellCapabilitiesAction extends AbstractAction implements PropertyChangeListener {

        private MutableComboBoxModel modelFilters;

        private Future<Map<String, String>> futureModes;

        AspellCapabilitiesAction(MutableComboBoxModel modelFilters) {
            super(jEdit.getProperty(REFRESH_BUTTON_START));
            this.modelFilters = modelFilters;
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent evt) {
            if (futureModes == null) {
                refreshList();
            } else {
                setEnabled(false);
                futureModes.cancel(true);
                futureModes = null;
            }
        }

        public void propertyChange(PropertyChangeEvent e) {
            if (ASPELL_EXE_PROP.equals(e.getPropertyName())) {
                if (!isEnabled()) {
                    setEnabled(true);
                }
            }
        }

        /**
	 * executes a FutureListModes in a new Thread.
	 * finally, update the combo model to contain all found modes
	 * the futureModes variable serves both to cancel in response to user action on Stop! button
	 * and to flag that some work is done (while it's not null).
	 * The aspell executable used is the one currently configured, even if not applied yet.
	**/
        private void refreshList() {
            new Thread() {

                public void run() {
                    putValue(Action.NAME, jEdit.getProperty(REFRESH_BUTTON_STOP));
                    String listing = jEdit.getProperty(SpellCheckOptionPane.LISTING, "");
                    final Map<String, String> filters = new HashMap<String, String>();
                    FutureListModes ftModes = new FutureListModes(propertyStore.get(ASPELL_EXE_PROP));
                    futureModes = ftModes;
                    setEnabled(true);
                    try {
                        filters.putAll(ftModes.get(10, TimeUnit.SECONDS));
                    } catch (InterruptedException ie) {
                        Log.log(Log.ERROR, AspellOptionPane.this, ie);
                        GUIUtilities.error(AspellOptionPane.this, "ioerror", new String[] { "Interrupted while listing modes" });
                        ftModes.cancel(true);
                    } catch (CancellationException ce) {
                    } catch (ExecutionException ee) {
                        Log.log(Log.ERROR, AspellOptionPane.this, ee);
                        GUIUtilities.error(AspellOptionPane.this, "ioerror", new String[] { jEdit.getProperty("list-dict-error.message", new Object[] { ee.getCause().getMessage() }) });
                        ftModes.cancel(true);
                    } catch (TimeoutException te) {
                        Log.log(Log.ERROR, AspellOptionPane.this, te);
                        GUIUtilities.error(AspellOptionPane.this, "ioerror", new String[] { jEdit.getProperty("list-dict-timeout-error.message") });
                        ftModes.cancel(true);
                    }
                    if (!filters.isEmpty()) Log.log(Log.DEBUG, AspellOptionPane.this, "finished refreshList (" + filters.size() + " filters found)"); else Log.log(Log.ERROR, AspellOptionPane.this, "no filter found)");
                    putValue(Action.NAME, jEdit.getProperty(REFRESH_BUTTON_START));
                    setEnabled(true);
                    futureModes = null;
                    updateModels(filters);
                }
            }.start();
        }

        private void updateModels(final Map<String, String> modes) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    while (modelFilters.getSize() != 0) modelFilters.removeElementAt(0);
                    String text;
                    if (modes.isEmpty()) {
                        text = jEdit.getProperty("options.SpellCheck.filtersDescription.error");
                    } else text = jEdit.getProperty("options.SpellCheck.filtersDescription.prolog");
                    for (Map.Entry<String, String> mode : modes.entrySet()) {
                        modelFilters.addElement(mode.getKey());
                        text += mode.getKey() + " : " + mode.getValue() + "\n";
                    }
                    modelFilters.addElement(FILTER_AUTO);
                    _filtersDescription.setText(text);
                }
            });
        }
    }
}

class ModeTableModel extends AbstractTableModel {

    private ArrayList<Entry> modes;

    private java.util.List<String> usedFilters;

    ModeTableModel() {
        Mode[] _modes = jEdit.getModes();
        modes = new ArrayList<Entry>(_modes.length);
        usedFilters = new ArrayList<String>(10);
        for (int i = 0; i < _modes.length; i++) {
            Entry e = new Entry(_modes[i].getName());
            modes.add(e);
            if (!usedFilters.contains(e.filter)) usedFilters.add(e.filter);
        }
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return modes.size();
    }

    public Class getColumnClass(int col) {
        return String.class;
    }

    public Object getValueAt(int row, int col) {
        Entry mode = (Entry) modes.get(row);
        switch(col) {
            case 0:
                return mode.name;
            case 1:
                return mode.filter;
            default:
                throw new InternalError();
        }
    }

    public boolean isCellEditable(int row, int col) {
        return (col != 0);
    }

    public void setValueAt(Object value, int row, int col) {
        if (col == 0) return;
        Entry mode = (Entry) modes.get(row);
        switch(col) {
            case 1:
                mode.filter = (String) value;
                break;
            default:
                throw new InternalError();
        }
        fireTableRowsUpdated(row, row);
    }

    public String getColumnName(int index) {
        switch(index) {
            case 0:
                return null;
            case 1:
                return null;
            default:
                throw new InternalError();
        }
    }

    public java.util.List<String> getUsedFilters() {
        return usedFilters;
    }

    public void save() {
        for (int i = 0; i < modes.size(); i++) {
            ((Entry) modes.get(i)).save();
        }
    }
}

class Entry {

    String name;

    String filter;

    Entry(String name) {
        this.name = name;
        filter = jEdit.getProperty(FILTERS_PROP + "." + name, FILTER_AUTO);
    }

    void save() {
        if (!FILTER_AUTO.equals(filter)) jEdit.setProperty(FILTERS_PROP + "." + name, filter); else jEdit.unsetProperty(FILTERS_PROP + "." + name);
    }
}
