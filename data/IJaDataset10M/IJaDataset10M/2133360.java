package com.cfinkel.reports.wrappers;

import com.cfinkel.reports.exceptions.BadReportSyntaxException;
import com.cfinkel.reports.generatedbeans.*;
import com.cfinkel.reports.util.Util;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

/**
 * $Author: charles $
 * $Revision: 8904 $
 * $Date: 2006-05-01 18:02:06 -0400 (Mon, 01 May 2006) $
 *
 * Created by IntelliJ IDEA.
 * User: charles
 * Date: Mar 25, 2006
 * Time: 5:40:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Input implements Serializable {

    private static final Logger log = Logger.getLogger(Input.class);

    private boolean hasValidation;

    public Report getReport() {
        return report;
    }

    public abstract ControlElement getControlElement();

    private Report report;

    public abstract Map<String, String> getValues() throws Exception;

    public abstract String getDefaultVal();

    public abstract String getDescription();

    public abstract Control getControl();

    public abstract Datatype getDatatype();

    private InputElement inputElement;

    public int getDepth() {
        return depth;
    }

    private int depth = 0;

    public InputElement get() {
        return inputElement;
    }

    static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");

    static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("hh:mm a");

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy hh:mm a");

    public Input(InputElement inputElement, Report report, int depth) throws BadReportSyntaxException {
        this.report = report;
        this.depth = depth;
        this.hasValidation = false;
        if (report.getAllInputs().get(inputElement.getName()) != null) throw new BadReportSyntaxException("There are more than one input named '" + inputElement.getName() + "'");
        report.getAllInputs().put(inputElement.getName(), this);
        this.inputElement = inputElement;
        this.dependents = new LinkedHashMap<String, Input>();
        this.report = report;
        for (DependentInputElement dependentInputElement : inputElement.getDependentInput()) {
            if (report.getAllInputs().get(dependentInputElement.getName()) != null) throw new BadReportSyntaxException("There are more than one dependentInput named '" + dependentInputElement.getName() + "'");
            Input dependentInput;
            if (dependentInputElement.getWhen().get(0).getParentValue().equals("*")) {
                dependentInput = new DependentInputWithDynamicValues(dependentInputElement, report, this, this.depth + 1);
            } else {
                dependentInput = new DependentInputWithStaticValues(dependentInputElement, report, this, this.depth + 1);
            }
            dependents.put(dependentInputElement.getName(), dependentInput);
        }
    }

    Map<String, Input> dependents;

    public boolean isHasDependents() {
        return ((dependents != null) && dependents.size() > 0);
    }

    public Map<String, Input> getDependents() {
        return dependents;
    }

    /**
     *
     * @return javascript for getting selected value of input.
     * different for radio buttons (annoying)
     */
    public String getInputValueJavascript() {
        StringBuilder sb = new StringBuilder();
        if (this.getControl().equals(Control.RADIO)) {
            sb.append("getSelectedRadioValue(document.getElementsByName('");
            sb.append(this.get().getName());
            sb.append("'))");
        } else {
            sb.append("document.getElementById('");
            sb.append(this.get().getName());
            sb.append("').value");
        }
        return sb.toString();
    }

    public String getName() {
        return inputElement.getName();
    }

    /**
     * adds call to AJAX javascript update thang and validation checker
     * @return html for AJAX javascript update thang
     */
    public String getOnChangeHTML() {
        StringBuilder sb = new StringBuilder();
        if (!this.isHasDependents() && !this.isHasValidation()) return "";
        if (this.getControl().equals(Control.RADIO)) {
            sb.append("onclick=\"");
        } else {
            sb.append("onchange=\"");
        }
        if (this.isHasDependents()) {
            sb.append(this.getName());
            sb.append("_changed();");
        }
        if (this.isHasValidation()) {
            sb.append(" check_");
            sb.append(this.getName());
            sb.append("();");
        }
        sb.append("\"");
        return sb.toString();
    }

    static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    static final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

    static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    /**
     * formats the input value for proper insertion into the query
     * @param s
     * @return formatted String input
     */
    public Object format(String s) throws ParseException {
        Object object;
        try {
            switch(this.getDatatype()) {
                case DATE:
                    object = new Timestamp(dateFormat.parse(s).getTime());
                    break;
                case DATETIME:
                    object = new Timestamp(dateTimeFormat.parse(s).getTime());
                    break;
                case TIME:
                    object = new Timestamp(timeFormat.parse(s).getTime());
                    break;
                case STRING:
                    object = s;
                    break;
                case FLOAT:
                    object = Float.valueOf(s);
                    break;
                case INTEGER:
                    object = Integer.valueOf(s);
                    break;
                default:
                    object = s;
                    break;
            }
        } catch (ParseException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new ParseException(e.toString(), 0);
        }
        return object;
    }

    protected static String getDefaultValue(String defaultString, Datatype dataType) {
        if (defaultString == null) return "";
        if (Util.equalsAny(dataType, Datatype.FLOAT, Datatype.INTEGER, Datatype.STRING)) {
            return defaultString;
        }
        String defaultVal = defaultString;
        if (defaultVal.equalsIgnoreCase("now") || defaultVal.equalsIgnoreCase("today")) {
            Calendar calendar = new GregorianCalendar();
            if (defaultVal.equalsIgnoreCase("today")) {
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
            }
            Date date = calendar.getTime();
            switch(dataType) {
                case DATE:
                    defaultVal = dateFormat.format(date);
                    break;
                case DATETIME:
                    defaultVal = dateTimeFormat.format(date);
                    break;
                case TIME:
                    defaultVal = timeFormat.format(date);
                    break;
                default:
                    break;
            }
        } else if (defaultVal.startsWith("m")) {
            DateTime dateTime = new DateTime();
            Integer minutes = Integer.parseInt(defaultVal.substring(1, defaultVal.length()));
            dateTime = dateTime.minusMinutes(minutes);
            defaultVal = dateTimeFormatter.print(dateTime);
        } else if (defaultVal.startsWith("d")) {
            DateTime dateTime = new DateTime();
            Integer days = Integer.parseInt(defaultVal.substring(1, defaultVal.length()));
            dateTime = dateTime.minusDays(days);
            defaultVal = dateTimeFormatter.print(dateTime);
        }
        return defaultVal;
    }

    /**
     * gets values from attribute 'values'
     * @param valuesString
     * @return values from attribute 'values'
     */
    public static HashMap<String, String> getValuesFromAttribute(String valuesString) {
        HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
        if (!Util.anyAreNullOrBlank(valuesString)) {
            StringTokenizer st = new StringTokenizer(valuesString, "(),");
            while (st.hasMoreTokens()) {
                String key = st.nextToken();
                String value = st.nextToken();
                tempMap.put(key, value);
            }
        }
        return tempMap;
    }

    /**
     * gets control attribute based on control element
     * kind of a kludge
     * @param controlElement
     * @return
     * @throws BadReportSyntaxException
     */
    protected static Control getControlAttributeFromControlElement(ControlElement controlElement) throws BadReportSyntaxException {
        if (controlElement.getListbox() != null) {
            return Control.LISTBOX;
        } else if (controlElement.getTextarea() != null) {
            return Control.TEXTAREA;
        } else if (controlElement.getRadio() != null) {
            return Control.RADIO;
        } else {
            String message = "No proper control for this input.  This should never happen.";
            log.error(message);
            throw new BadReportSyntaxException(message);
        }
    }

    /**
     * @return has validation?
     */
    public boolean isHasValidation() {
        return hasValidation;
    }
}
