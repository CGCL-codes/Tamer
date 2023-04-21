package com.armatiek.infofuze.stream;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.input.ProxyInputStream;
import org.apache.commons.io.input.TeeInputStream;
import com.armatiek.infofuze.config.Config;
import com.armatiek.infofuze.config.Definitions;

/**
 * 
 * @author Maarten Kroon
 */
public class RepeatableInputStream extends ProxyInputStream {

    protected DeferredFileOutputStream dfos;

    protected boolean isClosed = false;

    public RepeatableInputStream(InputStream proxy) throws IOException {
        super(proxy);
        int deferredOutputStreamThreshold = Config.getInstance().getDeferredOutputStreamThreshold();
        dfos = new DeferredFileOutputStream(deferredOutputStreamThreshold, Definitions.PROJECT_NAME, "." + Definitions.TMP_EXTENSION);
        super.in = new TeeInputStream(proxy, dfos, true);
    }

    @Override
    public void close() throws IOException {
        super.close();
        isClosed = true;
    }

    @Override
    protected void beforeRead(int n) throws IOException {
        if (isClosed) {
            in = dfos.getDeferredInputStream();
            isClosed = false;
        }
        super.beforeRead(n);
    }
}
