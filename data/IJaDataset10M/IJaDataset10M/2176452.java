package org.enjavers.jethro.dspi.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.enjavers.jethro.api.JethroManager;
import org.enjavers.jethro.api.Metadata;
import org.enjavers.jethro.api.dto.DTOConstraintExpression;
import org.enjavers.jethro.api.dto.DTODescriptor;
import org.enjavers.jethro.api.dto.DTODomain;
import org.enjavers.jethro.dspi.DJPException;
import org.enjavers.jethro.dspi.DefaultJethroProvider;

/**
 * @author Alessandro Lombardi, Fabrizio Gambelunghe
 */
public class DTODescriptorSPI implements DTODescriptor {

    private Class _DTOClass;

    private String _tableName;

    private String _entityName;

    private String[] _fieldNames;

    private String[] _propertyNames;

    private PrimaryKey _pk;

    private ForeignKey[] _foreignKeys;

    private DTODomain[] _propertyDomains;

    private DTOConstraintExpression _constraintExpression;

    /**
	 * These are the descriptive properties, labeled by a flag into xml file;
	 * this array must be a subset of _propertyNames.
	 */
    private String[] _descriptiveProperties;

    private JethroManager _enclosingManager;

    protected void _constructor(JethroManager i_enclosingManager, Class i_dtoClass, String i_table, String i_entity, String[] i_fieldNames, String[] i_propertyNames, DTODomain[] i_propertyDomains, PrimaryKey i_pk, ForeignKey[] i_fks, String[] i_descriptiveProperties, DTOConstraintExpression i_constraintExpression) {
        _enclosingManager = i_enclosingManager;
        _DTOClass = i_dtoClass;
        _tableName = i_table != null ? i_table.toUpperCase() : null;
        _entityName = i_entity != null ? i_entity.toUpperCase() : null;
        if (i_fieldNames != null) {
            _fieldNames = new String[i_fieldNames.length];
            for (int i = 0; i < i_fieldNames.length; i++) _fieldNames[i] = i_fieldNames[i].toUpperCase();
        }
        _propertyNames = new String[i_propertyNames.length];
        for (int i = 0; i < i_propertyNames.length; i++) _propertyNames[i] = i_propertyNames[i].toUpperCase();
        _pk = i_pk;
        _foreignKeys = i_fks;
        _propertyDomains = i_propertyDomains;
        if (i_descriptiveProperties != null) {
            _descriptiveProperties = new String[i_descriptiveProperties.length];
            for (int i = 0; i < i_descriptiveProperties.length; i++) _descriptiveProperties[i] = i_descriptiveProperties[i].toUpperCase();
        }
        _constraintExpression = i_constraintExpression;
    }

    /**
	 * This constructor is used only to build temporary descriptors needed
	 * (for example) by dynamic query. 
	 * @see org.enjavers.jethro.dspi.DescriptorRepository#createDescriptor(java.lang.String[], org.enjavers.jethro.api.dto.DTODomain i_domains)
	 */
    public DTODescriptorSPI(JethroManager i_enclosingManager, String[] i_propertyNames, Class[] i_propertyTypes) {
        this(i_enclosingManager, null, null, "TEMPORARY_DTO", null, i_propertyNames, i_propertyTypes, null, null, null, null);
    }

    /**
	 * This constructor is used only to build temporary descriptors needed
	 * (for example) by dynamic query. 
	 * @see org.enjavers.jethro.dspi.DescriptorRepository#createDescriptor(java.lang.String[], org.enjavers.jethro.api.dto.DTODomain i_domains)
	 */
    public DTODescriptorSPI(JethroManager i_enclosingManager, String[] i_propertyNames, DTODomain[] i_domains) {
        this(i_enclosingManager, null, null, "TEMPORARY_DTO", null, i_propertyNames, i_domains, null, null, null, null);
    }

    /**
	 * for compability with version 1.07 or less.
	 */
    public DTODescriptorSPI(JethroManager i_enclosingManager, Class i_dtoClass, String i_table, String i_entity, String[] i_fieldNames, String[] i_propertyNames, DTODomain[] i_propertyDomains, PrimaryKey i_pk, ForeignKey[] i_fks, String[] i_descriptiveProperties) {
        _constructor(i_enclosingManager, i_dtoClass, i_table, i_entity, i_fieldNames, i_propertyNames, i_propertyDomains, i_pk, i_fks, i_descriptiveProperties, null);
    }

    public DTODescriptorSPI(JethroManager i_enclosingManager, Class i_dtoClass, String i_table, String i_entity, String[] i_fieldNames, String[] i_propertyNames, DTODomain[] i_propertyDomains, PrimaryKey i_pk, ForeignKey[] i_fks, String[] i_descriptiveProperties, DTOConstraintExpression i_constraintExpression) {
        _constructor(i_enclosingManager, i_dtoClass, i_table, i_entity, i_fieldNames, i_propertyNames, i_propertyDomains, i_pk, i_fks, i_descriptiveProperties, i_constraintExpression);
    }

    /**
	 * @deprecated Use constructor with DTODomain: 
	 * this constructor will use base domain wrappers; the sql data types will be set to null.
	 */
    public DTODescriptorSPI(JethroManager i_enclosingManager, Class i_dtoClass, String i_table, String i_entity, String[] i_fieldNames, String[] i_propertyNames, Class[] i_propertyTypes, PrimaryKey i_pk, ForeignKey[] i_fks, String[] i_descriptiveProperties, DTOConstraintExpression i_constraintExpression) {
        DTODomain[] domains = new DTODomain[i_propertyTypes.length];
        for (int i = 0; i < domains.length; i++) {
            domains[i] = DTODomain.getBaseDomain(i_propertyTypes[i], null);
        }
        _constructor(i_enclosingManager, i_dtoClass, i_table, i_entity, i_fieldNames, i_propertyNames, domains, i_pk, i_fks, i_descriptiveProperties, i_constraintExpression);
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getDTOClass()
	 */
    public Class getDTOClass() {
        if (_DTOClass == null) throw new DJPException("Illegal State: this descriptor is not related to a specific dto class; maybe it's a temporary descriptor?");
        return _DTOClass;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getTableName()
	 */
    public String getTableName() {
        if (_tableName == null) throw new DJPException("Illegal State: this descriptor is not related to a specific table; maybe it's a temporary descriptor?");
        return _tableName;
    }

    public String getEntityName() {
        if (_entityName == null) throw new DJPException("Illegal State: this descriptor is not related to a specific entity; maybe it's a temporary descriptor?");
        return _entityName;
    }

    /**
	 * Set the entity name for this object; this setter method is needed since
	 * the entity name is not contained into xml resource, and must be retrieved
	 * in a second time by the deploy descriptor.
	 * As precondition, to set the entity name is required that the current value is
	 * null: only once the entity name can be set.
	 * 
	 * @deprecated starting from 1.07, the entity name is passed to the decode of
	 * the DescriptorsRepository and the descriptor has the entity name set from
	 * the start.
	 * 
	 * @param i_entity
	 */
    public void setEntityName(String i_entity) {
        if (_entityName == null) _entityName = i_entity.toUpperCase(); else throw new DJPException("Illegal state : cannot reset the entity name. (Only provider can do it!)");
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getFieldName(java.lang.String)
	 */
    public String getFieldName(String i_propertyName) {
        if (_fieldNames == null) throw new DJPException("Illegal State: this descriptor doesn't have field names, but only properties; maybe it's a temporary descriptor?");
        int index = search(i_propertyName, _propertyNames);
        if (index < 0) throw new IllegalArgumentException("The Property: " + i_propertyName + " doesn't exist.");
        return _fieldNames[index];
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getFieldNames()
	 */
    public String[] getFieldNames() {
        if (_fieldNames == null) throw new DJPException("Illegal State: this descriptor doesn't have field names, but only properties; maybe it's a temporary descriptor?");
        return _fieldNames;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getPropertyName(java.lang.String)
	 */
    public String getPropertyName(String i_tableFieldName) {
        if (_fieldNames == null) throw new DJPException("Illegal State: this descriptor doesn't have field names, but only properties; maybe it's a temporary descriptor?");
        int index = search(i_tableFieldName.toUpperCase(), _fieldNames);
        if (index < 0) throw new IllegalArgumentException("The Field : " + i_tableFieldName.toUpperCase() + " doesn't exist.");
        return _propertyNames[index];
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getPropertyDataType(String i_propertyName);
	 */
    public Class getPropertyDataType(String i_propertyName) {
        return getPropertyDomain(i_propertyName).getDomainClass();
    }

    public DTODomain getPropertyDomain(String i_propertyName) {
        int index = search(i_propertyName.toUpperCase(), _propertyNames);
        if (index < 0) throw new IllegalArgumentException("The Property: " + i_propertyName.toUpperCase() + " doesn't exist.");
        return _propertyDomains[index];
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#isPK(java.lang.String)
	 */
    public boolean isPK(String i_tableFieldName) {
        if (_pk == null) throw new DJPException("Illegal State: this descriptor doesn't have a primary key; maybe it's a temporary descriptor?");
        int index = search(i_tableFieldName.toUpperCase(), _pk.pkFields);
        return index >= 0;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#isDescriptive(java.lang.String)
	 */
    public boolean isDescriptive(String i_propertyName) {
        if (_descriptiveProperties == null) throw new DJPException("Illegal State: this descriptor doesn't have descriptive properties; maybe it's a temporary descriptor?");
        int index = search(i_propertyName.toUpperCase(), _descriptiveProperties);
        return index >= 0;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getPropertyNames()
	 */
    public String[] getPropertyNames() {
        return _propertyNames;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getPKey()
	 */
    public PrimaryKey getPKey() {
        if (_pk == null) throw new DJPException("Illegal State: this descriptor doesn't have a primary key; maybe it's a temporary descriptor?");
        return _pk;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getDescriptiveProperties()
	 */
    public String[] getDescriptiveProperties() {
        if (_descriptiveProperties == null) throw new DJPException("Illegal State: this descriptor doesn't have descriptive properties; maybe it's a temporary descriptor?");
        return _descriptiveProperties;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getFKeys()
	 */
    public ForeignKey[] getFKeys() {
        if (_foreignKeys == null) throw new DJPException("Illegal State: this descriptor doesn't have foreign keys; maybe it's a temporary descriptor?");
        return _foreignKeys;
    }

    public ForeignKey getFK(String i_fkName) {
        for (int i = 0; i < _foreignKeys.length; i++) {
            if (_foreignKeys[i].fkName.equalsIgnoreCase(i_fkName)) return _foreignKeys[i];
        }
        throw new IllegalArgumentException("The foreign key " + i_fkName + " doesn't exist.");
    }

    public ForeignKey getFKByProperty(String i_propertyName) {
        if (!existProperty(i_propertyName.toUpperCase())) throw new IllegalArgumentException("The Property: " + i_propertyName.toUpperCase() + " doesn't exist.");
        ForeignKey[] fkeys = getFKeys();
        for (int f = 0; f < fkeys.length; f++) {
            for (int p = 0; p < fkeys[f].fieldPairs.length; p++) {
                if (fkeys[f].fieldPairs[p].sourceFieldName.equalsIgnoreCase(i_propertyName)) return fkeys[f];
            }
        }
        return null;
    }

    public ForeignKey[] getFKByEntity(String i_foreign_entity) {
        ArrayList fkFound = new ArrayList();
        ForeignKey[] fkeys = getFKeys();
        for (int f = 0; f < fkeys.length; f++) {
            String entityName = ((DefaultJethroProvider) getEnclosingManager().getProvider()).getDescriptorsRepository().getDeployDescriptor().tableToEntity(fkeys[f].destTableName.toUpperCase());
            if (entityName.equalsIgnoreCase(i_foreign_entity)) fkFound.add(fkeys[f]);
        }
        ForeignKey[] result = new ForeignKey[fkFound.size()];
        for (int r = 0; r < result.length; r++) {
            result[r] = (ForeignKey) fkFound.get(r);
        }
        return result;
    }

    public String toString() {
        boolean bTmpDesc = (_DTOClass == null || _tableName == null || _fieldNames == null || _pk == null || _foreignKeys == null);
        StringBuffer toReturn = new StringBuffer("");
        if (bTmpDesc) toReturn.append("[Temporary Descriptor]\n");
        if (!bTmpDesc) {
            toReturn.append("DTO CLASS= " + _DTOClass + "\n");
            toReturn.append("TABLE NAME= " + _tableName + "\n");
            toReturn.append(_pk.toString() + "\n");
            List foreignKeysList = Arrays.asList(_foreignKeys);
            if (foreignKeysList.size() == 0) toReturn.append("[No Foreign Keys]\n"); else toReturn.append(foreignKeysList.toString() + "\n");
        }
        toReturn.append("PROPERTY NAME		|		FIELD NAME		|		DOMAIN		");
        toReturn.append("\n--------------------------------------------------------------------------------------------------\n");
        for (int p = 0; p < _propertyNames.length; p++) {
            String fieldName = bTmpDesc ? " - " : _fieldNames[p];
            toReturn.append(_propertyNames[p]);
            toReturn.append("	|	");
            toReturn.append(fieldName);
            toReturn.append("	|	");
            toReturn.append(_propertyDomains[p]);
            toReturn.append("\n");
        }
        if (_constraintExpression != null) toReturn.append("CONSTRAINT : " + _constraintExpression + "\n");
        toReturn.append("--------------------------------------------------------------------------------------------------\n");
        return toReturn.toString();
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#existProperty(java.lang.String)
	 */
    public boolean existProperty(String i_propertyName) {
        String[] properties = getPropertyNames();
        for (int i = 0; i < properties.length; i++) {
            if (i_propertyName.equalsIgnoreCase(properties[i])) return true;
        }
        return false;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#existFK(java.lang.String)
	 */
    public boolean existFK(String i_fkname) {
        ForeignKey[] fkeys = getFKeys();
        for (int i = 0; i < fkeys.length; i++) {
            if (i_fkname.equalsIgnoreCase(fkeys[i].fkName)) return true;
        }
        return false;
    }

    public boolean isFKField(String i_tableFieldName) {
        ForeignKey[] fkeys = getFKeys();
        ArrayList fkFields = new ArrayList(fkeys.length);
        for (int i = 0; i < fkeys.length; i++) {
            for (int j = 0; j < fkeys[i].fieldPairs.length; j++) {
                if (fkeys[i].fieldPairs[j].sourceFieldName.equalsIgnoreCase(i_tableFieldName)) return true;
            }
        }
        return false;
    }

    /**
	 * Facility for searching into a strings array.
	 */
    private int search(String i_obj, String[] i_array) {
        for (int i = 0; i < i_array.length; i++) {
            if (i_array[i].equalsIgnoreCase(i_obj)) return i;
        }
        return -1;
    }

    /**
	 * equals override: two descriptors are equals if they hold the same metadata
	 * (note that the entity names are considered case-insensitive.
	 * Note that this implementation assumes that only provider implementation
	 * can be compared: i.e, the parameter is casted to DTODescriptorSPI, not
	 * DTODescriptor; anyway, you should use always one provider at time! 
	 * @see java.lang.Object#equals(Object)
	 */
    public boolean equals(Object i_descriptor) {
        if (i_descriptor == null || !(i_descriptor instanceof DTODescriptor)) return false;
        DTODescriptorSPI castedDescriptor = (DTODescriptorSPI) i_descriptor;
        boolean dtoClassEqual;
        if (_DTOClass == null) dtoClassEqual = castedDescriptor._DTOClass == null; else dtoClassEqual = _DTOClass.equals(castedDescriptor._DTOClass);
        if (!dtoClassEqual) return false;
        boolean tableNameEqual;
        if (_tableName == null) tableNameEqual = castedDescriptor._tableName == null; else tableNameEqual = _tableName.equalsIgnoreCase(castedDescriptor._tableName);
        if (!tableNameEqual) return false;
        boolean entityNameEqual;
        if (_entityName == null) entityNameEqual = castedDescriptor._entityName == null; else entityNameEqual = _entityName.equalsIgnoreCase(castedDescriptor._entityName);
        if (!entityNameEqual) return false;
        boolean fieldNamesEqual = true;
        if (_fieldNames != null) {
            for (int f = 0; f < _fieldNames.length; f++) {
                if (_fieldNames[f] == null) fieldNamesEqual = fieldNamesEqual && (castedDescriptor._fieldNames[f] == null); else fieldNamesEqual = fieldNamesEqual && _fieldNames[f].equalsIgnoreCase(castedDescriptor._fieldNames[f]);
                if (!fieldNamesEqual) return false;
            }
        } else if (castedDescriptor._fieldNames != null) return false;
        boolean propertyNamesEqual = true;
        for (int p = 0; p < _propertyNames.length; p++) {
            if (_propertyNames[p] == null) propertyNamesEqual = propertyNamesEqual && (castedDescriptor._propertyNames[p] == null); else propertyNamesEqual = propertyNamesEqual && _propertyNames[p].equalsIgnoreCase(castedDescriptor._propertyNames[p]);
            if (!propertyNamesEqual) return false;
        }
        boolean primaryKeyEqual;
        if (_pk == null) primaryKeyEqual = castedDescriptor._pk == null; else primaryKeyEqual = _pk.equals(castedDescriptor._pk);
        if (!primaryKeyEqual) return false;
        boolean foreignKeysEqual = true;
        if (_foreignKeys != null) for (int f = 0; f < _foreignKeys.length; f++) {
            if (_foreignKeys[f] == null) foreignKeysEqual = foreignKeysEqual && (castedDescriptor._foreignKeys[f] == null); else foreignKeysEqual = foreignKeysEqual && _foreignKeys[f].equals(castedDescriptor._foreignKeys[f]);
            if (!foreignKeysEqual) return false;
        } else if (castedDescriptor._foreignKeys != null) return false;
        boolean propertyDataTypesEqual = true;
        for (int d = 0; d < _propertyDomains.length; d++) {
            DTODomain currentTargetDomain = castedDescriptor.getPropertyDomain(castedDescriptor._propertyNames[d]);
            if (!_propertyDomains[d].equals(currentTargetDomain)) return false;
        }
        boolean desctiptiveEqual = true;
        if (_descriptiveProperties != null) for (int d = 0; d < _descriptiveProperties.length; d++) {
            if (_descriptiveProperties[d] == null) desctiptiveEqual = desctiptiveEqual && (castedDescriptor._descriptiveProperties[d] == null); else desctiptiveEqual = desctiptiveEqual && _descriptiveProperties[d].equalsIgnoreCase(castedDescriptor._descriptiveProperties[d]);
            if (!desctiptiveEqual) return false;
        } else if (castedDescriptor._descriptiveProperties != null) return false;
        return true;
    }

    public String[] getRelatedEntities() {
        String[] entities = new String[_foreignKeys.length];
        for (int e = 0; e < entities.length; e++) {
            entities[e] = ((DefaultJethroProvider) getEnclosingManager().getProvider()).getDescriptorsRepository().getDeployDescriptor().tableToEntity(_foreignKeys[e].destTableName.toUpperCase());
        }
        return entities;
    }

    public DTODescriptor[] getRelatedDescriptors() {
        String[] entities = getRelatedEntities();
        DTODescriptor[] descriptors = new DTODescriptor[entities.length];
        for (int e = 0; e < descriptors.length; e++) {
            descriptors[e] = ((DefaultJethroProvider) getEnclosingManager().getProvider()).getDescriptorsRepository().getDescriptor(entities[e]);
        }
        return descriptors;
    }

    public boolean isNtoNRelationEntity() {
        if (getFKeys().length < 2) return false;
        ForeignKey[] fkeys = getFKeys();
        int both_pk_fk_count = 0;
        for (int f = 0; f < fkeys.length; f++) {
            if (isPK(fkeys[f])) both_pk_fk_count++;
        }
        String[] pkFields = getPKey().pkFields;
        for (int p = 0; p < pkFields.length; p++) {
            if (!isFKField(pkFields[p])) return false;
        }
        return both_pk_fk_count >= 2;
    }

    public boolean isPK(ForeignKey i_fk) {
        for (int i = 0; i < i_fk.fieldPairs.length; i++) {
            if (!isPK(i_fk.fieldPairs[i].sourceFieldName.toUpperCase())) return false;
        }
        return true;
    }

    /**
	 * @return The field names that are foreign keys.
	 */
    public String[] getFKFields() {
        ForeignKey[] fkeys = getFKeys();
        ArrayList fkFields = new ArrayList(fkeys.length);
        for (int i = 0; i < fkeys.length; i++) {
            for (int j = 0; j < fkeys[i].fieldPairs.length; j++) {
                fkFields.add(fkeys[i].fieldPairs[j].sourceFieldName.toUpperCase());
            }
        }
        String[] fkFieldsArray = new String[fkFields.size()];
        for (int i = 0; i < fkFieldsArray.length; i++) {
            fkFieldsArray[i] = (String) fkFields.get(i);
        }
        return fkFieldsArray;
    }

    public Metadata.EntitiesPair[] getAssociativeRelations() {
        return getEnclosingManager().getMetadata().getAssociativeRelations(_entityName);
    }

    public boolean isRelatedTo(String i_entity) {
        String[] related_entities = getRelatedEntities();
        for (int i = 0; i < related_entities.length; i++) {
            if (related_entities[i].equalsIgnoreCase(i_entity)) return true;
        }
        return false;
    }

    public boolean isAssociatedTo(String i_entity) {
        Metadata.EntitiesPair[] associated_entities = getAssociativeRelations();
        for (int i = 0; i < associated_entities.length; i++) {
            if (associated_entities[i].getAssociatedEntity().equalsIgnoreCase(i_entity)) return true;
        }
        return false;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getConstraintExpression()
	 */
    public DTOConstraintExpression getConstraintExpression() {
        return _constraintExpression;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#getNonPKFields()
	 */
    public String[] getNonPKFields() {
        String[] fields = getFieldNames();
        String[] nonPKFields = new String[fields.length - getPKey().pkFields.length];
        int dest = 0;
        for (int i = 0; i < fields.length; i++) {
            if (!isPK(fields[i])) {
                nonPKFields[dest] = fields[i];
                dest++;
            }
        }
        return nonPKFields;
    }

    public String[] getPKFields() {
        return getPKey().pkFields;
    }

    /**
	 * @see org.enjavers.jethro.api.dto.DTODescriptor#matchPropertyNames(String)
	 */
    public String[] matchPropertyNames(String i_regex) {
        ArrayList matchings = new ArrayList();
        String[] properties = getPropertyNames();
        for (int p = 0; p < properties.length; p++) {
            if (properties[p].matches(i_regex)) matchings.add(properties[p]);
        }
        String[] result = new String[matchings.size()];
        for (int r = 0; r < result.length; r++) {
            result[r] = (String) matchings.get(r);
        }
        return result;
    }

    public String[] getNonPKProperties() {
        String[] fields = getFieldNames();
        String[] nonPKProperties = new String[fields.length - getPKey().pkFields.length];
        int dest = 0;
        for (int i = 0; i < fields.length; i++) {
            if (!isPK(fields[i])) {
                nonPKProperties[dest] = getPropertyName(fields[i]);
                dest++;
            }
        }
        return nonPKProperties;
    }

    public String[] getPKProperties() {
        String[] pkFields = getPKey().pkFields;
        String[] pkProps = new String[pkFields.length];
        for (int p = 0; p < pkProps.length; p++) {
            pkProps[p] = getPropertyName(pkFields[p]);
        }
        return pkProps;
    }

    public boolean isTemporary() {
        return (_DTOClass == null || _fieldNames == null || _descriptiveProperties == null || _pk == null || _foreignKeys == null);
    }

    /**
	 * @see org.enjavers.jethro.api.Manageable#getEnclosingManager()
	 */
    public JethroManager getEnclosingManager() {
        return _enclosingManager;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
