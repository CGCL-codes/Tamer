package ch.mye.nmapi.lib.handler;

import ch.mye.nmapi.lib.element.MessageElement;
import ch.mye.nmapi.lib.exception.NetconfException;

/**
 *
 * @author luk
 */
public interface NetconfHandler<T extends MessageElement> {

    public String getXml(T messageElement);

    public T getElement(String xml) throws NetconfException;
}
