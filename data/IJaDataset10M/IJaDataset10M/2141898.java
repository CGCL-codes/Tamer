package org.sqlsplatter.tinyhorror.other;

import static org.sqlsplatter.tinyhorror.other.exceptions.ErrCode.FIELD_NOT_FOUND;
import static org.sqlsplatter.tinyhorror.other.exceptions.ErrCode.TABLENAME_AMBIGUOUS;
import static org.sqlsplatter.tinyhorror.other.exceptions.ErrCode.TABLE_NOT_FOUND;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.sqlsplatter.tinyhorror.actions.Select.SelectionField;
import org.sqlsplatter.tinyhorror.aggregates.Aggregate;
import org.sqlsplatter.tinyhorror.objects.ThsColumn;
import org.sqlsplatter.tinyhorror.objects.ThsTable;
import org.sqlsplatter.tinyhorror.other.exceptions.THSException;
import org.sqlsplatter.tinyhorror.other.structures.AliasableIdent;
import org.sqlsplatter.tinyhorror.values.DataType;
import org.sqlsplatter.tinyhorror.values.FieldValue;
import org.sqlsplatter.tinyhorror.values.Value;
import org.sqlsplatter.tinyhorror.values.functions.IFunctionValue;

/**
 * Maps fields to positions inside record (Object[]) and types.
 * <p>
 * Pay attention: IdentifierValues are hashed considering only prefix and name,
 * not alias.<br>
 * Alias mapping is done by SelectableWrapper class.
 * <p>
 * Important conventions:
 * <ul>
 * <li>'Field' = Prefix and name; 'Name' = name only.</li>
 * <li>'Resolved' table = Table which name has been changed to the alias,
 *      if it has one, or that has the same name, if it hasn't one.</li>
 * </ul>
 */
public class TableFieldsMapper {

    /** indicates a non-uniquely identifiable field. */
    private static final Integer AMBIG_COLUMN = -1;

    private final Map<FieldValue, Integer> fieldsPositions;

    private final Map<String, ThsTable> resolvedTables;

    /**
	 * For tables without aliases (ie. in DELETE).
	 * TODO: false; DELETE can have aliases, so we must destroy this method
	 */
    public TableFieldsMapper(ThsTable table) {
        List<DataType> typesList = new ArrayList<DataType>();
        resolvedTables = new HashMap<String, ThsTable>();
        fieldsPositions = new HashMap<FieldValue, Integer>();
        List<? extends ThsColumn> columns = table.getColumns();
        for (int pos = 0; pos < columns.size(); pos++) {
            ThsColumn column = columns.get(pos);
            typesList.add(column.getType());
            FieldValue fieldIdent = new FieldValue(table.getName(), column.getName());
            fieldsPositions.put(fieldIdent, pos);
            FieldValue colIdent = new FieldValue(null, column.getName());
            fieldsPositions.put(colIdent, pos);
        }
        resolvedTables.put(table.getName(), table);
    }

    /**
	 * Maps fields to positions and types.
	 *
	 * @param fromTables
	 *            "FROM" tables.
	 * @param tables
	 *            tables dictionary.
	 */
    public TableFieldsMapper(List<AliasableIdent> fromTables, Map<String, ThsTable> tables) throws THSException {
        List<DataType> typesList = new ArrayList<DataType>();
        resolvedTables = new HashMap<String, ThsTable>();
        fieldsPositions = new HashMap<FieldValue, Integer>();
        int pos = 0;
        for (AliasableIdent fromTable : fromTables) {
            ThsTable table = tables.get(fromTable.name);
            if (table == null) {
                throw new THSException(TABLE_NOT_FOUND, fromTable.name);
            }
            String resTableName = fromTable.getResolvedName();
            if (resolvedTables.containsKey(resTableName)) {
                throw new THSException(TABLENAME_AMBIGUOUS, resTableName);
            } else {
                resolvedTables.put(resTableName, table);
            }
            for (ThsColumn column : table.getColumns()) {
                typesList.add(column.getType());
                String colName = column.getName();
                FieldValue nameKey = new FieldValue(colName);
                insertFieldValueInPositions(nameKey, pos);
                FieldValue fieldKey = new FieldValue(resTableName, colName);
                fieldsPositions.put(fieldKey, pos);
                pos++;
            }
        }
    }

    /**
	 * Build a mapper from selectables.<br>
	 * This disables the getResolvedTables function.
	 * <p>
	 * Todo: doesn't support mapping of 'Table.Column' format when the field
	 * used to construct has only the Column.
	 *
	 * @param fields
	 *            selection fields, nullable
	 * @param functions
	 *            selection functions, nullable
	 * @param aggregates
	 *            selection aggregates, nullable
	 */
    public TableFieldsMapper(List<SelectionField<FieldValue>> fields, List<SelectionField<IFunctionValue>> functions, List<SelectionField<Aggregate>> aggregates) {
        resolvedTables = null;
        int totFields = (fields == null ? 0 : fields.size()) + (functions == null ? 0 : functions.size()) + (aggregates == null ? 0 : aggregates.size());
        fieldsPositions = new HashMap<FieldValue, Integer>((int) (totFields / 0.7));
        if (fields != null) {
            for (SelectionField<FieldValue> selField : fields) {
                int pos = selField.position;
                insertFieldValueInPositions(selField.value, pos);
                if (selField.alias != null) {
                    FieldValue aliasField = new FieldValue(selField.alias);
                    insertFieldValueInPositions(aliasField, pos);
                }
                if (selField.value.table != null) {
                    FieldValue columnOnly = new FieldValue(selField.value.column);
                    insertFieldValueInPositions(columnOnly, pos);
                }
            }
        }
        if (functions != null) {
            for (SelectionField<IFunctionValue> function : functions) {
                int pos = function.position;
                if (function.alias != null) {
                    FieldValue aliasField = new FieldValue(function.alias);
                    insertFieldValueInPositions(aliasField, pos);
                }
            }
        }
        if (aggregates != null) {
            for (SelectionField<Aggregate> aggregate : aggregates) {
                int pos = aggregate.position;
                if (aggregate.alias != null) {
                    FieldValue aliasField = new FieldValue(aggregate.alias);
                    insertFieldValueInPositions(aliasField, pos);
                }
            }
        }
    }

    /**
	 * Insert a field in the positions map; if existing, assign an AMBIG_COLUMN position.
	 */
    private void insertFieldValueInPositions(FieldValue field, int pos) {
        if (fieldsPositions.containsKey(field)) {
            fieldsPositions.put(field, AMBIG_COLUMN);
        } else {
            fieldsPositions.put(field, pos);
        }
    }

    /**
	 * Expands stars to table fields.<br>
	 * This method is needed if if we link field values containing stars; if we
	 * don't do this, linking of a star field will fail.
	 * <p>
	 * <b>WARNING!</b> This method modifies the content all the lists passed.
	 *
	 * @param fields
	 *            selection fields list.
	 * @param functions
	 *            selection functions list.
	 * @param aggregates
	 *            selection aggregates list.
	 */
    public void expandStarFields(List<SelectionField<FieldValue>> fields, List<SelectionField<IFunctionValue>> functions, List<SelectionField<Aggregate>> aggregates, List<AliasableIdent> fromTables) {
        for (ListIterator<SelectionField<FieldValue>> selItr = fields.listIterator(); selItr.hasNext(); ) {
            SelectionField<FieldValue> selField = selItr.next();
            boolean isStar = "*".equals(selField.value.table) || "*".equals(selField.value.column);
            if (isStar) {
                convertStar(selField, selItr, fields, functions, aggregates, fromTables);
            }
        }
    }

    /**
	 * Convert a star to a list of fields.<br>
	 * <b>WARNING</b>Passed lists are modified reflecting the inserted fields.
	 */
    private void convertStar(SelectionField<FieldValue> selStar, ListIterator<SelectionField<FieldValue>> selItr, List<SelectionField<FieldValue>> fields, List<SelectionField<IFunctionValue>> functions, List<SelectionField<Aggregate>> aggregates, List<AliasableIdent> fromTables) {
        int insertedFields = 0;
        FieldValue star = selStar.value;
        int starPos = selStar.position;
        boolean isAllTables = star.table == null;
        selItr.remove();
        if (isAllTables) {
            for (AliasableIdent fromTable : fromTables) {
                String resTableName = fromTable.getResolvedName();
                int insertPos = starPos + insertedFields;
                insertedFields += insertTableFields(resTableName, selItr, insertPos);
            }
        } else {
            String resTableName = star.table;
            insertedFields = insertTableFields(resTableName, selItr, starPos);
        }
        int nextSelFieldIdx = selItr.nextIndex();
        insertedFields--;
        moveValuesPositions(nextSelFieldIdx, insertedFields, starPos, fields, functions, aggregates);
    }

    /**
	 * Move position of all selection values (fields, functions, aggregates)
	 *
	 * @param nextSelFieldIdx
	 *            index of next selection field inside the selection fields
	 *            list.
	 * @param insertedFields
	 *            number of inserted fields
	 * @param starPos
	 *            selection position of former star.
	 */
    private void moveValuesPositions(int nextSelFieldIdx, int insertedFields, int starPos, List<SelectionField<FieldValue>> fields, List<SelectionField<IFunctionValue>> functions, List<SelectionField<Aggregate>> aggregates) {
        for (int i = nextSelFieldIdx; i < fields.size(); i++) {
            SelectionField field = fields.get(i);
            if (field.position > starPos) field.position += insertedFields;
        }
        for (SelectionField function : functions) {
            if (function.position > starPos) function.position += insertedFields;
        }
        for (SelectionField aggregate : aggregates) {
            if (aggregate.position > starPos) aggregate.position += insertedFields;
        }
    }

    /**
	 * Insert new selection fields into the selection list, using columns of the
	 * passed table.
	 *
	 * @param resTableName
	 *            table name
	 * @param resTables
	 *            resolved tables
	 * @param selItr
	 *            selection fields iterator
	 * @param selCurrPos
	 *            current position inside selection list
	 * @return number of fields inserted.
	 */
    private int insertTableFields(String resTableName, ListIterator<SelectionField<FieldValue>> selItr, int selCurrPos) {
        ThsTable table = resolvedTables.get(resTableName);
        int fieldsInserted = 0;
        for (ThsColumn column : table.getColumns()) {
            String colName = column.getName();
            FieldValue field = new FieldValue(resTableName, colName);
            String alias = null;
            SelectionField<FieldValue> selField = new SelectionField<FieldValue>(field, selCurrPos, alias);
            selItr.add(selField);
            selCurrPos++;
            fieldsInserted++;
        }
        return fieldsInserted;
    }

    /**
	 * Return the position of a specific mapped field by table and name strings.
	 *
	 * @param fieldValue
	 *            field to get the position of.
	 * @return position of the field, 0-based.
	 * @throws SQLException
	 *             if field is not found.
	 */
    public int getFieldPosition(String table, String name) throws THSException {
        Value fieldValue = new FieldValue(table, name);
        return getFieldPosition(fieldValue);
    }

    /**
	 * Return the position of a specific mapped field.
	 *
	 * @param fieldValue
	 *            field to get the position of.
	 * @return position of the field, 0-based.
	 * @throws SQLException
	 *             if field is not found.
	 */
    public int getFieldPosition(Value fieldValue) throws THSException {
        Integer pos = fieldsPositions.get(fieldValue);
        if (pos == null) throw new THSException(FIELD_NOT_FOUND, fieldValue);
        return pos;
    }

    /**
	 * Get a copy of the resolved tables.
	 *
	 * @return copy of map of resolved tables.
	 */
    public Map<String, ThsTable> getResolvedTables() {
        assert (resolvedTables != null) : "Either there has been an initialization error or this object has been constructed using SelectionField[s].";
        return new HashMap<String, ThsTable>(resolvedTables);
    }
}
