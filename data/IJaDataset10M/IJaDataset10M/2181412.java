package net.sf.JRecord.Details;

import java.util.ArrayList;
import java.util.HashMap;
import net.sf.JRecord.Common.Constants;
import net.sf.JRecord.Common.FieldDetail;
import net.sf.JRecord.Common.RecordException;
import net.sf.JRecord.Common.XmlConstants;

public class ArrayListLine<FieldDef extends FieldDetail, RecordDef extends AbstractRecordDetail<FieldDef>, Layout extends AbstractLayoutDetails<FieldDef, RecordDef>> implements AbstractLine<Layout> {

    protected ArrayList<Object> fields = new ArrayList<Object>();

    protected Layout layout;

    protected int preferredLayout = Constants.NULL_INTEGER;

    protected boolean newRecord;

    protected boolean rebuildRequired = false;

    protected AbstractTreeDetails<FieldDef, RecordDef, Layout, ArrayListLine<FieldDef, RecordDef, Layout>> children;

    private static HashMap<Class, AbstractTreeDetails> nullTrees = new HashMap<Class, AbstractTreeDetails>(5);

    public ArrayListLine(Layout layoutDetails, int recordIdx) {
        layout = layoutDetails;
        newRecord = recordIdx < 0;
        preferredLayout = recordIdx;
        fields.add("");
        if (newRecord) {
            fields.add("True");
        }
        if (layout != null && layout.hasChildren()) {
            children = new TreeDetails<FieldDef, RecordDef, Layout, AbstractChildDetails<RecordDef>, ArrayListLine<FieldDef, RecordDef, Layout>>();
        } else {
            children = getNullTree();
        }
    }

    private AbstractTreeDetails<FieldDef, RecordDef, Layout, ArrayListLine<FieldDef, RecordDef, Layout>> getNullTree() {
        AbstractTreeDetails<FieldDef, RecordDef, Layout, ArrayListLine<FieldDef, RecordDef, Layout>> ret;
        if (nullTrees.containsKey(this.getClass())) {
            ret = nullTrees.get(this.getClass());
        } else {
            ret = new NullTreeDtls<FieldDef, RecordDef, Layout, AbstractChildDetails<RecordDef>, ArrayListLine<FieldDef, RecordDef, Layout>>();
            nullTrees.put(this.getClass(), ret);
        }
        return ret;
    }

    /**
	 * @see java.lang.Object#clone()
	 */
    public Object clone() {
        Object ret = null;
        try {
            ret = super.clone();
        } catch (Exception e) {
        }
        if (!(ret instanceof ArrayListLine)) {
            ArrayListLine<FieldDef, RecordDef, Layout> line = new ArrayListLine<FieldDef, RecordDef, Layout>(layout, preferredLayout);
            for (int i = 0; i < fields.size(); i++) {
                try {
                    line.setRawField(preferredLayout, i, fields.get(i));
                } catch (Exception e) {
                }
            }
            ret = line;
        }
        return ret;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getData()
	 */
    public byte[] getData() {
        return null;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getData(int, int)
	 */
    public byte[] getData(int start, int len) {
        return null;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getField(net.sf.JRecord.Common.FieldDetail)
	 */
    public Object getField(FieldDetail field) {
        return getFieldRaw(preferredLayout, field.getPos());
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getField(int, int)
	 */
    public Object getField(int recordIdx, int fieldIdx) {
        switch(fieldIdx) {
            case Constants.KEY_INDEX:
                return null;
        }
        return getFieldRaw(recordIdx, fieldIdx);
    }

    private Object getFieldRaw(int recordIdx, int fieldIdx) {
        if (fields.size() > fieldIdx && fieldIdx >= 0) {
            return fields.get(fieldIdx);
        }
        return null;
    }

    @Override
    public AbstractFieldValue getFieldValue(FieldDetail field) {
        return new FieldValue(this, field);
    }

    @Override
    public AbstractFieldValue getFieldValue(int recordIdx, int fieldIdx) {
        return new FieldValue(this, recordIdx, fieldIdx);
    }

    @Override
    public AbstractFieldValue getFieldValue(String fieldName) {
        return getFieldValue(layout.getFieldFromName(fieldName));
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getFieldBytes(int, int)
	 */
    public byte[] getFieldBytes(int recordIdx, int fieldIdx) {
        return null;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getFieldHex(int, int)
	 */
    public String getFieldHex(int recordIdx, int fieldIdx) {
        return null;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getFieldText(int, int)
	 */
    public String getFieldText(int recordIdx, int fieldIdx) {
        String s = "";
        Object o;
        if (fieldIdx < fields.size() && (o = fields.get(fieldIdx)) != null) {
            s = o.toString();
        }
        return s;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getFullLine()
	 */
    public String getFullLine() {
        return "";
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getPreferredLayoutIdx()
	 */
    @Override
    public int getPreferredLayoutIdx() {
        return preferredLayout;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getPreferredLayoutIdx()
	 */
    @Override
    public int getPreferredLayoutIdxAlt() {
        return preferredLayout;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#replace(byte[], int, int)
	 */
    public void replace(byte[] rec, int start, int len) {
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setData(java.lang.String)
	 */
    public void setData(String newVal) {
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setData(byte[])
	 */
    @Override
    public void setData(byte[] newVal) {
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setField(net.sf.JRecord.Common.FieldDetail, java.lang.Object)
	 */
    public void setField(FieldDetail field, Object value) throws RecordException {
        setRawField(preferredLayout, field.getPos(), value);
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setField(int, int, java.lang.Object)
	 */
    public void setField(int recordIdx, int fieldIdx, Object val) throws RecordException {
        setRawField(recordIdx, fieldIdx, val);
    }

    public void setRawField(int recordIdx, int fieldIdx, Object val) throws RecordException {
        for (int i = fields.size(); i <= fieldIdx; i++) {
            fields.add(null);
        }
        if (val != null || fields.get(fieldIdx) != null) {
            fields.set(fieldIdx, val);
        }
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setField(java.lang.String, java.lang.Object)
	 */
    public void setField(String fieldName, Object value) throws RecordException {
        setField(layout.getFieldFromName(fieldName), value);
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setFieldHex(int, int, java.lang.String)
	 */
    public String setFieldHex(int recordIdx, int fieldIdx, String val) throws RecordException {
        return "";
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setFieldText(int, int, java.lang.String)
	 */
    public void setFieldText(int recordIdx, int fieldIdx, String value) throws RecordException {
        setField(recordIdx, fieldIdx, value);
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#setWriteLayout(int)
	 */
    public void setWriteLayout(int pWriteLayout) {
        preferredLayout = pWriteLayout;
        fields.set(XmlConstants.NAME_INDEX, layout.getRecord(preferredLayout).getRecordName());
    }

    /**
	 * @param pLayout The layouts to set.
	 */
    public void setLayout(final Layout pLayout) {
        preferredLayout = pLayout.getRecordIndex(layout.getRecord(preferredLayout).getRecordName());
        this.layout = pLayout;
    }

    /**
	 * @see net.sf.JRecord.Details.AbstractLine#getLayout()
	 */
    public Layout getLayout() {
        return layout;
    }

    /**
	 * Set the line provider
	 *
	 * @param pLineProvider The lineProvider to set.
	 */
    public void setLineProvider(LineProvider<Layout> pLineProvider) {
    }

    /**
	 * Test if Tree rebuild is required
	 */
    public boolean isRebuildTreeRequired() {
        boolean ret = rebuildRequired;
        rebuildRequired = false;
        return ret;
    }

    /**
	 * @return the children
	 */
    public final AbstractTreeDetails<FieldDef, RecordDef, Layout, ArrayListLine<FieldDef, RecordDef, Layout>> getTreeDetails() {
        return children;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public <L extends AbstractLine> L getNewDataLine() {
        return (L) clone();
    }
}
