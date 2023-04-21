package org.mcisb.ui.util;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import javax.swing.event.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class DefaultParameterPanel extends ParameterPanel implements ListSelectionListener, DocumentListener, ChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    protected final Map<Object, JLabel> labels = new HashMap<Object, JLabel>();

    /**
	 * 
	 */
    protected final Map<Object, JCheckBox> checkBoxes = new HashMap<Object, JCheckBox>();

    /**
	 * 
	 */
    protected final Map<Object, JList> lists = new HashMap<Object, JList>();

    /**
	 * 
	 */
    protected final Map<Object, JSpinner> spinners = new HashMap<Object, JSpinner>();

    /**
	 * 
	 */
    protected final Map<Object, JTextField> textFields = new HashMap<Object, JTextField>();

    /**
	 * 
	 */
    private final transient Preferences preferences;

    /**
	 * 
	 */
    private transient MouseListener mouseListener;

    /**
	 *
	 * @param title
	 * @param optionsMap
	 * @param passwordMap
	 * @param selectionMode
	 * @param preferences
	 * @param selectable
	 */
    public DefaultParameterPanel(final String title, final Map<Object, Object> optionsMap, final Map<Object, Boolean> passwordMap, final int selectionMode, final Preferences preferences, final boolean selectable) {
        super(title);
        this.preferences = preferences;
        int[] selectionModes = new int[optionsMap.size()];
        Arrays.fill(selectionModes, selectionMode);
        init(optionsMap, passwordMap, selectionModes, selectable);
    }

    /**
	 *
	 * @param title
	 * @param optionsMap
	 * @param preferences
	 * @param selectable
	 */
    public DefaultParameterPanel(final String title, final Map<Object, Object> optionsMap, final Preferences preferences, final boolean selectable) {
        this(title, optionsMap, null, ListSelectionModel.SINGLE_SELECTION, preferences, selectable);
    }

    /**
	 * 
	 * @param title
	 * @param optionsMap
	 * @param passwordMap
	 * @param preferences
	 */
    public DefaultParameterPanel(final String title, final Map<Object, Object> optionsMap, final Map<Object, Boolean> passwordMap, final Preferences preferences) {
        this(title, optionsMap, passwordMap, ListSelectionModel.SINGLE_SELECTION, preferences, true);
    }

    /**
	 * 
	 * @param title
	 * @param optionsMap
	 * @param preferences
	 */
    public DefaultParameterPanel(final String title, final Map<Object, Object> optionsMap, final Preferences preferences) {
        this(title, optionsMap, null, ListSelectionModel.SINGLE_SELECTION, preferences, true);
    }

    /**
	 * 
	 * @param title
	 * @param optionsMap
	 */
    public DefaultParameterPanel(final String title, final Map<Object, Object> optionsMap) {
        this(title, optionsMap, null, ListSelectionModel.SINGLE_SELECTION, null, true);
    }

    /**
	 * 
	 * @param key
	 * @return Object[]
	 */
    public Object[] getSelectedValues(final Object key) {
        JCheckBox checkBox = checkBoxes.get(key);
        JSpinner spinner = spinners.get(key);
        if (checkBox != null) {
            JList list = lists.get(key);
            if (list == null) {
                return new Boolean[] { Boolean.valueOf(checkBox.isSelected()) };
            }
            if (checkBox.isSelected()) {
                if (list.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
                    Object selectedValue = list.getSelectedValue();
                    if (selectedValue != null) {
                        return new Object[] { selectedValue };
                    }
                } else {
                    return list.getSelectedValues();
                }
            }
        } else if (spinner != null) {
            return new Object[] { spinner.getValue() };
        } else {
            JTextField textField = textFields.get(key);
            return new Object[] { textField.getText() };
        }
        return new Object[0];
    }

    @Override
    public void dispose() {
        for (Iterator<Map.Entry<Object, JCheckBox>> iterator = checkBoxes.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, JCheckBox> entry = iterator.next();
            final JCheckBox checkBox = entry.getValue();
            final ItemListener[] itemListeners = checkBox.getItemListeners();
            for (int i = 0; i < itemListeners.length; i++) {
                checkBox.removeItemListener(itemListeners[i]);
            }
            if (preferences != null) {
                preferences.putBoolean(entry.getKey().toString(), checkBox.isSelected());
            }
        }
        for (Iterator<Map.Entry<Object, JList>> iterator = lists.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, JList> entry = iterator.next();
            final JList list = entry.getValue();
            list.getSelectionModel().removeListSelectionListener(this);
            if (!list.isEnabled()) {
                list.clearSelection();
            }
            if (preferences != null) {
                preferences.put(entry.getKey().toString(), Arrays.toString(list.getSelectedIndices()));
            }
        }
        for (Iterator<Map.Entry<Object, JSpinner>> iterator = spinners.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, JSpinner> entry = iterator.next();
            final JSpinner spinner = entry.getValue();
            spinner.removeChangeListener(this);
            if (preferences != null) {
                preferences.put(entry.getKey().toString(), spinner.getValue().toString());
            }
        }
        for (Iterator<Map.Entry<Object, JTextField>> iterator = textFields.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, JTextField> entry = iterator.next();
            final JTextField textField = entry.getValue();
            textField.getDocument().removeDocumentListener(this);
            textField.removeMouseListener(getMouseListener());
            if (!(textField instanceof JPasswordField) && preferences != null) {
                preferences.put(entry.getKey().toString(), textField.getText());
            }
        }
    }

    @Override
    public void valueChanged(final ListSelectionEvent e) {
        setValid();
    }

    @Override
    public void changedUpdate(final DocumentEvent e) {
        setValid();
    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        setValid();
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        setValid();
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        setValid();
    }

    /**
	 * 
	 * @param key
	 * @param selected
	 */
    public void setSelected(final Object key, final boolean selected) {
        checkBoxes.get(key).setSelected(selected);
    }

    /**
	 * 
	 *
	 * @param key
	 * @param enabled
	 */
    public void setEnabled(final Object key, final boolean enabled) {
        checkBoxes.get(key).setEnabled(enabled);
    }

    /**
	 * 
	 *
	 */
    void setValid() {
        for (Iterator<JList> iterator = lists.values().iterator(); iterator.hasNext(); ) {
            JList list = iterator.next();
            if (list.isEnabled() && list.getSelectedValue() == null) {
                setValid(false);
                return;
            }
        }
        for (Iterator<JTextField> iterator = textFields.values().iterator(); iterator.hasNext(); ) {
            JTextField textField = iterator.next();
            String text = textField.getText();
            if (text == null || text.getBytes().length == 0) {
                setValid(false);
                return;
            }
        }
        setValid(true);
    }

    /**
	 *
	 * @param optionsMap
	 * @param passwordMap
	 * @param selectionModes
	 * @param selectable
	 */
    private void init(final Map<Object, Object> optionsMap, final Map<Object, Boolean> passwordMap, final int[] selectionModes, final boolean selectable) {
        int y = 0;
        for (Iterator<Map.Entry<Object, Object>> iterator = optionsMap.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<Object, Object> entry = iterator.next();
            final Object key = entry.getKey();
            final boolean password = (passwordMap == null) ? false : ((passwordMap.get(key) == null) ? false : passwordMap.get(key).booleanValue());
            final boolean lastY = (y == optionsMap.size() - 1);
            final java.util.List<Component> components = new ArrayList<Component>();
            final Object options = entry.getValue();
            Vector<?> checkBoxOptions = null;
            SpinnerModel spinnerModel = null;
            JLabel label = null;
            if (options instanceof Vector<?>) {
                checkBoxOptions = (Vector<?>) options;
            } else if (options instanceof SpinnerModel) {
                spinnerModel = (SpinnerModel) options;
            }
            if (checkBoxOptions != null) {
                JCheckBox checkBox = null;
                if (checkBoxOptions.size() > 0) {
                    final String preference = preferences == null ? null : preferences.get(key.toString(), null);
                    final int[] selectedIndices = CollectionUtils.toIntArray(preference);
                    checkBox = new JCheckBox(key.toString());
                    checkBox.setSelected(!selectable || selectedIndices.length > 0);
                    final JList list = new JList(checkBoxOptions);
                    list.setEnabled(!selectable || checkBox.isSelected());
                    list.setSelectedIndices(selectedIndices);
                    list.setSelectionMode(selectionModes[y]);
                    list.getSelectionModel().addListSelectionListener(this);
                    lists.put(key, list);
                    if (selectable) {
                        checkBox.addItemListener(new ItemListener() {

                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                boolean enabled = e.getStateChange() == ItemEvent.SELECTED;
                                if (enabled && getSelectedValues(key).length == 0) {
                                    final int FIRST = 0;
                                    list.setSelectedIndex(FIRST);
                                }
                                list.setEnabled(enabled);
                                setValid();
                            }
                        });
                    }
                    components.add(checkBox);
                    components.add(new JScrollPane(list));
                    list.ensureIndexIsVisible(CollectionUtils.getFirst(selectedIndices));
                } else {
                    checkBox = new JCheckBox();
                    if (preferences != null) {
                        checkBox.setSelected(preferences.getBoolean(key.toString(), false));
                    }
                    label = new JLabel(key.toString());
                    components.add(label);
                    components.add(checkBox);
                }
                checkBox.setOpaque(false);
                checkBoxes.put(key, checkBox);
            } else if (spinnerModel != null) {
                label = new JLabel(key.toString());
                final JSpinner spinner = new JSpinner(spinnerModel);
                spinner.addChangeListener(this);
                spinners.put(key, spinner);
                components.add(label);
                components.add(spinner);
                final String value = preferences == null ? null : preferences.get(key.toString(), null);
                if (value != null) {
                    if (spinnerModel instanceof SpinnerNumberModel) {
                        spinnerModel.setValue(Double.valueOf(value));
                    } else {
                        spinnerModel.setValue(value);
                    }
                }
            } else {
                label = new JLabel(key.toString());
                final JTextField textField = (password) ? new JPasswordField() : new JTextField();
                textField.addMouseListener(getMouseListener());
                textField.getDocument().addDocumentListener(this);
                textFields.put(key, textField);
                components.add(label);
                components.add(textField);
                if (!password && preferences != null) {
                    textField.setText(preferences.get(key.toString(), null));
                }
            }
            for (int x = 0; x < components.size(); x++) {
                boolean lastX = (x == components.size() - 1);
                final Object component = components.get(x);
                add((Component) component, x, y, lastX, lastX, (component instanceof JScrollPane) || lastY, lastY, (component instanceof JScrollPane) ? GridBagConstraints.BOTH : (lastX ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE));
            }
            if (label != null) {
                labels.put(key, label);
            }
            y++;
        }
        setValid();
    }

    /**
	 * 
	 * @return MouseListener
	 */
    private MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = new JMenuMouseListener(new JTextComponentMenu());
        }
        return mouseListener;
    }
}
