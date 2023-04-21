package cn.ekuma.epos.datalogic.define.dao;

import cn.ekuma.data.dao.ModifiedLogDAO;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.bean.TaxCategory;
import com.openbravo.data.model.Field;

/**
 *
 * @author Administrator
 */
public class TaxCategoryDAO extends ModifiedLogDAO<TaxCategory> {

    public TaxCategoryDAO(I_Session s) {
        super(s);
    }

    @Override
    public TableDefinition getTable() {
        return new TableDefinition(s, "TAXCATEGORIES", new Field[] { new Field("ID", Datas.STRING, Formats.STRING), new Field("NAME", Datas.STRING, Formats.STRING), new Field("LASTMODIFIED", Datas.TIMESTAMP, Formats.TIMESTAMP) }, new int[] { 0 });
    }

    @Override
    public void writeInsertValues(DataWrite dp, TaxCategory obj) throws BasicException {
        dp.setString(1, obj.getID());
        dp.setString(2, obj.getName());
        dp.setTimestamp(3, obj.getLastModified());
    }

    public TaxCategory readValues(DataRead dr, TaxCategory obj) throws BasicException {
        if (obj == null) obj = new TaxCategory();
        obj.setID(dr.getString(1));
        obj.setName(dr.getString(2));
        obj.setLastModified(dr.getTimestamp(3));
        return obj;
    }

    @Override
    public Class getSuportClass() {
        return TaxCategory.class;
    }
}
