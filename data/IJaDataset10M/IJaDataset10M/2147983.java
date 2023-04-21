package gov.lanl.adore.demo;

import gov.lanl.arc.ARCException;
import gov.lanl.arc.heritrixImpl.ARCFileWriter;
import gov.lanl.archive.trans.TransProperties;
import gov.lanl.util.uuid.UUIDFactory;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <code>TutorialArcWriter</code> wraps up the resource processing operations.
 * Resource URLs retrieved from a doc are resolved to a byte array then written
 * to the specified ARCFileWriter instance. Once a resource has successfully
 * been written, the resources ARCFile Resolver URL is returned.
 * 
 * @author Ryan Chute <rchute@lanl.gov>
 * 
 */
public class TutorialArcWriter {

    private String arcurl;

    private ARCFileWriter arcwriter;

    /**
     * Initializes a new TutorialArcWriter instance for the defined
     * ARCFileWriter and ARCFileResolver BaseUrl
     * 
     * @param arcwriter
     *            Initialized ARCFileWriter instance
     * @param arcurl
     *            BaseUrl of ARC File Resolver web-app
     */
    public TutorialArcWriter(ARCFileWriter arcwriter, String arcurl) {
        this.arcwriter = arcwriter;
        this.arcurl = arcurl;
    }

    /**
     * Writes resource located at defined url to initialized ARCFileWriter
     * instance.
     * 
     * @param url
     *            URL of resource to be resolved and written to an arc file
     * @param arcmimetype
     *            Mimetype of the resource to be resolved
     * @return ARC File Resolver URL to written resource
     * @throws ARCException
     */
    public String write(String url, String arcmimetype) throws ARCException {
        String arcid = TransProperties.getLocalDataStreamPrefix() + UUIDFactory.generateUUID().toString().substring(9);
        byte[] deref;
        try {
            deref = resolveRef(url);
        } catch (MalformedURLException e) {
            throw new ARCException(e.getMessage());
        } catch (IOException e) {
            throw new ARCException(e.getMessage());
        }
        arcwriter.write(arcid, "0.0.0.0", arcmimetype, deref);
        return composeRef(arcid);
    }

    /**
     * Composes an ARC File Resolver URL for the provided identifier
     * 
     * @param id
     *            Identifier of the resource
     * @return ARC File Resolver URL to written resource
     */
    public String composeRef(String id) {
        String ref = arcurl + "&rfr_id=" + TransProperties.getLocalOpenUrlReferrerID() + "&url_ver=Z39.88-2004&rft_id=" + id;
        return ref;
    }

    /**
     * Gets specified resource from remote URL as byte array
     * 
     * @param url
     *            Location of the resource to resolve
     * @return Btye Array instance of resource
     * @throws MalformedURLException
     * @throws IOException
     */
    public static byte[] resolveRef(String url) throws MalformedURLException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        URL addressurl = new URL(url);
        InputStream in = addressurl.openStream();
        BufferedReader bin = new BufferedReader(new InputStreamReader(in));
        int bufferSize = 4096;
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = in.read(buffer, 0, bufferSize)) != -1) {
            out.write(buffer, 0, bytesRead);
            out.flush();
        }
        return out.toByteArray();
    }
}
