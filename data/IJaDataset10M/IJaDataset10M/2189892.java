package com.lizardtech.djvu;

/**
 * <p>
 * This class is an abstraction of all hyperlinks used by the viewer.
 * </p>
 * 
 */
public interface Hyperlink {

    /**
   * Get optional URL which this map area can be associated with. If it's not
   * empty then clicking this map area with the mouse will make the browser
   * load the HTML page referenced by this url.  Note: This may also be a
   * relative URL.
   *
   * @return the URL string
   */
    public abstract String getURL();

    /**
   * Get the target for the URL. Standard targets are:<br>
   * <b>_blank</b> - Load the link in a new blank window<br>
   * <b>_self</b> - Load the link into the plugin window<br>
   * <b>_top</b> - Load the link into the top-level frame<br>
   *
   * @return DOCUMENT ME!
   */
    public abstract String getTarget();
}
