package webwatcher;

import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.tree.*;

/**
 * @author   Sverre H. Huseby
 *           &lt;<A HREF="mailto:shh@thathost.com">shh@thathost.com</A>&gt;
 * @version  $Id: WebPage.java 16 2010-08-22 22:14:44Z miraculix0815 $
 */
public final class WebPage extends Observable implements Runnable, MutableTreeNode, Serializable {

    private static final int MAX_THREADS = 7;

    private static Object waitObject = new Object();

    private static int numThreads = 0;

    private PropertyProvider prop;

    private URL url = null;

    /** The title, or something. */
    private String Title;

    /** Personal annotation. */
    private String description;

    /** Page when last seen by user. */
    private SmarterChecksum csInitial = new SmarterChecksum();

    /** From previous compare. */
    private SmarterChecksum csLast = new SmarterChecksum();

    private State status;

    private MutableTreeNode Parent;

    private static Thread getThread(Runnable runnable) {
        synchronized (waitObject) {
            while (numThreads == MAX_THREADS) {
                try {
                    waitObject.wait();
                } catch (InterruptedException e) {
                }
            }
            ++numThreads;
        }
        Thread ret = new Thread(runnable, "refresh thread " + numThreads);
        ret.setDaemon(true);
        ret.setPriority(Thread.MIN_PRIORITY);
        return ret;
    }

    private static void returnThread(Thread t) {
        synchronized (waitObject) {
            if (--numThreads < 0) numThreads = 0;
            waitObject.notify();
        }
        t = null;
    }

    private String extractTitle(String content) {
        String lc;
        int idxStart, idxEnd;
        String tagStart = "<title>";
        String tagEnd = "</title>";
        lc = content.toLowerCase();
        if ((idxStart = lc.indexOf(tagStart)) < 0) return "";
        idxStart += tagStart.length();
        if ((idxEnd = lc.indexOf(tagEnd, idxStart)) < 0) return "";
        return content.substring(idxStart, idxEnd).trim();
    }

    private int getPage(StringBuffer ret) throws IOException {
        Properties sysProp;
        int ResponseCode = HttpURLConnection.HTTP_OK;
        BufferedReader br = null;
        try {
            URLConnection con = url.openConnection();
            con.setDefaultUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(false);
            if (con instanceof HttpURLConnection) {
                HttpURLConnection httpcon = (HttpURLConnection) con;
                ResponseCode = httpcon.getResponseCode();
                if (ResponseCode != httpcon.HTTP_OK) {
                    httpcon.disconnect();
                    return (ResponseCode);
                }
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                int NumberOfLines = 0;
                while ((line = br.readLine()) != null) {
                    ret.append(line + "\n");
                    ++NumberOfLines;
                }
                httpcon.disconnect();
            } else {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    ret.append(line + "\n");
                }
            }
        } catch (IOException e) {
        } finally {
            if (br != null) br.close();
        }
        return ResponseCode;
    }

    private int updateChecksum(StringBuffer sb) throws IOException {
        int ResponseCode = getPage(sb);
        csLast.calculate(sb);
        if (Title == null || Title.length() == 0) setTitle(extractTitle(sb.toString()));
        return (ResponseCode);
    }

    private void doRefreshThreadStuff() {
        if (csInitial.isInitialized() && !csLast.equals(csInitial)) setStatus(State.PAGE_UPDATED); else {
            configureProxy();
            try {
                StringBuffer sb = new StringBuffer();
                if (updateChecksum(sb) != HttpURLConnection.HTTP_OK) {
                    setStatus(State.ERROR);
                } else if (csLast.equals(csInitial)) setStatus(State.PAGE_NOT_UPDATED); else {
                    setStatus(State.PAGE_UPDATED);
                    DiskLog.log(this.Title, sb.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                setStatus(State.ERROR);
            }
        }
    }

    private void configureProxy() {
        Properties sysProp = System.getProperties();
        if (prop == null) {
            sysProp.put("http.proxyHost", prop.getProperty("proxy.host"));
            sysProp.put("http.proxyPort", prop.getProperty("proxy.port"));
        } else if (prop.getBooleanProperty("useProxy")) {
            sysProp.put("http.proxyHost", prop.getProperty("proxy.host"));
            sysProp.put("http.proxyPort", prop.getProperty("proxy.port"));
        } else {
            sysProp.remove("http.proxyHost");
            sysProp.remove("http.proxyPort");
        }
    }

    private String readLine(BufferedReader in) throws IOException {
        String ret;
        ret = in.readLine();
        if (ret == null) throw new EOFException("unexpected end of file reading URL file");
        return ret;
    }

    private void setStatus(State status) {
        this.status = status;
        setChanged();
        notifyObservers();
    }

    private void setStatusWithoutNotify(State status) {
        this.status = status;
    }

    static final int compare(WebPage x1, WebPage x2, boolean changedOnTop, boolean ignoreCaseInSort) {
        if (changedOnTop) {
            if (x1.status == State.PAGE_UPDATED && x2.status != State.PAGE_UPDATED) return -1;
            if (x1.status != State.PAGE_UPDATED && x2.status == State.PAGE_UPDATED) return 1;
        }
        if (ignoreCaseInSort) return x1.getSomeTitle().compareToIgnoreCase(x2.getSomeTitle());
        return x1.getSomeTitle().compareTo(x2.getSomeTitle());
    }

    static final void sortArray(WebPage[] x, boolean changedOnTop, boolean ignoreCaseInSort) {
        int q, w, smallest;
        WebPage tmp;
        for (q = 0; q < x.length; q++) {
            smallest = q;
            for (w = q + 1; w < x.length; w++) {
                if (compare(x[w], x[smallest], changedOnTop, ignoreCaseInSort) < 0) smallest = w;
            }
            tmp = x[q];
            x[q] = x[smallest];
            x[smallest] = tmp;
        }
    }

    void load(BufferedReader in) throws IOException {
        String s;
        s = readLine(in);
        url = new URL(s);
        s = readLine(in);
        Title = s;
        s = readLine(in);
        csInitial.setChecksum(s);
        s = readLine(in);
        csLast.setChecksum(s);
        description = "";
        for (; ; ) {
            s = readLine(in);
            if (s.equals(".")) break;
            if (description.length() > 0) description += "\n";
            if (s.charAt(0) == '.') description += s.substring(1); else description += s;
        }
        if (csInitial.isInitialized() && !csLast.equals(csInitial)) setStatusWithoutNotify(State.PAGE_UPDATED); else setStatusWithoutNotify(State.NO_RESULT);
    }

    void save(BufferedWriter out) throws IOException {
        String s;
        int pos, next, len;
        if (url != null) {
            s = url.toString();
            out.write(s, 0, s.length());
        }
        out.newLine();
        if (Title != null) out.write(Title, 0, Title.length());
        out.newLine();
        if (csInitial != null) {
            s = csInitial.toString();
            out.write(s, 0, s.length());
        }
        out.newLine();
        if (csLast != null) {
            s = csLast.toString();
            out.write(s, 0, s.length());
        }
        out.newLine();
        if (description != null) {
            for (pos = 0; pos < description.length(); pos = next + 1) {
                next = description.indexOf("\n", pos);
                if (next < 0) next = description.length();
                len = next - pos;
                if (len > 0 || next < description.length()) {
                    if (description.charAt(pos) == '.') out.write('.');
                    out.write(description, pos, len);
                    out.newLine();
                }
            }
        }
        out.write('.');
        out.newLine();
    }

    public enum State {

        NO_RESULT, QUEUED, WORKING, ERROR, PAGE_NOT_UPDATED, PAGE_UPDATED
    }

    public WebPage(PropertyProvider prop) {
        this.prop = prop;
        setStatusWithoutNotify(State.NO_RESULT);
    }

    public WebPage(PropertyProvider prop, URL url) {
        this(prop);
        setURL(url);
    }

    public WebPage(PropertyProvider prop, String url) throws MalformedURLException {
        this(prop);
        setURL(url);
    }

    public URL getURL() {
        return url;
    }

    public void setURL(URL url) {
        if (!url.equals(this.url)) {
            this.url = url;
            setStatus(State.NO_RESULT);
        }
    }

    public void setURL(String url) throws MalformedURLException {
        setURL(new URL(url));
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        if (this.Title == null || title.compareTo(this.Title) != 0) {
            this.Title = title;
            setChanged();
            notifyObservers();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (this.description == null || description.compareTo(this.description) != 0) {
            this.description = description;
            setChanged();
            notifyObservers();
        }
    }

    public String getSomeTitle() {
        if (Title != null && Title.length() > 0) return Title;
        if (url != null) return url.toString();
        return "";
    }

    public void refresh() {
        Thread thread;
        synchronized (this) {
            if (status == State.WORKING || status == State.QUEUED) return;
            setStatus(State.QUEUED);
        }
        thread = getThread(this);
        setStatus(State.WORKING);
        thread.start();
    }

    public State getStatus() {
        return status;
    }

    public String getStatusText() {
        switch(status) {
            case NO_RESULT:
                return "Not yet checked";
            case QUEUED:
                return "Queued for checking";
            case WORKING:
                return "Checking...";
            case ERROR:
                return "Couldn't access";
            case PAGE_NOT_UPDATED:
                return "Not changed since last visited";
            case PAGE_UPDATED:
                return "Changed since last visited";
            default:
                return "Internal error in WebPage.java";
        }
    }

    public void markAsSeen() {
        csInitial = csLast;
        setStatus(State.PAGE_NOT_UPDATED);
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
            doRefreshThreadStuff();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnThread(Thread.currentThread());
        }
    }

    /**
   * Returns the children of the receiver as an
   * <code>Enumeration</code>.
   * Since a <code>WebPage</code> never has children the
   * <code>Enumeration</code> is always empty.
   */
    public Enumeration children() {
        return new Vector().elements();
    }

    /**
   * Returns false if the receiver allows children.
   * Since a <code>WebPage</code> never has children this method
   * returns always <code>FALSE</code>.
   */
    public boolean getAllowsChildren() {
        return false;
    }

    /**
   * Returns the child <code>TreeNode</code> at index
   * <code>childIndex</code>.
   * @throws ArrayIndexOutOfBoundsException since a
   * <code>WebPage</code> has no child.
   */
    public TreeNode getChildAt(int childIndex) throws ArrayIndexOutOfBoundsException {
        throw new ArrayIndexOutOfBoundsException();
    }

    /**
   * Returns the number of children <code>TreeNodes</code> the receiver
   * contains.
   * Since a <code>WebPage</code> has no child this method returns
   * always <code>0</code>;
   */
    public int getChildCount() {
        return 0;
    }

    /**
   * Returns the index of <code>node</code> in the receivers children.
   * Since a <code>WebPage</code> would never contain a
   * <code>node</code>, always <code>-1</code> will be returned.
   */
    public int getIndex(TreeNode node) {
        return -1;
    }

    /**
   * Returns the parent <code>TreeNode</code> of the receiver.
   */
    public TreeNode getParent() {
        return Parent;
    }

    /**
   * Returns true if the receiver is a leaf.
   * Since a <code>WebPage</code> is a leaf this method returns
   * always <code>TRUE</code>.
   */
    public boolean isLeaf() {
        return true;
    }

    /**
   * Returns the Title of the instance.
   */
    public String toString() {
        return Title;
    }

    /**
   * Sets the parent of the receiver to <code>newParent</code>.
   */
    public void setParent(MutableTreeNode newParent) {
        Parent = newParent;
    }

    /**
   * Removes the receiver from its parent.
   * If there is no parent (is a root) nothing happens.
   */
    public void removeFromParent() {
        if (Parent != null) {
            Parent.remove(this);
        }
    }

    /**
   * Resets the user object of the receiver to <code>object</code>.
   * It sets the title of the Page to <code>object.toString()</code>.
   * If <code>object</code> is <code>null</code> the Title remains
   * unchanged.
   */
    public void setUserObject(Object object) {
        if (object != null) Title = object.toString();
    }

    /**
   * Removes the child <code>node</code> from the receiver.
   * Since a <code>WebPage</code> wouldn't have a child nothing happens.
   */
    public void remove(MutableTreeNode node) {
    }

    /**
   * Removes the child at <code>index</code> from the receiver.
   * @throws ArrayIndexOutOfBoundsException since a
   * <code>WebPage</code> wouldn't have a child.
   */
    public void remove(int index) {
    }

    /**
   * Adds <code>child</code> to the receiver at <code>index</code>.
   * @throws ArrayIndexOutOfBoundsException since a
   * <code>WebPage</code> can't have a child.
   */
    public void insert(MutableTreeNode child, int index) throws ArrayIndexOutOfBoundsException {
    }
}
