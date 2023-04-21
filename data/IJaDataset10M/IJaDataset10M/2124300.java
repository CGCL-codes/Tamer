package local.joogie.boogie.types;

/**
 * @author schaef
 *
 */
public class ArrArrayType extends BoogieArrayType {

    /**
	 * @param name
	 * @param nestedtype
	 */
    public ArrArrayType(String name, BoogieArrayType nestedtype) {
        super(name, BoogieTypeFactory.getIntType(), nestedtype);
    }
}
