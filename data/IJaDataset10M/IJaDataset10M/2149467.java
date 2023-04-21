package gg.arkehion.store.hadoopimpl;

import gg.arkehion.configuration.Configuration;
import gg.arkehion.exceptions.ArFileException;
import gg.arkehion.exceptions.ArFileWormException;
import gg.arkehion.exceptions.ArUnvalidIndexException;
import gg.arkehion.store.ArkDirConstants;
import gg.arkehion.store.ArkDualCasFileInterface;
import gg.arkehion.store.ArkStoreInterface;
import gg.arkehion.store.abstimpl.ArkAbstractDualDoc;
import gg.arkehion.store.abstimpl.ArkAbstractUpdateTimeQueue;
import gg.arkehion.store.fileimpl.ArkFsDirFunction;
import goldengate.common.digest.FilesystemBasedDigest;
import goldengate.common.logging.GgInternalLogger;
import goldengate.common.logging.GgInternalLoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Version using Hadoop DFS
 * 
 * @author frederic bregier
 * 
 */
public class ArkHadoopDualDoc extends ArkAbstractDualDoc {

    /**
     * Internal Logger
     */
    private static final GgInternalLogger logger = GgInternalLoggerFactory.getLogger(ArkHadoopDualDoc.class);

    /**
     * associated Storage
     */
    protected ArkHadoopStore storage = null;

    /**
     * Full parent path
     */
    protected Path parentPath = null;

    /**
     * Full path
     */
    protected Path realPath = null;

    /**
     * Metadata path
     */
    protected Path metaPath = null;

    /**
     * Associated Distributed FS
     */
    protected FileSystem dfs = null;

    /**
     * Create a new Document according to the storage and its did (unique id for
     * the document).
     * 
     * The Document is marked as Ready.
     * 
     * @param storage
     *            associated Storage
     * @param did
     *            Document Id
     * @throws ArUnvalidIndexException
     */
    public ArkHadoopDualDoc(ArkStoreInterface storage, long did) throws ArUnvalidIndexException {
        super(did);
        if (storage == null) {
            throw new ArUnvalidIndexException("Storage is not defined");
        }
        this.storage = (ArkHadoopStore) storage;
        dfs = this.storage.dfs;
        String temp = this.storage.legacy.getBasePath() + this.storage.getObjectGlobalPath();
        parentPath = new Path(temp + this.getObjectGlobalPathWoBasename());
        realPath = new Path(temp + this.getObjectGlobalPath());
        metaPath = new Path(temp + this.getObjectGlobalPath() + ArkDirConstants.METAEXT);
        this.isReady = true;
    }

    @Override
    protected final boolean prepareDirectory() throws ArFileException {
        return ArkFsDirFunction.hadoopDirFunction.createDir(dfs, parentPath);
    }

    @Override
    public final ArkHadoopStore getStore() {
        return storage;
    }

    @Override
    public final ArkHadoopLegacy getLegacy() {
        return storage.getLegacy();
    }

    @Override
    public final void clear() {
        super.clear();
        parentPath = null;
        realPath = null;
    }

    @Override
    public final long length() throws ArUnvalidIndexException {
        if (!this.isReady) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        try {
            if (!dfs.exists(realPath)) {
                throw new ArUnvalidIndexException("Doc does not exist");
            }
            FileStatus status = dfs.getFileStatus(realPath);
            if (status.isFile()) {
                return status.getLen();
            }
        } catch (FileNotFoundException e) {
            throw new ArUnvalidIndexException("File not found", e);
        } catch (IOException e) {
            throw new ArUnvalidIndexException("Issue while accessing the file", e);
        }
        throw new ArUnvalidIndexException("Incorrect Path");
    }

    @Override
    public final boolean canRead() throws ArUnvalidIndexException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        if (isInReading() || isInWriting()) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        if (metaExists()) {
            return ArkFsDirFunction.hadoopDirFunction.canRead(dfs, realPath);
        }
        return false;
    }

    @Override
    protected final boolean realCanWrite() throws ArUnvalidIndexException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        if (isInReading() || isInWriting()) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        try {
            prepareDirectory();
        } catch (ArFileException e) {
            throw new ArUnvalidIndexException("Store unready", e);
        }
        if (ArkFsDirFunction.hadoopDirFunction.canWrite(dfs, parentPath)) {
            if (metaExists()) {
                logger.debug("Metadata already exists");
                return false;
            }
            if (exists()) {
                logger.debug("Metadata does not exist but Document already exists");
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected final boolean metaExists() throws ArUnvalidIndexException {
        return ArkFsDirFunction.hadoopDirFunction.isFile(dfs, metaPath);
    }

    @Override
    public final boolean exists() throws ArUnvalidIndexException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        return metaExists() && ArkFsDirFunction.hadoopDirFunction.isFile(dfs, realPath);
    }

    @Override
    protected final boolean updateLastTime(long time) {
        try {
            dfs.setTimes(storage.path, time, -1);
        } catch (IOException e) {
        }
        return true;
    }

    @Override
    protected final boolean realDelete() {
        boolean deleted = false;
        try {
            deleted = dfs.delete(metaPath, false);
        } catch (FileNotFoundException e) {
            deleted = true;
        } catch (IOException e) {
            deleted = false;
        }
        try {
            deleted |= dfs.delete(realPath, false);
        } catch (FileNotFoundException e) {
            deleted = true;
        } catch (IOException e) {
        }
        position = 0;
        return deleted;
    }

    @Override
    public final void copy(ArkDualCasFileInterface docnew) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        if ((!this.isReady) || (!docnew.isReady())) {
            throw new ArUnvalidIndexException("No Doc are ready");
        }
        if (!this.canRead()) {
            throw new ArFileException("Source doc cannot be read");
        }
        if (docnew instanceof ArkHadoopDualDoc) {
            ArkHadoopDualDoc docnew2 = (ArkHadoopDualDoc) docnew;
            ArkHadoopStore targetStore = docnew2.getStore();
            ArkHadoopLegacy targetLegacy = targetStore.getLegacy();
            ArkHadoopStore srcStore = this.getStore();
            ArkHadoopLegacy srcLegacy = srcStore.getLegacy();
            if (this.equals(docnew2)) {
                throw new ArFileException("Target and Source are identical");
            }
            if (!docnew2.canWrite()) {
                throw new ArFileException("Target doc already exists");
            }
            try {
                ArkFsDirFunction.hadoopDirFunction.copyPathToPathInternal(srcLegacy, metaPath, targetLegacy, docnew2.metaPath, false);
                ArkFsDirFunction.hadoopDirFunction.copyPathToPathInternal(srcLegacy, realPath, targetLegacy, docnew2.realPath, true);
                docnew2.DKey = DKey;
                ArkAbstractUpdateTimeQueue.add(docnew2);
            } catch (ArFileException e) {
                docnew2.deleteInternal(false);
                throw e;
            }
        } else {
            ArkHadoopStore srcStore = this.getStore();
            ArkHadoopLegacy srcLegacy = srcStore.getLegacy();
            if (this.equals(docnew)) {
                throw new ArFileException("Target and Source are identical");
            }
            if (!docnew.canWrite()) {
                throw new ArFileException("Target doc already exists");
            }
            docnew.store(blocksize, readMetadata());
            InputStream inputStream = ArkFsDirFunction.hadoopDirFunction.getInputStreamInternal(srcLegacy, realPath, blocksize);
            DKey = docnew.write(inputStream);
            if (DKey == null) {
                logger.error("File not copied correctly but cannot undo");
                throw new ArFileException("Cannot get DKey");
            }
        }
    }

    @Override
    public final void setLoopIdUnique(ArkStoreInterface storage, long did) throws ArUnvalidIndexException {
        super.setLoopIdUnique(storage, did);
        this.storage = (ArkHadoopStore) storage;
        String temp = this.storage.legacy.getBasePath() + this.storage.getObjectGlobalPath();
        parentPath = new Path(temp + this.getObjectGlobalPathWoBasename());
        realPath = new Path(temp + this.getObjectGlobalPath());
        metaPath = new Path(temp + this.getObjectGlobalPath() + ArkDirConstants.METAEXT);
        this.isReady = true;
    }

    @Override
    protected final void writeMetadata(String metadata) throws ArFileException {
        FSDataOutputStream fsDataOutputStream = null;
        try {
            fsDataOutputStream = dfs.create(metaPath);
            fsDataOutputStream.writeBytes(metadata);
            fsDataOutputStream.hsync();
            fsDataOutputStream.close();
        } catch (IOException e) {
            deleteInternal(false);
            throw new ArFileException("Cannot write document", e);
        }
    }

    @Override
    public final String readMetadata() throws ArUnvalidIndexException, ArFileException {
        FSDataInputStream fsDataInputStream = null;
        try {
            fsDataInputStream = dfs.open(metaPath);
            byte[] bytes = new byte[blocksize];
            StringBuilder builder = new StringBuilder();
            while (true) {
                int nbRead = 0;
                int totalRead = 0;
                while (totalRead < this.blocksize) {
                    try {
                        nbRead = fsDataInputStream.read(bytes, totalRead, blocksize - totalRead);
                    } catch (IOException e) {
                        throw new ArFileException("Error while reading metadata", e);
                    }
                    if (nbRead == -1) {
                        break;
                    }
                    totalRead += nbRead;
                }
                if (nbRead == -1) {
                    if (totalRead == 0) {
                        break;
                    }
                    String newString = new String(bytes, 0, totalRead);
                    builder.append(newString);
                    break;
                } else {
                    String newString = new String(bytes, 0, totalRead);
                    builder.append(newString);
                    continue;
                }
            }
            fsDataInputStream.close();
            fsDataInputStream = null;
            return builder.toString();
        } catch (IOException e) {
            throw new ArFileException("Cannot read metadata", e);
        } finally {
            if (fsDataInputStream != null) {
                try {
                    fsDataInputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public final String storeFromLocalFile(String srcFile, String metadata) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc unready");
        }
        if (!canWrite()) {
            throw new ArFileWormException("Target doc already exists");
        }
        FilesystemBasedDigest digest = null;
        try {
            digest = new FilesystemBasedDigest(Configuration.algoMark);
        } catch (NoSuchAlgorithmException e1) {
            throw new ArFileException("Cannot create Digest", e1);
        }
        FSDataOutputStream fsDataOutputStream = null;
        CipherOutputStream cipherOutputStream = null;
        FileInputStream inputStream = null;
        try {
            writeMetadata(metadata);
            if (getLegacy().isEncrypted()) {
                try {
                    fsDataOutputStream = dfs.create(realPath, false, blocksize);
                } catch (IOException e) {
                    throw new ArFileException("File cannot be written", e);
                }
                Cipher cipherOut = getLegacy().getKeyObject().toCrypt();
                if (cipherOut == null) {
                    throw new ArFileException("CIpher uncorrect");
                }
                cipherOutputStream = new CipherOutputStream(fsDataOutputStream, cipherOut);
                position = 0;
                inputStream = new FileInputStream(srcFile);
                byte[] bytes = new byte[blocksize];
                int read = 0;
                while (read >= 0) {
                    read = inputStream.read(bytes);
                    if (read > 0) {
                        cipherOutputStream.write(bytes, 0, read);
                        digest.Update(bytes, 0, read);
                        position += read;
                    }
                }
                inputStream.close();
                inputStream = null;
                cipherOutputStream.flush();
                cipherOutputStream.close();
                cipherOutputStream = null;
                fsDataOutputStream = null;
            } else {
                digest = null;
                Path src = new Path(srcFile);
                dfs.copyFromLocalFile(src, realPath);
                File file = new File(srcFile);
                position = file.length();
            }
        } catch (IOException e) {
            deleteInternal(false);
            throw new ArFileException("Cannot write document", e);
        } finally {
            if (fsDataOutputStream != null) {
                try {
                    fsDataOutputStream.close();
                } catch (IOException e) {
                }
            }
            if (cipherOutputStream != null) {
                try {
                    cipherOutputStream.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        ArkAbstractUpdateTimeQueue.add(this);
        if (digest != null) {
            DKey = FilesystemBasedDigest.getHex(digest.Final());
        } else {
            DKey = this.getDKey();
        }
        if (DKey == null) {
            this.deleteInternal(false);
            throw new ArFileException("Cannot get DKey");
        }
        return DKey;
    }

    @Override
    public final void copyToLocalFiles(String dest, String destmetadata) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        Path dst = new Path(destmetadata);
        try {
            dfs.copyToLocalFile(metaPath, dst);
        } catch (FileNotFoundException e) {
            throw new ArUnvalidIndexException("Cannot find document", e);
        } catch (IOException e) {
            throw new ArUnvalidIndexException("Cannot copy document", e);
        }
        copyToLocalFile(dest);
    }

    @Override
    public final void copyToLocalFile(String destFile) throws ArUnvalidIndexException, ArFileException, ArFileWormException {
        if (getLegacy().isEncrypted()) {
            FSDataInputStream fsDataInputStream = null;
            try {
                fsDataInputStream = dfs.open(realPath, blocksize);
            } catch (IOException e) {
                throw new ArFileException("File cannot be written", e);
            }
            Cipher cipherIn = getLegacy().getKeyObject().toDecrypt();
            if (cipherIn == null) {
                throw new ArFileException("CIpher uncorrect");
            }
            CipherInputStream cipherInputStream = new CipherInputStream(fsDataInputStream, cipherIn);
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(destFile);
                byte[] bytes = new byte[blocksize];
                int read = 0;
                while (read >= 0) {
                    read = cipherInputStream.read(bytes);
                    if (read > 0) {
                        outputStream.write(bytes, 0, read);
                    }
                }
                cipherInputStream.close();
                cipherInputStream = null;
                outputStream.flush();
                outputStream.close();
                outputStream = null;
            } catch (FileNotFoundException e) {
                throw new ArFileException("Error while copying", e);
            } catch (IOException e) {
                throw new ArFileException("Error while copying", e);
            } finally {
                if (cipherInputStream != null) {
                    try {
                        cipherInputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            Path dst = new Path(destFile);
            try {
                dfs.copyToLocalFile(realPath, dst);
            } catch (FileNotFoundException e) {
                throw new ArUnvalidIndexException("Cannot find document", e);
            } catch (IOException e) {
                throw new ArUnvalidIndexException("Cannot copy document", e);
            }
        }
    }

    @Override
    public final long getTime() throws ArUnvalidIndexException, ArFileException {
        try {
            return dfs.getFileStatus(realPath).getModificationTime();
        } catch (IOException e) {
            throw new ArFileException("Cannot access time", e);
        }
    }

    @Override
    protected final Object getTarget() {
        return realPath;
    }

    @Override
    protected final Object getMetaTarget() {
        return metaPath;
    }

    @Override
    public final String getDKey() throws ArFileException {
        if (DKey == null) {
            DKey = ArkHadoopDirFunction.hadoopDirFunction.computeStringMark(getLegacy(), getTarget());
        }
        return DKey;
    }
}
