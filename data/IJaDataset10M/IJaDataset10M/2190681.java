package com.application.areca.metadata.trace;

import java.io.File;
import java.io.IOException;
import com.application.areca.impl.FileSystemRecoveryEntry;
import com.application.areca.metadata.MetadataConstants;
import com.application.areca.metadata.MetadataEncoder;
import com.myJava.file.FileSystemManager;
import com.myJava.file.metadata.FileMetaData;
import com.myJava.file.metadata.FileMetaDataAccessor;
import com.myJava.file.metadata.FileMetaDataAccessorHelper;
import com.myJava.file.metadata.FileMetaDataSerializationException;
import com.myJava.file.metadata.FileMetaDataSerializer;
import com.myJava.util.log.Logger;

public class ArchiveTraceParser {

    /**
	 * Parses the entry's trace and extract its size.
	 */
    public static long extractFileSizeFromTrace(String trace) {
        try {
            int idx = trace.indexOf(MetadataConstants.SEPARATOR);
            return Long.parseLong(trace.substring(0, idx));
        } catch (RuntimeException e) {
            processException(trace, e);
            throw e;
        }
    }

    private static void processException(String trace, Exception e) {
        Logger.defaultLogger().error("Error processing trace : [" + trace + "]", e);
    }

    public static String extractHashFromTrace(String trace) {
        int idx1 = trace.indexOf(MetadataConstants.SEPARATOR);
        int idx2 = trace.indexOf(MetadataConstants.SEPARATOR, idx1 + 1);
        if (idx2 < 0) {
            return trace;
        } else {
            return trace.substring(0, idx2);
        }
    }

    public static FileMetaData extractFileAttributesFromTrace(String trace, long version) throws FileMetaDataSerializationException {
        try {
            FileMetaData data = null;
            int idx1 = trace.indexOf(MetadataConstants.SEPARATOR);
            int idx2 = trace.indexOf(MetadataConstants.SEPARATOR, idx1 + 1);
            if (idx2 < 0) {
                data = FileMetaDataAccessorHelper.getFileSystemAccessor().buildEmptyMetaData();
                data.setLastmodified(Long.parseLong(trace.substring(idx1 + 1)));
            } else {
                data = FileMetaDataAccessorHelper.getFileSystemAccessor().getMetaDataSerializer().deserialize(trace.substring(idx2 + 1), version);
                data.setLastmodified(Long.parseLong(trace.substring(idx1 + 1, idx2)));
            }
            if (data.getLastmodified() < 0) {
                Logger.defaultLogger().warn("CAUTION : Negative modification date (" + data.getLastmodified() + "). It will be set to 0.");
                data.setLastmodified(0);
            }
            return data;
        } catch (RuntimeException e) {
            processException(trace, e);
            throw e;
        }
    }

    public static FileMetaData extractDirectoryAttributesFromTrace(String trace, long version) throws FileMetaDataSerializationException {
        try {
            FileMetaData data = null;
            int idx1 = trace.indexOf(MetadataConstants.SEPARATOR);
            if (idx1 < 0) {
                data = FileMetaDataAccessorHelper.getFileSystemAccessor().buildEmptyMetaData();
                data.setLastmodified(Long.parseLong(trace));
            } else {
                data = FileMetaDataAccessorHelper.getFileSystemAccessor().getMetaDataSerializer().deserialize(trace.substring(idx1 + 1), version);
                data.setLastmodified(Long.parseLong(trace.substring(0, idx1)));
            }
            if (data.getLastmodified() < 0) {
                Logger.defaultLogger().warn("CAUTION : Negative modification date (" + data.getLastmodified() + "). It will be set to 0.");
                data.setLastmodified(0);
            }
            return data;
        } catch (RuntimeException e) {
            processException(trace, e);
            throw e;
        }
    }

    public static FileMetaData extractSymLinkAttributesFromTrace(String trace, long version) throws FileMetaDataSerializationException {
        try {
            FileMetaData data = null;
            int idx1 = trace.indexOf(MetadataConstants.SEPARATOR);
            if (idx1 < 0) {
                data = FileMetaDataAccessorHelper.getFileSystemAccessor().buildEmptyMetaData();
            } else {
                int idx2 = trace.indexOf(MetadataConstants.SEPARATOR, idx1 + 1);
                if (idx2 < 0) {
                    data = FileMetaDataAccessorHelper.getFileSystemAccessor().buildEmptyMetaData();
                    data.setLastmodified(Long.parseLong(trace.substring(idx1 + 1)));
                } else {
                    data = FileMetaDataAccessorHelper.getFileSystemAccessor().getMetaDataSerializer().deserialize(trace.substring(idx2 + 1), version);
                    data.setLastmodified(Long.parseLong(trace.substring(idx1 + 1, idx2)));
                }
                if (data.getLastmodified() < 0) {
                    Logger.defaultLogger().warn("CAUTION : Negative modification date (" + data.getLastmodified() + "). It will be set to 0.");
                    data.setLastmodified(0);
                }
            }
            return data;
        } catch (RuntimeException e) {
            processException(trace, e);
            throw e;
        }
    }

    public static String extractSymLinkPathFromTrace(String trace) {
        try {
            int idx = trace.indexOf(MetadataConstants.SEPARATOR);
            String p;
            if (idx == -1) {
                p = trace.substring(1);
            } else {
                p = trace.substring(1, idx);
            }
            return MetadataEncoder.decode(p);
        } catch (RuntimeException e) {
            processException(trace, e);
            throw e;
        }
    }

    public static boolean extractSymLinkFileFromTrace(String trace) {
        try {
            return trace.charAt(0) == MetadataConstants.T_FILE;
        } catch (RuntimeException e) {
            processException(trace, e);
            throw e;
        }
    }

    public static FileMetaData extractPipeAttributesFromTrace(String trace, long version) throws FileMetaDataSerializationException {
        return extractDirectoryAttributesFromTrace(trace, version);
    }

    /**
	 * Builds the key + hash
	 */
    protected static String serialize(FileSystemRecoveryEntry entry, boolean trackMetaData, boolean trackSymlinks) throws IOException, FileMetaDataSerializationException {
        if (entry == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        short type = FileSystemManager.getType(entry.getFile());
        if (trackSymlinks && FileMetaDataAccessor.TYPE_LINK == type) {
            sb.append(MetadataConstants.T_SYMLINK).append(MetadataEncoder.encode(entry.getKey())).append(MetadataConstants.SEPARATOR).append(hash(entry, true)).append(MetadataConstants.SEPARATOR).append(FileSystemManager.lastModified(entry.getFile()));
        } else if (trackSymlinks && FileMetaDataAccessor.TYPE_PIPE == type) {
            sb.append(MetadataConstants.T_PIPE).append(MetadataEncoder.encode(entry.getKey())).append(MetadataConstants.SEPARATOR).append(FileSystemManager.lastModified(entry.getFile()));
        } else if (FileSystemManager.isFile(entry.getFile())) {
            sb.append(MetadataConstants.T_FILE).append(MetadataEncoder.encode(entry.getKey())).append(MetadataConstants.SEPARATOR).append(hash(entry, false));
        } else {
            sb.append(MetadataConstants.T_DIR).append(MetadataEncoder.encode(entry.getKey())).append(MetadataConstants.SEPARATOR).append(FileSystemManager.lastModified(entry.getFile()));
        }
        if (trackMetaData) {
            FileMetaDataSerializer serializer = FileMetaDataAccessorHelper.getFileSystemAccessor().getMetaDataSerializer();
            sb.append(MetadataConstants.SEPARATOR);
            File target = trackSymlinks ? entry.getFile() : FileSystemManager.getCanonicalFile(entry.getFile());
            serializer.serialize(FileSystemManager.getMetaData(target, false), sb);
        }
        return sb.toString();
    }

    /**
	 * Builds the hash key
	 */
    public static String hash(FileSystemRecoveryEntry fEntry, boolean asLink) throws IOException {
        if (fEntry == null) {
            return null;
        } else if (asLink) {
            char prefix;
            if (FileSystemManager.isDirectory(fEntry.getFile())) {
                prefix = MetadataConstants.T_DIR;
            } else {
                prefix = MetadataConstants.T_FILE;
            }
            return prefix + MetadataEncoder.encode(FileSystemManager.getCanonicalPath(fEntry.getFile()));
        } else if (FileSystemManager.isFile(fEntry.getFile())) {
            return new StringBuffer().append(fEntry.getSize()).append(MetadataConstants.SEPARATOR).append(FileSystemManager.lastModified(fEntry.getFile())).toString();
        } else {
            throw new IllegalArgumentException("Only files are accepted. " + fEntry.getKey() + " is not a file.");
        }
    }

    /**
	 * Checks whether the entry has been modified
	 */
    public static boolean hasBeenModified(String newHash, String oldHash) {
        return !newHash.equals(oldHash);
    }
}
