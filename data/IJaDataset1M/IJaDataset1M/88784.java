package com.reserveamerica.jirarmi.impl;

import org.ofbiz.core.entity.GenericValue;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.reserveamerica.jirarmi.exceptions.JiraException;

/**
 * @author bstasyszyn
 *
 */
public class RendererUtil {

    /**
   * 
   * @param issue
   * @param issueValue
   * @param fieldId
   * @return The rendered content.
   * @throws JiraException
   */
    public static String getRenderedContent(Issue issue, GenericValue issueValue, String fieldId) throws JiraException {
        String rawValue;
        if (issueValue.containsKey(fieldId)) {
            rawValue = issueValue.getString(fieldId);
        } else {
            CustomField customField = ComponentManager.getInstance().getCustomFieldManager().getCustomFieldObject(fieldId);
            if (customField == null) {
                throw new JiraException("Field not found");
            }
            Object value = issue.getCustomFieldValue(customField);
            if (value instanceof String) {
                rawValue = (String) value;
            } else {
                rawValue = null;
            }
        }
        return getRenderedContent(issue, fieldId, rawValue);
    }

    /**
   * 
   * @param issue
   * @param issueValue
   * @param fieldId
   * @return
   * @throws JiraException
   */
    public static String getRenderedContent(Issue issue, String fieldId, String rawValue) throws JiraException {
        if (rawValue == null) {
            return null;
        }
        FieldLayoutItem fieldLayoutItem = JiraManagers.fieldLayoutManager.getFieldLayout(issue.getProjectObject(), issue.getIssueTypeObject().getId()).getFieldLayoutItem(fieldId);
        if (fieldLayoutItem == null) {
            return null;
        }
        return JiraManagers.rendererManager.getRenderedContent(fieldLayoutItem.getRendererType(), rawValue, issue.getIssueRenderContext());
    }
}
