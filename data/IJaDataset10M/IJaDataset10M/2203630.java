package it.dangelo.saj.validation.json_vl;

public interface ArrayValidator extends Validator {

    Validator getItem();

    Integer getMaxLength();

    Integer getMinLength();

    Integer getLength();

    boolean canContainsNull();
}
