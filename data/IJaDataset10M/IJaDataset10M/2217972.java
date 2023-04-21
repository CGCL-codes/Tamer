package jaxil.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jaxil.Builder;
import jaxil.Loader;

/**
 * 
 * @author gael
 * @see Loader
 */
public class DefaultLoader implements Loader {

    private static DefaultLoader singleton = null;

    private ScriptEngine engine;

    /**
	 * Initialization of the loader
	 * Initialization of the JavaScript engine.
	 * initialization of the StandardBuilder
	 */
    private DefaultLoader() {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        Builder b = JBuilder.getInstance();
        engine.put("b", (Builder) b);
    }

    private DefaultLoader(Builder b) {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        engine.put("b", (Builder) b);
    }

    /**
	 * 
	 * @return a DefaultLoader
	 */
    public static DefaultLoader getInstance() {
        if (singleton == null) singleton = new DefaultLoader();
        return singleton;
    }

    public static DefaultLoader newInstance() {
        return new DefaultLoader(JBuilder.newInstance());
    }

    public static DefaultLoader newInstance(Builder builder) {
        return new DefaultLoader(builder);
    }

    /**
	 * execute JavaScript code
	 */
    public void eval(String s) {
        try {
            engine.eval(s);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    /**
	 * execute a JavaScript file
	 * @param path : the path of the file
	 */
    public void load(String path) {
        try {
            load(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
	 * execute a Reader stream of JavaScript
	 */
    public void load(Reader reader) {
        try {
            final BufferedReader r = new BufferedReader(reader);
            String s;
            while ((s = r.readLine()) != null) {
                engine.eval(s);
            }
            r.close();
        } catch (final IOException e) {
            System.err.println(e.toString());
        } catch (ScriptException e) {
            System.err.println(e.toString());
        }
    }

    /**
	 * execute a stream of JavaScript
	 */
    public void load(InputStream in) {
        load(new InputStreamReader(in));
    }

    /**
	 * load the Java XML Application
	 * @param args
	 */
    public static void main(String[] args) {
        Loader l = DefaultLoader.getInstance();
        ClassLoader c = ClassLoader.getSystemClassLoader();
        if (args == null) l.load(c.getResourceAsStream("builder.js")); else if (args.length == 0) l.load(c.getResourceAsStream("builder.js")); else l.load(args[0]);
    }

    @Override
    public void cleanMemory() {
        singleton = null;
        engine = null;
    }
}
