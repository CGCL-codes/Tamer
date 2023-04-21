package ounl.otec.mace.contextserver.content;

import java.io.*;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.secure.SecureXmlRpcClient;
import org.apache.log4j.Logger;
import ounl.otec.mace.contextserver.ServerConstants;

/**
 * ContextBlogger 2006 Date: 7.11.2006
 * 
 * @author Bashar al takrouri
 * @version 1.0
 * @author Tim de Jong
 * @version 1.1
 * @comment Changed class name to WordpressBlog XML-RPC protocol usage class to
 *          post content to wordpress blog. Req: commons-codec-1.3.jar
 *          xmlrpc-2.0 To use the media object you should provide the name of
 *          the file to be uploaded. e.g "c:/Sunset.jpg"
 */
public class WordpressBlog {

    private XmlRpcClient sclient;

    static Logger m_wordpressLogger = Logger.getLogger(WordpressBlog.class.getName());

    /**
	 * The WordpressBlog class constructor
	 */
    public WordpressBlog() {
        try {
            URL url = new URL(ServerConstants.K_WORDPRESS_XML_RPC_URL);
            if ("https".equalsIgnoreCase(url.getProtocol())) {
                sclient = new SecureXmlRpcClient(url);
            } else {
                sclient = new XmlRpcClient(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
	 * The post method is used to create a new post and send this post to the
	 * blogging server
	 * 
	 * @param categories
	 *            A string array contains the categories for the created post.
	 * @param title
	 *            The title of the new post.
	 * @param description
	 *            The blog body itself.
	 * @return actual postID from the blogger server.
	 */
    public String post(String categories, String title, String description) {
        String result = null;
        Vector<Object> params = new Vector<Object>();
        params.add("0");
        params.add(ServerConstants.K_BLOG_USERNAME);
        params.add(ServerConstants.K_BLOG_PASSWORD);
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        String category[] = { categories };
        ht.put("categories", category);
        ht.put("title", title);
        ht.put("description", description);
        params.add(ht);
        params.add(new Boolean(true));
        String preview = "false";
        if (null != preview && !"true".equals(preview)) {
            try {
                result = (String) sclient.execute("metaWeblog.newPost", params);
            } catch (Exception e) {
                e.printStackTrace();
                m_wordpressLogger.error(e.getMessage());
            }
        }
        return result;
    }

    /**
	 * The getBytesFromFile method is used to Returns the contents of the file
	 * in a byte array.
	 * <p>
	 * This method is used by the media object method to get the file content
	 * and convert it to a stream of bits that can be serialized later on to the
	 * server.
	 * 
	 * @param file
	 *            This is the file that you want to read.
	 * @return This retrun a serial of bytes in bye array format.
	 */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        is.close();
        return bytes;
    }

    /**
	 * The MediaObject method is used post or upload a media object to the
	 * blogger server.
	 * 
	 * @param SourceName
	 *            This is the name of the source file.
	 * @param fname
	 *            Thsi is the name of the file on the server after uploading it.
	 * @return The url string for the posted file on the server.
	 */
    public String MediaObject(byte[] tempfl) {
        String url1 = "";
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        try {
            ht.put("name", "files/" + "xx.jpg");
            ht.put("bits", tempfl);
            Vector<Object> params = new Vector<Object>();
            params.add("0");
            params.add(ServerConstants.K_BLOG_USERNAME);
            params.add(ServerConstants.K_BLOG_PASSWORD);
            params.add(ht);
            Hashtable tempObj = new Hashtable();
            tempObj = (Hashtable) sclient.execute("metaWeblog.newMediaObject", params);
            url1 = tempObj.get("url").toString();
        } catch (Exception e) {
        }
        return url1;
    }

    /**
	 * The GetEntries method is used to Returns the contents of the blog entry
	 * "a post entry".
	 * 
	 * @param PostNum
	 *            This is the ID of blog tob e retrieved.
	 * @return A hashtable contains all the entry fields are retrieved.
	 */
    public Hashtable GetEntries(String PostNum) {
        Vector<String> params = new Vector<String>();
        params.add(PostNum);
        params.add(ServerConstants.K_BLOG_USERNAME);
        params.add(ServerConstants.K_BLOG_PASSWORD);
        try {
            Hashtable result = (Hashtable) sclient.execute("metaWeblog.getPost", params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * The GetRecentEntries method is used to Returns the contents of last
	 * posted blog entries.
	 * 
	 * @param BlogNum
	 *            The entier blog ID.
	 * @param NumOfPosts
	 *            This is the the number of how many entries to be retrived.
	 * @return This is a vector contains all the posts that have been retrieved
	 *         by the process.
	 * 
	 */
    public Vector GetRecentEntries(String BlogNum, int NumOfPosts) {
        Vector<String> params = new Vector<String>();
        params.add(BlogNum);
        params.add(ServerConstants.K_BLOG_USERNAME);
        params.add(ServerConstants.K_BLOG_PASSWORD);
        params.add("'" + NumOfPosts + '"');
        try {
            Vector result = (Vector) sclient.execute("metaWeblog.getRecentPosts", params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * The GetCategories method is used to Returns the an XML string for all the
	 * categories.
	 * 
	 * @return XML string contains all the retirved categories from the blogger
	 *         server. <?xml version='1.0'?> <categoryList> <category>
	 *         <categoryId>...</categoryId> <categoryName>...</categoryName>
	 *         </category> </categoryList>
	 * 
	 */
    public String GetCategories() {
        Vector<String> params = new Vector<String>();
        params.add(new String("0"));
        params.add(ServerConstants.K_BLOG_USERNAME);
        params.add(ServerConstants.K_BLOG_PASSWORD);
        String results = "<?xml version='1.0'?>" + "\n" + "<categoryList>" + "\n";
        try {
            Vector result = (Vector) sclient.execute("metaWeblog.getCategories", params);
            Hashtable a = null;
            while (!(result.isEmpty())) {
                a = (Hashtable) result.remove(0);
                results = results + "<category>" + "\n";
                results = results + "<categoryId>" + a.get("categoryId").toString() + "</categoryId>" + "\n";
                results = results + "<categoryName>" + a.get("categoryName").toString() + "</categoryName>" + "\n";
                results = results + "</category>" + "\n";
            }
            results = results + "</categoryList>";
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
