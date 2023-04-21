package cn.ekuma.epos.datalogic.define.dao.crm.contract;

import cn.ekuma.data.dao.ModifiedLogDAO;
import cn.ekuma.epos.datalogic.define.dao.shard.StateObjectDAOHelper;
import cn.ekuma.epos.db.table.crm.contract.I_Contract;
import com.openbravo.bean.Customer;
import com.openbravo.bean.crm.contract.Contract;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;

public class ContractDAO extends ModifiedLogDAO<Contract> {

    public ContractDAO(I_Session s) {
        super(s);
    }

    @Override
    public TableDefinition getTable() {
        TableDefinition table = StateObjectDAOHelper.getTable(s, "CONTRACT");
        table.addField(new Field(I_Contract.NUMID, Datas.STRING, Formats.STRING));
        table.addField(new Field(I_Contract.CUSTOMERID, Datas.STRING, Formats.STRING, Customer.class));
        table.addField(new Field(I_Contract.EFFECTIVEDATE, Datas.TIMESTAMP, Formats.TIMESTAMP));
        table.addField(new Field(I_Contract.AMOUNT, Datas.DOUBLE, Formats.DOUBLE));
        table.addField(new Field(I_Contract.CONTRACTTYPE, Datas.STRING, Formats.STRING));
        return table;
    }

    @Override
    public void writeInsertValues(DataWrite dp, Contract obj) throws BasicException {
        StateObjectDAOHelper.writeInsertValues(dp, obj, 1);
        dp.setString(I_Contract.COLUMN_COUNT + 1, obj.getNumID());
        dp.setString(I_Contract.COLUMN_COUNT + 2, obj.getCustomerId());
        dp.setTimestamp(I_Contract.COLUMN_COUNT + 3, obj.getEffectiveDate());
        dp.setDouble(I_Contract.COLUMN_COUNT + 4, obj.getAmount());
        dp.setString(I_Contract.COLUMN_COUNT + 5, obj.getContractType());
    }

    @Override
    public Class getSuportClass() {
        return Contract.class;
    }

    @Override
    public Contract readValues(DataRead dr, Contract obj) throws BasicException {
        if (obj == null) obj = new Contract();
        StateObjectDAOHelper.readValues(dr, obj, 1);
        obj.setNumID(dr.getString(I_Contract.COLUMN_COUNT + 1));
        obj.setCustomerId(dr.getString(I_Contract.COLUMN_COUNT + 2));
        obj.setEffectiveDate(dr.getTimestamp(I_Contract.COLUMN_COUNT + 3));
        obj.setAmount(dr.getDouble(I_Contract.COLUMN_COUNT + 4));
        obj.setContractType(dr.getString(I_Contract.COLUMN_COUNT + 5));
        return obj;
    }
}
