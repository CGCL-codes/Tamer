package net.sourceforge.powerswing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.SpinnerUI;

/**
 * 
 * 
 * @author mkerrigan2
 */
public class PJSpinner extends JComponent {

    /**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "SpinnerUI";

    private static final Action DISABLED_ACTION = new DisabledAction();

    private transient SpinnerModel model;

    private JComponent editor;

    private ChangeListener modelListener;

    private transient ChangeEvent changeEvent;

    private boolean editorExplicitlySet = false;

    /**
     * Constructs a complete spinner with pair of next/previous buttons and an
     * editor for the <code>SpinnerModel</code>.
     */
    public PJSpinner(SpinnerModel model) {
        this.model = model;
        this.editor = createEditor(model);
        updateUI();
    }

    /**
     * Constructs a spinner with an <code>Integer SpinnerNumberModel</code>
     * with initial value 0 and no minimum or maximum limits.
     */
    public PJSpinner() {
        this(new SpinnerNumberModel());
    }

    /**
     * Returns the look and feel (L&F) object that renders this component.
     * 
     * @return the <code>SpinnerUI</code> object that renders this component
     */
    public SpinnerUI getUI() {
        return (SpinnerUI) ui;
    }

    /**
     * Sets the look and feel (L&F) object that renders this component.
     * 
     * @param ui
     *            the <code>SpinnerUI</code> L&F object
     * @see UIDefaults#getUI
     */
    public void setUI(SpinnerUI ui) {
        super.setUI(ui);
    }

    /**
     * Returns the suffix used to construct the name of the look and feel (L&F)
     * class used to render this component.
     * 
     * @return the string "SpinnerUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Resets the UI property with the value from the current look and feel.
     * 
     * @see UIManager#getUI
     */
    public void updateUI() {
        setUI((SpinnerUI) UIManager.getUI(this));
        invalidate();
    }

    /**
     * This method is called by the constructors to create the
     * <code>JComponent</code> that displays the current value of the
     * sequence. The editor may also allow the user to enter an element of the
     * sequence directly. An editor must listen for <code>ChangeEvents</code>
     * on the <code>model</code> and keep the value it displays in sync with
     * the value of the model.
     * <p>
     * Subclasses may override this method to add support for new
     * <code>SpinnerModel</code> classes. Alternatively one can just replace
     * the editor created here with the <code>setEditor</code> method. The
     * default mapping from model type to editor is:
     * <ul>
     * <li><code>SpinnerNumberModel =&gt; PJSpinner.NumberEditor</code>
     * <li><code>SpinnerDateModel =&gt; PJSpinner.DateEditor</code>
     * <li><code>SpinnerListModel =&gt; PJSpinner.ListEditor</code>
     * <li><i>all others </i> =&gt; <code>PJSpinner.DefaultEditor</code>
     * </ul>
     * 
     * @return a component that displays the current value of the sequence
     * @param model
     *            the value of getModel
     * @see #getModel
     * @see #setEditor
     */
    protected JComponent createEditor(SpinnerModel model) {
        if (model instanceof SpinnerNumberModel) {
            return new NumberEditor(this);
        }
        return new DefaultEditor(this);
    }

    /**
     * Changes the model that represents the value of this spinner. If the
     * editor property has not been explicitly set, the editor property is
     * (implicitly) set after the <code>"model"</code>
     * <code>PropertyChangeEvent</code>
     * has been fired. The editor property is set to the value returned by
     * <code>createEditor</code>, as in:
     * 
     * <pre>
     * setEditor(createEditor(model));
     * </pre>
     * 
     * @param model
     *            the new <code>SpinnerModel</code>
     * @see #getModel
     * @see #getEditor
     * @see #setEditor
     * @throws IllegalArgumentException
     *             if model is <code>null</code>
     * 
     * @beaninfo bound: true attribute: visualUpdate true description: Model
     *           that represents the value of this spinner.
     */
    public void setModel(SpinnerModel model) {
        if (model == null) {
            throw new IllegalArgumentException("null model");
        }
        if (!model.equals(this.model)) {
            SpinnerModel oldModel = this.model;
            this.model = model;
            if (modelListener != null) {
                this.model.addChangeListener(modelListener);
            }
            firePropertyChange("model", oldModel, model);
            if (!editorExplicitlySet) {
                setEditor(createEditor(model));
                editorExplicitlySet = false;
            }
            repaint();
            revalidate();
        }
    }

    /**
     * Returns the <code>SpinnerModel</code> that defines this spinners
     * sequence of values.
     * 
     * @return the value of the model property
     * @see #setModel
     */
    public SpinnerModel getModel() {
        return model;
    }

    /**
     * Returns the current value of the model, typically this value is displayed
     * by the <code>editor</code>.
     * <p>
     * This method simply delegates to the <code>model</code>. It is
     * equivalent to:
     * 
     * <pre>
     * getModel().getValue()
     * </pre>
     * 
     * @see #setValue
     * @see SpinnerModel#getValue
     */
    public Object getValue() {
        return getModel().getValue();
    }

    /**
     * Changes current value of the model, typically this value is displayed by
     * the <code>editor</code>. If the <code>SpinnerModel</code>
     * implementation doesn't support the specified value then an
     * <code>IllegalArgumentException</code> is thrown.
     * <p>
     * This method simply delegates to the <code>model</code>. It is
     * equivalent to:
     * 
     * <pre>
     * getModel().setValue(value)
     * </pre>
     * 
     * @throws IllegalArgumentException
     *             if <code>value</code> isn't allowed
     * @see #getValue
     * @see SpinnerModel#setValue
     */
    public void setValue(Object value) {
        getModel().setValue(value);
    }

    /**
     * Returns the object in the sequence that comes after the object returned
     * by <code>getValue()</code>. If the end of the sequence has been
     * reached then return <code>null</code>. Calling this method does not
     * effect <code>value</code>.
     * <p>
     * This method simply delegates to the <code>model</code>. It is
     * equivalent to:
     * 
     * <pre>
     * getModel().getNextValue()
     * </pre>
     * 
     * @return the next legal value or <code>null</code> if one doesn't exist
     * @see #getValue
     * @see #getPreviousValue
     * @see SpinnerModel#getNextValue
     */
    public Object getNextValue() {
        return getModel().getNextValue();
    }

    /**
     * We pass <code>Change</code> events along to the listeners with the the
     * slider (instead of the model itself) as the event source.
     */
    private class ModelListener implements ChangeListener, Serializable {

        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }

    /**
     * Adds a listener to the list that is notified each time a change to the
     * model occurs. The source of <code>ChangeEvents</code> delivered to
     * <code>ChangeListeners</code> will be this <code>PJSpinner</code>.
     * Note also that replacing the model will not affect listeners added
     * directly to PJSpinner. Applications can add listeners to the model
     * directly. In that case is that the source of the event would be the
     * <code>SpinnerModel</code>.
     * 
     * @param listener
     *            the <code>ChangeListener</code> to add
     * @see #removeChangeListener
     * @see #getModel
     */
    public void addChangeListener(ChangeListener listener) {
        if (modelListener == null) {
            modelListener = new ModelListener();
            getModel().addChangeListener(modelListener);
        }
        listenerList.add(ChangeListener.class, listener);
    }

    /**
     * Removes a <code>ChangeListener</code> from this spinner.
     * 
     * @param listener
     *            the <code>ChangeListener</code> to remove
     * @see #fireStateChanged
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    /**
     * Returns an array of all the <code>ChangeListener</code> s added to this
     * PJSpinner with addChangeListener().
     * 
     * @return all of the <code>ChangeListener</code> s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }

    /**
     * Sends a <code>ChangeEvent</code>, whose source is this
     * <code>PJSpinner</code>, to each <code>ChangeListener</code>. When
     * a <code>ChangeListener</code> has been added to the spinner, this
     * method method is called each time a <code>ChangeEvent</code> is
     * received from the model.
     * 
     * @see #addChangeListener
     * @see #removeChangeListener
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Returns the object in the sequence that comes before the object returned
     * by <code>getValue()</code>. If the end of the sequence has been
     * reached then return <code>null</code>. Calling this method does not
     * effect <code>value</code>.
     * <p>
     * This method simply delegates to the <code>model</code>. It is
     * equivalent to:
     * 
     * <pre>
     * getModel().getPreviousValue()
     * </pre>
     * 
     * @return the previous legal value or <code>null</code> if one doesn't
     *         exist
     * @see #getValue
     * @see #getNextValue
     * @see SpinnerModel#getPreviousValue
     */
    public Object getPreviousValue() {
        return getModel().getPreviousValue();
    }

    /**
     * Changes the <code>JComponent</code> that displays the current value of
     * the <code>SpinnerModel</code>. It is the responsibility of this method
     * to <i>disconnect </i> the old editor from the model and to connect the
     * new editor. This may mean removing the old editors
     * <code>ChangeListener</code> from the model or the spinner itself and
     * adding one for the new editor.
     * 
     * @param editor
     *            the new editor
     * @see #getEditor
     * @see #createEditor
     * @see #getModel
     * @throws IllegalArgumentException
     *             if editor is <code>null</code>
     * 
     * @beaninfo bound: true attribute: visualUpdate true description:
     *           JComponent that displays the current value of the model
     */
    public void setEditor(JComponent editor) {
        if (editor == null) {
            throw new IllegalArgumentException("null editor");
        }
        if (!editor.equals(this.editor)) {
            JComponent oldEditor = this.editor;
            this.editor = editor;
            if (oldEditor instanceof DefaultEditor) {
                ((DefaultEditor) oldEditor).dismiss(this);
            }
            editorExplicitlySet = true;
            firePropertyChange("editor", oldEditor, editor);
            revalidate();
            repaint();
        }
    }

    /**
     * Returns the component that displays and potentially changes the model's
     * value.
     * 
     * @return the component that displays and potentially changes the model's
     *         value
     * @see #setEditor
     * @see #createEditor
     */
    public JComponent getEditor() {
        return editor;
    }

    /**
     * Commits the currently edited value to the <code>SpinnerModel</code>.
     * <p>
     * If the editor is an instance of <code>DefaultEditor</code>, the call
     * if forwarded to the editor, otherwise this does nothing.
     * 
     * @throws ParseException
     *             if the currently edited value couldn't be commited.
     */
    public void commitEdit() throws ParseException {
        JComponent editor = getEditor();
        if (editor instanceof DefaultEditor) {
            ((DefaultEditor) editor).commitEdit();
        }
    }

    /**
     * A simple base class for more specialized editors that displays a
     * read-only view of the model's current value with a
     * <code>JFormattedTextField<code>.  Subclasses
     * can configure the <code>JFormattedTextField<code> to create
     * an editor that's appropriate for the type of model they
     * support and they may want to override
     * the <code>stateChanged</code> and <code>propertyChanged</code>
     * methods, which keep the model and the text field in sync.
     * <p>
     * This class defines a <code>dismiss</code> method that removes the
     * editors <code>ChangeListener</code> from the <code>PJSpinner</code>
     * that it's part of.   The <code>setEditor</code> method knows about
     * <code>DefaultEditor.dismiss</code>, so if the developer
     * replaces an editor that's derived from <code>PJSpinner.DefaultEditor</code>
     * its <code>ChangeListener</code> connection back to the 
     * <code>PJSpinner</code> will be removed.  However after that,
     * it's up to the developer to manage their editor listeners.
     * Similarly, if a subclass overrides <code>createEditor</code>,
     * it's up to the subclasser to deal with their editor
     * subsequently being replaced (with <code>setEditor</code>).
     * We expect that in most cases, and in editor installed
     * with <code>setEditor</code> or created by a <code>createEditor</code>
     * override, will not be replaced anyway.
     * <p>
     * This class is the <code>LayoutManager<code> for it's single
     * <code>JFormattedTextField</code> child.   By default the
     * child is just centered with the parents insets.
     */
    public static class DefaultEditor extends JPanel implements ChangeListener, PropertyChangeListener, LayoutManager {

        /**
         * Constructs an editor component for the specified
         * <code>PJSpinner</code>. This <code>DefaultEditor</code> is it's
         * own layout manager and it is added to the spinner's
         * <code>ChangeListener</code> list. The constructor creates a single
         * <code>JFormattedTextField<code> child,
         * initializes it's value to be the spinner model's current value
         * and adds it to <code>this</code> <code>DefaultEditor</code>.  
         * 
         * @param spinner the spinner whose model <code>this</code> editor will monitor
         * @see #getTextField
         * @see PJSpinner#addChangeListener
         */
        public DefaultEditor(PJSpinner spinner) {
            super(null);
            JTextField ftf = new JTextField();
            ftf.setText(spinner.getValue().toString());
            ftf.addPropertyChangeListener(this);
            ftf.setEditable(false);
            add(ftf);
            setLayout(this);
            spinner.addChangeListener(this);
            ActionMap ftfMap = ftf.getActionMap();
            if (ftfMap != null) {
                ftfMap.put("increment", DISABLED_ACTION);
                ftfMap.put("decrement", DISABLED_ACTION);
            }
        }

        /**
         * Disconnect <code>this</code> editor from the specified
         * <code>PJSpinner</code>. By default, this method removes itself
         * from the spinners <code>ChangeListener</code> list.
         * 
         * @param spinner
         *            the <code>PJSpinner</code> to disconnect this editor
         *            from; the same spinner as was passed to the constructor.
         */
        public void dismiss(PJSpinner spinner) {
            spinner.removeChangeListener(this);
        }

        /**
         * Returns the <code>PJSpinner</code> ancestor of this editor or
         * null. Typically the editor's parent is a <code>PJSpinner</code>
         * however subclasses of <codeJSpinner</code> may override the the
         * <code>createEditor</code> method and insert one or more containers
         * between the <code>PJSpinner</code> and it's editor.
         * 
         * @return <code>PJSpinner</code> ancestor
         * @see PJSpinner#createEditor
         */
        public PJSpinner getSpinner() {
            for (Component c = this; c != null; c = c.getParent()) {
                if (c instanceof PJSpinner) {
                    return (PJSpinner) c;
                }
            }
            return null;
        }

        /**
         * Returns the <code>JFormattedTextField</code> child of this editor.
         * By default the text field is the first and only child of editor.
         * 
         * @return the <code>JFormattedTextField</code> that gives the user
         *         access to the <code>SpinnerDateModel's</code> value.
         * @see #getSpinner
         * @see #getModel
         */
        public JTextField getTextField() {
            return (JTextField) getComponent(0);
        }

        /**
         * This method is called when the spinner's model's state changes. It
         * sets the <code>value</code> of the text field to the current value
         * of the spinners model.
         * 
         * @param e
         *            not used
         * @see #getTextField
         * @see PJSpinner#getValue
         */
        public void stateChanged(ChangeEvent e) {
            PJSpinner spinner = (PJSpinner) (e.getSource());
            getTextField().setText(spinner.getValue().toString());
        }

        /**
         * Called by the <code>JFormattedTextField</code> 
         * <code>PropertyChangeListener</code>.
         * When the <code>"value"</code> property changes, which implies that
         * the user has typed a new number, we set the value of the spinners
         * model.
         * <p>
         * This class ignores <code>PropertyChangeEvents</code> whose source
         * is not the <code>JFormattedTextField</code>, so subclasses may
         * safely make <code>this</code> <code>DefaultEditor</code> a
         * <code>PropertyChangeListener</code> on other objects.
         * 
         * @param e
         *            the <code>PropertyChangeEvent</code> whose source is the
         *            <code>JFormattedTextField</code> created by this class.
         * @see #getTextField
         */
        public void propertyChange(PropertyChangeEvent e) {
            PJSpinner spinner = getSpinner();
            if (spinner == null) {
                return;
            }
            Object source = e.getSource();
            String name = e.getPropertyName();
            if ((source instanceof JTextField) && "value".equals(name)) {
                Object lastValue = spinner.getValue();
                try {
                    spinner.setValue(getTextField().getText());
                } catch (IllegalArgumentException iae) {
                    try {
                        ((JTextField) source).setText(lastValue.toString());
                    } catch (IllegalArgumentException iae2) {
                    }
                }
            }
        }

        /**
         * This <code>LayoutManager</code> method does nothing. We're only
         * managing a single child and there's no support for layout
         * constraints.
         * 
         * @param name
         *            ignored
         * @param child
         *            ignored
         */
        public void addLayoutComponent(String name, Component child) {
        }

        /**
         * This <code>LayoutManager</code> method does nothing. There isn't
         * any per-child state.
         * 
         * @param child
         *            ignored
         */
        public void removeLayoutComponent(Component child) {
        }

        /**
         * Returns the size of the parents insets.
         */
        private Dimension insetSize(Container parent) {
            Insets insets = parent.getInsets();
            int w = insets.left + insets.right;
            int h = insets.top + insets.bottom;
            return new Dimension(w, h);
        }

        /**
         * Returns the preferred size of first (and only) child plus the size of
         * the parents insets.
         * 
         * @param parent
         *            the Container that's managing the layout
         * @return the preferred dimensions to lay out the subcomponents of the
         *         specified container.
         */
        public Dimension preferredLayoutSize(Container parent) {
            Dimension preferredSize = insetSize(parent);
            if (parent.getComponentCount() > 0) {
                Dimension childSize = getComponent(0).getPreferredSize();
                preferredSize.width += childSize.width;
                preferredSize.height += childSize.height;
            }
            return preferredSize;
        }

        /**
         * Returns the minimum size of first (and only) child plus the size of
         * the parents insets.
         * 
         * @param parent
         *            the Container that's managing the layout
         * @return the minimum dimensions needed to lay out the subcomponents of
         *         the specified container.
         */
        public Dimension minimumLayoutSize(Container parent) {
            Dimension minimumSize = insetSize(parent);
            if (parent.getComponentCount() > 0) {
                Dimension childSize = getComponent(0).getMinimumSize();
                minimumSize.width += childSize.width;
                minimumSize.height += childSize.height;
            }
            return minimumSize;
        }

        /**
         * Resize the one (and only) child to completely fill the area within
         * the parents insets.
         */
        public void layoutContainer(Container parent) {
            if (parent.getComponentCount() > 0) {
                Insets insets = parent.getInsets();
                int w = parent.getWidth() - (insets.left + insets.right);
                int h = parent.getHeight() - (insets.top + insets.bottom);
                getComponent(0).setBounds(insets.left, insets.top, w, h);
            }
        }

        /**
         * Pushes the currently edited value to the <code>SpinnerModel</code>.
         * <p>
         * The default implementation invokes <code>commitEdit</code> on the
         * <code>JFormattedTextField</code>.
         * 
         * @throw ParseException if the edited value is not legal
         */
        public void commitEdit() throws ParseException {
        }
    }

    public static class NumberEditor extends DefaultEditor {

        /**
         * Construct a <code>PJSpinner</code> editor that supports displaying
         * and editing the value of a <code>SpinnerNumberModel</code> with a
         * <code>JFormattedTextField</code>.<code>This</code>
         * <code>NumberEditor</code>
         * becomes both a <code>ChangeListener</code> on the spinner and a
         * <code>PropertyChangeListener</code> on the new
         * <code>JFormattedTextField</code>.
         * 
         * @param spinner
         *            the spinner whose model <code>this</code> editor will
         *            monitor
         * @exception IllegalArgumentException
         *                if the spinners model is not an instance of
         *                <code>SpinnerNumberModel</code>
         * 
         * @see #getModel
         * @see #getFormat
         * @see SpinnerNumberModel
         */
        public NumberEditor(PJSpinner spinner) {
            this(spinner, new DecimalFormat());
        }

        /**
         * Construct a <code>PJSpinner</code> editor that supports displaying
         * and editing the value of a <code>SpinnerNumberModel</code> with a
         * <code>JFormattedTextField</code>.<code>This</code>
         * <code>NumberEditor</code>
         * becomes both a <code>ChangeListener</code> on the spinner and a
         * <code>PropertyChangeListener</code> on the new
         * <code>JFormattedTextField</code>.
         * 
         * @param spinner
         *            the spinner whose model <code>this</code> editor will
         *            monitor
         * @param decimalFormatPattern
         *            the initial pattern for the <code>DecimalFormat</code>
         *            object that's used to display and parse the value of the
         *            text field.
         * @exception IllegalArgumentException
         *                if the spinners model is not an instance of
         *                <code>SpinnerNumberModel</code> or if
         *                <code>decimalFormatPattern</code> is not a legal
         *                argument to <code>DecimalFormat</code>
         * 
         * @see #getTextField
         * @see SpinnerNumberModel
         * @see java.text.DecimalFormat
         */
        public NumberEditor(PJSpinner spinner, String decimalFormatPattern) {
            this(spinner, new DecimalFormat(decimalFormatPattern));
        }

        /**
         * Construct a <code>PJSpinner</code> editor that supports displaying
         * and editing the value of a <code>SpinnerNumberModel</code> with a
         * <code>JFormattedTextField</code>.<code>This</code>
         * <code>NumberEditor</code>
         * becomes both a <code>ChangeListener</code> on the spinner and a
         * <code>PropertyChangeListener</code> on the new
         * <code>JFormattedTextField</code>.
         * 
         * @param spinner
         *            the spinner whose model <code>this</code> editor will
         *            monitor
         * @param decimalFormatPattern
         *            the initial pattern for the <code>DecimalFormat</code>
         *            object that's used to display and parse the value of the
         *            text field.
         * @exception IllegalArgumentException
         *                if the spinners model is not an instance of
         *                <code>SpinnerNumberModel</code>
         * 
         * @see #getTextField
         * @see SpinnerNumberModel
         * @see java.text.DecimalFormat
         */
        private NumberEditor(PJSpinner spinner, DecimalFormat format) {
            super(spinner);
            format.getMultiplier();
            if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
                throw new IllegalArgumentException("model not a SpinnerNumberModel");
            }
            JTextField ftf = getTextField();
            ftf.setEditable(true);
        }

        /**
         * Return our spinner ancestor's <code>SpinnerNumberModel</code>.
         * 
         * @return <code>getSpinner().getModel()</code>
         * @see #getSpinner
         * @see #getTextField
         */
        public SpinnerNumberModel getModel() {
            return (SpinnerNumberModel) (getSpinner().getModel());
        }
    }

    /**
     * An Action implementation that is always disabled.
     */
    private static class DisabledAction implements Action {

        public Object getValue(String key) {
            return null;
        }

        public void putValue(String key, Object value) {
        }

        public void setEnabled(boolean b) {
        }

        public boolean isEnabled() {
            return false;
        }

        public void addPropertyChangeListener(PropertyChangeListener l) {
        }

        public void removePropertyChangeListener(PropertyChangeListener l) {
        }

        public void actionPerformed(ActionEvent ae) {
        }
    }
}
