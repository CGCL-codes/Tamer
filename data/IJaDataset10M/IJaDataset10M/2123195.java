package org.ifcx.scripting.scala;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.URLClassLoader;
import java.net.URL;

public class ScalaTest {

    public static void main(String[] args) {
        try {
            URL[] urls = { new java.io.File("../lib/scala-library.jar").toURL() };
            URLClassLoader urlLoader = new URLClassLoader(urls, ScalaTest.class.getClassLoader());
            Thread.currentThread().setContextClassLoader(urlLoader);
        } catch (Exception e) {
            System.out.println("Failed to initialize classloader.");
            e.printStackTrace();
            System.exit(1);
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        System.out.println("Got a manager");
        ScriptEngine engine = manager.getEngineByName("scala");
        if (null == engine) {
            System.out.println("No got an engine!");
            System.exit(1);
        }
        System.out.println("got the engine");
        ScriptEngineFactory factory = new ScalaScriptEngineFactory();
        System.out.println("Got a factory");
        System.out.println("Engine version : " + factory.getEngineVersion());
        System.out.println("Language version : " + factory.getLanguageVersion());
        Object result = null;
        try {
            result = engine.eval("42");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());
    }
}
