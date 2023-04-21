package org.webstrips.core.comic.javascript;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionHelper;
import org.mozilla.javascript.JavaMethodObject;
import org.mozilla.javascript.ParseIntMethod;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;
import org.mozilla.javascript.WarningMethod;
import org.mozilla.javascript.regexp.NativeRegExp;
import org.webstrips.LoadingException;
import org.webstrips.WebStrips;
import org.webstrips.core.comic.Archive;
import org.webstrips.core.comic.ArchiveEntry;
import org.webstrips.core.comic.Comic;
import org.webstrips.core.comic.ComicCapabilities;
import org.webstrips.core.comic.ComicDescription;
import org.webstrips.core.comic.ComicStrip;
import org.webstrips.core.comic.Links;
import org.webstrips.core.comic.Navigation;
import org.webstrips.core.comic.javascript.JavaScriptEvent.Type;
import org.webstrips.core.data.ContentProvider;

public class JavaScriptComic extends Comic implements Archive, Navigation, Links, ErrorReporter {

    private ComicCapabilities capabilities;

    private File source;

    private ClassShutter shutter = new ComicClassShutter();

    private ScriptableObject scope;

    private ComicDescription description;

    private Vector<ArchiveEntry> tmpArchive;

    private Function imageUrlFunction, titleFunction, firstFunction, previousFunction, nextFunction, newestFunction, archiveFunction, linkFunction;

    class ArchivePushMethod extends JavaMethodObject {

        private static final long serialVersionUID = 1L;

        private boolean start = false;

        private int[] args = new int[] { FunctionHelper.JAVA_STRING_TYPE, FunctionHelper.JAVA_STRING_TYPE };

        public ArchivePushMethod(Scriptable scope, boolean start) {
            super(start ? "archiveInsert" : "archiveAppend", scope);
            this.start = start;
        }

        @Override
        public boolean acceptsVariableArguments() {
            return false;
        }

        @Override
        protected int[] getArgumentSpec() {
            return args;
        }

        @Override
        protected Object invokeMethod(Object... objects) {
            if (tmpArchive == null) Context.throwAsScriptRuntimeEx(new RuntimeException("Method not allowed"));
            String id = (String) objects[0];
            String title = (String) objects[1];
            if (title == null || id == null) Context.throwAsScriptRuntimeEx(new RuntimeException("Null argument"));
            ArchiveEntry e = new ArchiveEntry(JavaScriptComic.this);
            e.setId(id);
            e.setTitle(title);
            if (start) {
                tmpArchive.insertElementAt(e, 0);
            } else {
                tmpArchive.add(e);
            }
            return true;
        }
    }

    public JavaScriptComic(File source, ContentProvider cp, ComicDescription d) throws IOException, LoadingException {
        super(cp);
        FileReader reader = new FileReader(source);
        this.description = d;
        this.source = source;
        Context context = openContext();
        scope = context.initStandardObjects();
        NativeRegExp.init(context, scope, false);
        try {
            Script script = context.compileReader(reader, source.getAbsolutePath(), 1, null);
            script.exec(context, scope);
        } finally {
            Context.exit();
        }
        initComicMethods();
        initLinks();
    }

    private void initLinks() throws LoadingException {
        ArrayList<String> cap = new ArrayList<String>();
        imageUrlFunction = getGlobalFunction("image");
        titleFunction = getGlobalFunction("title");
        firstFunction = getGlobalFunction("first");
        previousFunction = getGlobalFunction("previous");
        nextFunction = getGlobalFunction("next");
        newestFunction = getGlobalFunction("newest");
        archiveFunction = getGlobalFunction("archive");
        linkFunction = getGlobalFunction("link");
        if (imageUrlFunction == null) throw new LoadingException("image(id) function does not exist");
        if (titleFunction == null) throw new LoadingException("title(id) function does not exist");
        if (firstFunction != null) cap.add(Navigation.NAVIGATION_FIRST);
        if (previousFunction != null) cap.add(Navigation.NAVIGATION_PREVIOUS);
        if (nextFunction != null) cap.add(Navigation.NAVIGATION_NEXT);
        if (newestFunction != null) cap.add(Navigation.NAVIGATION_NEWEST);
        if (linkFunction != null) {
            cap.add(Links.LINKS);
        }
        if (archiveFunction != null) {
            cap.add(Archive.ARCHIVE);
        }
        capabilities = new ComicCapabilities(cap);
    }

    private void initComicMethods() {
        StringFormatMethod format = new StringFormatMethod(scope);
        ScriptableObject.defineProperty(scope, format.getFunctionName(), format, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        UrlExistsMethod urle = new UrlExistsMethod(scope, getContentProvider());
        ScriptableObject.defineProperty(scope, urle.getFunctionName(), urle, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        RetreiveHtmlMethod rhtml = new RetreiveHtmlMethod(scope, getContentProvider());
        ScriptableObject.defineProperty(scope, rhtml.getFunctionName(), rhtml, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        SleepMethod sl = new SleepMethod(scope);
        ScriptableObject.defineProperty(scope, sl.getFunctionName(), sl, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        PrintMethod pm = new PrintMethod(scope);
        ScriptableObject.defineProperty(scope, pm.getFunctionName(), pm, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        DateFormatMethod fm = new DateFormatMethod(scope);
        ScriptableObject.defineProperty(scope, fm.getFunctionName(), fm, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        ArchivePushMethod as = new ArchivePushMethod(scope, true);
        ScriptableObject.defineProperty(scope, as.getFunctionName(), as, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        ArchivePushMethod ae = new ArchivePushMethod(scope, false);
        ScriptableObject.defineProperty(scope, ae.getFunctionName(), ae, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        WarningMethod wrn = new WarningMethod(scope);
        ScriptableObject.defineProperty(scope, wrn.getFunctionName(), wrn, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
        ParseIntMethod parseInt = new ParseIntMethod(scope);
        ScriptableObject.defineProperty(scope, parseInt.getFunctionName(), parseInt, ScriptableObject.DONTENUM | ScriptableObject.READONLY);
    }

    private Function getGlobalFunction(String name) {
        try {
            Object o = scope.get(name, scope);
            if (o instanceof UniqueTag) {
                UniqueTag t = (UniqueTag) o;
                if (t.readResolve() == UniqueTag.NOT_FOUND) {
                    WebStrips.getApplicationLogger().report(WebStrips.JSINFO, "Method not found: %s", name);
                }
                return null;
            }
            if (o instanceof Function) return (Function) o;
            return null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public ComicCapabilities getCapabilities() {
        return capabilities;
    }

    @Override
    public String stripImageUrl(ComicStrip cs) {
        Object result = null;
        try {
            Context context = openContext();
            result = imageUrlFunction.call(context, scope, imageUrlFunction, new Object[] { cs.getId() });
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw e;
        } finally {
            closeContext();
        }
        if (result instanceof String) return result.toString();
        resultError("image", result, cs.getId());
        return null;
    }

    @Override
    public String stripTitle(ComicStrip cs) {
        Object result = null;
        try {
            Context context = openContext();
            result = titleFunction.call(context, scope, titleFunction, new Object[] { cs.getId() });
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw e;
        } finally {
            closeContext();
        }
        if (result instanceof String) return result.toString();
        resultError("title", result, cs.getId());
        return null;
    }

    public ArchiveEntry[] archive(ArchiveEntry from) {
        try {
            tmpArchive = new Vector<ArchiveEntry>();
            Context context = openContext();
            String id = (from == null) ? null : from.getId();
            archiveFunction.call(context, scope, archiveFunction, new Object[] { id });
        } catch (RhinoException e) {
            tmpArchive = null;
            closeContext();
            handleError(e);
            throw e;
        } finally {
        }
        closeContext();
        ArchiveEntry[] result = new ArchiveEntry[tmpArchive.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tmpArchive.get(i);
        }
        tmpArchive = null;
        return result;
    }

    public ComicStrip first() {
        Object result = null;
        try {
            Context context = openContext();
            result = firstFunction.call(context, scope, firstFunction, new Object[] {});
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw e;
        } finally {
            closeContext();
        }
        if (result == null) {
            resultError("first", result, null);
            return null;
        }
        return new ComicStrip(this, result.toString());
    }

    public ComicStrip newest() {
        Object result = null;
        try {
            Context context = openContext();
            result = newestFunction.call(context, scope, newestFunction, new Object[] {});
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw e;
        } finally {
            closeContext();
        }
        if (result == null) {
            resultError("newest", result, null);
            return null;
        }
        return new ComicStrip(this, result.toString());
    }

    public ComicStrip next(ComicStrip id) {
        Object result = null;
        try {
            Context context = Context.enter();
            result = nextFunction.call(context, scope, nextFunction, new Object[] { id.getId() });
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw e;
        } finally {
            closeContext();
        }
        if (result == null) {
            resultError("next", result, id.getId());
            return null;
        }
        return new ComicStrip(this, result.toString());
    }

    public ComicStrip previous(ComicStrip id) {
        Object result = null;
        try {
            Context context = openContext();
            result = previousFunction.call(context, scope, previousFunction, new Object[] { id.getId() });
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw e;
        } finally {
            closeContext();
        }
        if (result == null) {
            resultError("previous", result, id.getId());
            return null;
        }
        return new ComicStrip(this, result.toString());
    }

    public String link(ComicStrip cs) {
        Object result = null;
        try {
            Context context = openContext();
            result = linkFunction.call(context, scope, linkFunction, new Object[] { cs.getId() });
        } catch (RhinoException e) {
            closeContext();
            handleError(e);
            throw new RuntimeException("Script error: " + e.details());
        } finally {
            closeContext();
        }
        if (result != null) {
            try {
                return new URL(result.toString()).toString();
            } catch (MalformedURLException e) {
            }
        }
        return null;
    }

    private void resultError(String function, Object result, Object arg) {
        WebStrips.getApplicationLogger().report(WebStrips.JSINFO, "%s: Function '%s' returned '%s' for argument '%s'", description.comicName(), function, result, arg);
    }

    private Context openContext() {
        Context cx = Context.enter();
        cx.setClassShutter(shutter);
        cx.setErrorReporter(this);
        return cx;
    }

    private void closeContext() {
        Context c = Context.getCurrentContext();
        if (c != null) {
            try {
                Context.exit();
            } catch (IllegalStateException e) {
            }
        }
    }

    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (sourceName == null) sourceName = source.getAbsolutePath();
        JavaScriptEvent e = new JavaScriptEvent(message, sourceName, lineSource, line, lineOffset, Type.ERROR);
        JavaScriptManager.getManager().reportScriptEvent(e);
        throw new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (sourceName == null) sourceName = source.getAbsolutePath();
        JavaScriptEvent e = new JavaScriptEvent(message, sourceName, lineSource, line, lineOffset, Type.RUNTIME_ERROR);
        JavaScriptManager.getManager().reportScriptEvent(e);
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
    }

    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (sourceName == null) sourceName = source.getAbsolutePath();
        JavaScriptEvent e = new JavaScriptEvent(message, sourceName, lineSource, line, lineOffset, Type.WARNING);
        JavaScriptManager.getManager().reportScriptEvent(e);
    }

    private void handleError(RhinoException err) {
        if (err instanceof EcmaError) {
            JavaScriptEvent e = new JavaScriptEvent(err);
            JavaScriptManager.getManager().reportScriptEvent(e);
        }
    }
}
