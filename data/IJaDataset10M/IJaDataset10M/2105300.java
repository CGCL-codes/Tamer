package ch.ethz.mxquery.test.dmcq.LRexperiments.bench;

import java.io.FileNotFoundException;
import java.util.Vector;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.Token;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.util.SimulationClock;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.test.dmcq.LRexperiments.lroad.*;
import ch.ethz.mxquery.model.Iterator;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.util.KXmlSerializer;

public final class DirectXMLIterator extends Iterator {

    LRoadSimpleDriver ld;

    private String fileName;

    private boolean inited = false;

    private boolean done = true;

    private boolean endOfStream = false;

    private boolean dataAsElements = false;

    private boolean first = true;

    private LRoadEvent currentEvent = null;

    private String value = null;

    private int intVal = 0;

    private int printOnce = -1;

    public DirectXMLIterator(Context ctx, QueryLocation loc, String fileName, boolean readMinute, boolean injectDummy) throws MXQueryException {
        super(ctx, loc);
        ld = new LRoadSimpleDriver(readMinute, injectDummy);
        this.fileName = fileName;
        ld.setInputFilename(fileName);
        try {
            ld.init();
        } catch (FileNotFoundException e) {
            throw new DynamicException(ErrorCodes.A0001_EC_FILE_NOT_FOUND, "File '" + fileName + "' does not exist!", loc);
        }
    }

    public DirectXMLIterator(Context ctx, QueryLocation loc, String fileName, boolean elements) throws MXQueryException {
        this(ctx, loc, fileName, true, false);
        dataAsElements = elements;
    }

    private int getCurrSecond() {
        return (int) SimulationClock.getInstance().getCurrentTime();
    }

    public TokenInterface next() throws MXQueryException {
        Token currentToken;
        for (; ; ) {
            if (done) {
                currentEvent = ld.next();
                if (currentEvent == null) break;
                int recordTime = currentEvent.getTime();
                int currSecond = getCurrSecond();
                printOnce = recordTime;
                while (recordTime > getCurrSecond()) {
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                        throw new MXQueryException(ErrorCodes.A0009_EC_EVALUATION_NOT_POSSIBLE, "Data delay: currentThread sleep interrupted", loc);
                    }
                }
                currSecond = getCurrSecond();
                done = false;
            }
            if (dataAsElements) currentToken = currentEvent.next(); else currentToken = currentEvent.nextA();
            if (currentToken.getEventType() != Type.END_SEQUENCE) {
                return currentToken;
            } else {
                done = true;
            }
        }
        currentToken = (Token) Token.END_SEQUENCE_TOKEN;
        value = null;
        called++;
        return currentToken;
    }

    public boolean isAttribute() {
        return true;
    }

    protected KXmlSerializer createIteratorStartTag(KXmlSerializer serializer) throws Exception {
        super.createIteratorStartTag(serializer);
        serializer.attribute(null, "fileName", fileName);
        return serializer;
    }

    protected XDMIterator copy(Context context, XDMIterator[] subIters, Vector nestedPredCtxStack) throws MXQueryException {
        return null;
    }
}
