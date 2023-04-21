package net.disy.ogc.wps.v_1_0_0.converter;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.apache.commons.lang.Validate;

public class JAXBElementTypeConverter<T> extends AbstractConverter<JAXBElement<? extends T>, T> {

    private final Class<T> destinationClass;

    private final QName name;

    public JAXBElementTypeConverter(Class<T> destinationClass, QName name) {
        super();
        Validate.notNull(destinationClass);
        Validate.notNull(name);
        this.destinationClass = destinationClass;
        this.name = name;
    }

    public QName getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<JAXBElement<? extends T>> getSourceClass() {
        final Class c = JAXBElement.class;
        return c;
    }

    @Override
    public Class<T> getDestinationClass() {
        return destinationClass;
    }

    @Override
    protected T convertToNotNull(JAXBElement source) {
        return (T) source.getValue();
    }

    @Override
    protected JAXBElement convertFromNotNull(T source) {
        return new JAXBElement<T>(getName(), getDestinationClass(), source);
    }
}
