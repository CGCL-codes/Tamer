package es.rvp.java.simpletag.core.validators;

/**
 * Retorna siempre cierto.
 *
 * @author Rodrigo Villamil Perez
 */
public class ValidatorNull implements Validator<String> {

    public boolean validate(final String data) {
        return true;
    }
}
