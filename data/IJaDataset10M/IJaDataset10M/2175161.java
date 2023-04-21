package org.jdu.dao.wrapper;

import org.jdu.exception.ServiceException;
import weblogic.jdbc.common.OracleBlobImpl;

/**
 * Classe wrapper per Oracle BLOB se si utilizza un connection pool weblogic
 * 
 * @author epelli
 * 
 */
public class WeblogicBLOBWrapper implements LOBWrapper {

    private OracleBlobImpl blob;

    public Object getLob() {
        return blob;
    }

    public void setLob(Object lob) {
        this.blob = (OracleBlobImpl) lob;
    }

    public void writeLob(Object bytes) throws Exception {
        if (!(bytes instanceof byte[])) {
            throw new ServiceException("L'oggetto lob da inserire deve essere di tipo " + byte[].class);
        }
        if (bytes != null && blob != null) {
            byte[] b = (byte[]) bytes;
            if (blob.length() >= b.length) {
                this.blob.truncate(b.length);
            }
            this.blob.putBytes(1, b);
        }
    }
}
