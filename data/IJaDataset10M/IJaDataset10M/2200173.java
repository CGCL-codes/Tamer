package com.jedox.etl.components.load;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jedox.etl.components.config.load.FileConfigurator;
import com.jedox.etl.core.component.InitializationException;
import com.jedox.etl.core.component.RuntimeException;
import com.jedox.etl.core.connection.IConnection;
import com.jedox.etl.core.connection.IFileConnection;
import com.jedox.etl.core.load.Load;
import com.jedox.etl.core.util.IWriter;

public class FileLoad extends Load {

    private static final Log log = LogFactory.getLog(FileLoad.class);

    public FileLoad() {
        setConfigurator(new FileConfigurator());
    }

    public FileConfigurator getConfigurator() {
        return (FileConfigurator) super.getConfigurator();
    }

    protected String getEncoding() throws RuntimeException {
        if (getConnection() != null) {
            return getConnection().getEncoding();
        }
        return null;
    }

    public IFileConnection getConnection() throws RuntimeException {
        IConnection connection = super.getConnection();
        if ((connection != null) && (connection instanceof IFileConnection)) {
            IFileConnection c = (IFileConnection) connection;
            c.setWritable(true);
            return c;
        }
        throw new RuntimeException("File connection is needed for source " + getName() + ".");
    }

    private void write(IWriter writer) throws RuntimeException {
        writer.write(getProcessor());
        log.info("Lines written to file " + getConnection().getDatabase() + ": " + writer.getLinesOut());
    }

    @Override
    public void execute() {
        if (isExecutable()) {
            try {
                log.info("Starting File load " + getName());
                switch(getMode()) {
                    case CREATE:
                        {
                            write(getConnection().getWriter(getName(), false));
                            break;
                        }
                    case ADD:
                        {
                            write(getConnection().getWriter(getName(), true));
                            break;
                        }
                    case INSERT:
                        {
                            write(getConnection().getWriter(getName(), true));
                            break;
                        }
                    case UPDATE:
                        {
                            write(getConnection().getWriter(getName(), false));
                            break;
                        }
                    default:
                        {
                            log.error("Unsupported mode " + getMode().toString() + " in load " + getName() + " of type File.");
                        }
                }
            } catch (Exception e) {
                log.error("Failed to write to file: " + e.getMessage());
                log.debug("", e);
            }
            log.info("Finished load " + getName());
        }
    }

    public void test() throws RuntimeException {
        super.test();
        if (hasConnection()) getConnection().test();
    }

    public void init() throws InitializationException {
        super.init();
    }
}
