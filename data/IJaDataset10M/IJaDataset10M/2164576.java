package org.middleheaven.io.repository.machine;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.middleheaven.io.FileNotFoundManagedException;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.AbstractManagedRepository;
import org.middleheaven.io.repository.ArrayManagedFilePath;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.watch.WatchService;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.classification.Classifier;
import org.middleheaven.util.collections.CollectionUtils;
import org.middleheaven.util.collections.TransformedCollection;

/**
 * 
 */
class MachineIOSystemManagedFileRepository extends AbstractManagedRepository implements ManagedFileRepository {

    private final File root;

    private final MachineFileSystemAcessStrategy accessStrategy;

    /**
	 * 
	 * Create a {@link MachineIOSystemManagedFileRepository} based on a file.
	 * If the file is null, this object repesents  ManagedFileRepository instead
	 * @param accessStrategy the accessStrategy in use.
	 * @param parent the parent file.
	 * @param root the pivot file.
	 */
    public MachineIOSystemManagedFileRepository(MachineFileSystemAcessStrategy accessStrategy, File root) {
        this.root = root;
        this.accessStrategy = accessStrategy;
    }

    ManagedFilePath pathForFile(File systemFile) {
        String[] parts;
        try {
            String separator = File.separator;
            if (separator.equals("\\")) {
                parts = StringUtils.split(systemFile.getCanonicalPath().replace('\\', '/'), "/");
            } else {
                parts = StringUtils.split(systemFile.getCanonicalPath(), File.separator);
            }
            return new ArrayManagedFilePath(this, parts[0], Arrays.copyOfRange(parts, 1, parts.length));
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }

    private boolean isDriveRoot(File file) {
        return file.getParentFile() == null;
    }

    ManagedFile resolveFile(File file) {
        if (file == null) {
            return null;
        }
        file = file.getAbsoluteFile();
        if (isDriveRoot(file)) {
            File[] roots = File.listRoots();
            for (File root : roots) {
                if (root.equals(file)) {
                    return new FileIOManagedFileAdapter(this, root, pathForFile(file));
                }
            }
            throw new FileNotFoundManagedException(file.getAbsolutePath());
        } else {
            return new FileIOManagedFileAdapter(this, file, pathForFile(file));
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public WatchService getWatchService() {
        return accessStrategy.getWatchService();
    }

    /**
	 * 
	 * {@inheritDoc}
	 */
    public boolean equals(Object other) {
        return (other instanceof MachineIOSystemManagedFileRepository) && equals((MachineIOSystemManagedFileRepository) other);
    }

    private boolean equals(MachineIOSystemManagedFileRepository other) {
        return this.root == null ? other.root == null : this.root.equals(other.root);
    }

    /**
	 * 
	 * {@inheritDoc}
	 */
    public int hashCode() {
        return root.hashCode();
    }

    /**
	 * 
	 * {@inheritDoc}
	 */
    public String toString() {
        return root.toString();
    }

    @Override
    public boolean isReadable() {
        return root.canRead();
    }

    @Override
    public ManagedFile retrive(ManagedFilePath path) throws ManagedIOException {
        try {
            return new FileIOManagedFileAdapter(this, new File(path.getPath()).getCanonicalFile(), path);
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }

    /**
	 * @param systemFile
	 */
    public void createFolder(File systemFile) {
        systemFile.mkdirs();
    }

    /**
	 * @param systemFile
	 */
    public void createFile(File systemFile) {
        try {
            systemFile.createNewFile();
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        }
    }

    @Override
    public boolean isWriteable() {
        return root.canWrite();
    }

    @Override
    public boolean isWatchable() {
        return root != null;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void close() throws IOException {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isOpen() {
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Iterable<ManagedFilePath> getRoots() {
        if (root == null) {
            File[] children = File.listRoots();
            if (children == null) {
                return CollectionUtils.emptyCollection();
            } else {
                return TransformedCollection.transform(Arrays.asList(children), new Classifier<ManagedFilePath, File>() {

                    @Override
                    public ManagedFilePath classify(File obj) {
                        return pathForFile(obj);
                    }
                });
            }
        } else {
            return Collections.<ManagedFilePath>singleton(this.pathForFile(root));
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public ManagedFilePath getPath(String first, String... more) {
        if (root == null) {
            if (this.isDriveRoot(new File(first + "/"))) {
                return new ArrayManagedFilePath(this, first, more);
            }
            return new ArrayManagedFilePath(this, null, CollectionUtils.appendToArrayBegining(more, first));
        } else {
            return this.pathForFile(root).resolve(StringUtils.join("/", first, more));
        }
    }
}
