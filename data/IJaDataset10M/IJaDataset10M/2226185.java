package org.foj.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.GenericDao;
import org.foj.model.Issue;
import org.foj.service.IssueManager;
import org.foj.service.CommentManager;
import org.springframework.flex.messaging.MessageTemplate;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "IssueService", endpointInterface = "org.foj.service.IssueManager")
public class IssueManagerImpl implements IssueManager {

    private GenericDao<Issue, Long> issueDao;

    private CommentManager commentManager;

    private MessageTemplate messageTemplate;

    public IssueManagerImpl(GenericDao<Issue, Long> issueDao, CommentManager commentManager, MessageTemplate messageTemplate) {
        this.issueDao = issueDao;
        this.commentManager = commentManager;
        this.messageTemplate = messageTemplate;
    }

    public List<Issue> getAll() {
        return issueDao.getAll();
    }

    public Issue get(Long id) {
        return issueDao.get(id);
    }

    public Issue save(Issue issue) {
        String messageBody = "Issue was saved";
        messageTemplate.send("flexMessage", messageBody);
        return issueDao.save(issue);
    }

    public void remove(Long id) {
        commentManager.deleteAllCommentsForIssueId(id);
        String messageBody = "Issue was removed";
        messageTemplate.send("flexMessage", messageBody);
        issueDao.remove(id);
    }
}
