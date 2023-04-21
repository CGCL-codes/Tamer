package org.databene.dbsanity.parser;

import java.util.Set;
import org.databene.commons.ArrayFormat;
import org.databene.commons.StringUtil;
import org.databene.commons.SyntaxError;
import org.databene.dbsanity.model.SanityCheckFile;
import org.databene.dbsanity.model.SanityCheckFolder;
import org.databene.dbsanity.model.StrategyBasedCheck;
import org.databene.webdecs.xml.AbstractXMLElementParser;
import org.databene.webdecs.xml.ParseContext;
import org.w3c.dom.Element;

/**
 * Parent class for DB Sanity's XML parsers.<br/><br/>
 * Created: 05.12.2010 13:24:53
 * @since 0.4
 * @author Volker Bergmann
 */
public abstract class AbstractDbSanityXMLParser extends AbstractXMLElementParser<Object> {

    protected static final String COLUMNS = "columns";

    protected static final String CONDITION = "condition";

    protected static final String REPORT_COLUMNS = "reportColumns";

    protected static final String EXPRESSION = "expression";

    protected static final String EXECUTOR = "executor";

    protected static final String MAX = "max";

    protected static final String MIN = "min";

    public AbstractDbSanityXMLParser(String elementName, Set<String> requiredAttributes, Set<String> optionalAttributes, Class<?>... supportedParentTypes) {
        super(elementName, requiredAttributes, optionalAttributes, supportedParentTypes);
    }

    public final Object doParse(Element element, Object[] parentPath, ParseContext<Object> context) {
        return parse(element, parentPath, (DbSanityParseContext) context);
    }

    public abstract Object parse(Element element, Object[] parentPath, DbSanityParseContext context);

    protected String getTableName(StrategyBasedCheck check, String xml) {
        String tableName = check.getTable();
        if (StringUtil.isEmpty(tableName)) throw new SyntaxError("Missing 'table' spec in outer <check>", xml);
        return tableName;
    }

    protected String[] parseColumns(Element element, boolean required) {
        String spec;
        if (required) spec = getRequiredAttribute(COLUMNS, element); else spec = getOptionalAttribute(COLUMNS, element);
        if (StringUtil.isEmpty(spec)) return new String[0];
        return StringUtil.trimAll(ArrayFormat.parse(spec, ",", String.class));
    }

    protected String getReportColumnsSpec(Element element) {
        return getOptionalAttribute(REPORT_COLUMNS, element);
    }

    protected String getCondition(Element element) {
        return getOptionalAttribute(CONDITION, element);
    }

    protected Executor parseExecutor(Element element, DbSanityParseContext context) {
        String spec = getOptionalAttribute(EXECUTOR, element);
        return (StringUtil.isEmpty(spec) ? defaultExecutor(context) : Executor.valueOf(spec.toUpperCase()));
    }

    protected Executor defaultExecutor(DbSanityParseContext context) {
        return Executor.DATABASE;
    }

    protected Long parseMin(Element element) {
        return parseOptionalLong(MIN, element);
    }

    protected Long parseMax(Element element) {
        return parseOptionalLong(MAX, element);
    }

    public static SanityCheckFolder getParentSanityCheckFolder(Object[] parentPath) {
        if (parentPath == null) return null;
        for (int i = parentPath.length - 1; i >= 0; i--) if (parentPath[i] instanceof SanityCheckFolder) return (SanityCheckFolder) parentPath[i];
        return null;
    }

    public static SanityCheckFile getParentSanityCheckFile(Object[] parentPath) {
        if (parentPath == null) return null;
        for (int i = parentPath.length - 1; i >= 0; i--) if (parentPath[i] instanceof SanityCheckFile) return (SanityCheckFile) parentPath[i];
        return null;
    }
}
