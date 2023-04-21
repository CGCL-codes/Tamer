package org.form4j.form.field.data;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JFormattedTextField;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;
import org.form4j.form.field.Field;
import org.form4j.form.field.calc.CalcHelper;
import org.form4j.form.field.calc.Calculateable;
import org.form4j.form.formatters.FormattedTextFieldInputVerifier;
import org.form4j.form.formatters.FormatterFactoryBuilder;
import org.form4j.form.main.Form;
import org.form4j.form.util.PropertyUtil;
import org.form4j.form.util.StringUtil;
import org.form4j.form.util.complaints.Complainable;
import org.form4j.form.util.complaints.Complaint;
import org.form4j.form.util.tip.ToolTipUtil;
import org.w3c.dom.Element;

/**
 * Formatted Textfield Form control.
 * <p>
 * (see: Form definition reference  and examples for
 * <a href="../../../../../../manual/dataFields.html#textfield" target="_top">Textfield</a>
 * in the <a href="../../../../../../manual/index.html" target="_top">form4j Manual</a>!)
 * </p>
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.75 $ $Date: 2011/04/23 10:08:56 $
 **/
public class FormattedTextField extends AbstractSingleComponentDataField implements DocumentListener, FocusListener, Complainable, Calculateable {

    private boolean initializing = false;

    /**
     * Construct form4j formatted text field control.
     * @param form the parent form
     * @param desc the XML field descriptor.
     * @throws Exception Exception
     */
    public FormattedTextField(final Form form, final Element desc) throws Exception {
        super(form, desc);
        initializing = true;
        if (System.getProperty("java.version").startsWith("1.5") || System.getProperty("java.version").startsWith("1.6")) jdk15ActionHandling = true; else jdk15ActionHandling = false;
        JFormattedTextField component = new JFormattedTextField(FormatterFactoryBuilder.getInstance().createFormatterFactory(getFieldDescriptor())) {

            private int toolTipModifiers = 0;

            public Point getToolTipLocation(final MouseEvent event) {
                toolTipModifiers = event.getModifiers();
                return super.getToolTipLocation(event);
            }

            public JToolTip createToolTip() {
                return ToolTipUtil.createToolTip(FormattedTextField.this, toolTipModifiers);
            }

            public Dimension getPreferredSize() {
                return (isVisible() ? super.getPreferredSize() : new Dimension(0, 0));
            }

            public void processFocusEvent(FocusEvent evt) {
                inTextFocusEvent = true;
                super.processFocusEvent(evt);
                inTextFocusEvent = false;
            }
        };
        if (ToolTipUtil.isToolTipDebugActive()) {
            component.setToolTipText(ToolTipUtil.TOOLTIP_PLACEHOLDER);
        }
        component.setInputVerifier(new FormattedTextFieldInputVerifier(this));
        setComponent(component);
        init();
        adaptCommit(component);
        initializing = false;
    }

    private void adaptCommit(JFormattedTextField component) {
        if (isTriggerOnItem() && component.getFormatter() instanceof DefaultFormatter) {
            try {
                DefaultFormatter defaultFormatter = (DefaultFormatter) ((DefaultFormatter) component.getFormatter()).clone();
                defaultFormatter.setOverwriteMode(false);
                defaultFormatter.setCommitsOnValidEdit(true);
                DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory((DefaultFormatter) defaultFormatter.clone(), (DefaultFormatter) defaultFormatter.clone(), (DefaultFormatter) defaultFormatter.clone());
                component.setFormatterFactory(defaultFormatterFactory);
            } catch (Exception e) {
                LOG.error(e);
            }
        }
    }

    /**
     * set the data for this field.
     *
     * @param dataObj
     *            the data for this field
     */
    public void setData(final Object dataObj) {
        LOG.debug("Data(" + getKey() + "):      '" + dataObj + "'");
        LOG.debug("Formatter: " + ((JFormattedTextField) getComponent()).getFormatter());
        Object data = handleEmpty(dataObj);
        LOG.debug("Data(" + getKey() + ") after handleEmpty:      '" + dataObj + "'");
        if (((JFormattedTextField) getComponent()).getFormatter() instanceof NumberFormatter) try {
            if (data == null) {
                LOG.debug("Clearing null text field (" + getKey() + ")");
                ((JFormattedTextField) getComponent()).setText("");
                ((JFormattedTextField) getComponent()).setValue(null);
            } else if (((String) data).trim().length() == 0) {
                LOG.debug("Clearing empty text field (" + getKey() + ")");
                ((JFormattedTextField) getComponent()).setText("");
                ((JFormattedTextField) getComponent()).setValue(null);
            } else {
                ((JFormattedTextField) getComponent()).setValue(new Double((String) data));
            }
        } catch (Exception e) {
            LOG.debug(e);
        } else {
            LOG.debug("SETTING VALUE B (" + getKey() + ")   " + StringUtil.normalizeString((String) data));
            ((JFormattedTextField) getComponent()).setValue(StringUtil.normalizeString((String) data));
        }
        textFieldTouched = false;
    }

    /**
     * get the data for this field.
     * @return the data for this field
     */
    public Object getData() {
        LOG.debug("DATA '" + ((JFormattedTextField) getComponent()).getText() + "'");
        try {
            if (((JFormattedTextField) getComponent()).getFormatter() instanceof NumberFormatter) return ((JFormattedTextField) getComponent()).getValue().toString(); else if (getFieldDescriptor().getAttribute("format").equals("decimal")) {
                return cleanDecimal();
            }
        } catch (Exception e) {
        }
        return ((JFormattedTextField) getComponent()).getValue();
    }

    private String cleanDecimal() {
        String s = ((JFormattedTextField) getComponent()).getText();
        String cleaned = formatToPlainDecimal(s);
        LOG.debug("SPECIAL DECIMAL WITHOUT Pattern -->>>" + cleaned + "<<<<");
        if (!cleaned.equals(s)) ((JFormattedTextField) getComponent()).setText(cleaned);
        return cleaned;
    }

    private String formatToPlainDecimal(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            switch(s.charAt(i)) {
                case '\'':
                    break;
                case ',':
                    break;
                default:
                    sb.append(s.charAt(i));
                    break;
            }
        }
        return sb.toString();
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

    /**
     * @return Returns the autoUpperCase.
     */
    public boolean isAutoUpperCase() {
        return autoUpperCase;
    }

    /**
     * @param autoUpperCaseParm The autoUpperCase to set.
     */
    public void setAutoUpperCase(final boolean autoUpperCaseParm) {
        this.autoUpperCase = autoUpperCaseParm;
    }

    /**
     * Get empty status of the field.
     * @return true when the field is empty (data node in model is null)
     */
    public boolean isEmpty() {
        return empty;
    }

    /**
     * Set empty status of the field.
     * @param b true when the field is empty (data node in model is null)
     */
    public void setEmpty(final boolean b) {
        empty = b;
    }

    /**
     * Empty Key Listener method.
     * @param arg0 the key event
     */
    public void keyPressed(final KeyEvent arg0) {
        if (isFunctionKey(arg0)) caretPos = ((JFormattedTextField) getComponent()).getCaretPosition();
        justAllSelected = false;
    }

    public void keyTyped(final KeyEvent arg0) {
        justAllSelected = false;
    }

    public void keyReleased(final KeyEvent arg0) {
        justAllSelected = false;
        if (jdk15ActionHandling && arg0.getKeyCode() == KeyEvent.VK_ENTER) {
            JFormattedTextField tf = (JFormattedTextField) getComponent();
            tf.postActionEvent();
        }
        if (isEnterIsTab() && arg0.getKeyCode() == KeyEvent.VK_ENTER) getFocusComponent().transferFocus();
        if (isFunctionKey(arg0)) fixCaretPosition();
    }

    private boolean isFunctionKey(KeyEvent evt) {
        if (evt.isActionKey()) try {
            String txt = evt.getKeyText(evt.getKeyCode()).toUpperCase();
            if (txt.length() > 1 && txt.startsWith("F")) return true;
        } catch (Exception e) {
        }
        return false;
    }

    /** called when focus is gained.
     * @param evt the focus event
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(final FocusEvent evt) {
        if (selectedOnFocus) {
            justAllSelected = true;
            selectAllLater();
        }
        setCaretLater(evt);
    }

    /**
     * called when focus is lost.
     * @param evt the focus event
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(final FocusEvent evt) {
        justAllSelected = false;
        if (isEmpty() && getWhenEmpty() == LEAVE_EMPTY_WHEN_EMPTY && !textFieldTouched) return;
        if (!evt.isTemporary() && isTriggerOnFocus()) {
            triggerModelChange();
        }
        caretPos = ((JFormattedTextField) getComponent()).getCaretPosition();
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
        if (justAllSelected) selectAllLater();
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
        textFieldTouched = true;
        if (isItemChanging()) return;
        if (isTriggerOnItem() && !inTextFocusEvent) triggerModelChange();
    }

    protected void triggerModelChange() {
        int len = ((JFormattedTextField) getComponent()).getDocument().getLength();
        if (!initializing) try {
            boolean emptyParsedOK = false;
            try {
                String text1 = String.valueOf(((JFormattedTextField) getComponent()).getValue());
                LOG.debug("text1 before trying to commitEdit '" + text1 + "'");
                String text2 = ((JFormattedTextField) getComponent()).getText();
                LOG.debug("text2 before trying to commitEdit '" + text2 + "'");
                ((JFormattedTextField) getComponent()).commitEdit();
            } catch (Exception e) {
                boolean emptyIsOk = getFieldDescriptor().getAttribute("emptyIsOk").equals("true");
                if (emptyIsOk) {
                    emptyParsedOK = true;
                }
                LOG.info(e);
            }
            String text = (emptyParsedOK ? "" : String.valueOf(((JFormattedTextField) getComponent()).getValue()));
            if (getFieldDescriptor().getAttribute("format").equals("decimal")) text = cleanDecimal();
            LOG.debug("text '" + text + "'");
            String key = getXPath();
            fireFieldChangeListeners(this, key, text);
            processBeforeOps();
            getForm().getModel().setData(getComponent(), getXPath(), text);
            processAfterOps();
            if (isTriggerOnFocus() && text != null && text.trim().length() == 0) {
                ((JFormattedTextField) getComponent()).setText("");
                ((JFormattedTextField) getComponent()).setValue(null);
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    /**
     * get the field associated with this complainable.
     * @return the field associated with this complainable.
     */
    public Field getField() {
        return this;
    }

    /**
     * get the message that appears in the body of complaint option dialog.
     * @return the message that appears in the body of complaint option dialog
     */
    public String getComplaint() {
        return complaint;
    }

    /**
     * get the message that appears in the body of the complaint option dialog when a range has been violated.
     * @return the message that appears in the body of the complaint option dialog when a range has been violated
     */
    public String getRangeComplaint() {
        return rangeComplaint;
    }

    /**
     * get the title that appears in the frame label of the complaint option dialog.
     * @return the title that appears in the frame label of the complaint option dialog
     */
    public String getComplaintTitle() {
        return complaintTitle;
    }

    /**
     * get the label of the edit option button.
     * @return the label of the edit button in the complaint option dialog
     */
    public String getEditOption() {
        return editOption;
    }

    /**
     * get the label of the revert button in the complaint option dialog.
     * @return the label of the revert button in the complaint option dialog
     */
    public String getRevertOption() {
        return revertOption;
    }

    /**
     * set the message that appears in the body of the complaint option dialog.
     * @param string the message that appears in the body of the complaint option dialog
     */
    public void setComplaint(final String string) {
        complaint = string;
    }

    /**
     * set the message that appears in the body of the complaint option dialog when a range has been violated.
     * @param string the message that appears in the body of the complaint option dialog when a range has been violated
     */
    public void setRangeComplaint(final String string) {
        rangeComplaint = string;
    }

    /**
     * set the title that appears in the frame label of the complaint option dialog.
     * @param string the title that appears in the frame label of the complaint option dialog
     */
    public void setComplaintTitle(final String string) {
        complaintTitle = string;
    }

    /**
     * set the label of the edit button in the complaint option dialog.
     * @param string the label of the edit button in the complaint option dialog
     */
    public void setEditOption(final String string) {
        editOption = string;
    }

    /**
     * set  the label of the revert button in the complaint option dialog.
     * @param string the label of the revert button in the complaint option dialog
     */
    public void setRevertOption(final String string) {
        revertOption = string;
    }

    /**
     * get the formatted text field that wants to complain.
     * @return the formatted text field that wants to complain
     */
    public JFormattedTextField getFormattedTextField() {
        return ((JFormattedTextField) getComponent());
    }

    /**
     * get the autorevert flag.
     */
    public boolean isAutoRevert() {
        return autoReverting;
    }

    /**
     * set  the autorevert flag.
     * @param autorevert should the complaint autorevert the field instead of visually complaining
     */
    public void setAutoRevert(final boolean autorevert) {
        autoReverting = autorevert;
    }

    /**
     * @return Returns the focusCaret.
     */
    public String getFocusCaret() {
        return focusCaret;
    }

    /**
     * @param focusCaretParm The focusCaret to set.
     */
    public void setFocusCaret(final String focusCaretParm) {
        this.focusCaret = focusCaretParm;
        if (focusCaret.equals("keep")) focusCaretCode = FOCUS_CARET_KEEP; else if (focusCaret.equals("end")) focusCaretCode = FOCUS_CARET_END; else if (focusCaret.equals("start")) focusCaretCode = FOCUS_CARET_START;
    }

    /**
     * initializes the formatted text field control.
     * @throws Exception Exception
     */
    public void init() throws Exception {
        ((JFormattedTextField) getComponent()).setHorizontalAlignment(determineAlignment(getFieldDescriptor()));
        maxLength = Integer.parseInt(getFieldDescriptor().getAttribute("maxLength"));
        PropertyUtil.setAttributes(this, getFieldDescriptor(), new String[] { "autoUpperCase", "focusCaret" }, new Class[] { boolean.class, String.class });
        ((JFormattedTextField) getComponent()).setDocument(new LimitingDocument(getMaxLength(), getForm(), this, isAutoUpperCase()));
        ((JFormattedTextField) getComponent()).getDocument().addDocumentListener(this);
        ((JFormattedTextField) getComponent()).addFocusListener(this);
        Complaint.initComplainable(this, getFieldDescriptor());
        PropertyUtil.setInheritedAttributes(this, getFieldDescriptor(), new String[] { "whenEmpty" }, new Class[] { String.class }, null);
        ((JFormattedTextField) getComponent()).addKeyListener(this);
        selectedOnFocus = getFieldDescriptor().getAttribute("selectedOnFocus").equals("true");
        super.init();
        if (this.getFieldDescriptor().getAttribute("expr").trim().length() > 0) {
            setExpr(this.getFieldDescriptor().getAttribute("expr").trim());
            reEval(getFieldDescriptor().getAttribute("expr"));
        }
        if (getFieldDescriptor().getAttribute("actionListener").trim().length() > 0) try {
            String actionCommand = getFieldDescriptor().getAttribute("actionCommand").trim();
            StringTokenizer st = new StringTokenizer(getFieldDescriptor().getAttribute("actionListener").trim(), " ,");
            Vector v = new Vector();
            while (st.hasMoreTokens()) v.addElement(st.nextToken());
            for (int i = v.size() - 1; i >= 0; i--) {
                final Component evtSource = getFocusComponent();
                final Field field = this;
                final ActionListener listener = (ActionListener) Class.forName((String) v.elementAt(i)).newInstance();
                ((JFormattedTextField) getComponent()).addActionListener(listener);
                ((JFormattedTextField) getComponent()).setActionCommand(actionCommand);
                ((JFormattedTextField) getComponent()).addFocusListener(new FocusListener() {

                    public void focusGained(final FocusEvent evt) {
                    }

                    public void focusLost(final FocusEvent evt) {
                        if (!evt.isTemporary()) {
                            ActionEvent wrappedEvent = new ActionEvent(evtSource, 0, field.getName());
                            listener.actionPerformed(wrappedEvent);
                        }
                    }
                });
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    /**
     * Set the arithmetic expression for this calculated field.
     * @param string the arithmetic expression for this calculated field
     */
    public void setExpr(final String string) {
        CalcHelper.installItemChangeListeners(this, string);
    }

    /**
     * Re-evaluates arithmetic expression and show obtained result.
     * @throws Exception
     */
    public void reEval(final String expression) throws Exception {
        LOG.debug("expression " + expression);
        LOG.debug(getFieldDescriptor().getAttribute("name") + " " + CalcHelper.eval(this, expression));
        ((JFormattedTextField) getComponent()).setValue(new Double(CalcHelper.eval(this, expression)));
        triggerModelChange();
    }

    /**
     * Perform any mappings after calculation was re-evaluated.
     * @param newValue the calculated value to map
     */
    public void performMappingsAfterCalc(final String newValue) {
    }

    private int determineAlignment(final Element desc) {
        String align = desc.getAttribute("align");
        if (align.equals("left")) return JFormattedTextField.LEFT; else if (align.equals("right")) return JFormattedTextField.RIGHT; else if (align.equals("center")) return JFormattedTextField.CENTER;
        return JFormattedTextField.LEFT;
    }

    private void selectAllLater() {
        SwingUtilities.invokeLater(new Thread() {

            public void run() {
                try {
                    Thread.sleep(100L);
                } catch (Exception e) {
                }
                ((JFormattedTextField) getComponent()).selectAll();
            }
        });
    }

    private void setCaretLater(final FocusEvent evt) {
        SwingUtilities.invokeLater(new Thread() {

            public void run() {
                fixCaretPosition();
                if (selectedOnFocus) selectAllLater();
            }
        });
    }

    private void fixCaretPosition() {
        LOG.debug("CARET CODE " + focusCaretCode);
        switch(focusCaretCode) {
            case FOCUS_CARET_START:
                ((JFormattedTextField) getComponent()).setCaretPosition(0);
                break;
            case FOCUS_CARET_END:
                ((JFormattedTextField) getComponent()).setCaretPosition(((JFormattedTextField) getComponent()).getDocument().getLength());
                break;
            default:
                if (caretPos != -1 && ((JFormattedTextField) getComponent()).getDocument().getLength() > caretPos) {
                    ((JFormattedTextField) getComponent()).setCaretPosition(caretPos);
                }
                break;
        }
    }

    /**
     * Lenght limiting document local class.
     */
    private static class LimitingDocument extends PlainDocument {

        private int limit;

        private Form form;

        private Field field;

        private boolean autoUpper = false;

        public LimitingDocument(final int lengthLimit, final Form parentForm, final Field parentField, final boolean upperCase) {
            super();
            this.limit = lengthLimit;
            this.form = parentForm;
            this.field = parentField;
            this.autoUpper = upperCase;
        }

        protected void insertUpdate(final AbstractDocument.DefaultDocumentEvent chng, final AttributeSet attr) {
            if (getLength() <= limit) super.insertUpdate(chng, attr); else try {
                String text = getText(0, limit);
                this.remove(0, getLength());
                this.insertString(0, text, attr);
            } catch (Exception e) {
                LOG.error(e);
            }
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
                if (autoUpper) super.insertString(offset, string.toUpperCase(), attributeSet); else super.insertString(offset, string, attributeSet);
            }
        }
    }

    private static final int FOCUS_CARET_KEEP = 0;

    private static final int FOCUS_CARET_END = 1;

    private static final int FOCUS_CARET_START = 2;

    private String focusCaret = "keep";

    private int focusCaretCode = FOCUS_CARET_KEEP;

    private int caretPos = -1;

    private String complaint = "";

    private String rangeComplaint = "";

    private String complaintTitle = "";

    private String editOption = "";

    private String revertOption = "";

    private boolean autoReverting = false;

    private static final int DEFAULT_MAXLENGTH = 72;

    private int maxLength = DEFAULT_MAXLENGTH;

    private boolean autoUpperCase = false;

    private boolean empty = false;

    private boolean textFieldTouched = false;

    private boolean justAllSelected = false;

    private boolean selectedOnFocus = false;

    private boolean inTextFocusEvent = false;

    private boolean jdk15ActionHandling = false;

    private static final Logger LOG = Logger.getLogger(FormattedTextField.class.getName());
}
