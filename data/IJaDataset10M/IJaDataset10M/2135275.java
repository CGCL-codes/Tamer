package net.sf.opensftp;

import net.sf.opensftp.prompter.Prompter;

/**
 * <code>SftpUtil</code> provides a handy set of utilities designed to ease your
 * work with SFTP.
 * 
 * @author BurningXFlame@gmail.com
 */
public interface SftpUtil {

    /**
	 * Represents the StrictHostKeyChecking option 'ask'.
	 */
    public static final int STRICT_HOST_KEY_CHECKING_OPTION_ASK = 0;

    /**
	 * Represents the StrictHostKeyChecking option 'yes'.
	 */
    public static final int STRICT_HOST_KEY_CHECKING_OPTION_YES = 1;

    /**
	 * Represents the StrictHostKeyChecking option 'no'.
	 */
    public static final int STRICT_HOST_KEY_CHECKING_OPTION_NO = 2;

    /**
	 * Sets the <code>prompter</code> property. Once a valid
	 * <code>Prompter</code> is set, the <code>Prompter</code> should be used in
	 * any scenario where a <code>Prompter</code> is supposed to be used.
	 * 
	 * @see net.sf.opensftp.prompter.Prompter
	 */
    public void setPrompter(Prompter prompter);

    /**
	 * Sets the <code>progressListener</code> property. Once a valid
	 * <code>ProgressListener</code> is set, the <code>ProgressListener</code>
	 * should be used in any scenario where a
	 * <code><code>ProgressListener</code></code> is supposed to be used.
	 * 
	 * @see net.sf.opensftp.ProgressListener
	 */
    public void setProgressListener(ProgressListener progressListener);

    /**
	 * Connects to an SFTP server at the default port(22) with publickey
	 * authentication with empty passphrase. This method is equivalent to:
	 * 
	 * <pre>
	 * connect(host, 22, user, &quot;&quot;, identityFile, strictHostKeyChecking)
	 * </pre>
	 * 
	 * @see #connect(String, int, String, String, String, int, int)
	 * @return an {@link SftpSession} object representing the context of the
	 *         communication established between the client and the specified
	 *         SFTP server.
	 */
    public SftpSession connect(String host, String user, String identityFile, int strictHostKeyChecking) throws SftpException;

    /**
	 * Connects to an SFTP server at the default port(22) with publickey
	 * authentication. This method is equivalent to:
	 * 
	 * <pre>
	 * connect(host, 22, user, passphrase, identityFile, strictHostKeyChecking)
	 * </pre>
	 * 
	 * @see #connect(String, int, String, String, String, int, int)
	 * @return an {@link SftpSession} object representing the context of the
	 *         communication established between the client and the specified
	 *         SFTP server.
	 */
    public SftpSession connect(String host, String user, String passphrase, String identityFile, int strictHostKeyChecking) throws SftpException;

    /**
	 * Connects to an SFTP server with publickey authentication.
	 * 
	 * @param host
	 *            Host name
	 * @param port
	 *            Port number
	 * @param user
	 *            User name
	 * @param passphrase
	 *            Passphrase. An empty string ("") indicates no passphrase.
	 * @param identityFile
	 *            The path of the identityFile. For security, it's strongly
	 *            recommended to place your identityFile in the
	 *            <code>.ssh</code> folder under your home folder,
	 *            /home/<i>yourusername</i>/.ssh for Linux for instance. If you
	 *            specify a value begining with '~' and a following directory
	 *            separator ('/' or '\\'), the '~' is treated as your home
	 *            folder.
	 * @param strictHostKeyChecking
	 *            The strictHostKeyChecking option. Valid values are
	 *            {@link #STRICT_HOST_KEY_CHECKING_OPTION_ASK},
	 *            {@link #STRICT_HOST_KEY_CHECKING_OPTION_YES} and
	 *            {@link #STRICT_HOST_KEY_CHECKING_OPTION_NO}.
	 * @param timeout
	 *            the timeout in milliseconds. A timeout of zero is treated as
	 *            an infinite timeout.
	 * 
	 * @return an {@link SftpSession} object representing the context of the
	 *         communication established between the client and the specified
	 *         SFTP server.
	 */
    public SftpSession connect(String host, int port, String user, String passphrase, String identityFile, int strictHostKeyChecking, int timeout) throws SftpException;

    /**
	 * Connects to an SFTP server at the default port(22) with password
	 * authentication. This method is equivalent to:
	 * 
	 * <pre>
	 * connectByPasswdAuth(host, 22, user, password, strictHostKeyChecking)
	 * </pre>
	 * 
	 * @see #connectByPasswdAuth(String, int, String, String, int, int)
	 * @return an {@link SftpSession} object representing the context of the
	 *         communication established between the client and the specified
	 *         SFTP server.
	 */
    public SftpSession connectByPasswdAuth(String host, String user, String password, int strictHostKeyChecking) throws SftpException;

    /**
	 * Connects to an SFTP server with password authentication.
	 * 
	 * @param host
	 *            Host name
	 * @param port
	 *            Port number
	 * @param user
	 *            User name
	 * @param password
	 *            Password. An empty string ("") indicates no password.
	 * @param strictHostKeyChecking
	 *            The strictHostKeyChecking option. Valid values are
	 *            {@link #STRICT_HOST_KEY_CHECKING_OPTION_ASK},
	 *            {@link #STRICT_HOST_KEY_CHECKING_OPTION_YES} and
	 *            {@link #STRICT_HOST_KEY_CHECKING_OPTION_NO}.
	 * @param timeout
	 *            the timeout in milliseconds. A timeout of zero is treated as
	 *            an infinite timeout.
	 * 
	 * @return an {@link SftpSession} object representing the context of the
	 *         communication established between the client and the specified
	 *         SFTP server.
	 */
    public SftpSession connectByPasswdAuth(String host, int port, String user, String password, int strictHostKeyChecking, int timeout) throws SftpException;

    /**
	 * Disconnects from an SFTP server.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 */
    public void disconnect(SftpSession session);

    /**
	 * Represents the ls command. This method is equivalent to:
	 * 
	 * <pre>
	 * ls(session, &quot;.&quot;)
	 * </pre>
	 * 
	 * @see #ls(SftpSession, String)
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>List<{@link SftpFile}></code> object, if this ls operation
	 *         succeeds.
	 */
    public SftpResult ls(SftpSession session);

    /**
	 * Represents the ls command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The path to be listed.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>List<{@link SftpFile}></code> object, if this ls operation
	 *         succeeds.
	 */
    public SftpResult ls(SftpSession session, String path);

    /**
	 * Represents the put command. This method is equivalent to:
	 * 
	 * <pre>
	 * put(session, localFilename, &quot;.&quot;)
	 * </pre>
	 * 
	 * @see #put(SftpSession, String, String)
	 */
    public SftpResult put(SftpSession session, String localFilename);

    /**
	 * Represents the put command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param localFilename
	 *            The path of the local file to be uploaded.
	 * @param remoteFilename
	 *            The remote path where to place the local file.
	 * 
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult put(SftpSession session, String localFilename, String remoteFilename);

    /**
	 * Represents the get command. This method is equivalent to:
	 * 
	 * <pre>
	 * get(session, remoteFilename, &quot;.&quot;)
	 * </pre>
	 * 
	 * @see #get(SftpSession, String, String)
	 */
    public SftpResult get(SftpSession session, String remoteFilename);

    /**
	 * Represents the get command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param remoteFilename
	 *            The path of the remote file to be downloaded.
	 * @param localFilename
	 *            The local path where to place the remote file.
	 * 
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult get(SftpSession session, String remoteFilename, String localFilename);

    /**
	 * Represents the cd command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The directory
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult cd(SftpSession session, String path);

    /**
	 * Represents the lcd command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The directory
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult lcd(SftpSession session, String path);

    /**
	 * Represents the mkdir command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The path of the directory you want to create.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult mkdir(SftpSession session, String path);

    /**
	 * Represents the rename command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param oldpath
	 *            The path of the file or directory you want to rename.
	 * @param newpath
	 *            the new file name
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult rename(SftpSession session, String oldpath, String newpath);

    /**
	 * Represents the rm command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param filename
	 *            The path of the file you want to deleted.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult rm(SftpSession session, String filename);

    /**
	 * Represents the rmdir command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The path of the directory you want to delete.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult rmdir(SftpSession session, String path);

    /**
	 * Represents the pwd command.
	 * 
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>String</code> object representing the current path, if this
	 *         pwd operation succeeds.
	 */
    public SftpResult pwd(SftpSession session);

    /**
	 * Represents the lpwd command.
	 * 
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>String</code> object representing the current path, if this
	 *         lpwd operation succeeds.
	 */
    public SftpResult lpwd(SftpSession session);

    /**
	 * Represents the chgrp command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param gid
	 *            The GID of the new group
	 * @param path
	 *            The path of the file or directory the group of which you want
	 *            to change.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult chgrp(SftpSession session, int gid, String path);

    /**
	 * Represents the chown command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param uid
	 *            The UID of the new owner
	 * @param path
	 *            The path of the file or directory the owner of which you want
	 *            to change.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult chown(SftpSession session, int uid, String path);

    /**
	 * Represents the chmod command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param mode
	 *            an three-digit octal number, 0755 for instance, representing
	 *            the new permissions
	 * @param path
	 *            The path of the file or directory the permission of which you
	 *            want to change.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult chmod(SftpSession session, int mode, String path);

    /**
	 * Represents the ln/symlink command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param src
	 *            The path of the file which you want to symlink.
	 * @param link
	 *            The path of the link file
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult ln(SftpSession session, String src, String link);

    /**
	 * Represents the lumask command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param umask
	 *            The new umask.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult lumask(SftpSession session, String umask);

    /**
	 * Represents the help command.
	 * 
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult help(SftpSession session);

    /**
	 * Represents the lmkdir command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The path of the folder which you want to create.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation
	 */
    public SftpResult lmkdir(SftpSession session, String path);

    /**
	 * Represents the lls command. This method is equivalent to:
	 * 
	 * <pre>
	 * lls(session, &quot;.&quot;)
	 * </pre>
	 * 
	 * @see #lls(SftpSession, String)
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>List<{@link SftpFile}></code> object, if this lls operation
	 *         succeeds.
	 */
    public SftpResult lls(SftpSession session);

    /**
	 * Represents the lls command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @param path
	 *            The local path you want to list.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>List<{@link SftpFile}></code> object, if this lls operation
	 *         succeeds.
	 */
    public SftpResult lls(SftpSession session, String path);

    /**
	 * Represents the version command.
	 * 
	 * @param session
	 *            The {@link SftpSession} object you previously got when
	 *            connecting.
	 * @return an {@link SftpResult} object representing the result of this
	 *         operation. Invoking <code>getExtension()</code> on the returned
	 *         <code>SftpResult</code> object should return a
	 *         <code>String</code> object, if this operation succeeds.
	 */
    public SftpResult version(SftpSession session);
}
