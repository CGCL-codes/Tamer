package fr.gouv.defense.terre.esat.formathlon.entity.validation.validator;

import fr.gouv.defense.terre.esat.formathlon.entity.validation.annotation.Length;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator permettant de valider l'annotation Length.
 * 
 * @author maxime.guinchard
 * @version 1.0
 */
public class LengthValidator implements ConstraintValidator<Length, String> {

    /**
     * Valeur min.
     */
    private int min;

    /**
     * Valeur max.
     */
    private int max;

    /**
     * Initialise les donneess.
     * @param constraintAnnotation Donnees de l'annotation.
     */
    @Override
    public void initialize(Length constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    /**
     * Valide une description.
     * True si tout est ok. Sinon false.
     * @param value Description
     * @param context Contexte
     * @return boolean
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            if (value.length() < min) {
                return false;
            }
            if (value.length() > max) {
                return false;
            }
        }
        return true;
    }
}
