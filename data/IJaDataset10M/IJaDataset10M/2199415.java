package pipe4j.pipe.std;

import java.io.IOException;
import java.io.InputStream;
import pipe4j.pipe.SimpleStreamDecoratorPipe;

/**
 * Feeds pipeline from stdin.
 * 
 * @author bbennett
 */
public class Stdin extends SimpleStreamDecoratorPipe {

    @Override
    protected InputStream getDecoratedInputStream(InputStream inputStream) throws IOException {
        return System.in;
    }
}
