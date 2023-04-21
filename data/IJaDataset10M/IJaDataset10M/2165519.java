package cn.ekuma.impexp.model;

import cn.ekuma.impexp.*;
import com.openbravo.data.loader.DataWriteUtils;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.TableDefinition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public abstract class AbstractImpExpTableDefinition {

    protected TableDefinition baseTable;

    int[] filterIndexs;

    Map<Integer, FieldConverse> filedConversions;

    Map<Integer, FieldGenerator> fieldGenerators;

    static int STATE_Default = 0;

    static int STATE_Imported = 1;

    static int STATE_Exported = 2;

    int state;

    List<ImpExpRowDefinition> datas;

    int[] updateIgnoreIndexs;

    public AbstractImpExpTableDefinition(TableDefinition baseTable) {
        this.baseTable = baseTable;
        filedConversions = new HashMap<Integer, FieldConverse>();
        fieldGenerators = new HashMap<Integer, FieldGenerator>();
        filterIndexs = new int[] {};
        updateIgnoreIndexs = new int[] {};
        datas = new ArrayList<ImpExpRowDefinition>();
    }

    public ImpExpRowDefinition getRow(int rowIndex) {
        return datas.get(rowIndex);
    }

    public abstract Object[] getData(int rowIndex);

    public abstract int[] getFieldIndexs();

    public abstract int[] getUpdateFieldIndexs();

    public SerializerRead getSerializerReadBasic() {
        return baseTable.getSerializerReadBasic(getFieldIndexs());
    }

    public SerializerWrite getSerializerInsertBasic() {
        return baseTable.getSerializerInsertBasic(getFieldIndexs());
    }

    public SerializerWrite getSerializerDeleteBasic() {
        return baseTable.getSerializerDeleteBasic();
    }

    public SerializerWrite getSerializerUpdateBasic() {
        return baseTable.getSerializerUpdateBasic(getUpdateFieldIndexs());
    }

    public SentenceExec getInsertSentence() {
        return getInsertSentence(getFieldIndexs());
    }

    public SentenceExec getInsertSentence(int[] fieldindx) {
        return new PreparedSentence(baseTable.getM_s(), getInsertSQL(), getSerializerInsertBasic(), null);
    }

    public SentenceList getListSentence() {
        return getListSentence(getSerializerReadBasic());
    }

    public SentenceList getListSentence(SerializerRead sr) {
        return new PreparedSentence(baseTable.getM_s(), getListSQL(), null, sr);
    }

    public SentenceExec getDeleteSentence() {
        return getDeleteSentence(getSerializerDeleteBasic());
    }

    public SentenceExec getDeleteSentence(SerializerWrite sw) {
        return new PreparedSentence(baseTable.getM_s(), getDeleteSQL(), sw, null);
    }

    public SentenceExec getUpdateSentence() {
        return getUpdateSentence(getUpdateFieldIndexs());
    }

    public SentenceExec getUpdateSentence(int[] fieldindx) {
        return new PreparedSentence(baseTable.getM_s(), getUpdateSQL(), getSerializerUpdateBasic(), null);
    }

    public String getListSQL() {
        return baseTable.getListSQL(getFieldIndexs());
    }

    public String getListSQL(Date start, Date end) {
        String sql = baseTable.getListSQL(getFieldIndexs());
        String dateField = baseTable.getDateFieldName();
        if (dateField != null) {
            sql += " where " + baseTable.getDateFieldName() + ">=" + DataWriteUtils.getSQLValue(start);
            sql += " and " + baseTable.getDateFieldName() + "<=" + DataWriteUtils.getSQLValue(start);
        }
        return sql;
    }

    public String getDeleteSQL() {
        return baseTable.getDeleteSQL();
    }

    public String getInsertSQL() {
        return baseTable.getInsertSQL(getFieldIndexs());
    }

    public String getUpdateSQL() {
        return baseTable.getUpdateSQL(getUpdateFieldIndexs());
    }

    public TableDefinition getBaseTable() {
        return baseTable;
    }

    public List<ImpExpRowDefinition> getDatas() {
        return datas;
    }

    public Map<Integer, FieldGenerator> getFieldGenerators() {
        return fieldGenerators;
    }

    public Map<Integer, FieldConverse> getFiledConversions() {
        return filedConversions;
    }

    public int[] getFilterIndexs() {
        return filterIndexs;
    }

    public int getState() {
        return state;
    }

    public int[] getUpdateIgnoreIndexs() {
        return updateIgnoreIndexs;
    }

    public void addRow(ImpExpRowDefinition row) {
        datas.add(row);
    }
}
