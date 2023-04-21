package seco.classloader;

/**
 * 
 * <p>
 * This interface represents an entity that is capable of retrieving a Java class
 * by its name.
 * </p>
 *
 * @author Borislav Iordanov
 *
 */
public interface ClassFinder {

    ClassInfo findClassInfo();
}
