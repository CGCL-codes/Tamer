package com.platonov.dao;

/**
 * User: User
 * Date: 03.03.11   /    Time: 18:50
 */
public class ModelDictionaryDAO {

    public static String getForm = "select * from PHONE_FORM where FORM = ?";

    public static String isExistPhoneForm = "select 1 from PHONE_FORM where FORM = ?";

    public static String getForms = "select * from PHONE_FORM";

    public static String updateForm = "update PHONE_FORM set FORM = ? WHERE FORM = ?";

    public static String insertForm = "insert into PHONE_FORM values(?)";

    public static String deleteForm = "delete from PHONE_FORM where FORM = ?";

    public static String getTicketForm = "select * from TICKET_FORM where TICKET_FORM_NUM = ?";

    public static String getTicketForms = "select * from TICKET_FORM";

    public static String getNextTicketFormID = "select max(TICKET_FORM_NUM) from TICKET_FORM";

    public static String isExistTicketForm = "select 1 from TICKET_FORM where TICKET_FORM_NAME = ?";

    public static String updateTicketFormNum = "update TICKET_FORM set TICKET_FORM_NUM = ? where TICKET_FORM_NUM = ?";

    public static String updateTicketFormName = "update TICKET_FORM set TICKET_FORM_NAME = ? where TICKET_FORM_NUM = ?";

    public static String insertTicketForm = "insert into TICKET_FORM values(?,?)";

    public static String deleteTicketForm = "delete from TICKET_FORM where TICKET_FORM_NUM = ?";

    public static String getDept = "select * from DEPT where DEPT_NUM = ?";

    public static String getDepts = "select * from DEPT";

    public static String isExistDept = "select 1 from DEPT where DEPT_NAME = ?";

    public static String getNextDeptID = "select max(DEPT_NUM) from DEPT";

    public static String updateDeptNum = "update DEPT set DEPT_NUM = ? where DEPT_NUM = ?";

    public static String updateDeptName = "update DEPT set DEPT_NAME = ? where DEPT_NUM = ?";

    public static String insertDept = "insert into DEPT values(?,?)";

    public static String deleteDept = "delete from DEPT where DEPT_NUM = ?";

    public static String getPost = "select * from POST where POST_NUM = ?";

    public static String getPosts = "select * from POST";

    public static String isExistPost = "select 1 from POST where POST_NAME = ?";

    public static String getNextPostID = "select max(POST_NUM) from POST";

    public static String updatePostNum = "update POST set POST_NUM = ? where POST_NUM = ?";

    public static String updatePostName = "update POST set POST_NAME = ? where POST_NUM = ?";

    public static String insertPost = "insert into POST values(?,?)";

    public static String deletePost = "delete from POST where POST_NUM = ?";
}
