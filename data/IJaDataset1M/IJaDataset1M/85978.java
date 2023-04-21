package common.protocolwrapper;

import java.io.InputStream;
import java.io.OutputStream;

public interface RequestProcessor {

    public void processRequest(InputStream is, OutputStream os) throws ServiceWrapperException;
}
