package br.com.caelum.stella.validation.ie;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.caelum.stella.MessageProducer;
import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.BaseValidator;
import br.com.caelum.stella.validation.DigitoVerificadorInfo;
import br.com.caelum.stella.validation.InvalidValue;
import br.com.caelum.stella.validation.RotinaDeDigitoVerificador;
import br.com.caelum.stella.validation.ValidadorDeDV;
import br.com.caelum.stella.validation.Validator;
import br.com.caelum.stella.validation.error.IEError;

class IEPernambucoNovaValidator implements Validator<String> {

    private static final int DVX_MOD = 11;

    private static final int DVY_MOD = 11;

    private static final String MISSING_ZEROS = "00000";

    private static final int DVX_POSITION = MISSING_ZEROS.length() + 8;

    private static final int DVY_POSITION = MISSING_ZEROS.length() + 9;

    private static final Integer[] DVX_MULTIPLIERS = IEConstraints.P2;

    private static final Integer[] DVY_MULTIPLIERS = IEConstraints.P13;

    private static final RotinaDeDigitoVerificador[] DVX_ROTINAS = { IEConstraints.Rotina.E, IEConstraints.Rotina.POS_IE };

    private static final RotinaDeDigitoVerificador[] DVY_ROTINAS = { IEConstraints.Rotina.E, IEConstraints.Rotina.POS_IE };

    private static final DigitoVerificadorInfo DVX_INFO = new DigitoVerificadorInfo(0, DVX_ROTINAS, DVX_MOD, DVX_MULTIPLIERS, DVX_POSITION);

    private static final DigitoVerificadorInfo DVY_INFO = new DigitoVerificadorInfo(0, DVY_ROTINAS, DVY_MOD, DVY_MULTIPLIERS, DVY_POSITION);

    private static final ValidadorDeDV DVX_CHECKER = new ValidadorDeDV(DVX_INFO);

    private static final ValidadorDeDV DVY_CHECKER = new ValidadorDeDV(DVY_INFO);

    private final boolean isFormatted;

    public static final Pattern FORMATED = Pattern.compile("(\\d{7})[-](\\d{2})");

    public static final Pattern UNFORMATED = Pattern.compile("(\\d{7})(\\d{2})");

    private static final String REPLACEMENT = MISSING_ZEROS + "$1$2";

    public IEPernambucoNovaValidator(MessageProducer messageProducer, boolean isFormatted) {
        this.baseValidator = new BaseValidator(messageProducer);
        this.isFormatted = isFormatted;
    }

    private List<InvalidValue> getInvalidValues(String IE) {
        List<InvalidValue> errors = new ArrayList<InvalidValue>();
        errors.clear();
        if (IE != null) {
            String unformatedIE = checkForCorrectFormat(IE, errors);
            if (errors.isEmpty()) {
                if (!hasValidCheckDigits(unformatedIE)) {
                    errors.add(IEError.INVALID_CHECK_DIGITS);
                }
            }
        }
        return errors;
    }

    private String checkForCorrectFormat(String ie, List<InvalidValue> errors) {
        String unformatedIE = null;
        if (isFormatted) {
            if (!(FORMATED.matcher(ie).matches())) {
                errors.add(IEError.INVALID_FORMAT);
            }
            unformatedIE = ie.replaceAll("\\D", "");
        } else {
            if (!UNFORMATED.matcher(ie).matches()) {
                errors.add(IEError.INVALID_DIGITS);
            }
            unformatedIE = ie;
        }
        return unformatedIE;
    }

    private boolean hasValidCheckDigits(String value) {
        String testedValue = null;
        Matcher matcher = UNFORMATED.matcher(value);
        if (matcher.matches()) {
            testedValue = matcher.replaceAll(REPLACEMENT);
        }
        boolean DVXisValid = DVX_CHECKER.isDVValid(testedValue);
        boolean DVYisValid = DVY_CHECKER.isDVValid(testedValue);
        return (DVXisValid) && (DVYisValid);
    }

    public boolean isEligible(String value) {
        boolean result;
        if (isFormatted) {
            result = FORMATED.matcher(value).matches();
        } else {
            result = UNFORMATED.matcher(value).matches();
        }
        return result;
    }

    private final BaseValidator baseValidator;

    public void assertValid(String cpf) {
        baseValidator.assertValid(getInvalidValues(cpf));
    }

    public List<ValidationMessage> invalidMessagesFor(String cpf) {
        return baseValidator.generateValidationMessages(getInvalidValues(cpf));
    }
}
