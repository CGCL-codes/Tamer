package securus.services;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author e.dorofeev
 */
public class Attachment {

    private final InputStream in;

    private final String name;

    public Attachment(InputStream in, String path) {
        this.in = in;
        int i = path.lastIndexOf('/');
        if (i != -1) {
            path = path.substring(i + 1);
        }
        this.name = path;
    }

    public InputStream getInputStream() {
        return in;
    }

    public void close() throws IOException {
        in.close();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
