package seedpod.model.rdb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import seedpod.model.SeedpodModel;
import seedpod.model.Thing;
import seedpod.util.XMLWriter;

public class Rdb {

    public static int MAX_NAME_LENGTH = 63;

    protected String _rdbName;

    protected boolean _dbSupportsArrayType = false;

    protected boolean _suportsGlobalOid = false;

    protected Vector<Relation> _relations = new Vector<Relation>();

    protected Vector<Attribute> _attributes = new Vector<Attribute>();

    protected Vector<View> _views = new Vector<View>();

    protected Vector<SimpleForeignKey> _foreignKeys = new Vector<SimpleForeignKey>();

    protected Vector<ViewReference> _viewKeys = new Vector<ViewReference>();

    public Rdb(String rdbName) {
        _rdbName = rdbName;
    }

    public String getName() {
        return _rdbName;
    }

    public Vector<Relation> getRelations() {
        return _relations;
    }

    public Vector<View> getViews() {
        return _views;
    }

    public Relation createThingRelation(String thingName) {
        _suportsGlobalOid = true;
        Relation thingRelation = createRelation(SeedpodModel.RdbCls.THING, true);
        Attribute primaryKey = thingRelation.getPrimaryKey();
        primaryKey.setDatabaseType(RdbValueType.SERIAL);
        Attribute typeAtt = new Attribute(this, thingRelation, SeedpodModel.RdbSlot.THING_TYPE, RdbValueType.VARCHAR);
        typeAtt.setDatabaseTypeParameter(String.valueOf(64));
        typeAtt.setIsRequired(true);
        thingRelation.addAttribute(typeAtt);
        Attribute statusAtt = new Attribute(this, thingRelation, SeedpodModel.RdbSlot.THING_STATUS, RdbValueType.VARCHAR);
        statusAtt.setDatabaseTypeParameter(String.valueOf(20));
        Collection<String> allowedValues = new Vector<String>();
        allowedValues.add(Thing.STATUS_DELETED);
        allowedValues.add(Thing.STATUS_INVALID);
        allowedValues.add(Thing.STATUS_SAVED);
        statusAtt.setAllowedValues(allowedValues);
        statusAtt.setDefaultValue(Thing.STATUS_INVALID);
        thingRelation.addAttribute(statusAtt);
        return thingRelation;
    }

    public Vector<SimpleForeignKey> getForeignKeys() {
        return _foreignKeys;
    }

    public Vector<ViewReference> getViewKeys() {
        return _viewKeys;
    }

    public Vector<Attribute> getAttributes() {
        return _attributes;
    }

    /**
	 * Create a relationship in the RDB with a default ID field as primary key
	 * 
	 * @param relationName
	 * @return
	 */
    private Relation createRelation(String relationName) {
        if (relationName == null) relationName = getUniqueRelationName();
        Relation table = new Relation(this, relationName);
        if (!_relations.contains(table)) _relations.add(table);
        return table;
    }

    public Relation createRelation(String relationName, boolean setDefaultPK) {
        Relation rel = createRelation(relationName);
        if (setDefaultPK) createDefaultPK(rel);
        return rel;
    }

    private void createDefaultPK(Relation table) {
        try {
            Attribute pk = addAttribute(table, Relation.DEFAULT_PRIMARY_KEY, RdbValueType.INTEGER, true);
            table.setPrimaryKey(pk);
            String refThingIdName = Relation.DEFAULT_PRIMARY_KEY;
            if (_suportsGlobalOid && !table.getName().equalsIgnoreCase(SeedpodModel.RdbCls.THING)) {
                addForeignKey(table, refThingIdName, getRelation(SeedpodModel.RdbCls.THING), RdbValueType.INTEGER);
            }
        } catch (NoRelationFoundException e) {
            System.out.println("error in RDB.createRelation();" + e.getMessage());
        }
    }

    /**
	 * creates a default ID primary key for the view. However, no rule or constraint is created
	 * @param view
	 */
    private void createPKforView(View view) {
        addAttribute(view, Relation.DEFAULT_PRIMARY_KEY, RdbValueType.INTEGER, false);
    }

    public View createView(String viewName, boolean setDefaultPK) {
        View view = new View(this, viewName);
        if (setDefaultPK) createPKforView(view);
        _views.add(view);
        return view;
    }

    public SimpleForeignKey addForeignKey(Relation fromTable, String foreignKey, Relation toTable, RdbValueType type) throws NoRelationFoundException {
        if (toTable == null || toTable.getPrimaryKey() == null) {
            throw new NoRelationFoundException(toTable.getName(), foreignKey);
        }
        SimpleForeignKey fk = new SimpleForeignKey(this, fromTable, foreignKey, toTable, toTable.getPrimaryKey().getName(), type);
        fk.setReferencedAttribute(toTable);
        _foreignKeys.add(fk);
        return fk;
    }

    public ViewReference addViewKey(Relation fromTable, String keyName, View refView, RdbValueType type) throws NoRelationFoundException, NoViewFoundException {
        if (refView.getPrimaryKey() == null) {
            System.err.println("throwing NoViewFoundException in Rdb.addViewKey() for " + keyName);
            throw new NoViewFoundException(refView.getName());
        }
        Attribute toAtt = refView.getPrimaryKey();
        ViewReference vk = new ViewReference(this, fromTable, keyName, refView, toAtt.getName(), type);
        _viewKeys.add(vk);
        return vk;
    }

    public SimpleForeignKey addForeignKey(SimpleForeignKey fk) {
        _foreignKeys.add(fk);
        return fk;
    }

    private String getUniqueRelationName() {
        return "rel_" + _relations.size();
    }

    public boolean supportsArrayType() {
        return _dbSupportsArrayType;
    }

    public boolean copyRel(String sourceTable, String targetTable) {
        throw new java.lang.UnsupportedOperationException("Method copyRel() not yet implemented.");
    }

    public boolean copyRel(String sourceTable, String targetTable, boolean replace) {
        throw new java.lang.UnsupportedOperationException("Method copyRel() not yet implemented.");
    }

    public boolean renameRel(String old, String newName) {
        throw new java.lang.UnsupportedOperationException("Method renameRel() not yet implemented.");
    }

    public boolean removeRel(String relationName) {
        throw new java.lang.UnsupportedOperationException("Method removeRel() not yet implemented.");
    }

    public Attribute addAttribute(Relation relation, String attName, RdbValueType dbtype, boolean isPrimaryKey) {
        Attribute attr = new Attribute(this, relation, attName, dbtype);
        addAttribute(relation, attr);
        attr.setIsPrimaryKey(isPrimaryKey);
        return attr;
    }

    public Attribute addAttribute(Relation relation, Attribute attribute) {
        relation.addAttribute(attribute);
        if (!_attributes.contains(attribute)) _attributes.add(attribute);
        return attribute;
    }

    public void removeAttr(String relationName, String attribute) throws NoRelationFoundException, Relation.DeletePrimaryKeyException {
        Relation rel = getRelation(relationName);
        rel.removeAttribute(attribute);
    }

    public void renameAttribute(String relName, String oldAttName, String newAttName) throws NoRelationFoundException, Relation.NoAttributeFoundException {
        Relation rel = getRelation(relName);
        Attribute att = rel.getAttribute(oldAttName);
        att.setName(newAttName);
    }

    public Relation getRelation(String name) throws NoRelationFoundException {
        Relation rel;
        for (int i = 0; i < _relations.size(); i++) {
            rel = (Relation) _relations.elementAt(i);
            if (rel.getName().equalsIgnoreCase(name)) return rel;
        }
        throw new NoRelationFoundException(name);
    }

    public View getView(String viewName) throws NoViewFoundException {
        View v;
        for (int i = 0; i < _views.size(); i++) {
            v = (View) _views.elementAt(i);
            if (v.getName().equalsIgnoreCase(viewName)) return v;
        }
        throw new NoViewFoundException(viewName);
    }

    public Attribute getAttribute(String name) {
        Attribute att;
        for (int i = 0; i < _attributes.size(); i++) {
            att = (Attribute) _attributes.elementAt(i);
            if (att.getName().equalsIgnoreCase(name)) return att;
        }
        return null;
    }

    /**
	 * writes out a generic XML document for the RDB
	 * 
	 * @param out
	 * @return
	 * @throws KBtoDB.XMLWriter.XMLException
	 */
    public XMLWriter serializeXML(XMLWriter out) throws XMLWriter.XMLException {
        Iterator<Relation> relItr = _relations.iterator();
        Relation rel;
        XMLWriter rdbOut = out.addElement("schema");
        out.addElement("name", _rdbName);
        while (relItr.hasNext()) {
            rel = (Relation) relItr.next();
            rel.serializeXML(out);
        }
        Iterator<SimpleForeignKey> keyItr = _foreignKeys.iterator();
        SimpleForeignKey fk;
        while (keyItr.hasNext()) {
            fk = (SimpleForeignKey) keyItr.next();
            fk.serializeXML(rdbOut);
        }
        Iterator<View> viewItr = _views.iterator();
        View view;
        while (viewItr.hasNext()) {
            view = (View) (viewItr.next());
            view.serializeXML(rdbOut);
        }
        Iterator<ViewReference> viewKeyItr = _viewKeys.iterator();
        ViewReference vk;
        while (viewKeyItr.hasNext()) {
            vk = (ViewReference) viewKeyItr.next();
            vk.serializeXML(rdbOut);
        }
        return rdbOut;
    }
}
