package commons.lang;

/**
 * TODO: Add description
 * @author Andrei Latyshau
 */
public interface Adaptable {

    boolean supportsAtaptionTo(Class objectClass);

    Object adaptTo(Class objectClass);
}
