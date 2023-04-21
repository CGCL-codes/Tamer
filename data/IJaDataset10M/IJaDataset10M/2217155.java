package org.equanda.reporting.parser;

import org.equanda.reporting.parameter.ParameterValue;
import org.equanda.reporting.parameter.ParameterValueAdapter;
import org.equanda.tapestry.pages.EquandaBasePage;
import org.equanda.tapestry.util.SuggestValueList;
import org.apache.log4j.Logger;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.util.Date;

/**
 * Used for storing parameter details
 *
 * @author NetRom team
 */
public class ReportParameter implements Serializable {

    private static final Logger log = Logger.getLogger(ReportParameter.class);

    public static DateTimeFormatter dateParser = DateTimeFormat.forPattern("yyyy-MM-dd");

    public static DateTimeFormatter timeParser = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private String name;

    private String type;

    private String label;

    private String help;

    private String classParam;

    private String valueParam;

    private String valueListParam;

    private String tableParam;

    private String fieldParam;

    private String reportName;

    private Object value;

    private boolean isHidden;

    private boolean isReadonly;

    private boolean isOptional;

    private boolean isBoolean;

    private boolean isInteger;

    private boolean isDouble;

    private boolean isString;

    private boolean isTime;

    private boolean isDate;

    private boolean isInitialized;

    public ReportParameter() {
    }

    public ReportParameter(String reportName, String name, String type, String label, String defaultClass, String defaultValue, String defaultValueList, String defaultTable, String defaultField, String hidden, String readonly, String optional, String help) {
        this.reportName = reportName;
        this.name = name;
        this.type = type;
        this.label = label;
        this.help = help;
        this.classParam = defaultClass;
        this.valueParam = defaultValue;
        this.valueListParam = defaultValueList;
        this.tableParam = defaultTable;
        this.fieldParam = defaultField;
        this.isHidden = "true".equalsIgnoreCase(hidden) || "1".equalsIgnoreCase(hidden);
        this.isReadonly = "true".equalsIgnoreCase(readonly) || "1".equalsIgnoreCase(readonly);
        this.isOptional = "true".equalsIgnoreCase(optional) || "1".equalsIgnoreCase(optional);
        if ("string".equalsIgnoreCase(type)) isString = true;
        if ("boolean".equalsIgnoreCase(type)) isBoolean = true;
        if ("integer".equalsIgnoreCase(type)) isInteger = true;
        if ("double".equalsIgnoreCase(type)) isDouble = true;
        if ("date".equalsIgnoreCase(type)) isDate = true;
        if ("time".equalsIgnoreCase(type)) isTime = true;
    }

    public void initialize() {
        if (isInitialized) return;
        ParameterValue obj = new ParameterValueAdapter();
        if (classParam != null) {
            try {
                obj = (ParameterValue) Class.forName(classParam).newInstance();
            } catch (Exception e) {
                log.error("problems while instantiating : " + classParam, e);
            }
        }
        if (valueParam != null) {
            if (isString) {
                value = valueParam;
            }
            if (isBoolean) {
                if (!valueParam.equals("true") && !valueParam.equals("false")) {
                    log.error("incorrect value format for parameter " + name);
                } else {
                    value = valueParam;
                }
            }
            if (isInteger) {
                try {
                    Integer.parseInt(valueParam);
                    value = valueParam;
                } catch (NumberFormatException nfe) {
                    log.error("incorrect value format for parameter " + name, nfe);
                }
            }
            if (isDouble) {
                try {
                    Double.parseDouble(valueParam);
                    value = valueParam;
                } catch (NumberFormatException nfe) {
                    log.error("incorrect value format for parameter " + name, nfe);
                }
            }
            if (isDate) {
                try {
                    value = dateParser.parseDateTime(valueParam).toDate();
                } catch (Exception pe) {
                    log.error("incorrect value format for parameter " + name, pe);
                }
            }
            if (isTime) {
                try {
                    value = new java.sql.Timestamp(timeParser.parseDateTime(valueParam).getMillis());
                } catch (Exception pe) {
                    log.error("incorrect value format for parameter " + name, pe);
                }
            }
        }
        if (valueParam == null && valueListParam == null) {
            if (isBoolean) {
                value = obj.getBooleanValue().toString();
            } else if (isInteger) {
                value = obj.getIntValue().toString();
            } else if (isDouble) {
                value = obj.getDecimalValue().toString();
            } else if (isString) {
                value = obj.getStringValue();
            } else if (isDate) {
                value = obj.getDateValue();
            } else if (isTime) {
                value = obj.getTimeValue();
            } else {
                log.error(" unknown type - " + type);
                value = obj.getStringValue();
                isString = true;
            }
        }
        isInitialized = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getTapestryType() {
        if (valueListParam != null) {
            return "choice";
        } else {
            return type;
        }
    }

    public String getLabel() {
        return label != null ? label : name;
    }

    public String getHelp() {
        return help;
    }

    public boolean hasHelp() {
        return help != null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public Object getValue() {
        if (isString && isOptional && value == null) return "";
        if (isInteger && isOptional && value == null) return 0;
        if (isDouble && isOptional && value == null) return 0.0;
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isReadonly() {
        return isReadonly;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public boolean isInteger() {
        return isInteger;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public boolean isString() {
        return isString;
    }

    public boolean isTime() {
        return isTime;
    }

    public boolean isDate() {
        return isDate;
    }

    public String getStringValue() {
        if (isDate) {
            return dateParser.print(((Date) getValue()).getTime());
        } else if (isTime) {
            return timeParser.print(((Date) getValue()).getTime());
        }
        return getValue().toString();
    }

    public String getValueList() {
        return valueListParam;
    }

    public IPropertySelectionModel initialModel(EquandaBasePage pageForI18n) {
        if (valueListParam == null) {
            return null;
        }
        String labelPrefix = "report." + reportName + '.' + name + '.';
        return new SuggestValueList(isDate() || isTime(), valueListParam, labelPrefix, pageForI18n);
    }

    public boolean hasTableAndField() {
        return tableParam != null && fieldParam != null;
    }

    public String getTable() {
        return tableParam;
    }

    public String getField() {
        return fieldParam;
    }
}
