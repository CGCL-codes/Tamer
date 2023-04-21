package org.jtax.utils;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

/**
*
*  Simple command-line argument parser.  Currently supports arguments
*  in the following forms: "-xyz", "-f1 blah1", and "--foo2=blah2", and
*  returns a java.util.Hashtable with the results.  Standalone flag-type
*  parameters (of which "-xyz" would be a group) are simply given a Boolean
*  "true" in the resulting hashtable.<br>
*  In its simplest usage, your code would make the following call:<p>
*     Hashtable params = ArgParser.parse(args);<p>
*  ...where "args" is obviously the command-line argument string array.<br>
*  ArgParser also includes a 'main' method with debug output so you can
*  run it directly from the command line with various parameters to see
*  how it treats them.<br>
*  Additionally, ArgParser lets you allow short and long versions of
*  the same parameter by supplying replacement names in another hashtable.
*  See non-javadoc comments at the end of the source for examples.<p>
*
*  X This class currently ignores non-option arguments (because I didn't 
*  X need them when I wrote this), but it shouldn't be too difficult to 
*  X add.  For example, in the argument string "-xyz --foo=blah abcde"
*  X the "abcde" part would just get dropped because it's not associated
*  X with any parameters.<br>
*  
*  Non-option arguments are now handled, and "--" is supported as an
*  end-of-options marker.  See comments below for details.<p>
*  
*  ArgParser also leaves validation of the parameters up to you, since you
*  know better than I do what you want to accept as input.
*
*  Requires Java sdk 1.4, as it makes use of the String.split() method.
*  Could easily be re-worked to get around this, though.
*
*  ToDo:
*  - Consider re-working with Properties instead of Hashtable, so as to
*     allow calling app to set defaults.  Main drawback so far is that
*     Properties doesn't like having non-string keys or values, so 
*     setting flag params to Booleans, while possible, would break
*     the ability of the app to serialize the Properties object.  It
*     wouldn't be able to without custom code as it is with Hashtable,
*     but at least we're not breaking any assumptions that way.
*     Might just have to implement my own Hashtable-based solution
*     that can take a default set *shrug*.
*
*  @author Joel Fouse
*  @version 2.0
*
*/
public class ArgParser {

    private static boolean doDebug = false;

    public static void main(String[] args) {
        doDebug = true;
        debug("");
        debug("args length: " + args.length);
        Hashtable params = parse(args);
        String foo;
        debug("");
        debug("Current parameter set:");
        debug("--------");
        for (Enumeration e = params.keys(); e.hasMoreElements(); ) {
            foo = (String) e.nextElement();
            if (foo.equals("_UNASSIGNED")) {
                StringBuffer sb = new StringBuffer();
                String[] tmp = (String[]) params.get(foo);
                for (int i = 0; i < tmp.length; i++) {
                    sb.append(tmp[i]);
                    sb.append(i == tmp.length - 1 ? " " : ", ");
                }
                debug(foo + ": [ " + sb.toString() + " ]");
            } else {
                debug(foo + ": " + params.get(foo));
            }
        }
        debug("--------");
    }

    public static Hashtable parse(String[] args) {
        return parse(args, null, null);
    }

    public static Hashtable parse(String[] args, Hashtable argFilter) {
        return parse(args, argFilter, null);
    }

    public static Hashtable parseWithDefaults(String[] args, Hashtable defaults) {
        return parse(args, null, defaults);
    }

    public static Hashtable parseWithFilter(String[] args, Hashtable argFilter) {
        return parse(args, argFilter, null);
    }

    public static Hashtable parse(String[] args, Hashtable argFilter, Hashtable defaults) {
        Hashtable params = defaults != null ? defaults : new Hashtable();
        if (argFilter == null) {
            argFilter = new Hashtable();
        }
        int rejIndex = 0;
        Vector remaining = new Vector();
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--")) {
                for (++i; i < args.length; i++) {
                    remaining.add(args[i]);
                }
            } else if (args[i].startsWith("--") && (args[i].indexOf("=") > 2)) {
                String[] argtmp = args[i].substring(2).split("=");
                params.put(filter(argtmp[0], argFilter), argtmp[1]);
            } else if (args[i].startsWith("-")) {
                if (args.length > i + 1 && !args[i + 1].startsWith("-")) {
                    params.put(filter(args[i].substring(1), argFilter), args[++i]);
                } else {
                    String flagset = args[i].substring(1);
                    for (int j = 0; j < flagset.length(); j++) {
                        params.put(filter(flagset.substring(j, j + 1), argFilter), "true");
                    }
                }
            } else {
                remaining.add((String) args[i]);
            }
        }
        if (remaining.size() > 0) {
            String[] tmp = new String[remaining.size()];
            remaining.toArray(tmp);
            params.put("_UNASSIGNED", tmp);
        }
        return params;
    }

    private static String filter(String arg, Hashtable argFilter) {
        if (argFilter.containsKey(arg)) {
            return (String) argFilter.get(arg);
        } else {
            return arg;
        }
    }

    private static void debug(String dbg) {
        if (doDebug) {
            System.out.println("[ debug ] " + dbg);
        }
    }
}
