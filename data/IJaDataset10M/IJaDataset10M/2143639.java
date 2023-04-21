package com.vlee.ejb.employee;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.Serializable;

public class EmpRemunerationPackageObject implements Serializable {

    public Integer pkid;

    public Integer emp_pkid;

    public String field1;

    public String field2;

    public Timestamp time_effective;

    public String status;

    public Timestamp lastUpdate;

    public Integer userIdUpdate;

    public EmpRemunerationPackageObject() {
        pkid = new Integer(0);
        emp_pkid = new Integer(0);
        field1 = "";
        field2 = "";
        time_effective = new Timestamp(0);
        status = "";
        lastUpdate = new Timestamp(0);
        userIdUpdate = new Integer(0);
    }

    public String toString() {
        String dbgStr = "EmpRemunerationPackageObject:\n";
        dbgStr += "pkid = " + pkid.toString() + "\n";
        dbgStr += "emp_pkid = " + emp_pkid.toString() + "\n";
        dbgStr += "field1 = " + field1 + "\n";
        dbgStr += "field2 = " + field2 + "\n";
        dbgStr += "time_effective = " + time_effective.toString() + "\n";
        dbgStr += "status = " + status + "\n";
        dbgStr += "lastUpdate = " + lastUpdate.toString() + "\n";
        dbgStr += "userIdUpdate = " + userIdUpdate.toString() + "\n";
        return dbgStr;
    }
}
