package com.scholardesk.abstracts.mgt;

import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import com.scholardesk.abstracts.AbstractsTask;
import com.scholardesk.abstracts.constants.AbstractStatus;
import com.scholardesk.abstracts.constants.AccountRole;
import com.scholardesk.abstracts.mapper.AccountMapper;
import com.scholardesk.abstracts.model.Account;
import com.scholardesk.email.Email;
import com.scholardesk.utilities.StringUtil;
import com.scholardesk.validator.ValidatorUtil;

public class ContactUsersTask extends AbstractsTask {

    private boolean m_validation_passed = true;

    private ContactUsersFormObject m_form_object = new ContactUsersFormObject();

    private List<Account> m_submitters = null;

    private List<Account> m_reviewers = null;

    private List<Account> m_chairs = null;

    private List<Account> m_coordinators = null;

    private String m_abstract_status = null;

    private HashSet<String> m_email_addresses = new HashSet<String>();

    @Override
    public void process() {
        AccountMapper _mapper = new AccountMapper();
        m_abstract_status = http_request.getParameter("abstract_status_id");
        m_submitters = _mapper.findAllByProgramSubmitter(program.getId());
        if (StringUtil.exists(m_abstract_status)) {
            Integer _abstract_status_id = Integer.valueOf(m_abstract_status);
            m_submitters = _mapper.findAllByProgramSubmitterAbstractStatus(program.getId(), _abstract_status_id);
        }
        m_reviewers = _mapper.findAllByProgramRole(program.getId(), AccountRole.REVIEWER.getId());
        m_chairs = _mapper.findAllByProgramRole(program.getId(), AccountRole.CHAIR.getId());
        m_coordinators = _mapper.findAllByProgramRole(program.getId(), AccountRole.COORDINATOR.getId());
        if (http_request.getParameter("task_completed") == null) return;
        m_form_object.setSubject(http_request.getParameter("subject"));
        m_form_object.setMessage(http_request.getParameter("message"));
        m_form_object.setAddresses(http_request.getParameterValues("addresses"));
        m_validation_passed = m_form_object.validate("contact_users_form", new String[] { "contact_users_form" });
        if (!m_validation_passed) return;
        String _message = m_form_object.getMessage();
        String _subject = m_form_object.getSubject();
        if (_message != null) {
            List _values = Arrays.asList(m_form_object.getAddresses());
            if (_values.contains("submitters")) addAddresses(m_submitters);
            if (_values.contains("reviewers")) addAddresses(m_reviewers);
            if (_values.contains("chairs")) addAddresses(m_chairs);
            if (_values.contains("coordinators")) addAddresses(m_coordinators);
            sendMessage(_message, _subject);
        }
    }

    private void addAddresses(List<Account> _accounts) {
        for (Account _account : _accounts) m_email_addresses.add(Email.buildToWithName(_account.getEmail(), _account.getDisplayName()));
    }

    private void sendMessage(String _message, String _subject) {
        if (m_email_addresses == null) return;
        String _bcc_addresses = StringUtil.join(m_email_addresses, ",");
        try {
            Email _email = new Email();
            _email.setContent(_message);
            _email.setSubject(_subject);
            _email.setBcc(_bcc_addresses);
            _email.setTo(Email.buildToWithName(account.getEmail(), account.getDisplayName()));
            _email.setFrom(account.getEmail());
            _email.setFromName(account.getDisplayName());
            _email.send();
        } catch (Exception _e) {
            _e.printStackTrace();
            throw new RuntimeException(_e);
        }
    }

    @Override
    public String getView() {
        return "contact_users.vm";
    }

    @Override
    public HttpServletRequest setRequest() {
        if (!m_validation_passed) {
            String js_array = ValidatorUtil.toJavascriptArray(m_form_object.getFailures());
            http_request.setAttribute("failure_js", js_array);
            http_request.setAttribute("failures", m_form_object.getFailures());
            http_request.setAttribute("load_js", toJavascriptArray("response", getWebParams(m_form_object)));
        } else {
            if (http_request.getParameter("task_completed") != null) http_request.setAttribute("task_completed", true);
            if (http_request.getParameter("subject") != null) http_request.setAttribute("subject", http_request.getParameter("subject"));
        }
        http_request.setAttribute("statuses", AbstractStatus.getIndex());
        http_request.setAttribute("submitters", m_submitters);
        http_request.setAttribute("reviewers", m_reviewers);
        http_request.setAttribute("chairs", m_chairs);
        http_request.setAttribute("coordinators", m_coordinators);
        http_request.setAttribute("abstract_status", m_abstract_status);
        return http_request;
    }
}
