package com.dcivision.ldap.bean;

import java.util.List;
import java.util.ArrayList;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.staff.bean.StaffRecord;
import com.dcivision.ldap.core.LdapImportException;

/**
  LdapImportResult.java

  This class is Result of LdapImportAction.

  @author          Charlie Liu
  @company         DCIVision Limited
  @creation date   17/07/2004
  @version         $Revision: 1.3.2.3 $
*/
public class LdapImportResult extends AbstractBaseObject {

    public static final String RESULT_RECORD_CREATED = "C";

    public static final String RESULT_RECORD_DUPLIDATED = "D";

    public static final String RESULT_RECORD_REMOVED = "R";

    public static final String RESULT_RECORD_REACTIVATED = "A";

    private String createAccountResult = "";

    private String createStaffResult = "";

    private String ldapName = "";

    private StaffRecord staff;

    private List exceptionList = new ArrayList();

    /**
   * Constructor
   *
   */
    public LdapImportResult() {
        staff = new StaffRecord();
    }

    public void addException(LdapImportException e) {
        exceptionList.add(e);
    }

    public List getExceptionList() {
        return exceptionList;
    }

    public void setCreateAccountResult(String str) {
        this.createAccountResult = str;
    }

    public String getCreateAccountResult() {
        return this.createAccountResult;
    }

    public void setLdapName(String name) {
        this.ldapName = name;
    }

    public String getLdapName() {
        return this.ldapName;
    }

    public void setStaff(StaffRecord staff) {
        this.staff = staff;
    }

    public StaffRecord getStaff() {
        return this.staff;
    }

    public void setCreateStaffResult(String str) {
        this.createStaffResult = str;
    }

    public String getCreateStaffResult() {
        return this.createStaffResult;
    }
}
