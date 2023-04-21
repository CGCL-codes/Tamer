package simple.http.serve;

import simple.http.Response;
import simple.http.Request;

/**
 * This is used to display the contents of a directory when that 
 * directory is referenced. This will generate a HTML response 
 * that will reference every file in that directory using the 
 * <code>File.list</code> method. This creates the HTML contents 
 * of the directory referenced
 * <p>
 * This must be given a URI style path, that is a path in the 
 * form of a URI path, UNIX style, for example /a/b. This allows 
 * the <code>Component</code> more flexibility across platforms. 
 * The path given MUST be relative to the current directory. So 
 * if the path is ./path/ then the path given must be /path/. 
 * Backward references are not permitted so a path like ../
 * is illegal.
 *
 * @author Niall Gallagher
 */
final class DirectoryComponent extends IndexedComponent {

    /**
    * Constructs a HTML <code>Component</code> to represent the path
    * specified. The <code>handle</code> method will generate a HTML 
    * listing of all the files in the directory with hyperlinks to 
    * them. 
    *
    * @param target this is the HTTP request URI for this resource
    * @param context the root context of this directory resource
    */
    public DirectoryComponent(Context context, String target) {
        super(context, target);
    }

    /**
    * This will generate the HTML listing of the contents of the
    * referenced directory. Because this listing may be for a 
    * directory that is some directories in depth there may be a 
    * need to give paths relative to the current path. For example 
    * if the URI path "/pub/bin" were the directory "/pub/bin/" 
    * then if the relative URL "README.txt" was clicked on the 
    * page "/pub/bin" the browser would think that it would have 
    * to look for that using the path "/usr/README.txt" because 
    * the browser thinks that "/pub/bin" is not a directory but 
    * rather a resource in "/pub". For a browser to realize that 
    * the URL path it is looking at is a directory and thus look
    * for relative URLs in it, it needs a "/" ending for this.
    * <p>
    * If the method used is HEAD then this will write only the
    * headers and will subsequently close the pipeline. However
    * this will not handle POST, OPTIONS, TRACE, DELETE or PUT
    * requests and will generate a "501 Not Implemented" message
    * if attempted, see RFC 2616 sec 5.1.1.
    *
    * @param req the <code>Request</code> to be processed
    * @param resp the <code>Response</code> to be processed
    *
    * @exception Exception this is thrown if an uncaptured 
    * <code>Exception</code> propagates
    */
    protected void process(Request req, Response resp) throws Exception {
        if (req.getDate("If-Modified-Since") < getLastModified()) {
            Format format = context.getFormat();
            byte[] text = format.getContents(context, target);
            resp.setDate("Date", System.currentTimeMillis());
            resp.setDate("Last-Modified", getLastModified());
            resp.set("Content-Type", format.getContentType());
            resp.setContentLength(text.length);
            if (req.getMethod().equals("HEAD")) {
                resp.commit();
            } else if (req.getMethod().equals("GET")) {
                resp.getOutputStream().write(text);
                resp.getOutputStream().close();
            } else {
                handle(req, resp, 501);
            }
        } else {
            handle(req, resp, 304);
        }
    }
}
