package net.sourceforge.xjftp.protocol.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.Iterator;
import net.sourceforge.xjftp.core.CommandException;
import net.sourceforge.xjftp.core.FileList;
import net.sourceforge.xjftp.core.ListedFile;
import net.sourceforge.xjftp.representation.Representation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the server data transfer process. It is responsible for transferring
 * files to and from the client. A separate data socket is created to transfer
 * the data.
 * <p>
 * Copyright &copy; 2005 <a href="http://xjftp.sourceforge.net/">XJFTP Team</a>.
 * All rights reserved. Use is subject to <a href="http://xjftp.sourceforge.net/LICENSE.TXT">licence terms</a> (<a href="http://www.apache.org/licenses/LICENSE-2.0.html">Apache License v2.0</a>)<br/>
 * <p>
 * Last modified: $Date: 2005/05/22 23:17:37 $, by $Author: mmcnamee $
 * <p>
 * @author Mark McNamee (<a href="mailto:mmcnamee@users.sourceforge.net">mmcnamee at users.sourceforge.net</a>)
 * @version $Revision: 1.5 $
 */
public class DataConnectionHandlerImpl extends AbstractDataConnectionHandlerImpl {

    private static final Log LOG = LogFactory.getLog(DataConnectionHandlerImpl.class);

    /**
     * Creates a server data transfer process for the specified ServerPI.
     */
    public DataConnectionHandlerImpl() {
        super();
    }

    /**
     * Opens the data connection, reads the data according to the current
     * transmission mode, representation type and structure, and writes it into
     * the local file "path".
     */
    public boolean receiveFile(String path) throws CommandException {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            this.fos = new FileOutputStream(file);
            connectToUser();
            this.controlHandler.reply(150, "Opening " + this.representation.getName() + " mode data connection.");
            this.transmissionMode.receiveFile(this.dataSocket, this.fos, this.representation);
            return true;
        } catch (ConnectException e) {
            throw new CommandException(425, "Can't open data connection.");
        } catch (IOException e) {
            throw new CommandException(550, "Can't write to file");
        } finally {
            terminateOutputStream();
            terminateDataConnection();
            terminatePassiveServerSocket();
        }
    }

    /**
     * Opens the data connection reads the specified local file and writes it to
     * the data socket using the current transmission mode, representation type
     * and structure.
     */
    public boolean sendFile(String path) throws CommandException {
        try {
            File file = new File(path);
            if (!file.isFile()) {
                throw new CommandException(550, "Not a plain file.");
            }
            this.fis = new FileInputStream(file);
            connectToUser();
            this.controlHandler.reply(150, "Opening " + this.representation.getName() + " mode data connection.");
            this.transmissionMode.sendFile(this.fis, this.dataSocket, this.representation);
            return true;
        } catch (FileNotFoundException e) {
            LOG.debug("No such file: " + e);
            throw new CommandException(550, "No such file.");
        } catch (ConnectException e) {
            throw new CommandException(425, "Can't open data connection.");
        } catch (IOException e) {
            LOG.debug("Not a regular file: " + e);
            throw new CommandException(553, "Not a regular file.");
        } finally {
            terminateInputStream();
            terminateDataConnection();
        }
    }

    public int sendNameList(FileList fileList) throws CommandException {
        return sendList(fileList, false);
    }

    public int sendList(FileList fileList) throws CommandException {
        return sendList(fileList, true);
    }

    public int sendList(FileList fileList, boolean longFormat) throws CommandException {
        int reply = 0;
        try {
            int numFiles = fileList != null ? fileList.size() : 0;
            connectToUser();
            this.representation = getNewRepresentation(Representation.CHAR_ASCII);
            PrintWriter writer = new PrintWriter(this.representation.getOutputStream(this.dataSocket));
            this.controlHandler.reply(150, "Opening " + this.representation.getName() + " mode data connection.");
            writer.print("total " + numFiles + "\n");
            if (fileList != null) {
                Iterator iter = fileList.iterator();
                while (iter.hasNext()) {
                    ListedFile curFile = (ListedFile) iter.next();
                    if (longFormat) {
                        writer.write(curFile.toFtpString());
                    } else {
                        writer.write(curFile.toFtpNameOnlyString());
                    }
                }
            }
            writer.flush();
            reply = this.controlHandler.reply(226, "Transfer complete.");
        } catch (ConnectException e) {
            throw new CommandException(425, "Can't open data connection.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(550, "No such directory.");
        } finally {
            terminateDataConnection();
        }
        return reply;
    }

    public void abortTransfer() throws IOException {
        this.terminateInputStream();
        this.terminateOutputStream();
        this.terminateDataConnection();
        this.terminatePassiveServerSocket();
    }
}
