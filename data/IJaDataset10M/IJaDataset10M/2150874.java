package org.jtools.config.protocol;

import org.jpattern.xml.QName;
import org.jtools.config.Configure;

/**
 *
 * @author  Rainer
 */
public interface ConfigProtocol {

    ConfigProtocol getNamespaceProtocol(String uri);

    Object onBeginChildElement(Object parent, QName name, boolean[] invoked) throws Exception;

    void onEndChildElement(Object parent, QName name, Object element, boolean invoked) throws Exception;

    boolean onSetAttribute(Object element, QName name, String value) throws Exception;

    boolean onAddText(Object element, StringBuilder value) throws Exception;

    boolean onSetText(Object element, StringBuilder value) throws Exception;

    Configure<?> getConfigLoader();
}
