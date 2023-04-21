package client.sms;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.ServiceException;
import net.sf.hibernate.HibernateException;
import client.IAPInfo;
import client.smsConfig;
import com.creawor.hz_market.t_infor_review.t_infor_review;
import com.creawor.hz_market.t_infor_review.t_infor_review_EditMap;
import com.creawor.hz_market.t_information.t_information_EditMap;
import com.creawor.hz_market.t_information.t_information_Form;
import com.creawor.hz_market.t_information.t_information_QueryMap;
import com.creawor.hz_market.util.ComQuery;

public class SMSService {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(SMSService.class);

    /**
	 * @desc:�����鱨Ԥ�����ܡ�(�Ѳ���)
	 * @param info :
	 * @auther : chen
	 * @date : Jan 8, 2009
	 */
    public static void SendDuishouInfo(t_information_Form info) {
        if (logger.isDebugEnabled()) {
            logger.debug("���Ͷ����鱨(t_information_Form) - start company=" + info.getCompany() + " leveltype=" + info.getLeveltype());
        }
        if ("����".equalsIgnoreCase(info.getCompany()) && "����".equals(info.getLeveltype())) {
            String[] mobiles = smsConfig.getInst().getMobiles();
            if (logger.isDebugEnabled()) {
                logger.debug("���Ͷ����鱨Ԥ����" + mobiles[0] + "content:" + info.getContent());
            }
            sendSMS(mobiles, info.getId(), info.getContent());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SendDuishouInfo(t_information_Form) - end");
        }
    }

    public static void inforAlert(t_information_Form info) {
        if ("����Ӫ��".equalsIgnoreCase(info.getInfo_type())) {
            CompanyInfoAlert(info);
        } else if ("���簲ȫ".equalsIgnoreCase(info.getInfo_type())) {
            SMSService service = new SMSService();
            service.sendSMS(info.getMobiles(), info.getId(), info.getContent());
        } else {
        }
    }

    public static void NetInfoAlert(t_information_Form info) {
        if (logger.isDebugEnabled()) {
            logger.debug("���ü����鱨���� company=" + info.getCompany());
        }
        if ("����".equalsIgnoreCase(info.getInfo_type())) return;
        if (logger.isDebugEnabled()) {
            logger.debug("���ž������͹���!");
        }
        String[] mobiles = null;
        if ("һ��Ԥ��".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevel1(info.getCounty());
        } else if ("����Ԥ��".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevel2(info.getCounty());
        } else if ("��Ԥ��".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevel3(info.getCounty());
        } else if ("��һ����Ӧ".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevelS1(info.getCounty());
        } else if ("�ж�����Ӧ".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevelS2(info.getCounty());
        } else if ("������Ӧ".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevelS3(info.getCounty());
        } else if ("���ļ���Ӧ".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.netAlertLevelS4(info.getCounty());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("��ʼ���� ���ž�������" + info.getContent());
        }
        sendSMS(mobiles, info.getId(), info.getName() + ":\r\n" + info.getContent() + "  \r\n ��Ϣ��Դ��" + info.getCounty());
        if (logger.isDebugEnabled()) {
            logger.debug("webSendDuishouInfo(t_information_Form) - end");
        }
    }

    /**
	 * @desc:�����鱨Ԥ������
	 * @param info :
	 * @auther : chen
	 * @date : Jan 8, 2009
	 */
    public static void CompanyInfoAlert(t_information_Form info) {
        if (logger.isDebugEnabled()) {
            logger.debug("���ü����鱨���� company=" + info.getCompany());
        }
        if ("����".equalsIgnoreCase(info.getInfo_type())) return;
        if (logger.isDebugEnabled()) {
            logger.debug("���ž������͹���!");
        }
        String[] mobiles = null;
        if ("�񼶸澯".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.alertLevel1(info.getCounty());
        } else if ("�򼶸澯".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.alertLevel2(info.getCounty());
        } else if ("�󼶸澯".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.alertLevel3(info.getCounty());
        } else if ("�����澯".equalsIgnoreCase(info.getAlert_level())) {
            mobiles = AlertPeople.alertLevel4(info.getCounty());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("��ʼ���� ���ž�������" + info.getContent());
        }
        sendSMS(mobiles, info.getId(), info.getName() + ":\r\n" + info.getContent() + "  \r\n ��Ϣ��Դ��" + info.getCounty());
        if (logger.isDebugEnabled()) {
            logger.debug("webSendDuishouInfo(t_information_Form) - end");
        }
    }

    public static String sendSMS(String[] mobiles, String infoId, String content) {
        String rsStr = null;
        if (!IAPInfo.getInst().isSMS()) {
            rsStr = "ϵͳû���������Ź��ܣ�";
            return null;
        }
        logger.info("sendSMS(String[], String, String) - start");
        content = "��ս���鱨ɳ��������\r\n " + content;
        logger.info("��Ϣ���=" + infoId + "\r\n ���ݣ�" + content + "\r\n �����ֻ���" + mobiles.length);
        if (null == mobiles || mobiles.length == 0) {
            return null;
        } else {
            String m = null;
            for (int j = 0; j < mobiles.length; j++) {
                m = mobiles[j];
                logger.info(j + "�ֻ�" + m);
            }
        }
        System.out.println(content);
        try {
            NotifyLocator notifyLocator = new NotifyLocator();
            NotifySoap_PortType notify = notifyLocator.getNotifySoap();
            try {
                ComQuery stm = new ComQuery();
                String mobile;
                for (int i = 0; i < mobiles.length; i++) {
                    mobile = mobiles[i];
                    try {
                        logger.info("notify.sendSMS(" + IAPInfo.getInst().getSystemid() + "," + IAPInfo.getInst().getSysAccount() + "," + IAPInfo.getInst().getSysPassword() + ",\"\"," + mobile + ",\"\",\"\",false,\"\",true,false,\"\"," + content);
                        SendSMSResult rs = notify.sendSMS(IAPInfo.getInst().getSystemid(), IAPInfo.getInst().getSysAccount(), IAPInfo.getInst().getSysPassword(), "", mobile, "", "", false, "", true, false, "", content);
                        System.out.println("SendSMSID=" + rs.getSendSMSID());
                        if (null != infoId) {
                            String upsql = "insert into t_sms_revert(infor_id,sms_id,mobile) values(" + infoId + ",'" + rs.getSendSMSID() + "','" + mobile + "')";
                            stm.updateDB(upsql);
                        }
                    } catch (Exception e) {
                        logger.error("#######���ö��ŷ��Ͷ˿ڳ���", e);
                    }
                }
            } catch (Exception e) {
                logger.error("sendSMS(String[], String, String)", e);
                e.printStackTrace();
            }
        } catch (ServiceException e) {
            logger.error("sendSMS(String[], String, String)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("sendSMS(String[], String, String) - end");
        }
        return rsStr;
    }

    public static String sendSMSOnly(String[] mobiles, String infoId, String content) {
        if (logger.isDebugEnabled()) {
            logger.debug("sendSMSOnly(String[], String, String) - start");
        }
        String rsStr = null;
        try {
            NotifyLocator notifyLocator = new NotifyLocator();
            NotifySoap_PortType notify = notifyLocator.getNotifySoap();
            try {
                ComQuery stm = new ComQuery();
                String mobile;
                for (int i = 0; i < mobiles.length; i++) {
                    mobile = mobiles[i];
                    try {
                        SendSMSResult rs = notify.sendSMS(IAPInfo.getInst().getSystemid(), IAPInfo.getInst().getSysAccount(), IAPInfo.getInst().getSysPassword(), "", mobile, "", "", false, "", true, false, "", content);
                        System.out.println("SendSMSID=" + rs.getSendSMSID());
                        String upsql = "insert into t_sms_revert(infor_id,sms_id,mobile) values(" + infoId + ",'" + rs.getSendSMSID() + "','" + mobile + "')";
                        stm.updateDB(upsql);
                    } catch (Exception e) {
                        logger.error("sendSMSOnly(String[], String, String)", e);
                    }
                }
            } catch (Exception e) {
                logger.error("sendSMSOnly(String[], String, String)", e);
                e.printStackTrace();
            }
        } catch (ServiceException e) {
            logger.error("sendSMSOnly(String[], String, String)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("sendSMSOnly(String[], String, String) - end");
        }
        return rsStr;
    }

    public void getRevert() {
        if (logger.isDebugEnabled()) {
            logger.debug("getRevert() - start");
        }
        try {
            NotifyLocator notifyLocator = new NotifyLocator();
            NotifySoap_PortType notify = notifyLocator.getNotifySoap();
            SMSResult rs = notify.receiveSMS(IAPInfo.getInst().getSystemid(), IAPInfo.getInst().getSysAccount(), IAPInfo.getInst().getSysPassword(), true);
            SmsDetail smss[] = rs.getSmsInfo();
            SmsDetail sms = null;
            for (int i = 0; i < smss.length; i++) {
                sms = smss[i];
                Revert2DB(sms.getContent(), sms.getFromMobile(), sms.getSendSMSID());
            }
        } catch (Exception e) {
            logger.error("getRevert()", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getRevert() - end");
        }
    }

    public void getRevertFB() {
        if (logger.isDebugEnabled()) {
            logger.debug("getRevertFB() - start");
        }
        try {
            NotifyLocator notifyLocator = new NotifyLocator();
            NotifySoap_PortType notify = notifyLocator.getNotifySoap();
            KWSMSResult rs = notify.receiveKeywordSMS(IAPInfo.getInst().getSystemid(), IAPInfo.getInst().getSysAccount(), IAPInfo.getInst().getSysPassword(), "sp", false);
            KWSMSDetail smss[] = rs.getKwSMSInfo();
            KWSMSDetail sms = null;
            for (int i = 0; i < smss.length; i++) {
                sms = smss[i];
                System.out.println(sms.getExContent());
            }
        } catch (Exception e) {
            logger.error("getRevertFB()", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getRevertFB() - end");
        }
    }

    public boolean ldaoprocess(String content, String fromMobile, String smsId) {
        if (logger.isDebugEnabled()) {
            logger.debug("ldaoprocess(String, String, String) - start");
        }
        String pMobile = (String) smsConfig.getInst().getProcessuser().get(smsConfig.USER_MOBILE);
        boolean flg = false;
        String sql = "select u.mobile, u.dept_code, u.username, r.role_name,r.description from t_user_role ur ,t_role r ,t_user u where ur.role_id=r.id and ur.user_id=u.id  and (role_name='countyManager' or role_name='infomanager') ";
        if (null != fromMobile || AlertPeople.isprocessMobile(fromMobile)) {
            if ("sy".equalsIgnoreCase(content)) {
                flg = true;
            } else if ("sg".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='�й�˾'";
            } else if ("sq".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='����ֹ�˾'";
            } else if ("sf".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='�·�'";
            } else if ("ry".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='��Դ'";
            } else if ("wy".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='��Դ'";
            } else if ("lc".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='�ֲ�'";
            } else if ("nx".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='����'";
            } else if ("qj".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='��'";
            } else if ("rh".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='�ʻ�'";
            } else if ("sx".equalsIgnoreCase(content)) {
                flg = true;
                sql += " and dept_code='ʼ��'";
            }
            if (flg) {
                String inforId = getInforIdBySMSId(smsId);
                if (null == inforId) return false;
                String userName = getUserInfo(fromMobile);
                if (null == userName) userName = fromMobile;
                try {
                    t_information_QueryMap query = new t_information_QueryMap();
                    t_information_Form inform = query.getByID(inforId);
                    List ls = ComQuery.find(sql);
                    List mobils = new ArrayList();
                    if (null != ls) {
                        Iterator it = ls.iterator();
                        Map user = null;
                        while (it.hasNext()) {
                            user = (Map) it.next();
                            if (null != user.get("mobile")) {
                                mobils.add(user.get("mobile"));
                            }
                        }
                        String[] mobilarr = AlertPeople.List2Array(mobils);
                        sendSMS(mobilarr, inforId, inform.getContent());
                    }
                } catch (Exception e) {
                    logger.error("ldaoprocess(String, String, String)", e);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("ldaoprocess(String, String, String) - end");
        }
        return flg;
    }

    public void Revert2DB(String content, String fromMobile, String smsId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Revert2DB(String, String, String) - start");
        }
        String inforId = getInforIdBySMSId(smsId);
        if (null == inforId) return;
        String userName = getUserInfo(fromMobile);
        if (null == userName) userName = fromMobile;
        try {
            if (ldaoprocess(content, fromMobile, smsId)) return;
            String flgStr = content.substring(0, 2);
            if (flgStr.toLowerCase().startsWith("qb")) {
                String deptName = this.getUserInfoDept(fromMobile);
                t_information_Form infor = new t_information_Form();
                infor.setContent(content.substring(2, content.length()));
                infor.setName("�ֻ��鱨");
                infor.setType("info");
                infor.setLeveltype("����");
                infor.setOpentype("����");
                infor.setReview_flag("0");
                infor.setWriter(userName);
                infor.setCompany("�ƶ�");
                infor.setCounty(deptName);
                t_information_EditMap editMap = new t_information_EditMap();
                editMap.add(infor);
            } else {
                t_infor_review newreview = new t_infor_review();
                newreview.setContent(content);
                newreview.setCreate_name(userName);
                newreview.setInfor_id(Integer.parseInt(inforId));
                newreview.setInsert_day(new Date());
                t_infor_review_EditMap reviewEdit = new t_infor_review_EditMap();
                reviewEdit.add(newreview);
            }
        } catch (HibernateException e) {
            logger.error("Revert2DB(String, String, String)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Revert2DB(String, String, String) - end");
        }
    }

    public String getInforIdBySMSId(String smsId) {
        if (logger.isDebugEnabled()) {
            logger.debug("getInforIdBySMSId(String) - start");
        }
        String sql = "select * from t_sms_revert where sms_id='" + smsId + "'";
        ComQuery stm = new ComQuery();
        List list = stm.find(sql);
        if (null != list && list.size() > 0) {
            Map row = (Map) list.get(0);
            String returnString = (String) row.get("infor_id");
            if (logger.isDebugEnabled()) {
                logger.debug("getInforIdBySMSId(String) - end");
            }
            return returnString;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getInforIdBySMSId(String) - end");
        }
        return null;
    }

    public String getUserInfo(String mobile) {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserInfo(String) - start");
        }
        String sql = "select * from t_user where mobile='" + mobile + "'";
        ComQuery stm = new ComQuery();
        List list = stm.find(sql);
        if (null != list && list.size() > 0) {
            Map row = (Map) list.get(0);
            String returnString = (String) row.get("usercode");
            if (logger.isDebugEnabled()) {
                logger.debug("getUserInfo(String) - end");
            }
            return returnString;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getUserInfo(String) - end");
        }
        return null;
    }

    public String getUserInfoDept(String mobile) {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserInfoDept(String) - start");
        }
        String sql = "select * from t_user where mobile='" + mobile + "'";
        ComQuery stm = new ComQuery();
        List list = stm.find(sql);
        if (null != list && list.size() > 0) {
            Map row = (Map) list.get(0);
            String returnString = (String) row.get("dept_code");
            if (logger.isDebugEnabled()) {
                logger.debug("getUserInfoDept(String) - end");
            }
            return returnString;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getUserInfoDept(String) - end");
        }
        return null;
    }

    public void setRevertAsReview(String infoId, String revewContent, String mobile) {
        if (logger.isDebugEnabled()) {
            logger.debug("setRevertAsReview(String, String, String) - start");
        }
        String usesql = "select * from t_user where mobile='" + mobile + "'";
        ComQuery query = new ComQuery();
        List rs = query.find(usesql);
        if (null != rs && rs.size() > 0) {
            Map row = (Map) rs.get(0);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("setRevertAsReview(String, String, String) - end");
        }
    }

    public static void main(String[] args) {
        if (logger.isDebugEnabled()) {
            logger.debug("main(String[]) - start");
        }
        SMSService service = new SMSService();
        service.getRevert();
        if (logger.isDebugEnabled()) {
            logger.debug("main(String[]) - end");
        }
    }
}
