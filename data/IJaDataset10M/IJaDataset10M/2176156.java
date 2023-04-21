package nuts.exts.vfs.ndfs;

import java.io.Serializable;
import java.util.Collection;
import nuts.core.orm.dao.DataAccessClient;
import nuts.core.orm.dao.DataAccessSession;
import nuts.exts.vfs.ndfs.dao.NdfsData;
import nuts.exts.vfs.ndfs.dao.NdfsDataDAO;
import nuts.exts.vfs.ndfs.dao.NdfsDataExample;
import nuts.exts.vfs.ndfs.dao.NdfsFile;
import nuts.exts.vfs.ndfs.dao.NdfsFileDAO;
import nuts.exts.vfs.ndfs.dao.NdfsFileExample;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;

/**
 * A Ndfs File System
 */
public abstract class NdfsFileSystem extends AbstractFileSystem implements Serializable {

    private DataAccessClient dataAccessClient;

    protected int blockSize = 512 * 1024;

    /**
	 * @param rootName
	 * @param fileSystemOptions
	 */
    protected NdfsFileSystem(FileName rootName, FileSystemOptions fileSystemOptions, DataAccessClient dataAccessClient) {
        super(rootName, null, fileSystemOptions);
        this.dataAccessClient = dataAccessClient;
    }

    public abstract NdfsFileDAO<NdfsFile, NdfsFileExample> createFileDAO(DataAccessSession das);

    public abstract NdfsDataDAO<NdfsData, NdfsDataExample> createDataDAO(DataAccessSession das);

    /**
	 * @return the dataAccessClient
	 */
    public DataAccessClient getDataAccessClient() {
        return dataAccessClient;
    }

    /**
	 * @param dataAccessClient the dataAccessClient to set
	 */
    public void setDataAccessClient(DataAccessClient dataAccessClient) {
        this.dataAccessClient = dataAccessClient;
    }

    /**
	 * @return the blockSize
	 */
    public int getBlockSize() {
        return blockSize;
    }

    /**
	 * @param blockSize the blockSize to set
	 */
    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    protected FileObject createFile(FileName name) throws Exception {
        NdfsFileObject file = new NdfsFileObject(name, this);
        return file;
    }

    protected void addCapabilities(Collection caps) {
        caps.addAll(NdfsFileProvider.capabilities);
    }
}
