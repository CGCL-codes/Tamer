package com.bitgate.util.mime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import com.bitgate.server.Server;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.services.engine.Encoder;

/**
 * This class handles mime attachments for E-Mail based posts.  This class is used primarily in the SMTP server, which handles
 * data coming into the system through actual strings instead of file-based data.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/mime/MimeAttachmentMail.java#24 $
 */
public class MimeAttachmentMail {

    private String filename, attachType, attachContent, variableName, attachEncoding, fileVariableStore;

    private int filesize, contentDisposition, attachCounter;

    private boolean isEmpty, isLastAttachment, isMultiPart;

    private ArrayList headers;

    private MimeProcessorMail mProcessor;

    /** Contents disposition of the attachment is not known. */
    public static final int DISPOSITION_UNKNOWN = 0;

    /** Contents disposition of the attachment contains form data. */
    public static final int DISPOSITION_FORMDATA = 1;

    /** Contents disposition of the attachment contains an attachment file - most E-Mail is like this. */
    public static final int DISPOSITION_ATTACHMENT = 2;

    /** Contents disposition of the attachment contains inline posted data - only some E-Mail is like this. */
    public static final int DISPOSITION_INLINE = 3;

    /**
     * Constructor: Takes in a BufferedReader, mime processor object, split string, and attachment counter.
     *
     * @param in The <code>BufferedReader</code> object to read from.
     * @param mProcessor The <code>MimeProcessorMail</code> object to communicate with.
     * @param splitSequence The Mime splitter sequence to use.
     * @param attachCounter The mime attachment counter.
     */
    public MimeAttachmentMail(BufferedReader in, MimeProcessorMail mProcessor, String splitSequence, int attachCounter) {
        this.filename = null;
        this.mProcessor = mProcessor;
        this.attachCounter = attachCounter;
        this.filesize = 0;
        this.contentDisposition = DISPOSITION_UNKNOWN;
        this.isEmpty = true;
        this.attachType = null;
        this.attachContent = null;
        this.variableName = null;
        this.attachEncoding = null;
        this.fileVariableStore = null;
        this.headers = new ArrayList();
        this.isLastAttachment = false;
        this.isMultiPart = false;
        handleAttachment(in);
    }

    private void handleAttachment(BufferedReader in) {
        if (mProcessor.getCurrentBoundary() == null) {
            Debug.inform("Handling attachment, no mime split, so entire message is plain text.");
        } else {
            Debug.inform("Handling attachment, split '" + mProcessor.getCurrentBoundary() + "'");
        }
        ArrayList attachmentHeaders = new ArrayList();
        String line = null, headerLine = null;
        while (true) {
            try {
                line = in.readLine();
            } catch (IOException e) {
                line = null;
                Debug.debug("Unable to read line: " + e.getMessage());
            }
            if (line == null || line.equals("")) {
                Debug.inform("End of headers, breaking loop.");
                break;
            }
            if (headerLine == null) {
                headerLine = line;
            } else {
                headerLine += line;
            }
            if (line.equals("--" + mProcessor.getCurrentBoundary())) {
                headerLine = null;
                Debug.inform("Skipping addition of boundary.");
                continue;
            }
            if (headerLine.indexOf(":") != -1 && !headerLine.endsWith(";")) {
                if (headerLine != null) {
                    headers.add(headerLine);
                    Debug.inform("Added header line '" + headerLine + "'");
                }
                headerLine = null;
            }
        }
        if (headerLine != null) {
            headers.add(headerLine);
        }
        for (int i = 0; i < headers.size(); i++) {
            parseHeader((String) headers.get(i));
        }
        if (!isMultiPart) {
            setContents(in, mProcessor.getCurrentBoundary());
        } else {
            Debug.inform("This is a multi-part boundary message, skipping for next bounary check.");
        }
        attachmentHeaders.clear();
        attachmentHeaders = null;
    }

    private void parseHeader(String header) {
        int colonPosition = header.indexOf(":");
        String[] headerContents = null;
        if (colonPosition != -1) {
            headerContents = header.substring(colonPosition + 1).split(";");
            header = header.substring(0, colonPosition);
        }
        header = header.trim().toLowerCase();
        if (header.equals("content-disposition")) {
            for (int i = 0; i < headerContents.length; i++) {
                String disposition = headerContents[i].trim();
                if (disposition.indexOf("=") != -1) {
                    int equalsPosition = disposition.indexOf("=");
                    String dHeader = disposition.substring(0, equalsPosition).toLowerCase();
                    String dValue = disposition.substring(equalsPosition + 1);
                    if (dValue.startsWith("\"")) {
                        dValue = dValue.substring(1);
                    }
                    if (dValue.endsWith("\"")) {
                        dValue = dValue.substring(0, dValue.length() - 1);
                    }
                    if (dHeader.equals("filename")) {
                        setFilename(dValue);
                    } else if (dHeader.equals("name")) {
                        setVariableName(dValue);
                    }
                    Debug.debug("Header '" + dHeader + "' = '" + dValue + "'");
                } else {
                    disposition = disposition.toLowerCase();
                    if (disposition.equals("form-data") || disposition.equals("formdata")) {
                        setContentDisposition(DISPOSITION_FORMDATA);
                    } else if (disposition.equals("inline")) {
                        setContentDisposition(DISPOSITION_INLINE);
                    } else if (disposition.equals("attachment")) {
                        setContentDisposition(DISPOSITION_ATTACHMENT);
                    }
                }
                Debug.debug("Disposition: '" + headerContents[i].trim() + "'");
            }
        } else if (header.equals("content-type")) {
            String contentTypes[] = headerContents[0].split(";");
            Debug.debug("Content type data '" + headerContents[0] + "'");
            for (int i = 0; i < headerContents.length; i++) {
                String disposition = headerContents[i].trim();
                if (disposition.indexOf("=") != -1) {
                    int equalsPosition = disposition.indexOf("=");
                    String dHeader = disposition.substring(0, equalsPosition).toLowerCase();
                    String dValue = disposition.substring(equalsPosition + 1);
                    if (dValue.startsWith("\"")) {
                        dValue = dValue.substring(1);
                    }
                    if (dValue.endsWith("\"")) {
                        dValue = dValue.substring(0, dValue.length() - 1);
                    }
                    if (dHeader.equals("name") || dHeader.equals("filename")) {
                        setFilename(dValue);
                    } else if (dHeader.equals("boundary")) {
                        mProcessor.addBoundary(dValue);
                        isMultiPart = true;
                    }
                    Debug.debug("Header '" + dHeader + "' = '" + dValue + "'");
                } else if (headerContents[i].indexOf("/") != -1) {
                    setContentType(contentTypes[i].trim());
                }
            }
            contentTypes = null;
        } else if (header.equals("content-transfer-encoding")) {
            setTransferEncoding(headerContents[0].trim());
        }
        if (header == null || headerContents == null) {
            return;
        }
        Debug.debug("Header '" + header + "' contains '" + headerContents.length + "' entries.");
        headerContents = null;
    }

    /**
     * Sets the filename of the attachment.
     *
     * @param filename The filename to assign.
     */
    public void setFilename(String filename) {
        if (filename.indexOf("/") != -1) {
            this.filename = filename.substring(filename.lastIndexOf("/") + 1);
        } else {
            this.filename = filename;
        }
        if (filename.indexOf("\\") != -1) {
            this.filename = filename.substring(filename.lastIndexOf("\\") + 1);
        }
        Debug.debug("Setting filename to '" + filename + "'");
    }

    /**
     * Sets the size of the attachment.
     *
     * @param size The size to set.
     */
    public void setSize(int size) {
        this.filesize = size;
        Debug.debug("Setting contents size to '" + size + "'");
    }

    /**
     * Sets the contents of the attachment.
     *
     * @param in The <code>BufferedReader</code> object to read from.
     * @param splitSequence The split string to use to identify the end of an attachment.
     */
    public void setContents(BufferedReader in, String splitSequence) {
        boolean hasSeparator = false;
        if (splitSequence != null && !splitSequence.equals("")) {
            hasSeparator = true;
        }
        this.isEmpty = false;
        if (hasSeparator) {
            Debug.inform("BufferedReader in contents.  No mime separator.  attachEncoding='" + attachEncoding + "'");
        } else {
            Debug.inform("BufferedReader in contents.  Split='" + splitSequence + "' attachEncoding='" + attachEncoding + "'");
        }
        fileVariableStore = Server.getVariableStore() + "/" + System.currentTimeMillis() + attachCounter + ".post";
        Debug.inform("Variable store filename = '" + fileVariableStore);
        (new File(Server.getVariableStore())).mkdirs();
        (new File(fileVariableStore)).deleteOnExit();
        PrintStream pStream = null;
        int bytesWritten = 0;
        try {
            pStream = new PrintStream(new FileOutputStream(fileVariableStore));
        } catch (FileNotFoundException e) {
            Debug.inform("Unable to write data to '" + fileVariableStore + "': " + e.getMessage());
            return;
        }
        while (true) {
            String line = null;
            try {
                line = in.readLine();
            } catch (IOException e) {
                Debug.debug("Unable to read attachment data: " + e.getMessage());
            }
            if (line == null) {
                Debug.inform("End of file reached: Last attachment.");
                isLastAttachment = true;
                break;
            }
            if (hasSeparator) {
                if (line.equals("--" + splitSequence + "--")) {
                    mProcessor.removeBoundary();
                    if (mProcessor.getCurrentBoundary() == null) {
                        isLastAttachment = true;
                        Debug.inform("Reached end of attachment and last attachment.");
                    } else {
                        Debug.inform("Reached end of attachment in sub-mime attachment list.");
                    }
                    break;
                } else if (line.equals("--" + splitSequence)) {
                    Debug.inform("Reached end of attachment.");
                    break;
                }
            }
            if (attachEncoding != null && attachEncoding.toLowerCase().equals("base64")) {
            } else if (attachEncoding != null && attachEncoding.toLowerCase().equals("quoted-printable")) {
                String origLine = line;
                line = Encoder.quotedPrintableDecode(line);
                if (!origLine.endsWith("=")) {
                    line += "\r\n";
                }
            } else {
                line += "\r\n";
            }
            try {
                pStream.write(line.getBytes(), 0, line.length());
                pStream.flush();
                bytesWritten += line.length();
            } catch (Exception e) {
                Debug.inform("Unable to write file variable store: " + e);
            }
        }
        try {
            pStream.close();
        } catch (Exception e) {
            Debug.inform("Unable to write file variable store: " + e);
        }
        setSize(bytesWritten);
    }

    /**
     * Sets the attachment content type.
     *
     * @param contentType The content type to assign.
     */
    public void setContentType(String contentType) {
        this.attachContent = contentType;
        Debug.debug("Set content type to '" + attachContent + "'");
    }

    /**
     * Sets the attachment disposition.
     *
     * @param disposition The attachment disposition to assign.
     */
    public void setContentDisposition(int disposition) {
        this.contentDisposition = disposition;
        Debug.debug("Set content disposition to '" + disposition + "'");
    }

    /**
     * Sets the attachment variable name.
     *
     * @param variableName The variable name to assign.
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName.toLowerCase();
        Debug.debug("Set variable name to '" + variableName.toLowerCase() + "'");
    }

    /**
     * Sets the transfer encoding type.
     * 
     * @param transferEncoding The transfer encoding to set.
     */
    public void setTransferEncoding(String transferEncoding) {
        this.attachEncoding = transferEncoding;
        Debug.debug("Set transfer encoding to '" + attachEncoding + "'");
    }

    /**
     * Returns the current filename.
     *
     * @return <code>String</code> containing the filename.
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Returns the size of the attachment.
     *
     * @return <code>int</code> containing the size of the attachment.
     */
    public int getSize() {
        return this.filesize;
    }

    /**
     * Returns the content type of the attachment.
     *
     * @return <code>String</code> containing the content type.
     */
    public String getContentType() {
        return this.attachContent;
    }

    /**
     * Returns the content disposition type.
     *
     * @return <code>int</code> containing the content disposition.
     */
    public int getContentDisposition() {
        return this.contentDisposition;
    }

    /**
     * Returns the current variable name.
     *
     * @return <code>String</code> containing the variable name.
     */
    public String getVariableName() {
        if (this.variableName == null) {
            return null;
        }
        return this.variableName.toLowerCase();
    }

    /**
     * Returns the current variable file store name.
     *
     * @return <code>String</code> containing the store name.
     */
    public String getFileVariableStore() {
        return this.fileVariableStore;
    }

    /**
     * Returns the current transfer encoding.
     *
     * @return <code>String</code> containing the transfer encoding.
     */
    public String getTransferEncoding() {
        return this.attachEncoding;
    }

    /**
     * Returns a flag determining if the current attachment is the last one.
     *
     * @return <code>true</code> if this is the last attachment, <code>false</code> otherwise.
     */
    public boolean isLastAttachment() {
        return this.isLastAttachment;
    }

    /**
     * Returns whether or not the attachment is part of a multi-part attachment list.
     *
     * @return <code>true</code> if the attachment is part of a multi-part document, <code>false</code> otherwise.
     */
    public boolean isMultiPart() {
        return this.isMultiPart;
    }
}
