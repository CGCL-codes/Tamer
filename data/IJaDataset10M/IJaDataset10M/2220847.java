package org.resource;

import java.io.IOException;
import net.sf.thyvin.core.res.IResource;

public interface ResourceLoader {

    public Object loadResource(IResource res) throws IOException, Exception;
}
