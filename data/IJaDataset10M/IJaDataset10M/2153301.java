package cx.ath.mancel01.dependencyshot.reflect;

/**
 *
 * @author Mathieu ANCELIN
 */
public interface ReflectionService {

    Class getInterface(Class c);

    ReflectionClass getClassModel(Class c);
}
