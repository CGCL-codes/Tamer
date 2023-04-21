package org.dita.dost.reader;

import static org.dita.dost.util.Constants.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.dita.dost.exception.DITAOTException;
import org.dita.dost.exception.DITAOTXMLErrorHandler;
import org.dita.dost.log.MessageBean;
import org.dita.dost.log.MessageUtils;
import org.dita.dost.util.CatalogUtils;
import org.dita.dost.util.DITAAttrUtils;
import org.dita.dost.util.FileUtils;
import org.dita.dost.util.FilterUtils;
import org.dita.dost.util.OutputUtils;
import org.dita.dost.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

/**
 * This class extends AbstractReader, used to parse relevant dita topics
 * and ditamap files for GenMapAndTopicListModule.
 * 
 * <p><strong>Not thread-safe</strong>. Instances can be reused by calling
 * {@link #reset()} between calls to {@link #parse(File)}.</p>
 * 
 * @version 1.0 2004-11-25
 * 
 * @author Wu, Zhi Qiang
 */
public final class GenListModuleReader extends AbstractXMLReader {

    /** XMLReader instance for parsing dita file */
    private XMLReader reader = null;

    /** Map of XML catalog info */
    private Map<String, String> catalogMap = null;

    /** Basedir of the current parsing file */
    private String currentDir = null;

    /** Flag for conref in parsing file */
    private boolean hasConRef = false;

    /** Flag for href in parsing file */
    private boolean hasHref = false;

    /** Flag for keyref in parsing file */
    private boolean hasKeyRef = false;

    /** Flag for whether parsing file contains coderef */
    private boolean hasCodeRef = false;

    /** Set of all the non-conref and non-copyto targets refered in current parsing file */
    private final Set<String> nonConrefCopytoTargets;

    /** Set of conref targets refered in current parsing file */
    private final Set<String> conrefTargets;

    /** Set of href nonConrefCopytoTargets refered in current parsing file */
    private final Set<String> hrefTargets;

    /** Set of href targets with anchor appended */
    private final Set<String> hrefTopicSet;

    /** Set of chunk targets */
    private final Set<String> chunkTopicSet;

    /** Set of subject schema files */
    private final Set<String> schemeSet;

    /** Set of subsidiary files */
    private final Set<String> subsidiarySet;

    /** Set of sources of those copy-to that were ignored */
    private final Set<String> ignoredCopytoSourceSet;

    /** Map of copy-to target to souce	*/
    private final Map<String, String> copytoMap;

    /** Map of key definitions */
    private final Map<String, String> keysDefMap;

    /** Map to store multi-level keyrefs */
    private final Map<String, String> keysRefMap;

    /** Flag for conrefpush   */
    private boolean hasconaction = false;

    /** Flag used to mark if parsing entered into excluded element */
    private boolean insideExcludedElement = false;

    /** Used to record the excluded level */
    private int excludedLevel = 0;

    /** foreign/unknown nesting level */
    private int foreignLevel = 0;

    /** chunk nesting level */
    private int chunkLevel = 0;

    /** mark topics in reltables */
    private int relTableLevel = 0;

    /** chunk to-navigation level */
    private int chunkToNavLevel = 0;

    /** Topic group nesting level */
    private int topicGroupLevel = 0;

    /** Flag used to mark if current file is still valid after filtering */
    private boolean isValidInput = false;

    /** Contains the attribution specialization from props */
    private String props;

    /** Set of outer dita files */
    private final Set<String> outDitaFilesSet;

    private String rootDir = null;

    private String currentFile = null;

    private String rootFilePath = null;

    private boolean setSystemid = true;

    /** Stack for @processing-role value */
    private final Stack<String> processRoleStack;

    /** Depth inside a @processing-role parent */
    private int processRoleLevel;

    /** Topics with role of "resource-only" */
    private final Set<String> resourceOnlySet;

    private final Set<String> crossSet;

    private final Set<String> schemeRefSet;

    /** Relationship graph between subject schema */
    private Map<String, Set<String>> relationGraph = null;

    /** StringBuffer to store <exportanchors> elements */
    private StringBuffer result = new StringBuffer();

    /** Flag to show whether a file has <exportanchors> tag */
    private boolean hasExport = false;

    /** For topic/dita files whether a </file> tag should be added */
    private boolean shouldAppendEndTag = false;

    /** Store the href of topicref tag */
    private String topicHref = "";

    /** Topicmeta set for merge multiple exportanchors into one.
     * Each topicmeta/prolog can define many exportanchors */
    private final Set<String> topicMetaSet;

    /** Refered topic id */
    private String topicId = "";

    /** Map to store plugin id */
    private final Map<String, Set<String>> pluginMap = new HashMap<String, Set<String>>();

    /** Transtype */
    private String transtype;

    /** Map to store referenced branches. */
    private final Map<String, List<String>> vaildBranches;

    /** Int to mark referenced nested elements. */
    private int level;

    /** Topicref stack */
    private final Stack<String> topicrefStack;

    /** Store the primary ditamap file name. */
    private String primaryDitamap = "";

    /** Get DITAAttrUtil */
    private final DITAAttrUtils ditaAttrUtils = DITAAttrUtils.getInstance();

    /** Store the external/peer keydefs */
    private final Map<String, String> exKeysDefMap;

    /**
     * Get transtype.
     * @return the transtype
     */
    public String getTranstype() {
        return transtype;
    }

    /**
     * Set transtype.
     * @param transtype the transtype to set
     */
    public void setTranstype(final String transtype) {
        this.transtype = transtype;
    }

    /**
     * @return the pluginMap
     */
    public Map<String, Set<String>> getPluginMap() {
        return pluginMap;
    }

    /**
     * @return the result
     */
    public StringBuffer getResult() {
        return result;
    }

    /**
     * Constructor.
     */
    public GenListModuleReader() {
        nonConrefCopytoTargets = new HashSet<String>(INT_64);
        hrefTargets = new HashSet<String>(INT_32);
        hrefTopicSet = new HashSet<String>(INT_32);
        chunkTopicSet = new HashSet<String>(INT_32);
        schemeSet = new HashSet<String>(INT_32);
        schemeRefSet = new HashSet<String>(INT_32);
        conrefTargets = new HashSet<String>(INT_32);
        copytoMap = new HashMap<String, String>(INT_16);
        subsidiarySet = new HashSet<String>(INT_16);
        ignoredCopytoSourceSet = new HashSet<String>(INT_16);
        outDitaFilesSet = new HashSet<String>(INT_64);
        keysDefMap = new HashMap<String, String>();
        keysRefMap = new HashMap<String, String>();
        exKeysDefMap = new HashMap<String, String>();
        processRoleLevel = 0;
        processRoleStack = new Stack<String>();
        resourceOnlySet = new HashSet<String>(INT_32);
        crossSet = new HashSet<String>(INT_32);
        topicMetaSet = new HashSet<String>(INT_16);
        vaildBranches = new HashMap<String, List<String>>(INT_32);
        level = 0;
        topicrefStack = new Stack<String>();
        props = null;
        try {
            reader = StringUtils.getXMLReader();
        } catch (final SAXException e) {
            throw new RuntimeException("Unable to create XML parser: " + e.getMessage(), e);
        }
        reader.setContentHandler(this);
        try {
            reader.setProperty(LEXICAL_HANDLER_PROPERTY, this);
        } catch (final SAXNotRecognizedException e1) {
            logger.logException(e1);
        } catch (final SAXNotSupportedException e1) {
            logger.logException(e1);
        }
    }

    /**
     * Init xml reader used for pipeline parsing.
     *
     * @param ditaDir ditaDir
     * @param validate whether validate input file
     * @param rootFile input file
     * @throws SAXException parsing exception
     */
    public void initXMLReader(final String ditaDir, final boolean validate, final String rootFile, final boolean arg_setSystemid) throws SAXException {
        rootDir = new File(rootFile).getAbsoluteFile().getParent();
        rootDir = FileUtils.removeRedundantNames(rootDir);
        rootFilePath = new File(rootFile).getAbsolutePath();
        rootFilePath = FileUtils.removeRedundantNames(rootFilePath);
        reader.setFeature(FEATURE_NAMESPACE_PREFIX, true);
        if (validate == true) {
            reader.setFeature(FEATURE_VALIDATION, true);
            reader.setFeature(FEATURE_VALIDATION_SCHEMA, true);
        } else {
            final String msg = MessageUtils.getMessage("DOTJ037W").toString();
            logger.logWarn(msg);
        }
        final XMLGrammarPool grammarPool = GrammarPoolManager.getGrammarPool();
        setGrammarPool(reader, grammarPool);
        CatalogUtils.setDitaDir(ditaDir);
        catalogMap = CatalogUtils.getCatalog(ditaDir);
        setSystemid = arg_setSystemid;
        try {
            Class.forName(RESOLVER_CLASS);
            reader.setEntityResolver(CatalogUtils.getCatalogResolver());
        } catch (final ClassNotFoundException e) {
            reader.setEntityResolver(this);
        }
    }

    /**
     * 
     * Reset the internal variables.
     */
    public void reset() {
        hasKeyRef = false;
        hasConRef = false;
        hasHref = false;
        hasCodeRef = false;
        currentDir = null;
        insideExcludedElement = false;
        excludedLevel = 0;
        foreignLevel = 0;
        chunkLevel = 0;
        relTableLevel = 0;
        chunkToNavLevel = 0;
        topicGroupLevel = 0;
        isValidInput = false;
        hasconaction = false;
        nonConrefCopytoTargets.clear();
        hrefTargets.clear();
        hrefTopicSet.clear();
        chunkTopicSet.clear();
        conrefTargets.clear();
        copytoMap.clear();
        ignoredCopytoSourceSet.clear();
        outDitaFilesSet.clear();
        keysDefMap.clear();
        keysRefMap.clear();
        exKeysDefMap.clear();
        schemeSet.clear();
        schemeRefSet.clear();
        level = 0;
        topicrefStack.clear();
        processRoleLevel = 0;
        processRoleStack.clear();
        ditaAttrUtils.reset();
    }

    /**
     * To see if the parsed file has conref inside.
     * 
     * @return true if has conref and false otherwise
     */
    public boolean hasConRef() {
        return hasConRef;
    }

    /**
     * To see if the parsed file has keyref inside.
     * 
     * @return true if has keyref and false otherwise
     */
    public boolean hasKeyRef() {
        return hasKeyRef;
    }

    /**
     * To see if the parsed file has coderef inside.
     * 
     * @return true if has coderef and false otherwise
     */
    public boolean hasCodeRef() {
        return hasCodeRef;
    }

    /**
     * To see if the parsed file has href inside.
     * 
     * @return true if has href and false otherwise
     */
    public boolean hasHref() {
        return hasHref;
    }

    /**
     * Get all targets except copy-to.
     * 
     * @return Returns allTargets.
     */
    public Set<String> getNonCopytoResult() {
        final Set<String> nonCopytoSet = new HashSet<String>(INT_128);
        nonCopytoSet.addAll(nonConrefCopytoTargets);
        nonCopytoSet.addAll(conrefTargets);
        nonCopytoSet.addAll(copytoMap.values());
        nonCopytoSet.addAll(ignoredCopytoSourceSet);
        addCoderefFiles(nonCopytoSet);
        return nonCopytoSet;
    }

    /**
     * Add coderef outside coderef files.
     * @param nonCopytoSet
     */
    private void addCoderefFiles(final Set<String> nonCopytoSet) {
        for (final String filename : subsidiarySet) {
            if (isOutFile(filename) && OutputUtils.getGeneratecopyouter() == OutputUtils.Generate.OLDSOLUTION) {
                nonCopytoSet.add(filename);
            }
        }
    }

    /**
     * Get the href target.
     * 
     * @return Returns the hrefTargets.
     */
    public Set<String> getHrefTargets() {
        return hrefTargets;
    }

    /**
     * Get conref targets.
     * 
     * @return Returns the conrefTargets.
     */
    public Set<String> getConrefTargets() {
        return conrefTargets;
    }

    /**
     * Get subsidiary targets.
     * 
     * @return Returns the subsidiarySet.
     */
    public Set<String> getSubsidiaryTargets() {
        return subsidiarySet;
    }

    /**
     * Get outditafileslist.
     * 
     * @return Returns the outditafileslist.
     */
    public Set<String> getOutDitaFilesSet() {
        return outDitaFilesSet;
    }

    /**
     * Get non-conref and non-copyto targets.
     * 
     * @return Returns the nonConrefCopytoTargets.
     */
    public Set<String> getNonConrefCopytoTargets() {
        return nonConrefCopytoTargets;
    }

    /**
     * Returns the ignoredCopytoSourceSet.
     *
     * @return Returns the ignoredCopytoSourceSet.
     */
    public Set<String> getIgnoredCopytoSourceSet() {
        return ignoredCopytoSourceSet;
    }

    /**
     * Get the copy-to map.
     * 
     * @return copy-to map
     */
    public Map<String, String> getCopytoMap() {
        return copytoMap;
    }

    /**
     * Get the Key definitions.
     * 
     * @return Key definitions map
     */
    public Map<String, String> getKeysDMap() {
        return keysDefMap;
    }

    public Map<String, String> getExKeysDefMap() {
        return exKeysDefMap;
    }

    /**
     * Set the relative directory of current file.
     * 
     * @param dir dir
     */
    public void setCurrentDir(final String dir) {
        this.currentDir = dir;
    }

    /**
     * Check if the current file is valid after filtering.
     * 
     * @return true if valid and false otherwise
     */
    public boolean isValidInput() {
        return isValidInput;
    }

    /**
     * Check if the current file has conaction.
     * @return true if has conaction and false otherwise
     */
    public boolean hasConaction() {
        return hasconaction;
    }

    /**
     * Parse input xml file.
     * 
     * @param file file
     * @throws SAXException SAXException
     * @throws IOException IOException
     * @throws FileNotFoundException FileNotFoundException
     */
    public void parse(final File file) throws FileNotFoundException, IOException, SAXException {
        currentFile = file.getAbsolutePath();
        reader.setErrorHandler(new DITAOTXMLErrorHandler(file.getName()));
        final InputSource is = new InputSource(new FileInputStream(file));
        if (setSystemid) {
            is.setSystemId(file.toURI().toURL().toString());
        }
        reader.parse(is);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts) throws SAXException {
        String domains = null;
        final Properties params = new Properties();
        final String printValue = atts.getValue(ATTRIBUTE_NAME_PRINT);
        ditaAttrUtils.increasePrintLevel(printValue);
        if (ditaAttrUtils.needExcludeForPrintAttri(transtype)) {
            return;
        }
        String attrValue = atts.getValue(ATTRIBUTE_NAME_PROCESSING_ROLE);
        final String href = atts.getValue(ATTRIBUTE_NAME_HREF);
        if (attrValue != null) {
            processRoleStack.push(attrValue);
            processRoleLevel++;
            if (ATTR_PROCESSING_ROLE_VALUE_RESOURCE_ONLY.equals(attrValue)) {
                if (href != null) {
                    resourceOnlySet.add(FileUtils.resolveFile(currentDir, href));
                }
            } else if (ATTR_PROCESSING_ROLE_VALUE_NORMAL.equalsIgnoreCase(attrValue)) {
                if (href != null) {
                    crossSet.add(FileUtils.resolveFile(currentDir, href));
                }
            }
        } else if (processRoleLevel > 0) {
            processRoleLevel++;
            if (ATTR_PROCESSING_ROLE_VALUE_RESOURCE_ONLY.equalsIgnoreCase(processRoleStack.peek())) {
                if (href != null) {
                    resourceOnlySet.add(FileUtils.resolveFile(currentDir, href));
                }
            } else if (ATTR_PROCESSING_ROLE_VALUE_NORMAL.equalsIgnoreCase(processRoleStack.peek())) {
                if (href != null) {
                    crossSet.add(FileUtils.resolveFile(currentDir, href));
                }
            }
        } else {
            if (href != null) {
                crossSet.add(FileUtils.resolveFile(currentDir, href));
            }
        }
        attrValue = atts.getValue(ATTRIBUTE_NAME_CLASS);
        if (attrValue != null) {
            if (TOPIC_TOPIC.matches(attrValue)) {
                topicId = atts.getValue(ATTRIBUTE_NAME_ID);
                final String filename = FileUtils.getRelativePathFromMap(rootFilePath, currentFile);
                if (result.indexOf(filename + QUESTION) != -1) {
                    result = new StringBuffer(result.toString().replace(filename + QUESTION, topicId));
                }
            }
            if (FileUtils.isDITAMapFile(currentFile) && rootFilePath.equals(currentFile) && MAP_MAP.matches(attrValue) && INDEX_TYPE_ECLIPSEHELP.equals(transtype)) {
                String pluginId = atts.getValue(ATTRIBUTE_NAME_ID);
                if (pluginId == null) {
                    pluginId = "org.sample.help.doc";
                }
                final Set<String> set = StringUtils.restoreSet(pluginId);
                pluginMap.put("pluginId", set);
            }
            if (INDEX_TYPE_ECLIPSEHELP.equals(transtype)) {
                if (MAP_TOPICMETA.matches(attrValue) || TOPIC_PROLOG.matches(attrValue)) {
                    topicMetaSet.add(qName);
                }
                if (DELAY_D_EXPORTANCHORS.matches(attrValue)) {
                    hasExport = true;
                    if (FileUtils.isDITAMapFile(currentFile)) {
                        String editedHref = "";
                        if (topicHref.endsWith(FILE_EXTENSION_XML)) {
                            editedHref = topicHref.replace(FILE_EXTENSION_XML, FILE_EXTENSION_DITA);
                        } else {
                            editedHref = topicHref;
                        }
                        result.append("<file name=\"" + editedHref + "\">");
                        result.append("<topicid name=\"" + topicId + "\"/>");
                    } else if (FileUtils.isDITATopicFile(currentFile)) {
                        String filename = FileUtils.getRelativePathFromMap(rootFilePath, currentFile);
                        if (filename.endsWith(FILE_EXTENSION_XML)) {
                            filename = filename.replace(FILE_EXTENSION_XML, FILE_EXTENSION_DITA);
                        }
                        filename = filename.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
                        result.append("<file name=\"" + filename + "\">");
                        result.append("<topicid name=\"" + topicId + "\">");
                        shouldAppendEndTag = true;
                    }
                } else if (DELAY_D_ANCHORKEY.matches(attrValue)) {
                    final String keyref = atts.getValue(ATTRIBUTE_NAME_KEYREF);
                    result.append("<keyref name=\"" + keyref + "\"/>");
                } else if (DELAY_D_ANCHORID.matches(attrValue)) {
                    final String id = atts.getValue(ATTRIBUTE_NAME_ID);
                    if (FileUtils.isDITAMapFile(currentFile)) {
                        if (!topicId.equals(id)) {
                            result.append("<id name=\"" + id + "\"/>");
                        }
                    } else if (FileUtils.isDITATopicFile(currentFile)) {
                        if (!topicId.equals(id)) {
                            result.append("<id name=\"" + id + "\"/>");
                        }
                    }
                }
            }
        }
        if (attrValue != null) {
            if (SUBJECTSCHEME_SUBJECTSCHEME.matches(attrValue)) {
                if (this.relationGraph == null) {
                    this.relationGraph = new LinkedHashMap<String, Set<String>>();
                }
                Set<String> children = this.relationGraph.get("ROOT");
                if (children == null || children.isEmpty()) {
                    children = new LinkedHashSet<String>();
                }
                children.add(this.currentFile);
                this.relationGraph.put("ROOT", children);
                schemeRefSet.add(FileUtils.getRelativePathFromMap(rootFilePath, currentFile));
            } else if (SUBJECTSCHEME_SCHEMEREF.matches(attrValue)) {
                Set<String> children = this.relationGraph.get(this.currentFile);
                if (children == null) {
                    children = new LinkedHashSet<String>();
                    this.relationGraph.put(currentFile, children);
                }
                if (href != null) {
                    children.add(FileUtils.resolveFile(rootDir, href));
                }
            }
        }
        if (foreignLevel > 0) {
            foreignLevel++;
            return;
        } else if (attrValue != null && (TOPIC_FOREIGN.matches(attrValue) || TOPIC_UNKNOWN.matches(attrValue))) {
            foreignLevel++;
        }
        if (chunkLevel > 0) {
            chunkLevel++;
        } else if (atts.getValue(ATTRIBUTE_NAME_CHUNK) != null) {
            chunkLevel++;
        }
        if (relTableLevel > 0) {
            relTableLevel++;
        } else if (attrValue != null && MAP_RELTABLE.matches(attrValue)) {
            relTableLevel++;
        }
        if (chunkToNavLevel > 0) {
            chunkToNavLevel++;
        } else if (atts.getValue(ATTRIBUTE_NAME_CHUNK) != null && atts.getValue(ATTRIBUTE_NAME_CHUNK).indexOf("to-navigation") != -1) {
            chunkToNavLevel++;
        }
        if (topicGroupLevel > 0) {
            topicGroupLevel++;
        } else if (atts.getValue(ATTRIBUTE_NAME_CLASS) != null && atts.getValue(ATTRIBUTE_NAME_CLASS).contains(MAPGROUP_D_TOPICGROUP.matcher)) {
            topicGroupLevel++;
        }
        if (attrValue == null && !ELEMENT_NAME_DITA.equals(localName)) {
            params.clear();
            params.put("%1", localName);
            logger.logInfo(MessageUtils.getMessage("DOTJ030I", params).toString());
        }
        if (attrValue != null && TOPIC_TOPIC.matches(attrValue)) {
            domains = atts.getValue(ATTRIBUTE_NAME_DOMAINS);
            if (domains == null) {
                params.clear();
                params.put("%1", localName);
                logger.logInfo(MessageUtils.getMessage("DOTJ029I", params).toString());
            } else {
                props = StringUtils.getExtProps(domains);
            }
        }
        if (insideExcludedElement) {
            ++excludedLevel;
            return;
        }
        if (FilterUtils.needExclude(atts, props)) {
            insideExcludedElement = true;
            ++excludedLevel;
            return;
        }
        if (attrValue != null) {
            if ((MAP_MAP.matches(attrValue)) || (TOPIC_TITLE.matches(attrValue))) {
                isValidInput = true;
            } else if (TOPIC_OBJECT.matches(attrValue)) {
                parseAttribute(atts, ATTRIBUTE_NAME_DATA);
            }
        }
        if (OutputUtils.getOnlyTopicInMap() && this.canResolved()) {
            final String classValue = atts.getValue(ATTRIBUTE_NAME_CLASS);
            if (MAP_TOPICREF.matches(classValue)) {
                final String hrefValue = atts.getValue(ATTRIBUTE_NAME_HREF);
                final String conrefValue = atts.getValue(ATTRIBUTE_NAME_CONREF);
                if (!StringUtils.isEmptyString(hrefValue)) {
                    final String attrScope = atts.getValue(ATTRIBUTE_NAME_SCOPE);
                    if ("external".equalsIgnoreCase(attrScope) || "peer".equalsIgnoreCase(attrScope) || hrefValue.indexOf(COLON_DOUBLE_SLASH) != -1 || hrefValue.startsWith(SHARP)) {
                        return;
                    }
                    final File target = new File(hrefValue);
                    String fileName = null;
                    if (target.isAbsolute()) {
                        fileName = FileUtils.getRelativePathFromMap(rootFilePath, hrefValue);
                    }
                    fileName = FileUtils.normalizeDirectory(currentDir, hrefValue);
                    fileName = fileName.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
                    final boolean canParse = parseBranch(atts, hrefValue, fileName);
                    if (!canParse) {
                        return;
                    } else {
                        topicrefStack.push(localName);
                    }
                } else if (!StringUtils.isEmptyString(conrefValue)) {
                    final String attrScope = atts.getValue(ATTRIBUTE_NAME_SCOPE);
                    if ("external".equalsIgnoreCase(attrScope) || "peer".equalsIgnoreCase(attrScope) || conrefValue.indexOf(COLON_DOUBLE_SLASH) != -1 || conrefValue.startsWith(SHARP)) {
                        return;
                    }
                    final File target = new File(conrefValue);
                    String fileName = null;
                    if (target.isAbsolute()) {
                        fileName = FileUtils.getRelativePathFromMap(rootFilePath, conrefValue);
                    }
                    fileName = FileUtils.normalizeDirectory(currentDir, conrefValue);
                    fileName = fileName.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
                    final boolean canParse = parseBranch(atts, conrefValue, fileName);
                    if (!canParse) {
                        return;
                    } else {
                        topicrefStack.push(localName);
                    }
                }
            }
        }
        parseAttribute(atts, ATTRIBUTE_NAME_CONREF);
        parseAttribute(atts, ATTRIBUTE_NAME_HREF);
        parseAttribute(atts, ATTRIBUTE_NAME_COPY_TO);
        parseAttribute(atts, ATTRIBUTE_NAME_IMG);
        parseAttribute(atts, ATTRIBUTE_NAME_CONACTION);
        parseAttribute(atts, ATTRIBUTE_NAME_KEYS);
        parseAttribute(atts, ATTRIBUTE_NAME_CONKEYREF);
        parseAttribute(atts, ATTRIBUTE_NAME_KEYREF);
    }

    /**
     * Method for see whether a branch should be parsed.
     * @param atts {@link Attributes}
     * @param hrefValue {@link String}
     * @param fileName normalized file name(remove '#')
     * @return boolean
     */
    private boolean parseBranch(final Attributes atts, final String hrefValue, final String fileName) {
        final String currentFileRelative = FileUtils.getRelativePathFromMap(rootFilePath, currentFile);
        if (currentDir == null && currentFileRelative.equals(primaryDitamap)) {
            addReferredBranches(hrefValue, fileName);
            return true;
        } else {
            final String id = atts.getValue(ATTRIBUTE_NAME_ID);
            if (level == 0 && StringUtils.isEmptyString(id)) {
                final boolean found = searchBrachesMap(id);
                if (found) {
                    addReferredBranches(hrefValue, fileName);
                    level++;
                    return true;
                } else {
                    return false;
                }
            } else if (level != 0) {
                addReferredBranches(hrefValue, fileName);
                level++;
                return true;
            } else if (!StringUtils.isEmptyString(id)) {
                final boolean found = searchBrachesMap(id);
                if (found) {
                    addReferredBranches(hrefValue, fileName);
                    level++;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Search braches map with branch id and current file name.
     * @param id String branch id.
     * @return boolean true if found and false otherwise.
     */
    private boolean searchBrachesMap(final String id) {
        final String currentFileRelative = FileUtils.getRelativePathFromMap(rootFilePath, currentFile);
        if (vaildBranches.containsKey(currentFileRelative)) {
            final List<String> branchIdList = vaildBranches.get(currentFileRelative);
            if (branchIdList.contains(id)) {
                return true;
            } else if (branchIdList.size() == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Add branches into map.
     * @param hrefValue
     * @param fileName
     */
    private void addReferredBranches(final String hrefValue, final String fileName) {
        String branchId = null;
        if (hrefValue.contains(SHARP)) {
            branchId = hrefValue.substring(hrefValue.lastIndexOf(SHARP) + 1);
            if (vaildBranches.containsKey(fileName)) {
                final List<String> branchIdList = vaildBranches.get(fileName);
                branchIdList.add(branchId);
            } else {
                final List<String> branchIdList = new ArrayList<String>();
                branchIdList.add(branchId);
                vaildBranches.put(fileName, branchIdList);
            }
        } else {
            vaildBranches.put(fileName, new ArrayList<String>());
        }
    }

    /**
     * Clean up.
     */
    @Override
    public void endDocument() throws SAXException {
        if (processRoleLevel > 0) {
            processRoleLevel--;
            processRoleStack.pop();
        }
        if (FileUtils.isDITATopicFile(currentFile) && shouldAppendEndTag) {
            result.append("</file>");
            shouldAppendEndTag = false;
        }
        checkMultiLevelKeys(keysDefMap, keysRefMap);
    }

    /**
     * Check if the current file is a ditamap with "@processing-role=resource-only".
     */
    @Override
    public void startDocument() throws SAXException {
        final String href = FileUtils.getRelativePathFromMap(rootFilePath, currentFile);
        if (FileUtils.isDITAMapFile(currentFile) && resourceOnlySet.contains(href) && !crossSet.contains(href)) {
            processRoleLevel++;
            processRoleStack.push(ATTR_PROCESSING_ROLE_VALUE_RESOURCE_ONLY);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (processRoleLevel > 0) {
            if (processRoleLevel == processRoleStack.size()) {
                processRoleStack.pop();
            }
            processRoleLevel--;
        }
        if (foreignLevel > 0) {
            foreignLevel--;
            return;
        }
        if (chunkLevel > 0) {
            chunkLevel--;
        }
        if (relTableLevel > 0) {
            relTableLevel--;
        }
        if (chunkToNavLevel > 0) {
            chunkToNavLevel--;
        }
        if (topicGroupLevel > 0) {
            topicGroupLevel--;
        }
        if (insideExcludedElement) {
            if (excludedLevel == 1) {
                insideExcludedElement = false;
            }
            --excludedLevel;
        }
        if (topicMetaSet.contains(qName) && hasExport) {
            if (FileUtils.isDITAMapFile(currentFile)) {
                result.append("</file>");
            } else if (FileUtils.isDITATopicFile(currentFile)) {
                result.append("</topicid>");
            }
            hasExport = false;
            topicMetaSet.clear();
        }
        if (!topicrefStack.isEmpty() && localName.equals(topicrefStack.peek())) {
            level--;
            topicrefStack.pop();
        }
        ditaAttrUtils.decreasePrintLevel();
    }

    /**
     * Resolve the publicId used in XMLCatalog.
     * @see org.dita.dost.reader.AbstractXMLReader#resolveEntity(String, String)
     * @param publicId publicId in doctype declarations
     * @param systemId systemId in doctype declarations
     * @throws java.io.IOException if dita-catalog.xml is not available
     * @exception org.xml.sax.SAXException if dita-catalog.xml is not in valid format.
     */
    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
        if (catalogMap.get(publicId) != null) {
            final File dtdFile = new File(catalogMap.get(publicId));
            return new InputSource(dtdFile.getAbsolutePath());
        } else if (catalogMap.get(systemId) != null) {
            final File schemaFile = new File(catalogMap.get(systemId));
            return new InputSource(schemaFile.getAbsolutePath());
        }
        return null;
    }

    /**
     * Parse the input attributes for needed information.
     */
    private void parseAttribute(final Attributes atts, final String attrName) throws SAXException {
        String attrValue = atts.getValue(attrName);
        String filename = null;
        final String attrClass = atts.getValue(ATTRIBUTE_NAME_CLASS);
        final String attrScope = atts.getValue(ATTRIBUTE_NAME_SCOPE);
        final String attrFormat = atts.getValue(ATTRIBUTE_NAME_FORMAT);
        final String attrType = atts.getValue(ATTRIBUTE_NAME_TYPE);
        final String codebase = atts.getValue(ATTRIBUTE_NAME_CODEBASE);
        if (attrValue == null) {
            return;
        }
        if (ATTRIBUTE_NAME_CONREF.equals(attrName) || ATTRIBUTE_NAME_CONKEYREF.equals(attrName)) {
            hasConRef = true;
        } else if (ATTRIBUTE_NAME_HREF.equals(attrName)) {
            if (attrClass != null && PR_D_CODEREF.matches(attrClass)) {
                hasCodeRef = true;
            } else {
                hasHref = true;
            }
        } else if (ATTRIBUTE_NAME_KEYREF.equals(attrName)) {
            hasKeyRef = true;
        }
        if (ATTRIBUTE_NAME_KEYS.equals(attrName) && attrValue.length() != 0) {
            String target = atts.getValue(ATTRIBUTE_NAME_HREF);
            final String keyRef = atts.getValue(ATTRIBUTE_NAME_KEYREF);
            if (target != null) {
                target = StringUtils.escapeXML(target);
            }
            final String copy_to = atts.getValue(ATTRIBUTE_NAME_COPY_TO);
            if (!StringUtils.isEmptyString(copy_to)) {
                target = copy_to;
            }
            if (target == null) {
                target = "";
            }
            final String temp = target;
            for (final String key : attrValue.split(" ")) {
                if (!keysDefMap.containsKey(key) && !key.equals("")) {
                    if (target != null && target.length() != 0) {
                        if (attrScope != null && (attrScope.equals("external") || attrScope.equals("peer"))) {
                            exKeysDefMap.put(key, target);
                            keysDefMap.put(key, target);
                        } else {
                            String tail = "";
                            if (target.indexOf(SHARP) != -1) {
                                tail = target.substring(target.indexOf(SHARP));
                                target = target.substring(0, target.indexOf(SHARP));
                            }
                            if (new File(target).isAbsolute()) {
                                target = FileUtils.getRelativePathFromMap(rootFilePath, target);
                            }
                            target = FileUtils.normalizeDirectory(currentDir, target);
                            keysDefMap.put(key, target + tail);
                        }
                    } else if (!StringUtils.isEmptyString(keyRef)) {
                        keysRefMap.put(key, keyRef);
                    } else {
                        keysDefMap.put(key, "");
                    }
                } else {
                    final Properties prop = new Properties();
                    prop.setProperty("%1", key);
                    prop.setProperty("%2", target);
                    logger.logInfo(MessageUtils.getMessage("DOTJ045I", prop).toString());
                }
                target = temp;
            }
        }
        if ("external".equalsIgnoreCase(attrScope) || "peer".equalsIgnoreCase(attrScope) || attrValue.indexOf(COLON_DOUBLE_SLASH) != -1 || attrValue.startsWith(SHARP)) {
            return;
        }
        if (attrValue.startsWith("file:/") && attrValue.indexOf("file://") == -1) {
            attrValue = attrValue.substring("file:/".length());
            if (UNIX_SEPARATOR.equals(File.separator)) {
                attrValue = UNIX_SEPARATOR + attrValue;
            }
        }
        final File target = new File(attrValue);
        if (target.isAbsolute() && !ATTRIBUTE_NAME_DATA.equals(attrName)) {
            attrValue = FileUtils.getRelativePathFromMap(rootFilePath, attrValue);
        } else if (ATTRIBUTE_NAME_DATA.equals(attrName)) {
            if (!StringUtils.isEmptyString(codebase)) {
                filename = FileUtils.normalizeDirectory(codebase, attrValue);
            } else {
                filename = FileUtils.normalizeDirectory(currentDir, attrValue);
            }
        } else {
            filename = FileUtils.normalizeDirectory(currentDir, attrValue);
        }
        try {
            filename = URLDecoder.decode(filename, UTF8);
        } catch (final UnsupportedEncodingException e) {
        }
        if (MAP_TOPICREF.matches(attrClass)) {
            if (ATTR_TYPE_VALUE_SUBJECT_SCHEME.equalsIgnoreCase(attrType)) {
                schemeSet.add(filename);
            }
            if (INDEX_TYPE_ECLIPSEHELP.equals(transtype)) {
                if (attrFormat == null || ATTR_FORMAT_VALUE_DITA.equalsIgnoreCase(attrFormat)) {
                    if (attrName.equals(ATTRIBUTE_NAME_HREF)) {
                        topicHref = filename;
                        topicHref = topicHref.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
                        if (attrValue.lastIndexOf(SHARP) != -1) {
                            final int position = attrValue.lastIndexOf(SHARP);
                            topicId = attrValue.substring(position + 1);
                        } else {
                            if (FileUtils.isDITAFile(topicHref)) {
                                topicId = topicHref + QUESTION;
                            }
                        }
                    }
                } else {
                    topicHref = "";
                    topicId = "";
                }
            }
        }
        if (("DITA-foreign".equals(attrType) && ATTRIBUTE_NAME_DATA.equals(attrName)) || attrClass != null && PR_D_CODEREF.matches(attrClass)) {
            subsidiarySet.add(filename);
            return;
        }
        if (FileUtils.isValidTarget(filename.toLowerCase()) && (StringUtils.isEmptyString(atts.getValue(ATTRIBUTE_NAME_COPY_TO)) || !FileUtils.isTopicFile(atts.getValue(ATTRIBUTE_NAME_COPY_TO).toLowerCase()) || (atts.getValue(ATTRIBUTE_NAME_CHUNK) != null && atts.getValue(ATTRIBUTE_NAME_CHUNK).contains("to-content"))) && !ATTRIBUTE_NAME_CONREF.equals(attrName) && !ATTRIBUTE_NAME_COPY_TO.equals(attrName) && (canResolved() || FileUtils.isSupportedImageFile(filename.toLowerCase()))) {
            if (attrFormat != null) {
                nonConrefCopytoTargets.add(filename + STICK + attrFormat);
            } else {
                nonConrefCopytoTargets.add(filename);
            }
        }
        if (attrFormat != null && !ATTR_FORMAT_VALUE_DITA.equalsIgnoreCase(attrFormat)) {
            return;
        }
        if (ATTRIBUTE_NAME_HREF.equals(attrName) && FileUtils.isTopicFile(filename) && canResolved()) {
            hrefTargets.add(new File(filename).getPath());
            toOutFile(new File(filename).getPath());
            if (chunkLevel > 0 && chunkToNavLevel == 0 && topicGroupLevel == 0 && relTableLevel == 0) {
                chunkTopicSet.add(filename);
            } else {
                hrefTopicSet.add(filename);
            }
        }
        if (ATTRIBUTE_NAME_CONREF.equals(attrName) && FileUtils.isDITAFile(filename)) {
            conrefTargets.add(filename);
            toOutFile(new File(filename).getPath());
        }
        if (ATTRIBUTE_NAME_COPY_TO.equals(attrName) && FileUtils.isTopicFile(filename)) {
            final String href = atts.getValue(ATTRIBUTE_NAME_HREF);
            if (StringUtils.isEmptyString(href)) {
                final StringBuffer buff = new StringBuffer();
                buff.append("[WARN]: Copy-to task [href=\"\" copy-to=\"");
                buff.append(filename);
                buff.append("\"] was ignored.");
                logger.logWarn(buff.toString());
            } else if (copytoMap.get(filename) != null) {
                final Properties prop = new Properties();
                prop.setProperty("%1", href);
                prop.setProperty("%2", filename);
                logger.logWarn(MessageUtils.getMessage("DOTX065W", prop).toString());
                ignoredCopytoSourceSet.add(href);
            } else if (!(atts.getValue(ATTRIBUTE_NAME_CHUNK) != null && atts.getValue(ATTRIBUTE_NAME_CHUNK).contains("to-content"))) {
                copytoMap.put(filename, FileUtils.normalizeDirectory(currentDir, href));
            }
            final String pathWithoutID = FileUtils.resolveFile(currentDir, attrValue);
            if (chunkLevel > 0 && chunkToNavLevel == 0 && topicGroupLevel == 0) {
                chunkTopicSet.add(pathWithoutID);
            } else {
                hrefTopicSet.add(pathWithoutID);
            }
        }
        if (ATTRIBUTE_NAME_CONACTION.equals(attrName)) {
            if (attrValue.equals("mark") || attrValue.equals("pushreplace")) {
                hasconaction = true;
            }
        }
    }

    /**
     * Get multi-level keys list
     */
    private List<String> getKeysList(final String key, final Map<String, String> keysRefMap) {
        final List<String> list = new ArrayList<String>();
        final Iterator<Entry<String, String>> iter = keysRefMap.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, String> entry = iter.next();
            if (entry.getValue().equals(key)) {
                final String entryKey = entry.getKey();
                list.add(entryKey);
                if (keysRefMap.containsValue(entryKey)) {
                    final List<String> tempList = getKeysList(entryKey, keysRefMap);
                    list.addAll(tempList);
                }
            }
        }
        return list;
    }

    /**
     * Update keysDefMap for multi-level keys
     */
    private void checkMultiLevelKeys(final Map<String, String> keysDefMap, final Map<String, String> keysRefMap) {
        String key = null;
        String value = null;
        final Map<String, String> tempMap = new HashMap<String, String>();
        final Iterator<Entry<String, String>> iter = keysDefMap.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<String, String> entry = iter.next();
            key = entry.getKey();
            value = entry.getValue();
            if (keysRefMap.containsValue(key)) {
                final List<String> keysList = getKeysList(key, keysRefMap);
                for (final String multikey : keysList) {
                    tempMap.put(multikey, value);
                }
            }
        }
        keysDefMap.putAll(tempMap);
    }

    private boolean isOutFile(final String toCheckPath) {
        if (!toCheckPath.startsWith("..")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMapFile() {
        final String current = FileUtils.removeRedundantNames(currentFile);
        if (FileUtils.isDITAMapFile(current)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean canResolved() {
        if ((OutputUtils.getOnlyTopicInMap() == false) || isMapFile()) {
            return true;
        } else {
            return false;
        }
    }

    private void addToOutFilesSet(final String hrefedFile) {
        if (canResolved()) {
            outDitaFilesSet.add(hrefedFile);
        }
    }

    private void toOutFile(final String filename) throws SAXException {
        final Properties prop = new Properties();
        prop.put("%1", FileUtils.normalizeDirectory(rootDir, filename));
        prop.put("%2", FileUtils.removeRedundantNames(currentFile));
        if ((OutputUtils.getGeneratecopyouter() == OutputUtils.Generate.NOT_GENERATEOUTTER) || (OutputUtils.getGeneratecopyouter() == OutputUtils.Generate.GENERATEOUTTER)) {
            if (isOutFile(filename)) {
                if (OutputUtils.getOutterControl().equals(OutputUtils.OutterControl.FAIL)) {
                    final MessageBean msgBean = MessageUtils.getMessage("DOTJ035F", prop);
                    throw new SAXParseException(null, null, new DITAOTException(msgBean, null, msgBean.toString()));
                }
                if (OutputUtils.getOutterControl().equals(OutputUtils.OutterControl.WARN)) {
                    final String message = MessageUtils.getMessage("DOTJ036W", prop).toString();
                    logger.logWarn(message);
                }
                addToOutFilesSet(filename);
            }
        }
    }

    /**
     * Get out file set.
     * @return out file set
     */
    public Set<String> getOutFilesSet() {
        return outDitaFilesSet;
    }

    /**
     * @return the hrefTopicSet
     */
    public Set<String> getHrefTopicSet() {
        return hrefTopicSet;
    }

    /**
     * @return the chunkTopicSet
     */
    public Set<String> getChunkTopicSet() {
        return chunkTopicSet;
    }

    /**
     * Get scheme set.
     * @return scheme set
     */
    public Set<String> getSchemeSet() {
        return this.schemeSet;
    }

    /**
     * Get scheme ref set.
     * @return scheme ref set
     */
    public Set<String> getSchemeRefSet() {
        return this.schemeRefSet;
    }

    /**
     * List of files with "@processing-role=resource-only".
     * @return the resource-only set
     */
    public Set<String> getResourceOnlySet() {
        resourceOnlySet.removeAll(crossSet);
        return resourceOnlySet;
    }

    /**
     * Get getRelationshipGrap.
     * @return relationship grap
     */
    public Map<String, Set<String>> getRelationshipGrap() {
        return this.relationGraph;
    }

    /**
     * @return the catalogMap
     */
    public Map<String, String> getCatalogMap() {
        return catalogMap;
    }

    public String getPrimaryDitamap() {
        return primaryDitamap;
    }

    public void setPrimaryDitamap(final String primaryDitamap) {
        this.primaryDitamap = primaryDitamap;
    }
}
