package cn.myapps.core.expimp.imp.ejb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.map.LinkedMap;

public class ImpView extends ImpElement {

    public ImpView(String appId, String modId) {
        super(appId, modId);
    }

    public static final String NAME = "T_VIEW";

    public static final String DISPLAY_NAME = "view";

    public static final String FOREINGKEY_COLUMN_STYLE = "STYLE";

    public static final String FOREINGKEY_COLUMN_FORM_ID = "FORM_ID";

    public Map getImportDatas(Collection dynaBeans, String[] ids) throws Exception {
        Map rtn = new LinkedMap();
        Collection beans = getBeansOfTable(dynaBeans, NAME);
        for (Iterator iter = beans.iterator(); iter.hasNext(); ) {
            DynaBean bean = (DynaBean) iter.next();
            String id = (String) bean.get(IMPOBJECT_ID);
            for (int i = 0; i < ids.length; i++) {
                if (id.equals(ids[i])) {
                    String styleId = (String) bean.get(FOREINGKEY_COLUMN_STYLE);
                    Map impStyle = new ImpStyleRepository(applicationid, moduleid).getImportDatas(dynaBeans, new String[] { styleId });
                    rtn.putAll(impStyle);
                    String formId = (String) bean.get(FOREINGKEY_COLUMN_FORM_ID);
                    Map impForm = new ImpForm(applicationid, moduleid).getImportDatas(dynaBeans, new String[] { formId });
                    rtn.putAll(impForm);
                    setForeignKeyValue(bean);
                    rtn.put(id, bean);
                    String[] colIds = this.getIdsByForeignKeyValue(dynaBeans, ImpColumn.FOREINGKEY_COLUMN_VIEW_ID, id);
                    Map colActs = new ImpColumn(applicationid, moduleid).getImportDatas(dynaBeans, colIds);
                    rtn.putAll(colActs);
                    String[] actIds0 = this.getIdsByForeignKeyValue(dynaBeans, ImpActivity.FOREINGKEY_COLUMN_VIEW_ID, id);
                    Map impActs0 = new ImpActivity(applicationid, moduleid).getImportDatas(dynaBeans, actIds0, id);
                    rtn.putAll(impActs0);
                }
            }
        }
        return rtn;
    }

    public String getTableName() {
        return NAME;
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public void setForeignKeyValue(DynaBean bean) {
        resetPrimaryKey(bean);
        resetForeignKey(bean, FOREINGKEY_COLUMN_STYLE);
        resetForeignKey(bean, FOREINGKEY_COLUMN_FORM_ID);
        bean.set(FOREIGNKEY_COLUMN_MODULE, moduleid);
        bean.set(FOREIGNKEY_COLUMN_APPLICATION, applicationid);
    }
}
