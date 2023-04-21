package org.dcm4cheri.net;

import org.dcm4che.net.AAssociateRQ;
import org.dcm4che.net.CommonExtNegotiation;
import org.dcm4che.net.PresContext;
import org.dcm4che.net.PDUException;
import org.dcm4che.net.UserIdentityRQ;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0.0
 */
final class AAssociateRQImpl extends AAssociateRQACImpl implements AAssociateRQ {

    static AAssociateRQImpl parse(UnparsedPDUImpl raw) throws PDUException {
        return (AAssociateRQImpl) new AAssociateRQImpl().init(raw);
    }

    AAssociateRQImpl() {
    }

    public final CommonExtNegotiation removeCommonExtNegotiation(String uid) {
        return (CommonExtNegotiation) commonExtNegs.remove(uid);
    }

    public final CommonExtNegotiation getCommonExtNegotiation(String uid) {
        return (CommonExtNegotiation) commonExtNegs.get(uid);
    }

    public Collection listCommonExtNegotiations() {
        return commonExtNegs.values();
    }

    public void clearCommonExtNegotiations() {
        commonExtNegs.clear();
    }

    public final UserIdentityRQ getUserIdentity() {
        return userIdentityRQ;
    }

    public final void setUserIdentity(UserIdentityRQ userIdentity) {
        this.userIdentityRQ = userIdentity;
    }

    protected int type() {
        return 1;
    }

    protected int pctype() {
        return 0x20;
    }

    protected int useridtype() {
        return 0x58;
    }

    protected String typeAsString() {
        return "AAssociateRQ";
    }

    protected void append(PresContext pc, StringBuffer sb) {
        sb.append("\n\tpc-").append(pc.pcid()).append(":\tas=").append(DICT.lookup(pc.getAbstractSyntaxUID()));
        for (Iterator it = pc.getTransferSyntaxUIDs().iterator(); it.hasNext(); ) sb.append("\n\t\tts=").append(DICT.lookup((String) it.next()));
    }

    protected void appendPresCtxSummary(StringBuffer sb) {
        sb.append("\n\tpresCtx:\toffered=").append(presCtxs.size());
    }
}
