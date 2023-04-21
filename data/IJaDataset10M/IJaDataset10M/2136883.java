package org.armedbear.j;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class SystemBuffer implements Constants {

    public static final int TYPE_SYSTEM = 0;

    public static final int TYPE_NORMAL = 1;

    public static final int TYPE_ARCHIVE = 2;

    public static final int TYPE_DIRECTORY = 3;

    public static final int TYPE_SHELL = 4;

    public static final int TYPE_MAN = 5;

    public static final int TYPE_OUTPUT = 6;

    public static final int TYPE_IMAGE = 7;

    public static final int TYPE_MAILBOX = 8;

    public static final int TYPE_TELNET = 9;

    public static final int TYPE_SSH = 10;

    public static final int TYPE_LIST_OCCURRENCES = 11;

    protected int type = TYPE_SYSTEM;

    protected boolean readOnly;

    protected boolean forceReadOnly;

    protected Mode mode;

    protected String lineSeparator;

    protected int lineCount;

    private boolean isLoaded;

    private Line firstLine;

    private Line lastLine;

    private File file;

    private String loadEncoding;

    private List tags;

    public SystemBuffer() {
    }

    public SystemBuffer(File file) {
        this.file = file;
    }

    public final int getType() {
        return type;
    }

    public final synchronized Line getFirstLine() {
        return firstLine;
    }

    public synchronized void setFirstLine(Line line) {
        firstLine = line;
    }

    public final Position getEnd() {
        Line line = firstLine;
        if (line == null) return null;
        while (line.next() != null) line = line.next();
        return new Position(line, line.length());
    }

    public final File getFile() {
        return file;
    }

    public final void setFile(File file) {
        this.file = file;
    }

    public final synchronized boolean isLoaded() {
        return isLoaded;
    }

    public final synchronized void setLoaded(boolean b) {
        isLoaded = b;
    }

    public final Mode getMode() {
        return mode;
    }

    public final int getModeId() {
        return mode == null ? 0 : mode.getId();
    }

    public final String getModeName() {
        return mode == null ? null : mode.toString();
    }

    public final synchronized List getTags() {
        return tags;
    }

    public final synchronized void setTags(List tags) {
        this.tags = tags;
    }

    public final void setForceReadOnly(boolean b) {
        forceReadOnly = b;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public final boolean contains(Line line) {
        Line l = getFirstLine();
        while (l != null) {
            if (l == line) return true;
            l = l.next();
        }
        return false;
    }

    public int load() {
        if (!isLoaded) {
            try {
                if (file.isFile()) {
                    InputStream in = file.getInputStream();
                    if (in != null) {
                        load(in, file.getEncoding());
                        in.close();
                    }
                }
                if (getFirstLine() == null) {
                    appendLine("");
                    lineSeparator = System.getProperty("line.separator");
                }
            } catch (IOException e) {
                Log.error(e);
            }
            isLoaded = true;
        }
        return LOAD_COMPLETED;
    }

    public void load(InputStream istream, String encoding) {
        if (mode != null && mode.getId() == BINARY_MODE) {
            loadBinary(istream);
            return;
        }
        byte[] buf = new byte[4096];
        int totalBytes = 0;
        try {
            int bytesRead = istream.read(buf);
            loadProgress(totalBytes = totalBytes + bytesRead);
            boolean isUnicode = false;
            boolean isLittleEndian = true;
            if (bytesRead >= 2) {
                byte byte1 = buf[0];
                byte byte2 = buf[1];
                if (byte1 == (byte) 0xfe && byte2 == (byte) 0xff) {
                    isUnicode = true;
                    isLittleEndian = false;
                    loadEncoding = "UnicodeBig";
                } else if (byte1 == (byte) 0xff && byte2 == (byte) 0xfe) {
                    isUnicode = true;
                    loadEncoding = "UnicodeLittle";
                }
            }
            boolean skipLF = false;
            if (isUnicode) {
                FastStringBuffer sb = new FastStringBuffer(256);
                int i = 2;
                while (bytesRead > 0) {
                    while (i < bytesRead - 1) {
                        char c;
                        final byte b1 = buf[i++];
                        final byte b2 = buf[i++];
                        if (isLittleEndian) c = (char) ((b2 << 8) + (b1 & 0xff)); else c = (char) ((b1 << 8) + (b2 & 0xff));
                        switch(c) {
                            case '\r':
                                appendLine(sb.toString());
                                sb.setLength(0);
                                skipLF = true;
                                break;
                            case '\n':
                                if (skipLF) {
                                    if (lineSeparator == null) lineSeparator = "\r\n";
                                    skipLF = false;
                                } else {
                                    if (lineSeparator == null) lineSeparator = "\n";
                                    appendLine(sb.toString());
                                    sb.setLength(0);
                                }
                                break;
                            default:
                                if (skipLF) {
                                    if (lineSeparator == null) lineSeparator = "\r";
                                    skipLF = false;
                                }
                                sb.append(c);
                                break;
                        }
                    }
                    bytesRead = istream.read(buf);
                    i = 0;
                }
                if (sb.length() > 0) {
                    appendLine(sb.toString());
                } else {
                    appendLine("");
                }
            } else {
                if (encoding == null) {
                    encoding = Editor.preferences().getStringProperty(Property.DEFAULT_ENCODING);
                }
                loadEncoding = encoding;
                ByteBuffer bb = new ByteBuffer(256);
                while (bytesRead > 0) {
                    for (int i = 0; i < bytesRead; i++) {
                        byte b = buf[i];
                        switch(b) {
                            case 13:
                                appendLine(new String(bb.getBytes(), 0, bb.length(), encoding));
                                bb.setLength(0);
                                skipLF = true;
                                break;
                            case 10:
                                if (skipLF) {
                                    if (lineSeparator == null) lineSeparator = "\r\n";
                                    skipLF = false;
                                } else {
                                    if (lineSeparator == null) lineSeparator = "\n";
                                    appendLine(new String(bb.getBytes(), 0, bb.length(), encoding));
                                    bb.setLength(0);
                                }
                                break;
                            default:
                                if (skipLF) {
                                    if (lineSeparator == null) lineSeparator = "\r";
                                    skipLF = false;
                                }
                                bb.append(b);
                                break;
                        }
                    }
                    bytesRead = istream.read(buf);
                    if (bytesRead > 0) loadProgress(totalBytes = totalBytes + bytesRead);
                }
                if (bb.length() > 0) {
                    appendLine(new String(bb.getBytes(), 0, bb.length(), encoding));
                } else {
                    appendLine("");
                }
            }
            isLoaded = true;
        } catch (Exception e) {
            Log.error(e);
        }
        loadFinished(isLoaded);
    }

    public final Line getLastLine() {
        return lastLine;
    }

    public final void setLastLine(Line line) {
        lastLine = line;
    }

    protected void appendLine(Line line) {
        line.setPrevious(lastLine);
        if (lastLine != null) lastLine.setNext(line);
        lastLine = line;
        if (getFirstLine() == null) setFirstLine(line);
    }

    public void appendLine(String s) {
        appendLine(new TextLine(s));
    }

    public void append(String s) {
        int begin = 0;
        int end = 0;
        boolean skipLF = false;
        final int limit = s.length();
        for (int i = 0; i < limit; i++) {
            switch(s.charAt(i)) {
                case '\r':
                    appendLine(s.substring(begin, end));
                    ++end;
                    begin = end;
                    skipLF = true;
                    break;
                case '\n':
                    if (skipLF) {
                        ++begin;
                        ++end;
                        skipLF = false;
                    } else {
                        appendLine(s.substring(begin, end));
                        ++end;
                        begin = end;
                    }
                    break;
                default:
                    skipLF = false;
                    ++end;
            }
        }
        if (begin < end) appendLine(s.substring(begin, end));
    }

    private void appendBinaryLine(int start, byte[] bytes, int numBytes) {
        appendLine(new BinaryLine(start, bytes, numBytes));
    }

    public void renumber() {
        for (Line line = getFirstLine(); line != null; line = line.next()) line.setLineNumber(lineCount++);
    }

    public void writeBuffer() throws SaveException {
        if (file.isFile() && !file.canWrite()) {
            Log.error("writeFile: file is not writable: " + file);
            throw new SaveException(file, file.canonicalPath() + " is not writable");
        }
        if (Platform.isPlatformWindows()) {
            File tempFile = writeTemporaryFile();
            if (!makePatchFile()) {
                if (!Utilities.makeBackup(file, false)) {
                    Log.error("backup failed");
                    throw new SaveException(file, "Unable to write backup file for " + file.canonicalPath());
                }
            }
            if (!Utilities.deleteRename(tempFile, file)) {
                Log.error("unable to rename " + tempFile.canonicalPath() + " to " + file.canonicalPath());
                throw new SaveException(file, "Unable to rename temporary file");
            }
        } else {
            if (!makePatchFile()) {
                if (!Utilities.makeBackup(file, true)) {
                    Log.error("backup failed");
                    throw new SaveException(file, "Unable to write backup file for ".concat(file.canonicalPath()));
                }
            }
            if (!writeFile(file)) {
                Log.error("writeFile failed");
                throw new SaveException(file, "Unable to write ".concat(file.canonicalPath()));
            }
        }
    }

    private final boolean makePatchFile() {
        if (file.isFile()) {
            File patchFile = getPatchFile();
            if (patchFile != null) {
                if (!patchFile.isFile()) return Utilities.copyFile(file, patchFile);
            }
        }
        return false;
    }

    public final File getPatchFile() {
        String suffix;
        if (this instanceof Buffer) suffix = ((Buffer) this).getStringProperty(Property.PATCH_MODE); else if (mode != null) suffix = mode.getStringProperty(Property.PATCH_MODE); else {
            suffix = Editor.preferences().getStringProperty(Property.PATCH_MODE);
        }
        if (suffix != null) {
            suffix = suffix.trim();
            if (suffix.length() > 0) {
                if (suffix.charAt(0) != '.') suffix = ".".concat(suffix);
                return File.getInstance(file.canonicalPath().concat(suffix));
            }
        }
        return null;
    }

    public boolean writeFile(File outputFile) {
        try {
            BufferedOutputStream out = new BufferedOutputStream(outputFile.getOutputStream());
            if (lineSeparator == null) lineSeparator = System.getProperty("line.separator");
            String encoding = outputFile.getEncoding();
            if (encoding == null) encoding = getSaveEncoding();
            Line line = getFirstLine();
            if (line != null) {
                final byte[] byteOrderMark = getByteOrderMark(encoding);
                if (byteOrderMark != null) out.write(byteOrderMark);
                out.write(line.getBytes(encoding));
                line = line.next();
                final byte[] sepBytes = getSeparatorBytes(encoding);
                while (line != null) {
                    out.write(sepBytes);
                    out.write(line.getBytes(encoding));
                    line = line.next();
                }
            }
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            Log.error(e);
            return false;
        }
    }

    public String getSaveEncoding() {
        String encoding = file == null ? null : file.getEncoding();
        if (encoding == null) {
            encoding = loadEncoding;
            if (encoding == null) encoding = Editor.preferences().getStringProperty(Property.DEFAULT_ENCODING);
        }
        if (encoding == null) Debug.bug();
        return encoding;
    }

    byte[] getByteOrderMark(String encoding) throws UnsupportedEncodingException {
        byte[] bytes = "test".getBytes(encoding);
        if ((bytes[0] == (byte) 0xfe && bytes[1] == (byte) 0xff) || (bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xfe)) {
            byte[] byteOrderMark = new byte[2];
            byteOrderMark[0] = bytes[0];
            byteOrderMark[1] = bytes[1];
            return byteOrderMark;
        }
        return null;
    }

    byte[] getSeparatorBytes(String encoding) throws UnsupportedEncodingException {
        byte[] bytes = lineSeparator.getBytes(encoding);
        if (bytes.length > 2) {
            if ((bytes[0] == (byte) 0xfe && bytes[1] == (byte) 0xff) || (bytes[0] == (byte) 0xff && bytes[1] == (byte) 0xfe)) {
                byte[] sepBytes = new byte[bytes.length - 2];
                for (int i = 0; i < sepBytes.length; i++) sepBytes[i] = bytes[i + 2];
                return sepBytes;
            }
        }
        return bytes;
    }

    synchronized void _empty() {
        Line line = getFirstLine();
        while (line != null) {
            Line nextLine = line.next();
            line.setPrevious(null);
            line.setNext(null);
            line = nextLine;
        }
        setFirstLine(null);
        lastLine = null;
        isLoaded = false;
    }

    protected void loadProgress(int totalBytesRead) {
    }

    protected void loadFinished(boolean success) {
    }

    private void loadBinary(InputStream istream) {
        byte[] array = readAllBytes(istream);
        if (array != null) {
            for (int start = 0; start < array.length; start += 16) {
                int bytesLeft = array.length - start;
                int count = bytesLeft >= 16 ? 16 : bytesLeft;
                appendBinaryLine(start, array, count);
            }
            isLoaded = true;
        }
        loadFinished(isLoaded);
    }

    private byte[] readAllBytes(InputStream in) {
        final int chunkSize = 0x8000;
        byte[] array = null;
        int totalBytes = 0;
        byte[] chunk = new byte[chunkSize];
        int bytesRead;
        try {
            while ((bytesRead = in.read(chunk, 0, chunk.length)) > 0) {
                if (array == null) {
                    array = new byte[bytesRead];
                    System.arraycopy(chunk, 0, array, 0, bytesRead);
                } else {
                    byte[] newArray = new byte[totalBytes + bytesRead];
                    if (totalBytes > 0) System.arraycopy(array, 0, newArray, 0, totalBytes);
                    System.arraycopy(chunk, 0, newArray, totalBytes, bytesRead);
                    array = newArray;
                }
                totalBytes += bytesRead;
                Debug.assertTrue(array.length == totalBytes);
            }
        } catch (IOException e) {
            Log.error(e);
            array = null;
        }
        return array;
    }

    private File writeTemporaryFile() throws SaveException {
        boolean succeeded = false;
        File tempFile = Utilities.getTempFile(file.getParent());
        if (tempFile != null) succeeded = writeFile(tempFile);
        if (!succeeded) {
            tempFile = Utilities.getTempFile();
            if (tempFile != null) succeeded = writeFile(tempFile);
        }
        if (!succeeded) {
            throw new SaveException(file, "Unable to write temporary file for ".concat(file.canonicalPath()));
        }
        return tempFile;
    }
}
