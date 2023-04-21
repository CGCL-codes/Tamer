package net.ludmal.boilcode.client;

import java.io.Serializable;
import java.util.Date;

public class CodeBlock implements Serializable, Comparable<CodeBlock> {

    private Long _Id;

    private String _title;

    private String _body;

    private String _language;

    private String _userEmail;

    private boolean _isPublic;

    private Date _date = new Date();

    public CodeBlock(String title, String body, String language, String userEmail, boolean isPublic) {
        this._title = title;
        this._body = body;
        this._language = language;
        this._userEmail = userEmail;
        this._isPublic = isPublic;
    }

    public CodeBlock() {
    }

    public Long getId() {
        return _Id;
    }

    public void setId(Long id) {
        this._Id = id;
    }

    public void setIsPublic(boolean isPublic) {
        this._isPublic = isPublic;
    }

    public boolean getIsPublic() {
        return this._isPublic;
    }

    public String getTitle() {
        return this._title;
    }

    public String getBody() {
        return this._body;
    }

    public String getLanguage() {
        return this._language;
    }

    public String getUserEmail() {
        return this._userEmail;
    }

    public Date getDate() {
        return this._date;
    }

    public void setUserEmail(String userEmail) {
        this._userEmail = userEmail;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public void setBody(String body) {
        this._body = body;
    }

    public void setLanguage(String language) {
        this._language = language;
    }

    public void setDate(Date date) {
        this._date = date;
    }

    public int compareTo(CodeBlock o) {
        return (o == null || o._title == null) ? -1 : -o._title.compareTo(_title);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CodeBlock) {
            return _title == ((CodeBlock) o).getTitle();
        }
        return false;
    }
}
