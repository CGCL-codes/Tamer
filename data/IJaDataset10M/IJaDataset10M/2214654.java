package com.threerings.jpkg.debian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;
import javax.mail.internet.InternetHeaders;
import org.apache.commons.io.IOUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;
import com.threerings.jpkg.PackageTarFile;
import com.threerings.jpkg.UnixStandardPermissions;
import com.threerings.jpkg.ar.ArchiveEntry;

/**
 * Handles the creation of the Debian package control.tar.gz file.
 */
public class ControlFile implements ArchiveEntry {

    /**
     * Construct a new ControlFile which creates the contents of control.tar.gz entry in the
     * Debian package.
     * @param info The fully populated package meta data.
     * @param dataTar The fully populated {@link PackageTarFile} represented by this control file.
     * @throws IOException If any i/o exceptions occur during the control file creation.
     * @throws ScriptDataTooLargeException If any maintainer script is too large to be added to the tar file.
     */
    public ControlFile(PackageInfo info, PackageTarFile dataTar) throws IOException, ScriptDataTooLargeException {
        _controlData = createTarArray(info, dataTar);
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(_controlData);
    }

    public long getSize() {
        return _controlData.length;
    }

    public String getPath() {
        return DEB_AR_CONTROL_FILE;
    }

    public int getUserId() {
        return UnixStandardPermissions.ROOT_USER.getId();
    }

    public int getGroupId() {
        return UnixStandardPermissions.ROOT_GROUP.getId();
    }

    public int getMode() {
        return UnixStandardPermissions.STANDARD_FILE_MODE;
    }

    /**
     * Create the control.tar.gz file as a byte array.
     */
    private byte[] createTarArray(PackageInfo info, PackageTarFile dataTar) throws IOException, ScriptDataTooLargeException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final TarOutputStream controlTar = new TarOutputStream(new GZIPOutputStream(output));
        controlTar.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        try {
            addControlFile(controlTar, info, dataTar);
            addMd5Sums(controlTar, dataTar);
            addMaintainerScripts(controlTar, info);
        } finally {
            controlTar.close();
        }
        return output.toByteArray();
    }

    /**
     * Add the control file to the tar file.
     */
    private void addControlFile(TarOutputStream tar, PackageInfo info, PackageTarFile dataTar) throws IOException {
        final InternetHeaders headers = info.getControlHeaders();
        headers.addHeader(INSTALLED_SIZE, String.valueOf(dataTar.getTotalDataSize()));
        final StringBuilder controlFile = new StringBuilder();
        @SuppressWarnings("unchecked") final Enumeration<String> en = headers.getAllHeaderLines();
        while (en.hasMoreElements()) {
            controlFile.append(en.nextElement()).append('\n');
        }
        final TarEntry entry = standardEntry(DEB_CONTROL_FILE, UnixStandardPermissions.STANDARD_FILE_MODE, controlFile.length());
        tar.putNextEntry(entry);
        IOUtils.write(controlFile.toString(), tar);
        tar.closeEntry();
    }

    /**
     * Add the md5sums file to the tar file.
     */
    private void addMd5Sums(TarOutputStream tar, PackageTarFile dataTar) throws IOException {
        final StringBuilder md5sums = new StringBuilder();
        for (final Entry<String, String> sum : dataTar.getMd5s().entrySet()) {
            md5sums.append(sum.getKey()).append(' ').append(sum.getValue()).append('\n');
        }
        final TarEntry entry = standardEntry(DEB_MD5_FILE, UnixStandardPermissions.STANDARD_FILE_MODE, md5sums.length());
        tar.putNextEntry(entry);
        IOUtils.write(md5sums.toString(), tar);
        tar.closeEntry();
    }

    /**
     * Add the maintainer scripts to the tar file.
     */
    private void addMaintainerScripts(TarOutputStream tar, PackageInfo info) throws IOException, ScriptDataTooLargeException {
        for (final MaintainerScript script : info.getMaintainerScripts().values()) {
            if (script.getSize() > Integer.MAX_VALUE) {
                throw new ScriptDataTooLargeException("The script data is too large for the tar file. script=[" + script.getType().getFilename() + "].");
            }
            final TarEntry entry = standardEntry(script.getType().getFilename(), UnixStandardPermissions.EXECUTABLE_FILE_MODE, (int) script.getSize());
            tar.putNextEntry(entry);
            IOUtils.copy(script.getStream(), tar);
            tar.closeEntry();
        }
    }

    /**
     * Returns a TarEntry object with correct default values.
     */
    private TarEntry standardEntry(String name, int mode, int size) {
        final TarEntry entry = new TarEntry(name);
        entry.setNames(UnixStandardPermissions.ROOT_USER.getName(), UnixStandardPermissions.ROOT_GROUP.getName());
        entry.setIds(UnixStandardPermissions.ROOT_USER.getId(), UnixStandardPermissions.ROOT_GROUP.getId());
        entry.setSize(size);
        entry.setMode(mode);
        return entry;
    }

    /** The field name in the control file for the installed size of the package. */
    private static final String INSTALLED_SIZE = "Installed-Size";

    /** The name of the control file in the Debian package. */
    private static final String DEB_AR_CONTROL_FILE = "control.tar.gz";

    /** Constants for entries in the control file. */
    private static final String DEB_CONTROL_FILE = "control";

    private static final String DEB_MD5_FILE = "md5sums";

    /** The control.tar.gz data is held in this byte array after creation. */
    private final byte[] _controlData;
}
