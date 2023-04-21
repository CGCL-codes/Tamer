package dk.itu.maisun.wamp.registry.jaxb;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/** The customized field serializer for automatic activity. */
public final class AutomaticActivity_CustomFieldSerializer {

    /** Deserialize an automatic activity.
	 * @param streamReader the stream reader
	 * @param instance the instance
	 * @throws SerializationException the serialization exception */
    public static void deserialize(SerializationStreamReader streamReader, AutomaticActivity instance) throws SerializationException {
    }

    /** Instantiate an automatic activity.
	 * @param streamReader the stream reader
	 * @return the automatic activity
	 * @throws SerializationException the serialization exception */
    public static AutomaticActivity instantiate(SerializationStreamReader streamReader) throws SerializationException {
        AutomaticActivity result = new AutomaticActivity();
        result.setEndPoint(streamReader.readString());
        result.setName(streamReader.readString());
        result.setWSDL(streamReader.readString());
        Output output = new Output();
        output.setXpath(streamReader.readString());
        result.setOutput(output);
        SoapMessage msg = new SoapMessage();
        msg.setAny(streamReader.readString());
        result.setSoapMessage(msg);
        return result;
    }

    /** Serialize an automatic activity.
	 * @param streamWriter the stream writer
	 * @param instance the instance
	 * @throws SerializationException the serialization exception */
    public static void serialize(SerializationStreamWriter streamWriter, AutomaticActivity instance) throws SerializationException {
        streamWriter.writeString(instance.getEndPoint());
        streamWriter.writeString(instance.getName());
        streamWriter.writeString(instance.getWSDL());
        streamWriter.writeString(instance.getOutput().getXpath());
        streamWriter.writeString((String) instance.getSoapMessage().getAny());
    }
}
