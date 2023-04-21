package edu.washington.mysms.server.extend;

import edu.washington.mysms.coding.ColumnsDefinition;
import edu.washington.mysms.coding.ResultColumn;
import edu.washington.mysms.coding.ResultTable;

/**
 * A specialized table for containing arguments to DDFs.
 * This table is for internal use only, and not for sending
 * to the client.
 * 
 * @author Anthony Poon
 */
public abstract class DDFArgumentsTable extends ResultTable {

    private static final long serialVersionUID = 9075799401459494128L;

    private boolean hasValues;

    private boolean hasMetaInfo;

    protected DDFArgumentsTable(boolean hasMetaInfo, boolean hasValues) {
        super();
        ColumnsDefinition columns = new ColumnsDefinition();
        columns.add(new ResultColumn("argName", !hasMetaInfo, String.class));
        columns.add(new ResultColumn("argType", !hasMetaInfo, String.class));
        columns.add(new ResultColumn("argValue", !hasValues, String.class));
        columns.setFinalized();
        this.setColumnsDefinition(columns);
        this.hasValues = hasValues;
        this.hasMetaInfo = hasMetaInfo;
    }

    public boolean hasValues() {
        return this.hasValues;
    }

    public boolean hasMetaInfo() {
        return this.hasMetaInfo;
    }

    public void setHasValues(boolean hasValues) {
        this.hasValues = hasValues;
    }

    public void setHasMetaInfo(boolean hasMetaInfo) {
        this.hasMetaInfo = hasMetaInfo;
    }

    public static class MetaInfo extends DDFArgumentsTable {

        private static final long serialVersionUID = -4286625745482479510L;

        public MetaInfo() {
            super(true, false);
        }

        public String[] getTypes() {
            String[] types = new String[this.size()];
            for (int i = 0; i < types.length; i++) {
                types[i] = (String) this.get(i).get("argType");
            }
            return types;
        }

        public String[] getNames() {
            String[] names = new String[this.size()];
            for (int i = 0; i < names.length; i++) {
                names[i] = (String) this.get(i).get("argName");
            }
            return names;
        }
    }

    public static class Values extends DDFArgumentsTable {

        private static final long serialVersionUID = -4857333821366394635L;

        public Values() {
            super(false, true);
        }

        public String[] getValues() {
            String[] values = new String[this.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = (String) this.get(i).get("argValue");
            }
            return values;
        }
    }

    public static class Both extends DDFArgumentsTable {

        private static final long serialVersionUID = 1999398273906627467L;

        public Both() {
            super(true, true);
        }

        public String[] getTypes() {
            String[] types = new String[this.size()];
            for (int i = 0; i < types.length; i++) {
                types[i] = (String) this.get(i).get("argType");
            }
            return types;
        }

        public String[] getNames() {
            String[] names = new String[this.size()];
            for (int i = 0; i < names.length; i++) {
                names[i] = (String) this.get(i).get("argName");
            }
            return names;
        }

        public String[] getValues() {
            String[] values = new String[this.size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = (String) this.get(i).get("argValue");
            }
            return values;
        }
    }
}
