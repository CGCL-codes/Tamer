package flex.messaging.io.amf.translator.decoder;

import flex.messaging.io.TypeMarshallingContext;
import java.util.Collection;
import java.lang.reflect.Array;

/**
 * A special version of CollectionDecoder than runs a new decoder
 * over every item before it is added to the decoded collection
 * to restore references to any instances that may have undergone
 * translation to another type.
 *
 * Note that tracking references is an expensive exercise and will
 * scale poorly with larger amounts of data.
 *
 * @author Peter Farland
 *
 * @exclude
 */
public class ReferenceAwareCollectionDecoder extends CollectionDecoder {

    /**
     * We want to iterate through all of the contents of the Collection
     * to check for references that need to be restored and translations
     * that may still be required (in the case of typed ASObjects).
     *
     * Return false to ensure we create a new collection and copy
     * all of the contents.
     */
    protected boolean isSuitableCollection(Object encodedObject, Class desiredClass) {
        return false;
    }

    protected Collection decodeCollection(Collection shell, Object encodedObject) {
        Collection decodedCollection = (Collection) shell;
        Object decodedObject = null;
        Object obj = null;
        TypeMarshallingContext context = TypeMarshallingContext.getTypeMarshallingContext();
        ActionScriptDecoder decoder = null;
        if (encodedObject instanceof String) {
            encodedObject = ((String) encodedObject).toCharArray();
        } else {
            if (encodedObject instanceof Collection) {
                encodedObject = ((Collection) encodedObject).toArray();
            }
            context.getKnownObjects().put(encodedObject, shell);
        }
        int len = Array.getLength(encodedObject);
        for (int i = 0; i < len; i++) {
            obj = Array.get(encodedObject, i);
            if (obj == null) {
                decodedCollection.add(null);
            } else {
                Object ref = null;
                if (canUseByReference(obj)) ref = context.getKnownObjects().get(obj);
                if (ref == null) {
                    decoder = DecoderFactory.getReferenceAwareDecoder(obj, obj.getClass());
                    decodedObject = decoder.decodeObject(obj, obj.getClass());
                    if (canUseByReference(decodedObject)) {
                        context.getKnownObjects().put(obj, decodedObject);
                    }
                } else {
                    decodedObject = ref;
                }
                decodedCollection.add(decodedObject);
            }
        }
        return decodedCollection;
    }
}
