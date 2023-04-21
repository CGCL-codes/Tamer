package edu.asu.vspace.dspace.dnd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;
import edu.asu.vspace.dspace.dspaceMetamodel.DSpaceImage;

public class DSpaceImageTransfer extends ByteArrayTransfer {

    static final DSpaceImageTransfer _instance = new DSpaceImageTransfer();

    static final String IMAGE = "DSpaceImage";

    static final int IMAGEID = registerType(IMAGE);

    public static DSpaceImageTransfer getInstance() {
        return _instance;
    }

    protected DSpaceImageTransfer() {
    }

    public void javaToNative(Object object, TransferData transferData) {
        if (!checkMyType(object) || !isSupportedType(transferData)) {
            DND.error(DND.ERROR_INVALID_DATA);
        }
        DSpaceImage myImage = (DSpaceImage) object;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream writeOut = new ObjectOutputStream(out);
            {
                writeOut.writeObject(myImage);
            }
            byte[] buffer = out.toByteArray();
            writeOut.close();
            super.javaToNative(buffer, transferData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object nativeToJava(TransferData transferData) {
        if (isSupportedType(transferData)) {
            byte[] buffer = (byte[]) super.nativeToJava(transferData);
            if (buffer == null) return null;
            ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(bis);
                Object obj = ois.readObject();
                if (obj instanceof DSpaceImage) return obj;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected String[] getTypeNames() {
        return new String[] { IMAGE };
    }

    protected int[] getTypeIds() {
        return new int[] { IMAGEID };
    }

    boolean checkMyType(Object object) {
        if (object == null || !(object instanceof DSpaceImage)) {
            return false;
        }
        return true;
    }

    protected boolean validate(Object object) {
        return checkMyType(object);
    }
}
