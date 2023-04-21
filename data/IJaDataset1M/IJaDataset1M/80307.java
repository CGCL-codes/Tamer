package org.apache.jasper.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.servlet.JspServletWrapper;

/**
 * Main JSP compiler class. This class uses Ant for compiling.
 *
 * @author Anil K. Vijendran
 * @author Mandar Raje
 * @author Pierre Delisle
 * @author Kin-man Chung
 * @author Remy Maucherat
 * @author Mark Roth
 */
public abstract class Compiler {

    protected org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Compiler.class);

    static Object javacLock = new Object();

    protected JspCompilationContext ctxt;

    protected ErrorDispatcher errDispatcher;

    protected PageInfo pageInfo;

    protected JspServletWrapper jsw;

    protected TagFileProcessor tfp;

    protected Options options;

    protected Node.Nodes pageNodes;

    public void init(JspCompilationContext ctxt, JspServletWrapper jsw) {
        this.jsw = jsw;
        this.ctxt = ctxt;
        this.options = ctxt.getOptions();
    }

    /**
     * <p>Retrieves the parsed nodes of the JSP page, if they are available.
     * May return null.  Used in development mode for generating detailed
     * error messages.  http://issues.apache.org/bugzilla/show_bug.cgi?id=37062.
     * </p>
     */
    public Node.Nodes getPageNodes() {
        return this.pageNodes;
    }

    /** 
     * Compile the jsp file into equivalent servlet in .java file
     * @return a smap for the current JSP page, if one is generated,
     *         null otherwise
     */
    protected String[] generateJava() throws Exception {
        String[] smapStr = null;
        long t1, t2, t3, t4;
        t1 = t2 = t3 = t4 = 0;
        if (log.isDebugEnabled()) {
            t1 = System.currentTimeMillis();
        }
        pageInfo = new PageInfo(new BeanRepository(ctxt.getClassLoader(), errDispatcher), ctxt.getJspFile());
        JspConfig jspConfig = options.getJspConfig();
        JspConfig.JspProperty jspProperty = jspConfig.findJspProperty(ctxt.getJspFile());
        pageInfo.setELIgnored(JspUtil.booleanValue(jspProperty.isELIgnored()));
        pageInfo.setScriptingInvalid(JspUtil.booleanValue(jspProperty.isScriptingInvalid()));
        if (jspProperty.getIncludePrelude() != null) {
            pageInfo.setIncludePrelude(jspProperty.getIncludePrelude());
        }
        if (jspProperty.getIncludeCoda() != null) {
            pageInfo.setIncludeCoda(jspProperty.getIncludeCoda());
        }
        String javaFileName = ctxt.getServletJavaFileName();
        ServletWriter writer = null;
        try {
            String javaEncoding = ctxt.getOptions().getJavaEncoding();
            OutputStreamWriter osw = null;
            try {
                osw = new OutputStreamWriter(new FileOutputStream(javaFileName), javaEncoding);
            } catch (UnsupportedEncodingException ex) {
                errDispatcher.jspError("jsp.error.needAlternateJavaEncoding", javaEncoding);
            }
            writer = new ServletWriter(new PrintWriter(osw));
            ctxt.setWriter(writer);
            JspUtil.resetTemporaryVariableName();
            ParserController parserCtl = new ParserController(ctxt, this);
            pageNodes = parserCtl.parse(ctxt.getJspFile());
            if (ctxt.isPrototypeMode()) {
                Generator.generate(writer, this, pageNodes);
                writer.close();
                writer = null;
                return null;
            }
            Validator.validate(this, pageNodes);
            if (log.isDebugEnabled()) {
                t2 = System.currentTimeMillis();
            }
            Collector.collect(this, pageNodes);
            tfp = new TagFileProcessor();
            tfp.loadTagFiles(this, pageNodes);
            if (log.isDebugEnabled()) {
                t3 = System.currentTimeMillis();
            }
            ScriptingVariabler.set(pageNodes, errDispatcher);
            TagPluginManager tagPluginManager = options.getTagPluginManager();
            tagPluginManager.apply(pageNodes, errDispatcher, pageInfo);
            TextOptimizer.concatenate(this, pageNodes);
            ELFunctionMapper.map(this, pageNodes);
            Generator.generate(writer, this, pageNodes);
            writer.close();
            writer = null;
            ctxt.setWriter(null);
            if (log.isDebugEnabled()) {
                t4 = System.currentTimeMillis();
                log.debug("Generated " + javaFileName + " total=" + (t4 - t1) + " generate=" + (t4 - t3) + " validate=" + (t2 - t1));
            }
        } catch (Exception e) {
            if (writer != null) {
                try {
                    writer.close();
                    writer = null;
                } catch (Exception e1) {
                }
            }
            new File(javaFileName).delete();
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e2) {
                }
            }
        }
        if (!options.isSmapSuppressed()) {
            smapStr = SmapUtil.generateSmap(ctxt, pageNodes);
        }
        tfp.removeProtoTypeFiles(ctxt.getClassFileName());
        return smapStr;
    }

    /** 
     * Compile the servlet from .java file to .class file
     */
    protected abstract void generateClass(String[] smap) throws FileNotFoundException, JasperException, Exception;

    /** 
     * Compile the jsp file from the current engine context
     */
    public void compile() throws FileNotFoundException, JasperException, Exception {
        compile(true);
    }

    /**
     * Compile the jsp file from the current engine context.  As an side-
     * effect, tag files that are referenced by this page are also compiled.
     * @param compileClass If true, generate both .java and .class file
     *                     If false, generate only .java file
     */
    public void compile(boolean compileClass) throws FileNotFoundException, JasperException, Exception {
        compile(compileClass, false);
    }

    /**
     * Compile the jsp file from the current engine context.  As an side-
     * effect, tag files that are referenced by this page are also compiled.
     *
     * @param compileClass If true, generate both .java and .class file
     *                     If false, generate only .java file
     * @param jspcMode true if invoked from JspC, false otherwise
     */
    public void compile(boolean compileClass, boolean jspcMode) throws FileNotFoundException, JasperException, Exception {
        if (errDispatcher == null) {
            this.errDispatcher = new ErrorDispatcher(jspcMode);
        }
        try {
            String[] smap = generateJava();
            if (compileClass) {
                generateClass(smap);
            }
        } finally {
            if (tfp != null) {
                tfp.removeProtoTypeFiles(null);
            }
            tfp = null;
            errDispatcher = null;
            pageInfo = null;
            if (!this.options.getDevelopment()) {
                pageNodes = null;
            }
            if (ctxt.getWriter() != null) {
                ctxt.getWriter().close();
                ctxt.setWriter(null);
            }
        }
    }

    /**
     * This is a protected method intended to be overridden by 
     * subclasses of Compiler. This is used by the compile method
     * to do all the compilation. 
     */
    public boolean isOutDated() {
        return isOutDated(true);
    }

    /**
     * Determine if a compilation is necessary by checking the time stamp
     * of the JSP page with that of the corresponding .class or .java file.
     * If the page has dependencies, the check is also extended to its
     * dependeants, and so on.
     * This method can by overidden by a subclasses of Compiler.
     * @param checkClass If true, check against .class file,
     *                   if false, check against .java file.
     */
    public boolean isOutDated(boolean checkClass) {
        String jsp = ctxt.getJspFile();
        if (jsw != null && (ctxt.getOptions().getModificationTestInterval() > 0)) {
            if (jsw.getLastModificationTest() + (ctxt.getOptions().getModificationTestInterval() * 1000) > System.currentTimeMillis()) {
                return false;
            } else {
                jsw.setLastModificationTest(System.currentTimeMillis());
            }
        }
        long jspRealLastModified = 0;
        try {
            URL jspUrl = ctxt.getResource(jsp);
            if (jspUrl == null) {
                ctxt.incrementRemoved();
                return false;
            }
            URLConnection uc = jspUrl.openConnection();
            jspRealLastModified = uc.getLastModified();
            uc.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        long targetLastModified = 0;
        File targetFile;
        if (checkClass) {
            targetFile = new File(ctxt.getClassFileName());
        } else {
            targetFile = new File(ctxt.getServletJavaFileName());
        }
        if (!targetFile.exists()) {
            return true;
        }
        targetLastModified = targetFile.lastModified();
        if (checkClass && jsw != null) {
            jsw.setServletClassLastModifiedTime(targetLastModified);
        }
        if (targetLastModified < jspRealLastModified) {
            if (log.isDebugEnabled()) {
                log.debug("Compiler: outdated: " + targetFile + " " + targetLastModified);
            }
            return true;
        }
        if (jsw == null) {
            return false;
        }
        List depends = jsw.getDependants();
        if (depends == null) {
            return false;
        }
        Iterator it = depends.iterator();
        while (it.hasNext()) {
            String include = (String) it.next();
            try {
                URL includeUrl = ctxt.getResource(include);
                if (includeUrl == null) {
                    return true;
                }
                URLConnection includeUconn = includeUrl.openConnection();
                long includeLastModified = includeUconn.getLastModified();
                includeUconn.getInputStream().close();
                if (includeLastModified > targetLastModified) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the error dispatcher.
     */
    public ErrorDispatcher getErrorDispatcher() {
        return errDispatcher;
    }

    /**
     * Gets the info about the page under compilation
     */
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public JspCompilationContext getCompilationContext() {
        return ctxt;
    }

    /**
     * Remove generated files
     */
    public void removeGeneratedFiles() {
        try {
            String classFileName = ctxt.getClassFileName();
            if (classFileName != null) {
                File classFile = new File(classFileName);
                if (log.isDebugEnabled()) log.debug("Deleting " + classFile);
                classFile.delete();
            }
        } catch (Exception e) {
        }
        try {
            String javaFileName = ctxt.getServletJavaFileName();
            if (javaFileName != null) {
                File javaFile = new File(javaFileName);
                if (log.isDebugEnabled()) log.debug("Deleting " + javaFile);
                javaFile.delete();
            }
        } catch (Exception e) {
        }
    }

    public void removeGeneratedClassFiles() {
        try {
            String classFileName = ctxt.getClassFileName();
            if (classFileName != null) {
                File classFile = new File(classFileName);
                if (log.isDebugEnabled()) log.debug("Deleting " + classFile);
                classFile.delete();
            }
        } catch (Exception e) {
        }
    }
}
