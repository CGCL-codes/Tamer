package ces.platform.system.facade;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import ces.coral.lang.StringUtil;
import ces.coral.log.Logger;
import ces.platform.system.common.CesSystemException;
import ces.platform.system.common.Constant;
import ces.platform.system.common.IdGenerator;
import ces.platform.system.dbaccess.Authority;
import ces.platform.system.dbaccess.AuthorityAssign;
import ces.platform.system.dbaccess.Figure;
import ces.platform.system.dbaccess.Resource;
import ces.platform.system.dbaccess.User;
/**
 * <p>����:
 * <font class=titlefont>
 * ��Ȩ�޹�����
 * </font>
 * <p>����:
 * <font class=descriptionfont>
 * <br>Ȩ�޶��塢���û���Ȩ��
 * </font>
 * <p>�汾��:
 * <font class=versionfont>
 * Copyright (c) 2.50.2003.1223
 * </font>
 * <p>��˾:
 * <font class=companyfont>
 * �Ϻ�������Ϣ��չ���޹�˾
 * </font>
 * @author �Ĺ���
 * @version 2.50.2003.1223
 */

public class AuthorityManager {

    static Logger logger=new Logger(AuthorityManager.class);

    static final int EXTERNAL_PAREANT = -1;
    public AuthorityManager(){}

    /**
     * ϵͳȨ�޶��巽������ЩȨ�޿���ϵͳ����ƽ̨�����й���
     * @param String resId ��ԴID
     * @param String resName ��Դ����
     * @param int resTypeID ��Դ����
     * @param int operateTypeID ��������ID
     * @param String remark ��Դ˵��
     * @throws Exception �������Ȩ�޳������׳��쳣
     */
    public void createSysResource(String resId,
                                String resName,
                                int resTypeID,
                                int operateTypeID,
                                String remark
                                ) throws Exception{

        try{
            int intId = (int)(IdGenerator.getInstance().getId(IdGenerator.GEN_ID_SYS_RESOURCE));
            String strPath = Constant.ROOT_IDENTIFIER+Constant.RESOURCE_SEPARATOR+resId;
            strPath = StringUtil.replaceAll(strPath,Constant.RESOURCE_SEPARATOR,Constant.RESOURCE_SEPARATOR_STORE);
            String parentPath = resId.substring(0,strPath.lastIndexOf(Constant.RESOURCE_SEPARATOR_STORE));
            logger.debug("parentPath = "+parentPath);

            Resource objParent = new Resource(parentPath);
            objParent.loadByPath();
            Resource objTemp = new Resource(intId);
            objTemp.setTypeID(resTypeID);
            objTemp.setOperateTypeID(operateTypeID);
            objTemp.setParentID(objParent.getId());
            objTemp.setName(resName);
            objTemp.setPath(resId);
            objTemp.setPathName(strPath);
            objTemp.setLevelNum(objParent.getLevelNum()+1);
            objTemp.setRemark(remark);
            objTemp.setResourceID(resId.substring(resId.lastIndexOf(",")+1));

            objTemp.doNew();

        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::createSysResource()"
                    +" Exception while do createSysResource"+e.toString());
        }
    }

    /**
     * �ⲿ��Դ���巽������Щ��Դֻ�洢��ϵͳ����ƽ̨�����������й���,
     * ��Щ��Դ�����������ͽṹ
     * @param String resId ��ԴID
     * @param String resName ��Դ����
     * @param int resTypeID ��Դ����
     * @param int operateTypeID ��������ID
     * @param String remark ��Դ˵��
     * @throws Exception �������Ȩ�޳������׳��쳣
     */
    public void createExtResource(String resId,
                                String resName,
                                int resTypeID,
                                int operateTypeID,
                                String remark
                                ) throws Exception{
        try{
            int intId = (int)(IdGenerator.getInstance().getId(IdGenerator.GEN_ID_SYS_RESOURCE));
            String strPath = Constant.ROOT_IDENTIFIER+Constant.RESOURCE_SEPARATOR+resId;
            strPath = StringUtil.replaceAll(strPath,Constant.RESOURCE_SEPARATOR,Constant.RESOURCE_SEPARATOR_STORE);

            Resource objTemp = new Resource(intId);
            objTemp.setTypeID(resTypeID);
            objTemp.setOperateTypeID(operateTypeID);
            objTemp.setParentID(AuthorityManager.EXTERNAL_PAREANT);
            objTemp.setName(resName);
            objTemp.setPath(strPath);
            objTemp.setPathName(""); //��·������Ϊ��
            objTemp.setLevelNum(0);  //���������Ϊ0
            objTemp.setRemark(remark);
            objTemp.setResourceID(strPath.substring(strPath.lastIndexOf(Constant.RESOURCE_SEPARATOR_STORE)+1));

            objTemp.doNew();
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::createExtResource()"
                    +" Exception while do createExtResource"+e.toString());
        }
    }

    /**
     * �ж�ϵͳ���Ƿ��������Ҫ��ϵͳ��Դ
     * @param strRes String �ⲿ�ṩ����Դ��ʶ��
     * @param intResTypeID int ��Դ����
     * @throws Exception ����������׳��쳣
     * @return boolean ������ڣ��򷵻�True�����򷵻�false
     */
    public boolean hasSysResource(String strRes,
                                  int intResTypeID) throws Exception{
        boolean blnRest = false;
        try{
            Resource res = new Resource();
            blnRest = res.isExist(strRes,intResTypeID);
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::hasSysResource()"
                    +" Exception while do hasSysResource"+e.toString());
        }
        return blnRest;
    }

    /**
      * ��ȡ���к�
      * @param strSname  ���ݿ������к�����
      * @return          �������к�
      *
     */
//    public long getSequenceId(String strName) throws Exception{
//        return IdGenerator.getInstance().getId(strName);
//    }

    /**
     * ��ȡ�ⲿȨ�ޣ���ЩȨ�޵Ĳ�����StructAuth�Ľṹ����
     * @param objSrt StructResource ��Դ��Ϣ�ṹ,�����������²�����
     * StructResource.resourceID ��Դ��ʶ
     * StructResource.type_id ��Դ����
     * StructResource.operateTypeID ����Դ�Ĳ�������
     * StructResource.operateID ����Դ�Ĳ���
     * @return ��õ�Ȩ����Ϣ��װ��StructAuth�ṹ����
     */
    public StructAuth getExternalAuthority(StructResource objSrt) throws Exception{
        StructAuth objSt = new StructAuth();
        try{
            //�����Դ����
            Resource objRes = new Resource();
            objRes.setResourceID(objSrt.getResourceID());
            objRes.setTypeID(objSrt.getTypeID());
            objRes.loadExtResource();
            //���Ȩ�޶���
            Authority au = new Authority();
            au.setRefID(objRes.getId());
            au.setOperateTypeId(objSrt.getOperateTypeID());
            au.setOperateID(objSrt.getOperateID());
            au.loadExternal();

            objSt.setAuthID(au.getAuthorityID());
            objSt.setResourceID(objSrt.getResourceID());
            objSt.setResourceName(au.getAuthorityName());
            objSt.setOperateTypeId(objSrt.getOperateTypeID());
            objSt.setOperateID(objSrt.getOperateID());

        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::getExternalAuthority()"
                    +" Exception while do getExternalAuthority"+e.toString());
        }

        return objSt;
    }

    /**
     * ��ȡ�ⲿȨ�ޣ���ЩȨ�޵Ĳ�����StructAuthAssign�Ľṹ����
     * @param resourceID ��Դ��ʶ
     * @param typeID ��Դ���ͺ�
     * @return �������ԴresId��ص���Ȩ��Ϣ�б��б��е�Ԫ����StructAuthAssign����
     */
    public Vector getExtAuthAssign(String resourceID,int typeID) throws Exception{
        StructAuthAssign objAs = null;
        Vector vcRet = new Vector();
        AuthorityAssign sysAs = null;
        try{
            //�����Դ����
            Resource objRes = new Resource();
            objRes.setResourceID(resourceID);
            objRes.setTypeID(typeID);
            objRes.loadExtResource();
            //��������ԴȨ��
            Vector vcAuthes = objRes.getAuthorities();
            Enumeration enumAu = vcAuthes.elements();
            Authority au = null;
            while(enumAu.hasMoreElements()){
                au = (Authority)enumAu.nextElement();
                Vector vcAssign = au.getAuthorityAssigns();
                Enumeration enumAs = vcAssign.elements();
                while (enumAs.hasMoreElements()) {
                    sysAs = (AuthorityAssign) enumAs.nextElement();
                    objAs = new StructAuthAssign();

                    objAs.setUserID(sysAs.getUser().getUserID());
                    objAs.setUserName(sysAs.getUser().getUserName());
                    objAs.setAuthorityID(sysAs.getAuthority().getAuthorityID());
                    objAs.setResourceID(resourceID);
                    objAs.setResourceName(objRes.getName());
                    objAs.setAuthorityType(au.getAuthorityType());
                    objAs.setOperateTypeID(au.getOperateTypeId());
                    objAs.setOperateID(au.getOperateID());
                    objAs.setProviderID(sysAs.getProvider().getUserID());
                    objAs.setProvidName(sysAs.getProvider().getUserName());

                    vcRet.add(objAs);
                }
            }

        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::getExtAuthAssign()"
                    +" Exception while do getExternalAuthority"+e.toString());
        }

        return vcRet;
    }

    /**
     * ��ȡĳ�û���ĳ��Դӵ�е��ⲿȨ�ޣ���ЩȨ�޵Ĳ�����StructAuthAssign�Ľṹ����
     * @param resourceID ��Դ��ʶ
     * @param typeID ��Դ���ͺ�
     * @param userID �û�ID
     * @return �������ԴresId��ص���Ȩ��Ϣ�б��б��е�Ԫ����StructAuthAssign����
     */
    public Vector getExtAuthAssignsOfUser(String resourceID,int typeID,int userID) throws Exception{
        StructAuthAssign objAs = null;
        Vector vcRet = new Vector();
        AuthorityAssign sysAs = null;
        try{
            //�����Դ����
            Resource objRes = new Resource();
            objRes.setResourceID(resourceID);
            objRes.setTypeID(typeID);
            objRes.loadExtResource();
            //��������ԴȨ��
            Vector vcAuthes = objRes.getAuthorities();
            Enumeration enumAu = vcAuthes.elements();
            Authority au = null;
            while(enumAu.hasMoreElements()){
                au = (Authority)enumAu.nextElement();
                Vector vcAssign = au.getAuthorityAssigns();
                Enumeration enumAs = vcAssign.elements();
                while (enumAs.hasMoreElements()) {
                    sysAs = (AuthorityAssign) enumAs.nextElement();
                    if (sysAs.getUser().getUserID() == userID) {
                        objAs = new StructAuthAssign();
                        objAs.setUserID(sysAs.getUser().getUserID());
                        objAs.setUserName(sysAs.getUser().getUserName());
                        objAs.setAuthorityID(sysAs.getAuthority().
                                             getAuthorityID());
                        objAs.setResourceID(resourceID);
                        objAs.setResourceName(objRes.getName());
                        objAs.setAuthorityType(au.getAuthorityType());
                        objAs.setOperateTypeID(au.getOperateTypeId());
                        objAs.setOperateID(au.getOperateID());
                        objAs.setProviderID(sysAs.getProvider().getUserID());
                        objAs.setProvidName(sysAs.getProvider().getUserName());

                        vcRet.add(objAs);
                    }
                }
            }

        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::getExtAuthAssignsOfUser()"
                    +" Exception while do getExtAuthAssignsOfUser"+e.toString());
        }

        return vcRet;
    }



    /**
     * ��ֻ�洢��ϵͳ����ƽ̨������ϵͳ����ƽ̨���Ƶ�Ȩ���ڸ��û���
     * @param intUserId int ����Ȩ�û���ID
     * @param intAuthId int �ڸ��û���Ȩ��ID
     * @param intProvider int ��Ȩ�ߵ��û�ID
     * @throws Exception ����������׳��쳣��
     */
    public void assignExtAuthToUser(int intUserId,int intAuthId,int intProvider) throws Exception{
        if(intUserId==0||intAuthId==0){
            throw new CesSystemException("AuthorityManager:assignExtAuthToUser()::Illegal data values for insert");
        }
        Authority objAu = new Authority(intAuthId);
        User objUser = new User(intUserId);
        User provider = new User(intProvider);
        try{
            objAu.load();
            objUser.load();
            provider.load();

            AuthorityAssign as = new AuthorityAssign();
            as.setUser(objUser);
            as.setUserFigure(Figure.DEFAULTFIGURE);
            as.setAuthority(objAu);
            as.setProvider(provider);
            as.setProviderFigure(Figure.DEFAULTFIGURE);

            as.doNew();
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager:assignExtAuthToUser()::Exception when assign External authority to User");
        }
    }

    /**
     * ��ֻ�洢��ϵͳ����ƽ̨������ϵͳ����ƽ̨���Ƶ�Ȩ���ڸ��û���
     * @param vcStructAuthAssign ��Ȩ�����ļ��ϡ�
     * @param StructAuthAssign.userID int ����Ȩ�û���ID
     * @param StructAuthAssign.authorityID int �ڸ��û���Ȩ��ID
     * @param StructAuthAssign.providID int ��Ȩ�ߵ��û�ID
     * @throws Exception ����������׳��쳣��
     */
    public void assignExtAuthToUserBatch(Vector vcStructAuthAssign) throws Exception{
        StructAuthAssign objSaa = null;
        int count = vcStructAuthAssign.size();
        int intUserId;
        int intAuthId;
        int intProvider;
        AuthorityAssign[] arrAdd = new AuthorityAssign[count];
         try{
             for (int i = 0; i < count; i++) {
                 objSaa = (StructAuthAssign)vcStructAuthAssign.get(i);
                 intUserId = objSaa.getUserID();
                 intAuthId = objSaa.getAuthorityID();
                 intProvider = objSaa.getProviderID();
                 if(intUserId==0||intAuthId==0||intProvider==0){
                     throw new CesSystemException("AuthorityManager:assignExtAuthToUserBatch()::Illegal data values for insert");
                 }
                 Authority objAu = new Authority(intAuthId);
                 User objUser = new User(intUserId);
                 User provider = new User(intProvider);

                 objAu.load();
                 objUser.load();
                 provider.load();

                 AuthorityAssign as = new AuthorityAssign();
                 as.setUser(objUser);
                 as.setUserFigure(Figure.DEFAULTFIGURE);
                 as.setAuthority(objAu);
                 as.setProvider(provider);
                 as.setProviderFigure(Figure.DEFAULTFIGURE);

                 arrAdd[i] = as;
             }

             new AuthorityAssign().doAddBatch(arrAdd);
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager:assignExtAuthToUser()::Exception when assign External authority to User");
        }
    }

    /**
     * ��ȡ�����û����ڸ������sql-where�Ӿ��ַ���
     * @param userID �û�ID
     * @param strTable ���ݱ���������ж������ʹ�ö��Ÿ���
     * @return �������Ӧ��Where������䣬�򷵻�Where�����Ӿ䣬���򷵻�null.
     * @throws Exception ����������׳��쳣
     */
    public String getSqlWhereOfUser(int userID,String strTable) throws Exception{
        String strRet = null;
        if(userID==0||strTable==null) return strRet;
        try{
            User objU = new User(userID);
            objU.load();
            strRet = objU.getSqlWhereOfUser(strTable);
        }catch(Exception e){
            logger.error("AuthorityManager::getSqlWhereOfUser():"+e);
            throw new CesSystemException("AuthorityManager::getSqlWhereOfUser()"
                    +" Exception while do getSqlWhereOfUser"+e.toString());
        }
        return strRet;
    }

    /**
     * ��ȡ�����û����ڸ������sql-where�Ӿ��ַ���,<br>
     * ����sql-where�Ӿ��ַ����еĲ���ʹ���û��ṩ�ļ�ֵ�Խ��д���<br>
     * �û���ֵ�ԣ�Hashtable�е�ֵ����ʽΪ��fieldName,value.<br>
     * �磺secret_level,3(��ʾ�ܼ���3��)
     * @param userID int �û�ID
     * @param strTable String ���ݱ���������ж������ʹ�ö��Ÿ���
     * @param hashTable Hashtable �洢��ֵ�ԣ��ֶ���:ֵ��
     * @return String �������Ӧ��Where������䣬�򷵻�Where�����Ӿ䣬���򷵻�null.
     * @throws Exception ����������׳��쳣
     */
    public String getSqlWhereOfUserWithParam(int userID,String strTable,Hashtable hashTable) throws Exception{
        String strRet = null;
        if(userID==0||strTable==null||hashTable==null) return strRet;
        try{
            User objU = new User(userID);
            objU.load();
            strRet = objU.getSqlWhereOfUser(strTable,hashTable);
        }catch(Exception e){
            logger.error("AuthorityManager::getSqlWhereOfUserWithParam():"+e);
            throw new CesSystemException("AuthorityManager::getSqlWhereOfUserWithParam()"
                    +" Exception while do getSqlWhereOfUserWithParam"+e.toString());
        }
        return strRet;

    }

    /**
     * ɾ����Դ���󣬲�ɾ�������Դ��ص�Ȩ�޼���Ȩ��ϵ��
     * @param resourceID int ��Դ��ʶ
     * @param typeID int ��Դ���ͱ�ʶ
     * @param userID int ִ��ɾ���������û�ID��һ��Ϊ���뵱ǰϵͳ���û�ID
     * @throws Exception ����������׳��쳣��
     */
    public void deleteExtResource(String resourceID,int typeID,int userID) throws Exception{
        Resource objRes = new Resource();
        try{
            User objUser = new User(userID);
            objUser.load();
            //�����Դ����

            objRes.setResourceID(resourceID);
            objRes.setTypeID(typeID);
            objRes.loadExtResource();
            objRes.setRuler(objUser);

            objRes.doDelete();
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager:deleteExtResource()"
            +" Exception while do deleteExtResource"+e.toString());
        }
    }

    /**
     * ����ɾ����ԴȨ�ޣ���ɾ������Щ��Դ��ص���Ȩ��ϵ��
     * @param vcStrutResource Vector ��Դ��Ϣ�б���Ԫ��ΪStrutResource���Ͷ���<br>
     * �ڸýṹ���е�Ԫ�ر������ã�<br>
     * resourceID int ��Դ��ʶ <br>
     * typeID int ��Դ���ͱ�ʶ <br>
     * @param userID int ִ��ɾ���������û�ID��һ��Ϊ���뵱ǰϵͳ���û�ID
     * @throws Exception ����������׳��쳣��
     */
    public void deleteExtResourceBatch(Vector vcStrutResource,int userID) throws Exception{
        Resource objRes = new Resource();
        int count = vcStrutResource.size();
        Resource[] arrDel = new Resource[count];

        try{
            User objUser = new User(userID);
            objUser.load();
            for(int i=0;i<count;i++){
                StructResource objSrt = (StructResource)vcStrutResource.get(i);
                objRes.setResourceID(objSrt.getResourceID());
                objRes.setTypeID(objSrt.getTypeID());
                objRes.loadExtResource();
                objRes.setRuler(objUser);

                arrDel[i] = objRes;
            }
            new Resource().doDeleteBatch(arrDel);
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager:deleteExtResourceBatch()"
            +" Exception while do deleteExtResourceBatch"+e.toString());
        }
    }

    /**
     * ɾ����Ȩ��ϵ��������û�����ô����û���
     * @param objStruct StructAuthAssign ��������Ķ���
     * �����������������
     * StructAuthAssign.userID ����Ȩ�û�ID
     * StructAuthAssign.authorityID Ȩ��ID
     * StructAuthAssign.providID ִ��ɾ���������û�ID��һ��Ϊ���뵱ǰϵͳ���û�ID��
     * @throws Exception ����������׳��쳣
     */
    public void deleteExtAuthAssign(StructAuthAssign objStruct) throws Exception{
        AuthorityAssign as = new AuthorityAssign();
        try{
            Authority au = new Authority(objStruct.getAuthorityID());
            if(objStruct.getAuthorityID()==0){
                throw new CesSystemException("AuthorityManager:deleteExtAuthAssign()"
                +" Illegal data values for delete");
            }else {
                au.load();
            }
            User op = new User(objStruct.getUserID());
            if(objStruct.getUserName()==null||objStruct.getUserName().equals("")){
                op.load();
            }else{
                op.setUserName(objStruct.getUserName());
            }

            User provider = new User(objStruct.getProviderID());
            if(objStruct.getProvidName()==null||objStruct.getProvidName().equals("")){
                provider.load();
            }else{
                provider.setUserName(objStruct.getProvidName());
            }


            as.setAuthority(au);
            as.setUser(op);
            as.setUserFigure(Figure.DEFAULTFIGURE);
            as.setProvider(provider);
            as.setProviderFigure(Figure.DEFAULTFIGURE);

            as.doDelete();
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager:deleteExtAuthAssign()"
            +" Exception while do deleteExtAuthAssign"+e.toString());
        }
    }

    /**
     * ����ɾ����Ȩ��ϵ��
     * @param vcStructAuthAssign Vector ��������Ľṹ�弯�������е�
     * Ԫ������ΪStructAuthAssign������ÿ��Ԫ��
     * �������ò�������Ȩ�û�ID��Ȩ��ID��������û�����ô����û���
     * @throws Exception ����������׳��쳣
     */
    public void deleteExtAuthAssignBatch(Vector vcStructAuthAssign) throws Exception{

        int count = vcStructAuthAssign.size();
        AuthorityAssign[] arrDel = new AuthorityAssign[count];
        StructAuthAssign objStruct = null;
        int i = 0;
        try{
            Enumeration enum = vcStructAuthAssign.elements();
            while(enum.hasMoreElements()){
                objStruct = (StructAuthAssign)enum.nextElement();
                Authority au = new Authority(objStruct.getAuthorityID());
                if (objStruct.getAuthorityID() == 0 ) {
                    throw new CesSystemException(
                        "AuthorityManager:deleteExtAuthAssignBatch()"
                        + " Illegal data values for delete");
                }
                else  {
                    au.load();
                }

                User op = new User(objStruct.getUserID());
                if (objStruct.getUserName() == null ||
                    objStruct.getUserName().equals("")) {
                    op.load();
                }
                else {
                    op.setUserName(objStruct.getUserName());
                }

                User provider = new User(objStruct.getProviderID());
                if (objStruct.getProvidName() == null ||
                    objStruct.getProvidName().equals("")) {
                    provider.load();
                }
                else {
                    provider.setUserName(objStruct.getProvidName());
                }
                AuthorityAssign as = new AuthorityAssign();
                as.setAuthority(au);
                as.setUser(op);
                as.setUserFigure(Figure.DEFAULTFIGURE);
                as.setProvider(provider);
				as.setRuler(provider);
                as.setProviderFigure(Figure.DEFAULTFIGURE);

                arrDel[i++] = as;
            }

            new AuthorityAssign().doDeleteBatch(arrDel);
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager:deleteExtAuthAssign()"
            +" Exception while do deleteExtAuthAssign"+e.toString());
        }
    }
    /**
     * ��ȡ�û��в���Ȩ�޵�ĳ����Դ
     * @param userId int
     * @param mark String
     * @param operateId String
     * @throws Exception
     * @return Vector
     */
    public Vector getResource(int userId,String mark,String operateId) throws Exception{
        Vector vResource=null;
        try{
            User user=new User();
            vResource=user.getUserRes(userId,mark,operateId);
            if(vResource==null){
                vResource=new Vector();
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        return vResource;
    }

    public static void main(String[] args){
        AuthorityManager am = new AuthorityManager();
//        Vector vc = new Vector();
//        //4
//        StructAuthAssign obj = new StructAuthAssign();
//        obj.setAuthorityID(3914);
//        obj.setUserID(1002);
//        obj.setProviderID(1102);
//        //5
//        StructAuthAssign obj1 = new StructAuthAssign();
//        obj1.setAuthorityID(3912);
//        obj1.setUserID(1302);
//        obj1.setProviderID(1102);
//
//        vc.add(obj);
//        vc.add(obj1);
        try{
//            am.deleteExtAuthAssignBatch(vc);
            Vector v=am.getResource(104,"system","1");
            for(int i=0;i<v.size();i++){
            String tmp=(String)v.get(i);
            //System.out.println("@@@@@@@@@@@"+tmp);
        }

        }catch(Exception e){
            e.printStackTrace();
        }

        //System.out.println("=============success=====");

    }

}

