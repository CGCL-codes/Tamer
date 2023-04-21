package org.bing.adapter.com.caucho.hessian.io;

/**
 * Factory for returning serialization methods.
 */
public abstract class AbstractSerializerFactory {

    /**
   * Returns the serializer for a class.
   *
   * @param cl the class of the object that needs to be serialized.
   *
   * @return a serializer object for the serialization.
   */
    public abstract Serializer getSerializer(Class cl) throws HessianProtocolException;

    /**
   * Returns the deserializer for a class.
   *
   * @param cl the class of the object that needs to be deserialized.
   *
   * @return a deserializer object for the serialization.
   */
    public abstract Deserializer getDeserializer(Class cl) throws HessianProtocolException;
}
