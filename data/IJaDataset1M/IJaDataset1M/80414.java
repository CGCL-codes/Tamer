package net.taylor.portal.theme;

import java.net.URL;
import net.taylor.jsf.ClasspathResourceResolver;
import com.sun.facelets.impl.ResourceResolver;

public class DatabaseResourceResolver extends ClasspathResourceResolver implements ResourceResolver {

    @Override
    public URL resolveUrl(String resource) {
        if (Util.faceletExists(resource)) {
            return Util.createFacelectUrl(resource);
        } else {
            return super.resolveUrl(resource);
        }
    }
}
