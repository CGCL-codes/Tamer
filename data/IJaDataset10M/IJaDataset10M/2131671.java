package redora.client.validation;

import com.google.gwt.json.client.JSONObject;
import redora.client.Persistable;
import redora.client.mvp.ClientFactory;
import redora.client.util.Field;
import redora.client.util.GWTViewUtil;

public class BusinessRuleViolation {

    public Class<? extends Persistable> cls;

    public Field field;

    public int ruleId;

    public String message;

    private BusinessRuleViolation(String className, String fieldName, int ruleId, String message, ClientFactory clientFactory) {
        this.cls = clientFactory.getLocator().locateClass(className);
        this.field = GWTViewUtil.from(fieldName, clientFactory.getLocator().locateFields(cls));
        this.ruleId = ruleId;
        this.message = message;
    }

    private BusinessRuleViolation(String className, String fieldName, String fieldClassName, int ruleId, String message, ClientFactory clientFactory) {
        this.cls = clientFactory.getLocator().locateClass(className);
        Class fieldCls;
        if (fieldClassName != null) fieldCls = clientFactory.getLocator().locateClass(fieldClassName); else fieldCls = cls;
        this.field = GWTViewUtil.from(fieldName, clientFactory.getLocator().locateFields(fieldCls));
        this.ruleId = ruleId;
        this.message = message;
    }

    public static BusinessRuleViolation from(JSONObject json, ClientFactory clientFactory) {
        String objectName = json.get("persistable").isString().stringValue();
        int ruleId = Integer.parseInt(json.get("businessRuleId").toString());
        return new BusinessRuleViolation(objectName, json.containsKey("field") ? json.get("field").isString().stringValue() : null, json.containsKey("fieldClass") ? json.get("fieldClass").isString().stringValue() : null, ruleId, clientFactory.getLocator().locateMessage().rule(objectName, ruleId, json.containsKey("arguments") ? json.get("arguments").isArray() : null), clientFactory);
    }

    public String toString() {
        return cls.getName() + (field == null ? "" : "." + field.name) + " " + ruleId;
    }
}
