package org.webthree.dictionary;

import java.util.Locale;
import org.webthree.dictionary.help.SearchHelp;
import org.webthree.dictionary.meta.URN;
import org.webthree.dictionary.view.View;

/**
 * @author michael.gerzabek@gmx.net
 * 
 */
public interface Element extends DictionaryObject {

    final String ROLE = Element.class.getName();

    final String label_SHORT = "short";

    final String label_MEDIUM = "medium";

    final String label_LONG = "long";

    final String input_BUTTON = "button";

    final String input_TEXT = "text";

    final String input_INPUT = "input";

    final String input_DATE = "date";

    final String input_TIME = "time";

    final String input_CHECKBOX = "checkbox";

    final String input_SELECT = "selection";

    final String input_RADIO = "radio";

    final String input_F4 = "F4";

    final String input_ICON = "icon";

    final String input_IMAGE = "image";

    final String input_PASSWORD = "password";

    /**
     * Get the <code>Domain</code> of this Element.
     * @return
     */
    Domain getDomain();

    /**
     * Get the <code>Parameter</code>-Token for this Element.
     * @return
     */
    String getParameterToken();

    /**
     * Get the label for the denoted field.
     * 
     * @param locale {@see java.util.Locale}
     * @param type ( short | medium | long )
     * @return the Label for this <code>Element</code>
     */
    String getLabel(Locale locale, String type);

    /**
     * With default <code>type</code> 'short'.
     * @param locale
     * @return
     */
    String getLabel(Locale locale);

    /**
     * With default <code>Locale</code> 'de-DE'.
     * @param type
     * @return
     */
    String getLabel(String type);

    /**
     * With default <code>Locale</code> 'de-DE' and default
     * type 'long'.
     * @param type
     * @return
     */
    String getLabel();

    /**
     * Returns editable status - can be overwritten!
     * 
     * @return
     */
    boolean isEditable();

    View getView();

    /**
     * Get's the viewtype.
     * @return ( input | datum | selection | F4 | checkbox | text )
     */
    String viewType();

    /**
     * Deriven from <code>Domain</code>.
     * @return
     */
    int getLength();

    SearchHelp getSearchHelp();

    URN getUserHelp();

    URN getTechnicalHelp();
}
