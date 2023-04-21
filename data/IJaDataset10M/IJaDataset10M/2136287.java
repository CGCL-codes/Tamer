package ch.squix.nataware.directory;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import ch.squix.nataware.security.Crypto;
import ch.squix.nataware.serializer.ProtocolBuffersSerializer;
import ch.squix.nataware.util.Util;
import com.google.protobuf.Message;

/**
 * @author Carl
 */
public class RestDirectoryServiceClient implements IDirectoryServiceClient {

    private String serviceAddress;

    public RestDirectoryServiceClient(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public <K extends Message, V extends Message> V get(K key, V valuePrototype) throws Exception {
        String url = getUrlFromKey(key, valuePrototype);
        final ProtocolBuffersSerializer<V> valueSerializer = new ProtocolBuffersSerializer<V>(valuePrototype);
        Client client = createClient();
        Response response = client.get(url);
        Representation responseRepresentation = response.getEntity();
        V value = valueSerializer.deserialize(responseRepresentation.getStream());
        return value;
    }

    private Client createClient() {
        Client client = new Client(Protocol.HTTP);
        return client;
    }

    public <K extends Message, V extends Message> void put(K key, V value) throws Exception {
        final ProtocolBuffersSerializer<V> valueSerializer = new ProtocolBuffersSerializer<V>(value);
        final byte[] valueData = valueSerializer.serialize(value);
        String url = getUrlFromKey(key, value);
        Client client = createClient();
        Request request = new Request(Method.PUT, url);
        Representation representation = new InputRepresentation(new ByteArrayInputStream(valueData), MediaType.APPLICATION_OCTET_STREAM);
        representation.setMediaType(MediaType.APPLICATION_OCTET_STREAM);
        request.setEntity(representation);
        Response response = client.handle(request);
        if (!response.getStatus().isSuccess()) {
            throw new Exception("Put failed for url:" + url);
        }
    }

    private String getUrlFromKey(Message key, Message valuePrototype) throws Exception {
        byte[] keyData = serializeElement(key);
        keyData = addClassDataToKey(key, valuePrototype, keyData);
        String keyUrl = encodeKey(keyData);
        return serviceAddress + keyUrl;
    }

    /**
     * @param <K>
     * @param messageToSerialize
     * @param keyPrototype
     * @return
     * @throws IOException
     */
    private static <K extends Message> byte[] serializeElement(K messageToSerialize) throws IOException {
        final ProtocolBuffersSerializer<K> keySerializer = new ProtocolBuffersSerializer<K>(messageToSerialize);
        final byte[] keyData = keySerializer.serialize(messageToSerialize);
        return keyData;
    }

    /**
     * @param <V>
     * @param <K>
     * @param keyPrototype
     * @param valuePrototype
     * @param keyData
     * @return
     */
    private static <V, K> byte[] addClassDataToKey(K keyPrototype, V valuePrototype, byte[] keyData) {
        keyData = Util.concatByteArrays(keyData, keyPrototype.getClass().getName().getBytes());
        keyData = Util.concatByteArrays(keyData, valuePrototype.getClass().getName().getBytes());
        return keyData;
    }

    /**
     * @param key
     * @return
     * @throws Exception
     */
    private static String encodeKey(byte[] key) throws Exception {
        byte[] keyHash = Crypto.CreateHash(key);
        return Util.Base64Encode(keyHash);
    }
}
