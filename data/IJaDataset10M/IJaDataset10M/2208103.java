package pmp;

/**
 * Abstract superclass for all macros.
 * <p>
 * Title: PMP: Macroprocessor
 * </p>
 * <p>
 * Description: Java macroprocessor
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * 
 * @author Luděk Hlaváček
 * @version 1.0
 */
public abstract class AbstractMacro {

    /**
     * Instances of macros are instantiated using nullary constructors.
     */
    public AbstractMacro() {
        PARAMS_REQUIRED = 0;
        try {
            PARAMS_REQUIRED = getClass().getDeclaredField("PARAMS_REQUIRED").getInt(this);
        } catch (SecurityException ex) {
        } catch (NoSuchFieldException ex) {
        } catch (IllegalAccessException ex) {
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Main method of macro. This method is called when this macro is invoked.
     * All subclasses must imlement this method
     * 
     * @param mp
     *            reference to Macroprocessor
     * @param args
     *            parameters of macro. In args[0] is stored name of macro.
     *            Subclasses are free to modify this array.
     * @return result of the macro as String
     */
    public abstract String run(Macroprocessor mp, String[] args);

    /**
     * <p>
     * Number of required parameters. Before invoking run method, macroprocessor
     * checks the number of passed parameters and appends empty strings (<code>""</code>)
     * to the end of <code>args</code> array if necessary.
     * </p>
     * <p>
     * This method obtains number of parameters from field
     * <code>PARAMS_REQUIRED</code>.
     * </p>
     * <p>
     * Value is number of formal parameters of macro. I.e.: it doesn't include
     * name of macro, which is passed as parameter with index 0. So if your
     * macro requires two parameters, this field should be set to 2, like this:
     * </p>
     * <p>
     * <code>public static final int PARAMS_REQUIRED = 2;</code>
     * </p>
     * <p>
     * This field allows you to omit length-checking of <code>args</code>
     * array in {@link #run(Macroprocessor,String[])} method. (But you still
     * have to perform test for empty string if your macro requires non-empty
     * parameters.
     * </p>
     * 
     * @return number of parameters to be pased to this macro
     */
    public final int getRequiredParams() {
        return PARAMS_REQUIRED;
    }

    /**
     * Number of required parameters. This field is filled by constructor and
     * contains the same value as filed with the same name declared in subclass.
     */
    protected int PARAMS_REQUIRED;

    /**
     * <p>
     * Returns string representation of macro. This method is used by
     * <code>defn</code> macro. If you want reimplement this method, you must
     * make sure it will not return <code>null</code>.
     * </p>
     * <p>
     * For user-defined macros it should return definition and for builtin ones
     * empty string unless it has some meaning.
     * </p>
     * 
     * @see pmp.macro.Defn
     * @return empty String
     */
    @Override
    public String toString() {
        return "";
    }
}
