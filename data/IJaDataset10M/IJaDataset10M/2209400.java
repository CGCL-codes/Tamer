package ru.adv.cache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.util.Assert;
import ru.adv.util.ErrorCodeException;
import ru.adv.util.InputOutput;
import ru.adv.util.Stream;

public class BLOBCacheLoader extends AbstractCacheLoader {

    @Override
    public boolean isGzippedStore() {
        return false;
    }

    public Object store(Object key, Object data) throws IOException {
        Assert.isInstanceOf(InputOutput.class, data);
        InputOutput io = (InputOutput) data;
        String handle = createHandle(key);
        final FileOutputStream fileOutputStream = new FileOutputStream(handle);
        BufferedOutputStream stream = new BufferedOutputStream(fileOutputStream);
        try {
            Stream.readTo(io.getBufferedInputStream(), stream);
        } catch (ErrorCodeException e) {
            throw new IOException(e.getMessage());
        } finally {
            stream.close();
        }
        return handle;
    }

    public File load(Object handle) {
        return new File(handle.toString());
    }
}
