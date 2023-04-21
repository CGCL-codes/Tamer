package org.monet.kernel.model;

import java.util.Date;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.library.LibraryDate;

public class TaskEnrolment extends BaseObject {

    private String idUser;

    private Date createDate;

    public TaskEnrolment(String idUser, Date createDate) {
        super();
        this.idUser = idUser;
        this.createDate = createDate;
    }

    public String getIdUser() {
        return this.idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getCreateDate(String sFormat, String codeLanguage) {
        if (this.createDate == null) return null;
        return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, sFormat, true, Strings.BAR45);
    }

    public String getCreateDate(String sFormat) {
        return this.getCreateDate(sFormat, Language.getCurrent());
    }

    public String getCreateDate() {
        return this.getCreateDate(LibraryDate.Format.DEFAULT, Language.getCurrent());
    }

    public Date getInternalCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date dtCreateDate) {
        this.createDate = dtCreateDate;
    }
}
