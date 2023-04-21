package org.dicom4j.network.association;

public interface AsynOperationsWindow {

    public int count();

    public void removeAsynOperation(int messageID);
}
