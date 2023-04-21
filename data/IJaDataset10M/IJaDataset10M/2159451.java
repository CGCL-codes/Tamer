package net.entropysoft.dashboard.plugin.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.entropysoft.dashboard.plugin.variables.IVariable;
import net.entropysoft.dashboard.plugin.variables.VariableManager;
import net.entropysoft.dashboard.plugin.variables.VariablePath;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 * Transfer class for Variable
 * 
 * @author cedric
 */
public class VariableTransfer extends ByteArrayTransfer {

    public static final String TYPE_NAME = "Variable-transfer-format";

    public static int TYPE = registerType(TYPE_NAME);

    private static VariableTransfer instance = new VariableTransfer();

    private VariableTransfer() {
    }

    public static VariableTransfer getInstance() {
        return instance;
    }

    protected int[] getTypeIds() {
        return new int[] { TYPE };
    }

    protected String[] getTypeNames() {
        return new String[] { TYPE_NAME };
    }

    public void javaToNative(Object object, TransferData transferData) {
        if (!isSupportedType(transferData)) {
            DND.error(DND.ERROR_INVALID_DATA);
        }
        final byte[] bytes = toByteArray((IVariable) object);
        if (bytes != null) {
            super.javaToNative(bytes, transferData);
        }
    }

    protected Object nativeToJava(TransferData transferData) {
        byte[] bytes = (byte[]) super.nativeToJava(transferData);
        return fromByteArray(bytes);
    }

    public Object fromByteArray(byte[] bytes) {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            int identifierLength = in.readInt();
            String[] identifier = new String[identifierLength];
            for (int i = 0; i < identifierLength; i++) {
                identifier[i] = in.readUTF();
            }
            VariablePath variablePath = new VariablePath(identifier);
            return VariableManager.getInstance().getVariable(variablePath);
        } catch (Exception ex) {
            return null;
        }
    }

    public byte[] toByteArray(IVariable variable) {
        String[] identifier = variable.getPath().getPathElements();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeInt(identifier.length);
            for (int i = 0; i < identifier.length; i++) {
                dos.writeUTF(identifier[i]);
            }
            return bos.toByteArray();
        } catch (Exception ex) {
            return null;
        }
    }
}
