package org.nexopenframework.xml.binding;

import org.nexopenframework.xml.XmlException;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class MarshallingException extends XmlException {

    private static final long serialVersionUID = 1L;

    public MarshallingException() {
        super();
    }

    public MarshallingException(String message) {
        super(message, "Serialization");
    }

    public MarshallingException(Throwable thr) {
        super(thr, "Serialization");
    }

    public MarshallingException(String message, Throwable thr) {
        super(message, thr, "Serialization");
    }
}
