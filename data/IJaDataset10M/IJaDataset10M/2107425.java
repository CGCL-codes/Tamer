package seedpod.model.rdb;

import java.util.Vector;
import seedpod.util.XMLWriter;

public class View extends Relation {

    private Vector<Relation> _unionTables = new Vector<Relation>();

    private Relation _concreteTable = null;

    public View(String name) {
        super(name);
    }

    public View(Rdb rdb, String name) {
        super(rdb, name);
    }

    public void setConcreteClassTable(Relation table) {
        _concreteTable = table;
        if (table.getPrimaryKey() != null) setPrimaryKey(table.getPrimaryKey());
        addToUnionRelation(table);
    }

    public Attribute getPrimaryKey() {
        Attribute pk = super.getPrimaryKey();
        try {
            pk = getAttribute(Relation.DEFAULT_PRIMARY_KEY);
        } catch (NoAttributeFoundException e) {
            pk = _rdb.addAttribute(this, Relation.DEFAULT_PRIMARY_KEY, RdbValueType.INTEGER, true);
            System.err.println("add primary key for " + getName());
        }
        return pk;
    }

    public Relation getConcreteClassTable() {
        return _concreteTable;
    }

    public void addToUnionRelation(Relation table) {
        if (!_unionTables.contains(table)) _unionTables.add(table);
    }

    public Vector<Relation> getUnionRelations() {
        return _unionTables;
    }

    /**
     * @todo implement view definition such that what it select aggregates from
     *       is defined.
     * @param out
     * @return
     * @throws XMLWriter.XMLException
     */
    public XMLWriter serializeXML(XMLWriter out) throws XMLWriter.XMLException {
        XMLWriter viewStream = out.addElement("view");
        viewStream.addElement("name", _name);
        Attribute att;
        Vector<Attribute> attributes = getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            att = (Attribute) attributes.elementAt(i);
            att.serializeXML(viewStream);
        }
        return viewStream;
    }
}
