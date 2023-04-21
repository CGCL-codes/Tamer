package it.dangelo.saj.validation.json_vl;

import it.dangelo.saj.SAJException;

public interface DateValidator extends Validator {

    DateType getDateType();

    DateElement[] getEnumerations();

    DateElement getMaxExclusive();

    DateElement getMaxInclusive();

    DateElement getMinExclusive();

    DateElement getMinInclusive();

    String format();

    DateElement getDefault();

    String getDefaultString() throws SAJException;
}
