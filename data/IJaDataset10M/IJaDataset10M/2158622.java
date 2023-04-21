package DE.FhG.IGD.semoa.comm;

import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.net.*;
import codec.asn1.*;
import java.io.*;
import java.util.*;

/**
 * A service reply for the <i>Portal Daemon</i> (Pod). Its
 * ASN.1 definition is:
 * <blockquote><pre>
 *
 * Payload ::= SEQUENCE {
 *   to     IA5STRING,
 *   from   IA5STRING,
 *   status INTEGER
 * }
 * </pre></blockquote>
 *
 * @author Volker Roth
 * @version "$Id: PodReply.java 827 2003-01-22 05:29:27Z jpeters $"
 */
public class PodReply extends ASN1Sequence implements Reply {

    /**
     * The delay between two retries.
     */
    public static final long RETRY_DELAY = 1000L;

    /**
     * The receiver (to) address.
     */
    protected ASN1IA5String to_;

    /**
     * The sender (from) address.
     */
    protected ASN1IA5String from_;

    /**
     * The request status.
     */
    protected ASN1Integer status_;

    /**
     * The maximum size of requests.
     */
    protected int max_;

    /**
     * The &quot;keep alive&quot; flag.
     */
    protected boolean keep_ = false;

    /**
     * Creates an instance ready for decoding.
     */
    public PodReply() {
        super(3);
        to_ = new ASN1IA5String();
        from_ = new ASN1IA5String();
        status_ = new ASN1Integer(ErrorCode.OK.intValue());
        add(to_);
        add(from_);
        add(status_);
    }

    /**
     *
     * @param to The name of the receiver.
     * @param from The name of the sender.
     * @param status The request status.
     */
    public PodReply(String to, String from, ErrorCode status) {
        super(3);
        if (to == null || from == null || status == null) {
            throw new NullPointerException("to or from or status");
        }
        to_ = new ASN1IA5String(to);
        from_ = new ASN1IA5String(from);
        status_ = new ASN1Integer(status.intValue());
        add(to_);
        add(from_);
        add(status_);
    }

    public void setMaximumSize(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("argument is negative");
        }
        max_ = n;
    }

    public boolean keepAlive() {
        return keep_;
    }

    public Request handleReply(Request request) throws IOException {
        PodRequest req;
        ErrorCode status;
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (!(request instanceof PodRequest)) {
            throw new IllegalArgumentException("not a pod request");
        }
        req = (PodRequest) request;
        status = status();
        if (status == ErrorCode.OK) {
            keep_ = false;
            return null;
        }
        if (status == ErrorCode.RECIPIENT_CONGESTED || status == ErrorCode.RECIPIENT_UNREACHABLE) {
            if (req.tryAgain()) {
                try {
                    System.out.println("sleeping...");
                    Thread.currentThread().sleep(RETRY_DELAY);
                } catch (InterruptedException e) {
                    throw new IOException("interrupted");
                }
            }
            throw new CommunicationException("not reachable", status);
        }
        if (status == ErrorCode.NO_SUCH_RECIPIENT) {
            throw new CommunicationException("unknown recipient", status);
        }
        throw new CommunicationException("It wasn't my fault", ErrorCode.UNKNOWN);
    }

    public ErrorCode status() {
        ErrorCode err;
        int status;
        status = status_.getBigInteger().intValue();
        return ErrorCode.byNumber(status);
    }

    public void write(OutputStream out) throws IOException {
        DEREncoder enc;
        try {
            enc = new DEREncoder(out);
            encode(enc);
        } catch (ASN1Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void read(InputStream in) throws IOException {
        DERDecoder dec;
        try {
            dec = new DERDecoder(in, max_);
            decode(dec);
        } catch (ASN1Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public String toString() {
        return "Sender   : " + from_.getString() + "\nReceiver : " + to_.getString() + "\nStatus   : " + status();
    }
}
