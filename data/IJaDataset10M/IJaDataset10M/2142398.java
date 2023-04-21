package remote.ejb;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import remote.entities.file.RemoteFileData;
import remote.entities.file.RemoteFileDescriptor;
import remote.exceptions.RemoteFileInUseException;
import remote.exceptions.RemoteFileNotFoundException;
import remote.services.ExceptionLogger;

@Stateless
@Local(RemoteFileManager.class)
public class RemoteFileManagerBean implements RemoteFileManager {

    /**
	 * A fájlnevekben szereplő elválasztó karakter (a kliens operációs
	 * rendszerétől független).
	 */
    private static final String separator = "/";

    /**
	 * Adott elérési útvonallal rendelkező fájl leíróját lekérdező lekérdezés.
	 */
    private static final String QUERY_FD_BY_PATH = "SELECT d FROM RemoteFileDescriptor d WHERE d.path = :path";

    /**
	 * Adott elérési útvonallal rendelkező fájl leíróját lekérdező lekérdezés.
	 */
    private static final String QUERY_DATA_BY_FD = "SELECT d FROM RemoteFileData d WHERE d.fileDescriptor = :desc";

    private static final String QUERY_FILEDESCRIPTORS_BY_TYPE = "SELECT d FROM RemoteFileDescriptor d WHERE d.directory = :dir ORDER BY d.path DESC";

    /**
	 * Perzisztens egyedek kezelését végző egyedkezelő.
	 */
    @PersistenceContext
    private EntityManager _entityManager;

    public void changeFileDescriptor(Long id, RemoteFileDescriptor newDescriptor) throws RemoteFileNotFoundException, RemoteFileInUseException {
        RemoteFileDescriptor descriptor = _entityManager.find(RemoteFileDescriptor.class, id);
        if (null == descriptor) {
            throw new RemoteFileNotFoundException("[#" + id + "]");
        }
        if (descriptor.isInUse()) {
            throw new RemoteFileInUseException(descriptor.getPath());
        }
        descriptor.setPath(normalizePath(newDescriptor.getPath()));
    }

    public RemoteFileDescriptor createDirectory(String path) throws RemoteFileNotFoundException, RemoteFileInUseException {
        path = normalizePath(path);
        try {
            ensureNotExists(path);
        } catch (RemoteFileInUseException rfiux) {
            return getFileDescriptorByPath(path);
        }
        RemoteFileDescriptor parentDescriptor = getParentDescriptor(path);
        RemoteFileDescriptor descriptor = new RemoteFileDescriptor();
        descriptor.setId(-1L);
        descriptor.setPath(path);
        descriptor.setInUse(false);
        descriptor.setChildren(new LinkedList<RemoteFileDescriptor>());
        descriptor.setDirectory(true);
        descriptor.setParent(parentDescriptor);
        Calendar currentDate = new GregorianCalendar();
        descriptor.setCreationTime(currentDate);
        descriptor.setModificationTime(currentDate);
        if (null != parentDescriptor) {
            Collection<RemoteFileDescriptor> childrenList = parentDescriptor.getChildren();
            childrenList.add(descriptor);
        }
        _entityManager.persist(descriptor);
        return descriptor;
    }

    public RemoteFileDescriptor createFile(String path) throws RemoteFileNotFoundException, RemoteFileInUseException {
        path = normalizePath(path);
        try {
            ensureNotExists(path);
        } catch (RemoteFileInUseException rfiux) {
            return getFileDescriptorByPath(path);
        }
        RemoteFileDescriptor parentDescriptor = getParentDescriptor(path);
        RemoteFileDescriptor descriptor = new RemoteFileDescriptor();
        descriptor.setId(-1L);
        descriptor.setPath(path);
        descriptor.setInUse(false);
        descriptor.setChildren(null);
        descriptor.setDirectory(false);
        descriptor.setParent(parentDescriptor);
        Calendar currentDate = new GregorianCalendar();
        descriptor.setCreationTime(currentDate);
        descriptor.setModificationTime(currentDate);
        RemoteFileData fileData = new RemoteFileData();
        fileData.setFileDescriptor(descriptor);
        fileData.setData(new byte[0]);
        descriptor.setLength(0);
        if (null != parentDescriptor) {
            Collection<RemoteFileDescriptor> childrenList = parentDescriptor.getChildren();
            childrenList.add(descriptor);
        }
        _entityManager.persist(descriptor);
        _entityManager.persist(fileData);
        return descriptor;
    }

    private final Logger _logger = Logger.getLogger(getClass().getName());

    private Logger getLogger() {
        return _logger;
    }

    public void deleteDirectoryTree(RemoteFileDescriptor directory) throws RemoteFileNotFoundException, RemoteFileInUseException {
        getLogger().info(MessageFormat.format("Deleting directory {0}.", directory.getPath()));
        RemoteFileDescriptor descriptor = getFileDescriptorByPath(normalizePath(directory.getPath()));
        if (descriptor.isInUse()) {
            throw new RemoteFileInUseException(directory.getPath());
        }
        if (null != descriptor.getChildren() && 0 < descriptor.getChildren().size()) {
            Collection<RemoteFileDescriptor> children = descriptor.getChildren();
            for (RemoteFileDescriptor child : new LinkedList<RemoteFileDescriptor>(children)) {
                if (child.isDirectory()) {
                    deleteDirectoryTree(child);
                } else {
                    deleteFile(child);
                }
            }
        }
        deleteFile(descriptor);
    }

    public void deleteFile(RemoteFileDescriptor file) throws RemoteFileNotFoundException, RemoteFileInUseException {
        getLogger().info(MessageFormat.format("Deleting file {0}.", file.getPath()));
        RemoteFileDescriptor descriptor = getFileDescriptorByPath(normalizePath(file.getPath()));
        if (descriptor.isInUse()) {
            throw new RemoteFileInUseException(file.getPath());
        }
        if (descriptor.isDirectory() && descriptor.getLength() > 0) {
            throw new RemoteFileInUseException(file.getPath());
        }
        getFileDescriptorByPath(descriptor.getParentPath()).getChildren().remove(descriptor);
        final RemoteFileData data = getDataForDescriptor(descriptor);
        if (null != data) {
            _entityManager.remove(_entityManager.merge(data));
        }
    }

    /**
	 * Megadott elérési útvonallal rendelkező fájl vagy könyvtár <i>nem</i>
	 * létezését biztosítja.
	 * 
	 * @param path
	 *            annak a fájlnak vagy könyvtárnak az elérési útja, aminek nem
	 *            létezését feltételezzük
	 * @see #createFile(String)
	 * @see #createDirectory(String)
	 */
    private void ensureNotExists(String path) {
        boolean found = false;
        try {
            System.out.println("Ensuring not exists: " + path);
            getFileDescriptorByPath(path);
            found = true;
        } catch (RemoteFileNotFoundException rfnfx) {
        }
        if (found) {
            throw new RemoteFileInUseException(path);
        }
    }

    private RemoteFileData getDataForDescriptor(final RemoteFileDescriptor descriptor) {
        final Query query = _entityManager.createQuery("SELECT d FROM RemoteFileData d WHERE d.fileDescriptor = :descriptor");
        query.setParameter("descriptor", descriptor);
        return (RemoteFileData) query.getSingleResult();
    }

    /**
	 * Adott elérési úthoz tartozó fájlleírót meghatározó segédmetódus.
	 * 
	 * @param path
	 *            az elérési út
	 * @return az elérési úthoz tartozó fájlleíró objektum
	 * @throws RemoteFileNotFoundException
	 *             akkor dobódik, ha a megadott elérési úton nem található fájl
	 *             vagy könyvtár
	 */
    private RemoteFileDescriptor getFileDescriptorByPath(String path) throws RemoteFileNotFoundException {
        path = normalizePath(path);
        try {
            Query query = _entityManager.createQuery(QUERY_FD_BY_PATH);
            query.setParameter("path", path);
            return _entityManager.merge((RemoteFileDescriptor) query.getSingleResult());
        } catch (NoResultException enfx) {
            throw new RemoteFileNotFoundException(path);
        }
    }

    private RemoteFileDescriptor getParentDescriptor(String path) {
        path = normalizePath(path);
        String parentPath = path.substring(0, path.lastIndexOf(separator));
        if ("".equals(parentPath)) {
            if (path.equals(separator)) {
                return null;
            } else {
                return getFileDescriptorByPath(separator);
            }
        } else {
            return getFileDescriptorByPath(parentPath);
        }
    }

    @WebMethod(operationName = "getRootDirectory")
    public RemoteFileDescriptor getRootDirectory() {
        RemoteFileDescriptor rootDir;
        try {
            rootDir = getFileDescriptorByPath(separator);
        } catch (RemoteFileNotFoundException rfnfx) {
            rootDir = createDirectory(separator);
        }
        return rootDir;
    }

    @WebMethod(operationName = "getSeparator")
    public String getSeparator() {
        return separator;
    }

    @WebMethod(operationName = "listDirectoryContents")
    public List<RemoteFileDescriptor> listDirectoryContents(@WebParam(name = "directory") RemoteFileDescriptor directory) {
        RemoteFileDescriptor descriptor = _entityManager.find(RemoteFileDescriptor.class, directory.getId());
        Collection<RemoteFileDescriptor> children = descriptor.getChildren();
        return new LinkedList<RemoteFileDescriptor>(children);
    }

    private String normalizePath(String path) {
        if (!path.startsWith(separator)) {
            path = separator + path;
        }
        return path;
    }

    @WebMethod(operationName = "openFile")
    public RemoteFileDescriptor openFile(@WebParam(name = "path") String path, @WebParam(name = "mode") RemoteFileMode mode) throws RemoteFileNotFoundException, RemoteFileInUseException {
        try {
            RemoteFileDescriptor descriptor = getFileDescriptorByPath(normalizePath(path));
            if (descriptor.isInUse()) {
                throw new RemoteFileInUseException(path);
            }
            return descriptor;
        } catch (RemoteFileNotFoundException rfnfx) {
            if (RemoteFileMode.ReadWrite == mode || RemoteFileMode.OverWrite == mode) {
                RemoteFileDescriptor descriptor = createFile(path);
                return descriptor;
            } else {
                throw new RemoteFileNotFoundException(path);
            }
        }
    }

    @WebMethod(operationName = "readFile")
    public RemoteFileData readFile(@WebParam(name = "remoteFile") RemoteFileDescriptor remoteFile) throws RemoteFileNotFoundException, RemoteFileInUseException {
        RemoteFileDescriptor descriptor = getFileDescriptorByPath(normalizePath(remoteFile.getPath()));
        if (descriptor.isInUse()) {
            throw new RemoteFileInUseException(remoteFile.getPath());
        }
        if (descriptor.isDirectory()) {
            return null;
        }
        try {
            Query query = _entityManager.createQuery(QUERY_DATA_BY_FD);
            query.setParameter("desc", descriptor);
            return (RemoteFileData) query.getSingleResult();
        } catch (NoResultException nrx) {
            throw new RuntimeException();
        }
    }

    @WebMethod(operationName = "writeFile")
    public void writeFile(@WebParam(name = "remoteFile") RemoteFileDescriptor remoteFile, @WebParam(name = "fileData") RemoteFileData fileData) throws RemoteFileNotFoundException, RemoteFileInUseException {
        RemoteFileDescriptor descriptor = getFileDescriptorByPath(normalizePath(remoteFile.getPath()));
        if (descriptor.isInUse()) {
            throw new RemoteFileInUseException(remoteFile.getPath());
        }
        if (descriptor.isDirectory()) {
            throw new RuntimeException();
        }
        try {
            Query query = _entityManager.createQuery(QUERY_DATA_BY_FD);
            query.setParameter("desc", descriptor);
            _entityManager.merge((RemoteFileData) query.getSingleResult()).setData(fileData.getData());
            if (null == fileData.getData()) {
                descriptor.setLength(0);
            } else {
                descriptor.setLength(fileData.getData().length);
            }
            descriptor.setModificationTime(new GregorianCalendar());
        } catch (NoResultException enfx) {
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    public int cleanup() {
        int cleanedFiles = 0;
        try {
            Query query = _entityManager.createQuery(QUERY_FILEDESCRIPTORS_BY_TYPE);
            query.setParameter("dir", true);
            List<RemoteFileDescriptor> directories = (List<RemoteFileDescriptor>) query.getResultList();
            for (final RemoteFileDescriptor dir : directories) {
                if (getSeparator().equals(dir.getPath())) {
                    continue;
                }
                if (null == dir.getParentPath()) {
                    deleteDirectoryTree(dir);
                    cleanedFiles++;
                }
                final RemoteFileDescriptor parent = getFileDescriptorByPath(dir.getParentPath());
                if (!parent.getChildren().contains(dir)) {
                    deleteDirectoryTree(dir);
                    cleanedFiles++;
                }
                for (RemoteFileDescriptor child : dir.getChildren()) {
                    if (null == child.getParentPath() || !child.getParentPath().equals(dir.getPath())) {
                        if (child.isDirectory()) {
                            deleteDirectoryTree(child);
                            cleanedFiles++;
                        } else {
                            deleteFile(child);
                            cleanedFiles++;
                        }
                    }
                }
            }
            query = _entityManager.createQuery(QUERY_FILEDESCRIPTORS_BY_TYPE);
            query.setParameter("dir", false);
            List<RemoteFileDescriptor> files = (List<RemoteFileDescriptor>) query.getResultList();
            for (final RemoteFileDescriptor file : files) {
                if (getSeparator().equals(file.getPath())) {
                    deleteFile(file);
                }
                if (null == file.getParentPath()) {
                    deleteFile(file);
                    cleanedFiles++;
                }
                final RemoteFileDescriptor parent = getFileDescriptorByPath(file.getParentPath());
                if (!parent.getChildren().contains(file)) {
                    deleteFile(file);
                    cleanedFiles++;
                }
            }
        } catch (NullPointerException x) {
            ExceptionLogger.getInstance(this).logError("Exception thrown during cleanup: ", x);
        }
        return cleanedFiles;
    }
}
