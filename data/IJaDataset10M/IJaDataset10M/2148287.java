package org.example;

import nl.wijsmullerbros.gs.StreamProxy;

/**
 * @author bwijsmuller
 *
 */
public interface RemoteService {

    StreamProxy createOutputStreamProxy();

    StreamProxy createInputStreamProxy(String filePath);
}
