package org.ujac.util.exi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Collection;
import java.util.Properties;
import org.ujac.util.BeanException;
import org.ujac.util.BeanUtils;
import org.ujac.util.exi.type.BaseNumberType;
import org.ujac.util.exi.type.ByteArrayType;
import org.ujac.util.exi.type.ByteType;
import org.ujac.util.exi.type.CharArrayType;
import org.ujac.util.exi.type.CharType;
import org.ujac.util.exi.type.ConditionResultHolderType;
import org.ujac.util.exi.type.DoubleArrayType;
import org.ujac.util.exi.type.EnumType;
import org.ujac.util.exi.type.FloatArrayType;
import org.ujac.util.exi.type.FloatType;
import org.ujac.util.exi.type.IntArrayType;
import org.ujac.util.exi.type.LongArrayType;
import org.ujac.util.exi.type.MapEntryType;
import org.ujac.util.exi.type.NullValueType;
import org.ujac.util.exi.type.ObjectArrayType;
import org.ujac.util.exi.type.MapType;
import org.ujac.util.exi.type.ResourceBundleType;
import org.ujac.util.exi.type.RowType;
import org.ujac.util.exi.type.DateType;
import org.ujac.util.exi.type.LongType;
import org.ujac.util.exi.type.ShortArrayType;
import org.ujac.util.exi.type.ShortType;
import org.ujac.util.exi.type.TableType;
import org.ujac.util.exi.type.DoubleType;
import org.ujac.util.exi.type.StringType;
import org.ujac.util.exi.type.DefaultType;
import org.ujac.util.exi.type.IntegerType;
import org.ujac.util.exi.type.BooleanType;
import org.ujac.util.exi.type.CollectionType;
import org.ujac.util.exi.type.TimeType;
import org.ujac.util.exi.type.TimestampType;
import org.ujac.util.text.FormatHelper;

/**
 * Title:       ExpressionInterpreter<br>
 * Description: Interpreter for expressions fitting into the following syntax.<br>
 * Basically an expression consists of an object, an operation and an operand: ${object operation operand} <br>
 * The operation and the operand are optional.<br>
 * The object can be an expression itself, so encapsulation of expressions is possible. <br> 
 * Example: ${${today decrYear 1} getYear} produces a String containing the value of last year (currently 2001). <br>
 * <br>
 * Operations can be operators as well. Supported operators are +, -, / and * for mathematical operations. <br> 
 * Additional there are two operators '.' and '->' which can be used to reference a property on an object. <br>
 * The special thing about operators is, that they do not need to be separated from the object and the operand. 
 * So is for example the expression ${test+10} valid, in case the object test contains a numeric object.<br>
 * <br>
 * List of currently supported operations for Date values: <br>
 * <b>incrYear</b>: used to increment the refering date by an amount of years. <br> 
 * <b>incrMonth</b>: used to increment the refering date by an amount of months. <br> 
 * <b>incrDay</b>: used to increment the refering date by an amount of days. <br> 
 * <b>decrYear</b>: used to decrement the refering date by an amount of years. <br> 
 * <b>decrMonth</b>: used to decrement the refering date by an amount of months. <br> 
 * <b>decrDay</b>: used to decrement the refering date by an amount of days. <br> 
 * <b>min</b>: used to determine the minimum value of object and operand. <br> 
 * <b>max</b>: used to determine the maximum value of object and operand. <br> 
 * <b>prevUltimo</b>: used to determine the previous ultimo date of the refering date. <br> 
 * <b>nextUltimo</b>: used to determine the next ultimo date of the refering date. <br> 
 * <b>getYear</b>: used to fetch the year from the refering date. <br>
 * <b>getMonth</b>: used to fetch the month from the refering date. <br>
 * <b>getDay</b>: used to fetch the day from the refering date. <br>
 * <b>properties, accessible by reference operator: <br>
 * &nbsp;<b>year</b>: Same as getYear<br>
 * &nbsp;<b>month</b>: Same as getMonth<br>
 * &nbsp;<b>day</b>: Same as getDay<br>
 * <br>
 * List of currently supported operations for String values: <br>
 * <b>length</b>: used to fetch the length from the refering string. <br> 
 * <br>
 * List of currently supported operations for Integer values: <br>
 * <b>min</b>: used to determine the minimum value of object and operand. <br> 
 * <b>max</b>: used to determine the maximum value of object and operand. <br> 
 * <b>add</b> or <b>+</b>: used to add the value of the operand to the object. <br> 
 * <b>sub</b> or <b>-</b>: used to substract the value of the operand from the object. <br> 
 * <b>mul</b> or <b>*</b>: used to multiply the value of the operand with the object. <br> 
 * <b>div</b> or <b>/</b>: used to divide the operand value from the object's value. <br> 
 * <br>
 * List of currently supported operations for Long values: <br>
 * <b>min</b>: used to determine the minimum value of object and operand. <br> 
 * <b>max</b>: used to determine the maximum value of object and operand. <br> 
 * <b>add</b> or <b>+</b>: used to add the value of the operand to the object. <br> 
 * <b>sub</b> or <b>-</b>: used to substract the value of the operand from the object. <br> 
 * <b>mul</b> or <b>*</b>: used to multiply the value of the operand with the object. <br> 
 * <b>div</b> or <b>/</b>: used to divide the operand value from the object's value. <br> 
 * <br>
 * List of currently supported operations for Double values: <br>
 * <b>min</b>: used to determine the minimum value of object and operand. <br> 
 * <b>max</b>: used to determine the maximum value of object and operand. <br> 
 * <b>add</b> or <b>+</b>: used to add the value of the operand to the object. <br> 
 * <b>sub</b> or <b>-</b>: used to substract the value of the operand from the object. <br> 
 * <b>mul</b> or <b>*</b>: used to multiply the value of the operand with the object. <br> 
 * <b>div</b> or <b>/</b>: used to divide the operand value from the object's value. <br> 
 * <br>
 * @author lauerc
 */
public class ExpressionInterpreter implements Cloneable {

    /** The attribute name to use to storer expression interpreter instances. */
    public static final String ATTRIBUTE_NAME = "org.ujac.expression.interpreter";

    /** Constant for the parse state 'object'. */
    public static final int PS_OBJECT = 1;

    /** Constant for the parse state 'operation'. */
    public static final int PS_OPERATION = 2;

    /** Constant for the parse state 'operand'. */
    public static final int PS_OPERAND = 3;

    /** The name of the default type handler. */
    public static final Class DEFAULT_TYPE = Object.class;

    /** The name of the null value type handler. */
    public static final Class NULL_VALUE_TYPE = NullType.class;

    /** The argument type list for a getter method. */
    public static final Class<?>[] GETTER_ARG_TYPES = new Class<?>[] {};

    /** The argument list for a getter method. */
    public static final Object[] GETTER_ARGS = new Object[] {};

    /** The argument list for a getter method. */
    private DateFormat intDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /** The argument list for a getter method. */
    private DateFormat intTimeFormat = new SimpleDateFormat("HH:mm:ss");

    /** The argument list for a getter method. */
    private DateFormat intTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** The instance to use as prototype. */
    private static ExpressionInterpreter prototype = new ExpressionInterpreter();

    /** A map holding the registered type handlers by their names. */
    private List typeHandlerList = new ArrayList();

    /** A map holding the registered type handlers by their names. */
    private Map typeHandlers = new HashMap();

    /** A map holding the registered type handlers by their names. */
    private Map typeNameMap = new HashMap();

    /**
   * Creates an ExpressionInterpreter instance according to the registered prototype. 
   * @return The created ExpressionInterpreter instance.
   */
    public static ExpressionInterpreter createInstance() {
        if (prototype == null) {
            String clazzName = System.getProperty(ATTRIBUTE_NAME);
            if (clazzName != null) {
                try {
                    return prototype = (ExpressionInterpreter) Class.forName(clazzName).newInstance();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException("Unable to create ExpressionInterpreter instance from class '" + clazzName + "': " + ex.getMessage());
                } catch (InstantiationException ex) {
                    throw new RuntimeException("Unable to create ExpressionInterpreter instance from class '" + clazzName + "': " + ex.getMessage());
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Unable to create ExpressionInterpreter instance from class '" + clazzName + "': " + ex.getMessage());
                } catch (ClassCastException ex) {
                    throw new RuntimeException("Unable to create ExpressionInterpreter instance from class '" + clazzName + "': " + ex.getMessage());
                }
            }
            return new ExpressionInterpreter();
        }
        return (ExpressionInterpreter) prototype.clone();
    }

    /**
   * Defines the expression interpreter prototype.
   * @param prototype The prototype to define.
   */
    public static void setPrototype(ExpressionInterpreter prototype) {
        ExpressionInterpreter.prototype = prototype;
    }

    /**
   * Constructs an ExpressionInterpreter instance with no specific attributes. 
   */
    public ExpressionInterpreter() {
        registerDefaultTypeHandlers();
    }

    /**
   * Registers an expression type
   * @param type The new expression type.
   */
    public void registerTypeHandler(ExpressionType type) {
        registerTypeHandler(type.getType(), type);
    }

    /**
   * Registers an expression type
   * @param typeClass The type class.
   * @param type The new expression type.
   */
    public void registerTypeHandler(Class typeClass, ExpressionType type) {
        if (typeHandlers.containsKey(typeClass)) {
            int numTypeHandlers = typeHandlerList.size();
            for (int i = 0; i < numTypeHandlers; i++) {
                ExpressionType et = (ExpressionType) typeHandlerList.get(i);
                Class etClass = et.getType();
                if (etClass.equals(typeClass)) {
                    typeHandlerList.set(i, type);
                    break;
                }
            }
        } else {
            typeHandlerList.add(type);
        }
        typeHandlers.put(typeClass, type);
        typeNameMap.put(typeClass.getName(), type);
        String alias = type.getAlias();
        if (alias != null) {
            typeNameMap.put(alias, type);
        }
    }

    /**
   * Unregisters an expression type
   * @param type The expression type to unregister.
   */
    public void unregisterTypeHandler(ExpressionType type) {
        unregisterTypeHandler(type.getType());
    }

    /**
   * Unregisters an expression type
   * @param typeClass The class of the expression type to unregister.
   */
    public void unregisterTypeHandler(Class typeClass) {
        typeHandlers.remove(typeClass);
        int numTypeHandlers = typeHandlerList.size();
        for (int i = 0; i < numTypeHandlers; i++) {
            ExpressionType et = (ExpressionType) typeHandlerList.get(i);
            Class etClass = et.getType();
            if (etClass.equals(typeClass)) {
                typeHandlerList.remove(i);
                Iterator iterNameMapEntries = typeNameMap.entrySet().iterator();
                while (iterNameMapEntries.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterNameMapEntries.next();
                    if (entry.getValue().equals(et)) {
                        iterNameMapEntries.remove();
                    }
                }
                break;
            }
        }
    }

    /**
   * Unregisters all expression types
   */
    public void unregisterAllTypeHandlers() {
        typeHandlers.clear();
        typeHandlerList.clear();
        typeNameMap.clear();
    }

    /**
   * Registers the default type handlers. This method is called 
   * by the constructor.
   */
    public void registerDefaultTypeHandlers() {
        registerTypeHandler(new DefaultType(this));
        registerTypeHandler(new NullValueType(this));
        registerTypeHandler(new BooleanType(this));
        registerTypeHandler(new ConditionResultHolderType(this));
        registerTypeHandler(new DateType(this));
        registerTypeHandler(new TimeType(this));
        registerTypeHandler(new TimestampType(this));
        registerTypeHandler(new DoubleType(this, Double.class, "double"));
        registerTypeHandler(new FloatType(this));
        registerTypeHandler(BigDecimal.class, new DoubleType(this, BigDecimal.class, "BigDecimal"));
        registerTypeHandler(new IntegerType(this, Integer.class, "int"));
        registerTypeHandler(SequenceIndex.class, new IntegerType(this, SequenceIndex.class, null));
        registerTypeHandler(new ShortType(this));
        registerTypeHandler(new ByteType(this));
        registerTypeHandler(new LongType(this));
        registerTypeHandler(new StringType(this));
        registerTypeHandler(new CharType(this));
        registerTypeHandler(new EnumType(this));
        registerTypeHandler(new TableType(this));
        registerTypeHandler(new RowType(this));
        registerTypeHandler(new CollectionType(this));
        registerTypeHandler(new MapType(this));
        registerTypeHandler(new MapEntryType(this));
        registerTypeHandler(new ResourceBundleType(this));
        registerTypeHandler(new ObjectArrayType(this));
        registerTypeHandler(new ShortArrayType(this));
        registerTypeHandler(new IntArrayType(this));
        registerTypeHandler(new LongArrayType(this));
        registerTypeHandler(new FloatArrayType(this));
        registerTypeHandler(new DoubleArrayType(this));
        registerTypeHandler(new CharArrayType(this));
        registerTypeHandler(new ByteArrayType(this));
    }

    /**
   * Retrieves the value for the given parameter name.
   * @param paramName The parameter name.
   * @param ctx The expression context.
   * @return The value that's found or null.
   * @exception ExpressionException If the parameter could not be
   *   successfuly evaluated.
   */
    public Object getParameterValue(String paramName, ExpressionContext ctx) throws ExpressionException {
        Object value = ctx.getProperty(paramName);
        if (value != null) {
            return value;
        }
        Object bean = ctx.getBean();
        if (bean == null) {
            return null;
        }
        if (paramName == null) {
            return null;
        }
        try {
            value = BeanUtils.getProperty(bean, paramName, true);
            ctx.setProperty(paramName, value);
        } catch (BeanException ex) {
            throw new ExpressionException("Property '" + paramName + "' could not be evaluated for given bean from class " + bean.getClass().getName() + ".", ex);
        }
        return value;
    }

    /**
   * Parses constant from given text.
   * @param constant The constant text.
   * @return The extracted text.
   * @throws ExpressionException In case a syntax error was detected. 
   */
    public String getConstantValue(String constant) throws ExpressionException {
        int length = constant.length();
        if (constant.charAt(0) == '\'') {
            if (constant.charAt(length - 1) != '\'') {
                throw new ExpressionException("Syntax error in constant definition at token '" + constant + "'.");
            }
            return constant.substring(1, length - 1);
        }
        if (constant.charAt(0) == '\"') {
            if (constant.charAt(length - 1) != '\"') {
                throw new ExpressionException("Syntax error in constant definition at token '" + constant + "'.");
            }
            return constant.substring(1, length - 1);
        }
        return constant;
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a String.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed boolean value.
   * @exception ExpressionException If the expression could not be
   *   successfuly resolved
   */
    public String evalString(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalString(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a String.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the 
   *   parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed boolean value.
   * @exception ExpressionException If the expression could not be
   *   successfuly resolved
   */
    public String evalString(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return "";
        }
        return evalString(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a String.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed boolean value.
   * @exception ExpressionException If the expression could not be
   *   successfuly resolved
   */
    public String evalString(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return "";
        }
        Object result = evalObject(text, ctx);
        if (result == null) {
            return "";
        }
        return result.toString();
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Boolean.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed boolean value.
   * @exception ExpressionException If the expression could not be
   *   successfuly resolved
   */
    public boolean evalBoolean(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalBoolean(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Boolean.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the 
   *   parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed boolean value.
   * @exception ExpressionException If the expression could not be
   *   successfuly resolved
   */
    public boolean evalBoolean(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return false;
        }
        return evalBoolean(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Boolean.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed boolean value.
   * @exception ExpressionException If the expression could not be
   *   successfuly resolved
   */
    public boolean evalBoolean(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return false;
        }
        Object result = evalObject(text, ctx);
        if (result == null) {
            return false;
        }
        if (result instanceof Boolean) {
            return ((Boolean) result).booleanValue();
        }
        return new Boolean(result.toString()).booleanValue();
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Date.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed Date value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Date evalDate(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalDate(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Date.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed Date.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Date evalDate(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return null;
        }
        return evalDate(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Date.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed Date.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Date evalDate(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return null;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Date) {
            return (Date) result;
        }
        try {
            return intDateFormat.parse(text);
        } catch (Exception ex) {
            throw new ExpressionException("Unable to evaluate code '" + text + "' as a date value.");
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Time.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed Time value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Time evalTime(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalTime(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Time.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed Time.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Time evalTime(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return null;
        }
        return evalTime(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Time.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed Time.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Time evalTime(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return null;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Time) {
            return (Time) result;
        } else if (result instanceof Date) {
            return new Time(((Date) result).getTime());
        } else {
            try {
                return new Time((intTimeFormat.parse(text)).getTime());
            } catch (Exception ex) {
                throw new ExpressionException("Unable to evaluate code '" + text + "' as a time value.");
            }
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Timestamp.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed Timestamp value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Timestamp evalTimestamp(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalTimestamp(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Time.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed Timestamp.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Timestamp evalTimestamp(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return null;
        }
        return evalTimestamp(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a Time.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed Timestamp.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Timestamp evalTimestamp(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return null;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Timestamp) {
            return (Timestamp) result;
        } else if (result instanceof Date) {
            return new Timestamp(((Date) result).getTime());
        } else {
            try {
                return new Timestamp((intTimestampFormat.parse(text)).getTime());
            } catch (Exception ex) {
                throw new ExpressionException("Unable to evaluate code '" + text + "' as a timestamp value.");
            }
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an integer.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed integer value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public int evalInt(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalInt(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an int.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed int.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public int evalInt(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return 0;
        }
        return evalInt(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an int.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed int.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public int evalInt(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return 0;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Number) {
            return ((Number) result).intValue();
        }
        try {
            if (result == null) {
                return 0;
            }
            return new Integer(result.toString()).intValue();
        } catch (Exception ex) {
            throw new ExpressionException("Unable to evaluate code '" + text + "' as a integer value.");
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a long.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed long value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public long evalLong(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalLong(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an long.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed long.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public long evalLong(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return 0l;
        }
        return evalLong(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an long.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed long.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public long evalLong(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return 0l;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        try {
            if (result == null) {
                return 0L;
            }
            return new Long(result.toString()).longValue();
        } catch (Exception ex) {
            throw new ExpressionException("Unable to evaluate code '" + text + "' as a long value.");
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a double.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed double value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public double evalDouble(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalDouble(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a double.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed double.
   * @exception ExpressionException If the expression could not besuccessfuly resolved
   */
    public double evalDouble(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return 0.0;
        }
        return evalDouble(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a double.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed double.
   * @exception ExpressionException If the expression could not besuccessfuly resolved
   */
    public double evalDouble(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return 0.0;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Number) {
            return ((Number) result).doubleValue();
        }
        try {
            if (result == null) {
                return 0.0;
            }
            return new Double(result.toString()).doubleValue();
        } catch (Exception ex) {
            throw new ExpressionException("Unable to evaluate code '" + text + "' as a double value.");
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a float.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed double value.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public float evalFloat(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalFloat(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a double.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed double.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public float evalFloat(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return 0.0f;
        }
        return evalFloat(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as a double.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed double.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public float evalFloat(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return 0.0f;
        }
        Object result = evalObject(text, ctx);
        if (result instanceof Number) {
            return ((Number) result).floatValue();
        }
        try {
            if (result == null) {
                return 0.0F;
            }
            return new Float(result.toString()).floatValue();
        } catch (Exception ex) {
            throw new ExpressionException("Unable to evaluate code '" + text + "' as a float value.");
        }
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an Object.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param formatHelper The format helper to use.
   * @return The computed Object.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Object evalObject(String text, Map params, FormatHelper formatHelper) throws ExpressionException {
        return evalObject(text, params, null, formatHelper);
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an Object.
   * @param text The text to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The computed Object.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Object evalObject(String text, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        if (text == null) {
            return null;
        }
        return evalObject(text, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates a text, which is likely to contain expressions as an Object.
   * @param text The text to evaluate.
   * @param ctx The expression context.
   * @return The computed Object.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Object evalObject(String text, ExpressionContext ctx) throws ExpressionException {
        if (text == null) {
            return null;
        }
        ExpressionTuple expression = parseExpr(text);
        if (expression == null) {
            return text;
        }
        return evalExpr(expression, ctx);
    }

    /**
   * Parses a text, which is likely to contain expressions.
   * @param expression The expression, to be parsed.
   * @return The produced expression.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public ExpressionTuple parseExpr(String expression) throws ExpressionException {
        return parseExpr(expression.toCharArray(), 0, expression.length() - 1);
    }

    /**
   * Parses a text, which is likely to contain expressions.
   * @param expression The expression, to be parsed.
   * @param offset The offset from which to parse the expression string. 
   * @param lastPos The last position to which to parse the expression string. 
   * @return The produced expression.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public ExpressionTuple parseExpr(char[] expression, int offset, int lastPos) throws ExpressionException {
        ExpressionTuple expressionTuple = scanExpression(expression, offset, lastPos);
        if (expressionTuple == null) {
            return null;
        }
        ExpressionTuple rootExpression = expressionTuple;
        int absolutePosition = expressionTuple.getAbsolutePosition();
        int length = expressionTuple.getLength();
        int endPos = absolutePosition + length - 1;
        while (absolutePosition <= endPos) {
            if (expressionTuple.getObject() == null) {
                absolutePosition = parseOperand(expressionTuple, PS_OBJECT, absolutePosition);
            } else {
                ExpressionTuple objectExpression = new ExpressionTuple(expressionTuple);
                objectExpression.setAbsolutePosition(absolutePosition);
                objectExpression.setRelativePosition(absolutePosition - expressionTuple.getAbsolutePosition());
                objectExpression.setLength(expressionTuple.getLength() - (absolutePosition - expressionTuple.getAbsolutePosition()));
                objectExpression.setObject(expressionTuple.getObject());
                objectExpression.setOperand(expressionTuple.getOperand());
                objectExpression.setOperation(expressionTuple.getOperation());
                expressionTuple.setObject(objectExpression);
                expressionTuple.setOperand(null);
                expressionTuple.setOperation(null);
            }
            if (absolutePosition > endPos) {
                return rootExpression;
            }
            absolutePosition = parseOperation(expressionTuple, absolutePosition);
            if (absolutePosition > endPos) {
                return rootExpression;
            }
            if (expressionTuple.getOperation() == null) {
                return rootExpression;
            }
            Operand object = expressionTuple.getObject();
            if (!object.isSimple()) {
                ExpressionTuple objectExpr = (ExpressionTuple) object;
                if ((objectExpr.getOperation() != null) && !objectExpr.isExplitelyEncapsulated() && (objectExpr.getOperation().getPriority() < expressionTuple.getOperation().getPriority())) {
                    ExpressionTuple operandExpr = new ExpressionTuple(rootExpression);
                    operandExpr.setAbsolutePosition(absolutePosition);
                    operandExpr.setRelativePosition(absolutePosition - rootExpression.getAbsolutePosition());
                    operandExpr.setLength(expressionTuple.getLength() - (absolutePosition - expressionTuple.getAbsolutePosition()));
                    operandExpr.setObject(objectExpr.getOperand());
                    operandExpr.setOperation(expressionTuple.getOperation());
                    expressionTuple.setOperand(operandExpr);
                    expressionTuple.setObject(objectExpr.getObject());
                    expressionTuple.setOperation(objectExpr.getOperation());
                    expressionTuple = operandExpr;
                }
            }
            absolutePosition = parseOperand(expressionTuple, PS_OPERAND, absolutePosition);
            if (absolutePosition > endPos) {
                return rootExpression;
            }
        }
        return rootExpression;
    }

    /**
   * Parses an operation
   * @param expressionTuple The expression tuple to parse.
   * @param position The current parse posittion.
   * @return The position, where to continue parsing.
   * @exception ExpressionException if the parsing failed.
   */
    private int parseOperation(ExpressionTuple expressionTuple, int position) throws ExpressionException {
        int operationStart = -1;
        char[] source = expressionTuple.getSource();
        int lastPos = expressionTuple.getAbsolutePosition() + expressionTuple.getLength() - 1;
        int i = position;
        for (; i <= lastPos; i++) {
            char c = source[i];
            switch(c) {
                case ' ':
                case '\n':
                case '\t':
                    if (operationStart != -1) {
                        expressionTuple.setOperation(checkOperation(new String(source, operationStart, i - operationStart)));
                        return i + 1;
                    }
                    break;
                case '$':
                    if (operationStart != -1) {
                        expressionTuple.setOperation(checkOperation(new String(source, operationStart, i - operationStart)));
                        return i;
                    }
                    return position;
                case '.':
                    expressionTuple.setOperation(new Operation(new String(source, i, 1), 10));
                    return i + 1;
                case '[':
                    expressionTuple.setOperation(new Operation("[]", 10));
                    return i + 1;
                case '/':
                case '*':
                case '%':
                    expressionTuple.setOperation(new Operation(new String(source, i, 1), 9));
                    return i + 1;
                case '+':
                case '-':
                    expressionTuple.setOperation(new Operation(new String(source, i, 1), 8));
                    return i + 1;
                case '!':
                case '>':
                case '<':
                    if (operationStart == -1) {
                        operationStart = i;
                    }
                    ++i;
                    char oc = c;
                    c = source[i];
                    if (c != '=') {
                        if (oc == '!') {
                            throw new ExpressionException("The operation '!" + c + "' is undefined at expression '" + new String(source, expressionTuple.getAbsolutePosition(), expressionTuple.getLength()) + "'");
                        }
                        expressionTuple.setOperation(new Operation(new String(source, operationStart, i - operationStart), 6));
                        return i;
                    }
                case '=':
                    if (operationStart == -1) {
                        operationStart = i;
                    }
                    if ((i - operationStart) >= 1) {
                        expressionTuple.setOperation(new Operation(new String(source, operationStart, i - operationStart + 1), 6));
                        return i + 1;
                    }
                    break;
                case '?':
                case ':':
                    expressionTuple.setOperation(new Operation(new String(source, i, 1), 5));
                    return i + 1;
                default:
                    if (operationStart == -1) {
                        operationStart = i;
                    }
                    break;
            }
        }
        if (operationStart != -1) {
            expressionTuple.setOperation(checkOperation(new String(source, operationStart, i - operationStart)));
            return i + 1;
        }
        return position;
    }

    /** The operation priority map. */
    private static final Map operationPriorityMap = new HashMap();

    static {
        operationPriorityMap.put(".", new Integer(10));
        operationPriorityMap.put("*", new Integer(9));
        operationPriorityMap.put("/", new Integer(9));
        operationPriorityMap.put("%", new Integer(9));
        operationPriorityMap.put("+", new Integer(8));
        operationPriorityMap.put("-", new Integer(8));
        operationPriorityMap.put("<", new Integer(6));
        operationPriorityMap.put(">", new Integer(6));
        operationPriorityMap.put(">=", new Integer(6));
        operationPriorityMap.put("<=", new Integer(6));
        operationPriorityMap.put("==", new Integer(6));
        operationPriorityMap.put("&&", new Integer(5));
        operationPriorityMap.put("and", new Integer(5));
        operationPriorityMap.put("||", new Integer(4));
        operationPriorityMap.put("or", new Integer(4));
    }

    /**
   * Chooses the priority of the given operation. 
   * @param name The operation name.
   * @return The operation with added priority.
   */
    private static final Operation checkOperation(String name) {
        Integer priority = (Integer) operationPriorityMap.get(name);
        int p = 7;
        if (priority != null) {
            p = priority.intValue();
        }
        return new Operation(name, p);
    }

    /**
   * Parses an operand
   * @param expressionTuple The expression tuple to parse.
   * @param parseState The current parse state.
   * @param position The current parse posittion.
   * @return The position, where to continue parsing.
   * @exception ExpressionException if the parsing failed.
   */
    private int parseOperand(ExpressionTuple expressionTuple, int parseState, int position) throws ExpressionException {
        Operation operation = expressionTuple.getOperation();
        char[] source = expressionTuple.getSource();
        int lastPos = expressionTuple.getAbsolutePosition() + expressionTuple.getLength() - 1;
        int i = position;
        if ((parseState == PS_OPERAND) && (operation.getName().charAt(0) == '[')) {
            int level = 0;
            int constantStart = -1;
            int constantEnd = -1;
            int valueStart = -1;
            int valueEnd = -1;
            char constantQuotationMark = 0;
            boolean parseExpr = false;
            for (; i <= lastPos; i++) {
                char c = source[i];
                switch(c) {
                    case '$':
                        if (i < lastPos) {
                            if (source[i + 1] == '{') {
                                ++level;
                                parseExpr = true;
                            }
                        }
                        break;
                    case '}':
                        --level;
                        break;
                    case '\'':
                    case '\"':
                        if ((level == 0) && (constantEnd < 0)) {
                            if (constantStart < 0) {
                                constantStart = i + 1;
                                ++i;
                                for (; i <= lastPos; i++) {
                                    char nc = source[i];
                                    if (nc == c) {
                                        constantQuotationMark = nc;
                                        constantEnd = i - 1;
                                        break;
                                    }
                                }
                            }
                        } else {
                            constantEnd = i;
                        }
                        break;
                    case ']':
                        if (level == 0) {
                            if (parseExpr) {
                                ExpressionTuple nested = parseExpr(source, position, i);
                                nested.setExplitelyEncapsulated(true);
                                expressionTuple.setOperand(nested);
                                return i + 1;
                            }
                            if (constantEnd >= 0) {
                                Operand operand = new ConstantOperand(source, constantStart, constantStart - position, constantEnd - constantStart + 1);
                                if (constantQuotationMark != 0) {
                                    operand.setTotalPosition(constantStart - 1);
                                    operand.setTotalLength(operand.getLength() + 2);
                                }
                                expressionTuple.setOperand(operand);
                                return i + 1;
                            }
                            if (valueEnd >= 0) {
                                if (Character.isDigit(source[valueStart])) {
                                    ConstantOperand constant = new ConstantOperand(expressionTuple);
                                    int valueLength = valueEnd - valueStart + 1;
                                    constant.setObject(parseNumber(new String(source, valueStart, valueLength)));
                                    constant.setAbsolutePosition(valueStart);
                                    constant.setRelativePosition(valueStart - position);
                                    constant.setLength(valueLength);
                                    expressionTuple.setOperand(constant);
                                    return i + 1;
                                }
                                VariableOperand variable = new VariableOperand(expressionTuple);
                                int valueLength = valueEnd - valueStart + 1;
                                variable.setVariable(new String(source, valueStart, valueLength));
                                variable.setAbsolutePosition(valueStart);
                                variable.setRelativePosition(valueStart - position);
                                variable.setLength(valueLength);
                                expressionTuple.setOperand(variable);
                                return i + 1;
                            }
                            Operand operand = new ConstantOperand(source, valueStart, valueStart - position, valueEnd - valueStart);
                            expressionTuple.setOperand(operand);
                            return i + 1;
                        }
                    default:
                        if (!Character.isWhitespace(c)) {
                            if (valueStart == -1) {
                                valueStart = i;
                                valueEnd = i;
                            } else {
                                valueEnd = i;
                            }
                        }
                        break;
                }
            }
            throw new ExpressionException("Missing closing bracket in  source '" + new String(source) + "'.");
        }
        boolean forceConstant = ((operation != null) && (operation.getName().charAt(0) == '.'));
        Operand operand = parseOperand(source, i, lastPos, forceConstant);
        if (operand == null) {
            return i;
        }
        if (parseState == PS_OBJECT) {
            expressionTuple.setObject(operand);
        } else {
            expressionTuple.setOperand(operand);
        }
        return operand.getTotalPosition() + operand.getTotalLength();
    }

    /**
   * Parses an operand
   * @param source The source code to parse.
   * @param position The current parse posittion.
   * @param lastPos The last valid parse posittion.
   * @param forceConstant In case the parsed item might be a variable name, forces the treatment as a constant.
   * @return The position, where to continue parsing.
   * @exception ExpressionException if the parsing failed.
   */
    public Operand parseOperand(char[] source, int position, int lastPos, boolean forceConstant) throws ExpressionException {
        int operandStart = -1;
        int i = position;
        ExpressionType castType = null;
        boolean signedNegative = false;
        boolean numericConstant = false;
        parseLoop: for (; i <= lastPos; i++) {
            char c = source[i];
            switch(c) {
                case '(':
                    {
                        int castTypeStart = i + 1;
                        while (i <= lastPos) {
                            char nc = source[i];
                            if (nc == ')') {
                                String typeName = new String(source, castTypeStart, i - castTypeStart);
                                castType = getTypeByName(typeName);
                                continue parseLoop;
                            }
                            ++i;
                        }
                        throw new ExpressionException("Detected unterminated type cast position " + castTypeStart + " in  source '" + new String(source) + "'.");
                    }
                case '$':
                    {
                        if (i < lastPos) {
                            if (source[i + 1] == '{') {
                                ExpressionTuple nested = parseExpr(source, i, lastPos);
                                nested.setExplitelyEncapsulated(true);
                                return nested;
                            }
                            throw new ExpressionException("Detected problem at position " + i + " in  source '" + new String(source) + "'.");
                        }
                    }
                case '-':
                    if (operandStart < 0) {
                        if (!signedNegative) {
                            signedNegative = true;
                            continue;
                        }
                    }
                case '+':
                case '*':
                case '/':
                case '<':
                case '>':
                case '=':
                case '!':
                case '[':
                case '.':
                case '?':
                case ':':
                    if (operandStart != -1) {
                        if ((c == '.') && numericConstant) {
                            continue;
                        }
                    }
                    i -= 1;
                    break parseLoop;
                case ' ':
                case '\r':
                case '\n':
                case '\t':
                case ',':
                    if (operandStart != -1) {
                        --i;
                        break parseLoop;
                    }
                    break;
                case '\"':
                case '\'':
                    if (operandStart != -1) {
                        throw new ExpressionException("Syntax error in expression: Syntax error at constant at position " + i + " while parsing expression '" + new String(source) + "'.");
                    }
                    ++i;
                    operandStart = i;
                    while (i <= lastPos) {
                        char nc = source[i];
                        if (nc == c) {
                            ConstantOperand operand = new ConstantOperand(source, operandStart, operandStart - position, i - operandStart);
                            operand.setForcedType(castType);
                            operand.setObject(new String(source, operandStart, i - operandStart));
                            operand.setTotalPosition(operandStart - 1);
                            operand.setLength(i - operandStart);
                            operand.setTotalLength(operand.getLength() + 2);
                            return operand;
                        }
                        ++i;
                    }
                    throw new ExpressionException("Syntax error in expression:Unexpected end of expression at position " + i + " while parsing expression '" + new String(source) + "'.");
                default:
                    if (operandStart == -1) {
                        numericConstant = Character.isDigit(source[i]);
                        if ((i > position) && numericConstant && (source[i - 1] == '-')) {
                            operandStart = i - 1;
                        } else {
                            operandStart = i;
                        }
                    }
                    break;
            }
        }
        if (operandStart == -1) {
            return null;
        }
        if (i > lastPos) {
            i = lastPos;
        }
        int operandLength = i - operandStart + 1;
        String operand = new String(source, operandStart, operandLength);
        if (numericConstant) {
            ConstantOperand constant = new ConstantOperand(source, operandStart, operandStart - position, operandLength);
            Object constantValue = parseNumber(operand);
            if (castType != null) {
                constantValue = castType.typeCast(constantValue);
            }
            constant.setObject(constantValue);
            constant.setLength(operandLength);
            return constant;
        }
        if ("true".equals(operand) || "false".equals(operand)) {
            ConstantOperand constant = new ConstantOperand(source, operandStart, operandStart - position, operandLength);
            constant.setObject(new Boolean(operand));
            constant.setLength(operandLength);
            return constant;
        }
        if (forceConstant) {
            ConstantOperand constant = new ConstantOperand(source, operandStart, operandStart - position, operandLength);
            constant.setObject(operand);
            constant.setLength(operandLength);
            return constant;
        }
        VariableOperand variable = new VariableOperand(source, operandStart, operandStart - position, operandLength);
        variable.setForcedType(castType);
        variable.setVariable(operand);
        variable.setLength(operandLength);
        variable.setNegative(signedNegative);
        return variable;
    }

    /**
   * Parses a number from the given string.
   * @param text The text to parse.
   * @return The parsed number
   * @exception NumberFormatException In case the parsing failed.
   */
    private Number parseNumber(String text) throws NumberFormatException {
        Number number = null;
        if (text.indexOf('.') > -1) {
            number = new Double(text);
        } else {
            if (text.startsWith("0x")) {
                number = new Long(Long.parseLong(text.substring(2), 16));
            } else {
                number = new Long(text);
            }
        }
        return number;
    }

    /**
   * Scans the given text for expressions. 
   * @param expression The text to scan for expressions.
   * @param offset The search offset.
   * @param lastPos The last position until wich to scan for expressions.
   * @return ExpressionTuple The detected expression or null if no expression was detected.
   * @exception ExpressionException if the parsing failed.
   */
    private ExpressionTuple scanExpression(char[] expression, int offset, int lastPos) throws ExpressionException {
        int length = expression.length;
        int parenthesisCount = 0;
        int exprStart = -1;
        for (int i = offset; i <= lastPos; i++) {
            char c = expression[i];
            if (c == '$') {
                if (i < length - 1) {
                    if (expression[i + 1] == '{') {
                        if (exprStart < 0) {
                            exprStart = i + 2;
                        }
                        ++parenthesisCount;
                    }
                }
            } else if (c == '}') {
                --parenthesisCount;
                if (parenthesisCount == 0) {
                    ExpressionTuple tuple = new ExpressionTuple(expression, exprStart, exprStart - offset, i - exprStart);
                    tuple.setTotalPosition(exprStart - 2);
                    tuple.setTotalLength(tuple.getLength() + 3);
                    return tuple;
                }
                if (parenthesisCount < 0) {
                    parenthesisCount = 0;
                }
            }
        }
        if (parenthesisCount > 0) {
            throw new ExpressionException("Expression was not terminated correctly in text '" + new String(expression, offset, expression.length - offset) + "'.");
        }
        return null;
    }

    /**
   * Evaluates an expression as a String value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @return The produced text.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public String evalStringOperand(Operand operand, ExpressionContext ctx) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return null;
        }
        FormatHelper formatHelper = ctx.getFormatHelper();
        if (result instanceof Date) {
            if (result instanceof Time) {
                return formatHelper.getTimeFormat().format(result);
            } else if (result instanceof Timestamp) {
                return formatHelper.getTimestampFormat().format(result);
            }
            return formatHelper.getDateFormat().format(result);
        } else if (result instanceof Double || result instanceof BigDecimal || result instanceof Float) {
            double val = ((Number) result).doubleValue();
            if (Double.isInfinite(val) || Double.isNaN(val)) {
                return "-";
            }
            return formatHelper.getDoubleFormat().format(result);
        } else if (result instanceof Number) {
            return formatHelper.getIntegerFormat().format(result);
        }
        return result.toString();
    }

    /**
   * Evaluates an expression as a char value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public char evalCharOperand(Operand operand, ExpressionContext ctx) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0;
        }
        if (result instanceof Character) {
            return ((Character) result).charValue();
        }
        String value = result.toString();
        if (value.length() > 0) {
            return value.charAt(0);
        }
        return 0;
    }

    /**
   * Evaluates an expression as a boolean value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public boolean evalBooleanOperand(Operand operand, ExpressionContext ctx) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return false;
        }
        if (result instanceof Boolean) {
            return ((Boolean) result).booleanValue();
        }
        return new Boolean(result.toString()).booleanValue();
    }

    /**
   * Evaluates an expression as a byte value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @return The produced result.
   * @exception ExpressionException If the expression could not besuccessfuly resolved
   */
    public byte evalByteOperand(Operand operand, ExpressionContext ctx) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0;
        }
        if (result instanceof Number) {
            return ((Number) result).byteValue();
        }
        String value = result.toString();
        try {
            if (value.startsWith("0x")) {
                return Byte.parseByte(value.substring(2), 16);
            }
            return Byte.parseByte(value);
        } catch (NumberFormatException ex) {
            throw new OperandException("Detected invalid byte expression :'" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as an integer value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public int evalIntOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0;
        }
        if (result instanceof Number) {
            return ((Number) result).intValue();
        }
        if (!useFormat) {
            try {
                String value = result.toString();
                if (value.startsWith("0x")) {
                    return Integer.parseInt(value.substring(2), 16);
                }
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                throw new OperandException("Detected invalid integer expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
            }
        }
        try {
            return ctx.getFormatHelper().getIntegerFormat().parse(result.toString()).intValue();
        } catch (ParseException pex) {
            throw new OperandException("Detected invalid integer expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as a short value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not besuccessfuly resolved
   */
    public short evalShortOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0;
        }
        if (result instanceof Number) {
            return ((Number) result).shortValue();
        }
        if (!useFormat) {
            try {
                String value = result.toString();
                if (value.startsWith("0x")) {
                    return Short.parseShort(value.substring(2), 16);
                }
                return Short.parseShort(value);
            } catch (NumberFormatException ex) {
                throw new OperandException("Detected invalid short expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
            }
        }
        try {
            return ctx.getFormatHelper().getIntegerFormat().parse(result.toString()).shortValue();
        } catch (ParseException pex) {
            throw new OperandException("Detected invalid short expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as a long value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public long evalLongOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0l;
        }
        if (result instanceof Number) {
            return ((Number) result).longValue();
        }
        if (!useFormat) {
            try {
                String value = result.toString();
                if (value.startsWith("0x")) {
                    return Long.parseLong(value.substring(2), 16);
                }
                return Long.parseLong(value);
            } catch (NumberFormatException ex) {
                throw new OperandException("Detected invalid long expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
            }
        }
        try {
            return ctx.getFormatHelper().getIntegerFormat().parse(result.toString()).longValue();
        } catch (ParseException pex) {
            throw new OperandException("Detected invalid long expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as a double value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public double evalDoubleOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0.0;
        }
        if (result instanceof Number) {
            return ((Number) result).doubleValue();
        }
        if (!useFormat) {
            try {
                return Double.parseDouble(result.toString());
            } catch (NumberFormatException ex) {
                throw new OperandException("Detected invalid double expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
            }
        }
        try {
            return ctx.getFormatHelper().getDoubleFormat().parse(result.toString()).doubleValue();
        } catch (ParseException pex) {
            throw new ExpressionException("Detected invalid double expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as a float value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public float evalFloatOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            return 0.0f;
        }
        if (result instanceof Number) {
            return ((Number) result).floatValue();
        }
        if (!useFormat) {
            try {
                return Float.parseFloat(result.toString());
            } catch (NumberFormatException ex) {
                throw new OperandException("Detected invalid float expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
            }
        }
        try {
            return ctx.getFormatHelper().getDoubleFormat().parse(result.toString()).floatValue();
        } catch (ParseException pex) {
            throw new ExpressionException("Detected invalid float expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as a Date value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Date evalDateOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            throw new OperandException("Date expression '" + operand.getCode() + "' resolved to null!");
        }
        if (result instanceof Date) {
            return (Date) result;
        }
        String strValue = result.toString();
        try {
            if (useFormat) {
                return ctx.getFormatHelper().getDateFormat().parse(strValue);
            }
            return intDateFormat.parse(strValue);
        } catch (ParseException ex) {
            if (!useFormat) {
                try {
                    return intDateFormat.parse(strValue);
                } catch (ParseException pex) {
                }
            }
            throw new OperandException("Detected invalid Date expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression as a Time value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Time evalTimeOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            throw new OperandException("Time expression '" + operand.getCode() + "' resolved to null!");
        }
        if (result instanceof Time) {
            return (Time) result;
        }
        String strValue = result.toString();
        try {
            if (useFormat) {
                return new Time(ctx.getFormatHelper().getTimeFormat().parse(strValue).getTime());
            }
            return new Time(intTimeFormat.parse(strValue).getTime());
        } catch (ParseException ex) {
            if (!useFormat) {
                try {
                    return new Time(intTimeFormat.parse(strValue).getTime());
                } catch (ParseException pex) {
                }
            }
            throw new OperandException("Detected invalid Time expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an expression a an Timestamp value.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @param useFormat Tells whether the user format from the format helper shall be considered or not.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Timestamp evalTimestampOperand(Operand operand, ExpressionContext ctx, boolean useFormat) throws ExpressionException {
        Object result = evalOperand(operand, ctx);
        if (result == null) {
            throw new OperandException("Timestamp expression '" + operand.getCode() + "' resolved to null!");
        }
        if (result instanceof Timestamp) {
            return (Timestamp) result;
        }
        String strValue = result.toString();
        try {
            if (useFormat) {
                return new Timestamp(ctx.getFormatHelper().getTimestampFormat().parse(strValue).getTime());
            }
            return new Timestamp(intTimestampFormat.parse(strValue).getTime());
        } catch (ParseException ex) {
            if (!useFormat) {
                try {
                    return new Timestamp(intTimestampFormat.parse(strValue).getTime());
                } catch (ParseException pex) {
                }
            }
            throw new OperandException("Detected invalid Timestamp expression: '" + result.toString() + "' as result of expression '" + operand.getCode() + "'.");
        }
    }

    /**
   * Evaluates an operand.
   * @param operand The operand to evaluate.
   * @param ctx The expression context.
   * @return The produced text.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Object evalOperand(Operand operand, ExpressionContext ctx) throws ExpressionException {
        ExpressionType forcedType = operand.getForcedType();
        if (!operand.isSimple()) {
            Object result = evalExpr((ExpressionTuple) operand, ctx);
            if (forcedType != null) {
                return forcedType.typeCast(result);
            }
            return result;
        }
        if (operand instanceof VariableOperand) {
            VariableOperand variable = (VariableOperand) operand;
            String variableName = variable.getVariable();
            Object objectValue = getParameterValue(variableName, ctx);
            if (variable.isNegative()) {
                if (objectValue instanceof Number) {
                    BaseNumberType typeHandler = (BaseNumberType) getTypeHandler(objectValue);
                    objectValue = typeHandler.getNegative((Number) objectValue);
                }
            }
            variable.setObject(objectValue);
            if ((objectValue != null) && (objectValue instanceof String)) {
                String value = (String) objectValue;
                if ((value.length() > 0) && (value.charAt(0) == '$')) {
                    objectValue = evalObject(value, ctx);
                    if (forcedType != null) {
                        objectValue = forcedType.typeCast(objectValue);
                    }
                    return objectValue;
                }
            }
            objectValue = variable.getValue();
            if (forcedType != null) {
                objectValue = forcedType.typeCast(objectValue);
            }
            return objectValue;
        }
        ConstantOperand constant = (ConstantOperand) operand;
        return constant.getObject();
    }

    /**
   * Evaluates an expression.
   * @param expr The expression to evaluate.
   * @param params The parameters.
   * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
   * @param formatHelper The format helper to use.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Object evalExpr(ExpressionTuple expr, Map params, Object bean, FormatHelper formatHelper) throws ExpressionException {
        return evalExpr(expr, new ExpressionContext(params, bean, formatHelper));
    }

    /**
   * Evaluates an expression.
   * @param expr The expression to evaluate.
   * @param ctx The expression context.
   * @return The produced result.
   * @exception ExpressionException If the expression could not be successfuly resolved
   */
    public Object evalExpr(ExpressionTuple expr, ExpressionContext ctx) throws ExpressionException {
        Operand object = expr.getObject();
        String paramName = null;
        if (object.isSimple()) {
            if (object instanceof VariableOperand) {
                VariableOperand variable = (VariableOperand) object;
                paramName = variable.getVariable();
                Object objectValue = getParameterValue(paramName, ctx);
                if (variable.isNegative()) {
                    if (objectValue instanceof Number) {
                        BaseNumberType typeHandler = (BaseNumberType) getTypeHandler(objectValue);
                        objectValue = typeHandler.getNegative((Number) objectValue);
                    }
                }
                variable.setObject(objectValue);
                if ((objectValue != null) && (objectValue instanceof String)) {
                    String value = (String) objectValue;
                    if ((value.length() > 0) && (value.charAt(0) == '$')) {
                        objectValue = evalObject(value, ctx);
                        variable.setObject(objectValue);
                    }
                }
            }
        } else {
            ExpressionTuple objectExpression = (ExpressionTuple) expr.getObject();
            objectExpression.setResult(evalExpr(objectExpression, ctx));
        }
        Operation operation = expr.getOperation();
        Operand operand = expr.getOperand();
        if ((operation == null) && (operand == null)) {
            expr.setResult(object.getValue());
            return expr.getValue();
        }
        ExpressionType handler = getTypeHandler(object.getValue());
        return handler.evalTuple(expr, ctx);
    }

    /**
   * Gets all currently registered type handlers.
   * @return The currently registerd type handlers.
   */
    protected Collection getTypeHandlers() {
        return typeHandlerList;
    }

    /**
   * Gets the handler for the object's type
   * @param object The parameter object.
   * @return The type handler for the object.
   */
    protected ExpressionType getTypeHandler(Object object) {
        if (object == null) {
            return (ExpressionType) typeHandlers.get(NULL_VALUE_TYPE);
        }
        ExpressionType handler = (ExpressionType) typeHandlers.get(object.getClass());
        if (handler != null) {
            return handler;
        }
        int numTypeHandlers = typeHandlerList.size();
        for (int i = 0; i < numTypeHandlers; i++) {
            ExpressionType type = (ExpressionType) typeHandlerList.get(i);
            Class typeClass = type.getType();
            if (typeClass.equals(DEFAULT_TYPE)) {
                continue;
            }
            if (type.getType().isInstance(object)) {
                return type;
            }
        }
        return (ExpressionType) typeHandlers.get(DEFAULT_TYPE);
    }

    /**
   * Gets the handler for the object's type by its name.
   * @param name The type name.
   * @return The type handler for the type name.
   * @exception ExpressionException In case no handler was found for the given type name.
   */
    protected ExpressionType getTypeByName(String name) throws ExpressionException {
        if (name == null) {
            throw new ExpressionException("No handler declared for null type!");
        }
        ExpressionType handler = (ExpressionType) typeNameMap.get(name);
        if (handler != null) {
            return handler;
        }
        throw new ExpressionException("No handler declared for type '" + name + "'!");
    }

    /**
   * Provides a command line tool for testing purposes.
   * @param args The command line attributes. 
   */
    public static void main(String[] args) {
        Map properties = new HashMap();
        if (args.length != 0) {
            try {
                Properties props = new Properties();
                props.load(new FileInputStream(args[0]));
                properties.putAll(props);
            } catch (IOException ex) {
                System.err.println("Unable to load properties from file '" + properties + "'.");
                System.exit(1);
            }
        }
        ExpressionInterpreter exi = ExpressionInterpreter.createInstance();
        FormatHelper formatHelper = FormatHelper.createInstance();
        BufferedReader cmdReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("exi>");
                String expr = cmdReader.readLine().trim();
                try {
                    if (expr.equalsIgnoreCase("exit")) {
                        break;
                    }
                    System.out.println(exi.evalObject(expr, properties, formatHelper));
                } catch (ExpressionException ex) {
                    System.err.println("Evaluation failed: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
   * @see java.lang.Object#clone()
   */
    public Object clone() {
        ExpressionInterpreter clone = null;
        try {
            clone = (ExpressionInterpreter) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("Failed to clone ExpressionInterpreter instance: " + ex.getMessage(), ex);
        }
        clone.typeHandlerList = (List) ((ArrayList) typeHandlerList).clone();
        clone.typeHandlers = (Map) ((HashMap) typeHandlers).clone();
        return clone;
    }
}
