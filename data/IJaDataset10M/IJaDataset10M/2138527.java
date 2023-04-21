package net.jadoth.sqlengine.sql.columntypes;

import net.jadoth.sqlengine.sql.definitions.BaseTableColumn;
import net.jadoth.sqlengine.sql.definitions.COLUMN;
import net.jadoth.sqlengine.sql.definitions.TABLE;

public interface SMALLINT extends SqlIntegerColumn {

    public class Implementation extends COLUMN.Implementation implements SMALLINT {

        public Implementation(final BaseTableColumn.Descriptor descriptor) {
            super(descriptor);
        }

        public Implementation(final BaseTableColumn.Descriptor descriptor, final TABLE owner) {
            super(descriptor, owner);
        }
    }
}
