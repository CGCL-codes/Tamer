package com.zzsoft.wbi.view.page.list.dhtmlxgrid;

import java.io.StringReader;
import java.util.List;
import util.ajax.ResponseWriter;
import framework.zze2p.C;
import framework.zze2p.action.Base1Action;
import framework.zze2p.db.information.DB_ADM;
import framework.zze2p.db.information.cascade.Pojo_Tab_C_I;
import framework.zze2p.mod.applymod.json.Json2ObjC;
import framework.zze2p.mod.applymod.json.JsonArrayDOM;
import framework.zze2p.mod.pojo4.Pojo_4HO;
import framework.zze2p.mod.pojo4.Pojo_4I;
import framework.zze2p.mod.pojodb.PojoDB;
import framework.zze2p.mod.pojodb.PojoDB_I;

public class SelectPageAction extends Base1Action {

    protected static BackGroundCondition backGroundCondition;

    protected static String DefIndexColNameFormat = BackGroundCondition.DefIndexColNameFormat;

    public void init() {
        this.setPojoSession();
        this.setMAttributeByForm();
        this.setPojoByMAttribute(PojoDB.class);
    }

    public void findById() {
        init();
        List ld = SelectPageBoC.getByIdFromDB_Config(pojoSession, (Pojo_4I) pojo);
        try {
            pojo.replaceEum$ValueOrNote();
            String json = Json2ObjC.Obj2Json(ld).toString();
            System.out.println(json);
            ResponseWriter.writerString(json);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void findFromDBandXML_Config() {
        init();
        List ld = SelectPageBoC.getByIdFromDBandXML_Config(pojoSession, (Pojo_4I) pojo);
        try {
            Pojo_4I pojoQuery = SelectPageBoC.toPagePojo_New((Pojo_4I) pojo, ld);
            String json = Json2ObjC.Obj2Json(pojoQuery).toString();
            System.out.println(json);
            ResponseWriter.writerString(json);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void updateOrSave_Config() {
        this.getForm(Pojo_4HO.class);
        pojo.replaceEum$ValueOrNote();
        String json = pojo.getString(C.action.json.JsonString_ColName);
        new JsonArrayDOM();
        Pojo_4I pojoDB = (Pojo_4I) Json2ObjC.json2Obj(json);
        Pojo_Tab_C_I pTab = (Pojo_Tab_C_I) DB_ADM.getTabByFullName(C_QueueList.$Full_TabName);
        SelectPageBoC.updateOrSave_Config(this.pojoSession, pTab, pojoDB);
    }

    public void findAllFromXML_Config() {
        this.getForm(Pojo_4HO.class);
        pojo.replaceEum$ValueOrNote();
        Pojo_Tab_C_I pTabInto = (Pojo_Tab_C_I) DB_ADM.getTabByFullName(pojo.getString(C.Pojo.Pojo4.Tab_Full_Name));
        List ld = SelectPageBoC.getAllFromXML_Config(null, pTabInto, 0, null, 0);
        String json = Json2ObjC.Obj2Json(ld).toString();
        System.out.println(json);
        ResponseWriter.writerString(json);
    }

    public void getByCondition_Rec() throws Exception {
        init();
        pojo.replaceEum$ValueOrNote();
        String json = pojo.getString(C.action.json.JsonString_ColName);
        System.out.println(json);
        PojoDB_I pojoDo = (PojoDB_I) Json2ObjC.json2Obj(json, PojoDB.class.getName());
        ((PojoDB) pojoDo).setEnumState$Note();
        if (backGroundCondition != null) backGroundCondition.buildBGConditionByKeyValue(DefIndexColNameFormat, this.pojoSession, pojoDo);
        String str = SelectPageBoC.getByConditionToJson_Rec(pojoDo);
        System.out.println(str);
        ResponseWriter.writerString(str);
    }

    public static void main(String[] args) throws Exception {
        String s = "{'$$Pojo_Tab_DEF_Name$$':'zztpl-tpl-CUSTOMER', '$$DoColNames$$':'TPL_CUSTOMER_CITY,TPL_CUSTOMER_COMPANY_NAME,TPL_CUSTOMER_COMPANY_ADDRESS,TPL_CUSTOMER_POSTCODE,TPL_CUSTOMER_COMPANY_TELPHONE,TPL_CUSTOMER_COMPANY_CONECT_NAME,TPL_CUSTOMER_COMPANY_DESC'}";
        StringReader j = new StringReader(s);
        String json = j.toString();
        PojoDB_I pojo = (PojoDB_I) Json2ObjC.json2Obj(s, PojoDB.class.getName());
        System.out.println(" >>> " + Json2ObjC.pojo2JsonString(pojo));
        String str = SelectPageBoC.getByConditionToJson_Rec(pojo);
        System.out.println(str);
    }

    public void getByConditionBG_Rec() throws Exception {
        init();
        String json = pojo.getString(C.action.json.JsonString_ColName);
        System.out.println(json);
        PojoDB_I pojo = (PojoDB_I) Json2ObjC.json2Obj(json);
        if (backGroundCondition != null) backGroundCondition.buildBGConditionByKeyValue(DefIndexColNameFormat, this.pojoSession, pojo);
        String str = SelectPageBoC.getByConditionToJson_Rec(pojo);
        System.out.println(str);
        ResponseWriter.writerString(str);
    }

    public static BackGroundCondition getBackGroundCondition() {
        return backGroundCondition;
    }

    public static void setBackGroundCondition(BackGroundCondition backGroundCondition) {
        SelectPageAction.backGroundCondition = backGroundCondition;
    }

    public static String getDefIndexColNameFormat() {
        return DefIndexColNameFormat;
    }

    public static void setDefIndexColNameFormat(String defIndexName) {
        DefIndexColNameFormat = defIndexName;
    }
}
