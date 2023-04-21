package inc.che.common.ant.filter;

import java.io.*;
import java.util.*;
import java.text.*;
import org.apache.tools.ant.filters.*;
import org.apache.tools.ant.types.Parameter;

;

/**
 *  <b>Klasse filtert alle CVS Changelogs ab einem bestimmten Datum</b>
 * @version $Id: CvsChangelogFilterReader.java,v 1.1 2005/03/06 12:56:56 stevemcmee Exp $
 * @author <address> Steve McMee &lt;stevemcmee@users.sourceforge.net&gt;</address>
 */
public class CvsChangelogFilterReader extends BaseParamFilterReader implements ChainableReader {

    /** Parameter name for the words to filter on. */
    private static final String START_KEY = "start";

    private Date lastDate;

    private String lastMessage;

    private List messages = new ArrayList();

    private Hashtable files = new Hashtable();

    private Date start;

    /**
     * Remaining line to be read from this filter, or <code>null</code> if
     * the next call to <code>read()</code> should read the original stream
     * to find the next matching line.
     */
    private String line = null;

    private DateFormat paramDateFormat = new SimpleDateFormat("yyyy.MM.dd:HH.mm.ss");

    private DateFormat changelogDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm");

    private boolean read = true;

    private StringBuffer buffer;

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public CvsChangelogFilterReader() {
        super();
    }

    public CvsChangelogFilterReader(Reader in) {
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
    public final int read() throws IOException {
        try {
            if (!getInitialized()) {
                initialize();
                setInitialized(true);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
        int ch = -1;
        if (read) {
            line = readLine();
            while (line != null) {
                String vgldateStr = null;
                Date vglDate = null;
                if (line.startsWith("date:")) {
                    vgldateStr = line.substring(5, line.indexOf(" "));
                    try {
                        lastDate = changelogDateFormat.parse(vgldateStr);
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(lastDate);
                        cal.add(cal.HOUR, 1);
                        lastDate = cal.getTime();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        throw new IOException(ex.getMessage());
                    }
                } else {
                    if (line.startsWith("message:")) {
                        if (this.start.before(lastDate)) {
                            String tmp = line.substring(8);
                            if (!tmp.startsWith("#")) {
                                lastMessage = tmp;
                                this.messages.add(lastMessage);
                            } else {
                                lastMessage = null;
                            }
                        } else {
                            lastMessage = null;
                        }
                    } else {
                        if (line.startsWith("file:")) {
                            if (lastMessage != null) {
                                String file = getFileName(line.substring(5));
                                if (this.files.get(lastMessage) == null) {
                                    this.files.put(lastMessage, new HashSet());
                                }
                                ((Set) this.files.get(lastMessage)).add(file);
                            }
                        }
                    }
                }
                line = readLine();
            }
            if (line == null) {
                read = false;
                buffer = new StringBuffer();
                Iterator iterator = this.messages.iterator();
                while (iterator.hasNext()) {
                    String message = (String) iterator.next();
                    if (message != null) {
                        buffer.append("- ").append(message);
                        buffer.append("Files:\n");
                        Set fset = (Set) this.files.get(message);
                        if (fset != null) {
                            Iterator fiterator = fset.iterator();
                            while (fiterator.hasNext()) {
                                String file = (String) fiterator.next();
                                buffer.append("\t").append(file).append("\n");
                            }
                            buffer.append("\n");
                        }
                    }
                }
            }
        }
        if (!read) {
            if (buffer.length() > 1) {
                ch = buffer.charAt(0);
                buffer.deleteCharAt(0);
            }
        }
        return ch;
    }

    private void setStart(final Date date) {
        this.start = date;
    }

    private void setStart(final String start) throws ParseException {
        setStart(paramDateFormat.parse(start));
    }

    private final Date getStart() {
        return start;
    }

    /**
     * Creates a new CvsChangelogFilterReader using the passed in
     * Reader for instantiation.
     *
     * @param rdr A Reader object providing the underlying stream.
     *            Must not be <code>null</code>.
     *
     * @return a new filter based on this configuration, but filtering
     *         the specified reader
     */
    public final Reader chain(final Reader rdr) {
        CvsChangelogFilterReader newFilter = new CvsChangelogFilterReader(rdr);
        newFilter.setStart(getStart());
        newFilter.setInitialized(true);
        return newFilter;
    }

    /**
     * Parses the parameters to add user-defined contains strings.
     */
    private final void initialize() throws ParseException {
        Parameter[] params = getParameters();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (START_KEY.equals(params[i].getType())) {
                    setStart(params[i].getValue());
                }
            }
        }
    }

    private final String getFileName(String filePath) {
        return filePath.substring(0, filePath.indexOf("("));
    }
}
