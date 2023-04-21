package jpfm.fs;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import jpfm.AccessLevel;
import jpfm.DirectoryStream;
import jpfm.FileAttributesProvider;
import jpfm.FileDescriptor;
import jpfm.FileFlags;
import jpfm.FileId;
import jpfm.FileType;
import jpfm.JPfm.Manager;
import jpfm.JPfmContainable;
import jpfm.JPfmError;
import jpfm.JPfmReadable;
import jpfm.UnmountException;
import jpfm.fs.Type.BASIC;
import jpfm.VolumeVisibility;
import jpfm.annotations.MightBeBlocking;
import jpfm.mount.MountId;
import jpfm.operations.Read;
import jpfm.operations.readwrite.Completer;
import jpfm.operations.readwrite.ReadRequest;
import jpfm.volume.AbstractFile;
import jpfm.mount.BasicCascadeMount;
import jpfm.mount.MountLocation;
import jpfm.util.ReadUtils;
import jpfm.volume.ReferenceProvider;

/**
 * Abstract volumes are case insensitive
 * @author Shashank Tulsyan
 */
public class SimpleReadOnlyFileSystem implements BasicFileSystem {

    protected final DirectoryStream rootDirectoryStream;

    private final LinkedList<SimpleCascadeMount> cascadedFileSystems = new LinkedList<SimpleCascadeMount>();

    private final RootDirectoryStream rootDirectoryStream_Cas = new RootDirectoryStream(this);

    public SimpleReadOnlyFileSystem(DirectoryStream rootDirectoryStream) {
        this.rootDirectoryStream = rootDirectoryStream;
    }

    @Override
    public FileAttributesProvider getRootAttributes() {
        return rootDirectoryStream_Cas;
    }

    @Override
    public final FileAttributesProvider open(String[] filePath) {
        FileAttributesProvider ret = SimpleReadOnlyFileSystem.find(rootDirectoryStream, filePath, 0);
        if (ret == null) {
            synchronized (cascadedFileSystems) {
                for (SimpleCascadeMount bfs : cascadedFileSystems) {
                    if (bfs.getMountLocation().get()[0].equalsIgnoreCase(filePath[0])) {
                        String[] filePathForCascade = new String[filePath.length - 1];
                        System.arraycopy(filePath, 1, filePathForCascade, 0, filePathForCascade.length);
                        return bfs.getFileSytem().open(filePathForCascade);
                    }
                }
            }
            return null;
        }
        return ret;
    }

    @Override
    public void open(FileAttributesProvider ret) {
        if (ret.getFileType() == FileType.FILE) {
            try {
                ((JPfmReadable) ret).open();
            } catch (Exception any) {
                synchronized (cascadedFileSystems) {
                    for (BasicCascadeMount bcm : cascadedFileSystems) {
                        bcm.getFileSytem().open(ret);
                    }
                }
            }
        }
    }

    @Override
    public final FileAttributesProvider getFileAttributes(FileId fileI) {
        if (fileI.implies(rootDirectoryStream.getFileDescriptor())) {
            return rootDirectoryStream;
        }
        FileAttributesProvider ret = findFile(fileI);
        if (ret == null) {
            synchronized (cascadedFileSystems) {
                for (SimpleCascadeMount bfs : cascadedFileSystems) {
                    ret = bfs.getFileSytem().getFileAttributes(fileI);
                    if (ret != null) return ret;
                }
            }
            return null;
        }
        return ret;
    }

    @Override
    public final DirectoryStream list(FileId folderToList) {
        if (folderToList == null) {
            System.err.println("jpfm.fs.SimpleReadOnlyFileSystem:146 Folder to list is null!, JPfmAbstractFileSystem : line 58");
            return null;
        }
        if (folderToList.implies(rootDirectoryStream.getFileDescriptor())) {
            return rootDirectoryStream_Cas;
        }
        FileAttributesProvider fse = findFile(folderToList);
        if (fse == null) {
            synchronized (cascadedFileSystems) {
                for (SimpleCascadeMount bfs : cascadedFileSystems) {
                    fse = bfs.getFileSytem().list(folderToList);
                    if (fse != null) break;
                }
            }
            if (fse == null) return null;
        }
        if (!(fse instanceof DirectoryStream)) {
            return null;
        }
        return (DirectoryStream) fse;
    }

    @MightBeBlocking(reason = "Request is forwarded to appropriate jpfm.JPfmReadable")
    @Override
    public void read(Read read) throws Exception {
        FileAttributesProvider fse = findFile(read.getFileId());
        if (fse == null) {
            synchronized (cascadedFileSystems) {
                for (SimpleCascadeMount bfs : cascadedFileSystems) {
                    if (bfs.getFileSytem().getFileAttributes(read.getFileId()) != null) {
                        bfs.getFileSytem().read(read);
                        return;
                    }
                }
            }
            read.complete(JPfmError.NOT_FOUND, 0, new SimpleReadCompleter());
            return;
        }
        if (!(fse instanceof JPfmReadable)) {
            read.complete(JPfmError.END_OF_DATA, 0, new SimpleReadCompleter());
            return;
        }
        final Read oldRead = read;
        read = ReadUtils.trimReadBeyondFileSize(read, fse.getFileSize());
        if (read == null) {
            oldRead.complete(JPfmError.END_OF_DATA, 0, new SimpleReadCompleter());
            return;
        }
        ((JPfmReadable) fse).read(read);
    }

    public BasicCascadeMount cascadeMount(BasicCascadableProvider basicCascadable) throws UnsupportedOperationException {
        SimpleCascadeMount cm;
        Set<FileAttributesProvider> filesCascadingOver = basicCascadable.filesCascadingOver();
        LinkedHashSet<ReadOnlyRawFileData> fileDatas = new LinkedHashSet<ReadOnlyRawFileData>();
        for (FileAttributesProvider id : filesCascadingOver) {
            try {
                fileDatas.add(((ReferenceProvider) id).getReference(id.getFileDescriptor().getFileId(), AccessLevel.READ_DATA));
            } catch (ClassCastException cce) {
                throw new UnsupportedOperationException("Readable reference to one of the files could not be obtained", cce);
            }
        }
        BasicFileSystem bfs = basicCascadable.getFileSystem(fileDatas, rootDirectoryStream.getFileDescriptor());
        if (bfs != null) {
            String mountName = basicCascadable.suggestedName();
            cm = new SimpleCascadeMount(new String[] { mountName }, fileDatas, bfs, this);
            synchronized (cascadedFileSystems) {
                cascadedFileSystems.add(cm);
            }
            return cm;
        } else {
            throw new NullPointerException("FileSystem instance could not be created");
        }
    }

    public BASIC getType() {
        return null;
    }

    private static final class RootDirectoryStream implements DirectoryStream {

        private final SimpleReadOnlyFileSystem parent;

        public RootDirectoryStream(SimpleReadOnlyFileSystem parent) {
            this.parent = parent;
        }

        public Iterator<FileAttributesProvider> iterator() {
            return new Inte(parent.rootDirectoryStream.iterator(), parent.cascadedFileSystems.iterator());
        }

        private static final class Inte implements Iterator<FileAttributesProvider> {

            private final Iterator<FileAttributesProvider> itReg;

            private final Iterator<SimpleCascadeMount> itCas;

            public Inte(Iterator<FileAttributesProvider> itReg, Iterator<SimpleCascadeMount> itCas) {
                this.itReg = itReg;
                this.itCas = itCas;
            }

            public boolean hasNext() {
                return itReg.hasNext() || itCas.hasNext();
            }

            public FileAttributesProvider next() {
                FileAttributesProvider next = null;
                if (itReg.hasNext()) next = itReg.next(); else if (itCas.hasNext()) next = itCas.next().getFileSytem().getRootAttributes(); else next = null;
                return next;
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }

        public FileType getFileType() {
            return parent.rootDirectoryStream.getFileType();
        }

        public FileDescriptor getFileDescriptor() {
            return parent.rootDirectoryStream.getFileDescriptor();
        }

        public long getFileSize() {
            return 0;
        }

        public long getCreateTime() {
            return 0;
        }

        public long getAccessTime() {
            return 0;
        }

        public long getWriteTime() {
            return 0;
        }

        public long getChangeTime() {
            return 0;
        }

        public String getName() {
            return parent.rootDirectoryStream.getName();
        }

        public FileDescriptor getParentFileDescriptor() {
            return parent.rootDirectoryStream.getParentFileDescriptor();
        }

        public FileFlags getFileFlags() {
            return parent.rootDirectoryStream.getFileFlags();
        }
    }

    private static final class SimpleReadCompleter implements Completer {

        private final StackTraceElement[] stackTrace;

        private SimpleReadCompleter() {
            this.stackTrace = new Throwable().getStackTrace();
        }

        public int getBytesFilledTillNow(ReadRequest pendingRequest) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void completeNow(ReadRequest pendingRequest) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public StackTraceElement[] getStackTrace() {
            return stackTrace;
        }
    }

    @Override
    public final void close(FileId file) {
        FileAttributesProvider fse = findFile(file);
        if (fse == null) {
            return;
        }
        if (!(fse instanceof AbstractFile)) {
            return;
        }
        ((JPfmReadable) fse).close();
    }

    @Override
    public final long capacity() {
        return SimpleReadOnlyFileSystem.getDirectorySize(rootDirectoryStream);
    }

    @Override
    public final void delete(FileId fileToDelete) {
        FileAttributesProvider fse = findFile(fileToDelete);
        ;
        if (!(fse instanceof JPfmContainable)) {
            throw new UnsupportedOperationException("Does not implement JPfmContainable, cannot find parent");
        }
        Iterator<FileAttributesProvider> it = ((JPfmContainable) fse).getParent().iterator();
        while (it.hasNext()) {
            if (it.next().equals(fse)) it.remove();
        }
    }

    public FileAttributesProvider findFile(FileId fileDescriptor) {
        FileAttributesProvider ret = SimpleReadOnlyFileSystem.find(rootDirectoryStream, fileDescriptor);
        return ret;
    }

    public static final long getDirectorySize(DirectoryStream directory) {
        int size = 0;
        for (FileAttributesProvider ele : directory) {
            if (ele.getFileType() == FileType.FILE) {
                size += ele.getFileSize();
            } else {
                if (ele instanceof DirectoryStream) {
                    size += getDirectorySize((DirectoryStream) ele);
                }
            }
        }
        return size;
    }

    public static final FileAttributesProvider find(DirectoryStream container, FileId fileDescriptor) {
        for (FileAttributesProvider fse : container) {
            if (fse.getFileDescriptor().implies(fileDescriptor)) {
                return fse;
            }
            if (fse instanceof DirectoryStream) {
                if (!(fse instanceof FileAttributesProvider)) {
                    continue;
                }
                FileAttributesProvider tmp = find((DirectoryStream) fse, fileDescriptor);
                if (tmp != null) return tmp;
            }
        }
        return null;
    }

    public static final FileAttributesProvider find(DirectoryStream container, String[] name, int index) {
        for (FileAttributesProvider de : container) {
            if (name[index].equalsIgnoreCase(de.getName())) {
                if (index == name.length - 1) {
                    return de;
                }
                if (de instanceof DirectoryStream) {
                    FileAttributesProvider ret = find((DirectoryStream) de, name, index + 1);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return null;
    }

    public static final class SimpleCascadeMount implements BasicCascadeMount {

        private final Set<ReadOnlyRawFileData> readOnlyRawFileDatas;

        private final MountLocation mountLocation;

        private boolean isMounted;

        private final BasicFileSystem fileSystem;

        private final SimpleReadOnlyFileSystem hostFileSystem;

        public SimpleCascadeMount(String[] mountLocation, Set<ReadOnlyRawFileData> readOnlyRawFileDatas, BasicFileSystem fileSystem, final SimpleReadOnlyFileSystem hostFileSystem) {
            this.mountLocation = new MountLocation(mountLocation, true);
            this.isMounted = false;
            this.readOnlyRawFileDatas = readOnlyRawFileDatas;
            this.fileSystem = fileSystem;
            this.hostFileSystem = hostFileSystem;
        }

        public MountId getMountId() {
            return new MountId(this.hashCode(), true);
        }

        public MountLocation getMountLocation() {
            return mountLocation;
        }

        public VolumeVisibility getVolumeVisibility() {
            return VolumeVisibility.INHERITED;
        }

        public boolean isMounted() {
            return isMounted;
        }

        public void unMount() throws UnmountException {
            synchronized (this) {
                for (ReadOnlyRawFileData filedata : readOnlyRawFileDatas) {
                    filedata.close();
                }
                isMounted = false;
            }
            synchronized (hostFileSystem.cascadedFileSystems) {
                hostFileSystem.cascadedFileSystems.remove(this);
            }
        }

        public ThreadGroup getThreadGroup(Manager manager) {
            throw new UnsupportedOperationException("Cascaded filesystem resides in the same threadgroup as the base filesystem");
        }

        public BasicFileSystem getFileSytem() {
            return fileSystem;
        }
    }
}
