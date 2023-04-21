package net.vermaas.blogger;

import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 * Imports all blog entries found in the specified BlogSource to Blogger.
 * 
 * As part of the import it can validate the http(s) links in your blog entries
 * and report to the log file if the links are still valid. You can use this
 * information to do some clean up after importing the blog entries.
 *
 * By default the tool will read it settings from the {@link DEFAULT_SETTINGS_FILE}
 * file, but you can pass in another settings file as argument.
 *
 * @author Gero Vermaas
 */
public class BloggerImporter {

    private static final String APACHEREDIRECTSFILE = "apacheRedirectsFile";

    private static final String BLOGID = "blogId";

    private static final String REDIRECT_PERMANENT_ = "Redirect permanent ";

    private static final String USERNAME = "userName";

    private static final String BLOGSOURCECLASS = "blogSourceClass";

    private static final String COMMENTSURISUFFIX = "commentsUriSuffix";

    private static final String CURRENTBLOGPATH = "currentBlogPath";

    private static final String FEEDURIBASE = "feedUriBase";

    private static final String IMPORTCOMMENTS = "importComments";

    private static final String IMPORTTRACKERFILE = "importTrackerFile";

    private static final String INCLUDEORIGINALCOMMENTDETAILS = "includeOriginalCommentDetails";

    private static final String PASSWORD = "password";

    private static final String POSTURISUFFIX = "postUriSuffix";

    private static final String RELATIVEHREFSPREFIX = "relativeHrefsPrefix";

    private static final String SETTINGSFILECHANGERELATIVEHREFS = "settingsFilechangeRelativeHrefs";

    private static final String VALIDATELINKS = "validateLinks";

    private Properties settings = new Properties();

    private static final String DEFAULT_SETTINGS_FILE = "bloggerimporter.properties";

    private URL blogPostUrlBase;

    private URL blogPostUrl;

    private boolean includeOriginalCommentDetails;

    private BloggerService bloggerService;

    private static final Logger log = Logger.getLogger(BloggerImporter.class);

    /**
   * Imports all blog entries. What entries to import is determined by
   * the settings file that can be passed in as (only) argument. If
   * no settings file is passed as argument, it will use the default
   * settings file {@link DEfAULT_SETTINGS_FILE}.
   * @param args
   */
    public static void main(String[] args) {
        BloggerImporter bi = new BloggerImporter();
        String settingsFile = DEFAULT_SETTINGS_FILE;
        if (args.length > 0) {
            settingsFile = args[0];
        }
        bi.importBlogEntries(settingsFile);
    }

    /**
   * Import all bog entries.
   * @param settingsFile File containing all required settings. Settings in this
   * file also determine what the source of the blogposts to be imported is.
   */
    void importBlogEntries(String settingsFile) {
        try {
            settings.load(new FileInputStream(settingsFile));
            boolean importComments = getBooleanSetting(IMPORTCOMMENTS);
            BlogSource blogSource = getBlogSource();
            blogSource.setSettings(settings);
            blogPostUrlBase = new URL(getSetting(FEEDURIBASE) + "/" + getSetting(BLOGID));
            log.debug("blogPostUrl: " + blogPostUrlBase);
            blogPostUrl = new URL(blogPostUrlBase.toString() + getSetting(POSTURISUFFIX));
            bloggerService = new BloggerService("PebbleToBloggerImporter");
            bloggerService.setUserCredentials(getSetting(USERNAME), getSetting(PASSWORD));
            includeOriginalCommentDetails = getBooleanSetting(INCLUDEORIGINALCOMMENTDETAILS);
            LinkValidator lv = new LinkValidator(Boolean.parseBoolean(getSetting(SETTINGSFILECHANGERELATIVEHREFS)), getSetting(RELATIVEHREFSPREFIX), getSetting(CURRENTBLOGPATH));
            boolean validateLinks = getBooleanSetting(VALIDATELINKS);
            Map<String, String> oldNewMap = new HashMap<String, String>();
            File importTrackerFile = new File(getSetting(IMPORTTRACKERFILE));
            readImportedBlogIds(importTrackerFile, oldNewMap);
            FileWriter fw = new FileWriter(importTrackerFile, true);
            FileWriter redirectsFile = getRedirectsFW(blogPostUrl);
            BlogPostDetails bpd = null;
            while (blogSource.hasNext()) {
                try {
                    bpd = (BlogPostDetails) blogSource.next();
                    if (oldNewMap.keySet().contains(bpd.getOriginalPostId())) {
                        continue;
                    }
                    if (validateLinks) {
                        lv.validateAndFixLinks(bpd, oldNewMap);
                    }
                    Entry postedEntry = postBlogEntry(bpd.getBlogPost());
                    String postId = getPostId(postedEntry);
                    if (importComments) {
                        importComments(postId, bpd.getComments());
                    }
                    oldNewMap.put(bpd.getOriginalPostId(), bpd.getBlogPost().getId());
                    writeImportedEntryIds(fw, bpd, postedEntry);
                    writeRedirect(redirectsFile, bpd.getOriginalPostId(), postedEntry.getHtmlLink().getHref());
                } catch (InvalidEntryException iee) {
                    if (iee.toString().indexOf("exceeded rate limit") != -1) {
                        if (fw != null) {
                            fw.close();
                        }
                        log.error("Got an invalid entry exception. Could be caused by exceeding rate limit.", iee);
                        System.out.println("Google Blogger returned and InvalidEntryException." + "\nThis is typically caused by exceeding the rate limits for posting." + "\nYou can reset the limit by doing a manual post via the blogger website " + "\n(and removing it aftwards)" + "\nThen restart the BloggerImporter and it will continue where it left off.");
                        break;
                    } else {
                        log.error("Problem with importing blogPost or comment, will continue with rest. " + "Post was: " + bpd.getOriginalPostId(), iee);
                    }
                } catch (ServiceException se) {
                    log.error("Error while posting blogEntry or comments", se);
                    throw new RuntimeException("Could not import blogEntry or comments", se);
                }
            }
            if (fw != null) {
                fw.close();
            }
            if (redirectsFile != null) {
                redirectsFile.close();
            }
        } catch (AuthenticationException ae) {
            throw new RuntimeException(ae);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    String getSetting(String name) {
        return settings.getProperty(name);
    }

    boolean getBooleanSetting(String name) {
        return Boolean.parseBoolean(getSetting(name));
    }

    /**
   * Post the specified blog entry.
   * @param blogPost blog entry to be posted.
   * @return The posted blog entry.
   * @throws java.io.IOException
   * @throws com.google.gdata.util.ServiceException
   */
    Entry postBlogEntry(Entry blogPost) throws IOException, ServiceException {
        Entry postedEntry = bloggerService.insert(blogPostUrl, blogPost);
        log.debug("Posted blog entry: " + postedEntry.getHtmlLink().getHref());
        return postedEntry;
    }

    /**
   * Posts all comment entries in the list to the blogPost identified by
   * postId.
   * @param postId The blogpost to which the comments must be posted.
   * @param comments The list of comments.
   * @throws java.io.IOException
   * @throws com.google.gdata.util.ServiceException
   */
    void importComments(String postId, List<Entry> comments) throws IOException, ServiceException {
        URL commentsUrl = getCommentPostUrl(postId);
        for (Entry comment : comments) {
            if (includeOriginalCommentDetails) {
                appendDetailsToBody(comment);
            }
            bloggerService.insert(commentsUrl, comment);
            log.debug("Posted comment entry");
        }
    }

    /**
   * Extracts the posId from a blogpost.
   * @param blogPost The blog post from which the postId must be extracted.
   * @return postId of blog entry.
   */
    String getPostId(Entry blogPost) {
        String selfLinkHref = blogPost.getSelfLink().getHref();
        String[] tokens = selfLinkHref.split("/");
        return tokens[tokens.length - 1];
    }

    /**
   * Creates the URL for posting comments for a specific post.
   * @param postId The ID or the blogpost for which then comments URL
   * must be created.
   * @return
   * @throws java.net.MalformedURLException
   */
    URL getCommentPostUrl(String postId) throws MalformedURLException {
        return new URL(getSetting(FEEDURIBASE) + "/" + getSetting(BLOGID) + "/" + postId + getSetting(COMMENTSURISUFFIX));
    }

    /**
   * Append the details of the comments to the comment body. This is done
   * because Blogger does not allow you to set the author details of
   * comments through the API. It always uses the name of the signed-in
   * user for this.
   * @param comment The comment entry for which the body must be updated.
   */
    void appendDetailsToBody(Entry comment) {
        String body = comment.getPlainTextContent();
        body = html2text(body);
        body += "<br/><i><b>Note:</b> Comment imported. ";
        List<Person> authors = comment.getAuthors();
        if (authors.size() > 0) {
            body += "Original ";
            Person p = authors.get(0);
            if (p.getName() != null) {
                body += " by " + p.getName();
            }
            if (p.getEmail() != null || p.getUri() != null) {
                body += " (";
            }
            if (p.getEmail() != null) {
                body += " email: " + p.getEmail();
            }
            if (p.getEmail() != null) {
                body += " website: <a href\"" + p.getUri() + "\">" + p.getUri() + "</a>";
            }
            if (p.getEmail() != null || p.getUri() != null) {
                body += " )";
            }
        }
        body += " at " + comment.getPublished().toUiString();
        body += "</i>";
        comment.setContent(new PlainTextConstruct(body));
    }

    /**
   * Reads the log file containing the IDs of the blog entries that
   * have already been imported.
   *
   * @param importTrackerFile File containing the IDs of imported entries
   * @param importedEntries Map containing ID of original entry as key and
   * URL of new entry as value.
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
    void readImportedBlogIds(File importTrackerFile, Map<String, String> importedEntries) throws FileNotFoundException, IOException {
        if (!importTrackerFile.exists()) {
            log.info("No log file containing imported entries found, will import all.");
            return;
        }
        BufferedReader br = new BufferedReader(new FileReader(importTrackerFile));
        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, "\t");
            String origLink = st.nextToken();
            String newLlink = st.nextToken();
            importedEntries.put(origLink, newLlink);
        }
        br.close();
    }

    /**
   * Rudementatary converter of HTML to plain text. Simply removed all
   * HTML tags.
   *
   * @param strIn The text containing HTML tags.
   * @return Plain text version of html text.
   */
    String html2text(String strIn) {
        return strIn.toString().replaceAll("\\<.*?>", "");
    }

    BlogSource getBlogSource() {
        Class blogSourceClass;
        try {
            blogSourceClass = Class.forName(settings.getProperty(BLOGSOURCECLASS));
            return (BlogSource) blogSourceClass.newInstance();
        } catch (Exception ex) {
            log.error("Could not instantiate blogSource: " + settings.getProperty(BLOGSOURCECLASS));
            throw new RuntimeException(ex);
        }
    }

    /**
   * Save the details of the imported entry to the log file.
   * @param fw FileWriter to write details to.
   * @param bpd Details of the original blog post.
   * @param postedEntry Details of the posted entry to Blogger.
   * @throws java.io.IOException
   */
    void writeImportedEntryIds(FileWriter fw, BlogPostDetails bpd, Entry postedEntry) throws IOException {
        fw.write(bpd.getOriginalPostId());
        fw.write("\t");
        fw.write(postedEntry.getHtmlLink().getHref());
        fw.write(System.getProperties().getProperty("line.separator"));
        fw.flush();
    }

    /**
   * Writes details of a Apache redirect statement to a FileWriter.
   * @param redirectsFw FileWriter for the Apache redirects file.
   * @param orgPostId The original post Id on the source blog (this is excluding
   *                  the path to the root of the blog).
   * @param newUrl The new location of the blog post at Blogger.
   * @throws java.io.IOException
   */
    void writeRedirect(FileWriter redirectsFw, String orgPostId, String newUrl) throws IOException {
        redirectsFw.write(REDIRECT_PERMANENT_);
        redirectsFw.write("/" + getSetting(CURRENTBLOGPATH));
        redirectsFw.write(orgPostId);
        redirectsFw.write(" ");
        redirectsFw.write(newUrl);
        redirectsFw.write(System.getProperties().getProperty("line.separator"));
        redirectsFw.flush();
    }

    /**
   * Returns a FileWriter for the Apache redirect file.
   * @param blogFeedUrl The Url of the atom feed at Blogger.
   * @return A FileWriter for the Apache redirect file.
   * @throws java.io.IOException
   */
    private FileWriter getRedirectsFW(URL blogFeedUrl) throws IOException {
        FileWriter fw;
        File redirFile = new File(getSetting(APACHEREDIRECTSFILE));
        if (redirFile.exists()) {
            fw = new FileWriter(getSetting(APACHEREDIRECTSFILE), true);
        } else {
            fw = new FileWriter(getSetting(APACHEREDIRECTSFILE));
            writeRedirect(fw, "/rss.xml", blogFeedUrl.toExternalForm());
        }
        return fw;
    }
}
