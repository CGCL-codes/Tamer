package net.sf.djdoc.bo;

/**
 * The instances of the <code>Api</code> interface represent Java APIs documented
 * with the Javadoc tool.
 */
public interface Api {

    /**
   * Getter method for this <code>Api</code>'s name.
   *
   * @return The name of this <code>Api</code>
   */
    String getName();

    /**
   * Getter method for this <code>Api</code>'s description.
   *
   * @return The description of this <code>Api</code>
   */
    String getDescription();

    /**
   * Getter method for this <code>Api</code>'s path.
   *
   * @return The path of this <code>Api</code>
   */
    String getPath();

    /**
   * Getter method for this <code>Api</code>'s title.
   *
   * @return The title of this <code>Api</code>
   */
    String getTitle();

    /**
   * Getter method for this <code>Api</code>'s stylesheet name.
   *
   * @return The stylesheet name of this <code>Api</code>
   */
    String getStylesheetName();
}
