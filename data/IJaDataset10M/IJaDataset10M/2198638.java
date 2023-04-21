package eu.actorsproject.xlim.type;

import eu.actorsproject.xlim.XlimType;

public interface TypeFactory {

    /**
	 * @param typeName
	 * @return TypeKind of the given typeName (or null if the type 
	 *         doesn't exist/isn't registered with the TypeFactory)
	 *         
	 * The returned TypeKind represents an unparameteric type (kind T)
	 * or type type constructor of a parametric type (kind typeArg->T).
	 */
    TypeKind getTypeKind(String typeName);

    boolean supportsType(String typeName);

    XlimType leastUpperBound(XlimType t1, XlimType t2);

    XlimType createInteger(int size);

    XlimType createBoolean();

    XlimType create(String typeName);

    /**
	 * @param elementType  type parameter of List (element type)
	 * @param size         length of List
	 * @return             the instance List(type,size)
	 */
    XlimType createList(XlimType elementType, int size);
}
