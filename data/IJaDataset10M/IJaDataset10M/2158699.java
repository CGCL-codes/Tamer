package monet.editors.model;

import java.util.HashMap;
import java.util.Map;

public class DeclarationConstants {

    public static String DECLARATION_FORM = "form";

    public static String DECLARATION_COLLECTION = "collection";

    public static String DECLARATION_CONTAINER = "container";

    public static String DECLARATION_DOCUMENT = "document";

    public static String DECLARATION_SERVICE = "service";

    public static String DECLARATION_TASK = "task";

    public static String DECLARATION_ROLE = "role";

    private static final String[] MAIN_DECLARATIONS = { DECLARATION_COLLECTION, DECLARATION_CONTAINER, DECLARATION_DOCUMENT, DECLARATION_FORM, DECLARATION_ROLE, DECLARATION_SERVICE, DECLARATION_TASK };

    public static String DECLARATION_BEHAVIOR_FORM = "behaviorForm";

    public static String DECLARATION_BEHAVIOR_COLLECTION = "behaviorCollection";

    public static String DECLARATION_BEHAVIOR_CONTAINER = "behaviorContainer";

    public static String DECLARATION_BEHAVIOR_DOCUMENT = "behaviorDocument";

    public static String DECLARATION_BEHAVIOR_ROLE = "behaviorRole";

    public static String DECLARATION_BEHAVIOR_SERVICE = "behaviorService";

    public static String DECLARATION_BEHAVIOR_TASK = "behaviorTask";

    private static final String[] BEHAVIORS = { DECLARATION_BEHAVIOR_COLLECTION, DECLARATION_BEHAVIOR_CONTAINER, DECLARATION_BEHAVIOR_DOCUMENT, DECLARATION_BEHAVIOR_ROLE, DECLARATION_BEHAVIOR_SERVICE, DECLARATION_BEHAVIOR_TASK };

    public static String DECLARATION_FIELD_SECTION = "field-section";

    public static String DECLARATION_FIELD_TEXT = "field-text";

    public static String DECLARATION_FIELD_NUMBER = "field-number";

    public static String DECLARATION_FIELD_BOOLEAN = "field-boolean";

    public static String DECLARATION_FIELD_DATE = "field-date";

    public static String DECLARATION_FIELD_MEMO = "field-memo";

    public static String DECLARATION_FIELD_FILE = "field-file";

    public static String DECLARATION_FIELD_PICTURE = "field-picture";

    public static String DECLARATION_FIELD_SELECT = "field-select";

    public static String DECLARATION_FIELD_SIGN = "field-sign";

    public static String DECLARATION_FIELD_LINK = "field-link";

    public static String DECLARATION_FIELD_STAMP = "field-stamp";

    public static String DECLARATION_FIELD_CHECK = "field-check";

    public static String DECLARATION_FIELD_PATTERN = "field-pattern";

    public static String DECLARATION_FIELD_THESAURUS = "field-thesaurus";

    private static final String[] FIELD_DECLARATIONS = { DECLARATION_FIELD_TEXT, DECLARATION_FIELD_SIGN, DECLARATION_FIELD_SECTION, DECLARATION_FIELD_NUMBER, DECLARATION_FIELD_BOOLEAN, DECLARATION_FIELD_DATE, DECLARATION_FIELD_MEMO, DECLARATION_FIELD_FILE, DECLARATION_FIELD_PICTURE, DECLARATION_FIELD_SELECT, DECLARATION_FIELD_LINK, DECLARATION_FIELD_STAMP, DECLARATION_FIELD_CHECK, DECLARATION_FIELD_PATTERN, DECLARATION_FIELD_THESAURUS };

    public static String DECLARATION_BEHAVIOR_EVENT = "event";

    public static String DECLARATION_BEHAVIOR_OPERATION = "operation";

    public static String DECLARATION_BEHAVIOR_FORMULA = "formula";

    public static String DECLARATION_BEHAVIOR_VALIDATIONS = "validations";

    private static final String[] BEHAVIORS_DECLARATIONS = { DECLARATION_BEHAVIOR_EVENT, DECLARATION_BEHAVIOR_FORMULA, DECLARATION_BEHAVIOR_OPERATION, DECLARATION_BEHAVIOR_VALIDATIONS };

    private Map<String, Integer> declarationsId;

    private Map<String, Integer> behaviorId;

    private Map<String, Integer> fieldId;

    private Map<String, Integer> behaviorDeclId;

    public DeclarationConstants() {
        int i = 0;
        declarationsId = new HashMap<String, Integer>(7);
        for (int j = 0; j < MAIN_DECLARATIONS.length; j++, i++) declarationsId.put(MAIN_DECLARATIONS[j], i);
        behaviorId = new HashMap<String, Integer>(7);
        for (int j = 0; j < BEHAVIORS.length; j++, i++) behaviorId.put(BEHAVIORS[j], i);
        fieldId = new HashMap<String, Integer>(19);
        for (int j = 0; j < FIELD_DECLARATIONS.length; j++, i++) fieldId.put(FIELD_DECLARATIONS[j], i);
        behaviorDeclId = new HashMap<String, Integer>(4);
        for (int j = 0; j < BEHAVIORS_DECLARATIONS.length; j++, i++) behaviorDeclId.put(BEHAVIORS_DECLARATIONS[j], i);
    }

    public int getDeclarationId(String key) {
        if (declarationsId.containsKey(key)) return declarationsId.get(key).intValue();
        if (behaviorId.containsKey(key)) return behaviorId.get(key).intValue();
        if (fieldId.containsKey(key)) return fieldId.get(key).intValue();
        if (behaviorDeclId.containsKey(key)) return behaviorDeclId.get(key).intValue();
        return -1;
    }
}
