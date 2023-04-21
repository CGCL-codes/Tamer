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
 * <p>标题:
 * <font class=titlefont>
 * 《权限管理》类
 * </font>
 * <p>描述:
 * <font class=descriptionfont>
 * <br>权限定义、按用户授权等
 * </font>
 * <p>版本号:
 * <font class=versionfont>
 * Copyright (c) 2.50.2003.1223
 * </font>
 * <p>公司:
 * <font class=companyfont>
 * 上海中信信息发展有限公司
 * </font>
 * @author 饶国明
 * @version 2.50.2003.1223
 */

public class AuthorityManager {

    static Logger logger=new Logger(AuthorityManager.class);

    static final int EXTERNAL_PAREANT = -1;
    public AuthorityManager(){}

    /**
     * 系统权限定义方法，这些权限可由系统管理平台来进行管理
     * @param String resId 资源ID
     * @param String resName 资源名称
     * @param int resTypeID 资源类型
     * @param int operateTypeID 操作类型ID
     * @param String remark 资源说明
     * @throws Exception 如果创建权限出错，则抛出异常
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
     * 外部资源定义方法，这些资源只存储在系统管理平台而不对他进行管理,
     * 这些资源不能生成树型结构
     * @param String resId 资源ID
     * @param String resName 资源名称
     * @param int resTypeID 资源类型
     * @param int operateTypeID 操作类型ID
     * @param String remark 资源说明
     * @throws Exception 如果创建权限出错，则抛出异常
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
            objTemp.setPathName(""); //将路径名置为空
            objTemp.setLevelNum(0);  //将层次数置为0
            objTemp.setRemark(remark);
            objTemp.setResourceID(strPath.substring(strPath.lastIndexOf(Constant.RESOURCE_SEPARATOR_STORE)+1));

            objTemp.doNew();
        }catch(Exception e){
            throw new CesSystemException("AuthorityManager::createExtResource()"
                    +" Exception while do createExtResource"+e.toString());
        }
    }

    /**
     * 判断系统中是否存在所需要的系统资源
     * @param strRes String 外部提供的资源标识号
     * @param intResTypeID int 资源类型
     * @throws Exception 如果出错，则抛出异常
     * @return boolean 如果存在，则返回True，否则返回false
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
      * 获取序列号
      * @param strSname  数据库中序列号名称
      * @return          返回序列号
      *
     */
//    public long getSequenceId(String strName) throws Exception{
//        return IdGenerator.getInstance().getId(strName);
//    }

    /**
     * 获取外部权限，这些权限的参数以StructAuth的结构返回
     * @param objSrt StructResource 资源信息结构,必须设置以下参数：
     * StructResource.resourceID 资源标识
     * StructResource.type_id 资源类型
     * StructResource.operateTypeID 对资源的操作类型
     * StructResource.operateID 对资源的操作
     * @return 获得的权限信息封装在StructAuth结构体中
     */
    public StructAuth getExternalAuthority(StructResource objSrt) throws Exception{
        StructAuth objSt = new StructAuth();
        try{
            //获得资源对象
            Resource objRes = new Resource();
            objRes.setResourceID(objSrt.getResourceID());
            objRes.setTypeID(objSrt.getTypeID());
            objRes.loadExtResource();
            //获得权限对象
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
     * 获取外部权限，这些权限的参数以StructAuthAssign的结构返回
     * @param resourceID 资源标识
     * @param typeID 资源类型号
     * @return 获得与资源resId相关的授权信息列表，列表中的元素是StructAuthAssign对象。
     */
    public Vector getExtAuthAssign(String resourceID,int typeID) throws Exception{
        StructAuthAssign objAs = null;
        Vector vcRet = new Vector();
        AuthorityAssign sysAs = null;
        try{
            //获得资源对象
            Resource objRes = new Resource();
            objRes.setResourceID(resourceID);
            objRes.setTypeID(typeID);
            objRes.loadExtResource();
            //获得与该资源权限
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
     * 获取某用户对某资源拥有的外部权限，这些权限的参数以StructAuthAssign的结构返回
     * @param resourceID 资源标识
     * @param typeID 资源类型号
     * @param userID 用户ID
     * @return 获得与资源resId相关的授权信息列表，列表中的元素是StructAuthAssign对象。
     */
    public Vector getExtAuthAssignsOfUser(String resourceID,int typeID,int userID) throws Exception{
        StructAuthAssign objAs = null;
        Vector vcRet = new Vector();
        AuthorityAssign sysAs = null;
        try{
            //获得资源对象
            Resource objRes = new Resource();
            objRes.setResourceID(resourceID);
            objRes.setTypeID(typeID);
            objRes.loadExtResource();
            //获得与该资源权限
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
     * 将只存储在系统管理平台而不受系统管理平台控制的权限授给用户。
     * @param intUserId int 被授权用户的ID
     * @param intAuthId int 授给用户的权限ID
     * @param intProvider int 授权者的用户ID
     * @throws Exception 如果出错，则抛出异常。
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
     * 将只存储在系统管理平台而不受系统管理平台控制的权限授给用户。
     * @param vcStructAuthAssign 授权参数的集合。
     * @param StructAuthAssign.userID int 被授权用户的ID
     * @param StructAuthAssign.authorityID int 授给用户的权限ID
     * @param StructAuthAssign.providID int 授权者的用户ID
     * @throws Exception 如果出错，则抛出异常。
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
     * 获取给定用户属于给定表的sql-where子句字符串
     * @param userID 用户ID
     * @param strTable 数据表名，如果有多个表，则使用逗号隔开
     * @return 如果有相应的Where条件语句，则返回Where条件子句，否则返回null.
     * @throws Exception 如果出错，则抛出异常
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
     * 获取给定用户属于给定表的sql-where子句字符串,<br>
     * 并将sql-where子句字符串中的参数使用用户提供的键值对进行处理。<br>
     * 用户键值对（Hashtable中的值）形式为：fieldName,value.<br>
     * 如：secret_level,3(表示密级，3级)
     * @param userID int 用户ID
     * @param strTable String 数据表名，如果有多个表，则使用逗号隔开
     * @param hashTable Hashtable 存储键值对（字段名:值）
     * @return String 如果有相应的Where条件语句，则返回Where条件子句，否则返回null.
     * @throws Exception 如果出错，则抛出异常
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
     * 删除资源对象，并删除与该资源相关的权限及授权关系。
     * @param resourceID int 资源标识
     * @param typeID int 资源类型标识
     * @param userID int 执行删除操作的用户ID，一般为进入当前系统的用户ID
     * @throws Exception 如果出错，则抛出异常。
     */
    public void deleteExtResource(String resourceID,int typeID,int userID) throws Exception{
        Resource objRes = new Resource();
        try{
            User objUser = new User(userID);
            objUser.load();
            //获得资源对象

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
     * 批量删除资源权限，并删除与这些资源相关的授权关系。
     * @param vcStrutResource Vector 资源信息列表，其元素为StrutResource类型对象。<br>
     * 在该结构体中的元素必须设置：<br>
     * resourceID int 资源标识 <br>
     * typeID int 资源类型标识 <br>
     * @param userID int 执行删除操作的用户ID，一般为进入当前系统的用户ID
     * @throws Exception 如果出错，则抛出异常。
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
     * 删除授权关系，如果有用户名最好传入用户名
     * @param objStruct StructAuthAssign 传入参数的对象。
     * 必须设置下列数据项：
     * StructAuthAssign.userID 被授权用户ID
     * StructAuthAssign.authorityID 权限ID
     * StructAuthAssign.providID 执行删除操作的用户ID，一般为进入当前系统的用户ID。
     * @throws Exception 如果出错，则抛出异常
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
     * 批量删除授权关系。
     * @param vcStructAuthAssign Vector 传入参数的结构体集，向量中的
     * 元素类型为StructAuthAssign，并且每个元素
     * 必须设置参数被授权用户ID，权限ID，如果有用户名最好传入用户名
     * @throws Exception 如果出错，则抛出异常
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
     * 获取用户有操作权限的某类资源
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

