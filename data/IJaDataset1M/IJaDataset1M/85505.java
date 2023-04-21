package jaxil;

/**
 * PathRouter split addresses
 * 
 * @since 23/10/2008
 * @version 1.0
 * @author gael
 */
public interface PathRouter {

    void setPath(String s);

    String getName();

    String getParent();

    String getPathParent();
}
