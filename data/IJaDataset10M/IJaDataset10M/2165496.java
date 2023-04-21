package ces.platform.system.dbaccess;

import java.io.*;
import java.sql.*;
import java.util.*;

import ces.coral.dbo.*;
//import ces.coral.exception.*;
import ces.platform.system.common.*;
import ces.coral.log.*;
//import ces.platform.system.common.*;

/**
 * <p>标题:
 * <font class=titlefont>
 * 《运算条件和运算符号》类
 * </font>
 * <p>描述:
 * <font class=descriptionfont>
 * <br>从数据库中获取运算条件和运算符号，以便扩展。
 * </font>
 * <p>版本号:
 * <font class=versionfont>
 * Copyright (c) 2.50.2004.0317
 * </font>
 * <p>公司:
 * <font class=companyfont>
 * 上海中信信息发展有限公司
 * </font>
 * @author 饶国明
 * @version 2.50.2004.0317
 */

public class Condition extends OperationBase implements Serializable{

    static Logger logger=new Logger(Condition.class);

    /**
     * 算术运算操作，如加减乘除等
     */
    public static final String STATE_ARITH = "1";

    /**
     * 逻辑运算操作，如and，or等
     */
    public static final String STATE_LOGIC = "2";


    String strCode = "";
    String strName = "";
    String strSqlType = "";
    String strState = "";

    public Condition() {
    }

    public Condition(String code,String name,String sqlType,String state){
        this.strCode = code;
        this.strName = name;
        this.strSqlType = sqlType;
        this.strState = state;
    }

    public void setCode(String strCode){
        this.strCode = strCode;
    }

    public String getCode(){
        return this.strCode;
    }

    public void setName(String strName){
        this.strName = strName;
    }

    public String getName(){
        return this.strName;
    }

    public void setSqlType(String strSqlType){
        this.strSqlType = strSqlType;
    }

    public String getSqlType(){
        return this.strSqlType;
    }

    public void setState(String strState){
        this.strState = strState;
    }

    public String getState(){
        return this.strState;
    }

    public Vector getAllData() throws Exception{
        Vector allDatas = new Vector();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        String strQuery ="select code,name,sql_type,state from "+Common.FIELD_CONDITION_TABLE;
        DBOperation dbo=factory.createDBOperation(POOL_NAME);
        try {
            con = dbo.getConnection();
            ps  = con.prepareStatement(strQuery);
           result =  ps.executeQuery();
            while (result.next()) {

                String strCode = result.getString("code");
                String strName = result.getString("name");
                String strSqlType = result.getString("sql_type");
                String strState = result.getString("state");
                Condition cd = new Condition(strCode,strName,strSqlType,
                                    strState);
                allDatas.addElement(cd);
            }
        } catch (SQLException se) {
            throw new CesSystemException("Condition.getAllData(): SQLException:  " + se);
        } finally {
            close(dbo,ps,result);
        }
        return allDatas;
    }

    public Vector getLogicConditon() throws Exception{
        Vector vcRet = new Vector();

        try{
            Enumeration enum = getAllData().elements();
            while(enum.hasMoreElements()){
                Condition cond = (Condition)enum.nextElement();
                if(cond.getState().equals(Condition.STATE_LOGIC)){
                    vcRet.addElement(cond);
                }
            }
        }catch (Exception se) {
            throw new CesSystemException("Condition.getLogicConditon(): Exception:  " + se);
        }

        return vcRet;
    }

    public Vector getArithConditon() throws Exception{
        Vector vcRet = new Vector();

        try{
            Enumeration enum = getAllData().elements();
            while(enum.hasMoreElements()){
                Condition cond = (Condition)enum.nextElement();
                if(cond.getState().equals(Condition.STATE_ARITH)){
                    vcRet.addElement(cond);
                }
            }
        }catch (Exception se) {
            throw new CesSystemException("Condition.getArithConditon(): Exception:  " + se);
        }

        return vcRet;
    }

	protected void doAddNew(Connection con) throws Exception {}
	
	protected void doSelfDelete(Connection con) throws Exception {} 
    /**
     * 重载基类的抽象方法
     * @param con
     */
    public void doNew(Connection con){

    }
    public void doDelete(Connection con){

    }
    public void doUpdate(Connection con){

    }
    public boolean isExist(){

        return false;

    }
    public boolean isExist(Connection con){
        return false;
    }
}