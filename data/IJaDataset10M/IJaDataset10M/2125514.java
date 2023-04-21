package org.eclipse.jdt.internal.compiler.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;

/**
 * Implementation of a Java file object that corresponds to an entry in a zip/jar file
 */
public class ArchiveFileObject implements JavaFileObject {

    private ZipEntry zipEntry;

    private ZipFile zipFile;

    private String entryName;

    private File file;

    private Charset charset;

    public ArchiveFileObject(File file, ZipFile zipFile, String entryName, Charset charset) {
        this.zipFile = zipFile;
        this.zipEntry = zipFile.getEntry(entryName);
        this.entryName = entryName;
        this.file = file;
        this.charset = charset;
    }

    public Modifier getAccessLevel() {
        if (getKind() != Kind.CLASS) {
            return null;
        }
        ClassFileReader reader = null;
        try {
            reader = ClassFileReader.read(this.zipFile, this.entryName);
        } catch (ClassFormatException e) {
        } catch (IOException e) {
        }
        if (reader == null) {
            return null;
        }
        final int accessFlags = reader.accessFlags();
        if ((accessFlags & ClassFileConstants.AccPublic) != 0) {
            return Modifier.PUBLIC;
        }
        if ((accessFlags & ClassFileConstants.AccAbstract) != 0) {
            return Modifier.ABSTRACT;
        }
        if ((accessFlags & ClassFileConstants.AccFinal) != 0) {
            return Modifier.FINAL;
        }
        return null;
    }

    public Kind getKind() {
        String name = this.entryName.toLowerCase();
        if (name.endsWith(Kind.CLASS.extension)) {
            return Kind.CLASS;
        } else if (name.endsWith(Kind.SOURCE.extension)) {
            return Kind.SOURCE;
        } else if (name.endsWith(Kind.HTML.extension)) {
            return Kind.HTML;
        }
        return Kind.OTHER;
    }

    public NestingKind getNestingKind() {
        switch(getKind()) {
            case SOURCE:
                return NestingKind.TOP_LEVEL;
            case CLASS:
                ClassFileReader reader = null;
                try {
                    reader = ClassFileReader.read(this.zipFile, this.entryName);
                } catch (ClassFormatException e) {
                } catch (IOException e) {
                }
                if (reader == null) {
                    return null;
                }
                if (reader.isAnonymous()) {
                    return NestingKind.ANONYMOUS;
                }
                if (reader.isLocal()) {
                    return NestingKind.LOCAL;
                }
                if (reader.isMember()) {
                    return NestingKind.MEMBER;
                }
                return NestingKind.TOP_LEVEL;
            default:
                return null;
        }
    }

    public boolean isNameCompatible(String simpleName, Kind kind) {
        return this.zipEntry.getName().endsWith(simpleName + kind.extension);
    }

    public boolean delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArchiveFileObject)) {
            return false;
        }
        ArchiveFileObject archiveFileObject = (ArchiveFileObject) o;
        return archiveFileObject.toUri().equals(this.toUri());
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        if (getKind() == Kind.SOURCE) {
            return Util.getCharContents(this, ignoreEncodingErrors, org.eclipse.jdt.internal.compiler.util.Util.getZipEntryByteContent(this.zipEntry, this.zipFile), this.charset.toString());
        }
        return null;
    }

    public long getLastModified() {
        return this.zipEntry.getTime();
    }

    public String getName() {
        return this.zipEntry.getName();
    }

    public InputStream openInputStream() throws IOException {
        return this.zipFile.getInputStream(this.zipEntry);
    }

    public OutputStream openOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Writer openWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    public URI toUri() {
        try {
            return new URI("jar:" + this.file.toURI().getPath() + "!" + this.zipEntry.getName());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.file.getAbsolutePath() + "[" + this.zipEntry.getName() + "]";
    }
}
