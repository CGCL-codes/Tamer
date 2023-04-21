package org.gaea.lib.struct.select.type;

/**
 * type set "set<Type>"
 * 
 * @author fslabranche
 */
public class TypeComplexeSet extends TypeComplexe {

    /**
	 * Type of set
	 */
    Type _type;

    /**
	 * create a TypeComplexeSet
	 * 
	 * @param aType
	 *            Type
	 */
    public TypeComplexeSet(Type aType) {
        _type = aType;
    }

    /**
	 * get the type of set
	 * 
	 * @return Type
	 */
    public Type getAttribute() {
        return _type;
    }

    @Override
    public Types getType() {
        return Types.SET;
    }

    @Override
    public Type clone() {
        return new TypeComplexeSet(_type.clone());
    }

    @Override
    public boolean equals(Type aType) {
        if (Types.SET == aType.getType()) {
            if (((TypeComplexeSet) aType).getAttribute().equals(getAttribute())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "set <" + _type.toString() + ">";
    }
}
