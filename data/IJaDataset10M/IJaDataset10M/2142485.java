package cz.cvut.phone.core.dto;

import java.io.Serializable;

/**
 *
 * @author Frantisek Hradil
 */
public class LanguageDTO implements Serializable {

    private String languageID;

    public LanguageDTO() {
    }

    public LanguageDTO(String languageID) {
        this.languageID = languageID;
    }

    public String getLanguageID() {
        return languageID;
    }

    public void setLanguageID(String languageID) {
        this.languageID = languageID;
    }
}
