package org.limmen.crs.model;

/**
 * The Documentation class is used all over the configuration model and it's
 * purpose is to provide information on all the parts of the configuration
 * (the file, sections and settings) in several languages.
 *
 * @author Ivo Limmen
 * @see Configuration
 * @see Section
 * @see Setting
 */
public class Documentation {

    private String language;

    private String text;

    public Documentation() {
        this(null, null);
    }

    public Documentation(String aLanguage, String aText) {
        super();
        setLanguage(aLanguage);
        setText(aText);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(getClass())) {
            Documentation other = (Documentation) obj;
            if (getLanguage().equals(other.getLanguage())) {
                if (getText().equals(other.getText())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setText(String text) {
        this.text = text;
    }
}
