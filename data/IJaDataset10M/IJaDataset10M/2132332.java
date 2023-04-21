package org.magicbox.validator;

import org.magicbox.domain.AnnuncioImpl;
import org.magicbox.domain.Annuncio;
import org.magicbox.util.ValidatorUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Classe di per validazione feed
 * 
 * @author Massimiliano Dess� (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
@SuppressWarnings("unchecked")
public class RssValidator implements Validator {

    public boolean supports(Class clazz) {
        return AnnuncioImpl.class.isAssignableFrom(clazz);
    }

    public void validate(Object command, Errors errors) {
        Annuncio spedizione = (Annuncio) command;
        ValidatorUtils.checkMinLenghtVar(spedizione.getContenuto(), errors, 4, "contenuto");
    }
}
