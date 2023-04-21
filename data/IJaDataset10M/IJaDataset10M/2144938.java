package jreceiver.j2me.common.rpc.xmlrpc;

import java.util.Hashtable;
import java.util.Vector;
import jreceiver.j2me.common.rec.Rec;
import jreceiver.j2me.common.rec.RecException;
import jreceiver.j2me.common.rec.source.TuneRec;
import jreceiver.j2me.common.rec.security.User;
import jreceiver.j2me.common.rpc.RpcException;
import jreceiver.j2me.common.rpc.Tunes;
import jreceiver.j2me.common.rec.util.TuneQuery;

/**
 * Tune-related queries to a remote server via XML-RPC.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2003/05/14 06:24:00 $
 */
public class TunesImpl extends ServerTableImpl implements Tunes {

    /**
     * ctor for this implementation
     */
    public TunesImpl(String remote_host, User user) throws RpcException {
        super(HANDLER_NAME, remote_host, user);
    }

    /**
     * restore a rec object from hashtable form
     */
    public Rec reconstituteRec(Hashtable hash) throws RecException {
        return TuneRec.createInstance(hash);
    }

    /**
     * see interface for description and behavior
     */
    public Vector getKeysForFolder(int folder_id, int driver_id, String order_by, int rec_offset, int rec_count) throws RpcException {
        if (folder_id < 1 || rec_offset < 0) throw new IllegalArgumentException();
        Vector params = new Vector();
        params.addElement(new Integer(folder_id));
        params.addElement(new Integer(driver_id));
        params.addElement(order_by != null ? order_by : "");
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (Vector) execute(GET_KEYS_FOR_FOLDER, params);
    }

    /**
     * Return a count of Tune ids for a particular playlist, filtering out mime-types
     * not supported by the specified driver.
     */
    public int getKeyCountForPlaylist(int pl_src_id, int driver_id) throws RpcException {
        if (pl_src_id < 1) throw new IllegalArgumentException();
        Vector params = new Vector();
        params.addElement(new Integer(pl_src_id));
        params.addElement(new Integer(driver_id));
        Integer ii = (Integer) execute(GET_KEYCOUNT_FOR_PLAYLIST, params);
        return ii.intValue();
    }

    /**
     * see interface for description and behavior
     */
    public Vector getKeysForPlaylist(int pl_src_id, int driver_id, String order_by, int rec_offset, int rec_count) throws RpcException {
        if (pl_src_id < 1 || rec_offset < 0) throw new IllegalArgumentException();
        Vector params = new Vector();
        params.addElement(new Integer(pl_src_id));
        params.addElement(new Integer(driver_id));
        params.addElement(order_by != null ? order_by : "");
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (Vector) execute(GET_KEYS_FOR_PLAYLIST, params);
    }

    /**
     * Return a count of Tune ids for a hash query, filtering out mime-types
     * not supported by the specified driver.
     */
    public int getKeyCountForQuery(TuneQuery tune_query, int driver_id) throws RpcException {
        Vector params = new Vector();
        params.addElement(tune_query != null ? tune_query.toHash() : new Hashtable());
        params.addElement(new Integer(driver_id));
        Integer ii = (Integer) execute(GET_KEYCOUNT_FOR_QUERY, params);
        return ii.intValue();
    }

    /**
     * Return a list of Tune ids for a hash query, filtering out mime-types
     * not supported by the specified driver.
     */
    public Vector getKeysForQuery(TuneQuery tune_query, int driver_id, String order_by, int rec_offset, int rec_count) throws RpcException {
        Vector params = new Vector();
        params.addElement(tune_query != null ? tune_query.toHash() : new Hashtable());
        params.addElement(new Integer(driver_id));
        params.addElement(order_by != null ? order_by : "");
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (Vector) execute(GET_KEYS_FOR_QUERY, params);
    }

    /**
     * Obtain a BINARY list of tunes for a hash query, custom
     * formatted with a caller-specified pattern, typically
     * for use in building an internal playlist in an embedded
     * device where memory is at a premium.
     * <p>
     * Data is returned as a byte array.  All results are
     * concatenated.  Any record separators needed must be
     * specified in the pattern.
     * <p>
     * Formatting of each record is done through a MessageFormatB
     * (binary) pattern.  As an example, the following
     * <pre>
     *     String pattern = "{0,word,le}{1,word,le}{2,word,le}";
     * </pre>
     * will return each query result as
     * <pre>
     *     c001000 e5655000 b5010000
     * </pre>
     * where each result is encoded as a 12-byte record, where
     * {0,word,le} will be replaced with the source identifier
     * (src_id) as a 4-byte word in little-endian order; {1...}
     * will be replaced with the filesize and {2...} replaced with
     * the offset in the tune file of the first MPEG frame.
     * <p>
     * This method provides a wrapper for the remote call to
     * the server.
     *
     * @param tune_query <code>Hashtable</code> TODO: point to documentation
     * @param pattern    <code>byte[]</code> a MessageFormatB pattern where {0} will be replaced
     *                   with the src_id and {1} will be replaced with the filesize and {2} replaced
     *                   with the MPEG data offset.
     * @param begin      <code>int</code> the index of the first item of the results to return
     * @param end        <code>int</code> the index of the final item of the results to return
     * @return <code>byte[]</code> concatenated binary records
     * @exception RpcException
     */
    public byte[] encodeBinary(TuneQuery tune_query, int driver_id, byte[] pattern, int rec_offset, int rec_count) throws RpcException {
        Vector params = new Vector();
        params.addElement(tune_query != null ? tune_query.toHash() : new Hashtable());
        params.addElement(new Integer(driver_id));
        params.addElement(pattern);
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (byte[]) execute(ENCODE_BINARY, params);
    }

    /**
     * Obtain a TEXT list of tunes for a hash query, custom
     * formatted with a caller-specified pattern, for use in
     * displaying a menu to the user.
     * <p>
     * Data is returned as a String.  All results are
     * concatenated.  Any record separators needed must be
     * specified in the pattern.
     * <p>
     * Formatting of each record is done through a MessageFormat
     * pattern.  As an example, the following
     * <pre>
     *     String pattern = "{0,hex}=T{1}\r\n";
     * </pre>
     * will format each result matching the filter as
     * <pre>
     *     1d0=TSenses Working Overtime\r\n
     * </pre>
     * where {0,hex} will be replaced with the source identifier
     * (src_id), in hexadecimal; {1} will be replaced with the
     * tune title.
     * <p>
     * Note that the 'hex' element format isn't a standard
     * formatter of MessageFormat, but it is supported here in a
     * minor kludge.
     * <p>
     * This method provides a wrapper for the remote call to
     * the server.
     *
     * @param tune_query <code>Hashtable</code> TODO: point to documentation
     * @param pattern    <code>String</code> a MessageFormat pattern where {0} will be replaced
     *                   with the src_id and {1} will be replaced with the tune title.
     * @param begin      <code>int</code> the index of the first item of the results to return
     * @param end        <code>int</code> the index of the final item of the results to return
     * @return <code>String</code> concatenated String records.
     * @exception RpcException
     */
    public String encodeText(TuneQuery tune_query, int driver_id, String pattern, int rec_offset, int rec_count) throws RpcException {
        Vector params = new Vector();
        params.addElement(tune_query != null ? tune_query.toHash() : new Hashtable());
        params.addElement(new Integer(driver_id));
        params.addElement(pattern != null ? pattern : "");
        params.addElement(new Integer(rec_offset));
        params.addElement(new Integer(rec_count));
        return (String) execute(ENCODE_TEXT, params);
    }
}
