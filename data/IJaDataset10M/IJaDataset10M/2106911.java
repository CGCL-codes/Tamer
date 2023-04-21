package org.brandao.brutos;

import org.brandao.brutos.io.ByteArrayResource;
import org.brandao.brutos.io.Resource;
import org.brandao.brutos.xml.AbstractXMLApplicationContext;

/**
 *
 * @author Brandao
 */
public class ByteArrayXMLApplicationContext extends AbstractXMLApplicationContext {

    private Resource[] resources;

    public ByteArrayXMLApplicationContext(byte[][] arrays, AbstractApplicationContext parent) {
        super(parent);
        resources = new Resource[arrays.length];
        for (int i = 0; i < arrays.length; i++) resources[i] = new ByteArrayResource(arrays[i]);
    }

    public ByteArrayXMLApplicationContext(byte[][] array) {
        this(array, null);
    }

    public ByteArrayXMLApplicationContext(byte[] array) {
        this(new byte[][] { array }, null);
    }

    public ByteArrayXMLApplicationContext(byte[] array, AbstractApplicationContext parent) {
        this(new byte[][] { array }, parent);
    }

    protected Resource[] getContextResources() {
        return resources;
    }
}
