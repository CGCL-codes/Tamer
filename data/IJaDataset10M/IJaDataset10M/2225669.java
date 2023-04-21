package com.gorillalogic.dal.common.value;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.common.*;
import com.gorillalogic.dal.model.*;
import com.gorillalogic.dal.common.expr.GCLOps;
import java.lang.reflect.Array;

class VectorTable extends CommonTable implements CommonDomain, TypeBuilder, CommonTableExtender {

    private String name;

    private VectorColumn[] columns = null;

    private CommonType type;

    public void apply(Sweep sweep, boolean end) {
        super.apply(sweep, false);
        sweep.fd("name", name);
        sweep.fd("type", type);
        sweep.end(end);
    }

    VectorTable(CommonExpr expr, CommonType type, CommonScope scope) throws AccessException {
        this.type = type;
        name = expr.asGCL();
        expr.addSelfAsColumn(scope, this, false);
        expr.addSelfAsRow(scope, this);
    }

    VectorTable(CommonExpr expr) throws AccessException {
        type = expr.commonType();
        doAddColumn("value", type);
        name = expr.asGCL();
    }

    VectorTable(String name, CommonTable table) throws AccessException {
        this.name = name;
        final int rcx = table.getRowIdColumnIndex();
        final int ocx = table.getOidColumnIndex();
        final int rc = table.rowCount();
        int cc = table.columnCount();
        for (int cx = 0; cx < cc; cx++) {
            CommonColumnHdr hdr = table.commonColumn(cx);
            int lcx = doAddColumn(hdr.getName(), hdr.commonType());
            VectorColumn vc = columns[lcx];
            vc.setVectorCount(table, rc);
        }
        if (columnCount() == 1) {
            type = columns[0].commonType();
        } else {
            type = this;
        }
        cc = columnCount();
        CommonItr itr = table.commonLoopLock();
        while (itr.next()) {
            for (int cx = 0; cx < cc; cx++) {
                VectorColumn vc = columns[cx];
                vc.setFrom(itr.count(), cx, itr);
            }
        }
    }

    void setColumn(CommonExpr expr, CommonScope scope, int cx) throws AccessException {
        CommonTable table = expr.computeTable(scope);
        VectorColumn column = columns[cx];
        column.setVectorCount(table, table.rowCount());
        CommonItr itr = table.commonLoopLock();
        while (itr.next()) {
            column.setFrom(itr.count(), itr);
        }
    }

    public CommonTableExtender commonExtend(boolean toss) throws OperationException {
        return this;
    }

    protected CommonRow doAddCommonRow(CommonTable.InitializationStrategy izy) throws AccessException {
        int pos = 0;
        final int cc = columns == null ? 0 : columns.length;
        for (int cx = 0; cx < cc; cx++) {
            pos = columns[cx].append(1);
        }
        return commonRow(pos);
    }

    protected CommonRow doAddCommonRef(CommonRow row) throws AccessException {
        if (row.commonType().isValueType()) {
            return commonRow(addCopyRow(row));
        } else {
            return super.doAddCommonRef(row);
        }
    }

    private int addCopyRow(CommonRow row) throws AccessException {
        int pos = -1;
        final int cc = columns == null ? 0 : columns.length;
        for (int cx = 0; cx < cc; cx++) {
            VectorColumn vc = columns[cx];
            pos = vc.append(1);
            vc.setFrom(pos, cx, row);
        }
        return pos;
    }

    public TypeBuilder builder(boolean toss) throws OperationException {
        return this;
    }

    protected int doAddColumn(String columnName, CommonExpr expr) throws AccessException {
        CommonType type = expr.commonType();
        return doAddColumn(columnName, type);
    }

    protected int doAddColumn(String columnName, CommonType type) throws AccessException {
        Type.DispatchReturningInt dispatch = new Type.DispatchReturningInt() {

            public int applyInt(Type type) throws AccessException {
                return addColumn(new IntVectorColumn());
            }

            public int applyLong(Type type) throws AccessException {
                return addColumn(new LongVectorColumn());
            }

            public int applyFloat(Type type) throws AccessException {
                return addColumn(new FloatVectorColumn());
            }

            public int applyDouble(Type type) throws AccessException {
                return addColumn(new DoubleVectorColumn());
            }

            public int applyBoolean(Type type) throws AccessException {
                return addColumn(new BooleanVectorColumn());
            }

            public int applyString(Type type) throws AccessException {
                return addColumn(new StringVectorColumn());
            }

            public int applyAny(Type type) throws AccessException {
                return addColumn(new AnyVectorColumn());
            }

            public int applyTable(Type type) throws AccessException {
                throw new InternalException("Vector table");
            }
        };
        int pos = type.apply(dispatch);
        columns[pos].setName(columnName);
        return pos;
    }

    private int addColumn(VectorColumn refColumn) {
        if (columns == null) {
            columns = new VectorColumn[1];
        } else {
            VectorColumn[] temp = new VectorColumn[columns.length + 1];
            System.arraycopy(columns, 0, temp, 0, columns.length);
            columns = temp;
        }
        int pos = columns.length - 1;
        columns[pos] = refColumn;
        return pos;
    }

    protected String doPath(PathStrategy strategy) {
        return name;
    }

    protected int doRowCount() {
        if (columns == null) return 0;
        return columns[0].rowCount();
    }

    public int columnCount() {
        return columns == null ? 0 : columns.length;
    }

    protected CommonColumnHdr doCommonColumn(int column) throws BoundsException {
        return columns[column];
    }

    public boolean isValueType() {
        return true;
    }

    public CommonTable commonExtent() {
        return type == this ? this : type.commonExtent();
    }

    public CommonDomain commonDomain() {
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws OperationException {
        this.name = name;
    }

    abstract class VectorColumn extends CommonColumnHdr {

        private String name;

        public CommonType getEnclosingCommonType() {
            return VectorTable.this;
        }

        abstract int rowCount();

        public String getName() {
            return name;
        }

        protected void doSetName(String name) throws OperationException {
            this.name = name;
        }

        abstract void setVectorCount(CommonTable table, int count);

        abstract int append(int count) throws AccessException;

        abstract void setFrom(int rx, CommonRow row) throws AccessException;

        abstract void setFrom(int rx, int cx, CommonRow row) throws AccessException;

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            throw new UnsupportedException("VectorTable.setFrom");
        }

        protected void doWriteTypedTable(CommonRow destRow, CommonTable value) throws AccessException {
            throw new UnsupportedException("VectorTable.doWriteTypedTable");
        }

        public int getMinCard() {
            return 0;
        }

        public int getMaxCard() {
            return Integer.MAX_VALUE;
        }

        int ord(CommonScope scope, Object vector) throws AccessException {
            int ord = (int) scope.commonData().asRowId();
            if (vector == null || ord < 0 || ord >= Array.getLength(vector)) {
                BoundsException.checkRow(VectorTable.this, ord);
            }
            return ord;
        }
    }

    class IntVectorColumn extends VectorColumn {

        private int[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XINT;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new int[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new int[count];
            } else {
                pos = vector.length;
                int[] temp = new int[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getInt(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getInt(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            int v = right.computeInt(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeInt(scope);
        }

        protected final int readTypedInt(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedInt(CommonScope scope, int value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }

    class LongVectorColumn extends VectorColumn {

        private long[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XLONG;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new long[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new long[count];
            } else {
                pos = vector.length;
                long[] temp = new long[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getLong(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getLong(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            long v = right.computeLong(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeLong(scope);
        }

        protected final long readTypedLong(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedLong(CommonScope scope, long value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }

    class FloatVectorColumn extends VectorColumn {

        private float[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XFLOAT;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new float[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new float[count];
            } else {
                pos = vector.length;
                float[] temp = new float[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getFloat(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getFloat(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            float v = right.computeFloat(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeFloat(scope);
        }

        protected final float readTypedFloat(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedFloat(CommonScope scope, float value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }

    class DoubleVectorColumn extends VectorColumn {

        private double[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XDOUBLE;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new double[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new double[count];
            } else {
                pos = vector.length;
                double[] temp = new double[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getDouble(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getDouble(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            double v = right.computeDouble(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeDouble(scope);
        }

        protected final double readTypedDouble(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedDouble(CommonScope scope, double value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }

    class BooleanVectorColumn extends VectorColumn {

        private boolean[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XBOOLEAN;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new boolean[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new boolean[count];
            } else {
                pos = vector.length;
                boolean[] temp = new boolean[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getBoolean(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getBoolean(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            boolean v = right.computeBoolean(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeBoolean(scope);
        }

        protected final boolean readTypedBoolean(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedBoolean(CommonScope scope, boolean value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }

    class StringVectorColumn extends VectorColumn {

        private String[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XSTRING;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new String[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new String[count];
            } else {
                pos = vector.length;
                String[] temp = new String[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getString(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getString(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            String v = right.computeString(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeString(scope);
        }

        protected final String readTypedString(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedString(CommonScope scope, String value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }

    class AnyVectorColumn extends VectorColumn {

        private Object[] vector = null;

        int rowCount() {
            return vector == null ? 0 : vector.length;
        }

        public CommonType commonType() {
            return CommonType.XANY;
        }

        void setVectorCount(CommonTable table, int count) {
            vector = new Object[count];
        }

        int append(int count) throws AccessException {
            int pos = 0;
            if (vector == null) {
                vector = new Object[count];
            } else {
                pos = vector.length;
                Object[] temp = new Object[vector.length + count];
                System.arraycopy(vector, 0, temp, 0, vector.length);
                vector = temp;
            }
            return pos;
        }

        final void setFrom(int ord, CommonRow row) throws AccessException {
            vector[ord] = row.getAny(0);
        }

        final void setFrom(int ord, int cx, CommonRow row) throws AccessException {
            vector[ord] = row.getAny(cx);
        }

        public void setFrom(CommonScope leftScope, CommonExpr right, CommonScope rightScope) throws AccessException {
            int pos = (int) leftScope.commonData().asRowId();
            Object v = right.computeAny(rightScope);
            vector[pos] = v;
        }

        protected void appendFrom(CommonExpr expr, CommonScope scope, int count) throws AccessException {
            int pos = append(1);
            vector[pos] = expr.computeAny(scope);
        }

        protected final Object readTypedAny(CommonScope scope) throws AccessException {
            int ord = ord(scope, vector);
            return vector[ord];
        }

        protected final void writeTypedAny(CommonScope scope, Object value) throws AccessException {
            int ord = ord(scope, vector);
            vector[ord] = value;
        }
    }
}
