package com.hangar.fileupdate.dao;

import java.util.List;
import java.util.Map;
import com.hangar.fileupdate.model.SelfConfig;
import com.hangar.fileupdate.smutil.SMUtil;
import org.apache.commons.lang.StringUtils;

public class CommonDAO {

    /**
	 * ���ָ�����е���һ��ֵ
	 * @param seqName
	 * @return
	 */
    public static String getSeqNextVal(String seqName) {
        if (StringUtils.isBlank(seqName)) return null;
        String rtn = "";
        String sql = "select " + seqName + ".nextval N from dual";
        Map tmp = SMUtil.queryMapBySql(sql);
        if (tmp != null) {
            rtn = tmp.get("N") == null ? "" : tmp.get("N").toString();
        }
        return rtn;
    }

    /**
	 * ����������������б�
	 * @return
	 */
    public static List getMainServerList() {
        return SMUtil.queryBySql("select * from t_server_list t where t.server_type='02'");
    }

    /**
	 * ��������ڲ��������б�
	 * @return
	 */
    public static List getInnerServerList() {
        return SMUtil.queryBySql("select * from t_server_list t where t.server_type='03'");
    }

    /**
	 * ��ñ����������Ϣ
	 * @return
	 */
    public static SelfConfig getSelfConfig() {
        return (SelfConfig) SMUtil.queryObject("getSelfConfig");
    }

    /**
	 * ��ݷ����ź��ⲿ��������ַ������ڲ��������ĸ��¼ƻ�״̬
	 * @param pid
	 * @param parentServer
	 * @return
	 */
    public static Map getInnerServerUpdatePlanState(String pid, String parentServer) {
        if (StringUtils.isBlank(pid) || StringUtils.isBlank(parentServer)) return null;
        String sql = "select c.result gxjg,c.result_msg gxjg_nr,to_char(c.end_time,'yyyy-mm-dd hh24:mi:ss') wcsj" + " from t_receive_update_rst c,t_server_list d" + " where c.server_ip=d.ip and d.server_type='03'" + " and d.parent_server='" + parentServer + "' and c.pid=" + pid;
        return SMUtil.queryMapBySql(sql);
    }

    public static void main(String[] args) {
        SelfConfig a = getSelfConfig();
        System.out.println(a.getDes_key());
    }
}
