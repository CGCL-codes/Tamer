package mimosa.ontology;

import java.util.List;
import mimosa.table.TypeDescription;

/**
 *
 * @author Jean-Pierre Muller
 */
public class IndividualDescription implements TypeDescription {

    /**
	 * @see mimosa.table.TypeDescription#checkType(java.lang.Object)
	 */
    public boolean checkType(Object value) {
        return value instanceof Individual;
    }

    /**
	 * @see mimosa.table.TypeDescription#getCompatibleTypes()
	 */
    public List<Object> getCompatibleTypes() {
        return null;
    }

    /**
	 * @see mimosa.table.TypeDescription#getType()
	 */
    public Object getType() {
        return null;
    }

    /**
	 * @see mimosa.table.TypeDescription#getTypeName()
	 */
    public String getTypeName() {
        return "Entity";
    }

    /**
	 * @see mimosa.table.TypeDescription#isSubTypeOf(mimosa.table.TypeDescription)
	 */
    public boolean isSubTypeOf(TypeDescription type) {
        return false;
    }
}
