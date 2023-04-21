package org.dctmvfs.vfs.provider.dctm.client.serializing;

import java.io.InputStream;
import java.util.Map;

public interface AttributeDeserializer {

    /**
	 * Reads attributes from the given inputStream. The format of the inputstream must match the
	 * implementation. The given stream will be closed before this method returns. 
	 * @param stream the input stream
	 * @return a Map containing the attributes found in the stream
	 * @throws SerializerException
	 */
    public abstract Map deserialize(InputStream stream) throws SerializerException;
}
