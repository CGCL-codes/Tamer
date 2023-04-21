package tm.utilities;

import java.awt.image.BufferedImage;
import java.io.Reader;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.IOException;
import javax.imageio.ImageIO;

public class URLFileSource extends FileSource {

    protected URL base;

    public URLFileSource(URL base) {
        this.base = base;
    }

    Reader fileNameToReader(String fileName) throws IOException {
        URL url = null;
        try {
            url = new URL(base, fileName);
        } catch (MalformedURLException ex) {
            throw new IOException("Malformed URL " + ex.getMessage());
        }
        InputStream inStream = url.openStream();
        return new InputStreamReader(inStream);
    }

    @Override
    public BufferedImage readImage(String fileName) throws IOException {
        URL url = null;
        try {
            url = new URL(base, fileName);
        } catch (MalformedURLException ex) {
            throw new IOException("Malformed URL " + ex.getMessage());
        }
        return ImageIO.read(url);
    }
}
