package com.manydesigns.portofino.rad.calculations;

import com.manydesigns.portofino.base.*;
import com.manydesigns.portofino.base.calculations.*;
import com.manydesigns.portofino.util.Defs;
import com.manydesigns.portofino.util.Util;
import com.manydesigns.xmlbuffer.XhtmlBuffer;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class RadCalculations {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    public static boolean setupFormula(MDConfig config, String formula, int aid, boolean constraint, String constraintError) throws Exception {
        MDClass attrCls = config.getMDClassByName(Defs.ATTR_CLS);
        MDObject attrObj = attrCls.getMDObject(aid);
        return setupFormula(formula, attrObj, constraint, constraintError);
    }

    public static boolean setupFormula(String formula, MDObject attrObj, boolean constraint, String constraintError) throws Exception {
        MDConfig config = attrObj.getConfig();
        Transaction tx = config.getCurrentTransaction();
        boolean success = true;
        MDObject clsObj = attrObj.getRelAttributeObject("class");
        MDClass acCls = config.getMDClassByName(Defs.ATTRCALC_CLS);
        String query = MessageFormat.format("SELECT ac.\"id\" AS \"ac_id\"" + " FROM \"{0}\".\"{1}\" ac" + " WHERE ac.\"attribute\" = ?" + " AND ac.\"class\" = ?", config.getSchema1(), Defs.ATTRCALC_CLS);
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = tx.prepareStatement(query);
            st.clearParameters();
            st.setInt(1, attrObj.getId());
            st.setInt(2, clsObj.getId());
            rs = st.executeQuery();
            while (rs.next()) {
                int acId = rs.getInt("ac_id");
                List<MDObject> objs = new ArrayList<MDObject>();
                MDObject acObj = acCls.getMDObject(acId);
                objs.add(acObj);
                MDObject calcObj = acObj.getRelAttributeObject("calculation");
                objs.add(calcObj);
                Hashtable<MDObject, MDRelAttribute> relAttrForUpdate = new Hashtable<MDObject, MDRelAttribute>();
                objs = config.propagateForDelete(objs, relAttrForUpdate);
                config.delete(objs, relAttrForUpdate);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                st.close();
            } catch (Exception e) {
            }
        }
        if (formula != null) {
            FormulaEditorLexer l = new FormulaEditorLexer(new StringReader(formula));
            FormulaEditorParser p = new FormulaEditorParser(l);
            XhtmlBuffer xb = new XhtmlBuffer();
            Hashtable map = CalculatedAttributes.ProduceAttributeCodeList(attrObj, clsObj, xb, false);
            FormulaEditorVisitor visitor = new FormulaEditorCreateVisitor(config, map);
            p.setVisitor(visitor);
            MDObject result;
            try {
                result = (MDObject) p.parse().value;
                if (visitor.anyErrors()) throw new Exception();
                MDObject acom = acCls.createNewMDObject();
                acom.setBooleanAttribute(Defs.ATTRCALC_CONSTRAINT, constraint);
                acom.setTextAttribute(Defs.ATTRCALC_CONSTRAINT_DESCR, constraintError);
                acom.setRelAttributeObject("attribute", attrObj);
                acom.setRelAttributeObject("class", clsObj);
                acom.setRelAttributeObject("calculation", result);
                acom.markForCreation();
                tx.sync();
            } catch (Exception e) {
                tx.rollback();
                StringBuffer errmsg = new StringBuffer();
                errmsg.append(Util.getLocalizedString(Defs.MDPORTI18N, config.getLocale(), "Error_in_formula_definition"));
                errmsg.append(formula);
                errmsg.append("\n");
                for (int i = 0; i < visitor.getErrorColumn(); i++) {
                    errmsg.append(" ");
                }
                errmsg.append("^\n");
                errmsg.append(e.getMessage());
                throw new Exception(errmsg.toString(), e);
            }
            MDConfig downConfig = config.getConfigContainer().getAppConfig();
            MDAttribute attr = downConfig.getAttributeById(attrObj.getId());
            tx.recalculateAttribute(attr);
        }
        return success;
    }
}
