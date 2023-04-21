package org.ramadda.repository.type;

import org.ramadda.repository.*;
import org.ramadda.repository.database.*;
import org.ramadda.repository.output.*;
import org.w3c.dom.*;
import ucar.unidata.sql.Clause;
import ucar.unidata.sql.SqlUtil;
import ucar.unidata.util.DateUtil;
import ucar.unidata.util.HtmlUtil;
import ucar.unidata.util.IOUtil;
import ucar.unidata.util.LogUtil;
import ucar.unidata.util.Misc;
import ucar.unidata.util.StringUtil;
import ucar.unidata.util.TwoFacedObject;
import ucar.unidata.util.WrapperException;
import ucar.unidata.xml.XmlUtil;
import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

/**
 * Class TypeHandler _more_
 *
 *
 * @author RAMADDA Development Team
 * @version $Revision: 1.3 $
 */
public class GenericTypeHandler extends TypeHandler {

    /** _more_ */
    public static final String ATTR_CLASS = "class";

    /** _more_ */
    public static final String COL_ID = "id";

    /** _more_ */
    List<Column> columns = new ArrayList<Column>();

    /** _more_ */
    List colNames = new ArrayList();

    /** _more_ */
    Hashtable nameMap = new Hashtable();

    /**
     * _more_
     */
    public GenericTypeHandler() {
        super(null);
    }

    /**
     * _more_
     *
     * @param repository _more_
     * @param type _more_
     * @param description _more_
     */
    public GenericTypeHandler(Repository repository, String type, String description) {
        super(repository, type, description);
    }

    /**
     * _more_
     *
     * @param repository _more_
     * @param entryNode _more_
     *
     * @throws Exception _more_
     */
    public GenericTypeHandler(Repository repository, Element entryNode) throws Exception {
        super(repository, entryNode);
        init(entryNode);
    }

    /**
     * _more_
     *
     *
     * @param request _more_
     * @param entry _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public String getIconUrl(Request request, Entry entry) throws Exception {
        String icon = getProperty("icon", (String) null);
        if (icon != null) {
            return iconUrl(icon);
        }
        return super.getIconUrl(request, entry);
    }

    /**
     * _more_
     *
     * @param entryNode _more_
     *
     * @throws Exception _more_
     */
    protected void init(Element entryNode) throws Exception {
        super.init(entryNode);
        if (getType().indexOf(".") >= 0) {
        }
        setDefaultDataType(XmlUtil.getAttribute(entryNode, ATTR_DATATYPE, (String) null));
        List columnNodes = XmlUtil.findChildren(entryNode, TAG_COLUMN);
        if (columnNodes.size() == 0) {
            return;
        }
        init((List<Element>) columnNodes);
    }

    /**
     * _more_
     *
     * @param columnNodes _more_
     *
     * @throws Exception _more_
     */
    public void init(List<Element> columnNodes) throws Exception {
        Statement statement = getDatabaseManager().createStatement();
        colNames.add(COL_ID);
        StringBuffer tableDef = new StringBuffer("CREATE TABLE " + getTableName() + " (\n");
        tableDef.append(COL_ID + " varchar(200))");
        try {
            getDatabaseManager().executeAndClose(tableDef.toString());
        } catch (Throwable exc) {
            if (exc.toString().indexOf("already exists") < 0) {
            }
        }
        StringBuffer indexDef = new StringBuffer();
        indexDef.append("CREATE INDEX " + getTableName() + "_INDEX_" + COL_ID + "  ON " + getTableName() + " (" + COL_ID + ");\n");
        try {
            getDatabaseManager().loadSql(indexDef.toString(), true, false);
        } catch (Throwable exc) {
        }
        int valuesOffset = getValuesOffset();
        for (int colIdx = 0; colIdx < columnNodes.size(); colIdx++) {
            Element columnNode = (Element) columnNodes.get(colIdx);
            String className = XmlUtil.getAttribute(columnNode, ATTR_CLASS, Column.class.getName());
            Class c = Misc.findClass(className);
            Constructor ctor = Misc.findConstructor(c, new Class[] { getClass(), Element.class, Integer.TYPE });
            Column column = (Column) ctor.newInstance(new Object[] { this, columnNode, new Integer(valuesOffset + colNames.size() - 1) });
            columns.add(column);
            colNames.addAll(column.getColumnNames());
            column.createTable(statement);
        }
        getDatabaseManager().closeAndReleaseConnection(statement);
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public List<String> getColumnNames() {
        return colNames;
    }

    public List<Column> getMyColumns() {
        return columns;
    }

    /**
     * _more_
     *
     * @return _more_
     */
    @Override
    public List<Column> getColumns() {
        List<Column> allColumns = new ArrayList<Column>();
        if (getParent() != null) {
            List<Column> parentColumns = getParent().getColumns();
            allColumns.addAll(parentColumns);
        }
        if (columns != null) {
            allColumns.addAll(columns);
        }
        return allColumns;
    }

    /**
     * _more_
     *
     * @param entry _more_
     *
     * @return _more_
     */
    public String getCategory(Entry entry) {
        for (Column column : columns) {
            if (column.getName().equals("category")) {
                Object[] values = entry.getValues();
                if (values != null) {
                    String s = column.getString(values);
                    if (s != null) {
                        return s;
                    }
                    break;
                }
            }
        }
        return super.getCategory(entry);
    }

    /**
     * _more_
     *
     * @param columnName _more_
     *
     * @return _more_
     */
    public Column findColumn(String columnName) {
        for (Column column : columns) {
            if (column.getName().equals(columnName)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Could not find column:" + columnName);
    }

    /**
     * _more_
     *
     * @param map _more_
     *
     * @return _more_
     */
    public Object[] makeValues(Hashtable map) {
        Object[] values = makeEntryValueArray();
        int idx = 0;
        for (Column column : columns) {
            Object data = map.get(column.getName());
            values[idx] = data;
            idx++;
        }
        return values;
    }

    /**
     * _more_
     *
     * @param columnName _more_
     * @param value _more_
     *
     * @return _more_
     */
    public Object convert(String columnName, String value) {
        Column column = findColumn(columnName);
        return column.convert(value);
    }

    /**
     * _more_
     *
     * @return _more_
     */
    private boolean haveDatabaseTable() {
        return colNames.size() > 0;
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public int getNumberOfMyValues() {
        if (!haveDatabaseTable()) {
            return 0;
        }
        return colNames.size() - 1;
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public Object[] makeEntryValueArray() {
        int numberOfValues = getTotalNumberOfValues();
        return new Object[numberOfValues];
    }

    /**
     * _more_
     *
     * @param entry _more_
     *
     * @return _more_
     */
    public Object[] getEntryValues(Entry entry) {
        Object[] values = entry.getValues();
        if (values == null) {
            values = makeEntryValueArray();
            entry.setValues(values);
        }
        return values;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     * @param parent _more_
     * @param newEntry _more_
     *
     * @throws Exception _more_
     */
    public void initializeEntryFromForm(Request request, Entry entry, Entry parent, boolean newEntry) throws Exception {
        Object[] values = getEntryValues(entry);
        if (haveDatabaseTable()) {
            for (Column column : columns) {
                column.setValue(request, entry, values);
            }
        }
        super.initializeEntryFromForm(request, entry, parent, newEntry);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     * @param node _more_
     *
     * @throws Exception _more_
     */
    public void initializeEntryFromXml(Request request, Entry entry, Element node) throws Exception {
        Object[] values = getEntryValues(entry);
        super.initializeEntryFromXml(request, entry, node);
        Hashtable<String, Element> nodes = new Hashtable<String, Element>();
        NodeList elements = XmlUtil.getElements(node);
        for (int i = 0; i < elements.getLength(); i++) {
            Element child = (Element) elements.item(i);
            nodes.put(child.getTagName(), child);
        }
        for (Column column : columns) {
            String value = null;
            Element child = nodes.get(column.getName());
            if (child != null) {
                value = XmlUtil.getChildText(child);
                if (XmlUtil.getAttribute(child, "encoded", false)) {
                    value = new String(RepositoryUtil.decodeBase64(value));
                }
            }
            if (value == null) {
                value = XmlUtil.getAttribute(node, column.getName(), (String) null);
            }
            if (value == null) {
                continue;
            }
            column.setValue(entry, values, value);
        }
    }

    /**
     * _more_
     *
     * @param arg _more_
     * @param value _more_
     * @param entry _more_
     *
     * @return _more_
     */
    public int matchValue(String arg, Object value, Entry entry) {
        for (Column column : columns) {
            int match = column.matchValue(arg, value, ((entry == null) ? null : entry.getValues()));
            if (match == MATCH_FALSE) {
                return MATCH_FALSE;
            }
            if (match == MATCH_TRUE) {
                return MATCH_TRUE;
            }
        }
        return MATCH_UNKNOWN;
    }

    /**
     * _more_
     *
     * @param longName _more_
     *
     * @return _more_
     */
    public List<TwoFacedObject> getListTypes(boolean longName) {
        List<TwoFacedObject> list = super.getListTypes(longName);
        for (Column column : columns) {
            if (column.getCanList()) {
                list.add(new TwoFacedObject((longName ? (getDescription() + " - ") : "") + column.getDescription(), column.getFullName()));
            }
        }
        return list;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param what _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Result processList(Request request, String what) throws Exception {
        Column theColumn = null;
        for (Column column : columns) {
            if (column.getCanList() && column.getFullName().equals(what)) {
                theColumn = column;
                break;
            }
        }
        if (theColumn == null) {
            return super.processList(request, what);
        }
        String column = theColumn.getFullName();
        String tag = theColumn.getName();
        String title = theColumn.getDescription();
        List<Clause> where = assembleWhereClause(request);
        Statement statement = select(request, SqlUtil.distinct(column), where, "");
        String[] values = SqlUtil.readString(getDatabaseManager().getIterator(statement), 1);
        StringBuffer sb = new StringBuffer();
        OutputType output = request.getOutput();
        if (output.equals(OutputHandler.OUTPUT_HTML)) {
            sb.append(RepositoryUtil.header(title));
            sb.append("<ul>");
        } else if (output.equals(XmlOutputHandler.OUTPUT_XML)) {
            sb.append(XmlUtil.XML_HEADER + "\n");
            sb.append(XmlUtil.openTag(tag + "s"));
        }
        Properties properties = repository.getFieldProperties(theColumn.getPropertiesFile());
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                continue;
            }
            String longName = theColumn.getLabel(values[i]);
            if (output.equals(OutputHandler.OUTPUT_HTML)) {
                sb.append("<li>");
                sb.append(longName);
            } else if (output.equals(XmlOutputHandler.OUTPUT_XML)) {
                String attrs = XmlUtil.attrs(ATTR_ID, values[i]);
                if (properties != null) {
                    for (Enumeration keys = properties.keys(); keys.hasMoreElements(); ) {
                        String key = (String) keys.nextElement();
                        if (key.startsWith(values[i] + ".")) {
                            String value = (String) properties.get(key);
                            value = value.replace("${value}", values[i]);
                            key = key.substring((values[i] + ".").length());
                            attrs = attrs + XmlUtil.attr(key, value);
                        }
                    }
                }
                sb.append(XmlUtil.tag(tag, attrs));
            } else if (output.equals(CsvOutputHandler.OUTPUT_CSV)) {
                sb.append(SqlUtil.comma(values[i], longName));
                sb.append("\n");
            }
        }
        if (output.equals(OutputHandler.OUTPUT_HTML)) {
            sb.append("</ul>");
        } else if (output.equals(XmlOutputHandler.OUTPUT_XML)) {
            sb.append(XmlUtil.closeTag(tag + "s"));
        }
        return new Result(title, sb, repository.getOutputHandler(request).getMimeType(output));
    }

    /**
     * _more_
     *
     * @param obj _more_
     *
     * @return _more_
     */
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param statement _more_
     * @param entry _more_
     *
     * @throws Exception _more_
     */
    public void deleteEntry(Request request, Statement statement, Entry entry) throws Exception {
        deleteEntryFromDatabase(request, statement, entry.getId());
        super.deleteEntry(request, statement, entry);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param statement _more_
     * @param id _more_
     *
     * @throws Exception _more_
     */
    public void deleteEntry(Request request, Statement statement, String id) throws Exception {
        deleteEntryFromDatabase(request, statement, id);
        super.deleteEntry(request, statement, id);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param statement _more_
     * @param id _more_
     *
     * @throws Exception _more_
     */
    private void deleteEntryFromDatabase(Request request, Statement statement, String id) throws Exception {
        if (!haveDatabaseTable()) {
            return;
        }
        String query = SqlUtil.makeDelete(getTableName(), COL_ID, SqlUtil.quote(id));
        statement.execute(query);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param searchCriteria _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public List<Clause> assembleWhereClause(Request request, StringBuffer searchCriteria) throws Exception {
        List<Clause> where = super.assembleWhereClause(request, searchCriteria);
        int originalSize = where.size();
        for (Column column : columns) {
            if (!column.getCanSearch()) {
                continue;
            }
            column.assembleWhereClause(request, where, searchCriteria);
        }
        if ((originalSize != where.size()) && (originalSize > 0)) {
            where.add(Clause.join(Tables.ENTRIES.COL_ID, getTableName() + ".id"));
        }
        return where;
    }

    /**
     * _more_
     *
     *
     * @param isNew _more_
     * @param typeInserts _more_
     */
    public void getInsertSql(boolean isNew, List<TypeInsertInfo> typeInserts) {
        super.getInsertSql(isNew, typeInserts);
        if (!haveDatabaseTable()) {
            return;
        }
        if (isNew) {
            typeInserts.add(new TypeInsertInfo(this, SqlUtil.makeInsert(getTableName(), SqlUtil.comma(colNames), SqlUtil.getQuestionMarks(colNames.size()))));
        } else {
            typeInserts.add(new TypeInsertInfo(this, SqlUtil.makeUpdate(getTableName(), COL_ID, StringUtil.listToStringArray(colNames))));
        }
    }

    /**
     * _more_
     *
     * @param entry _more_
     * @param stmt _more_
     * @param isNew _more_
     *
     * @throws Exception _more_
     */
    public void setStatement(Entry entry, PreparedStatement stmt, boolean isNew) throws Exception {
        super.setStatement(entry, stmt, isNew);
        setStatement(entry, entry.getValues(), stmt, isNew);
    }

    /**
     * _more_
     *
     * @param entry _more_
     * @param values _more_
     * @param stmt _more_
     * @param isNew _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public int setStatement(Entry entry, Object[] values, PreparedStatement stmt, boolean isNew) throws Exception {
        int stmtIdx = 1;
        stmt.setString(stmtIdx++, entry.getId());
        if (values != null) {
            for (Column column : columns) {
                stmtIdx = column.setValues(stmt, values, stmtIdx);
            }
        }
        if (!isNew) {
            stmt.setString(stmtIdx, entry.getId());
            stmtIdx++;
        }
        return stmtIdx;
    }

    /**
     * _more_
     *
     * @param entry _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public void initializeEntryFromDatabase(Entry entry) throws Exception {
        Object[] values = getEntryValues(entry);
        super.initializeEntryFromDatabase(entry);
        if (!haveDatabaseTable()) {
            return;
        }
        getValues(Clause.eq(COL_ID, entry.getId()), values);
    }

    /**
     * _more_
     *
     * @param clause _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Object[] getValues(Clause clause) throws Exception {
        return getValues(clause, makeEntryValueArray());
    }

    /**
     * _more_
     *
     * @param clause _more_
     * @param values _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public Object[] getValues(Clause clause, Object[] values) throws Exception {
        Statement stmt = getDatabaseManager().select(SqlUtil.comma(colNames), getTableName(), clause);
        try {
            ResultSet results2 = stmt.getResultSet();
            if (results2.next()) {
                int valueIdx = 2;
                for (Column column : columns) {
                    valueIdx = column.readValues(results2, values, valueIdx);
                }
            }
        } finally {
            getDatabaseManager().closeAndReleaseConnection(stmt);
        }
        return values;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     * @param column _more_
     * @param tmpSb _more_
     * @param values _more_
     *
     * @throws Exception _more_
     */
    public void formatColumnHtmlValue(Request request, Entry entry, Column column, StringBuffer tmpSb, Object[] values) throws Exception {
        column.formatValue(entry, tmpSb, Column.OUTPUT_HTML, values);
    }

    /**
     * _more_
     *
     * @param entry _more_
     * @param sb _more_
     *
     * @throws Exception _more_
     */
    public void getTextCorpus(Entry entry, StringBuffer sb) throws Exception {
        super.getTextCorpus(entry, sb);
    }

    /**
     * _more_
     *
     * @param requst _more_
     * @param entry _more_
     * @param output _more_
     *
     * @return _more_
     */
    public boolean shouldShowInHtml(Request requst, Entry entry, OutputType output) {
        return output.equals(OutputHandler.OUTPUT_HTML);
    }

    /**
     *
     *
     * @param request _more_
     * @param entry _more_
     * @param name _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    @Override
    public String getFieldHtml(Request request, Entry entry, String name) throws Exception {
        Object[] values = entry.getValues();
        if (values != null) {
            for (Column column : getColumns()) {
                if (column.isField(name)) {
                    StringBuffer tmpSB = new StringBuffer();
                    formatColumnHtmlValue(request, entry, column, tmpSB, values);
                    return tmpSB.toString();
                }
            }
        }
        return super.getFieldHtml(request, entry, name);
    }

    /**
     * _more_
     *
     * @param entry _more_
     * @param request _more_
     * @param output _more_
     * @param showDescription _more_
     * @param showResource _more_
     * @param linkToDownload _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    public StringBuffer getInnerEntryContent(Entry entry, Request request, OutputType output, boolean showDescription, boolean showResource, boolean linkToDownload) throws Exception {
        StringBuffer sb = super.getInnerEntryContent(entry, request, output, showDescription, showResource, linkToDownload);
        if (true) {
            Object[] values = entry.getValues();
            if (values != null) {
                for (Column column : getMyColumns()) {
                    if (!column.getCanShow()) {
                        continue;
                    }
                    StringBuffer tmpSb = new StringBuffer();
                    formatColumnHtmlValue(request, entry, column, tmpSb, values);
                    sb.append(HtmlUtil.formEntry(column.getLabel() + ":", tmpSb.toString()));
                }
            }
        } else if (output.equals(XmlOutputHandler.OUTPUT_XML)) {
        }
        return sb;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param entry _more_
     * @param html _more_
     *
     * @return _more_
     *
     * @throws Exception _more_
     */
    protected String processDisplayTemplate(Request request, Entry entry, String html) throws Exception {
        html = super.processDisplayTemplate(request, entry, html);
        Object[] values = entry.getValues();
        OutputType output = request.getOutput();
        if (values != null) {
            for (Column column : columns) {
                StringBuffer tmpSb = new StringBuffer();
                column.formatValue(entry, tmpSb, Column.OUTPUT_HTML, values);
                html = html.replace("${" + column.getName() + ".content}", tmpSb.toString());
                html = html.replace("${" + column.getName() + ".label}", column.getLabel());
            }
        }
        return html;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param initTables _more_
     *
     * @return _more_
     */
    protected List getTablesForQuery(Request request, List initTables) {
        super.getTablesForQuery(request, initTables);
        for (Column column : columns) {
            if (!column.getCanSearch()) {
                continue;
            }
            if (request.defined(column.getFullName())) {
                initTables.add(getTableName());
                break;
            }
        }
        return initTables;
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public String getTableName() {
        String typeName = getType();
        return typeName;
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param formBuffer _more_
     * @param entry _more_
     *
     * @throws Exception _more_
     */
    public void addSpecialToEntryForm(Request request, StringBuffer formBuffer, Entry entry) throws Exception {
        super.addSpecialToEntryForm(request, formBuffer, entry);
        addColumnsToEntryForm(request, formBuffer, entry);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param formBuffer _more_
     * @param entry _more_
     *
     * @throws Exception _more_
     */
    public void addColumnsToEntryForm(Request request, StringBuffer formBuffer, Entry entry) throws Exception {
        addColumnsToEntryForm(request, formBuffer, entry, ((entry == null) ? null : entry.getValues()));
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param formBuffer _more_
     * @param entry _more_
     * @param values _more_
     *
     * @throws Exception _more_
     */
    public void addColumnsToEntryForm(Request request, StringBuffer formBuffer, Entry entry, Object[] values) throws Exception {
        Hashtable state = new Hashtable();
        for (Column column : columns) {
            addColumnToEntryForm(request, column, formBuffer, entry, values, state);
        }
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param column _more_
     * @param formBuffer _more_
     * @param entry _more_
     * @param values _more_
     * @param state _more_
     *
     * @throws Exception _more_
     */
    public void addColumnToEntryForm(Request request, Column column, StringBuffer formBuffer, Entry entry, Object[] values, Hashtable state) throws Exception {
        column.addToEntryForm(request, entry, formBuffer, values, state);
    }

    /**
     * _more_
     *
     * @param entry _more_
     * @param node _more_
     *
     * @throws Exception _more_
     */
    public void addToEntryNode(Entry entry, Element node) throws Exception {
        super.addToEntryNode(entry, node);
        if (!haveDatabaseTable()) {
            return;
        }
        Object[] values = getEntryValues(entry);
        for (Column column : columns) {
            column.addToEntryNode(entry, values, node);
        }
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param formBuffer _more_
     *
     * @throws Exception _more_
     */
    public void addToSpecialSearchForm(Request request, StringBuffer formBuffer) throws Exception {
        super.addToSpecialSearchForm(request, formBuffer);
        addColumnsToSearchForm(request, formBuffer, new ArrayList<Clause>(), true, false);
    }

    /**
     * _more_
     *
     * @param formBuffer _more_
     * @param request _more_
     * @param where _more_
     * @param advancedForm _more_
     *
     * @throws Exception _more_
     */
    public void addToSearchForm(Request request, StringBuffer formBuffer, List<Clause> where, boolean advancedForm) throws Exception {
        super.addToSearchForm(request, formBuffer, where, advancedForm);
        addColumnsToSearchForm(request, formBuffer, where, advancedForm, true);
    }

    /**
     * _more_
     *
     * @param request _more_
     * @param formBuffer _more_
     * @param where _more_
     * @param advancedForm _more_
     * @param makeToggleBox _more_
     *
     * @throws Exception _more_
     */
    public void addColumnsToSearchForm(Request request, StringBuffer formBuffer, List<Clause> where, boolean advancedForm, boolean makeToggleBox) throws Exception {
        StringBuffer typeSB = (makeToggleBox ? new StringBuffer() : formBuffer);
        for (Column column : columns) {
            column.addToSearchForm(request, typeSB, where);
        }
        if (makeToggleBox && (typeSB.toString().length() > 0)) {
            typeSB = new StringBuffer(HtmlUtil.formTable() + typeSB + HtmlUtil.formTableClose());
            formBuffer.append(HtmlUtil.p());
            formBuffer.append(HtmlUtil.makeShowHideBlock(msg(getLabel()), typeSB.toString(), true));
        }
    }
}
