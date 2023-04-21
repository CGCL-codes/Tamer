package csiebug.web.html.form;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.naming.NamingException;
import csiebug.util.AssertUtility;
import csiebug.util.DateFormatException;
import csiebug.util.DateFormatUtility;
import csiebug.util.NumberFormatUtility;
import csiebug.util.StringUtility;
import csiebug.util.WebUtility;
import csiebug.web.html.HtmlBuilder;
import csiebug.web.html.HtmlComponent;
import csiebug.web.html.HtmlRenderException;

/**
 * 產生HTML Text
 * @author George_Tsai
 * @version 2009/6/15
 */
public class HtmlText extends HtmlComponent {

    private String htmlId;

    private String name;

    private String isReadOnly;

    private String className;

    private String isReturnValue;

    private String defaultValue;

    private String dataType;

    private String isMasked;

    private String onChange;

    private String onBlur;

    private String onClick;

    private String onKeyDown;

    private String header;

    private String headerClass;

    private String footer;

    private String footerClass;

    private String buttonClass;

    private String isRequired;

    private String userValue;

    private OpEnum op;

    private String maxlength;

    private String fixlength;

    private String size;

    private String style;

    private String maxvalue;

    private String minvalue;

    private String maxinteger;

    private String maxdecimal;

    private String numberStep;

    private String imagePath;

    private String typesetting;

    private WebUtility webutil = new WebUtility();

    public HtmlText(String htmlId, String name, String isReadOnly, String className, String isReturnValue, String defaultValue, String dataType, String isMasked, String onChange, String onBlur, String onClick, String onKeyDown, String header, String headerClass, String footer, String footerClass, String buttonClass, String isRequired, String userValue, OpEnum op, String maxlength, String fixlength, String size, String style, String maxvalue, String minvalue, String maxinteger, String maxdecimal, String numberStep, String imagePath, String typesetting) {
        this.htmlId = htmlId;
        this.name = name;
        this.isReadOnly = isReadOnly;
        this.className = className;
        this.isReturnValue = isReturnValue;
        this.defaultValue = defaultValue;
        this.dataType = dataType;
        this.isMasked = isMasked;
        this.onChange = onChange;
        this.onBlur = onBlur;
        this.onClick = onClick;
        this.onKeyDown = onKeyDown;
        this.header = header;
        this.headerClass = headerClass;
        this.footer = footer;
        this.footerClass = footerClass;
        this.buttonClass = buttonClass;
        this.isRequired = isRequired;
        this.userValue = userValue;
        this.op = op;
        this.maxlength = maxlength;
        this.fixlength = fixlength;
        this.size = size;
        this.style = style;
        this.maxvalue = maxvalue;
        this.minvalue = minvalue;
        this.maxinteger = maxinteger;
        this.maxdecimal = maxdecimal;
        this.numberStep = numberStep;
        this.imagePath = StringUtility.removeStartEndSlash(imagePath);
        this.typesetting = typesetting;
    }

    public HtmlText() {
    }

    public String renderStart() throws HtmlRenderException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        try {
            if (typesetting == null || !typesetting.equalsIgnoreCase("false")) {
                htmlBuilder.tdStart().className("TdHeader").tagClose();
                String strIsRequired = "false";
                if (htmlId != null) {
                    if (webutil.getRequestAttribute(htmlId + "_IsRequired") != null) {
                        if (webutil.getRequestAttribute(htmlId + "_IsRequired").toString().equals("true")) {
                            strIsRequired = "true";
                        }
                    } else {
                        if (AssertUtility.isTrue(isRequired)) {
                            strIsRequired = "true";
                        }
                    }
                } else {
                    if (AssertUtility.isTrue(isRequired)) {
                        strIsRequired = "true";
                    }
                }
                if (header != null || strIsRequired.equalsIgnoreCase("true")) {
                    htmlBuilder.labelStart();
                    if (htmlId != null) {
                        htmlBuilder.id(htmlId + "_header");
                    }
                    if (name != null) {
                        htmlBuilder.name(name + "_header");
                    }
                    if (headerClass != null) {
                        htmlBuilder.className(headerClass);
                    } else {
                        htmlBuilder.className("LabelHeader");
                    }
                    htmlBuilder.tagClose();
                    if (strIsRequired.equalsIgnoreCase("true")) {
                        htmlBuilder.appendString(webutil.getMessage("common.star"));
                    }
                    if (header != null) {
                        htmlBuilder.text(header + "：");
                    }
                    htmlBuilder.labelEnd();
                    htmlBuilder.text("  ");
                }
                htmlBuilder.tdEnd();
                htmlBuilder.tdStart().className("TdBody").tagClose();
            }
        } catch (UnsupportedEncodingException e) {
            throw new HtmlRenderException(e);
        } catch (NamingException e) {
            throw new HtmlRenderException(e);
        }
        return htmlBuilder.toString();
    }

    public String renderBody(String content) throws HtmlRenderException {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        HtmlSelect select = null;
        try {
            if (dataType != null && (dataType.equalsIgnoreCase("IDNO") || dataType.equalsIgnoreCase("password"))) {
                htmlBuilder.inputStart().type("password");
            } else {
                if (dataType != null && dataType.equalsIgnoreCase("time12")) {
                    select = new HtmlSelect();
                    select.setTypesetting("false");
                    select.setBlankOptionFlag("false");
                    Map<String, String> option = new LinkedHashMap<String, String>();
                    option.put("am", webutil.getMessage("common.am"));
                    option.put("pm", webutil.getMessage("common.pm"));
                    webutil.setRequestAttribute("midOption", option);
                    select.setOption("midOption");
                }
                htmlBuilder.inputStart().type("text");
            }
            if (htmlId != null) {
                htmlBuilder.id(htmlId);
                if (select != null) {
                    select.setId(htmlId + "_mid");
                }
            }
            if (name != null) {
                htmlBuilder.name(name);
                if (select != null) {
                    select.setId(name + "_mid");
                }
            }
            boolean readOnlyFlag = false;
            if (htmlId != null && AssertUtility.isTrue(webutil.getRequestAttribute(htmlId + "_IsReadOnly"))) {
                readOnlyFlag = true;
            } else if (AssertUtility.isTrue(isReadOnly)) {
                readOnlyFlag = true;
            }
            if (readOnlyFlag) {
                htmlBuilder.readOnly();
                isReadOnly = "true";
            } else {
                isReadOnly = "false";
            }
            if (select != null) {
                select.setIsReadOnly(isReadOnly);
                if (readOnlyFlag) {
                    select.setClassName("SelectMidReadOnly");
                } else {
                    select.setClassName("SelectMid");
                }
            }
            if (className != null) {
                htmlBuilder.className(className);
            } else {
                if (dataType == null || dataType.equalsIgnoreCase("string") || dataType.equalsIgnoreCase("function") || dataType.equalsIgnoreCase("password")) {
                    if (isReadOnly == null || isReadOnly.equalsIgnoreCase("false")) {
                        htmlBuilder.className("Text");
                    } else {
                        htmlBuilder.className("TextReadOnly");
                    }
                } else if (dataType.equalsIgnoreCase("email")) {
                    if (isReadOnly == null || isReadOnly.equalsIgnoreCase("false")) {
                        htmlBuilder.className("TextEmail");
                    } else {
                        htmlBuilder.className("TextEmailReadOnly");
                    }
                } else if (dataType.equalsIgnoreCase("IDNO")) {
                    if (isReadOnly == null || isReadOnly.equalsIgnoreCase("false")) {
                        htmlBuilder.className("TextIDNO");
                    } else {
                        htmlBuilder.className("TextIDNOReadOnly");
                    }
                } else if (dataType.equalsIgnoreCase("number") || dataType.equalsIgnoreCase("currency")) {
                    if (isReadOnly == null || isReadOnly.equalsIgnoreCase("false")) {
                        htmlBuilder.className("TextCurrency");
                    } else {
                        htmlBuilder.className("TextCurrencyReadOnly");
                    }
                } else if (dataType.toLowerCase().equalsIgnoreCase("date")) {
                    if (isReadOnly == null || isReadOnly.equalsIgnoreCase("false")) {
                        htmlBuilder.className("TextDate");
                    } else {
                        htmlBuilder.className("TextDateReadOnly");
                    }
                } else if (dataType.toLowerCase().startsWith("time")) {
                    if (isReadOnly == null || isReadOnly.equalsIgnoreCase("false")) {
                        htmlBuilder.className("TextTime");
                    } else {
                        htmlBuilder.className("TextTimeReadOnly");
                    }
                } else if (dataType.toLowerCase().equalsIgnoreCase("datetime")) {
                    if (isReadOnly.equalsIgnoreCase("true")) {
                        htmlBuilder.className("TextDatetimeReadOnly");
                    } else {
                        htmlBuilder.className("TextDatetime");
                    }
                } else if (dataType.equalsIgnoreCase("color")) {
                    if (isReadOnly.equalsIgnoreCase("true")) {
                        htmlBuilder.className("TextColorReadOnly");
                    } else {
                        htmlBuilder.className("TextColor");
                    }
                } else if (dataType.equalsIgnoreCase("ip")) {
                    if (isReadOnly.equalsIgnoreCase("true")) {
                        htmlBuilder.className("TextIPReadOnly");
                    } else {
                        htmlBuilder.className("TextIP");
                    }
                } else if (dataType.equalsIgnoreCase("ipv6")) {
                    if (isReadOnly.equalsIgnoreCase("true")) {
                        htmlBuilder.className("TextIPv6ReadOnly");
                    } else {
                        htmlBuilder.className("TextIPv6");
                    }
                }
            }
            if (dataType != null) {
                htmlBuilder.tagProperty("DataType", dataType.toLowerCase());
                if (AssertUtility.isTrue(isMasked)) {
                    if (dataType.equalsIgnoreCase("date")) {
                        String dateFormatString = DateFormatUtility.getDateFormat(Integer.parseInt(webutil.getSysDateFormat())).replaceAll("y", "9").replaceAll("M", "9").replaceAll("d", "9");
                        webutil.addPageLoadScript("$(\"#" + htmlId + "\").mask(\"" + dateFormatString + "\");");
                    } else if (dataType.equalsIgnoreCase("time12") || dataType.equalsIgnoreCase("time24")) {
                        webutil.addPageLoadScript("$(\"#" + htmlId + "\").mask(\"99:99\");");
                    } else if (dataType.equalsIgnoreCase("datetime")) {
                        String dateFormatString = DateFormatUtility.getDateFormat(Integer.parseInt(webutil.getSysDateFormat())).replaceAll("y", "9").replaceAll("M", "9").replaceAll("d", "9");
                        webutil.addPageLoadScript("$(\"#" + htmlId + "\").mask(\"" + dateFormatString + " 99:99\");");
                    } else if (dataType.equalsIgnoreCase("ip")) {
                        webutil.addPageLoadScript("$.mask.definitions['2']='[ 12]';");
                        webutil.addPageLoadScript("$.mask.definitions['9']='[ 1234567890]';");
                        webutil.addPageLoadScript("$(\"#" + htmlId + "\").mask(\"299.299.299.299\");");
                        htmlBuilder.tagProperty("Masked", "true");
                    } else if (dataType.equalsIgnoreCase("ipv6")) {
                        webutil.addPageLoadScript("$.mask.definitions['f']='[ 0123456789abcdef]';");
                        webutil.addPageLoadScript("$(\"#" + htmlId + "\").mask(\"ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\");");
                        htmlBuilder.tagProperty("Masked", "true");
                    }
                }
                if (minvalue != null) {
                    htmlBuilder.tagProperty("minValue", minvalue);
                } else {
                    htmlBuilder.tagProperty("minValue", "-1");
                }
                if (maxvalue != null) {
                    htmlBuilder.tagProperty("maxValue", maxvalue);
                } else {
                    htmlBuilder.tagProperty("maxValue", "-1");
                }
                if (maxinteger != null) {
                    htmlBuilder.tagProperty("maxInteger", maxinteger);
                } else {
                    htmlBuilder.tagProperty("maxInteger", "-1");
                }
                if (maxdecimal != null) {
                    htmlBuilder.tagProperty("maxDecimal", maxdecimal);
                } else {
                    htmlBuilder.tagProperty("maxDecimal", "-1");
                }
            } else {
                htmlBuilder.tagProperty("DataType", "string");
                htmlBuilder.tagProperty("minValue", "-1");
                htmlBuilder.tagProperty("maxValue", "-1");
                htmlBuilder.tagProperty("maxInteger", "-1");
                htmlBuilder.tagProperty("maxDecimal", "-1");
            }
            String strIsRequired = "false";
            if (htmlId != null) {
                if (webutil.getRequestAttribute(htmlId + "_IsRequired") != null) {
                    if (webutil.getRequestAttribute(htmlId + "_IsRequired").toString().equals("true")) {
                        strIsRequired = "true";
                    }
                } else {
                    if (AssertUtility.isTrue(isRequired)) {
                        strIsRequired = "true";
                    }
                }
            } else {
                if (AssertUtility.isTrue(isRequired)) {
                    strIsRequired = "true";
                }
            }
            htmlBuilder.tagProperty("isRequired", strIsRequired);
            String strValue = "";
            if (defaultValue != null) {
                strValue = defaultValue;
            }
            if ((isReturnValue == null || isReturnValue.equalsIgnoreCase("true")) && webutil.getRequest().getParameter(name) != null) {
                strValue = StringUtility.cleanXSSPattern(webutil.getRequest().getParameter(name));
            }
            if (userValue != null && webutil.getRequestAttribute(userValue) != null) {
                strValue = webutil.getRequestAttribute(userValue).toString();
            }
            if (dataType != null && !strValue.equals("")) {
                if (dataType.equalsIgnoreCase("currency")) {
                    strValue = NumberFormatUtility.getCurrency(strValue);
                } else if (dataType.equalsIgnoreCase("color")) {
                    strValue = "#ffffff";
                } else if (dataType.equalsIgnoreCase("time12") && htmlId != null) {
                    strValue = DateFormatUtility.completeHourMinuteSecond(strValue);
                    if (!DateFormatUtility.compareTime(strValue, "11:59")) {
                        webutil.setRequestAttribute(htmlId + "_midOptionValue", "am");
                    } else {
                        webutil.setRequestAttribute(htmlId + "_midOptionValue", "pm");
                        strValue = StringUtility.addZero((Integer.parseInt(strValue.split(":")[0], 10) - 12), 2) + ":" + strValue.split(":")[1];
                    }
                    select.setUserValue(htmlId + "_midOptionValue");
                } else if (dataType.equalsIgnoreCase("time24") && htmlId != null) {
                    strValue = DateFormatUtility.completeHourMinuteSecond(strValue);
                }
            }
            htmlBuilder.value(strValue);
            if (maxlength != null) {
                htmlBuilder.maxLength(maxlength);
            }
            if (fixlength != null) {
                htmlBuilder.tagProperty("fixLength", fixlength);
            } else {
                htmlBuilder.tagProperty("fixLength", "-1");
            }
            if (size != null) {
                htmlBuilder.size(size);
            }
            if (style != null) {
                htmlBuilder.style(style);
            }
            if (onChange != null) {
                htmlBuilder.onChange(onChange);
            } else {
                htmlBuilder.onChange("checkDataType(this, '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
            }
            if (onBlur != null) {
                htmlBuilder.onBlur(onBlur);
            }
            if (onClick != null) {
                htmlBuilder.onClick(onClick);
            } else {
                if ((isReadOnly == null || !isReadOnly.equalsIgnoreCase("true")) && op != null && op.equals(OpEnum.TEXT)) {
                    if (dataType != null && dataType.toLowerCase().equalsIgnoreCase("date")) {
                        if (onChange != null) {
                            if (imagePath != null) {
                                htmlBuilder.onClick("openCalendar(this, " + webutil.getEnvVariable("defaultDateFormat") + ", '" + onChange.replaceAll("'", "&quote") + "', '" + imagePath + "', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            } else {
                                htmlBuilder.onClick("openCalendar(this, " + webutil.getEnvVariable("defaultDateFormat") + ", '" + onChange.replaceAll("'", "&quote") + "', '" + webutil.getBasePathForHTML() + "images', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            }
                        } else {
                            if (imagePath != null) {
                                htmlBuilder.onClick("openCalendar(this, " + webutil.getEnvVariable("defaultDateFormat") + ", '', '" + imagePath + "', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            } else {
                                htmlBuilder.onClick("openCalendar(this, " + webutil.getEnvVariable("defaultDateFormat") + ", '', '" + webutil.getBasePathForHTML() + "images', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            }
                        }
                    }
                    if (dataType != null && dataType.equalsIgnoreCase("color")) {
                        htmlBuilder.onClick("showColorPicker('" + htmlId + "');");
                    }
                }
            }
            if (onKeyDown != null) {
                htmlBuilder.onKeyDown(onKeyDown);
            } else {
                if (isReadOnly == null || !isReadOnly.equalsIgnoreCase("true")) {
                    if (dataType != null && dataType.toLowerCase().equalsIgnoreCase("date")) {
                        htmlBuilder.onKeyDown("closeCalendar(this)");
                    }
                    if (dataType != null && dataType.equalsIgnoreCase("currency")) {
                        if (!AssertUtility.isNotNullAndNotSpace(numberStep)) {
                            if (maxlength != null) {
                                htmlBuilder.onKeyDown("checkComma(this, " + maxlength + ")");
                            } else {
                                htmlBuilder.onKeyDown("checkComma(this, 0)");
                            }
                        } else {
                            String max = "-1";
                            String min = "-1";
                            if (AssertUtility.isNotNullAndNotSpace(maxvalue)) {
                                max = maxvalue;
                            }
                            if (AssertUtility.isNotNullAndNotSpace(minvalue)) {
                                min = minvalue;
                            }
                            if (maxlength != null) {
                                htmlBuilder.onKeyDown("checkComma(this, " + maxlength + ");stepNumber(this, event, " + numberStep + ", " + max + ", " + min + ");");
                            } else {
                                htmlBuilder.onKeyDown("checkComma(this, 0);stepNumber(this, event, " + numberStep + ", " + max + ", " + min + ");");
                            }
                        }
                    }
                    if (dataType != null && dataType.equalsIgnoreCase("number") && AssertUtility.isNotNullAndNotSpace(numberStep)) {
                        String max = "-1";
                        String min = "-1";
                        if (AssertUtility.isNotNullAndNotSpace(maxvalue)) {
                            max = maxvalue;
                        }
                        if (AssertUtility.isNotNullAndNotSpace(minvalue)) {
                            min = minvalue;
                        }
                        htmlBuilder.onKeyDown("stepNumber(this, event, " + numberStep + ", " + max + ", " + min + ");");
                    }
                    if (dataType != null && dataType.equalsIgnoreCase("color")) {
                        htmlBuilder.onKeyDown("hideColorPicker('" + htmlId + "')");
                    }
                }
            }
            htmlBuilder.tagClose();
            htmlBuilder.inputEnd();
            if (isReadOnly == null || !isReadOnly.equalsIgnoreCase("true")) {
                if (dataType != null && dataType.toLowerCase().equalsIgnoreCase("date")) {
                    if (op == null || op.equals(OpEnum.BUTTON)) {
                        htmlBuilder.space();
                        if (imagePath != null) {
                            if (style != null) {
                                htmlBuilder.imageStart().src(imagePath + "/calendar_icon.gif").style("vertical-align:top;cursor:pointer;" + style);
                            } else {
                                htmlBuilder.imageStart().src(imagePath + "/calendar_icon.gif").style("vertical-align:top;cursor:pointer;");
                            }
                        } else {
                            if (style != null) {
                                htmlBuilder.imageStart().src(webutil.getBasePathForHTML() + "images/calendar_icon.gif").style("vertical-align:top;cursor:pointer;" + style);
                            } else {
                                htmlBuilder.imageStart().src(webutil.getBasePathForHTML() + "images/calendar_icon.gif").style("vertical-align:top;cursor:pointer;");
                            }
                        }
                        if (htmlId != null) {
                            htmlBuilder.id(htmlId + "_button");
                        }
                        if (buttonClass != null) {
                            htmlBuilder.className(buttonClass);
                        }
                        if (onChange != null) {
                            if (imagePath != null) {
                                htmlBuilder.onClick("openCalendar(document.getElementById('" + htmlId + "'), " + webutil.getEnvVariable("defaultDateFormat") + ", '" + onChange.replaceAll("'", "&quote") + "', '" + imagePath + "', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            } else {
                                htmlBuilder.onClick("openCalendar(document.getElementById('" + htmlId + "'), " + webutil.getEnvVariable("defaultDateFormat") + ", '" + onChange.replaceAll("'", "&quote") + "', '" + webutil.getBasePathForHTML() + "images', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            }
                        } else {
                            if (imagePath != null) {
                                htmlBuilder.onClick("openCalendar(document.getElementById('" + htmlId + "'), " + webutil.getEnvVariable("defaultDateFormat") + ", '', '" + imagePath + "', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            } else {
                                htmlBuilder.onClick("openCalendar(document.getElementById('" + htmlId + "'), " + webutil.getEnvVariable("defaultDateFormat") + ", '', '" + webutil.getBasePathForHTML() + "images', '" + webutil.getMessage("common.warning") + "', '" + webutil.getMessage("common.ok") + "', '" + webutil.getMessage("common.error.interval") + "', '" + webutil.getMessage("common.error.DataType1Start") + "', '" + webutil.getMessage("common.error.DataType1End") + "', '" + webutil.getMessage("common.error.DataType2") + "', '" + webutil.getMessage("common.error.DataType3") + "', '" + webutil.getMessage("common.error.DataType4") + "', '" + webutil.getMessage("common.error.DataType5") + "', '" + webutil.getMessage("common.error.DataType6") + "', '" + webutil.getMessage("common.error.DataType7") + "', '" + webutil.getMessage("common.error.DataType8Start") + "', '" + webutil.getMessage("common.error.DataType8End") + "', '" + webutil.getMessage("common.error.DataType9Start") + "', '" + webutil.getMessage("common.error.DataType9End") + "', '" + webutil.getMessage("common.error.DataType10Start") + "', '" + webutil.getMessage("common.error.DataType10End") + "', '" + webutil.getMessage("common.error.DataType11") + "', '" + webutil.getMessage("common.error.DataType12Start") + "', '" + webutil.getMessage("common.error.DataType12End") + "', '" + webutil.getMessage("common.error.DataType13") + "', '" + webutil.getMessage("common.error.DataType14") + "');");
                            }
                        }
                        htmlBuilder.tagClose();
                        htmlBuilder.imageEnd();
                    }
                    htmlBuilder.divStart().id(htmlId + "_calendar");
                    htmlBuilder.className("calendar");
                    htmlBuilder.style("position:absolute;left:0;top:0; z-index:5;display:none;");
                    htmlBuilder.tagClose();
                    htmlBuilder.divEnd();
                } else if (dataType != null && dataType.equalsIgnoreCase("color")) {
                    if (op == null || op.equals(OpEnum.BUTTON)) {
                        htmlBuilder.space();
                        if (imagePath != null) {
                            if (style != null) {
                                htmlBuilder.imageStart().src(imagePath + "/colorize.png").style("vertical-align:top;cursor:pointer;width:22px;height:22px" + style);
                            } else {
                                htmlBuilder.imageStart().src(imagePath + "/colorize.png").style("vertical-align:top;cursor:pointer;width:22px;height:22px");
                            }
                        } else {
                            if (style != null) {
                                htmlBuilder.imageStart().src(webutil.getBasePathForHTML() + "images/colorize.png").style("vertical-align:top;cursor:pointer;width:22px;height:22px" + style);
                            } else {
                                htmlBuilder.imageStart().src(webutil.getBasePathForHTML() + "images/colorize.png").style("vertical-align:top;cursor:pointer;width:22px;height:22px");
                            }
                        }
                        if (htmlId != null) {
                            htmlBuilder.id(htmlId + "_button");
                        }
                        if (buttonClass != null) {
                            htmlBuilder.className(buttonClass);
                        }
                        htmlBuilder.onClick("showColorPicker('" + htmlId + "');");
                        htmlBuilder.tagClose();
                        htmlBuilder.imageEnd();
                    }
                    htmlBuilder.divStart().id(htmlId + "_colorPicker");
                    htmlBuilder.style("position:absolute;left:0;top:0; z-index:5;display:none;");
                    htmlBuilder.tagClose();
                    htmlBuilder.divEnd();
                    webutil.addPageLoadScript("$('#" + htmlId + "_colorPicker').farbtastic('#" + htmlId + "');");
                } else if (dataType != null && dataType.equalsIgnoreCase("function")) {
                    htmlBuilder.space();
                    if (imagePath != null) {
                        if (style != null) {
                            htmlBuilder.imageStart().src(imagePath + "/search_icon.gif").style("vertical-align:top;cursor:pointer;" + style);
                        } else {
                            htmlBuilder.imageStart().src(imagePath + "/search_icon.gif").style("vertical-align:top;cursor:pointer;");
                        }
                    } else {
                        if (style != null) {
                            htmlBuilder.imageStart().src(webutil.getBasePathForHTML() + "images/search_icon.gif").style("vertical-align:top;cursor:pointer;" + style);
                        } else {
                            htmlBuilder.imageStart().src(webutil.getBasePathForHTML() + "images/search_icon.gif").style("vertical-align:top;cursor:pointer;");
                        }
                    }
                    if (htmlId != null) {
                        htmlBuilder.id(htmlId + "_button");
                    }
                    if (buttonClass != null) {
                        htmlBuilder.className(buttonClass);
                    }
                    htmlBuilder.onClick(htmlId + "_onClick(document.getElementById('" + htmlId + "'));");
                    htmlBuilder.tagClose();
                    htmlBuilder.imageEnd();
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new HtmlRenderException(e);
        } catch (NamingException e) {
            throw new HtmlRenderException(e);
        } catch (DateFormatException e) {
            throw new HtmlRenderException(e);
        }
        if (select != null) {
            return select.render() + htmlBuilder.toString();
        } else {
            return htmlBuilder.toString();
        }
    }

    public String renderEnd() {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        if (footer != null) {
            htmlBuilder.labelStart();
            if (htmlId != null) {
                htmlBuilder.id(htmlId + "_footer");
            }
            if (name != null) {
                htmlBuilder.name(name + "_footer");
            }
            if (footerClass != null) {
                htmlBuilder.className(footerClass);
            } else {
                htmlBuilder.className("LabelFooter");
            }
            htmlBuilder.tagClose();
            htmlBuilder.text(footer);
            htmlBuilder.labelEnd();
        }
        if (typesetting == null || !typesetting.equalsIgnoreCase("false")) {
            htmlBuilder.tdEnd();
        }
        return htmlBuilder.toString();
    }

    public void setId(String id) {
        this.htmlId = id;
    }

    public String getId() {
        return this.htmlId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setIsReadOnly(String readOnly) {
        this.isReadOnly = readOnly;
    }

    public String getIsReadOnly() {
        return this.isReadOnly;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setIsReturnValue(String returnValue) {
        this.isReturnValue = returnValue;
    }

    public String getIsReturnValue() {
        return this.isReturnValue;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getOnChange() {
        return this.onChange;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnClick() {
        return this.onClick;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    public String getHeaderClass() {
        return this.headerClass;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooterClass(String footerClass) {
        this.footerClass = footerClass;
    }

    public String getFooterClass() {
        return this.footerClass;
    }

    public void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    public String getButtonClass() {
        return this.buttonClass;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getIsRequired() {
        return this.isRequired;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setUserValue(String value) {
        this.userValue = value;
    }

    public String getUserValue() {
        return this.userValue;
    }

    public void setOp(OpEnum value) {
        this.op = value;
    }

    public OpEnum getOp() {
        return this.op;
    }

    public void setMaxlength(String value) {
        this.maxlength = value;
    }

    public String getMaxlength() {
        return this.maxlength;
    }

    public void setFixlength(String value) {
        this.fixlength = value;
    }

    public String getFixlength() {
        return this.fixlength;
    }

    public void setSize(String value) {
        this.size = value;
    }

    public String getSize() {
        return this.size;
    }

    public void setStyle(String value) {
        this.style = value;
    }

    public String getStyle() {
        return this.style;
    }

    public void setMaxvalue(String value) {
        this.maxvalue = value;
    }

    public String getMaxvalue() {
        return this.maxvalue;
    }

    public void setMinvalue(String value) {
        this.minvalue = value;
    }

    public String getMinvalue() {
        return this.minvalue;
    }

    public void setMaxinteger(String value) {
        this.maxinteger = value;
    }

    public String getMaxinteger() {
        return this.maxinteger;
    }

    public void setMaxdecimal(String value) {
        this.maxdecimal = value;
    }

    public String getMaxdecimal() {
        return this.maxdecimal;
    }

    public void setImagePath(String value) {
        this.imagePath = StringUtility.removeStartEndSlash(value);
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setTypesetting(String value) {
        this.typesetting = value;
    }

    public String getTypesetting() {
        return this.typesetting;
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    public String getOnBlur() {
        return onBlur;
    }

    public void setOnKeyDown(String onKeyDown) {
        this.onKeyDown = onKeyDown;
    }

    public String getOnKeyDown() {
        return onKeyDown;
    }

    public void setIsMasked(String isMasked) {
        this.isMasked = isMasked;
    }

    public String getIsMasked() {
        return isMasked;
    }

    public void setNumberStep(String numberStep) {
        this.numberStep = numberStep;
    }

    public String getNumberStep() {
        return numberStep;
    }
}
