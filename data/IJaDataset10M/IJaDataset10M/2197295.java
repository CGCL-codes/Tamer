package com.reserveamerica.jirarmi.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.GenericValue;
import org.ofbiz.core.util.UtilDateTime;
import com.atlassian.core.util.map.EasyMap;
import com.atlassian.jira.event.issue.IssueEventSource;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.fields.FieldAccessor;
import com.atlassian.jira.issue.fields.OrderableField;
import com.atlassian.jira.issue.fields.layout.field.FieldLayout;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutManager;
import com.atlassian.jira.issue.fields.screen.FieldScreenRenderLayoutItem;
import com.atlassian.jira.issue.fields.screen.FieldScreenRenderTab;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.jira.issue.util.IssueUpdateBean;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.jira.workflow.WorkflowTransitionUtil;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.reserveamerica.commons.Token;
import com.reserveamerica.jirarmi.IssueService;
import com.reserveamerica.jirarmi.Utils;
import com.reserveamerica.jirarmi.beans.issue.FieldRemote;
import com.reserveamerica.jirarmi.beans.issue.FieldValueRemote;
import com.reserveamerica.jirarmi.beans.issue.IssueDetailsRemote;
import com.reserveamerica.jirarmi.beans.issue.IssueRemote;
import com.reserveamerica.jirarmi.beans.issue.IssueTypeKind;
import com.reserveamerica.jirarmi.beans.issue.IssueTypeRemote;
import com.reserveamerica.jirarmi.beans.workflow.ActionRemote;
import com.reserveamerica.jirarmi.exceptions.AuthorizationException;
import com.reserveamerica.jirarmi.exceptions.CreateException;
import com.reserveamerica.jirarmi.exceptions.EntityNotFoundException;
import com.reserveamerica.jirarmi.exceptions.IssueNotFoundException;
import com.reserveamerica.jirarmi.exceptions.JiraException;
import com.reserveamerica.jirarmi.exceptions.VersionNotFoundException;
import com.reserveamerica.jirarmi.transformers.PermissionTransformer;
import com.reserveamerica.jirarmi.transformers.issue.FieldTransformer;
import com.reserveamerica.jirarmi.transformers.issue.FieldValueTransformer;
import com.reserveamerica.jirarmi.transformers.issue.IssueDetailsTransformer;
import com.reserveamerica.jirarmi.transformers.issue.IssueTransformer;
import com.reserveamerica.jirarmi.transformers.issue.IssueTypeConstTransformer;
import com.reserveamerica.jirarmi.transformers.user.UserTransformer;
import com.reserveamerica.jirarmi.transformers.workflow.ActionTransformer;

/**
 * @author BStasyszyn
 */
public class IssueServiceImpl implements IssueService {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(IssueServiceImpl.class);

    @Override
    public IssueDetailsRemote createIssue(Token token, IssueDetailsRemote issue) throws JiraException {
        try {
            Issue parentIssue = null;
            if (issue.getParentId() != null) {
                parentIssue = JiraUtils.getIssueObject(issue.getParentId());
            }
            Issue createdIssue = JiraManagers.issueFactory.getIssue(JiraManagers.issueManager.createIssue(RequestContext.getUser(), IssueDetailsTransformer.get().transform(issue)));
            if (parentIssue != null) {
                JiraManagers.subtaskManager.createSubTaskIssueLink(parentIssue, createdIssue, RequestContext.getUser());
            }
            return IssueDetailsTransformer.get().transform(createdIssue);
        } catch (com.atlassian.jira.exception.CreateException ex) {
            log.error("createIssue(" + token + ") - Unable to create issue.", ex);
            throw new CreateException("Unable to create issue. Details: " + ex.getMessage());
        }
    }

    @Override
    public void updateIssue(Token token, IssueDetailsRemote value, boolean sendMail) throws JiraException {
        MutableIssue issue = JiraUtils.getIssueObject(value.getId());
        if (issue == null) {
            throw new IssueNotFoundException(value.getId());
        }
        if (!JiraManagers.permissionsManager.hasPermission(Permissions.EDIT_ISSUE, issue, RequestContext.getUser())) {
            throw new AuthorizationException(PermissionTransformer.get().transform(Permissions.EDIT_ISSUE));
        }
        value.setUpdated(UtilDateTime.nowTimestamp());
        IssueDetailsTransformer.get().transform(issue, value);
        updateIssue(issue, sendMail, true);
    }

    @Override
    public IssueDetailsRemote getIssueByWorkflow(Token token, Long workFlowId) throws JiraException {
        try {
            GenericValue issue = JiraManagers.issueManager.getIssueByWorkflow(workFlowId);
            if (issue == null) {
                throw new IssueNotFoundException("Issue not found for workflow [" + workFlowId + "].", workFlowId);
            }
            return IssueDetailsTransformer.get().transform(issue);
        } catch (GenericEntityException ex) {
            log.error("getIssueByWorkflow(" + token + ") - Unable to get issue by workflow workflow [" + workFlowId + "].", ex);
            throw new JiraException("Unable to get issue by workflow workflow [" + workFlowId + "].");
        }
    }

    @Override
    public Collection<IssueRemote> getIssuesForProject(Token token, Long projectId) throws JiraException {
        try {
            JiraUtils.getProject(projectId);
            Collection<Long> issueIds = JiraManagers.issueManager.getIssueIdsForProject(projectId);
            Collection<IssueRemote> issues = new ArrayList<IssueRemote>(issueIds.size());
            for (Long issueId : issueIds) {
                Issue issue = JiraUtils.getIssueObject(issueId);
                if (JiraManagers.permissionsManager.hasPermission(Permissions.BROWSE, issue, RequestContext.getUser())) {
                    issues.add(IssueTransformer.get().transform(issue));
                } else if (log.isDebugEnabled()) {
                    log.debug("The user [" + RequestContext.getUser().getName() + "] does not have " + Permissions.BROWSE + " permission to for the issue [" + issue.getId() + "].");
                }
            }
            return issues;
        } catch (GenericEntityException ex) {
            log.error("getIssuesForProject(" + token + ") - Unable to get issues for project [" + projectId + "].", ex);
            throw new JiraException("Unable to get issues for project [" + projectId + "]");
        }
    }

    @Override
    public IssueDetailsRemote getIssue(Token token, Long issueId) throws JiraException {
        return IssueDetailsTransformer.get().transform(JiraUtils.getIssueObject(issueId));
    }

    @Override
    public IssueDetailsRemote getIssue(Token token, String key) throws JiraException {
        return IssueDetailsTransformer.get().transform(JiraUtils.getIssueObjectByKey(key));
    }

    @Override
    public Long getIssueId(Token token, String key) throws JiraException {
        return JiraUtils.getIssueObjectByKey(key).getId();
    }

    @Override
    public Set<String> getIssueWatchers(Token token, Long issueId) throws EntityNotFoundException, JiraException {
        return UserTransformer.get().toNames(JiraManagers.issueManager.getWatchers(JiraUtils.getIssueObject(issueId)));
    }

    @Override
    public List<IssueRemote> getIssues(Token token, Set<Long> issueIds) throws JiraException {
        List<IssueRemote> values = new ArrayList<IssueRemote>(issueIds.size());
        for (Long issueId : issueIds) {
            try {
                values.add(IssueTransformer.get().transform(JiraUtils.getIssueObject(issueId)));
            } catch (IssueNotFoundException ex) {
                log.warn("getIssues(" + token + ") - " + ex.getMessage());
            }
        }
        return values;
    }

    public Collection<IssueRemote> getAffectsIssues(Token token, Long... versionIds) throws JiraException, RemoteException {
        return getAffectsIssues(token, Utils.toSet(versionIds));
    }

    @Override
    public Collection<IssueRemote> getAffectsIssues(Token token, Set<Long> versionIds) throws JiraException, RemoteException {
        try {
            Map<Long, Issue> issues = new LinkedHashMap<Long, Issue>();
            for (Long versionId : versionIds) {
                try {
                    for (GenericValue issueValue : JiraManagers.versionManager.getAffectsIssues(JiraUtils.getVersionObject(versionId))) {
                        Issue issue = JiraManagers.issueFactory.getIssue(issueValue);
                        issues.put(issue.getId(), issue);
                    }
                } catch (VersionNotFoundException ex) {
                    log.warn("getAffectsIssues(" + token + ") - " + ex.getMessage());
                }
            }
            return IssueTransformer.get().transform(issues.values());
        } catch (GenericEntityException ex) {
            log.error("getAffectsIssues(" + token + ") - Unable to perform operation.", ex);
            throw new JiraException("Unable to perform operation. Reason: " + ex.getMessage());
        }
    }

    @Override
    public Collection<IssueRemote> getFixIssues(Token token, Long... versionIds) throws JiraException, RemoteException {
        return getFixIssues(token, Utils.toSet(versionIds));
    }

    @Override
    public Collection<IssueRemote> getFixIssues(Token token, Set<Long> versionIds) throws JiraException, RemoteException {
        try {
            Map<Long, Issue> issues = new LinkedHashMap<Long, Issue>();
            for (Long versionId : versionIds) {
                try {
                    for (GenericValue issueValue : JiraManagers.versionManager.getFixIssues(JiraUtils.getVersionObject(versionId))) {
                        Issue issue = JiraManagers.issueFactory.getIssue(issueValue);
                        issues.put(issue.getId(), issue);
                    }
                } catch (VersionNotFoundException ex) {
                    log.warn("getFixIssues(" + token + ") - " + ex.getMessage());
                }
            }
            return IssueTransformer.get().transform(issues.values());
        } catch (GenericEntityException ex) {
            log.error("getFixIssues(" + token + ") - Unable to perform operation.", ex);
            throw new JiraException("Unable to perform operation. Reason: " + ex.getMessage());
        }
    }

    @Override
    public Collection<IssueRemote> getVotedIssues(Token token, String userName) throws JiraException {
        try {
            return IssueTransformer.get().transform(JiraManagers.issueManager.getVotedIssues(JiraUtils.getUser(userName)));
        } catch (GenericEntityException ex) {
            log.error("getVotedIssues(" + token + ") - Unable to get issue voted by [" + userName + "]", ex);
            throw new JiraException("Unable to get issue voted by [" + userName + "]");
        }
    }

    @Override
    public Collection<IssueRemote> getVotedIssues(Token token) throws JiraException {
        try {
            return IssueTransformer.get().transform(JiraManagers.issueManager.getVotedIssues(RequestContext.getUser()));
        } catch (GenericEntityException ex) {
            log.error("getVotedIssues(" + token + ") - Unable to get issue voted by [" + RequestContext.getUser().getName() + "]", ex);
            throw new JiraException("Unable to get issue voted by [" + RequestContext.getUser().getName() + "]");
        }
    }

    @Override
    public Collection<IssueRemote> getWatchedIssues(Token token, String userName) throws EntityNotFoundException, JiraException {
        return IssueTransformer.get().transform(JiraManagers.issueManager.getWatchedIssues(JiraUtils.getUser(userName)));
    }

    @Override
    public Collection<IssueRemote> getWatchedIssues(Token token) throws EntityNotFoundException, JiraException {
        return IssueTransformer.get().transform(JiraManagers.issueManager.getWatchedIssues(RequestContext.getUser()));
    }

    @Override
    public Collection<IssueTypeRemote> getIssueTypesForProject(Token token, Long projectId, IssueTypeKind kind) throws JiraException {
        Project project = JiraUtils.getProjectObject(projectId);
        Collection<IssueType> issueTypes;
        switch(kind) {
            case ALL_ISSUE_TYPES:
                issueTypes = JiraManagers.issueTypeSchemeManager.getIssueTypesForProject(project);
                break;
            case ALL_STANDARD_ISSUE_TYPES:
                issueTypes = JiraManagers.issueTypeSchemeManager.getNonSubTaskIssueTypesForProject(project);
                break;
            case ALL_SUB_TASK_ISSUE_TYPES:
                issueTypes = JiraManagers.issueTypeSchemeManager.getSubTaskIssueTypesForProject(project);
                break;
            default:
                throw new JiraException("Unsupported issue-type kind [" + kind + "].");
        }
        return IssueTypeConstTransformer.get().transform(issueTypes);
    }

    @Override
    public Collection<ActionRemote> getAvailableWorkflowActions(Token token, Long issueId) throws JiraException {
        Map<Integer, ActionDescriptor> actions = JiraManagers.issueUtils.loadAvailableActions(JiraUtils.getIssueObject(issueId));
        return (actions != null) ? ActionTransformer.get().transform(actions.values()) : null;
    }

    @Override
    public List<FieldRemote> getFieldsForWorkflowAction(Token token, Long issueId, Long actionId) throws JiraException {
        return FieldTransformer.get().transform(getFields(JiraUtils.getIssueObject(issueId), actionId));
    }

    @Override
    public IssueDetailsRemote progressWorkflowAction(Token token, Long issueId, Long actionId, Collection<FieldValueRemote> params) throws JiraException {
        progressWorkflowAction(JiraUtils.getIssueObject(issueId), actionId, FieldValueTransformer.get().transform(params));
        return IssueDetailsTransformer.get().transform(JiraUtils.getIssueObject(issueId));
    }

    private List<Field> getFields(MutableIssue issue, Long actionId) {
        WorkflowTransitionUtil workflowTransitionUtil = JiraManagers.getWorkflowTransitionUtil(issue, actionId);
        List<Field> fields = new ArrayList<Field>();
        if (StringUtils.isNotBlank(workflowTransitionUtil.getActionDescriptor().getView())) {
            for (FieldScreenRenderTab fieldScreenRenderTab : workflowTransitionUtil.getFieldScreenRenderer().getFieldScreenRenderTabs()) {
                for (FieldScreenRenderLayoutItem fieldScreenRenderLayoutItem : fieldScreenRenderTab.getFieldScreenRenderLayoutItemsForProcessing()) {
                    if (fieldScreenRenderLayoutItem.isShow(issue)) {
                        fields.add(fieldScreenRenderLayoutItem.getOrderableField());
                    }
                }
            }
        }
        return fields;
    }

    private void progressWorkflowAction(MutableIssue issue, Long actionId, Map<String, String[]> params) throws JiraException {
        WorkflowTransitionUtil workflowTransitionUtil = JiraManagers.getWorkflowTransitionUtil(issue, actionId);
        Map<String, Object> workflowTransitionParams = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(workflowTransitionUtil.getActionDescriptor().getView())) {
            if (params == null) {
                params = Collections.emptyMap();
            }
            setParam(params, workflowTransitionParams, WorkflowTransitionUtil.FIELD_COMMENT);
            setParam(params, workflowTransitionParams, WorkflowTransitionUtil.FIELD_COMMENT_LEVEL);
            setParam(params, workflowTransitionParams, WorkflowTransitionUtil.FIELD_COMMENT_ROLE_LEVEL);
            for (FieldScreenRenderTab fieldScreenRenderTab : workflowTransitionUtil.getFieldScreenRenderer().getFieldScreenRenderTabs()) {
                for (FieldScreenRenderLayoutItem fieldScreenRenderLayoutItem : fieldScreenRenderTab.getFieldScreenRenderLayoutItemsForProcessing()) {
                    if (fieldScreenRenderLayoutItem.isShow(issue)) {
                        OrderableField orderableField = fieldScreenRenderLayoutItem.getOrderableField();
                        orderableField.populateFromParams(workflowTransitionParams, params);
                    }
                }
            }
            workflowTransitionUtil.setParams(workflowTransitionParams);
            ErrorCollection errors = workflowTransitionUtil.validate();
            if (errors.hasAnyErrors()) {
                throw new JiraException("Fields not valid for workflow action [" + workflowTransitionUtil.getActionDescriptor().getName() + "]: \n" + errors);
            }
        }
        ErrorCollection errors = workflowTransitionUtil.progress();
        if (errors.hasAnyErrors()) {
            throw new JiraException("Error occurred when running workflow action [" + workflowTransitionUtil.getActionDescriptor().getName() + "]: \n" + errors);
        }
    }

    private void updateIssue(MutableIssue issue, boolean sendMail, boolean subtasksUpdated) throws JiraException {
        Issue originalIssue = JiraUtils.getIssueObject(issue.getId());
        issue.store();
        FieldAccessor fieldAccessor = JiraManagers.fieldAccessor;
        FieldLayoutManager fieldLayoutManager = JiraManagers.fieldLayoutManager;
        FieldLayout fieldLayout = fieldLayoutManager.getFieldLayout(issue);
        Map<String, ModifiedValue> modifiedFields = issue.getModifiedFields();
        DefaultIssueChangeHolder issueChangeHolder = new DefaultIssueChangeHolder();
        for (String fieldId : modifiedFields.keySet()) {
            if (fieldAccessor.isOrderableField(fieldId)) {
                OrderableField field = fieldAccessor.getOrderableField(fieldId);
                FieldLayoutItem fieldLayoutItem = fieldLayout.getFieldLayoutItem(field);
                ModifiedValue modifiedValue = modifiedFields.get(fieldId);
                field.updateValue(fieldLayoutItem, issue, modifiedValue, issueChangeHolder);
            }
        }
        issue.resetModifiedFields();
        IssueUpdateBean issueUpdateBean = new IssueUpdateBean(issue.getGenericValue(), originalIssue.getGenericValue(), EventType.ISSUE_UPDATED_ID, RequestContext.getUser(), sendMail, subtasksUpdated);
        issueUpdateBean.setComment(issueChangeHolder.getComment());
        issueUpdateBean.setChangeItems(issueChangeHolder.getChangeItems());
        issueUpdateBean.setDispatchEvent(true);
        issueUpdateBean.setParams(EasyMap.build("eventsource", IssueEventSource.ACTION));
        try {
            JiraManagers.issueUpdater.doUpdate(issueUpdateBean, false);
        } catch (com.atlassian.jira.JiraException ex) {
            log.error("updateIssue - Unable to update issue [" + issue.getId() + "].", ex);
            throw new JiraException("Unable to update issue [" + issue.getId() + "]. Details: " + ex.getMessage());
        }
    }

    private static void setParam(Map<String, String[]> params, Map<String, Object> workflowTransitionParams, String paramName) {
        String[] comment = params.get(paramName);
        if (comment != null && comment.length > 0) {
            workflowTransitionParams.put(paramName, comment[0]);
        }
    }
}
