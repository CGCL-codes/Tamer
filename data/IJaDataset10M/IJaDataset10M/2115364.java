package nl.hoepsch.json.transformer;

public interface MarshallingContext {

    void setMaxDepth(Integer depth);

    void pushParent(String parent);

    Object popParent();

    Boolean mayEmit();

    <T> Transformer<T> getTransformer(Class<T> propertyClass);

    Boolean mustEmit(String propertyName, Object propertyValue);

    Boolean emitNullValues();

    MarshallingContext clone();
}
