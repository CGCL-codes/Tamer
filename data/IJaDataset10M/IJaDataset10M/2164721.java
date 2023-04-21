package de.spieleck.app.jacson.filter;

import java.util.Map;
import java.util.HashMap;
import de.spieleck.config.ConfigNode;
import de.spieleck.config.ConfigVerify.Acceptor;
import de.spieleck.app.jacson.JacsonException;
import de.spieleck.app.jacson.JacsonConfigException;
import de.spieleck.app.jacson.JacsonRegistry;
import de.spieleck.app.jacson.util.ConfigUtil;
import java.io.File;
import java.io.RandomAccessFile;

/**
 * Filter to look up lines from a file.
 *
 * @author brenck (Dirk.Brenckmann -at- gmx.de )
 * @author fsn
 * @version $Id: FileInsertFilter.java 45 2005-10-08 22:55:04Z pcs $
 * @jacson:plugin subtype="modify"
 */
public class FileInsertFilter extends FilterBase implements Acceptor {

    /**
     *	File cache
     */
    private static final Map RANDOMACCESSFILES = new HashMap();

    public static final String EVAL_NODE = "evaluation";

    public static final String LINE_NODE = "line";

    public static final String FILE_NODE = "filename";

    public static final String USEONNULL_NODE = "useOnNullChunks";

    /**
     * Is the filter to be used on "null"-chunks?
     * @jacson:param 
     */
    protected boolean useOnNullChunks = false;

    /**
     * Evaluation type
     * @jacson:param name="evaluation" default="false"
     */
    protected boolean lazyEval;

    /**
     * Line to access
     * @jacson:param
     */
    protected String line = null;

    /**
     * File to access
     * @jacson:param name="filename" required="true"
     */
    protected String file = null;

    /**
     *	Overwritten init method.
     *	Called first and only once in initialization process.
     */
    public void init(ConfigNode config, JacsonRegistry registry) throws JacsonConfigException {
        ConfigUtil.verify(config, this);
        useOnNullChunks = config.getBoolean(USEONNULL_NODE, false);
        lazyEval = PadFilter.obtainLazyness(config, EVAL_NODE);
        line = PadFilter.obtainXFix(config, LINE_NODE, lazyEval);
        file = PadFilter.obtainXFix(config, FILE_NODE, lazyEval);
    }

    /**
	 *	Overwritten accept method.
	 *	Called only once and after the call to init.
	 * (You can skip this by removing <code>ConfigUtil.verify()</code>
	 *	from <code>init()</code>)
	 */
    public boolean accept(ConfigNode node) {
        String name = node.getName();
        return EVAL_NODE.equals(name) || LINE_NODE.equals(name) || FILE_NODE.equals(name) || USEONNULL_NODE.equals(name);
    }

    /**
     *	Replace chunk by something found in a file.
     */
    public void putChunk(String chunk) throws JacsonException {
        if ((chunk == null) && (!useOnNullChunks)) {
            drain.putChunk(chunk);
        } else {
            String curFile = tryExpand(file, lazyEval);
            String curLine = tryExpand(line, lazyEval);
            drain.putChunk(getLine(curFile, curLine));
        }
    }

    public String getLine(String filename, String linenumber) {
        File file = null;
        if ((filename == null) || (filename.trim().length() < 1)) {
            return null;
        } else file = new File(filename);
        if ((!file.exists()) || (!file.isFile()) || (!file.canRead())) {
            return null;
        } else linenumber = (((linenumber == null) || (linenumber.trim().length() < 1)) ? "0" : linenumber);
        int line = 0;
        try {
            line = new Integer(linenumber).intValue();
        } catch (Exception e) {
        }
        RandomAccessFile rafile = (RandomAccessFile) RANDOMACCESSFILES.get(filename);
        if (rafile == null) {
            try {
                rafile = new RandomAccessFile(file, "r");
                RANDOMACCESSFILES.put(filename, rafile);
            } catch (Exception e) {
                return null;
            }
        }
        try {
            String ret = null;
            rafile.seek(0);
            for (int i = 0; (i < line); i++) {
                ret = rafile.readLine();
                if (ret == null) {
                    break;
                }
            }
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

    /** 
     * Lazy or non lazy evaluation of value.
     * @param val the value to be expanded or not
     * @param lazy true means the value needs expanding
     */
    protected String tryExpand(String val, boolean lazy) {
        return PadFilter.tryExpand(val, lazy, getRegState());
    }
}
