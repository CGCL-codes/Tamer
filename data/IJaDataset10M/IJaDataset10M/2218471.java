package com.entelience.esis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import com.entelience.sql.DbHelper;
import com.entelience.util.BaseXMLParser;

/**
 * Parser derived from xerces used to manage the import
 * of XML audit data.
 */
public class FlashLangXmlParser extends BaseXMLParser {

    private int state = 0;

    private static final int BEGIN = 0;

    private static final int ADDED = 1;

    private static final int CHANGED = 2;

    private static final int REMOVED = 3;

    protected List<String> getLabelsToDelete() {
        return toDelete;
    }

    protected Map<String, String> getLabelsToAddOrUpdate() {
        return tags;
    }

    private Map<String, String> tags;

    private List<String> toDelete;

    protected String lang;

    protected String entityName;

    protected void parse_disactivate() {
        initCharEating();
    }

    protected void addTag(String tag, String value) throws Exception {
        tags.put(tag, value);
    }

    protected void modifyTag(String tag, String value) throws Exception {
        tags.put(tag, value);
    }

    protected void deleteTag(String tag) throws Exception {
        toDelete.add(tag);
    }

    /**
     * Main function for the parser, triggered on the start of each
     * element.
     */
    public void startElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
        try {
            if ("entity".equals(element.localpart)) {
                entityName = attrs.getValue("name");
                lang = attrs.getValue("lang");
                if (DbHelper.nullify(entityName) == null) {
                    throw new IllegalArgumentException("No entity name");
                }
                if (DbHelper.nullify(lang) == null) {
                    throw new IllegalArgumentException("No entity lang");
                }
                state = ADDED;
            } else if ("added".equals(element.localpart)) {
                state = ADDED;
            } else if ("changed".equals(element.localpart)) {
                state = CHANGED;
            } else if ("removed".equals(element.localpart)) {
                state = REMOVED;
            } else {
                if (state != REMOVED && state != BEGIN) {
                    startEatChars();
                }
            }
        } catch (Exception e) {
            _logger.fatal("Fatal error with xml parser declared.", e);
            XNIException ex = new XNIException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * This function is triggered by the parser each time it
     * parse an ending tag
     */
    public void endElement(QName element, Augmentations augs) {
        try {
            if (!"esis".equals(element.localpart) && !"added".equals(element.localpart) && !"changed".equals(element.localpart) && !"removed".equals(element.localpart) && !"entity".equals(element.localpart)) {
                if (state == ADDED) addTag(element.localpart, DbHelper.unnullify(stopEatChars())); else if (state == CHANGED) modifyTag(element.localpart, DbHelper.unnullify(stopEatChars())); else if (state == REMOVED) deleteTag(element.localpart);
            }
        } catch (Exception e) {
            _logger.fatal("Fatal error with xml parser declared.", e);
            XNIException ex = new XNIException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    /** Default constructor. */
    public FlashLangXmlParser() {
        super();
    }

    public static FlashLangXmlParser newParser() {
        _logger.debug("FlashLangXmlParser - new parser.");
        return new FlashLangXmlParser();
    }

    public int parse(String filename) throws Exception {
        tags = new HashMap<String, String>();
        toDelete = new ArrayList<String>();
        parse(new XMLInputSource(null, filename, null));
        parse_disactivate();
        return 0;
    }

    /**
     * The main parser routine
     */
    public int parse(InputStream i) throws Exception {
        tags = new HashMap<String, String>();
        toDelete = new ArrayList<String>();
        parse(new XMLInputSource(null, null, null, i, null));
        parse_disactivate();
        return 0;
    }
}
