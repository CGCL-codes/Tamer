package com.be.http.forms;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class JournalDetailForm extends ValidatorForm {

    private static final long serialVersionUID = 282372615672113075L;

    private String action;

    private long id;

    private String bookText;

    private long userID;

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBookText(String bookText) {
        this.bookText = bookText;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getAction() {
        return action;
    }

    public long getId() {
        return id;
    }

    public String getBookText() {
        return bookText;
    }

    public long getUserID() {
        return userID;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        action = "";
        id = 0;
        bookText = "";
        userID = 0;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        ActionErrors errors = super.validate(mapping, request);
        return errors;
    }

    public String toString() {
        return action + ";" + id + ";" + bookText + ";" + userID;
    }
}
