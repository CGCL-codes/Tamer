package cn.ekuma.epos.datalogic.define.dao.erp;

import cn.ekuma.data.dao.ViewDAO;
import com.openbravo.bean.erp.viewbean.TimeStore_V;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.Session;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TimeStore_VDAO extends ViewDAO<TimeStore_V> {

    public TimeStore_VDAO(I_Session s) {
        super(s);
    }

    public TimeStore_V readValues(DataRead dr, TimeStore_V t) throws BasicException {
        if (t == null) t = new TimeStore_V();
        t.ID = dr.getString(1);
        t.name = dr.getString(2);
        t.createDate = dr.getTimestamp(3);
        t.location = dr.getString(4);
        t.productCat = dr.getString(5);
        t.isBuiled = dr.getBoolean(6);
        return t;
    }

    public List listBuildedTimeStore() throws BasicException {
        return new PreparedSentence(s, "SELECT T.ID, T.NAME, T.CURDATE, S.NAME, C.NAME, T.BUILDED FROM TIMESTORES T  LEFT OUTER JOIN LOCATIONS S ON S.ID = T.LOCATIONREF  LEFT OUTER JOIN CATEGORIES C ON  T.CATEGORYREF=C.ID WHERE T.BUILDED=" + s.getDB().TRUE(), null, this).list();
    }

    @Override
    public Class getSuportClass() {
        return TimeStore_V.class;
    }
}
