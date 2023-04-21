package com.bayareasoftware.chartengine.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bayareasoftware.chartengine.ds.DataStream;
import com.bayareasoftware.chartengine.model.StringUtil;

/**
 * JSEngine is our internal script engine that executes transformations on DataStream using
 * javascript
 * 
 */
public final class JSEngine {

    private static final Log log = LogFactory.getLog(JSEngine.class);

    private JSEngine() {
    }

    public static boolean isScript(String q) {
        return StringUtil.trim(q) != null;
    }

    public static DataStream evalStream(DataStream ds, String script) throws Exception {
        if (isScript(script)) {
            DataGrid grid = evalGrid(ds, script);
            return grid.toStream();
        }
        return ds;
    }

    private static void verifyScript(String script) throws ScriptException {
        if (script.indexOf("importPackage") != -1) {
            throw new ScriptException("importing packages is not allowed in script");
        }
    }

    private static DataGrid evalGrid(DataStream inputData, String script) throws Exception {
        verifyScript(script);
        DataGrid[] args = new DataGrid[1];
        try {
            args[0] = new DataGrid(inputData);
        } finally {
            inputData.close();
            inputData = null;
        }
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("ECMAScript");
        if (jsEngine == null) {
            jsEngine = mgr.getEngineByName("js");
            if (jsEngine == null) {
                System.err.println("Couldn't find 'ECMAScript' or 'js' engine");
                return null;
            }
        }
        DataGrid ret = null;
        try {
            jsEngine.put("functions", JSFunctions.get());
            jsEngine.put("args", args);
            jsEngine.eval("var data = args[0]; var fn = functions");
            jsEngine.eval(script);
            Object result = jsEngine.get("data");
            if (result != null) {
                if (result instanceof DataGrid) {
                    ret = (DataGrid) result;
                } else {
                    log.error("Script must return a DataGrid object");
                    throw new ScriptException("Script must return a DataGrid object");
                }
            }
        } catch (ScriptException se) {
            if (log.isDebugEnabled()) {
                log.debug("caught script exception, re-throwing", se);
            }
            rethrowScriptException(se);
        }
        return ret;
    }

    private static void rethrowScriptException(ScriptException sce) throws ScriptException {
        String msg = null, filename = null;
        int line = sce.getLineNumber();
        int column = sce.getColumnNumber();
        filename = sce.getFileName();
        msg = sce.getMessage();
        if (msg == null) {
            msg = "";
        }
        if (filename == null || "<Unknown source>".equals(filename)) {
            filename = "SCRIPT";
        }
        msg = msg.replace("Wrapped javax.script.ScriptException: ", "");
        msg = msg.replace("org.mozilla.javascript.WrappedException: ", "");
        msg = msg.replace("org.mozilla.javascript.EvaluatorException: ", "");
        msg = msg.replace("sun.org.mozilla.javascript.internal.EvaluatorException: ", "");
        msg = msg.replace("sun.org.mozilla.javascript.internal.EcmaError: ", "");
        msg = msg.replace("com.bayareasoftware.chartengine.js.", "");
        int ind = msg.indexOf("(<Unknown source>");
        if (ind != -1) {
            msg = msg.substring(0, ind);
        }
        throw new ScriptException(msg, filename, line, column);
    }
}
