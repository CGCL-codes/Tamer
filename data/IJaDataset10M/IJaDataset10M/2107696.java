package org.hydra.messages.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.hydra.deployers.ADeployer;
import org.hydra.html.fields.FieldInput;
import org.hydra.html.fields.FieldTextArea;
import org.hydra.html.fields.IField;
import org.hydra.messages.CommonMessage;
import org.hydra.messages.handlers.abstracts.AMessageHandler;
import org.hydra.messages.interfaces.IMessage;
import org.hydra.utils.Constants;
import org.hydra.utils.DBUtils;
import org.hydra.utils.ErrorUtils;
import org.hydra.utils.Utils;

public class AdmTemplates extends AMessageHandler {

    public IMessage list(CommonMessage inMessage) {
        if (!validateData(inMessage, "appid")) return inMessage;
        String appId = inMessage.getData().get("appid");
        String content = String.format("[[Application|Templates|%s|html]]", appId);
        getLog().debug("Try to get content for: " + content);
        return (ADeployer.deployContent(content, inMessage));
    }

    public IMessage addForm(CommonMessage inMessage) {
        if (!validateData(inMessage, "appid")) return inMessage;
        String appID = inMessage.getData().get("appid");
        ArrayList<IField> fields = new ArrayList<IField>();
        fields.add(new FieldInput("template_key", "", 35));
        fields.add(new FieldTextArea("template_content", "", "style=\"width: 40em; height: 25em; border: 1px solid #7F9DB9;\""));
        String form = Utils.generateForm(String.format("<h4>[[DB|Text|New_Template|span]]</h4>"), appID, "AdmTemplates", "add", "AdmTemplates", "list", Constants._admin_app_action_div, fields, null, inMessage);
        return (ADeployer.deployContent(form, inMessage));
    }

    public IMessage updateForm(CommonMessage inMessage) {
        if (!validateData(inMessage, "appid", "key")) return inMessage;
        String appID = inMessage.getData().get("appid");
        String key = inMessage.getData().get("key");
        String cfName = "Template";
        List<String> errorFields = new ArrayList<String>();
        List<ErrorUtils.ERROR_CODES> errorCodes = new ArrayList<ErrorUtils.ERROR_CODES>();
        Map<String, String> cols = DBUtils.testForExistenceKey(errorFields, errorCodes, appID, cfName, key, "NULL");
        if (errorCodes.size() != 0) {
            inMessage.setError("Key: " + key + " not exist!");
            inMessage.clearContent();
            return (inMessage);
        }
        String content = (cols.containsKey("content") ? cols.get("content") : "");
        ArrayList<IField> fields = new ArrayList<IField>();
        fields.add(new FieldInput("template_key", key, 35, true));
        fields.add(new FieldTextArea("template_content", "__1__", "style=\"width: 40em; height: 25em; border: 1px solid #7F9DB9;\""));
        String form = Utils.generateForm(String.format("<h4>[[DB|Text|Update_Template|span]]</h4>"), appID, "AdmTemplates", "update", "AdmTemplates", "list", Constants._admin_app_action_div, fields, null, inMessage);
        form = form.replaceAll("__1__", Matcher.quoteReplacement(content));
        ADeployer.deployContent(form, inMessage);
        return (inMessage);
    }

    public IMessage add(CommonMessage inMessage) {
        String[] mandatoryFields = { "appid", "template_key", "template_content" };
        if (!validateData(inMessage, mandatoryFields)) return inMessage;
        List<String> errorFields = new ArrayList<String>();
        List<ErrorUtils.ERROR_CODES> errorCodes = new ArrayList<ErrorUtils.ERROR_CODES>();
        String appID = inMessage.getData().get("appid");
        String cfName = "Template";
        getLog().debug("Test for valid key");
        String template_key = inMessage.getData().get("template_key").trim();
        String template_content = inMessage.getData().get("template_content").trim();
        Utils.testFieldKey(errorFields, errorCodes, template_key, "template_key", Constants._max_input_field_limit);
        Utils.testFieldKey(errorFields, errorCodes, template_content, "template_content", Constants._max_textarea_field_limit);
        if (errorCodes.size() == 0) {
            getLog().debug("Test for user existence");
            DBUtils.testForNonExistenceOfKey(errorFields, errorCodes, appID, cfName, template_key, "template_key");
        }
        if (errorFields.size() != 0) {
            getLog().error("Errors found");
            inMessage.clearContent();
            inMessage.setHighlightFields(errorFields);
            inMessage.setNoHighlightFields(mandatoryFields);
            inMessage.setError(Utils.getErrorDescription(errorFields, errorCodes));
            return inMessage;
        }
        getLog().debug("Create key/value data map");
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("content", inMessage.getData().get("template_content"));
        getLog().debug("All ok, try to add new data...");
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            ErrorUtils.ERROR_CODES errorCode = DBUtils.setValue(appID, cfName, template_key, entry.getKey(), entry.getValue());
            if (errorCode != ErrorUtils.ERROR_CODES.NO_ERROR) {
                inMessage.setError("Error: " + errorCode);
                return inMessage;
            }
        }
        return list(inMessage);
    }

    public IMessage update(CommonMessage inMessage) {
        String[] mandatoryFields = { "appid", "template_key", "template_content" };
        if (!validateData(inMessage, mandatoryFields)) return inMessage;
        List<String> errorFields = new ArrayList<String>();
        List<ErrorUtils.ERROR_CODES> errorCodes = new ArrayList<ErrorUtils.ERROR_CODES>();
        String appID = inMessage.getData().get("appid");
        String cfName = "Template";
        getLog().debug("Test for valid key");
        String template_key = inMessage.getData().get("template_key").trim();
        String template_content = inMessage.getData().get("template_content").trim();
        Utils.testFieldKey(errorFields, errorCodes, template_key, "template_key", Constants._max_input_field_limit);
        Utils.testFieldKey(errorFields, errorCodes, template_content, "template_content", Constants._max_textarea_field_limit);
        if (errorCodes.size() == 0) {
            getLog().debug("Test for user existence");
            DBUtils.testForExistenceKey(errorFields, errorCodes, appID, cfName, template_key, "template_key");
        }
        if (errorFields.size() != 0) {
            getLog().error("Errors found");
            inMessage.clearContent();
            inMessage.setHighlightFields(errorFields);
            inMessage.setNoHighlightFields(mandatoryFields);
            inMessage.setError(Utils.getErrorDescription(errorFields, errorCodes));
            return inMessage;
        }
        getLog().debug("Create key/value data map");
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("content", inMessage.getData().get("template_content"));
        getLog().debug("All ok, try to add new data...");
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            ErrorUtils.ERROR_CODES errorCode = DBUtils.setValue(appID, cfName, template_key, entry.getKey(), entry.getValue());
            if (errorCode != ErrorUtils.ERROR_CODES.NO_ERROR) {
                inMessage.setError("Error: " + errorCode);
                return inMessage;
            }
        }
        return list(inMessage);
    }

    public IMessage delete(CommonMessage inMessage) {
        if (!validateData(inMessage, "appid", "key")) return inMessage;
        String appId = inMessage.getData().get("appid");
        String key = inMessage.getData().get("key");
        ErrorUtils.ERROR_CODES errCode = DBUtils.deleteKey(appId, "Template", key);
        if (errCode != ErrorUtils.ERROR_CODES.NO_ERROR) {
            inMessage.setError(errCode.toString());
            return inMessage;
        }
        return (list(inMessage));
    }
}
