package mipt.gui.data.choice.table.ref;

public class RefTableChoiceModel extends mipt.gui.data.choice.table.TableChoiceModel {

    /**
 * ReferenceTableChoiceEditor constructor comment.
 * @param manager mipt.gui.choice.data.table.TableManager
 */
    public RefTableChoiceModel(mipt.gui.data.choice.table.TableManager manager) {
        super(manager);
    }

    /**
 * If position is not null and is absent in DataVector return false
 */
    public boolean addData(mipt.data.Data dataToAdd, mipt.data.Data position) {
        if (!super.addData(dataToAdd, position)) return false;
        String[] fieldNames = dataToAdd.getFieldNames();
        for (int i = 0; i < fieldNames.length; i++) {
            Object field = dataToAdd.get(fieldNames[i]);
            if (field instanceof mipt.data.DataSet) {
                manager.getTable().ensureEnoughRowHeightFor(((mipt.data.DataSet) field).toArray());
            }
        }
        return true;
    }

    /**
 * 
 * @return mipt.gui.choice.TableModel
 * @param headers java.lang.Object[]
 */
    public mipt.gui.choice.TableModel createTableModel(Object[] headers) {
        return new mipt.gui.choice.TableModel(headers) {

            public Class getColumnClass(int column) {
                Class cls = super.getColumnClass(column);
                if (mipt.data.DataSet.class.isAssignableFrom(cls)) setNonEditableColumn(column);
                return cls;
            }
        };
    }

    /**
 * 
 * @return java.lang.Object
 * @param field mipt.gui.choice.data.table.TableField
 * @param data mipt.data.Data
 */
    protected Object getFieldOf(mipt.gui.data.choice.table.TableField field, mipt.data.Data data) {
        if (field instanceof RefTableField) {
            if (field.name == null) return data;
            RefTableField refField = (RefTableField) field;
            mipt.data.Data refData;
            if (refField instanceof ListRefTableField) {
                if (refField.dataType == null) {
                    return data.getDataSet(field.name);
                } else {
                    if (data instanceof mipt.data.TypedFieldData) return ((mipt.data.TypedFieldData) data).getDataSet(refField.dataType, field.name); else return data.getDataSet(field.name);
                }
            } else {
                if (refField.dataType == null) {
                    refData = data.getData(field.name);
                } else {
                    if (data instanceof mipt.data.TypedFieldData) refData = ((mipt.data.TypedFieldData) data).getData(refField.dataType, field.name); else return data.getData(field.name);
                }
                if (refField.nextReference == null) return refData; else return getFieldOf(refField.nextReference, refData);
            }
        } else {
            return data.get(field.name);
        }
    }
}
