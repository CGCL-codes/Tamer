package comlanka.appengine.sitemap;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import comlanka.appengine.property.ASP;
import comlanka.appengine.property.Property;
import org.xml.sax.SAXException;

abstract class SitemapGenerator<U extends ISitemapUrl, THIS extends SitemapGenerator<U, THIS>> {

    /** 50000 URLs per sitemap maximum */
    public static final int MAX_URLS_PER_SITEMAP = 50000;

    private final String baseUrl;

    private final File baseDir;

    private final String fileNamePrefix;

    private final String fileNameSuffix;

    private final boolean allowMultipleSitemaps;

    private final ArrayList<U> urls = new ArrayList<U>();

    private final W3CDateFormat dateFormat;

    private final int maxUrls;

    private final boolean autoValidate;

    private final boolean gzip;

    private final ISitemapUrlRenderer<U> renderer;

    private int mapCount = 0;

    private boolean finished = false;

    private boolean appengine = true;

    private final ArrayList<File> outFiles = new ArrayList<File>();

    public SitemapGenerator(AbstractSitemapGeneratorOptions<?> options, ISitemapUrlRenderer<U> renderer) {
        baseDir = options.baseDir;
        baseUrl = options.baseUrl;
        appengine = options.appengine;
        fileNamePrefix = options.fileNamePrefix;
        W3CDateFormat dateFormat = options.dateFormat;
        if (dateFormat == null) dateFormat = new W3CDateFormat();
        this.dateFormat = dateFormat;
        allowMultipleSitemaps = options.allowMultipleSitemaps;
        maxUrls = options.maxUrls;
        autoValidate = options.autoValidate;
        gzip = options.gzip;
        this.renderer = renderer;
        fileNameSuffix = gzip ? ".xml.gz" : ".xml";
    }

    /** Add one URL of the appropriate type to this sitemap.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or else write out one sitemap immediately.
	 * @param url the URL to add to this sitemap
	 * @return this
	 */
    public THIS addUrl(U url) {
        if (finished) throw new RuntimeException("Sitemap already printed; you must create a new generator to make more sitemaps");
        UrlUtils.checkUrl(url.getUrl().toString(), baseUrl);
        if (urls.size() == maxUrls) {
            if (!allowMultipleSitemaps) throw new RuntimeException("More than " + maxUrls + " urls, but allowMultipleSitemaps is false.  Enable allowMultipleSitemaps to split the sitemap into multiple files with a sitemap index.");
            if (mapCount == 0) mapCount++;
            writeSiteMap();
            mapCount++;
            urls.clear();
        }
        urls.add(url);
        return getThis();
    }

    /** Add multiple URLs of the appropriate type to this sitemap, one at a time.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or write out one sitemap immediately.
	 * @param urls the URLs to add to this sitemap
	 * @return this
	 */
    public THIS addUrls(Iterable<? extends U> urls) {
        for (U url : urls) addUrl(url);
        return getThis();
    }

    /** Add multiple URLs of the appropriate type to this sitemap, one at a time.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or write out one sitemap immediately.
	 * @param urls the URLs to add to this sitemap
	 * @return this
	 */
    public THIS addUrls(U... urls) {
        for (U url : urls) addUrl(url);
        return getThis();
    }

    /** Add multiple URLs of the appropriate type to this sitemap, one at a time.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or write out one sitemap immediately.
	 * @param urls the URLs to add to this sitemap
	 * @return this
	 * @throws MalformedURLException
	 */
    public THIS addUrls(String... urls) throws MalformedURLException {
        for (String url : urls) addUrl(url);
        return getThis();
    }

    /** Add one URL of the appropriate type to this sitemap.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or else write out one sitemap immediately.
	 * @param url the URL to add to this sitemap
	 * @return this
	 * @throws MalformedURLException
	 */
    public THIS addUrl(String url) throws MalformedURLException {
        U sitemapUrl;
        try {
            sitemapUrl = renderer.getUrlClass().getConstructor(String.class).newInstance(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addUrl(sitemapUrl);
    }

    /** Add multiple URLs of the appropriate type to this sitemap, one at a time.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or write out one sitemap immediately.
	 * @param urls the URLs to add to this sitemap
	 * @return this
	 */
    public THIS addUrls(URL... urls) {
        for (URL url : urls) addUrl(url);
        return getThis();
    }

    /** Add one URL of the appropriate type to this sitemap.
	 * If we have reached the maximum number of URLs, we'll throw an exception if {@link #allowMultipleSitemaps} is false,
	 * or write out one sitemap immediately.
	 * @param url the URL to add to this sitemap
	 * @return this
	 */
    public THIS addUrl(URL url) {
        U sitemapUrl;
        try {
            sitemapUrl = renderer.getUrlClass().getConstructor(URL.class).newInstance(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addUrl(sitemapUrl);
    }

    @SuppressWarnings("unchecked")
    THIS getThis() {
        return (THIS) this;
    }

    /** Write out remaining URLs; this method can only be called once.  This is necessary so we can keep an accurate count for {@link #writeSitemapsWithIndex()}.
	 * 
	 * @return a list of files we wrote out to disk
	 */
    public List<File> write() {
        if (finished) throw new RuntimeException("Sitemap already printed; you must create a new generator to make more sitemaps");
        if (urls.size() == 0 && mapCount == 0) throw new RuntimeException("No URLs added, sitemap would be empty; you must add some URLs with addUrls");
        writeSiteMap();
        finished = true;
        return outFiles;
    }

    /** After you've called {@link #write()}, call this to generate a sitemap index of all sitemaps you generated.  
	 * 
	 */
    public void writeSitemapsWithIndex() {
        if (!finished) throw new RuntimeException("Sitemaps not generated yet; call write() first");
        File outFile = new File(baseDir, "sitemap_index.xml");
        SitemapIndexGenerator sig;
        try {
            sig = new SitemapIndexGenerator.Options(baseUrl, outFile).dateFormat(dateFormat).autoValidate(autoValidate).build();
        } catch (MalformedURLException e) {
            throw new RuntimeException("bug", e);
        }
        sig.addUrls(fileNamePrefix, fileNameSuffix, mapCount).write();
    }

    private void writeSiteMap() {
        if (urls.size() == 0) return;
        String fileNamePrefix;
        if (mapCount > 0) {
            fileNamePrefix = this.fileNamePrefix + mapCount;
        } else {
            fileNamePrefix = this.fileNamePrefix;
        }
        if (true) {
            FileService fileService = FileServiceFactory.getFileService();
            try {
                AppEngineFile file = fileService.createNewBlobFile("text/xml");
                boolean lock = false;
                FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
                PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
                writeSiteMap(out);
                out.close();
                String path = file.getFullPath();
                file = new AppEngineFile(path);
                lock = true;
                writeChannel = fileService.openWriteChannel(file, lock);
                writeChannel.closeFinally();
                BlobKey blobKey = fileService.getBlobKey(file);
                BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();
                Property property = ASP.getProperty("SITEMAP_KEY");
                if (property != null) {
                    BlobKey oldBlobKey = new BlobKey(property.getValue());
                    try {
                        blobStoreService.delete(oldBlobKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ASP.setPropertyValue("SITEMAP_KEY", blobKey.getKeyString());
                DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM);
                Date date = new Date();
                String dateString = dateFormat.format(date);
                ASP.setPropertyValue("SITEMAP_DATE", dateString);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        } else {
            File outFile = new File(baseDir, fileNamePrefix + fileNameSuffix);
            outFiles.add(outFile);
            try {
                OutputStreamWriter out;
                if (gzip) {
                    FileOutputStream fileStream = new FileOutputStream(outFile);
                    GZIPOutputStream gzipStream = new GZIPOutputStream(fileStream);
                    out = new OutputStreamWriter(gzipStream);
                } else {
                    out = new FileWriter(outFile);
                }
                writeSiteMap(out);
                if (autoValidate) SitemapValidator.validateWebSitemap(outFile);
            } catch (IOException e) {
                throw new RuntimeException("Problem writing sitemap file " + outFile, e);
            } catch (SAXException e) {
                throw new RuntimeException("Sitemap file failed to validate (bug?)", e);
            }
        }
    }

    private void writeSiteMap(OutputStreamWriter out) throws IOException {
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" ");
        if (renderer.getXmlNamespaces() != null) {
            out.write(renderer.getXmlNamespaces());
            out.write(' ');
        }
        out.write(">\n");
        for (U url : urls) {
            renderer.render(url, out, dateFormat);
        }
        out.write("</urlset>");
        out.close();
    }

    private void writeSiteMap(PrintWriter out) throws IOException {
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" ");
        if (renderer.getXmlNamespaces() != null) {
            out.write(renderer.getXmlNamespaces());
            out.write(' ');
        }
        out.write(">\n");
        for (U url : urls) {
            renderer.render(url, out, dateFormat);
        }
        out.write("</urlset>");
        out.close();
    }
}
