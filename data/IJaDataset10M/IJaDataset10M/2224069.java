package org.openflashchart.json;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openflashchart.chart.BaseGraph;
import org.openflashchart.chart.BaseGraphProperty;
import org.openflashchart.chart.Chart;
import org.openflashchart.component.Elements;
import org.openflashchart.component.Labels;
import org.openflashchart.elements.Element;

/**
 * The format string to Json
 * 
 * @author zhuzhenhua
 * 
 */
public class JsonHelper {

    private static final String DOUBLE_QUOTATION_MARKS = "\"";

    private static final String CR = "\n";

    private static final String CRLF = "\r\n\r\n";

    private static final String ODD_TAB = "\t";

    private static final String DOUBLE_TAB = "\t\t";

    private static final String COMMA = ",";

    private static final String COLON = ":";

    private static final String BRACES_LEFT = "{";

    private static final String BRACES_RIGTH = "}";

    private static final String BRACES_LEFT_WITH_SINGLE_MARKS = "'{'";

    private static final String BRACES_RIGTH_WITH_SINGLE_MARKS = "'}'";

    private static final String BRACKET_LEFT = "[";

    private static final String BRACKET_RIGTH = "]";

    private static final String BASE_PROPERTY_UINT_PATTERN = DOUBLE_TAB + DOUBLE_QUOTATION_MARKS + "{0}" + DOUBLE_QUOTATION_MARKS + COLON + ODD_TAB + DOUBLE_QUOTATION_MARKS + "{1}" + DOUBLE_QUOTATION_MARKS + COMMA + CR;

    private static final String BASE_BRACES_UINT_PATTERN = DOUBLE_QUOTATION_MARKS + "{0}" + DOUBLE_QUOTATION_MARKS + COLON + DOUBLE_QUOTATION_MARKS + "{1}" + DOUBLE_QUOTATION_MARKS + COMMA;

    private static final String BASE_PROPERTY_UINT_PATTERN_NO_MARKS = DOUBLE_TAB + DOUBLE_QUOTATION_MARKS + "{0}" + DOUBLE_QUOTATION_MARKS + COLON + ODD_TAB + "{1}" + COMMA + CR;

    private static final String BASE_PROPERTY_PATTERN = DOUBLE_QUOTATION_MARKS + "{0}" + DOUBLE_QUOTATION_MARKS + COLON + BRACES_LEFT_WITH_SINGLE_MARKS + CR + "{1}" + ODD_TAB + BRACES_RIGTH_WITH_SINGLE_MARKS;

    private static final String BASE_ELEMENT_PATTERN = BRACES_LEFT_WITH_SINGLE_MARKS + CR + "{0}" + ODD_TAB + BRACES_RIGTH_WITH_SINGLE_MARKS;

    private static final String BASE_ELEMENTS_PATTERN = ODD_TAB + DOUBLE_QUOTATION_MARKS + "{0}" + DOUBLE_QUOTATION_MARKS + COLON + ODD_TAB + "{1}";

    private static final String CHART_PATTERN = BRACES_LEFT_WITH_SINGLE_MARKS + CR + "{0}" + BRACES_RIGTH_WITH_SINGLE_MARKS;

    private static final String BRACES_PATTERN = BRACES_LEFT_WITH_SINGLE_MARKS + "{0}" + BRACES_RIGTH_WITH_SINGLE_MARKS;

    /**
	 * 
	 * get field name and value then fill into map by recursive
	 * 
	 * @param classObj
	 * @param level
	 *            recursive's level
	 * @param map
	 *            store field name and value
	 * @return void
	 * 
	 */
    @SuppressWarnings("unchecked")
    private static void getFieldNamesAndValues(Object classObj, int level, Map map) {
        Field[] fields = null;
        Class c = classObj.getClass();
        for (int l = 0; l < level; ++l) {
            c = c.getSuperclass();
        }
        fields = c.getDeclaredFields();
        for (Field field : fields) {
            Object fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = field.get(classObj);
            } catch (IllegalArgumentException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }
            if (fieldValue != null) {
                map.put(field.getName(), fieldValue);
            }
        }
        if (!c.getName().equals("java.lang.Object")) {
            getFieldNamesAndValues(classObj, ++level, map);
        }
    }

    /**
	 * replace last comma
	 * 
	 * @param jsonSB
	 * @return StringBuffer result of replaced
	 */
    private static void shearComma(StringBuffer jsonSB) {
        int commaPosition = jsonSB.lastIndexOf(COMMA);
        if (commaPosition > -1) {
            jsonSB.replace(commaPosition, commaPosition + 1, "");
        }
    }

    /**
	 * Get json string of parameter,this method is format class about graphic's
	 * in this project
	 * 
	 * @param classObj
	 * @return String fomated string
	 * 
	 */
    public static String getJsonString(Object classObj) {
        if (classObj instanceof BaseGraphProperty || classObj instanceof Element) {
            return getJsonStringForBaseProperty(classObj);
        } else if (classObj instanceof Chart) {
            return getjsonStringForChart(classObj);
        }
        return "";
    }

    /**
	 * Format base property of Element or graphic's element
	 * 
	 * @param classObj
	 * @return String
	 * 
	 */
    @SuppressWarnings("unchecked")
    private static String getJsonStringForBaseProperty(Object classObj) {
        if (classObj instanceof Elements) {
            return formatPorpertyString(BASE_ELEMENTS_PATTERN, new Object[] { classObj.getClass().getSimpleName().toLowerCase(), ((Elements) classObj).getElements() });
        }
        Map<Object, Object> hashMap = new HashMap<Object, Object>();
        getFieldNamesAndValues(classObj, 0, hashMap);
        StringBuffer jsonSB = new StringBuffer();
        Set keySet = hashMap.keySet();
        for (Object key : keySet) {
            String fieldName = (String) key;
            if (fieldName != null) {
                Object fieldValue = hashMap.get(key);
                fieldName = revisionFieldName(fieldName);
                if (null != fieldValue) {
                    if (fieldValue instanceof Labels) {
                        jsonSB.append(DOUBLE_TAB).append(getJsonStringForBaseProperty(fieldValue)).append(COMMA).append(CR);
                    } else if (fieldValue instanceof List) {
                        StringBuffer tempSB = new StringBuffer();
                        parseList(fieldName, fieldValue, tempSB);
                        jsonSB.append(formatPorpertyString(BASE_PROPERTY_UINT_PATTERN_NO_MARKS, new Object[] { fieldName, tempSB }));
                    } else {
                        if (fieldValue instanceof String && !isNumerical((String) fieldValue)) {
                            jsonSB.append(formatPorpertyString(BASE_PROPERTY_UINT_PATTERN, new Object[] { fieldName, fieldValue }));
                        } else {
                            jsonSB.append(formatPorpertyString(BASE_PROPERTY_UINT_PATTERN_NO_MARKS, new Object[] { fieldName, fieldValue }));
                        }
                    }
                }
            }
        }
        shearComma(jsonSB);
        if (classObj instanceof Element) {
            return formatPorpertyString(BASE_ELEMENT_PATTERN, new Object[] { jsonSB });
        } else if (classObj instanceof BaseGraphProperty || classObj instanceof Labels) {
            return formatPorpertyString(BASE_PROPERTY_PATTERN, new Object[] { classObj.getClass().getSimpleName().toLowerCase(), jsonSB });
        } else {
            return formatPorpertyString(BASE_ELEMENTS_PATTERN, new Object[] { classObj.getClass().getSimpleName().toLowerCase(), jsonSB });
        }
    }

    /**
	 * 
	 * Format chart to json
	 * 
	 * @param classObj
	 * @return String
	 * 
	 */
    @SuppressWarnings("unchecked")
    private static String getjsonStringForChart(Object classObj) {
        StringBuffer jsonSB = new StringBuffer();
        Map<Object, Object> hashMap = new HashMap<Object, Object>();
        getFieldNamesAndValues(classObj, 0, hashMap);
        Set keySet = hashMap.keySet();
        for (Object key : keySet) {
            String fieldName = (String) key;
            Object fieldValue = hashMap.get(key);
            if (null != fieldValue) {
                if (fieldValue instanceof List) {
                    List<BaseGraph> graphs = (List) fieldValue;
                    for (BaseGraph graph : graphs) {
                        jsonSB.append(ODD_TAB).append(graph).append(COMMA).append(CRLF);
                    }
                } else if (fieldValue instanceof String) {
                    jsonSB.append(formatPorpertyString(BASE_PROPERTY_UINT_PATTERN, new Object[] { fieldName, fieldValue }));
                }
            }
        }
        shearComma(jsonSB);
        return formatPorpertyString(CHART_PATTERN, new Object[] { jsonSB });
    }

    /**
	 * 
	 * Parse List and then refactor to json
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param jsonSB
	 * @return void
	 * 
	 */
    @SuppressWarnings("unchecked")
    private static void parseList(String fieldName, Object fieldValue, StringBuffer tempSB) {
        List<String> fieldValueList = (List) fieldValue;
        StringBuffer fieldValueTemp = new StringBuffer(BRACKET_LEFT);
        for (int i = 0, len = fieldValueList.size(); i < len; ++i) {
            Object itemObj = fieldValueList.get(i);
            if (itemObj instanceof String || null == itemObj) {
                String fieldValueListItem = (null == itemObj ? "" : (String) itemObj);
                if (!(fieldValueListItem.startsWith(BRACES_LEFT) && fieldValueListItem.endsWith(BRACES_RIGTH))) {
                    fieldValueTemp.append(DOUBLE_QUOTATION_MARKS + fieldValueListItem + DOUBLE_QUOTATION_MARKS);
                } else {
                    fieldValueTemp.append(parseBraces(fieldValueListItem));
                }
            } else if (itemObj instanceof List) {
                parseList(fieldName, itemObj, fieldValueTemp);
            } else {
                fieldValueTemp.append(itemObj);
            }
            fieldValueTemp.append(COMMA);
        }
        fieldValueTemp.append(BRACKET_RIGTH);
        shearComma(fieldValueTemp);
        tempSB.append(fieldValueTemp);
    }

    /**
	 * 
	 * Parse string with braces and then refactor to json
	 * 
	 * @param item
	 * @return StringBuffer
	 * 
	 */
    private static String parseBraces(String item) {
        String brace = null;
        StringBuffer returnSB = new StringBuffer();
        brace = item.substring(1, item.length() - 1);
        if (!"".equals(brace)) {
            String[] braces = brace.split(COMMA);
            for (int i = 0, len = braces.length; i < len; ++i) {
                String[] items = braces[i].split(COLON);
                if (2 == items.length) {
                    if (isNumerical(items[1])) {
                        returnSB.append(formatPorpertyString(BASE_PROPERTY_UINT_PATTERN_NO_MARKS.replace(ODD_TAB, "").replace(CR, ""), new Object[] { items[0], items[1] }));
                    } else {
                        returnSB.append(formatPorpertyString(BASE_BRACES_UINT_PATTERN, new Object[] { items[0], items[1] }));
                    }
                }
            }
            shearComma(returnSB);
            return formatPorpertyString(BRACES_PATTERN, new Object[] { returnSB }).toString();
        }
        return "";
    }

    /**
	 * Refactor field name
	 * 
	 * @param fieldName
	 * @return String
	 */
    private static String revisionFieldName(String fieldName) {
        if (null != fieldName) {
            if (fieldName.startsWith("list_")) {
                fieldName = fieldName.replaceAll("list_", "");
            }
            return fieldName.replaceAll("___", "").replaceAll("__", "-");
        }
        return "";
    }

    /**
	 * Decide parameter is numerical
	 * 
	 * @param strVal
	 * @return boolean
	 */
    private static boolean isNumerical(String strVal) {
        Pattern pattern = Pattern.compile("[+-]?\\d+(?:\\.\\d+)?");
        Matcher m = pattern.matcher(strVal);
        return m.matches();
    }

    /**
	 * Format target object to json
	 * 
	 * @param pattern
	 * @param targetObj
	 * @return String
	 */
    private static String formatPorpertyString(String pattern, Object[] targetObj) {
        return (new MessageFormat(pattern)).format(targetObj);
    }
}
