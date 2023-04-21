package ces.platform.system.dbaccess;

import ces.platform.system.common.*;
import java.util.*;

/**
 * <p>����:
 * <font class=titlefont>
 * ����������
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>���ڴ洢��ݿ�ı�����ֺ�һЩϵͳ�������Ӷ����
 * ��Դ�����е�Ӳ���룬��������
 * </font>
 * <p>�汾��:
 * <font class=versionfont>
 * Copyright (c) 2.50.2003.0925
 * </font>
 * <p>��˾:
 * <font class=companyfont>
 * �Ϻ�������Ϣ��չ���޹�˾
 * </font>
 * @author ����
 * @version 2.50.2003.0925
 */
public class Common {

    /**
     * �˳���������ݿ��е�Ȩ����Ϣ�?t_sys_authority��
     */
    public static final String AUTHORITY_TABLE = "t_sys_authority";

    /**
     * �˳���������ݿ��еķ�����־��Ϣ�?t_sys_assign_log��
     */
    public static final String ASSIGN_LOG_TABLE = "t_sys_assign_log";

    /**
     * �˳���������ݿ��е�Ȩ�޷�����Ϣ�?t_sys_authority_assign��
     */
    public static final String AUTHORITY_ASSIGN_TABLE = "t_sys_authority_assign";

    /**
     * �˳���������ݿ��е���Դ��Ϣ�?t_sys_resource��
     */
    public static final String RESOURCE_TABLE = "t_sys_resource";

    /**
     * �˳���������ݿ��е���Դ���ͱ?t_sys_resource_type��
     */
    public static final String RESOURCE_TYPE_TABLE = "t_sys_resource_type";

    /**
     * �˳���������ݿ��е���֯��Ϣ�?t_sys_organize��
     */
    public static final String ORGANIZE_TABLE = "t_sys_organize";

    /**
     * �˳���������ݿ��е���֯������Ϣ�?t_sys_organize_b��
     */
    public static final String ORGANIZE_B_TABLE = "t_sys_organize_b";

    /**
     * �˳���������ݿ��е���֯������Ϣ�?t_sys_organize_bak_record��
     */
    public static final String ORGANIZE_BAK_RECORD_TABLE = "t_sys_organize_bak_record";

    /**
     * �˳���������ݿ��е���֯��ϵ��Ϣ�?t_sys_organize_relation��
     */
    public static final String ORGANIZE_RELATION_TABLE = "t_sys_organize_relation";

    /**
     * �˳���������ݿ��е���֯��ϵ��Ϣ���ݱ?t_sys_organize_relation_b��
     */
    public static final String ORGANIZE_RELATION_B_TABLE = "t_sys_organize_relation_b";

    /**
     * �˳���������ݿ��е���֯������Ϣ�?t_sys_organize_type��
     */
    public static final String ORGANIZE_TYPE_TABLE = "t_sys_organize_type";

    /**
     * �˳���������ݿ��е���֯������Ϣ���ݱ?t_sys_organize_type_b��
     */
    public static final String ORGANIZE_TYPE_B_TABLE = "t_sys_organize_type_b";

    /**
     * �˳���������ݿ��е���֯���͹�ϵ�?t_sys_organize_type_relation��
     */
    public static final String ORGANIZE_TYPE_RELATION_TABLE = "t_sys_organize_type_relation";

    /**
     * �˳���������ݿ��е���֯���͹�ϵ���ݱ?t_sys_organize_type_relation_b��
     */
    public static final String ORGANIZE_TYPE_RELATION_B_TABLE = "t_sys_organize_type_relation_b";

    /**
     * �˳���������ݿ��еĽ�ɫ��Ϣ�?t_sys_role��
     */
    public static final String ROLE_TABLE = "t_sys_role";

    /**
     * �˳���������ݿ��е��û��ڽ�ɫ��ϵ��Ϣ�?t_sys_role_assign��
     */
    public static final String ROLE_ASSIGN_TABLE = "t_sys_role_assign";

    /**
     * �˳���������ݿ��еĽ�ɫȨ�޹�ϵ��Ϣ�?t_sys_role_authority��
     */
    public static final String ROLE_AUTHORITY_TABLE = "t_sys_role_authority";

    /**
     * �˳���������ݿ��еĽ�ɫ��ɫ��ϵ��Ϣ�?t_sys_role_role��
     */
    public static final String ROLE_ROLE_TABLE = "t_sys_role_role";

    /**
     * �˳���������ݿ��е��û���Ϣ�?t_sys_user��
     */
    public static final String USER_TABLE = "t_sys_user";

    /**
     * �˳���������ݿ��е��û��飨��ݣ���Ϣ�?t_sys_user_group��
     */
    public static final String USER_GROUP_TABLE = "t_sys_user_group";

    /**
     * �˳���������ݿ��е��û��飨��ݣ���ϵ��Ϣ�?t_sys_user_group_relation��
     */
    public static final String USER_GROUP_RELATION_TABLE = "t_sys_user_group_relation";

    /**
     * �˳���������ݿ��е��û���Ȩ�����ϵ�?t_sys_user_level��
     */
    public static final String USER_LEVEL_TABLE = "t_sys_user_level";

    /**
     * �˳���������ݿ��е��û��Ự��Ϣ�?t_sys_user_session��
     */
    public static final String USER_SESSION_TABLE = "t_sys_user_session";

    /**
     * �˳���������ݿ��е��û��Ự��ʷ��Ϣ�?t_sys_user_session_history��
     */
    public static final String USER_SESSION_HISTORY_TABLE = "t_sys_user_session_history";

    /**
     * �˳���������ݿ��е��û���Ȩ����Ϣ�?t_sys_usergroup_authority��
     */
    public static final String USER_GROUP_AUTHORITY_TABLE = "t_sys_usergroup_authority";

    /**
     * �˳���������ݿ��е��û����ɫ��Ϣ�?t_sys_usergroup_role��
     */
    public static final String USER_GROUP_ROLE_TABLE = "t_sys_usergroup_role";

    /**
     * �˳���������ݿ��е��û�ְ����ϵ�?t_sys_employee��
     */
    public static final String EMPLOYEE_TABLE = "t_sys_employee";

    /**
     * �˳���������ݿ��е�ID����ɱ?t_sys_identifier��
     */
    public static final String IDENTIFIER_TABLE = "t_sys_identifier";

    /**
     * �˳���������ݿ��еĲ������ͱ?t_sys_operate_type��
     */
    public static final String OPERATE_TYPE_TABLE = "t_sys_operate_type";

    /**
     * �˳���������ݿ��еĲ����?t_sys_operate��
     */
    public static final String OPERATE_TABLE = "t_sys_operate";

    /**
     * �˳���������ݿ��е����Ȩ�ޱ?t_sys_datapri��
     */
    public static final String DATAPRI_TABLE = "t_sys_datapriv";

    /**
     * �˳���������ݿ��е��û����Ա?t_sys_userprop��
     */
    public static final String USERPROP_TABLE = "t_sys_userprop";

    /**
     * �˳���������ݿ��е��û����Ա?t_sys_personnel_info��
     */
    public static final String PERSONNEL_INFO = "t_sys_personnel_info";

    /**
     * �˳���������ݿ��е��û���¼ʱ��?t_sys_loginable_time��
     */
    public static final String LOGINABLE_TIME = "t_sys_loginable_time";

    /**
	 * �˳���������ݿ��еı���ά���?t_sys_personnel_info��
	 */
    public static final String CODE_CATEGORY = "t_code_category";

    /**
	 * �˳���������ݿ��еı���ά���?t_code_data��
	 */
    public static final String CODE_DATA = "t_code_data";

    /**
    * �õ���ǰʱ��
    * @return  ��ǰʱ��
    * @throws Exception ���?�쳣
    */
    public static java.sql.Timestamp getSysDate() throws Exception {
        java.sql.Timestamp ts1 = null;
        String strTime = "";
        try {
            java.util.Date date1 = new java.util.Date();
            long ld1 = date1.getTime();
            ts1 = new java.sql.Timestamp(ld1);
            ts1.setNanos((int) (ld1 % 1000) * 1000000);
        } catch (Exception e) {
            throw new CesSystemException(e.toString());
        }
        return ts1;
    }

    /**
    * �õ���ǰʱ�����24Сʱ
    * @return  ��ǰʱ�����24Сʱ
    * @throws Exception ���?�쳣
    */
    public static String getDefaultTime() throws Exception {
        String strTime = "";
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.HOUR, 24);
            strTime = String.valueOf(c.get(Calendar.YEAR)) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + String.valueOf(c.get(Calendar.DATE)) + " " + String.valueOf(c.get(Calendar.HOUR)) + ":00";
        } catch (Exception e) {
            throw new CesSystemException(e.toString());
        }
        return strTime;
    }

    /**
    * �˱��������ݿ���������Ȩ��Where�Ӿ��������
    */
    public static final String FIELD_CONDITION_TABLE = "t_sys_field_condition";

    /**
	* �˱����������û���organizeid
	*/
    public static final int OrgID = -2;

    /**
	* �˱����������û���organizetypeid
	*/
    public static final String strOrgTypeID = "#";
}
