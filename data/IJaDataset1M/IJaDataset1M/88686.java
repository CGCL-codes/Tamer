package org.equanda.validation;

/**
 * CaseConverter to always convert to upper case.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class UpperCaseConverter implements CaseConverter {

    public boolean isCaseConversionUpper() {
        return true;
    }

    public boolean isCaseConversionLower() {
        return false;
    }
}
