package fmpp.dataloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import fmpp.Engine;
import fmpp.tdd.DataLoader;

/**
 * Ancestor of data loaders that create the result based on a file.
 * The first argument of the data loader will be the path of the file.
 * If the path is a realtive path, then it will be realative to the data root
 * directory (an engine level setting), or if data root is null, then relative
 * to the working directory (OS facility). The path can use slash (/) instead
 * of the OS specific separator char.
 */
public abstract class FileDataLoader implements DataLoader {

    protected Engine engine;

    protected List args;

    protected File dataFile;

    public Object load(Engine engine, List args) throws Exception {
        this.engine = engine;
        this.args = args;
        if (args.size() < 1) {
            throw new IllegalArgumentException("At least 1 argument (file name) needed");
        }
        Object obj = args.get(0);
        if (!(obj instanceof String)) {
            throw new IllegalArgumentException("The 1st argument (file name) must be a string.");
        }
        String path = (String) obj;
        path = path.replace('/', File.separatorChar);
        dataFile = new File(path);
        if (!dataFile.isAbsolute()) {
            dataFile = new File(engine.getDataRoot(), path);
        }
        InputStream in = new FileInputStream(dataFile);
        try {
            return load(in);
        } finally {
            in.close();
        }
    }

    /**
     * <code>FileDataLoader</code> subclasess override this method to parse
     * the file. 
     */
    protected abstract Object load(InputStream data) throws Exception;
}
