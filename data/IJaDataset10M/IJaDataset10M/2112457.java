package org.form4j.form.field.data;

import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import org.form4j.form.field.action.ActionTrigger;
import org.form4j.form.formatters.FormattedDateFieldInputVerifier;
import org.form4j.form.main.Form;
import org.form4j.form.util.DateUtil;
import org.form4j.form.util.PropertyUtil;
import org.form4j.form.util.StringUtil;
import org.form4j.form.util.apply.Apply;
import org.form4j.form.util.apply.Applyable;
import org.w3c.dom.Element;
import com.toedter.calendar.JCalendar;

/**
 * Formatted Textfield for Dates Form control.
 * <p>
 * (see: Form definition reference  and examples for
 * <a href="../../../../../../manual/dataFields.html#datefield" target="_top">Datefield</a>
 * in the <a href="../../../../../../manual/index.html" target="_top">form4j Manual</a>!)
 * </p>
 *
 *
 * @author $Author: cjuon $
 * @version 0.2 $Revision: 1.27 $ $Date: 2007/05/24 03:54:48 $
 **/
public class FormattedDateField extends FormattedTextField implements DocumentListener, FocusListener, Applyable, ActionTrigger {

    /**
     * Constructs date field form4j control with date formatting and calendar popup.
     * @param form the parent form4j
     * @param desc XML the field descriptor
     * @throws Exception Exception
     */
    public FormattedDateField(final Form form, final Element desc) throws Exception {
        super(form, desc);
        getComponent().setInputVerifier(new FormattedDateFieldInputVerifier(this));
    }

    /**
     * set the data for this field
     * @param data the data for this field
     */
    public void setData(final Object data) {
        LOG.debug("Data0 '" + String.valueOf(data) + "'   isNull " + (data == null));
        String s = handleEmpty(data);
        LOG.debug("Data1 '" + String.valueOf(s) + "'");
        if (s.length() > 0) {
            ((JFormattedTextField) getComponent()).setValue(DateUtil.parseDate(StringUtil.normalizeString(s), getDateFormat()));
            expandSymbolicDateValues(s);
        } else {
            ((JFormattedTextField) getComponent()).setValue(null);
        }
    }

    /**
     * get the data for this field
     * @return the data for this field
     */
    public Object getData() {
        Date date = (Date) ((JFormattedTextField) getComponent()).getValue();
        LOG.debug("DATE " + date);
        if (date != null) return date.getTime() + ""; else return null;
    }

    /**
     * set field to default content (e.g. content of attribute 'default' in field descriptor)
     */
    public void setToDefault() {
        if (getComponent().isEnabled()) ((JFormattedTextField) getComponent()).setValue(DateUtil.parseDate(getFieldDescriptor().getAttribute("default"), getDateFormat()));
    }

    /**
     * Get the current date format.
     * @return the current date format.
     */
    public DateFormat getDateFormat() {
        return dataFormat;
    }

    /**
     * install mouselistener for calendar popup and establish data format.
     * @throws Exception Exception
     */
    public void init() throws Exception {
        if (!getFieldDescriptor().getAttribute("dataFormat").equals("long")) dataFormat = new SimpleDateFormat(getFieldDescriptor().getAttribute("dataFormat"));
        final Applyable applyable = (Applyable) this;
        getComponent().addMouseListener(new MouseAdapter() {

            public void mouseClicked(final MouseEvent evt) {
                if (evt.getClickCount() < 2) return;
                LOG.debug("about to launch CalendarPopup data '" + getData() + "' using format '" + getDateFormat() + "'");
                getDataFromCalendarPopup(applyable);
            }
        });
        super.init();
        PropertyUtil.setInheritedAttributes(this, getFieldDescriptor(), new String[] { "applyTitle", "closeOption", "applyOption" }, new Class[] { String.class, String.class, String.class }, null);
    }

    /**
     * get data from popup and re-request focus.
     * @param evt the action event.
     */
    public void fireActions(final ActionEvent evt) {
        getDataFromCalendarPopup(this);
        getFocusComponent().requestFocus();
    }

    private void getDataFromCalendarPopup(final Applyable applyable) {
        if (((JFormattedTextField) getComponent()).isEditable() && ((JFormattedTextField) getComponent()).isEnabled()) {
            JCalendar jcalendar = (JCalendar) Apply.getDataFromApplyDialog(applyable);
            if (jcalendar != null) {
                String newData = formatDate(jcalendar.getCalendar().getTime());
                LOG.debug("Data from popup '" + newData + "'");
                if (!newData.equals(getData())) {
                    setData(newData);
                    triggerModelChange();
                    LOG.debug("back from modal, Got '" + newData + "'");
                }
            }
        }
    }

    /**
     * get the message object that appears in the body of apply option dialog
     * @return the message object that appears in the body of apply option dialog
     */
    public Object getApplyEditor() {
        JCalendar jcalendar = new JCalendar();
        Calendar calendar = new GregorianCalendar();
        try {
            calendar.setTime((Date) ((JFormattedTextField) getComponent()).getValue());
        } catch (Exception e) {
            LOG.debug(e);
        }
        jcalendar.setCalendar(calendar);
        jcalendar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        return jcalendar;
    }

    /**
     * get the title that appears in the frame label of the apply option dialog
     * @return the title that appears in the frame label of the apply option dialog
     */
    public String getApplyTitle() {
        return applyTitle;
    }

    /**
     * get the label of the close button in the complaint option dialog
     * @return the label of the close button in the complaint option dialog
     */
    public String getCloseOption() {
        return closeOption;
    }

    /**
     * get the label of the apply button in the apply option dialog
     * @return the label of the apply button in the apply option dialog
     */
    public String getApplyOption() {
        return applyOption;
    }

    /**
     * set the label of the apply button in the apply option dialog
     * @param string the label of the apply button in the apply option dialog
     */
    public void setApplyOption(final String string) {
        applyOption = string;
    }

    /**
     * set the title that appears in the frame label of the apply option dialog
     * @param string the title that appears in the frame label of the apply option dialog
     */
    public void setApplyTitle(final String string) {
        applyTitle = string;
    }

    /**
     * set the label of the close button in the complaint option dialog
     * @param string the label of the close button in the complaint option dialog
     */
    public void setCloseOption(final String string) {
        closeOption = string;
    }

    /**
     * get the component associated with this applyable.
     * @return the component associated with this applyable.
     */
    public JComponent getParentComponent() {
        return getComponent();
    }

    private String formatDate(final Date date) {
        if (dataFormat == null) return date.getTime() + ""; else return dataFormat.format(date);
    }

    protected void triggerModelChange() {
        try {
            ((JFormattedTextField) getComponent()).commitEdit();
            String formattedData = formatDate((Date) ((JFormattedTextField) getComponent()).getValue());
            String key = getXPath();
            fireFieldChangeListeners(this, key, formattedData);
            processBeforeOps();
            getForm().getModel().setData(getComponent(), key, formattedData);
            processAfterOps();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private void expandSymbolicDateValues(final String s) {
        if (s.indexOf("NOW") != -1) try {
            String formattedData = formatDate((Date) ((JFormattedTextField) getComponent()).getValue());
            synchronized (getForm().getModel()) {
                getForm().getModel().setFireEvents(false);
                getForm().getModel().setData(getComponent(), getFieldDescriptor().getAttribute("key"), formattedData);
                getForm().getModel().setFireEvents(true);
            }
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    private SimpleDateFormat dataFormat;

    private String applyOption;

    private String applyTitle;

    private String closeOption;

    private static final Logger LOG = Logger.getLogger(FormattedDateField.class.getName());
}
