package org.omwg.ui.dnd;

import org.eclipse.swt.dnd.*;
import org.wsmo.common.*;

/**
 * @author Jan Henke, jan.henke@deri.org
 */
public class DomeTransfer extends ByteArrayTransfer {

    private static final String MYTYPENAME = "my_type_name";

    private static final int MYTYPEID = registerType(MYTYPENAME);

    private static DomeTransfer _instance = new DomeTransfer();

    Object[] identifiable;

    public static DomeTransfer getInstance() {
        return _instance;
    }

    private DomeTransfer() {
    }

    protected int[] getTypeIds() {
        return new int[] { MYTYPEID };
    }

    protected String[] getTypeNames() {
        return new String[] { MYTYPENAME };
    }

    protected void javaToNative(Object object, TransferData transferData) {
        if (object == null || !(object instanceof Entity || object instanceof Object[])) return;
        if (isSupportedType(transferData)) {
            identifiable = (Object[]) object;
        }
    }

    protected Object nativeToJava(TransferData transferData) {
        super.nativeToJava(transferData);
        return identifiable;
    }
}
