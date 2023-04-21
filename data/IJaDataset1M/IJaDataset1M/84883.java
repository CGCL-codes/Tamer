package cmspider.utilities.executor;

import java.io.InputStream;

public abstract class PipeHandler extends Thread {

    abstract void startListen(InputStream is);
}
