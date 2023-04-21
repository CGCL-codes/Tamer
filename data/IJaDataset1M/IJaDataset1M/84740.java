package org.owasp.orizon.dawn;

import java.io.File;
import org.owasp.orizon.OrizonLog;
import org.owasp.orizon.core.Import;
import org.owasp.orizon.core.Method;
import org.owasp.orizon.report.Report;

public abstract class Helper {

    protected final int MAX_HELPERS = 8192;

    protected Import[] i;

    protected int iIndex;

    protected String name;

    protected OrizonLog log;

    protected Method m;

    protected Report r;

    protected boolean ready;

    protected boolean initialized = false;

    protected boolean importSet = false;

    protected String helperPreamble;

    protected boolean preambleSet = false;

    protected String helper;

    protected String baseDir;

    protected String dynamicPatternsFile = null;

    protected String dynamicPattern = null;

    protected String helperOutFilename;

    public Helper() {
        log = new OrizonLog(Helper.class);
        r = new Report();
        ready = false;
        initialized = true;
        baseDir = "";
    }

    public Helper(Method m) {
        this();
        if (m != null) {
            this.m = m;
            name = "Helper" + m.getName();
            ready = true;
        }
    }

    public String getName() {
        return (ready) ? name : null;
    }

    public void setBaseDir(String dir) {
        this.baseDir = dir;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public boolean load(Method m) {
        if (!initialized) {
            log.error("it seems you didn't initialize this object correctly. Please don't override my constructors...");
            return false;
        }
        if (ready) {
            log.error("a method has already been loaded. Try calling create() or run()");
            return false;
        }
        if (m == null) {
            log.error("trying to feed me with null pointer... nice try!");
            return false;
        }
        ready = true;
        this.m = m;
        name = "Helper" + m.getName();
        return true;
    }

    public boolean delete() {
        File f = new File(this.name);
        return f.delete();
    }

    public abstract boolean setImportCount(int i);

    public abstract boolean setImport(Import i);

    public abstract boolean setPreamble();

    public abstract boolean create();

    public abstract boolean compile();

    public abstract boolean run(String parmLine);

    public final String getHelperXmlOutputFilename() {
        return helperOutFilename;
    }

    /**
	 * @return the dynamicPatternsFile
	 * @deprecated
	 */
    public final String getDynamicPatternsFile() {
        return dynamicPatternsFile;
    }

    /**
	 * @param dynamicPatternsFile the dynamicPatternsFile to set
	 * @deprecated
	 */
    public final void setDynamicPatternsFile(String dynamicPatternsFile) {
        this.dynamicPatternsFile = dynamicPatternsFile;
    }

    /**
	 * @return the dynamicPattern
	 */
    public final String getDynamicPattern() {
        return dynamicPattern;
    }

    /**
	 * @param dynamicPattern the dynamicPattern to set
	 */
    public final void setDynamicPattern(String dynamicPattern) {
        this.dynamicPattern = dynamicPattern;
    }
}
