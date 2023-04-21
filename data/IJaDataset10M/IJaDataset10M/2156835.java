package net.stickycode.coercion.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import net.stickycode.resource.ResourceLocation;
import net.stickycode.resource.ResourceNotFoundException;
import net.stickycode.resource.ResourceProtocol;
import net.stickycode.stereotype.component.StickyExtension;

@StickyExtension
public class DummyProtocol implements ResourceProtocol {

    @Override
    public boolean canResolve(String protocol) {
        return "dummy".equals(protocol);
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) throws ResourceNotFoundException {
        try {
            return new ByteArrayInputStream(resourceLocation.getPath().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
