package net.sourceforge.cruisecontrol.sourcecontrols;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.SourceControl;
import net.sourceforge.cruisecontrol.util.DiscardConsumer;
import net.sourceforge.cruisecontrol.util.StreamPumper;
import net.sourceforge.cruisecontrol.util.ValidationHelper;
import net.sourceforge.cruisecontrol.util.IO;
import net.sourceforge.cruisecontrol.util.StreamLogger;
import org.apache.log4j.Logger;

/**
 * This class implements the SourceControlElement methods for a Clear Case
 * repository.
 *
 * @author Thomas Leseney
 * @author <a href="mailto:jcyip@thoughtworks.com">Jason Yip</a>
 * @author Eric Lefevre
 * @author Ralf Krakowski
 */
public class ClearCase implements SourceControl {

    private static final int DEFAULT = 0;

    private static final int DISABLED = 1;

    private static final int ENABLED = 2;

    private static final Logger LOG = Logger.getLogger(ClearCase.class);

    private SourceControlProperties properties = new SourceControlProperties();

    /**
     * The path of the clear case view
     */
    private String viewPath;

    /**
     * The branch to check for modifications
     */
    private String branch;

    private int recursive = DEFAULT;

    private int all = DEFAULT;

    /**
     * Date format required by commands passed to Clear Case
     */
    private final SimpleDateFormat inDateFormatter = new SimpleDateFormat("dd-MMMM-yyyy.HH:mm:ss");

    /**
     * Date format returned in the output of Clear Case commands.
     */
    private final SimpleDateFormat outDateFormatter = new SimpleDateFormat("yyyyMMdd.HHmmss");

    /**
     * Unlikely combination of characters to separate fields in a ClearCase query
     */
    static final String DELIMITER = "#~#";

    /**
     * Even more unlikely combination of characters to indicate end of one line in query.
     * Carriage return (\n) can be used in comments and so is not available to us.
     */
    static final String END_OF_STRING_DELIMITER = "@#@#@#@#@#@#@#@#@#@#@#@";

    /**
     * Sets the local working copy to use when making queries.
     *
     * @param path
     */
    public void setViewpath(String path) {
        viewPath = new File(path).getAbsolutePath();
    }

    /**
     * Sets the branch that we're concerned about checking files into.
     *
     * @param branch
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * Set whether to check against sub-folders in the view path
     */
    public void setRecursive(boolean recursive) {
        this.recursive = recursive ? ENABLED : DISABLED;
    }

    /**
     * Set when checking the entire view path.
     * <p/>
     * When checking the entire view path this option invokes 'lshistory -all'
     * instead of 'lshistory -recursive', which is much faster.
     * <p/>
     * This option is mutually exclusive with the recursive property.
     * <p/>
     * Note that 'all' does not use your view's config-spec rules. It behaves
     * like having a single line config-spec that selects just ELEMENT * /<branch>/LATEST
     * (i.e. 'lshistory -all' results that contain @@ are discarded). This differs from
     * 'recurse', which only shows items selected by your current view.
     */
    public void setAll(boolean all) {
        this.all = all ? ENABLED : DISABLED;
        if (this.recursive == DEFAULT && all) {
            this.recursive = DISABLED;
        }
    }

    public void setProperty(String property) {
        properties.assignPropertyName(property);
    }

    public Map getProperties() {
        return properties.getPropertiesAndReset();
    }

    public void validate() throws CruiseControlException {
        ValidationHelper.assertIsSet(viewPath, "viewpath", this.getClass());
        if (recursive == ENABLED && all == ENABLED) {
            ValidationHelper.fail("'recursive' and 'all' are mutually exclusive attributes for ClearCase");
        }
    }

    /**
     * Returns an {@link java.util.List List} of {@link ClearCaseModification}
     * detailing all the changes between now and the last build.
     *
     * @param lastBuild the last build time
     * @param now       time now, or time to check, NOT USED
     * @return the list of modifications, an empty (not null) list if no
     *         modifications.
     */
    public List getModifications(Date lastBuild, Date now) {
        String lastBuildDate = inDateFormatter.format(lastBuild);
        String nowDate = inDateFormatter.format(now);
        properties.put("clearcaselastbuild", lastBuildDate);
        properties.put("clearcasenow", nowDate);
        String command = "cleartool lshistory";
        if (branch != null) {
            command += " -branch " + branch;
        }
        if (recursive == DEFAULT || recursive == ENABLED) {
            command += " -r";
        } else if (all == ENABLED) {
            command += " -all";
        }
        command += " -nco -since " + lastBuildDate;
        command += " -fmt %u" + DELIMITER + "%Nd" + DELIMITER + "%En" + DELIMITER + "%Vn" + DELIMITER + "%o" + DELIMITER + "!%l" + DELIMITER + "!%a" + DELIMITER + "%Nc" + END_OF_STRING_DELIMITER + "\\n";
        File root = new File(viewPath);
        LOG.info("ClearCase: getting modifications for " + viewPath);
        LOG.debug("Command to execute : " + command);
        List modifications = null;
        try {
            Process p = Runtime.getRuntime().exec(command, null, root);
            Thread stderr = logErrorStream(p);
            InputStream input = p.getInputStream();
            modifications = parseStream(input);
            getRidOfLeftoverData(input);
            p.waitFor();
            stderr.join();
            IO.close(p);
        } catch (Exception e) {
            LOG.error("Error in executing the Clear Case command : ", e);
        }
        if (modifications == null) {
            modifications = new ArrayList();
        }
        return modifications;
    }

    private Thread logErrorStream(Process process) {
        Thread stderr = new Thread(StreamLogger.getWarnPumper(LOG, process));
        stderr.start();
        return stderr;
    }

    private void getRidOfLeftoverData(InputStream stream) {
        new StreamPumper(stream, new DiscardConsumer()).run();
    }

    /**
     * Parses the input stream to construct the modifications list.
     * Package-private to make it available to the unit test.
     *
     * @param input the stream to parse
     * @return a list of modification elements
     * @throws IOException
     */
    List parseStream(InputStream input) throws IOException {
        ArrayList modifications = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String ls = System.getProperty("line.separator");
        String line;
        String lines = "";
        while ((line = reader.readLine()) != null) {
            if (!lines.equals("")) {
                lines += ls;
            }
            lines += line;
            ClearCaseModification mod = null;
            if (lines.indexOf(END_OF_STRING_DELIMITER) > -1) {
                mod = parseEntry(lines.substring(0, lines.indexOf(END_OF_STRING_DELIMITER)));
                lines = "";
            }
            if (mod != null) {
                modifications.add(mod);
            }
        }
        return modifications;
    }

    /**
     * Parses a single line from the reader. Each line contains a single revision
     * with the format : <br>
     * username#~#date_of_revision#~#element_name#~#operation_type#~#comments  <br>
     *
     * @param line the line to parse
     * @return a modification element corresponding to the given line
     */
    private ClearCaseModification parseEntry(String line) {
        LOG.debug("parsing entry: " + line);
        String[] tokens = tokeniseEntry(line);
        if (tokens == null) {
            return null;
        }
        String username = tokens[0].trim();
        String timeStamp = tokens[1].trim();
        String elementName = tokens[2].trim();
        String version = tokens[3].trim();
        String operationType = tokens[4].trim();
        String labelList = tokens[5].substring(1).trim();
        Vector labels = extractLabelsList(labelList);
        String attributeList = tokens[6].substring(1).trim();
        Hashtable attributes = extractAttributesMap(attributeList);
        String comment = tokens[7].trim();
        if (operationType.equals("mkbranch") || operationType.equals("rmbranch")) {
            return null;
        }
        if (elementName.indexOf("@@") >= 0) {
            return null;
        }
        ClearCaseModification mod = new ClearCaseModification();
        mod.userName = username;
        mod.revision = version;
        String folderName, fileName;
        int sep = elementName.lastIndexOf(File.separator);
        if (sep > -1) {
            folderName = elementName.substring(0, sep);
            fileName = elementName.substring(sep + 1);
        } else {
            folderName = null;
            fileName = elementName;
        }
        ClearCaseModification.ModifiedFile modfile = mod.createModifiedFile(fileName, folderName);
        try {
            mod.modifiedTime = outDateFormatter.parse(timeStamp);
        } catch (ParseException e) {
            mod.modifiedTime = null;
        }
        modfile.action = operationType;
        modfile.revision = version;
        mod.type = "clearcase";
        mod.labels = labels;
        mod.attributes = attributes;
        mod.comment = comment;
        properties.modificationFound();
        return mod;
    }

    private String[] tokeniseEntry(String line) {
        int maxTokens = 8;
        int minTokens = maxTokens - 1;
        String[] tokens = new String[maxTokens];
        Arrays.fill(tokens, "");
        int tokenIndex = 0;
        for (int oldIndex = 0, index = line.indexOf(DELIMITER, 0); true; oldIndex = index + DELIMITER.length(), index = line.indexOf(DELIMITER, oldIndex), tokenIndex++) {
            if (tokenIndex > maxTokens) {
                LOG.debug("Too many tokens; skipping entry");
                return null;
            }
            if (index == -1) {
                tokens[tokenIndex] = line.substring(oldIndex);
                break;
            } else {
                tokens[tokenIndex] = line.substring(oldIndex, index);
            }
        }
        if (tokenIndex < minTokens) {
            LOG.debug("Not enough tokens; skipping entry");
            return null;
        }
        return tokens;
    }

    /**
     * @param attributeList
     * @return parsed list
     */
    private Hashtable extractAttributesMap(String attributeList) {
        Hashtable attributes = null;
        if (attributeList.length() > 0) {
            attributes = new Hashtable();
            StringTokenizer attrST = new StringTokenizer(attributeList, "(), ");
            while (attrST.hasMoreTokens()) {
                String attr = attrST.nextToken();
                int idx = attr.indexOf('=');
                if (idx > 0) {
                    String attrName = attr.substring(0, idx);
                    String attrValue = attr.substring(idx + 1);
                    if (attrValue.startsWith("\"")) {
                        attrValue = attrValue.substring(1, attrValue.length() - 1);
                    }
                    attributes.put(attrName, attrValue);
                }
            }
        }
        return attributes;
    }

    /**
     * @param labelList
     * @return parsed list
     */
    private Vector extractLabelsList(String labelList) {
        Vector labels = null;
        if (labelList.length() > 0) {
            labels = new Vector();
            StringTokenizer labelST = new StringTokenizer(labelList, "(), ");
            while (labelST.hasMoreTokens()) {
                labels.add(labelST.nextToken().trim());
            }
        }
        return labels;
    }
}
