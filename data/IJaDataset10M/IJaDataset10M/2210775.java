package org.codehaus.janino.samples;

import org.codehaus.janino.*;

/**
 * A test program that allows you to play around with the
 * {@link org.codehaus.janino.ScriptEvaluator ScriptEvaluator} class.
 */
public class ScriptDemo extends DemoBase {

    public static void main(String[] args) throws Exception {
        String script = "System.out.println(\"Hello \" + a);";
        Class returnType = void.class;
        String[] parameterNames = { "a" };
        Class[] parameterTypes = { String.class };
        int i;
        for (i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (!arg.startsWith("-")) break;
            if (arg.equals("-s")) {
                script = args[++i];
            } else if (arg.equals("-rt")) {
                returnType = DemoBase.stringToType(args[++i]);
            } else if (arg.equals("-pn")) {
                parameterNames = DemoBase.explode(args[++i]);
            } else if (arg.equals("-pt")) {
                parameterTypes = DemoBase.stringToTypes(args[++i]);
            } else if (arg.equals("-help")) {
                ScriptDemo.usage();
                System.exit(0);
            } else {
                System.err.println("Invalid command line option \"" + arg + "\".");
                ScriptDemo.usage();
                System.exit(0);
            }
        }
        if (parameterTypes.length != parameterNames.length) {
            System.err.println("Parameter type count and parameter name count do not match.");
            ScriptDemo.usage();
            System.exit(1);
        }
        if (args.length - i != parameterNames.length) {
            System.err.println("Argument and parameter count do not match.");
            ScriptDemo.usage();
            System.exit(1);
        }
        Object[] parameterValues = new Object[parameterNames.length];
        for (int j = 0; j < parameterNames.length; ++j) {
            parameterValues[j] = DemoBase.createObject(parameterTypes[j], args[i + j]);
        }
        ScriptEvaluator se = new ScriptEvaluator(script, returnType, parameterNames, parameterTypes);
        Object res = se.evaluate(parameterValues);
        System.out.println("Result = " + (res == null ? "(null)" : res.toString()));
    }

    private ScriptDemo() {
    }

    private static void usage() {
        System.err.println("Usage:  ScriptDemo { <option> } { <parameter-value> }");
        System.err.println("Valid options are");
        System.err.println(" -s <script>");
        System.err.println(" -rt <return-type>");
        System.err.println(" -pn <comma-separated-parameter-names>");
        System.err.println(" -pt <comma-separated-parameter-types>");
        System.err.println(" -help");
        System.err.println("The number of parameter names, types and values must be identical.");
    }
}
