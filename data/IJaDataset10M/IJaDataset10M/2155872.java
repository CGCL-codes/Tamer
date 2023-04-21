package se.uu.it.cats.brick.storage;

import se.uu.it.cats.brick.network.ConnectionManager;

public class StorageManager {

    private static StorageManager _instanceHolder = new StorageManager();

    public static SharedByte sb = new SharedByte();

    public static StorageManager getInstance() {
        return _instanceHolder;
    }

    private StorageManager() {
    }

    public void dataInput(byte b, String source) {
        sb.updateData(b);
        ConnectionManager.getInstance().sendByteToAllExcept(sb.getData(), source);
    }

    public void informAboutUpdate(SharedByte sb) {
        ConnectionManager.getInstance().sendByteToAll(sb.getData());
    }

    public byte getData() {
        return sb.getData();
    }

    public void setData(byte b) {
        sb.setData(b);
    }
}
