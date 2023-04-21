package ch.ethz.mxquery.test.dmcq.LRexperiments.lroad;

import ch.ethz.mxquery.datamodel.QName;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.LongAttrToken;
import ch.ethz.mxquery.datamodel.xdm.ElementToken;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.XDMScope;
import ch.ethz.mxquery.exceptions.MXQueryException;

public final class AccBalQuery implements LRoadEvent {

    int type;

    int time;

    int Vid;

    int Qid;

    private int iPos;

    private int currentIntVal;

    private String name;

    private XDMScope scope = new XDMScope();

    private static final QName repQName;

    private static final QName typeQName;

    private static final QName timeQName;

    private static final QName vidQName;

    private static final QName qidQName;

    static {
        try {
            repQName = new QName("rep");
            typeQName = new QName("Type");
            timeQName = new QName("Time");
            vidQName = new QName("VID");
            qidQName = new QName("Qid");
        } catch (MXQueryException e) {
            throw new RuntimeException("Could not instantiate");
        }
    }

    public int getInt() {
        return currentIntVal;
    }

    public String getName() {
        return name;
    }

    public Token next() {
        return (Token) Token.END_SEQUENCE_TOKEN;
    }

    public Token nextA() throws MXQueryException {
        Token token;
        switch(iPos) {
            case -1:
                iPos++;
                token = new ElementToken(Type.START_TAG, null, repQName, scope);
                return token;
            case 0:
                iPos++;
                token = new LongAttrToken(Type.INTEGER, null, 2, typeQName, scope);
                return token;
            case 1:
                iPos++;
                token = new LongAttrToken(Type.INTEGER, null, time, timeQName, scope);
                return token;
            case 2:
                iPos++;
                token = new LongAttrToken(Type.INTEGER, null, Vid, vidQName, scope);
                return token;
            case 3:
                iPos++;
                token = new LongAttrToken(Type.INTEGER, null, Qid, qidQName, scope);
                return token;
            case 4:
                iPos++;
                token = new ElementToken(Type.END_TAG, null, repQName, scope);
                return token;
            case 5:
                iPos = -1;
                return (Token) Token.END_SEQUENCE_TOKEN;
        }
        return (Token) Token.END_SEQUENCE_TOKEN;
    }

    public AccBalQuery(int time, int vid, int qid) {
        super();
        this.time = time;
        Vid = vid;
        Qid = qid;
        iPos = -1;
    }

    public int getTime() {
        return time;
    }
}
