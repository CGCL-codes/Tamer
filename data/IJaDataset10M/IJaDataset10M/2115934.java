package common;

import common.log.Log;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
   This is an abstract base class that supports a reflection-based interface to
   command line argument processing.  Command line arguments are processed
   by the {@link #process(String[])} method.  The call supports a re-mapping
   of arguments.  Arguments are broken into a prefix and value, separated by
   "=".  The prefix is mapped to a method name.  The methods are invoked with
   or without a single String argument.
*/
public abstract class CommandLineProcessor {

    /** Argument value of true. */
    public static final String ARGV_TRUE = "true";

    /** Argument value of false. */
    public static final String ARGV_FALSE = "false";

    /** Argument value of false .*/
    public static final String DEFAULT_ARG_PREFIX = "--arg";

    private static final String DASH = "-";

    private static final char DASH_CHR = DASH.charAt(0);

    private static HashMap<String, String> ourArgMap = new HashMap<String, String>();

    protected static int ourStatus = 0;

    protected static String ourCurrentArg;

    private static CommandLineProcessor ourCurrent = null;

    /**
     Gets the current command line processor object.  This method is required
     by the {@link CommandLineProcessor} class.  It is called once at the
     beginning of {@link CommandLineProcessor#process(String[])} and at the
     top of the argument processing loop.

     @return the current CommandLineProcessor object or null to stop the
     argument processing loop.
  */
    public abstract CommandLineProcessor getCurrent();

    /**
     Processes command line arguments.
     <ol>
     <li>At the top of the argument loop, the {@link #getCurrent()} method
     is called to obtain a new reflection target.  If the returned value is
     null, the loop is exited.

     <li>The entire argument is optionally re-mapped by {@link
     #mapArg(String)}.  If the mapped argument is null, the next command
     line argument is processed.

     <li>Each argument is decomposed into a prefix and a value.

     <li>If the argument does not begin with "-", the prefix is set to
     {@link #DEFAULT_ARG_PREFIX} and the value is set with the argument.

     <li>If the argument does begin with "-", the argument value decomposed
     into a prefix and value using "=" as a separator.  If no separator is
     found, the prefix is set to the argument and the value is null.

     <li>The prefix is optionally re-mapped by {@link #mapArg(String)}.  If
     the mapped prefix is null, the next command line argument is processed.

     <li>The prefix is converted to a method name with the {@link
     #getMethodName(int,String)}.  If the method name is null, the next command
     line argument is processed.

     <li>If the method does not exist for the reflection object, an error
     message is issued and the next command line argument is processed.

     <li>If the value is null, the method is invoked on the reflection object
     with no arguments.  If the value is not null, the method is invoked on
     the reflection object with a single String argument, the value.

     <li>The next command line argument is processed.
     </ol>

     @param theArgs the command line arguments

     @return the current value of the class status flag.
  */
    public int process(String[] theArgs) {
        Log.main.setDefaultPrefixes();
        Log.main.removeLogMessageType(Log.VERBOSE);
        Log.main.removeLogMessageType(Log.DEBUG);
        ourStatus = 0;
        ourCurrent = this;
        for (String arg : theArgs) {
            ourCurrentArg = arg;
            ourCurrent = ourCurrent.getCurrent();
            if (ourCurrent == null) {
                break;
            }
            String prefix = mapArg(ourCurrentArg);
            if (prefix == null) {
                continue;
            }
            String value = null;
            int index = prefix.indexOf("=");
            if ((index > 0) && prefix.startsWith("-")) {
                value = prefix.substring(index + 1);
                prefix = prefix.substring(0, index);
            }
            Class<? extends CommandLineProcessor> target = ourCurrent.getClass();
            Class<?>[] methodArgs = new Class[0];
            Method method = null;
            if (!prefix.startsWith(DASH)) {
                value = prefix;
                prefix = DEFAULT_ARG_PREFIX;
            }
            prefix = mapArg(prefix);
            if (prefix == null) {
                continue;
            }
            int numDashes = 0;
            StringBuffer newArg = new StringBuffer();
            int numChrs = prefix.length();
            for (int chrIndex = 0; chrIndex < numChrs; chrIndex++) {
                char c = prefix.charAt(chrIndex);
                if (newArg.length() > 0) {
                    newArg.append(c);
                } else if (c != DASH_CHR) {
                    newArg.append(c);
                } else {
                    numDashes++;
                }
            }
            prefix = newArg.toString();
            if (value != null) {
                methodArgs = new Class[1];
                methodArgs[0] = String.class;
            }
            String methodName = getMethodName(numDashes, prefix);
            if (methodName != null) {
                try {
                    method = target.getMethod(methodName, methodArgs);
                } catch (Exception e) {
                    Log.main.println(Log.DEBUG, e.toString());
                    error("invalid argument: " + ourCurrentArg);
                }
            }
            if (method != null) {
                try {
                    Object[] args = null;
                    if (value == null) {
                        args = new Object[0];
                    } else {
                        args = new Object[1];
                        args[0] = value;
                    }
                    method.invoke(ourCurrent, args);
                } catch (Exception e) {
                    Log.main.println(Log.DEBUG, e.toString());
                    ourStatus++;
                }
            }
        }
        return ourStatus;
    }

    /**
     Writes an error message and increments the status value.

     @param theMessage the error message.
  */
    public int error(String theMessage) {
        ourStatus++;
        Log.main.println(Log.ERROR, theMessage);
        return ourStatus;
    }

    /**
     Writes a warning message and returns the status value.

     @param theMessage the warning message.
  */
    public int warning(String theMessage) {
        Log.main.println(Log.WARNING, theMessage);
        return ourStatus;
    }

    /**
     Default --verbose action: set the {@link Log#VERBOSE} message mask.
  */
    public void setVerbose() {
        setVerbose(ARGV_TRUE);
    }

    /**
     Default --verbose=value action: set/reset the {@link Log#VERBOSE}
     message mask.

     @param theFlag the argument value.  If the value is {@link #ARGV_TRUE},
     the {@link Log#VERBOSE} message mask is added to the main {@link Log}.
     If the value is {@link #ARGV_FALSE}, the {@link Log#VERBOSE} message
     mask is removed from the main {@link Log}.
  */
    public void setVerbose(String theFlag) {
        if (theFlag.equals(ARGV_TRUE)) {
            Log.main.addLogMessageType(Log.VERBOSE);
        } else if (theFlag.equals(ARGV_FALSE)) {
            Log.main.removeLogMessageType(Log.VERBOSE);
        }
    }

    /**
     Default --debug action: set the {@link Log#DEBUG} message mask.
  */
    public void setDebug() {
        setDebug(ARGV_TRUE);
    }

    /**
     Default --debug=value action: set/reset the {@link Log#DEBUG} message
     mask.

     @param theFlag the argument value.  If the value is {@link #ARGV_TRUE},
     the {@link Log#DEBUG} message mask is added to the main {@link Log}.
     If the value is {@link #ARGV_FALSE}, the {@link Log#DEBUG} message
     mask is removed from the main {@link Log}.
  */
    public void setDebug(String theFlag) {
        if (theFlag.equals(ARGV_TRUE)) {
            Log.main.addLogMessageType(Log.DEBUG);
        } else if (theFlag.equals(ARGV_FALSE)) {
            Log.main.removeLogMessageType(Log.DEBUG);
        }
    }

    /**
     Gets the method name from the specified argument prefix.  This method
     prepends the string "set" to the capitalized argument prefix.  For
     example, for prefixes "verbose", "Verbose" and "VERBOSE", this method
     returns "setVerbose".  Override this method to change the prefix to
     method name mapping.

     @param theNumDashes the number of dashes leading the argument
     prefix.  This value is not used in this implementation of the method.

     @param thePrefix the argument prefix, with any leading dashes removed.

     @return the method name.
  */
    public String getMethodName(int theNumDashes, String thePrefix) {
        return "set" + StringUtils.capitalize(thePrefix);
    }

    /**
     Maps an argument to a new value.

     @param theArg the argument value.

     @return the argument value, possibly mapped to a new string.
  */
    public String mapArg(String theArg) {
        String map = ourArgMap.get(theArg);
        return (map != null) ? map : theArg;
    }

    /**
     Sets an argument map.

     @param theArg the argument initial value.

     @param theMap the new argument value.
  */
    public void setArgMap(String theArg, String theMap) {
        ourArgMap.put(theArg, theMap);
    }
}
