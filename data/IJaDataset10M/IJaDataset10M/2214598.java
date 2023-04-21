package ti.plato.ui.views.status;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import ti.io.VersionedExternalizable;
import ti.plato.ui.views.status.constants.Constants;

/**
 * 
 */
public class WorkspaceSaveContainer extends VersionedExternalizable {

    private int[] columnWidths = null;

    public int[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(int[] columnWidthsParameter) {
        columnWidths = columnWidthsParameter;
    }

    public void clear() {
        columnWidths = null;
    }

    @Override
    protected int getCurrentVersion() {
        return Constants.currentWorkspaceVersion;
    }

    @Override
    protected void readVersioned(int version, DataInput in) throws IOException {
        if (version < 2) return;
        int columnWidthLength = in.readInt();
        if (columnWidthLength != 0) {
            columnWidths = new int[columnWidthLength];
            for (int idx = 0; idx < columnWidthLength; idx++) columnWidths[idx] = in.readInt();
        }
    }

    @Override
    protected void writeVersioned(DataOutput out) throws IOException {
        if (columnWidths == null) out.writeInt(0); else {
            out.writeInt(columnWidths.length);
            for (int idx = 0; idx < columnWidths.length; idx++) out.writeInt(columnWidths[idx]);
        }
    }
}
