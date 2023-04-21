package org.form4j.form.field.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;
import org.form4j.form.field.Field;
import org.form4j.form.field.action.ActionTrigger;
import org.form4j.form.main.Form;
import org.form4j.form.util.PropertyUtil;
import org.form4j.form.util.StringUtil;
import org.form4j.form.util.tip.ToolTipUtil;
import org.w3c.dom.Element;

/**
 * Abstract textfield with attachable action Form control.
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.23 $ $Date: 2009/01/25 05:57:46 $
 **/
public abstract class TextWithAction extends AbstractDataField implements DocumentListener, FocusListener, MouseListener, ActionTrigger {

    /**
     * Constructs abstract text field with associated action.
     * @param form the parent form4j
     * @param desc the XML field descriptor
     * @throws Exception Exception
     */
    public TextWithAction(final Form form, final Element desc) throws Exception {
        super(form, desc);
        JTextField component = new JTextField() {

            private int toolTipModifiers = 0;

            public Point getToolTipLocation(final MouseEvent event) {
                toolTipModifiers = event.getModifiers();
                return super.getToolTipLocation(event);
            }

            public JToolTip createToolTip() {
                return ToolTipUtil.createToolTip(TextWithAction.this, toolTipModifiers);
            }

            public Dimension getPreferredSize() {
                return (isVisible() ? super.getPreferredSize() : new Dimension(0, 0));
            }
        };
        if (ToolTipUtil.isToolTipDebugActive()) {
            component.setToolTipText(ToolTipUtil.TOOLTIP_PLACEHOLDER);
        }
        setComponent(component);
        component.setHorizontalAlignment(determineAlignment(desc));
        maxLength = Integer.parseInt(desc.getAttribute("maxLength"));
        component.setDocument(new LimitingDocument(getMaxLength(), getForm(), this));
        if (getFieldDescriptor().getAttribute("modelTriggering").equals("item")) ((JTextField) getComponent()).getDocument().addDocumentListener(this); else if (getFieldDescriptor().getAttribute("modelTriggering").equals("focus")) ((JTextField) getComponent()).addFocusListener(this); else if (getFieldDescriptor().getAttribute("modelTriggering").equals("focusAndItem")) {
            ((JTextField) getComponent()).getDocument().addDocumentListener(this);
            ((JTextField) getComponent()).addFocusListener(this);
        }
        ((JTextField) getComponent()).addKeyListener(this);
        init();
        boolean textFieldReadOnly = desc.getAttribute("textFieldReadOnly").equals("true");
        if (textFieldReadOnly) ((JTextField) getComponent()).setEditable(false);
    }

    /**
     * Process the action associated with this Field.
     * abstract method to override by subclass.
     * @param parent the parent 'text with action' field
     */
    public abstract void processAction(final TextWithAction parent);

    /**
     * set the data for this field.
     * @param data the data for this field
     */
    public void setData(final Object data) {
        String sData = handleEmpty(data);
        LOG.debug("Data:      '" + data + "'");
        ((JTextField) getComponent()).setText(StringUtil.normalizeString((String) sData));
    }

    /**
     * get the data for this field.
     * @return the data for this field
     */
    public Object getData() {
        LOG.debug("DATA '" + ((JTextField) getComponent()).getText() + "'");
        return ((JTextField) getComponent()).getText();
    }

    /**
     * get the maximum allowed text-length for this textfield.
     * @return the maximum allowed text-length for this textfield
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * @param length the maximum allowed text-length for this textfield.
     */
    public void setMaxLength(final int length) {
        maxLength = length;
    }

    public void setBorder(boolean paintBorder) {
        if (!paintBorder) ((JTextField) getComponent()).setBorder(null);
    }

    /** called when focus is gained.
     * @param evt the focus event
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(final FocusEvent evt) {
    }

    /**
     * called when focus is lost.
     * @param evt the focus event
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(final FocusEvent evt) {
        triggerModelChange();
    }

    /**
     * fire all action listeners with the given action event.
     * @param evt action event
     */
    public void fireActions(final ActionEvent evt) {
        LOG.debug(evt, new Throwable());
        try {
            for (int action = 0; action < actionListeners.size(); action++) {
                LOG.debug("firing ActionListener " + actionListeners.elementAt(action) + " Data before >>" + getData() + "<<");
                ((ActionListener) actionListeners.elementAt(action)).actionPerformed(evt);
                LOG.debug("Data after >>" + getData() + "<<");
            }
        } catch (Exception e) {
            LOG.info(e);
        }
    }

    /**
     * called on document changed updated.
     * @param evt the document event
     */
    public void changedUpdate(final DocumentEvent evt) {
        LOG.debug(evt);
        documentChange(evt);
    }

    /**
     * called on document insert update.
     * @param evt the document event
     */
    public void insertUpdate(final DocumentEvent evt) {
        LOG.debug(evt);
        documentChange(evt);
    }

    /**
     * called on document remove update.
     * @param evt the document event
     */
    public void removeUpdate(final DocumentEvent evt) {
        LOG.debug(evt);
        documentChange(evt);
    }

    private void documentChange(final DocumentEvent evt) {
        LOG.debug(evt);
        if (isItemChanging()) return;
        triggerModelChange();
    }

    protected void triggerModelChange() {
        if (editable) try {
            String text = ((JTextField) getComponent()).getText();
            LOG.debug("TEXT  '" + text + "'");
            String key = getXPath();
            fireFieldChangeListeners(this, key, text);
            processBeforeOps();
            getForm().getModel().setData(getComponent(), getXPath(), text);
            processAfterOps();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    /**
     * mouse event handler to process double click.
     * Process action associated with this Field.
     * @param evt the mouse event
     */
    public void mouseClicked(final MouseEvent evt) {
        if (evt.getClickCount() < 2) return;
        processAction(this);
    }

    /**
     * (empty) mouse event handler.
     * @param evt the mouse event
     */
    public void mouseEntered(final MouseEvent evt) {
    }

    /**
     * (empty) mouse event handler.
     * @param evt the mouse event
     */
    public void mouseExited(final MouseEvent evt) {
    }

    /**
     * (empty) mouse event handler.
     * @param evt the mouse event
     */
    public void mousePressed(final MouseEvent evt) {
    }

    /**
     * (empty) mouse event handler.
     * @param evt the mouse event
     */
    public void mouseReleased(final MouseEvent evt) {
    }

    /**
     * complete standard initialisation of actions.
     * @throws Exception Exception
     */
    public void init() throws Exception {
        super.init();
        PropertyUtil.setAttributes(this, getFieldDescriptor(), new String[] { "border" }, new Class[] { boolean.class }, new String[] { "false" });
        if (getFieldDescriptor().getAttribute("actionListener").trim().length() > 0) addActionListeners(getFieldDescriptor());
        if (getFieldDescriptor().getAttribute("actionCommand").trim().length() > 0) {
            ((JTextField) getComponent()).setActionCommand(getFieldDescriptor().getAttribute("actionCommand").trim());
        }
        ((JTextField) getComponent()).addMouseListener(this);
    }

    private int determineAlignment(final Element desc) {
        String align = desc.getAttribute("align");
        if (align.equals("left")) return JTextField.LEFT; else if (align.equals("right")) return JTextField.RIGHT; else if (align.equals("center")) return JTextField.CENTER;
        return JTextField.LEFT;
    }

    private void addActionListeners(final Element desc) throws Exception {
        StringTokenizer st = new StringTokenizer(desc.getAttribute("actionListener"), ", ");
        while (st.hasMoreTokens()) addActionListener(st.nextToken());
    }

    private void addActionListener(final String actionListenerClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (getComponent() instanceof JTextField) {
            ActionListener actionListener = (ActionListener) Class.forName(actionListenerClassName).newInstance();
            actionListeners.remove(actionListener);
            actionListeners.add(actionListener);
            ((JTextField) getComponent()).removeActionListener(actionListener);
            ((JTextField) getComponent()).addActionListener(actionListener);
        }
    }

    private static class LimitingDocument extends PlainDocument {

        private int limit;

        private Form form;

        private Field field;

        public LimitingDocument(final int lengthLimit, final Form parentForm, final Field parentField) {
            super();
            this.limit = lengthLimit;
            this.form = parentForm;
            this.field = parentField;
        }

        public void insertString(final int offset, final String string, final AttributeSet attributeSet) throws BadLocationException {
            synchronized (this) {
                if (string == null) {
                    return;
                }
                if ((getLength() + string.length()) > limit) {
                    LOG.info("Limit " + limit + " exceeded by text length " + (getLength() + string.length()));
                    return;
                }
                super.insertString(offset, string, attributeSet);
            }
        }
    }

    private Vector actionListeners = new Vector();

    private int maxLength = 128;

    protected boolean editable = true;

    private static final Logger LOG = Logger.getLogger(TextWithAction.class.getName());
}
