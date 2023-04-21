package gap.jac.file;

import java.io.IOException;
import java.util.Set;
import gap.jac.tools.JavaFileObject;
import gap.jac.file.JavacFileManager.Archive;
import gap.jac.util.List;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;

public class ZipFileIndexArchive implements Archive {

    private final ZipFileIndex zfIndex;

    private JavacFileManager fileManager;

    public ZipFileIndexArchive(JavacFileManager fileManager, ZipFileIndex zdir) throws IOException {
        super();
        this.fileManager = fileManager;
        this.zfIndex = zdir;
    }

    public boolean contains(String name) {
        return zfIndex.contains(name);
    }

    public List<String> getFiles(String subdirectory) {
        return zfIndex.getFiles((subdirectory.endsWith("/") || subdirectory.endsWith("\\")) ? subdirectory.substring(0, subdirectory.length() - 1) : subdirectory);
    }

    public JavaFileObject getFileObject(String subdirectory, String file) {
        String fullZipFileName = subdirectory + file;
        ZipFileIndex.Entry entry = zfIndex.getZipIndexEntry(fullZipFileName);
        JavaFileObject ret = new ZipFileIndexFileObject(fileManager, zfIndex, entry, zfIndex.getZipFile().getPath());
        return ret;
    }

    public Set<String> getSubdirectories() {
        return zfIndex.getAllDirectories();
    }

    public void close() throws IOException {
        zfIndex.close();
    }

    /**
     * A subclass of JavaFileObject representing zip entries using the gap.jac.file.ZipFileIndex implementation.
     */
    public static class ZipFileIndexFileObject extends BaseFileObject {

        /** The entry's name.
         */
        private String name;

        /** The zipfile containing the entry.
         */
        ZipFileIndex zfIndex;

        /** The underlying zip entry object.
         */
        ZipFileIndex.Entry entry;

        /** The InputStream for this zip entry (file.)
         */
        InputStream inputStream = null;

        /** The name of the zip file where this entry resides.
         */
        String zipName;

        ZipFileIndexFileObject(JavacFileManager fileManager, ZipFileIndex zfIndex, ZipFileIndex.Entry entry, String zipFileName) {
            super(fileManager);
            this.name = entry.getFileName();
            this.zfIndex = zfIndex;
            this.entry = entry;
            this.zipName = zipFileName;
        }

        public InputStream openInputStream() throws IOException {
            if (inputStream == null) {
                inputStream = new ByteArrayInputStream(read());
            }
            return inputStream;
        }

        protected CharsetDecoder getDecoder(boolean ignoreEncodingErrors) {
            return fileManager.getDecoder(fileManager.getEncodingName(), ignoreEncodingErrors);
        }

        public OutputStream openOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        public Writer openWriter() throws IOException {
            throw new UnsupportedOperationException();
        }

        /** @deprecated see bug 6410637 */
        @Deprecated
        public String getName() {
            return name;
        }

        public boolean isNameCompatible(String cn, JavaFileObject.Kind k) {
            cn.getClass();
            if (k == Kind.OTHER && getKind() != k) return false;
            return name.equals(cn + k.extension);
        }

        /** @deprecated see bug 6410637 */
        @Deprecated
        public String getPath() {
            return zipName + "(" + entry.getName() + ")";
        }

        public long getLastModified() {
            return entry.getLastModified();
        }

        public boolean delete() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ZipFileIndexFileObject)) return false;
            ZipFileIndexFileObject o = (ZipFileIndexFileObject) other;
            return entry.equals(o.entry);
        }

        @Override
        public int hashCode() {
            return zipName.hashCode() + (name.hashCode() << 10);
        }

        public String getZipName() {
            return zipName;
        }

        public String getZipEntryName() {
            return entry.getName();
        }

        public URI toUri() {
            String zipName = new File(getZipName()).toURI().normalize().getPath();
            String entryName = getZipEntryName();
            if (File.separatorChar != '/') {
                entryName = entryName.replace(File.separatorChar, '/');
            }
            return URI.create("jar:" + zipName + "!" + entryName);
        }

        private byte[] read() throws IOException {
            if (entry == null) {
                entry = zfIndex.getZipIndexEntry(name);
                if (entry == null) throw new FileNotFoundException();
            }
            return zfIndex.read(entry);
        }

        public CharBuffer getCharContent(boolean ignoreEncodingErrors) throws IOException {
            CharBuffer cb = fileManager.getCachedContent(this);
            if (cb == null) {
                InputStream in = new ByteArrayInputStream(zfIndex.read(entry));
                try {
                    ByteBuffer bb = fileManager.makeByteBuffer(in);
                    JavaFileObject prev = fileManager.log.useSource(this);
                    try {
                        cb = fileManager.decode(bb, ignoreEncodingErrors);
                    } finally {
                        fileManager.log.useSource(prev);
                    }
                    fileManager.recycleByteBuffer(bb);
                    if (!ignoreEncodingErrors) fileManager.cache(this, cb);
                } finally {
                    in.close();
                }
            }
            return cb;
        }

        @Override
        protected String inferBinaryName(Iterable<? extends File> path) {
            String entryName = getZipEntryName();
            if (zfIndex.symbolFilePrefix != null) {
                String prefix = zfIndex.symbolFilePrefix;
                if (entryName.startsWith(prefix)) entryName = entryName.substring(prefix.length());
            }
            return removeExtension(entryName).replace(File.separatorChar, '.');
        }
    }
}
