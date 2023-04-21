package org.ifcx.oo.groovy;

import com.sun.star.document.XScriptInvocationContext;
import com.sun.star.frame.XModel;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.reflection.InvocationTargetException;
import com.sun.star.script.provider.ScriptErrorRaisedException;
import com.sun.star.script.provider.ScriptFrameworkErrorException;
import com.sun.star.script.provider.ScriptFrameworkErrorType;
import com.sun.star.script.provider.XScript;
import com.sun.star.uno.Any;
import com.sun.star.uno.Type;
import com.sun.star.uno.XComponentContext;
import org.ifcx.oo.scripting.framework.container.ScriptMetaData;
import org.ifcx.oo.scripting.framework.provider.ClassLoaderFactory;
import org.ifcx.oo.scripting.framework.provider.ScriptContext;
import groovy.lang.Binding;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.CompilationFailedException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class GroovyScript implements XScript {

    private ScriptMetaData m_metaData;

    private XComponentContext m_xContext;

    private XMultiComponentFactory m_xMultiComponentFactory;

    private XModel m_xModel;

    private URL m_source;

    private ClassLoader m_classLoader;

    private Binding m_binding;

    private GroovyShell m_shell;

    Script m_script;

    public static final String VAR_XSCRIPTCONTEXT = "XSCRIPTCONTEXT";

    public static final String VAR_ARGS = "args";

    public GroovyScript(XComponentContext ctx, ScriptMetaData metaData, XModel xModel) {
        m_metaData = metaData;
        m_xContext = ctx;
        m_xModel = xModel;
        try {
            m_xMultiComponentFactory = m_xContext.getServiceManager();
            m_classLoader = ClassLoaderFactory.getURLClassLoader(m_metaData);
            m_binding = new Binding();
            XScriptInvocationContext m_xInvocCtx = null;
            m_binding.setVariable(VAR_XSCRIPTCONTEXT, ScriptContext.createContext(m_xModel, m_xInvocCtx, m_xContext, m_xMultiComponentFactory));
            m_binding.setVariable(VAR_ARGS, null);
            m_shell = new GroovyShell(m_classLoader, m_binding, CompilerConfiguration.DEFAULT);
            m_script = compile();
        } catch (Exception e) {
            LogUtils.DEBUG(LogUtils.getTrace(e));
            throw new com.sun.star.uno.RuntimeException("Error constructing GroovyScript : " + e.getMessage());
        }
        LogUtils.DEBUG("GroovyScript script data = " + metaData);
    }

    public Script compile() throws ScriptFrameworkErrorException {
        try {
            m_source = m_metaData.getSourceURL();
            m_metaData.loadSource();
            String source = m_metaData.getSource();
            if (source == null || source.length() == 0) {
                throw new ScriptFrameworkErrorException("Failed to read script", null, m_metaData.getLanguageName(), m_metaData.getLanguage(), ScriptFrameworkErrorType.UNKNOWN);
            }
            final String USER_SCRIPT_PATH = "/user/Scripts/groovy/";
            final String SHARED_SCRIPT_PATH = "/share/Scripts/groovy/";
            final String DOCUMENT_SCRIPT_PATH = "/Scripts/groovy/";
            final String DOCUMENT_SCHEME = "vnd.sun.star.tdoc:";
            String codeBase;
            String prefix;
            String name = m_metaData.getLanguageName();
            final String location = m_metaData.getParcelLocation();
            int x;
            if (location.startsWith(DOCUMENT_SCHEME)) {
                x = location.lastIndexOf(DOCUMENT_SCRIPT_PATH);
                prefix = location.substring(x + DOCUMENT_SCRIPT_PATH.length());
                codeBase = "openoffice/document/scripts/groovy";
            } else if ((x = location.lastIndexOf(USER_SCRIPT_PATH)) > 0) {
                prefix = location.substring(x + USER_SCRIPT_PATH.length());
                codeBase = "openoffice/user/scripts/groovy";
            } else if ((x = location.lastIndexOf(SHARED_SCRIPT_PATH)) > 0) {
                prefix = location.substring(x + SHARED_SCRIPT_PATH.length());
                codeBase = "openoffice/shared/scripts/groovy";
            } else {
                codeBase = "openoffice/unknown/scripts/groovy";
                prefix = "";
            }
            codeBase = codeBase + "/" + prefix + "/" + name;
            name = codeBase.replaceAll("/", ".");
            return m_shell.parse(new GroovyCodeSource(source, name, codeBase));
        } catch (CompilationFailedException e) {
            e.printStackTrace();
            throw new ScriptFrameworkErrorException("Compilation error " + m_metaData.getLanguageName(), null, m_metaData.getLanguageName(), m_metaData.getLanguage(), ScriptFrameworkErrorType.UNKNOWN);
        } catch (Exception e) {
            if (LogUtils.isDebugEnabled()) {
                e.printStackTrace();
                {
                    try {
                        FileOutputStream fs = new FileOutputStream("GroovyScriptDebugLog.txt");
                        PrintWriter pw = new PrintWriter(fs);
                        e.printStackTrace(pw);
                        pw.close();
                    } catch (IOException ioe) {
                    }
                }
            }
            throw new ScriptFrameworkErrorException("Runtime error : " + e.getMessage(), null, m_metaData.getLanguageName(), m_metaData.getLanguage(), ScriptFrameworkErrorType.UNKNOWN);
        }
    }

    /**
     * documentStorageID and document reference
     * for use in script name resolving
     *
     * @param aParams        All parameters; pure, out params are
     *                       undefined in sequence, i.e., the value
     *                       has to be ignored by the callee
     * @param aOutParamIndex Out indices
     * @param aOutParam      Out parameters
     * @throws IllegalArgumentException If there is no matching script name
     * @throws com.sun.star.script.CannotConvertException
     *                                  If args do not match or cannot
     *                                  be converted the those of the
     *                                  invokee
     * @throws com.sun.star.reflection.InvocationTargetException
     *                                  If the running script throws
     *                                  an exception this information
     *                                  is captured and rethrown as
     *                                  this exception type.
     * @returns The value returned from the function
     * being invoked
     */
    public Object invoke(Object[] aParams, short[][] aOutParamIndex, Object[][] aOutParam) throws ScriptFrameworkErrorException, InvocationTargetException {
        aOutParamIndex[0] = new short[0];
        aOutParam[0] = new Object[0];
        m_binding.setVariable(VAR_ARGS, aParams);
        try {
            if (LogUtils.isDebugEnabled()) {
                if (!m_metaData.getSourceURL().equals(m_source)) {
                    LogUtils.DEBUG("----------------------------");
                    LogUtils.DEBUG("!!!! Script URL changed !!!!");
                    LogUtils.DEBUG("----------------------------");
                }
            }
            Object result;
            final ClassLoader old_loader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(m_classLoader);
                final GroovyScriptEditor editor = GroovyScriptEditor.getEditor(m_source);
                if (editor == null) {
                    result = m_script.run();
                } else {
                    result = editor.execute(this);
                }
            } finally {
                Thread.currentThread().setContextClassLoader(old_loader);
            }
            if (LogUtils.isDebugEnabled()) {
                LogUtils.DEBUG("GroovyScript result = " + (null == result ? "NULL" : result.toString()));
            }
            if (result == null) {
                return new Any(new Type(), null);
            }
            return result;
        } catch (GroovyRuntimeException e) {
            e.printStackTrace();
            throw new InvocationTargetException("Groovy error " + m_metaData.getLanguageName(), null, processGroovyException(e, m_metaData.getLanguageName()));
        } catch (Exception e) {
            if (LogUtils.isDebugEnabled()) {
                e.printStackTrace();
                {
                    try {
                        FileOutputStream fs = new FileOutputStream("GroovyScriptDebugLog.txt");
                        PrintWriter pw = new PrintWriter(fs);
                        e.printStackTrace(pw);
                        pw.close();
                    } catch (IOException ioe) {
                    }
                }
            }
            throw new ScriptFrameworkErrorException("Runtime error : " + e.getMessage(), null, m_metaData.getLanguageName(), m_metaData.getLanguage(), ScriptFrameworkErrorType.UNKNOWN);
        }
    }

    private void raiseEditor(int lineNum) {
        GroovyScriptEditor editor;
        try {
            URL sourceUrl = m_metaData.getSourceURL();
            editor = GroovyScriptEditor.getEditor(sourceUrl);
            if (editor == null) {
                editor = GroovyScriptEditor.getEditor();
                XScriptInvocationContext m_xInocCtx = null;
                editor.edit(ScriptContext.createContext(m_xModel, m_xInocCtx, m_xContext, m_xMultiComponentFactory), m_metaData);
                editor = GroovyScriptEditor.getEditor(sourceUrl);
            }
            if (editor != null) {
                editor.indicateErrorLine(lineNum);
            }
        } catch (Exception ignore) {
        }
    }

    private ScriptErrorRaisedException processGroovyException(GroovyRuntimeException e, String script) {
        LogUtils.DEBUG("Groovy error RAW message " + e.getMessage());
        String message = e.getMessage();
        int usefullInfoIndex = message.lastIndexOf("\' :");
        int lineNum = (e.getNode() == null) ? -1 : e.getNode().getLineNumber();
        raiseEditor(lineNum);
        if (usefullInfoIndex > -1) {
            message = message.substring(usefullInfoIndex + 2);
        }
        {
            LogUtils.DEBUG("Error or ParseError Exception error: ");
            LogUtils.DEBUG("\tscript: " + script);
            LogUtils.DEBUG("\tline: " + lineNum);
            LogUtils.DEBUG("\tmessage: " + message);
            return new ScriptErrorRaisedException(message, null, script, "Groovy", lineNum);
        }
    }
}
