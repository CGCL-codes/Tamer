package inc.che.common.ant.filter;

import java.io.*;
import java.util.*;
import java.text.*;
import org.apache.tools.ant.filters.*;
import org.apache.tools.ant.types.Parameter;

/**
 *  <b>Klasse zum Ausfiltern von Bereichen in Quelldateien</b>
 * @version $Id: RangeFilterReader.java,v 1.1 2005/03/06 12:56:56 stevemcmee Exp $
 * @author <address> Steve McMee &lt;stevemcmee@users.sourceforge.net&gt; </address>
 */
public class RangeFilterReader extends BaseParamFilterReader implements ChainableReader {

    private static final String BEGIN_KEY = "begin";

    private static final String END_KEY = "end";

    private String begin;

    private String end;

    private boolean readRange = false;

    private boolean lockRange = false;

    private String line = null;

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public RangeFilterReader() {
        super();
    }

    public RangeFilterReader(Reader in) {
        super(in);
    }

    /**
     * Returns the next character in the filtered stream, only including
     * lines from the original stream which contain all of the specified words.
     *
     * @return the next character in the resulting stream, or -1
     * if the end of the resulting stream has been reached
     *
     * @exception IOException if the underlying stream throws an IOException
     * during reading
     */
    public int read() throws IOException {
        if (!getInitialized()) {
            initialize();
            setInitialized(true);
        }
        int ch = -1;
        if (line != null) {
            ch = line.charAt(0);
            if (line.length() == 1) {
                line = null;
            } else {
                line = line.substring(1);
            }
        } else {
            do {
                line = readLine();
                if (line != null) {
                    if (this.lockRange == false && line.indexOf(getBegin()) != -1) {
                        this.lockRange = true;
                        this.readRange = true;
                    }
                    if (this.readRange == true && line.indexOf(getEnd()) != -1) {
                        line = null;
                        this.readRange = false;
                    }
                    if (this.readRange == true) line = null;
                }
            } while (this.readRange == true);
            if (line != null) {
                return read();
            }
        }
        return ch;
    }

    /**
     * Creates a new CvsInfoFilterReader using the passed in
     * Reader for instantiation.
     *
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     *
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public Reader chain(final Reader rdr) {
        CvsInfoFilterReader newFilter = new CvsInfoFilterReader(rdr);
        newFilter.setBegin(getBegin());
        newFilter.setEnd(getEnd());
        newFilter.setInitialized(true);
        return newFilter;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getBegin() {
        return this.begin;
    }

    public String getEnd() {
        return this.end;
    }

    /**
     * Parses the parameters to add user-defined contains strings.
     */
    private void initialize() {
        Parameter[] params = getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (BEGIN_KEY.equals(params[i].getName())) {
                    setBegin(params[i].getValue());
                }
                if (END_KEY.equals(params[i].getName())) {
                    setEnd(params[i].getValue());
                }
            }
        }
    }
}
