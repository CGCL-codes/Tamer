package org.exist.http.webdav.methods;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.exist.EXistException;
import org.exist.collections.Collection;
import org.exist.collections.IndexInfo;
import org.exist.collections.triggers.TriggerException;
import org.exist.security.PermissionDeniedException;
import org.exist.security.User;
import org.exist.storage.BrokerPool;
import org.exist.storage.DBBroker;
import org.exist.util.Lock;
import org.exist.util.LockException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author wolf
 */
public class Put extends AbstractWebDAVMethod {

    public Put(BrokerPool pool) {
        super(pool);
    }

    public void process(User user, HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        File tempFile = saveRequestContent(request);
        String url = tempFile.toURI().toASCIIString();
        String contentType = request.getContentType();
        DBBroker broker = null;
        Collection collection = null;
        boolean collectionLocked = true;
        try {
            broker = pool.get(user);
            if (path == null) path = "";
            if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
            int p = path.lastIndexOf('/');
            if (p < 1) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "No collection specified for PUT");
                return;
            }
            String collectionName = path.substring(0, p);
            path = path.substring(p + 1);
            collection = broker.openCollection(collectionName, Lock.WRITE_LOCK);
            if (collection == null) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Parent collection " + collectionName + " not found");
                return;
            }
            if (collection.hasChildCollection(path)) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Cannot overwrite an existing collection with a resource");
                return;
            }
            if (contentType == null) {
                contentType = URLConnection.guessContentTypeFromName(path);
            }
            LOG.debug("storing document " + path + "; content-type = " + contentType);
            if (contentType == null || contentType.equalsIgnoreCase("text/xml") || contentType.equals("application/xml")) {
                InputSource is = new InputSource(url);
                IndexInfo info = collection.validate(broker, path, is);
                collection.release();
                collectionLocked = false;
                collection.store(broker, info, is, false);
            } else {
                byte[] chunk = new byte[4096];
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                FileInputStream is = new FileInputStream(tempFile);
                int l;
                while ((l = is.read(chunk)) > -1) {
                    os.write(chunk, 0, l);
                }
                collection.addBinaryResource(broker, path, os.toByteArray());
            }
        } catch (EXistException e) {
            throw new ServletException("Failed to store resource: " + e.getMessage(), e);
        } catch (PermissionDeniedException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (TriggerException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SAXException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (LockException e) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
        } finally {
            if (collectionLocked) collection.release();
            pool.release(broker);
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    private File saveRequestContent(HttpServletRequest request) throws IOException {
        ServletInputStream is = request.getInputStream();
        int len = request.getContentLength();
        File tempFile = File.createTempFile("exist", ".tmp");
        OutputStream os = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int count, l = 0;
        do {
            count = is.read(buffer);
            if (count > 0) os.write(buffer, 0, count);
            l += count;
        } while (l < len);
        os.close();
        return tempFile;
    }
}
