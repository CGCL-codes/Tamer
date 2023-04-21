package net.sf.commons.ssh;

/**
 * @author Egor Ivanov (crackcraft at gmail dot com)
 * @since 1.2
 */
public interface SftpFileAttributes {

    long getAccessedTime();

    long getGID();

    long getModifiedTime();

    long getPermissions();

    long getSize();

    long getUID();

    boolean isBlock();

    boolean isCharacter();

    boolean isDirectory();

    boolean isFifo();

    boolean isFile();

    boolean isLink();

    boolean isSocket();
}
