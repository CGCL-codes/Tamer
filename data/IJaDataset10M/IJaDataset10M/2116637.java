package scap.check.content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public class DefaultHrefResolver implements HrefResolver {

    public DefaultHrefResolver() {
    }

    protected FileSourceContent newFileSourceContent(File file, HrefResolver resolver) throws IOException {
        return new FileSourceContent(file, resolver);
    }

    protected URLSourceContent newURLSourceContent(URI uri, HrefResolver resolver) {
        return new URLSourceContent(uri, resolver);
    }

    @Override
    public SourceContext resolveRelative(URI uri, String href, SourceContext context) throws MalformedURLException, FileNotFoundException, IOException {
        URI newUri = uri.resolve(href);
        return newURLSourceContent(newUri, this);
    }
}
