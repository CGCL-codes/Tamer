package org.digitall.common.cashflow.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.digitall.lib.sql.LibSQL;

public class Account {

    private int idAccount = -1;

    private int code;

    private String name;

    private int idParent = 0;

    private int idAccountType = -1;

    private String initialAmountcolor;

    private String assignedAmountToACColor;

    private String assignedAmountToCCColor;

    private String description;

    private boolean isImputable;

    private boolean isHeritage;

    private boolean isDeduction;

    private boolean isCash;

    private boolean isValues;

    private int childrenNumber = 0;

    private String fullName;

    private boolean subAccount = false;

    public Account() {
    }

    public Account(int _idAccount) {
        idAccount = _idAccount;
    }

    public Account(int _idAccount, String _Name, int _idAccountType) {
        idAccount = _idAccount;
        name = _Name;
        idAccountType = _idAccountType;
    }

    public void setIDAccount(int _idAccount) {
        this.idAccount = _idAccount;
    }

    public int getIDAccount() {
        return idAccount;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdAccountType(int _idAccountType) {
        this.idAccountType = _idAccountType;
    }

    public int getIdAccountType() {
        return idAccountType;
    }

    public void setInitialAmountcolor(String initialAmountcolor) {
        this.initialAmountcolor = initialAmountcolor;
    }

    public String getInitialAmountcolor() {
        return initialAmountcolor;
    }

    public void setAssignedAmountToACColor(String assignedAmountToACColor) {
        this.assignedAmountToACColor = assignedAmountToACColor;
    }

    public String getAssignedAmountToACColor() {
        return assignedAmountToACColor;
    }

    public void setAssignedAmountToCCColor(String assignedAmountToCCColor) {
        this.assignedAmountToCCColor = assignedAmountToCCColor;
    }

    public String getAssignedAmountToCCColor() {
        return assignedAmountToCCColor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setIsImputable(boolean isImputable) {
        this.isImputable = isImputable;
    }

    public boolean isIsImputable() {
        return isImputable;
    }

    public int saveData() {
        String params = code + ",'" + name + "'," + idParent + "," + idAccountType + ",'" + initialAmountcolor + "','" + assignedAmountToACColor + "','" + assignedAmountToCCColor + "','" + description + "','" + isImputable + "','" + isDeduction + "','" + isCash + "','" + isValues + "','" + subAccount + "'";
        int result = -1;
        if (idAccount == -1) {
            result = LibSQL.getInt("accounting.addAccount", params);
            idAccount = result;
        } else {
            params = idAccount + "," + params;
            result = LibSQL.getInt("accounting.setAccount", params);
        }
        return result;
    }

    public void retrieveData() {
        ResultSet data = LibSQL.exFunction("accounting.getAccount", idAccount);
        try {
            if (data.next()) {
                code = data.getInt("code");
                name = data.getString("name");
                idParent = data.getInt("idparent");
                idAccountType = data.getInt("idaccounttype");
                initialAmountcolor = data.getString("initialamountcolor");
                assignedAmountToACColor = data.getString("assignedamounttoetcolor");
                assignedAmountToCCColor = data.getString("assignedamounttocccolor");
                description = data.getString("description");
                isImputable = data.getBoolean("isimputable");
                isHeritage = data.getBoolean("isheritage");
                isDeduction = data.getBoolean("isdeduction");
                isCash = data.getBoolean("iscash");
                isValues = data.getBoolean("isvalues");
                fullName = data.getString("name");
                subAccount = data.getBoolean("subaccount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveChildrenNumber() {
        childrenNumber = LibSQL.getInt("accounting.getChildrenByAccount", "" + idAccount);
    }

    public int saveColor() {
        String params = idAccount + ",'" + initialAmountcolor + "'";
        return LibSQL.getInt("accounting.setAccountColor", params);
    }

    public void setIsHeritage(boolean isHeritage) {
        this.isHeritage = isHeritage;
    }

    public boolean isIsHeritage() {
        return isHeritage;
    }

    public void setIsDeduction(boolean isDeduction) {
        this.isDeduction = isDeduction;
    }

    public boolean isIsDeduction() {
        return isDeduction;
    }

    public void setIsCash(boolean isCash) {
        this.isCash = isCash;
    }

    public boolean isIsCash() {
        return isCash;
    }

    public void setIsValues(boolean isValues) {
        this.isValues = isValues;
    }

    public boolean isIsValues() {
        return isValues;
    }

    public void setChildrenNumber(int childrenNumber) {
        this.childrenNumber = childrenNumber;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public int getNewCode() {
        return LibSQL.getInt("accounting.getAccountNewCode", "" + idParent);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setSubAccount(boolean subAccount) {
        this.subAccount = subAccount;
    }

    public boolean isSubAccount() {
        return subAccount;
    }
}
