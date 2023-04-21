package com.google.gdt.eclipse.designer.ie.jsni;

import com.google.gdt.eclipse.designer.hosted.tdz.GWTEnvironmentUtils;
import com.google.gdt.eclipse.designer.ie.jsni.IDispatchImpl.HResultException;
import com.google.gdt.eclipse.designer.ie.util.Utils;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.javac.JsniMethod;
import com.google.gwt.dev.shell.CompilingClassLoader;
import com.google.gwt.dev.shell.DispatchIdOracle;
import com.google.gwt.dev.shell.JsValue;
import com.google.gwt.dev.shell.Jsni;
import com.google.gwt.dev.shell.ModuleSpace;
import com.google.gwt.dev.shell.ModuleSpaceHost;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.internal.ole.win32.IDispatch;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link com.google.gwt.dev.shell.ModuleSpace} for Internet Explorer 6.
 */
public class ModuleSpaceIE6 extends ModuleSpace {

    private final Map<String, NativeFunctionInfo> m_nativeFunctions = new HashMap<String, NativeFunctionInfo>();

    private static class NativeFunctionInfo {

        private final OleAutomation m_function;

        private final int m_callId;

        public NativeFunctionInfo(OleAutomation function, int callId) {
            m_function = function;
            m_callId = callId;
        }

        public void dispose() {
            m_function.dispose();
        }
    }

    /**
	 * Invoke a JavaScript function. This is instance method that caches COM things for speed.
	 * 
	 * @param name
	 *            the name of the function
	 * @param vArgs
	 *            the array of arguments. vArgs[0] is the this parameter supplied to the function, which must
	 *            be null if it is static.
	 * @return the return value of the JavaScript function
	 */
    private Variant doInvokeOnWindow(String name, Variant args[]) {
        NativeFunctionInfo functionInfo = m_nativeFunctions.get(name);
        if (functionInfo == null) {
            int ids[] = window.getIDsOfNames(new String[] { name });
            if (ids == null) {
                throw new RuntimeException("Could not find a native method with the signature '" + name + "'");
            }
            int functionId = ids[0];
            Variant functionVariant = window.getProperty(functionId);
            OleAutomation function = functionVariant.getAutomation();
            int callId = function.getIDsOfNames(new String[] { "call" })[0];
            functionVariant.dispose();
            functionInfo = new NativeFunctionInfo(function, callId);
            m_nativeFunctions.put(name, functionInfo);
        }
        return functionInfo.m_function.invoke(functionInfo.m_callId, args);
    }

    /**
	 * Invoke a JavaScript function. The static function exists to allow platform-dependent code to make
	 * JavaScript calls without having a ModuleSpaceIE6 (and all that entails) if it is not required.
	 * 
	 * @param window
	 *            the window containing the function
	 * @param name
	 *            the name of the function
	 * @param vArgs
	 *            the array of arguments. vArgs[0] is the this parameter supplied to the function, which must
	 *            be null if it is static.
	 * @return the return value of the JavaScript function
	 */
    protected Variant doInvokeOnWindow2(OleAutomation window, String name, Variant[] vArgs) {
        OleAutomation funcObj = null;
        Variant funcObjVar = null;
        try {
            int[] ids = window.getIDsOfNames(new String[] { name });
            if (ids == null) {
                throw new RuntimeException("Could not find a native method with the signature '" + name + "'");
            }
            int functionId = ids[0];
            funcObjVar = window.getProperty(functionId);
            funcObj = funcObjVar.getAutomation();
            int callDispId = funcObj.getIDsOfNames(new String[] { "call" })[0];
            return funcObj.invoke(callDispId, vArgs);
        } finally {
            if (funcObjVar != null) {
                funcObjVar.dispose();
            }
            if (funcObj != null) {
                funcObj.dispose();
            }
        }
    }

    private final OleAutomation window;

    /**
	 * Constructs a browser interface for use with an IE6 'window' automation object.
	 * 
	 * @param moduleName
	 */
    public ModuleSpaceIE6(ModuleSpaceHost host, IDispatch scriptFrameWindow, String moduleName) {
        super(host.getLogger(), host, moduleName);
        window = Utils.newOleAutomation(scriptFrameWindow);
    }

    @Override
    public void dispose() {
        for (NativeFunctionInfo function : m_nativeFunctions.values()) {
            function.dispose();
        }
        if (window != null) {
            window.dispose();
        }
        super.dispose();
        IDispatchProxy.clearIDispatchProxyRefs(getIsolatedClassLoader());
        for (int i = 0; i < 2; i++) {
            if (!GWTEnvironmentUtils.DEVELOPERS_HOST) {
                System.gc();
            }
            System.runFinalization();
            JsValue.mainThreadCleanup();
        }
    }

    /**
	 * Invokes a native javascript function.
	 * 
	 * @param name
	 *            the name of the function to invoke
	 * @param jthis
	 *            the function's 'this' context
	 * @param types
	 *            the type of each argument
	 * @param args
	 *            the arguments to be passed
	 * @return the return value as a Variant.
	 */
    @Override
    protected JsValue doInvoke(String name, Object jthis, Class<?>[] types, Object[] args) throws Throwable {
        Variant[] vArgs = null;
        try {
            CompilingClassLoader isolatedClassLoader = getIsolatedClassLoader();
            int len = args.length;
            vArgs = new Variant[len + 1];
            Class<?> jthisType = jthis == null ? Object.class : jthis.getClass();
            vArgs[0] = SwtOleGlue.convertObjectToVariant(isolatedClassLoader, jthisType, jthis);
            for (int i = 0; i < len; ++i) {
                vArgs[i + 1] = SwtOleGlue.convertObjectToVariant(isolatedClassLoader, types[i], args[i]);
            }
            Variant result = doInvokeOnWindow(name, vArgs);
            try {
                return new JsValueIE6(result);
            } finally {
                if (result != null) {
                    result.dispose();
                }
            }
        } finally {
            for (int i = 0; i < vArgs.length; ++i) {
                if (vArgs[i] != null) {
                    vArgs[i].dispose();
                }
            }
        }
    }

    @Override
    protected void doCreateNativeMethods(String jsni) {
        checkedExecute(jsni);
    }

    private void checkedExecute(String jsni) {
        try {
            Variant result = execute(jsni);
            if (result != null) {
                result.dispose();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to create JSNI methods", e);
        }
    }

    @Override
    protected void createStaticDispatcher(TreeLogger logger) {
        checkedExecute("function __defineStatic(__arg0) { window.__static = __arg0; }");
    }

    @Override
    protected Object getStaticDispatcher() {
        return new IDispatchProxy(getIsolatedClassLoader());
    }

    /**
	 * On IE6, we currently have no way of throwing arbitrary exception objects into JavaScript. What we throw
	 * in exception cases is an exception not under our exact control, so the best we can do is match
	 * descriptions to indicate a match. In practice this works well.
	 */
    @Override
    protected boolean isExceptionSame(Throwable original, Object exception) {
        Throwable caught;
        try {
            HResultException hre = new HResultException(original);
            RuntimeException jse = createJavaScriptException(getIsolatedClassLoader(), exception);
            Method method = jse.getClass().getMethod("getDescription");
            String description = (String) method.invoke(jse);
            return hre.getMessage().equals(description);
        } catch (SecurityException e) {
            caught = e;
        } catch (NoSuchMethodException e) {
            caught = e;
        } catch (IllegalArgumentException e) {
            caught = e;
        } catch (IllegalAccessException e) {
            caught = e;
        } catch (InvocationTargetException e) {
            caught = e;
        }
        throw new RuntimeException("Failed to invoke JavaScriptException.getDescription()", caught);
    }

    private Variant execute(String code) {
        int[] dispIds = window.getIDsOfNames(new String[] { "execScript", "code" });
        Variant[] vArgs = new Variant[1];
        vArgs[0] = new Variant(code);
        int[] namedArgs = new int[1];
        namedArgs[0] = dispIds[1];
        Variant result = window.invoke(dispIds[0], vArgs, namedArgs);
        vArgs[0].dispose();
        if (result == null) {
            String lastError = window.getLastError();
            throw new RuntimeException("Error (" + lastError + ") executing JavaScript:\n" + code);
        }
        return result;
    }
}
