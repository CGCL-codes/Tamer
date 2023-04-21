package gnu.hylafax;

import gnu.inet.ftp.ServerResponseException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Interface for the HylaFAX client protocol.
 * 
 * @version $Revision: 162 $
 * @author Steven Jardine <steve@mjnservices.com>
 */
public interface ClientProtocol {

    /**
     * Notify on all job state changes. Used with the JPARM NOTIFY command.
     */
    public static final String NOTIFY_ALL = "DONE+REQUEUE";

    /**
     * Notify when the job is done. Used with the JPARM NOTIFY command.
     */
    public static final String NOTIFY_DONE = "DONE";

    /**
     * Do not notify when the job is done or requeued. Used with the JPARM
     * NOTIFY command.
     */
    public static final String NOTIFY_NONE = "NONE";

    /**
     * Notify when the job is requeued. Used with the JPARM NOTIFY command.
     */
    public static final String NOTIFY_REQUEUE = "REQUEUE";

    /**
     * use the GMT timezone for date fields.
     */
    public static final String TZONE_GMT = "GMT";

    /**
     * use the local timezone for date fields.
     */
    public static final String TZONE_LOCAL = "LOCAL";

    /**
     * establish administrator privileges given password
     * 
     * @param password
     *            administrator password
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void admin(String password) throws IOException, ServerResponseException;

    /**
     * Asks the HylaFAX server to answer the call to the specified modem. <br>
     * Works only with established administrator priviledges.
     * 
     * @param modem
     * @throws IOException
     * @throws ServerResponseException
     */
    public void answer(String modem) throws IOException, ServerResponseException;

    /**
     * get site config parameters of the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void config(String parm, int value) throws IOException, ServerResponseException;

    /**
     * get site config parameters of the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void config(String parm, long value) throws IOException, ServerResponseException;

    /**
     * get site config parameters of the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter as an Object
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void config(String parm, Object value) throws IOException, ServerResponseException;

    /**
     * get site config parameters of the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void config(String parm, String value) throws IOException, ServerResponseException;

    /**
     * get the FILEFMT string value. The FILEFMT string specifies how file
     * status information is formatted when returned by the LIST and STAT
     * commands. Refer to the HylaFAX man pages, hfaxd(8c), for information on
     * the formatting codes that can be used in this string.
     * 
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     * @return the current FILEFMT value
     */
    public String filefmt() throws IOException, ServerResponseException;

    /**
     * set the FILEFMT string value. the FILEFMT string specifies how file
     * status information is returned when the LIST and STAT commands are used.
     * Refer to the HylaFAX man pages, hfaxd(8c), for the formatting codes.
     * 
     * @param value
     *            the new value of the FILEFMT string
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void filefmt(String value) throws IOException, ServerResponseException;

    /**
     * specify data transfer format
     * 
     * @param value
     *            the data transfer format.
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server responded with an error code
     */
    public void form(String value) throws IOException, ServerResponseException;

    /**
     * get the current idle timeout in seconds
     * 
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     * @return server's idle timeout in seconds
     */
    public long idle() throws IOException, ServerResponseException;

    /**
     * set the idle timeout value to the given number of seconds
     * 
     * @param timeout
     *            new timeout value in seconds (MAX = 7200)
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void idle(long timeout) throws IOException, ServerResponseException;

    /**
     * delete the given job this can be called on a suspended or done job.
     * 
     * @param jobid
     *            id of the job to delete
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                server replied with error code
     */
    public void jdele(long jobid) throws IOException, ServerResponseException;

    /**
     * interrupt the given job id
     * 
     * @param jobid
     *            id of the job to interrupt
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void jintr(long jobid) throws IOException, ServerResponseException;

    /**
     * kill the job with the given job id
     * 
     * @param jobid
     *            the id of the job to kill
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void jkill(long jobid) throws IOException, ServerResponseException;

    /**
     * create a new job. get the new job id using the job() method. The new job
     * is the current job.
     * 
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void jnew() throws IOException, ServerResponseException;

    /**
     * create a new job. get the new job id using the job() method. The new job
     * is the current job.
     * 
     * @param useDefaultJob
     *            if true the default job is selected prior to the jnew call. If
     *            false the job inherits parameters from the last job selected.
     *            This functionality may change if the default parameters from
     *            the hfaxd server is changed.
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void jnew(boolean useDefaultJob) throws IOException, ServerResponseException;

    /**
     * get the current job id 0 indicates the current job id is "default" value
     * 
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public long job() throws IOException, ServerResponseException;

    /**
     * set the current job id
     * 
     * @param value
     *            new current job id
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void job(long value) throws IOException, ServerResponseException;

    /**
     * get the job format string. read the HylaFAX man pages, hfaxd(8c), for
     * format codes.
     * 
     * @exception IOException
     *                a socket IO error occurred.
     * @exception ServerResponseException
     *                the server responded with an error code
     */
    public String jobfmt() throws IOException, ServerResponseException;

    /**
     * set the job format string. read the HylaFAX man pages, hfaxd(8c), for
     * format codes.
     * 
     * @param value
     *            new job format string
     * @exception IOException
     *                a socket IO error occurred.
     * @exception ServerResponseException
     *                the server responded with an error
     */
    public void jobfmt(String value) throws IOException, ServerResponseException;

    /**
     * get job parameters of the current job
     * 
     * @param parm
     *            the name of the job parameter to change
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     * @return value of the named job parameter
     */
    public String jparm(String parm) throws IOException, ServerResponseException;

    /**
     * set job parameters on the current job
     * 
     * @param parm
     *            the name of the job parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void jparm(String parm, int value) throws IOException, ServerResponseException;

    /**
     * set job parameters on the current job
     * 
     * @param parm
     *            the name of the job parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void jparm(String parm, long value) throws IOException, ServerResponseException;

    /**
     * set job parameters on the current job
     * 
     * @param parm
     *            the name of the job parameter to change
     * @param value
     *            the value of the given parameter as an Object
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void jparm(String parm, Object value) throws IOException, ServerResponseException;

    /**
     * set job parameters on the current job
     * 
     * @param parm
     *            the name of the job parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void jparm(String parm, String value) throws IOException, ServerResponseException;

    /**
     * reset the state of the current job. get/set the current job id via the
     * 'job' method
     * 
     * @exception IOException
     *                an IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void jrest() throws IOException, ServerResponseException;

    /**
     * submit the current job to the scheduler
     * 
     * @return the job id
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public long jsubm() throws IOException, ServerResponseException;

    /**
     * submit the given job to the scheduler
     * 
     * @param jobid
     *            the id of the job to submit
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     * @return the submitted job id, should match jobid passed in
     */
    public int jsubm(long jobid) throws IOException, ServerResponseException;

    /**
     * Suspend the job with the given job id.
     * 
     * @param jobid
     *            id of the job to suspend
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void jsusp(long jobid) throws IOException, ServerResponseException;

    /**
     * Wait for the job with the given job id to complete.
     * 
     * @param jobid
     *            id of the job to wait for
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void jwait(long jobid) throws IOException, ServerResponseException;

    /**
     * get the modem format string value. the modem format string specifies how
     * modem status information should be displayed. refer to the HylaFAX man
     * pages, hfaxd(8c), for the format string codes.
     * 
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server responded with an error code
     */
    public String mdmfmt() throws IOException, ServerResponseException;

    /**
     * set the modem format string. the modem format string is used to format
     * the modem status information. Refer to the HylaFAX man pages, hfaxd(8c),
     * for formatting codes.
     * 
     * @param value
     *            the new modem format string to use
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server responded with an error code
     */
    public void mdmfmt(String value) throws IOException, ServerResponseException;

    /**
     * perform server No Operation could be used as a keep-alive
     * 
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void noop() throws IOException, ServerResponseException;

    /**
     * open a connection to the localhost on the default port
     * 
     * @exception UnknownHostException
     *                cannot resolve the given hostname
     * @exception IOException
     *                IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void open() throws UnknownHostException, IOException, ServerResponseException;

    /**
     * open a connection to a given server at default port this is an alias for
     * connect()
     * 
     * @param host
     *            the hostname of the HylaFAX server
     * @exception UnknownHostException
     *                cannot resolve the given hostname
     * @exception IOException
     *                IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     */
    public void open(String host) throws UnknownHostException, IOException, ServerResponseException;

    /**
     * send the password for this username and session
     * 
     * @param password
     *            the password to login with
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void pass(String password) throws IOException, ServerResponseException;

    /**
     * end session
     * 
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void quit() throws IOException, ServerResponseException;

    /**
     * get the received file output format string. The rcvfmt string specifies
     * how received faxes (files in the rcvq directory) are displayed. Refer to
     * the HylaFAX man pages, hfaxd(8c), for the format string codes.
     * 
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server responded with an error code
     */
    public String rcvfmt() throws IOException, ServerResponseException;

    /**
     * set the receive file output format string. The rcvfmt string specifies
     * how received faxes (files in the rcvq directory) are displayed. refer to
     * the HylaFAX man pages, hfaxd(8c), for the format string codes.
     * 
     * @param value
     *            the new format string
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server responded with an error code
     */
    public void rcvfmt(String value) throws IOException, ServerResponseException;

    /**
     * set site parameters on the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void site(String parm, int value) throws IOException, ServerResponseException;

    /**
     * set site parameters on the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void site(String parm, long value) throws IOException, ServerResponseException;

    /**
     * set site parameters on the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter as an Object
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void site(String parm, Object value) throws IOException, ServerResponseException;

    /**
     * set site parameters on the current client.
     * 
     * @param parm
     *            the name of the site parameter to change
     * @param value
     *            the value of the given parameter
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void site(String parm, String value) throws IOException, ServerResponseException;

    /**
     * Returns the size (in bytes) of the given regular file. This is the size
     * on the server and may not accurately represent the file size once the
     * file has been transferred (particularly via ASCII mode)
     * 
     * @param pathname
     *            the name of the file to get the size for
     * @exception IOException
     *                caused by a socket IO error
     * @exception ServerResponseException
     *                caused by a server response indicating an error
     * @exception FileNotFoundException
     *                the given path does not exist
     */
    public long size(String pathname) throws IOException, FileNotFoundException, ServerResponseException;

    /**
     * store temp file, the file is stored in a uniquely named file on the
     * server. The remote temp file is deleted when the connection is closed.
     * 
     * @exception IOException
     *                io error occurred talking to the server
     * @exception ServerResponseException
     *                server replied with error code
     * @return the filename of the temp file
     */
    public String stot(InputStream data) throws IOException, ServerResponseException;

    /**
     * set the timezone display format valid tzone values are TZONE_GMT and
     * TZONE_LOCAL
     * 
     * @param value
     *            new timezone display setting
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     */
    public void tzone(String value) throws IOException, ServerResponseException;

    /**
     * send the user name for this session
     * 
     * @param username
     *            name of the user to login as
     * @exception IOException
     *                io error occurred
     * @exception ServerResponseException
     *                server replied with an error code
     * @return true if a password is required, false if no password is required
     */
    public boolean user(String username) throws IOException, ServerResponseException;

    /**
     * verify dialstring handling and/or least-cost routing.
     * 
     * @param dialstring
     *            the dialstring to verify
     * @exception IOException
     *                a socket IO error occurred
     * @exception ServerResponseException
     *                the server replied with an error code
     * @return the InetAddress of the server that will handle the given
     *         dialstring
     */
    public InetAddress vrfy(String dialstring) throws IOException, ServerResponseException;
}
