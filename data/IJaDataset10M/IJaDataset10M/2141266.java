package kellinwood.framework.scripting;

import org.apache.bsf.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author ken
 */
public class ScriptContext {

    /**
     * Holds value of property filename.
     */
    private String filename;

    /**
     * Holds value of property manager.
     */
    private BSFManager manager;

    /**
     * Holds value of property interpreterName.
     */
    private String interpreterName;

    /**
     * Holds value of property script.
     */
    private String script;

    /**
     * Holds value of property engine.
     */
    private BSFEngine engine;

    private Object scriptInstance = null;

    private ByteArrayOutputStream baos = null;

    private PrintStream pout = null;

    /** Call a method in the script. */
    public Object call(String methodName, Class[] paramTypes, Object[] params) {
        boolean called = false;
        Object result = null;
        if (scriptInstance != null) {
            try {
                Method m = scriptInstance.getClass().getMethod(methodName, paramTypes);
                if (m != null) {
                    result = m.invoke(scriptInstance, params);
                    called = true;
                }
            } catch (Throwable x) {
            }
        }
        if (!called) {
            try {
                result = engine.call(null, methodName, params);
            } catch (Throwable x) {
            }
        }
        return result;
    }

    /** Creates a new ScriptContext that runs the specified script file.  
     *  The interpreter invoked depends on the file extension.
     */
    ScriptContext(String filename) throws Exception {
        setFilename(filename);
        manager = new BSFManager();
        baos = new ByteArrayOutputStream();
        pout = new PrintStream(baos);
        manager.declareBean("out", pout, PrintStream.class);
        StringBuffer sbuf = new StringBuffer();
        String fileExt = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        interpreterName = ScriptingUtil.getInterpreterForFileExtension(fileExt);
        if (interpreterName == null) throw new IllegalArgumentException("Unsupported file extension '." + fileExt + " for script: " + filename);
        File file = new File(filename);
        BufferedReader bufr = new BufferedReader(new FileReader(file));
        String line = bufr.readLine();
        while (line != null) {
            sbuf.append(line);
            sbuf.append('\n');
            line = bufr.readLine();
        }
        bufr.close();
        script = sbuf.toString();
        boolean setPrintStreamCalled = false;
        boolean mainCalled = false;
        engine = manager.loadScriptingEngine(interpreterName);
        Object sx = null;
        if (interpreterName.equals("beanshell")) {
            sx = engine.eval(filename, 0, 0, script);
        } else engine.exec(filename, 0, 0, script);
        scriptInstance = null;
        if (sx instanceof Class) {
            try {
                scriptInstance = ((Class) sx).newInstance();
            } catch (Throwable x) {
            }
        } else scriptInstance = sx;
        call("setPrintStream", new Class[] { PrintStream.class }, new Object[] { pout });
        call("main", new Class[] { String[].class }, new Object[] { new String[] {} });
    }

    void dispose() {
        call("dispose", new Class[] {}, new Object[] {});
        engine.terminate();
    }

    /**
     * Getter for property filename.
     * @return Value of property filename.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Setter for property filename.
     * @param filename New value of property filename.
     */
    private void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getter for property manager.
     * @return Value of property manager.
     */
    public BSFManager getManager() {
        return this.manager;
    }

    /**
     * Getter for property interpreterName.
     * @return Value of property interpreterName.
     */
    public String getInterpreterName() {
        return this.interpreterName;
    }

    /**
     * Getter for property script.
     * @return Value of property script.
     */
    public String getScript() {
        return this.script;
    }

    /**
     * Getter for property engine.
     * @return Value of property engine.
     */
    public BSFEngine getEngine() {
        return this.engine;
    }

    /**
     * Getter for property output.
     * @return Value of property output.
     */
    public String getOutput() {
        return baos.toString();
    }
}
