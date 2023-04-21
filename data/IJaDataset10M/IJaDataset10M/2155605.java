package org.databene.dbsanity.parser;

import java.io.File;
import java.util.Set;
import org.databene.commons.ArrayUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.FileUtil;
import org.databene.dbsanity.DbSanity;
import org.databene.dbsanity.model.SanityCheckFile;
import org.databene.dbsanity.model.SanityCheckFolder;
import org.w3c.dom.Element;

/**
 * Parent class for DB Sanity's XML parsers.<br/><br/>
 * Created: 05.12.2010 13:58:32
 * @since 0.4
 * @author Volker Bergmann
 */
public class DbSanityParser extends AbstractDbSanityXMLParser {

    private static final Set<String> OPTIONAL_ATTRIBUTES = CollectionUtil.toSet("ignorableTables", "recursionDepth", "authorInCloud", "tableInCloud");

    public DbSanityParser() {
        super("dbsanity", null, OPTIONAL_ATTRIBUTES);
    }

    @Override
    public boolean supports(Element element, Object[] parentPath) {
        String name = element.getNodeName();
        if (!"dbsanity".equals(name)) return false;
        return ArrayUtil.isEmpty(parentPath) || parent(parentPath) instanceof SanityCheckFolder;
    }

    @Override
    public SanityCheckFile parse(Element element, Object[] parentPath, DbSanityParseContext context) {
        File sourceFile = context.getSourceFile();
        File reportFolder = context.getReportFolder();
        File docPage = FileUtil.fileOfLimitedPathLength(reportFolder, sourceFile.getName(), ".html", DbSanity.MAX_PATH_LENGTH, false);
        File tempFolder = context.getTempFolder();
        SanityCheckFolder parent = getParentSanityCheckFolder(parentPath);
        SanityCheckFile checkFile = new SanityCheckFile(sourceFile, reportFolder, tempFolder, docPage, parent);
        String ignorableTables = getOptionalAttribute("ignorableTables", element);
        if (ignorableTables != null) {
            if ("dbsanity.xml".equals(sourceFile.getName())) checkFile.getRootSuite().setIgnorableTables(ignorableTables);
            parent.setIgnorableTables(ignorableTables);
        }
        Integer recursionDepth = parseOptionalInteger("recursionDepth", element);
        if (recursionDepth != null) context.set("recursionDepth", recursionDepth);
        Boolean authorInCloud = parseOptionalBoolean("authorInCloud", element);
        if (authorInCloud != null) context.set("authorInCloud", authorInCloud);
        Boolean tableInCloud = parseOptionalBoolean("tableInCloud", element);
        if (tableInCloud != null) context.set("tableInCloud", tableInCloud);
        context.parseChildElementsOf(element, context.createSubPath(parentPath, checkFile));
        return checkFile;
    }
}
