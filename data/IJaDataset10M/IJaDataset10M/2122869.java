package grog.validation;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Marco Brambilla
 */
public class ValidatorManager {

    public static enum LANGUAGE {

        PROLOG
    }

    ;

    private static EnumMap<LANGUAGE, Validator> validators;

    private static Map<LANGUAGE, Validator> table = new EnumMap<LANGUAGE, Validator>(LANGUAGE.class);

    static {
        Validator v = new PrologValidator();
        v.loadKnowledgeBase();
        table.put(LANGUAGE.PROLOG, v);
    }

    public static Validator getValidator(LANGUAGE language) {
        return table.get(language);
    }

    public static String getTheoryEditorName(LANGUAGE language) {
        return table.get(language).getCodeEditorName();
    }

    public static String getDefaultEmptyTheory(LANGUAGE language) {
        return table.get(language).getDefaultEmptyCode();
    }
}
