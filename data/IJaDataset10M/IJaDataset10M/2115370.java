package org.gaea.lib.struct.select.attribute;

/**
 * attribute orderby "order by attribute(asc|desc)?"
 * 
 * @author fslabranche
 */
public class AttributeOrderBy extends Attribute {

    /**
	 * attribute to order by
	 */
    private Attribute _attribute;

    /**
	 * sort ascending or not
	 */
    private boolean _ascending;

    /**
	 * create an orderBy AttributeBD(asc|desc)
	 * 
	 * @param attribute
	 *            AttributeBD
	 * @param ascending
	 *            boolean
	 */
    public AttributeOrderBy(Attribute attribute, boolean ascending) {
        _attribute = attribute;
        _ascending = ascending;
    }

    /**
	 * set the Attribute
	 * 
	 * @param anAttribute
	 *            Attribute
	 */
    public void setAttribute(Attribute anAttribute) {
        _attribute = anAttribute;
    }

    /**
	 * get the attribute
	 * 
	 * @return Attribute
	 */
    public Attribute getAttribute() {
        return _attribute;
    }

    /**
	 * get if the attribute is order ascending or descending
	 * 
	 * @return boolean, true if ascending, false if descending
	 */
    public boolean isAscending() {
        return _ascending;
    }

    @Override
    public Attribute clone() {
        return new AttributeOrderBy(_attribute.clone(), _ascending);
    }

    @Override
    public boolean equals(Attribute anAttribute) {
        if (anAttribute.getType() == getType()) {
            if (((AttributeOrderBy) anAttribute).getAttribute().equals(getAttribute()) && ((AttributeOrderBy) anAttribute).isAscending() == _ascending) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AttributeType getType() {
        return AttributeType.ORDERBY;
    }

    @Override
    public String toString() {
        return _attribute.toString() + "(" + (_ascending ? "asc" : "desc") + ")";
    }

    @Override
    public OQLPredicateType getOQLPredicateType() {
        return _attribute.getOQLPredicateType();
    }
}
