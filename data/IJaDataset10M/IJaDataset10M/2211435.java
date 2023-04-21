package org.wikipediacleaner.api.constants.wiki;

import java.awt.ComponentOrientation;

/**
 * Configuration for <a href="http://he.wikipedia.org/w/index.php">Hebrew wikipedia</a>.
 */
public final class WikipediaHe extends AbstractWikipediaSettings {

    /**
   * Constructor.
   */
    public WikipediaHe() {
        super("he", "ויקיפדיה העברית");
    }

    /**
   * @return Component orientation.
   */
    @Override
    public ComponentOrientation getComponentOrientation() {
        return ComponentOrientation.RIGHT_TO_LEFT;
    }
}
