package org.gaea.lib.struct.select.attribute;

/**
 * Attribute Identifier
 * 
 * @author fslabranche
 */
public class AttributeIdentifier extends Attribute {

    /**
	 * type of attribute identifier
	 * 
	 * @author fslabranche
	 */
    public enum IdentifierType {

        IDENTIFIER_ATTRIBUTESIMPLE, IDENTIFIER_ATTRIBUTEARRAY, IDENTIFIER_FUNCTION
    }

    /**
	 * identifier type
	 */
    private IdentifierType _identifierType;

    /**
	 * identifier name
	 */
    private Attribute _identifier;

    /**
	 * list of parameter for IDENTIFIER_ATTRIBUTEARRAY and IDENTIFIER_FUNCTION
	 */
    AttributeArrayArguments _listParameter;

    /**
	 * child of the identifier user(2).name
	 */
    private AttributeIdentifier _child = null;

    /**
	 * create an AttributeIdentifier
	 * 
	 * @param identifier
	 *            String
	 */
    public AttributeIdentifier(String identifier) {
        this(new AttributeIdentifierName(identifier), new AttributeArrayArguments(IdentifierType.IDENTIFIER_ATTRIBUTESIMPLE), IdentifierType.IDENTIFIER_ATTRIBUTESIMPLE);
    }

    /**
	 * create an AttributeIdentifier
	 * 
	 * @param identifier
	 *            Attribute
	 * @param listParameter
	 *            Vector<Attribute>
	 * @param type
	 *            IdentifierType
	 */
    public AttributeIdentifier(String identifier, AttributeArrayArguments listParameter, IdentifierType type) {
        this(new AttributeIdentifierName(identifier), listParameter, type);
    }

    /**
	 * create an AttributeIdentifier
	 * 
	 * @param identifier
	 *            Attribute
	 * @param listParameter
	 *            Vector<Attribute>
	 * @param type
	 *            IdentifierType
	 */
    public AttributeIdentifier(Attribute identifier, AttributeArrayArguments listParameter, IdentifierType type) {
        _identifier = identifier;
        _listParameter = listParameter;
        _identifierType = type;
    }

    /**
	 * modify the current identifier
	 * 
	 * @param identifier
	 *            AttributeIdentifier
	 */
    public void setIdentifierClass(Attribute identifier) {
        _identifier = identifier;
        _listParameter = new AttributeArrayArguments(IdentifierType.IDENTIFIER_ATTRIBUTESIMPLE);
        _identifierType = IdentifierType.IDENTIFIER_ATTRIBUTESIMPLE;
    }

    /**
	 * get the identifier name
	 * 
	 * @return Attribute
	 */
    public Attribute getIdentifier() {
        return _identifier;
    }

    /**
	 * get the identifier type
	 * 
	 * @return IdentifierType
	 */
    public IdentifierType getIdentifierType() {
        return _identifierType;
    }

    /**
	 * get the list of parameter for IDENTIFIER_ATTRIBUTEARRAY and
	 * IDENTIFIER_FUNCTION
	 * 
	 * @return Vector<Attribute>
	 */
    public AttributeArrayArguments getListParameter() {
        return _listParameter;
    }

    /**
	 * verify if has parameter for IDENTIFIER_ATTRIBUTEARRAY and
	 * IDENTIFIER_FUNCTION
	 * 
	 * @return boolean
	 */
    public boolean hasParemeter() {
        return _listParameter.getArrayArg().size() != 0;
    }

    /**
	 * verify if has child identifier
	 * 
	 * @return boolean
	 */
    public boolean hasChild() {
        return (_child == null ? false : true);
    }

    /**
	 * set the identifier child
	 * 
	 * @param anAttributeIdentifier
	 *            AttributeIdentifier
	 */
    public void setChild(AttributeIdentifier anAttributeIdentifier) {
        if (hasChild()) {
            _child.setChild(anAttributeIdentifier);
        } else {
            _child = anAttributeIdentifier;
        }
    }

    /**
	 * set the identifier child
	 * 
	 * @param anAttributeIdentifier
	 *            AttributeIdentifier
	 */
    public void assignChild(AttributeIdentifier anAttributeIdentifier) {
        _child = anAttributeIdentifier;
    }

    /**
	 * get the identifier child
	 * 
	 * @return AttributeIdentifier
	 */
    public AttributeIdentifier getChild() {
        return _child;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Attribute clone() {
        AttributeIdentifier anAttribute = new AttributeIdentifier(_identifier, (AttributeArrayArguments) _listParameter.clone(), _identifierType);
        if (hasChild()) {
            anAttribute.setChild((AttributeIdentifier) getChild().clone());
        }
        return anAttribute;
    }

    @Override
    public boolean equals(Attribute anAttribute) {
        if (anAttribute.getType() == getType()) {
            if (((AttributeIdentifier) anAttribute).getIdentifier().equals(getIdentifier()) && ((AttributeIdentifier) anAttribute).getIdentifierType() == getIdentifierType()) {
                if (((AttributeIdentifier) anAttribute).hasParemeter() && hasParemeter()) {
                    if (!((AttributeIdentifier) anAttribute).getListParameter().equals(getListParameter())) {
                        return false;
                    }
                }
                if (((AttributeIdentifier) anAttribute).hasChild() && hasChild()) {
                    if (((AttributeIdentifier) anAttribute).getChild().equals(getChild())) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public AttributeType getType() {
        return AttributeType.IDENTIFIER;
    }

    @Override
    public String toString() {
        String out = _identifier.toString() + _listParameter.toString();
        out += (hasChild() ? "." + _child.toString() : "");
        return out;
    }

    @Override
    public OQLPredicateType getOQLPredicateType() {
        if (this.hasChild()) {
            return this.getChild().getOQLPredicateType();
        }
        if (this.getIdentifierType() == IdentifierType.IDENTIFIER_ATTRIBUTESIMPLE) {
            return OQLPredicateType.ATOMIC;
        }
        return OQLPredicateType.COMPLEX;
    }
}
