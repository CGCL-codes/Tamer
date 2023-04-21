package recoder.bytecode;

import java.util.List;
import recoder.abstraction.EnumConstant;

/**
 * @author Tobias Gutzmann
 *
 */
public class EnumConstantInfo extends FieldInfo implements EnumConstant {

    /**
	 * @param accessFlags
	 * @param name
	 * @param type
	 * @param cf
	 * @param constantValue
	 * @param typeArgs
	 */
    public EnumConstantInfo(int accessFlags, String name, String type, ClassFile cf, String constantValue, List<TypeArgumentInfo> typeArgs) {
        super(accessFlags, name, type, false, cf, constantValue, typeArgs);
    }
}
