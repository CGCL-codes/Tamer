package org.engine.language;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.engine.Universe;

/**
 * The listener interface for receiving languageChange events. The class that is
 * interested in processing a languageChange event implements this interface,
 * and the object created with that class is registered with a component using
 * the component's <code>addlanguageChangeListener<code> method. When
 * the languageChange event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see languageChangeEvent
 */
public class LanguageChangeListener implements ActionListener {

    /** The change to. */
    private Language changeTo;

    /** The parent universe. */
    private Universe parentUniverse;

    /**
     * Instantiates a new language change listener.
     * 
     * @param changeTo
     *            the change to
     * @param parentUniverse
     *            the parent universe
     */
    public LanguageChangeListener(Language changeTo, Universe parentUniverse) {
        this.changeTo = changeTo;
        this.parentUniverse = parentUniverse;
    }

    public void actionPerformed(ActionEvent e) {
        parentUniverse.setLanguage(changeTo);
    }
}
