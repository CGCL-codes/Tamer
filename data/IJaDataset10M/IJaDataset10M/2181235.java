package cn.ekuma.epos.datalogic.define.dao;

import cn.ekuma.data.dao.BaseDAO;
import com.openbravo.bean.Product;
import com.openbravo.bean.ProductStockLevel;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.KeyBuilder;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;
import com.openbravo.pos.bean.ProductExt;

public class ProductStockLevelDAO extends BaseDAO<ProductStockLevel> {

    LocationDAO locationDAO;

    public ProductStockLevelDAO(I_Session s, LocationDAO locationDAO) {
        super(s);
        this.locationDAO = locationDAO;
    }

    public ProductStockLevelDAO(I_Session s) {
        super(s);
    }

    @Override
    public TableDefinition getTable() {
        return new TableDefinition(s, "STOCKLEVEL", new Field[] { new Field("ID", Datas.STRING, Formats.STRING), new Field("LOCATION", Datas.STRING, Formats.STRING), new Field("PRODUCT", Datas.STRING, Formats.STRING, Product.class), new Field("STOCKSECURITY", Datas.DOUBLE, Formats.DOUBLE), new Field("STOCKMAXIMUM", Datas.DOUBLE, Formats.DOUBLE) }, new int[] { 0 });
    }

    @Override
    public ProductStockLevel readValues(DataRead dr, ProductStockLevel obj) throws BasicException {
        if (obj == null) obj = new ProductStockLevel();
        obj.setId(dr.getString(1));
        obj.setLocationId(dr.getString(2));
        obj.setProductId(dr.getString(3));
        obj.setStockSecurity(dr.getDouble(4));
        obj.setStockMax(dr.getDouble(5));
        if (locationDAO != null) obj.setLocation(locationDAO.find(KeyBuilder.getKey(dr.getString(2))));
        return obj;
    }

    @Override
    public void writeInsertValues(DataWrite dp, ProductStockLevel obj) throws BasicException {
        dp.setString(1, obj.getId());
        dp.setString(2, obj.getLocationId());
        dp.setString(3, obj.getProductId());
        dp.setDouble(4, obj.getStockSecurity());
        dp.setDouble(5, obj.getStockMax());
    }

    @Override
    public Class getSuportClass() {
        return ProductStockLevel.class;
    }

    @Override
    public Class transClass(Class in) {
        if (in == ProductExt.class) return Product.class;
        return super.transClass(in);
    }
}
