package net.sourceforge.sqlexplorer.plugin.views;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class TableNodeTransfer extends ByteArrayTransfer {

    public String toString() {
        return "TableNodeTransfer";
    }

    private static final String TYPE_NAME = "TableNodeTransfer-format" + (new Long(System.currentTimeMillis())).toString();

    private static final int TYPEID = registerType(TYPE_NAME);

    private static final TableNodeTransfer INSTANCE = new TableNodeTransfer();

    private Object selection;

    /**
	 * Only the singleton instance of this class may be used. 
	 */
    private TableNodeTransfer() {
    }

    /**
	 * Returns the singleton.
	 */
    public static TableNodeTransfer getInstance() {
        return INSTANCE;
    }

    /**
	 * Returns the local transfer data.
	 * 
	 * @return the local transfer data
	 */
    public Object getSelection() {
        return selection;
    }

    /**
	 * Tests whether native drop data matches this transfer type.
	 * 
	 * @param result result of converting the native drop data to Java
	 * @return true if the native drop data does not match this transfer type.
	 * 	false otherwise.
	 */
    private boolean isInvalidNativeType(Object result) {
        return !(result instanceof byte[]) || !TYPE_NAME.equals(new String((byte[]) result));
    }

    /**
	 * Returns the type id used to identify this transfer.
	 * 
	 * @return the type id used to identify this transfer.
	 */
    protected int[] getTypeIds() {
        return new int[] { TYPEID };
    }

    /**
	 * Returns the type name used to identify this transfer.
	 * 
	 * @return the type name used to identify this transfer.
	 */
    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
    }

    /**
	 * Overrides org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(Object,
	 * TransferData).
	 * Only encode the transfer type name since the selection is read and
	 * written in the same process.
	 * 
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#javaToNative(java.lang.Object, org.eclipse.swt.dnd.TransferData)
	 */
    public void javaToNative(Object object, TransferData transferData) {
        byte[] check = TYPE_NAME.getBytes();
        super.javaToNative(check, transferData);
    }

    /**
	 * Overrides org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(TransferData).
	 * Test if the native drop data matches this transfer type.
	 * 
	 * @see org.eclipse.swt.dnd.ByteArrayTransfer#nativeToJava(TransferData)
	 */
    public Object nativeToJava(TransferData transferData) {
        Object result = super.nativeToJava(transferData);
        if (isInvalidNativeType(result)) {
            return null;
        }
        return selection;
    }

    /**
	 * Sets the transfer data for local use.
	 * 
	 * @param s the transfer data
	 */
    public void setSelection(Object s) {
        selection = s;
    }
}
