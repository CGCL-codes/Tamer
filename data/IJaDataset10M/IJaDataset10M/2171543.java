package wr3.util;

import java.io.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

/**
 * Groovy utility, ִ��Groovy�ű�.
 * <pre>
 * usage:
 * 1) .groovy file: 	// ִ�нű��ļ� 
 *	gr = GroovyUtil.create()
 *	  .set("x", "100").set("y", new Integer(100));
 *	gr.run ("test.groovy");
 * 	gr.get("z");
 * 
 * 2) express string:	// ִ�нű�Ƭ��
 * 	express = "z = \"id${x}: ${y**2}\", 'return value'";
 * 	gr = GroovyUtil.Express.create(express)
 * 	  .set ("x", "100").set ("y", new Integer(100));
 * 	rt = gr.eval ();
 * </pre>
 * @see http://groovy.codehaus.org/Embedding+Groovy
 * 
 * @author james
 * 
 * @see Script
 * @see TestGroovyUtil
 */
public class GroovyUtil {

    private Binding binding;

    private GroovyScriptEngine engine;

    public GroovyUtil() {
        initBinding();
        initEngine();
    }

    private void initBinding() {
        binding = new Binding();
    }

    private void initEngine() {
        try {
            engine = new GroovyScriptEngine("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * get instance with GroovyScriptEngine
	 * for Express string eval, and .groovy file run.  
	 * @return
	 */
    public static GroovyUtil create() {
        return new GroovyUtil();
    }

    /**
	 * set var's {name:value}
	 * @param name	variable name
	 * @param value	variable value
	 */
    public GroovyUtil set(String name, Object value) {
        binding.setVariable(name, value);
        return this;
    }

    /**
	 * run groovy script file 
	 * @param express bsh script source string
	 * @return last var's value
	 */
    public Object run(String filename) {
        Object rt = null;
        try {
            rt = engine.run(new File(filename).toURI().toString(), binding);
        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return rt;
    }

    /**
	 * get all var's name (NOT value), like "x = 10"
	 * not include properties like "def x = 10"
	 * @return
	 */
    public String[] getVars() {
        Set<?> keys = binding.getVariables().keySet();
        String[] vars = (String[]) (keys.toArray(new String[keys.size()]));
        return vars;
    }

    /**
	 * get named var's value
	 * @param name variable name
	 * @return variable value
	 */
    public Object get(String name) {
        return binding.getVariable(name);
    }

    /**
	 * ִ��Ƭ��
	 */
    public static final class Express {

        private Binding binding;

        private groovy.lang.Script script;

        private Express() {
            binding = new Binding();
        }

        /**
		 * get parsed express, ready for eval.
		 * @param express
		 * @return
		 */
        public static Express create(String express) {
            Express o = new Express();
            o.parse(express);
            return o;
        }

        private void parse(String express) {
            GroovyShell shell = new GroovyShell(binding);
            script = shell.parse(express);
        }

        /**
		 * set var's (name, value)
		 * @param name
		 * @param value
		 */
        public Express set(String name, Object value) {
            binding.setVariable(name, value);
            return this;
        }

        /**
		 * eval groovy script 
		 * @param express bsh script source string
		 * @return last var's value
		 */
        public Object eval() {
            return script.run();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CLI cli1 = new CLI().set("e", "express", true, "[-e express string], ���磺-e \"x=3;y=5;x*5\"").set("f", "file", true, "[-f groovy file], ���磺-f test\\GroovyUtil.groovy").parse(args);
        if (args.length == 0) {
            cli1.help("ִ��groovy�ļ���Ƭ��");
        }
        if (cli1.has("express")) {
            GroovyUtil.Express gr = GroovyUtil.Express.create(cli1.get("express"));
            System.out.println(gr.eval());
        }
        if (cli1.has("file")) {
            GroovyUtil gr = GroovyUtil.create();
            System.out.println(gr.run(cli1.get("file")));
        }
    }
}
