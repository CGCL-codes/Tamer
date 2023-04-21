package com.scholardesk.abstracts.submit;

import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import com.scholardesk.abstracts.AbstractsTask;
import com.scholardesk.abstracts.constants.AbstractStatus;
import com.scholardesk.abstracts.mapper.AbstractMapper;
import com.scholardesk.abstracts.model.Abstract;
import com.scholardesk.email.Email;

public class PublishTask extends AbstractsTask {

    private Abstract m_abstract;

    private boolean m_email_error = false;

    @Override
    public void process() {
        m_abstract = (Abstract) http_session.getAttribute("abstract");
        if (m_abstract == null) throw new RuntimeException("Abstract not available in session! Perhaps the session expired?");
        if (m_abstract.getAbstractStatusId().equals(AbstractStatus.IN_PROGRESS.getId())) m_abstract.setAbstractStatusId(program.getDefaultAbstractStatusId());
        m_abstract.setDateModified(new Timestamp(new java.util.Date().getTime()));
        new AbstractMapper().update(m_abstract);
        if (!m_abstract.getAbstractStatusId().equals(AbstractStatus.ACCEPTED.getId())) emailNotify();
        http_session.setAttribute("review_in_progress", null);
        http_session.setAttribute("abstract", null);
    }

    private void emailNotify() {
        try {
            Email _email = new Email("abstract_submission_confirm.eml");
            _email.addContext("UNDER_REVIEW", AbstractStatus.UNDER_REVIEW.getId());
            _email.addContext("ACCEPTED", AbstractStatus.ACCEPTED.getId());
            _email.addContext("program", program);
            _email.addContext("abstract", m_abstract);
            _email.addContext("account", account);
            _email.addContext("request", http_request);
            _email.setTo(Email.buildToWithName(account.getEmail(), account.getDisplayName()));
            _email.setFrom(config.getString("email.from_address"));
            _email.setFromName(config.getString("email.from_name"));
            _email.send();
        } catch (Exception _e) {
            _e.printStackTrace();
            m_email_error = true;
        }
    }

    @Override
    public String getView() {
        return "publish.vm";
    }

    @Override
    public HttpServletRequest setRequest() {
        if (m_email_error) http_request.setAttribute("email_error", true);
        http_request.setAttribute("abstract", m_abstract);
        http_request.setAttribute("UNDER_REVIEW", AbstractStatus.UNDER_REVIEW.getId());
        http_request.setAttribute("ACCEPTED", AbstractStatus.ACCEPTED.getId());
        return http_request;
    }
}
