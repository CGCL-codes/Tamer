package net.sourceforge.xjftp.protocol.handlers;

import net.sourceforge.xjftp.requesthandler.FtpRequestHandler;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * I handle the Control connection from client to server. This is used
 * for passing all the FTP Commands to the server, and for the responses
 * to be sent back to the client. Most of the logic that handles the FTP
 * session is contained within the implementation of this interface.
 * <p>
 * Copyright &copy; 2005 <a href="http://xjftp.sourceforge.net/">XJFTP Team</a>.
 * All rights reserved. Use is subject to <a href="http://xjftp.sourceforge.net/LICENSE.TXT">licence terms</a> (<a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache License v2.0</a>)<br/>
 * <p>
 * Last modified: $Date: 2005/01/22 17:18:23 $, by $Author: mmcnamee $
 * <p>
 * @author Mark McNamee (<a href="mailto:mmcnamee@users.sourceforge.net">mmcnamee at users.sourceforge.net</a>)
 * @version $Revision: 1.5 $
 */
public interface ControlConnectionHandler extends Runnable, Serializable {

    public void initialise(Socket clientSock) throws IOException;

    public void setRequestHandler(FtpRequestHandler listener);

    public FtpRequestHandler getRequestHandler();

    public int reply(int code, String text);

    public String getCurrentDir();

    public void setCurrentDir(String currentDir);

    public String getRequestedPath();

    public void setRequestedPath(String requestedPath);

    public String getBaseDir();

    public String getUsername();
}
