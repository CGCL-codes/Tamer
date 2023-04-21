package net.sourceforge.xjftp.requesthandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.sourceforge.xjftp.core.FileList;
import net.sourceforge.xjftp.core.ListedFile;
import net.sourceforge.xjftp.protocol.handlers.ControlConnectionHandler;

/**
 * I provide an API that the FTP server command handler can call when certain events occur.
 * I currently implemented just what is needed, if extensions are required, please contact
 * the author
 * <p>
 * Copyright &copy; 2005 <a href="http://xjftp.sourceforge.net/">XJFTP Team</a>.
 * All rights reserved. Use is subject to <a href="http://xjftp.sourceforge.net/LICENSE.TXT">licence terms</a> (<a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache License v2.0</a>)<br/>
 * <p>
 * Last modified: $Date: 2005/01/27 00:18:28 $, by $Author: mmcnamee $
 * <p>
 * @author Mark McNamee (<a href="mailto:mmcnamee@users.sourceforge.net">mmcnamee at users.sourceforge.net</a>)
 * @version $Revision: 1.2 $
 */
public interface FtpRequestHandler {

    /**
     * Login authentication method - state should be maintained within the 
     * {@link net.sourceforge.xjftp.protocol.handlers.ControlConnectionHandler} only,
     * so the username and password are actually passed to each method call! This call
     * is performed at login on the FTP server to validate whether the user and password
     * are ok according to whatever service you have configured it to call.
     */
    public FtpRequestEventResponse login(String username, String password) throws IOException;

    /**
     * Get a list of all files/documents/messages on the server (i.e. awaiting collection)
     * The {@link FileList} container is special in that it can only contain {@link ListedFile} objects
     * which contain enough information to generate a proper FTP-style listing, containing
     * data, size, owner and group and filename + potentially faked up permissions of the file 
     */
    public FileList listRequested(String username, String password, String filter) throws IOException;

    /**
     * This method is called when a new file is uploaded to the FTP Server. You may
     * wish to write it to disk permanently, or you may with to sent it to a database
     * or a remote SOAP web service etc. This should be done, and the {@link FtpRequestEventResponse}
     * should indicate whether the operation was a success or not. 
     * If <code>ascii</code> is false, then you'll need to do something along the lines of 
     * BASE-64 encoding the data first if your ultimate destination service cannot handle binary (e.g. SOAP)!
     */
    public FtpRequestEventResponse fileArrived(String username, String password, File f, boolean ascii) throws FileNotFoundException, IOException;

    /**
     * I get the remoteFile in to the local file object. If the retrieval is successful
     * then the FtpOperationResult should be marked as a success response. The client
     * of this interface should then read the file and send it back to the FTP client.  
     */
    public FtpRequestEventResponse fileRequested(String username, String password, File localfile, String filename) throws FileNotFoundException, IOException;

    /**
     * &quot;Delete&quot; a file from the remote service. In many cases, this might
     * instead mark the file as deleted, or mark the file as &quot;read&quot; if it
     * is dealing with incoming messages (like an email or file exchange service. If
     * it's operating on the local file system, then it could either delete it, or move it
     * to a deleted folder, depending on how fancy you need your FTP implementation to be
     */
    public FtpRequestEventResponse fileDeleted(String username, String password, String filename) throws FileNotFoundException, IOException;

    /**
     * The SITE command in FTP is used to allow custom commands to be passed directly to the server
     * in order to provide functionality not actually defined in the FTP RFC (RFC 959). As
     * such, you can do with it as you like! If you do not wish to implement the SITE command,
     * simply return null.
     * @return null if the SITE command is not supported, otherwise some sort of {@link FtpRequestEventResponse} 
     */
    public FtpRequestEventResponse handleSitecommand(String username, String password, String command) throws IOException;

    public ControlConnectionHandler getControlHandler();

    public void setControlHandler(ControlConnectionHandler controlHandler);

    public String createNativePath(String ftpPath);

    public String resolvePath(String path);
}
