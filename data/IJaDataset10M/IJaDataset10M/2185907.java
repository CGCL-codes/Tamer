package org.dbwiki.data.io;

import java.util.Stack;
import java.util.Vector;
import org.dbwiki.data.schema.AttributeSchemaNode;
import org.dbwiki.data.schema.DatabaseSchema;
import org.dbwiki.data.schema.GroupSchemaNode;
import org.dbwiki.exception.WikiException;

/** An XML parser that scans the document and generates a schema
 * Currently, the schema is produced as a string and then eventually re-parsed by SchemaParser.
 * @author jcheney
 *
 */
public class StructureParser implements InputHandler {

    private class ElementNode {

        private Vector<ElementNode> _children;

        private String _label;

        public ElementNode(String label) {
            _label = label;
            _children = new Vector<ElementNode>();
        }

        public Vector<ElementNode> children() {
            return _children;
        }

        public String label() {
            return _label;
        }

        /** Slightly hacky function to get the node associated with a given path, to use as the root. 
		 *  
		 * 	@param path
		 * @return
		 */
        public ElementNode get(String path) {
            if (path.startsWith("/" + _label)) {
                int idx = path.indexOf('/', 1);
                if (idx == -1) {
                    return this;
                } else {
                    for (int iChild = 0; iChild < children().size(); iChild++) {
                        ElementNode child = children().get(iChild).get(path.substring(idx));
                        if (child != null) {
                            return child;
                        }
                    }
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private Stack<ElementNode> _elementStack = null;

    private ElementNode _root;

    private Exception _exception = null;

    public void startDocument() {
        _exception = null;
        _root = null;
        _elementStack = new Stack<ElementNode>();
    }

    public void endDocument() {
    }

    public void startElement(String label) {
        if (_root == null) {
            _root = new ElementNode(label);
            _elementStack.push(_root);
        } else {
            ElementNode currentElement = _elementStack.peek();
            ElementNode child = null;
            for (int iChild = 0; iChild < currentElement.children().size(); iChild++) {
                if (currentElement.children().get(iChild).label().equals(label)) {
                    child = currentElement.children().get(iChild);
                    break;
                }
            }
            if (child == null) {
                child = new ElementNode(label);
                currentElement.children().add(child);
            }
            _elementStack.push(child);
        }
    }

    public void startElement(String label, Attribute[] attrs) throws org.dbwiki.exception.WikiException {
        this.startElement(label);
        for (int iAttribute = 0; iAttribute < attrs.length; iAttribute++) {
            Attribute attribute = attrs[iAttribute];
            this.startElement(attribute.name());
            this.text(attribute.value().toCharArray());
            this.endElement(attribute.name());
        }
    }

    public void endElement(String label) {
        _elementStack.pop();
    }

    public void text(char[] value) {
    }

    public void exception(Exception excpt) {
        _exception = excpt;
    }

    public boolean hasException() {
        return (_exception != null);
    }

    public Exception getException() {
        return _exception;
    }

    public DatabaseSchema getDatabaseSchema(String path) throws WikiException {
        ElementNode node = _root.get(path);
        return buildRoot(node);
    }

    public DatabaseSchema getDatabaseSchema() throws WikiException {
        return buildRoot(_root);
    }

    private void buildSchema(DatabaseSchema schema, ElementNode node, GroupSchemaNode parent) throws WikiException {
        if (node.children().size() > 0) {
            GroupSchemaNode schemaNode = new GroupSchemaNode(schema.size(), node.label(), parent);
            schema.add(schemaNode);
            for (int i = 0; i < node.children().size(); i++) {
                this.buildSchema(schema, node.children().get(i), schemaNode);
            }
        } else {
            AttributeSchemaNode schemaNode = new AttributeSchemaNode(schema.size(), node.label(), parent);
            schema.add(schemaNode);
        }
    }

    private DatabaseSchema buildRoot(ElementNode node) throws WikiException {
        DatabaseSchema schema = new DatabaseSchema();
        GroupSchemaNode rootSchemaNode = new GroupSchemaNode(schema.size(), node.label(), null);
        schema.add(rootSchemaNode);
        for (int i = 0; i < node.children().size(); i++) {
            buildSchema(schema, node.children().get(i), rootSchemaNode);
        }
        return schema;
    }
}
