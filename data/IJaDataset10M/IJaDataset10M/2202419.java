package org.wikiup.modules.worms.imp.relatives;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.wikiup.core.impl.Null;
import org.wikiup.core.impl.attribute.AttributeImpl;
import org.wikiup.core.impl.context.XPathContext;
import org.wikiup.core.impl.getter.dl.ByAttributeNameSelector;
import org.wikiup.core.inf.Attribute;
import org.wikiup.core.inf.Document;
import org.wikiup.core.inf.Getter;
import org.wikiup.core.util.Assert;
import org.wikiup.core.util.ValueUtil;
import org.wikiup.modules.worms.WormsEntity;
import org.wikiup.modules.worms.WormsEntityRelativesFactory;
import org.wikiup.modules.worms.imp.component.Property;
import org.wikiup.modules.worms.imp.iterator.ResultSetIterator;

public abstract class ResultSetRelatives extends EntityRelatives {

    private Map<String, Attribute> fields = new HashMap<String, Attribute>();

    private ResultSet resultSet;

    private Getter<Document> relatives;

    public void init(Document data, WormsEntity entity) {
        relatives = new ByAttributeNameSelector(data, "name", "relation");
        setEntity(entity);
    }

    public void setResultSet(ResultSet result) {
        try {
            ResultSetMetaData meta = result.getMetaData();
            int count = meta.getColumnCount();
            resultSet = result.next() ? result : null;
            if (resultSet != null) for (int i = 1; i <= count; i++) {
                String name = meta.getColumnName(i);
                fields.put(name, new ResultSetFieldValue(name));
            }
        } catch (SQLException ex) {
            Assert.fail(ex);
        }
    }

    @Override
    public Attribute getAttribute(String name) {
        return fields.get(name);
    }

    @Override
    public Iterable<Attribute> getAttributes() {
        return fields.values();
    }

    @Override
    public Iterable<Document> getChildren(String name) {
        return getChildren();
    }

    @Override
    public Iterable<Document> getChildren() {
        return new Iterable<Document>() {

            public Iterator<Document> iterator() {
                return resultSet != null ? new ResultSetIterator<Document>(resultSet, new ResultSetRelativesChild(ResultSetRelatives.this)) : Null.getInstance();
            }
        };
    }

    @Override
    public Document getChild(String name) {
        Document desc = relatives.get(name);
        if (desc != null) return WormsEntityRelativesFactory.getInstance().buildEntityRelatives(desc, getEntity(), new XPathContext(this));
        return null;
    }

    public void release() {
        try {
            resultSet.close();
        } catch (SQLException ex) {
            Assert.fail(ex);
        }
    }

    private class ResultSetFieldValue implements Attribute {

        private String name;

        public ResultSetFieldValue(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return ValueUtil.toString(getObject());
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
        }

        public Object getObject() {
            try {
                return resultSet.getObject(name);
            } catch (SQLException ex) {
                Assert.fail(ex);
            }
            return null;
        }

        public void setObject(Object obj) {
        }
    }

    private static class ResultSetRelativesChild extends AttributeImpl implements Document {

        private ResultSetRelatives relatives;

        public ResultSetRelativesChild(ResultSetRelatives relatives) {
            this.relatives = relatives;
        }

        public Document getChild(String name) {
            return null;
        }

        public Iterable<Document> getChildren(String name) {
            return Null.getInstance().iter();
        }

        public Iterable<Document> getChildren() {
            return Null.getInstance().iter();
        }

        public Document addChild(String name) {
            return Null.getInstance();
        }

        public void removeNode(Document child) {
        }

        public Document getParentNode() {
            return null;
        }

        public Attribute getAttribute(String name) {
            return relatives.getAttribute(name);
        }

        public Iterable<Attribute> getAttributes() {
            return new NonePropertyAttributeIterable<Attribute>(relatives.getAttributes());
        }

        public Attribute addAttribute(String name) {
            return Null.getInstance();
        }

        public void removeAttribute(Attribute attrValue) {
        }

        @Override
        public String getName() {
            return relatives.getName();
        }
    }
}
