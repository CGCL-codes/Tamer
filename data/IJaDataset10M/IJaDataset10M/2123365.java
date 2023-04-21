package edu.rice.cs.drjava.ui.config;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;
import edu.rice.cs.drjava.config.*;
import edu.rice.cs.drjava.DrJava;
import edu.rice.cs.plt.lambda.Lambda;
import edu.rice.cs.util.swing.SwingFrame;
import edu.rice.cs.util.swing.Utilities;

/** The graphical form of an Option. Provides a way to see the values of Option while running DrJava and perform live 
  * updating of Options.
  * @version $Id: OptionComponent.java 5232 2010-04-24 00:14:05Z mgricken $
  */
public abstract class OptionComponent<T, C extends JComponent> implements Serializable {

    protected final Option<T> _option;

    protected final JLabel _label;

    protected final SwingFrame _parent;

    protected volatile boolean _entireColumn;

    protected volatile String _labelText;

    protected volatile C _guiComponent;

    public OptionComponent(Option<T> option, String labelText, SwingFrame parent) {
        _option = option;
        _labelText = labelText;
        _label = new JLabel(_labelText);
        _label.setHorizontalAlignment(JLabel.RIGHT);
        _parent = parent;
        if (option != null) {
            DrJava.getConfig().addOptionListener(option, new OptionListener<T>() {

                public void optionChanged(OptionEvent<T> oe) {
                    resetToCurrent();
                }
            });
        }
    }

    /** Special constructor for degenerate option components does not take an option.
   *  @param labelText Text for descriptive label of this option.
   *  @param parent The parent frame.
   */
    public OptionComponent(String labelText, SwingFrame parent) {
        this(null, labelText, parent);
    }

    public Option<T> getOption() {
        return _option;
    }

    public String getLabelText() {
        return _label.getText();
    }

    public JLabel getLabel() {
        return _label;
    }

    public boolean useEntireColumn() {
        return _entireColumn;
    }

    /** Returns the JComponent to display for this OptionComponent. */
    public C getComponent() {
        return _guiComponent;
    }

    /** Set the JComponent to display for this OptionComponent.
    * @param component GUI component */
    public void setComponent(C component) {
        _guiComponent = component;
        if ((_guiComponent != null) && (_option != null)) {
            _guiComponent.setEnabled(DrJava.getConfig().isEditable(_option));
            for (Component subComponent : _guiComponent.getComponents()) {
                subComponent.setEnabled(DrJava.getConfig().isEditable(_option));
            }
        }
    }

    /** Sets the detailed description text for all components in this OptionComponent.
   *  Should be called by subclasses that wish to display a description.
   *  @param description the description of the component
   */
    public abstract void setDescription(String description);

    /** Whether the component should occupy the entire column. */
    public OptionComponent<T, C> setEntireColumn(boolean entireColumn) {
        _entireColumn = entireColumn;
        return this;
    }

    /** Whether the component should occupy the entire column. */
    public boolean getEntireColumn() {
        return _entireColumn;
    }

    /** Updates the appropriate configuration option with the new value if different from the old one and legal. Any 
    * changes should be done immediately so that current and future references to the Option should reflect the changes.
    * @return false, if value is invalid; otherwise true.  This method may spawn asynchronous event thread tasks via
    * firing OptionListeners and textField DocumentListeners.
    */
    public abstract boolean updateConfig();

    /** Resets the entry field to reflect the actual stored value for the option. */
    public void resetToCurrent() {
        if (_option != null) setValue(DrJava.getConfig().getSetting(_option));
    }

    /** Resets the actual value of the component to the original default. */
    public void resetToDefault() {
        if (_option != null) {
            setValue(_option.getDefault());
            notifyChangeListeners();
        }
    }

    /** Sets the value that is currently displayed by this component. */
    public abstract void setValue(T value);

    public void showErrorMessage(String title, OptionParseException e) {
        showErrorMessage(title, e.value, e.message);
    }

    public void showErrorMessage(String title, String value, String message) {
        JOptionPane.showMessageDialog(_parent, "There was an error in one of the options that you entered.\n" + "Option: '" + getLabelText() + "'\n" + "Your value: '" + value + "'\n" + "Error: " + message, title, JOptionPane.WARNING_MESSAGE);
    }

    /** Interface for change listener. */
    public static interface ChangeListener extends Lambda<Object, Object> {
    }

    /** Adds a change listener to this component.
    * @param listener listener to add
    */
    public void addChangeListener(ChangeListener listener) {
        _changeListeners.add(listener);
    }

    /** Removes a change listener to this component.
    * @param listener listener to remove
    */
    public void removeChangeListener(ChangeListener listener) {
        _changeListeners.remove(listener);
    }

    /** Notify all change listeners of a change. Notification performed in the event thread. */
    protected void notifyChangeListeners() {
        assert _parent.duringInit() || Utilities.TEST_MODE || EventQueue.isDispatchThread();
        ChangeListener[] listeners = _changeListeners.toArray(new ChangeListener[_changeListeners.size()]);
        for (ChangeListener l : listeners) l.value(OptionComponent.this);
    }

    /** List of change listeners.  A volatile Vector is used here because a race involving operations on this field was 
    * encountered in MainFrameTest during _frame.pack() in initialization. It previously was a nonvolatile ArrayList. */
    private volatile Vector<ChangeListener> _changeListeners = new Vector<ChangeListener>();
}
