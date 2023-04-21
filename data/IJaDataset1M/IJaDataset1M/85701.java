package org.mozilla.javascript.commonjs.module;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Implements the require() function as defined by 
 * <a href="http://wiki.commonjs.org/wiki/Modules/1.1">Common JS modules</a>.
 * <h1>Thread safety</h1>
 * You will ordinarily create one instance of require() for every top-level
 * scope. This ordinarily means one instance per program execution, except if 
 * you use shared top-level scopes and installing most objects into them. 
 * Module loading is thread safe, so using a single require() in a shared 
 * top-level scope is also safe.
 * <h1>Creation</h1>
 * If you need to create many otherwise identical require() functions for 
 * different scopes, you might want to use {@link RequireBuilder} for 
 * convenience.
 * <h1>Making it available</h1>
 * In order to make the require() function available to your JavaScript 
 * program, you need to invoke either {@link #install(Scriptable)} or 
 * {@link #requireMain(Context, String)}.
 * @author Attila Szegedi
 * @version $Id: Require.java 6395 2011-05-05 17:00:20Z mguillem $
 */
public class Require extends BaseFunction {

    private static final long serialVersionUID = 1L;

    private final ModuleScriptProvider moduleScriptProvider;

    private final Scriptable nativeScope;

    private final Scriptable paths;

    private final boolean sandboxed;

    private final Script preExec;

    private final Script postExec;

    private String mainModuleId = null;

    private Scriptable mainExports;

    private final Map<String, Scriptable> exportedModuleInterfaces = new ConcurrentHashMap<String, Scriptable>();

    private final Object loadLock = new Object();

    private static final ThreadLocal<Map<String, Scriptable>> loadingModuleInterfaces = new ThreadLocal<Map<String, Scriptable>>();

    /**
     * Creates a new instance of the require() function. Upon constructing it,
     * you will either want to install it in the global (or some other) scope 
     * using {@link #install(Scriptable)}, or alternatively, you can load the
     * program's main module using {@link #requireMain(Context, String)} and 
     * then act on the main module's exports.
     * @param cx the current context
     * @param nativeScope a scope that provides the standard native JavaScript 
     * objects.
     * @param moduleScriptProvider a provider for module scripts
     * @param preExec an optional script that is executed in every module's 
     * scope before its module script is run.
     * @param postExec an optional script that is executed in every module's 
     * scope after its module script is run.
     * @param sandboxed if set to true, the require function will be sandboxed. 
     * This means that it doesn't have the "paths" property, and also that the 
     * modules it loads don't export the "module.uri" property.  
     */
    public Require(Context cx, Scriptable nativeScope, ModuleScriptProvider moduleScriptProvider, Script preExec, Script postExec, boolean sandboxed) {
        this.moduleScriptProvider = moduleScriptProvider;
        this.nativeScope = nativeScope;
        this.sandboxed = sandboxed;
        this.preExec = preExec;
        this.postExec = postExec;
        setPrototype(ScriptableObject.getFunctionPrototype(nativeScope));
        if (!sandboxed) {
            paths = cx.newArray(nativeScope, 0);
            defineReadOnlyProperty(this, "paths", paths);
        } else {
            paths = null;
        }
    }

    /**
     * Calling this method establishes a module as being the main module of the
     * program to which this require() instance belongs. The module will be
     * loaded as if require()'d and its "module" property will be set as the
     * "main" property of this require() instance. You have to call this method
     * before the module has been loaded (that is, the call to this method must
     * be the first to require the module and thus trigger its loading). Note
     * that the main module will execute in its own scope and not in the global
     * scope. Since all other modules see the global scope, executing the main
     * module in the global scope would open it for tampering by other modules.
     * @param cx the current context 
     * @param mainModuleId the ID of the main module
     * @return the "exports" property of the main module
     * @throws IllegalStateException if the main module is already loaded when
     * required, or if this require() instance already has a different main 
     * module set. 
     */
    public Scriptable requireMain(Context cx, String mainModuleId) {
        if (this.mainModuleId != null) {
            if (!this.mainModuleId.equals(mainModuleId)) {
                throw new IllegalStateException("Main module already set to " + this.mainModuleId);
            }
            return mainExports;
        }
        ModuleScript moduleScript;
        try {
            moduleScript = moduleScriptProvider.getModuleScript(cx, mainModuleId, null, paths);
        } catch (RuntimeException x) {
            throw x;
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
        if (moduleScript != null) {
            mainExports = getExportedModuleInterface(cx, mainModuleId, null, true);
        } else if (!sandboxed) {
            URI mainUri = null;
            try {
                mainUri = new URI(mainModuleId);
            } catch (URISyntaxException usx) {
            }
            if (mainUri == null || !mainUri.isAbsolute()) {
                File file = new File(mainModuleId);
                if (!file.isFile()) {
                    throw ScriptRuntime.throwError(cx, nativeScope, "Module \"" + mainModuleId + "\" not found.");
                }
                mainUri = file.toURI();
            }
            mainExports = getExportedModuleInterface(cx, mainUri.toString(), mainUri, true);
        }
        this.mainModuleId = mainModuleId;
        return mainExports;
    }

    /**
     * Binds this instance of require() into the specified scope under the 
     * property name "require".
     * @param scope the scope where the require() function is to be installed.
     */
    public void install(Scriptable scope) {
        ScriptableObject.putProperty(scope, "require", this);
    }

    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args == null || args.length < 1) {
            throw ScriptRuntime.throwError(cx, scope, "require() needs one argument");
        }
        String id = (String) Context.jsToJava(args[0], String.class);
        URI uri = null;
        if (id.startsWith("./") || id.startsWith("../")) {
            if (!(thisObj instanceof ModuleScope)) {
                throw ScriptRuntime.throwError(cx, scope, "Can't resolve relative module ID \"" + id + "\" when require() is used outside of a module");
            }
            ModuleScope moduleScope = (ModuleScope) thisObj;
            URI base = moduleScope.getBase();
            URI current = moduleScope.getUri();
            if (base == null) {
                uri = current.resolve(id);
                id = uri.toString();
            } else {
                id = base.relativize(current).resolve(id).toString();
                if (id.charAt(0) == '.') {
                    if (sandboxed) {
                        throw ScriptRuntime.throwError(cx, scope, "Module \"" + id + "\" is not contained in sandbox.");
                    } else {
                        uri = current.resolve(id);
                        id = uri.toString();
                    }
                }
            }
        }
        return getExportedModuleInterface(cx, id, uri, false);
    }

    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw ScriptRuntime.throwError(cx, scope, "require() can not be invoked as a constructor");
    }

    private Scriptable getExportedModuleInterface(Context cx, String id, URI uri, boolean isMain) {
        Scriptable exports = exportedModuleInterfaces.get(id);
        if (exports != null) {
            if (isMain) {
                throw new IllegalStateException("Attempt to set main module after it was loaded");
            }
            return exports;
        }
        Map<String, Scriptable> threadLoadingModules = loadingModuleInterfaces.get();
        if (threadLoadingModules != null) {
            exports = threadLoadingModules.get(id);
            if (exports != null) {
                return exports;
            }
        }
        synchronized (loadLock) {
            exports = exportedModuleInterfaces.get(id);
            if (exports != null) {
                return exports;
            }
            final ModuleScript moduleScript = getModule(cx, id, uri);
            if (sandboxed && !moduleScript.isSandboxed()) {
                throw ScriptRuntime.throwError(cx, nativeScope, "Module \"" + id + "\" is not contained in sandbox.");
            }
            exports = cx.newObject(nativeScope);
            final boolean outermostLocked = threadLoadingModules == null;
            if (outermostLocked) {
                threadLoadingModules = new HashMap<String, Scriptable>();
                loadingModuleInterfaces.set(threadLoadingModules);
            }
            threadLoadingModules.put(id, exports);
            try {
                Scriptable newExports = executeModuleScript(cx, id, exports, moduleScript, isMain);
                if (exports != newExports) {
                    threadLoadingModules.put(id, newExports);
                    exports = newExports;
                }
            } catch (RuntimeException e) {
                threadLoadingModules.remove(id);
                throw e;
            } finally {
                if (outermostLocked) {
                    exportedModuleInterfaces.putAll(threadLoadingModules);
                    loadingModuleInterfaces.set(null);
                }
            }
        }
        return exports;
    }

    private Scriptable executeModuleScript(Context cx, String id, Scriptable exports, ModuleScript moduleScript, boolean isMain) {
        final ScriptableObject moduleObject = (ScriptableObject) cx.newObject(nativeScope);
        URI uri = moduleScript.getUri();
        URI base = moduleScript.getBase();
        defineReadOnlyProperty(moduleObject, "id", id);
        if (!sandboxed) {
            defineReadOnlyProperty(moduleObject, "uri", uri.toString());
        }
        final Scriptable executionScope = new ModuleScope(nativeScope, uri, base);
        executionScope.put("exports", executionScope, exports);
        executionScope.put("module", executionScope, moduleObject);
        moduleObject.put("exports", moduleObject, exports);
        install(executionScope);
        if (isMain) {
            defineReadOnlyProperty(this, "main", moduleObject);
        }
        executeOptionalScript(preExec, cx, executionScope);
        moduleScript.getScript().exec(cx, executionScope);
        executeOptionalScript(postExec, cx, executionScope);
        return ScriptRuntime.toObject(nativeScope, ScriptableObject.getProperty(moduleObject, "exports"));
    }

    private static void executeOptionalScript(Script script, Context cx, Scriptable executionScope) {
        if (script != null) {
            script.exec(cx, executionScope);
        }
    }

    private static void defineReadOnlyProperty(ScriptableObject obj, String name, Object value) {
        ScriptableObject.putProperty(obj, name, value);
        obj.setAttributes(name, ScriptableObject.READONLY | ScriptableObject.PERMANENT);
    }

    private ModuleScript getModule(Context cx, String id, URI uri) {
        try {
            final ModuleScript moduleScript = moduleScriptProvider.getModuleScript(cx, id, uri, paths);
            if (moduleScript == null) {
                throw ScriptRuntime.throwError(cx, nativeScope, "Module \"" + id + "\" not found.");
            }
            return moduleScript;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    @Override
    public String getFunctionName() {
        return "require";
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public int getLength() {
        return 1;
    }
}
