package de.mguennewig.pobjimport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.*;
import javax.xml.parsers.*;
import de.mguennewig.pobjects.*;
import de.mguennewig.pobjects.metadata.*;
import de.mguennewig.pobjtool.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * A parser for the XML file that defines a PObject database content.
 *
 * @author Michael G�nnewig
 */
public final class Importer extends AbstractParser {

    private static final String INVALID_ATTR_VALUE = "invalidAttributeValue";

    private static final HashMap<String, Integer> OPERATORS = new HashMap<String, Integer>(16);

    static {
        OPERATORS.put("eq", Integer.valueOf(Predicate.EQUALS));
        OPERATORS.put("ne", Integer.valueOf(Predicate.NOT_EQUALS));
        OPERATORS.put("lt", Integer.valueOf(Predicate.LESS));
        OPERATORS.put("le", Integer.valueOf(Predicate.LESS_EQUALS));
        OPERATORS.put("gt", Integer.valueOf(Predicate.GREATER));
        OPERATORS.put("ge", Integer.valueOf(Predicate.GREATER_EQUALS));
        OPERATORS.put("in", Integer.valueOf(Predicate.IN));
        OPERATORS.put("and", Integer.valueOf(Predicate.AND));
        OPERATORS.put("or", Integer.valueOf(Predicate.OR));
        OPERATORS.put("not", Integer.valueOf(Predicate.NOT));
        OPERATORS.put("isNull", Integer.valueOf(Predicate.IS_NULL));
        OPERATORS.put("isNotNull", Integer.valueOf(Predicate.IS_NOT_NULL));
        OPERATORS.put("like", Integer.valueOf(Predicate.LIKE));
    }

    private final Container db;

    private final Map<String, Record> objects;

    /** Creates a new Importer. */
    public Importer(final Container db) {
        super();
        this.db = db;
        this.objects = new HashMap<String, Record>();
    }

    public void importFile(final String filename) throws IOException, ParserException, PObjException {
        importFile(new File(filename));
    }

    public void importFile(final File file) throws IOException, ParserException, PObjException {
        final DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder parser = domFactory.newDocumentBuilder();
            objects.clear();
            parsePObjectData(parser.parse(file));
        } catch (SAXException e) {
            throw new ParserException(e, "ioError", e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    private void parsePObjectData(final Document doc) throws ParserException, PObjConstraintException, PObjSQLException {
        NodeList nodeList;
        final Element rootNode = doc.getDocumentElement();
        if (!"pobjects_data".equals(rootNode.getTagName())) throw new ParserException(rootNode, "expectedElement", "pobjects_data");
        nodeList = NodeContainer.getChildrenByTagName(rootNode, "query");
        for (int i = 0; i < nodeList.getLength(); i++) parseQuery((Element) nodeList.item(i));
        nodeList = NodeContainer.getChildrenByTagName(rootNode, "table");
        for (int i = 0; i < nodeList.getLength(); i++) parseTable((Element) nodeList.item(i));
    }

    private void parseTable(final Element tableNode) throws ParserException, PObjConstraintException, PObjSQLException {
        final String name = getIdentifier(tableNode, "name", null);
        final TableExpr te = db.getDictionary().getTableExpr(name);
        if (te == null) throw new ParserException(tableNode, "noSuchTable", name);
        if (!(te instanceof ClassDecl)) throw new ParserException(tableNode, "tableNotPersistent", name);
        final NodeList nodeList = NodeContainer.getChildrenByTagName(tableNode, "entry");
        for (int i = 0; i < nodeList.getLength(); i++) parseEntry((Element) nodeList.item(i), (ClassDecl) te);
    }

    private Record parseEntry(final Element entryNode, final ClassDecl te) throws ParserException, PObjConstraintException, PObjSQLException {
        final NodeList nodeList = NodeContainer.getChildrenByTagName(entryNode, "field");
        final Record record = db.createObject(te);
        if (record instanceof PObject) db.makePersistent((PObject) record);
        if (entryNode.hasAttribute("id")) {
            final String id = getIdentifier(entryNode, "id", null);
            if (objects.containsKey(id)) throw new ParserException(entryNode, "idAlreadyDefined", id);
            objects.put(id, record);
        }
        for (int i = 0; i < nodeList.getLength(); i++) parseField((Element) nodeList.item(i), te, record);
        if (record instanceof PObject) ((PObject) record).store(); else if (!db.insertRecord(record)) throw new PObjSQLException("Failed to import entry `" + te.getName() + "'");
        return record;
    }

    private static byte[] decodeHex(final char[] data) {
        final int len = data.length;
        if ((len & 0x1) != 0) throw new IllegalArgumentException("Odd number of characters");
        final byte[] out = new byte[len >> 1];
        for (int i = 0, j = 0; j < len; i++) {
            int f = Character.digit(data[j++], 16) << 4;
            f |= Character.digit(data[j++], 16);
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    private static Type unwrapTypeProxy(final Type type) {
        final Type t = TypeRef.resolve(type);
        if (Proxy.isProxyClass(t.getClass())) {
            final InvocationHandler handler = Proxy.getInvocationHandler(t);
            if (handler instanceof TypeClassHandler) return ((TypeClassHandler) handler).getType();
        }
        return t;
    }

    private void parseField(final Element fieldNode, final ClassDecl te, final Record record) throws ParserException, PObjSQLException {
        final String name = getIdentifier(fieldNode, "name", null);
        final Column field = te.getColumnByName(name);
        if (field == null) throw new ParserException(fieldNode, "noSuchField", name);
        final Type type = unwrapTypeProxy(field.getType());
        Object value = null;
        final Node firstChild = fieldNode.getFirstChild();
        if (type instanceof ReferenceToClass) {
            if (firstChild != null) throw new ParserException(fieldNode, "expectedEmptyElement");
            if (fieldNode.hasAttribute("target")) {
                final String targetId = getIdentifier(fieldNode, "target", null);
                if (!objects.containsKey(targetId)) throw new ParserException(fieldNode, "noSuchId", targetId);
                value = objects.get(targetId);
            }
        } else if ((type instanceof BlobType) && (firstChild != null)) {
            value = firstChild.getNodeValue();
            value = new PObjBlob(decodeHex(value.toString().toCharArray()));
            try {
                if (type instanceof ObjectType) value = ((ObjectType) type).valueOf((PObjBlob) value);
            } catch (SQLException e) {
                throw new PObjSQLException(e);
            }
        } else if (firstChild != null) {
            value = firstChild.getNodeValue();
            try {
                value = type.parse((String) value);
            } catch (PObjSyntaxException e) {
                throw new ParserException(fieldNode, "invalidValue", value);
            }
        }
        record.set(field, value);
    }

    private Record parseQuery(final Element queryNode) throws ParserException {
        final String id = getIdentifier(queryNode, "id", null);
        if (objects.containsKey(id)) throw new ParserException(queryNode, "idAlreadyDefined", id);
        final Query q = db.newQuery();
        final String tableName = getIdentifier(queryNode, "table", null);
        final TableExpr te = db.getDictionary().getTableExpr(tableName);
        if (te == null) throw new ParserException(queryNode, "noSuchTable", tableName);
        if (!(te instanceof ClassDecl)) throw new ParserException(queryNode, "tableNotPersistent", tableName);
        q.addTableExpr(te, false);
        final NodeList nodeList = NodeContainer.getChildrenByTagName(queryNode, "predicate");
        if (nodeList.getLength() < 0) expectCount(queryNode, nodeList, 1, "expectedElement", "predicate");
        for (int i = 0; i < nodeList.getLength(); i++) q.addConj(parsePredicate((Element) nodeList.item(i), (ClassDecl) te));
        final Record[] result = q.execute()[0];
        if (result.length != 1) throw new ParserException(queryNode, "queryNotUnique", id);
        objects.put(id, result[0]);
        return result[0];
    }

    private Predicate parsePredicate(final Element predicateNode, final ClassDecl te) throws ParserException {
        NodeList nodeList;
        final String operator = getIdentifier(predicateNode, "operator", null);
        if (!OPERATORS.containsKey(operator)) {
            throw new ParserException(predicateNode, INVALID_ATTR_VALUE, "operator");
        }
        final int opId = OPERATORS.get(operator).intValue();
        nodeList = NodeContainer.getChildrenByTagName(predicateNode, "*");
        switch(opId) {
            case Predicate.EQUALS:
            case Predicate.NOT_EQUALS:
            case Predicate.LESS:
            case Predicate.LESS_EQUALS:
            case Predicate.GREATER:
            case Predicate.GREATER_EQUALS:
            case Predicate.LIKE:
            case Predicate.IN:
            case Predicate.AND:
            case Predicate.OR:
                expectCount(predicateNode, nodeList, 2, "exptectTerm");
                final Term left = parseTerm((Element) nodeList.item(0), te);
                final Term right = parseTerm((Element) nodeList.item(1), te);
                if ((opId == Predicate.IN) && !(right instanceof LiteralSet)) {
                    throw new ParserException(nodeList.item(1), "expectedElement", "literalSet");
                }
                if ((opId == Predicate.LIKE) && !(right instanceof Literal)) {
                    throw new ParserException(nodeList.item(1), "expectedElement", "literal");
                }
                return new Predicate(left, opId, right);
            case Predicate.NOT:
                expectCount(predicateNode, nodeList, 1, "expectTerm");
                Term term = parseTerm((Element) nodeList.item(0), te);
                if (term instanceof FctCall) {
                    return Predicate.not(term);
                } else if (term instanceof Predicate) {
                    return Predicate.not(term);
                } else {
                    throw new ParserException(predicateNode, "unexpectedElement", nodeList.item(0).getNodeName());
                }
            case Predicate.IS_NULL:
            case Predicate.IS_NOT_NULL:
                expectCount(predicateNode, nodeList, 1, "expectTerm");
                term = parseTerm((Element) nodeList.item(0), te);
                return new Predicate(term, opId, null);
            default:
                throw new Error("Unhandled predicate operator value " + operator);
        }
    }

    private Term parseTerm(final Element termNode, final ClassDecl te) throws ParserException {
        final String tagName = termNode.getTagName();
        if ("fctCall".equals(tagName)) return parseFctCall(termNode, te); else if ("literal".equals(tagName)) return parseLiteral(termNode); else if ("literalSet".equals(tagName)) return parseLiteralSet(termNode); else if ("member".equals(tagName)) return parseMember(termNode, te); else if ("predicate".equals(tagName)) return parsePredicate(termNode, te); else throw new ParserException(termNode, "noSuchTerm", tagName);
    }

    private FctCall parseFctCall(final Element fctNode, final ClassDecl te) throws ParserException {
        final NodeList nodeList = NodeContainer.getChildrenByTagName(fctNode, "*");
        final Term[] args = new Term[nodeList.getLength()];
        final String name = getIdentifier(fctNode, "name", null);
        for (int i = 0; i < nodeList.getLength(); i++) args[i] = parseTerm((Element) nodeList.item(i), te);
        return new FctCall(name, args);
    }

    /** Parses a <code>member</code> node.
   *
   * <p>A <code>member</code> node refers to a field of the table to which
   * the {@link CheckConstraint check constraint} in which this {@link Member}
   * occurs belongs.</p>
   */
    private static Member parseMember(final Element memberNode, final ClassDecl cd) throws ParserException {
        final String[] columnNames = getAttribute(memberNode, "columns", null).split("\\s+");
        final Column[] columns = new Column[columnNames.length];
        ClassDecl te = cd;
        for (int i = 0; i < columnNames.length; i++) {
            final Column column = te.getColumnByName(columnNames[i]);
            if (column == null) throw new ParserException(memberNode, "noSuchField", columnNames[i]);
            columns[i] = column;
            final Type type = column.getType();
            if (type instanceof ReferenceToClass) te = ((ReferenceToClass) type).getTargetClass();
        }
        return new Member(0, columns);
    }

    /** Parses a <code>literal</code> node. */
    private Literal parseLiteral(final Element literalNode) throws ParserException {
        final String type = getIdentifier(literalNode, "type", null);
        final String value = getAttribute(literalNode, "value", null);
        try {
            if ("boolean".equals(type)) {
                return new Literal(new BooleanType().parse(value));
            } else if ("double".equals(type)) {
                return new Literal(new DoubleType().parse(value));
            } else if ("int32".equals(type)) {
                return new Literal(new Int32Type().parse(value));
            } else if ("isrc".equals(type)) {
                return new Literal(new ISRCType().parse(value));
            } else if ("numeric".equals(type)) {
                final int precision = value.length();
                return new Literal(new NumericType(precision, precision).parse(value));
            } else if ("timeStamp".equals(type)) {
                return new Literal(new TimeStampType(value.length()).parse(value));
            } else if ("string".equals(type)) {
                return new Literal(value);
            } else {
                throw new ParserException(literalNode, INVALID_ATTR_VALUE, "type");
            }
        } catch (NumberFormatException e) {
            throw new ParserException(literalNode, INVALID_ATTR_VALUE, "value");
        } catch (PObjSyntaxException e) {
            throw new ParserException(literalNode, INVALID_ATTR_VALUE, "value");
        }
    }

    /** Parses a <code>literalSet</code> node.
   *
   * @see #parseLiteral
   */
    private LiteralSet parseLiteralSet(final Element literalSetNode) throws ParserException {
        final NodeList nodeList = NodeContainer.getChildrenByTagName(literalSetNode, "literal");
        final Object[] values = new Object[nodeList.getLength()];
        if (values.length == 0) throw new ParserException(literalSetNode, "expectedElement", "literal");
        for (int i = 0; i < values.length; i++) values[i] = parseLiteral((Element) nodeList.item(i)).getValue();
        return new LiteralSet(values);
    }
}
