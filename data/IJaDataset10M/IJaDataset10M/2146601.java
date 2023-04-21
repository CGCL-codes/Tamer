package ces.platform.infoplat.core.dao;

import java.sql.*;
import java.util.*;
import ces.platform.infoplat.core.*;
import ces.platform.infoplat.core.base.*;
import ces.platform.infoplat.core.tree.*;
import ces.platform.infoplat.utils.*;
import ces.platform.system.facade.*;

/**
 *  <b>Ȩ�޵��뵼����</b> <br>
 *
 *
 *@Title      ��Ϣƽ̨
 *@Company    �Ϻ�������Ϣ���޹�˾
 *@version    2.5
 *@author     ����
 *@created    2004��4��14��
 */
public class PepodomDAO extends BaseDAO {

    public PepodomDAO() {
    }

    /**
     * �����Դ��Ȩ���б���Ϣ
     * @param pepodom
     * @return
     * @throws java.lang.Exception
     */
    public Pepodom[] getExtPepodom(String resID) throws Exception {
        try {
            AuthorityManager am = new AuthorityManager();
            Vector vSaa = am.getExtAuthAssign(resID, Const.RES_TYPE_ID);
            StructAuthAssign[] Saas = new StructAuthAssign[vSaa.size()];
            Pepodom[] ps = new Pepodom[vSaa.size()];
            for (int n = 0; n < vSaa.size(); n++) {
                ps[n] = new Pepodom();
            }
            int i;
            int j;
            for (i = 0, j = 0; i < vSaa.size() && j < vSaa.size(); i++) {
                Saas[i] = (StructAuthAssign) vSaa.get(i);
                boolean flag = false;
                for (int m = 0; m <= j; m++) {
                    if (Saas[i].getUserID() == ps[m].getUserID()) {
                        int operateNum = Integer.parseInt(Saas[i].getOperateID());
                        String operateID = ps[m].getOperateID().substring(0, operateNum - 1) + "1" + ps[m].getOperateID().substring(operateNum);
                        ps[m].setOperateID(operateID);
                        String[] authIDsString = Function.stringToArray(ps[m].getAuthorityID());
                        authIDsString[operateNum - 1] = Integer.toString(Saas[i].getAuthorityID());
                        ps[m].setAuthorityID(Function.arrayToStr(authIDsString));
                        String[] provIDs = Function.stringToArray(ps[m].getProvidID());
                        provIDs[operateNum - 1] = Integer.toString(Saas[i].getProviderID());
                        ps[m].setProvidID(Function.arrayToStr(provIDs));
                        flag = true;
                    }
                }
                if (!flag) {
                    ps[j].setUserID(Saas[i].getUserID());
                    ps[j].setUserName(Saas[i].getUserName());
                    ps[j].setResID(Saas[i].getResourceID());
                    int operateNum = Integer.parseInt(Saas[i].getOperateID());
                    String operateID = ps[j].getOperateID().substring(0, operateNum - 1) + "1" + ps[j].getOperateID().substring(operateNum);
                    ps[j].setOperateID(operateID);
                    String[] authIDsString = Function.stringToArray(ps[j].getAuthorityID());
                    authIDsString[operateNum - 1] = Integer.toString(Saas[i].getAuthorityID());
                    ps[j].setAuthorityID(Function.arrayToStr(authIDsString));
                    String[] provIDs = Function.stringToArray(ps[j].getProvidID());
                    provIDs[operateNum - 1] = Integer.toString(Saas[i].getProviderID());
                    ps[j].setProvidID(Function.arrayToStr(provIDs));
                    j++;
                }
            }
            Pepodom[] ps1 = new Pepodom[j];
            for (int k = 0; k < j; k++) {
                ps1[k] = ps[k];
            }
            return ps1;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * ���Ѿ��е��û���Ȩ�޵���������
     * @param ps�����û�Ϊ��λ�����飩
     * @throws java.lang.Exception
     */
    public void assignExtPepodom(Pepodom[] ps) throws Exception {
        try {
            Vector vSaa = new Vector();
            Vector vDsaa = new Vector();
            AuthorityManager am = new AuthorityManager();
            for (int i = 0; i < ps.length; i++) {
                for (int j = 0; j < Const.OPERATE_ID_INIT.length(); j++) {
                    String[] authIDsString = Function.stringToArray(ps[i].getAuthorityID());
                    int authID = Integer.parseInt(authIDsString[j]);
                    String[] provIDs = Function.stringToArray(ps[i].getProvidID());
                    int providerID = Integer.parseInt(provIDs[j]);
                    if (ps[i].getOperateID().substring(j, j + 1).equalsIgnoreCase("1")) {
                        if (authID == 0) {
                            StructAuthAssign saa = new StructAuthAssign();
                            StructResource sr = new StructResource();
                            sr.setResourceID(ps[i].getResID());
                            sr.setOperateID(Integer.toString(j + 1));
                            sr.setOperateTypeID(ps[i].getOperateTypeID());
                            sr.setTypeID(ps[i].getResTypeID());
                            StructAuth sa = new AuthorityManager().getExternalAuthority(sr);
                            authID = sa.getAuthID();
                            saa.setAuthorityID(authID);
                            saa.setEndTime((Timestamp) ps[i].getEndTime());
                            saa.setOperateID(Integer.toString(j + 1));
                            saa.setOperateTypeID(ps[i].getOperateTypeID());
                            providerID = ps[i].getLoginProvider();
                            saa.setProviderID(providerID);
                            saa.setResourceID(ps[i].getResID());
                            saa.setUserID(ps[i].getUserID());
                            vSaa.add(saa);
                        }
                    } else {
                        if (authID != 0) {
                            StructAuthAssign saa = new StructAuthAssign();
                            saa.setAuthorityID(authID);
                            saa.setOperateTypeID(ps[i].getOperateTypeID());
                            saa.setProviderID(ps[i].getLoginProvider());
                            saa.setUserID(ps[i].getUserID());
                            vDsaa.add(saa);
                        }
                    }
                }
            }
            am.deleteExtAuthAssignBatch(vDsaa);
            am.assignExtAuthToUserBatch(vSaa);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * ��ĳһ����Դ����������Դ���踸��Դ��Ȩ��
     * �������Դ�ĸ��ӹ�ϵ��վ��Ƶ�������ĵ�����������
     * @param Ps Ȩ����
     * @param treePath ���ڵ����·��
     * @throws java.lang.Exception
     */
    public void assignAllChildren(Pepodom[] ps1, String treePath) throws Exception {
        Pepodom[] pepodoms = new Pepodom[ps1.length];
        for (int i = 0; i < ps1.length; i++) {
            pepodoms[i] = ps1[i];
        }
        TreeNode[] tns = null;
        int len = treePath.length();
        if (len > 10) {
            Channel channel = new Channel();
            channel.setPath(treePath);
            tns = channel.getList();
        } else {
            Site site = new Site();
            site.setPath(treePath);
            tns = site.getList();
        }
        try {
            if (tns != null) {
                for (int i = 0; i < tns.length; i++) {
                    String resID = Integer.toString(Const.CHANNEL_TYPE_RES + ((Channel) tns[i]).getChannelID());
                    String resName = tns[i].getTitle();
                    AuthorityManager am = new AuthorityManager();
                    boolean hasResource = am.hasSysResource(resID, Const.OPERATE_TYPE_ID);
                    if (!hasResource) {
                        int operateTypeID = Const.OPERATE_TYPE_ID;
                        int resTypeID = Const.RES_TYPE_ID;
                        String remark = "";
                        am.createExtResource(resID, resName, resTypeID, operateTypeID, remark);
                    }
                    Pepodom[] ps = Pepodom.getExtPepodom(resID);
                    for (int j = 0; j < pepodoms.length; j++) {
                        int m = 0;
                        for (m = 0; m < ps.length; m++) {
                            if (ps[m].getUserID() == pepodoms[j].getUserID()) {
                                pepodoms[j].setAuthorityID(ps[m].getAuthorityID());
                                pepodoms[j].setResID(resID);
                                break;
                            }
                        }
                        if (m == ps.length) {
                            pepodoms[j].setAuthorityID(Const.AUTHORITY_ID);
                            pepodoms[j].setResID(resID);
                        }
                    }
                    Pepodom.assignExtPepodom(pepodoms, tns[i].getPath(), true);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * ��ӵ����û������Ȩ����Ĭ�ϵ���СȨ��
     * @param p
     * @throws java.lang.Exception
     */
    public void addNewActor(Pepodom p) throws Exception {
        StructResource sr = new StructResource();
        AuthorityManager authorityManager = new AuthorityManager();
        try {
            for (int i = 0; i < Const.OPERATE_ID_INIT.length(); i++) {
                if (p.getOperateID().substring(i, i + 1).equalsIgnoreCase("1")) {
                    sr.setResourceID(p.getResID());
                    sr.setOperateID(Integer.toString(i + 1));
                    sr.setOperateTypeID(p.getOperateTypeID());
                    sr.setTypeID(p.getResTypeID());
                    log.debug("2*: " + sr.getResourceID() + " : " + sr.getOperateID() + " : " + sr.getOperateTypeID() + " : " + sr.getTypeID());
                    StructAuth sa = authorityManager.getExternalAuthority(sr);
                    int authID = sa.getAuthID();
                    int userID = p.getUserID();
                    Authentication auth = new Authentication();
                    boolean hasPermission = auth.isAuthAssignExist(userID, authID);
                    if (!hasPermission) {
                        int providerID = p.getLoginProvider();
                        log.debug("3*: " + userID + " : " + authID + " : " + providerID);
                        authorityManager.assignExtAuthToUser(userID, authID, providerID);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("����û�Ȩ��ʧ��!", ex);
            throw ex;
        }
    }

    /**
     * �ж�һ���û��Ƿ���ж�Ӧһ����Դ��ĳһ��Ȩ��
     * @param p
     * @return
     * @throws java.lang.Exception
     */
    public boolean isDisplay(Pepodom p, Hashtable extrAuths) throws Exception {
        try {
            StructResource sr = new StructResource();
            int userID = 0;
            int authID = 0;
            int operateNum = 0;
            for (int i = 0; i < Const.OPERATE_ID_INIT.length(); i++) {
                if (p.getOperateID().substring(i, i + 1).equalsIgnoreCase("1")) {
                    operateNum = i;
                    break;
                }
            }
            sr.setResourceID(p.getResID());
            sr.setOperateID(Integer.toString(operateNum + 1));
            sr.setOperateTypeID(p.getOperateTypeID());
            sr.setTypeID(p.getResTypeID());
            StructAuth sa = new AuthorityManager().getExternalAuthority(sr);
            authID = sa.getAuthID();
            userID = p.getLoginProvider();
            Authentication auth = new Authentication();
            boolean flag = auth.hasPermission(userID, authID, extrAuths);
            return flag;
        } catch (Exception ex) {
            log.error("�ж�һ���û��Ƿ���ж�Ӧһ����Դ��ĳһ��Ȩ��ʧ��!", ex);
            throw ex;
        }
    }

    /**
     * �õ�һ���û���һ����Դ�Ĳ���
     *
     */
    public void getCertificatedOperations(Pepodom p) {
    }
}
