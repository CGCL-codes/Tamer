package org.spbgu.pmpu.athynia.central.network.communications.join;

import org.apache.log4j.Logger;
import org.spbgu.pmpu.athynia.common.Executor;
import org.spbgu.pmpu.athynia.common.ExecutorException;
import org.spbgu.pmpu.athynia.common.JoinPart;
import org.spbgu.pmpu.athynia.common.ResourceManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: vasiliy
 */
public class SearchTask implements Executor {

    private static final Logger LOG = Logger.getLogger(SearchTask.class);

    private static final int INTEGER_LENGTH_IN_BYTES_IN_UTF8 = 8;

    public void execute(InputStream fromServer, OutputStream toServer, ResourceManager manager) throws ExecutorException {
        try {
            byte[] keyLengthBuffer = new byte[INTEGER_LENGTH_IN_BYTES_IN_UTF8];
            fromServer.read(keyLengthBuffer);
            int keyLength = Integer.parseInt(decodeStringWithInteger(new String(keyLengthBuffer, "UTF-8")));
            byte[] keyBuffer = new byte[keyLength];
            fromServer.read(keyBuffer);
            String key = new String(keyBuffer, "UTF-8");
            LOG.debug("Searching in index with a key: " + key);
            JoinPart joinPart = manager.search(key);
            if (joinPart != null) {
                LOG.debug(key + " FOUND!!");
                byte[] joinPartAsBinary = joinPart.toBinaryForm();
                toServer.write(getIntInUtf8(joinPartAsBinary.length).getBytes("UTF-8"));
                toServer.write(joinPartAsBinary);
            } else {
                LOG.debug(key + " NOT FOUND!!");
                toServer.write(new byte[1]);
            }
            toServer.flush();
        } catch (IOException e) {
            LOG.warn("Can't communicate with central", e);
        }
    }

    private String decodeStringWithInteger(String s) {
        StringBuffer buffer = new StringBuffer(s);
        while (buffer.substring(0, 1).equals("0") && buffer.length() > 1) {
            buffer.delete(0, 1);
        }
        return buffer.toString();
    }

    private String getIntInUtf8(int i) {
        StringBuffer buffer = new StringBuffer();
        String integer = Integer.toString(i);
        buffer.append(integer);
        while (buffer.length() < org.spbgu.pmpu.athynia.common.CommunicationConstants.INTEGER_LENGTH_IN_BYTES_IN_UTF8) {
            buffer.insert(0, "0");
        }
        return buffer.toString();
    }
}
