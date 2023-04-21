package cn.myapps.core.expimp.exp.ejb;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import cn.myapps.core.expimp.exp.util.SQLPackage;

public class ExpPage extends ExpElement {

    private static final String NAME = "T_PAGE";

    public ExpPage(boolean expAll) {
        super(expAll);
    }

    public String getName() {
        return NAME;
    }

    public Map<String, SQLPackage> exportSQLS(String[] ids, Collection ignoreIds) throws Exception {
        Map rtn = new LinkedHashMap();
        StringBuffer query = new StringBuffer();
        Map formSQLs = new ExpForm(isExpAll()).exportSQLS(ids, ignoreIds);
        rtn.putAll(formSQLs);
        for (int i = 0; i < ids.length; i++) {
            query.append("SELECT * FROM ");
            query.append(getName() + " ");
            query.append("WHERE id ='");
            query.append(ids[i] + "'");
            SQLPackage values = new SQLPackage(getName(), query.toString());
            rtn.put("page_" + ids[i], values);
            query = new StringBuffer();
        }
        return rtn;
    }
}
