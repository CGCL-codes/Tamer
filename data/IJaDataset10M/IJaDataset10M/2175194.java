package org.semtinel.core.data.api;

/**
 *
 * @author kai
 */
public interface Notation {

    Concept getConcept();

    Long getId();

    Language getLanguage();

    String getText();

    void setConcept(Concept concept);

    void setId(Long id);

    void setLanguage(Language language);

    void setText(String text);
}
