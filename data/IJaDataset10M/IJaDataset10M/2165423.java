package org.gaea.commandline.command;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import org.gaea.commandline.StringPadder;
import org.gaea.common.exception.GaeaException;
import org.gaea.common.supportedclass.ISupported;
import org.gaea.common.supportedclass.Supported;
import org.gaea.common.util.ReflectionHelper;
import org.gaea.lib.selector.Translate;

/**
 * Command "select"
 * 
 * Executes the query on the server and displays it.
 * 
 * @author jsgoupil
 */
public class CommandSelect implements ICommand {

    private static final String SERIAL_VERSION_UID_STR = "serialVersionUID";

    /** Variable that hides/shows the serial version UID field */
    private static final boolean SHOW_SERIAL_VERSION_UID_FIELD = false;

    /** Variable that hides/shows the jdo fields */
    private static final boolean SHOW_JDO_FIELDS = false;

    private Translate _translate;

    private CommandBase _parent;

    /**
	 * Constructor
	 * 
	 * @param parent
	 */
    public CommandSelect(CommandBase parent) {
        _parent = parent;
        _translate = new Translate(_parent.getParent().getConnector());
    }

    public void execute(String args) {
        try {
            Date d1 = Calendar.getInstance().getTime();
            Collection result = _translate.runQuery("select " + args);
            Date d2 = Calendar.getInstance().getTime();
            displayResult(result, d2.getTime() - d1.getTime());
        } catch (GaeaException ex) {
            _parent.getParent().displayError(ex);
        }
    }

    public String getCommand() {
        return "select";
    }

    public String getExplication() {
        return "Executes the select on the database.";
    }

    /**
	 * Displays the result depending on the <code>collection</code>. It will
	 * format automatically all the table to make it the most beautiful.
	 * 
	 * @param collection
	 * @param milliseconds
	 *            Number of milliseconds ellapsed to make the request
	 */
    private void displayResult(Collection<?> collection, long milliseconds) {
        Iterator it;
        it = collection.iterator();
        int numberOfRows = 0;
        if (it.hasNext()) {
            Object obj = it.next();
            Field[] fields = getFields(obj.getClass());
            Vector<Integer> columnSize = new Vector<Integer>(fields.length);
            Vector<Field> columnHeader = new Vector<Field>(fields.length);
            for (Field field : fields) {
                if (isUsefulField(field)) {
                    columnHeader.add(field);
                    columnSize.add(field.getName().length());
                }
            }
            Vector<Vector<String>> data = new Vector<Vector<String>>(collection.size());
            do {
                Vector<String> columnValue = new Vector<String>(columnHeader.size());
                int i = 0;
                for (Field field : columnHeader) {
                    String value = "";
                    value = getFieldValue(field, obj).toString();
                    columnValue.add(value);
                    int length = value.length();
                    if (length > columnSize.get(i)) {
                        columnSize.set(i, length);
                    }
                    i++;
                }
                data.add(columnValue);
                if (it.hasNext()) {
                    obj = it.next();
                } else {
                    break;
                }
            } while (true);
            String dashLine = "+-";
            String dash;
            boolean first = true;
            for (Integer size : columnSize) {
                if (!first) {
                    dashLine += "-+-";
                } else {
                    first = false;
                }
                dash = "";
                dash = StringPadder.rightPad("", "-", size);
                dashLine += dash;
            }
            dashLine += "-+";
            Integer[] sizes = columnSize.toArray(new Integer[0]);
            System.out.println(dashLine);
            int i = 0;
            first = true;
            for (Field field : columnHeader) {
                if (!first) {
                    System.out.print(" ");
                } else {
                    first = false;
                }
                System.out.print("| ");
                String columnName = StringPadder.rightPad(field.getName(), " ", sizes[i]);
                i++;
                System.out.print(columnName);
            }
            System.out.println(" |");
            System.out.println(dashLine);
            for (Vector<String> line : data) {
                i = 0;
                first = true;
                for (String value : line) {
                    if (!first) {
                        System.out.print(" ");
                    } else {
                        first = false;
                    }
                    System.out.print("| ");
                    value = StringPadder.rightPad(value, " ", sizes[i]);
                    i++;
                    System.out.print(value);
                }
                System.out.println(" |");
            }
            System.out.println(dashLine);
            numberOfRows = data.size();
        }
        String display = numberOfRows + " row";
        if (numberOfRows > 1) {
            display += "s";
        }
        display += " in set(%.3f sec)\n";
        System.out.printf(display, (float) milliseconds / 1000);
    }

    /**
	 * Checks if the specified field is useful, i.e. if the field is use made
	 * and not added by jdo persistance tools.
	 * 
	 * @param field
	 *            Field to be checked.
	 * @return False if the field has been added by JDO persistance tools, true
	 *         otherwise.
	 */
    public static boolean isUsefulField(Field field) {
        String fieldName = field.getName();
        if (SHOW_JDO_FIELDS && fieldName.startsWith("jdo")) {
            return true;
        } else if (SHOW_SERIAL_VERSION_UID_FIELD && fieldName.equals(SERIAL_VERSION_UID_STR)) {
            return true;
        } else if (field.isSynthetic()) {
            return false;
        } else if (fieldName.startsWith("jdo") || fieldName.startsWith("class$") || fieldName.equals(SERIAL_VERSION_UID_STR)) {
            return false;
        }
        return true;
    }

    /**
	 * Checks if the field is primitive or is a string.
	 * 
	 * @param clazz
	 *            Field to be checked
	 * @return True if the field is primitive or a string, false if it is null
	 *         or otherwise.
	 */
    public static boolean isClassFakePrimitive(Class clazz) {
        if (clazz == null) {
            return false;
        }
        boolean isBoolean = clazz == Boolean.class;
        boolean isByte = clazz == Byte.class;
        boolean isCharacter = clazz == Character.class;
        boolean isDouble = clazz == Double.class;
        boolean isFloat = clazz == Float.class;
        boolean isInteger = clazz == Integer.class;
        boolean isLong = clazz == Long.class;
        boolean isShort = clazz == Short.class;
        if (isBoolean || isByte || isCharacter || isDouble || isFloat || isInteger || isLong || isShort) {
            return true;
        }
        return false;
    }

    /**
	 * Returns an array of <code>Field</code> objects reflecting all the
	 * fields declared in the class represented in this <code>Object</code>.
	 * 
	 * @param clazz
	 *            Class which fields will be fetched.
	 * @return Array of <code>Field</code> representing all fields.
	 */
    public static Field[] getFields(Class clazz) {
        if (clazz == null) {
            return null;
        }
        Vector<Field> returnFields = new Vector();
        if (isClassFakePrimitive(clazz)) {
            try {
                Field valueField = clazz.getDeclaredField("value");
                returnFields.add(valueField);
                return returnFields.toArray(new Field[] {});
            } catch (Exception e) {
            }
        }
        return ReflectionHelper.getFields(clazz);
    }

    /**
	 * Returns the value of the field, on the specified object (if it is not
	 * primitive, it returns a string). The value is automatically wrapped in an
	 * object if it has a primitive type.
	 * 
	 * @param field
	 *            Field to get the value of.
	 * @param obj
	 *            object from which the represented field's value is to be
	 *            extracted
	 * @return Value of the field if it is primitive or a string, error message
	 *         if there has been an error or Object toString value if it's an
	 *         object.
	 */
    public Object getFieldValue(Field field, Object obj) {
        if (field == null) {
            return new String("");
        }
        if (isList(field.getType())) {
            return new String("List: " + field.toString());
        }
        try {
            Object fieldValue = _parent.getParent().getConnector().getField(obj, field);
            if (fieldValue == null) {
                return "NULL";
            }
            ISupported supportedClass = Supported.getInstance().getSupportedClassByObject(fieldValue);
            if (supportedClass != null) {
                return supportedClass.toString();
            } else if (isSimpleType(field.getType())) {
                return fieldValue;
            } else {
                return new String("Object: " + fieldValue.toString());
            }
        } catch (GaeaException e) {
            return new String("Error");
        }
    }

    /**
	 * Checks if the clazz is a list (collection).
	 * 
	 * @param clazz
	 *            Clazz to be checked
	 * @return True if the field is a list, false if it is null or otherwise.
	 */
    public static boolean isList(Class clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isArray()) {
            return true;
        }
        if (isClassCollection(clazz)) {
            return true;
        }
        if (isClassDictionary(clazz)) {
            return true;
        }
        return false;
    }

    /**
	 * Checks if the specified class inherits from Dictionary.
	 * 
	 * @param classToCheck
	 *            Class to check.
	 * @return True if the class is a dictionary.
	 */
    public static boolean isClassDictionary(Class classToCheck) {
        try {
            classToCheck.asSubclass(java.util.Dictionary.class);
            return true;
        } catch (ClassCastException exc) {
        }
        return false;
    }

    /**
	 * Checks if the specified class inherits from Collection.
	 * 
	 * @param classToCheck
	 *            Class to check.
	 * @return True if the class is a collection.
	 */
    public static boolean isClassCollection(Class classToCheck) {
        try {
            classToCheck.asSubclass(java.util.Collection.class);
            return true;
        } catch (ClassCastException exc) {
        }
        return false;
    }

    /**
	 * Checks if the type of the class is supported.
	 * 
	 * @param type
	 *            Class to be checked
	 * @return True if the field is primitive or a string, false if it is null
	 *         or otherwise.
	 */
    public static boolean isSimpleType(Class type) {
        if (type == null) {
            return false;
        }
        boolean isReallyPrimitive = type.isPrimitive();
        boolean isFakePrimitive = isClassFakePrimitive(type);
        if (isReallyPrimitive || isFakePrimitive) {
            return true;
        }
        return false;
    }
}
