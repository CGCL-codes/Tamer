package com.sun.corba.se.impl.protocol.giopmsgheaders;

import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.CompletionStatus;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.encoding.CDRInputStream_1_2;
import com.sun.corba.se.impl.encoding.CDROutputStream_1_2;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;

/**
 * This implements the GIOP 1.2 Reply header.
 *
 * @author Ram Jeyaraman 05/14/2000
 * @version 1.0
 */
public final class ReplyMessage_1_2 extends Message_1_2 implements ReplyMessage {

    private ORB orb = null;

    private ORBUtilSystemException wrapper = null;

    private int reply_status = (int) 0;

    private ServiceContexts service_contexts = null;

    private IOR ior = null;

    private String exClassName = null;

    private int minorCode = (int) 0;

    private CompletionStatus completionStatus = null;

    private short addrDisposition = KeyAddr.value;

    ReplyMessage_1_2(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    ReplyMessage_1_2(ORB orb, int _request_id, int _reply_status, ServiceContexts _service_contexts, IOR _ior) {
        super(Message.GIOPBigMagic, GIOPVersion.V1_2, FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPReply, 0);
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        request_id = _request_id;
        reply_status = _reply_status;
        service_contexts = _service_contexts;
        ior = _ior;
    }

    public int getRequestId() {
        return this.request_id;
    }

    public int getReplyStatus() {
        return this.reply_status;
    }

    public short getAddrDisposition() {
        return this.addrDisposition;
    }

    public ServiceContexts getServiceContexts() {
        return this.service_contexts;
    }

    public void setServiceContexts(ServiceContexts sc) {
        this.service_contexts = sc;
    }

    public SystemException getSystemException(String message) {
        return MessageBase.getSystemException(exClassName, minorCode, completionStatus, message, wrapper);
    }

    public IOR getIOR() {
        return this.ior;
    }

    public void setIOR(IOR ior) {
        this.ior = ior;
    }

    public void read(org.omg.CORBA.portable.InputStream istream) {
        super.read(istream);
        this.request_id = istream.read_ulong();
        this.reply_status = istream.read_long();
        isValidReplyStatus(this.reply_status);
        this.service_contexts = new ServiceContexts((org.omg.CORBA_2_3.portable.InputStream) istream);
        ((CDRInputStream) istream).setHeaderPadding(true);
        if (this.reply_status == SYSTEM_EXCEPTION) {
            String reposId = istream.read_string();
            this.exClassName = ORBUtility.classNameOf(reposId);
            this.minorCode = istream.read_long();
            int status = istream.read_long();
            switch(status) {
                case CompletionStatus._COMPLETED_YES:
                    this.completionStatus = CompletionStatus.COMPLETED_YES;
                    break;
                case CompletionStatus._COMPLETED_NO:
                    this.completionStatus = CompletionStatus.COMPLETED_NO;
                    break;
                case CompletionStatus._COMPLETED_MAYBE:
                    this.completionStatus = CompletionStatus.COMPLETED_MAYBE;
                    break;
                default:
                    throw wrapper.badCompletionStatusInReply(CompletionStatus.COMPLETED_MAYBE, new Integer(status));
            }
        } else if (this.reply_status == USER_EXCEPTION) {
        } else if ((this.reply_status == LOCATION_FORWARD) || (this.reply_status == LOCATION_FORWARD_PERM)) {
            CDRInputStream cdr = (CDRInputStream) istream;
            this.ior = IORFactories.makeIOR(cdr);
        } else if (this.reply_status == NEEDS_ADDRESSING_MODE) {
            this.addrDisposition = AddressingDispositionHelper.read(istream);
        }
    }

    public void write(org.omg.CORBA.portable.OutputStream ostream) {
        super.write(ostream);
        ostream.write_ulong(this.request_id);
        ostream.write_long(this.reply_status);
        if (this.service_contexts != null) {
            service_contexts.write((org.omg.CORBA_2_3.portable.OutputStream) ostream, GIOPVersion.V1_2);
        } else {
            ServiceContexts.writeNullServiceContext((org.omg.CORBA_2_3.portable.OutputStream) ostream);
        }
        ((CDROutputStream) ostream).setHeaderPadding(true);
    }

    public static void isValidReplyStatus(int replyStatus) {
        switch(replyStatus) {
            case NO_EXCEPTION:
            case USER_EXCEPTION:
            case SYSTEM_EXCEPTION:
            case LOCATION_FORWARD:
            case LOCATION_FORWARD_PERM:
            case NEEDS_ADDRESSING_MODE:
                break;
            default:
                ORBUtilSystemException localWrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PROTOCOL);
                throw localWrapper.illegalReplyStatus(CompletionStatus.COMPLETED_MAYBE);
        }
    }

    public void callback(MessageHandler handler) throws java.io.IOException {
        handler.handleInput(this);
    }
}
