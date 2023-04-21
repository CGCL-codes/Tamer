package ch.ethz.mxquery.smstest;

import java.io.StringReader;
import ch.ethz.mxquery.query.XQCompiler;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.query.impl.CompilerImpl;
import ch.ethz.mxquery.sms.StoreFactory;
import ch.ethz.mxquery.sms.MMimpl.StreamStoreInput;
import ch.ethz.mxquery.sms.interfaces.StreamStore;
import ch.ethz.mxquery.contextConfig.CompilerOptions;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.datamodel.QName;
import ch.ethz.mxquery.dmcq.InputStreamStore;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;
import ch.ethz.mxquery.model.Window;
import ch.ethz.mxquery.model.XDMIterator;
import ch.ethz.mxquery.xdmio.XDMInputFactory;
import ch.ethz.mxquery.xdmio.XDMSerializer;
import ch.ethz.mxquery.xdmio.XDMSerializerSettings;
import ch.ethz.mxquery.xdmio.XMLSource;

/**
 * This is a simple example of using MXQuery in a streaming mode, consisting of
 * input queue and a single continuous query reading from it The next release of
 * MXQuery will contain a stream server that will provide a more flexible and
 * general setup
 * 
 * @author Peter Fischer
 * 
 */
public class QueryThreadOutputQueue {

    String query = "declare variable $input external; forseq $x in $input tumbling window start when fn:true() end when fn:true() return $x";

    InputStreamStore iss;

    class FeedThread extends Thread {

        public void run() {
            StreamStoreInput ssi = iss.getStoreInput();
            try {
                for (int i = 0; i < 3; i++) {
                    XMLSource curItemIt = XDMInputFactory.createXMLInput(Context.getGlobalContext(), new StringReader("<seq>" + i + "</seq>"), true, Context.NO_VALIDATION, QueryLocation.OUTSIDE_QUERY_LOC);
                    System.out.println("Push " + i);
                    ssi.pushIterator(curItemIt);
                }
            } catch (MXQueryException e) {
                e.printStackTrace();
                try {
                    ssi.endStream();
                } catch (MXQueryException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        QueryThreadOutputQueue sa = new QueryThreadOutputQueue();
        sa.runPushItems();
    }

    /**
     * A example of a push stream where the caller pushes individual items into
     * the input queue This can be useful if the incoming stream consists of
     * individual items
     * 
     * @param query
     *                the query to run
     * @throws MXQueryException
     */
    private void runPushItems() throws MXQueryException {
        FeedThread feed = new FeedThread();
        feed.start();
        Context ctx = new Context();
        CompilerOptions co = new CompilerOptions();
        co.setXquery11(true);
        XQCompiler compiler = new CompilerImpl();
        PreparedStatement statement = compiler.compile(ctx, query, co, null, null);
        XDMIterator result = statement.evaluate();
        statement.addExternalResource(new QName("input"), iss.getStore().getIterator(ctx));
        StreamStore sr = result.getContext().getStores().createStreamStore(StoreFactory.LAZY_SEQ_FIFO, "output-stream");
        sr.setIterator(result);
        Window output = (Window) sr.getIterator(result.getContext());
        XDMSerializerSettings ser = new XDMSerializerSettings();
        ser.setOmitXMLDeclaration(true);
        XDMSerializer ip = new XDMSerializer(ser);
        Window curr = null;
        while (output.hasNextItem()) {
            System.out.println("Access Item");
            if (curr != null) curr.destroyWindow();
            curr = (Window) output.nextItem();
            String res = ip.eventsToXML(curr);
            System.out.print(res);
        }
    }

    public QueryThreadOutputQueue() throws MXQueryException {
        iss = new InputStreamStore(1);
    }
}
