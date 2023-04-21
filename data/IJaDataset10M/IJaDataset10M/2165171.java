package library.database.mybatis.domain;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import library.enums.Role;

@SuppressWarnings("serial")
public class PersonDescription implements Serializable {

    @NotNull
    protected Role role;

    @NotNull
    protected String nameFirst;

    @NotNull
    protected String nameLast;

    @NotNull
    protected String address;

    @NotNull
    protected boolean credit;

    @NotNull
    protected List<Credential> credentials;

    @NotNull
    protected List<Library> libraries;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }

    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }
}
