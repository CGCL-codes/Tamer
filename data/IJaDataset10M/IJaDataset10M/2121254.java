package ch.iserver.ace.net.beep.profile;

import java.io.InputStream;

/**
 *
 */
public interface QueryProcessor {

    public byte[] process(InputStream input) throws Exception;
}
