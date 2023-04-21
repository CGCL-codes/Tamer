package org.databene.benerator.anno;

/**
 * Interface for resolving resource paths for test case classes.<br/><br/>
 * Created: 12.12.2011 12:36:14
 * @since 0.7.4
 * @author Volker Bergmann
 */
public interface PathResolver {

    String getPathFor(String uri, Class<?> testClass);
}
