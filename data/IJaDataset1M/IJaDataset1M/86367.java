package gov.lanl.adore.djatoka.repo;

import gov.lanl.adore.djatoka.util.IOUtils;
import gov.lanl.util.resource.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.log4j.Logger;

/**
 * Simplifies OpenURL requests to adore-resource-resolver
 * @author Ryan Chute
 */
public class RepoResolverProxy {

    static Logger logger = Logger.getLogger(RepoResolverProxy.class.getName());

    public static String SVCID_ADORE4_GETRESOURCE = "info:lanl-repo/svc/getDatastream";

    private static String SVC_PROFILE_ADORE4 = "openurl-aDORe4";

    public static String SVCID_ADORE8_GETMETADATA = "info:lanl-repo/svc/getMetadata";

    private static String SVC_PROFILE_ADORE8 = "openurl-aDORe8";

    private URL defaultBaseurl;

    /** 
     * Default constructor; requires explicit setDefaultBaseurl() call
     */
    public RepoResolverProxy() {
    }

    /** 
     * Constructor used to initially define the service url
     * @param baseurl
     *        Base URL of the adore-resource-resolver service <br>
     *        (i.e. http://localhost:8080/adore-resource-resolver)
     *        
     */
    public RepoResolverProxy(URL baseurl) {
        if (baseurl == null) throw new NullPointerException("empty baseurl");
        this.defaultBaseurl = baseurl;
    }

    /**
     * Gets a Resource object given a resourceId and the collectionId
     * in which it resides
     * @param collectionId
     *        the repositoryId of resourceId 
     *        (e.g. info:lanl-repo/resourcedb/560bbd27-9558-4742-bac4-df21cba5c4cf)
     * @param resourceId
     *        the unique identifier of the datastream
     *        (e.g. info:lanl-repo/ds/d5bb085a-e342-489e-a416-c46f1a21dc53)
     * @return
     *        A Resource object containing bitstream and content-type 
     * @throws Exception
     */
    public Resource get(String collectionId, String resourceId, String svcId) throws Exception {
        if (defaultBaseurl == null) throw new NullPointerException("empty defaultBaseurl");
        URL baseUrl;
        try {
            baseUrl = new URL(defaultBaseurl.toString() + "/" + collectionId.substring(collectionId.lastIndexOf("/") + 1) + "/" + (svcId.equals(SVCID_ADORE8_GETMETADATA) ? SVC_PROFILE_ADORE8 : SVC_PROFILE_ADORE4));
            logger.debug("Service URL: " + baseUrl);
        } catch (MalformedURLException e) {
            throw new Exception(e);
        }
        return get(baseUrl.toString(), resourceId, svcId, false);
    }

    public static String getURL(String serviceUrl, String resourceId, String svcId, boolean appendProfile) throws Exception {
        if (svcId == null) svcId = SVCID_ADORE4_GETRESOURCE;
        if (appendProfile) serviceUrl += "/" + ((svcId.equals(SVCID_ADORE8_GETMETADATA)) ? SVC_PROFILE_ADORE8 : SVC_PROFILE_ADORE4);
        String openurl = serviceUrl + "?url_ver=Z39.88-2004" + "&rft_id=" + URLEncoder.encode(resourceId, "UTF-8") + "&svc_id=" + svcId;
        return openurl;
    }

    /**
     * Gets a Resource object given a resourceId and service url 
     * @param serviceUrl
     *        Service URL for repo in which resource resides <br>
     *        (e.g. http://localhost:8080/adore-resource-resolver/d5bb085a-e342-489e-a416-c46f1a21dc53/openurl-aDORe4)
     * @param resourceId
     *        the unique identifier of the datastream
     *        (e.g. info:lanl-repo/ds/d5bb085a-e342-489e-a416-c46f1a21dc53)
     * @param svcId
     *        the service to perform on the resourceId
     *        (e.g. info:lanl-repo/svc/getDatastream)
     * @return
     *        A Resource object containing bitstream and content-type 
     * @throws Exception
     */
    public Resource get(String serviceUrl, String resourceId, String svcId, boolean appendProfile) throws Exception {
        Resource resource = new Resource();
        String openurl = getURL(serviceUrl, resourceId, svcId, appendProfile);
        logger.debug("OpenURL Request: " + openurl);
        URL url;
        try {
            url = new URL(openurl);
            HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
            int code = huc.getResponseCode();
            if (code == 200) {
                InputStream is = huc.getInputStream();
                resource.setBytes(IOUtils.getByteArray(is));
                resource.setContentType(huc.getContentType());
            } else if (code == 404) {
                return null;
            } else {
                logger.error("An error of type " + code + " occurred for " + url.toString());
                throw new Exception("Cannot get " + url.toString());
            }
        } catch (MalformedURLException e) {
            throw new Exception("A MalformedURLException occurred for " + openurl);
        } catch (IOException e) {
            throw new Exception("An IOException occurred attempting to connect to " + openurl);
        }
        return resource;
    }

    /**
     * Gets the base Repo Resolver Service URL
     * @return
     *        Service BaseUrl
     */
    public URL getDefaultBaseurl() {
        return defaultBaseurl;
    }

    /**
     * Sets the base Repo Resolver Service URL
     * @param baseurl
     *        Service URL for repo in which resource resides <br>
     *        (e.g. http://localhost:8080/adore-resource-resolver/d5bb085a-e342-489e-a416-c46f1a21dc53/openurl-aDORe4)
     */
    public void setDefaultBaseurl(URL baseurl) {
        this.defaultBaseurl = baseurl;
    }

    /**
     * Main Method - Parses command line args<br>
     * 
     * Expects the following args:<br>
     *   [baseUrl]<br>
     *        base Resource Resolver Service URL<br>
     *   [collectionId]<br>
     *         the repositoryId of resourceId<br>
     *   [resourceId]<br>
     *        the unique identifier of the datastream<br>
     *   [outputFile]<br>
     *        path to output file
     * @param args
     *        String Array containing processing configurations
     */
    public static void main(String[] args) {
        RepoResolverProxy resolver;
        try {
            if (args.length != 4) {
                System.out.println("Usage: gov.lanl.adore.djatoka.repo.RepoResolverProxy [baseUrl] [collectionId] [resourceId] [outputFile]");
                System.exit(0);
            }
            long s = System.currentTimeMillis();
            resolver = new RepoResolverProxy(new URL(args[0]));
            Resource resource = resolver.get(args[1], args[2], RepoResolverProxy.SVCID_ADORE4_GETRESOURCE);
            System.out.println("Time to get resource: " + (System.currentTimeMillis() - s));
            System.out.println("Content-type: " + resource.getContentType());
            System.out.println("Writing file to: " + args[3]);
            FileOutputStream fos = new FileOutputStream(new File(args[3]));
            fos.write(resource.getBytes());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
