package ces.research.oa.document.mail.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import ces.arch.form.ListForm;

public class MailListForm extends ListForm {

    private String doAction;

    private String regUserId;

    private String send;

    private String draft;

    private String deleted;

    private String sys;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        query = "<query id=\"0\">" + "<target pojo=\"MailPojo\" as=\"o\"/>" + "<compare target=\"o.regUserId\" name=\"regUserId\" operator=\"EQ\"/>" + "<compare target=\"o.send\" name=\"send\" operator=\"EQ\"/>" + "<compare target=\"o.draft\" name=\"draft\" operator=\"EQ\"/>" + "<compare target=\"o.deleted\" name=\"deleted\" operator=\"EQ\"/>" + "<compare target=\"o.sys\" name=\"sys\" operator=\"EQ\"/>" + "</query>";
    }

    public String getDoAction() {
        return doAction;
    }

    public void setDoAction(String doAction) {
        this.doAction = doAction;
    }

    public String getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(String regUserId) {
        this.regUserId = regUserId;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }
}
