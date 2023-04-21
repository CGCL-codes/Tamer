package org.owasp.jxt.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.owasp.jxt.CompileException;
import org.owasp.jxt.JxtCompilation;
import org.owasp.jxt.JxtConfigException;
import org.owasp.jxt.JxtEngine;
import org.owasp.jxt.LocatedMessage;
import org.owasp.jxt.QuickCache;
import org.owasp.jxt.compiler.Compiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JxtServlet
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 13 $
 */
public class JxtServlet extends HttpServlet {

    private static final Logger _log = LoggerFactory.getLogger(JxtServlet.class);

    /** From Servlet Spec, attribute that has the name of the
     * web-app's temporary directory */
    private static final String TEMPDIR_ATTRIBUTE = "javax.servlet.context.tempdir";

    /** The classpath the Tomcat/Catalina uses for JSP compilation.
     * This is unfortunately specific to one container.  There does
     * not appear to be a standard way to retrieve it across all
     * servlet containers. */
    private static final String CATALINA_JSP_CLASSPATH = "org.apache.catalina.jsp_classpath";

    /** From Tomcat: "If development is false and checkInterval is
     * greater than zero, background compilations are
     * enabled. checkInterval is the time in seconds between checks to
     * see if a JSP page needs to be recompiled. [0]" */
    private static final String INITPARAM_CHECK_INTERVAL = "checkInterval";

    /** From Tomcat: "Causes a JSP (and its dependent files) to not be
     * checked for modification during the specified time interval (in
     * seconds) from the last time the JSP was checked for
     * modification. A value of 0 will cause the JSP to be checked on
     * every access.  Used in development mode only. [4]" */
    private static final String INITPARAM_MODIFICATION_TEST_INTERVAL = "modificationTestInterval";

    /** The default modification test interval. */
    private static final int DEFAULT_MODIFICATION_TEST_INTERVAL = 4;

    /** From Tomcat: Which compiler Ant should use to compile JSP
     * pages.  See the Ant documentation for more
     * information. [javac] */
    private static final String INITPARAM_COMPILER = "compiler";

    private static final String INITPARAM_COMPILER_SOURCE_VM = "compilerSourceVM";

    private static final String INITPARAM_COMPILER_TARGET_VM = "compilerTargetVM";

    private static final String INITPARAM_CLASSDEBUGINFO = "classdebuginfo";

    private static final String INITPARAM_CLASSPATH = "classpath";

    /** From Tomcat: "Is Jasper used in development mode? If true, the
     * frequency at which JSPs are checked for modification may be
     * specified via the modificationTestInterval
     * parameter. [true]" */
    private static final String INITPARAM_DEVELOPMENT = "development";

    private static final String INITPARAM_ENABLE_POOLING = "enablePooling";

    private static final String INITPARAM_FORK = "fork";

    private static final String INITPARAM_IE_CLASS_ID = "ieClassId";

    private static final String INITPARAM_JAVA_ENCODING = "javaEncoding";

    private static final String INITPARAM_KEEPGENERATED = "keepgenerated";

    private static final String INITPARAM_MAPPEDFILE = "mappedfile";

    private static final String INITPARAM_TRIMSPACES = "trimSpaces";

    private static final String INITPARAM_SUPPRESS_SMAP = "suppressSmap";

    private static final String INITPARAM_DUMP_SMAP = "dumpSmap";

    private static final String INITPARAM_GEN_STR_AS_CHAR_ARRAY = "genStrAsCharArray";

    private static final String INITPARAM_ERROR_ON_USE_BEAN_INVALID_CLASS_ATTRIBUTE = "errorOnUseBeanInvalidClassAttribute";

    private static final String INITPARAM_SCRATCHDIR = "scratchdir";

    private static final String INITPARAM_XPOWERED_BY = "xpoweredBy";

    /** Milliseconds per second. */
    private static final long MS_PER_SECOND = 1000L;

    private JxtEngine _engine;

    private WrapperCache _jxtWrapperCache;

    private static boolean booleanInitParam(ServletConfig config, String param, boolean defValue) {
        String value = config.getInitParameter(param);
        if (null == value) {
            return defValue;
        }
        if ("true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "1".equals(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
            return false;
        }
        _log.warn("Invalid value for " + param + " parameter, expected boolean, got: " + value);
        return defValue;
    }

    private static int integerInitParam(ServletConfig config, String param, int defValue) {
        String value = config.getInitParameter(param);
        if (null == value) {
            return defValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            _log.warn("Invalid value for " + param + " parameter, expected an integer, got: " + value);
            return defValue;
        }
    }

    /**
     * Initializes the Servlet Wrapper Cache based upon the servlet
     * initialization parameters.
     */
    private WrapperCache initWrapperCache(ServletConfig config) {
        long intervalBetweenStaleChecks;
        boolean developmentMode = booleanInitParam(config, INITPARAM_DEVELOPMENT, true);
        if (!developmentMode) {
            intervalBetweenStaleChecks = QuickCache.NO_STALE_CHECKS;
            int checkInterval = integerInitParam(config, INITPARAM_CHECK_INTERVAL, 0);
            if (checkInterval > 0) {
                _log.warn(INITPARAM_CHECK_INTERVAL + " is not supported");
            }
        } else {
            int modTestInterval = integerInitParam(config, INITPARAM_MODIFICATION_TEST_INTERVAL, DEFAULT_MODIFICATION_TEST_INTERVAL);
            if (modTestInterval < 0) {
                modTestInterval = DEFAULT_MODIFICATION_TEST_INTERVAL;
                _log.warn("Invalid " + INITPARAM_MODIFICATION_TEST_INTERVAL + " value, got " + modTestInterval + ", but must be >= 0");
            }
            intervalBetweenStaleChecks = modTestInterval * MS_PER_SECOND;
        }
        return new WrapperCache(intervalBetweenStaleChecks);
    }

    private Compiler createCompiler(ServletConfig config) {
        String name = config.getInitParameter(INITPARAM_COMPILER);
        if (null == name) {
            return Compiler.newInstance();
        }
        try {
            return Compiler.forName(name);
        } catch (IllegalArgumentException e) {
            _log.warn("Could not create compiler", e);
            return Compiler.newInstance();
        }
    }

    private Compiler initCompiler(ServletConfig config) {
        Compiler compiler = createCompiler(config);
        compiler.setSource(config.getInitParameter(INITPARAM_COMPILER_SOURCE_VM));
        compiler.setTarget(config.getInitParameter(INITPARAM_COMPILER_TARGET_VM));
        compiler.setDebug(booleanInitParam(config, INITPARAM_CLASSDEBUGINFO, true));
        return compiler;
    }

    @Override
    public final void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        String classPath = (String) ctx.getAttribute(CATALINA_JSP_CLASSPATH);
        File tempDir = (File) ctx.getAttribute(TEMPDIR_ATTRIBUTE);
        Compiler compiler = initCompiler(config);
        try {
            _engine = JxtEngine.builder().classPath(classPath).webRoot(new File(ctx.getRealPath("/"))).tempDir(tempDir).build();
        } catch (JxtConfigException e) {
            throw new ServletException("Configuration Error", e);
        }
        _jxtWrapperCache = initWrapperCache(config);
        _log.info("Initialized (version {})", _engine.getVersion());
        _log.debug("Class-Path = {}", classPath);
    }

    @Override
    public final void destroy() {
        _engine = null;
        _log.info("Destroyed");
    }

    @Override
    public final void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jxtFile = (String) request.getAttribute("javax.servlet.include.servlet_path");
        String pathInfo;
        if (jxtFile != null) {
            pathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
        } else {
            jxtFile = request.getServletPath();
            pathInfo = request.getPathInfo();
        }
        if (pathInfo != null) {
            jxtFile += pathInfo;
        }
        try {
            Wrapper wrapper = _jxtWrapperCache.get(jxtFile);
            _log.debug("Including {}", jxtFile);
            wrapper.service(request, response);
        } catch (JxtFileNotFoundException e) {
            _log.warn("Could not find {}", jxtFile);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        }
    }

    /**
     * Wraps compiled instances of JXT files.  This handles tracking
     * the source files used to create ths instance so that stale
     * checks may be performed.
     */
    class Wrapper {

        private String _name;

        private JxtServletBase _instance;

        private ServletException _initException;

        private List<File> _sourceFiles;

        private long _initTime;

        public Wrapper(String name) {
            _name = name;
            try {
                _log.debug("init {}", _name);
                _initTime = System.currentTimeMillis();
                JxtCompilation<JxtServletBase> compilation = _engine.compileServlet(_name);
                for (LocatedMessage warning : compilation.getWarnings()) {
                    _log.warn(warning.toString());
                }
                _instance = compilation.getTemplateClass().newInstance();
                _instance.init(getServletConfig());
                setSourceFiles(compilation.getSourceFiles());
            } catch (ServletException e) {
                _initException = e;
            } catch (CompileException e) {
                _initException = new ServletException("JXT Compilation Failed", e);
                setSourceFiles(e.getSourceFiles());
            } catch (InstantiationException e) {
                _initException = new ServletException("JXT Instantiation Exception", e);
            } catch (IllegalAccessException e) {
                _initException = new ServletException("JXT Access Exception", e);
            } catch (RuntimeException e) {
                _initException = new ServletException("JXT Initialization Exception", e);
            } catch (Error e) {
                _initException = new ServletException("JXT Initialization Error", e);
            }
        }

        private void setSourceFiles(Set<String> sourceFiles) {
            _sourceFiles = new ArrayList<File>(sourceFiles.size());
            ServletContext ctx = getServletContext();
            for (String sourceName : sourceFiles) {
                String realPath = ctx.getRealPath(sourceName);
                if (realPath == null) {
                    realPath = sourceName;
                }
                _sourceFiles.add(new File(realPath));
            }
        }

        @SuppressWarnings("deprecation")
        private boolean isSingleThreadModel() {
            return _instance instanceof javax.servlet.SingleThreadModel;
        }

        /**
         * Performs the stale check on a single JXT (and all its
         * dependencies).  Note: the lastmodificationtime check comes
         * from the calling WrapperCache/QuickCache.
         */
        public boolean isStale() {
            if (_sourceFiles == null) {
                File file = new File(getServletContext().getRealPath(_name));
                return file.exists() && file.lastModified() > _initTime;
            } else {
                for (File file : _sourceFiles) {
                    if (file.exists() && file.lastModified() > _initTime) {
                        return true;
                    }
                }
                return false;
            }
        }

        public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            if (_initException != null) {
                throw _initException;
            }
            if (isSingleThreadModel()) {
                synchronized (this) {
                    _instance.service(request, response);
                }
            } else {
                _instance.service(request, response);
            }
        }

        public void destroy() {
            if (_instance != null) {
                _instance.destroy();
            }
        }
    }

    /**
     * Cache for wrappers.
     */
    class WrapperCache extends QuickCache<String, Wrapper> {

        WrapperCache(long intervalBetweenStaleChecks) {
            super(intervalBetweenStaleChecks);
        }

        @Override
        protected Wrapper resolve(String jxtFile) {
            File file = new File(getServletContext().getRealPath(jxtFile));
            if (!file.exists()) {
                throw new JxtFileNotFoundException();
            }
            return new Wrapper(jxtFile);
        }

        @Override
        protected boolean isStale(String jxtFile, Wrapper wrapper) {
            return wrapper.isStale();
        }
    }

    private static class JxtFileNotFoundException extends RuntimeException {
    }
}
