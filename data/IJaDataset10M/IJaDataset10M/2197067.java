package com.google.gwt.core.ext.typeinfo;

/**
 * @deprecated Formerly used to manage Javadoc-comment style metadata. Replaced
 *             by Java 1.5 annotations. All implementations now return empty
 *             arrays. This interface and all implementations methods will be
 *             removed in a future release.
 */
@Deprecated
public interface HasMetaData {

    /**
   * Gets each list of metadata for the specified tag name.
   * 
   * @deprecated Javadoc comment metadata has been deprecated in favor of proper
   *             Java annotations. See
   *             {@link HasAnnotations#getAnnotation(Class)} for equivalent
   *             functionality.
   */
    @Deprecated
    String[][] getMetaData(String tagName);

    /**
   * Gets the name of available metadata tags.
   * 
   * @deprecated Javadoc comment metadata has been deprecated in favor of proper
   *             Java annotations. The {@link HasAnnotations} interface does not
   *             support a mechanism to enumerate all of the annotations on a
   *             member; the type of the desired annotation must be known.
   */
    @Deprecated
    String[] getMetaDataTags();
}
