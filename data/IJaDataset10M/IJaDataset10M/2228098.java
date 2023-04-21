package net.sf.karatasi.server;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.japi.net.rest.Http11Header;
import net.sf.japi.net.rest.Http11Method;
import net.sf.japi.net.rest.Http11StatusCode;
import net.sf.japi.net.rest.HttpException;
import net.sf.japi.net.rest.RestInputStream;
import net.sf.japi.net.rest.RestMethod;
import net.sf.japi.net.rest.RestOutputStream;
import net.sf.japi.net.rest.server.AbstractSession;
import net.sf.japi.net.rest.server.RestServer;
import net.sf.japi.net.rest.util.RestUtil;
import net.sf.karatasi.UserAuthenticator;
import net.sf.karatasi.Util;
import net.sf.karatasi.database.DBValueException;
import net.sf.karatasi.database.Database;
import net.sf.karatasi.librarian.DatabaseLibrarian;
import net.sf.karatasi.librarian.DatabaseStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A thread for a data synchronization client. Each (http) connection is handled by it's own client.
 * The run method reads the first line of the http header, decodes method, URL and version,
 * and passes to a specialized handler.
 *
 * @author <a href="mailto:christian.hujer@users.sourceforge.net">Christian Hujer</a>
 * @author Mathias Kussinger
 * @author Christa Runge
 */
@SuppressWarnings({ "HardcodedFileSeparator", "HardcodedLineSeparator" })
public class SyncServerSession extends AbstractSession {

    /** Maximum size of a uploaded file. */
    private static final int MAX_UPLOAD_SIZE = 1000000000;

    /** The database librarian for this connection. */
    private final DatabaseLibrarian databaseLibrarian;

    /** The Logger. */
    private static final Logger LOG = Logger.getLogger("net.sf.japi.net.rest");

    /** The Content-Type for Karatasi. */
    private static final String CONTENT_TYPE_KARATASI = "application/octet-stream";

    /** Constructs a ServerSession.
     * The ServerSession is not auto-started. To start the ServerSession, invoke {@link #start()}.
     *
     * @param client Client with which the server shall communicate.
     * @param server Server.
     * @param databaseLibrarian the database librarian the server uses for the database management.
     * @param userAuthenticator the instance responsible for device authentication. If null the device
     *        authentication is disabled.
     */
    public SyncServerSession(@NotNull final Socket client, @NotNull final RestServer server, final DatabaseLibrarian databaseLibrarian, final UserAuthenticator userAuthenticator) {
        super(client, server, new KaratasiAuthenticator(userAuthenticator));
        this.databaseLibrarian = databaseLibrarian;
    }

    /** Handles the http response for a file list request.
     * It requires "version" and "device" data in the url map.
     * @param request RestInputStream.
     * @param response RestOutputStream.
     * @throws IOException for i/o functions.
     */
    @RestMethod("/list")
    public void handleList(@NotNull final RestInputStream request, @NotNull final RestOutputStream response) throws IOException {
        final String encoding = "UTF-8";
        final List<DatabaseStatus> dbList = databaseLibrarian.getDatabaseStatusList();
        final StringBuilder buffer = new StringBuilder();
        buffer.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"no\"?>\r\n");
        buffer.append("<!DOCTYPE dblist PUBLIC \"-//Karatasi//DTD Karatasi DB List 1.0//EN\" \"/DTD/karatasiDbList1.0.dtd\">\r\n");
        buffer.append("<dblist>\r\n");
        for (final DatabaseStatus dbData : dbList) {
            buffer.append("    <file fullname=\"");
            buffer.append(URLEncoder.encode(dbData.getFullName(), encoding));
            final long dbSize;
            final int dbVersion;
            final long dbModificationTime;
            if (dbData.isHealthy()) {
                dbSize = dbData.getFileSize();
                dbVersion = dbData.getDbVersion();
                dbModificationTime = dbData.getTimeStamp().getTime() / 1000;
            } else {
                dbSize = 0;
                dbVersion = 0;
                dbModificationTime = 0;
            }
            buffer.append("\" size=\"");
            buffer.append(dbSize);
            buffer.append("\" version=\"");
            buffer.append(dbVersion);
            buffer.append("\" time=\"");
            buffer.append(dbModificationTime);
            buffer.append("\" />\r\n");
        }
        buffer.append("</dblist>\r\n");
        final byte[] list = buffer.toString().getBytes(encoding);
        response.setHeader(Http11Header.CONTENT_LANGUAGE, "en");
        response.setHeader(Http11Header.CONTENT_TYPE, "text/xml; charset=" + encoding);
        response.setHeader(Http11Header.CONTENT_LENGTH, Integer.toString(list.length));
        response.write(list);
        response.flush();
        LOG.info("/list sent to client " + request.getClientAddress());
    }

    @RestMethod("/mirror")
    public void handleMirror(@NotNull final RestInputStream request, @NotNull final RestOutputStream response) throws IOException {
        final StringBuilder buffer = new StringBuilder();
        for (final Map.Entry<String, String> entry : request.getRequestURI().getQueryParameters().entrySet()) {
            buffer.append("url ");
            buffer.append(entry.getKey());
            buffer.append("::");
            buffer.append(entry.getValue());
            buffer.append("\r\n");
        }
        for (final Map.Entry<String, String> entry : request.getRequestHeaders().entrySet()) {
            buffer.append("body ");
            buffer.append(entry.getKey());
            buffer.append("::");
            buffer.append(entry.getValue());
            buffer.append("\r\n");
        }
        final byte[] list = buffer.toString().getBytes("UTF-8");
        response.setHeader(Http11Header.CONTENT_LANGUAGE, "en");
        response.setHeader(Http11Header.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.setHeader(Http11Header.CONTENT_LENGTH, Integer.toString(list.length));
        response.write(list);
        response.flush();
        LOG.info("/mirror sent to client " + request.getClientAddress());
    }

    /** Downloads a database to the iPhone client.
     *
     * @param request RestInputStream.
     * @param response RestOutputStream.
     * @throws IOException in case of I/O problems.
     */
    @RestMethod(value = "/databases", methods = { Http11Method.GET }, scanChildren = true)
    public void getDatabase(@NotNull final RestInputStream request, @NotNull final RestOutputStream response) throws IOException {
        final String fullName = extractFullName("/databases", request);
        if (fullName == null) {
            throw new HttpException(Http11StatusCode.NOT_FOUND);
        }
        LOG.info("received GET request for /databases/" + fullName + " from " + request.getClientAddress());
        final String versionString = request.getRequestURI().get("version");
        final String device = request.getRequestURI().get("device");
        try {
            if (device == null) {
                throw new IllegalArgumentException();
            }
            final int clientVersion = Integer.parseInt(versionString);
            synchronized (databaseLibrarian) {
                final Database database = databaseLibrarian.borrowDatabase(fullName);
                if (database != null) {
                    try {
                        if (!database.isHealthy() || database.getVersion() > clientVersion) {
                            throw new HttpException(Http11StatusCode.NOT_FOUND);
                        }
                        final File databaseFile = database.getFile();
                        final InputStream databaseFileIn = new FileInputStream(databaseFile);
                        try {
                            response.setHeader(Http11Header.CONTENT_LANGUAGE, "en");
                            response.setHeader(Http11Header.CONTENT_TYPE, CONTENT_TYPE_KARATASI);
                            response.setHeader(Http11Header.CONTENT_LENGTH, Long.toString(databaseFile.length()));
                            Util.copy(databaseFileIn, response);
                            LOG.info("file " + databaseFile + " sent to " + request.getClientAddress());
                        } finally {
                            databaseFileIn.close();
                        }
                    } finally {
                        try {
                            databaseLibrarian.returnDatabase(database);
                        } catch (final SQLException e) {
                            LOG.info("SQLException - cannot return " + fullName + " to the librarian");
                        } catch (final DBValueException e) {
                            LOG.info("SQLException - cannot return " + fullName + " to the librarian");
                        }
                    }
                } else {
                    throw new HttpException(Http11StatusCode.CONFLICT);
                }
            }
        } catch (final IllegalArgumentException e) {
            throw new HttpException(Http11StatusCode.NOT_FOUND);
        }
    }

    @RestMethod(value = "/databases", methods = { Http11Method.PUT }, scanChildren = true)
    public void putDatabaseGeneric(@NotNull final RestInputStream request, @NotNull final RestOutputStream response) throws Exception {
        final Map<String, String> supportedHeaders = RestUtil.newCaseInsensitiveMap();
        {
            supportedHeaders.put(Http11Header.CONTENT_ENCODING, "");
            final String contentEncoding = request.getRequestHeaders().get(Http11Header.CONTENT_ENCODING);
            if (contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity")) {
                throw new HttpException(Http11StatusCode.UNSUPPORTED_MEDIA_TYPE, "Content-Coding " + contentEncoding + " not supported.");
            }
        }
        int numberOfBytes;
        {
            supportedHeaders.put(Http11Header.CONTENT_LENGTH, "");
            final String contentLength = request.getRequestHeaders().get(Http11Header.CONTENT_LENGTH);
            if (contentLength == null) {
                throw new HttpException(Http11StatusCode.LENGTH_REQUIRED);
            }
            try {
                numberOfBytes = Integer.parseInt(contentLength);
            } catch (final NumberFormatException e) {
                throw new HttpException(Http11StatusCode.BAD_REQUEST, "Invalid Content-Length " + contentLength);
            }
            if (numberOfBytes < 0) {
                throw new HttpException(Http11StatusCode.BAD_REQUEST, "Invalid Content-Length " + contentLength);
            }
            if (numberOfBytes > MAX_UPLOAD_SIZE) {
                throw new HttpException(Http11StatusCode.REQUEST_ENTITY_TOO_LARGE, "Content-Length " + numberOfBytes + " too large.");
            }
        }
        supportedHeaders.put(Http11Header.CONTENT_LOCATION, "");
        supportedHeaders.put(Http11Header.CONTENT_MD5, "");
        {
            supportedHeaders.put(Http11Header.CONTENT_RANGE, "");
            final String contentRange = request.getRequestHeaders().get(Http11Header.CONTENT_RANGE);
            if (contentRange != null) {
                throw new HttpException(Http11StatusCode.NOT_IMPLEMENTED, "Content-Range not implemented.");
            }
        }
        {
            supportedHeaders.put(Http11Header.CONTENT_TYPE, "");
            final String contentType = request.getRequestHeaders().get(Http11Header.CONTENT_TYPE);
            if (contentType != null && !CONTENT_TYPE_KARATASI.equalsIgnoreCase(contentType)) {
                throw new HttpException(Http11StatusCode.UNSUPPORTED_MEDIA_TYPE, "Content-Type " + contentType + " not supported.");
            }
        }
        for (final String header : request.getRequestHeaders().keySet()) {
            if (header.toLowerCase(Locale.ENGLISH).startsWith("content-") && !supportedHeaders.containsKey(header)) {
                throw new HttpException(Http11StatusCode.NOT_IMPLEMENTED, header + " not implemented.");
            }
        }
        uploadDatabase(request, response, numberOfBytes);
    }

    /** karatasi-specific part of the PUT method for /database URI: upload a database.
     *
     * @param request RestInputStream.
     * @param response RestOutputStream.
     * @param bytesRemaining
     * @throws IOException in case of I/O problems.
     * @throws DBValueException
     */
    private void uploadDatabase(final RestInputStream request, final RestOutputStream response, final int numberOfBytes) throws HttpException, IOException, SQLException, DBValueException {
        final String fullName = extractFullName("/databases", request);
        if (fullName == null) {
            throw new HttpException(Http11StatusCode.NOT_FOUND);
        }
        LOG.info("received PUT request for /databases/" + fullName + " from " + request.getClientAddress());
        int bytesRemaining = numberOfBytes;
        synchronized (databaseLibrarian) {
            Database database = null;
            boolean isNew;
            try {
                database = databaseLibrarian.borrowTmpDbReplace(fullName);
                isNew = false;
            } catch (final FileNotFoundException e) {
                database = databaseLibrarian.borrowTmpDbNew();
                isNew = true;
            }
            final byte[] buf = new byte[BUFFER_SIZE];
            if (database != null) {
                try {
                    final File uploadFile = database.getFile();
                    try {
                        final OutputStream fileOut = new FileOutputStream(uploadFile);
                        try {
                            for (int bytesRead; bytesRemaining > 0 && (bytesRead = request.read(buf, 0, Math.min(buf.length, bytesRemaining))) != -1; bytesRemaining -= bytesRead) {
                                fileOut.write(buf, 0, bytesRead);
                            }
                        } finally {
                            fileOut.close();
                        }
                        if (bytesRemaining > 0) {
                            throw new EOFException("Client sent fewer bytes than expected / indicated by Content-Length header.");
                        }
                        assert bytesRemaining == 0;
                        database.refresh();
                        if (!database.isHealthy()) {
                            final File targetFile = new File(uploadFile.getParentFile(), Database.deriveFilenameFromFullname(fullName));
                            try {
                                database.moveFileTo(targetFile);
                            } catch (final IOException e) {
                                LOG.info(e.toString() + e.getMessage());
                            }
                        }
                    } catch (final Exception e) {
                        database.kill();
                        throw new HttpException(Http11StatusCode.INTERNAL_SERVER_ERROR, e.toString());
                    }
                    if (isNew) {
                        response.setStatus(Http11StatusCode.CREATED);
                    } else {
                        response.setStatus(Http11StatusCode.OK);
                    }
                    response.flush();
                } finally {
                    databaseLibrarian.returnDatabase(database);
                }
            } else {
                for (int bytesRead; bytesRemaining > 0 && (bytesRead = request.read(buf, 0, Math.min(buf.length, bytesRemaining))) != -1; bytesRemaining -= bytesRead) {
                }
                throw new HttpException(Http11StatusCode.CONFLICT);
            }
        }
    }

    @RestMethod("/DTD/karatasiDbList1.0.dtd")
    public void handleKatatasiDtd(@NotNull final RestInputStream request, @NotNull final RestOutputStream response) throws IOException {
        final String encoding = "UTF-8";
        response.setHeader(Http11Header.CONTENT_LANGUAGE, "en");
        response.setHeader(Http11Header.CONTENT_TYPE, "text/xml; charset=" + encoding);
        Util.copy(getClass().getClassLoader().getResourceAsStream("resources/karatasiDbList1.0.dtd"), response);
        response.flush();
        LOG.info("/karatasiDbList1.0.dtd sent to client " + request.getClientAddress());
    }

    /** Shows an index.
     *
     * @param request RestInputStream.
     * @param response RestOutputStream.
     * @throws IOException in case of I/O problems.
     */
    @RestMethod(value = "/")
    public void index(@NotNull final RestInputStream request, @NotNull final RestOutputStream response) throws Exception {
        final String encoding = "UTF-8";
        response.setHeader(Http11Header.CONTENT_LANGUAGE, "en");
        response.setHeader(Http11Header.CONTENT_TYPE, "text/html; charset=" + encoding);
        Util.copy(getClass().getClassLoader().getResourceAsStream("resources/index.html"), response);
        response.flush();
        LOG.info("/ sent to client " + request.getClientAddress());
    }

    /** Extracts the database full name from a URI path.
     * The database full name is the part of the URI path behind the parent path.
     * It may contain special characters, blanks, umlauts, slashes, etc.
     * it may even be the empty string.
     * This is true for the upload as well as the download scenarios.
     *
     * @param parentPath Path which is used to find the full name.
     * @param path the URI path.
     * @return the full name or <code>null</code> if the fullname is empty.
     */
    @Nullable
    public static String extractFullName(@NotNull final String parentPath, @NotNull final String path) {
        if (!path.startsWith(parentPath)) {
            return null;
        }
        String fullName = path.substring(parentPath.length());
        fullName = fullName.startsWith("/") ? fullName.substring(1) : fullName;
        return fullName;
    }

    /** Extracts the database full name from a request URI. The database full name is the last part of the URI path.
     * This is true for the upload as well as the download scenarios.
     * @param parentPath Path which is used to find the full name.
     * @param request the HTTP request.
     * @return the full name or <code>null</code> if the fullname cannot be retrieved.
     */
    @Nullable
    private static String extractFullName(@NotNull final String parentPath, @NotNull final RestInputStream request) {
        final String path = request.getRequestURI().getPath();
        return extractFullName(parentPath, path);
    }
}
