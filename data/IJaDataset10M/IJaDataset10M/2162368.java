package beantable;

import java.beans.*;
import java.io.*;
import java.net.*;

public class XMLBeanInfo extends DelegatingBeanInfo implements ExceptionListener {

    public XMLBeanInfo(final URL url) throws IOException {
        super();
        if (url == null) {
            throw new IllegalArgumentException();
        }
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(url.openStream());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
   * @deprecated The stream is closed by this constructor.
   */
    public XMLBeanInfo(final BufferedInputStream stream) throws IOException {
        super();
        try {
            this.init(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private final void init(final BufferedInputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException();
        }
        final XMLDecoder decoder = new XMLDecoder(stream);
        decoder.setExceptionListener(this);
        final Object object = decoder.readObject();
        if (object instanceof BeanInfo) {
            this.setDelegate((BeanInfo) object);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void exceptionThrown(final Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }
}
