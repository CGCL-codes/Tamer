package net.wotonomy.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import net.wotonomy.control.EOAndQualifier;
import net.wotonomy.control.EOClassDescription;
import net.wotonomy.control.EOFetchSpecification;
import net.wotonomy.control.EOGenericRecord;
import net.wotonomy.control.EOGlobalID;
import net.wotonomy.control.EOIntegralKeyGlobalID;
import net.wotonomy.control.EOKeyGlobalID;
import net.wotonomy.control.EOKeyValueArchiver;
import net.wotonomy.control.EOKeyValueQualifier;
import net.wotonomy.control.EOKeyValueUnarchiver;
import net.wotonomy.control.EOQualifier;
import net.wotonomy.control.EOVectorKeyGlobalID;
import net.wotonomy.foundation.NSArray;
import net.wotonomy.foundation.NSDictionary;
import net.wotonomy.foundation.NSKeyValueCoding;
import net.wotonomy.foundation.NSMutableArray;
import net.wotonomy.foundation.NSMutableDictionary;
import net.wotonomy.foundation.NSPropertyListSerialization;

/**
* An EOEntity is a mapping between a Java class and a database table or view.
* It indicates which attributes should be fetched from the table/view, what
* attributes are part of the primary key, what class the entity should map to.
*
* @author ezamudio@nasoft.com
* @author $Author: cgruber $
* @version $Revision: 894 $
*/
public class EOEntity implements EOPropertyListEncoding {

    protected NSMutableDictionary _attributes = new NSMutableDictionary();

    protected NSMutableDictionary _relations = new NSMutableDictionary();

    private NSMutableArray _classPropertyNames = new NSMutableArray();

    private NSMutableArray _classProperties = new NSMutableArray();

    private NSMutableArray _classPropertyAttributes = new NSMutableArray();

    private NSMutableArray _classPropertyManyRelationships = new NSMutableArray();

    private NSMutableArray _classPropertyOneRelationships = new NSMutableArray();

    protected NSArray _pkAttributes = NSArray.EmptyArray;

    protected NSArray _pkAttributeNames = NSArray.EmptyArray;

    protected NSMutableDictionary _fetchSpecs = new NSMutableDictionary();

    protected NSMutableArray _lockingAttributes = new NSMutableArray();

    protected String _className;

    protected String _name;

    protected String _externalName;

    protected boolean _isAbstract;

    protected boolean _isReadOnly;

    protected EOModel _model;

    protected NSDictionary _userInfo;

    protected NSDictionary _internalInfo;

    private boolean _loadedFetchSpecs;

    public EOEntity() {
        super();
    }

    public EOEntity(NSDictionary dict, Object obj) {
        super();
        _model = (EOModel) obj;
        setName((String) dict.objectForKey("name"));
        setExternalName((String) dict.objectForKey("externalName"));
        setClassName((String) dict.objectForKey("className"));
        if (dict.objectForKey("internalInfo") != null) _internalInfo = (NSDictionary) dict.objectForKey("internalInfo");
        if (dict.objectForKey("userInfo") != null) _userInfo = (NSDictionary) dict.objectForKey("userInfo");
        NSArray atr = (NSArray) dict.objectForKey("attributes");
        for (int i = 0; i < atr.count(); i++) {
            NSDictionary d = (NSDictionary) atr.objectAtIndex(i);
            EOAttribute atrib = new EOAttribute(d, this);
            addAttribute(atrib);
        }
        atr = (NSArray) dict.objectForKey("primaryKeyAttributes");
        NSMutableArray pka = new NSMutableArray();
        for (int i = 0; i < atr.count(); i++) {
            EOAttribute a = attributeNamed((String) atr.objectAtIndex(i));
            pka.addObject(a);
        }
        _pkAttributes = new NSArray(pka);
        _pkAttributeNames = atr;
        _lockingAttributes.removeAllObjects();
        atr = (NSArray) dict.objectForKey("attributesUsedForLocking");
        for (int i = 0; i < atr.count(); i++) {
            String x = (String) atr.objectAtIndex(i);
            EOAttribute a = attributeNamed(x);
            _lockingAttributes.addObject(a);
        }
        atr = (NSArray) dict.objectForKey("classProperties");
        if (atr != null) {
            for (int i = 0; i < atr.count(); i++) if (!_classPropertyNames.containsObject((atr.objectAtIndex(i)))) _classPropertyNames.addObject(atr.objectAtIndex(i));
        }
        atr = (NSArray) dict.objectForKey("relationships");
        if (atr != null) {
            for (int i = 0; i < atr.count(); i++) {
                NSDictionary d = (NSDictionary) atr.objectAtIndex(i);
                EORelationship rel = new EORelationship(d, this);
                addRelationship(rel);
            }
        }
    }

    public void addAttribute(EOAttribute atr) {
        if (atr.name() == null) throw new IllegalArgumentException("Cannot add an unnamed attribute to an entity.");
        if (_attributes.objectForKey(atr.name()) != null) throw new IllegalArgumentException("Entity " + name() + " already has an attribute named " + atr.name());
        _attributes.setObjectForKey(atr, atr.name());
        atr.setEntity(this);
        _lockingAttributes.addObject(atr);
        _classProperties.addObject(atr);
        _classPropertyNames.addObject(atr.name());
        _classPropertyAttributes.addObject(atr);
    }

    public void removeAttribute(EOAttribute atr) {
        _attributes.removeObjectForKey(atr.name());
        atr.setEntity(null);
        _classProperties.removeObject(atr);
        _classPropertyNames.removeObject(atr.name());
        _classPropertyAttributes.removeObject(atr);
    }

    public void addFetchSpecification(EOFetchSpecification fspec, String name) {
        loadFetchSpecifications();
        if (_fetchSpecs.objectForKey(name) != null) throw new IllegalArgumentException("Entity " + name() + " already has a fetch specification named " + name);
        _fetchSpecs.setObjectForKey(fspec, name);
    }

    public void removeFetchSpecificationNamed(String name) {
        _fetchSpecs.removeObjectForKey(name);
    }

    public EOFetchSpecification fetchSpecificationNamed(String name) {
        loadFetchSpecifications();
        return (EOFetchSpecification) _fetchSpecs.objectForKey(name);
    }

    public NSArray fetchSpecificationNames() {
        loadFetchSpecifications();
        return _fetchSpecs.allKeys();
    }

    /** Loads fetch specifications from the .fspec file,
	 * if one exists.
	 */
    private void loadFetchSpecifications() {
        if (_loadedFetchSpecs) return;
        _loadedFetchSpecs = true;
        if (model().path() == null) return;
        File f = new File(model().path());
        f = new File(f, name() + ".fspec");
        if (!f.exists()) return;
        NSDictionary fdict = null;
        String x = null;
        try {
            FileInputStream fin = new FileInputStream(f);
            byte[] b = new byte[fin.available()];
            fin.read(b);
            fin.close();
            x = new String(b);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot read file " + f);
        }
        fdict = NSPropertyListSerialization.dictionaryForString(x);
        if (fdict == null) throw new IllegalArgumentException("Cannot read dictionary from " + f);
        NSArray keys = fdict.allKeys();
        EOKeyValueUnarchiver unarch = new EOKeyValueUnarchiver(fdict);
        for (int i = 0; i < keys.count(); i++) {
            String k = (String) keys.objectAtIndex(i);
            EOFetchSpecification fs = (EOFetchSpecification) unarch.decodeObjectForKey(k);
            if (fs != null) _fetchSpecs.setObjectForKey(fs, k);
        }
    }

    public NSArray attributes() {
        return _attributes.allValues();
    }

    public EOAttribute attributeNamed(String name) {
        return (EOAttribute) _attributes.objectForKey(name);
    }

    public NSArray flattenedAttributes() {
        return null;
    }

    public void setClassName(String name) {
        _className = name;
    }

    public String className() {
        return _className;
    }

    public void setName(String name) {
        _name = name;
    }

    public String name() {
        return _name;
    }

    public void setExternalName(String name) {
        _externalName = name;
    }

    public String externalName() {
        return _externalName;
    }

    public void addRelationship(EORelationship rel) {
        if (rel.name() == null) throw new IllegalArgumentException("Cannot add an unnamed relationship to an entity.");
        if (_relations.objectForKey(rel.name()) != null) throw new IllegalArgumentException("Entity " + name() + " already has a relationship named " + rel.name());
        if (_attributes.objectForKey(rel.name()) != null) throw new IllegalArgumentException("Entity " + name() + " has an attribute named " + rel.name());
        _relations.setObjectForKey(rel, rel.name());
        _classProperties.addObject(rel);
        _classPropertyNames.addObject(rel.name());
        if (rel.isToMany()) _classPropertyManyRelationships.addObject(rel); else _classPropertyOneRelationships.addObject(rel);
    }

    public void removeRelationship(EORelationship rel) {
        _relations.removeObjectForKey(rel.name());
        _classProperties.removeObject(rel);
        _classPropertyNames.removeObject(rel.name());
        _classPropertyManyRelationships.removeObject(rel);
        _classPropertyOneRelationships.removeObject(rel);
    }

    /** Returns the relationships from this entity to other entities.
	 * @return An array of the relationships of this entity.
	 */
    public NSArray relationships() {
        return _relations.allValues();
    }

    public EORelationship relationshipNamed(String name) {
        return (EORelationship) _relations.objectForKey(name);
    }

    public EOModel model() {
        return _model;
    }

    public void setPrimaryKeyAtributes(NSArray pk) {
        _pkAttributes = pk;
    }

    public NSArray primaryKeyAttributes() {
        return _pkAttributes;
    }

    public NSArray primaryKeyAttributeNames() {
        if (_pkAttributeNames.count() != _pkAttributes.count()) {
            NSMutableArray arr = new NSMutableArray();
            for (int i = 0; i < _pkAttributes.count(); i++) {
                EOAttribute a = (EOAttribute) _pkAttributes.objectAtIndex(i);
                arr.addObject(a.name());
            }
            _pkAttributeNames = new NSArray(arr);
        }
        return _pkAttributeNames;
    }

    public boolean hasSimplePrimaryKey() {
        return _pkAttributes.count() == 1;
    }

    public boolean isValidPrimaryKeyAttribute(EOAttribute attr) {
        return !attr.allowsNull();
    }

    public void setAttributesUsedForLocking(NSArray value) {
        _lockingAttributes.removeAllObjects();
        _lockingAttributes.addObjectsFromArray(value);
    }

    public NSArray attributesUsedForLocking() {
        return new NSArray(_lockingAttributes);
    }

    public void setClassProperties(NSArray value) {
        _classProperties.removeAllObjects();
        _classProperties.addObjectsFromArray(value);
        _classPropertyNames.removeAllObjects();
        _classPropertyAttributes.removeAllObjects();
        _classPropertyOneRelationships.removeAllObjects();
        _classPropertyManyRelationships.removeAllObjects();
        for (int i = 0; i < value.count(); i++) {
            EOProperty o = (EOProperty) value.objectAtIndex(i);
            _classPropertyNames.addObject(o.name());
            if (o instanceof EOAttribute) {
                _classPropertyAttributes.addObject(o);
            } else if (o instanceof EORelationship) {
                if (((EORelationship) o).isToMany()) _classPropertyManyRelationships.addObject(o); else _classPropertyOneRelationships.addObject(o);
            }
        }
    }

    public NSArray classProperties() {
        if (_classProperties == null) {
            if (_classPropertyNames == null) return NSArray.EmptyArray; else {
                NSMutableArray props = new NSMutableArray();
                NSMutableArray atribs = new NSMutableArray();
                NSMutableArray ones = new NSMutableArray();
                NSMutableArray manies = new NSMutableArray();
                for (int i = 0; i < _classPropertyNames.count(); i++) {
                    String name = (String) _classPropertyNames.objectAtIndex(i);
                    EOAttribute a = attributeNamed(name);
                    EORelationship r = relationshipNamed(name);
                    if (a != null) {
                        props.addObject(a);
                        atribs.addObject(a);
                    } else if (r != null) {
                        props.addObject(r);
                        if (r.isToMany()) manies.addObject(r); else ones.addObject(r);
                    } else throw new IllegalArgumentException("Cannot find attribute or relationship named " + name);
                }
                _classProperties = props;
                _classPropertyAttributes = atribs;
                _classPropertyOneRelationships = ones;
                _classPropertyManyRelationships = manies;
            }
        }
        return _classProperties;
    }

    public NSArray classPropertyNames() {
        return _classPropertyNames;
    }

    public NSArray classPropertyAttributeNames() {
        if (_classPropertyAttributes == null) return NSArray.EmptyArray;
        NSMutableArray arr = new NSMutableArray(_classPropertyAttributes.count());
        for (int i = 0; i < _classPropertyAttributes.count(); i++) {
            EOAttribute a = (EOAttribute) _classPropertyAttributes.objectAtIndex(i);
            arr.addObject(a.name());
        }
        return arr;
    }

    public NSArray classPropertyToManyRelationshipNames() {
        if (_classPropertyManyRelationships == null) return NSArray.EmptyArray;
        NSMutableArray arr = new NSMutableArray(_classPropertyManyRelationships.count());
        for (int i = 0; i < _classPropertyManyRelationships.count(); i++) {
            EOAttribute a = (EOAttribute) _classPropertyManyRelationships.objectAtIndex(i);
            arr.addObject(a.name());
        }
        return arr;
    }

    public NSArray classPropertyToOneRelationshipNames() {
        if (_classPropertyOneRelationships == null) return NSArray.EmptyArray;
        NSMutableArray arr = new NSMutableArray(_classPropertyOneRelationships.count());
        for (int i = 0; i < _classPropertyOneRelationships.count(); i++) {
            EOAttribute a = (EOAttribute) _classPropertyOneRelationships.objectAtIndex(i);
            arr.addObject(a.name());
        }
        return arr;
    }

    public void setIsAbstractEntity(boolean flag) {
        _isAbstract = flag;
    }

    public boolean isAbstractEntity() {
        return _isAbstract;
    }

    public void setReadOnly(boolean flag) {
        _isReadOnly = flag;
    }

    public boolean isReadOnly() {
        return _isReadOnly;
    }

    public void setStoredProcedure(EOStoredProcedure proc, String operation) {
    }

    public EOStoredProcedure storedProcedureForOperation(String operation) {
        return null;
    }

    public NSArray subEntities() {
        return null;
    }

    public NSArray attributesToFetch() {
        return attributes();
    }

    public NSArray externalModelsReferenced() {
        return null;
    }

    public EOClassDescription classDescriptionForInstances() {
        EOClassDescription cd = EOClassDescription.classDescriptionForEntityName(name());
        if (cd == null) {
            cd = new EOEntityClassDescription(this);
            Class cl = null;
            try {
                cl = Class.forName(className());
            } catch (ClassNotFoundException ex) {
                cl = EOGenericRecord.class;
            }
            EOClassDescription.registerClassDescription(cd, cl);
        }
        return cd;
    }

    /**
	 * Creates a global ID for a row. The row must have values
	 * for all the primary key attributes.
	 * @param row A raw row for this entity.
	 * @return A key global ID to identify this row.
	 */
    public EOGlobalID globalIDForRow(Map row) {
        NSArray pknames = primaryKeyAttributeNames();
        EOKeyGlobalID gid = null;
        if (pknames.count() == 1 && row.get(pknames.objectAtIndex(0)) instanceof Number) {
            Number n = (Number) row.get(pknames.objectAtIndex(0));
            gid = new EOIntegralKeyGlobalID(name(), n);
        } else {
            Object[] vals = new Object[pknames.count()];
            for (int i = 0; i < pknames.count(); i++) {
                Object v = row.get(pknames.objectAtIndex(i));
                vals[i] = v;
            }
            gid = new EOVectorKeyGlobalID(name(), vals);
        }
        return gid;
    }

    /**
	 * Returns a dictionary with the primary key values contained in 
	 * the global id.
	 * @param gid A Key global ID.
	 * @return A dictionary with the primary key values for gid.
	 */
    public NSDictionary primaryKeyForGlobalID(EOGlobalID gid) {
        if (!(gid instanceof EOKeyGlobalID)) return null;
        Object[] vals = ((EOKeyGlobalID) gid).keyValues();
        NSArray pknames = primaryKeyAttributeNames();
        return new NSDictionary(vals, pknames.toArray());
    }

    public EOQualifier qualifierForPrimaryKey(Map pkey) {
        NSArray pknames = primaryKeyAttributeNames();
        EOQualifier q = null;
        NSMutableArray subq = new NSMutableArray(pknames.count());
        for (int i = 0; i < pknames.count(); i++) {
            String key = (String) pknames.objectAtIndex(i);
            Object v = pkey.get(key);
            if (v == null || v == NSKeyValueCoding.NullValue) throw new IllegalArgumentException("Primary key with null values.");
            subq.addObject(new EOKeyValueQualifier(key, EOQualifier.QualifierOperatorEqual, v));
        }
        if (subq.count() == 1) q = (EOQualifier) subq.objectAtIndex(0); else q = new EOAndQualifier(subq);
        return q;
    }

    public void setUserInfo(NSDictionary value) {
        _userInfo = value;
    }

    public NSDictionary userInfo() {
        return _userInfo;
    }

    public void awakeWithPropertyList(NSDictionary plist) {
    }

    public void encodeIntoPropertyList(NSMutableDictionary dict) {
        dict.setObjectForKey(name(), "name");
        dict.setObjectForKey(externalName(), "externalName");
        dict.setObjectForKey(className(), "className");
        NSMutableArray arr = new NSMutableArray(_attributes.allValues());
        for (int i = 0; i < _attributes.count(); i++) {
            EOAttribute a = (EOAttribute) arr.objectAtIndex(i);
            NSMutableDictionary d = new NSMutableDictionary();
            a.encodeIntoPropertyList(d);
            arr.replaceObjectAtIndex(i, d);
        }
        dict.setObjectForKey(arr, "attributes");
        if (_relations.count() > 0) {
            arr = new NSMutableArray(_relations.allValues());
            for (int i = 0; i < _relations.count(); i++) {
                EORelationship r = (EORelationship) arr.objectAtIndex(i);
                NSMutableDictionary d = new NSMutableDictionary();
                r.encodeIntoPropertyList(d);
                arr.replaceObjectAtIndex(i, d);
            }
            dict.setObjectForKey(arr, "relationships");
        }
        NSMutableDictionary d = new NSMutableDictionary();
        loadFetchSpecifications();
        java.util.Enumeration enumeration = _fetchSpecs.keyEnumerator();
        while (enumeration.hasMoreElements()) {
            String k = (String) enumeration.nextElement();
            EOFetchSpecification f = (EOFetchSpecification) _fetchSpecs.objectForKey(k);
            EOKeyValueArchiver arch = new EOKeyValueArchiver();
            f.encodeWithKeyValueArchiver(arch);
            d.setObjectForKey(arch.dictionary(), k);
        }
        dict.setObjectForKey(d, "fetchSpecificationDictionary");
        dict.setObjectForKey(_classPropertyNames, "classProperties");
        dict.setObjectForKey(_pkAttributeNames, "primaryKeyAttributes");
        arr = new NSMutableArray(_lockingAttributes);
        for (int i = 0; i < arr.count(); i++) arr.replaceObjectAtIndex(i, ((EOAttribute) arr.objectAtIndex(i)).name());
        dict.setObjectForKey(arr, "attributesUsedForLocking");
        if (_userInfo != null && _userInfo.count() > 0) dict.setObjectForKey(_userInfo, "userInfo");
        if (_internalInfo != null && _internalInfo.count() > 0) dict.setObjectForKey(_internalInfo, "internalInfo");
    }

    public EOAttribute _attributeForPath(String path) {
        NSArray comps = NSArray.componentsSeparatedByString(path, ".");
        if (comps.count() < 2) return null;
        EORelationship r = null;
        EOEntity e = this;
        for (int i = 0; i < comps.count() - 1; i++) {
            String name = (String) comps.objectAtIndex(i);
            r = e.relationshipNamed(name);
            if (r == null) return null;
            e = r.destinationEntity();
        }
        return e.attributeNamed((String) comps.lastObject());
    }
}
