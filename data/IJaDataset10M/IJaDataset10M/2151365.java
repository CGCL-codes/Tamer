package com.scholardesk.abstracts.model;

import java.util.Comparator;
import com.scholardesk.abstracts.mapper.AccountMapper;
import com.scholardesk.annotation.DbColumn;
import com.scholardesk.annotation.HasOne;
import com.scholardesk.model.ModelObject;

public class Role implements ModelObject, Comparable {

    String account_id;

    String program_id;

    String role_id;

    Integer role_status = 0;

    String role_label;

    Account account;

    Assignment assignment;

    public Role() {
        super();
    }

    public String getId() {
        return role_id;
    }

    public void setId(String role_id) {
        this.role_id = role_id;
    }

    @DbColumn
    public String getAccountId() {
        return account_id;
    }

    @DbColumn
    public void setAccountId(String account_id) {
        this.account_id = account_id;
    }

    @DbColumn
    public String getProgramId() {
        return program_id;
    }

    @DbColumn
    public void setProgramId(String program_id) {
        this.program_id = program_id;
    }

    @DbColumn
    public String getRoleId() {
        return getId();
    }

    @DbColumn
    public void setRoleId(String role_id) {
        setId(role_id);
    }

    @DbColumn
    public Integer getRoleStatus() {
        return this.role_status;
    }

    @DbColumn
    public void setRoleStatus(Integer role_status) {
        this.role_status = role_status;
    }

    public Account getAccount() {
        if (account != null) return account;
        if (account_id != null) {
            Account _account = new AccountMapper().find(account_id);
            setAccount(_account);
            return _account;
        }
        return null;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getRoleLabel() {
        return role_label;
    }

    public void setRoleLabel(String role_label) {
        this.role_label = role_label;
    }

    public Assignment getAssignment() {
        return this.assignment;
    }

    @HasOne(foreign_key_column = "account_id", foreign_key_method = "accountId")
    public void setAssignment(Assignment _assignment) {
        this.assignment = _assignment;
    }

    public int compareTo(Object _role) {
        return (getRoleStatus() - ((Role) _role).getRoleStatus());
    }

    public static Comparator RoleComparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            Role _role1 = (Role) o1;
            Role _role2 = (Role) o2;
            return (_role1.getRoleId().compareToIgnoreCase(_role2.getRoleId()));
        }
    };

    public static Comparator AccountComparator = new Comparator() {

        public int compare(Object o1, Object o2) {
            Role _role1 = (Role) o1;
            Role _role2 = (Role) o2;
            if (_role1.getAccount() != null && _role2.getAccount() != null) return (_role1.getAccount().getLastName().compareToIgnoreCase(_role2.getAccount().getLastName()));
            return -1;
        }
    };
}
