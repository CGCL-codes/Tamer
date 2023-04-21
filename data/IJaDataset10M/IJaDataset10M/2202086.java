package cn.ekuma.epos.datalogic.define.dao.erp;

import cn.ekuma.data.dao.ModifiedLogDAO;
import cn.ekuma.epos.db.table.erp.I_OrderLine;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.bean.Tax;
import com.openbravo.bean.erp.OrderLine;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;
import java.util.HashMap;

/**
 *
 * @author Administrator
 */
public class OrderLineDAO extends ModifiedLogDAO<OrderLine> {

    public OrderLineDAO(I_Session s) {
        super(s);
    }

    @Override
    public TableDefinition getTable() {
        return new TableDefinition(s, "ORDERLINES", new Field[] { new Field(I_OrderLine.ID, Datas.STRING, Formats.STRING), new Field(I_OrderLine.ORDERRID, Datas.STRING, Formats.STRING), new Field(I_OrderLine.LINE, Datas.INT, Formats.INT), new Field(I_OrderLine.PRODUCT, Datas.STRING, Formats.STRING), new Field(I_OrderLine.ATTRIBUTESETINSTANCE_ID, Datas.STRING, Formats.STRING), new Field(I_OrderLine.UNITS, Datas.DOUBLE, Formats.DOUBLE), new Field(I_OrderLine.PRICE, Datas.DOUBLE, Formats.DOUBLE), new Field(I_OrderLine.TAXID, Datas.STRING, Formats.STRING), new Field(I_OrderLine.DEFAULTPRICE, Datas.DOUBLE, Formats.DOUBLE), new Field(I_OrderLine.PRICERATE, Datas.DOUBLE, Formats.DOUBLE), new Field(I_OrderLine.ISPREFSELL, Datas.BOOLEAN, Formats.BOOLEAN), new Field(I_OrderLine.CONSULUNIT, Datas.DOUBLE, Formats.DOUBLE), new Field(I_OrderLine.ATTRIBUTES, Datas.BYTES, Formats.NULL), new Field(I_OrderLine.LASTMODIFIED, Datas.TIMESTAMP, Formats.TIMESTAMP), new Field(I_OrderLine.CONSULTPRICE, Datas.DOUBLE, Formats.DOUBLE), new Field(I_OrderLine.LOTNO, Datas.STRING, Formats.STRING), new Field(I_OrderLine.VALIDITYDATE, Datas.TIMESTAMP, Formats.TIMESTAMP) }, new int[] { 0 });
    }

    @Override
    public void writeInsertValues(DataWrite dp, OrderLine obj) throws BasicException {
        dp.setString(1, obj.getKey());
        dp.setString(2, obj.m_sOrder);
        dp.setInt(3, new Integer(obj.m_iLine));
        dp.setString(4, obj.productid);
        dp.setString(5, obj.attsetinstid);
        dp.setDouble(6, obj.multiply);
        dp.setDouble(7, obj.price);
        dp.setString(8, obj.tax.getId());
        dp.setDouble(9, obj.defaultPrice);
        dp.setDouble(10, obj.priceRate);
        dp.setBoolean(11, obj.isPrefSell);
        dp.setDouble(12, obj.consulMultiply);
        dp.setBytes(13, ImageUtils.writeSerializable(obj.attributes));
        dp.setTimestamp(14, obj.getLastModified());
        dp.setDouble(15, obj.getConsultPrice());
        dp.setString(16, obj.getLotNo());
        dp.setTimestamp(17, obj.getValidityDate());
    }

    public OrderLine readValues(DataRead dr, OrderLine p) throws BasicException {
        if (p == null) p = new OrderLine();
        p.id = dr.getString(1);
        p.m_sOrder = dr.getString(2);
        p.m_iLine = dr.getInt(3).intValue();
        p.productid = dr.getString(4);
        p.attsetinstid = dr.getString(5);
        p.multiply = dr.getDouble(6);
        p.price = dr.getDouble(7);
        p.tax = new Tax(dr.getString(8), dr.getString(9), dr.getString(10), dr.getString(11), dr.getString(12), dr.getDouble(13), dr.getBoolean(14), dr.getInt(15));
        p.attributes = (HashMap<String, String>) ImageUtils.readSerializable(dr.getBytes(16));
        p.defaultPrice = dr.getDouble(17);
        p.priceRate = dr.getDouble(18);
        p.isPrefSell = dr.getBoolean(19);
        p.consulMultiply = dr.getDouble(20);
        p.setConsultPrice(dr.getDouble(21));
        return p;
    }

    @Override
    public Class getSuportClass() {
        return OrderLine.class;
    }
}
