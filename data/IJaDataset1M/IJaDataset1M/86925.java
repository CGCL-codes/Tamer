package cn.ekuma.epos.datalogic.define.dao;

import cn.ekuma.data.dao.ModifiedLogDAO;
import com.openbravo.bean.Tax;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;
import com.openbravo.bean.TaxLine;

/**
 *
 * @author Administrator
 */
public class TaxLineDAO extends ModifiedLogDAO<TaxLine> {

    public TaxLineDAO(I_Session s) {
        super(s);
    }

    @Override
    public TableDefinition getTable() {
        return new TableDefinition(s, "TAXLINES", new Field[] { new Field("ID", Datas.STRING, Formats.STRING), new Field("RECEIPT", Datas.STRING, Formats.STRING), new Field("TAXID", Datas.STRING, Formats.STRING, Tax.class), new Field("BASE", Datas.DOUBLE, Formats.DOUBLE), new Field("AMOUNT", Datas.DOUBLE, Formats.DOUBLE), new Field("LASTMODIFIED", Datas.TIMESTAMP, Formats.TIMESTAMP) }, new int[] { 0 });
    }

    @Override
    public void writeInsertValues(DataWrite dp, TaxLine obj) throws BasicException {
        dp.setString(1, obj.getId());
        dp.setString(2, obj.getReceiptID());
        dp.setString(3, obj.getTaxID());
        dp.setDouble(4, obj.getBase());
        dp.setDouble(5, obj.getAmount());
        dp.setTimestamp(6, obj.getLastModified());
    }

    @Override
    public Class getSuportClass() {
        return TaxLine.class;
    }

    public TaxLine readValues(DataRead dr, TaxLine obj) throws BasicException {
        if (obj == null) obj = new TaxLine();
        obj.setId(dr.getString(1));
        obj.setReceiptID(dr.getString(2));
        obj.setTaxID(dr.getString(3));
        obj.setBase(dr.getDouble(4));
        obj.setAmount(dr.getDouble(5));
        obj.setLastModified(dr.getTimestamp(6));
        return obj;
    }
}
