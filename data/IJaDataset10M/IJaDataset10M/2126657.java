package org.red5.io.flv;

import org.red5.io.IStreamableFileService;
import org.red5.io.object.Deserializer;
import org.red5.io.object.Serializer;

/**
 * A FLVService sets up the service and hands out FLV objects to 
 * its callers
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Dominick Accattato (daccattato@gmail.com)
 * @author Luke Hubbard, Codegent Ltd (luke@codegent.com)
 */
public interface IFLVService extends IStreamableFileService {

    /**
	 * Sets the serializer
	 * 
	 * @param serializer
	 */
    public void setSerializer(Serializer serializer);

    /**
	 * Sets the deserializer
	 * 
	 * @param deserializer
	 */
    public void setDeserializer(Deserializer deserializer);
}
