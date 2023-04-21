package package1;

import java.util.ArrayList;
import java.util.List;
import tudresden.ocl20.pivot.modelinstance.IModelInstance;

/**
 * <p>
 * Provider class to load a Java {@link IModelInstance} for testing.
 * </p>
 * 
 * @author Claas Wilke
 */
public class ModelInstance01ProviderClass {

    /**
	 * <p>
	 * Returns a {@link List} of {@link Object}s that are part of the
	 * {@link IModelInstance}.
	 * </p>
	 * 
	 * @return A {@link List} of {@link Object}s that are part of the
	 *         {@link IModelInstance}.
	 */
    public static List<Object> getModelObjects() {
        List<Object> result;
        result = new ArrayList<Object>();
        package1.package2.Type1 package1package2type1instance01;
        package1package2type1instance01 = new package1.package2.Type1();
        result.add(package1package2type1instance01);
        package1.package2.Type2 package1package2type2instance01;
        package1package2type2instance01 = new package1.package2.Type2();
        result.add(package1package2type2instance01);
        package1.Type2 package1type2instance01;
        package1type2instance01 = new package1.Type2();
        result.add(package1type2instance01);
        return result;
    }
}
