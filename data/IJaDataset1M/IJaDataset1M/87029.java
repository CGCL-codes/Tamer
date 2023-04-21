package org.dmd.dms;

import java.util.ArrayList;
import org.dmd.dmc.DmcAttribute;
import org.dmd.dmc.DmcAttributeInfo;
import org.dmd.dmc.DmcValueException;
import org.dmd.dms.generated.dmo.AttributeDefinitionDMO;
import org.dmd.dms.generated.dmw.AttributeDefinitionDMW;

public class AttributeDefinition extends AttributeDefinitionDMW {

    TypeDefinition typeDef;

    public ArrayList<ClassDefinition> usedByClasses;

    /**
     * Indicates the actions that include this attribute.
     */
    public ArrayList<ActionDefinition> usedByActions;

    DmcAttributeInfo attrInfo;

    /**
     * Default constructor.
     */
    public AttributeDefinition() {
        super(new AttributeDefinitionDMO(), MetaSchemaAG._AttributeDefinition);
        attrInfo = null;
    }

    public AttributeDefinition(AttributeDefinitionDMO obj) {
        super(obj);
        attrInfo = null;
    }

    /**
	 * Default constructor used in creating the meta schema.
	 * @param mn The meta name of the definition.
	 * @throws DmcValueException 
	 */
    AttributeDefinition(String n, TypeDefinition td) throws DmcValueException {
        super(n);
        typeDef = td;
        attrInfo = null;
    }

    protected AttributeDefinition(String mn) throws DmcValueException {
        super(mn);
        attrInfo = null;
    }

    /**
     * Adds a class to our list of classes that use this attribute.
     */
    void addUsingClass(ClassDefinition cd) {
        if (usedByClasses == null) {
            usedByClasses = new ArrayList<ClassDefinition>();
        }
        if (!usedByClasses.contains(cd)) {
            usedByClasses.add(cd);
        }
    }

    /**
     * Adds an action to our list of actions that use this attribute.
     */
    void addUsingAction(ActionDefinition ad) {
        if (usedByActions == null) {
            usedByActions = new ArrayList<ActionDefinition>();
        }
        if (!usedByActions.contains(ad)) {
            usedByActions.add(ad);
        }
    }

    /**
     * This method is used by parsers and deserializers to get attribute info for attributes
     * associated with auxiliary classes. It shouldn't be used for any other purpose.
     * @return The attribute info.
     */
    public DmcAttributeInfo getAttributeInfo() {
        if (attrInfo == null) attrInfo = new DmcAttributeInfo(getName().getNameString(), getDmdID(), getType().getName().getNameString(), getValueType(), getDataType());
        return (attrInfo);
    }

    public DmcAttribute<?> getAttributeInstance(DmcAttributeInfo ai) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        DmcAttribute<?> rc = (DmcAttribute<?>) getType().getAttributeHolder(ai);
        rc.setAttributeInfo(ai);
        return (rc);
    }

    public String getDMSAGReference() {
        return (getDefinedIn().getDMSASGName() + ".__" + getName());
    }

    public String getAdapterClassImport() {
        return (getType().getDefinedIn().getSchemaPackage() + ".generated.types.adapters." + getAdapterClassName());
    }

    public String getAdapterClassName() {
        String suffix = "";
        String REF = "";
        if (getType().getIsRefType() && (!getType().getIsExtendedRefType())) REF = "REF";
        switch(getValueType()) {
            case HASHMAPPED:
            case TREEMAPPED:
                suffix = "MAP";
                break;
            case HASHSET:
            case TREESET:
                suffix = "SET";
                break;
            case MULTI:
                suffix = "MV";
                break;
            case SINGLE:
                suffix = "SV";
                break;
        }
        return (getType().getName() + REF + suffix + "Adapter");
    }
}
