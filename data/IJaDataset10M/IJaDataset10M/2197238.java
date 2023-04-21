package oracle.toplink.essentials.platform.xml;

import java.net.URL;

/**
 * A schema reference is used to access a schema in order to validate a
 * document.
 */
public interface XMLSchemaReference {

    public static final int COMPLEX_TYPE = 1;

    public static final int SIMPLE_TYPE = 2;

    public static final int ELEMENT = 3;

    public static final int GROUP = 5;

    /**
     * Returns the path to be traversed for validation purposes.
     *
     * @return a string represented the path to be traversed
     */
    public String getSchemaContext();

    /**
     * Indicates if the schema reference references a simple type definition,
     * complex type definition, element or group
     *
     * @return COMPLEX_TYPE=1, SIMPLE_TYPE=2, ELEMENT=3, GROUP=5
     */
    public int getType();

    /**
     * A URL which referenes the Schema.
     *
     * @return the schema URL
     */
    public URL getURL();
}
