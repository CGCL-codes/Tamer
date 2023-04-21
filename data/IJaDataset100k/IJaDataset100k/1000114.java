package ces.research.oa.util;

import java.util.List;
import java.util.Vector;
import org.springframework.context.ApplicationContext;
import ces.arch.bo.IBo;
import ces.coral.dbo.DBHandle;
import ces.research.oa.document.util.ApplicationContextFactory;

public class IndexCountUtil {

    private static DBHandle dbo = new DBHandle("oa");

    private static DBHandle dbw = new DBHandle("workflow");

    private static String sql = "";

    public static String getUnionValueByVector(Vector v) {
        if (!(null != v && v.size() > 0)) {
            return "0";
        }
        return ((String[]) v.elementAt(0))[0];
    }

    /**
	 * ͨ�� ���� �͵�ǰ��¼�û�Id ��ȡ�ڰ��ļ�����  
	 * @param tableName
	 * @param userId
	 * @return int
	 */
    public static int getUndoCount(String tableName, int userId) {
        sql = " select count(*)" + " from " + tableName + " doc," + " (select process_instance_id" + " from (select aa.process_instance_id," + "               row_number() over(partition by aa.process_instance_id order by aa.id desc) no" + "          from t_wf_workitem aa," + "               T_WF_PROCESS_INSTANCE cc," + "               (select distinct t.process_instance_id" + "                 from t_wf_workitem t) bb" + "         where aa.process_instance_id = bb.process_instance_id" + "           and aa.status <> 13" + "           and aa.status <> 15" + "           and aa.status <> 14" + "           and aa.owner_id = " + userId + "           and cc.id = bb.process_instance_id)" + " where no = 1) tmp" + " where tmp.process_instance_id = doc.process_instance_id";
        return Integer.parseInt(getUnionValueByVector(dbw.select(sql)));
    }

    /**
	 * ͨ�� �ؼ��� �͵�ǰ��¼�û�Id ��ȡ�����ļ�����
	 * @param keyWord
	 * @param userId
	 * @return int
	 */
    public static int getUnyueCount(String keyWord, int userId) {
        sql = " select count(*)" + " from t_wf_lend_out lo" + " inner join T_WF_PROCESS_INSTANCE pi on lo.PROCESS_INSTANCE_ID = pi.id" + " where pi.PROCESS_ID = '" + keyWord + "'" + " and lo.STATUS = 0" + " and lo.TO_USER = " + userId;
        return Integer.parseInt(getUnionValueByVector(dbw.select(sql)));
    }

    /**
	 * ��ѯ��ǰ��¼�û���ص����ڶ������Ϣ����, ��date������ֵ���ѯָ�����ڵ�����
	 * @param date
	 * @param userId
	 * @return int
	 */
    public static int getInspectingCount(String date, int userId) {
        sql = "select count(*) from t_oa_inspect_info t where IS_END=0 AND register_user_id=" + userId;
        if (date != null) {
            sql += " AND END_DATE=to_date('" + date + "','RR-mm-dd')";
        }
        return Integer.parseInt(getUnionValueByVector(dbo.select(sql)));
    }

    /**
	 * ��ѯ��ǰ��¼�û����֪ͨ��Ϣ, ���Ǽ�ʱ�䵹��, typeΪ֪ͨ����:0-һ��֪ͨ; 1-����֪ͨ;
	 * @param type
	 * @param userId
	 * @return
	 */
    public static List getMessages(int type, int userId) {
        sql = "select * from ( select t.id, t.title, t.register_time from t_oa_messages_info t " + " where  " + "  t.message_type=" + type + " and ( t.register_user_id=" + userId + "  or to_char(t.scope) like '%" + userId + "%' or to_char(t.scope)='0' or to_char(t.scope)='" + userId + "' )" + " and (status='1' or issue='yes') order by t.id desc ) where rownum<5 ";
        if (1 == type) {
            sql = "select * from ( select t.id, t.title, t.register_time from t_oa_messages_info t " + " where  " + "  t.message_type=" + type + " and (t.register_user_id=" + userId + "  or to_char(t.scope) like '%" + userId + "%' or to_char(t.scope)='0' or to_char(t.scope)='" + userId + "' )" + " and (status='1' or issue='yes') order by t.id desc) where rownum<5 ";
        }
        return dbo.select(sql);
    }

    /**
	 * ��ѯ��ǰ��¼�û����ڵ�ֵ�ల��, ��ʱ�䵹��
	 * @param userId
	 * @return
	 */
    public static List getKeepWatch(int userId) {
        sql = "select t.id, t.keepwatch_date, t.keepwatch_period_time, t.keepwatch_type, t.principal_name, t.lead_name " + " from t_oa_keepwatch_info t " + " where rownum<4 AND t.keepwatch_person_id=" + userId + " order by t.keepwatch_date desc";
        return dbo.select(sql);
    }

    public static void main(String[] args) {
        System.out.println(IndexCountUtil.getInspectingCount(null, 28504));
    }
}
