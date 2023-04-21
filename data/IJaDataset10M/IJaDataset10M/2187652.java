package net.sourceforge.pebble.ant.metaweblog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.xmlrpc.XmlRpcClient;

/**
 * Ant task to post a file to a blog via the MetaWeblog API.
 *
 * @author    Simon Brown
 */
public class NewMediaObjectTask extends Task {

    private String blogid;

    private String username;

    private String password;

    private String src;

    private String dest;

    private String url;

    private String handler = "metaWeblog";

    /**
   * Performs the work of this task.
   *
   * @throws org.apache.tools.ant.BuildException   if something goes wrong
   */
    public void execute() throws BuildException {
        try {
            System.out.println("Calling " + handler + ".newMediaObject at " + url);
            System.out.println(" blogid=" + blogid);
            System.out.println(" username=" + username);
            System.out.println(" password=********");
            System.out.println(" name=" + dest);
            System.out.println(" type=" + "");
            XmlRpcClient xmlrpc = new XmlRpcClient(url);
            Vector params = new Vector();
            params.add(blogid);
            params.add(username);
            params.add(password);
            Hashtable struct = new Hashtable();
            struct.put("name", dest);
            struct.put("type", "");
            struct.put("bits", readFile());
            params.add(struct);
            struct = (Hashtable) xmlrpc.execute(handler + ".newMediaObject", params);
            System.out.println("URL for media object is " + struct.get("url"));
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private byte[] readFile() throws Exception {
        byte bytes[];
        File file = new File(src);
        InputStream in = new FileInputStream(src);
        bytes = new byte[(int) file.length()];
        in.read(bytes);
        in.close();
        return bytes;
    }

    /**
   * Sets the blog id.
   *
   * @param blogid    a String
   */
    public void setBlogid(String blogid) {
        this.blogid = blogid;
    }

    /**
   * Sets the XML-RPC username.
   *
   * @param username    a String
   */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
   * Sets the XML-RPC password.
   *
   * @param password    a String
   */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
   * Sets the URL of the XML-RPC call.
   *
   * @param url   the URL as a String
   */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
   * Sets the name of the XML-RPC Blogger API handler (e.g. "blogger").
   *
   * @param handler   the name of the handler as a String
   */
    public void setHandler(String handler) {
        this.handler = handler;
    }

    /**
   * Sets the source file.
   *
   * @param src   the source as a String
   */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
   * Sets the destination file.
   *
   * @param dest   the destination as a String
   */
    public void setDest(String dest) {
        this.dest = dest;
    }
}
