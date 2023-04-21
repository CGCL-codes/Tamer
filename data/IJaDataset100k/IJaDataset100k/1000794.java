package com.reserveamerica.jirarmi.impl;

import java.util.List;
import com.reserveamerica.commons.Token;
import com.reserveamerica.jirarmi.WorklogService;
import com.reserveamerica.jirarmi.beans.issue.WorklogRemote;
import com.reserveamerica.jirarmi.exceptions.JiraException;
import com.reserveamerica.jirarmi.transformers.issue.WorklogTransformer;

/**
 * @author BStasyszyn
 */
public class WorklogServiceImpl implements WorklogService {

    public WorklogRemote createWorklog(Token token, WorklogRemote worklog, Long newEstimate, boolean dispatchEvent) throws JiraException {
        String userName = RequestContext.getUserName();
        return WorklogTransformer.get().transform(JiraManagers.worklogManager.create(RequestContext.getUser(), WorklogTransformer.get().transform(new MutableWorklogRemote(worklog, userName, userName)), newEstimate, dispatchEvent));
    }

    public WorklogRemote updateWorklog(Token token, WorklogRemote worklog, Long newEstimate, boolean dispatchEvent) throws JiraException {
        return WorklogTransformer.get().transform(JiraManagers.worklogManager.update(RequestContext.getUser(), WorklogTransformer.get().transform(new MutableWorklogRemote(worklog, null, RequestContext.getUserName())), newEstimate, dispatchEvent));
    }

    public boolean deleteWorklog(Token token, Long worklogId, Long newEstimate, boolean dispatchEvent) throws JiraException {
        return JiraManagers.worklogManager.delete(RequestContext.getUser(), JiraUtils.getWorklogObject(worklogId), newEstimate, dispatchEvent);
    }

    public WorklogRemote getWorklog(Token token, Long worklogId) throws JiraException {
        return WorklogTransformer.get().transform(JiraUtils.getWorklogObject(worklogId));
    }

    public List<WorklogRemote> getWorklogsByIssue(Token token, Long issueId) throws JiraException {
        return WorklogTransformer.get().transform(JiraManagers.worklogManager.getByIssue(JiraUtils.getIssueObject(issueId)));
    }

    public long getCountForWorklogsRestrictedByGroup(Token token, String groupName) throws JiraException {
        return JiraManagers.worklogManager.getCountForWorklogsRestrictedByGroup(groupName);
    }

    public int swapWorklogGroupRestriction(Token token, String groupName, String swapGroup) throws JiraException {
        JiraUtils.getGroup(swapGroup);
        return JiraManagers.worklogManager.swapWorklogGroupRestriction(groupName, swapGroup);
    }

    private static class MutableWorklogRemote extends WorklogRemote {

        private static final long serialVersionUID = 1423193393398594622L;

        public MutableWorklogRemote(WorklogRemote src, String author, String updateAuthor) {
            super(src);
            if (author != null) {
                setAuthor(author);
            }
            if (updateAuthor != null) {
                setUpdateAuthor(updateAuthor);
            }
        }
    }
}
