package flex.messaging.io.amf.translator.decoder;

/**
 * Decodes an ActionScript native object to a Java native object.
 *
 * @exclude
 */
public class NativeDecoder extends ActionScriptDecoder {

    public Object decodeObject(Object shell, Object encodedObject, Class desiredClass) {
        return encodedObject;
    }
}
